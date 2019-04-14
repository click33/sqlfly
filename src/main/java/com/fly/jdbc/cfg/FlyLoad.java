package com.fly.jdbc.cfg;

/**
 * 初始化加载FlyRun
 * @author kongyongshun
 *
 */
public interface FlyLoad {

	
	/**
	 * 加载之前执行
	 * @return
	 */
	public boolean loadBefore();
	
	
	/**
	 * 开始加载
	 * @return
	 */
	public boolean load();
	

	/**
	 * 加载之后执行
	 * @return
	 */
	public boolean loadAfter();
	
	
}
