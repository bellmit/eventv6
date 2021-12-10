package cn.ffcs.zhsq.mybatis.domain.smsTask;

import java.io.Serializable;

public class SmsTask implements Serializable {
	
	Long taskId;			//主键
	Long bizId;				//业务ID
	String bizType;			//业务类型 00：新事件
	String phoneNum;		//短信接收电话号码
	Integer successCount;	//发送成功次数
	Integer failCount;		//发送失败次数
	String taskType;		//任务类型 0：将到期发送短信
	String taskStatus;		//任务状态 0：未完成；1：已完成
	
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	
}
