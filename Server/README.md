# rtc_demo_opensource

# 项目部署

## 准备工作

### 火山引擎

在开始之前，需要先在火山引擎创建账号，创建应用。获取AppID/AppKey、Ak/Sk。

获取AppID/AppKey：

- 说明文档：https://www.volcengine.com/docs/6348/69865

获取Ak/Sk：

- 说明文档：https://www.volcengine.com/docs/6291/65568

- 获取途径：火山引擎->控制台->密钥管理 https://console.volcengine.com/iam/keymanage/



### 配置文件

> 如果目的是运行起来，不对业务逻辑和部署方式做定制化修改，只需要看业务服务一节，根据说明修改字段。

#### 业务服务

配置文件路径：./VolcEngineRTC_Solution_Demo/conf/conf.yaml

```YAML
mysql_dsn: "root:bytedance@tcp(mysql_server:3306)/rtc_demo_db?parseTime=true&loc=Local"
redis_addr: "redis_server:6379"
redis_password: ""
addr: "0.0.0.0"
port: "18080"
server_url: "http://your_ip:18080/rtc_demo/rtm" # rtm回调地址，ip需要做替换
cs_app_id: your_app_id
cs_app_key: your_app_key
cs_experience_time: 20 # # 房间体验时长(min)，超时自动结束。
svc_app_id: your_app_id
svc_app_key: your_app_key
svc_experience_time: 20 # # 房间体验时长(min)，超时自动结束。
vi_app_id: your_app_id
vi_app_key: your_app_key
vi_experience_time: 20 # # 房间体验时长(min)，超时自动结束。
ktv_app_id: your_app_id
ktv_app_key: your_app_key
ktv_experience_time: 20 # # 房间体验时长(min)，超时自动结束。
volc_ak: your_ak # openapi鉴权使用
volc_sk: your_sk # openapi鉴权使用
```

**参数解释**

1. mysql_dsn：如果docker模式启动，使用如上参数。否则替换为自己的dsn。
2. redis_addr：如果docker模式启动，使用如上参数。否则替换为自己的addr。
3. port：gin：框架监听端口。默认为0.0.0.0:18080，可自己配置(注意不要丢失冒号:)。
4. server_url：url需要替换成自己的服务器ip:port或url。path不需要修改。本例中可将 your_ip 修改为 127.0.0.1
5. xx_app_id：需要替换为火山引擎申请的appID，xx代指场景名称。
6. xx_app_key：需要替换为火山引擎申请的appKey。
7. xx_experience_time：房间体验时长，超时自动结束。默认为20min，可自己配置。
8. volc_ak：需要替换为火山引擎申请的AK。
9. volc_sk：需要替换为火山引擎申请的SK。

**说明**

如果使用提供的docker-compose启动，server_url中your_ip需要替换为宿主机ip，mysql_dsn和redis_addr不要修改，其它参数修改为已申请的相应值。

如果自己在本地编译运行，自己搭建mysql及redis服务器，参数按照实际情况进行修改。



## Linux 部署

### 前置条件

需要 Linux 环境，并使用包管理例如 apt、yum 等。

本例演示 Ubuntu 22.04.1 LTS，使用 apt 的部署流程。



### 准备工作

#### 安装前置软件

> 开始之前请获取 root 权限或自行在指令前添加 sudo。

```Shell
#安装前置软件
apt update && apt install wget git software-properties-common vim -y
```





#### 安装 golang

> 开始之前请获取 root 权限或自行在指令前添加 sudo。
>

```Shell
#安装 go
wget https://go.dev/dl/go1.19.3.linux-amd64.tar.gz && rm -rf /usr/local/go && tar -C /usr/local -xzf go1.19.3.linux-amd64.tar.gz

#检查 go 是否成功安装
go version
```



#### 安装 mysql

> 开始之前请获取 root 权限或自行在指令前添加 sudo。
>

```Shell
#下载 mysql-apt-config
wget https://dev.mysql.com/get/mysql-apt-config_0.8.24-1_all.deb

#选择 apt 源
#选择 MySQL Server & Cluster 选项，选定 mysql-5.7 并确认 ok。
dpkg -i mysql-apt-config_0.8.24-1_all.deb

#更新 apt 源
#如果出现 signature couldnt be verified 请运行以下语句，并重新更新 apt 源
#apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 467B942D3A79BD29
apt update

#检查 msyql 可安装版本
apt-cache policy mysql-server

#安装 mysql
apt install mysql-server=5.7* -y

#启动 mysql 服务
service msyql start
```



#### 安装 redis

