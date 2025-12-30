# livestream_timer.py
# LibreOffice Writer Python macro: floating stopwatch + word count

import uno
import unohelper
import re
from com.sun.star.awt import XActionListener
from com.sun.star.awt.PosSize import POSSIZE


# --------- module globals (persist while LO session runs) ----------
_g = {
    "dialog": None,
    "timer": None,
    "running": False,
    "elapsed": 0,  # seconds
    "controls": {},
}


def _get_doc():
    return XSCRIPTCONTEXT.getDocument()


def _count_words_fast(text):
    # Basic word counting: sequences of letters/numbers/underscore
    # Good enough for livestreaming; avoids LO stats quirks.
    return len(re.findall(r"\b\w+\b", text, flags=re.UNICODE))


def _format_time(seconds):
    h = seconds // 3600
    m = (seconds % 3600) // 60
    s = seconds % 60
    return f"{h:02d}:{m:02d}:{s:02d}"


def _update_ui():
    dlg = _g["dialog"]
    if not dlg:
        return

    # Update time
    _g["controls"]["lblTime"].Text = _format_time(_g["elapsed"])

    # Update word count
    try:
        doc = _get_doc()
        txt = doc.Text.String
        wc = _count_words_fast(txt)
    except Exception:
        wc = 0

    _g["controls"]["lblWords"].Text = f"{wc} words"


class _BtnListener(unohelper.Base, XActionListener):
    def __init__(self, handler):
        self._handler = handler

    def actionPerformed(self, event):
        self._handler(event)

    def disposing(self, event):
        pass


class _TickListener(unohelper.Base, XActionListener):
    def actionPerformed(self, event):
        if _g["running"]:
            _g["elapsed"] += 1
        _update_ui()

    def disposing(self, event):
        pass


def _toggle_start_pause(_event=None):
    _g["running"] = not _g["running"]
    _g["controls"]["btnStart"].Label = "Pause" if _g["running"] else "Start"


def _reset(_event=None):
    _g["elapsed"] = 0
    _update_ui()


def _close(_event=None):
    try:
        if _g["timer"]:
            _g["timer"].stop()
    except Exception:
        pass

    try:
        if _g["dialog"]:
            _g["dialog"].setVisible(False)
            _g["dialog"].dispose()
    except Exception:
        pass

    _g["dialog"] = None
    _g["timer"] = None
    _g["running"] = False
    _g["elapsed"] = 0
    _g["controls"].clear()


def _create_modeless_dialog():
    ctx = uno.getComponentContext()
    smgr = ctx.ServiceManager

    dlg_model = smgr.createInstanceWithContext(
        "com.sun.star.awt.UnoControlDialogModel", ctx
    )
    dlg_model.Width = 120
    dlg_model.Height = 70
    dlg_model.Title = "Stream Timer"

    def add_label(name, x, y, w, h, text, bold=False):
        m = dlg_model.createInstance("com.sun.star.awt.UnoControlFixedTextModel")
        m.Name = name
        m.PositionX = x
        m.PositionY = y
        m.Width = w
        m.Height = h
        m.Label = text
        if bold:
            try:
                m.FontWeight = 150.0  # semi-bold-ish
            except Exception:
                pass
        dlg_model.insertByName(name, m)

    def add_button(name, x, y, w, h, label):
        m = dlg_model.createInstance("com.sun.star.awt.UnoControlButtonModel")
        m.Name = name
        m.PositionX = x
        m.PositionY = y
        m.Width = w
        m.Height = h
        m.Label = label
        dlg_model.insertByName(name, m)

    add_label("lblTime", 6, 6, 108, 12, "00:00:00", bold=True)
    add_label("lblWords", 6, 20, 108, 10, "0 words")

    add_button("btnStart", 6, 36, 34, 14, "Start")
    add_button("btnReset", 43, 36, 34, 14, "Reset")
    add_button("btnClose", 80, 36, 34, 14, "Close")

    dlg = smgr.createInstanceWithContext("com.sun.star.awt.UnoControlDialog", ctx)
    dlg.setModel(dlg_model)

    # Create peer (THIS makes it modeless/floating)
    toolkit = smgr.createInstanceWithContext("com.sun.star.awt.Toolkit", ctx)
    dlg.createPeer(toolkit, None)
    dlg.setPosSize(200, 200, 0, 0, POSSIZE)  # initial position (x,y)
    dlg.setVisible(True)

    # Grab controls
    _g["controls"]["lblTime"] = dlg.getControl("lblTime")
    _g["controls"]["lblWords"] = dlg.getControl("lblWords")
    _g["controls"]["btnStart"] = dlg.getControl("btnStart")
    _g["controls"]["btnReset"] = dlg.getControl("btnReset")
    _g["controls"]["btnClose"] = dlg.getControl("btnClose")

    # Button listeners
    _g["controls"]["btnStart"].addActionListener(_BtnListener(_toggle_start_pause))
    _g["controls"]["btnReset"].addActionListener(_BtnListener(_reset))
    _g["controls"]["btnClose"].addActionListener(_BtnListener(_close))

    return dlg


def LivestreamTimer(*args):
    """
    Main entry point shown in Tools -> Macros -> Run Macro.
    Opens a floating timer/word count window. Running it again won't create a duplicate.
    """
    if _g["dialog"] is not None:
        # Already open; just bring visible
        try:
            _g["dialog"].setVisible(True)
        except Exception:
            pass
        return

    _g["dialog"] = _create_modeless_dialog()
    _g["elapsed"] = 0
    _g["running"] = False
    _update_ui()

    # Setup the tick timer (1 second)
    ctx = uno.getComponentContext()
    smgr = ctx.ServiceManager
    t = smgr.createInstanceWithContext("com.sun.star.awt.Timer", ctx)
    t.setTimeout(1000)
    t.addActionListener(_TickListener())
    t.start()
    _g["timer"] = t


# Exported macros must be listed here
g_exportedScripts = (LivestreamTimer,)
