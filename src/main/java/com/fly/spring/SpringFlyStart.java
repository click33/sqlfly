package com.fly.spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fly.jdbc.aop.FlyAop;
import com.fly.jdbc.cfg.FlyConfig;
import com.fly.jdbc.cfg.FlyObjects;
import com.fly.jdbc.paging.FlyPaging;


/**
 * 与SpringBoot集成, 保证此类被扫描，即可完成SqlFly与SpringBoot的集成 
 * @author kongyongshun
 *
 */
@Component
@Configuration
public class SpringFlyStart {

	// 读取配置信息 FlyConfig 
	@Bean
	@ConfigurationProperties(prefix = "spring.sqlfly")
	public FlyConfig getFlyConfigBean() {
		return new FlyConfig();
	}
	
	// 自动注入 FlyConfig 
	@Autowired
	public void setFlyConfig(FlyConfig config){
		FlyObjects.setConfig(config); 
	}

	// 注入分页工具类 
	@Autowired(required = false)
	public void setPaging(FlyPaging paging){
		FlyObjects.setPaging(paging);
	}
	
	// 注入分页aop实现 
	@Autowired(required = false)
	public void setAop(FlyAop aop){
		FlyObjects.setAop(aop);
	}
	
	// 注入连接池 
	@Autowired(required = false)
	public void setDataSource(DataSource dataSource){
		FlyObjects.setDataSource(dataSource);
	}
	
	
}
