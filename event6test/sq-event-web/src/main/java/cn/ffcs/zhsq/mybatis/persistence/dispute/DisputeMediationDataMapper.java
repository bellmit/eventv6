package cn.ffcs.zhsq.mybatis.persistence.dispute;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface DisputeMediationDataMapper {
    /**
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> getConfictTopListByOrgCode(Map<String, Object> params);

    public Map<String, Object> getOutStandingNum(Map<String, Object> params);

    public List<Map<String, Object>> getOutStandingDistribution(Map<String, Object> params);

    public List<Map<String,Object>> getDataForResolutionRateNext(Map<String,Object> params);

    public Map<String, Object> getConfictMediationNum(Map<String, Object> params);

    public List<Map<String, Object>> findTwoYearByMap(Map<String, Object> params);

    /**
     * 获取矛盾纠纷数量
     * @param params
     * @return
     */
    public Map<String, Object> getDisputeNum(Map<String, Object> params);

    /**
     * 获取同比情况
     * @param params
     * @return
     */
    public List<Map<String, Object>> getTwoYearMoM(Map<String, Object> params);

    /**
     * 获取矛盾纠纷类型
     * @param params
     * @return
     */
    public List<Map<String, Object>> getDisputeTypeNum(Map<String, Object> params);

    /**
     * 根据infoOrgCode 查询 网格信息
     * @param params
     * @return
     */
    public List<Map<String, Object>> getGridInfoByInfoOrgCode(Map<String, Object> params);

    /**
     * 获取纠纷类型top1
     * @param params
     * @return
     */
    public Map<String, Object> getRegionsTypeTop1(Map<String, Object> params);

    public List<Map<String, Object>> searchList(Map<String, Object> params, RowBounds rowBounds);

    public long countList(Map<String, Object> params);
}
