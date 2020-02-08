package com.fly.jdbc.paging;

/**
 * Oracle的分页
 * @author kongyongshun
 *
 */
public class FlyPagingOracle implements FlyPaging{

	

	@Override
	public String getPagingSql(String sql, Page page) {
		String newsql = "select * from (" + "	select tab_1.*,ROWNUM rn from ( " + sql + " ) tab_1 " + ") tab_2 where rn between "
				+ (page.getStart() + 1) + " and " + (page.getStart() + page.getPageSize());
		return newsql;
	}
	
	
}
