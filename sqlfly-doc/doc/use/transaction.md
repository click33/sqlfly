# 事务

- 为了能够让`Connection`能够被及时回收，SqlFly采用 `用完即释` 的策略，即：在每一次执行sql后将立刻释放 `Connection`
- 在开启事务后，`用完即释`模式将被关闭，直至你提交或者回滚事务后，`Connection`才会被释放 
- 有些时候，我们希望sql根据业务进行提交或回滚，也需要用到事务

--- 

## 开启事务 
调用 `beginTransaction()` 开启事务 
``` java 
	// 测试5-1 开启事务 
	@Test
	public void test5_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		try {
			sqlFly.beginTransaction();	// 开启事务
			sqlFly.getUpdate("insert into sys_user values()");		// 执行一条会失败的sql
			sqlFly.commit();			// 提交事务 
			System.out.println("事务提交成功");
		} catch (Exception e) {
			sqlFly.rollback();			// 回滚事务
			System.out.println("事务提交失败，已回滚");
			e.printStackTrace();
		}
	}
```

## 开始事务 - lambda表达式方式
- 如果你的jdk版本为1.8或以上，那么使用以下lambda方式开启事务将会让你的代码更加优雅，
- 如果你的jdk版本低于1.8，那么**强烈建议**你升级至1.8或以上
``` java 
	@Test
	public void test5_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		sqlFly.begin(()->{	// SqlFly将在开启事务后，再调用此代码块
			System.out.println("事务已被开启");
			sqlFly.getUpdate("insert into sys_user values()");		// 执行一条会失败的sql
		}, (e)->{	// 事务发生异常后，SqlFly将回滚事务后，再调用此代码块
			System.out.println("事务已被回滚");
			e.printStackTrace();
		});
	}
```
- begin()方法接受两个参数
- 第一个参数为 `begin 代码块`，SqlFly会在开启事务后调用
- 第二个参数为 `rollback 代码块`，SqlFly会在 `begin 代码块` 发生异常并回滚事务后调用
- begin()方法有一个重载，只需要传入 一个 `begin 代码块` 就可以了，这时候`begin 代码块`内抛出的异常将不会被自动捕获，需要你手动捕捉并回滚事务


## 开始事务 - lambda表达式方式，并接收一个返回值
- 在java的 `lambda表达式` 中是不能为表达式之外的变量赋值的，如果我们想把一个值从 `lambda表达式` 内部带出来，应该怎么办？
``` java
	// 测试5-3 开启事务 lambda表达式, 并指定返回值 
	@Test
	public void test5_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String return_obj = sqlFly.beginRT(() -> { // SqlFly将在开启事务后，再调用此代码块
			System.out.println("事务已被开启");
			sqlFly.getUpdate("insert into sys_user values()"); // 执行一条会失败的sql
			return "这是返回的值，可以是任意类型";
		}, (e) -> { // 事务发生异常后，SqlFly将回滚事务后，再调用此代码块
			System.out.println("事务已被回滚");
			e.printStackTrace();
			return "这是返回的值，可以是任意类型";
		});
		System.out.println("返回的值：" + return_obj);
	}
```
- 如上，`beginRT()` 方法的用法与 `begin()` 基本一致，不同点就在于 `beginRT()` 是可以带返回值的，
- 而且这个返回值并不局限于一个类型，它的类型取决于你用什么类型的变量去接受它（没错，就是这么神奇）


