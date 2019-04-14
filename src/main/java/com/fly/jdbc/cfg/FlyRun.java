package com.fly.jdbc.cfg;


import javax.sql.DataSource;

import com.fly.jdbc.aop.FlyAopDefault;
import com.fly.jdbc.datasource.FlyDataSource;
import com.fly.jdbc.aop.FlyAop;
import com.fly.jdbc.paging.FlyPaging;
import com.fly.jdbc.paging.FlyPagingMysql;

/**
 * Fly运行时的一些方式
 */
public class FlyRun {

	
	public static FlyCfg flyCfg;				// 默认的配置文件
	

	static{
		FlyStart.run();
	}
	

	public static FlyAop flyAop = new FlyAopDefault();				// 默认的AOP类
	public static FlyPaging flyPaging = new FlyPagingMysql();		// 默认的分页方式
	public static DataSource dataSource;							// 默认的连接池
	/**
	 * @return the dataSource
	 */
	public static DataSource getDataSource() {
		if(dataSource == null){
			dataSource = new FlyDataSource();
		}
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public static void setDataSource(DataSource dataSource) {
		FlyRun.dataSource = dataSource;
	}
	
	
	
}
