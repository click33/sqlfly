package com.fly.re.out;

import java.util.Arrays;
import java.util.List;

import com.fly.re.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.DaoMethod;
import com.fly.re.model.MkClass;
import com.fly.re.model.MkTable;

/**
 * 代码生成默认实现类
 * @author kongyongshun
 *
 */
public class FlyOutWorkDefault implements FlyOutWork{

	// 配置信息
	CodeCfg cc;

	public FlyOutWork setCodeCfg(CodeCfg codeCfg){
		this.cc = codeCfg;
		return this;
	}

	
	
	
	@Override
	public String mkAdd(MkTable table){
		String tableName = table.name;
		String modelName = OutUtil.wordFirstSmall(tableName).substring(0, 1);
		DaoMethod me = new DaoMethod();
		me.note = "增";
		me.isOverride = cc.is_three;
		me.returnType = "int";
		me.methodName = OutUtil.method_add;
		me.shapeCode = table.getClassName() + " " + modelName;
		
		List<String> columnList = table.getColumnListString();
		String sk = "", sv = ""; // 列部分，值部分，参数部分
		for (String columnName : columnList) {
			sk += columnName + ", ";
			sv += "#{" + columnName + "}, ";
		}
		me.sqlCode = "insert into " + tableName + "(" + OutUtil.strLastDrop(sk, 2) +
				") values (" + OutUtil.strLastDrop(sv, 2) + ")";
		me.returnCode = "getSqlFly().getUpdate(sql, " + modelName + ")";
		return me.toString();
	}
	public String mkDelete(MkTable table){
		String tableName = table.name;
		DaoMethod me = new DaoMethod();
		me.note = "删";
		me.isOverride = cc.is_three;
		me.returnType = "int";
		me.methodName = OutUtil.method_delete;
		me.shapeCode = "long id";
		me.sqlCode = "delete from " + tableName +  " where " + table.pk + " = ?";
		me.returnCode = "getSqlFly().getUpdate(sql, id)";
		return me.toString();
	}
	public String mkUpdate(MkTable table){
		String tableName = table.name;
		String modelName = OutUtil.wordFirstSmall(tableName).substring(0, 1);
		DaoMethod me = new DaoMethod();
		me.note = "改";
		me.isOverride = cc.is_three;
		me.returnType = "int";
		me.methodName = OutUtil.method_update;
		me.shapeCode = table.getClassName() + " " + modelName;
		

		List<String> columnList = table.getColumnListString();
		String sk = ""; // set参部分，args参数
		for (String columnName : columnList) {
			if (columnName.equals(table.pk)) {
				continue;
			}
			sk += columnName + " = #{" + columnName + "}, ";
		}
		me.sqlCode = "update " + tableName + " set " + OutUtil.strLastDrop(sk, 2) + " where " + table.pk + " = ?"; // 再加个主键
		me.returnCode = "getSqlFly().getUpdate(sql, " + modelName + ")";
		return me.toString();
	}
	public String mkGetById(MkTable table){
		String tableName = table.name;
		DaoMethod me = new DaoMethod();
		me.note = "查";
		me.isOverride = cc.is_three;
		me.returnType = table.getClassName();
		me.methodName = OutUtil.method_getById;
		me.shapeCode = "long id";
		me.sqlCode = "select * from " + tableName +  " where id = ?";
		me.returnCode = "getSqlFly().getModel(" + table.getClassName() + ".class, sql, id)";
		return me.toString();
	}
	public String mkGetList(MkTable table){
		
		String tableName = table.name;
		
		String zhushi = "\t// 查询，根据条件(参数为null或0时默认忽略此条件)\r\n";	// 方法注释
		if(cc.is_three == true){
			zhushi += "\t@Override\r\n";
		}
		String methodHead = "\tpublic List<" + table.getClassName() + 
				"> " + OutUtil.method_getList + "(" + table.getClassName() + "SO so) {\r\n";
		String methodCode = "\t\tString sql = \"select * from " + tableName + " where 1=1\";\r\n\r\n";	// 代码部分   
		
		for (Column column : table.columnList) {
			String tj = "";
			String getP = "so.get" + OutUtil.wordFirstBig(column.name) + "()";
			if(Arrays.asList("int", "long").contains(column.javaType)){
				tj = getP + " != 0";
			}else{
				tj = OutUtil.class_FlyUtil + ".isNull(" + getP + ") == false";
			}
			methodCode += "\t\tif (" + tj + ") {\r\n";
			methodCode += "\t\t\tsql += \" and " + column.name + " = #{" + column.name + "}\";\r\n";
			methodCode += "\t\t}\r\n";
			// methodCode += "\r\n\t\t\t\t.toIf(" + tj + ", \" and " + column.name + " = ?\", " + getP + ")";
		}
		methodCode += "\t\tsql += so.getSortString();\r\n\r\n";
		methodCode += "\t\tList<" + table.getClassName() + "> list = getSqlFly().getListPage(so.getPage(), " + table.getClassName() + ".class, sql, so);\r\n";
		methodCode += "\t\treturn list;\r\n";
		
		String str = zhushi + methodHead + methodCode + "\t}\r\n";
		return str;
	}
	
	
	
