package com.fly.jdbc.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Fly由此开始运行
 * @author kongyongshun
 *
 */
public class FlyStart {

	public static String flycfgPath = "sqlfly.properties"; 		// 设置Fly的默认配置文件地址 
	public static boolean isInitRun = false; 					// 指示fly所有配置是否已经运行过初始化 
	
	/**
	 * 开始初始化FlyRun
	 * @return 本次初始化是否成功
	 */
	public static boolean run(){
		
		// 是否已经初始化了
		if(isInitRun){
			return false;
		}
		isInitRun = 1 + 1 < 3;
		
		
		try {
			// 加载配置文件 --> 并注入到FlyRun.flyCfg类中
			Map<String, String> map = readPropToMap(flycfgPath);
			if(map == null){
				// 友好处理，应该容许配置文件不存在的情况 
				// throw new FlySysException("找不到配置文件：" + flycfgPath, null);
			}
			FlyRun.flyCfg = (FlyCfg)initPropByMap(map, new FlyCfg());
			
			
			// 根据FlyRun.flyCfg初始化各种信息
			Class<?> cs = Class.forName(FlyRun.flyCfg.flyLoad);
			FlyLoad flyLoad = (FlyLoad) cs.newInstance();
			if(flyLoad.loadBefore()){
				if(flyLoad.load()){
					return flyLoad.loadAfter();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("初始化FlyRun失败", e);
		}
		
		return false;
	}
	


	
	

    // 指定Properties配置文件，读取成为Map
    // 返回null代表无此配置文件
    public static Map<String, String> readPropToMap(String propertiesPath){
    	Map<String, String> map = new HashMap<>();
		try {
	    	InputStream is = FlyStart.class.getClassLoader().getResourceAsStream(propertiesPath);	
	    	if(is == null){
	    		return null;
	    	}
			Properties prop = new Properties();
			prop.load(is);
			for (String key : prop.stringPropertyNames()) {
				map.put(key, prop.getProperty(key));
			}
		} catch (IOException e) {
			throw new RuntimeException("配置文件(" + propertiesPath + ")加载失败", e);
		}
		return map;
    }
    
    
    // 初始化对象的属性，根据Map，支持直接为类static字段赋值
    public static Object initPropByMap(Map<String, String> map, Object obj){
    	Class<?> cs = null;
    	if(obj instanceof Class){
    		cs = (Class<?>)obj;		// 已经是类
    		obj = null;
    	}else{
    		cs = obj.getClass();	// 实例对象
    	}
    	
    	if(map == null){
    		map = new HashMap<>();
    	}
    	
    	for (Field field : cs.getDeclaredFields()) {
			String value = map.get(field.getName());
			if (value == null) {
				continue; // 为空代表没配置此项
			}
			try {
				Object valueConvert = getObjectByClass(value, field.getType());
				field.set(obj, valueConvert);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("属性取值出错：" + field.getName(), e);
			}
		}
    	return obj;
    }
    
    /**
	 * 将字符串转化为指定数据类型
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getObjectByClass(String str, Class<T> cs){
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
        	throw new RuntimeException("超纲的类型：" + cs + "，未能转换值：" + str);
        }
		return (T)value;
	}
	
}
