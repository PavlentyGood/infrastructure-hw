#!/usr/bin/env sh
cd .. &&
./gradlew build &&
docker build . -t people
