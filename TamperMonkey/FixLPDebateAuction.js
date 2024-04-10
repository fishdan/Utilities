// ==UserScript==
// @name         Fix LP Debate Order
// @namespace    http://tampermonkey.net/
// @version      2024-04-10
// @description  rearrange the candidate divs in random order
// @author       fishdan
// @match        https://lnc2024.com/presidential-debate-contest/
// @icon         https://www.google.com/s2/favicons?sz=64&domain=lnc2024.com
// @grant        none
// ==/UserScript==

(function() {
    'use strict';
    console.log('v.1');
//To make work on a standalone page instead of inside TamperMonkey uncomment the <script> and </script>
//tags and put the script tag and everything inside it on the web page
//<script>
// Function to shuffle an array (Fisher-Yates shuffle algorithm)
function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]]; // Swap elements
    }
}

// Collect all sub-divs and remove the "et-last-child" class
const allSubDivs = [];
for (let i = 1; i <= 3; i++) {
    const rowDivs = document.querySelectorAll(`.et_pb_row_${i} > div`);
    rowDivs.forEach(div => {
        div.classList.remove('et-last-child'); // Remove the class from all sub-divs
        allSubDivs.push(div);
    });
}

// Shuffle the array of sub-divs
shuffleArray(allSubDivs);

// Distribute the shuffled sub-divs across the 3 main divs and handle the "et-last-child" class
const mainDivs = ['et_pb_row_1', 'et_pb_row_2', 'et_pb_row_3'];
mainDivs.forEach((divClass, index) => {
    const parentDiv = document.querySelector(`.${divClass}`);
    parentDiv.innerHTML = ''; // Clear existing content
    for (let j = 0; j < 3; j++) {
        parentDiv.appendChild(allSubDivs[index * 3 + j]);
    }
    // Add "et-last-child" class to the last sub-div in each row
    parentDiv.lastElementChild.classList.add('et-last-child');
});
//<script>



})();