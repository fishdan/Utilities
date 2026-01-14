# Testing Guide for "Absolute Enable Right Click & Copy" Script

## Quick Start Testing

1. **Install the script in TamperMonkey**
   - Open TamperMonkey dashboard
   - Create a new script or edit the existing one
   - Copy the contents of `AbsoluteEnableRightClickCopy.user.js`

2. **Open the test page**
   - Open `test-rightclick-protection.html` in your browser
   - The script should automatically run on the test page

3. **Run the tests**
   - Follow the instructions in each test section
   - Click "Run All Tests" to see a summary

## Manual Testing Checklist

### ✅ Basic Functionality Tests

- [ ] **Right-click menu appears**
  - Right-click anywhere on a protected page
  - Context menu should appear (not be blocked)

- [ ] **Text selection works**
  - Try to select text by dragging
  - Text should highlight and be selectable

- [ ] **Copy works**
  - Select text and press Ctrl+C (Cmd+C on Mac)
  - Text should copy to clipboard

- [ ] **Paste works**
  - Copy some text, then paste it somewhere
  - Paste should work normally

- [ ] **Keyboard shortcut works**
  - Press Ctrl + ` (backtick) on a protected page
  - Should see confirmation dialog
  - After confirming, absolute mode should activate

- [ ] **TamperMonkey menu command works**
  - Click TamperMonkey icon → "Enable Absolute Right Click Mode"
  - Should see confirmation dialog
  - After confirming, absolute mode should activate

### ✅ Protection Bypass Tests

Test on websites that use different protection methods:

1. **Simple oncontextmenu block**
   - Sites that just use `oncontextmenu="return false"`
   - Should be bypassed

2. **Event listener protection**
   - Sites using `addEventListener('contextmenu', ...)`
   - Should be bypassed

3. **CSS user-select: none**
   - Sites preventing text selection via CSS
   - Should be overridden by script's CSS injection

4. **Multiple protection layers**
   - Sites using multiple methods simultaneously
   - All should be bypassed

### ✅ Blacklist Testing

The script should **NOT** run on these sites (check that it doesn't interfere):
- youtube.com
- google.com
- twitter.com
- instagram.com
- facebook.com
- github.com
- stackoverflow.com
- reddit.com

### ✅ Browser Compatibility Tests

Test in different browsers:
- [ ] Chrome/Chromium
- [ ] Firefox (with TamperMonkey)
- [ ] Edge
- [ ] Opera

### ✅ Edge Cases

- [ ] **Dynamic content**
  - Test on pages that load content via JavaScript
  - Script should still work after content loads

- [ ] **Iframes**
  - Test on pages with iframes
  - May need to test if script works in iframes (depends on @include settings)

- [ ] **Single Page Applications (SPAs)**
  - Test on React/Vue/Angular apps
  - Script should work on dynamically loaded content

## Automated Testing (Advanced)

### Using Browser DevTools

1. **Open DevTools Console**
   - Press F12 or right-click → Inspect
   - Go to Console tab

2. **Check for errors**
   - Look for any JavaScript errors
   - Script should not throw errors

3. **Verify script injection**
   ```javascript
   // Check if CSS was injected
   document.querySelector('style[type="text/css"]')
   
   // Check if event listeners are removed
   document.oncontextmenu
   // Should be null
   ```

4. **Test event propagation**
   ```javascript
   // Add a test listener
   document.addEventListener('contextmenu', function(e) {
       console.log('Context menu event fired:', e);
   }, true);
   
   // Right-click and check console
   ```

### Using Browser Automation (Selenium/Playwright)

For automated testing, you could create a test suite:

```javascript
// Example Playwright test
test('right-click should work', async ({ page }) => {
  await page.goto('http://localhost/test-rightclick-protection.html');
  
  // Try right-click
  await page.click('#test1', { button: 'right' });
  
  // Check if context menu appears (implementation depends on your setup)
});
```

## Common Issues to Check

1. **Script not running**
   - Check TamperMonkey dashboard → Script is enabled
   - Check @include matches the URL you're testing
   - Check browser console for errors

2. **Partial functionality**
   - Some protections might use newer methods
   - Check if script needs updates for new protection techniques

3. **Performance issues**
   - Script should not slow down page loading
   - Check browser performance tab

4. **Conflicts with other scripts**
   - Disable other TamperMonkey scripts
   - Test if script works in isolation

## Real-World Test Sites

Here are some sites that commonly use right-click protection (use responsibly):

1. **News sites** - Many news sites protect their content
2. **Image galleries** - Protect images from being saved
3. **E-learning platforms** - Protect course content
4. **Document viewers** - Protect PDF/document content

⚠️ **Note**: Only test on sites you have permission to test, or use the provided test page.

## Reporting Issues

If you find issues, note:
- Browser and version
- TamperMonkey version
- URL where issue occurs
- Steps to reproduce
- Console errors (if any)
- Expected vs actual behavior
