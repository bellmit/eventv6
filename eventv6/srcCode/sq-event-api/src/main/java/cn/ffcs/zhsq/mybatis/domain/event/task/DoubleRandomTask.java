package cn.ffcs.zhsq.mybatis.domain.event.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 双随机任务模块bo对象
 * @Author: zkongbai
 * @Date: 11-07 10:15:16
 * @Copyright: 2017 福富软件
 */
public class DoubleRandomTask implements Serializable,Cloneable {
	private static final long serialVersionUID = -6258664381089837707L;
	
	private Long id;
	private Long eventId; //事件ID
	private String judgeComment; //评价意见
	private String judgeResule; //评价结果(1:合格,2:不合格)
	private Long judgePerson; //评价人
	private Long judgePersonGridId;//评价人所属网格
	private Date createTime;
	private Long createUserId;
	private String createOrgCode;
	private Date updateTime;
	private String taskType;//任务类型,2:镇街级任务,1:市级任务(未兼容原情况,null也为市级任务)--add 2018/1/11
	private String status; //状态.1:有效,0:无效
	private Map<String,Object> extData;
	
	//正常就是: judgeResule不为空,且创建时间与更新时间时间差<=3天    或者   judgeResule为空,但是创建时间与当前时间差<=3天
	//过期: judgeResule不为空,且创建时间与更新时间时间差>3天    或者    judgeResule为空,但是创建时间与当前时间差>3天
	private String outOfDateFlag;//过期标识,1:正常,2:过期
	private String createTimeStart2;
	private String createTimeEnd2;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getJudgeComment() {
		return judgeComment;
	}
	public void setJudgeComment(String judgeComment) {
		this.judgeComment = judgeComment;
	}
	public String getJudgeResule() {
		return judgeResule;
	}
	public void setJudgeResule(String judgeResule) {
		this.judgeResule = judgeResule;
	}
	public Long getJudgePerson() {
		return judgePerson;
	}
	public void setJudgePerson(Long judgePerson) {
		this.judgePerson = judgePerson;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTimeStart2() {
		return createTimeStart2;
	}
	public void setCreateTimeStart2(String createTimeStart2) {
		this.createTimeStart2 = createTimeStart2;
	}
	public String getCreateTimeEnd2() {
		return createTimeEnd2;
	}
	public void setCreateTimeEnd2(String createTimeEnd2) {
		this.createTimeEnd2 = createTimeEnd2;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getOutOfDateFlag() {
		return outOfDateFlag;
	}
	public void setOutOfDateFlag(String outOfDateFlag) {
		this.outOfDateFlag = outOfDateFlag;
	}
	public Map<String, Object> getExtData() {
		return extData;
	}
	public void setExtData(Map<String, Object> extData) {
		this.extData = extData;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateOrgCode() {
		return createOrgCode;
	}
	public void setCreateOrgCode(String createOrgCode) {
		this.createOrgCode = createOrgCode;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public Long getJudgePersonGridId() {
		return judgePersonGridId;
	}
	public void setJudgePersonGridId(Long judgePersonGridId) {
		this.judgePersonGridId = judgePersonGridId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

}