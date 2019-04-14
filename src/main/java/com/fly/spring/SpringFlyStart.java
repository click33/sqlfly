package com.fly.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fly.jdbc.cfg.FlyCfg;
import com.fly.jdbc.cfg.FlyRun;
import com.fly.jdbc.cfg.FlyStart;


/**
 * 与SpringBoot集成, 保证此类被扫描，即可完成SqlFly与SpringBoot的集成 
 * @author kongyongshun
 *
 */
@Service	
public class SpringFlyStart {

	
	// 注入前提是打开FlyCfg类上面的三个注解 
	
	@Autowired
	public SpringFlyStart(FlyCfg flyCfg){
		FlyStart.isInitRun = true;	// 无需主动初始化
		FlyRun.flyCfg = flyCfg;		// springboot注入配置类
	}
	
	
	
}
