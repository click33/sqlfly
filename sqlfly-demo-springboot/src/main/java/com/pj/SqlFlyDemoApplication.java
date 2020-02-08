package com.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fly.jdbc.SqlFlyFactory;
import com.fly.spring.SqlFlySetup;

@SqlFlySetup	// 加载SqlFLy，一定要加这个注解
@SpringBootApplication
public class SqlFlyDemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SqlFlyDemoApplication.class, args); // 启动 springboot 
		System.out.println(SqlFlyFactory.getSqlFly().getCount("select * from sys_user"));
	}
	
	
}
