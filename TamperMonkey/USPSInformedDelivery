// ==UserScript==
// @name        USPS Informed Delivery Navigation
// @namespace   http://tampermonkey.net/
// @version     1.0
// @description Navigate between dates on USPS Informed Delivery
// @author      Dan fishdan.com
// @match       https://informeddelivery.usps.com/box/pages/secure/DashboardAction_input.action*
// @grant       GM_addStyle
// ==/UserScript==

(function() {
    'use strict';

    // Add styles for button div
    GM_addStyle('.welcometext { display: flex; justify-content: center; gap: 20px; margin-bottom: 20px; align-items: center; } .welcometext span { background-color: yellow; padding: 5px; }');

    // Get current date from URL
    let urlParams = new URLSearchParams(window.location.search);
    let selectedDate = urlParams.get('selectedDate');
    let selectedZip = urlParams.get('selectedZip11');
    let currentDate = new Date(decodeURIComponent(selectedDate));
    let oneDay = 24*60*60*1000;

    // Create date strings
    let yesterday = new Date(currentDate.getTime() - oneDay);
    let tomorrow = new Date(currentDate.getTime() + oneDay);
    let yesterdayString = `${yesterday.getMonth()+1}%2F${yesterday.getDate()}%2F${yesterday.getFullYear()}`;
    let tomorrowString = `${tomorrow.getMonth()+1}%2F${tomorrow.getDate()}%2F${tomorrow.getFullYear()}`;

    // Create human-readable date strings for button labels
    let yesterdayLabel = `${('0' + (yesterday.getMonth()+1)).slice(-2)}/${('0' + yesterday.getDate()).slice(-2)}/${yesterday.getFullYear()}`;
    let tomorrowLabel = `${('0' + (tomorrow.getMonth()+1)).slice(-2)}/${('0' + tomorrow.getDate()).slice(-2)}/${tomorrow.getFullYear()}`;

    // Create URL strings
    let yesterdayUrl = `https://informeddelivery.usps.com/box/pages/secure/DashboardAction_input.action?selectedDate=${yesterdayString}&selectedZip11=${selectedZip}`;
    let tomorrowUrl = `https://informeddelivery.usps.com/box/pages/secure/DashboardAction_input.action?selectedDate=${tomorrowString}&selectedZip11=${selectedZip}`;

    // Create new buttons and date span
    let btnDiv = document.createElement('div');
    btnDiv.className = 'welcometext';
    let btnPrev = document.createElement('button');
    let btnNext = document.createElement('button');
    let dateSpan = document.createElement('span');
    btnPrev.textContent = yesterdayLabel;
    btnNext.textContent = tomorrowLabel;
    dateSpan.textContent = `${('0' + (currentDate.getMonth()+1)).slice(-2)}/${('0' + currentDate.getDate()).slice(-2)}/${currentDate.getFullYear()}`;
    btnPrev.onclick = function() {window.location.href = yesterdayUrl;};
    btnNext.onclick = function() {window.location.href = tomorrowUrl;};
    btnDiv.appendChild(btnNext);
    btnDiv.appendChild(dateSpan);
    btnDiv.appendChild(btnPrev);

    // Get the address bar element
    let addressBar = document.getElementById('MPAAddressBar');

    // Insert the new button div below the address bar
    if (addressBar) {
        addressBar.parentNode.insertBefore(btnDiv, addressBar.nextSibling);
    }

})();

/*
MIT License

Copyright (c) 2023 Daniel Fishman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

