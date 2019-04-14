package com.fly.jdbc.paging;

import java.util.List;
import java.util.Map;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.cfg.FlyRun;
import com.fly.util.Page;

/**
 * mysql的分页
 * @author kongyongshun
 *
 */
public class FlyPagingMysql implements FlyPaging{

	
	
	
	
	@Override
	public <T> List<T> getListPage(SqlFly sqlFly, Page page, Class<T> cs, String sql, Object... args) {
		if (page == null) {
			return sqlFly.getList(cs, sql + " limit " + FlyRun.flyCfg.defaultLimit, args);
		}
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getList(cs, sql + " limit " + page.getStart() + "," + page.getPageSize(), args);
	}

	
	
	@Override
	public List<Map<String, Object>> getMapListPage(SqlFly sqlFly, Page page, String sql, Object... args) {
		if (page == null) {
			return sqlFly.getMapList(sql + " limit " + FlyRun.flyCfg.defaultLimit, args);
		}
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getMapList(sql + " limit " + page.getStart() + "," + page.getPageSize(), args);
	}
	
	
	
	
}
