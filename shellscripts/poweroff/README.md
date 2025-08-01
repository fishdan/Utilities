# Auto Power-Off on Power Supply Disconnect (with Cancel Option)

This setup automatically powers off the system when the power supply is disconnected, **unless the user cancels within 60 seconds**.  

It is designed to work even when triggered by **udev** (which normally cannot display interactive popups) by using **systemd user services** to show a GUI or terminal prompt.

---

## Components

1. **udev Rule** – Detects power supply disconnection  
   - `/etc/udev/rules.d/99-poweroff.rules`

2. **Root Trigger Script** – Called by udev, launches the user service  
   - `/usr/local/bin/poweroff-trigger.sh`

3. **User Popup Script** – Runs in the logged-in user session, shows a 60s countdown and allows cancellation  
   - `/usr/local/bin/poweroff-popup.sh`

---

## ⚙️ Installation

### 1. Create the udev rule

Create `/etc/udev/rules.d/99-poweroff.rules`:

```
SUBSYSTEM=="power_supply", ATTR{online}=="0", RUN+="/usr/local/bin/poweroff-trigger.sh"
```

> Adjust the `SUBSYSTEM` and `ATTR` if needed for your hardware.  
> Check with:
> ```
> udevadm monitor --environment --udev
> ```

Reload udev rules:

```
sudo udevadm control --reload
```

---

### 2. Install the trigger script

Create `/usr/local/bin/poweroff-trigger.sh`:

```
#!/bin/sh
# Trigger user notification via systemd

USER=$(who | awk 'NR==1 {print $1}')
DISPLAY=":0"

sudo -u $USER DISPLAY=$DISPLAY DBUS_SESSION_BUS_ADDRESS="unix:path=/run/user/$(id -u $USER)/bus" \
    systemd-run --user --unit=poweroff-popup --no-block \
    /usr/local/bin/poweroff-popup.sh
```

Make it executable:

```
sudo chmod +x /usr/local/bin/poweroff-trigger.sh
```

---

### 3. Install the popup script

Create `/usr/local/bin/poweroff-popup.sh`:

```
#!/bin/bash
# 60-second countdown shutdown unless canceled

if command -v zenity >/dev/null 2>&1; then
    # GUI popup
    zenity --question \
        --timeout=60 \
        --default-cancel \
        --title="Power Supply Disconnected" \
        --text="The system will automatically power off in 60 seconds.
Click 'Cancel' to stop shutdown."
    result=$?
else
    # Terminal fallback with countdown
    gnome-terminal -- bash -c '
        echo "Power supply disconnected. Auto shutdown in 60s unless canceled."
        echo "Type stop to abort:"
        for i in $(seq 60 -1 1); do
            printf "\rShutting down in %2d seconds... " "$i"
            read -t 1 input && break
        done
        echo
        if [ "$input" = "stop" ]; then
            exit 1
        fi
    '
    result=$?
fi

# 0 = OK clicked, 5 = timeout, 1 = cancel
if [ "$result" -eq 1 ]; then
    echo "Shutdown canceled by user." | systemd-cat
else
    systemctl poweroff
fi
```

Make it executable:

```
sudo chmod +x /usr/local/bin/poweroff-popup.sh
```

---

## ✅ Behavior

- When AC power is disconnected:
  1. **udev** triggers `poweroff-trigger.sh`.
  2. A **systemd user service** starts `poweroff-popup.sh` in the GUI session.
  3. User sees:
     - A **Zenity popup** (if available), or  
     - A **terminal window with countdown**  
  4. If the user **clicks Cancel** or **types `stop`**, shutdown is aborted.
  5. If **no input** within 60 seconds, system powers off.

---

## 🔹 Notes

- Requires **systemd**, **zenity** (optional for GUI), and a terminal emulator like `gnome-terminal`.  
- If your system uses a different terminal, update the `gnome-terminal` command.  
- Logs are sent to `systemd-cat` and visible with:
  ```
  journalctl -t poweroff-popup.sh
  ```
- This approach ensures **interactive shutdown cancellation works even when triggered by udev**.

---

## 🛠️ Testing

You can simulate the trigger without unplugging power:

```
sudo /usr/local/bin/poweroff-trigger.sh
```

---

## 🔒 Safety

- The script **only powers off** if no user cancels.  
- **Timeout** ensures the system does not stay on without power indefinitely.  
- Suitable for **UPS / embedded / kiosk systems** where unattended shutdown is preferred.

