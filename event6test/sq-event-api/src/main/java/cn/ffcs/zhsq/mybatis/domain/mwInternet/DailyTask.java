package cn.ffcs.zhsq.mybatis.domain.mwInternet;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 日常任务表模块bo对象
 * @Author: guoyd
 * @Date: 04-08 10:20:12
 * @Copyright: 2018 福富软件
 */
public class DailyTask implements Serializable {

	private Long ddtId; //任务编号
	private String infoOrgCode; //所属小区
	private String infoOrgName; //小区名称
	private String taskTitle; //任务标题
	private String taskDesc; //任务描述
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
	//
	private String overTimeStr;
	private String createTimeStr;
	private String updateTimeStr;
	
	public Long getDdtId() {
		return ddtId;
	}
	public void setDdtId(Long ddtId) {
		this.ddtId = ddtId;
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
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
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
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
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