package cn.ffcs.zhsq.eventExtend.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 事件扩展属性接口
 * @author zhangls
 *
 */
public interface IEventExtendService {
	/**
	 * 新增事件扩展属性
	 * @param eventExtend
	 * 必填属性
	 * 		eventId			事件id
	 * 非必填属性
	 * 		isInspected		是否被督察督办，0 否；1 是
	 * 		isJointHandled	是否联合办理，0 否；1 是
	 * 		isJointOperated	是否联席交办，0 否；1 是
	 * 		status			状态，0 无效；1 有效
	 * @return
	 * 		新增成功，返回extendId，否则返回-1L
	 * @throws Exception 
	 */
	public Long saveEventExtend(Map<String, Object> eventExtend) throws Exception;
	
	/**
	 * 更新事件扩展属性
	 * @param eventExtend
	 * 必填属性
	 * 		extendId		主键id
	 * 非必填属性
	 * 		eventId			事件id
	 * 		isInspected		是否被督察督办，0 否；1 是
	 * 		isJointHandled	是否联合办理，0 否；1 是
	 * 		isJointOperated	是否联席交办，0 否；1 是
	 * 		status			状态，0 无效；1 有效
	 * @return 更新成功返回true，否则返回false
	 * @throws Exception 
	 */
	public boolean updateEventExtendById(Map<String, Object> eventExtend) throws Exception;
	
	/**
	 * 依据id删除事件扩展属性
	 * @param extendId
	 * @param delUserId
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean delEventExtendById(Long extendId, Long delUserId);
	
	/**
	 * 依据id获取事件扩展属性
	 * @param extendId	主键id
	 * @return
	 * 		extendId		主键id
	 * 		eventId			事件id
	 * 		isInspected		是否被督察督办，0 否；1 是
	 * 		isJointHandled	是否联合办理，0 否；1 是
	 * 		isJointOperated	是否联席交办，0 否；1 是
	 * 		status			状态，0 无效；1 有效
	 */
	public Map<String, Object> findEventExtendById(Long extendId);
	
	/**
	 * 依据事件id获取事件扩展属性
	 * @param eventId	事件id
	 * @return
	 * 		extendId		主键id
	 * 		eventId			事件id
	 * 		isInspected		是否被督察督办，0 否；1 是
	 * 		isJointHandled	是否联合办理，0 否；1 是
	 * 		isJointOperated	是否联席交办，0 否；1 是
	 * 		status			状态，0 无效；1 有效
	 */
	public Map<String, Object> findEventExtendByEventId(Long eventId);
	
	/**
	 * 依据事件id获取事件扩展属性
	 * @param eventId	事件id
	 * @param map	            额外参数
	 *            orgCode	            组织编码
	 *            isFormat	            是否格式化输出数据（字典转化） String 1：是；0：否
	 * @return
	 * 		extendId		主键id
	 * 		eventId			事件id
	 * 		isInspected		是否被督察督办，0 否；1 是
	 * 		isJointHandled	是否联合办理，0 否；1 是
	 * 		isJointOperated	是否联席交办，0 否；1 是
	 * 		status			状态，0 无效；1 有效
	 */
	public Map<String, Object> findEventExtendByEventId(Long eventId,Map<String,Object> map);
	
	/**
	 * 分页获取事件扩展记录
	 * 默认查询事件状态为：00，01，02，03，04
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			listType	列表类型 
	 * 				1		辖区内被督察督办事件
	 * 				2		我的被督察督办事件
	 * 				3		辖区内事件
	 * 				4		我的待办事件列表
	 * 			infoOrgCode			地域编码
	 * 			startInfoOrgCode	默认地域编码，需要infoOrgCode参数时，且infoOrgCode为空时，启用该值
	 * 			handleDateFlag		办理情况，1 正常；2 将到期；3 已过期；当handleDateFlag为3时，不查询状态为04的事件
	 * 			eventStatus			事件状态，多个值使用英文逗号连接
	 * @return
	 */
	public EUDGPagination findEventExtendPagination(int pageNo,
			int pageSize, Map<String, Object> params);
	
	/**
	 * 获取事件时限申请初始化变量
	 * @param eventId			事件id
	 * @param applicationType	申请类别，1 督查督办；2 延时申请
	 * @param userInfo			用户信息
	 * @param extraParam
	 * 				instanceId	流程实例id
	 * 				isAutoAudit	是否自动添加时限审核，true为是
	 * @return
	 * 		isAutoAudit		是否自动添加时限审核，true为是；默认为true
	 * 		applicationType	申请类别，1 督查督办；2 延时申请
	 * 		auditorId		审核人员id，isAutoAudit为true时设置
	 * 		auditorOrgId	审核组织id，isAutoAudit为true时设置
	 * 		businessId		业务id
	 * 		businessKeyId	业务主键id
	 * 		serviceName		审核通过后，回调实现名称
	 * @throws Exception 
	 */
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 依据事件id获取该事件祖先数量
	 * @param eventId	事件id
	 * @param params
	 * 			userOrgCode	组织编码
	 * @return
	 */
	public int findAncestorCountByEventId(Long eventId, Map<String, Object> params);
	
	/**
	 * 依据事件id获取该事件组织列表
	 * @param eventId	事件id
	 * @param params
	 * 			userOrgCode	组织编码
	 * @return
	 */
	public List<Map<String, Object>> findAncestorListByEventId(Long eventId, Map<String, Object> params);
}
