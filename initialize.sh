#!/bin/bash

echo "============================="
echo "Generating app artifactory..."
echo "============================="
    ./gradlew clean build

echo "============================"
echo "Building app docker image..."
echo "============================"
    docker build -t playlist-by-weather .

echo "============================"
echo "Deploying the application..."
echo "============================"
    docker-compose up