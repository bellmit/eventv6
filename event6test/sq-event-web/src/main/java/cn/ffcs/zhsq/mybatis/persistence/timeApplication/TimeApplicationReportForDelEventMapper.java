package cn.ffcs.zhsq.mybatis.persistence.timeApplication;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @Description:删除事件数据报表
 * @Author: zhangtc
 * @Date: 2018/12/11 20:20
 */
public interface TimeApplicationReportForDelEventMapper extends MyBatisBaseMapper<Map<String, Object>>{

    /**
     * 获取数据列表
     * @param params
     *        orgCode   信息域编码
     *        gridId    网格id
     *        查询时间
     *        beginTime  开始时间
     *        endTime    结束时间
     * @return 返回数据列表
     * @throws Exception
     * */
    public List<Map<String,Object>> findListDataOfDelEvent(Map<String,Object> params);

}
