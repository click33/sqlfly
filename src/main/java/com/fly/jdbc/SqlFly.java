package com.fly.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fly.jdbc.cfg.FlyObjects;
import com.fly.jdbc.exception.FlySQLException;
import com.fly.jdbc.paging.Page;
import com.fly.jdbc.util.FlyDbUtil;
import com.fly.jdbc.util.FlyLambdaBegin;
import com.fly.jdbc.util.FlyLambdaBeginRT;
import com.fly.jdbc.util.FlyLambdaRollback;
import com.fly.jdbc.util.FlyLambdaRollbackRT;

/**
 * Fly核心对象，JDBC的封装
 * <p>
 * 该类提供一系列精简的API进行数据库的访问
 */
public class SqlFly extends SqlFlyBase {

	/* * * * * * * * * * * * * 基本交互 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * 执行任意类型sql，args为参数列表(其余函数同理)
	 */
	public PreparedStatement getExecute(String sql, Object... args) {
		return FlyDbUtil.getExecute(getConnection(), this.prevSql = sql, args);
	}

	/**
	 * 增删改，返回受影响行数
	 */
	public int getUpdate(String sql, Object... args) {
		PreparedStatement ps = getExecute(sql, args);
		try {
			return ps.getUpdateCount();
		} catch (SQLException e) {
			throw new FlySQLException("增删改sql执行异常", e);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				throw new FlySQLException("释放Statement失败", e);
			}
			closeByIsBegin();
		}
	}

	/**
	 * 查询，返回ResultSet结果集
	 * <p>
	 * 大多数情况下你不应该调用此方法，此方法会将ResultSet处理权交给你<b> <br/>
	 * 这意味着Fly不会自动在处理完毕结果集后将其关闭， <br/>
	 * 因此你必须在处理完结果集后执行rs.getStatement().close()释放资源; <br/>
	 * 并且在调用此方法之前必须开启事务
	 */
	public ResultSet getResultSet(String sql, Object... args) {
		try {
			return getExecute(sql, args).getResultSet();
		} catch (SQLException e) {
			throw new FlySQLException("获得ResultSet结果集失败", e);
		}
	}

	/**
	 * 聚合查询，返回第一行第一列值
	 */
	public Object getScalar(String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			if (rs.next()) {
				return rs.getObject(1);
			}
		} catch (SQLException e) {
			throw new FlySQLException("聚合查询失败", e);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
		return null;
	}

	/* * * * * * * * * * * * * 结果集映射为实体类 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * <h1>将结果集映射为指定Model的一条记录</h1> 例如查询上一条 insert sql 的主键：SELECT @@identity
	 * <br/>long id = FlyFactory.getFly().getModel(long.class, "SELECT @@identity");
	 */
	public <T> T getModel(Class<T> cs, String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			return FlyDbUtil.getModel(rs, cs);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
	}

	/**
	 * <h1>将结果集映射为指定Model集合</h1>
	 * <h1>支持基本数据类型，如：Integer、Double等</h1> 参数：sql语句，映射类型，参数列表<br/>
	 * <br/>
	 */
	public <T> List<T> getList(Class<T> cs, String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			return FlyDbUtil.getList(rs, cs);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
	}

	/**
	 * 根据分页获取集合
	 */
	public <T> List<T> getListPage(Page page, Class<T> cs, String sql, Object... args) {
		if(page == null) {
			page = new Page(1, FlyObjects.getConfig().getDefaultLimit());
		}
		if(page.getIs_count()) {
			page.setCount(getCount(sql, args));
		}
		sql = FlyObjects.getPaging().getPagingSql(sql, page);
		return getList(cs, sql, args);
	}

	
	/* * * * * * * * * * * * * 结果集映射为Map * * * * * * * * * * * * * * * * * * * * */
	
	/** 将结果集映射为 -- Map  */
	public Map<String, Object> getMap(String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			return FlyDbUtil.getMap(rs);
		} catch (SQLException e) {
			throw new FlySQLException("将结果集映射为List<Map>集合失败", e);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
	}
	
	/** 将结果集映射为--List< Map >集合 */
	public List<Map<String, Object>> getMapList(String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			return FlyDbUtil.getMapList(rs);
		} catch (SQLException e) {
			throw new FlySQLException("将结果集映射为List<Map>集合失败", e);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
	}

	/** 将结果集映射为--List< Map >集合 - 并分页 */
	public List<Map<String, Object>> getMapListPage(Page page, String sql, Object... args) {
		if(page == null) {
			page = new Page(1, FlyObjects.getConfig().getDefaultLimit());
		}
		if(page.getIs_count()) {
			page.setCount(getCount(sql, args));
		}
		sql = FlyObjects.getPaging().getPagingSql(sql, page);
		return getMapList(sql, args);
	}

	
	
	/**
	 * 将结果集映射为--List< Map >集合,指定列(keyCol)的数据作key,填null默认第一列
	 */
	public List<Map<String, Object>> getListMapByCol(String keyCol, String sql, Object... args) {
		ResultSet rs = getResultSet(sql, args);
		try {
			return FlyDbUtil.getMapListByCol(rs, keyCol);
		} catch (SQLException e) {
			throw new FlySQLException("将结果集映射为List<Map>集合失败", e);
		} finally {
			FlyDbUtil.close(rs);
			closeByIsBegin();
		}
	}

	
	
	
	
	/* * * * * * * * * * * * * 快捷方法 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * 返回指定sql能查到多少条记录
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int getCount(String sql, Object... args) {
		return getScalarInt("select count(*) from (" + sql + ") as tab_x", args);
	}

	/**
	 * 聚合查询，返回第一行第一列值，并将其强转成为Integer
	 */
	public Integer getScalarInt(String sql, Object... args) {
		Object obj = getScalar(sql, args);
		return obj == null ? null : Integer.parseInt(obj.toString());
	}

	/**
	 * 增删改的重载：此方法()==getUpdate()+commit()
	 */
	public int getUpdateCommit(String sql, Object... args) {
		try {
			return getUpdate(sql, args);
		} finally {
			commit();
		}
	}

	// SQL构建类 
	public <T> List<T> getListPage(Page page, Class<T> cs, SAP sap) {
		return getListPage(page, cs, sap.sql, sap.args.toArray());
	}
	public List<Map<String, Object>> getMapListPage(Page page, SAP sap) {
		return getMapListPage(page, sap.sql, sap.args.toArray());
	}

	
	
	
	
	
	
	// 重写改变其返回值
	public SqlFly beginTransaction() {
		super.beginTransaction();
		return this;
	}
	
	/**
	 * 以lambda方式开始事务
	 * @param code lambda表达式
	 * @return 
	 */
	public void begin(FlyLambdaBegin code) {
		beginTransaction();
		code.run();
		commit();
	}
	
	/**
	 * 以lambda方式开始事务
	 * @param begin begin代码块
	 * @param rollback rollback代码块
	 * @return 
	 */
	public void begin(FlyLambdaBegin begin, FlyLambdaRollback rollback) {
		try {
			beginTransaction();
			begin.run();
			commit();
		} catch (Exception e) {
			rollback();
			if(rollback != null) {
				rollback.run(e);
			}
		}
	}
	
	
	/**
	 * 已lambda方式开启事务，并返回一个值 
	 */
	@SuppressWarnings("unchecked")
	public <T> T beginRT(FlyLambdaBeginRT begin) {
		beginTransaction();
		Object return_obj =  begin.run();
		commit();
		return (T)return_obj;
	}

	/**
	 * 已lambda方式开启事务，并返回一个值 
	 */
	@SuppressWarnings("unchecked")
	public <T> T beginRT(FlyLambdaBeginRT begin, FlyLambdaRollbackRT rollback) {
		Object return_obj = null;
		try {
			beginTransaction();
			return_obj = begin.run();
			commit();
		} catch (Exception e) {
			rollback();
			if(rollback != null) {
				return_obj = rollback.run(e);
			}
		}
		return (T)return_obj;
	}
	
	
	
}
