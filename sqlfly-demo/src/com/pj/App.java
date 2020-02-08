package com.pj;

import java.util.Map;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;

/**
 * SqlFly示例 
 */
public class App {
	public static void main(String[] args) {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		Map<String, Object> map = sqlFly.getMap("select * from sys_user where id = 10001 ");
		System.out.println(map);
	}
}

/*
 * 测试清单
 * Test1	测试1  获取SqlFly示例 
 * Test2	测试2  执行增删改  
 * Test3	测试3  执行查询  
 * Test4	测试4  分页查询 
 * Test5	测试5  事务
 * Test6	测试6  连接池 
 * Test7	测试7  自定也切面操作
 * Test8 	测试8  框架配置 
 * Test9	测试9  代码生成器 
 * 
 * 运行方法：直接new Test1().test1_1()调用
 * 或者双击方法名 以 junit方式运行 
 */

