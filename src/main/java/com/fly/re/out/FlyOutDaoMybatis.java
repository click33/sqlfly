package com.fly.re.out;

import java.util.Arrays;
import java.util.List;

import com.fly.re.model.Column;
import com.fly.re.model.MkClass;
import com.fly.re.model.MybatisMehod;
import com.fly.re.model.Table;

/**
 * 生成 -- mybatis版
 * @author kongyongshun
 *
 */
public class FlyOutDaoMybatis extends FlyOutDaoDefault implements FlyOutDao  {
	
	

	
	@Override
	public String mkSave(Table table){
		String tableName = table.name;
		MybatisMehod me = new MybatisMehod();
		me.note = "增";
		me.methodType = "insert";
		me.methodName = "save";
		
		List<String> columnList = table.getColumnListString();
		String sk = "", sv = ""; // 列部分，值部分，参数部分
		for (String columnName : columnList) {
			sk += columnName + ", ";
			sv += "#{" + columnName + "}, ";
		}
		me.sqlCode = "insert into \r\n\t\t" + tableName + "(" + OutUtil.strLastDrop(sk, 2) +
				") \r\n\t\tvalues (" + OutUtil.strLastDrop(sv, 2) + ")";
		return me.toString();
	}
	public String mkDelete(Table table){
		String tableName = table.name;
		MybatisMehod me = new MybatisMehod();
		me.note = "删";
		me.methodType = "delete";
		me.methodName = "delete";
		me.sqlCode = "delete from " + tableName +  " where " + table.pk + " = #{" + table.pk + "}";
		return me.toString();
	}
	public String mkUpdate(Table table){
		String tableName = table.name;
		MybatisMehod me = new MybatisMehod();
		me.note = "改";
		me.methodType = "update";
		me.methodName = "update";
		
		List<String> columnList = table.getColumnListString();
		String sk = ""; // set参部分
		for (String columnName : columnList) {
			if (columnName.equals(table.pk)) {
				continue;
			}
			sk += "\t\t" + columnName + " = #{" + columnName + "}, \r\n";
		}
		me.sqlCode = "update " + tableName + " set \r\n" + OutUtil.strLastDrop(sk, 4) + " \r\n\t\twhere " + table.pk + "=#{" + table.pk + "}"; // 再加个主键
		return me.toString();
	}
	public String mkGetById(Table table){
		String tableName = table.name;
		MybatisMehod me = new MybatisMehod();
		me.note = "查";
		me.methodType = "select";
		me.methodName = "getById";
		me.resultType = codeCfg.packagePath + ".model." + table.getClassName();
		me.sqlCode = "select * from " + tableName +  " where " + table.pk + " = #{" + table.pk + "}";
		return me.toString();
	}
	public String mkGetList(Table table){
		
		String tableName = table.name;
		MybatisMehod me = new MybatisMehod();
		me.note = "查询，根据条件(参数为null或0时默认忽略此条件)";	// 方法注释
		me.methodType = "select";
		me.methodName = "getById";
		me.resultType = codeCfg.packagePath + ".model." + table.getClassName();
		me.sqlCode = "select * from " + tableName +  " where 1=1 \r\n";
		
		for (Column column : table.columnList) {
			String tj = "";
			if(Arrays.asList("int", "long").contains(column.javaType)){
				tj = "\"" + column.name + " != 0\"";
			}else{
				tj = "'" + column.name + " != null and " + column.name + " != \"\" '";
			}
			me.sqlCode += "\t\t<if test=" + tj + ">\r\n";
			me.sqlCode += "\t\t\t" + column.name + " = #{" + column.name + "}\r\n";
			me.sqlCode += "\t\t</if>\r\n";
		}
		
		return me.toString();
	}
	
	
	@Override
	public String mkDaoI(Table table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		mc.packageInfo = codeCfg.packagePath + ".dao;";	// 包信息
		mc.classNotes = "Dao: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "interface " + className + "Dao";
		
		mc.importList.add("import java.util.*;\r\n");
		mc.importList.add("import "+ OutUtil.path_fly_util +".*;");	// fly.util
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so

		mc.methodList.add("\tpublic int save(" + className + " obj);\r\n");		// 新增
		mc.methodList.add("\tpublic int delete(long id);\r\n");		// 删除
		mc.methodList.add("\tpublic int update(" + className + " obj);\r\n");		// 修改
		mc.methodList.add("\tpublic " + className + " getById(long id);\r\n");		// 查询，根据id
		mc.methodList.add("\tpublic List<" + className + "> getList(" + className + "SO so);\r\n");		// 查询集合
		
		return mc.toString();
	}
	@Override
	public String mkDaoImpl(Table table) {
		
		String className = table.getClassName();
		
		String str = "";
		str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		str += "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n";
		str += "<mapper namespace=\"" + codeCfg.packagePath + ".dao."+ className  +"Dao\">\r\n\r\n\r\n";
		
		// 所有函数
		str += mkSave(table);
		str += mkDelete(table);
		str += mkUpdate(table);
		str += mkGetById(table);
		str += mkGetList(table);
		
		str += "</mapper>\r\n";
		
		return str;
	}
	
	

	@Override
	public String mkIO() {

		System.out.println("\n\n===============  接口与实现类 共计：(" + codeCfg.tableList.size() + ") ======================");
		for (Table table : codeCfg.tableList) {
			String className = OutUtil.wordFirstBig(table.name);
			
			OutUtil.outFile(codeCfg.getIOPath() + "/dao/" + className + "Dao.java", mkDaoI(table));
			OutUtil.outFile(codeCfg.getIOPath() + "/dao/impl/" + className + "Mapper.xml", mkDaoImpl(table));

			OutUtil.outFile(codeCfg.getIOPath() + "/service/" + className + "Service.java", mkServiceI(table));
			OutUtil.outFile(codeCfg.getIOPath() + "/service/impl/" + className + "ServiceImpl.java",mkServiceImpl(table));

			OutUtil.outFile(codeCfg.getIOPath() + "/controller/api/" + className + "Controller.java",mkController(table));
			System.out.println(table.name + "\t --> Dao、Service、Controller 写入成功！");
		}

		System.out.println("\n\n=============== 工厂类生成 ======================");
		OutUtil.outFile(codeCfg.getIOPath() + "/dao/FC.java", mkDaoFactory());
		System.out.println("FC.java\t --> 写入成功");
		OutUtil.outFile(codeCfg.getIOPath() + "/service/FS.java", mkServiceFactory());
		System.out.println("FS.java\t --> 写入成功");

		return null;
	}
	
	
	@Override
	protected String getServiceGetList(String className, String varName){
		return "\tpublic AjaxJson getList(" + className + "SO so){\r\n"
				+ "\t\tcom.github.pagehelper.Page<?> pagePlug = com.github.pagehelper.PageHelper.startPage(so.getPageNo(), so.getPageSize());\r\n"
				+ "\t\tList<?> list = FC." + varName + "Dao.getList(so);\r\n"
				+ "\t\tPage page = new Page(so.getPageNo(), so.getPageSize()).setCount(pagePlug.getTotal());\r\n"
				+ "\t\treturn AjaxJson.getPageData(page, list);\r\n"
				+ "\t}\r\n";
	}
	
	
}
