package com.pj.model;

import java.util.Date;

/**
 * sys_user 用户表实体类 
 */
public class SysUser {
	
	private long id;			// id号
	private String username;	// 昵称
	private String password;	// 密码
	private int sex;			// 性别（1=男，2=女）
	private int age;			// 年龄 
	private Date create_time;	// 创建日期
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SysUser [id=" + id + ", username=" + username + ", password=" + password + ", sex=" + sex + ", age="
				+ age + ", create_time=" + create_time + "]";
	}
	
	
	
}
