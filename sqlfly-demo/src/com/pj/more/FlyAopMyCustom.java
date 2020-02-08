package com.pj.more;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fly.jdbc.aop.FlyAop;
import com.fly.jdbc.exception.FlySQLException;

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
