package com.fly.re.out;

import com.fly.re.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.MkClass;
import com.fly.re.model.MkTable;

public class FlyOutModelDefault implements FlyOutModel {


	// 配置信息
	CodeCfg codeCfg;

	public FlyOutModel setCodeCfg(CodeCfg codeCfg){
		this.codeCfg = codeCfg;
		return this;
	}
	
	
	
	@Override
	public MkClass mkModel(MkTable table) {
		// 创建类型对象 
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + OutUtil.getString(codeCfg.is_three, ".model", "." + table.name);	// 包信息
		mc.classNotes = "Model: " + table.name + " -- " + table.comment;	// 注释	
		mc.classAuthor = codeCfg.author;								// 作者	
		
		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName();
		mc.implementsName = "Serializable";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");
		
		// toString()函数
		String sts_code = "";
		
		// 字段
		for (Column column : table.columnList) {
			// 是否导入util包  
			if (column.javaType.equals("Date")) {
				mc.importList.add("import java.util.*;");
			}
			mc.fieldList.add("\tprivate " + column.javaType + " " + column.name + ";\t\t// " + column.comment);	// 字段
			sts_code += "\t\t\t\t\" ," + column.name + "=\" + " + column.name + " + \r\n"; 	// toString()
			
			if(codeCfg.is_lomock == false) {
				mc.methodList.add(OutUtil.get_getMethod(column));		// getter
				mc.methodList.add(OutUtil.get_setMethod(column, table.getClassName()));	// setter
			}
		}
		
		// 是否使用lomock 
		if(codeCfg.is_lomock == true) {
			mc.attList.add("@Data");
			mc.importList.add("import lombok.Data;");
		} else {
			// toString的相关代码 
			try {
				sts_code = sts_code.substring(7, sts_code.length() - 3);
			} catch (Exception e) {
			}
			String sts_doc = OutUtil.get_doc_toString();	// 注释 
			String sts_method = "\tpublic String toString() {\r\n\t\treturn \"" + table.getClassName()  + " [";	// 方法体 
			String sts = sts_doc + sts_method + sts_code + " \"]\";\r\n\t}\r\n";
			mc.methodList.add(sts);
		}
		return mc;
	}

	@Override
	public MkClass mkModelSO(MkTable table) {
		// 创建类型对象 
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + OutUtil.getString(codeCfg.is_three, ".model.so", "." + table.name);	// 包信息
		mc.classNotes = "ModelSO: " + table.name + " -- " + table.comment;	// 注释	
		mc.classAuthor = codeCfg.author;								// 作者	

		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName() + "SO";
		mc.implementsName = "Serializable";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");

		// 添加分页相关代码 
		mc.fieldList.add("\tprivate int pageNo;		// 当前页");
		mc.fieldList.add("\tprivate int pageSize;	// 页大小");
		mc.fieldList.add("\tprivate Page page;		// 当前分页对象 ");
		mc.methodList.add(OutUtil.get_getPage());
		mc.importList.add("import com.fly.jdbc.paging.Page;");
		
		// 添加排序相关代码 
		mc.fieldList.add("\tprivate int sort_type;	// 排序方式 ");
		mc.fieldList.add("\tprivate static final String[] arr = new String[]{\" id desc\"};  \r\n");
		mc.methodList.add(OutUtil.get_getSortString() + "\r\n\r\n");	
		
		
		// toString()函数
		String sts_code = ""; 	
		
		// 字段
		for (Column column : table.columnList) {
			// 是否导入util包  
			if (column.javaType.equals("Date")) {
				mc.importList.add("import java.util.*;");
			}
			mc.fieldList.add("\tprivate " + column.javaType + " " + column.name + ";\t\t// " + column.comment);	// 字段
			sts_code += "\t\t\t\t\" ," + column.name + "=\" + " + column.name + " + \r\n"; 	// toString()
			
			if(codeCfg.is_lomock == false) {
				mc.methodList.add(OutUtil.get_getMethod(column));		// getter
				mc.methodList.add(OutUtil.get_setMethod(column, table.getClassName() + "SO"));	// setter
			}
		}
		
		// 是否使用lomock 
		if(codeCfg.is_lomock == true) {
			mc.attList.add("@Data");
			mc.importList.add("import lombok.Data;");
		} else {
			// toString的相关代码 
			try {
				sts_code = sts_code.substring(7, sts_code.length() - 3);
			} catch (Exception e) {
			}
			String sts_doc = OutUtil.get_doc_toString();	// 注释 
			String sts_method = "\tpublic String toString() {\r\n\t\treturn \"" + table.getClassName()  + " [";	// 方法体 
			String sts = sts_doc + sts_method + sts_code + " \"]\";\r\n\t}\r\n";
			mc.methodList.add(sts);
		}
		return mc;
		
	}


	
	
	@Override
	public void mkIO() {
		System.out.println("\n\n===============  实体类生成 共计：(" + codeCfg.tableList.size() + ") ======================");
		for (MkTable table : codeCfg.tableList) {
			MkClass model = mkModel(table);
			MkClass modelSO = mkModelSO(table);
			OutUtil.outFile(codeCfg.getIOPath(model.packageInfo) + table.getClassName() + ".java", model.toString());
			OutUtil.outFile(codeCfg.getIOPath(modelSO.packageInfo) + table.getClassName() + "SO.java", modelSO.toString());
			System.out.println(table.name + "\t --> 实体类与SO写入成功！");
		}
	}
	
	
	
}
