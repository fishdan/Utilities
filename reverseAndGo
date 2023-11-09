// ==UserScript==
// @name         Reverse Highlighted Text and Navigate
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Reverse highlighted text on Ctrl+Q and navigate to it as a URL
// @author       fishdan
// @match        *://*/*
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    function reverseString(str) {
        return str.split('').reverse().join('');
    }

    function isUrl(str) {
        return /^(?:http|https):\/\//.test(str);
    }

    document.addEventListener('keydown', function(e) {
        // Check if Ctrl+Q is pressed
        if (e.ctrlKey && e.key.toLowerCase() === 'q') {
            e.preventDefault(); // Prevent the default action of the keypress
            var selection = window.getSelection().toString(); // Get the selected text
            if (selection) {
                var reversed = reverseString(selection); // Reverse the text
                var newLocation = isUrl(reversed) ? reversed : 'http://' + reversed;
                window.location.href = newLocation; // Try to change the window location
            }
        }
    });
})();
