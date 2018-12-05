package com.zealot.sentinel.datasource.http;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zealot.sentinel.datasource.core.bean.SentinelRuleInfo;
import com.zealot.sentinel.datasource.core.exception.DataSourceException;
import com.zealot.sentinel.datasource.http.utils.HttpUtils;

public class HttpRulesDataSource {

	
	public static List<SentinelRuleInfo> getAllRulesByGroupId(String url) throws DataSourceException{


		List<SentinelRuleInfo> list = null;
		
        String result = HttpUtils.doGet(url);   
        
        if(StringUtils.isNotBlank(result)){
            com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(result);
            String code = jsonobject.getString("code");
            
//            logger.info("result = " + result);
            if(StringUtils.isNotBlank(code) && "0".equals(code))
            {
            	String data = jsonobject.getString("data");
            	if(StringUtils.isNotBlank(data))
            	{
            		list = JSON.parseArray(data, SentinelRuleInfo.class);
            	}
            }
            else
            {
            	throw new DataSourceException(url+"获取规则服务出错,服务返回code="+code+";msg="+jsonobject.getString("msg"));
            }
        }
        return list;
	}

}
