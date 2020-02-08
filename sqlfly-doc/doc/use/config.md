# 框架配置
SqlFly支持多种方式配置框架信息

--- 
### 1、通过代码配置
``` java 
	@Test
	public void test8_1() {
		// 1、构建配置信息 
		FlyConfig config = new FlyConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setUrl("jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8");
		config.setUsername("root");
		config.setPassword("root123456");
		FlyObjects.setConfig(config);	// 注入到框架中
		
		System.out.println(SqlFlyFactory.getSqlFly().getConnection());	// 测试
	}
```

### 2、通过配置文件配置
在源代码根目录下（一般是src/，maven项目一般为src/main/resources/）创建配置文件：`sqlfly.properties`，并输入以下内容
```
# 连接信息
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8
username=root
password=root123456
# 是否启用内置连接池 
ispool=false
# 是否在控制台打印sql执行日志
printSql=true
# 是否在初始化时打印版本号
isV=false
```

### 3、springboot环境怎么配置？
springboot更加方便，直接在`application.yml`配置文件中，输入以下代码：

``` java
spring: 
    # SqlFly的配置 
    sqlfly: 
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8
        username: root
        password: root123456 
        # 是否启用内置连接池 
        ispool: false
        # 是否控制台打印日志 
        printSql: true 
```

- 如果你习惯于 `application.properties` 类型的配置文件，那也很好办: 
- 百度： [springboot properties与yml 配置文件的区别](https://www.baidu.com/s?ie=UTF-8&wd=springboot%20properties%E4%B8%8Eyml%20%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E7%9A%84%E5%8C%BA%E5%88%AB)


