package com.fly.re.out;

import com.fly.re.model.Column;
import com.fly.re.model.MkClass;
import com.fly.re.model.Table;

/**
 * 生成实体类 lomock版
 * @author kongyongshun
 *
 */
public class FlyOutModelLomock extends FlyOutModelDefault implements FlyOutModel {

	
	
	@Override
	public String mkModel(Table table) {
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + ".model;";	// 包信息
		mc.classNotes = "Model: " + table.name + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;									// 作者
		
		mc.attList.add("@Data");
		mc.importList.add("import lombok.Data;");
		
		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName() + " implements Serializable ";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");
		
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
			
		}
		
		
		return mc.toString();
	}

	@Override
	public String mkModelSO(Table table) {
		MkClass mc = new MkClass();
		mc.packageInfo = codeCfg.packagePath + ".model.so;";	// 包信息
		mc.classNotes = "Model: " + table.name + " -- " + table.comment;	// 注释
		mc.classAuthor = codeCfg.author;									// 作者
		
		// 实现序列化
		mc.importList.add("import java.io.Serializable;");
		mc.className = "class " + table.getClassName() + "SO implements Serializable ";
		mc.fieldList.add("\tprivate static final long serialVersionUID = 1L;\r\n");
		
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
			
		}
		
		// toString 和 lombok 
		mc.attList.add("@Data");
		mc.importList.add("import lombok.Data;");
		
		return mc.toString();
	}



	
	
	@Override
	public void mkIO() {
		super.mkIO();
	}
	
	
	
}
