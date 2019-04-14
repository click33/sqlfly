package com.fly.re.out;

import java.util.Arrays;
import java.util.List;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.DaoMethod;
import com.fly.re.model.MkClass;
import com.fly.re.model.Table;

/**
 * 代码生成默认实现类
 * @author kongyongshun
 *
 */
public class FlyOutDaoDefault implements FlyOutDao{

	protected boolean isJK = true;	// 是否在DaoImpl里集成接口
	protected boolean isSpringAtt = true;	// 是否在DaoImpl与ServiceImpl加上Spring扫描注解
	
	// 配置信息
	CodeCfg codeCfg;

	public FlyOutDao setCodeCfg(CodeCfg codeCfg){
		this.codeCfg = codeCfg;
		return this;
	}

	
	
	
	@Override
	public String mkSave(Table table){
		String tableName = table.name;
		String modelName = OutUtil.wordFirstSmall(tableName).substring(0, 1);
		DaoMethod me = new DaoMethod();
		me.isOverride = isJK;
		me.returnType = "int";
		me.methodName = "save";
		me.shapeCode = table.getClassName() + " " + modelName;
		
		List<String> columnList = table.getColumnListString();
		String sk = "", sv = "", sc = ""; // 列部分，值部分，参数部分
		for (String columnName : columnList) {
			sk += columnName + ",";
			sv += "?,";
			sc += modelName + ".get" + OutUtil.getSetGet(columnName) + "(),";
		}
		me.sqlCode = "insert into " + tableName + "(" + OutUtil.strLastDrop(sk) +
				") values (" + OutUtil.strLastDrop(sv) + ")";
		me.argsCode = OutUtil.strLastDrop(sc);
		me.returnCode = "getFly().getUpdate(sql, args)";
		return me.toString();
	}
	public String mkDelete(Table table){
		String tableName = table.name;
		DaoMethod me = new DaoMethod();
		me.isOverride = isJK;
		me.returnType = "int";
		me.methodName = "delete";
		me.shapeCode = "long id";
		me.sqlCode = "delete from " + tableName +  " where " + table.pk + " = ?";
		me.argsCode = "id";
		me.returnCode = "getFly().getUpdate(sql, args)";
		return me.toString();
	}
	public String mkUpdate(Table table){
		String tableName = table.name;
		String modelName = OutUtil.wordFirstSmall(tableName).substring(0, 1);
		DaoMethod me = new DaoMethod();
		me.isOverride = isJK;
		me.returnType = "int";
		me.methodName = "update";
		me.shapeCode = table.getClassName() + " " + modelName;
		

		List<String> columnList = table.getColumnListString();
		String sk = "", sc = ""; // set参部分，args参数
		for (String columnName : columnList) {
			if (columnName.equals(table.pk)) {
				continue;
			}
			sk += columnName + "=?,";
			sc += modelName + ".get" + OutUtil.getSetGet(columnName) + "(), ";
		}
		me.sqlCode = "update " + tableName + " set " + OutUtil.strLastDrop(sk) + " where " + table.pk + "=?"; // 再加个主键
		me.argsCode = OutUtil.strLastDrop(sc, 2);
		me.returnCode = "getFly().getUpdate(sql, args)";
		return me.toString();
	}
	public String mkGetById(Table table){
		String tableName = table.name;
		DaoMethod me = new DaoMethod();
		me.isOverride = isJK;
		me.returnType = table.getClassName();
		me.methodName = "getById";
		me.shapeCode = "long id";
		me.sqlCode = "select * from " + tableName +  " where " + table.pk + " = ?";
		me.argsCode = "id";
		me.returnCode = "getFly().getModel(" + table.getClassName() + ".class, sql,args)";
		return me.toString();
	}
	public String mkGetList(Table table){
		
		String tableName = table.name;
		
		String zhushi = "\t// 查询，根据条件(参数为null或0时默认忽略此条件)\r\n";	// 方法注释
		if(isJK == true){
			zhushi += "\t@Override\r\n";
		}
		String methodHead = "\tpublic List<" + table.getClassName() + 
				"> getList(Page page, " + table.getClassName() + "SO so){\r\n";
		String methodCode = "\t\tSqlExe sqlExe = new SqlExe(\"select * from " + tableName + " where 1=1\")";	// 代码部分
		
		for (Column column : table.columnList) {
			String tj = "";
			String getP = "so.get" + OutUtil.wordFirstBig(column.name) + "()";
			if(Arrays.asList("int", "long").contains(column.javaType)){
				tj = getP + " != 0";
			}else{
				tj = "Fly.isNull(" + getP + ") == false";
			}
			methodCode += "\r\n\t\t\t\t.toIf(" + tj + ", \" and " + column.name + " = ?\", " + getP + ")";
		}
		
		methodCode += ";\r\n\t\treturn getFly().getListPage(page, " + table.getClassName() + ".class, sqlExe);\r\n";
		
		String str = zhushi + methodHead + methodCode + "\t}\r\n";
		return str;
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
		mc.methodList.add("\tpublic List<" + className + "> getList(Page page, " + className + "SO so);\r\n");		// 查询集合
		
		return mc.toString();
	}
	@Override
	public String mkDaoImpl(Table table) {
		
		MkClass mc = new MkClass();
		String className = table.getClassName();
		mc.packageInfo = codeCfg.packagePath + ".dao.impl;";	// 包信息
		mc.classNotes = "Dao: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "class " + className + "DaoImpl";
		
		mc.importList.add("import java.util.*;");
		mc.importList.add("import "+ OutUtil.path_fly_util +".*;\r\n");	// fly.util
		mc.importList.add("import "+ OutUtil.path_fly_jdbc +".*;\r\n");	// fly.jdbc
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so
		if(isJK){
			mc.className += " implements " + className + "Dao";
			mc.importList.add("import " + codeCfg.packagePath + ".dao.*;");
		}
		if(isSpringAtt){
			mc.attList.add("@Component");
			mc.importList.add("import org.springframework.stereotype.Component;");
		}
		
		// 所有函数
		String getfly = "\t//底层SqlFly对象\r\n\tprivate " + OutUtil.sql_fly + " getFly() {\r\n\t\treturn " + OutUtil.fly_factory
				+ ".getFly();\r\n\t}\r\n\r\n";
		mc.methodList.add(getfly);
		mc.methodList.add(mkSave(table));
		mc.methodList.add(mkDelete(table));
		mc.methodList.add(mkUpdate(table));
		mc.methodList.add(mkGetById(table));
		mc.methodList.add(mkGetList(table));
		
		return mc.toString();
	}
	@Override
	public String mkServiceI(Table table) {
		
		MkClass mc = new MkClass();
		String className = table.getClassName();
		mc.packageInfo = codeCfg.packagePath + ".service;";	// 包信息
		mc.classNotes = "Service: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "interface " + className + "Service";

		mc.importList.add("import "+ OutUtil.path_fly_util +".*;\r\n");	// fly.util
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so

		mc.methodList.add("\tpublic long add(" + className + " obj);\r\n");		// 新增
		mc.methodList.add("\tpublic int delete(long id);\r\n");		// 删除
		mc.methodList.add("\tpublic int update(" + className + " obj);\r\n");		// 修改
		mc.methodList.add("\tpublic " + className + " getById(long id);\r\n");		// 查询，根据id
		mc.methodList.add("\tpublic AjaxJson getList(" + className + "SO so);\r\n");		// 查询集合
		
		return mc.toString();
	}
	@Override
	public String mkServiceImpl(Table table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String varName = OutUtil.wordFirstSmall(table.name);
		mc.packageInfo = codeCfg.packagePath + ".service.impl;";	// 包信息
		mc.classNotes = "Service: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "class " + className + "ServiceImpl implements " + className + "Service";
		
		mc.importList.add("import java.util.*;");
		mc.importList.add("import "+ OutUtil.path_fly_util +".*;");	// fly.util
		mc.importList.add("import "+ OutUtil.path_fly_jdbc +".*;\r\n");	// fly.jdbc
		mc.importList.add("import " + codeCfg.packagePath + ".service.*;");		// service接口
		mc.importList.add("import " + codeCfg.packagePath + ".dao.*;");		// Dao接口
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so
		if(isSpringAtt){
			mc.attList.add("@Service");
			mc.importList.add("import org.springframework.stereotype.Service;");
		}
		
		// 所有函数
		String add = "\tpublic long add(" + className + " obj){\r\n"
				+ "\t\ttry{\r\n"
				+ "\t\t\t" + OutUtil.fly_factory + ".getFly().beginTransaction();\r\n"
				+ "\t\t\tFC." + varName + "Dao.save(obj);\r\n"
				+ "\t\t\tlong id = " + OutUtil.fly_factory + ".getFly().getModel(long.class, \"SELECT @@identity\");\r\n"
				+ "\t\t\t" + OutUtil.fly_factory + ".getFly().commit();\r\n"
				+ "\t\t\treturn id;\r\n"
				+ "\t\t}catch(RuntimeException e){\r\n"
				+ "\t\t\t" + OutUtil.fly_factory + ".getFly().rollback();\r\n"
				+ "\t\t\tthrow e;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n";
		String delete = "\tpublic int delete(long id){\r\n"
				+ "\t\treturn FC." + varName + "Dao.delete(id);\r\n"
				+ "\t}\r\n";
		String update = "\tpublic int update(" + className + " obj){\r\n"
				+ "\t\treturn FC." + varName + "Dao.update(obj);\r\n"
				+ "\t}\r\n";
		String getById = "\tpublic " + className + " getById(long id){\r\n"
				+ "\t\treturn FC." + varName + "Dao.getById(id);\r\n"
				+ "\t}\r\n";
		String getList = getServiceGetList(className, varName);

		String att = "\t@Override\r\n";
		mc.methodList.add(att + add);
		mc.methodList.add(att + delete);
		mc.methodList.add(att + update);
		mc.methodList.add(att + getById);
		mc.methodList.add(att + getList);
		
		return mc.toString();
	}
	@Override
	public String mkController(Table table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String varName = OutUtil.wordFirstSmall(table.name);
		mc.packageInfo = codeCfg.packagePath + ".controller.api;";	// 包信息
		mc.classNotes = "Controller: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "class " + className + "Controller";

		mc.importList.add("import "+ OutUtil.path_fly_util +".*;\r\n");	// fly.util
		mc.importList.add("import " + codeCfg.packagePath + ".service.*;");		// service接口
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so

		mc.attList.add("@RestController");
		mc.attList.add("@RequestMapping(\"/" + OutUtil.wordFirstSmall(table.name) + "/\")");
		mc.importList.add("import org.springframework.web.bind.annotation.RestController;");
		mc.importList.add("import org.springframework.web.bind.annotation.RequestMapping;");
		
		// 所有函数
		String add = "\t@RequestMapping(\"add\")\r\n"
				+ "\tpublic AjaxJson add(" + className + " obj){\r\n"
				+ "\t\tlong id = FS." + varName + "Service.add(obj);\r\n"
				+ "\t\treturn AjaxJson.getSuccess(\"ok\").setData(id);\r\n"
				+ "\t}\r\n";
		String delete = "\t@RequestMapping(\"delete\")\r\n"
				+ "\tpublic AjaxJson delete(long id){\r\n"
				+ "\t\tint a = FS." + varName + "Service.delete(id);\r\n"
				+ "\t\treturn AjaxJson.getByLine(a);\r\n"
				+ "\t}\r\n";
		String update = "\t@RequestMapping(\"update\")\r\n"
				+ "\tpublic AjaxJson update(" + className + " obj){\r\n"
				+ "\t\tint a = FS." + varName + "Service.update(obj);\r\n"
				+ "\t\treturn AjaxJson.getByLine(a);\r\n"
				+ "\t}\r\n";
		String getById = "\t@RequestMapping(\"getById\")\r\n"
				+ "\tpublic AjaxJson getById(long id){\r\n"
				+ "\t\tObject data = FS." + varName + "Service.getById(id);\r\n"
				+ "\t\treturn AjaxJson.getSuccess(\"ok\").setData(data);\r\n"
				+ "\t}\r\n";
		String getList = "\t@RequestMapping(\"getList\")\r\n"
				+ "\tpublic AjaxJson getList(" + className + "SO so){\r\n"
				+ "\t\treturn FS." + varName + "Service.getList(so);\r\n"
				+ "\t}\r\n";

		mc.methodList.add(add);
		mc.methodList.add(delete);
		mc.methodList.add(update);
		mc.methodList.add(getById);
		mc.methodList.add(getList);
		
		return mc.toString();
	}
	@Override
	public String mkDaoFactory() {
		String fc = OutUtil.SpringBeanFC(codeCfg.projectPath, codeCfg.packagePath + ".dao", "FC");
		return fc;
	}
	@Override
	public String mkServiceFactory() {
		String fs = OutUtil.SpringBeanFC(codeCfg.projectPath, codeCfg.packagePath + ".service", "FS");
		return fs;
	}
	
	
	
	@Override
	public String mkIO() {

		System.out.println("\n\n===============  接口与实现类 共计：(" + codeCfg.tableList.size() + ") ======================");
		for (Table table : codeCfg.tableList) {
			String className = table.getClassName();
			
			OutUtil.outFile(codeCfg.getIOPath() + "/dao/" + className + "Dao.java", mkDaoI(table));
			OutUtil.outFile(codeCfg.getIOPath() + "/dao/impl/" + className + "DaoImpl.java", mkDaoImpl(table));

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




	


	protected String getServiceGetList(String className, String varName){
		return "\tpublic AjaxJson getList(" + className + "SO so){\r\n"
				+ "\t\tPage page = Page.getPage(so.getPageNo(), so.getPageSize());\r\n"
				+ "\t\tList<?> list = FC." + varName + "Dao.getList(page, so);\r\n"
				+ "\t\treturn AjaxJson.getPageData(page, list);\r\n"
				+ "\t}\r\n";
	}

	


	
}
