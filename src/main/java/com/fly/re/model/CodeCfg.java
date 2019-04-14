package com.fly.re.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fly.jdbc.FlyFactory;
import com.fly.jdbc.SqlFly;

/**
 * 总配置
 * @author kongyongshun
 *
 */
public class CodeCfg {

	public SqlFly sqlFly = FlyFactory.getFly();	// 默认SqlFly
	
	public String projectPath;		// 项目路径
	public String packagePath;		// 包路径
	public String author = "";			// 生成的代码作者名字
	public int fieldType = 1;		// 对表字段的处理方式（1=转小写，2=转大写，0=不变）

	public List<Table> tableList = new ArrayList<>();	// 检索出的表集合 
	
	
	// 返回IO的主目录
	public String getIOPath(){
		String path = new File(projectPath, packagePath.replace(".", "\\")).getAbsolutePath();
		return path;
	}
	
	
	
	
	
	
	
	
	
	
}
