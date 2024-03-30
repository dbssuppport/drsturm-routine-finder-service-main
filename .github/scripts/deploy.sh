#!/usr/bin/env sh

set -e

KEY_FILE=$1
GOOGLE_PROJECT=$2
LIBRARIES_BUCKET=$3
ARTIFACT_CLI=$4
ARTIFACT_SERVICE=$5

echo ${KEY_FILE} | base64 -d > client-secret.json
gcloud auth login --cred-file=client-secret.json 
gcloud config set project ${GOOGLE_PROJECT}
gcloud storage cp ${ARTIFACT_CLI} gs://${LIBRARIES_BUCKET}/routine-finder-cli/${ARTIFACT_CLI}
gcloud storage cp ${ARTIFACT_SERVICE} gs://${LIBRARIES_BUCKET}/routine-finder-service/${ARTIFACT_SERVICE}
