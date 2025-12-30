Here’s a clean, GitHub-ready **README.md** you can drop straight into the repo. It’s written for other writers/streamers, not LibreOffice developers.

---

# LibreOffice Writer Livestream Timer

A lightweight **LibreOffice Writer Python macro** that opens a **floating stopwatch + live word count window**, designed for **writing livestreams, sprints, and focus sessions**.

The window behaves like LibreOffice’s Word Count dialog:

* Modeless (you can keep typing)
* Small and unobtrusive
* Easy to capture on OBS or other streaming software

---

## ✨ Features

* ⏱ **Stopwatch-style timer** (HH:MM:SS)
* ✍️ **Live word count**
* ▶️ **Start / Pause**
* 🔄 **Reset**
* ❌ **Close**
* 🪟 Floating window (does not block writing)
* ⚡ Uses LibreOffice’s built-in timer (no threads, no hacks)

---

## 📸 Use Case

Perfect for:

* Writing livestreams (Twitch, YouTube, Kick, etc.)
* Word sprints
* Pomodoro-style sessions
* Accountability writing

Just park the window somewhere visible and capture it in your stream layout.

---

## 📦 Installation

1. Open **LibreOffice Writer**

2. Go to **Tools → Macros → Organize Macros → Python…**

3. Expand **My Macros → user**

4. Create a new file named:

   ```
   livestream_timer.py
   ```

5. Paste the contents of `livestream_timer.py` from this repository

6. Restart LibreOffice (recommended so the macro registers cleanly)

---

## ▶️ Running the Macro

1. Open **Tools → Macros → Run Macro…**

2. Navigate to:

   ```
   My Macros → user → livestream_timer → LivestreamTimer
   ```

3. Click **Run**

The floating timer window will appear.

> Running the macro again will **not** create a duplicate window.

---

## 🧮 Word Count Details

* Word count is calculated using a simple regex (`\b\w+\b`)
* Counts update once per second
* Designed for speed and stability during long sessions

This is intentionally **not** LibreOffice’s internal statistics system to avoid lag or locking during livestreams.

---

## 🖥 Platform Notes

* Works on **Windows, macOS, and Linux**
* Window “always-on-top” behavior depends on your OS/window manager
* Fully compatible with OBS window capture

---

## 🛠 Customization

Easy modifications if you want to tweak it:

* Change update frequency (timer vs word count)
* Adjust window size
* Change font size or layout
* Add hotkeys (e.g. toggle Start/Pause)

Open an issue or PR if you’d like help with a customization.

---

## 📄 License

MIT License
Use it, modify it, stream with it, ship it, steal it.  I don't believe in IP.

---

## ❤️ Credits

Created for writers who want **zero friction tools** while staying focused and live.

If this helped your stream or writing practice, a star on the repo is appreciated.

