package com.fly.re.read;

import com.fly.re.CodeCfg;

/**
 * 读取的接口
 * @author kongyongshun
 *
 */
public interface FlyRead {

	public FlyRead setCodeCfg(CodeCfg codeCfg);
	
	/**
	 * 根据CodeCfg配置读取
	 */
	public void readInfo();
	

	
	
	
}
