package com.fly.re;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Table;
import com.fly.re.out.FlyOutDao;
import com.fly.re.out.FlyOutDaoDefault;
import com.fly.re.out.FlyOutDaoEasy;
import com.fly.re.out.FlyOutModel;
import com.fly.re.out.FlyOutModelDefault;
import com.fly.re.out.FlyOutModelLomock;
import com.fly.re.read.FlyRead;
import com.fly.re.read.FlyReadMySql;
import com.fly.re.read.ReadUtil;

public class CodeUtil {
	
	
	public static CodeCfg codeCfg = new CodeCfg();			// 默认的配置信息类
	public static FlyRead flyRead = new FlyReadMySql();		// 默认的读取实现类
	public static FlyOutDao flyOutDao = new FlyOutDaoDefault();		// 默认的输出实现类
	public static FlyOutModel flyOutModel = new FlyOutModelDefault();		// 默认的实体类输出实现类
	
	
	
	

	/**
	 * 添加table
	 */
	public static void addTable(String ... tableNames){
		for (String tableName : tableNames) {
			System.out.println(codeCfg.tableList.add(new Table().setName(tableName)));
		}
	}
	
	
	/**
	 * 执行,根据配置获取数据库信息
	 */
	public static void run(){
		if(codeCfg.tableList == null || codeCfg.tableList.size() == 0){
			for (String tName : ReadUtil.getTableList(codeCfg.sqlFly.getConnection())) {
				codeCfg.tableList.add(new Table().setName(tName));
			}
		}

		flyRead.setCodeCfg(codeCfg).readInfo();
		flyOutModel.setCodeCfg(codeCfg).mkIO();
		flyOutDao.setCodeCfg(codeCfg).mkIO();
	}
	
	/**
	 * 执行
	 * @param author 作者
	 * @param projectPath 项目地址
	 * @param packagePath 父包地址
	 */
	public static void run(String author, String projectPath, String packagePath){
		CodeUtil.codeCfg.author = author;
		CodeUtil.codeCfg.projectPath = projectPath;
		CodeUtil.codeCfg.packagePath = packagePath;
		run();
	}
	

	// 执行，简单版
	public static void runEasy(String author, String projectPath, String packagePath){
		flyOutDao = new FlyOutDaoEasy();
		flyOutModel = new FlyOutModelLomock();
		CodeUtil.codeCfg.author = author;
		CodeUtil.codeCfg.projectPath = projectPath;
		CodeUtil.codeCfg.packagePath = packagePath;
		run();
	}
	
	
	
}
