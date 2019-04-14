package com.fly.re.out;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * String处理工具包
 * @author kongyongshun
 *
 */
public class OutUtil {


	// 防止 一次有变，到处乱改
	static String path_fly_jdbc = "com.fly.jdbc";

	static String fly = "Fly";
	static String fly_run = "FlyRun";
	static String fly_code = "FlyCode";
	static String sql_fly = "SqlFly";
	static String fly_factory = "FlyFactory";
	
	
	static String path_fly_util = "com.fly.util";
	static String ajax_json = "AjaxJson";
	
	
	// 输出指定字符串
	static void print(String str){
		System.out.print(str);
	}
	

	// 指定地址，写入指定内容
	static void outFile(String filePath, String txt){
		File file = new File(filePath);
		File fileDir = new File(file.getParent());
		if(fileDir.exists() == false){
			new File(file.getParent()).mkdirs();
		}
		try {
			file.createNewFile();
			Writer fw = new FileWriter(file.getAbsolutePath());
			fw.write(txt);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 将指定单词首字母大写;
	static String wordFirstBig(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	// 将指定单词首字母小写;
	static String wordFirstSmall(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}
	
	// 去掉字符串最后一个字符
	static String strLastDrop(String str) {
		try {
			return str.substring(0, str.length() - 1);
		} catch (StringIndexOutOfBoundsException e) {
			return str;
		}
	}
	
	// 去掉字符串最后x个字符
	static String strLastDrop(String str, int x) {
		try {
			return str.substring(0, str.length() - x);
		} catch (StringIndexOutOfBoundsException e) {
			return str;
		}
	}
	
	// 单词大小写转换
	// way=方式(1转小写 2转大写，其它不变)
	static String wordChangeBigSmall(String str, int way) {
		if (way == 1) {
			str = str.toLowerCase();
		} else if (way == 2) {
			str = str.toUpperCase();
		}
		return str;
	}
	
	// 快速组织文档注释,三行,一缩进
	static String getDoc(String str) {
		return "\t/**\r\n\t * " + str + "\r\n\t */\r\n";
	}
	
	// 指定字符串的getter形式
	static String getSetGet(String str) {
		if (str == null || str.equals("")) {
			return str;
		}
		if (str.length() == 1 || str.charAt(1) == '_' || str.charAt(1) == '$') {
			return wordFirstBig(str);
		}
		if (Character.isLowerCase(str.charAt(0)) && Character.isLowerCase(str.charAt(1))) {
			return wordFirstBig(str);
		} else {
			return str;
		}
	}
	
	// 指定字符串的字符串下划线转大写模式
	public static String wordEachBig(String str){
		String newStr = "";
		for (String s : str.split("_")) {
			newStr += wordFirstBig(s);
		}
		return newStr;
	}
	
	
	// 指定包的Spring工厂类
	public static String SpringBeanFC(String projectPath, String packagePath, String fcName){
		File wjj = new File(projectPath, packagePath.replace(".", "\\")); // 创建路径
		String[] classNameArray = wjj.list();
		
		String _package = "package " + packagePath + ";\r\n\r\n";
		String _import = "\r\nimport org.springframework.beans.factory.annotation.Autowired;\r\n";
		_import += "import org.springframework.stereotype.Component;\r\n\r\n";
		String fc = _package + _import + "/**\r\n* 工厂类\r\n*/\r\n@Component\r\n" + "public class " + fcName + "{\r\n\r\n\r\n"; // 工厂类
		
		for (String className : classNameArray) {
			try{
				if(className.indexOf(".java")==-1){
					continue;
				}
				className = className.replace(".java","");
				String Xxx = wordFirstBig(className);	//大写形式
				String xXX = wordFirstSmall(className);	//小写形式
				fc += "\t/**  */\r\n";
				fc += "\tpublic static "+className+" "+xXX+";\r\n";
				fc += "\t@Autowired\r\n";
				fc += "\tpublic void set"+Xxx+"("+Xxx+" "+xXX+") {\r\n";
				fc += "\t\t" + fcName + "."+xXX+" = "+xXX+";\r\n";
				fc += "\t}\r\n\r\n\r\n";
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		fc += "}";
		
		return fc;
	}
	
	
}
