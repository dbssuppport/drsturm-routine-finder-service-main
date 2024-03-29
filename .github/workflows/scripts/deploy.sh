#!/usr/bin/env sh

set -e

CURDIR=$(pwd)
KEY_FILE=$1
GOOGLE_PROJECT=$2

echo "CURDIR=$CURDIR"
echo "GOOGLE_PROJECT=$GOOGLE_PROJECT"

docker run --name gcp_cli -v ${CURDIR}:/work_dir --env KEY_FILE=$KEY_FILE --env GOOGLE_PROJECT=$GOOGLE_PROJECT google/cloud-sdk:428.0.0-slim sh -c "cd /work_dir && echo ${KEY_FILE} | base64 -d > client-secret.json && gcloud auth login --cred-file=client-secret.json && gcloud config set project ${GOOGLE_PROJECT}"
docker rm --force gcp_cli
