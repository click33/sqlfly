package com.fly.jdbc.aop;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import com.fly.jdbc.cfg.FlyRun;
import com.fly.jdbc.exception.FlySQLException;

/**
 * FlyAop的默认实现
 * @author kongyongshun
 *
 */
public class FlyAopDefault implements FlyAop {

	@Override
	public void exeBefore(String sql, Object[] args) {
		if(FlyRun.flyCfg.printSql){
			System.err.print(FlyRun.flyCfg.sqlhh);
			System.err.println(sql);
			System.err.print(FlyRun.flyCfg.argshh);
			System.err.println(Arrays.asList(args));
		}
	}

	@Override
	public void exeAfter(String sql, Object[] args, PreparedStatement preparedStatement) {
		
	}

	@Override
	public void exeException(String sql, Object[] args, SQLException e) throws FlySQLException {
		throw new FlySQLException("sql执行异常：" + sql, e);
	}

	
	@Override
	public void exeFinally(String sql, Object[] args, PreparedStatement preparedStatement) {
		
	}

	
	
	
}
