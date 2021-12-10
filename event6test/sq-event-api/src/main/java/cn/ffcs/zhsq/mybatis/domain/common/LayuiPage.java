package cn.ffcs.zhsq.mybatis.domain.common;

/**
 * layui使用分页
 */
public class LayuiPage {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int page;
    private int limit;
    final static private int pageNum = 1;
    final static private int limitNum = 20;

    public int getPage() {
        return this.page = page < pageNum ? pageNum : page;
    }
    public void setPage(int page) {
        this.page = page < pageNum ? pageNum : page;
    }
    public int getLimit() {
        return this.limit = limit < pageNum ? limitNum : limit;
    }
    public void setLimit(int limit) {
        this.limit = limit < pageNum ? limitNum : limit;
    }

}
