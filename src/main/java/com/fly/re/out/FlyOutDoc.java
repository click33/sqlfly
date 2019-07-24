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
	 */
	public FlyOutDoc setCodeCfg(CodeCfg codeCfg);
	
	/**
	 * 生成文档 add 
	 */
	public String mkAdd(MkTable table);

	/**
	 * 生成文档 delete 
	 */
	public String mkDelete(MkTable table);

	/**
	 * 生成文档 update 
	 */
	public String mkUpdate(MkTable table);

	/**
	 * 生成文档 getById 
	 */
	public String mkGetById(MkTable table);

	/**
	 * 生成文档 getList  
	 */
	public String mkGetList(MkTable table);
	
	
	/**
	 * 开始制作 
	 */
	public String mkIO();
	
	
	
	
}
