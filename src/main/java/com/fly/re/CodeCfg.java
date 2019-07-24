package com.fly.re;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fly.jdbc.SqlFly;
import com.fly.jdbc.SqlFlyFactory;
import com.fly.re.model.MkTable;

/**
 * 总配置，
 * 
 * @author kong 一路向下，古来征战几人回
 */
public class CodeCfg {

	public SqlFly sqlFly = SqlFlyFactory.getSqlFly(); // 默认SqlFly对象

	public String projectPath = ""; // 项目路径
	public String codePath = "src/main/java/"; // 代码路径
	public String packagePath = ""; // 包路径
	public String docPath = "doc/"; // 文档生成路径 
	
	public String author = ""; // 生成的代码作者名字
	public String package_ajaxjson = "com.fly.jdbc.util"; // AjaxJson类的地址，在代码生成时会用到
	public String class_ajaxjson = "AjaxJson"; // AjaxJson类的名字
	public int fieldType = 1; // 对数据库表字段的处理方式（1=转小写，2=转大写，0=不变）

	public Boolean is_lomock = false; // 是否使用 lomock
	public Boolean is_three = true; // 是否标准三层模式

	
	public List<String> tableNameList = new ArrayList<>(); // 要检索的表名字集合
	public List<MkTable> tableList = new ArrayList<>(); // 检索出的表集合

	// 返回IO的主目录
	public String getIOPath() {
		String path = new File(projectPath + codePath, packagePath.replace(".", "\\")).getAbsolutePath() + "\\";
		return path;
	}

	// 返回IO的主目录, 根据指定包名
	public String getIOPath(String packageStr) {
		String path = new File(projectPath + codePath, packageStr.replace(".", "\\")).getAbsolutePath() + "\\";
		return path;
	}
	
	// 返回文档写入地址
	public String getDocIOPath() {
		String path = new File(projectPath).getAbsolutePath() + "\\" + docPath + "\\";
		return path;
	}
	

	// 追加一个表名字
	public CodeCfg addTableName(String... tableNames) {
		for (String tableName : tableNames) {
			if (!tableNameList.contains(tableName))
				tableNameList.add(tableName);
		}
		return this;
	}

	// 一坨坨 get set
	public String getProjectPath() {
		return projectPath;
	}

	public CodeCfg setProjectPath(String projectPath) {
		this.projectPath = projectPath;
		return this;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public CodeCfg setPackagePath(String packagePath) {
		this.packagePath = packagePath;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public CodeCfg setAuthor(String author) {
		this.author = author;
		return this;
	}

	public int getFieldType() {
		return fieldType;
	}

	public CodeCfg setFieldType(int fieldType) {
		this.fieldType = fieldType;
		return this;
	}

	public Boolean getIs_lomock() {
		return is_lomock;
	}

	public CodeCfg setIs_lomock(Boolean is_lomock) {
		this.is_lomock = is_lomock;
		return this;
	}

	public Boolean getIs_three() {
		return is_three;
	}

	public CodeCfg setIs_three(Boolean is_three) {
		this.is_three = is_three;
		return this;
	}

	public String getPackage_ajaxjson() {
		return package_ajaxjson;
	}

	public CodeCfg setPackage_ajaxjson(String package_ajaxjson) {
		this.package_ajaxjson = package_ajaxjson;
		return this;
	}

	public String getClass_ajaxjson() {
		return class_ajaxjson;
	}

	public CodeCfg setClass_ajaxjson(String class_ajaxjson) {
		this.class_ajaxjson = class_ajaxjson;
		return this;
	}

	public String getCodePath() {
		return codePath;
	}

	public CodeCfg setCodePath(String codePath) {
		this.codePath = codePath;
		return this;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

}
