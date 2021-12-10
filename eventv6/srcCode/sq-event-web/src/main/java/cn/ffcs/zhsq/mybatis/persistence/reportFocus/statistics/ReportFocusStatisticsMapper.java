package cn.ffcs.zhsq.mybatis.persistence.reportFocus.statistics;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/3/30 16:30
 */
public interface ReportFocusStatisticsMapper extends MyBatisBaseMapper<Map<String, Object>> {

    /**
     * 获取数据列表
     * @param params
     *        orgCode   地域域编码
     *        报告开始、结束时间
     *        beginTime  开始时间
     *        endTime    结束时间
     * @return 返回数据列表
     * @throws Exception
     * */
    public List<Map<String,Object>> findListData(Map<String,Object> params);

    /**
     * 获取疫情防控超时环节列表数量
     * @param params
     * @return
     */
    public int findCount4EpcOverdue(Map<String, Object> params);

    /**
     * 分页获取疫情防控超时环节列表记录
     * @param params
     * @param bounds
     * @return
     */
    public List<Map<String, Object>> findList4EpcOverdue(Map<String, Object> params, RowBounds bounds);
    public List<Map<String, Object>> findList4EpcOverdue(Map<String, Object> params);
}
