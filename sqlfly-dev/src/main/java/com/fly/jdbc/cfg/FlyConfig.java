package com.fly.jdbc.cfg;

/**
 * 定义所有配置
 * 
 * @author kong
 *
 */
public class FlyConfig {

	// private String flyLoad = "com.fly.jdbc.cfg.FlyLoadDefault"; // FlyLoad默认的实现类

	// 连接池配置
	private String driverClassName;
	private String url;
	private String username;
	private String password;

	private Boolean ispool = false; // 是否使用连接池，其值若为false，则代表不再使用连接池
	private int init = 10; // 初始化连接数
	private int min = 5; // 最小链接数
	private int max = 20; // 最大连接数

	
	// 运行配置
	private Boolean printSql = false; // 是否在控制台输出每次执行的SQL与参数
	private String sqlhh = "[SQL] "; // 输出SQL的前缀
	private String argshh = "[ARGS]"; // 输出参数的前缀

	private int defaultLimit = 1000; // Page == null时默认取出的数据量
	private Boolean isV = true; // 是否在初始化配置时打印版本字符画
	

	
	// 初始化框架
//	static {
//		FlyConfigFactory.load();
//	}
	
	// ============ 一坨坨 getset ===================== 
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public Boolean getIspool() {
		return ispool;
	}
	public void setIspool(Boolean ispool) {
		this.ispool = ispool;
	}
	public int getInit() {
		return init;
	}
	public void setInit(int init) {
		this.init = init;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public Boolean getPrintSql() {
		return printSql;
	}
	public void setPrintSql(Boolean printSql) {
		this.printSql = printSql;
	}
	public String getSqlhh() {
		return sqlhh;
	}
	public void setSqlhh(String sqlhh) {
		this.sqlhh = sqlhh;
	}
	public String getArgshh() {
		return argshh;
	}
	public void setArgshh(String argshh) {
		this.argshh = argshh;
	}
	public int getDefaultLimit() {
		return defaultLimit;
	}
	public void setDefaultLimit(int defaultLimit) {
		this.defaultLimit = defaultLimit;
	}
	public Boolean getIsV() {
		return isV;
	}
	public void setIsV(Boolean isV) {
		this.isV = isV;
	}
	
	
	@Override
	public String toString() {
		return "FlyConfig [driverClassName=" + driverClassName + ", url=" + url + ", username=" + username
				+ ", password=" + password + ", ispool=" + ispool + ", init=" + init + ", min=" + min + ", max=" + max
				+ ", printSql=" + printSql + ", sqlhh=" + sqlhh + ", argshh=" + argshh + ", defaultLimit="
				+ defaultLimit + ", isV=" + isV + "]";
	}
	
	
	
	
}
