#!/bin/bash

echo 'wait for mysql and redis ready'

sum=0
echo -n "检查mysql"
while :; do
	bash -c "echo > /dev/tcp/mysql_server/3306"
	[ $? -eq 0 ] && echo "检查mysql通过" && echo $? && break
	sleep 1
	echo -n "."
	[ $sum -eq 3 ] && echo "检查mysql未通过" && echo $? && return
	sum=$((sum + 1))
done

sum=0
echo -n "检查redis"
while :; do
	bash -c "echo > /dev/tcp/redis_server/6379"
	[ $? -eq 0 ] && echo "检查redis通过" && break
	sleep 1
	echo -n "."
	[ $sum -eq 3 ] && echo "检查redis未通过" && return
	sum=$((sum + 1))
done


cd /go/src/github.com/volcengine/VolcEngineRTC_Solution_Demo/

sh startserver.sh
