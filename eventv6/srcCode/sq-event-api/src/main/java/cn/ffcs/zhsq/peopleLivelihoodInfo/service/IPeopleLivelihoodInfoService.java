package cn.ffcs.zhsq.peopleLivelihoodInfo.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeKeyword;
import cn.ffcs.zhsq.mybatis.domain.warningScheme.SchemeMatch;

/**
 * @Description: 民生信息模块服务
 * @Author: youwj
 * @Date: 05-28 11:20:04
 * @Copyright: 2019 福富软件
 */
public interface IPeopleLivelihoodInfoService {
	
	
	/**
	 * 新增或编辑数据
	 * @param livelihoodInfo 民生信息表业务对象
	 * @return 民生信息表uuid
	 */
	public Map<String,Object> saveOrUpdate(Map<String,Object> livelihoodInfo,UserInfo userInfo);

	/**
	 * 新增数据
	 * @param livelihoodInfo 民生信息表业务对象
	 * @return 民生信息表uuid
	 */
	public String insert(Map<String,Object> livelihoodInfo);

	/**
	 * 修改数据
	 * @param livelihoodInfo 民生信息表业务对象
	 * 		必填参数
	 * 			infoId	民生信息表业务ID
	 * @return 是否修改成功
	 */
	public Boolean update(Map<String,Object> livelihoodInfo);
	
	/**
	 * 修改数据
	 * @param livelihoodInfo 民生信息表业务对象
	 * 		必填参数
	 * 			infoSeqId	民生信息表序列ID
	 * @return 是否修改成功
	 */
	public Boolean updateBySeqId(Map<String,Object> livelihoodInfo);

	/**
	 * 删除数据
	 * @param livelihoodInfo 民生信息表业务对象
	 * 		必填参数
	 * 			infoId	民生信息表业务ID
	 * @return 是否删除成功
	 */
	public Boolean delete(Map<String,Object> livelihoodInfo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 民生信息表分页数据对象（List<Map<String,Object>>）
	 * @throws Exception 
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) throws Exception;
	
	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @return 民生信息表分页数据对象
	 */
	public List<Map<String,Object>> searchListByParams(Map<String, Object> params);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countList(Map<String, Object> params)throws Exception;
	
	/**
	 * 根据业务id查询数据
	 * @param id 民生信息表id（uuid）
	 * 		  orgCode 组织id（用于字典翻译，可不传）
	 * @return 民生信息表业务对象
	 */
	public Map<String, Object> searchById(String id,String orgCode);
	
	

	/**
	 * 根据业务id查询数据
	 * @param id 民生信息表id（序列id）
	 * 		  orgCode 组织id（用于字典翻译，可不传）
	 * @return 民生信息表业务对象
	 */
	public Map<String, Object> searchBySeqId(Long id,String orgCode);
	
	
	/** 工作流部分接口start（使用到的id均为表中的序列id字段）	 */
	
	/**
	 * 启动流程
	 * @param infoId		信息id
	 * @param userInfo		用户信息
	 * @param extraParam
	 * 			advice		办理意见
	 * 			isClose		true，直接结案
	 * @return
	 * @throws Exception
	 */
	public Long startWorkflow(Long infoId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	
	
	/**
	 * 提交流程
	 * @param infoId			信息id
	 * @param nextNodeName		下一环节名称
	 * @param nextUserInfoList	下一环节办理人员
	 * @param userInfo			用户信息
	 * @param extraParam
	 * 			advice			办理意见
	 * 			evaContent		评价意见
	 * 			evaLevel		评价等级
	 * 			nextUserIds		下一环节办理人员id，以英文逗号分隔
	 * 			nextOrgIds		下一环节办理人员组织id，以英文逗号分隔
	 * 			curNodeName		当前环节名称
	 * @return
	 * @throws Exception
	 */
	public Boolean subWorkflow(Long infoId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	
	/**
	 * 流程驳回
	 * @param infoId			信息id
	 * @param rejectToNodeName	驳回指定的节点名称
	 * @param userInfo			用户信息
	 * @param extraParam
	 * 			advice			驳回意见
	 * @return
	 * @throws Exception
	 */
	public Boolean rejectWorkflow4Clue(Long infoId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	
	/**
	 * 依据信息id获取流程实例
	 * @param infoId	信息id
	 * @param userInfo
	 * @return 查找成功返回instanceId，否则返回-1
	 * @throws Exception
	 */
	public Long capInstanceId(Long infoId, UserInfo userInfo) throws Exception;
	
	/**
	 * 判断用户是否可以启动流程
	 * @param userInfo
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean isUserAbleToStart(UserInfo userInfo) throws Exception;
	
	
	/**
	 * 依据环节信息进行主表状态变更
	 * @param infoId	信息id
	 * @param userInfo
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean alterInfoStatus(Long infoId, String curNodeName, String nextNodeName, UserInfo userInfo) throws Exception;
	
	
	/**
	 * 依据线索id获取当前办理任务信息
	 * @param infoId	信息id
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
	public Map<String, Object> findCurTaskData(Long infoId, UserInfo userInfo) throws Exception;
	
	
	/**
	 * 依据任务id获取任务办理人员信息
	 * @param taskId	任务id
	 * @return
	 * 		USER_TYPE	用户类型，3 办理人员为人员；1 办理人员为组织
	 * 		USER_ID		办理人员id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 		USER_NAME	办理人员姓名
	 * 		ORG_ID		办理人员组织id
	 * 		ORG_NAME	办理人员组织名称
	 */
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId);
	
	
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
	public Boolean isCurTaskPaticipant(Long infoId,List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 依据节点id获取该节点的操作按钮
	 * @param nodeId	节点id
	 * @return
	 * 		operateEvent	按钮点击事件名称
	 * 		anotherName		按钮名称
	 */
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId);
	
	/**
	 * 获取下一步可办理环节
	 * @param infoId	信息id
	 * @param userInfo	用户信息
	 * @param params
	 * @return
	 * 		nodeId			节点id
	 * 		nodeName		节点名称
	 * 		nodeNameZH		节点中文名称
	 * 		nodeCode		节点编码
	 * 		nodeType		节点类型
	 * 		transitionCode	节点线属性
	 * 		dynamicSelect	节点是否动态分配
	 * @throws Exception
	 */
	public List<Map<String, Object>> findNextTaskNodes(Long clueId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	
	public List<Map<String, Object>> findNextTaskNodes(Long clueId, UserInfo userInfo) throws Exception;
	
	
	
	/**
	 * 获取显示人员的环节中的人员信息
	 * @param userInfo
	 * @param curnodeName
	 * @param nodeName
	 * @param nodeCode
	 * @param nodeId
	 * @param params eventId 事件id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNodeInfo(UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params) throws Exception;
	
	
	
	/**
	 * 获取工作流相关展示信息（用于页面详情）
	 * @param infoId
	 * @param listType
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> capWorkflowRelativeData(String infoId, Integer listType, UserInfo userInfo) throws Exception;
	
	/** 工作流部分接口end	 */
}
