package cn.ffcs.zhsq.mybatis.domain.dataExchange;

import java.io.Serializable;
import java.util.Date;

public class DataExchange implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4633404882151620349L;

	private Integer interId;

    private String oppoSideBizType;

    private String oppoSideBizCode;

    private String oppoSideBizStatus;

    private String ownSideBizType;

    private String ownSideBizCode;

    private String ownSideBizStatus;

    private String srcPlatform;

    private String destPlatform;

    private String exchangeFlag;

    private String status;

    private Integer createUserId;

    private Date createTime;

    private Integer updateUserId;

    private Date updateTime;

    private String xmlData;

    public Integer getInterId() {
        return interId;
    }

    public void setInterId(Integer interId) {
        this.interId = interId;
    }

    public String getOppoSideBizType() {
        return oppoSideBizType;
    }

    public void setOppoSideBizType(String oppoSideBizType) {
        this.oppoSideBizType = oppoSideBizType == null ? null : oppoSideBizType.trim();
    }

    public String getOppoSideBizCode() {
        return oppoSideBizCode;
    }

    public void setOppoSideBizCode(String oppoSideBizCode) {
        this.oppoSideBizCode = oppoSideBizCode == null ? null : oppoSideBizCode.trim();
    }

    public String getOppoSideBizStatus() {
        return oppoSideBizStatus;
    }

    public void setOppoSideBizStatus(String oppoSideBizStatus) {
        this.oppoSideBizStatus = oppoSideBizStatus == null ? null : oppoSideBizStatus.trim();
    }

    public String getOwnSideBizType() {
        return ownSideBizType;
    }

    public void setOwnSideBizType(String ownSideBizType) {
        this.ownSideBizType = ownSideBizType == null ? null : ownSideBizType.trim();
    }

    public String getOwnSideBizCode() {
        return ownSideBizCode;
    }

    public void setOwnSideBizCode(String ownSideBizCode) {
        this.ownSideBizCode = ownSideBizCode == null ? null : ownSideBizCode.trim();
    }

    public String getOwnSideBizStatus() {
        return ownSideBizStatus;
    }

    public void setOwnSideBizStatus(String ownSideBizStatus) {
        this.ownSideBizStatus = ownSideBizStatus == null ? null : ownSideBizStatus.trim();
    }

    public String getSrcPlatform() {
        return srcPlatform;
    }

    public void setSrcPlatform(String srcPlatform) {
        this.srcPlatform = srcPlatform == null ? null : srcPlatform.trim();
    }

    public String getDestPlatform() {
        return destPlatform;
    }

    public void setDestPlatform(String destPlatform) {
        this.destPlatform = destPlatform == null ? null : destPlatform.trim();
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag == null ? null : exchangeFlag.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData == null ? null : xmlData.trim();
    }
}