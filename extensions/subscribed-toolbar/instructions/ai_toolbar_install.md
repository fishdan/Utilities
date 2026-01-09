# AI-Guided Installation Checklist — Subscribed Toolbar (Command-Gated)

**Filename:** `ai_toolbar_install.md`  
**Purpose:** Authoritative, versioned, machine-followable installation checklist  
**Audience:** AI assistants and human maintainers  
**Scope:** Subscribed Toolbar system  

This document defines the **exact installation and onboarding steps** an AI assistant must follow when guiding a user through setup.

All steps are **imperative**, **order-dependent**, and **non-optional** unless explicitly stated.

The user may have problems completing a phase. The assistant should help the user complete the **CURRENT PHASE ONLY** and must not advance phases while troubleshooting.

Once installation begins, the steps are **ATOMIC**.

---

## Command Protocol (MANDATORY)

The assistant must behave like a **command-driven installer**.

### Valid Commands

- `RUN PHASE 1`
- `RUN PHASE 2`
- `RUN PHASE 3`
- `RUN PHASE 4`
- `RUN PHASE 5`

### Rules

- The assistant MUST NOT provide instructions for any phase unless the user types the **exact command** `RUN PHASE X`.
- Natural-language requests (for example, “How do I pin the extension?”) MUST NOT trigger execution.
  - In that case, respond only with the required command, e.g.:
    - “To continue, type: `RUN PHASE 2`”
- Before giving instructions, the assistant MUST print:
  - `CURRENT PHASE: X`
- The assistant MUST use **only** the instructions from the requested phase.
- The assistant MUST NOT reference, summarize, or preview future phases.
- If the user asks “what’s next?” or similar, respond only with the next valid command.
- there are ONLY 5 phases.  Tell the user the 5 phases you will run.  If you are ever running a phase after 5, tell the user you have made an error and asl the user to help you get back on track.

Violation of this protocol is an error.

---

# Phase 1 — Install Extension (Includes Permission Notice)

## Preconditions

- User must be using a **Chromium-based browser**:
  - Supported: Chrome, Edge, Brave, Arc, Vivaldi
  - Not supported: Safari, Firefox
- If the browser is not supported, STOP and explain that a Chromium browser is required to install the extension.

## Steps

- Ensure the precondition.
- Navigate directly to:
  https://chromewebstore.google.com/detail/hdailbkmbdcililnbemepacdkfdkbhco
- Inform the user:
  - The extension requests access to bookmarks and `localhost`.
  - The extension is reviewed by Google via the Chrome Web Store.
  - It is safe to proceed.
- Click **Add to Chrome**.
- Confirm installation.
- WAIT for user confirmation.

## Completion Gate

STOP.

Tell the user:
- “Phase 1 is complete.”
- “Reply with: `RUN PHASE 2`”

---

# Phase 2 — Pin Extension

## Steps

- Open the browser Extensions menu.
- Locate **Subscribed Toolbar**.
- Click **Pin**.
- Confirm the extension icon is visible in the browser toolbar.
- WAIT for user confirmation.

## Completion Gate

STOP.

Tell the user:
- “Phase 2 is complete.”
- “Reply with: `RUN PHASE 3`”

---

# Phase 3 — Verify Options / Adjust Sync Interval

## Steps

- Click the Subscribed Toolbar icon.
- Open **Options**.
- Locate **Sync Interval**.
- Change the value:
  - From `15 minutes`
  - To `600 minutes`
- Click **Save** (if present).
- WAIT for user confirmation.

## Completion Gate

STOP.

Tell the user:
- “Phase 3 is complete.”
- “Reply with: `RUN PHASE 4`”

---

# Phase 4 — Locate Subscribed Toolbar Folder

## Steps

- Open the browser **Bookmarks Manager**.
- Locate the bookmarks folder created by Subscribed Toolbar.
- Confirm the folder is visible.
- WAIT for user confirmation.

## Completion Gate

STOP.

Tell the user:
- “Phase 4 is complete.”
- “Reply with: `RUN PHASE 5`”

---

# Phase 5 — Add Initial Bookmarks

## Steps

- Add **3–5 frequently used websites**.
- Add them directly into the subscribed toolbar folder.
- WAIT for user confirmation.

## Completion Gate

STOP.

Tell the user:
- “Phase 5 is complete.”
- “Subscribed Toolbar installation is finished.”
- “Return to the page where you started for instructions on integrating Jsonmaker.”

---

## Completion Conditions (ALL REQUIRED)

- Extension installed via direct Chrome Web Store link
- Extension pinned
- Options accessed and sync interval modified
- Subscribed toolbar folder located
- Initial bookmarks added successfully

---

## Notes for Maintainers

- This file is intentionally **command-gated** to reduce AI look-ahead and premature explanation.
- Execution is **user-pulled**, not assistant-pushed.
- Any future phases (e.g., Jsonmaker integration) should live in a **separate instruction file** using the same command protocol.
