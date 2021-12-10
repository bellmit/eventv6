package cn.ffcs.zhsq.mybatis.domain.gdpersionsendflow;

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.zhsq.mybatis.domain.common.Page;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 网格员协助送达流程模块bo对象
 * @Author: zhangch
 * @Date: 10-16 10:14:20
 * @table: 表信息描述 T_GD_PERSION_SEND_FLOW 网格员协助送达流程  网格员协助送达流程,序SEQ_GD_PERSION_SEND_FLOW  序列SEQ_GD_PERSION_SEND_FLOW
 * @Copyright: 2019 福富软件
 */
public class GdPersionSendFlow extends Page implements Serializable {
	private static final long serialVersionUID = -4693129339533720579L;

	private Long sendId; //主键 	NUMBER(16)
	private Long lastSendId; //上次送达的主键 	NUMBER(16)
	private String flowName; //流程名称 	VARCHAR2(200)
	private Date publishDate; //发起日期 	DATE
	private Date limitDate; //办理截止日期 	DATE
	private String receiveName; //送达人姓名 	VARCHAR2(20)
	private String receiveAddr; //送达人地址 	VARCHAR2(200)
	private String receiveState; //送达状态 	CHAR(1) 0 未知  1 已送达、2 未送达
	private String status; //流程状态 	CHAR(2) 01草稿、02流转中、03已送达、04未送达、05归档
	private String currentCode; //当前办理组织编码 	VARCHAR2(24)
	private String currentCodeCn; //当前办理组织名称 	VARCHAR2(50)
	private String currentNode; //当前办理节点 	VARCHAR2(20)
	private String currentHandler; //当前办理人 	VARCHAR2(500)
	private String orgCode; //所属组织 	VARCHAR2(24)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(16)
	private Date updateTime; //更新时间 	DATE
	private String isValid; //数据有效性 	CHAR(1)
	private String remake; //备注 	VARCHAR2(200)
	private String advice; //网格员反馈意见 	VARCHAR2(500)

	//非表字段属性
	private Long[] attachmentId;//附件ID集合
	private List<Attachment> attList;//附件对象集合
	private Integer limitDateNum; //期限天数
	private Integer surplusDateNum; //剩余天数
	private String publishDateStr;//发起日期格式化
	private String limitDateStr;//限制日期格式化
	private String statusStr;//状态格式化
	private String receiveStateStr;//送达状态格式化
	private String currLink; //当前所处的环节
	//用于做发布时间的条件过滤
	private String sendDateStart;//开始时间字符串
	private String sendDateEnd;//结束时间字符串

	private String sendType;//协作类型
	private String sendTypeCN;//协作类型
	private String expound;//详细说明


	private String plaintiff;//原告
	private String defendant;//被告
	private String caseNo;//案号
	private String caseReason;//案由

	public String getPlaintiff() {
		return plaintiff;
	}

	public void setPlaintiff(String plaintiff) {
		this.plaintiff = plaintiff;
	}

	public String getDefendant() {
		return defendant;
	}

	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseReason() {
		return caseReason;
	}