> 开始之前请获取 root 权限或自行在指令前添加 sudo。
>

```Shell
#更新 apt 源
apt update

#安装 redis
apt install redis

#启动 redis-server，并开启后台运行
#（可选，如提示端口已占用说明已自动运行）
#开启 redis-server 后通过 Ctrl + C 返回 Shell
redis-server &
```



### 配置文件

> 如果目的是运行起来，不对业务逻辑和部署方式做定制化修改，只需要看业务服务一节，根据说明修改字段。

#### mysql

rtc_demo.sql中包含了建库、建表、配置密码等操作，如果要修改mysql密码，需同步修改业务服务配置中的mysql_dsn机rtc_demo.sql文件中的如下语句。

配置文件目录：./Mysql/rtc_demo.sql

```SQL
USE `mysql`;
-- set root's password to bytedance
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'bytedance';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bytedance';
FlUSH PRIVILEGES;
```

将 sql 文件导入 mysql 中

```Shell
#当前目录在 Server 下
mysql < ./Env/Mysql/rtc_demo.sql
```



### 启动

在 Server 目录下，运行如下命令

```Shell
sh startserver.sh
```



## Docker 部署

### 前置条件

需要准备docker环境。

windows、mac建议安装[docker desktop](https://www.docker.com/products/docker-desktop)(包含docker-compose)，linux安装完docker需要额外安装docker-compose。



### 配置文件

> 如果目的是运行起来，不对业务逻辑和部署方式做定制化修改，只需要看业务服务一节，根据说明修改字段。

#### Docker

docker配置文件包括docker-compose.yml和业务服务及mysql的Dockerfile，redis使用官方镜像。

其中两个Dockerfile主要为copy文件和执行启动脚本，没有配置相关部分。业务服务配置如上节所示，mysql配置如下节所示。

配置文件目录：./docker-compose.yml

```YAML
version: '3'
services:
  mysql_server:
    container_name: mysql_server
    build: Env/Mysql
    restart: always
      
  redis_server:
    container_name: redis_server
    image: "redis:latest"
    restart: always


  rtc_demo_server:
    container_name: rtc_demo_server
    build: ./
    depends_on:
      - mysql_server
      - redis_server
    links:
      - "mysql_server:mysql_server"
      - "redis_server:redis_server"
    ports:
      - "18080:18080"
    restart: always
```

说明：

1. mysql_server的volumes为数据映射，默认映射到./app/mysql，可修改。

1. rtc_demo_server中ports映射了18080端口，如果修改了业务服务配置中的http端口，这里要做相应修改。



#### mysql

rtc_demo.sql中包含了建库、建表、配置密码等操作，如果要修改mysql密码，需同步修改业务服务配置中的mysql_dsn机rtc_demo.sql文件中的如下语句。

配置文件目录：./Env/Mysql/rtc_demo.sql

```SQL
USE `mysql`;
-- set root's password to bytedance
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'bytedance';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bytedance';
FlUSH PRIVILEGES;
```



### 启动

1. 在当前目录下(Server)执行如下命令，等待创建镜像及启动服务

```Shell
docker-compose up -d
```



## 验证

服务启动后，查看对应的日志，出现Listening and serving HTTP on 0.0.0.0:18080则启动成功。

![img](https://bytedance.feishu.cn/space/api/box/stream/download/asynccode/?code=MWM4ODE2MTU0YmYwYmRjZDBiODkzNWM4MTU4ZDkwZWJfaTk3ZDh1ZTVLaWxueVRIMWs3ZTNXSFpkS0lYell2b1lfVG9rZW46Ym94Y245aElib21KQ0Jjdzk0V0pwUGRBOXdlXzE2NjcyODkxMTI6MTY2NzI5MjcxMl9WNA)

调用passwordFreeLogin接口，如果有正常返回则服务启动成功(url中ip:port需替换为宿主机ip:port)。

```Shell
curl --location --request POST 'http://127.0.0.1:18080/rtc_demo/login' \
--header 'Content-Type: application/json' \
--data-raw '{
   "event_name":"passwordFreeLogin",
   "device_id":"123456",
   "content":"{\"user_name\":\"bytedance\"}"
}
'
```

以上两步完成后，说明服务正常启动。接下来就可以使用客户端进行接口调试了。



## 其他说明

### 日志

日志所在文件夹 ./app.log

日志格式：

time="2021-12-31T15:35:14+08:00" level=info msg="get login userID: 818140009019" Location="user.go:49" LogID=75119c42-3a98-4533-a3f7-d2b8468c03f6

常用方式：

1. 使用LogID标识一个请求的调用链条