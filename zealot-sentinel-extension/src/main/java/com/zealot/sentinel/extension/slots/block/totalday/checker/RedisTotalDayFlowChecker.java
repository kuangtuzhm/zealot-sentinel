package com.zealot.sentinel.extension.slots.block.totalday.checker;

import com.alibaba.csp.sentinel.context.Context;
import com.zealot.sentinel.extension.slots.block.common.SimpleDateUtil;
import com.zealot.sentinel.extension.slots.block.totalday.TotalDayFlowChecker;
import com.zealot.sentinel.extension.slots.block.totalday.TotalDayFlowRule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisTotalDayFlowChecker implements TotalDayFlowChecker {
	
	//日统计量因为资源不会特别多，此map可以考虑不清理，如果资源量多的情况，需要考虑清理过期的资源
	private static Map<String, AtomicInteger> map = new HashMap<>();
	
	private static final byte [] synConnFlag = new byte[0];
	
	private int totalDayCount;
	
	public RedisTotalDayFlowChecker(int totalDayCount)
	{
		this.totalDayCount = totalDayCount;
	}

	@Override
	public boolean check(String resource, Context context, int count) {
		//获取当日日期
		String dateStr = SimpleDateUtil.getCurrentTimeAsString();
		//资源key
		StringBuilder sb = new StringBuilder(dateStr);
		sb.append("_").append(resource);
		String key = sb.toString();
		
		
		return true;
	}

}
