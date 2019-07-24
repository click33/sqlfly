package com.fly.jdbc.paging;

/**
 * SqlServer的分页
 * @author kongyongshun
 *
 */
public class FlyPagingSqlServer implements FlyPaging{

	
	

	@Override
	public String getPagingSql(String sql, Page page) {
		String newsql=
				"select * from (" + 
				"	select *,ROW_NUMBER() over(order by getdate() asc) as row from ( " +sql + " )as T " + 
				") as TT " + 
				"where row between "+(page.getStart()+1)+" and "+(page.getStart()+page.getPageSize());
		return newsql;
	}
	
	
}
