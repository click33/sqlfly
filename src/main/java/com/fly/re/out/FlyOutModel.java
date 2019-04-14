package com.fly.re.out;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Table;

/**
 * 输出实体类
 */
public interface FlyOutModel {

	
	/**
	 * 写入配置
	 */
	public FlyOutModel setCodeCfg(CodeCfg codeCfg);

	/**
	 * 生成实体类
	 */
	public String mkModel(Table table);
	
	/**
	 * 生成实体类SO
	 */
	public String mkModelSO(Table table);
	
	
	/**
	 * 创建全部，并IO到指定地址
	 */
	public void mkIO();
	
	
	
}
