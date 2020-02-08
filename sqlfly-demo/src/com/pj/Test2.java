package com.pj;

import org.junit.Test;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;
import com.pj.model.SysUser;

// 测试2，执行增删改 
public class Test2 {

	// 测试2-1，获取受影响行数 
	@Test
	public void test2_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "delete from sys_user where id = ?";
		int line = sqlFly.getUpdate(sql, 10009);	// 执行sql并返回受影响行数 
		System.out.println("成功删除：" + line + "条数据");
	}
	
	// 测试2-2，增加一条记录 
	@Test
	public void test2_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" sys_user(username, password, sex, age, create_time)	" + 
				" values (?, ?, ?, ?, now()) 	";
		Object [] args = {"刘三刀", "123456", 1, 40};	// 与sql语句中的 ?占位符参数 一一对应即可 
		sqlFly.getUpdate(sql, args);
		System.out.println("记录增加成功");
	}
	
	// 测试2-3 #参数模式 
	@Test
	public void test2_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" sys_user(username, password, sex, age, create_time)	" + 
				" values (#{username}, #{password}, #{sex}, #{age}, now()) 	";
		SysUser user = new SysUser();
		user.setUsername("王冲");
		user.setPassword("123456");
		user.setSex(1);
		user.setAge(18);
		sqlFly.getUpdate(sql, user);	// 直接实体类入参 
		System.out.println("记录增加成功");
	}
	
	// 测试2-4 插入后立刻获得主键  
	@Test
	public void test2_4() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" ser_article(title, content, user_id, create_time)	" + 
				" values (?, ?, ?, now()) 	";
		Object [] args = {"齐鲁晚报", "据报到...", 10001};	
		// 必须开启事务，保证两条sql使用的是同一条Connection连接
		sqlFly.beginTransaction();		// 开启事务
		sqlFly.getUpdate(sql, args);	// 增加
		long id = sqlFly.getModel(long.class, "SELECT @@identity");	// 获取上一条插入记录的主键 
		sqlFly.commit();				// 提交事务 
		System.out.println("记录增加成功，记录主键为：" + id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
