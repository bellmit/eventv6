package cn.ffcs.zhsq.timeApplication;

import java.util.List;
import java.util.Map;

/**
 * 时限审核
 * @author zhangls
 *
 */
public interface ITimeApplicationCheckService {
	/**
	 * 时限申请审核状态
	 * STATUS_INVALID	删除
	 * STATUS_FAIL		审核不通过
	 * STATUS_SUCCESS	审核通过
	 * STATUS_INIT		待审核
	 * @author zhangls
	 *
	 */
	public enum CHECK_STATUS {
		STATUS_INVALID("0", "已删除"),
		STATUS_SUCCESS("1", "已通过"),	
		STATUS_FAIL("2", "未通过"),		
		STATUS_INIT("3", "待审核");		
		
		private String status;
		private String statusName;
		
		private CHECK_STATUS(String status, String statusName) {
			this.status = status;
			this.statusName = statusName;
		}
		
		public String getValue() {
			return this.status;
		}
		
		public String getName() {
			return this.statusName;
		}
		
		@Override
		public String toString() {
			return this.status;
		}
	}
	
	/**
	 * 审核人员类型
	 * AUDITOR_USER	人员
	 * AUDITOR_ORG	组织
	 * @author zhangls
	 *
	 */
	public enum AUDITOR_TYPE {
		AUDITOR_USER("0"),	//审核类型为人员
		AUDITOR_ORG("1"),	//审核类型为组织
		AUDITOR_ROLE("2"),	//审核类型为角色
		AUDITOR_NONE("9");	//不设置审核人员，即任何人都可审核该记录
		
		private String auditorType;
		
		private AUDITOR_TYPE(String auditorType) {
			this.auditorType = auditorType;
		}
		
		public String getValue() {
			return this.auditorType;
		}
		
		@Override
		public String toString() {
			return this.auditorType;
		}
	}
	
	/**
	 * 新增时限审核记录
	 * @param timeAppCheck
	 * 		必填参数
	 *   			applicationId	申请id
	 *   			userOrgCode	组织编码，用户获取个性化配置信息
	 *   			auditorId			审核用户id，当auditorType为ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_USER时必填
	 *   			auditorOrgId	审核组织id
	 *   
	 * @return 新增成功返回checkId，否则返回-1L
	 * @throws Exception 
	 */
	public Long saveTimeAppCheck(Map<String, Object> timeAppCheck) throws Exception;
	
	/**
	 * 依据checkId更新时限审核记录
	 * @param timeAppCheck
	 * 		必填参数
	 * 						checkId		审核记录id
	 *   					userOrgCode	组织编码，用户获取个性化配置信息
	 * 
	 * 		非必填参数
	 * 						updaterId				审核人员id
	 * 						updaterName		审核人员姓名
	 * 						updaterOrgId		审核人员组织id
	 * 						updaterOrgName	审核人员组织名称
	 * @return 更新成功返回true，否则返回false
	 * @throws Exception 
	 */
	public boolean updateTimeAppCheckById(Map<String, Object> timeAppCheck) throws Exception;
	
	/**
	 * 依据checkId删除时限审核记录
	 * @param checkId	主键
	 * @param delUserId	删除操作用户
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean delTimeAppCheckById(Long checkId, Long delUserId);
	
	/**
	 * 依据checkId获取时限审核记录
	 * @param checkId
	 * @return
	 */
	public Map<String, Object> findTimeAppCheckById(Long checkId);
	
	/**
	 * 无分页获取时限申请审核信息
	 * @param timeAppCheck
	 * 			applicationId	时限申请记录id
	 * 			isTransferUser	是否转换人员姓名，true为转换；默认为false
	 * 			isTransferOrg	是否转换组织名称，true为转换；默认为false
	 * @return
	 */
	public List<Map<String, Object>> findTimeAppCheckList(Map<String, Object> timeAppCheck);
	
}
