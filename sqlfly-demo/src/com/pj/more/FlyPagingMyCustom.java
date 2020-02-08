package com.pj.more;

import com.fly.jdbc.paging.FlyPaging;
import com.fly.jdbc.paging.Page;

// 自定义SqlFly的分页实现类
public class FlyPagingMyCustom implements FlyPaging{

	// 实现此方法，返回分页形式的sql，以备SqlFly的调用 
	@Override
	public String getPagingSql(String sql, Page page) {
		String newsql = sql + " limit " + page.getStart() + "," + page.getPageSize();
		return newsql;
	}

}
