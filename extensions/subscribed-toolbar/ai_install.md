# AI-Guided Installation Checklist — Personal Toolbar

**Filename:** `ai_install.md`  
**Purpose:** Authoritative, versioned, machine-followable installation checklist  
**Audience:** AI assistants and human maintainers  
**Scope:** Subscribed Toolbar + Jsonmaker system  

This document defines the **exact installation and onboarding steps** an AI assistant must follow when guiding a user through setup.  
All steps are **imperative**, **order-dependent**, and **non-optional** unless explicitly stated.

---

## Preconditions

- User must be using a **Chromium-based browser**
  - Supported: Chrome, Edge, Brave, Arc, Vivaldi
  - Not supported: Safari, Firefox
- If browser is not supported, **STOP** and explain that only Jsonmaker can be used without the extension.

---

## Phase 1 — Install Extension (Includes Permission Notice)

- Open Chromium browser
- Navigate directly to the Chrome Web Store listing:
  - https://chromewebstore.google.com/detail/hdailbkmbdcililnbemepacdkfdkbhco?utm_source=item-share-cb
- Inform user:
  - Extension requests access to bookmarks and `localhost`
  - Extension is reviewed by Google via the Chrome Web Store
  - It is safe to proceed
- Click **Add to Chrome**
- Confirm installation
- **WAIT** for user confirmation before proceeding

---

## Phase 2 — Pin Extension

- Open browser Extensions menu
- Locate **Subscribed Toolbar**
- Click **Pin**
- Confirm extension icon is visible in browser toolbar
- **WAIT** for confirmation

---

## Phase 3 — Verify Options / Adjust Sync Interval

- Click Subscribed Toolbar icon
- Open **Options**
- Locate **Sync Interval**
- Change value:
  - From `15 minutes`
  - To `600 minutes`
- Click **Save** (if present)
- **WAIT** for confirmation

---

## Phase 4 — Locate Subscribed Toolbar Folder

- Open browser **Bookmarks Manager**
- Locate bookmarks folder created by Subscribed Toolbar
- Confirm folder is visible
- **WAIT** for confirmation

---

## Phase 5 — Add Initial Bookmarks

- Add **3–5 frequently used websites**
- Add directly into the subscribed toolbar folder
- **WAIT** for confirmation

---

## Phase 6 — Create Jsonmaker Account

- Navigate to:
  - https://www.fishdan.com/jsonmaker/
- Create account or sign in
- Confirm user is logged in
- **WAIT** for confirmation

---

## Phase 7 — Import Toolbar into Jsonmaker

- Locate **Import** functionality in Jsonmaker
- Import browser bookmarks
- Select subscribed toolbar folder
- Complete import
- Confirm imported bookmarks are visible in Jsonmaker
- **WAIT** for confirmation

---

## Phase 8 — Connect Jsonmaker to Extension (CRITICAL)

- In Jsonmaker:
  - Select top-level node to sync
  - Click **View Node**
  - Copy View Node URL
- In browser:
  - Open Subscribed Toolbar **Options**
  - Paste URL into **Feed URL**
  - Click **SAVE**
  - Click **SYNC**
- **VERIFY ORDER:** `SAVE` → `SYNC`
- **WAIT** for confirmation

---

## Phase 9 — Confirm Source of Truth

- User explicitly confirms understanding:
  - Jsonmaker controls structure and content
  - Browser toolbar mirrors Jsonmaker
- **WAIT** for confirmation

---

## Phase 10 — Organize Folders

- Create at least one folder in Jsonmaker
- Move at least one link into that folder
- **WAIT** for confirmation

---

## Phase 11 — Partial Sharing via View Node

- Select a folder in Jsonmaker
- Click **View Node**
- Copy folder-specific URL
- Confirm understanding:
  - Only folder contents are shared via that URL
- **WAIT** for confirmation

---

## Phase 12 — Habit Establishment

- Add or modify one link in Jsonmaker
- Observe change syncing to browser toolbar
- **WAIT** for confirmation

---

## Completion Conditions (ALL REQUIRED)

- Extension installed via direct Chrome Web Store link
- Extension pinned
- Options accessed and sync interval modified
- Toolbar folder populated with user bookmarks
- Jsonmaker account created
- Toolbar imported into Jsonmaker
- Feed URL configured in extension
- SAVE clicked before SYNC
- Sync successful
- User understands:
  - Jsonmaker as source of truth
  - Partial sharing via View Node
  - Ongoing maintenance workflow

---

## Versioning Notes

- This file is intended to be version-controlled.
- Any UI, URL, or workflow changes must be reflected here.
- AI assistants should always follow the **latest committed version**.

