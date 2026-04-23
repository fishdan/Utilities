// ==UserScript==
// @name         PFN Mock Draft — alert when drafting-info contains dfish
// @namespace    http://tampermonkey.net/
// @version      1.0.0
// @description  Bell + notification when .drafting-info contains "dfish" (mock draft turn alert)
// @author       fishdan
// @match        https://www.profootballnetwork.com/mockdraft*
// @grant        GM_notification
// @run-at       document-idle
// ==/UserScript==

(function () {
    'use strict';

    const NEEDLE = 'dfish';
    const SELECTOR = 'span.drafting-info';

    let lastTriggered = false;

    function draftingInfoContainsNeedle() {
        const nodes = document.querySelectorAll(SELECTOR);
        for (let i = 0; i < nodes.length; i++) {
            const text = (nodes[i].textContent || '').toLowerCase();
            if (text.includes(NEEDLE)) {
                return true;
            }
        }
        return false;
    }

    function playBell() {
        const AC = window.AudioContext || window.webkitAudioContext;
        if (!AC) {
            return;
        }
        const ctx = new AC();
        const resume = ctx.state === 'suspended' ? ctx.resume() : Promise.resolve();
        resume.then(function () {
            const t = ctx.currentTime;
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();
            osc.type = 'sine';
            osc.connect(gain);
            gain.connect(ctx.destination);
            osc.frequency.setValueAtTime(784, t);
            osc.frequency.exponentialRampToValueAtTime(1047, t + 0.12);
            gain.gain.setValueAtTime(0.12, t);
            gain.gain.exponentialRampToValueAtTime(0.001, t + 0.45);
            osc.start(t);
            osc.stop(t + 0.45);
        }).catch(function () {});
    }

    function notifyUser() {
        const title = 'PFN mock draft';
        const text = 'Your pick — drafting info mentions "' + NEEDLE + '".';
        try {
            GM_notification({
                title: title,
                text: text,
                timeout: 8000,
            });
        } catch (e) {
            if (typeof Notification !== 'undefined' && Notification.permission === 'granted') {
                new Notification(title, { body: text });
            }
        }
        playBell();
    }

    function tick() {
        const on = draftingInfoContainsNeedle();
        if (on && !lastTriggered) {
            notifyUser();
            lastTriggered = true;
        } else if (!on) {
            lastTriggered = false;
        }
    }

    function requestNotifyPermission() {
        if (typeof Notification === 'undefined') {
            return;
        }
        if (Notification.permission === 'default') {
            Notification.requestPermission().catch(function () {});
        }
    }

    requestNotifyPermission();
    tick();

    const observer = new MutationObserver(function () {
        tick();
    });
    observer.observe(document.documentElement, {
        subtree: true,
        childList: true,
        characterData: true,
        attributes: true,
    });
})();
