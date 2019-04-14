package com.fly.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fly.jdbc.exception.FlySysException;



/**
 * @AtFly读取专用
 * @author kongyongshun
 *
 */
public class AtFlyReadUtil {

	
	
	// 缓存类型读取方式，以削减频繁反射造成的性能消耗
	private static Map<Class<?>, AtFlyClass> AtFlyClassMap = new HashMap<Class<?>, AtFlyClass>();

	// 创建指定类型的缓存
	private static void setAtFlyCache(Class<?> cs) {
		
		AtFlyClass afc = new AtFlyClass();
		afc.cs = cs;
		afc.isBaseType = isBaseType(cs);
		
		if(afc.isBaseType == false){
			
			// 1、如果非基础类型，则开始解剖属性
			List<AtFlyField> affList = new ArrayList<AtFlyField>();
			for (Field field : cs.getDeclaredFields()) {

				// 忽略serialVersionUID字段
				if(field.getName().equals("serialVersionUID") == true){
					continue;
				}
				
				// 2、先取出默认值
				AtFlyField aff = new AtFlyField();
				field.setAccessible(true); 		// 打开属性穿透
				aff.field = field;	 	// 属性
				aff.orm = true;
				aff.column = field.getName(); // 对应列
				
				// 3、检查是否具有@AtFly注解加持
				AtFly at = field.getAnnotation(AtFly.class);
				if (at != null) {
					aff.orm = at.orm();
				}
				
				// 4、如果该属性没有orm=false禁掉的话，进一步解析
				if(aff.orm && at != null){
					// 是否有列转换
					if (at.column().equals("") == false) {
						aff.column = at.column();
					}
					// 是否有特殊set
					if (at.set().equals("") == false) {
						aff.set = at.set();
						for (Method method : cs.getDeclaredMethods()) {
							if (method.getName().equals(aff.set)) {
								aff.setMethod = method;
								break;
							}
						}
						throw new FlySysException("在类型(" + cs + ")中找不到指定函数:" + aff.set, null);
					}
				}
				
				// 5、进一步解析字段的AtFlyClass属性
				if(aff.orm){
					aff.afc = getAtFlyCache(aff.field.getType());	// 递归解析
				}
				
				
				affList.add(aff);
			}
			afc.affList = affList;
		}
		
		AtFlyClassMap.put(cs, afc);
	}
	
	
	// 获得指定类型的注解缓存，没有就读取创建一个
	public static AtFlyClass getAtFlyCache(Class<?> cs) {
		if (AtFlyClassMap.containsKey(cs) == false) {
			setAtFlyCache(cs);
		}
		return AtFlyClassMap.get(cs);
	}
		
		
		
		
		

	// 判断一个类型是否为8大基本数据类型
	public static boolean isBaseType(Class<?> cs) {
		return (
				cs.isPrimitive() || 
				cs.equals(String.class) || 
				cs.equals(Integer.class) || 
				cs.equals(Byte.class) || 
				cs.equals(Long.class) || 
				cs.equals(Double.class) || 
				cs.equals(Float.class) || 
				cs.equals(Character.class) || 
				cs.equals(Short.class) || 
				cs.equals(BigDecimal.class) || 
				cs.equals(BigInteger.class) || 
				cs.equals(Boolean.class) || 
				cs.equals(java.sql.Date.class) || 
				cs.equals(java.util.Date.class) || 
				cs.equals(java.sql.Timestamp.class)
		);
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
