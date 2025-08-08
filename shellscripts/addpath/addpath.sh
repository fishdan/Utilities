#!/bin/bash
# Maintained at https://github.com/fishdan/Utilities/tree/5670406a6eeada2af0f536fe04c957885d534050/shellscripts/addpath

# Get the current directory
CURRENT_DIR=$(pwd)

# Check if it's already in PATH
if [[ ":$PATH:" != *":$CURRENT_DIR:"* ]]; then
    echo "Adding $CURRENT_DIR to PATH in ~/.bashrc"
    echo "export PATH=\"\$PATH:$CURRENT_DIR\"" >> ~/.bashrc
    echo "Sourcing ~/.bashrc to apply changes..."
    source ~/.bashrc
else
    echo "$CURRENT_DIR is already in your PATH."
fi
