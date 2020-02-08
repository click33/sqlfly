package com.fly.jdbc.mapping;

import java.util.List;

public class AtFlyClass {

	
	public Class<?> cs;					// 类型
	public boolean isBaseType;			// 是否为基础类型
	public List<AtFlyField> affList;	// 包含的字段集合
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AtFlyClass [cs=" + cs + ", isBaseType=" + isBaseType + ", affList=" + affList + "]";
	}
	
	
	
	
}
