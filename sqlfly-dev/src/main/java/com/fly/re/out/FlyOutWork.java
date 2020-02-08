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
	 * @param codeCfg
	 * @return
	 */
	public FlyOutWork setCodeCfg(CodeCfg codeCfg);

	
	// ================================================== 
	/**
	 *  生成add()
	 * @param table
	 * @return
	 */
	public String mkAdd(MkTable table);
	/**
	 * 生成delete()
	 * @param table
	 * @return
	 */
	public String mkDelete(MkTable table);
	/**
	 * 生成update()
	 * @param table
	 * @return
	 */
	public String mkUpdate(MkTable table);
	/**
	 * 生成getById()
	 * @param table
	 * @return
	 */
	public String mkGetById(MkTable table);
	/**
	 * 生成getList()
	 * @param table
	 * @return
	 */
	public String mkGetList(MkTable table);
	// ================================================== 
	
	

	/**
	 * 生成Dao接口样本
	 * @param table
	 * @return
	 */
	public String mkDaoI(MkTable table);
	
	/**
	 * 生成Dao一张表的所有实现方法
	 * @param table
	 * @return
	 */
	public String mkDaoImpl(MkTable table);
	
	
	/**
	 *  生成Service接口样本
	 * @param table
	 * @return
	 */
	public String mkServiceI(MkTable table);
	
	/**
	 * 生成Service实现类
	 * @param table
	 * @return
	 */
	public String mkServiceImpl(MkTable table);
	
	
	/**
	 * 生成Controller样本
	 * @param table
	 * @return
	 */
	public String mkController(MkTable table);


	/**
	 * 生成所有表的实现方法并IO
	 * @return
	 */
	public String mkIO();
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
