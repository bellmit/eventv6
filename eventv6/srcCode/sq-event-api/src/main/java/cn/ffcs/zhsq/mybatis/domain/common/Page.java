package cn.ffcs.zhsq.mybatis.domain.common;

/**
 * @author zhanzhenhai
 * @create 2019-01-29 10:11
 * 描述：分页类
 */
public class Page {

    /**
     * 当前页
     */
    private Integer page = 1;
    /**
     * 数据行数
     */
    private Integer rows = 20;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

}
