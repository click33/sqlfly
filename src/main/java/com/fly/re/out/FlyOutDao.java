package com.fly.re.out;

import com.fly.re.model.CodeCfg;
import com.fly.re.model.Table;

/**
 * 生成代码的接口
 * @author kongyongshun
 *
 */
public interface FlyOutDao {

	
	

	/**
	 * 写入配置
	 */
	public FlyOutDao setCodeCfg(CodeCfg codeCfg);

	
	// ================================================== 
	/**
	 * 生成save()
	 */
	public String mkSave(Table table);
	/**
	 * 生成delete()
	 */
	public String mkDelete(Table table);
	/**
	 * 生成update()
	 */
	public String mkUpdate(Table table);
	/**
	 * 生成getById()
	 */
	public String mkGetById(Table table);
	/**
	 * 生成getList()
	 */
	public String mkGetList(Table table);
	// ================================================== 
	
	

	/**
	 * 生成Dao接口样本
	 */
	public String mkDaoI(Table table);
	
	/**
	 * 生成Dao一张表的所有实现方法
	 */
	public String mkDaoImpl(Table table);
	
	
	/**
	 * 生成Service接口样本
	 */
	public String mkServiceI(Table table);
	
	/**
	 * 生成Service实现类
	 */
	public String mkServiceImpl(Table table);
	
	
	/**
	 * 生成Controller样本
	 */
	public String mkController(Table table);
	
	/**
	 * 生成Dao工厂接口类
	 */
	public String mkDaoFactory();
	
	/**
	 * 生成Service工厂接口类
	 */
	public String mkServiceFactory();
	
	/**
	 * 生成所有表的实现方法并IO
	 */
	public String mkIO();
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
