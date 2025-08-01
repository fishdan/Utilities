#!/bin/sh
# Auto shutdown in 60 seconds unless user cancels in the popup terminal

# Function to run in the new terminal
shutdown_prompt() {
    echo "Power supply disconnected. System will power off in 60 seconds..."
    echo "Type 'stop' and press Enter to cancel shutdown. (Ctrl+C also cancels)"

    for i in $(seq 60 -1 1); do
        printf "\rShutting down in %2d seconds... " "$i"
        read -t 1 user_input && break
    done

    echo
    if [ "$user_input" = "stop" ]; then
        echo "Poweroff canceled by user." | systemd-cat
        exit 0
    else
        systemctl poweroff
    fi
}

# Export the function so the new shell can use it
export -f shutdown_prompt

# Launch in a new terminal window
# Use x-terminal-emulator for portability, fallback to xterm
terminal_cmd=$(command -v x-terminal-emulator || command -v xterm)
$terminal_cmd -e bash -c shutdown_prompt &
