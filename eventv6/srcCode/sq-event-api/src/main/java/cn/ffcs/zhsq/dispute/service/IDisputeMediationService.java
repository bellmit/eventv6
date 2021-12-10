package cn.ffcs.zhsq.dispute.service;
 
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo; 
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;

/**
 * 矛盾纠纷
 * @author zhongshm
 *
 */
public interface IDisputeMediationService  {

	/**
	 * 分页列表
	 * Sep 11, 2014
	 * 9:20:45 AM
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findDisputePagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	public int countDisputes(Map<String, Object> params);

	/**
	 * 插入单条记录
	 * Sep 16, 2014
	 * 10:08:45 AM
	 * @param disputeMediation
	 * @return 主键ID
	 */
	public Long insertDisputeMediation(DisputeMediation disputeMediation);

	public Long saveDisputeMediation(DisputeMediation disputeMediation, Map<String, Object> otherParams);

	/**
	 * 更新微信矛盾纠纷状态
	 * @param disputeId
	 * @param disputeStatus
     * @return
     */
	public boolean updateWeiXinDispute(Long disputeId, String disputeStatus);

	/**
	 * 获取编号
	 * Sep 16, 2014
	 * 10:40:35 AM
	 * @return
	 */
	public String getMediationCode();

	/**
	 * 矛盾纠纷微信驳回
	 * @param mediationId 矛盾纠纷id
	 * @param disputeId 微信id
     * @return
     */
	@Transactional
	Boolean eventReject(Long mediationId, Long disputeId);

	/**
	 * 矛盾纠纷微信事件受理
	 * @param mediationId 矛盾 纠纷ID
	 * @param disputeId 微信ID
	 * @param mediationDeadlineStr    受理时间
	 * @param userInfo 用户
     * @return
     */
	@Transactional
	Boolean eventReport(Long mediationId, Long disputeId, String mediationDeadlineStr, UserInfo userInfo);

	/**
	 * 根据主键获取对象
	 * Sep 18, 2014
	 * 2:52:52 PM
	 * @param mediationId
	 * @return
	 */
	public DisputeMediation selectByPrimaryKey(Long mediationId);

	/**
	 * 更新（为空不更新）
	 * Sep 18, 2014
	 * 4:53:41 PM
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(DisputeMediation record);

	/**
	 * 根据删除多条
	 * Sep 19, 2014
	 * 10:25:24 AM
	 * @param ids
	 * @param userId 操作人id
	 * @return
	 */
	public int deleteByIds(List<Long> ids, Long userId);

	public int deleteAllByGridId(Map<String, Object> params);

	public int deleteById(Long id, Long userId);

	public List<DisputeMediation> findDisputeList(Map<String, Object> params);

	public Map<String, Object> findMaxDisputeType(Map<String, Object> params);

	boolean saveEventAndReport(DisputeMediation disputeMediation, UserInfo userInfo);

	/**
	 * 评价矛盾纠纷同步事件
	 * @param mediationId
	 * @param params
     * @return
     */
	public boolean evaluateEvent(Long mediationId, Map<String, Object> params);

	/**
	 * 保存事件并结案
	 * @param disputeMediation
	 * @param userInfo
     * @return
     */
	boolean updateEventAndClose(DisputeMediation disputeMediation, UserInfo userInfo);

	/**
	 * 保存矛盾纠纷上报并结案（提供手机端接口）
	 * @param disputeMediation
	 * @param params
     * @return
     */
	public Long saveEventAndClose(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> params);

	boolean updateAndReport(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> otherParams);

	/**
	 * 保存矛盾纠纷并上报（提供手机端接口）
	 * @param disputeMediation
	 * @param params
     * @return
     */
	public Long saveEventAndReport(DisputeMediation disputeMediation, UserInfo userInfo, Map<String, Object> params);

	/**
	 * 保存关联关系
	 * @param disputeMediation
	 * @param userInfo
     * @return
     */
	public Long saveEventReport(DisputeMediation disputeMediation, UserInfo userInfo);

	/**
	 *
	 * @param userInfo
	 * @return
     */
	public String getDisputeEventType(UserInfo userInfo, String type);
	
	/**
	 * 包含零报告分页列表 
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findDisputeAndZeroPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	/**
	 * 插入单条零报告记录 
	 * @param disputeMediation
	 * @return 主键ID
	 */
	public Long insertDisputeMediationZero(DisputeMediation disputeMediation);
	/**
	 * 根据 gridId 查询是否存在零报告
	 * @return
	 */
	public Integer findZeroIdByGridId(String gridId);
	
	/**
	 * 根据网格id删除零报告
	 * @param zeroId 
	 */
	public int deleteZero(String gridId,Long userId);
	/**
	 * 统计是否存在矛盾纠纷记录
	 * @param gridId
	 * @return
	 */
	int findCountByGridId(String gridId);


	/**
	 * 根据矛盾纠纷ID 查询对应的化解信息
	 * @param id
	 * @return
	 */
	public DisputeMediation searchResInfoById(Long id);

	/**
	 * 根据矛盾纠纷主表ID判断 化解信息是新增还是编辑
	 * @param disputeMediation
	 * @return
	 */
	public boolean insertOrUpdateResovle(DisputeMediation disputeMediation);

	/**
	 * 获取今日各地区矛盾纠纷新增数量
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGridDisputeNum(Map<String, Object> params);

	/**
	 * 获取今日网格员上报TOP10
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGridDispiteTop(Map<String, Object> params);
}
