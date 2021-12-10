package cn.ffcs.zhsq.timeApplication;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 时限申请
 * @author zhangls
 *
 */
public interface ITimeApplicationService {
	/**
	 * 时限申请 时间间隔单位
	 * WEEK_DAY		工作日
	 * NATURAL_DAY	自然日
	 * @author zhangls
	 *
	 */
	public enum INTERVAL_UNIT {	
		WEEK_DAY("1", "工作日"),		//工作日
		NATURAL_DAY("2", "自然日"),	//自然日
		NATURAL_HOUR("3", "小时");	//自然小时
		
		private String intervalUnit;
		private String intervalUnitName;
		
		private INTERVAL_UNIT(String intervalUnit, String intervalUnitName) {
			this.intervalUnit = intervalUnit;
			this.intervalUnitName = intervalUnitName;
		}
		
		public String getValue() {
			return intervalUnit;
		}
		
		public String getName() {
			return this.intervalUnitName;
		}
		
		@Override
		public String toString() {
			return this.intervalUnit;
		}
	}
	
	/**
	 * 时限申请类别
	 * 该类别与APPLICATION_TYPE_HIDE一体
	 * 该类别在页面中展示
	 * 
	 * EVENT_INSPECT	督查督办
	 * EVENT_DELAY		延时申请
	 * 
	 * @author zhangls
	 *
	 */
	public enum APPLICATION_TYPE {	
		EVENT_INSPECT("1", "督查督办"),	//督查督办
		EVENT_DELAY("2", "被督查延时申请"),	//被督查延时申请
		EVENT_TODO("3", "待办延时申请"),	//待办延时申请
		EVENT_REPORT("4", "上报时限申请"),	//上报增添时限
		EVENT_SEND("5", "下派时限申请"),	//下派增添时限
		EVENT_TYPE("8", "事件分类修改申请"),	//事件类别需要变更时，提交的申请
		EVENT_VERIFY("9", "事件重置申请"),	//事件审核记录重置，重新提交生成新的事件
		EVENT_CHECK("10", "核验事件申请");//核验不通过，会将事件逻辑删除
		
		private String applicationType;
		private String applicationTypeName;
		
		private APPLICATION_TYPE(String applicationType, String applicationTypeName) {
			this.applicationType = applicationType;
			this.applicationTypeName = applicationTypeName;
		}
		
		public String getValue() {
			return this.applicationType;
		}
		
		public String getName() {
			return this.applicationTypeName;
		}
		
		@Override
		public String toString() {
			return this.applicationType;
		}
	}
	
	/**
	 * 时限申请类别
	 * 该类别与APPLICATION_TYPE为一体
	 * 该类别不在页面中展示
	 * @author zhangls
	 *
	 */
	public enum APPLICATION_TYPE_HIDE {	
		EVENT_INVALID("6", "废除事件申请"),	//废除事件申请
		EVENT_SUSPEND("61", "挂起事件申请"),
		EVENT_PREPRESS("7", "事件预处理申请"),
		EVENT_RECOVERY("11", "事件恢复申请");
		
		private String applicationType;
		private String applicationTypeName;
		
		private APPLICATION_TYPE_HIDE(String applicationType, String applicationTypeName) {
			this.applicationType = applicationType;
			this.applicationTypeName = applicationTypeName;
		}
		
		public String getValue() {
			return this.applicationType;
		}
		
		public String getName() {
			return this.applicationTypeName;
		}
		
		@Override
		public String toString() {
			return this.applicationType;
		}
	}
	
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
	 * 		isShowSendMsg	是否可以发送短信，true为是；默认为false
	 * @throws Exception 
	 */
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 保存时限申请信息
	 * @param timeApplication
	 * 			applicationType	申请类型
	 * 			businessId		业务id，依据applicationType变化
	 * 			businessKeyId	业务模块主键id，依据applicationType变化
	 * 			status			申请状态
	 * 			reason			申请原因
	 * 			interval		申请时限
	 * 			intervalUnit	申请时限单位，默认为工作日
	 * 
	 * 			isDuplicatedCheck	是否进行判重验证，true为是；默认为true
	 * 			isAutoAudit			是否自动添加时限审核，true为是；默认为false
	 * 			timeAppCheckStatus	审核状态，默认为ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue()，isAutoAudit为true时，使用
	 * 			auditorId			审核人员id，isAutoAudit为true时，使用
	 * 			auditorOrgId		审核人员组织id，isAutoAudit为true时，使用
	 * 			checkAdvice			审核意见；isAutoAudit为true时使用
	 * 								为空时，审核状态不为ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue()时，使用reason属性
	 * @return 新增成功返回applicationId，否则返回-1L
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception;
	
