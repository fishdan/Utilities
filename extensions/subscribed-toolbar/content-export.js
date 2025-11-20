// Bridge between the page and the extension to export the subscribed toolbar folder.
// Pages can request export via: window.postMessage({ type: 'SUBSCRIBED_TOOLBAR_EXPORT' }, '*')
// Responses are posted back as: { type: 'SUBSCRIBED_TOOLBAR_EXPORT_RESULT', ok, feed, json, error }

window.addEventListener('message', async (event) => {
  if (event.source !== window) return;
  if (!event.data || event.data.type !== 'SUBSCRIBED_TOOLBAR_EXPORT') return;
  try {
    const res = await chrome.runtime.sendMessage({ type: 'EXPORT_SUBSCRIBED_FOLDER' });
    if (!res?.ok) {
      window.postMessage({ type: 'SUBSCRIBED_TOOLBAR_EXPORT_RESULT', ok: false, error: res?.error || 'export failed' }, '*');
      return;
    }
    window.postMessage({ type: 'SUBSCRIBED_TOOLBAR_EXPORT_RESULT', ok: true, feed: res.feed, json: res.json }, '*');
  } catch (e) {
    window.postMessage({ type: 'SUBSCRIBED_TOOLBAR_EXPORT_RESULT', ok: false, error: e?.message || 'unknown error' }, '*');
  }
});
