/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zealot.sentinel.extension.slots.block.totalday;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.zealot.sentinel.extension.slots.block.common.JimiRuleConstans;


public class TotalDayFlowRule extends AbstractRule {

    public TotalDayFlowRule() {}

    public TotalDayFlowRule(String resourceName) {
        setResource(resourceName);
    }

    /**
     * 每日限量
     */
    private int count;

    private int controlType = JimiRuleConstans.TOTALDAY_CONTROL_TYPE_DEFAULT;
    
    private TotalDayFlowChecker totalDayFlowChecker;

	@Override
	public boolean passCheck(Context context, DefaultNode node, int count,
			Object... args) {
		
		return totalDayFlowChecker.check(this.getResource(), context, count);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	TotalDayFlowChecker getTotalDayFlowChecker() {
		return totalDayFlowChecker;
	}

	void setTotalDayFlowChecker(TotalDayFlowChecker totalDayFlowChecker) {
		this.totalDayFlowChecker = totalDayFlowChecker;
	}
	
	@Override
    public String toString() {
        return "FlowRule{" +
            "resource=" + getResource() +
            ", limitApp=" + getLimitApp() +
            ", count=" + count +
            ", controlType=" + controlType +
            "}";
    }
}
