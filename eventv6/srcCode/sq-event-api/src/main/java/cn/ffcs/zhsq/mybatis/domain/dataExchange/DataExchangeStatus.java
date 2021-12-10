package cn.ffcs.zhsq.mybatis.domain.dataExchange;

import java.io.Serializable;
import java.util.Date;

public class DataExchangeStatus implements Serializable {
	private Long interId;				//状态交互主键
	
	private String oppoSideBizType;		//对方业务类型：0：旧事件；1：旧事件任务；2：新事件；3：新事件任务
	private String oppoSideBizCode;		//对方业务标识
	private String oppoSideBizStatus;	//对方业务状态
	
	private String ownSideBizType;		//我方业务类型,0：旧事件；1：旧事件任务；2：新事件；3：新事件任务
	private String ownSideBizCode;		//我方业务标识
	private String ownSideBizStatus;	//我方业务状态
	
	private String srcPlatform;			//来源平台，新事件平台：000；便民服务：004
	private String destPlatform;		//目标平台，新事件平台：000；便民服务：004
	
	private String exchangeFlag;		//数据交换标记，0：未交换；1：已交换
	private String status;				//状态，0：无效；1：有效
	private Long createUserId;			//创建人
	private String createUserName;		//创建人姓名
	private Date createTime;			//创建时间
	private String createTimeStr;		//创建时间文本
	private Long updateUserId;			//最后更新人
	private Date updateTime;			//最后更新时间
	private String updateTimeStr;		//最后更新时间文本
	
	private String xmlData;
	
	public Long getInterId() {
		return interId;
	}
	public void setInterId(Long interId) {
		this.interId = interId;
	}
	public String getOppoSideBizType() {
		return oppoSideBizType;
	}
	public void setOppoSideBizType(String oppoSideBizType) {
		this.oppoSideBizType = oppoSideBizType;
	}
	public String getOppoSideBizCode() {
		return oppoSideBizCode;
	}
	public void setOppoSideBizCode(String oppoSideBizCode) {
		this.oppoSideBizCode = oppoSideBizCode;
	}
	public String getOppoSideBizStatus() {
		return oppoSideBizStatus;
	}
	public void setOppoSideBizStatus(String oppoSideBizStatus) {
		this.oppoSideBizStatus = oppoSideBizStatus;
	}
	public String getOwnSideBizType() {
		return ownSideBizType;
	}
	public void setOwnSideBizType(String ownSideBizType) {
		this.ownSideBizType = ownSideBizType;
	}
	public String getOwnSideBizCode() {
		return ownSideBizCode;
	}
	public void setOwnSideBizCode(String ownSideBizCode) {
		this.ownSideBizCode = ownSideBizCode;
	}
	public String getOwnSideBizStatus() {
		return ownSideBizStatus;
	}
	public void setOwnSideBizStatus(String ownSideBizStatus) {
		this.ownSideBizStatus = ownSideBizStatus;
	}
	public String getSrcPlatform() {
		return srcPlatform;
	}
	public void setSrcPlatform(String srcPlatform) {
		this.srcPlatform = srcPlatform;
	}
	public String getDestPlatform() {
		return destPlatform;
	}
	public void setDestPlatform(String destPlatform) {
		this.destPlatform = destPlatform;
	}
	public String getExchangeFlag() {
		return exchangeFlag;
	}
	public void setExchangeFlag(String exchangeFlag) {
		this.exchangeFlag = exchangeFlag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
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
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public String getXmlData() {
		return xmlData;
	}
	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}
	
}
