package com.zealot.sentinel.datasource.core.exception;

public class DataSourceException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String url;

	public DataSourceException(String msg, Throwable cause)
	{
		super(msg,cause);
	}
	
	public DataSourceException()
	{
		super();
	}
	
	public DataSourceException(String msg)
	{
		super(msg);
	}
	
	public DataSourceException(String msg, String url)
	{
		super(msg);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
