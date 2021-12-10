package cn.ffcs.zhsq.mybatis.persistence.CoordinateInverseQuery;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;

import java.util.List;
import java.util.Map;

/**
 * 通过坐标反向查询
 *
 * @Author sulch
 * @Date 2017-02-09 17:26
 */
public interface CoordinateInverseQueryMapper extends MyBatisBaseMapper<MixedGridInfo> {
    /**
     * 通过坐标反向查询所属网格
     * @param params
     * @return
     */
    public List<CoordinateInverseQueryGridInfo> findGridInfo(Map<String, Object> params);
}
