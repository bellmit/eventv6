package cn.ffcs.zhsq.reportFocus;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 重点关注上报整合接口，对外开放接口
 * @ClassName:   IReportIntegrationService   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午7:51:10
 */
public interface IReportIntegrationService extends IReportFocusWorkflowService {
	/**
	 * 保存/更新重点关注上报信息
	 * @param reportFocus	上报信息
	 * @param userInfo		操作用户
	 * @param params		额外参数
	 * 			isStart				是否启动工作流，true为是；默认为false；
	 * 			formTypeId			表单类型id
	 * 			targetNextNodeName	下一环节名称，需要该环节为当前环节的可用下一环节，且该环节为人员办理，办理模板只有一个
	 * 			formType			表单类型，为空时，会通过formTypeId获取，targetNextNodeName有效时，启动该属性
	 * 			isCapDistributeUser	是否获取分送人员，默认为true，targetNextNodeName有效时，启动该属性
	 * 			isCapSelectUser		是否获取选送人员，默认为false，targetNextNodeName有效时，启动该属性
	 * @return
	 * 		reportId		上报id，上报信息添加成功时，返回
	 * 		result			办理结果，true办理成功；false办理失败			
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Map<String, Object> saveReportFocus(Map<String, Object> reportFocus, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 通过uuid获取关注上报信息
	 * @param reportUUID	上报uuid
	 * @param userInfo		操作用户
	 * @param params		额外信息
	 * @return
	 */
	public Map<String, Object> findReportFocusByUUIDSimple(String reportUUID, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 通过uuid获取关注上报信息
	 * @param reportUUID	上报uuid
	 * @param userInfo		操作用户
	 * @param params		额外信息
	 * 			reportId	上报id
	 * 			reportType 	1 两违防治业务；2 住建业务；
	 * 			isDictTransfer	是否进行字典转换，true为是；默认为true
	 * 			isCapRegionName	是否获取地域名称
	 * @return
	 * 		regionName	地域名称，isCapRegionName为true时，返回
	 * 		regionPath	地域全路径，isCapRegionName为true时，返回
	 */
	public Map<String, Object> findReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 依据UUID删除上报信息
	 * @param reportUUID	上报UUID
	 * @param userInfo		删除操作用户
	 * @param params		额外参数
	 * @return 成功删除记录数
	 */
	public int delReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 统计入格事件主流程待办数量
	 * @param params
	 * 			curUserId	当前办理人员id，为空时，使用operateUserId
	 * 			curOrgId	当前办理组织id，为空时，使用operateOrgId
	 * @return
	 * 		reportType	入格类型
	 * 		reportTotal	数量
	 * @throws Exception
	 */
	public List<Map<String, Integer>> findCount4IntegrationTodo(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取入格事件我的阅办记录数量
	 * @param params
	 * 			startRegionCode		默认地域编码
	 * 			userOrgCode			操作用户组织编码
	 * 			msgReceiveUserId	消息接收用户id，为空时，使用operateUserId
	 * 			msgReceiveOrgId		消息接收组织id，为空时，使用operateOrgId
	 * 			operateUserId		操作用户id
	 * 			operateOrgId		操作组织id
	 * @return
	 * @throws Exception
	 */
	public int findCount4IntegrationMsgReading(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取入格事件我的阅办记录列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			startRegionCode		默认地域编码
	 * 			userOrgCode			操作用户组织编码
	 * 			msgReceiveUserId	消息接收用户id，为空时，使用operateUserId
	 * 			msgReceiveOrgId		消息接收组织id，为空时，使用operateOrgId
	 * 			operateUserId		操作用户id
	 * 			operateOrgId		操作组织id
	 * @return
	 * @throws Exception
	 */
	public EUDGPagination findPaginationIntegrationMsgReading(int pageNo, int pageSize, Map<String, Object> params) throws Exception;
	
	/**
	 * 统计重点关注上报信息数量
	 * @param params		额外参数
	 * 		listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * 		    52 辖区所有带地域全路径查询
	 * 		 		isCapRegionPath 获取地域全路径，默认不获取，返回值：regionPath; true 获取, false 不获取;
	 *
	 * @return
	 * @throws Exception 
	 */
	public int findCount4ReportFocus(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页获取重点关注上报信息数量
	 * @param pageNo
	 * @param pageSize
	 * @param params		额外参数
	 * 		listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * 		isDictTransfer	是否进行字典转换，true为是；默认为true
	 * 		userOrgCode		组织编码，用于字典转换
	 * @return
	 * @throws Exception 
	 */
	public EUDGPagination findPagination4ReportFocus(int pageNo, int pageSize, Map<String, Object> params) throws Exception;

	/**
	 * 不分页获取重点关注上报信息数量
	 * @param params		额外参数
	 * 		listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * 		isDictTransfer	是否进行字典转换，true为是；默认为true
	 * 		userOrgCode		组织编码，用于字典转换
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findList4ReportFocus(Map<String, Object> params) throws Exception;

	/**
	 * 依据消息id设置消息为已读
	 * @param msgId		消息id
	 * @param userInfo	操作用户
	 * @return 操作成功返回true，否则返回false
	 */
	public boolean readMsgById(Long msgId, UserInfo userInfo);
	
	/**
	 * 依据短信模板获取短信内容
	 * @param params	构建参数
	 * 			smsTemplateName	短信模板名称
	 * @param userInfo	操作用户信息
	 * @return
	 * @throws Exception
	 */
	public String capSmsContent(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 新增催办/督办记录
	 * @param params	催办/督办信息
	 * 必填参数
	 * 			category			记录类别，1 催办；2 督办；
	 * 			instanceId			流程实例id
	 * 			remindRemark		催办/督办意见
	 * 非必填参数
	 * 			otherMobileNums		额外接收短信的号码，多个值使用英文逗号进行分割
	 * 			isSendSmsContent	是否发送短信，默认为true
	 * 			smsContent			短信内容，为空时，不发送短信
	 * 			
	 * @param userInfo	操作用户信息
	 * @return
	 * @throws Exception
	 */
	public boolean addUrgeOrRemind(Long instanceId, Map<String, Object> params, UserInfo userInfo) throws Exception;
}
