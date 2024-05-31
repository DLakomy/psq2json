#!/bin/bash

set -e

pwd=$(pwd)
cd ../..
sbt app/fullOptJS
cd "$pwd"
rm -f dist-prod/*
parcel build index.html --no-cache --dist-dir dist-prod --log-level info --no-source-maps
