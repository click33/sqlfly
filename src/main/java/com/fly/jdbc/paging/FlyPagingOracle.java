package com.fly.jdbc.paging;

import java.util.List;
import java.util.Map;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.cfg.FlyRun;
import com.fly.util.Page;

/**
 * Oracle的分页
 * @author kongyongshun
 *
 */
public class FlyPagingOracle implements FlyPaging{

	
	@Override
	public <T> List<T> getListPage(SqlFly sqlFly, Page page, Class<T> cs, String sql, Object... args) {
		if (page == null) {
			return sqlFly.getList(cs, "select * from ( " + sql + " ) T  where ROWNUM<" + (FlyRun.flyCfg.defaultLimit + 1),
					args);
		}
		String newsql = "select * from (" + "	select T.*,ROWNUM rn from ( " + sql + " ) T " + ") TT where rn between "
				+ (page.getStart() + 1) + " and " + (page.getStart() + page.getPageSize());
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getList(cs, newsql, args);
	}

	
	@Override
	public List<Map<String, Object>> getMapListPage(SqlFly sqlFly, Page page, String sql, Object... args) {
		if (page == null) {
			return sqlFly.getMapList("select * from ( " + sql + " ) T  where ROWNUM<" + (FlyRun.flyCfg.defaultLimit + 1),
					args);
		}
		String newsql = "select * from (" + "	select T.*,ROWNUM rn from ( " + sql + " ) T " + ") TT where rn between "
				+ (page.getStart() + 1) + " and " + (page.getStart() + page.getPageSize());
		page.setCount(sqlFly.getCount(sql, args));
		return sqlFly.getMapList(newsql, args);
	}
	
	
	
}
