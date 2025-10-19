# Dynamic Toolbar Privacy Policy

_Last updated: 2025-10-19_

Dynamic Toolbar is a Chrome extension developed by Daniel Fishman. The extension mirrors a user-provided bookmarks feed into a folder on the browser’s bookmarks bar. This policy describes the data the extension accesses, how that data is used, and the choices available to you.

## Data We Access and Store

- **Bookmarks**: The extension reads the Chrome bookmarks tree to locate or create the configured toolbar folder, and it adds, updates, or removes entries inside that folder to match your remote feed. No other bookmarks are modified.
- **Extension Settings**: The feed URL, target folder name, sync frequency, and sync mode are stored in `chrome.storage.sync` so they can be synced across your Chrome profile. No other personal data is stored.
- **Network Requests**: The extension fetches the JSON feed from the HTTPS URL you provide. These requests are made directly from your browser to that URL; the developer does not receive or proxy the data.

## Optional Host Permissions

When you save a feed URL, the extension requests runtime permission to access the corresponding domain so it can download the feed. The permission is limited to the origin you approved and can be revoked at any time from the Chrome Extensions settings.

## How We Use Data

- Bookmark information is used solely to keep the selected toolbar folder synchronized with the remote feed.
- Stored settings are used to remember your configuration and schedule background syncs.
- Network responses are validated and immediately reflected in the toolbar folder; they are not transmitted elsewhere or retained outside your browser.

The extension does not collect analytics, transmit personal information to the developer, or share data with third parties.

## Data Retention and Control

- Settings saved in `chrome.storage.sync` remain until you change them or uninstall the extension.
- Any bookmarks created by the extension remain in your bookmarks bar until removed by you or by the extension during a sync.
- You can revoke previously granted host permissions via **chrome://extensions** → **Details** → **Permissions**.

Uninstalling the extension removes its settings and stops all background activity.

## Security

Dynamic Toolbar relies on Chrome’s extension platform security mechanisms, including the permissions model and secure storage. The extension does not implement additional data transmission or storage beyond what is described here.

## Changes to This Policy

If the privacy policy changes, the updated version will be published in this repository. Your continued use of the extension after changes take effect constitutes acceptance of the revised policy.

## Contact

For questions about this policy or the extension, visit [https://www.fishdan.com/contact](https://www.fishdan.com/contact).
