package com.fly.jdbc.util;

/**
 * 以lambda表达式回滚事务的辅助类
 * @author kong
 *
 */
public interface FlyLambdaRollbackRT {
	
	/**
	 * 事务发生异常的方法 
	 */
	public Object run(Exception e);
	
}
