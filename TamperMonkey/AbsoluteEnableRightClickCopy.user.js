// ==UserScript==
// @name          Absolute Enable Right Click & Copy
// @namespace     Absolute Right Click
// @description   Force Enable Right Click & Copy & Highlight
// @shortcutKeys  [Ctrl + `] Activate Absolute Right Click Mode To Force Remove Any Type Of Protection
// @author        Absolute
// @version       2.0.0
// @include       *://*
// @icon          https://i.imgur.com/AC7SyUr.png
// @compatible    Chrome Google Chrome + Tampermonkey
// @grant         GM_registerMenuCommand
// @license       BSD
// @copyright     Absolute, 2016-Oct-06
// ==/UserScript==

(function() {
    'use strict';

    // CSS to enable text selection
    const css = document.createElement('style');
    css.type = 'text/css';
    css.textContent = `* {
        -webkit-user-select: text !important;
        -moz-user-select: text !important;
        -ms-user-select: text !important;
        user-select: text !important;
    }`;

    // Blacklist of sites where the script should not run
    const blackList = [
        'youtube.com', '.google.', '.google.com', 'greasyfork.org', 'twitter.com',
        'instagram.com', 'facebook.com', 'translate.google.com', '.amazon.', '.ebay.',
        'github.', 'stackoverflow.com', 'bing.com', 'live.com', '.microsoft.com',
        'dropbox.com', 'pcloud.com', 'box.com', 'sync.com', 'onedrive.com', 'mail.ru',
        'deviantart.com', 'pastebin.com', 'dailymotion.com', 'twitch.tv', 'spotify.com',
        'steam.com', 'steampowered.com', 'gitlab.com', '.reddit.com'
    ];

    // Sites that should always have absolute mode enabled
    const alwaysAbsoluteSites = ['example.com', 'www.example.com'];

    let enabled = false;

    /**
     * Removes event handlers from document and body
     */
    function removeEventHandlers() {
        const doc = document;
        const body = document.body;

        // Remove inline event handlers
        doc.oncontextmenu = null;
        doc.onselectstart = null;
        doc.ondragstart = null;
        doc.onmousedown = null;

        body.oncontextmenu = null;
        body.onselectstart = null;
        body.ondragstart = null;
        body.onmousedown = null;
        body.oncut = null;
        body.oncopy = null;
        body.onpaste = null;
    }

    /**
     * Stops propagation for clipboard and selection events
     */
    function stopEventPropagation() {
        const events = ['copy', 'cut', 'paste', 'select', 'selectstart'];
        events.forEach(event => {
            document.addEventListener(event, (e) => {
                e.stopPropagation();
            }, true);
        });
    }

    /**
     * Activates absolute mode - stops all event propagation for protected events
     */
    function absoluteMode() {
        const events = [
            'contextmenu', 'copy', 'cut', 'paste', 'mouseup', 'mousedown',
            'keyup', 'keydown', 'drag', 'dragstart', 'select', 'selectstart'
        ];
        events.forEach(event => {
            document.addEventListener(event, (e) => {
                e.stopPropagation();
            }, true);
        });
    }

    /**
     * Checks if current site should always have absolute mode enabled
     */
    function shouldAlwaysEnableAbsoluteMode() {
        const match = RegExp(alwaysAbsoluteSites.join('|')).exec(location.hostname);
        return match !== null;
    }

    /**
     * Handles keyboard shortcut (Ctrl + `)
     */
    function handleKeyPress(event) {
        // keyCode 192 is backtick (`), but using modern key property
        if (event.ctrlKey && (event.key === '`' || event.keyCode === 192)) {
            const activate = confirm('Activate Absolute Right Click Mode!');
            if (activate) {
                absoluteMode();
            }
        }
    }

    /**
     * Registers Tampermonkey menu command if available
     */
    function enableCommandMenu() {
        try {
            if (typeof GM_registerMenuCommand !== 'undefined') {
                GM_registerMenuCommand('Enable Absolute Right Click Mode', () => {
                    const activate = confirm('Activate Absolute Right Click Mode!');
                    if (activate) {
                        absoluteMode();
                    }
                });
            }
        } catch (err) {
            console.error('Error registering menu command:', err);
        }
    }

    /**
     * Creates a custom event handler for context menu events
     */
    function createEventHandler(event) {
        return {
            event: event,
            contextmenuEvent: null,
            isCanceled: false,

            createEvent() {
                const target = this.event.target;
                const doc = target.ownerDocument;
                
                // Use modern event constructor if available, fallback to deprecated method
                if (typeof MouseEvent !== 'undefined' && MouseEvent.prototype.initMouseEvent) {
                    const mouseEvent = doc.createEvent('MouseEvents');
                    mouseEvent.initMouseEvent(
                        this.event.type,
                        this.event.bubbles,
                        this.event.cancelable,
                        doc.defaultView,
                        this.event.detail,
                        this.event.screenX,
                        this.event.screenY,
                        this.event.clientX,
                        this.event.clientY,
                        this.event.ctrlKey,
                        this.event.altKey,
                        this.event.shiftKey,
                        this.event.metaKey,
                        this.event.button,
                        this.event.relatedTarget
                    );
                    this.contextmenuEvent = mouseEvent;
                } else {
                    // Modern approach using MouseEvent constructor
                    this.contextmenuEvent = new MouseEvent(this.event.type, {
                        bubbles: this.event.bubbles,
                        cancelable: this.event.cancelable,
                        view: doc.defaultView,
                        detail: this.event.detail,
                        screenX: this.event.screenX,
                        screenY: this.event.screenY,
                        clientX: this.event.clientX,
                        clientY: this.event.clientY,
                        ctrlKey: this.event.ctrlKey,
                        altKey: this.event.altKey,
                        shiftKey: this.event.shiftKey,
                        metaKey: this.event.metaKey,
                        button: this.event.button,
                        relatedTarget: this.event.relatedTarget
                    });
                }
            },

            fire() {
                if (!this.contextmenuEvent) {
                    this.createEvent();
                }
                const target = this.event.target;
                target.dispatchEvent(this.contextmenuEvent);
                this.isCanceled = this.contextmenuEvent.defaultPrevented;
            }
        };
    }

    /**
     * Monitors DOM changes using MutationObserver (replaces deprecated mutation events)
     */
    function createMutationObserver(callback) {
        if (typeof MutationObserver === 'undefined') {
            // Fallback for very old browsers (unlikely to be needed)
            return null;
        }

        const observer = new MutationObserver(() => {
            callback();
        });

        observer.observe(document, {
            childList: true,
            subtree: true,
            attributes: true,
            attributeOldValue: false,
            characterData: true
        });

        return observer;
    }

    /**
     * Main initialization function
     */
    function main() {
        removeEventHandlers();
        stopEventPropagation();

        // Enable absolute mode for specific sites
        if (shouldAlwaysEnableAbsoluteMode()) {
            absoluteMode();
        }

        enableCommandMenu();
        document.head.appendChild(css);
        document.addEventListener('keydown', handleKeyPress);
    }

    /**
     * Handles context menu events with advanced protection bypass
     */
    function setupContextMenuHandler() {
        let mutationObserver = null;
        let mutationDetected = false;

        // Use MutationObserver instead of deprecated mutation events
        mutationObserver = createMutationObserver(() => {
            mutationDetected = true;
        });

        window.addEventListener('contextmenu', function contextmenuHandler(event) {
            event.stopPropagation();
            event.stopImmediatePropagation();

            const handler = createEventHandler(event);
            mutationDetected = false;

            // Temporarily remove listener to avoid recursion
            window.removeEventListener(event.type, contextmenuHandler, true);

            // Fire the event and check if it was canceled
            handler.fire();

            // Re-add listener
            window.addEventListener(event.type, contextmenuHandler, true);

            // If event was canceled and DOM was modified, prevent default
            if (handler.isCanceled && mutationDetected) {
                event.preventDefault();
            }
        }, true);
    }

    // Main execution
    if (window && typeof window !== 'undefined' && document.head) {
        const url = window.location.hostname;
        const match = RegExp(blackList.join('|')).exec(url);

        if (!match && !enabled) {
            main();
            enabled = true;
            setupContextMenuHandler();
        }
    }

})();
