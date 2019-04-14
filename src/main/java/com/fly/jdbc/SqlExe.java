package com.fly.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * SqlFly的执行辅助类
 */
public class SqlExe {

	
	public String sql="";
	public List<Object> args = new ArrayList<Object>();
	
	public SqlExe(){}
	public SqlExe(String sql, Object... args){
		this.sql=sql;
		this.args = Fly.doListObj(args);
	}
	
	/* * * * * * * * * * * * * 协调方法 * * * * * * * * * * * * * * * * * * * * */
	
	
	// 获取 
	public String getSql() {
		return sql;
	}
	public List<Object> getArgs() {
		return args;
	}
	// 设置 
	public SqlExe setAll(String sql, Object... args) {
		this.sql=sql;
		this.args = Fly.doListObj(args);
		return this;
	}
	// 追加 
	public SqlExe appendAll(String sql, Object... args) {
		this.sql += sql;
		for (Object arg : args) {
			this.args.add(arg);
		}
		return this;
	}
	
	// 如果为true，再追加
	public SqlExe toIf(boolean isAppend, String sql, Object arg) {
		if (isAppend) {
			this.sql += sql;
			this.args.add(arg);
		}
		return this;
	}
	public SqlExe toIf(boolean isAppend, String sql, Object arg1, Object arg2, Object... args) {
		if (isAppend) {
			this.args.add(arg1);
			this.args.add(arg2);
			appendAll(sql, args);
		}
		return this;
	}
	
	
	
	
	
}