	/**
	 * 依据applicationId更新时限申请信息
	 * @param timeApplication
	 * @return 更新成功返回true，否则返回false
	 * @throws Exception 
	 */
	public boolean updateTimeApplicationById(Map<String, Object> timeApplication) throws Exception;
	
	/**
	 * 依据applicationId删除时限申请信息
	 * @param applicationId	主键
	 * @param delUserId		删除操作人员
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean delTimeApplicationById(Long applicationId, Long delUserId);
	
	/**
	 * 依据applicationId获取时限申请信息
	 * @param applicationId				时限审核记录id
	 * @param params
	 * 			isTimeAppCheckNeeded	true则获取时限审核信息；默认为false
	 * 			isTransferUser			是否转换人员姓名，true为转换；默认为false
	 * 			isTransferOrg			是否转换组织名称，true为转换；默认为false
	 * @return
	 */
	public Map<String, Object> findTimeAppliationById(Long applicationId, Map<String, Object> params);
	
	/**
	 * 获取时限申请信息数量
	 * @param timeApplication
	 * @return
	 */
	public int findTimeApplicationCount(Map<String, Object> timeApplication);
	
	/**
	 * 无分页获取时限申请信息
	 * @param timeApplication
	 * 			applicationTypeList		时限申请类型，类型为List<String>
	 * 			applicationType			时限申请类型，该属性优先于applicationTypeList
	 * 			businessKeyId			业务主键id
	 * 			userOrgCode				组织编码，isTransferDict为true时，必填
	 * 			isTransferUser			是否转换人员姓名，true为转换；默认为false
	 * 			isTransferOrg			是否转换组织名称，true为转换；默认为false
	 * 			isTransferDict			是否进行字典转换，true为转换；默认为false
	 * @return
	 */
	public List<Map<String, Object>> findTimeApplicationList(Map<String, Object> timeApplication);
	
	/**
	 * 分页获取时限申请信息
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param timeApplication
	 * 			listType	列表类型
	 * 				1 我的审核列表；2 我的申请列表；
	 * @return
	 */
	public EUDGPagination findTimeApplicationPagination(int pageNo, int pageSize,
			Map<String, Object> timeApplication);
	
	/**
	 * 获取事件时限申请信息数量
	 * @param timeApplication
	 * 			listType	列表类型，默认为1
	 * 				1 我的审核列表；
	 * 				2 我的申请列表；
	 * 				3 事件删除审核列表；
	 * 				4 事件删除列表；
	 * 				5 事件预处理审核列表；
	 * 			isCapApplicant			是否获取申请人信息，Boolean类型，true为获取，默认为false
	 * 				applicantName		申请人姓名，String类型，isCapApplicant为true时生效
	 * 			eventCreateTimeStart	事件采集开始时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			eventCreateTimeEnd		事件采集结束时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			createTimeStart			时限申请开始时间、事件删除开始时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			createTimeEnd			时限申请结束时间、事件删除结束时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * @return
	 * @throws Exception
	 */
	public int findEventTimeAppCount(Map<String, Object> timeApplication) throws Exception;
	
	/**
	 * 分页获取事件时限申请信息
	 * @param pageNo	页面
	 * @param pageSize	每页记录数
	 * @param timeApplication
	 * 			listType	列表类型，默认为1
	 * 				1 我的审核列表；
	 * 				2 我的申请列表；
	 * 				3 事件删除审核列表；
	 * 				4 事件删除列表；
	 * 				5 事件预处理审核列表；
	 * 			isCapApplicant			是否获取申请人信息，Boolean类型，true为获取，默认为false
	 * 				applicantName		申请人姓名，String类型，isCapApplicant为true时生效
	 * 			isCreatedBySelf			是否由当前人员提出申请，Boolean类型，true为是，默认为false，为true时表示获取operateUserId提出的申请
	 * 			operateUserId			操作用户id，Long类型
	 * 			operateUserOrgId		操作组织id，Long类型
	 * 			infoOrgCode			         地域编码，String类型
	 * 			userOrgCode			         组织编码，String类型
	 * 			eventCreateTimeStart	事件采集开始时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			eventCreateTimeEnd		事件采集结束时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			createTimeStart			时限申请开始时间、事件删除开始时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * 			createTimeEnd			时限申请结束时间、事件删除结束时间，String类型，格式为yyyy-mm-dd hh24:mi:ss
	 * @return
	 * 		applicantName	申请人姓名，isCapApplicant为true时获取
	 */
	public EUDGPagination findEventTimeAppPagination(int pageNo, int pageSize,
			Map<String, Object> timeApplication);
}
