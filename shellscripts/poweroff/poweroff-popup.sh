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
