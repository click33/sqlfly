# AtFly
- `AtFly` 是一个注解，它可以在 `SqlFly` 在执行查询时进行一定的辅助 
- 将 @AtFly 加在实体类的字段上，将会使字段拥有一些特殊的能力 

--- 

## 所有字段

### orm
- 框架处理字段的总开关，标注此属性是否接受映射，false代表赋值时直接忽略此字段，默认true 
- 例如，以下示例中字段将不会在查询映射时被赋值 
``` java 
	@AtFly(orm = false)
	private String username;	// 昵称
```

### set
- 标注此字段在被set时的方式，默认直接通过反射针对属性进行强制赋值
- 例如，以下示例中字段将会在赋值时，寻找名为 setUsername()的方法进行调用
``` java
	@AtFly(set="setUsername")
	private String username;	// 昵称
```
	

### column
- 标注此属性在结果集中所对应的列名，默认与属性同名
- 例如，以下示例中属性username将会在赋值时寻找名为name的字段进行取值 
``` java
	@AtFly(column = "name")
	private String username;	// 昵称
```
	
	
	
	
	