package cn.ffcs.zhsq.event.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 事件额外信息处理接口
 * @author zhangls
 *
 */
public interface IEventDisposal4ExtraService {
	/**
	 * 办理事件至归档
	 * @param eventIdStr	事件id，多个值使用英文逗号分隔
	 * @param userInfo		用户信息
	 * @param params		额外参数
	 * 			evaLevel	评价等级
	 * 			evaContent	评价内容
	 * @return
	 * 		successTotal	成功归档事件数量
	 * 		msgWrong		归档失败信息
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Map<String, Object> archiveWorkflow4Event(String eventIdStr, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 办理事件至归档
	 * @param eventIdList	事件id
	 * @param userInfo		用户信息
	 * @param params		额外参数
	 * 			evaLevel	评价等级
	 * 			evaContent	评价内容
	 * @return
	 * 		successTotal	成功归档事件数量
	 * 		msgWrong		归档失败信息
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Map<String, Object> archiveWorkflow4Event(List<Long> eventIdList, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 工作流提交办理
	 * @param params
	 * 			eventId			事件id，类型为Long，该属性优先于eventIdStr生效
	 * 			eventIdStr		事件id，类型为String，该属性优先于eventIdList生效
	 * 			eventIdList		事件id，类型为List<Long>
	 * 
	 * 			advice 			办理意见
	 * 			smsContent 		短信内容
	 * @param userInfo			当前登入用户信息
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Map<String, Object> subWorkflow2AppointedTask(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取事件站内消息
	 * @param pageNo		页码
	 * @param pageSize		每页记录数
	 * @param params
	 * 						moduleCode	模块编码
	 * @return
	 */
	public EUDGPagination findMsgPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 获取用于事件对接、对外等使用的信息
	 * @param eventId	事件信息，要求事件id需要能找到有效的事件信息
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * 					isCapAttachment	是否获取附件信息，Boolean类型，true为获取，默认为false
	 * @return 
	 * 			eventId				事件id，Long类型
	 * 			code					事件编码，String类型
	 * 			happenTimeStr	事发时间，String类型，格式为yyyy-MM-dd HH:mm:ss
	 * 			occurred			事发详址，String类型
	 * 			content				事件描述，String类型
	 * 			contactUser		联系人员，String类型
	 * 			tel						联系电话，String类型
	 * 			attachmentIds	附件id，String类型，有多个值时，使用英文逗号分隔，isCapAttachment为true时，该属性生效
	 * 			attachmentMapList	附件信息，List<Map<String, Object>类型，isCapAttachment为true时，该属性生效
	 * 				attachmentName	附件名称
	 * 				attachmentPath	附件路径
	 * 				attachmentSize		附件大小，单位为B
	 * @throws Exception 
	 * 			
	 */
	public Map<String, Object> fetchEventInfo4Dock(Long eventId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取需要星级评价的事件信息
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params	额外参数
	 * 			infoOrgCode	地域编码
	 * @return
	 */
	public EUDGPagination findJurisdictionEvent4EvaPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 批量评价事件
	 * @param eventIdStr	事件id，多个值使用英文逗号分隔
	 * @param userInfo		操作用户信息
	 * @param params		额外参数
	 * 			isDuplicateEva	是否可重复评价，默认为false，重复判断条件：evaObj + eventId
	 * @return
	 * 		total			需要评价事件数量
	 * 		successTotal	成功评价事件数量
	 * @throws ZhsqEventException 
	 */
	public Map<String, Object> evaEvent4Batch(String eventIdStr, UserInfo userInfo, Map<String, Object> params) throws ZhsqEventException;
	
	/**
	 * 获取评价记录数量
	 * @param params
	 * 			evaType		评价操作类型，1 镇级评价；2 区级复评；
	 * @return
	 */
	public int findCount4Eva(Map<String, Object> params);
}
