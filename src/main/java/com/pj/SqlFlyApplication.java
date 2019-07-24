package com.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.fly.jdbc.SqlFlyFactory;
import com.fly.jdbc.cfg.FlyConsts;

@SpringBootApplication
@ComponentScan(basePackages = FlyConsts.packagePath)	// sqlfly 
public class SqlFlyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SqlFlyApplication.class,args);		// run-->
		
		System.out.println(SqlFlyFactory.getSqlFly().getMap("select * from sys_user where id = 10001"));
		
		// 代码生成 
//		CodeUtil.codeCfg
//			.setAuthor("shengzhang")
//			.setCodePath("src/main/java/")
//			.setPackagePath( "com.pj.x_project")
//			.setIs_three(false)
//			.setIs_lomock(true);
//		CodeUtil.run();
		
	}

	
	
}