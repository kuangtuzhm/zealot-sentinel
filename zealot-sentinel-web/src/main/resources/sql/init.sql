CREATE TABLE `sentinel_rule_config` (
  `groupId` varchar(50) NOT NULL COMMENT '提供服务的项目',
  `serviceId` varchar(50) NOT NULL COMMENT '接口(服务)名',
  `appId` varchar(50) NOT NULL COMMENT '接受服务的appId',
  `qps` int(11) DEFAULT '-1' COMMENT 'qps',
  `total_day` int(11) DEFAULT '-1' COMMENT '日限量',
  `enable_flag` int(11) DEFAULT '1' COMMENT '1:启用 0:禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`groupId`,`serviceId`,`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
