package com.zealot.sentinel.extension.slots.block.common;

public class JimiRuleConstans {

	//日总量控制方式  默认单机并且内存控制。如果重启应用从头开始限制
	public static final int TOTALDAY_CONTROL_TYPE_DEFAULT = 0;
	
	//日总量控制方式  单机文件控制。
	public static final int TOTALDAY_CONTROL_TYPE_FILE = 1;
	
	//日总量控制方式  单机redis控制。
	public static final int TOTALDAY_CONTROL_TYPE_REDIS = 2;
}
