package com.fly.re.out;

import com.fly.re.CodeCfg;
import com.fly.re.model.MkClass;
import com.fly.re.model.MkTable;

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
	public MkClass mkModel(MkTable table);
	
	/**
	 * 生成实体类SO
	 */
	public MkClass mkModelSO(MkTable table);
	
	
	/**
	 * 创建全部，并IO到指定地址
	 */
	public void mkIO();
	
	
	
}