	public void setCaseReason(String caseReason) {
		this.caseReason = caseReason;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getSendTypeCN() {
		return sendTypeCN;
	}

	public void setSendTypeCN(String sendTypeCN) {
		this.sendTypeCN = sendTypeCN;
	}

	public String getExpound() {
		return expound;
	}

	public void setExpound(String expound) {
		this.expound = expound;
	}

	public GdPersionSendFlow (){

	}

	public GdPersionSendFlow (GdPersionSendFlow bo){
		this.limitDate = bo.getLimitDate();
		this.publishDate = bo.getPublishDate();
		this.flowName = bo.getFlowName();
		this.receiveName = bo.getReceiveName();
		this.orgCode = bo.getOrgCode();
		this.receiveAddr = bo.getReceiveAddr();
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getPublishDateStr() {
		return publishDateStr;
	}

	public void setPublishDateStr(String publishDateStr) {
		this.publishDateStr = publishDateStr;
	}

	public String getLimitDateStr() {
		return limitDateStr;
	}

	public void setLimitDateStr(String limitDateStr) {
		this.limitDateStr = limitDateStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getReceiveStateStr() {
		return receiveStateStr;
	}

	public void setReceiveStateStr(String receiveStateStr) {
		this.receiveStateStr = receiveStateStr;
	}

	public Integer getLimitDateNum() {
		return limitDateNum;
	}
	public void setLimitDateNum(Integer limitDateNum) {
		this.limitDateNum = limitDateNum;
	}
	public Long[] getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long[] attachmentId) {
		this.attachmentId = attachmentId;
	}
	public List<Attachment> getAttList() {
		return attList;
	}
	public void setAttList(List<Attachment> attList) {
		this.attList = attList;
	}
	public Long getSendId() {  //主键
		return sendId;
	}
	public void setSendId(Long sendId) { //主键
		this.sendId = sendId;
	}
	public Long getLastSendId() {  //上次送达的主键
		return lastSendId;
	}
	public void setLastSendId(Long lastSendId) { //上次送达的主键
		this.lastSendId = lastSendId;
	}
	public String getFlowName() {  //流程名称
		return flowName;
	}
	public void setFlowName(String flowName) { //流程名称
		this.flowName = flowName;
	}
	public Date getPublishDate() {  //发起日期
		return publishDate;
	}
	public void setPublishDate(Date publishDate) { //发起日期
		this.publishDate = publishDate;
	}
	public Date getLimitDate() {  //办理截止日期
		return limitDate;
	}
	public void setLimitDate(Date limitDate) { //办理截止日期
		this.limitDate = limitDate;
	}
	public String getReceiveName() {  //送达人姓名
		return receiveName;
	}
	public void setReceiveName(String receiveName) { //送达人姓名
		this.receiveName = receiveName;
	}
	public String getReceiveAddr() {  //送达人地址
		return receiveAddr;
	}
	public void setReceiveAddr(String receiveAddr) { //送达人地址
		this.receiveAddr = receiveAddr;
	}
	public String getReceiveState() {  //送达状态
		return receiveState;
	}
	public void setReceiveState(String receiveState) { //送达状态
		this.receiveState = receiveState;
	}
	public String getStatus() {  //流程状态
		return status;
	}
	public void setStatus(String status) { //流程状态
		this.status = status;
	}
	public String getCurrentCode() {  //当前办理组织编码
		return currentCode;
	}
	public void setCurrentCode(String currentCode) { //当前办理组织编码
		this.currentCode = currentCode;
	}
	public String getCurrentCodeCn() {  //当前办理组织名称
		return currentCodeCn;
	}
	public void setCurrentCodeCn(String currentCodeCn) { //当前办理组织名称
		this.currentCodeCn = currentCodeCn;
	}
	public String getCurrentNode() {  //当前办理节点
		return currentNode;
	}
	public void setCurrentNode(String currentNode) { //当前办理节点
		this.currentNode = currentNode;
	}
	public String getCurrentHandler() {  //当前办理人
		return currentHandler;
	}
	public void setCurrentHandler(String currentHandler) { //当前办理人
		this.currentHandler = currentHandler;
	}
	public String getOrgCode() {  //所属组织
		return orgCode;
	}
	public void setOrgCode(String orgCode) { //所属组织
		this.orgCode = orgCode;
	}
	public Long getCreator() {  //创建人
		return creator;
	}
	public void setCreator(Long creator) { //创建人
		this.creator = creator;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getUpdator() {  //更新人
		return updator;
	}
	public void setUpdator(Long updator) { //更新人
		this.updator = updator;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public String getIsValid() {  //数据有效性
		return isValid;
	}
	public void setIsValid(String isValid) { //数据有效性
		this.isValid = isValid;
	}
	public String getRemake() {  //备注
		return remake;
	}
	public void setRemake(String remake) { //备注
		this.remake = remake;
	}

	public String getCurrLink() {
		return currLink;
	}

	public void setCurrLink(String currLink) {
		this.currLink = currLink;
	}

	public String getSendDateStart() {
		return sendDateStart;
	}

	public void setSendDateStart(String sendDateStart) {
		this.sendDateStart = sendDateStart;
	}

	public String getSendDateEnd() {
		return sendDateEnd;
	}

	public void setSendDateEnd(String sendDateEnd) {
		this.sendDateEnd = sendDateEnd;
	}

	public Integer getSurplusDateNum() {
		return surplusDateNum;
	}

	public void setSurplusDateNum(Integer surplusDateNum) {
		this.surplusDateNum = surplusDateNum;
	}

	



}