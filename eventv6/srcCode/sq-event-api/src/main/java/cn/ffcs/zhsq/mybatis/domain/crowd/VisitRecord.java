package cn.ffcs.zhsq.mybatis.domain.crowd;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitRecord extends BaseCrowdInfo{

	private static final long serialVersionUID = 1L;
	
	private Long visitId;//主键   VISIT_ID
	
	private String criminalFacts;//近况简述 CRIMINAL_FACTS  对应“旧版走访记录”
	
	private String visitedType;//受访人员类型 VISITED_TYPE
	
	private Date visitTime;//走访时间  VISIT_TIME
	
	private String gridCode;//网格编码  GRID_CODE //这个字段没用
	
	private String gridCodeName;//网格名称
	
	private String talkContent;//谈话内容  TALK_CONTENT、交谈内容
	
	private String visitCause;//走访原因  VISIT_CAUSE
	
	private String visitForm;//走访形式  VISIT_FORM
	
	private String visitEffect;//走访效果  VISIT_EFFECT
	
	private String visitPhotoUrl;//走访图片  VISIT_PHOTOURL
	
	

	private String recentState;//近期动态 RECENT_STATE
	
	private String measures;//采取措施  MEASURES
	
	private String visitStatus;//状态 001  VISIT_STATUS
	
	private String remarks;//备注 REMARKS
	
	private String visitName;//走访人员      对应“旧版走访人”
	
	//
	private String visitTimeStr;//走访时间的文本形式
	private String visitedTypeStr;
	private String visitFormStr;
	private String visitEffectStr;
	private String recentStateStr;
	private String birthdayStr;
	
	// 石狮需求新增字段2013.12.20
	private Long importId; // 公共信息化重点人群表ID
	
	
	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	public String getBirthdayStr() {
		return birthdayStr;
	}

	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}

	public String getRecentStateStr() {
		return recentStateStr;
	}

	public void setRecentStateStr(String recentStateStr) {
		this.recentStateStr = recentStateStr;
	}

	public String getGridCodeName() {
		return gridCodeName;
	}

	public void setGridCodeName(String gridCodeName) {
		this.gridCodeName = gridCodeName;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

	public String getVisitedTypeStr() {
		return visitedTypeStr;
	}

	public void setVisitedTypeStr(String visitedTypeStr) {
		this.visitedTypeStr = visitedTypeStr;
	}

	public String getVisitFormStr() {
		return visitFormStr;
	}

	public void setVisitFormStr(String visitFormStr) {
		this.visitFormStr = visitFormStr;
	}

	public String getVisitEffectStr() {
		return visitEffectStr;
	}

	public void setVisitEffectStr(String visitEffectStr) {
		this.visitEffectStr = visitEffectStr;
	}
	
	public String getVisitPhotoUrl() {
		return visitPhotoUrl;
	}

	public void setVisitPhotoUrl(String visitPhotoUrl) {
		this.visitPhotoUrl = visitPhotoUrl;
	}
	
	public String getVisitTimeStr() {
		return visitTimeStr;
	}

	public void setVisitTimeStr(String visitTimeStr) {
		this.visitTimeStr = visitTimeStr;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getCriminalFacts() {
		return criminalFacts;
	}

	public void setCriminalFacts(String criminalFacts) {
		this.criminalFacts = criminalFacts;
	}

	public String getVisitedType() {
		return visitedType;
	}

	public void setVisitedType(String visitedType) {
		this.visitedType = visitedType;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
		if(visitTime != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.visitTimeStr = format.format(this.visitTime);
		}
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getTalkContent() {
		return talkContent;
	}

	public void setTalkContent(String talkContent) {
		talkContent = talkContent.replaceAll("\'", "");
		this.talkContent = talkContent;
	}

	public String getVisitCause() {
		return visitCause;
	}

	public void setVisitCause(String visitCause) {
		this.visitCause = visitCause;
	}

	public String getVisitForm() {
		return visitForm;
	}

	public void setVisitForm(String visitForm) {
		this.visitForm = visitForm;
	}

	public String getVisitEffect() {
		return visitEffect;
	}

	public void setVisitEffect(String visitEffect) {
		this.visitEffect = visitEffect;
	}

	public String getRecentState() {
		return recentState;
	}

	public void setRecentState(String recentState) {
		this.recentState = recentState;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getImportId() {
		return importId;
	}

	public void setImportId(Long importId) {
		this.importId = importId;
	}
}
