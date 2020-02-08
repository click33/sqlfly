# 示例

---

> - 本篇将带你从零开始搭建一个SqlFly环境，从而让你快速熟悉SqlFly的使用姿势
> - 数据库以MySQL为例

## 普通java版

#### 1、创建测试数据库
在数据库中创建测试数据库：`sqlfly-demo`，并在其中运行以下sql代码：

``` sql
# 创建：用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id号',
  `username` varchar(30) DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `sex` int(11) DEFAULT 1 COMMENT '性别(1=男，2=女)',
  `age` int(11) DEFAULT 1 COMMENT '年龄',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE 
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户表';

INSERT INTO `sys_user` VALUES (10001, '省长', '123456', 1, 18, '2019-08-05 17:58:05');
INSERT INTO `sys_user` VALUES (10002, '小言', '1234567', 1, 19, '2019-08-05 18:58:05');
INSERT INTO `sys_user` VALUES (10003, '闹心', '12345678', 2, 20, '2019-08-05 19:58:05');
INSERT INTO `sys_user` VALUES (10004, '榴莲', '123456789', 2, 21, '2019-08-05 20:58:05');


# 创建：文章表
DROP TABLE IF EXISTS ser_article;
CREATE TABLE `ser_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id号',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `content` longtext DEFAULT NULL COMMENT '内容',
  `user_id` int(11) DEFAULT 1 COMMENT '发表人user_id，外键',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='文章表';

INSERT INTO `ser_article` VALUES (0, '测试标题1', '测试内容1', 10001, '2019-08-06 20:58:05');
INSERT INTO `ser_article` VALUES (0, '测试标题2', '测试内容2', 10002, '2019-08-06 21:58:05');


```

#### 2、创建项目
在IDE中新建一个普通java项目：`sqlfly-test`

#### 3、设置jar包依赖
在项目**根目录**新建文件夹`lib`，将 `mysql数据库驱动` 和 `sqlfly-xxx.jar` 复制到其中
- 如果你没有MySQL数据库驱动，[点此下载：sql-connector-java-5.1.45.jar](https://color-test.oss-cn-qingdao.aliyuncs.com/sqlfly-doc/mysql-connector-java-5.1.45.jar)
- sqlfly-xxx.jar，请在 [下载](/start/download?id=sqlfly-demo) 页下载
- 复制完成后，再讲这两个jar包添加到项目的构建路径（不会的同学请自行百度）

#### 4、配置文件
在源代码根目录下（一般是src/）创建配置文件：`sqlfly.properties`，并输入以下内容

``` java
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

> 不要无脑复制，记得把连接用户名和密码改成自己的

#### 5、创建主类
在项目中新建包 `com.pj` ，在此包内新建主类 `App.java`，并输入以下代码：
``` java
/**
 * SqlFly示例 
 */  
public class App {
	public static void main(String[] args) {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		Map<String, Object> map = sqlFly.getMap("select * from sys_user where id = 10001 ");
		System.out.println(map);
	}
}
```

#### 6、运行
运行代码，当你从控制台看到类似下面的内容时，就代表框架已经成功搭建了

![运行结果](https://color-test.oss-cn-qingdao.aliyuncs.com/sqlfly-doc/App-run.jpg)



## SpringBoot集成版

> SpringBoot集成SqlFly的步骤大体与[普通java版]相同，且得益于SpringBoot的依赖注入特性，步骤将更加简单

#### 1、创建测试数据库
新建测试数据库 `sqlfly-demo` ，操作同[普通java版] 

#### 2、创建项目
在IDE中新建一个Springboot项目（不会的同学请自行百度）

#### 3、设置jar包依赖
- 在 `pom.xml` 中添加依赖：

``` xml 
<!-- sqlfly框架，在线文档：https://sqlfly.dev33.cn/  -->
<dependency> 
	<groupId>cn.dev33</groupId>
	<artifactId>sqlfly-spring</artifactId>
	<version>1.0.0</version>
</dependency>
```

#### 4、配置文件
在`application.yml`配置文件中，输入以下代码：

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

> - 如果你习惯于 `application.properties` 类型的配置文件，那也很好办: 
> - 百度： [springboot properties与yml 配置文件的区别](https://www.baidu.com/s?ie=UTF-8&wd=springboot%20properties%E4%B8%8Eyml%20%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E7%9A%84%E5%8C%BA%E5%88%AB)

#### 5、创建主类
在项目中新建包 `com.pj` ，在此包内新建主类 `SqlFlyDemoApplication.java`，输入以下代码：

``` java
@SqlFlySetup	// 加载SqlFLy，一定要加这个注解
@SpringBootApplication
public class SqlFlyDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SqlFlyDemoApplication.class, args); // 启动 springboot 
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		Map<String, Object> map = sqlFly.getMap("select * from sys_user where id = 10001 ");
		System.out.println(map);
	}
}
```

#### 6、运行
运行代码，当你从控制台看到类似下面的内容时，就代表框架已经成功搭建了

![运行结果](https://color-test.oss-cn-qingdao.aliyuncs.com/sqlfly-doc/App-run.jpg)




## 详细了解
通过这个示例，你已经对SqlFly有了初步的了解，那么现在开始详细了解一下它都有哪些[能力](/use/get-sqlfly)吧







