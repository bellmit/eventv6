package cn.ffcs.zhsq.dispute.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeFlowInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationCase;

import java.util.List;
import java.util.Map;

/**
 * @author ：wangh
 */
public interface IDisputeMediationJXService {
    /**
     * 分页列表
     * Sep 11, 2014
     * 9:20:45 AM
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findDisputePagination(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 矛盾纠纷对接新增
     * @param bo
     * @return
     */
    public int insertOrUpdate(DisputeMediation bo) throws Exception;

    /**
     * 流程信息新增
     * @param list
     * @return
     */
    public int insertFlowInfo(List<DisputeFlowInfo> list) throws Exception;

    /**
     * 根据第三方ID查询第三方入库的数据
     * @param disputeId
     * @return
     */
    public DisputeMediation searchByDisputeId(Long disputeId);

    /**
     * 第三方删除矛盾纠纷数据
     * @param disputeId
     * @return
     * @throws Exception
     */
    public int deleteByDisputeId(Long disputeId) throws Exception;

    /**
     * 流程信息查询
     * @param mediationId
     * @return
     */
    public List<DisputeFlowInfo> searchFlowList(Long mediationId);

    /**
     * 包案领导人信息 化解责任人信息
     * @param mediationId
     * @param userType
     * @return
     */
    public List<MediationCase> searchCaseList(Long mediationId, String userType);

    /**
     * 矛盾纠纷对接新增
     * @param bo
     * @return
     */
    public int insertOrUpdateNoFinish(DisputeMediation bo) throws Exception;

    /**
     * 分页列表
     * Sep 11, 2014
     * 9:20:45 AM
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findDisputeJX(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 查询疑似上报用户
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findDisputeUserJX(int pageNo, int pageSize, Map<String, Object> params);
}
