package com.fly.jdbc.aop;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fly.jdbc.exception.FlySQLException;

public interface FlyAop {

	
	/**
	 * 执行前要做的事 
	 * @param sql 要执行的sql语句 
	 * @param args 要执行的参数
	 */
	public void exeBefore(String sql, Object[] args);
	
	
	/**
	 *  执行后要做的事
	 * @param sql 要执行的sql语句 
	 * @param args 要执行的参数
	 * @param preparedStatement ps对象 
	 */
	public void exeAfter(String sql, Object[] args, PreparedStatement preparedStatement);
	
	
	/**
	 * Sql执行异常要做的事
	 * @param sql 要执行的sql语句 
	 * @param args 要执行的参数
	 * @param e 异常 
	 * @throws FlySQLException 异常 
	 */
	public void exeException(String sql, Object[] args, SQLException e) throws FlySQLException;
	

	
	/**
	 * 无论如何都会执行的最终
	 * @param sql 要执行的sql语句 
	 * @param args 要执行的参数
	 * @param preparedStatement ps对象
	 */
	public void exeFinally(String sql, Object[] args, PreparedStatement preparedStatement);
	
	
	
	
}
