package com.fly.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fly.jdbc.cfg.FlyConfig;
import com.fly.jdbc.cfg.FlyObjects;


/**
 * 与SpringBoot集成, 保证此类被扫描，即可完成SqlFly与SpringBoot的集成 
 * @author kongyongshun
 *
 */
@Service	
public class SpringFlyStart {

	
	// 注入前提是打开FlyConfig类上面的三个注解 
	
	// 自动注入 FlyConfig 
	@Autowired
	public void setFlyConfig(FlyConfig config){
		FlyObjects.setConfig(config); 
	}
	
	
	
	
}
