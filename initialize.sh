#!/bin/bash

echo "Generating app artifactory..."
    ./gradlew clean build

echo "Building app docker image..."
    docker build -t playlist-by-weather .

echo "Deploying the application..."
    docker-compose up