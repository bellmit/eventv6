package cn.ffcs.zhsq.mybatis.domain.eliminatelettertho;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 三书一函主表模块bo对象
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @table: 表信息描述 T_ELIMINATE_LETTER_THO 三书一函主表  序列SEQ_ELIMINATE_LETTER_THO
 * @Copyright: 2021 福富软件
 */
public class EliminateLetterTho extends LayuiPage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long thoId; //主键 	NUMBER(9)
    private String thoUuid; //逻辑主键 	VARCHAR2(32)
    private String bizStatus; //业务状态 	VARCHAR2(2)
    private String thtNo; //三书一函编码 	VARCHAR2(50)
    private String letterType; //文书类型 	CHAR(1)
    private String fbDepartCode; //发布单位编码 	VARCHAR2(6)
    private String fbDepartNameDet; //具体发布单位名称 	VARCHAR2(50)
    private Date fbDate; //发布时间 	TIMESTAMP
    private String letterNo; //文号 	VARCHAR2(50)
    private String caseName; //案件名称 	VARCHAR2(200)
    private String caseNo; //案件编码 	VARCHAR2(50)
    private String industrialComment; //行业领域补充说明 	VARCHAR2(2000)
    private String flowStep; //流程节点名称 	VARCHAR2(50)
    private Long instanceId; //流程实例ID 	NUMBER(16)
    private String letterContentClob; //文书内容 	CLOB
    private String regionCode; //地域编码 	VARCHAR2(24)
    private String orgCode; //组织编码 	VARCHAR2(24)
    private Long creator; //创建人 	NUMBER(9)
    private Date createTime; //创建时间 	TIMESTAMP
    private Long updator; //修改人 	NUMBER(9)
    private Date updateTime; //修改时间 	TIMESTAMP
    private String isValid; //有效状态 	CHAR(1)
    private String remark; //备注 	VARCHAR2(200)

    private String fbDateStr; //发布时间 仅用于类型转化
    private EliminateLetterThoReChg reChgs; //三书一函接收与整改情况表模块bo对象
    private List<EliminateLetterIndus> induss; //三书一函行业领域表模块bo对象

    private String[] bizStatusArr;//查询条件：业务状态数组
    private String[] flowStepArr;//查询条件：流程节点数组
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fbDateStart;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fbDateEnd;
    private String letterTypeCN;//文书类型名称
    private String indusCodeArr;//行业领域信息字符串

    private String advice;//每个环节的意见
    private UserInfo nextUser;//下一环节办理人信息
    private String nextUserStr;

    private String exportThoUuid;//导出的uuid

    public Long getThoId() {  //主键
        return thoId;
    }

    public void setThoId(Long thoId) { //主键
        this.thoId = thoId;
    }

    public String getThoUuid() {  //逻辑主键
        return thoUuid;
    }

    public void setThoUuid(String thoUuid) { //逻辑主键
        this.thoUuid = thoUuid;
    }

    public String getBizStatus() {  //业务状态
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) { //业务状态
        this.bizStatus = bizStatus;
    }

    public String getThtNo() {  //三书一函编码
        return thtNo;
    }

    public void setThtNo(String thtNo) { //三书一函编码
        this.thtNo = thtNo;
    }

    public String getLetterType() {  //文书类型
        return letterType;
    }

    public void setLetterType(String letterType) { //文书类型
        this.letterType = letterType;
    }

    public String getFbDepartCode() {  //发布单位编码
        return fbDepartCode;
    }

    public void setFbDepartCode(String fbDepartCode) { //发布单位编码
        this.fbDepartCode = fbDepartCode;
    }

    public String getFbDepartNameDet() {  //具体发布单位名称
        return fbDepartNameDet;
    }

    public void setFbDepartNameDet(String fbDepartNameDet) { //具体发布单位名称
        this.fbDepartNameDet = fbDepartNameDet;
    }

    public Date getFbDate() {  //发布时间
        return fbDate;
    }

    public void setFbDate(Date fbDate) { //发布时间
        this.fbDate = fbDate;
    }

    public String getLetterNo() {  //文号
        return letterNo;
    }

    public void setLetterNo(String letterNo) { //文号
        this.letterNo = letterNo;
    }

    public String getCaseName() {  //案件名称
        return caseName;
    }

    public void setCaseName(String caseName) { //案件名称
        this.caseName = caseName;
    }

    public String getCaseNo() {  //案件编码
        return caseNo;
    }

    public void setCaseNo(String caseNo) { //案件编码
        this.caseNo = caseNo;
    }

    public String getIndustrialComment() {  //行业领域补充说明
        return industrialComment;
    }

    public void setIndustrialComment(String industrialComment) { //行业领域补充说明
        this.industrialComment = industrialComment;
    }

    public String getFlowStep() {  //流程节点名称
        return flowStep;
    }

    public void setFlowStep(String flowStep) { //流程节点名称
        this.flowStep = flowStep;
    }

    public Long getInstanceId() {  //流程实例ID
        return instanceId;
    }

    public void setInstanceId(Long instanceId) { //流程实例ID
        this.instanceId = instanceId;
    }

    public String getLetterContentClob() {  //文书内容
        return letterContentClob;
    }

    public void setLetterContentClob(String letterContentClob) { //文书内容
        this.letterContentClob = letterContentClob;
    }

    public String getRegionCode() {  //地域编码
        return regionCode;
    }

    public void setRegionCode(String regionCode) { //地域编码
        this.regionCode = regionCode;
    }

    public String getOrgCode() {  //组织编码
        return orgCode;
    }

    public void setOrgCode(String orgCode) { //组织编码
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

    public Long getUpdator() {  //修改人
        return updator;
    }

    public void setUpdator(Long updator) { //修改人
        this.updator = updator;
    }

    public Date getUpdateTime() {  //修改时间
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) { //修改时间
        this.updateTime = updateTime;
    }

    public String getIsValid() {  //有效状态
        return isValid;
    }

    public void setIsValid(String isValid) { //有效状态
        this.isValid = isValid;
    }

    public String getRemark() {  //备注
        return remark;
    }

    public void setRemark(String remark) { //备注
        this.remark = remark;
    }

    public String[] getBizStatusArr() {
        return bizStatusArr;
    }

    public void setBizStatusArr(String[] bizStatusArr) {
        this.bizStatusArr = bizStatusArr;
    }

    public String[] getFlowStepArr() {
        return flowStepArr;
    }

    public void setFlowStepArr(String[] flowStepArr) {
        this.flowStepArr = flowStepArr;
    }

    public Date getFbDateStart() {
        return fbDateStart;
    }

    public void setFbDateStart(Date fbDateStart) {
        this.fbDateStart = fbDateStart;
    }

    public Date getFbDateEnd() {
        return fbDateEnd;
    }

    public void setFbDateEnd(Date fbDateEnd) {
        this.fbDateEnd = fbDateEnd;
    }

    public String getFbDateStr() {
        return fbDateStr;
    }

    public void setFbDateStr(String fbDateStr) {
        this.fbDateStr = fbDateStr;
    }

    public EliminateLetterThoReChg getReChgs() {
        return reChgs;
    }

    public void setReChgs(EliminateLetterThoReChg reChgs) {
        this.reChgs = reChgs;
    }

    public List<EliminateLetterIndus> getInduss() {
        return induss;
    }

    public void setInduss(List<EliminateLetterIndus> induss) {
        this.induss = induss;
    }

    public String getLetterTypeCN() {
        return letterTypeCN;
    }

    public void setLetterTypeCN(String letterTypeCN) {
        this.letterTypeCN = letterTypeCN;
    }

    public String getIndusCodeArr() {
        return indusCodeArr;
    }

    public void setIndusCodeArr(String indusCodeArr) {
        this.indusCodeArr = indusCodeArr;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public UserInfo getNextUser() {
        return nextUser;
    }

    public void setNextUser(UserInfo nextUser) {
        this.nextUser = nextUser;
    }

    public String getNextUserStr() {
        return nextUserStr;
    }

    public void setNextUserStr(String nextUserStr) {
        this.nextUserStr = nextUserStr;
    }

    public String getExportThoUuid() {
        return exportThoUuid;
    }

    public void setExportThoUuid(String exportThoUuid) {
        this.exportThoUuid = exportThoUuid;
    }
}