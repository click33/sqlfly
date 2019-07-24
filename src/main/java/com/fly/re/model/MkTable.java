package com.fly.re.model;

import java.util.ArrayList;
import java.util.List;

import com.fly.re.out.OutUtil;

/**
 * 一个表
 * @author kongyongshun
 *
 */
public class MkTable {

	
	public String name = "";			// 表名字
	public String comment;		// 表注释
	public String pk = "";		// 主键列
	
	public List<Column> columnList;	// 列集合 

	
	/**
	 * 范回列的String形式
	 * @return
	 */
	public List<String> getColumnListString() {
		List<String> sList = new ArrayList<>();
		for (Column column : columnList) {
			sList.add(column.name);
		}
		return sList;
	}
	
	

	/**
	 * @param name the name to set
	 */
	public MkTable setName(String name) {
		this.name = name;
		return this;
	}
	
	// 返回表名，小写形式
	public String getNameSmall() {
		return name.toLowerCase();
	}
	
	// 返回表名转类名(下划线转大写字母)
	public String getClassName(){
		return OutUtil.wordEachBig(name);
	}
	
	// 返回Dao名
	public String getDaoName() {
		return getClassName() + "Dao"; 
	}
	// 返回Service名
	public String getServiceName() {
		return getClassName() + "Service"; 
	}
//	// 返回Dao名 变量形式 
//	public String getDaoName() {
//		return getClassName() + "Dao"; 
//	}
//	// 返回Service名 变量形式 
//	public String getServiceName() {
//		return getClassName() + "Service"; 
//	}
//	
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Table [name=" + name + ", comment=" + comment + ", columnList=" + columnList + "]";
	}

	
	
}
