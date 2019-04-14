package com.fly.jdbc.aop;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fly.jdbc.exception.FlySQLException;

public interface FlyAop {

	
	/**
	 * 执行前要做的事 
	 * @return
	 */
	public void exeBefore(String sql, Object[] args);
	
	
	/**
	 * 执行后要做的事
	 */
	public void exeAfter(String sql, Object[] args, PreparedStatement preparedStatement);
	
	
	/**
	 * Sql执行异常要做的事
	 */
	public void exeException(String sql, Object[] args, SQLException e) throws FlySQLException;
	

	
	/**
	 * 无论如何都会执行的最终
	 */
	public void exeFinally(String sql, Object[] args, PreparedStatement preparedStatement);
	
	
	
	
}
