package com.pj;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

// 配置 druid 相关信息
@Configuration
public class DruidConfig {
	// 返回druid的DataSource
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource druid() {
		return new DruidDataSource();
	}
}
