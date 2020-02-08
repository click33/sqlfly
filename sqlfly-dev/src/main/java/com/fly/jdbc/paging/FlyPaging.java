package com.fly.jdbc.paging;

/**
 * 分页接口,若自定义分页，请实现此接口
 */
public interface FlyPaging {

	
	/**
	 * 根据原始SQL，返回分页形式的SQL
	 * @param sql sql语句
	 * @param page 分页对象 
	 * @return
	 */
	public String getPagingSql(String sql, Page page);
	
	
	
	
	
}
