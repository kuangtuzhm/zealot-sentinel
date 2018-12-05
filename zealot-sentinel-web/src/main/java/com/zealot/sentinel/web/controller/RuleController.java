package com.zealot.sentinel.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zealot.sentinel.web.constant.ResponseStatus;
import com.zealot.sentinel.web.entity.SentinelRuleInfo;
import com.zealot.sentinel.web.result.APIResultResponse;
import com.zealot.sentinel.web.service.SentinelRuleService;


@Controller
@RequestMapping(value="/rule")
public class RuleController{
	
	private final static Logger logger = LoggerFactory.getLogger(RuleController.class);

	@Autowired
	private SentinelRuleService sentinelRuleService;

	@RequestMapping(value="/list")
	@ResponseBody
	public APIResultResponse ruleList(Model model, String groupId)
	{
		logger.info("查找groupId="+groupId);
		APIResultResponse result = new APIResultResponse();
		
		try {
			List<SentinelRuleInfo> list = sentinelRuleService.findByGroupId(groupId);
			result.setData(list);
		} catch (Exception e) {
			result.setCode(ResponseStatus.ERROR);
			result.setMsg(e.getMessage());
			logger.error("根据分组["+groupId+"]查询规则错误："+e);
		}
		return result;
	}
	
}
