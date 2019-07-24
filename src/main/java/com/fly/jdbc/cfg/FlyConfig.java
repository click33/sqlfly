package com.fly.jdbc.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 定义所有配置
 * 
 * @author kong
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.sqlfly")
public class FlyConfig {

	public String flyLoad = "com.fly.jdbc.cfg.FlyLoadDefault"; // FlyLoad默认的实现类

	// 连接池配置
	public String driverClassName;
	public String url;
	public String username;
	public String password;

	public boolean ispool = false; // 是否使用连接池，其值若为false，则代表不再使用连接池
	public int init = 10; // 初始化连接数
	public int min = 5; // 最小链接数
	public int max = 20; // 最大连接数

	
	// 运行配置
	public boolean printSql = false; // 是否在控制台输出每次执行的SQL与参数
	public String sqlhh = "[SQL] "; // 输出SQL的前缀
	public String argshh = "[ARGS]"; // 输出参数的前缀

	public int defaultLimit = 1000; // Page == null时默认取出的数据量
	public boolean isV = true; // 是否在初始化配置时打印版本字符画

	
	// 初始化框架
//	static {
//		FlyConfigFactory.load();
//	}

	
}
