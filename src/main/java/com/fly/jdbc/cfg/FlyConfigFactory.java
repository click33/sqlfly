package com.fly.jdbc.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fly.jdbc.exception.FlySysException;

/**
 * Fly由此开始运行
 * @author kongyongshun
 *
 */
public class FlyConfigFactory {

	
	

	/**
	 * 根据指定路径获取配置信息 
	 * @param flycfgPath 
	 * @return 一个FlyConfig对象 
	 */
	public static FlyConfig createConfig(String configPath) {
		Map<String, String> map = readPropToMap(configPath);
		if(map == null){
			throw new FlySysException("找不到配置文件：" + configPath, null);
		}
		return (FlyConfig)initPropByMap(map, FlyConfig.class);
	}
	
	

	/**
	 * 将指定路径的properties配置文件读取到Map中 
	 * @param propertiesPath 配置文件地址
	 * @return 一个Map
	 */
    private static Map<String, String> readPropToMap(String propertiesPath){
    	Map<String, String> map = new HashMap<>();
		try {
	    	InputStream is = FlyConfigFactory.class.getClassLoader().getResourceAsStream(propertiesPath);	
	    	if(is == null){
	    		return null;
	    	}
			Properties prop = new Properties();
			prop.load(is);
			for (String key : prop.stringPropertyNames()) {
				map.put(key, prop.getProperty(key));
			}
		} catch (IOException e) {
			throw new FlySysException("配置文件(" + propertiesPath + ")加载失败", e);
		}
		return map;
    }
    
    
    /**
     * 将 Map 的值映射到 Model 上 
     * @param map 属性集合  
     * @param obj 对象，或类型 
     * @return 返回实例化后的对象 
     */
    private static Object initPropByMap(Map<String, String> map, Object obj){
    	
    	if(map == null){
    		map = new HashMap<>();
    	}
    	
    	// 1、取出类型 
    	Class<?> cs = null;
    	if(obj instanceof Class){	// 如果是一个类型，则将obj=null，以便完成静态属性反射赋值  
    		cs = (Class<?>)obj;		
    		obj = null;
    	}else{	// 如果是一个对象，则取出其类型  
    		cs = obj.getClass();	
    	}
    	
    	// 2、遍历类型属性，反射赋值 
    	for (Field field : cs.getDeclaredFields()) {
			String value = map.get(field.getName());
			if (value == null) {
				continue;	// 如果为空代表没有配置此项
			}
			try {
				Object valueConvert = getObjectByClass(value, field.getType());	// 转换值类型 
				field.setAccessible(true);
				field.set(obj, valueConvert);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new FlySysException("属性赋值出错：" + field.getName(), e);
			}
		}
    	return obj;
    }
    
    /**
     * 将字符串转化为指定数据类型 
     * @param str 值 
     * @param cs 要转换的类型 
     * @return 
     */
	@SuppressWarnings("unchecked")
	private static <T>T getObjectByClass(String str, Class<T> cs){
		Object value = null;
		if(str == null){
			value = null;
		}else if (cs.equals(String.class)) {
			value = str;
		} else if (cs.equals(int.class)||cs.equals(Integer.class)) {
			value = new Integer(str);
        } else if (cs.equals(long.class)||cs.equals(Long.class)) {
        	value = new Long(str);
        } else if (cs.equals(short.class)||cs.equals(Short.class)) {
        	value = new Short(str);
        } else if (cs.equals(float.class)||cs.equals(Float.class)) {
        	value = new Float(str);
        } else if (cs.equals(double.class)||cs.equals(Double.class)) {
        	value = new Double(str);
        } else if (cs.equals(boolean.class)||cs.equals(Boolean.class)) {
        	value = new Boolean(str);
        }else{
			throw new FlySysException("未能将值：" + str + "，转换类型为：" + cs, null);
        }
		return (T)value;
	}
	
	
	
	
}
