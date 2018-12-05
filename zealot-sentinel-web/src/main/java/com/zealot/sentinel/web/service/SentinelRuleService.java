package com.zealot.sentinel.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zealot.sentinel.web.entity.SentinelRuleInfo;
import com.zealot.sentinel.web.mapper.SentinelRuleMapper;

@Service
public class SentinelRuleService {
	
	@Autowired
	private SentinelRuleMapper sentinelRuleMapper;

	public List<SentinelRuleInfo> findByGroupId(String groupId)
	{
		List<SentinelRuleInfo> list = sentinelRuleMapper.findByGroupId(groupId);
		return list;
	}
}
