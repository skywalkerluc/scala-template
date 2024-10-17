#!/bin/bash

if [ "$1" == "web" ]; then
  bin/main
elif [ "$1" == "consumer" ]; then
  bin/consumer-main
else
  echo "Unknown command. The available commands are: 'web', 'consumer'."
fi
