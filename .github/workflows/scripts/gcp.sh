#!/usr/bin/env sh

set -e

cd /work_dir
echo ${KEY_FILE} | base64 -d > client-secret.json
gcloud auth login --cred-file=client-secret.json 
gcloud config set project ${GOOGLE_PROJECT}
ls -al routine-finder-service/build/libs
#gcloud storage cp spark/historical_load_currencies_exchange_rate.py gs://${LIBRARIES_BUCKET}/spark/historical-rates/historical_load_currencies_exchange_rate.py
