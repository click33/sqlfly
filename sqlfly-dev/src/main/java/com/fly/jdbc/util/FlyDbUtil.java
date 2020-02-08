package com.fly.jdbc.util;

import java.lang.reflect.Field;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fly.jdbc.SAP;
import com.fly.jdbc.cfg.FlyObjects;
import com.fly.jdbc.exception.FlySQLException;
import com.fly.jdbc.exception.FlySysException;
import com.fly.jdbc.mapping.AtFlyClass;
import com.fly.jdbc.mapping.AtFlyField;
import com.fly.jdbc.mapping.AtFlyReadUtil;

/**
 * SqlFly对数据库访问的最底层类
 * <p>
 * 你不需要调用此类的API，因为SqlFly类对此类进行了更完美的封装
 */
public class FlyDbUtil {

	// API

	/* * * * * * * * * * * * * 释放资源 * * * * * * * * * * * * * * * * * * * * */
	/** 
	 * 释放ResultSet
	 * @param rs
	 */
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
	 * @throws SQLException e
	 */
	@SuppressWarnings("unchecked")
	public static PreparedStatement getExecute(Connection connection, String sql, Object... args){
		
		// 如果是#{参数}模式
		if(sql.contains("#")) {
			SAP sap = refSql(sql, args[0], (args.length > 1 ? (Map<String, Object>)args[1] : null));
			sql = sap.sql;
			args = sap.args.toArray();
		} else if(sql.contains("?")) {	// 如果是?模式
			// 如果调用者粗心的将第一个参数填装成了Array或List
			if(args.length == 1) {
				if(args[0].getClass().isArray()) {
					args = (Object[])args[0];
				} else if(args[0] instanceof List) {
					args = ((List<Object>)args[0]).toArray();
				}
			}
		} else {
			// 既不是#{参数}，也不是? 	
			args = new Object[]{};
		}
		
		PreparedStatement pst = null;
		try {
			FlyObjects.getAop().exeBefore(sql, args);
			pst = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			pst.execute();
			FlyObjects.getAop().exeAfter(sql, args, pst);
			return pst;
		} catch (SQLException e) {
			FlyObjects.getAop().exeException(sql, args, e);
			return pst;
		}finally {
			FlyObjects.getAop().exeFinally(sql, args, pst);
		}
	}
	
	

	/* * * * * * * * * * * * * 结果集映射为Map * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * 将结果集映射为 -- Map 集合
	 * @param rs
	 * @return v
	 * @throws SQLException e
	 */
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
	 * @param rs rs
	 * @return v
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
	 * @return v
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
	 * @param rs rs
	 * @param cs cs
	 * @return v
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
	 * @param rs rs
	 * @param cs cs
	 * @return v
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
				java.sql.Timestamp date = rs.getTimestamp(key);
				if(date != null){
					value = new java.sql.Date(date.getTime());
				}
			} else {
				value = rs.getObject(key); // 都不匹配，走推荐路线
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
	
	
	

	
	// 参数刷新 
	// 进行 #{参数}模式到 ?模式的转换 
	private static SAP refSql(String sql, Object model, Map<String, Object> paramMap){
		SAP sap = new SAP();
		Matcher m = Pattern.compile("\\#\\{.+?\\}").matcher(sql);	// 拼接#号 
		while (m.find()) {
	        String param = m.group();
	        String paramName = param.substring(param.indexOf("{") + 1, param.indexOf("}")).trim();
	        Object value = null;
			try {
				Field field = model.getClass().getDeclaredField(paramName);
				field.setAccessible(true);
				value = field.get(model);
			} catch (NoSuchFieldException e) {
				// 从model中找不到这个属性时，尝试从Map中读取 
				if(paramMap != null && paramMap.containsKey(paramName)) {
					value = paramMap.get(paramName);
				} else {
					throw new FlySQLException("未能从给定的参数列表中读取到属性值：" + paramName, e);
				}
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				throw new FlySQLException("未能从给定的参数列表中读取到属性值：" + paramName, e);
			}
	        sap.args.add(value);
	    }
		sap.sql = m.replaceAll("?");	// 替换为jdbc可以识别的sql 
		return sap;
	}
	
	
}
