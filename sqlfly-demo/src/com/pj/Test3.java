package com.pj;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;
import com.pj.model.SysUser;

// 测试3 执行查询
public class Test3 {

	
	// 测试3-1 映射为 object 
	@Test
	public void test3_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select username from sys_user where id = ?";
		Object username = sqlFly.getScalar(sql, 10001);
		System.out.println("id=10001的用户，其username=" + username);
		System.out.println(username.getClass());
	}
	
	// 测试3-2 映射为 Model
	@Test
	public void test3_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user where id = ?";
		SysUser user = sqlFly.getModel(SysUser.class, sql, 10001);
		System.out.println("id=10001的用户，其详细信息为：" + user);
	}
	
	// 测试3-3 映射为List集合 
	@Test
	public void test3_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		List<SysUser> list = sqlFly.getList(SysUser.class, sql, 10001);
		System.out.println("当前用户表共有" + list.size() + "条数据，分别为：");
		for (SysUser user : list) {
			System.out.println(user);
		}
	}
	
	
	// 测试3-4 映射为 Map
	@Test
	public void test3_4() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Map<String, Object> map = sqlFly.getMap(sql, 10001);
		System.out.println("id=10001的用户，其详细信息为：" + map);
	}
	
	// 测试3-5 映射为 map集合 
	@Test
	public void test3_5() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		List<Map<String, Object>> listMap = sqlFly.getMapList(sql);
		System.out.println("当前用户表共有" + listMap.size() + "条数据，分别为：");
		for (Map<String, Object> map : listMap) {
			System.out.println(map);
		}
	}
	
	// 测试3-6  映射为Map并指定列做key 
	@Test
	public void test3_6() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select id, username from sys_user";
		List<Map<String, Object>> listMap = sqlFly.getListMapByCol("id", sql);
		System.out.println("映射结果为：" + listMap.get(0));
	}
	
	
	
	
	
}
