package com.fly.jdbc.mapping;

import java.lang.annotation.*;

/**
 * Fly的ORM注解，标注实体类与数据表的关系
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface AtFly {

	/* * * * * * * * * * * * * 映射相关 * * * * * * * * * * * * * * * * * * * * */
	/**
	 * Fly处理字段的总开关，标注此属性是否接受映射，false代表赋值时直接忽略此字段
	 * @return
	 */
	public boolean orm() default true;
	

	/**
	 * 
	 *  标注此字段在被set时的方式,默认直接赋值
	 * <br/>set=setId;代表调用this.setId(value)函数赋值
	 * @return
	 */
	public String set()default "";
	
	
	/* * * * * * * * * * * * * 与表的关系 * * * * * * * * * * * * * * * * * * * * */
//	/**
//	 * 标注此类哪个字段是主键，默认id
//	 */
//	public String pk()default "id";
//
//	/**
//	 * 标注此类所对应的表名，默认与类同名
//	 */
//	public String table()default "";
	
	/**
	 * 标注此属性在数据库中所对应的表列名，默认与属性同名
	 * @return
	 */
	public String column()default "";


}
