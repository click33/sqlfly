package com.fly.jdbc.util;

/**
 * 以lambda表达式开启事务的辅助类
 * @author kong
 *
 */
public interface FlyLambdaBeginRT {
	

	/**
	 * 执行事务的方法 
	 * @return
	 */
	public Object run();
	
	
}
