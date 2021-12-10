package cn.ffcs.zhsq.mybatis.domain.requestion;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 诉求联动单位模块bo对象
 * @Author: caiby
 * @Date: 03-12 10:09:52
 * @Copyright: 2018 福富软件
 */
public class ReqLink implements Serializable {

	private Long rluId; //主键
	private Long linkageUnitId; //联动单位ID
	private Long reqId; //诉求ID
	private String overview;
	private String visit;
	private String satisfaction;
	private Long userId;
	private String userName;
	private Date creatTime;
	private String status;
	private String linkageUnitName;
	private Date updateTime;
	private String state;//环节状态（6.已评价7.单位办理中，8单位退回，9.归档）
	
	private String limitDate;//处理期限LIMITDATE
	private String overTime;//截止日期OVERTIME
	
	private String wfStatus;
	private Long wfInstanceId;
	private String updateTimeStr;
	
	private String linkMan; //联络员
	private String linkManTel; //联络员联系方式
	private String leaderName; //分管领导
	private String leaderTel; //分管领导联系方式

	public ReqLink() {
		super();
	}
	
	public ReqLink(Long linkageUnitId,String linkageUnitName,Long reqId,Long userId,
			String userName,String limitDate,String overTime){
		super();
		this.linkageUnitId = linkageUnitId;
		this.linkageUnitName = linkageUnitName;
		this.reqId = reqId;
		this.userId = userId;
		this.userName = userName;
		this.creatTime = new Date();
		this.status = "1";
		this.state = "7";
		this.limitDate = limitDate;
		this.overTime = overTime;
	}
	
	public Long getRluId() {
		return rluId;
	}
	public void setRluId(Long rluId) {
		this.rluId = rluId;
	}
	public Long getLinkageUnitId() {
		return linkageUnitId;
	}
	public void setLinkageUnitId(Long linkageUnitId) {
		this.linkageUnitId = linkageUnitId;
	}
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getLinkageUnitName() {
		return linkageUnitName;
	}

	public void setLinkageUnitName(String linkageUnitName) {
		this.linkageUnitName = linkageUnitName;
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

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkManTel() {
		return linkManTel;
	}

	public void setLinkManTel(String linkManTel) {
		this.linkManTel = linkManTel;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getLeaderTel() {
		return leaderTel;
	}

	public void setLeaderTel(String leaderTel) {
		this.leaderTel = leaderTel;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	
	

}