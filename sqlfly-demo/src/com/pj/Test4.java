package com.pj;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;
import com.fly.jdbc.cfg.FlyObjects;
import com.fly.jdbc.paging.FlyPaging;
import com.fly.jdbc.paging.FlyPagingOracle;
import com.fly.jdbc.paging.FlyPagingSqlServer;
import com.fly.jdbc.paging.Page;
import com.pj.model.SysUser;
import com.pj.more.FlyPagingMyCustom;

// 测试4 分页查询 
public class Test4 {

	
	// 测试4-1 获取分页对象 
	@Test
	public void test4_1() {
		// 方式 1，通过构造方法
		Page page = new Page(1, 10);	// 当前页、页大小
		System.out.println(page);
		
		// 方式2，根据其静态方法
		Page page2 = Page.getPage(1, 10);	// 当前页、页大小 
		System.out.println(page2);
		
		// 方式3，通过 start 来构建
		Page page3 = new Page();
		page3.setStart(4);	// 直接设置起始位置 
		page.setPageSize(10);
		System.out.println(page3);
	}
	
	// 测试4-2 分页查询 - 映射为 List<Model>
	@Test
	public void test4_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Page page = new Page(1, 2);
		List<SysUser> list = sqlFly.getListPage(page, SysUser.class, sql);
		System.out.println("通过分页查询到" + list.size() + "条数据，分别为：");
		for (SysUser user : list) {
			System.out.println(user);
		}
	}
	
	// 测试4-3 分页查询 - 映射为 List<Map>
	@Test
	public void test4_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Page page = new Page(1, 2);
		List<Map<String, Object>> listMap = sqlFly.getMapListPage(page, sql);
		System.out.println("当前用户表共有" + listMap.size() + "条数据，分别为：");
		for (Map<String, Object> map : listMap) {
			System.out.println(map);
		}
	}
	
	// 测试4-4 切换分页方式 
	@Test
	public void test4_4() {
		// 切换为 SqlServer 分页方式 
		FlyPaging paging = new FlyPagingSqlServer();
		FlyObjects.setPaging(paging);
		
		// 切换为 Oracle 分页方式
		FlyPaging paging2 = new FlyPagingOracle();
		FlyObjects.setPaging(paging2);
	}
	
	// 测试4-5 自定义分页方式
	@Test
	public void test4_5() {
		FlyPaging paging = new FlyPagingMyCustom(); 
		FlyObjects.setPaging(paging); 
	}
	
	
	
}
