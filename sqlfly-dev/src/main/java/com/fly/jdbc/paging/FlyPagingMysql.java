package com.fly.jdbc.paging;

/**
 * mysql的分页
 * @author kongyongshun
 *
 */
public class FlyPagingMysql implements FlyPaging{

	
	@Override
	public String getPagingSql(String sql, Page page) {
		String newsql = sql + " limit " + page.getStart() + "," + page.getPageSize();
		return newsql;
	}
	
	
	
	
}
