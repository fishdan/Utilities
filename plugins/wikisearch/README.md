# WikiSearch for WordPress

Turn simple links into instant Wikipedia lookups on your site.

## What it does

WikiSearch scans page content for links whose `href` contains the string `wikisearch` and converts them into clickable elements that take the visitor to the corresponding Wikipedia article. For example, a link like:

```html
<a href="wikisearch">Albert Einstein</a>
```

will become a clickable element that navigates to:

```
https://en.wikipedia.org/wiki/Albert_Einstein
```

## Easiest way to use it (Gutenberg editor)

1. In the WordPress editor, **highlight the text** you want linked to Wikipedia.
2. Click the **link button** in the toolbar.
3. Type `wikisearch` as the URL (no `http://` needed).
4. Save and publish—WikiSearch will automatically convert it to a working Wikipedia link.

![Screenshot showing link dialog with wikisearch entered](screenshot.png)

## How it works (current behavior)

* **Front‑end script** runs after `DOMContentLoaded`, finds `a[href*="wikisearch"]`, builds a Wikipedia URL from the link text, and replaces the `<a>` with a `<span>` that redirects on click.cite22†wikisearch.css23†wikisearch.js
* **Styling** provides a traditional “link blue” color, underline, hover state, and pointer cursor.

> ⚠️ Accessibility note: replacing semantic `<a>` links with `<span>` removes keyboard focus/Tab support and ARIA semantics. See the **Roadmap** for improvements.

## Installation

1. Upload the plugin folder to `/wp-content/plugins/` (or install via the WordPress admin).
2. Activate **WikiSearch** from **Plugins → Installed Plugins**.
3. Ensure the plugin enqueues its JavaScript and CSS on the front‑end.

## Usage examples

**Simplest**

```html
<a href="wikisearch">Marie Curie</a>
```

**With display text different from the article slug**

```html
<a href="wikisearch">C. elegans</a>
<!-- goes to https://en.wikipedia.org/wiki/C._elegans -->
```

**Multiple on a page**

```html
<p>Read about <a href="wikisearch">Alan Turing</a> and <a href="wikisearch">Ada Lovelace</a>.</p>
```

## Customization (developer tips)

* **Styling**: override the `.wiki-search` rules in your theme/child theme.
* **Behavior**: fork the front‑end JS to change selection logic (e.g., only convert links with class `.wikisearch` or with a `data-wikisearch` attribute).
* **Language**: modify the base host from `en.wikipedia.org` to another Wikipedia language subdomain.

## Roadmap / Suggested improvements

These are recommendations for the next versions to improve UX, accessibility, and WordPress‑friendliness.

1. **Keep `<a>` semantics instead of replacing with `<span>`**

    * Convert placeholder links into real anchors with `href` set to the computed Wikipedia URL.
    * Benefits: keyboard access, screen readers, SEO, and no‑JS fallback.

2. **Safer selector**

    * Replace the broad selector `a[href*="wikisearch"]` with one of:

        * `a[href^="wikisearch"]` (hash‑only placeholder), or
        * `a.wikisearch` (class‑based opt‑in), or
        * `[data-wikisearch]` (attribute‑driven), using `data-wikisearch="Albert Einstein"`.

3. **Keyboard accessibility** (if `<span>` is kept temporarily)

    * Add `role="link"`, `tabindex="0"`, and activate on `Enter`/`Space` key.

4. **Language setting in WP Admin**

    * Add a Settings page to choose the Wikipedia language (e.g., `en`, `es`, `he`) and whether to open in a new tab.
    * Pass settings to JS with `wp_localize_script`.

5. **No‑JS fallback**

    * During render (filter `the_content` or shortcodes), compute the final `href` server‑side so links work without JavaScript.

6. **Shortcode and Gutenberg block**

    * Shortcode: `[wikisearch term="Albert Einstein" text="Einstein" lang="en"]`
    * Optional simple block for editors who prefer visual insertion.

7. **Security & performance**

    * Only enqueue assets on the front‑end and only on posts/pages that actually contain WikiSearch elements (conditional enqueue).
    * Use versioned enqueues to bust caches when scripts change.

8. **Internationalization & i18n**

    * Make all admin strings translatable. Support different Wikipedia subdomains per site locale.

9. **Unit tests / linting**

    * Add basic Jest tests for URL generation and selector behavior. Add ESLint configuration.

10. **Documentation**

* Include examples, FAQs (“Why did my link change?”, “How do I target French Wikipedia?”), and a changelog.

## FAQ

**Why is my GitHub link untouched?**

The current implementation intentionally skips links whose `href` contains `github` so developer documentation links are not altered.

**Can I make it open in a new tab?**

Yes—modify the code to set `target="_blank"` and `rel="noopener noreferrer"` when converting to anchors.

## Changelog

* **0.1.0** – First public release.

## License

MIT
