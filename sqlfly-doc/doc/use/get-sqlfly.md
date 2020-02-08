# 获取SqlFly实例

---

## 为什么要获取 SqlFly 类的实例？
- SqlFly类是与数据库交互的主要对象，你需要获取它来与数据库进行“沟通”

## 如何获取？
### 方式1、直接new

``` java
	@Test
	public void test1_1() {
		SqlFly sqlFly = new SqlFly();
		System.out.println(sqlFly.getId());
	}
```

### 方式2、通过 `SqlFlyFactory` 工厂类创建 （推荐）

``` java 
	@Test
	public void test1_2() {
		SqlFly sqlFly = SqlFlyFactory.getSqlFly();
		System.out.println(sqlFly.getId());
	}
```
> 通过 `SqlFlyFactory` 工厂类创建的 SqlFly实例 都是绑定在当前线程上的，也就是说在同一线程内获取到的都是同一实例对象，
> 这一特性将在web应用中有着极其重要的作用，而通过 `new SqlFly()` 方式则没有这个效果，
> 因此，更加推荐你通过 `SqlFlyFactory` 工厂类来创建 SqlFly实例 





