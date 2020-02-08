package com.pj;

import org.junit.Test;

import com.fly.jdbc.SqlFlyFactory;
import com.fly.jdbc.aop.FlyAop;
import com.fly.jdbc.cfg.FlyObjects;
import com.pj.more.FlyAopMyCustom;

// 测试7 自定也切面操作
public class Test7 {

	// 测试7-1 自定义切面
	@Test
	public void test7_1() {
		FlyAop aop = new FlyAopMyCustom();
		FlyObjects.setAop(aop);
		SqlFlyFactory.getSqlFly().getScalar("select count(*) from sys_user");	// 执行sql测试一下
	}
	
	
}
