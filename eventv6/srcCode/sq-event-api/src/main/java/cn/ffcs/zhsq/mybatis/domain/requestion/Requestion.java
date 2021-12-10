package cn.ffcs.zhsq.mybatis.domain.requestion;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 诉求表模块bo对象
 * @Author: caiby
 * @Date: 03-12 08:45:59
 * @Copyright: 2018 福富软件
 */
public class Requestion implements Serializable {

	private Long reqId; //主键
	private Long reqObjId; //诉求对象ID
	private String reqObjCode; //诉求企业code
	private String reqObjName; //诉求对象名称
	private String linkMan; //联系人
	private String linkTel; //联系方式
	private String code;
	private String title;
	private String type;
	private String content;
	private String desc;
	private Date expectTime;
	private String status;//(0,1)
	private Long userId;
	private String userName;
	private Date creatTime;
	private String source;//(1手机,2pc)
	private String state;//环节状态（1.受理中，2.单位处理，3.退回发起人，4.归档待评，5已评价）
	private String childState;//环节状态（1.有退回，2.有待评价，3都有）99空
	private Long keyId;//关联网站群表单ID
	private String visit;//回访信息
	private String satisfaction;//满意度
	private Long orgId;//发起人组织
	private String orgName;//发起人组织
	
	private Long wfDbid_;
	private String wfActivityName_;
	private String wfStatus;
	private Long wfInstanceId;
	private String wfCurOrg;
	private String tasktype;//(1主任务，2子任务)

	private String creatTimeStr;
	private String typeStr;
	private String expectTimeStr;
	private String overTimeStatus;
	
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public Long getReqObjId() {
		return reqObjId;
	}
	public void setReqObjId(Long reqObjId) {
		this.reqObjId = reqObjId;
	}
	public String getReqObjName() {
		return reqObjName;
	}
	public void setReqObjName(String reqObjName) {
		this.reqObjName = reqObjName;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getExpectTime() {
		return expectTime;
	}
	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public String getTasktype() {
		return tasktype;
	}
	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	public String getCreatTimeStr() {
		return creatTimeStr;
	}
	public void setCreatTimeStr(String creatTimeStr) {
		this.creatTimeStr = creatTimeStr;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getExpectTimeStr() {
		return expectTimeStr;
	}
	public void setExpectTimeStr(String expectTimeStr) {
		this.expectTimeStr = expectTimeStr;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Long getKeyId() {
		return keyId;
	}
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}
//	public List<Attachment> getFileList() {
//		return fileList;
//	}
//	public void setFileList(List<Attachment> fileList) {
//		this.fileList = fileList;
//	}
	public String getWfCurOrg() {
		return wfCurOrg;
	}
	public void setWfCurOrg(String wfCurOrg) {
		this.wfCurOrg = wfCurOrg;
	}
	public String getChildState() {
		return childState;
	}
	public void setChildState(String childState) {
		this.childState = childState;
	}
	public String getReqObjCode() {
		return reqObjCode;
	}
	public void setReqObjCode(String reqObjCode) {
		this.reqObjCode = reqObjCode;
	}
	public String getVisit() {
		return visit;
	}
	public void setVisit(String visit) {
		this.visit = visit;
	}
	public String getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
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
	public String getOverTimeStatus() {
		return overTimeStatus;
	}
	public void setOverTimeStatus(String overTimeStatus) {
		this.overTimeStatus = overTimeStatus;
	}
	

}