	// 生成Dao接口 - 全
	@Override
	public String mkDaoI(MkTable table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String modelName = OutUtil.wordFirstSmall(table.name).substring(0, 1);
		mc.packageInfo = cc.packagePath + OutUtil.getString(cc.is_three, ".dao", "." + table.getNameSmall());	// 包信息
		mc.classNotes = "Dao: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = cc.author;								// 作者
		mc.className = "interface " + className + "Dao";
		
		mc.importList.add("import java.util.*;\r\n");
		if(cc.is_three) {
			mc.importList.add("import " + cc.packagePath + ".model.*;");	// model
			mc.importList.add("import " + cc.packagePath + ".model.so.*;");	// so
		}

		mc.methodList.add(OutUtil.getDoc("增") + "\tpublic int " + OutUtil.method_add + "(" + className + " " + modelName + ");\r\n");		// 新增
		mc.methodList.add(OutUtil.getDoc("删") + "\tpublic int " + OutUtil.method_delete + "(long id);\r\n");		// 删除
		mc.methodList.add(OutUtil.getDoc("改") + "\tpublic int " + OutUtil.method_update + "(" + className + " " + modelName + ");\r\n");		// 修改
		mc.methodList.add(OutUtil.getDoc("查") + "\tpublic " + className + " " + OutUtil.method_getById + "(long id);\r\n");		// 查询，根据id
		mc.methodList.add(OutUtil.getDoc("查 - 集合") + "\tpublic List<" + className + "> " + OutUtil.method_getList + "(" + className + "SO so);\r\n");		// 查询集合
		
		return mc.toString();
	}
	// 生成Dao接口实现类  - 全
	@Override
	public String mkDaoImpl(MkTable table) {
		
		MkClass mc = new MkClass();
		String className = table.getClassName();
		mc.packageInfo = cc.packagePath + OutUtil.getString(cc.is_three, ".dao.impl", "." + table.getNameSmall());	// 包信息 
		mc.classNotes = "Dao: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = cc.author;								// 作者
		mc.className = "class " + className + "DaoImpl";
		
		mc.importList.add("import java.util.*;");
		mc.importList.add(OutUtil.import_sqlfly);	// 导入sqlfly
		// 如果是三层分包 
		if(cc.is_three) {
			mc.importList.add("import " + cc.packagePath + ".model.*;");	// model
			mc.importList.add("import " + cc.packagePath + ".model.so.*;");	// so
			mc.importList.add("import " + cc.packagePath + ".dao.*;");
		}
		if(cc.is_three){
			mc.implementsName = className + "Dao";
		}

		// spring的注解 
		mc.attList.add("@Component");
		mc.importList.add("import org.springframework.stereotype.Component;");
		
		// 所有函数
		String getfly = OutUtil.get_getSqlFly();
		mc.methodList.add(getfly);
		mc.methodList.add(mkAdd(table));
		mc.methodList.add(mkDelete(table));
		mc.methodList.add(mkUpdate(table));
		mc.methodList.add(mkGetById(table));
		mc.methodList.add(mkGetList(table));
		
		return mc.toString();
	}
	
