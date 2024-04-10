#!/usr/bin/env sh

set -e

KEY_FILE=$1
GOOGLE_PROJECT=$2
LIBRARIES_BUCKET=$3
ARTIFACT_CLI_PATH=$4
ARTIFACT_SERVICE_PATH=$5
ENV=$6
ROUTINE_FINDER_BUCKET=$7

ARTIFACT_CLI=$(echo $ARTIFACT_CLI_PATH | cut -d '/' -f4)
ARTIFACT_SERVICE=$(echo $ARTIFACT_CLI_PATH | cut -d '/' -f4)

add-apt-repository ppa:openjdk-r/ppa
apt-get -y update
apt-get -y install openjdk-11-jre-headless

echo ${KEY_FILE} | base64 -d > client-secret.json
gcloud auth login --cred-file=client-secret.json 
gcloud config set project ${GOOGLE_PROJECT}
gcloud storage cp ${ARTIFACT_CLI_PATH} gs://${LIBRARIES_BUCKET}/routine-finder-cli/${ARTIFACT_CLI}
gcloud storage cp ${ARTIFACT_SERVICE_PATH} gs://${LIBRARIES_BUCKET}/routine-finder-service/${ARTIFACT_SERVICE}
gcloud app deploy ${ARTIFACT_SERVICE_PATH} --appyaml=".github/scripts/app_config/${ENV}.yaml" --project="${GOOGLE_PROJECT}"
java -jar "${ARTIFACT_CLI_PATH}" import "mindmup/import.json" "${ROUTINE_FINDER_BUCKET}" false
