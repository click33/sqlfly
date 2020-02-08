package com.fly.jdbc.paging;

import java.io.Serializable;

/****
 * 分页工具类
 */
public class Page implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	int pageNo = 1;     // 当前页 
    int pageSize = 10;   // 页大小 
    int start;      // 起始位置 
    int count = -1;      // 总数据数 
    int pageCount;  // 可以分的总页数 
    Boolean is_count = true;  // 是否加载总数, 不加载总数的情况下可以减少一次数据库请求 



	public Page(){}
    
    /**
     * 当前页、页大小
     * @param pageNo
     * @param pageSize
     */
    public Page(int pageNo, int pageSize){
        // 防止恶意参数
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1 || pageSize > 10000) {
            pageSize = 10;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        initStart();
    }

    /**
     * 静态方法构建
     * @param pageNo 当前页
     * @param pageSize 页大小 
     * @return
     */
    public static Page getPage(int pageNo, int pageSize){
        return new Page(pageNo, pageSize);
    }


    // 打印此对象的信息
    @Override
    public String toString() {
        return "Page{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", start=" + start +
                ", count=" + count +
                ", pageCount=" + getPageCount() +
                ", is_count=" + is_count +
                '}';
    }

    
    
    
    public void initStart(){
    	this.start = (pageNo - 1) * pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }
    public Page setPageNo(int pageNo) {
    	this.pageNo = pageNo;
    	initStart();
    	return this;
    }

    public int getPageSize() {
        return pageSize;
    }
    public Page setPageSize(int pageSize) {
    	this.pageSize = pageSize;
    	initStart();
    	return this;
    }

    public int getStart() {
        return start;
    }
    public Page setStart(int start) {
        this.start = start;
        return this;
    }

    // 数据总数 
    /**
     * 根据总条数，计算总页数
     * @return
     */
    public int getPageCount() {
        int pc = count / pageSize;
        return (pageCount = count % pageSize == 0 ?  pc : pc + 1);
    }

    // 数据总数 
    public int getCount() {
        return count;
    }
    public Page setCount(int count) {
        this.count = count;
        return this;
    }

    // 是否加载数据总数 
    public Boolean getIs_count() {
		return is_count;
	}
	public Page setIs_count(Boolean is_count) {
		this.is_count = is_count;
		return this;
	}
	
	
	
}
