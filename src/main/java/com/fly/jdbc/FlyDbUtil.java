package com.fly.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fly.jdbc.cfg.FlyRun;
import com.fly.jdbc.exception.FlySQLException;
import com.fly.jdbc.exception.FlySysException;
import com.fly.jdbc.mapping.AtFlyClass;
import com.fly.jdbc.mapping.AtFlyField;
import com.fly.jdbc.mapping.AtFlyReadUtil;


/**
 * Fly对数据库访问的最底层类
 * <p>
 * 你不需要调用此类的API，因为SqlFly类对此类进行了更完美的封装
 */
public class FlyDbUtil {

	// API

	/* * * * * * * * * * * * * 释放资源 * * * * * * * * * * * * * * * * * * * * */
	/** 释放ResultSet */
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.getStatement().close();
			}
		} catch (SQLException e) {
			throw new FlySQLException("释放ResultSet或其Statement失败", e);
		}
	}

	/* * * * * * * * * * * * * 基本交互 * * * * * * * * * * * * * * * * * * * * */

	/**
	 * 执行任意类型sql，args为参数列表(其余函数同理)
	 * @param connection 连接
	 * @param sql 要执行的sql
	 * @param args sql语句中的参数
	 * @return 返回sql执行后的PreparedStatement对象
	 * @throws SQLException
	 */
	public static PreparedStatement getExecute(Connection connection, String sql, Object... args){
		PreparedStatement pst = null;
		try {
			FlyRun.flyAop.exeBefore(sql, args);
			pst = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					pst.setObject(i + 1, args[i]);
				}
			}
			pst.execute();
			FlyRun.flyAop.exeAfter(sql, args, pst);
			return pst;
		} catch (SQLException e) {
			FlyRun.flyAop.exeException(sql, args, e);
			return pst;
		}finally {
			FlyRun.flyAop.exeFinally(sql, args, pst);
		}
	}

	
	/* * * * * * * * * * * * * 结果集映射为Map * * * * * * * * * * * * * * * * * * * * */
	
	/** 将结果集映射为 -- Map 集合 */
	public static Map<String, Object> getMap(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		if (rs.next()) {
			Map<String, Object> rowData = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			return rowData;
		}
		return null;
	}
	
	/**
	 * 将结果集映射为--List< Map >集合，
	 * <p> 表头做K,表格做value
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getMapList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = rs.getMetaData();
		while (rs.next()) {
			Map<String, Object> rowData = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}
/**
	 * 将结果集映射为--List< Map >集合,指定列的数据作key
	 * @param rs 结果集
	 * @param keyCol 被当作key的列
	 * @return Map
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getMapListByCol(ResultSet rs,String keyCol) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = rs.getMetaData();
		
		//如果不填默认第一列
		if(keyCol==null) {
			keyCol=md.getColumnName(1);
		}

		//记录列的下标与准备Map
		int keyIndex=0;	
		for (int i = 0; i < md.getColumnCount(); i++) {
			if(md.getColumnName(i+1).equals(keyCol)==true) {
				keyIndex=i;
			}
			list.add(new LinkedHashMap<String, Object>());//需要多少个Map  --LinkedHashMap有序集合
		}
		
		//遍历结果集
		while (rs.next()) {
			String key = rs.getString(keyCol); // rs的指定列当作Key
			for (int i = 0; i < md.getColumnCount(); i++) {
				Object value = rs.getObject(i + 1); // rs单元格值
				list.get(i).put(key, value);
			}
		}
		list.remove(keyIndex);	//清除被当作key的一列
		return list;
	}

	
	/* * * * * * * * * * * * * 结果集映射为实体类 * * * * * * * * * * * * * * * * * * * * */
	/**
	 * <b>将结果集映射为指定Model集合</b>
	 */
	public static <T> List<T> getList(ResultSet rs, Class<T> cs) {
		try {
			List<T> objList = new ArrayList<T>();
			while (rs.next()) {
				objList.add(getVal_Type(rs, cs));
			}
			return objList;
		} catch (Exception e) {
			throw new FlySQLException("映射类型集合List<" + cs + ">时出错", e);
		}
	}
	/**
	 * <b>将结果集映射为指定Model集合</b>
	 */
	public static <T> T getModel(ResultSet rs, Class<T> cs) {
		try {
			if(rs.next()){
				return getVal_Type(rs, cs);
			}
			return null;
		} catch (SQLException e) {
			throw new FlySQLException("映射类型(" + cs + ")时出错", e);
		}
	}
	
	
	
	
	/* * * * * * * * * * * * * FlyDbUtil私货 * * * * * * * * * * * * * * * * * * * **/

	// 从[ResultSet]里取值，赋值到指定[类型]的Object上，并返回
	private static <T> T getVal_Type(ResultSet rs, Class<T> cs){
		
		AtFlyClass afc = AtFlyReadUtil.getAtFlyCache(cs);
		
		// 判断是否为基本类型字段
		if(afc.isBaseType){
			ResultSetMetaData md;
			try {
				md = rs.getMetaData();
				String key = md.getColumnName(1);
				return getVal_BaseType(rs, cs, key);
			} catch (SQLException e) {
				throw new FlySQLException("获取列名失败", e);
			}
		}else{
			return getVal_NotBaseType(rs, cs);
		}
	}

	// 处理8大基本数据类型+3种Date
	// 从[ResultSet]取值，基础类型，并指定[key]
	@SuppressWarnings("unchecked")
	private static <T>T getVal_BaseType(ResultSet rs, Class<T> cs, String key) {
		
		try {
			Object value = null;
			if (cs.equals(String.class)) {
				value = rs.getString(key);
			} else if (cs.equals(int.class) || cs.equals(Integer.class)) {
				value = rs.getInt(key);
			} else if (cs.equals(long.class) || cs.equals(Long.class)) {
				value = rs.getLong(key);
			} else if (cs.equals(short.class) || cs.equals(Short.class)) {
				value = rs.getShort(key);
			} else if (cs.equals(byte.class) || cs.equals(Byte.class)) {
				value = rs.getByte(key);
			} else if (cs.equals(float.class) || cs.equals(Float.class)) {
				value = rs.getFloat(key);
			} else if (cs.equals(double.class) || cs.equals(Double.class)) {
				value = rs.getDouble(key);
			} else if (cs.equals(boolean.class) || cs.equals(Boolean.class)) {
				value = rs.getBoolean(key);
			} else if (cs.equals(char.class) || cs.equals(Character.class)) {
				value = rs.getString(key).charAt(0);
			} else if (cs.equals(java.sql.Timestamp.class) || cs.equals(java.util.Date.class)) {
				value = rs.getTimestamp(key);
			} else if (cs.equals(java.sql.Date.class)) {
				value = new java.sql.Date(rs.getTimestamp(key).getTime());
			} else {
				value = rs.getObject(key); // 都不匹配，给推荐的吧
			}
			return (T)value;
		} catch (SQLException e) {
			throw new FlySQLException("字段(" + key + ")取值出错", e);
		}
	}

	// 从[ResultSet]获取值，非基础类型
	private static <T>T getVal_NotBaseType(ResultSet rs, Class<T> cs) {
		
		AtFlyClass afc = AtFlyReadUtil.getAtFlyCache(cs);
		
		T obj = null;
		try {
			obj = cs.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new FlySysException("实例化类型(" + cs + ")的实例失败", e);
		}

		for (AtFlyField ac : afc.affList) {
			try {
				if (ac.orm == false) {
					continue;
				}

				// 取值,根据此字段的类型
				Object value = null; // 指定值
				if(ac.afc.isBaseType){
					value = getVal_BaseType(rs, ac.field.getType(), ac.column);
				}else{
					value = getVal_NotBaseType(rs, cs);
				}
				
				// 赋值，判断有无特殊set
				if(ac.set == null){
					ac.field.set(obj, value); // 属性赋值
				}else{
					ac.setMethod.invoke(obj, value);	// 方法赋值
				}

			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
				throw new FlySysException("函数(" + ac.setMethod + ")调用发生异常", e);
			} catch (FlySQLException e) {
				// 字段赋值出错，暂时这样写，不让它报错了 
			}
		}

		return obj;
		
	}
	
	
	
	
	
	


}
