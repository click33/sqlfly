package com.fly.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.fly.jdbc.cfg.FlyRun;
import com.fly.jdbc.exception.FlySQLException;

/**
 * SqlFly的依赖治理
 * 
 * @author kongyongshun
 *
 */
public class SqlFlyBase {

	protected Connection connection; 	// 连接对象
	private DataSource dataSource; 		// 连接池对象
	protected boolean isBegin = false; 	// 是否已经开始事务
	public String sql;	 				// 上一条执行的SQL

	
	
	
	/* * * * * * * * * * * * * 连接池相关 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * 获得DataSource
	 */
	public DataSource getDataSource() {
		if (this.dataSource == null) {
			this.dataSource = FlyRun.getDataSource();
		}
		return dataSource;
	}

	/**
	 * 写入指定DataSource <br>
	 * 如果你在实例化SqlFly后并没有指定DataSource,那将使用Fly默认的连接池
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	
	/* * * * * * * * * * * * * 连接相关 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * 获得底层的connection对象，<b>慎用！一般情况下你没有理由调用此方法！</b>
	 * <p>
	 * 如果你希望将Connection取出并脱离此SqlFly的绑定，请务必在取出Connection后执行setConnection(null) <br/>
	 * 并在使用完毕后将其close()，以确保不会造成连接泄露
	 */
	public Connection getConnection() {
		if (connection == null) {
			try {
				setConnection(getDataSource().getConnection());
			} catch (SQLException e) {
				throw new FlySQLException("从连接池获取连接失败", e);
			}
		}
		return connection;
	}

	/**
	 * 给connection赋值，<b>慎用！一般情况下你没有理由调用此方法！</b>
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
		if(connection != null){
			endTransaction();	// 初始化一下连接状态
		}
	}

	/**
	 * 关闭此SqlFly底层绑定的connection
	 */
	public void close() {
		try {
			if (connection == null) {
				return;
			}
			if (isBegin == true) {
				rollback();
				return;
			}
			connection.close();
			setConnection(null);
		} catch (SQLException e) {
			throw new FlySQLException("释放连接失败", e);
		}
	}

	// 如果不在事务，直接释放
	protected void closeByIsBegin() {
		if (isBegin == false) {
			close();
		}
	}
	
	
	
	/* * * * * * * * * * * * * 事务相关 * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * 开始事务,如果已经在事务中，则先回滚之前的事务
	 * <p>
	 * SqlFly将不再实时回收连接,而是等到事务结束之后
	 * 
	 * @throws SQLException
	 */
	public SqlFlyBase beginTransaction() {
		try {
			// 如果已经在事务中，这里有四种选择：
			// 1、抛出异常
			// 2、放弃本次事务
			// 3、回滚之前的事务
			// 4、开始嵌套事务
			// 这里暂且选择第三种
			if (isBegin == true) {
				rollback();
			}
			getConnection(); 	// 先保证链接存在
			startTransaction();
			connection.setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 提交事务
	 */
	public SqlFlyBase commit() {
		try {
			if (connection == null || isBegin == false) {
				return this;
			}
			connection.commit();
			endTransaction();
			closeByIsBegin();
		} catch (SQLException e) {
			throw new FlySQLException("事务提交失败", e);
		}
		return this;
	}

	/**
	 * 回滚事务,且将autoCommit=true
	 */
	public SqlFlyBase rollback() {
		try {
			if (connection == null || isBegin == false) {
				return this;
			}
			connection.rollback();
			endTransaction();
			closeByIsBegin();
		} catch (SQLException e) {
			throw new FlySQLException("事务回滚失败", e);
		}
		return this;
	}

	/**
	 * 回滚事务，并且抛出异常
	 */
	public <T> T rollback(String msg) {
		rollback();
		throw new FlySQLException(msg, null);
	}
	public <T> T rollback(Throwable e) {
		rollback();
		throw new FlySQLException("发生异常已回滚", e);
	}
	public <T> T rollback(String msg, Throwable e) {
		rollback();
		throw new FlySQLException(msg, e);
	}
	
	// 开启事务时执行：isBegin = true;并且autoCommit=false
	private void startTransaction(){
		try {
			isBegin = true;
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new FlySQLException("事务开始失败: 设置连接autoCommit状态失败", e);
		}
	}
	// 结束事务时执行：isBegin = false;并且autoCommit=true
	private void endTransaction(){
		try {
			isBegin = false;
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new FlySQLException("事务结束失败: 设置连接autoCommit状态失败", e);
		}
	}
	
	
	
	
}
