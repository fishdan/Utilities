// ==UserScript==
// @name         Delete Specific Facebook Divs
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Delete all divs of class 'x1lliihq' if they don't contain names in the 'names' array
// @author       ChatGPT
// @match        *://www.facebook.com/*
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    let names = ['Daniel Fishman', 'Ken Moellman'];

    function checkAndRemoveDivs() {
        const divs = document.querySelectorAll('.x1lliihq');

        divs.forEach(div => {
            let shouldDelete = true;
            for (let name of names) {
                if (div.textContent.includes(name)) {
                    shouldDelete = false;
                    break;
                }
            }
            if (shouldDelete) {
                div.remove();
            }
        });
    }

    // Initially check
    checkAndRemoveDivs();

    // Check periodically as Facebook loads content dynamically
    setInterval(checkAndRemoveDivs, 5000); // Check every 5 seconds

})();
