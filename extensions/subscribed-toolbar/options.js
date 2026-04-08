const SETTINGS_KEY = 'subscribedToolbar.settings';
const DEFAULT_FEED_URL = chrome.runtime.getURL('dev-feed/bookmarks.json');
const DEFAULTS = {
  feedUrl: DEFAULT_FEED_URL,
  folderName: 'Subscribed Toolbar',
  intervalMinutes: 15,
  destructiveSync: false
};

document.getElementById('feedUrl').placeholder = DEFAULT_FEED_URL;

async function load() {
  const { [SETTINGS_KEY]: s } = await chrome.storage.sync.get(SETTINGS_KEY);
  const cfg = { ...DEFAULTS, ...(s || {}) };
  document.getElementById('feedUrl').value = cfg.feedUrl;
  document.getElementById('folderName').value = cfg.folderName;
  document.getElementById('intervalMinutes').value = cfg.intervalMinutes;
  document.querySelector('input[name="syncMode"][value="add_only"]').checked = !cfg.destructiveSync;
  document.querySelector('input[name="syncMode"][value="full_sync"]').checked = !!cfg.destructiveSync;
}

/** Same registrable host often appears as apex and www; Jsonmaker pages may use either. */
function feedOriginPatterns(parsed) {
  const isHttp = parsed.protocol === 'http:';
  const isHttps = parsed.protocol === 'https:';
  const isLocalHost = ['localhost', '127.0.0.1', '[::1]'].includes(parsed.hostname);
  if (!isHttp && !isHttps) {
    return [];
  }
  if (isHttp && !isLocalHost) {
    return [];
  }
  const { protocol, hostname, port } = parsed;
  const hostPort = port ? `${hostname}:${port}` : hostname;
  if (/^\d+\.\d+\.\d+\.\d+$/.test(hostname) || hostname.includes(':')) {
    return [`${protocol}//${hostPort}/*`];
  }
  const patterns = new Set([`${protocol}//${hostPort}/*`]);
  if (!isLocalHost) {
    const portSuffix = port ? `:${port}` : '';
    if (hostname.startsWith('www.') && hostname.length > 4) {
      const apex = hostname.slice(4);
      if (apex) {
        patterns.add(`${protocol}//${apex}${portSuffix}/*`);
      }
    } else if (!hostname.startsWith('www.')) {
      patterns.add(`${protocol}//www.${hostname}${portSuffix}/*`);
    }
  }
  return [...patterns];
}

async function ensureFeedPermission(feedUrl) {
  let parsed;
  try {
    parsed = new URL(feedUrl);
  } catch (_err) {
    throw new Error('Feed URL must be a valid http(s) address.');
  }
  const isHttp = parsed.protocol === 'http:';
  const isHttps = parsed.protocol === 'https:';
  const isLocalHost = ['localhost', '127.0.0.1', '[::1]'].includes(parsed.hostname);
  if (parsed.protocol === 'chrome-extension:') {
    if (parsed.hostname !== chrome.runtime.id) {
      throw new Error('Feed URL must belong to this extension when using chrome-extension://');
    }
    return; // bundled feeds require no extra permissions
  }
  if (!isHttp && !isHttps) {
    throw new Error('Feed URL must start with http:// or https://');
  }
  if (isHttp && !isLocalHost) {
    throw new Error('Non-local feeds must use https://');
  }
  const originPatterns = feedOriginPatterns(parsed);
  if (originPatterns.length === 0) {
    return;
  }
  const contains = await Promise.all(
    originPatterns.map((pattern) =>
      chrome.permissions.contains({ origins: [pattern] }).then((ok) => ({ pattern, ok }))
    )
  );
  const missing = contains.filter((r) => !r.ok).map((r) => r.pattern);
  if (missing.length === 0) {
    return;
  }
  const granted = await chrome.permissions.request({ origins: missing });
  if (!granted) {
    throw new Error(`Access to ${parsed.origin} (and related hosts) was not granted. Feed not saved.`);
  }
}

async function save() {
  const syncMode = document.querySelector('input[name="syncMode"]:checked')?.value || 'add_only';
  const feedUrl = document.getElementById('feedUrl').value.trim();
  if (!feedUrl) {
    setStatus('Feed URL is required.');
    return;
  }
  const payload = {
    feedUrl,
    folderName: document.getElementById('folderName').value.trim() || DEFAULTS.folderName,
    intervalMinutes: Math.max(1, Number(document.getElementById('intervalMinutes').value) || DEFAULTS.intervalMinutes),
    destructiveSync: syncMode === 'full_sync'
  };
  try {
    await ensureFeedPermission(payload.feedUrl);
  } catch (err) {
    setStatus(err.message);
    return;
  }
  await chrome.runtime.sendMessage({ type: 'SAVE_SETTINGS', payload });
  setStatus('Saved. Schedule updated.');
}

async function syncNow() {
  setStatus('Syncing…');
  const res = await chrome.runtime.sendMessage({ type: 'SYNC_NOW' });
  if (res?.ok) setStatus('Synced!');
  else setStatus('Sync failed: ' + (res?.error || 'unknown error'));
}

function setStatus(msg) {
  document.getElementById('status').textContent = msg;
  setTimeout(() => { document.getElementById('status').textContent = ''; }, 4000);
}

document.getElementById('saveBtn').addEventListener('click', save);
document.getElementById('syncBtn').addEventListener('click', syncNow);
document.addEventListener('DOMContentLoaded', load);
