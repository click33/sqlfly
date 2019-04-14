package com.fly.jdbc.paging;

import java.util.List;
import java.util.Map;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.cfg.FlyRun;
import com.fly.util.Page;

/**
 * SqlServer的分页
 * @author kongyongshun
 *
 */
public class FlyPagingSqlServer implements FlyPaging{

	
	
	@Override
	public <T> List<T> getListPage(SqlFly sqlFly, Page page, Class<T> cs, String sql, Object... args) {
		if(page==null) {
			return sqlFly.getList(cs, "select top " + FlyRun.flyCfg.defaultLimit + " * from ( "+sql+" ) as T", cs, args);
		}
		String newsql=
				"select * from (" + 
				"	select *,ROW_NUMBER() over(order by getdate() asc) as row from ( " +sql + " )as T " + 
				") as TT " + 
				"where row between "+(page.getStart()+1)+" and "+(page.getStart()+page.getPageSize());
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getList(cs, newsql, args);
	}

	
	@Override
	public List<Map<String, Object>> getMapListPage(SqlFly sqlFly, Page page, String sql, Object... args) {
		if(page==null) {
			return sqlFly.getMapList("select top " + FlyRun.flyCfg.defaultLimit + " * from ( "+sql+" ) as T", args);
		}
		String newsql=
				"select * from (" + 
				"	select *,ROW_NUMBER() over(order by getdate() asc) as row from ( " +sql + " )as T " + 
				") as TT " + 
				"where row between "+(page.getStart()+1)+" and "+(page.getStart()+page.getPageSize());
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getMapList(newsql, args);
	}
	
	
	
}
