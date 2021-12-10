package cn.ffcs.zhsq.keyelement.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;

public interface IFiveKeyElementService {
	
	public static final int PARENT_LEVEL_ONE = 1;//上报一级
	public static final int PARENT_LEVEL_TWO = 2;//上报二级
	
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
	public Map<String, Object> getNodeInfoForEvent(UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取用户选择框中的组织树
	 * @param userInfo
	 * @param orgId
	 * @param nodeCode
	 * @param level
	 * @param professionCode
	 * @param params 
	 * 			eventId 事件id
	 * 			nodeId 选中的节点的id
	 * 			orgRootId 给定的组织树根节点id
	 * @return
	 * @throws Exception
	 */
	public List<GdZTreeNode> getTreeForEvent(UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception;

	/**
	 * 获取组织人员列表
	 * @param orgId			组织id
	 * @param nodeCode		办理环节节点编码
	 * @param extraParam	额外参数
	 * 				eventId	事件id
	 * @return
	 * @throws Exception
	 */
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Map<String, Object> extraParam) throws Exception;
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception;
	
	public List<UserBO> getReportUserList(INodeCodeHandler nodeCodeHandler, String positionName, Long positionId, String includeNext, Long orgId, String orgCode);
	
	/**
	 * 获取采集并结案时，可办理人员
	 * @param orgId	采集操作者组织的父级组织
	 * @return
	 * @throws Exception
	 */
	public List<UserBO> findCollectToCloseUserList(Long orgId) throws Exception;
	
	public List<UserBO> findReportUserList(String nodeCode, String positionName, Long positionId, String includeNext, Long orgId, String orgCode) throws Exception;
	
	public INodeCodeHandler createNodeCodeHandle(String nodeCode) throws Exception;
	
	/**
	 * 过滤用户中的网格员
	 * @param orgId	组织id
	 * @param users	用户列表
	 * @return 只留下用户列表中不是网格员的记录
	 */
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users);
	
	/**
	 * 过滤用户中的网格员
	 * @param orgId	组织id
	 * @param users	用户列表
	 * @param isRemoveGridAdmin false为只留下用户列表中是网格员的记录；true为只留下用户列表中不是网格员的记录
	 * @return
	 */
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users, boolean isRemoveGridAdmin);
	
	/**
	 * 查找上级组织
	 * @param orgId 			开始查找的组织id
	 * @param level 			向上查找的层级
	 * @param professionCode	专业编码，如政府管理为zfgl
	 * @param nodeCode 			环节编码，如U6R1U5
	 * @param userInfo 			当前用户信息
	 * @param extraParam		额外参数
	 * 				eventId		事件id
	 * @return 未查找到时，返回null
	 * @throws Exception
	 */
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, String nodeCode, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	/**
	 * 查找上级组织
	 * 在新组织中，如果当前组织为非政府管理专业，则其上级(政府管理专业)为其同级政府管理专业的组织
	 * @param orgId
	 * @param level
	 * @param professionCode
	 * @return 未查找到时，返回null
	 */
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, UserInfo userInfo);
	/**
	 * 查找上级组织，默认专业为政府(zfgl)
	 * 在新组织中，如果当前组织为非政府管理专业，则其上级(政府管理专业)为其同级政府管理专业的组织
	 * @param orgId
	 * @param level
	 * @param userId
	 * @return 未查找到时，返回null
	 */
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, UserInfo userInfo);
	
	/**
	 * 判断当前当前网格下的用户是否网格员
	 * @param orgId  组织id
	 * @param userId 用户id
	 * @return
	 */
	public boolean isUserIdGridAdmin(Long orgId, Long userId);
	/**
	 * 判断当前当前网格下的用户是否网格员
	 * @param orgId		组织id
	 * @param userId	用户id
	 * @return 是网格员，返回"1"；不是网格员返回"0"
	 */
	public String isUserIdGridAdminForString(Long orgId, Long userId);
	/**
	 * 判断当前当前网格下的用户是否网格员
	 * @param isGridAdmin	true 是网格员；false 不是网格员
	 * @return 是网格员，返回"1"；不是网格员返回"0"
	 */
	public String isUserIdGridAdminForString(boolean isGridAdmin);
	
	/**
	 * 获取配置的网格管理员职务
	 * @param orgId 组织编码
	 * @return 没有获取到配置时，返回空串；否则，返回格式为'001','002'的网格管理员职务字符串
	 */
	public String capGridAdminDuty(Long orgId);
	/**
	 * 判断人员是否是指定职务的网格管理员
	 * 如果组织没有对应的所属地域，则所有人员均认为不满足条件
	 * @param startGridId 起始查找的网格编码
	 * @param userId 用户id
	 * @param gridAdminDuty 网格管理员职务
	 * 001 网格管理员；002 网格协管员；003 网格督导员；004 包片段警；
	 * @return true 存在；false 不存在
	 */
	public boolean isUserIdGridAdminExists(Long startGridId, Long userId, String gridAdminDuty);
	
	/**
	 * 查找同级职能部门，并添加
	 * @param orgInfoList 同级单位list
	 * @param nodes
	 * @param removeOrgId 需要扣除的组织专业对应的组织id
	 * @return 查找到的同级职能部门
	 */
	public Map<Long, OrgSocialInfoBO> findDeptBySameAndAdd(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Long removeOrgId);
}
