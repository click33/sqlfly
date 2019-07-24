package com.fly.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fly.jdbc.cfg.FlyConsts;


/**
 * Fly,让代码飞起来
 * <p>作为工具类，Fly提供了SqlFly操作中一些常用的函数，虽然这些函数功能简单，但它们的确可以使你的代码更加优雅 
 */
public class FlyUtil {
	
	
	
	
	/**
	 * 快速组装一个Object[]
	 */
	public static Object[] doArrayObj(Object... arg){
		return arg;
	}
	
	/**
	 * 快速组装一个String[]
	 */
	public static String[] doArrayStr(String... arg){
		return arg;
	}
	
	/**
	 * 快速组装一个List< Object >
	 */
	public static List<Object> doListObj(Object... args){
		List<Object>list=new ArrayList<Object>();
		for (Object object : args) {
			list.add(object);
		}
		return list;
	}
	
	/** 快速组装一个List< String > */
	public static List<String> doListStr(String... args){
		List<String>list=new ArrayList<String>();
		for (String str : args) {
			list.add(str);
		}
		return list;
	}
	
	/** 快速组装一个Map< String,String >集合  */
	public static Map<String, String> doMapStrStr(String... args){
		Map<String, String>map=new HashMap<String, String>();
		for (int i=1;i<args.length;i+=2) {
			map.put(args[i-1], args[i]);
		}
		return map;
	}

	/**
	 * <h1>快速组装一个Map< S,O >集合</h1>
	 */
	public static Map<String, Object> doMapStrObj(Object... args){
		Map<String, Object>map=new HashMap<String, Object>();
		for (int i=1;i<args.length;i+=2) {
			map.put(args[i-1].toString(), args[i]);
		}
		return map;
	}
	
	/**
	 * 在指定数组的末尾追加元素(此方法会新建数组，因此返回的数组并不是原来的数组)
	 * <br/>
	 */
	public static Object[]ArrayAdd(Object[]array,Object...args){
		Object[] newArray=new Object[array.length+args.length];
		System.arraycopy(array, 0, newArray, 0, array.length);
		for (int i = 0; i < args.length; i++) {
			newArray[array.length+i]=args[i];
		}
		return newArray;
	}
	
	
	
	
	private static Map<Class<?>, Object>singleMap=new HashMap<Class<?>, Object>();
	/** 返回指定类型的单例对象 */
	@SuppressWarnings("unchecked")
	public synchronized static<T>T getClassSingle(Class<T>cs){
		if(singleMap.get(cs)==null) {
			try {
				singleMap.put(cs, cs.newInstance());
			} catch (InstantiationException e) {
				throw new RuntimeException("无法实例化类型："+cs,e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("无法实例化类型："+cs,e);
			}
		}
		return (T)singleMap.get(cs);
	}
	
	
	/** 返回分页后的集合，内存分页 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static List getListPage(List list, Page page) {
//		List newList = new ArrayList();
//		if (page == null) {
//			return newList;
//		}
//		int start = page.getStart(); // 开始数
//		int pz = page.getPageSize(); // 页大小
//		page.setCount(list.size());
//		for (int i = start; i < start + pz; i++) {
//			if (i >= list.size()) {
//				break;
//			}
//			newList.add(list.get(i));
//		}
//		return newList;
//	}
	
	
	/**
	 * 该字符串是否为null或者空串
	 */
	public static boolean isNull(Object str) {
		return (str == null || str.equals(""));
	}
	
	
	
	/* * * * * * * * * * * * * 动态Sql辅助 * * * * * * * * * * * * * * * * * * * * */
	
	/** 生成指定个数?占位符，in查询专用，例如参数为3时，返回：(?, ?, ?) */
	public static String toIn(int num) {
		if (num > 0) {
			StringBuffer sb = new StringBuffer("(?");
			for (int i = 1; i < num; i++) {
				sb.append(", ?");
			}
			return sb.append(") ").toString();
		}
		return "(?) ";
	}
	
	
	
	
	
	
	// 打印 SqlFly
	@SuppressWarnings("unused")
	public static void printSqlFly() {
		String a = "" + 
				"   _____           _   ______   _         \n" + 
				"  / ____|         | | |  ____| | |        \n" + 
				" | (___     __ _  | | | |__    | |  _   _ \n" + 
				"  \\___ \\   / _` | | | |  __|   | | | | | |\n" + 
				"  ____) | | (_| | | | | |      | | | |_| |\n" + 
				" |_____/   \\__, | |_| |_|      |_|  \\__, |\n" + 
				"              | |                    __/ |\n" + 
				"              |_|                   |___/ \n" + 
				" SqlFly     " + FlyConsts.V + "         ";
		String b = "____ ____ _    ____ _    _   _ \n" + 
				"[__  |  | |    |___ |     \\_/  \n" + 
				"___] |_\\| |___ |    |___   |   \n" + 
				"SqlFly     " + FlyConsts.V + "         \n";
		String str = b;
		System.out.println(str);
	}
	
	
	
	
	
}


