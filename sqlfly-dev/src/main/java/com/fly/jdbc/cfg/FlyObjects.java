package com.fly.jdbc.cfg;

import javax.sql.DataSource;

import com.fly.jdbc.FlyUtil;
import com.fly.jdbc.aop.FlyAop;
import com.fly.jdbc.aop.FlyAopDefault;
import com.fly.jdbc.datasource.FlyDataSource;
import com.fly.jdbc.paging.FlyPaging;
import com.fly.jdbc.paging.FlyPagingMysql;

/**
 * 定义所有对象
 * 
 * @author kong
 *
 */
public class FlyObjects {

	public static String configPath = "sqlfly.properties"; // 表示sqlfly配置文件地址

	private static FlyConfig config;		// 配置信息对象 
	public static FlyConfig getConfig() {
		if (config == null) {
			initConfig();
		}
		return config;
	}
	public static void setConfig(FlyConfig config) {
		FlyObjects.config = config;
		if(config.getIsV()) {
			FlyUtil.printSqlFly();
		}
	}
	public synchronized static void initConfig() {
		if (config == null) {
			setConfig(FlyConfigFactory.createConfig(configPath));
		}
	}
	
	
	private static FlyAop aop; // AOP对象  
	public static FlyAop getAop() {
		if (aop == null) {
			initAop();
		}
		 return aop;
	}
	public static void setAop(FlyAop aop) {
		FlyObjects.aop = aop;
	}
	public synchronized static void initAop() {
		if (aop == null) {
			setAop(new FlyAopDefault());
		}
	}
	
	
	private static FlyPaging paging;	 // 分页对象  
	public static FlyPaging getPaging() {
		if(paging == null) {
			initPageing();
		}
		return paging;
	}
	public static void setPaging(FlyPaging paging) {
		FlyObjects.paging = paging;
	}
	public synchronized static void initPageing() {
		if(paging == null) {
			setPaging(new FlyPagingMysql());
		}
	}
	
	
	private static DataSource dataSource;	 // 连接池对象 
	public static DataSource getDataSource() {
		if(dataSource == null){
			initDataSource();
		}
		return dataSource;
	}
	public static void setDataSource(DataSource dataSource) {
		FlyObjects.dataSource = dataSource;
	}
	public synchronized static void initDataSource() {
		if(dataSource == null){
			FlyConfig c = getConfig();
			setDataSource(new FlyDataSource(c.getDriverClassName(), c.getUrl(), 
					c.getUsername(), c.getPassword(), c.getIspool(), c.getInit(), c.getMin(), c.getMax()));
		}
	}
	
	
	// SqlFly的id号 
	private static long sqlfly_id = 0;
	public static long getSqlFlyId() {
		return sqlfly_id++;
	}
	
	
//	// 初始化框架
//	static {
//		FlyConfigFactory.load();
//	}

}
