// -- Settings keys & defaults
const SETTINGS_KEY = 'subscribedToolbar.settings';
const DEFAULT_FEED_URL = chrome.runtime.getURL('dev-feed/bookmarks.json');
const DEFAULT_SETTINGS = {
  feedUrl: DEFAULT_FEED_URL,
  folderName: 'Subscribed Toolbar',
  intervalMinutes: 15,
  destructiveSync: false // false: add-only, true: replace folder contents each sync
};
const ALARM_NAME = 'subscribed-toolbar-sync';

// ---- Utilities
async function getSettings() {
  const { [SETTINGS_KEY]: s } = await chrome.storage.sync.get(SETTINGS_KEY);
  return { ...DEFAULT_SETTINGS, ...(s || {}) };
}

async function saveSettings(next) {
  await chrome.storage.sync.set({ [SETTINGS_KEY]: next });
  await ensureAlarm(next.intervalMinutes);
}

async function ensureAlarm(intervalMinutes) {
  await chrome.alarms.clear(ALARM_NAME);
  const min = Math.max(1, Number(intervalMinutes) || DEFAULT_SETTINGS.intervalMinutes);
  await chrome.alarms.create(ALARM_NAME, { periodInMinutes: min });
}

async function getToolbarFolderId() {
  // "1" is typically the toolbar, but let’s find it safely.
  const tree = await chrome.bookmarks.getTree();
  const root = tree[0];
  // Try well-known id "1" first:
  const byId = findNodeById(root, '1');
  if (byId) return '1';
  // Fallback: find by common titles (localized titles vary; this is best-effort)
  const candidates = (root.children || []).filter(n =>
    /bookmarks\s*bar|bookmark\s*bar/i.test(n.title)
  );
  if (candidates[0]) return candidates[0].id;
  // Last resort: use first child folder under root
  return (root.children && root.children[0] && root.children[0].id) || root.id;
}

function findNodeById(node, id) {
  if (node.id === id) return node;
  for (const c of (node.children || [])) {
    const found = findNodeById(c, id);
    if (found) return found;
  }
  return null;
}

async function ensureFolder(parentId, name) {
  const children = await chrome.bookmarks.getChildren(parentId);
  const existing = children.find(c => !c.url && c.title === name);
  if (existing) return existing.id;
  const created = await chrome.bookmarks.create({ parentId, title: name });
  return created.id;
}

// ---- Feed fetch & validation
async function fetchFeed(url) {
  const res = await fetch(url, { cache: 'no-store' });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  const json = await res.json();
  validateFeed(json);
  return json;
}

function validateFeed(feed) {
  if (!feed || typeof feed !== 'object') throw new Error('Invalid feed: not an object');
  if (!Array.isArray(feed.children)) throw new Error('Invalid feed: children[] required');
  // best-effort structural validation
  const validateNode = (n, path = 'root') => {
    if (!n || typeof n !== 'object') throw new Error(`Invalid node at ${path}`);
    if (typeof n.title !== 'string' || !n.title.trim()) {
      throw new Error(`Missing/empty title at ${path}`);
    }
    if ('children' in n && !Array.isArray(n.children)) {
      throw new Error(`children must be an array at ${path}`);
    }
    const hasChildren = Array.isArray(n.children);
    const nodeUrl = getNodeUrl(n);
    const isLink = !hasChildren && !!nodeUrl;
    if (!isLink && !hasChildren) {
      throw new Error(`Node must include children[] or value/url at ${path}`);
    }
    if (isLink) {
      if (!/^https?:\/\//i.test(nodeUrl)) {
        throw new Error(`Bad url at ${path}`);
      }
    }
    if (hasChildren) {
      n.children.forEach((c, i) => validateNode(c, `${path}/${n.title}[${i}]`));
    }
  };
  feed.children.forEach((c, i) => validateNode(c, `root[${i}]`));
}

// ---- Sync logic
async function syncNow() {
  const settings = await getSettings();
  const toolbarId = await getToolbarFolderId();
  const targetFolderId = await ensureFolder(toolbarId, settings.folderName);

  const feed = await fetchFeed(settings.feedUrl);

  if (settings.destructiveSync) {
    await clearFolder(targetFolderId);
    await createFromModel(targetFolderId, feed.children);
  } else {
    await reconcile(targetFolderId, feed.children);
  }
}

async function clearFolder(folderId) {
  const children = await chrome.bookmarks.getChildren(folderId);
  // Delete in sequence to avoid rate limits
  for (const c of children) {
    try {
      if (c.url) {
        await chrome.bookmarks.remove(c.id);
      } else {
        await chrome.bookmarks.removeTree(c.id);
      }
      await sleep(15);
    } catch (e) {
      console.warn('Delete failed:', c, e);
    }
  }
}

