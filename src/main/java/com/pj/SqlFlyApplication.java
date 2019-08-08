package com.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fly.jdbc.SqlFlyFactory;
import com.fly.spring.SqlFlySetup;

@SpringBootApplication
@SqlFlySetup
public class SqlFlyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SqlFlyApplication.class,args);		// run-->x
		System.out.println(SqlFlyFactory.getSqlFly().getMap("select * from sys_user"));
	}

	
	
	
}