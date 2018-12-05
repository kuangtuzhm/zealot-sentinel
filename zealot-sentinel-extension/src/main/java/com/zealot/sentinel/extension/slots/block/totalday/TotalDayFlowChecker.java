package com.zealot.sentinel.extension.slots.block.totalday;

import com.alibaba.csp.sentinel.context.Context;

public interface TotalDayFlowChecker {

	public boolean check(String resource, Context context, int count);
}
