package com.zealot.sentinel.datasource.http;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.zealot.sentinel.datasource.core.bean.SentinelRuleInfo;
import com.zealot.sentinel.datasource.core.exception.DataSourceException;
import com.zealot.sentinel.extension.slots.block.authority.JimiAuthorityRuleManager;
import com.zealot.sentinel.extension.slots.block.common.JimiRuleConstans;
import com.zealot.sentinel.extension.slots.block.totalday.TotalDayFlowRule;

public class HttpRulesInitUtil {

	public static void initRules(String url) throws DataSourceException
	{
		//首先获取规则
		List<SentinelRuleInfo> ruleList = HttpRulesDataSource.getAllRulesByGroupId(url);
		initRules(ruleList);
	}
	
	public static void initRules(List<SentinelRuleInfo> ruleList)
	{
		if(null != ruleList && ruleList.size() > 0)
		{
			List<FlowRule> rules = new ArrayList<FlowRule>();
			List<AuthorityRule> authRules = new ArrayList<AuthorityRule>();
			//日访问量限制
			List<TotalDayFlowRule> totalDayRules = new ArrayList<TotalDayFlowRule>();
			for(SentinelRuleInfo info : ruleList)
			{
				String resource = info.getAppId() + ":" + info.getServiceId();
				
				//权限验证
				AuthorityRule rule = new AuthorityRule();
		        //rule.setResource(info.getServiceId());
				rule.setResource(resource);
		        rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
		        rule.setLimitApp(info.getAppId());
		        authRules.add(rule);
				
				//流量限制 = -1表示不限制，所以不设规则
				if(info.getQps() > 0)
				{
					FlowRule rule1 = new FlowRule();
					rule1.setResource(resource);
					// set limit qps to 20
					rule1.setCount(info.getQps());
					rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
					rule1.setLimitApp(info.getAppId());
	
					rules.add(rule1);
				}
				
				if(info.getTotalDay() > 0)
				{
					TotalDayFlowRule r = new TotalDayFlowRule();
					r.setCount(info.getTotalDay());
					r.setLimitApp(info.getAppId());
					r.setResource(resource);
					//内存缓存访问量，缺点是服务重启后需要
					r.setControlType(JimiRuleConstans.TOTALDAY_CONTROL_TYPE_DEFAULT);
					
					totalDayRules.add(r);
				}
			}
			
			FlowRuleManager.loadRules(rules);
			JimiAuthorityRuleManager.loadRules(authRules);
		}
	}
}
