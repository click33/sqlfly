package com.fly.jdbc.paging;

import java.util.List;
import java.util.Map;

import com.fly.jdbc.SqlFly;
import com.fly.util.Page;

/**
 * 分页接口,若自定义分页，请实现此接口
 */
public interface FlyPaging {

	
	
	/**
	 * 分页 - 结果集映射为实体类
	 * @param sqlFly sqlfly实例
	 * @param page 分页
	 * @param cs 映射的类型
	 * @param sql sql语句
	 * @param args 参数 
	 * @return
	 */
	public <T> List<T> getListPage(SqlFly sqlFly, Page page, Class<T> cs, String sql, Object... args);
	

	/**
	 * 分页 - 结果集映射为Map
	 * @param sqlFly sqlfly实例
	 * @param page 分页
	 * @param sql sql语句
	 * @param args 参数 
	 * @return
	 */
	public List<Map<String, Object>> getMapListPage(SqlFly sqlFly, Page page, String sql, Object... args);
	
	
	
	
}