async function createFromModel(parentId, nodes) {
  for (const node of nodes) {
    const nodeUrl = getNodeUrl(node);
    if (nodeUrl) {
      await chrome.bookmarks.create({ parentId, title: node.title, url: nodeUrl });
      await sleep(10);
    } else {
      const folder = await chrome.bookmarks.create({ parentId, title: node.title });
      await sleep(10);
      await createFromModel(folder.id, node.children);
    }
  }
}

// Lightweight reconciliation (keeps IDs when possible; fixes order)
async function reconcile(parentId, desiredNodes) {
  const current = await chrome.bookmarks.getChildren(parentId);

  // Delete anything not desired (by title+url or title+folder)
  const desiredKeys = new Set(desiredNodes.map(keyOfNode));
  for (const c of current) {
    const k = keyOfChromeNode(c);
    if (!desiredKeys.has(k)) {
      if (c.url) await chrome.bookmarks.remove(c.id);
      else await chrome.bookmarks.removeTree(c.id);
      await sleep(10);
    }
  }

  // Ensure all desired exist in correct order
  let index = 0;
  for (const dn of desiredNodes) {
    const key = keyOfNode(dn);
    let existing = (await chrome.bookmarks.getChildren(parentId)).find(c => keyOfChromeNode(c) === key);

    if (!existing) {
      const nodeUrl = getNodeUrl(dn);
      if (nodeUrl) {
        existing = await chrome.bookmarks.create({ parentId, title: dn.title, url: nodeUrl, index });
        await sleep(10);
      } else {
        existing = await chrome.bookmarks.create({ parentId, title: dn.title, index });
        await sleep(10);
        await reconcile(existing.id, dn.children);
      }
    } else {
      // Move to correct index if needed; update title/URL if changed
      if (existing.index !== index) {
        await chrome.bookmarks.move(existing.id, { parentId, index });
        await sleep(5);
      }
      const nodeUrl = getNodeUrl(dn);
      if (nodeUrl && existing.url !== nodeUrl) {
        await chrome.bookmarks.update(existing.id, { title: dn.title, url: nodeUrl });
      } else if (!nodeUrl && existing.title !== dn.title) {
        await chrome.bookmarks.update(existing.id, { title: dn.title });
      }
      if (!nodeUrl) await reconcile(existing.id, dn.children);
    }
    index++;
  }
}

function keyOfNode(n) {
  const nodeUrl = getNodeUrl(n);
  return nodeUrl ? `L|${n.title}|${nodeUrl}` : `F|${n.title}`;
}
function keyOfChromeNode(n) {
  return n.url ? `L|${n.title}|${n.url}` : `F|${n.title}`;
}
function getNodeUrl(node) {
  if (!node || typeof node !== 'object') return null;
  if (Array.isArray(node.children)) return null; // folders take precedence even if a value/url exists
  const raw = typeof node.url === 'string' && node.url.trim()
    ? node.url.trim()
    : typeof node.value === 'string'
      ? node.value.trim()
      : '';
  return raw || null;
}
const sleep = (ms) => new Promise(r => setTimeout(r, ms));

// ---- Lifecycle & events
chrome.runtime.onInstalled.addListener(async () => {
  // Initialize settings/alarms and run a first sync
  const s = await getSettings();
  await saveSettings(s);
  try { await syncNow(); } catch (e) { console.warn('Initial sync failed:', e.message); }
});

chrome.alarms.onAlarm.addListener(async (a) => {
  if (a.name === ALARM_NAME) {
    try { await syncNow(); } catch (e) { console.warn('Sync error:', e.message); }
  }
});

// Allow manual sync from options page
chrome.runtime.onMessage.addListener((msg, _sender, sendResponse) => {
  (async () => {
    if (msg?.type === 'SYNC_NOW') {
      try { await syncNow(); sendResponse({ ok: true }); }
      catch (e) { sendResponse({ ok: false, error: e.message }); }
    }
  })();
  return true; // keep channel open for async sendResponse
});

// Expose settings save for options page
chrome.runtime.onMessage.addListener((msg, _sender, sendResponse) => {
  (async () => {
    if (msg?.type === 'SAVE_SETTINGS') {
      const merged = { ...(await getSettings()), ...(msg.payload || {}) };
      await saveSettings(merged);
      sendResponse({ ok: true });
    }
  })();
  return true;
});
