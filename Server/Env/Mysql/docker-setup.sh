#!/bin/bash
set -e

#查看mysql服务的状态，方便调试，这条语句可以删除

echo '1.启动mysql....'

mysqld --initialize-insecure
nohup mysqld > /dev/null 2>&1 &
sleep 1

echo '2.创建数据库及数据表....'
#创建数据库及数据表
mysql < /mysql/rtc_demo.sql
sleep 1
echo '创建数据库及数据表....'

echo 'mysql容器启动完毕'

while true; do sleep 100; done