	// 生成Service接口样本
	@Override
	public String mkServiceI(MkTable table) {
		
		MkClass mc = new MkClass();
		String className = table.getClassName();
		mc.packageInfo = cc.packagePath + ".service";	// 包信息
		mc.classNotes = "Service: " + className + " -- " + table.comment;	// 注释 
		mc.classAuthor = cc.author;								// 作者
		mc.className = "interface " + className + "Service";

		mc.importList.add("import java.util.List;\r\n");	// util包
		if(cc.is_three) {
			mc.importList.add("import " + cc.packagePath + ".model.*;");	// model
			mc.importList.add("import " + cc.packagePath + ".model.so.*;");	// so
		}

		mc.methodList.add(OutUtil.getDoc("增") + "\tpublic long add(" + className + " obj);\r\n");		// 新增
		mc.methodList.add(OutUtil.getDoc("删") + "\tpublic int delete(long id);\r\n");		// 删除
		mc.methodList.add(OutUtil.getDoc("改") + "\tpublic int update(" + className + " obj);\r\n");		// 修改
		mc.methodList.add(OutUtil.getDoc("查") + "\tpublic " + className + " getById(long id);\r\n");		// 查询，根据id
		mc.methodList.add(OutUtil.getDoc("查 - 集合") + "\tpublic List<" + className + "> getList(" + className + "SO so);\r\n");		// 查询集合
		
		return mc.toString();
	}
	// 生成Service实现类
	@Override
	public String mkServiceImpl(MkTable table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String varDaoName = OutUtil.wordFirstSmall(table.getClassName()) + "Dao";
		mc.packageInfo = cc.packagePath + OutUtil.getString(cc.is_three, ".service.impl", table.getNameSmall());	// 包信息 
		mc.classNotes = "Service: " + className + " -- " + table.comment;	// 注释 
		mc.classAuthor = cc.author;								// 作者 
		mc.className = "class " + className + "ServiceImpl";
		if(cc.is_three) {
			mc.implementsName = className + "Service";
		}
		
		mc.importList.add("import java.util.*;");
		mc.importList.add(OutUtil.import_sqlfly);	// fly.util
		if(cc.is_three) {
			mc.importList.add("import " + cc.packagePath + ".FC;");		// FC 
			mc.importList.add("import " + cc.packagePath + ".service.*;");		// service接口
			mc.importList.add("import " + cc.packagePath + ".model.*;");	// model
			mc.importList.add("import " + cc.packagePath + ".model.so.*;");	// so
		}

		// spring 注解
		mc.attList.add("@Service");
		mc.importList.add("import org.springframework.stereotype.Service;");
		
		// 所有函数
		String add = "\tpublic long add(" + className + " obj) {\r\n"
				+ "\t\ttry{\r\n"
				+ "\t\t\t" + OutUtil.class_SqlFlyFactory + ".getSqlFly().beginTransaction();\r\n"
				+ "\t\t\tFC." + varDaoName + ".add(obj);\r\n"
				+ "\t\t\tlong id = " + OutUtil.class_SqlFlyFactory + ".getSqlFly().getModel(long.class, \"SELECT @@identity\");\r\n"
				+ "\t\t\t" + OutUtil.class_SqlFlyFactory + ".getSqlFly().commit();\r\n"
				+ "\t\t\treturn id;\r\n"
				+ "\t\t}catch(RuntimeException e){\r\n"
				+ "\t\t\t" + OutUtil.class_SqlFlyFactory + ".getSqlFly().rollback();\r\n"
				+ "\t\t\tthrow e;\r\n"
				+ "\t\t}\r\n"
				+ "\t}\r\n";
		String delete = "\tpublic int delete(long id) {\r\n"
				+ "\t\treturn FC." + varDaoName + ".delete(id);\r\n"
				+ "\t}\r\n";
		String update = "\tpublic int update(" + className + " obj) {\r\n"
				+ "\t\treturn FC." + varDaoName + ".update(obj);\r\n"
				+ "\t}\r\n";
		String getById = "\tpublic " + className + " getById(long id) {\r\n"
				+ "\t\treturn FC." + varDaoName + ".getById(id);\r\n"
				+ "\t}\r\n";
		String getList = "\tpublic List<" + className + "> getList(" + className + "SO so) {\r\n"
				+ "\t\treturn FC." + varDaoName + ".getList(so);\r\n"
				+ "\t}\r\n";
		// String getList = getServiceGetList(className, varName);

		String att = "";
		if(cc.is_three) {
			att = "\t@Override\r\n";
		}
		mc.methodList.add(OutUtil.getNotes("删") + att + add);
		mc.methodList.add(OutUtil.getNotes("删") + att + delete);
		mc.methodList.add(OutUtil.getNotes("改") + att + update);
		mc.methodList.add(OutUtil.getNotes("查") + att + getById);
		mc.methodList.add(OutUtil.getNotes("查 - 集合") + att + getList);
		
		return mc.toString();
	}
	
