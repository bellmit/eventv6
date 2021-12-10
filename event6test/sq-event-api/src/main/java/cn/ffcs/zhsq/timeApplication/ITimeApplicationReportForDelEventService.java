package cn.ffcs.zhsq.timeApplication;

import java.util.List;
import java.util.Map;

/**
 * @Description:事件删除列表数据详情
 * @Author: zhangtc
 * @Date: 2018/12/4 17:37
 */
public interface ITimeApplicationReportForDelEventService {

    /**
     * 获取数据列表
     * @param params
     *        infoOrgCode   信息域编码
     *        gridId    网格id
     *        查询时间
     *        beginTime  开始时间，格式    'YYYY-MM-DD'
     *        endTime    结束时间，格式    'YYYY-MM-DD'
     * @return 返回数据列表
     * @throws Exception
     * */
    public List<Map<String,Object>> findListDataOfDelEvent(Map<String,Object> params) throws Exception;
}
