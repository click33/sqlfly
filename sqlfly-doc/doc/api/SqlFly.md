# SqlFly 
与数据库交互的核心

---

##  1、基础
#### getId()
- 获取此 `SqlFly实例` 的id，框架每创建一个SqlFly实例id都会+1，从0开始 

#### getPrevSql()
- 获取此 `SqlFly实例` 的上一句执行的sql，从未执行过sql的情况下返回 `null`


##  2、连接池相关
#### getDataSource()
- 获取此 `SqlFly实例` 的上所绑定的 `DataSource` 对象

#### setDataSource(DataSource dataSource)
- 设置此 `SqlFly实例` 的上所绑定的 `DataSource` 对象 <br/>
- 如果你在实例化SqlFly后并没有指定DataSource而进行了获取`Connection`的相关操作，那将使用SqlFly默认的连接池


##  3、连接相关
#### getConnection()
- 获取此 `SqlFly实例` 的上所绑定的 `Connection` 对象
	- **慎用！一般情况下你没有理由调用此方法！**
	- 如果你希望将Connection取出并脱离此SqlFly的绑定，请务必在取出Connection后执行setConnection(null) 
	- 并在使用完毕后将其close()，以确保不会造成连接泄露

#### setConnection(Connection connection)
- 设置此 `SqlFly实例` 的上所绑定的 `Connection` 对象 <br/>
	- **慎用！一般情况下你没有理由调用此方法！**

#### close()
- 关闭此 `SqlFly实例` 绑定的 `Connection` <br/>
- 如果此 `Connection` 正在进行事务，则事务会被回滚 


##  4、事务相关
#### beginTransaction()
- 开启事务
	- 为了能够让Connection能够被及时回收，SqlFly采用 用完即释 的策略，即：在每一次执行sql后将立刻释放 Connection
	- 在开启事务后，用完即释模式将被关闭，直至你提交或者回滚事务后，Connection才会被释放

#### begin(FlyLambdaBegin code)
- 以lambda的方式开启事务
- **1个重载**
	- begin(FlyLambdaBegin begin, FlyLambdaRollback rollback)	以lambda的方式开启事务，并指定 rollback代码块 
- 参见示例：[开始事务-lambda表达式方式](use/transaction?id=开始事务-lambda表达式方式)

#### beginRT(FlyLambdaBeginRT code)
- 以lambda的方式开启事务，并接受一个返回值
- **1个重载**
	- beginRT(FlyLambdaBeginRT begin, FlyLambdaRollbackRT rollback)	以lambda的方式开启事务，并指定 rollback代码块 
- 参见示例：[开始事务-lambda表达式方式，并接收一个返回值](use/transaction?id=开始事务-lambda表达式方式，并接收一个返回值)




#### commit() 
- 提交事务

#### rollback()
- 回滚事务
- **3个重载**
	- rollback(String msg)	回滚事务并抛出异常，异常信息为 msg 
	- rollback(Throwable e)	回滚事务并抛出异常，异常原因为 e 
	- rollback(String msg, Throwable e)	回滚事务并抛出异常，异常信息为 msg ，异常原因为 e 


##  5、基础sql执行
#### getExecute(String sql, Object... args)
- 执行任意类型sql，返回执行sql后的 `PreparedStatement`

| 参数名称	| 说明		|
| --------	| --------	|
| sql		| sql语句	|
| args		| 参数列表	|

#### getUpdate(String sql, Object... args)
- 执行增、删、改，返回受影响行数
- 参数同上

#### getResultSet(String sql, Object... args)
- 查询，返回ResultSet结果集
- 参数同上
	* 大多数情况下你不应该调用此方法，此方法会将ResultSet处理权交给你
	* 这意味着框架不会自动在处理完毕结果集后将其关闭， 
	* 因此你必须在处理完结果集后执行rs.getStatement().close()释放资源; 
	* 并且在调用此方法之前必须开启事务

#### getScalar(String sql, Object... args)
- 聚合查询，返回第一行第一列值


##  6、查询 - 结果集映射为实体类
#### getModel(Class< T > cs, String sql, Object... args)
- 将查询结果映射为指定 `类型T` 的实例

| 参数名称	| 说明								|
| --------	| --------							|
| cs		| 指定映射类型，同时也是返回值的类型  |
|			| 其余参数同上						|


#### getList(Class<T> cs, String sql, Object... args)
- 将查询结果映射为指定 `类型T` 的集合 
- 参数同上

#### getListPage(Page page, Class<T> cs, String sql, Object... args)
- 将分页查询的结果映射为指定 `类型T` 的集合

| 参数名称	| 说明			|
| --------	| --------		|
| page		| 分页参数		|
|			| 其余参数同上	|


## 7、查询 - 结果集映射为Map
#### getMap(String sql, Object... args)
- 将查询结果映射为 `Map< String, Object >`
- 参数见上 

#### getMapList(String sql, Object... args)
- 将查询结果映射为 `Map< String, Object >` 的集合 
- 参数见上 

#### getMapListPage(Page page, String sql, Object... args)
- 将分页查询的结果映射为 `Map< String, Object >` 的集合 
- 参数见上 

#### getListMapByCol(String keyCol, String sql, Object... args)
- 将查询结果旋转列，并映射为 `Map< String, Object >` 的集合 
- 参数见上 

| 参数名称	| 说明								|
| --------	| --------							|
| keyCol	| 用来做key的列，填null则取第一列	|
|			| 其余参数同上						|


## 8、其它快捷方法
#### getCount(String sql, Object... args)
- 返回这条sql能查到多少条记录

#### getScalarInt(String sql, Object... args)
- 将查询结果强转为 `Integer` 并返回










