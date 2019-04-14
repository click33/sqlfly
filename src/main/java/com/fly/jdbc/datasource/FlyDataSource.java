package com.fly.jdbc.datasource;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.fly.jdbc.cfg.FlyRun;
import com.fly.jdbc.exception.FlySQLException;
import com.fly.jdbc.exception.FlySysException;

/**
 * FlyDataSource提供连接池的一个简易实现
 */
public class FlyDataSource implements DataSource {

	public String driverClassName = FlyRun.flyCfg.driverClassName;
	public String url = FlyRun.flyCfg.url;
	public String username = FlyRun.flyCfg.username;
	public String password = FlyRun.flyCfg.password;

	public boolean ispool = FlyRun.flyCfg.ispool; // 是否使用连接池，其值若为false，则代表不再使用连接池
	public int init = FlyRun.flyCfg.init; // 初始化连接数
	public int min = FlyRun.flyCfg.min; // 最小链接数
	public int max = FlyRun.flyCfg.max; // 最大连接数

	
	public FlyDataSource() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			throw new FlySysException("驱动加载异常：" + driverClassName, e);
		}
	}

	// 连接相关

	private LinkedList<Connection> conns = new LinkedList<Connection>(); // 连接池

	// 根据配置信息加载一条链接
	private Connection loadConnection() {
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			if (ispool == false) {
				return connection; // 不启用连接池将不再代理
			}
			return new FlyConnectionProxy(connection).setFlyDataSource(this).getProxy(Connection.class);// 返回代理对象
		} catch (SQLException e) {
			throw new FlySQLException("创建连接失败：", e);
		}
	}

	// 尝试释放一条链接
	private void closeConn(Connection conn) {
		((FlyConnectionProxy) Proxy.getInvocationHandler(conn)).closeClose();// 真正的关闭
	}

	// 初始化池
	private void initPool() {
		if (conns == null) {
			conns = new LinkedList<Connection>();
		}
		for (int i = conns.size(); i < init; i++) {
			conns.add(loadConnection());
		}
	}

	/** 获取一条连接 */
	@Override
	public Connection getConnection() throws SQLException {
		if (ispool == false) {
			return loadConnection();
		}
		if (conns.size() < min || conns.size() == 0) {
			initPool();
		}
		return conns.removeFirst();
	}

	/** 获取一条连接,此方法参数无效 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

	/**
	 * 获取池中连接数
	 */
	public int getPoolCount() {
		return conns.size();
	}

	/**
	 * 回收一条连接，不是从本连接池中取出的不要让本池回收
	 */
	public void callBackConn(Connection conn) {
		try {
			if (conn == null) {
				return;
			} else if (ispool == false || conn.isClosed() || conns.size() >= max) {
				closeConn(conn);
			} else {
				if (conn.getAutoCommit() == false) {
					conn.rollback();
				}
				conns.add(conn);
			}
		} catch (SQLException e) {
			throw new FlySQLException("回收连接失败", e);
		}
	}

	// 这么多属性烦的一笔

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
