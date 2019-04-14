package com.fly.re.out;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.MkClass;
import com.fly.re.model.Table;

public class FlyOutModelDefault implements FlyOutModel {


	// 配置信息
	CodeCfg codeCfg;

	public FlyOutModel setCodeCfg(CodeCfg codeCfg){
		this.codeCfg = codeCfg;
		return this;
	}
	
	
	
	@Override
	public String mkModel(Table table) {
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + ".model;";	// 包信息
		mc.classNotes = "Model: " + table.name + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		
		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName() + " implements Serializable ";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");
		
		StringBuilder sts = new StringBuilder(); 	// toString()函数
		
		// 字段
		for (Column column : table.columnList) {
			
			// 是否导入sql包
			if (column.javaType.equals("Date") || column.javaType.equals("Timestamp")) {
				if(mc.importList.contains("import java.sql.*;") == false){
					mc.importList.add("import java.sql.*;");
				}
			}
			
			// 字段
			mc.fieldList.add("\tprivate " + column.javaType + " " + column.name + ";\t\t// " + column.comment);
			
			// get与set方法
			String getMethod = OutUtil.getDoc("@return " + column.comment);
			getMethod += "\tpublic " + column.javaType + " get" + OutUtil.getSetGet(column.name) + 
					"(){\r\n\t\treturn " + column.name + ";\r\n\t}\r\n";
			mc.methodList.add(getMethod);
			
			String setMethod = OutUtil.getDoc("@param " + column.name + " " + column.comment);
			setMethod += "\tpublic " + table.getClassName() + " set" + OutUtil.getSetGet(column.name) + 
					"(" + column.javaType + " " + column.name + ") {\r\n\t\tthis." + column.name + 
					"=" + column.name + ";\r\n\t\treturn this;\r\n\t}\r\n";
			mc.methodList.add(setMethod);
			
			sts.append("\t\t\t\t\" ," + column.name + "=\" + " + column.name + " + \r\n"); 	// toString()
			
		}
		
		// toString()
		String sts_head = "\r\n\t/* (non-Javadoc)\r\n\t * @see java.lang.Object#toString()\r\n\t */\r\n\t@Override\r\n";
		sts_head += "\tpublic String toString() {\r\n\t\treturn \"" +table.getClassName()  + " [";
		try {
			sts = new StringBuilder(sts.substring(7, sts.length() - 3));
		} catch (Exception e) {
		}
		sts.insert(0, sts_head);
		sts.append(" \"]\";\r\n\t}\r\n");
		mc.methodList.add(sts.toString());
		
		return mc.toString();
	}

	@Override
	public String mkModelSO(Table table) {
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + ".model.so;";	// 包信息
		mc.classNotes = "Model: " + table.name + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;								// 作者
		
		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName() + "SO implements Serializable ";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");
		
		StringBuilder sts = new StringBuilder(); 	// toString()函数
		
		// 字段
		for (Column column : table.getColumnListSO()) {

			// 是否导入sql包
			if (column.javaType.equals("Date") || column.javaType.equals("Timestamp")) {
				if(mc.importList.contains("import java.sql.*;") == false){
					mc.importList.add("import java.sql.*;");
				}
			}
			
			// 字段
			mc.fieldList.add("\tprivate " + column.javaType + " " + column.name + ";\t\t// " + column.comment);
			
			// get与set方法
			String getMethod = OutUtil.getDoc("@return " + column.comment);
			getMethod += "\tpublic " + column.javaType + " get" + OutUtil.getSetGet(column.name) + 
					"(){\r\n\t\treturn " + column.name + ";\r\n\t}\r\n";
			mc.methodList.add(getMethod);
			
			String setMethod = OutUtil.getDoc("@param " + column.name + " " + column.comment);
			setMethod += "\tpublic " + table.getClassName() + "SO set" + OutUtil.getSetGet(column.name) + 
					"(" + column.javaType + " " + column.name + ") {\r\n\t\tthis." + column.name + 
					"=" + column.name + ";\r\n\t\treturn this;\r\n\t}\r\n";
			mc.methodList.add(setMethod);
			
			sts.append("\t\t\t\t\" ," + column.name + "=\" + " + column.name + " + \r\n"); 	// toString()
			
		}
		
		// toString()
		String sts_head = "\r\n\t/* (non-Javadoc)\r\n\t * @see java.lang.Object#toString()\r\n\t */\r\n\t@Override\r\n";
		sts_head += "\tpublic String toString() {\r\n\t\treturn \"" + table.getClassName()  + " [";
		try {
			sts = new StringBuilder(sts.substring(7, sts.length() - 3));
		} catch (Exception e) {
		}
		sts.insert(0, sts_head);
		sts.append(" \"]\";\r\n\t}\r\n");
		mc.methodList.add(sts.toString());
	
		return mc.toString();
	}



	
	
	@Override
	public void mkIO() {
		System.out.println("\n\n===============  实体类生成 共计：(" + codeCfg.tableList.size() + ") ======================");
		for (Table table : codeCfg.tableList) {
			OutUtil.outFile(codeCfg.getIOPath() + "/model/" + table.getClassName() + ".java", mkModel(table));
			OutUtil.outFile(codeCfg.getIOPath() + "/model/so/" + table.getClassName() + "SO.java", mkModelSO(table));
			System.out.println(table.name + "\t --> 实体类与SO写入成功！");
		}
	}
	
	
	
}
