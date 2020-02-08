package com.pj;

import javax.sql.DataSource;

import org.junit.Test;

import com.fly.jdbc.cfg.FlyObjects;
import com.pj.more.DruidDataSourceUtil;

// 测试6 连接池 
public class Test6 {

	// 测试6-1 集成druid连接池 
	@Test
	public void test6_1() {
		DataSource dataSource = DruidDataSourceUtil.getDataSource();	// 获取 datasource
		FlyObjects.setDataSource(dataSource);							// 注入到SqlFly框架中 
		System.out.println(FlyObjects.getDataSource());
		// ... 此时再获取 SqlFly，其Connection 就是由druid连接池创建的了 
	}
	
	
	
}
