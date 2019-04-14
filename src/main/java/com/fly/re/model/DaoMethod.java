package com.fly.re.model;

/**
 * Dao层模板函数
 * @author kongyongshun
 *
 */
public class DaoMethod {

	
	public String note = "";	// 注释
	public boolean isOverride;	// 是否加上@Override注解
	public String returnType;	// 返回值
	public String methodName;	// 函数名字
	public String shapeCode;	// 形参语句
	public String sqlCode;		// sql部分
	public String argsCode;	// args部分
	public String returnCode;	// sql部分
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "\t// " + note + "\r\n";
		
		if(isOverride){
			str += "\t@Override\r\n";
		}
		
		str += "\tpublic " + returnType + " " + methodName + "(" + shapeCode + "){\r\n";	// 方法头
		str += "\t\tString sql = \"" + sqlCode + "\";\r\n";		// sql
		str += "\t\tObject[] args = {" + argsCode + "};\r\n";		// 参数
		str += "\t\treturn " + returnCode + ";\r\n";		// 参数
		
		str += "\t}\r\n";
		
		return str;
	}
	
	
	
	
}