	// 生成 Controller 样本 
	@Override
	public String mkController(MkTable table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String serviceVarName = OutUtil.wordFirstSmall(table.getClassName()) + OutUtil.getString(cc.is_three, "Service", "DaoImpl");
		mc.packageInfo = cc.packagePath + "." + OutUtil.getString(cc.is_three, "controller", table.name);	// 包信息  
		mc.classNotes = "Controller: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = cc.author;								// 作者 
		mc.className = "class " + className + "Controller";
		mc.importList.add("import java.util.List;\r\n");		// List 
		mc.importList.add("import " + cc.packagePath + ".FC;");		// FC 
		mc.importList.add("import " + cc.package_ajaxjson + "." + cc.class_ajaxjson + ";");		// AjaxJson

		if(cc.is_three) {
			mc.importList.add("import " + cc.packagePath + ".model.*;");	// model
			mc.importList.add("import " + cc.packagePath + ".model.so.*;");	// so
		}

		mc.attList.add("@RestController");
		mc.attList.add("@RequestMapping(\"/" + OutUtil.wordFirstSmall(table.name) + "/\")");
		mc.importList.add("import org.springframework.web.bind.annotation.RestController;");
		mc.importList.add("import org.springframework.web.bind.annotation.RequestMapping;");
		
		// 所有函数
		String add = "\t// 增  \r\n"
				+ "\t@RequestMapping(\"add\")\r\n"
				+ "\tpublic " + cc.class_ajaxjson + " add(" + className + " obj){\r\n"
				+ "\t\tlong id = FC." + serviceVarName + ".add(obj);\r\n"
				+ "\t\treturn " + cc.class_ajaxjson + ".getSuccessData(id);\r\n"
				+ "\t}\r\n";
		String delete = "\t// 删  \r\n"
				+ "\t@RequestMapping(\"delete\")\r\n"
				+ "\tpublic " + cc.class_ajaxjson + " delete(long id){\r\n"
				+ "\t\tint line = FC." + serviceVarName + ".delete(id);\r\n"
				+ "\t\treturn " + cc.class_ajaxjson + ".getByLine(line);\r\n"
				+ "\t}\r\n";
		String update = "\t// 改  \r\n"
				+ "\t@RequestMapping(\"update\")\r\n"
				+ "\tpublic " + cc.class_ajaxjson + " update(" + className + " obj){\r\n"
				+ "\t\tint line = FC." + serviceVarName + ".update(obj);\r\n"
				+ "\t\treturn " + cc.class_ajaxjson + ".getByLine(line);\r\n"
				+ "\t}\r\n";
		String getById = "\t// 查  \r\n"
				+ "\t@RequestMapping(\"getById\")\r\n"
				+ "\tpublic " + cc.class_ajaxjson + " getById(long id){\r\n"
				+ "\t\tObject data = FC." + serviceVarName + ".getById(id);\r\n"
				+ "\t\treturn " + cc.class_ajaxjson + ".getSuccessData(data);\r\n"
				+ "\t}\r\n";
		String getList = "\t// 查 - 集合  \r\n"
				+ "\t@RequestMapping(\"getList\")\r\n"
				+ "\tpublic " + cc.class_ajaxjson + " getList(" + className + "SO so){\r\n"
				+ "\t\tList<" + className + "> list = FC." + serviceVarName + ".getList(so);\r\n"
				+ "\t\treturn AjaxJson.getPageData(so.getPage(), list);\r\n"
				+ "\t}\r\n";

		mc.methodList.add(add);
		mc.methodList.add(delete);
		mc.methodList.add(update);
		mc.methodList.add(getById);
		mc.methodList.add(getList);
		
		return mc.toString();
	}


	
	
