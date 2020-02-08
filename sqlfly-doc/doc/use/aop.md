# 切面操作
SqlFly允许你在sql执行前、执行后、执行异常等时机进行切面操作，方便你进行sql分析、日志打印、功能扩展等操作

--- 

## 1、实现接口
创建 `FlyAopMyCustom.java` 继承 `FlyAop` 并实现所有函数 
``` java
//自定义SqlFly的切面实现类
public class FlyAopMyCustom implements FlyAop{
	// 执行前 
	@Override
	public void exeBefore(String sql, Object[] args) {
		System.out.println("====== sql执行前 ======");
		System.out.println("执行sql为：" + sql);
	}
	// 执行后
	@Override
	public void exeAfter(String sql, Object[] args, PreparedStatement preparedStatement) {
		System.out.println("====== sql执行后 ======");
	}
	// 执行异常时
	@Override
	public void exeException(String sql, Object[] args, SQLException e) throws FlySQLException {
		System.out.println("====== sql执行异常了 ======");
		throw new FlySQLException(e.getMessage(), e);
	}
	// 最终执行 
	@Override
	public void exeFinally(String sql, Object[] args, PreparedStatement preparedStatement) {
		System.out.println("====== 无论成功或异常都会执行 ======");
	}
}
```

## 2、注入到框架中
``` java
	@Test
	public void test7_1() {
		FlyAop aop = new FlyAopMyCustom();
		FlyObjects.setAop(aop);
		SqlFlyFactory.getSqlFly().getScalar("select count(*) from sys_user");	// 执行sql测试一下
	}
```


## 3、springboot环境中怎么做？
- springboot 自定义切面时，可以无需手动进行 `FlyObjects.setAop(aop)` 这一步操作，
- 只需要重写 `FlyAop` 接口，加上 `@Component` 注解，并且保证被 springboot 包扫描机制扫描到就可以了
- 不清楚SpringBoot包扫描机制的同学请自行百度


