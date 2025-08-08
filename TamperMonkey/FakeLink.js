// ==UserScript==
// @name         Dynamic Link Replacer
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Replace >>text_with_underscores<< with a clickable link by changing underscores to dots
// @match        *://*/*
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    function replaceDynamicLinks() {
        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
        let node;

        while (node = walker.nextNode()) {
            // Use a regular expression to match >>text_with_underscores<< format
            const regex = />>([a-zA-Z0-9_]+)<</g;
            if (node.nodeValue.match(regex)) {
                // Replace matched text patterns with links
                const span = document.createElement("span");
                span.innerHTML = node.nodeValue.replace(regex, (match, p1) => {
                    const linkText = p1.replace(/_/g, '.');   // Convert underscores to dots for display
                    const linkURL = `https://${linkText}`;    // Add https:// prefix for the URL
                    return `<a href="${linkURL}" target="_blank">${linkText}</a>`;
                });
                node.parentNode.replaceChild(span, node);
            }
        }
    }

    // Run the function initially and then every 10 seconds
    replaceDynamicLinks();
    setInterval(replaceDynamicLinks, 10000);

})();

