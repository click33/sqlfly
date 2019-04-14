package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fly.jdbc.FlyFactory;

@SpringBootApplication		//注解
public class AppSqlFly {
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(AppSqlFly.class,args);		// run-->
		System.out.println(FlyFactory.getFly().getList(long.class, "select id from sys_user"));
		
	}
	
}