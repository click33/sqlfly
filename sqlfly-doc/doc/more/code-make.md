# 代码生成器 

SqlFly框架内置代码生成器 

--- 

## 示例 - 生成标准三层代码
``` java 
	@Test
	public void test9_1() {
		CodeUtil.codeCfg
			.setProjectPath("E:\\sqlfly-code")
			.setCodePath("src/")
			.setPackagePath( "com.pj.x_project")
			.setAuthor("shengzhang")
			.setIs_three(true)
			.setIs_lomock(false);
		CodeUtil.run();
	}
```

## 示例 - 生成简洁模式代码
无接口，无业务逻辑层，按模块分包 
``` java 
	@Test
	public void test9_1() {
		CodeUtil.codeCfg
			.setProjectPath("E:\\sqlfly-code")
			.setCodePath("src/")
			.setPackagePath( "com.pj.x_project")
			.setAuthor("shengzhang")
			.setIs_three(false)
			.setIs_lomock(true);
		CodeUtil.run();
	}
```


## 所有可配置项

| 方法名称			| 默认值			| 说明																									|
| :--------			| :--------			| :--------																								|
| setProjectPath()	| ""				| 设置项目所在路径，写"\"可以求得当前项目根目录，但更推荐你填写一个本地磁盘路径，以免发生代码覆盖的情况	|
| setCodePath()		| "src/main/java/"	| 设置代码所在路径，普通项目一般为 `src/`，maven项目一般为`src/main/java/`								|
| setPackagePath()	| ""				| 设置生成代码的包路径																					|
| setAuthor()		| ""				| 设置生成代码的作者名称																					|
| setDocPath()		| "doc/"			| 设置接口文档的地址（markdown形式）																		|
| setIs_three()		| true				| 生成代码的方式（true=标准三层，false=简洁模式）														|
| setIs_lomock()	| false				| 生成代码的实体类中是否使用lomock																		|


> 注：以上的代码生成器生成的代码只能在spring环境中使用，如果要在非spring环境中使用，
> 则需要修改一下（其实也就是删掉几个有关spring的注解）
> 目前只保证MySql数据可以读取成功，如果要在其他数据库中使用，可实现 `FlyRead` 接口，替换 `CodeUtil.flyRead` 字段进行扩展

