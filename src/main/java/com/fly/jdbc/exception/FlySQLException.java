package com.fly.jdbc.exception;

/**
 * Fly 执行Sql发送异常
 */
public class FlySQLException extends RuntimeException {

	
	private static final long serialVersionUID = 6806129545290130142L;
	

	/**
	 * 获得一个异常FlySQLException
	 * @param message 异常描述
	 * @param cause 异常原因
	 * @return
	 */
	public FlySQLException(String message, Throwable cause) {
        super(message,cause);
    }
	

}
