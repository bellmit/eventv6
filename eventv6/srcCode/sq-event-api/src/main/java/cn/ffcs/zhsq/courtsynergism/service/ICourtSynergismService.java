package cn.ffcs.zhsq.courtsynergism.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.mybatis.domain.courtsynergism.CourtSynergism;

import java.util.List;
import java.util.Map;

/**
 * @Description: 法院协同办公模块服务
 * @Author: zhangch
 * @Date: 05-20 11:01:56
 * @Copyright: 2020 福富软件
 */
public interface ICourtSynergismService {

	/**
	 * 新增数据
	 * @param bo 法院协同办公业务对象
	 * @return 法院协同办公id
	 */
	public Long insert(CourtSynergism bo);

	/**
	 * 修改数据
	 * @param bo 法院协同办公业务对象
	 * @return 是否修改成功
	 */
	public boolean update(CourtSynergism bo);

	/**
	 * 删除数据
	 * @param bo 法院协同办公业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(CourtSynergism bo);

	/**
	 * 查询数据（分页）
	 * @param bo 查询参数
	 * @return 法院协同办公分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, CourtSynergism bo);
	
	/**
	 * 根据业务id查询数据
	 * @param id 法院协同办公id
	 * @return 法院协同办公业务对象
	 */
	public CourtSynergism searchById(Long id,UserInfo userInfo );

	/**
	 * 启动工作流
	 * @param bo
	 * @return
	 */
	int startWorkFlow(CourtSynergism bo, UserInfo userInfo);

	/**
	 * 获取当前节点
	 * @param synergismId	业务表ID
	 * @param userInfo
	 * @return 查找成功返回instanceId，否则返回-1
	 * @throws Exception
	 */
	Map<String, Object> getCurNode(Long synergismId, UserInfo userInfo) throws Exception;

	/**
	 * 获取下一节点
	 * @param synergismId	业务表ID
	 * @param userInfo
	 * @return 查找成功返回instanceId，否则返回-1
	 * @throws Exception
	 */
	List<Node> findNextTaskNodes(Long synergismId, UserInfo userInfo) throws Exception;

	/**
	 * 提交扭转
	 * @param bo
	 * @param userInfo
	 * @return
	 */
	int submit(CourtSynergism bo, UserInfo userInfo);

	/**
	 * 驳回扭转
	 * @param bo
	 * @param userInfo
	 * @return
	 */
	int reject(CourtSynergism bo, UserInfo userInfo);

	/**
	 * 流程驳回
	 * @param sendId			业务表ID
	 * @param rejectToNodeName	驳回指定的节点名称
	 * @param userInfo			用户信息
	 * @param extraParam
	 * 			advice			驳回意见
	 * @return
	 * @throws Exception
	 */
	boolean rejectWorkflow4Case(Long sendId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

	Long capInstanceId(CourtSynergism bo,UserInfo userInfo);

	/**
	 * 判断用户是否为当前任务的办理人员
	 * @param sendId		业务表ID
	 * @param userInfo		用户信息
	 * @param extraParam
	 * @return
	 * @throws Exception
	 */
	public boolean isCurTaskPaticipant(Long sendId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

	/**
	 * 判断用户是否为当前任务的办理人员
	 * @param participantMapList	当前任务的办理人员
	 * 			USER_TYPE			用户类型，3 办理人员为人员；1 办理人员为组织
	 * 			USER_ID				用户id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 			ORG_ID				组织id
	 * @param userInfo				用户信息
	 * @param extraParam
	 * @return
	 * @throws Exception
	 */
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

	/**
	 * 依据任务ID获取当前办理任务信息
	 * @param sendId	业务表ID
	 * @param userInfo	用户信息
	 * @return
	 * 		WF_ORGID	当前办理人员组织Id
	 * 		WF_ORGNAME	当前办理人员组织名称
	 * 		WF_USERID	当前办理人员userId，以英文逗号连接
	 * 		WF_USERNAME	当前办理人员姓名
	 * 		WF_DBID_	当前节点任务
	 * 		WF_ACTIVITY_NAME_	当前节点任务名称
	 * 		NODE_NAME	当前节点编码
	 * 		NODE_ID		当前节点id
	 * @throws Exception
	 */
	public Map<String, Object> findCurTaskData(Long sendId, UserInfo userInfo) throws Exception;

	/**
	 * 依据工作流任务id获取任务办理人员信息
	 * @param wfTaskId	工作流任务id
	 * @return
	 * 		USER_TYPE	用户类型，3 办理人员为人员；1 办理人员为组织
	 * 		USER_ID		办理人员id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 		USER_NAME	办理人员姓名
	 * 		ORG_ID		办理人员组织id
	 * 		ORG_NAME	办理人员组织名称
	 */
	public List<Map<String, Object>> findParticipantsByTaskId(Long wfTaskId);
}