#!/usr/bin/env bash
RUN_NAME="VolcEngineRTC_Solution_Demo"

set -e
if [ -d "output" ]; then
  rm -rf output
  mkdir output
fi


go build -o output/bin/${RUN_NAME} ./cmd

mkdir -p output/conf
cp conf/* output/conf
cp bootstrap.sh output/
chmod +x output/bootstrap.sh
chmod +x output/bin/${RUN_NAME}