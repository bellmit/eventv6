package cn.ffcs.zhsq.eventCase.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 案件 江西省罗坊镇
 * @author zhangls
 *
 */
public interface IEventCaseService extends IEventCaseWorkflowService {
	/**
	 * 保存案件信息
	 * @param eventCase	案件信息
	 * 			isStart					true 启动流程；默认为false
	 * 			isClose					true 启动并结案案件，默认为false
	 * 			isAlterResMarker 		true，保存/更新定位信息；默认为false
	 * 				resMarker.mapType	地图类型
	 * 				resMarker.x			经度
	 * 				resMarker.y			纬度
	 * 			isAlterInvolvedPeople	true，保存/更新涉及人员信息；默认为false
	 * 				involvedPeople.eventInvolvedPeople	涉及人员信息，格式为：证件类型1，姓名1，身份证号码1，家庭住址1，联系电话1，备注1；证件类型2，姓名2，身份证号码2，家庭住址2，联系电话2，备注2；
	 * 			isAlterAttachment		true，更新附件业务信息；默认为false
	 * 				attachment.attachmentIds	附件id，以英文逗号分隔
	 * @param userInfo	操作用户
	 * @return 新增成功返回caseId，否则返回-1
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long saveEventCase(Map<String, Object> eventCase, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据案件id更新案件信息，包括设计人员、定位、附件等信息
	 * @param eventCase	案件信息
	 * 			isStart					true 启动流程；默认为false
	 * 			isClose					true 启动并结案案件，默认为false
	 * 			isAlterResMarker 		true，保存/更新定位信息；默认为false
	 * 				resMarker.mapType	地图类型
	 * 				resMarker.x			经度
	 * 				resMarker.y			纬度
	 * 			isAlterInvolvedPeople	true，保存/更新涉及人员信息；默认为false
	 * 				involvedPeople.eventInvolvedPeople	涉及人员信息，格式为：证件类型1，姓名1，身份证号码1，家庭住址1，联系电话1，备注1；证件类型2，姓名2，身份证号码2，家庭住址2，联系电话2，备注2；
	 * 			isAlterAttachment		true，更新附件业务信息；默认为false
	 * 				attachment.attachmentIds	附件id，以英文逗号分隔
	 * 			handleDateInterval		处理时限，单位为工作日
	 * @param userInfo	操作用户
	 * @return 更新成功，返回true；否则返回false
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean updateEventCase(Map<String, Object> eventCase, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据案件id删除案件信息
	 * @param caseId	案件id
	 * @param delUserId	删除操作用户id
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean delEventCaseById(Long caseId, Long delUserId);
	
	/**
	 * 根据案件id获取案件对象，不进行字典属性转换
	 * @param caseId
	 * @return
	 */
	public Map<String, Object> findEventCaseByIdSimple(Long caseId);
	
	/**
	 * 根据案件id获取案件对象
	 * @param caseId	案件id
	 * @param params	额外参数
	 * 			isResMarkerNeeded		true，返回定位信息；默认为false
	 * 				resMarker.mapType	地图类型
	 * 			isInvolvedPeopleNeeded	true，返回涉及人员信息；默认为false
	 * 			isAttachmentNeeded		true，返回附件信息；默认为false
	 * 				attachment.eventSeq	附件类型，前、中、后
	 * @param orgCode	组织编码
	 * @return
	 * 		eventCase		案件信息，类型为cn.ffcs.zhsq.mybatis.domain.eventCase.EventCase
	 * 		resMarker		定位信息，类型为cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker
	 * 		involvedPeople	涉及人员信息，类型为List<cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople>
	 * 		eventInvolvedPeople 涉及人员信息字符串，格式为：证件类型1，姓名1，身份证号码1；证件类型2，姓名2，身份证号码2；
	 * 		attachment		附件信息，类型为List<cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment>
	 */
	public Map<String, Object> findEventCaseById(Long caseId, Map<String, Object> params, String orgCode);
	
	/**
	 * 获取案件记录总量
	 * @param params	额外查询条件
	 * 			urgencyDegree		紧急程度，多个值以英文逗号连接
	 * 			urgencyDegreeArray	紧急程度，类型为String[]，该参数优先于urgencyDegree
	 * 			isAddExtendCol		是否查询扩展列，类型为Boolean
	 * 			statusList			案件状态，类型为List<String>
	 * 			statusArray			案件状态，类型为String[]，该参数优先于statusList
	 * 			status				案件状态，有多个值时，使用英文逗号连接，该参数优先于statusArray，statusList
	 * @param userInfo	用户信息
	 * @return
	 */
	public int findEventCaseCount(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 分页获取案件记录
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params	额外查询条件
	 * 			urgencyDegree		紧急程度，多个值以英文逗号连接
	 * 			urgencyDegreeArray	紧急程度，类型为String[]，该参数优先于urgencyDegree
	 * 			isAddExtendCol		是否查询扩展列，类型为Boolean
	 * 			statusList			案件状态，类型为List<String>
	 * 			statusArray			案件状态，类型为String[]，该参数优先于statusList
	 * 			status				案件状态，有多个值时，使用英文逗号连接，该参数优先于statusArray，statusList
	 * @param userInfo	用户信息
	 * @return
	 */
	public EUDGPagination findEventCasePagination(int pageNo, int pageSize,
			Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 依据案件id获取督办信息
	 * @param caseId	案件id
	 * @return
	 * 		remarks			督办意见
	 * 		remindUserName	督办人员姓名
	 * 		remindDate		督办日期
	 * 		remindDateStr	督办日期文本
	 */
	public List<Map<String, Object>> findRemindListByCaseId(Long caseId);
	
	/**
	 * 依据案件id获取评价信息
	 * @param caseId	案件id
	 * @param orgCode	组织编码
	 * @return
	 * 		creatorName		评价人员姓名
	 * 		createDateStr	评价时间文本
	 * 		evaLevelName	评价等级名称
	 * 		evaContent		评价内容
	 */
	public List<Map<String, Object>> findEvaListByCaseId(Long caseId, String orgCode);

}
