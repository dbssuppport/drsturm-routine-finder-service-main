#!/usr/bin/env sh

set -e

VERSION=$1

chmod +x ./gradlew
./gradlew clean assemble
./gradlew check
./gradlew clean bootJar -Pversion=${VERSION}
