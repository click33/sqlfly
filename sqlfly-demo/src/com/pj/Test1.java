package com.pj;

import org.junit.Test;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;

// 测试1 获取SqlFly示例 
public class Test1 {

	// 测试1-1、通过获取SqlFly对象
	@Test
	public void test1_1() {
		SqlFly sqlFly = new SqlFly();
		System.out.println(sqlFly.getId());
	}

	// 测试1-2、通过获取SqlFly对象
	@Test
	public void test1_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		System.out.println(sqlFly.getId());
	}

}
