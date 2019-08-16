package com.fly.jdbc.cfg;

/**
 * 初始化加载FlyRun默认实现
 * @author kong
 *
 */
public class FlyLoadDefault implements FlyLoad {

	
	@Override
	public boolean loadBefore() {
		return true;
	}

	@Override
	public boolean load() {
		return true;
	}

	@Override
	public boolean loadAfter() {
		return true;
	}

	
	
	
	
}
