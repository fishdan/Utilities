# Dynamic Toolbar Extension

Dynamic Toolbar mirrors a remote bookmarks feed into a dedicated folder on your bookmarks bar, keeping the folder updated on a recurring schedule.

## Features
- Synchronises a JSON feed of bookmarks into a chosen toolbar folder.
- Two sync modes: add-only (default) or full sync that replaces folder contents.
- Configurable sync interval, feed URL, and target folder name.
- Manual sync button in the options page for on-demand updates.

## Installation
1. Clone or copy this project onto your machine.
2. Open `chrome://extensions` (or `brave://extensions`) in a Chromium-based browser.
3. Enable **Developer mode**.
4. Click **Load unpacked** and select the `extensions/dynamic-toolbar` directory.
5. Use **Reload** on the extension card after making local changes.

## Configuration
Open the extension options via **Details → Extension options**:
- **Feed URL** – HTTPS endpoint returning a JSON feed (see format below).
- **Target Folder Name** – Folder created/updated inside the bookmarks bar.
- **Sync Every (minutes)** – Refresh interval (minimum 1 minute).
- **Sync Mode** – Choose add-only (preserves existing items) or full sync (recreates the folder).

Saving updates the schedule immediately. Use **Sync now** for an immediate refresh.

## Feed Format
The feed must be a JSON object with a `children` array describing bookmark and folder nodes.

```json
{
  "title": "Subscribed Toolbar",
  "children": [
    { "title": "Codex Docs", "value": "https://platform.openai.com/docs" },
    {
      "title": "Tools",
      "children": [
        { "title": "GitHub", "value": "https://github.com" }
      ]
    }
  ]
}
```

Rules:
- Each node needs a non-empty `title`.
- Bookmark nodes include a valid `value` (or legacy `url`) field with an HTTPS address.
- Folder nodes include a `children` array with nested nodes.

## Notes
- Only the chosen folder is modified; other bookmarks remain untouched.
- Full sync mode removes manual edits within the target folder.
- The extension stores settings in `chrome.storage.sync`, so they follow your profile.
