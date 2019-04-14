package com.fly.re.read;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fly.jdbc.SqlFly;

/**
 * 从数据库里拿指定信息
 * @author kongyongshun
 *
 */
public class ReadUtil {

	
	

	// 获取Connection中所有表的名字
	public static List<String> getTableList(Connection connection) {
		List<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	
	// 返回所有表的表注释
	public static Map<String, String> getTcMap(SqlFly sqlFly) {
		Map<String, String> map = new HashMap<>();
		try {
			String sql = "show table status";
			ResultSet rs = sqlFly.getResultSet(sql);
			while(rs.next()){
				map.put(rs.getString("Name"), rs.getString("Comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	// 返回Connection中指定表中的主键列,无主键的返回"id"
	public static String getPkColumn(Connection connection, String tableName) {
		String pk = "id";	// 默认就叫id吧
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getPrimaryKeys(null, null, tableName);
			if (rs.next()) {
				pk = rs.getString("COLUMN_NAME");
			}
			return pk;
		} catch (Exception e) {
			System.out.println("\t表" + tableName + "读取主键失败");
		}
		return pk;
	}
	
	
	// 返回指定表的全部字段JDBC建议类型
	public static Map<String, String> getJtMap(SqlFly sqlFly, String tableName) {
		Map<String, String> map = new HashMap<>();
		try {
			String sql = "select * from " + tableName + " where 1=0";
			ResultSetMetaData rsmd = sqlFly.getResultSet(sql).getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String propName = rsmd.getColumnName(i + 1); // 列名
				String javaType = JDBC2JT(rsmd.getColumnClassName(i + 1)); // 类型
				map.put(propName, javaType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	// 返回JDBC建议类型转换后的类型
	public static String JDBC2JT(String cType) {
		if (cType.equals("java.lang.Integer")) {
			return "int";
		} else if (cType.equals("java.sql.Timestamp") || cType.equals("java.sql.Date")) {
			return "Timestamp";
		} else if (cType.equals("java.sql.Double")) {
			return "double";
		} else if (cType.equals("java.sql.Long")) {
			return "long";
		} else {
			return "String";
		}
	}
	
	
	
	

	
	
	
	
}
