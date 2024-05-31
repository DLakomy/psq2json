#!/bin/bash

# 1. Always read shell scripts you find on the Internet before executing
# 2. This one is not meant to be called directly. See package.json

set -e

pwd=$(pwd)
cd ../..
sbt app/fullOptJS
cd "$pwd"
rm -f dist-prod/*
parcel build index.html --no-cache --dist-dir dist-prod --log-level info --no-source-maps
