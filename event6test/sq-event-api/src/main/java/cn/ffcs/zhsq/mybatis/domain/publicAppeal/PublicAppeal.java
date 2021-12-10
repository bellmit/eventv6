package cn.ffcs.zhsq.mybatis.domain.publicAppeal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 公众诉求模块bo对象
 * @Author: zhongshm
 * @Date: 09-01 15:32:01
 * @Copyright: 2017 福富软件
 */
public class PublicAppeal implements Serializable {

	private Long appealId;
	private Long outId;
	private String appealNo;
	private String appealTitle;
	private String appealCatalog;
	private String appealCatalogStr;
	private String content;
	private String source;
	private String sourceStr;
	private Date appealTime;
	private String appealTimeStr;
	private String appealStatus;
	private String handleSit;
	private String handleSitStr;
	private String handleRs;
	private String userName;
	private String phone;
	private String isPub;
	private Long createUserId;
	private Date createTime;
	private Long updateUserId;
	private Date updateTime;
	private String status;
	private String orgCode;
	private String orgEntityPath;

	private String rep_org_name;
	private String rep_time;

	private Long eventId;

	//
	private List<Map<String, Object>> attrs;


	
	public String getSourceStr() {
		return sourceStr;
	}
	public void setSourceStr(String sourceStr) {
		this.sourceStr = sourceStr;
	}
	public String getAppealCatalogStr() {
		return appealCatalogStr;
	}
	public void setAppealCatalogStr(String appealCatalogStr) {
		this.appealCatalogStr = appealCatalogStr;
	}
	public String getHandleSitStr() {
		return handleSitStr;
	}
	public void setHandleSitStr(String handleSitStr) {
		this.handleSitStr = handleSitStr;
	}
	public Long getAppealId() {
		return appealId;
	}
	public void setAppealId(Long appealId) {
		this.appealId = appealId;
	}
	public Long getOutId() {
		return outId;
	}
	public void setOutId(Long outId) {
		this.outId = outId;
	}
	public String getAppealNo() {
		return appealNo;
	}
	public void setAppealNo(String appealNo) {
		this.appealNo = appealNo;
	}
	public String getAppealTitle() {
		return appealTitle;
	}
	public void setAppealTitle(String appealTitle) {
		this.appealTitle = appealTitle;
	}
	public String getAppealCatalog() {
		return appealCatalog;
	}
	public void setAppealCatalog(String appealCatalog) {
		this.appealCatalog = appealCatalog;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getAppealTime() {
		return appealTime;
	}
	public void setAppealTime(Date appealTime) {
		this.appealTime = appealTime;
	}
	public String getAppealStatus() {
		return appealStatus;
	}
	public void setAppealStatus(String appealStatus) {
		this.appealStatus = appealStatus;
	}
	public String getHandleSit() {
		return handleSit;
	}
	public void setHandleSit(String handleSit) {
		this.handleSit = handleSit;
	}
	public String getHandleRs() {
		return handleRs;
	}
	public void setHandleRs(String handleRs) {
		this.handleRs = handleRs;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIsPub() {
		return isPub;
	}
	public void setIsPub(String isPub) {
		this.isPub = isPub;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public String getAppealTimeStr() {
		return appealTimeStr;
	}

	public void setAppealTimeStr(String appealTimeStr) {
		this.appealTimeStr = appealTimeStr;
	}

	public List<Map<String, Object>> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<Map<String, Object>> attrs) {
		this.attrs = attrs;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgEntityPath() {
		return orgEntityPath;
	}

	public void setOrgEntityPath(String orgEntityPath) {
		this.orgEntityPath = orgEntityPath;
	}

	public String getRep_time() {
		return rep_time;
	}

	public void setRep_time(String rep_time) {
		this.rep_time = rep_time;
	}

	public String getRep_org_name() {
		return rep_org_name;
	}

	public void setRep_org_name(String rep_org_name) {
		this.rep_org_name = rep_org_name;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}