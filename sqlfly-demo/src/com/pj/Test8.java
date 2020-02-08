package com.pj;

import org.junit.Test;

import com.fly.jdbc.SqlFlyFactory;
import com.fly.jdbc.cfg.FlyConfig;
import com.fly.jdbc.cfg.FlyObjects;

// 测试8，框架配置
public class Test8 {

	// 测试8-1，代码配置 
	@Test
	public void test8_1() {
		// 1、构建配置信息 
		FlyConfig config = new FlyConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setUrl("jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8");
		config.setUsername("root");
		config.setPassword("root123456");
		FlyObjects.setConfig(config);	// 注入到框架中
		
		System.out.println(SqlFlyFactory.getSqlFly().getConnection());	// 测试
	}
	
	
	
	
}
