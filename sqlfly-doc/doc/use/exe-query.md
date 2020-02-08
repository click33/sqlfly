# 执行查询

- 本篇将展示SqlFly的各种查询能力 

---


## 映射为 Object 
``` java 
	@Test
	public void test3_1() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select username from sys_user where id = ?";
		Object username = sqlFly.getScalar(sql, 10001);
		System.out.println("id=10001的用户，其username=" + username);
	}
```
- `getScalar()` 虽然返回值为 `Object` 类型，但如果你打印其 `username.getClass()`，却会发现它的值是 `java.lang.String`，它的底层类型是由框架自由决定的，
- 这样看来，`getScalar()` 虽然调用方便，但它的映射类型却是不可控的，
- 如果需要强行控制其返回值类型，则需要调用 `getModel()`，往下看

## 映射为 Model
``` java 
	@Test
	public void test3_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user where id = ?";
		SysUser user = sqlFly.getModel(SysUser.class, sql, 10001);
		System.out.println("id=10001的用户，其详细信息为：" + user);
	}
```
- `getModel()` 的参数顺序为：映射类型、sql语句、参数列表（不限长参数）
- 映射类型的取值不仅可以是 `SysUser.class` 这样的实体类，也可以是 `int.class` 这样的基础类型

## 映射为 List集合
``` java
	@Test
	public void test3_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		List<SysUser> list = sqlFly.getList(SysUser.class, sql, 10001);
		System.out.println("当前用户表共有" + list.size() + "条数据，分别为：");
		for (SysUser user : list) {
			System.out.println(user);
		}
	}
```
- 参数同 `getModel()`，只不过返回值为 List集合 



## 映射为 Map
映射为model还得我先写个实体类，太麻烦了，我想直接映射为Map，可不可以？可以
``` java
	@Test
	public void test3_4() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Map<String, Object> map = sqlFly.getMap(sql, 10001);
		System.out.println("id=10001的用户，其详细信息为：" + map);
	}
```

## 映射为 Map的集合
也支持映射为Map的集合 
``` java 
	@Test
	public void test3_5() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		List<Map<String, Object>> listMap = sqlFly.getMapList(sql);
		System.out.println("当前用户表共有" + listMap.size() + "条数据，分别为：");
		for (Map<String, Object> map : listMap) {
			System.out.println(map);
		}
	}
```

## 映射为Map并指定列做key 
普通的map映射只能以表头做key，试想，有如下查询结果集：

| id        | username   |
| --------   | -----  |
| 10001     | 省长 |
| 10002        |   小言   |
| 10003        |    闹心    |
| 10004        |    榴莲   |

如果我们想以id做key，映射为map的话，该怎么做？ `getListMapByCol` 来解决这个问题 
``` java 
	public void test3_6() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select id, username from sys_user";
		List<Map<String, Object>> listMap = sqlFly.getListMapByCol("id", sql);
		System.out.println("映射结果为：" + listMap.get(0));
	}
```

打印输出：
``` 
映射结果为：{10002=小言, 10004=榴莲, 10001=省长, 10003=闹心}
```


