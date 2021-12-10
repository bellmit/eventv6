package cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 禁毒事件，序列：SEQ_DRUG_ENFORCEMENT_ID
 * @Author: zhangls
 * @Date: 07-05 15:43:49
 * @Copyright: 2017 福富软件
 */
public class DrugEnforcementEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long 	drugEnforcementId; 		//主键，序列：SEQ_DRUG_ENFORCEMENT_ID
	private Long	ciRsId;					//人口主键  对应表T_DC_CI_RS_TOP
	private Long 	drugId; 				//吸毒人员主键 对应表T_ZZ_DRUG_RECORD
	private String	addictName;				//吸毒人员姓名
	private String 	addictIdCard;			//吸毒人员身份证号码
	private String	addictGridPath;			//吸毒人员所属网格路径
	private String	addictDrugVar;			//毒品种类 字典为D121004
	private String	addictDrugVarName;		//毒品种类字典名称
	private String	content;				//禁毒事件事件内容 字典为B164
	private String 	drugSocialSituation;	//社会毒情，字典编码B178
	private String	drugSocialSituationName;//社会毒情，字典名称
	private Date 	reportDate; 			//上报时间
	private String	reportDateStr;			//上报时间文本
	private String	reportDateStart;		//上报开始时间
	private String	reportDateEnd;			//上报结束时间
	private String 	contactUser; 			//联系人员
	private String 	contactTel; 			//联系电话
	private Date handleDate;				//处理时限
	private String handleDateStr;			//处理时限文本
	private Date finDate;					//办结时间
	private String finDateStr;				//办结时间文本
	private String 	status; 				//状态，001草稿；002 处理中；004 已处理；006无效
	private Date 	createDate; 			//创建时间
	private Long 	createUser; 			//创建人员
	private Date 	updateDate; 			//更新时间
	private Long 	updateUser; 			//更新人员

	public Long getDrugEnforcementId() {
		return drugEnforcementId;
	}
	public void setDrugEnforcementId(Long drugEnforcementId) {
		this.drugEnforcementId = drugEnforcementId;
	}
	public Long getCiRsId() {
		return ciRsId;
	}
	public void setCiRsId(Long ciRsId) {
		this.ciRsId = ciRsId;
	}
	public Long getDrugId() {
		return drugId;
	}
	public void setDrugId(Long drugId) {
		this.drugId = drugId;
	}
	public String getAddictName() {
		return addictName;
	}
	public void setAddictName(String addictName) {
		this.addictName = addictName;
	}
	public String getAddictIdCard() {
		return addictIdCard;
	}
	public void setAddictIdCard(String addictIdCard) {
		this.addictIdCard = addictIdCard;
	}
	public String getAddictGridPath() {
		return addictGridPath;
	}
	public void setAddictGridPath(String addictGridPath) {
		this.addictGridPath = addictGridPath;
	}
	public String getAddictDrugVar() {
		return addictDrugVar;
	}
	public void setAddictDrugVar(String addictDrugVar) {
		this.addictDrugVar = addictDrugVar;
	}
	public String getAddictDrugVarName() {
		return addictDrugVarName;
	}
	public void setAddictDrugVarName(String addictDrugVarName) {
		this.addictDrugVarName = addictDrugVarName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDrugSocialSituation() {
		return drugSocialSituation;
	}
	public void setDrugSocialSituation(String drugSocialSituation) {
		this.drugSocialSituation = drugSocialSituation;
	}
	public String getDrugSocialSituationName() {
		return drugSocialSituationName;
	}
	public void setDrugSocialSituationName(String drugSocialSituationName) {
		this.drugSocialSituationName = drugSocialSituationName;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportDateStr() {
		return reportDateStr;
	}
	public void setReportDateStr(String reportDateStr) {
		this.reportDateStr = reportDateStr;
	}
	public String getReportDateStart() {
		return reportDateStart;
	}
	public void setReportDateStart(String reportDateStart) {
		this.reportDateStart = reportDateStart;
	}
	public String getReportDateEnd() {
		return reportDateEnd;
	}
	public void setReportDateEnd(String reportDateEnd) {
		this.reportDateEnd = reportDateEnd;
	}
	public String getContactUser() {
		return contactUser;
	}
	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public Date getHandleDate() {
		return handleDate;
	}
	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}
	public String getHandleDateStr() {
		return handleDateStr;
	}
	public void setHandleDateStr(String handleDateStr) {
		this.handleDateStr = handleDateStr;
	}
	public Date getFinDate() {
		return finDate;
	}
	public void setFinDate(Date finDate) {
		this.finDate = finDate;
	}
	public String getFinDateStr() {
		return finDateStr;
	}
	public void setFinDateStr(String finDateStr) {
		this.finDateStr = finDateStr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}


}