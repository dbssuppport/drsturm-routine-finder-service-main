#!/usr/bin/env sh

set -e

KEY_FILE=$1
GOOGLE_PROJECT=$2
LIBRARIES_BUCKET=$3
ARTIFACT=$4

echo ${KEY_FILE} | base64 -d > client-secret.json
gcloud auth login --cred-file=client-secret.json 
gcloud config set project ${GOOGLE_PROJECT}
