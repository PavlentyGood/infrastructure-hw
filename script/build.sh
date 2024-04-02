#!/usr/bin/env sh
cd .. &&
./gradlew bootJar &&
docker build . -t people
