package com.fly.re.out;

import java.util.Arrays;

import com.fly.re.model.Column;
import com.fly.re.model.MkClass;
import com.fly.re.model.Table;

/**
 * 生成简单的  --> 无Service层，Dao层无接口
 * @author kongyongshun
 *
 */
public class FlyOutDaoEasy extends FlyOutDaoDefault implements FlyOutDao {

	
	public FlyOutDaoEasy() {
		isJK = false;
	}
	
	// 重写： mkGetList
	public String mkGetList(Table table){
		
		String tableName = table.name;
		
		String zhushi = "\t// 查询，根据条件(参数为null或0时默认忽略此条件)\r\n";	// 方法注释
		if(isJK == true){
			zhushi += "\t@Override\r\n";
		}
		String methodHead = "\tpublic AjaxJson getList(" + table.getClassName() + "SO so){\r\n";

		String methodCode = "";
		methodCode += "\t\tPage page = Page.getPage(so.getPageNo(), so.getPageSize());\r\n";
		methodCode += "\t\tSqlExe sqlExe = new SqlExe(\"select * from " + tableName + " where 1=1\")";	// 代码部分
		
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
		
		methodCode += ";\r\n\t\tList<?> list = getFly().getListPage(page, " + table.getClassName() + ".class, sqlExe);\r\n";
		methodCode += "\t\treturn AjaxJson.getPageData(page, list);\r\n";
		
		String str = zhushi + methodHead + methodCode + "\t}\r\n";
		return str;
	}
	
	
	// 重写 mkController
	@Override
	public String mkController(Table table) {

		MkClass mc = new MkClass();
		String className = table.getClassName();
		String varName = OutUtil.wordFirstSmall(className);
		mc.packageInfo = codeCfg.packagePath + ".controller.api;";	// 包信息
		mc.classNotes = "Controller: " + className + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		mc.className = "class " + className + "Controller";

		mc.importList.add("import "+ OutUtil.path_fly_util +".*;\r\n");	// fly.util
		mc.importList.add("import " + codeCfg.packagePath + ".dao.impl.*;");		// service接口
		mc.importList.add("import " + codeCfg.packagePath + ".model.*;");	// model
		mc.importList.add("import " + codeCfg.packagePath + ".model.so.*;");	// so

		mc.attList.add("@RestController");
		mc.attList.add("@RequestMapping(\"/" + table.getClassName() + "/\")");
		mc.importList.add("import org.springframework.web.bind.annotation.RestController;");
		mc.importList.add("import org.springframework.web.bind.annotation.RequestMapping;");
		
		// 所有函数
		String add = "\t@RequestMapping(\"add\")\r\n"
				+ "\tpublic AjaxJson add(" + className + " obj){\r\n"
				+ "\t\tint a = FC." + varName + "DaoImpl.save(obj);\r\n"
				+ "\t\treturn AjaxJson.getByLine(a);\r\n"
				+ "\t}\r\n";
		String delete = "\t@RequestMapping(\"delete\")\r\n"
				+ "\tpublic AjaxJson delete(long id){\r\n"
				+ "\t\tint a = FC." + varName + "DaoImpl.delete(id);\r\n"
				+ "\t\treturn AjaxJson.getByLine(a);\r\n"
				+ "\t}\r\n";
		String update = "\t@RequestMapping(\"update\")\r\n"
				+ "\tpublic AjaxJson update(" + className + " obj){\r\n"
				+ "\t\tint a = FC." + varName + "DaoImpl.update(obj);\r\n"
				+ "\t\treturn AjaxJson.getByLine(a);\r\n"
				+ "\t}\r\n";
		String getById = "\t@RequestMapping(\"getById\")\r\n"
				+ "\tpublic AjaxJson getById(long id){\r\n"
				+ "\t\tObject data = FC." + varName + "DaoImpl.getById(id);\r\n"
				+ "\t\treturn AjaxJson.getSuccess(\"ok\").setData(data);\r\n"
				+ "\t}\r\n";
		String getList = "\t@RequestMapping(\"getList\")\r\n"
				+ "\tpublic AjaxJson getList(" + className + "SO so){\r\n"
				+ "\t\treturn FC." + varName + "DaoImpl.getList(so);\r\n"
				+ "\t}\r\n";

		mc.methodList.add(add);
		mc.methodList.add(delete);
		mc.methodList.add(update);
		mc.methodList.add(getById);
		mc.methodList.add(getList);
		
		return mc.toString();
	}
	
	// 重写 mkDaoFactory
	@Override
	public String mkDaoFactory() {
		String fc = OutUtil.SpringBeanFC(codeCfg.projectPath, codeCfg.packagePath + ".dao.impl", "FC");
		return fc;
	}

	// 重写 mkIO
	@Override
	public String mkIO() {

		System.out.println("\n\n===============  接口与实现类(简单型) 共计：(" + codeCfg.tableList.size() + ") ======================");
		for (Table table : codeCfg.tableList) {
			String className = table.getClassName();
			
			OutUtil.outFile(codeCfg.getIOPath() + "/dao/impl/" + className + "DaoImpl.java", mkDaoImpl(table));

			OutUtil.outFile(codeCfg.getIOPath() + "/controller/api/" + className + "Controller.java",mkController(table));
			System.out.println(table.name + "\t --> Dao、Controller 写入成功！");
		}

		System.out.println("\n\n=============== 工厂类生成 ======================");
		OutUtil.outFile(codeCfg.getIOPath() + "/dao/impl/FC.java", mkDaoFactory());
		System.out.println("FC.java\t --> 写入成功");

		return null;
	}




	
	
}
