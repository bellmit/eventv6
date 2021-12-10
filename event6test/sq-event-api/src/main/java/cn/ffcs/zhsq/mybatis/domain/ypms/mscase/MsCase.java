package cn.ffcs.zhsq.mybatis.domain.ypms.mscase;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 延平民生案件表模块bo对象
 * @Author: zhangzhenhai
 * @Date: 04-16 09:49:27
 * @Copyright: 2018 福富软件
 */
public class MsCase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5777971376556004655L;
	private Long caseId; //主键ID
	private String caseTitle; //案件标题
	private String caseNo; //案件编号
	private Long caseNoSerial; //案件编号流水号
	private Long relaCaseId; //重复案件ID
	private String relaCaseNo; //重复案件编号
	private String handleWay; //处理方式;01：直接处理；02：12345平台；03：民生110
	private String caseType; //案件类型；01：意见投诉；02：生活服务；03：困难求助；04：信息咨询
	private Long caseFromId; //案件归属地ID
	private String caseFromCode; //案件归属地Code
	private String caseDesc; //案件描述
	private Double pTPrice; //专业化队伍原价
	private Double pTDiscountPrice; //专业化队伍折后价
	private Double pTServicePrice; //材料及服务费
	private String returnRusult; //回访情况
	private String satisfaction; //01：满意；02：基本满意；03：不满意
	private String remark; //备注
	private String handleStatus; //00：草稿、01：办理中、02：回访中、03：已结案（已归档）、04：委办局驳回
	private String handleDeptName; //办理部门名称
	private String callinNum; //主叫号码
	private String calledNum; //被叫号码
	private Date callinTime; //呼叫时间
	private String callinType; //01：呼入
	private String callinReult; //01：成功
	private String customerName; //客户姓名
	private String customerGender; //01：男；02：女
	private Long receiverId; //接听人员ID
	private String receiverName; //接听人员姓名
	private Long createId; //创建人
	private Long createOrgId; //创建组织Id
	private String createOrgCode; //创建组织code
	private Date createTime; //创建时间
	private String delFlag; //0：删除；1：有效
	
	private Long[] attachmentId;//附件ID集合
	private String callinTimeStr; //呼叫时间Str
	private String createTimeStr; //创建时间Str
	private String ldIdArrStr; //添加的联动单位idStr
	private String caseFromName; //案件归属地名称
	private String rdhStatus; //处理过程表状态
	private String rdStatus; //派发单位表状态
	
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public Long getRelaCaseId() {
		return relaCaseId;
	}
	public void setRelaCaseId(Long relaCaseId) {
		this.relaCaseId = relaCaseId;
	}
	public String getRelaCaseNo() {
		return relaCaseNo;
	}
	public void setRelaCaseNo(String relaCaseNo) {
		this.relaCaseNo = relaCaseNo;
	}
	public String getHandleWay() {
		return handleWay;
	}
	public void setHandleWay(String handleWay) {
		this.handleWay = handleWay;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public Long getCaseFromId() {
		return caseFromId;
	}
	public void setCaseFromId(Long caseFromId) {
		this.caseFromId = caseFromId;
	}
	public String getCaseFromCode() {
		return caseFromCode;
	}
	public void setCaseFromCode(String caseFromCode) {
		this.caseFromCode = caseFromCode;
	}
	public String getCaseDesc() {
		return caseDesc;
	}
	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}
	public String getReturnRusult() {
		return returnRusult;
	}
	public void setReturnRusult(String returnRusult) {
		this.returnRusult = returnRusult;
	}
	public String getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	public String getHandleDeptName() {
		return handleDeptName;
	}
	public void setHandleDeptName(String handleDeptName) {
		this.handleDeptName = handleDeptName;
	}
	
	public String getCalledNum() {
		return calledNum;
	}
	public void setCalledNum(String calledNum) {
		this.calledNum = calledNum;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerGender() {
		return customerGender;
	}
	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public String getCallinNum() {
		return callinNum;
	}
	public void setCallinNum(String callinNum) {
		this.callinNum = callinNum;
	}
	public Date getCallinTime() {
		return callinTime;
	}
	public void setCallinTime(Date callinTime) {
		this.callinTime = callinTime;
	}
	public String getCallinType() {
		return callinType;
	}
	public void setCallinType(String callinType) {
		this.callinType = callinType;
	}
	public String getCallinReult() {
		return callinReult;
	}
	public void setCallinReult(String callinReult) {
		this.callinReult = callinReult;
	}
	public String getCallinTimeStr() {
		return callinTimeStr;
	}
	public void setCallinTimeStr(String callinTimeStr) {
		this.callinTimeStr = callinTimeStr;
	}
	public Long getCaseNoSerial() {
		return caseNoSerial;
	}
	public void setCaseNoSerial(Long caseNoSerial) {
		this.caseNoSerial = caseNoSerial;
	}
	public Double getpTPrice() {
		return pTPrice;
	}
	public void setpTPrice(Double pTPrice) {
		this.pTPrice = pTPrice;
	}
	public Double getpTDiscountPrice() {
		return pTDiscountPrice;
	}
	public void setpTDiscountPrice(Double pTDiscountPrice) {
		this.pTDiscountPrice = pTDiscountPrice;
	}
	public Double getpTServicePrice() {
		return pTServicePrice;
	}
	public void setpTServicePrice(Double pTServicePrice) {
		this.pTServicePrice = pTServicePrice;
	}
	public String getLdIdArrStr() {
		return ldIdArrStr;
	}
	public void setLdIdArrStr(String ldIdArrStr) {
		this.ldIdArrStr = ldIdArrStr;
	}
	public Long[] getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long[] attachmentId) {
		this.attachmentId = attachmentId;
	}
	public Long getCreateOrgId() {
		return createOrgId;
	}
	public void setCreateOrgId(Long createOrgId) {
		this.createOrgId = createOrgId;
	}
	public String getCreateOrgCode() {
		return createOrgCode;
	}
	public void setCreateOrgCode(String createOrgCode) {
		this.createOrgCode = createOrgCode;
	}
	public String getCaseFromName() {
		return caseFromName;
	}
	public void setCaseFromName(String caseFromName) {
		this.caseFromName = caseFromName;
	}
	public String getRdhStatus() {
		return rdhStatus;
	}
	public void setRdhStatus(String rdhStatus) {
		this.rdhStatus = rdhStatus;
	}
	public String getRdStatus() {
		return rdStatus;
	}
	public void setRdStatus(String rdStatus) {
		this.rdStatus = rdStatus;
	}
	

}