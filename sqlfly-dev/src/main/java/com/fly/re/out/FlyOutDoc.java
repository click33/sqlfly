package com.fly.re.out;

import com.fly.re.CodeCfg;
import com.fly.re.model.MkTable;

/**
 * 生成doc文档 
 * @author kong
 *
 */
public interface FlyOutDoc {

	
	/**
	 * 写入配置
	 * @param codeCfg
	 * @return
	 */
	public FlyOutDoc setCodeCfg(CodeCfg codeCfg);
	
	/**
	 * 生成文档 add 
	 * @param table
	 * @return
	 */
	public String mkAdd(MkTable table);

	/**
	 * 生成文档 delete 
	 * @param table
	 * @return
	 */
	public String mkDelete(MkTable table);

	/**
	 * 生成文档 update 
	 * @param table
	 * @return
	 */
	public String mkUpdate(MkTable table);

	/**
	 * 生成文档 getById 
	 * @param table
	 * @return
	 */
	public String mkGetById(MkTable table);

	/**
	 * 生成文档 getList  
	 * @param table
	 * @return
	 */
	public String mkGetList(MkTable table);
	
	
	/**
	 * 开始制作 
	 * @return
	 */
	public String mkIO();
	
	
	
	
}
