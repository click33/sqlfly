package com.fly.jdbc.datasource;

import java.lang.reflect.*;
import java.sql.Connection;

/**
 * 动态代理Connection，以便在Connection调用close()时不是真正的关闭，而是将其重新放到连接池内
 */
class FlyConnectionProxy implements InvocationHandler {

	private Connection connection; // 将要被代理的对象

	/**
	 * 构造一个代理对象
	 * 
	 * @param obj
	 *            将要被代理的对象
	 */
	public FlyConnectionProxy(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 返回obj其动态代理的对象，需注意c类型必须是个接口，且obj已经实现了它
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> c) {
		return (T) Proxy.newProxyInstance(FlyConnectionProxy.class.getClassLoader(), new Class[] { c }, this);
	}

	// 代理其关闭方法
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("close")) {
			dataSource.callBackConn(connection); // 通知连接池回收此链接
			return null;
		}
		return method.invoke(connection, args); // 执行此底层方法
	}

	// 真正的关闭方法
	public void closeClose() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 所绑定的连接池
	private FlyDataSource dataSource;

	public FlyConnectionProxy setFlyDataSource(FlyDataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

}
