#!/bin/bash

set -e

pwd=$(pwd)
cd ../..
sbt app/fullOptJS
cd "$pwd"
rm dist/*
parcel build index.html --no-cache --dist-dir dist --log-level info 
