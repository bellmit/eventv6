package cn.ffcs.zhsq.dispute.service;

import cn.ffcs.system.publicUtil.EUDGPagination;

import java.util.List;
import java.util.Map;

public interface IDisputeMediationDataService {

    /**
     * 获取突出矛盾纠纷数据
     * @param params
     * @return
     */
    public Map<String, Object> getOutStandingNum(Map<String,Object> params);

    /**
     * 获取突出矛盾纠纷 案件主要分布
     * @param params
     * @return
     */
    public List<Map<String, Object>> getOutStandingDistribution(Map<String,Object> params);

    /**
     * 获取本级的下一级的化解率TOP5
     * @param params
     * @return
     */
    public List<Map<String,Object>> getDataForResolutionRateNext(Map<String,Object> params);

    /**
     * 获取矛盾纠纷数量 矛盾纠纷总数 调解成功数 调解中数 调解率
     * @param params
     * @return
     */
    public Map<String, Object> getNowMonthNumAndNowYearNum(Map<String, Object> params);

    /**
     * 	矛盾纠纷 最近2年数量
     * @param
     * @return
     */
    public Map<String, Object> findTwoYearByMap(Map<String, Object> params);

    /**
     * 获取矛盾纠纷类型
     * @param params
     * @return
     */
    public List<Map<String, Object>> getConfictTopListByOrgCode(Map<String, Object> params);

    /**
     * 获取矛盾纠纷数量
     * @param params orgCode 必传 不传直接报错
     * @return TOTAL 总数， END_TOTAL 处置数，DEAL_TOTAL 处置中，处置率 DEAL_RATE
     */
    public Map<String, Object> getDisputeNum(Map<String, Object> params);

    /**
     * 获取同比环比
     * @param params orgCode 必传 不传直接报错
     * @return twoYear 同比环比（YEAR_ 年份，MONTH_ 月份，TOTAL_ 总数，END_TOTAL 处置数） ； nowYear 办理趋势（YEAR_ 年份，MONTH_ 月份，TOTAL_ 总数，END_TOTAL 处置数）
     */
    public Map<String, Object> getTwoYearMoM(Map<String, Object> params);

    /**
     * 获取矛盾纠纷类型
     * @param params
     * @return DICT_NAME 字典名称， TOTAL_ 对应字典数量
     */
    public List<Map<String, Object>> getDisputeTypeNum(Map<String, Object> params);

    /**
     * 获取事件分布排行
     * @param params
     * @return
     */
    public List<Map<String, Object>> getDisputeDistribution(Map<String, Object> params);

    /**
     * 获取各地区纠纷类型TOP1
     * @param params
     * @return
     */
    public List<Map<String, Object>> getRegionsTypeTop1(Map<String, Object> params);

    /**
     * 列表查询
     * @param params
     * @return
     */
    public EUDGPagination searchList(Map<String, Object> params, int page, int rows);
}
