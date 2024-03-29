#!/usr/bin/env sh

set -e

CURDIR=$(pwd)
KEY_FILE=$1
GOOGLE_PROJECT=$2

echo "CURDIR=$CURDIR"
echo "GOOGLE_PROJECT=$GOOGLE_PROJECT"

docker run --name gcp_cli -v ${CURDIR}:/work_dir --env KEY_FILE=$KEY_FILE --env GOOGLE_PROJECT=$GOOGLE_PROJECT google/cloud-sdk:428.0.0-slim sh -c "cd /work_dir && chmod +x .github/scripts/gcp.sh && ./.github/scripts/gcp.sh"
docker rm --force gcp_cli
