package cn.ffcs.zhsq.mybatis.domain.eliminatelettertho;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 三书一函接收与整改情况表模块bo对象
 * @Author: liangbzh
 * @Date: 08-18 08:48:37
 * @table: 表信息描述 T_ELIMINATE_LETTER_THO_RE_CHG 三书一函接收与整改情况表  序列SEQ_E_LETTER_THO_RE_CHG
 * @Copyright: 2021 福富软件
 */
public class EliminateLetterThoReChg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long chgId; //主键 	NUMBER(9)
    private String chgUuid; //逻辑主键 	VARCHAR2(32)
    private Long thoId; //主表主键 	NUMBER(9)
    private String reDepartCodeSub; //接收单位组织截取值 	VARCHAR2(10)
    private String reDepartCode; //接收单位编码 	VARCHAR2(9)
    private String reDepartNameDet; //具体接收单位名称 	VARCHAR2(50)
    private Long reUserId; //接收人员ID 	NUMBER(9)
    private String reUserName; //接收人员名称 	VARCHAR2(50)
    private String reUser; //接收人员JSON数据 	VARCHAR2(2000)
    private Date reDate; //接收时间 	TIMESTAMP
    private String reType; //回复情况 	CHAR(1)
    private String reDetail; //回复详情 	VARCHAR2(2000)
    private String reDissentAgree; //接收单位提出异议 	CHAR(1)
    private String reDissentDetail; //接收单位异议详情 	VARCHAR2(2000)
    private String dissentAgree; //是否同意异议 	CHAR(1)
    private String dissentDetail; //异议详情 	VARCHAR2(2000)
    private String chgType; //整改情况 	CHAR(1)
    private String chgDetail; //整改详情 	VARCHAR2(2000)
    private String indusChgAgree; //是否开展行业治理 	CHAR(1)
    private String indusChgDetail; //行业治理情况详情 	VARCHAR2(2000)
    private String longActionAgree; //是否建立长效机制 	CHAR(1)
    private String longActionDetail; //长效机制详情 	VARCHAR2(2000)
    private String othClob; //其他情况说明 	CLOB
    private String regionCode; //地域编码 	VARCHAR2(24)
    private String orgCode; //组织编码 	VARCHAR2(24)
    private Long creator; //创建人 	NUMBER(9)
    private Date createTime; //创建时间 	TIMESTAMP
    private Long updator; //修改人 	NUMBER(9)
    private Date updateTime; //修改时间 	TIMESTAMP
    private String isValid; //有效状态 	CHAR(1)
    private String remark; //备注 	VARCHAR2(200)

    private String reDateStr; //接收时间 格式转化

    private String chgTypeCN;//整改情况中文

    //制发单位是否同意异议
    public static class DissentAgree {
        public static final String AGREE = "2";     //同意
        public static final String DISAGREE = "1";  //不同意
    }

    public Long getChgId() {  //主键
        return chgId;
    }

    public void setChgId(Long chgId) { //主键
        this.chgId = chgId;
    }

    public String getChgUuid() {  //逻辑主键
        return chgUuid;
    }

    public void setChgUuid(String chgUuid) { //逻辑主键
        this.chgUuid = chgUuid;
    }

    public Long getThoId() {  //主表主键
        return thoId;
    }

    public void setThoId(Long thoId) { //主表主键
        this.thoId = thoId;
    }

    public String getReDepartCodeSub() {  //接收单位组织截取值
        return reDepartCodeSub;
    }

    public void setReDepartCodeSub(String reDepartCodeSub) { //接收单位组织截取值
        this.reDepartCodeSub = reDepartCodeSub;
    }

    public String getReDepartCode() {  //接收单位编码
        return reDepartCode;
    }

    public void setReDepartCode(String reDepartCode) { //接收单位编码
        this.reDepartCode = reDepartCode;
    }

    public String getReDepartNameDet() {  //具体接收单位名称
        return reDepartNameDet;
    }

    public void setReDepartNameDet(String reDepartNameDet) { //具体接收单位名称
        this.reDepartNameDet = reDepartNameDet;
    }

    public Long getReUserId() {  //接收人员ID
        return reUserId;
    }

    public void setReUserId(Long reUserId) { //接收人员ID
        this.reUserId = reUserId;
    }

    public String getReUserName() {  //接收人员名称
        return reUserName;
    }

    public void setReUserName(String reUserName) { //接收人员名称
        this.reUserName = reUserName;
    }

    public String getReUser() {  //接收人员JSON数据
        return reUser;
    }

    public void setReUser(String reUser) { //接收人员JSON数据
        this.reUser = reUser;
    }

    public Date getReDate() {  //接收时间
        return reDate;
    }

    public void setReDate(Date reDate) { //接收时间
        this.reDate = reDate;
    }

    public String getReType() {  //回复情况
        return reType;
    }

    public void setReType(String reType) { //回复情况
        this.reType = reType;
    }

    public String getReDetail() {  //回复详情
        return reDetail;
    }

    public void setReDetail(String reDetail) { //回复详情
        this.reDetail = reDetail;
    }

    public String getReDissentAgree() {  //接收单位提出异议
        return reDissentAgree;
    }

    public void setReDissentAgree(String reDissentAgree) { //接收单位提出异议
        this.reDissentAgree = reDissentAgree;
    }

    public String getReDissentDetail() {  //接收单位异议详情
        return reDissentDetail;
    }

    public void setReDissentDetail(String reDissentDetail) { //接收单位异议详情
        this.reDissentDetail = reDissentDetail;
    }

    public String getDissentAgree() {  //是否同意异议
        return dissentAgree;
    }

    public void setDissentAgree(String dissentAgree) { //是否同意异议
        this.dissentAgree = dissentAgree;
    }

    public String getDissentDetail() {  //异议详情
        return dissentDetail;
    }

    public void setDissentDetail(String dissentDetail) { //异议详情
        this.dissentDetail = dissentDetail;
    }

    public String getChgType() {  //整改情况
        return chgType;
    }

    public void setChgType(String chgType) { //整改情况
        this.chgType = chgType;
    }

    public String getChgDetail() {  //整改详情
        return chgDetail;
    }

    public void setChgDetail(String chgDetail) { //整改详情
        this.chgDetail = chgDetail;
    }

    public String getIndusChgAgree() {  //是否开展行业治理
        return indusChgAgree;
    }

    public void setIndusChgAgree(String indusChgAgree) { //是否开展行业治理
        this.indusChgAgree = indusChgAgree;
    }

    public String getIndusChgDetail() {  //行业治理情况详情
        return indusChgDetail;
    }

    public void setIndusChgDetail(String indusChgDetail) { //行业治理情况详情
        this.indusChgDetail = indusChgDetail;
    }

    public String getLongActionAgree() {  //是否建立长效机制
        return longActionAgree;
    }

    public void setLongActionAgree(String longActionAgree) { //是否建立长效机制
        this.longActionAgree = longActionAgree;
    }

    public String getLongActionDetail() {  //长效机制详情
        return longActionDetail;
    }

    public void setLongActionDetail(String longActionDetail) { //长效机制详情
        this.longActionDetail = longActionDetail;
    }

    public String getOthClob() {  //其他情况说明
        return othClob;
    }

    public void setOthClob(String othClob) { //其他情况说明
        this.othClob = othClob;
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

    public String getReDateStr() {
        return reDateStr;
    }

    public void setReDateStr(String reDateStr) {
        this.reDateStr = reDateStr;
    }

    public String getChgTypeCN() {
        return chgTypeCN;
    }

    public void setChgTypeCN(String chgTypeCN) {
        this.chgTypeCN = chgTypeCN;
    }
}