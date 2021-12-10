package cn.ffcs.zhsq.mybatis.domain.mwInternet;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 报修任务表模块bo对象
 * @Author: guoyd
 * @Date: 04-08 10:19:19
 * @Copyright: 2018 福富软件
 */
public class RepairTask implements Serializable {

	private Long drtId; //任务编号
	private String infoOrgCode; //所属小区
	private String infoOrgName; //小区名称
	private String faultDesc; //故障描述
	private String faultType; //故障类型
	private Date repairTime; //上报时间
	private Date orderTime; //预约时间
	private String faultAddr; //故障地址
	private String linkUser; //联系人
	private String linkTel; //电话
	private String isValid; //是否有效:0 无效 1 有效
	private Long createBy; //创建人
	private Date createTime; //创建时间
	private Long orgId;
	private Long updateBy; //修改人
	private Date updateTime; //修改时间
	
	private Date overTime;//处理时限
	private String state;//环节状态(0草稿，1待派发，2待反馈，3.待审核，4已归档)
	private Long taskUserId;
	private String taskUserName;
	private Long taskOrgId;
	private String warnState = "1";//提醒状态（1处于驳回状态）
	 //工作流
    private Long wfDbid_;
	private String wfActivityName_;
	private String wfStatus;
	private Long wfInstanceId;
	private String wfCurUser;
	
	private String faultTypeStr; //故障类型
	private String repairTimeStr; //上报时间
	private String orderTimeStr; //预约时间
	private String overTimeStr;//处理时限
	
	public Long getDrtId() {
		return drtId;
	}
	public void setDrtId(Long drtId) {
		this.drtId = drtId;
	}
	public String getInfoOrgCode() {
		return infoOrgCode;
	}
	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}
	public String getInfoOrgName() {
		return infoOrgName;
	}
	public void setInfoOrgName(String infoOrgName) {
		this.infoOrgName = infoOrgName;
	}
	public String getFaultDesc() {
		return faultDesc;
	}
	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}
	public String getFaultType() {
		return faultType;
	}
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
	public Date getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public String getFaultAddr() {
		return faultAddr;
	}
	public void setFaultAddr(String faultAddr) {
		this.faultAddr = faultAddr;
	}
	public String getLinkUser() {
		return linkUser;
	}
	public void setLinkUser(String linkUser) {
		this.linkUser = linkUser;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getWfDbid_() {
		return wfDbid_;
	}
	public void setWfDbid_(Long wfDbid_) {
		this.wfDbid_ = wfDbid_;
	}
	public String getWfActivityName_() {
		return wfActivityName_;
	}
	public void setWfActivityName_(String wfActivityName_) {
		this.wfActivityName_ = wfActivityName_;
	}
	public String getWfStatus() {
		return wfStatus;
	}
	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}
	public Long getWfInstanceId() {
		return wfInstanceId;
	}
	public void setWfInstanceId(Long wfInstanceId) {
		this.wfInstanceId = wfInstanceId;
	}
	public String getFaultTypeStr() {
		return faultTypeStr;
	}
	public void setFaultTypeStr(String faultTypeStr) {
		this.faultTypeStr = faultTypeStr;
	}
	public String getRepairTimeStr() {
		return repairTimeStr;
	}
	public void setRepairTimeStr(String repairTimeStr) {
		this.repairTimeStr = repairTimeStr;
	}
	public String getOrderTimeStr() {
		return orderTimeStr;
	}
	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}
	public String getOverTimeStr() {
		return overTimeStr;
	}
	public void setOverTimeStr(String overTimeStr) {
		this.overTimeStr = overTimeStr;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getWfCurUser() {
		return wfCurUser;
	}
	public void setWfCurUser(String wfCurUser) {
		this.wfCurUser = wfCurUser;
	}
	public Long getTaskUserId() {
		return taskUserId;
	}
	public void setTaskUserId(Long taskUserId) {
		this.taskUserId = taskUserId;
	}
	public String getTaskUserName() {
		return taskUserName;
	}
	public void setTaskUserName(String taskUserName) {
		this.taskUserName = taskUserName;
	}
	public Long getTaskOrgId() {
		return taskOrgId;
	}
	public void setTaskOrgId(Long taskOrgId) {
		this.taskOrgId = taskOrgId;
	}
	public String getWarnState() {
		return warnState;
	}
	public void setWarnState(String warnState) {
		this.warnState = warnState;
	}

}