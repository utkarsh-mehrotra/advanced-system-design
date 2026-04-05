#!/bin/bash

# Navigate to the script's directory
cd "$(dirname "$0")"

# Colors for output
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}Setting up LLD Demo Runner Environment...${NC}"

# Check if venv exists, if not create it
if [ ! -d ".venv" ]; then
    echo "Creating Python Virtual Environment..."
    python3 -m venv .venv
fi

# Activate venv
source .venv/bin/activate

# Install requirements
echo "Installing/Updating dependencies..."
pip install -r requirements.txt -q

echo -e "\n${GREEN}Booting Interactive Terminal Wizard...${NC}\n"

# Run the python script
python3 run_demo.py

# Deactivate venv after exit
deactivate
