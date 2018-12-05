package com.zealot.sentinel.datasource.core.bean;

public class SentinelRuleInfo {
    
    //
    private String serviceId;
    //
    private String appId;
    //qps
    private Integer qps;
    
    private Integer totalDay;
    

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getQps() {
        return qps;
    }

    public void setQps(Integer qps) {
        this.qps = qps;
    }

	public Integer getTotalDay() {
		return totalDay;
	}

	public void setTotalDay(Integer totalDay) {
		this.totalDay = totalDay;
	}
    
}
