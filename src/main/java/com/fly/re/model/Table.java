package com.fly.re.model;

import java.util.ArrayList;
import java.util.List;

import com.fly.re.out.OutUtil;

/**
 * 一个表
 * @author kongyongshun
 *
 */
public class Table {

	
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
	 * 返回列的so形式
	 */
	public List<Column> getColumnListSO(){
		List<Column> sList = new ArrayList<>();
		sList.add(new Column("pageNo", "当前页", "", "int"));
		sList.add(new Column("pageSize", "页大小", "", "int"));
		sList.addAll(columnList);
		return sList;
	}
	

	/**
	 * @param name the name to set
	 */
	public Table setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 返回表名转类名(下划线转大写字母)
	 */
	public String getClassName(){
		return OutUtil.wordEachBig(name);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Table [name=" + name + ", comment=" + comment + ", columnList=" + columnList + "]";
	}

	
	
}
