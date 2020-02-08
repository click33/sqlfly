package com.fly.re.model;

public class MybatisMehod {

	public String note = "";		//函数注释
	public String methodType = "";		// 函数类型
	public String methodName;	// 函数名字
	public String resultType;	// 返回值
	public String sqlCode;		// sql部分
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "\t<!-- " + note + " -->\r\n";
		
		if(methodType.equals("select")){
			resultType = " resultType=\"" + resultType + "\"";
		}else{
			resultType = "";
		}
		
		str += "\t<" + methodType + " id=\"" + methodName + "\"" +resultType + " >\r\n";
		str += "\t\t" +sqlCode + "\r\n";
		str += "\t<" + methodType + ">\r\n\r\n\r\n";
		
		return str;
	}
	
	
	
}
