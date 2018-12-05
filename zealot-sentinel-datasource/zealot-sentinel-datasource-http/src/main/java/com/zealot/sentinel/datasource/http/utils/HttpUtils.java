package com.zealot.sentinel.datasource.http.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.zealot.sentinel.datasource.core.exception.DataSourceException;

/**
 * Http工具类
 */
public class HttpUtils {

    /**
     * 发送POST请求
     * @param url 请求url
     * @param data 请求数据
     * @return 结果
     */
    public static String doGet(String url) throws DataSourceException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000).setConnectTimeout(20000)
                .setConnectionRequestTimeout(10000).build();
        httpGet.setConfig(requestConfig);

        // 设置回调接口接收的消息头
        httpGet.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        String context = null;
        try {
            response = httpClient.execute(httpGet);
//            System.out.println(response.getStatusLine());
            if(response.getStatusLine().getStatusCode() == 200)
            {
	            HttpEntity entity = response.getEntity();
	            context = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            else
            {
            	throw new DataSourceException(url+"获取规则失败:返回网络请求错误码="+response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new DataSourceException(url+"获取规则失败:",e);
        } finally {
            try {
            	if(response != null)
            	{
            		response.close();
            	}
                httpGet.abort();
                httpClient.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return context;
    }

    /**
     * 解析出url参数中的键值对
     * @param url url参数
     * @return 键值对
     */
    public static Map<String, String> getRequestParam(String url) {

        Map<String, String> map = new HashMap<String, String>();
        String[] arrSplit = null;

        // 每个键值为一组
        arrSplit = url.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                map.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    map.put(arrSplitEqual[0], "");
                }
            }
        }
        return map;
    }
}