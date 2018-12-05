package com.zealot;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

//开启事务支持
@EnableTransactionManagement
@SpringBootApplication
public class StartUp {

	public static void main(String[] args) {

		SpringApplication.run(StartUp.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
		// tomcatFactory.setPort(8081);
		tomcatFactory
				.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
		return tomcatFactory;
	}

	class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
		public void customize(Connector connector) {
			Http11NioProtocol protocol = (Http11NioProtocol) connector
					.getProtocolHandler();
			// 设置最大连接数
			protocol.setMaxConnections(500);
			// 设置最大线程数
			protocol.setMaxThreads(200);
			protocol.setConnectionTimeout(30000);
		}
	}

	//配置json解析的工具为fastjson
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {

		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}
}
