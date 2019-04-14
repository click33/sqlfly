package com.fly.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 与@AtFly相对应的实体类
 * @author kongyongshun
 *
 */
public class AtFlyField {

	public Field field;				// 代表的字段
	public boolean orm = true;		// 是否接受映射
	public String column; 			// 对应table列
	public String set;				// 特殊set
	public Method setMethod;			// 特殊set下的反射method
	
	public AtFlyClass afc;		// 所属于的AtFlyClass对象

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AtFlyField [field=" + field + ", orm=" + orm + ", column=" + column + ", set=" + set + ", setMethod="
				+ setMethod + ", afc=" + afc + "]";
	}
	
	
	
	
	
	
}
