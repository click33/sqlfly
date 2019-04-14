package com.fly.re.read;

import java.util.List;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.Table;

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
	public CodeCfg readInfo();
	
	
	/**
	 * 获取指定表的所有列信息
	 */
	public List<Column> getColumnList(Table table);
	
	
	
	
	
}
