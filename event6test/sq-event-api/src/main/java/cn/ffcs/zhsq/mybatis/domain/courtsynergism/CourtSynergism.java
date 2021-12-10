package cn.ffcs.zhsq.mybatis.domain.courtsynergism;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 法院协同办公模块bo对象
 * @Author: zhangch
 * @Date: 05-20 11:01:56
 * @table: 表信息描述 T_COURT_SYNERGISM 法院协同办公  SEQ_COURT_SYNERGISM  序列SEQ_COURT_SYNERGISM
 */
public class CourtSynergism implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long synergismId; //主键 	NUMBER(16)
	private String applyType; //申请类别 	VARCHAR2(2)
	private String courtName; //法官姓名 	VARCHAR2(30)
	private String department; //所属部门 	VARCHAR2(100)
	private String contactInformation; //联系方式 	VARCHAR2(13)
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private Date applyDate; //申请时间 	DATE
	private String itemDescription; //事项说明 	VARCHAR2(1600)
	private String gridCode; //所属网格 	VARCHAR2(32)
	private String gridName; //所属网格名称 	VARCHAR2(100)
	private String status; //状态 	VARCHAR2(2) 01 草稿 02 办理中 03 待评价 04 结束
	private String creatorName; //申请人姓名 	VARCHAR2(50)
	private String satisfaction; //满意度 	VARCHAR2(2)
	private String orgCode; //所属组织 	VARCHAR2(24)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新人 	NUMBER(16)
	private Date updateTime; //更新时间 	DATE
	private String isValid; //数据有效性 	CHAR(1)
	private String remake; //备注 	VARCHAR2(200)
	private String menuType; //菜单类型
	private String applyDateStr; //申请时间
	private Long curUserId;
	private Long curOrgId;

	private String applyTypeCN; //申请类别 	VARCHAR2(2)
	private String satisfactionCN; //满意度 	VARCHAR2(2)
	private String statusCN;

	private String curUserName; //当前办理人
	private String nextNodeId;
	private String nowNodeId;
	private String advice;

	private String applyDateStart;
	private String applyDateEnd;

	public String getApplyDateStart() {
		return applyDateStart;
	}

	public void setApplyDateStart(String applyDateStart) {
		this.applyDateStart = applyDateStart;
	}

	public String getApplyDateEnd() {
		return applyDateEnd;
	}

	public void setApplyDateEnd(String applyDateEnd) {
		this.applyDateEnd = applyDateEnd;
	}

	public String getNowNodeId() {
		return nowNodeId;
	}

	public void setNowNodeId(String nowNodeId) {
		this.nowNodeId = nowNodeId;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getNextNodeId() {
		return nextNodeId;
	}

	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

	public String getCurUserName() {
		return curUserName;
	}

	public void setCurUserName(String curUserName) {
		this.curUserName = curUserName;
	}

	public String getStatusCN() {
		return statusCN;
	}

	public void setStatusCN(String statusCN) {
		this.statusCN = statusCN;
	}

	public String getApplyTypeCN() {
		return applyTypeCN;
	}
	public void setApplyTypeCN(String applyTypeCN) {
		this.applyTypeCN = applyTypeCN;
	}
	public String getSatisfactionCN() {
		return satisfactionCN;
	}
	public void setSatisfactionCN(String satisfactionCN) {
		this.satisfactionCN = satisfactionCN;
	}
	public String getApplyDateStr() {
		return applyDateStr;
	}
	public void setApplyDateStr(String applyDateStr) {
		this.applyDateStr = applyDateStr;
	}
	public Long getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(Long curUserId) {
		this.curUserId = curUserId;
	}
	public Long getCurOrgId() {
		return curOrgId;
	}
	public void setCurOrgId(Long curOrgId) {
		this.curOrgId = curOrgId;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	public Long getSynergismId() {  //主键
		return synergismId;
	}
	public void setSynergismId(Long synergismId) { //主键
		this.synergismId = synergismId;
	}
	public String getApplyType() {  //申请类别
		return applyType;
	}
	public void setApplyType(String applyType) { //申请类别
		this.applyType = applyType;
	}
	public String getCourtName() {  //法官姓名
		return courtName;
	}
	public void setCourtName(String courtName) { //法官姓名
		this.courtName = courtName;
	}
	public String getDepartment() {  //所属部门
		return department;
	}
	public void setDepartment(String department) { //所属部门
		this.department = department;
	}
	public String getContactInformation() {  //联系方式
		return contactInformation;
	}
	public void setContactInformation(String contactInformation) { //联系方式
		this.contactInformation = contactInformation;
	}
	public Date getApplyDate() {  //申请时间
		return applyDate;
	}
	public void setApplyDate(Date applyDate) { //申请时间
		this.applyDate = applyDate;
	}
	public String getItemDescription() {  //事项说明
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) { //事项说明
		this.itemDescription = itemDescription;
	}
	public String getGridCode() {  //所属网格
		return gridCode;
	}
	public void setGridCode(String gridCode) { //所属网格
		this.gridCode = gridCode;
	}
	public String getStatus() {  //状态
		return status;
	}
	public void setStatus(String status) { //状态
		this.status = status;
	}
	public String getCreatorName() {  //申请人姓名
		return creatorName;
	}
	public void setCreatorName(String creatorName) { //申请人姓名
		this.creatorName = creatorName;
	}
	public String getSatisfaction() {  //满意度
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) { //满意度
		this.satisfaction = satisfaction;
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


}