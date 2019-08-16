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

import com.fly.jdbc.exception.FlySQLException;
import com.fly.jdbc.exception.FlySysException;

/**
 * FlyDataSource提供连接池的一个简易实现
 */
public class FlyDataSource implements DataSource {

	private String driverClassName;
	private String url;
	private String username;
	private String password;

	private boolean ispool;
	private int init;
	private int min;
	private int max;

	public FlyDataSource() {
	}

	public FlyDataSource(String driverClassName, String url, String username, String password, boolean ispool, int init,
			int min, int max) {
		super();
		this.driverClassName = driverClassName;
		this.url = url;
		this.username = username;
		this.password = password;
		this.ispool = ispool;
		this.init = init;
		this.min = min;
		this.max = max;
		this.loadDriver();
	}

	// 加载驱动
	private void loadDriver() {
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
			throw new FlySysException("数据库驱动加载异常：" + driverClassName, e);
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
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	// 配置属性的 get && set
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isIspool() {
		return ispool;
	}

	public void setIspool(boolean ispool) {
		this.ispool = ispool;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

}
