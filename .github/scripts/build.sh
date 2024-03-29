#!/usr/bin/env sh

set -e

chmod +x ./gradlew
./gradlew clean assemble
./gradlew check
./gradlew clean bootJar
