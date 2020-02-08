package com.pj;

import org.junit.Test;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;

// 测试5 事务示例
public class Test5 {

	// 测试5-1 开启事务
	@Test
	public void test5_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		try {
			sqlFly.beginTransaction(); // 开启事务
			sqlFly.getUpdate("insert into sys_user values()"); // 执行一条会失败的sql
			sqlFly.commit(); // 提交事务
			System.out.println("事务提交成功");
		} catch (Exception e) {
			sqlFly.rollback(); // 回滚事务
			System.out.println("事务提交失败，已回滚");
			e.printStackTrace();
		}
	}

	// 测试5-2 开启事务 lambda表达式
	@Test
	public void test5_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		sqlFly.begin(() -> { // SqlFly将在开启事务后，再调用此代码块
			System.out.println("事务已被开启");
			sqlFly.getUpdate("insert into sys_user values()"); // 执行一条会失败的sql
		}, (e) -> { // 事务发生异常后，SqlFly将回滚事务后，再调用此代码块
			System.out.println("事务已被回滚");
			e.printStackTrace();
		});
	}
	

	// 测试5-3 开启事务 lambda表达式, 并指定返回值 
	@Test
	public void test5_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String return_obj = sqlFly.beginRT(() -> { // SqlFly将在开启事务后，再调用此代码块
			System.out.println("事务已被开启");
			sqlFly.getUpdate("insert into sys_user values()"); // 执行一条会失败的sql
			return "这是返回的值，可以是任意类型";
		}, (e) -> { // 事务发生异常后，SqlFly将回滚事务后，再调用此代码块
			System.out.println("事务已被回滚");
			e.printStackTrace();
			return "这是返回的值，可以是任意类型";
		});
		System.out.println("返回的值：" + return_obj);
	}
	
	
	

}
