package com.fly.jdbc;

/**
 * Fly的工厂类
 * @author kongyongshun
 *
 */
public class SqlFlyFactory {

	private static ThreadLocal<SqlFly> SFTL = new ThreadLocal<SqlFly>();

	/**
	 * 返回当前线程所绑定的SqlFly对象
	 * @return v
	 */
	public static SqlFly getSqlFly() {
		if (SFTL.get() == null) {
			SFTL.set(new SqlFly());
		}
		return SFTL.get();
	}
	

	
	
}
