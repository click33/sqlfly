package com.fly.re.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fly.jdbc.FlyUtil;

/**
 * 代码生成结构类
 * @author kongyongshun
 */
public class MkClass {
	
	
	public String packageInfo = "";								// 此类的包信息, 例如：com.www.model
	public String classNotes = "";								// 类注释
	public String classAuthor = "";								// 类作者
	public String className = "";								// 类名字 带上class或interface关键字
	public String extendsName = "";								// 类名字 带上class或interface关键字
	public String implementsName = "";								// 类名字 带上class或interface关键字
	
	public Set<String> importList = new LinkedHashSet<>();		// 导包集合	例如：import java.io.Serializable;
	public Set<String> attList = new LinkedHashSet<>();		// 注解集合	例如：@Data
	public Set<String> fieldList = new LinkedHashSet<>();		// 字段集合	例如：\tprivate static final long serialVersionUID = 1L;\r\n
	public List<String> methodList = new ArrayList<>();		// 方法集合	例如：例如个毛线
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// 包
		String str = "package " + packageInfo + ";\r\n\r\n";		
		
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
		str += "public " + className;	
		if(FlyUtil.isNull(extendsName) == false) {
			str += " extend " + extendsName;
		}
		if(FlyUtil.isNull(implementsName) == false) {
			str += " implements " + implementsName;
		}
		str += " {\r\n\r\n";
		
		// 所有字段
		for (String s : fieldList) {
			str += s + "\r\n";
		}
		str += "\r\n\r\n";
		
		// 所有方法
		for (String s : methodList) {
			str += s + "\r\n";
		}
		str += "\r\n\r\n";
		
		// 收尾
		str += "\r\n}\r\n";
		
		return str;
	}
	
	
	
	
	
	

}
