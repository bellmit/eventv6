package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 南安矛盾纠纷模块bo对象
 * @Author: 黄文斌
 * @Date: 06-03 09:05:22
 * @table: 表信息描述 T_ZZ_DISPUTE_MEDIATION_NA     序列SEQ_T_ZZ_DISPUTE_MEDIATION_NA
 * @Copyright: 2020 福富软件
 */
public class MediationNa implements Serializable {
 

	/**
	 * 
	 */
	private static final long serialVersionUID = 4661569220438410215L;
	private Long id; // 	NUMBER(22)
	private Long pid; // 	NUMBER(22)
	private Long flowId; // 	NUMBER(22)
	private String flowName; // 	VARCHAR2(255)
	private String no; // 	VARCHAR2(30)
	private String createId; // 	VARCHAR2(30)
	private String createName; // 	VARCHAR2(255)
	private String createGroup; // 	VARCHAR2(255)
	private String createGroupNodepath; // 	VARCHAR2(255)
	private Date createTime; // 	DATE
	private String currentStep; // 	VARCHAR2(255)
	private String currentStepName; // 	VARCHAR2(255)
	private String currentRole; // 	VARCHAR2(255)
	private String currentUserid; // 	VARCHAR2(999)
	private String currentUsername; // 	VARCHAR2(999)
	private String currentGroupNodepath; // 	VARCHAR2(255)
	private String viewUserid; // 	VARCHAR2(999)
	private String viewUsername; // 	VARCHAR2(999)
	private String copyUserid; // 	VARCHAR2(255)
	private String lastUserid; // 	VARCHAR2(30)
	private String lastUsername; // 	VARCHAR2(255)
	private String lastUsergroup; // 	VARCHAR2(255)
	private String lastStep; // 	VARCHAR2(255)
	private String lastAction; // 	VARCHAR2(255)
	private String lastComment; // 	VARCHAR2(255)
	private String dealUserids; // 	VARCHAR2(1000)
	private String dealUsernames; // 	VARCHAR2(1000)
	private Date dealtime; // 	DATE
	private Date finishtime; // 	DATE
	private String status; // 	VARCHAR2(30)
	private Long statusflag; // 	NUMBER(22)
	private Long deleteflag; // 	NUMBER(22)
	private String mediator; // 	VARCHAR2(255)
	private String fullcontent; // 	BLOB
	private String attachment; // 	BLOB
	private String from; // 	VARCHAR2(255)
	private String locality; // 	VARCHAR2(255)
	private String acceptInstitution; // 	VARCHAR2(255)
	private String acceptPlace; // 	VARCHAR2(255)
	private String party; // 	BLOB
	private String partyName; // 	VARCHAR2(255)
	private Date acceptDate; // 	DATE
	private String dockingType; // 	VARCHAR2(255)
	private String disputeType; // 	VARCHAR2(255)
	private String statement; // 	BLOB
	private String caseNoType; // 	VARCHAR2(30)
	private String caseNo; // 	VARCHAR2(30)
	private String selfAssessment; // 	VARCHAR2(1000)
	private String leaderAssessment; // 	VARCHAR2(255)
	private String leaderId; // 	VARCHAR2(30)
	private String leaderName; // 	VARCHAR2(255)
	private Long changeCount; // 	NUMBER(22)
	private String dockingNo; // 	VARCHAR2(255)
	private String criminalBond; // 	VARCHAR2(255)
	private String verdictResult; // 	BLOB
	private String infoorgcode; //地域
	private String type; 


	
	public String getType_() {
		return type;
	}
	public void setType(String type_) {
		this.type = type_;
	}
	public String getInfoorgcode() {
		return infoorgcode;
	}
	public void setInfoorgcode(String infoorgcode) {
		this.infoorgcode = infoorgcode;
	}
	public Long getId() {  
		return id;
	}
	public void setId(Long id) { 
		this.id = id;
	}
	public Long getPid() {  
		return pid;
	}
	public void setPid(Long pid) { 
		this.pid = pid;
	}
	public Long getFlowId() {  
		return flowId;
	}
	public void setFlowId(Long flowId) { 
		this.flowId = flowId;
	}
	public String getFlowName() {  
		return flowName;
	}
	public void setFlowName(String flowName) { 
		this.flowName = flowName;
	}
	public String getNo() {  
		return no;
	}
	public void setNo(String no) { 
		this.no = no;
	}
	public String getCreateId() {  
		return createId;
	}
	public void setCreateId(String createId) { 
		this.createId = createId;
	}
	public String getCreateName() {  
		return createName;
	}
	public void setCreateName(String createName) { 
		this.createName = createName;
	}
	public String getCreateGroup() {  
		return createGroup;
	}
	public void setCreateGroup(String createGroup) { 
		this.createGroup = createGroup;
	}
	public String getCreateGroupNodepath() {  
		return createGroupNodepath;
	}
	public void setCreateGroupNodepath(String createGroupNodepath) { 
		this.createGroupNodepath = createGroupNodepath;
	}
	public Date getCreateTime() {  
		return createTime;
	}
	public void setCreateTime(Date createTime) { 
		this.createTime = createTime;
	}
	public String getCurrentStep() {  
		return currentStep;
	}
	public void setCurrentStep(String currentStep) { 
		this.currentStep = currentStep;
	}
	public String getCurrentStepName() {  
		return currentStepName;
	}
	public void setCurrentStepName(String currentStepName) { 
		this.currentStepName = currentStepName;
	}
	public String getCurrentRole() {  
		return currentRole;
	}
	public void setCurrentRole(String currentRole) { 
		this.currentRole = currentRole;
	}
	public String getCurrentUserid() {  
		return currentUserid;
	}
	public void setCurrentUserid(String currentUserid) { 
		this.currentUserid = currentUserid;
	}
	public String getCurrentUsername() {  
		return currentUsername;
	}
	public void setCurrentUsername(String currentUsername) { 
		this.currentUsername = currentUsername;
	}
	public String getCurrentGroupNodepath() {  
		return currentGroupNodepath;
	}
	public void setCurrentGroupNodepath(String currentGroupNodepath) { 
		this.currentGroupNodepath = currentGroupNodepath;
	}
	public String getViewUserid() {  
		return viewUserid;
	}
	public void setViewUserid(String viewUserid) { 
		this.viewUserid = viewUserid;
	}
	public String getViewUsername() {  
		return viewUsername;
	}
	public void setViewUsername(String viewUsername) { 
		this.viewUsername = viewUsername;
	}
	public String getCopyUserid() {  
		return copyUserid;
	}
	public void setCopyUserid(String copyUserid) { 
		this.copyUserid = copyUserid;
	}
	public String getLastUserid() {  
		return lastUserid;
	}
	public void setLastUserid(String lastUserid) { 
		this.lastUserid = lastUserid;
	}
	public String getLastUsername() {  
		return lastUsername;
	}
	public void setLastUsername(String lastUsername) { 
		this.lastUsername = lastUsername;
	}
	public String getLastUsergroup() {  
		return lastUsergroup;
	}
	public void setLastUsergroup(String lastUsergroup) { 
		this.lastUsergroup = lastUsergroup;
	}
	public String getLastStep() {  
		return lastStep;
	}
	public void setLastStep(String lastStep) { 
		this.lastStep = lastStep;
	}
	public String getLastAction() {  
		return lastAction;
	}
	public void setLastAction(String lastAction) { 
		this.lastAction = lastAction;
	}
	public String getLastComment() {  
		return lastComment;
	}
	public void setLastComment(String lastComment) { 
		this.lastComment = lastComment;
	}
	public String getDealUserids() {  
		return dealUserids;
	}
	public void setDealUserids(String dealUserids) { 
		this.dealUserids = dealUserids;
	}
	public String getDealUsernames() {  
		return dealUsernames;
	}
	public void setDealUsernames(String dealUsernames) { 
		this.dealUsernames = dealUsernames;
	}
	public Date getDealtime() {  
		return dealtime;
	}
	public void setDealtime(Date dealtime) { 
		this.dealtime = dealtime;
	}
	public Date getFinishtime() {  
		return finishtime;
	}
	public void setFinishtime(Date finishtime) { 
		this.finishtime = finishtime;
	}
	public String getStatus() {  
		return status;
	}
	public void setStatus(String status) { 
		this.status = status;
	}
	public Long getStatusflag() {  
		return statusflag;
	}
	public void setStatusflag(Long statusflag) { 
		this.statusflag = statusflag;
	}
	public Long getDeleteflag() {  
		return deleteflag;
	}
	public void setDeleteflag(Long deleteflag) { 
		this.deleteflag = deleteflag;
	}
	public String getMediator() {  
		return mediator;
	}
	public void setMediator(String mediator) { 
		this.mediator = mediator;
	}
	public String getFullcontent() {  
		return fullcontent;
	}
	public void setFullcontent(String fullcontent) { 
		this.fullcontent = fullcontent;
	}
	public String getAttachment() {  
		return attachment;
	}
	public void setAttachment(String attachment) { 
		this.attachment = attachment;
	}
	public String getFrom() {  
		return from;
	}
	public void setFrom(String from) { 
		this.from = from;
	}
	public String getLocality() {  
		return locality;
	}
	public void setLocality(String locality) { 
		this.locality = locality;
	}
	public String getAcceptInstitution() {  
		return acceptInstitution;
	}
	public void setAcceptInstitution(String acceptInstitution) { 
		this.acceptInstitution = acceptInstitution;
	}
	public String getAcceptPlace() {  
		return acceptPlace;
	}
	public void setAcceptPlace(String acceptPlace) { 
		this.acceptPlace = acceptPlace;
	}
	public String getParty() {  
		return party;
	}
	public void setParty(String party) { 
		this.party = party;
	}
	public String getPartyName() {  
		return partyName;
	}
	public void setPartyName(String partyName) { 
		this.partyName = partyName;
	}
	public Date getAcceptDate() {  
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) { 
		this.acceptDate = acceptDate;
	}
	public String getDockingType() {  
		return dockingType;
	}
	public void setDockingType(String dockingType) { 
		this.dockingType = dockingType;
	}
	public String getDisputeType() {  
		return disputeType;
	}
	public void setDisputeType(String disputeType) { 
		this.disputeType = disputeType;
	}
	public String getStatement() {  
		return statement;
	}
	public void setStatement(String statement) { 
		this.statement = statement;
	}
	public String getCaseNoType() {  
		return caseNoType;
	}
	public void setCaseNoType(String caseNoType) { 
		this.caseNoType = caseNoType;
	}
	public String getCaseNo() {  
		return caseNo;
	}
	public void setCaseNo(String caseNo) { 
		this.caseNo = caseNo;
	}
	public String getSelfAssessment() {  
		return selfAssessment;
	}
	public void setSelfAssessment(String selfAssessment) { 
		this.selfAssessment = selfAssessment;
	}
	public String getLeaderAssessment() {  
		return leaderAssessment;
	}
	public void setLeaderAssessment(String leaderAssessment) { 
		this.leaderAssessment = leaderAssessment;
	}
	public String getLeaderId() {  
		return leaderId;
	}
	public void setLeaderId(String leaderId) { 
		this.leaderId = leaderId;
	}
	public String getLeaderName() {  
		return leaderName;
	}
	public void setLeaderName(String leaderName) { 
		this.leaderName = leaderName;
	}
	public Long getChangeCount() {  
		return changeCount;
	}
	public void setChangeCount(Long changeCount) { 
		this.changeCount = changeCount;
	}
	public String getDockingNo() {  
		return dockingNo;
	}
	public void setDockingNo(String dockingNo) { 
		this.dockingNo = dockingNo;
	}
	public String getCriminalBond() {  
		return criminalBond;
	}
	public void setCriminalBond(String criminalBond) { 
		this.criminalBond = criminalBond;
	}
	public String getVerdictResult() {  
		return verdictResult;
	}
	public void setVerdictResult(String verdictResult) { 
		this.verdictResult = verdictResult;
	}


}