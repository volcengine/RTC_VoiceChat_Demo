y#!/usr/bin/env bash

go env -w GOPROXY=https://goproxy.cn,direct
go mod init
go mod tidy

echo 'rtc demo build'
sh build.sh
echo 'complete rtc demo build'

echo 'rtc demo server starting'
sh output/bootstrap.sh
echo 'complete rtc demo server start'