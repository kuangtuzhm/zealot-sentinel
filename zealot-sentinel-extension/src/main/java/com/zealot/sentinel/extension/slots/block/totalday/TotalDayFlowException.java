package com.zealot.sentinel.extension.slots.block.totalday;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class TotalDayFlowException extends BlockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer count;

	public TotalDayFlowException(String ruleLimitApp, int count) {

        super(ruleLimitApp);
        this.count = count;
    }

    public TotalDayFlowException(String message, int count, Throwable cause) {
        super(message, cause);
        this.count = count;
    }

    public TotalDayFlowException(String ruleLimitApp, int count, String message) {
        super(ruleLimitApp, message);
        this.count = count;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    
    
}
