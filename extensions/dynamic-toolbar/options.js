const SETTINGS_KEY = 'subscribedToolbar.settings';
const DEFAULTS = {
  feedUrl: 'https://example.com/bookmarks.json',
  folderName: 'Subscribed Toolbar',
  intervalMinutes: 15,
  destructiveSync: false
};

async function load() {
  const { [SETTINGS_KEY]: s } = await chrome.storage.sync.get(SETTINGS_KEY);
  const cfg = { ...DEFAULTS, ...(s || {}) };
  document.getElementById('feedUrl').value = cfg.feedUrl;
  document.getElementById('folderName').value = cfg.folderName;
  document.getElementById('intervalMinutes').value = cfg.intervalMinutes;
  document.querySelector('input[name="syncMode"][value="add_only"]').checked = !cfg.destructiveSync;
  document.querySelector('input[name="syncMode"][value="full_sync"]').checked = !!cfg.destructiveSync;
}

async function save() {
  const syncMode = document.querySelector('input[name="syncMode"]:checked')?.value || 'add_only';
  const payload = {
    feedUrl: document.getElementById('feedUrl').value.trim(),
    folderName: document.getElementById('folderName').value.trim() || DEFAULTS.folderName,
    intervalMinutes: Math.max(1, Number(document.getElementById('intervalMinutes').value) || DEFAULTS.intervalMinutes),
    destructiveSync: syncMode === 'full_sync'
  };
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
