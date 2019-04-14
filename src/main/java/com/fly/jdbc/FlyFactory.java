package com.fly.jdbc;

/**
 * Fly的工厂类
 * @author kongyongshun
 *
 */
public class FlyFactory {

	private static ThreadLocal<SqlFly> SFTL = new ThreadLocal<SqlFly>();

	/**
	 * 返回当前线程所绑定的SqlFly对象
	 */
	public static SqlFly getFly() {
		if (SFTL.get() == null) {
			SFTL.set(new SqlFly());
		}
		return SFTL.get();
	}
	

	
	// 返回一个SqlExe
	public static SqlExe getSqlExe(){
		return new SqlExe();
	}
	public static SqlExe getSqlExe(String sql, Object... args){
		return new SqlExe().appendAll(sql, args);
	}
	
	
	
}
