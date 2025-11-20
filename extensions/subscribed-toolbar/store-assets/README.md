# Chrome Web Store asset kit

The Chrome Web Store image guidelines call for:
- **Screenshots:** 1280×800 PNG/JPG (minimum 640×400) to show the extension UI in use.
- **Promotional tiles:** optional 920×680 (large) and 440×280 (small) PNG/JPG hero images.
- **Icons:** 128×128 square (already provided as `icon128.png`).

This folder packages the assets needed for the Subscribed Toolbar listing:

| File | Purpose |
| --- | --- |
| `promo-small.png` | 440×280 promotional tile summarizing the JSON feed sync story. |
| `promo-large.png` | 920×680 hero tile for featured placements. |
| `screenshot-options.png` | 1280×800 mock of the options page that highlights configurable feed + sync settings. |
| `screenshot-bookmarks.png` | 1280×800 illustration of the bookmarks bar staying in sync. |

## Regenerating
Run the custom generator (no third-party dependencies needed):

```bash
python tools/generate_store_assets.py
```
