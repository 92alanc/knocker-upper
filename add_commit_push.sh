# Adds, commits and pushes all changes
# Author: Alan Camargo

#!/bin/bash

if [ "$#" != 1 ]; then
    echo "You must provide the commit message."
    exit
fi

COMMIT_MESSAGE=$1

git add .
echo "Added all changes."

git commit -m "${COMMIT_MESSAGE}"

git push

