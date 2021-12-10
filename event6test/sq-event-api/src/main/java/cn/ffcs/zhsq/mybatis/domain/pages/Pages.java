package cn.ffcs.zhsq.mybatis.domain.pages;

import java.io.Serializable;

public class Pages implements Serializable{
	private int page;
	private int rows;
	final static private int pageNum = 1;
	final static private int rowsNum = 20;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page < pageNum ? pageNum : page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows < pageNum ? rowsNum : rows ;
	}
	
}
