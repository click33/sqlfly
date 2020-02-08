package com.pj.more;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 创建一个druid的数据源 
 */
public class DruidDataSourceUtil {

	

	private static String propertiesPath = "druid.properties";	// 配置文件地址
	private static DataSource dataSource = null;

	/**
	 * 获取数据源 
	 */
	public static synchronized DataSource getDataSource() {
		try {
			if(dataSource == null) {
				// 加载配置文件 
				InputStream is = DruidDataSourceUtil.class.getClassLoader().getResourceAsStream(propertiesPath);
			    Properties properties = new Properties();
			    properties.load(is);
			    // 创建连接池对象
			    DruidDataSourceUtil.dataSource = DruidDataSourceFactory.createDataSource(properties);
			}
			return dataSource;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
