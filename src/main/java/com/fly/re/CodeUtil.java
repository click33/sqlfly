package com.fly.re;

import com.fly.jdbc.FlyUtil;
import com.fly.re.out.FlyOutDoc;
import com.fly.re.out.FlyOutDocDefault;
import com.fly.re.out.FlyOutModel;
import com.fly.re.out.FlyOutModelDefault;
import com.fly.re.out.FlyOutWork;
import com.fly.re.out.FlyOutWorkDefault;
import com.fly.re.read.FlyRead;
import com.fly.re.read.FlyReadMySql;
import com.fly.re.read.ReadUtil;

public class CodeUtil {
	
	
	public static CodeCfg codeCfg = new CodeCfg();			// 默认的配置信息类 
	public static FlyRead flyRead;		// 默认的读取实现类
	public static FlyOutModel flyOutModel;		// 默认的实体类输出实现类
	public static FlyOutWork flyOutWork;		// 默认的业务代码输出实现类
	public static FlyOutDoc flyOutDoc;			// 默认的文档输出实现类
	
	
	
	/**
	 * 执行,根据配置获取数据库信息
	 */
	public static void run(){
		initRely();
		flyRead.setCodeCfg(codeCfg).readInfo();
		flyOutModel.setCodeCfg(codeCfg).mkIO();
		flyOutWork.setCodeCfg(codeCfg).mkIO();
		flyOutDoc.setCodeCfg(codeCfg).mkIO();
		System.out.println("\n\n--------------- 生成完毕  --------------\n\n");
		FlyUtil.printSqlFly();
	}
	

	
	// init 依赖
	private static void initRely() {
		// 初始化读取依赖 
		if(flyRead == null) {
			flyRead = new FlyReadMySql();
		}
		// 初始化实体类输出 
		if(flyOutModel == null) {
			flyOutModel = new FlyOutModelDefault();
		}
		// 初始化业务输出
		if(flyOutWork == null) {
			flyOutWork = new FlyOutWorkDefault();
		}
		// 初始化文档输出
		if(flyOutDoc == null) {
			flyOutDoc = new FlyOutDocDefault();
		}
		// 初始化数据表集合
		if(codeCfg.tableList == null || codeCfg.tableList.size() == 0){
			for (String tName : ReadUtil.getTableList(codeCfg.sqlFly.getConnection())) {
				codeCfg.tableNameList.add(tName);
			}
		}
	}
	
	
	
	
	
	
	
}
