#!/bin/bash

# Get the current directory
CURRENT_DIR=$(pwd)

# Check if it's already in PATH
if [[ ":$PATH:" != *":$CURRENT_DIR:"* ]]; then
    echo "Adding $CURRENT_DIR to PATH in ~/.bashrc"
    echo "export PATH=\"\$PATH:$CURRENT_DIR\"" >> ~/.bashrc
    echo "Done! Run 'source ~/.bashrc' to apply changes."
else
    echo "$CURRENT_DIR is already in your PATH."
fi
