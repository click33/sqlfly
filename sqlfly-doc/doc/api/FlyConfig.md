# FlyConfig
`FlyConfig` 是框架的配置类

--- 

## 所有字段

| 参数名称			| 类型		| 默认值	| 说明									|
| :--------			| :--------	| :--------	| :--------								|
| driverClassName	| String	| null		| 内置连接池 - 数据库驱动地址			|
| url				| String	| null		| 内置连接池 - 连接地址					|
| username			| String	| null		|  内置连接池 - 连接用户名				|
| password			| String	| null		| 内置连接池 - 连接密码					|
| ispool			| Boolean	| false		|  内置连接池 - 是否启用内置连接池		|
| init				| int		| 10		| 内置连接池 - 初始化连接数				|
| min				| int		| 5			| 内置连接池 - 最小链接数				|
| max				| int		| 20		| 内置连接池 - 最大连接数				|
| printSql			| Boolean	| false		| 是否在控制台输出每次执行的SQL与参数	|
| sqlhh				| String	| [SQL]		| 输出SQL的前缀							|
| argshh			| String	| [ARGS]	| 输出参数的前缀						|
| defaultLimit		| int		| 1000		| Page == null时默认取出的数据量		|
| isV				| Boolean	| true		| 是否在初始化配置时打印版本字符画		|

用法参见：[框架配置](../use/config)



	