package com.zealot.sentinel.extension.slots.block.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateUtil {
	
	/* 默认日期格式 */
	private static String format= "yyyy-MM-dd";

	/**
	 * 获得当前时间字符串，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间字符串
	 */
	public static String getCurrentTimeAsString() {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
}
