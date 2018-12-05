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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.property.DynamicSentinelProperty;
import com.alibaba.csp.sentinel.property.PropertyListener;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.zealot.sentinel.extension.slots.block.common.JimiRuleConstans;
import com.zealot.sentinel.extension.slots.block.totalday.checker.DefaultTotalDayFlowChecker;
import com.zealot.sentinel.extension.slots.block.totalday.checker.FileTotalDayFlowChecker;
import com.zealot.sentinel.extension.slots.block.totalday.checker.RedisTotalDayFlowChecker;

/**
 * Manager for frequent ("hot-spot") parameter flow rules.
 * 
 * @author jialiang.linjl
 * @author Eric Zhao
 * @since 0.2.0
 */
public class TotalDayFlowRuleManager {

	private static final Map<String, List<TotalDayFlowRule>> totalDayFlowRules = new ConcurrentHashMap<String, List<TotalDayFlowRule>>();

	private final static TotalDayFlowPropertyListener PROPERTY_LISTENER = new TotalDayFlowPropertyListener();
	
	private static SentinelProperty<List<TotalDayFlowRule>> currentProperty = new DynamicSentinelProperty<List<TotalDayFlowRule>>();

	static {
		currentProperty.addListener(PROPERTY_LISTENER);
	}

	/**
	 * Load parameter flow rules. Former rules will be replaced.
	 * 
	 * @param rules
	 *            new rules to load.
	 */
	public static void loadRules(List<TotalDayFlowRule> rules) {
		try {
			currentProperty.updateValue(rules);
		} catch (Throwable e) {
			RecordLog.info("[TotalDayFlowRuleManager] Failed to load rules", e);
		}
	}

	/**
	 * @param property
	 *            the property to listen
	 */
	public static void register2Property(
			SentinelProperty<List<TotalDayFlowRule>> property) {
		synchronized (PROPERTY_LISTENER) {
			currentProperty.removeListener(PROPERTY_LISTENER);
			property.addListener(PROPERTY_LISTENER);
			currentProperty = property;
			RecordLog
					.info("[TotalDayFlowRuleManager] New property has been registered to Total day rule manager");
		}
	}

	public static List<TotalDayFlowRule> getRulesOfResource(String resourceName) {
		return totalDayFlowRules.get(resourceName);
	}

	public static boolean hasRules(String resourceName) {
		List<TotalDayFlowRule> rules = totalDayFlowRules.get(resourceName);
		return rules != null && !rules.isEmpty();
	}

	/**
	 * Get a copy of the rules.
	 * 
	 * @return a new copy of the rules.
	 */
	public static List<TotalDayFlowRule> getRules() {
		List<TotalDayFlowRule> rules = new ArrayList<TotalDayFlowRule>();
		for (Map.Entry<String, List<TotalDayFlowRule>> entry : totalDayFlowRules
				.entrySet()) {
			rules.addAll(entry.getValue());
		}
		return rules;
	}

	private static final class TotalDayFlowPropertyListener implements
			PropertyListener<List<TotalDayFlowRule>> {

		@Override
		public void configUpdate(List<TotalDayFlowRule> list) {
			Map<String, List<TotalDayFlowRule>> rules = aggregateHotParamRules(list);
			if (rules != null) {
				totalDayFlowRules.clear();
				totalDayFlowRules.putAll(rules);
			}
			RecordLog
					.info("[TotalDayFlowRuleManager] Total day flow rules received: "
							+ totalDayFlowRules);
		}

		@Override
		public void configLoad(List<TotalDayFlowRule> list) {
			Map<String, List<TotalDayFlowRule>> rules = aggregateHotParamRules(list);
			if (rules != null) {
				totalDayFlowRules.clear();
				totalDayFlowRules.putAll(rules);
			}
			RecordLog
					.info("[TotalDayFlowRuleManager] Total day flow rules received: "
							+ totalDayFlowRules);
		}

		private Map<String, List<TotalDayFlowRule>> aggregateHotParamRules(
				List<TotalDayFlowRule> list) {
			Map<String, List<TotalDayFlowRule>> newRuleMap = new ConcurrentHashMap<String, List<TotalDayFlowRule>>();
			
			if (list == null || list.isEmpty()) {
	            return newRuleMap;
	        }
			
			for (TotalDayFlowRule rule : list) {
				if (!isValidRule(rule)) {
					RecordLog
							.warn("[TotalDayFlowRuleManager] Ignoring invalid rule when loading new rules: "
									+ rule);
					continue;
				}

				if (StringUtil.isBlank(rule.getLimitApp())) {
					rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
				}
				
				TotalDayFlowChecker checker = null;
				switch(rule.getControlType())
				{
					case JimiRuleConstans.TOTALDAY_CONTROL_TYPE_DEFAULT:
						{
							checker = new DefaultTotalDayFlowChecker(rule.getCount());
						}
						break;
					case JimiRuleConstans.TOTALDAY_CONTROL_TYPE_FILE:
						//写文件的方式暂时未实现
						checker = new FileTotalDayFlowChecker(rule.getCount());
						break;
					case JimiRuleConstans.TOTALDAY_CONTROL_TYPE_REDIS:
						//写redis的方式暂时未实现
						checker = new RedisTotalDayFlowChecker(rule.getCount());
						break;	
					default:
						{
							checker = new DefaultTotalDayFlowChecker(rule.getCount());
						}
						break;
				}

				rule.setTotalDayFlowChecker(checker);

				String resourceName = rule.getResource();
				List<TotalDayFlowRule> ruleList = newRuleMap.get(resourceName);
				if (ruleList == null) {
					ruleList = new ArrayList<TotalDayFlowRule>();
					newRuleMap.put(resourceName, ruleList);
				}
				ruleList.add(rule);
			}
			return newRuleMap;
		}
	}

	public static void checkFlow(ResourceWrapper resource, Context context,
			DefaultNode node, int count) throws BlockException {
		List<TotalDayFlowRule> rules = getRulesOfResource(resource.getName());
		if (rules != null) {
			for (TotalDayFlowRule rule : rules) {
				if (!rule.passCheck(context, node, count)) {
					throw new FlowException(rule.getLimitApp());
				}
			}
		}
	}

	static boolean isValidRule(TotalDayFlowRule rule) {
		return rule != null && !StringUtil.isBlank(rule.getResource())
				&& rule.getCount() >= 0;
	}

	private TotalDayFlowRuleManager() {
	}
}
