package com.fly.re.model;

/**
 * 一个列
 */
public class Column {

	
	
	public String name;		// 列名字
	public String comment;		// 字段注释
	public String dbType;		// 数据库类型
	public String javaType="";	// 对应的java类型
	
	
	
	
	public Column() {
	}
	public Column(String name, String comment, String dbType, String javaType) {
		super();
		this.name = name;
		this.comment = comment;
		this.dbType = dbType;
		this.javaType = javaType;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Column [name=" + name + ", comment=" + comment + ", dbType=" + dbType + ", javaType=" + javaType + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
