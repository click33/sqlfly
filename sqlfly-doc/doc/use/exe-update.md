# 执行增删改

- 方法 `getUpdate(String sql, Object... args) `会执行sql并返回受影响行数，
- 因此**增、删、改**的sql统一交给 `getUpdate` 方法来处理

---

### 示例 - 获取受影响行数
``` java
	@Test
	public void test2_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "delete from sys_user where id = ?";
		int line = sqlFly.getUpdate(sql, 10009);
		System.out.println("成功删除：" + line + "条数据");
	}
```

### 示例 - 增加一条记录
``` java 
	@Test
	public void test2_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" sys_user(username, password, sex, age, create_time)	" + 
				" values (?, ?, ?, ?, now()) 	";
		Object [] args = {"刘三刀", "123456", 1, 40};	// 与sql语句中的 ?占位符参数 一一对应即可 
		sqlFly.getUpdate(sql, args);
		System.out.println("记录增加成功");
	}
```

### ?参数占位符的弊端
不难发现，上面的示例虽然可以轻松的插入一条数据，却有一个非常明显的弊端，那就是当一条sql语句的参数非常多时，将会发生下面类似下面的这种情况
``` java
	String sql = 
			" insert into	" + 
			" sys_user(username, password, sex, age, phone, address, status, role_id, follow_count, fans_count, create_time)	" + 
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, now()) 	";
	Object [] args = {"韩勇", "123456", 1, 40, "15866668888", "山东省 济南市 历下区", 1, 11, 0, 0};
```
可以看出，此时再想做到参数列表与sql参数的一一对应将是极其困难的，`?参数占位符` 在应对这种参数较多的情况将会非常痛苦，稍微一不注意就会在参数顺序中迷失自我，
所以这时候就需要用 `#参数模式` 来解决这个问题 

### # 参数模式
##### 1、先创建实体类
``` java
public class SysUser {
	private long id;			// id号
	private String username;	// 昵称
	private String password;	// 密码
	private int sex;			// 性别（1=男，2=女）
	private int age;			// 年龄 
	private Date create_time;	// 创建日期
}
```
- 可以利用ide的快捷键生成字段的 `getter` 和 `setter` （不会的同学请自行百度）

##### 2、示例 - #参数模式
``` java 
	@Test
	public void test2_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" sys_user(username, password, sex, age, create_time)	" + 
				" values (#{username}, #{password}, #{sex}, #{age}, now()) 	";
		SysUser user = new SysUser();
		user.setUsername("王冲");
		user.setPassword("123456");
		user.setSex(1);
		user.setAge(18);
		sqlFly.getUpdate(sql, user);	// 直接实体类入参 
		System.out.println("记录增加成功");
	}
```
- 框架内部会将#{参数}翻译成?占位符，实际上最终执行的还是?占位符方式，不过这为你在书写sql时提供了极大的便利


## 示例 - 获取增加记录的主键
- 在自增主键的表中，有时候我们需要在插入记录后立即获取它的主键，有很多种方法可以做到这件事情，这里以mysql为例，提供一个思路
```
	@Test
	public void test2_4() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = " insert into	" + 
				" ser_article(title, content, user_id, create_time)	" + 
				" values (?, ?, ?, now()) 	";
		Object [] args = {"齐鲁晚报", "据报到...", 10001};	
		// 必须开启事务，保证两条sql使用的是同一条Connection连接
		sqlFly.beginTransaction();		// 开启事务
		sqlFly.getUpdate(sql, args);	// 增加
		long id = sqlFly.getModel(long.class, "SELECT @@identity");	// 获取上一条插入记录的主键 
		sqlFly.commit();				// 提交事务 
		System.out.println("记录增加成功，记录主键为：" + id);
	}
```


