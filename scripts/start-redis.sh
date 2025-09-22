#!/bin/bash

# Container name for easy reference
CONTAINER_NAME="rankings-redis"

# Check if container already exists
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Redis container already exists. Starting if not running..."
    if [ ! "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        docker start $CONTAINER_NAME
    fi
else
    echo "Creating and starting Redis container..."
    docker run -d \
        --name $CONTAINER_NAME \
        -p 6379:6379 \
        --restart unless-stopped \
        redis:7
fi

echo "Redis is running on localhost:6379"
echo "To stop Redis: docker stop $CONTAINER_NAME"
echo "To view logs: docker logs $CONTAINER_NAME"