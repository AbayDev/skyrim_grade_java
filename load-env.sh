#!/bin/bash
# Bash script to load environment variables from .env file
# Usage: source ./load-env.sh  (or . ./load-env.sh)

ENV_FILE=".env"

if [ ! -f "$ENV_FILE" ]; then
    echo "Error: .env file not found"
    echo "Please copy .env.example to .env and configure your settings"
    return 1 2>/dev/null || exit 1
fi

echo "Loading environment variables from .env file..."

# Read and export variables
set -a
source "$ENV_FILE"
set +a

echo "Environment variables loaded successfully!"
echo "You can now run: ./gradlew run"
