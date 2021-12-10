package cn.ffcs.zhsq.reportFocus;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 南安重点关注上报信息操作接口
 * 				  相关表：T_EVENT_REPORT_FOCUS
 * @ClassName:   IReportFocusService   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 上午11:18:18
 */
public interface IReportFocusService {
	/**
	 * 保存/更新关注上报信息
	 * @param reportFocus	关注上报信息
	 * 			reportId	上报id
	 * 			reportUUID	上报UUID
	 * 
	 * 			reportType 	1 两违防治业务；2 住建业务；
	 * 			isSaveReportFocusExtend	是否保存/更新上报扩展信息，默认为true，为false时，则只保存关注上报主表信息
	 * @param userInfo		操作用户信息
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long saveReportFocus(Map<String, Object> reportFocus, UserInfo userInfo) throws Exception;
	
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
	 * @return
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
	 * 统计关注上报信息数量
	 * @param params	查询参数
	 * @return
	 * @throws Exception 
	 */
	public int findCount4ReportFocus(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页获取关注上报信息记录
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params	查询参数
	 * @return
	 * @throws Exception 
	 */
	public EUDGPagination findPagination4ReportFocus(int pageNo, int pageSize, Map<String, Object> params) throws Exception;

	/**
	 * 不分页获取重点关注上报信息
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
	 * 依据短信模板获取短信内容
	 * @param params	构建参数
	 * 			smsTemplateName	短信模板名称
	 * @param userInfo	操作用户信息
	 * @return
	 * @throws Exception
	 */
	public String capSmsContent(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 积分记录(OA)
	 * @param recordType	记录类型，1 提交操作；2 督办操作；
	 * @param userInfo		操作用户信息
	 * @param params		额外参数
	 * 必填参数
	 * 			instanceId	流程实例id
	 * 			proInstance	流程实例，类型：cn.ffcs.workflow.om.ProInstance
	 * 			reportType	报告类型
	 * @return
	 * @throws Exception
	 */
	public boolean recordPoint(int recordType, UserInfo userInfo, Map<String, Object> params) throws Exception;
}
