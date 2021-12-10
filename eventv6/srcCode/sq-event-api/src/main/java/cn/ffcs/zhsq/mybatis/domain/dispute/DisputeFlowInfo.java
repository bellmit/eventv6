package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 办理流程表模块bo对象
 * @Author: wangh
 * @Date: 12-02 14:26:46
 * @table: 表信息描述 T_HANDLE_FLOW_INFO 办理流程表  办理流程表  序列SEQ_HANDLE_FLOW_INFO
 * @Copyright: 2020 福富软件
 */
public class DisputeFlowInfo implements Serializable {

  private static final long serialVersionUID = 1L;

	private String hfiId; //主键ID 	VARCHAR2(50)
	private Long bizId; //业务ID 	NUMBER(16)
	private String bizUuid; //业务ID（UUID） 	VARCHAR2(50)
	private String bizType; //业务类型 	VARCHAR2(100)
	private String handlerName; //办理人姓名 	VARCHAR2(50)
	private Long handlerId; //办理人ID 	NUMBER(16)
	private Long handleUnitId; //办理单位ID 	NUMBER(16)
	private String handleUnitCode; //办理单位CODE 	VARCHAR2(50)
	private String handleUnitName; //办理单位名称 	VARCHAR2(200)
	private Date handleTime; //办理时间 	TIMESTAMP
	private String handleOpinion; //办理意见 	VARCHAR2(2000)
	private String handleStatus; //办理状态 	VARCHAR2(2)
	private Integer handleSequence; //办理时序 	NUMBER(3)
	private String handleLinkName; //办理环节名称 	VARCHAR2(50)
	private String orgName; //创建组织名称 	VARCHAR2(100)
	private String orgCode; //创建组织Code 	VARCHAR2(50)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	TIMESTAMP
	private Long updator; //更新人 	NUMBER(16)
	private Date updateTime; //更新时间 	TIMESTAMP
	private String isValid; //是否有效 	CHAR(1)
	private String remark; //备注 	VARCHAR2(200)


	public String getHfiId() {  //主键ID
		return hfiId;
	}
	public void setHfiId(String hfiId) { //主键ID
		this.hfiId = hfiId;
	}
	public Long getBizId() {  //业务ID
		return bizId;
	}
	public void setBizId(Long bizId) { //业务ID
		this.bizId = bizId;
	}
	public String getBizUuid() {  //业务ID（UUID）
		return bizUuid;
	}
	public void setBizUuid(String bizUuid) { //业务ID（UUID）
		this.bizUuid = bizUuid;
	}
	public String getBizType() {  //业务类型
		return bizType;
	}
	public void setBizType(String bizType) { //业务类型
		this.bizType = bizType;
	}
	public String getHandlerName() {  //办理人姓名
		return handlerName;
	}
	public void setHandlerName(String handlerName) { //办理人姓名
		this.handlerName = handlerName;
	}
	public Long getHandlerId() {  //办理人ID
		return handlerId;
	}
	public void setHandlerId(Long handlerId) { //办理人ID
		this.handlerId = handlerId;
	}
	public Long getHandleUnitId() {  //办理单位ID
		return handleUnitId;
	}
	public void setHandleUnitId(Long handleUnitId) { //办理单位ID
		this.handleUnitId = handleUnitId;
	}
	public String getHandleUnitCode() {  //办理单位CODE
		return handleUnitCode;
	}
	public void setHandleUnitCode(String handleUnitCode) { //办理单位CODE
		this.handleUnitCode = handleUnitCode;
	}
	public String getHandleUnitName() {  //办理单位名称
		return handleUnitName;
	}
	public void setHandleUnitName(String handleUnitName) { //办理单位名称
		this.handleUnitName = handleUnitName;
	}
	public Date getHandleTime() {  //办理时间
		return handleTime;
	}
	public void setHandleTime(Date handleTime) { //办理时间
		this.handleTime = handleTime;
	}
	public String getHandleOpinion() {  //办理意见
		return handleOpinion;
	}
	public void setHandleOpinion(String handleOpinion) { //办理意见
		this.handleOpinion = handleOpinion;
	}
	public String getHandleStatus() {  //办理状态
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) { //办理状态
		this.handleStatus = handleStatus;
	}
	public Integer getHandleSequence() {  //办理时序
		return handleSequence;
	}
	public void setHandleSequence(Integer handleSequence) { //办理时序
		this.handleSequence = handleSequence;
	}
	public String getHandleLinkName() {  //办理环节名称
		return handleLinkName;
	}
	public void setHandleLinkName(String handleLinkName) { //办理环节名称
		this.handleLinkName = handleLinkName;
	}
	public String getOrgName() {  //创建组织名称
		return orgName;
	}
	public void setOrgName(String orgName) { //创建组织名称
		this.orgName = orgName;
	}
	public String getOrgCode() {  //创建组织Code
		return orgCode;
	}
	public void setOrgCode(String orgCode) { //创建组织Code
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
	public String getIsValid() {  //是否有效
		return isValid;
	}
	public void setIsValid(String isValid) { //是否有效
		this.isValid = isValid;
	}
	public String getRemark() {  //备注
		return remark;
	}
	public void setRemark(String remark) { //备注
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "DisputeFlowInfo{" +
				"hfiId='" + hfiId + '\'' +
				", bizId=" + bizId +
				", bizUuid='" + bizUuid + '\'' +
				", bizType='" + bizType + '\'' +
				", handlerName='" + handlerName + '\'' +
				", handlerId=" + handlerId +
				", handleUnitId=" + handleUnitId +
				", handleUnitCode='" + handleUnitCode + '\'' +
				", handleUnitName='" + handleUnitName + '\'' +
				", handleTime=" + handleTime +
				", handleOpinion='" + handleOpinion + '\'' +
				", handleStatus='" + handleStatus + '\'' +
				", handleSequence=" + handleSequence +
				", handleLinkName='" + handleLinkName + '\'' +
				", orgName='" + orgName + '\'' +
				", orgCode='" + orgCode + '\'' +
				", creator=" + creator +
				", createTime=" + createTime +
				", updator=" + updator +
				", updateTime=" + updateTime +
				", isValid='" + isValid + '\'' +
				", remark='" + remark + '\'' +
				'}';
	}
}