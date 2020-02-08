# 链接池

SqlFly内部有一个简易的连接池实现，只需在配置文件配置 `ispool=true`即可，当然你也可以轻松的集成第三方连接池 

--- 

## 集成第三方连接池 
- 以阿里的druid为例，[点此下载 druid-1.1.19.jar](https://color-test.oss-cn-qingdao.aliyuncs.com/sqlfly-doc/druid-1.1.19.jar)

##### 1、添加依赖
将 druid-1.1.19.jar 复制到根目录下的 `lib` 文件夹内 

##### 2、配置文件
在项目根目录下创建配置文件 `druid.properties` ，并复制以下信息
```
# 连接信息 
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8
username=root
password=root123456
```

##### 3、创建 `DruidDataSourceUtil ` 生成 `DataSource`
创建 `DruidDataSourceUtil.java`，并复制以下代码 
```
/** 创建一个druid的数据源  */
public class DruidDataSourceUtil {
	private static String propertiesPath = "druid.properties";	// 配置文件地址
	private static DataSource dataSource = null;
	/** 获取数据源  */
	public static synchronized DataSource getDataSource() {
		try {
			if(dataSource == null) {
				// 加载配置文件 
				InputStream is = DruidDataSourceUtil.class.getClassLoader().getResourceAsStream(propertiesPath);
			    Properties properties = new Properties();
			    properties.load(is);
			    // 创建连接池对象
			    DruidDataSourceUtil.dataSource = DruidDataSourceFactory.createDataSource(properties);
			}
			return dataSource;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
```

##### 4、将 `DataSource` 注入到SqlFly中
```
	@Test
	public void test6_1() {
		DataSource dataSource = DruidDataSourceUtil.getDataSource();	// 获取 datasource
		FlyObjects.setDataSource(dataSource);							// 注入到SqlFly框架中 
		System.out.println(FlyObjects.getDataSource());
		// ... 此时再获取 SqlFly，其Connection 就是由druid连接池创建的了 
	}
```

> 至此，大功告成



## 集成第三方连接池 - springboot版 
> SpringBoot集成druid的步骤大体与[普通java版]相同，且得益于SpringBoot的依赖注入特性，步骤将更加简单

##### 1、添加依赖
在pom.xml中
``` xml
	<!-- 阿里 druid 连接池  -->
	<dependency>
		<groupId>com.alibaba</groupId>
		<artifactId>druid</artifactId>
		<version>1.1.19</version>
	</dependency>
```

##### 2、配置文件
在 application.yml 里，复制以下代码
``` 
spring: 
    # druid连接池相关配置
    datasource: 
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/sqlfly-demo?useUnicode=true&characterEncoding=utf-8
        username: root
        password: root123456
```

##### 3、创建 `DruidConfig.java` 生成 `DataSource`
在包 `com.pj` 创建类  `DruidConfig.java`，并复制以下代码 
``` java 
	// 配置 druid 相关信息
	@Configuration
	public class DruidConfig {
		// 返回druid的DataSource
		@Bean
		@ConfigurationProperties(prefix = "spring.datasource")
		public DataSource druid() {
			return new DruidDataSource();
		}
	}
```

##### 4、测试
在 `SqlFlyDemoApplication.java` 里，测试
``` java 
	public static void main(String[] args) {
		SpringApplication.run(SqlFlyDemoApplication.class, args); // 启动 springboot 
		System.out.println(SqlFlyFactory.getSqlFly().getDataSource());
	}
```

> 如果看到类似以下打印，则证明已经druid已经集成成功

``` json
	{
		CreateTime:"xxxx-xx-xx xx:xx:xx",
		ActiveCount:0,
		PoolingCount:0,
		CreateCount:0,
		DestroyCount:0,
		CloseCount:0,
		ConnectCount:0,
		Connections:[
		]
	}
```



## 其它连接池
> 如果你能看懂上面两个例子的原理，就不难理解，所谓连接池，本质上不过就是为了构建一个 `DataSource` 对象而已，明白了这一点，你就可以轻松集成任意连接池了




