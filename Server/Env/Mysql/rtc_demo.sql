USE `mysql`;
-- set root's password to bytedance
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'bytedance';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bytedance';
FlUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS `rtc_demo_db`;

USE `rtc_demo_db`;

DROP TABLE IF EXISTS `svc_room`;
CREATE TABLE `svc_room`
(
    `id`                             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `app_id`                         varchar(100)             DEFAULT NULL COMMENT '直播app_ID',
    `room_id`                        varchar(100)             DEFAULT NULL COMMENT '直播间room_id',
    `room_name`                      varchar(200)             DEFAULT NULL COMMENT '直播间名称',
    `host_user_id`                   varchar(100)             DEFAULT NULL COMMENT '主播id',
    `host_user_name`                 varchar(200)             DEFAULT NULL COMMENT '主播名字',
    `status`                         int(11)                  DEFAULT NULL COMMENT '直播间状态',
    `enable_audience_interact_apply` int(11)                  DEFAULT NULL COMMENT '是否启用申请管理',
    `create_time`                    timestamp           NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                    timestamp           NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `start_time`                     bigint(20)               DEFAULT NULL COMMENT '开始时间戳',
    `finish_time`                    bigint(20)               DEFAULT NULL COMMENT '结束时间戳',
    `ext`                            varchar(200)             DEFAULT NULL COMMENT '拓展字段',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_app_room_id` (`app_id`, `room_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 245
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='语聊房直播间信息';

DROP TABLE IF EXISTS `svc_user`;
CREATE TABLE `svc_user`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `app_id`          varchar(100)             DEFAULT NULL COMMENT '直播间app_id',
    `room_id`         varchar(100)             DEFAULT NULL COMMENT '直播间roomid',
    `user_id`         varchar(255)             DEFAULT NULL COMMENT 'userid',
    `user_name`       varchar(255)             DEFAULT NULL COMMENT '用户昵称',
    `user_role`       int(11)                  DEFAULT NULL COMMENT '用户角色， 1：主播，2：观众',
    `net_status`      int(11)                  DEFAULT NULL COMMENT '用户网络状态',
    `interact_status` int(11)                  DEFAULT NULL COMMENT '用户互动状态',
    `seat_id`         int(11)                  DEFAULT '0' COMMENT '麦位id',
    `mic`             tinyint(4)               DEFAULT '0' COMMENT '麦克风状态',
    `create_time`     timestamp           NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp           NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `join_time`       bigint(20)               DEFAULT NULL COMMENT '加入时间戳',
    `leave_time`      bigint(20)               DEFAULT NULL COMMENT '离开时间戳',
    `device_id`       varchar(128)             DEFAULT NULL COMMENT 'device_id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_room_id_user_id` (`room_id`, `user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 581
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='语聊房用户信息';


DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`
(
    `id`         bigint(20) unsigned                             NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
    `user_id`    varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'user id',
    `user_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'user name',
    `created_at` timestamp                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `updated_at` timestamp                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14115
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='user profile information';