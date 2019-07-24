package com.fly.re.out;

import com.fly.re.CodeCfg;
import com.fly.re.model.MkTable;

/**
 * 生成代码的接口
 * @author kongyongshun
 *
 */
public interface FlyOutWork {

	
	

	/**
	 * 写入配置
	 */
	public FlyOutWork setCodeCfg(CodeCfg codeCfg);

	
	// ================================================== 
	/**
	 * 生成add()
	 */
	public String mkAdd(MkTable table);
	/**
	 * 生成delete()
	 */
	public String mkDelete(MkTable table);
	/**
	 * 生成update()
	 */
	public String mkUpdate(MkTable table);
	/**
	 * 生成getById()
	 */
	public String mkGetById(MkTable table);
	/**
	 * 生成getList()
	 */
	public String mkGetList(MkTable table);
	// ================================================== 
	
	

	/**
	 * 生成Dao接口样本
	 */
	public String mkDaoI(MkTable table);
	
	/**
	 * 生成Dao一张表的所有实现方法
	 */
	public String mkDaoImpl(MkTable table);
	
	
	/**
	 * 生成Service接口样本
	 */
	public String mkServiceI(MkTable table);
	
	/**
	 * 生成Service实现类
	 */
	public String mkServiceImpl(MkTable table);
	
	
	/**
	 * 生成Controller样本
	 */
	public String mkController(MkTable table);


	/**
	 * 生成所有表的实现方法并IO
	 */
	public String mkIO();
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
