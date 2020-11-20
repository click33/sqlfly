# SqlFly

## 介绍
一个好用的Java语言orm框架，在线文档：[https://sqlfly.dev33.cn](https://sqlfly.dev33.cn)

 :kissing_closed_eyes:  :kissing_closed_eyes:  :kissing_closed_eyes:  :innocent: :innocent: :innocent:    :stuck_out_tongue:  :stuck_out_tongue:  :stuck_out_tongue: :heart:  :heart:  :heart: 

## 特点
1. 对内将`jdbc`繁琐步骤进行封装，并对外提供简洁好用的API 
2. 自动将查询结果集映射到实体类中，可以让你完全脱离对`ResultSet`的操作，贴心省事
3. 内置了多种常见查询的封装，如：统计查询、分页查询等
4. 内置一个简单的连接池实现，不使用第三方连接池也可以获得较快的执行速度，当然也可以方便的集成第三方连接池，如：`c3p0`、`dbcp`、`druid`
5. 内置代码生成器，可以自动生成标准的三层架构代码，节省`80%`以上的重复代码编写 
6. 代码设计上冗余了一些接口，可方便的对框架进行扩展，以及与`spring`等第三方框架的结合 


## 设计思想
1. sql写在代码里，不会让你写在反人类的xml中（此处@`MyBatis`）
2. 接口设计上，不参与sql建设，最大减少调用者心智负担（此处@`Hibernate`和`jooq`），当然也会有一些例外，因为——它们实在太常用了
3. 所有api均是无状态函数，不会让你在调试时，发生“这个值什么时候改的？”的情况
4. `Conection` 为自动提交模式，在开启事务时关闭自动提交，最接近原生sql操作逻辑


## 贡献代码
1. 在github上fork一份到自己的仓库
2. clone自己的仓库到本地电脑
3. 在本地电脑修改、commit、push
4. 提交pr（点击：New Pull Request）
5. 等待合并


## 需求提交
- 我们深知一个优秀的项目需要海纳百川，[点我在线提交需求](http://sa-app.dev33.cn/wall.html?name=sqlfly)


## 建议贡献的地方
- 修复源码现有bug，或增加新的实用功能
- 完善在线文档，或者修复现有错误之处
- 更多demo示例：比如SSM版搭建步骤 
- 如果更新实用功能，可在文档友情链接处留下自己的推广链接


## QQ群
QQ交流群：[782974737 点击加入](https://jq.qq.com/?_wv=1027&k=5DHN5Ib)

![加群](https://color-test.oss-cn-qingdao.aliyuncs.com/sqlfly-doc/qqq.png)





