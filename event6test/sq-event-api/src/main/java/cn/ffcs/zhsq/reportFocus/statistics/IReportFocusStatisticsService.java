package cn.ffcs.zhsq.reportFocus.statistics;

import cn.ffcs.system.publicUtil.EUDGPagination;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/3/30 16:46
 */
public interface IReportFocusStatisticsService {
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
    public List<Map<String,Object>> findListData(Map<String,Object> params) throws Exception;

    /**
     * 分页获取疫情防控超时环节信息数量
     * @param pageNo
     * @param pageSize
     * @param params		额外参数
     * 		isDictTransfer	是否进行字典转换，true为是；默认为true
     * @return
     * @throws Exception
     */
    public EUDGPagination findPagination4EpcOverdue(int pageNo, int pageSize, Map<String, Object> params) throws Exception;

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
    public List<Map<String,Object>> findListDataOfEpcOverdue(Map<String,Object> params) throws Exception;
}
