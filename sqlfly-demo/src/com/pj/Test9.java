package com.pj;

import org.junit.Test;

import com.fly.re.CodeUtil;

// 测试9 代码生成
public class Test9 {


	// 测试9-1 代码生成器 标准三层 
	@Test
	public void test9_1() {
		CodeUtil.codeCfg
			.setProjectPath("E:\\sqlfly-code")
			.setCodePath("src/")
			.setPackagePath( "com.pj.x_project")
			.setAuthor("shengzhang")
			.setIs_three(true)
			.setIs_lomock(false);
		CodeUtil.run();
	}
	
	// 测试9-2 代码生成器 简介模式 
	@Test
	public void test9_2() {
		CodeUtil.codeCfg
			.setProjectPath("E:\\sqlfly-code")
			.setCodePath("src/")
			.setPackagePath( "com.pj.x_project")
			.setAuthor("shengzhang")
			.setIs_three(false)
			.setIs_lomock(false);
		CodeUtil.run();
	}
	
	// 项目地址  
	// 代码路径 
	// 包地址
	// 代码作者 
	// 是否标准三层代码
	// 是否使用lomock
}
