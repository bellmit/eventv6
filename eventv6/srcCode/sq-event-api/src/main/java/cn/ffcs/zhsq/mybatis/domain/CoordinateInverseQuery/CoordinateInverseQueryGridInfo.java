package cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;

import java.io.Serializable;

/**
 * 网格信息
 *
 * @Author sulch
 * @Date 2017-02-09 17:36
 */
public class CoordinateInverseQueryGridInfo implements Serializable{
    private Long gridId;// 标识
    private String infoOrgCode;// 信息域组织编码
    private String gridName;//网格名称

    public Long getGridId() {
        return gridId;
    }

    public void setGridId(Long gridId) {
        this.gridId = gridId;
    }

    public String getInfoOrgCode() {
        return infoOrgCode;
    }

    public void setInfoOrgCode(String infoOrgCode) {
        this.infoOrgCode = infoOrgCode;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }
}
