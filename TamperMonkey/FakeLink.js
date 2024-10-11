// ==UserScript==
// @name         Replace Fake Link
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Replace >>my_fake_link_com<< with a clickable link to https://my.fake.link.com
// @match        *://*/*
// @author       fishdan
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    function replaceFakeLink() {
        // Find all text nodes
        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
        let node;
        while (node = walker.nextNode()) {
            if (node.nodeValue.includes(">>my_fake_link_com<<")) {
                // Replace the text with an anchor element
                const span = document.createElement("span");
                span.innerHTML = node.nodeValue.replace(
                    />>my_fake_link_com<</g,
                    '<a href="https://my.fake.link.com" target="_blank">my.fake.link.com</a>'
                );
                node.parentNode.replaceChild(span, node);
            }
        }
    }

    // Run the function initially and then every 10 seconds
    replaceFakeLink();
    setInterval(replaceFakeLink, 10000);

})();