	// 生成工厂类
	public String mkFC() {
		String packageInfo = "package " + cc.packagePath + ";\r\n\r\n";	// 包信息 
		String importList = "import org.springframework.beans.factory.annotation.Autowired;\r\n";	// 导包集合 
		importList += "import org.springframework.stereotype.Component;\r\n\r\n";
		String code = "\r\n/**\r\n* 工厂类\r\n*/\r\n@Component\r\npublic class FC {\r\n\r\n\r\n";	// 代码体
		
		// String daoList = "\t// ===================== 所有 dao 对象 ===================== \r\n\r\n";	// 所有dao方法   
		// String serviceList = "\t// ===================== 所有 service 对象 ===================== \r\n\r\n";	// 所有service方法   
		
		code += "\t// ===================== 所有 dao 对象 ===================== \r\n\r\n";	// 所有dao方法   
		for (MkTable table : cc.tableList) {
			String daoName = table.getDaoName();
			if(cc.is_three == false) {
				daoName += "Impl";
			}
			code += OutUtil.getFCone(daoName, table.comment);
			code += "\r\n";
			if(cc.is_three == false) {		// 不是三层的情况下 追加导包信息 
				importList += "import " + cc.packagePath + "." + table.name + "." + daoName + ";\r\n";
			}
		}
		
		if(cc.is_three) {
			code += "\r\n\r\n\t// ===================== 所有 service 对象 ===================== \r\n\r\n";	// 所有service方法   
			for (MkTable table : cc.tableList) {
				String serviceName = table.getServiceName();
				if(cc.is_three == false) {
					serviceName += "Impl";
				}
				code += OutUtil.getFCone(serviceName, table.comment);
				code += "\r\n";
				if(cc.is_three == false) {		// 不是三层的情况下 追加导包信息 
					importList += "import " + cc.packagePath + " ." + serviceName + ";\r\n";
				}
			}
			// 三层的导包信息
			importList += "import " + cc.packagePath + " .dao.*;\r\n";
			importList += "import " + cc.packagePath + " .service.*;\r\n";
		}
		
		code += "\r\n}";
		return packageInfo + importList + code;	
	}
	
	// 开始生成 
	@Override
	public String mkIO() {

		System.out.println("\n\n===============  接口与实现类 共计：(" + cc.tableList.size() + ") ======================");
		for (MkTable table : cc.tableList) {
			String className = table.getClassName();	// java类名
			String small_name = table.getNameSmall();	// 转小写的名字 
			
			// 三层型 
			if(cc.is_three) {
				OutUtil.outFile(cc.getIOPath() + "/dao/" + className + "Dao.java", mkDaoI(table));
				OutUtil.outFile(cc.getIOPath() + "/dao/impl/" + className + "DaoImpl.java", mkDaoImpl(table));

				OutUtil.outFile(cc.getIOPath() + "/service/" + className + "Service.java", mkServiceI(table));
				OutUtil.outFile(cc.getIOPath() + "/service/impl/" + className + "ServiceImpl.java",mkServiceImpl(table));

				OutUtil.outFile(cc.getIOPath() + "/controller/" + className + "Controller.java",mkController(table));
				System.out.println(table.name + "\t --> Dao、Service、Controller 写入成功！");
			}
			// 精简型 
			if(cc.is_three == false) {
				OutUtil.outFile(cc.getIOPath() + small_name + "\\" + className + "DaoImpl.java", mkDaoImpl(table));

				OutUtil.outFile(cc.getIOPath() + small_name + "\\" + className + "Controller.java",mkController(table));
				System.out.println(table.name + "\t --> Dao、Controller 写入成功！");
			}
		}

		System.out.println("\n\n=============== 工厂类生成 ======================");
		OutUtil.outFile(cc.getIOPath() + "FC.java", mkFC());
		System.out.println("FC.java\t --> 写入成功");

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
