package cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 张天慈 on 2018/5/18.
 */
public class EventSBREClue implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long clueId; //线索编号，序列为：SEQ_SBRE_CLUE_ID
    private String clueTitle; //线索标题
    private String infoOrgCode; //地域编码
    private String clueSource; //线索来源：字典B591001
    private String clueSourceName; //线索来源中文
    private String importantDegree; //重要程度：字典B591002
    private String importantDegreeName; //重要程度中文
    private String isEncrypt; //1 加密；0 不加密
    private String isEncryptName;//1 是；0 否
    private String isRealName;//是否实名举报
    private String isRealNameName;//1 是；0 否
    private Date reportDate; //举报时间
    private String reportDateStr; //举报时间
    private String clueContent; //线索内容
    private String disposalMethod; //处置方式：字典B591003
    private String disposalMethodName; //处置方式中文
    private Date handleDate; //办理时限
    private String handleDateStr;//办理时限文本
    private String handleDateFlag;//处理时限标识 1：正常；2：将到期；3：已过期
    private Date endDate;    //结束时间
    private Date registerDate; //立案时间
    private String registerDateStr; //立案时间
    private String suspectedCharges; //涉嫌罪名：字典B591004
    private String suspectedChargesName; //涉嫌罪名中文
    private Long registerUnit; //立案单位，对应T_ZZ_PRVETION_TEAM中IS_UNIT为1的记录
    private String registerUnitName;//立案单位名称
    private String registerName; //立案人员姓名
    private Date punishDate; //处罚时间
    private String punishDateStr; //处罚时间
    private Long punishUnit; //处罚单位，对应T_ZZ_PRVETION_TEAM中IS_UNIT为1的记录
    private String punishUnitName;    //处罚单位名称
    private String punishmenter; //处罚办理人员姓名
    private Date transferDate; //移交时间
    private String transferDateStr; //移交时间
    private Long transferUnit; //移交单位，对应T_ZZ_PRVETION_TEAM中IS_UNIT为1的记录
    private String transferUnitName;    //移交单位名称
    private String transferName; //移交人员姓名
    private String disposalSituation;    //处理情况
    private Date feedbackDate; //反馈时间
    private String feedbackDateStr; //反馈时间
    private String feedbackName; //反馈人姓名
    private String informantAdvice;//举报人意见
    private String clueRemark; //备注
    private String clueStatus; //线索状态
    private String clueStatusName; //线索状态中文
    private Date createDate; //登记时间
    private String createDateStr; //登记时间

    private Long creatorId; //登记人员
    private String creatorOrgName;//登记人员所属组织名称
    private Date updateDate; //更新时间
    private Long updaterId; //更新人员

    private String gridPath; //所属区域
    private Long gridId; //网格Id
    private String gridName; //网格名称

    private String curOrgName;//当前办理组织名称

    private String involveAttackFocus; //涉及打击重点
    private String involveAttackFocusName; //涉及打击重点中文


    public String getClueSourceName() {
        return clueSourceName;
    }

    public void setClueSourceName(String clueSourceName) {
        this.clueSourceName = clueSourceName;
    }

    public String getImportantDegreeName() {
        return importantDegreeName;
    }

    public void setImportantDegreeName(String importantDegreeName) {
        this.importantDegreeName = importantDegreeName;
    }

    public String getReportDateStr() {
        return reportDateStr;
    }

    public void setReportDateStr(String reportDateStr) {
        this.reportDateStr = reportDateStr;
    }

    public String getDisposalMethodName() {
        return disposalMethodName;
    }

    public void setDisposalMethodName(String disposalMethodName) {
        this.disposalMethodName = disposalMethodName;
    }

    public String getRegisterDateStr() {
        return registerDateStr;
    }

    public void setRegisterDateStr(String registerDateStr) {
        this.registerDateStr = registerDateStr;
    }

    public String getSuspectedChargesName() {
        return suspectedChargesName;
    }

    public void setSuspectedChargesName(String suspectedChargesName) {
        this.suspectedChargesName = suspectedChargesName;
    }

    public String getPunishDateStr() {
        return punishDateStr;
    }

    public void setPunishDateStr(String punishDateStr) {
        this.punishDateStr = punishDateStr;
    }

    public String getTransferDateStr() {
        return transferDateStr;
    }

    public void setTransferDateStr(String transferDateStr) {
        this.transferDateStr = transferDateStr;
    }

    public String getFeedbackDateStr() {
        return feedbackDateStr;
    }

    public void setFeedbackDateStr(String feedbackDateStr) {
        this.feedbackDateStr = feedbackDateStr;
    }

    public String getClueStatusName() {
        return clueStatusName;
    }

    public void setClueStatusName(String clueStatusName) {
        this.clueStatusName = clueStatusName;
    }

    public Long getClueId() {
        return clueId;
    }

    public void setClueId(Long clueId) {
        this.clueId = clueId;
    }

    public String getClueTitle() {
        return clueTitle;
    }

    public void setClueTitle(String clueTitle) {
        this.clueTitle = clueTitle;
    }

    public String getInfoOrgCode() {
        return infoOrgCode;
    }

    public void setInfoOrgCode(String infoOrgCode) {
        this.infoOrgCode = infoOrgCode;
    }

    public String getClueSource() {
        return clueSource;
    }

    public void setClueSource(String clueSource) {
        this.clueSource = clueSource;
    }

    public String getImportantDegree() {
        return importantDegree;
    }

    public void setImportantDegree(String importantDegree) {
        this.importantDegree = importantDegree;
    }

    public String getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(String isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public String getIsEncryptName() {
        return isEncryptName;
    }

    public void setIsEncryptName(String isEncryptName) {
        this.isEncryptName = isEncryptName;
    }

    public String getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(String isRealName) {
        this.isRealName = isRealName;
    }

    public String getIsRealNameName() {
        return isRealNameName;
    }

    public void setIsRealNameName(String isRealNameName) {
        this.isRealNameName = isRealNameName;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getClueContent() {
        return clueContent;
    }

    public void setClueContent(String clueContent) {
        this.clueContent = clueContent;
    }

    public String getDisposalMethod() {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
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

    public String getHandleDateFlag() {
        return handleDateFlag;
    }

    public void setHandleDateFlag(String handleDateFlag) {
        this.handleDateFlag = handleDateFlag;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getSuspectedCharges() {
        return suspectedCharges;
    }

    public void setSuspectedCharges(String suspectedCharges) {
        this.suspectedCharges = suspectedCharges;
    }

    public Long getRegisterUnit() {
        return registerUnit;
    }

    public void setRegisterUnit(Long registerUnit) {
        this.registerUnit = registerUnit;
    }

    public String getRegisterUnitName() {
        return registerUnitName;
    }

    public void setRegisterUnitName(String registerUnitName) {
        this.registerUnitName = registerUnitName;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public Date getPunishDate() {
        return punishDate;
    }

    public void setPunishDate(Date punishDate) {
        this.punishDate = punishDate;
    }

    public Long getPunishUnit() {
        return punishUnit;
    }

    public void setPunishUnit(Long punishUnit) {
        this.punishUnit = punishUnit;
    }

    public String getPunishUnitName() {
        return punishUnitName;
    }

    public void setPunishUnitName(String punishUnitName) {
        this.punishUnitName = punishUnitName;
    }

    public String getPunishmenter() {
        return punishmenter;
    }

    public void setPunishmenter(String punishmenter) {
        this.punishmenter = punishmenter;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Long getTransferUnit() {
        return transferUnit;
    }

    public void setTransferUnit(Long transferUnit) {
        this.transferUnit = transferUnit;
    }

    public String getTransferUnitName() {
        return transferUnitName;
    }

    public void setTransferUnitName(String transferUnitName) {
        this.transferUnitName = transferUnitName;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getDisposalSituation() {
        return disposalSituation;
    }

    public void setDisposalSituation(String disposalSituation) {
        this.disposalSituation = disposalSituation;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getInformantAdvice() {
        return informantAdvice;
    }

    public void setInformantAdvice(String informantAdvice) {
        this.informantAdvice = informantAdvice;
    }

    public String getClueRemark() {
        return clueRemark;
    }

    public void setClueRemark(String clueRemark) {
        this.clueRemark = clueRemark;
    }

    public String getClueStatus() {
        return clueStatus;
    }

    public void setClueStatus(String clueStatus) {
        this.clueStatus = clueStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorOrgName() {
        return creatorOrgName;
    }

    public void setCreatorOrgName(String creatorOrgName) {
        this.creatorOrgName = creatorOrgName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getGridPath() {
        return gridPath;
    }

    public void setGridPath(String gridPath) {
        this.gridPath = gridPath;
    }

    public Long getGridId() {
        return gridId;
    }

    public void setGridId(Long gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getCurOrgName() {
        return curOrgName;
    }

    public void setCurOrgName(String curOrgName) {
        this.curOrgName = curOrgName;
    }

    public String getInvolveAttackFocus() {
        return involveAttackFocus;
    }

    public void setInvolveAttackFocus(String involveAttackFocus) {
        this.involveAttackFocus = involveAttackFocus;
    }

    public String getInvolveAttackFocusName() {
        return involveAttackFocusName;
    }

    public void setInvolveAttackFocusName(String involveAttackFocusName) {
        this.involveAttackFocusName = involveAttackFocusName;
    }
}
