// ==UserScript==
// @name         captureChatGPTAsPNG
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       fishdan
// @homepage     fishdan.com
// @match        https://chat.openai.com/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=google.com
// @grant        none
// @license      bsd
// ==/UserScript==
// @require https://html2canvas.hertzen.com/dist/html2canvas.min.js


alert('This chat can be saved as a png');
(function() {
  'use strict';

  // Create a button to trigger the capture process
  const button = document.createElement('button');
  button.innerHTML = 'Capture div as PNG';
  button.style.position = 'fixed';
  button.style.top = '10px';
  button.style.right = '10px';
  document.body.appendChild(button);

  // Add a click event listener to the button
  button.addEventListener('click', function() {
    // Get the div element you want to capture
            // Get the "main" element
      const mainElement = document.getElementsByTagName("main")[0];

      // Find the first "div" child element of the "main" element
      const firstElement = mainElement.querySelector("div");
      const lement1 = firstElement.querySelector("div");
      const lement2 = lement1.querySelector("div");
      const divElement = lement2.querySelector("div");
    // Wait for the div element to become available in the DOM
    const waitForDiv = setInterval(function() {
      if (divElement) {
        clearInterval(waitForDiv);
        let scrollHeight = Math.max(
            divElement.scrollHeight, document.documentElement.scrollHeight,
            divElement.offsetHeight, document.documentElement.offsetHeight,
            divElement.clientHeight, document.documentElement.clientHeight
        );
        // Create a clone of the div element that is large enough to contain all of its content
        const clone = divElement.cloneNode(true);
        mainElement.appendChild(clone);
        clone.style.width = clone.offsetWidth + 'px';
        clone.style.height = clone.offsetHeight + 'px';
        clone.style.overflow = 'visible';

        // Capture the contents of the clone as a PNG image
        html2canvas(clone).then(function(canvas) {
          // Convert the canvas to a data URL
          const img = canvas.toDataURL('image/png');

          // Create a link to download the data URL as a PNG file
          const link = document.createElement('a');
          link.download = 'div.png';
          link.href = img;
          document.body.appendChild(link);

          // Click the link to initiate the download
          link.click();

          // Remove the temporary container and link
          mainElement.removeChild(clone);
          document.body.removeChild(link);
        });
      }
    }, 100);
  });
})();
function loadJS(FILE_URL, async = true) {
  let scriptEle = document.createElement("script");

  scriptEle.setAttribute("src", FILE_URL);
  scriptEle.setAttribute("type", "text/javascript");
  scriptEle.setAttribute("async", async);

  document.body.appendChild(scriptEle);

  // success event
  scriptEle.addEventListener("load", () => {
    console.log("File loaded")
  });
   // error event
  scriptEle.addEventListener("error", (ev) => {
    console.log("Error on loading file", ev);
  });
}
loadJS("https://html2canvas.hertzen.com/dist/html2canvas.min.js", true);


