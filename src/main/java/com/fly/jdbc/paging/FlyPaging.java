package com.fly.jdbc.paging;

/**
 * 分页接口,若自定义分页，请实现此接口
 */
public interface FlyPaging {

	
	/**
	 * 根据原始SQL，返回分页形式的SQL
	 * @param sql 原始sql语句 
	 * @param pageNo 当前页
	 * @param pageSize 页大小 
	 * @return
	 */
	public String getPagingSql(String sql, Page page);
	
	
	
	
	
}
