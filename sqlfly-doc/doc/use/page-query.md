# 分页查询 

- SqlFly内置三种数据库的分页查询，分别是：`MySql`、`Sql Server`、`Oracle`，默认分页方式为 `MySql`，当然你也可以 [切换分页方式](#切换分页方式)，以及 [自定义分页方式](#自定义分页方式)

---

## 获取Page实例
- 要想分页，首先要获得分页工具类 `Page` 的实例，此类有一系列方法辅助你进行分页查询 
- 以下示例三种方式获得 `Page` 的实例

``` java
	@Test
	public void test4_1() {
		// 方式 1，通过构造方法
		Page page = new Page(1, 10);	// 当前页、页大小
		System.out.println(page);
		
		// 方式2，根据其静态方法
		Page page2 = Page.getPage(1, 10);	// 当前页、页大小 
		System.out.println(page2);
		
		// 方式3，通过 start 来构建
		Page page3 = new Page();
		page3.setStart(4);	// 直接设置起始位置 
		page.setPageSize(10);
		System.out.println(page3);
	}
```

- 对于大多数数据库的分页都是，通过 `start` 和 `pageSize` 两个字段来控制分页的
- 方式1和方式2会通过你传入的当前页和页大小自动计算 `start` 
- 方式3会直接使用你设置的 `start` 值


## 分页查询 - 映射为 List< Model >
``` java
	@Test
	public void test4_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Page page = new Page(1, 2);
		List<SysUser> list = sqlFly.getListPage(page, SysUser.class, sql);
		System.out.println("通过分页查询到" + list.size() + "条数据，分别为：");
		for (SysUser user : list) {
			System.out.println(user);
		}
	}
```

## 分页查询 - 映射为 List< Map >
``` java 
	@Test
	public void test4_3() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		String sql = "select * from sys_user";
		Page page = new Page(1, 2);
		List<Map<String, Object>> listMap = sqlFly.getMapListPage(page, sql);
		System.out.println("当前用户表共有" + listMap.size() + "条数据，分别为：");
		for (Map<String, Object> map : listMap) {
			System.out.println(map);
		}
	}
```


## 切换分页方式
- 很简单：只需要更改 FlyObjects.paging 接口的实现类就可以了 
``` java
	// 测试4-4 切换分页方式 
	@Test
	public void test4_4() {
		// 切换为 SqlServer 分页方式 
		FlyPaging paging = new FlyPagingSqlServer();
		FlyObjects.setPaging(paging);
		
		// 切换为 Oracle 分页方式
		FlyPaging paging2 = new FlyPagingOracle();
		FlyObjects.setPaging(paging2);
	}
```


## 自定义分页方式
如果你明白了如何切换分页方式，就不难理解如何自定义分页了 

**1、继承FlyPaging接口，实现getPagingSql方法**
``` java 
	// 自定义SqlFly的分页实现类
	public class FlyPagingMycustom implements FlyPaging{
		// 实现此方法，返回分页形式的sql，以备SqlFly的调用 
		@Override
		public String getPagingSql(String sql, Page page) {
			String newsql = sql + " limit " + page.getStart() + "," + page.getPageSize();
			return newsql;
		}
	}
```
**2、更改 FlyObjects.paging 接口的实现类**
``` java 
	@Test
	public void test4_5() {
		FlyPaging paging = new FlyPagingMycustom(); 
		FlyObjects.setPaging(paging); 
	}
```


## springboot的方式
- springboot 自定义分页方式时，可以无需手动进行 `FlyObjects.setPaging(paging)` 这一步操作，
- 只需要重写 `FlyPaging` 接口，加上 `@Component` 注解，并且保证被 springboot 包扫描机制扫描到就可以了
- 不清楚SpringBoot包扫描机制的同学请自行百度



