# WikiSearch Plugin

[![Version](https://img.shields.io/badge/version-0.1-blue.svg)](#)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![WordPress](https://img.shields.io/badge/WordPress-%5E6.0-blue)](https://wordpress.org/)
[![Download](https://img.shields.io/github/v/release/fishdan/Utilities?display_name=tag\&sort=semver)](https://github.com/fishdan/Utilities/blob/main/plugins/dist/wikisearch.zip)

<!-- ↑ Update the repo owner/name in the Download badge if this lives in a different repo. -->

One‑step Wikipedia linking for WordPress: highlight text, set the link URL to `wikisearch`, publish.

---

## ✨ Features

* **Gutenberg-friendly**: link any text with the URL `wikisearch` and it becomes a Wikipedia link based on the visible text
* **Zero config**: no settings required
* **Lightweight**: tiny JS + CSS, no external calls until a user clicks
* **Theme‑friendly styles** via `.wiki-search`
* Skips links that contain `github` in their `href`

---

## 🎥 Demo

[![WikiSearch demo](https://cdn.loom.com/sessions/thumbnails/12c90919d9bb4e2c84255c69624da187-a0e3e82ed5f25cee-full-play.gif)](https://www.loom.com/share/12c90919d9bb4e2c84255c69624da187)

---

## 📦 Installation

1. Copy the `wikisearch` folder into your WordPress `wp-content/plugins` directory **or** upload the ZIP via **Plugins → Add New → Upload Plugin**.
2. Activate **WikiSearch** under **Plugins → Installed Plugins**.

*(If zipping on Windows, prefer `git archive` or `tar -a -c -f wikisearch.zip wikisearch` so archive paths use forward slashes.)*

---

## 🚀 Usage

### Easiest (Gutenberg)

1. Highlight the text you want to link (e.g., `Albert Einstein`).
2. Click the **Link** button in the block toolbar.
3. Type `wikisearch` as the URL (no `http://`).
4. Publish. On the front‑end, the link will go to `https://en.wikipedia.org/wiki/Albert_Einstein`.

### Classic editor / raw HTML

```html
<a href="wikisearch">Marie Curie</a>
```

This will point to:

```
https://en.wikipedia.org/wiki/Marie_Curie
```

### Multiple on a page

```html
<p>Read about <a href="wikisearch">Alan Turing</a> and <a href="wikisearch">Ada Lovelace</a>.</p>
```

---

## ⚙️ Customization (developer tips)

* **Styling**: override the `.wiki-search` rules in your theme or child theme.
* **Language**: swap `en.wikipedia.org` for another language subdomain in the JS (future admin setting planned).
* **Open in new tab**: set `target="_blank" rel="noopener"` when converting links (see roadmap).

---

## 🛣️ Roadmap / Planned improvements

* Keep **anchor semantics** (convert placeholders to real `<a href>` instead of replacing with `<span>`) for better accessibility and no‑JS fallback
* **Safer selector** (opt‑in via class `a.wikisearch` or attribute `[data-wikisearch]`)
* **Admin setting** for Wikipedia language and target behavior
* **Shortcode + Gutenberg block** for explicit inserts
* Conditional asset enqueue and basic tests/linting

---

## ❓ FAQ

**Why isn’t my GitHub link changing?**

Links whose `href` contains `github` are intentionally skipped so developer docs aren’t altered.

**Can I force a different article slug than the visible text?**

For now the plugin uses the visible text. A shortcode and/or attribute‑based selector is planned for custom slugs.

---

## 🧾 Changelog

* **0.1.0** – Initial release.

---

## 📝 License

MIT. See `LICENSE` for details.



