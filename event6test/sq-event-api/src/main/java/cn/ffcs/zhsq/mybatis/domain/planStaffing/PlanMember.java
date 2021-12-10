package cn.ffcs.zhsq.mybatis.domain.planStaffing;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 应急预案人员 
 * @Author: LINZHU
 * @Date: 04-28 16:17:29
 * @Copyright: 2021 福富软件
 */
public class PlanMember implements Serializable {
	private static final long serialVersionUID = 1L;
	private String planMemberId; //应急人员主键
	private String planConfigId; //预案配置主键
	private String planRole; //预案人员角色，字典，牵头导、主办领导、配合领导、主办科室、配合科室
	private String planRoleName;
	private Long userId; //预案人员ID
	private String userName; //预案人员姓名
	private Long orgId; //预案人员所属组织id
	private String orgName; //预案人员所属组织名称
	private String mainJob; //主要职责
	private Long creator; //创建人
	private Date createTime; //创建时间
	private Long updator; //更新人
	private Date updateTime; //更新时间
	private String isValid; //记录状态（0-无效，1-有效）

	public String getPlanMemberId() {
		return planMemberId;
	}
	public void setPlanMemberId(String planMemberId) {
		this.planMemberId = planMemberId;
	}
	public String getPlanConfigId() {
		return planConfigId;
	}
	public void setPlanConfigId(String planConfigId) {
		this.planConfigId = planConfigId;
	}
	public String getPlanRole() {
		return planRole;
	}
	public void setPlanRole(String planRole) {
		this.planRole = planRole;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getMainJob() {
		return mainJob;
	}
	public void setMainJob(String mainJob) {
		this.mainJob = mainJob;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getPlanRoleName() {
		return planRoleName;
	}
	public void setPlanRoleName(String planRoleName) {
		this.planRoleName = planRoleName;
	}


}