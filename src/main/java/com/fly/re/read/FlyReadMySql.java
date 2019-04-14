package com.fly.re.read;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.Table;

public class FlyReadMySql implements FlyRead{

	// 配置信息
	CodeCfg codeCfg;

	public FlyRead setCodeCfg(CodeCfg codeCfg){
		this.codeCfg = codeCfg;
		return this;
	}
		

	@Override
	public CodeCfg readInfo() {
		Map<String, String> tcMap = ReadUtil.getTcMap(codeCfg.sqlFly);
		for (Table table : codeCfg.tableList) {
			table.comment = tcMap.get(table.name);	// 表注释
			getColumnList(table);	// 加持字段信息
		}
		return null;
	}
	
	
	
	// 获取指定表的所有列信息
	@Override
	public List<Column> getColumnList(Table table){
		List<Column> columns = new ArrayList<>();
		try {
			Map<String, String> jtMap = ReadUtil.getJtMap(codeCfg.sqlFly, table.name);
			ResultSet rs = codeCfg.sqlFly.getResultSet("show full columns from " + table.name);
			while (rs.next()) {
				Column column = new Column();
				column.name = rs.getString("Field"); 			// 字段名
				column.dbType = rs.getString("Type"); 		// DB类型
				column.javaType = jtMap.get(column.name); 					// java类型
				column.comment = rs.getString("Comment"); 	// 注释
				columns.add(column);
			}
			table.columnList = columns; 
			table.pk = "id";	// 主键列
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	

	

	
	
}
