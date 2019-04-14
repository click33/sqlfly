package com.fly.re.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成结构类
 * @author kongyongshun
 */
public class MkClass {
	
	
	public String packageInfo = "";								// 此类的包信息
	public String className = "";								// 类名字 带上class或interface关键字
	public String classNotes = "";								// 类注释
	public String classAuthor = "";								// 类作者
	
	public List<String> importList = new ArrayList<>();		// 导包集合
	public List<String> attList = new ArrayList<>();		// 注解集合
	public List<String> fieldList = new ArrayList<>();		// 字段集合
	public List<String> methodList = new ArrayList<>();		// 方法集合
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// 包
		String str = "package " + packageInfo + "\r\n\r\n";		
		
		// 导包
		for (String s : importList) {
			str += s + "\r\n";
		}
		str += "\r\n";		
		
		// 注释
		str += "/**\r\n * " + classNotes + "\r\n * @author " + classAuthor + "\r\n */\r\n";	
		
		// 注解
		for (String s : attList) {
			str += s + "\r\n";
		}
		
		// 类
		str += "public " + className + " {\r\n\r\n";	
		
		// 所有字段
		for (String s : fieldList) {
			str += s + "\r\n";
		}
		str += "\r\n";
		
		// 所有方法
		for (String s : methodList) {
			str += s + "\r\n";
		}
		str += "\r\n";
		
		// 收尾
		str += "\r\n}\r\n";
		
		return str;
	}
	
	
	
	
	
	

}
