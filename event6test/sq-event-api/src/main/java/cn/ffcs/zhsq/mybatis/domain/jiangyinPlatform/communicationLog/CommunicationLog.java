package cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 通讯记录表模块bo对象
 * @Author: 林树轩
 * @Date: 03-11 08:59:39
 * @table: 表信息描述 T_JY_COMMUNICATION_LOG 通讯记录表  江阴大屏通讯记录表  序列SEQ_T_JY_COMMUNICATION_LOG
 * @Copyright: 2020 福富软件
 */
public class CommunicationLog implements Serializable {

  private static final long serialVersionUID = 1L;

	private String logUuid; //通讯记录主键 	VARCHAR(32)
	private String contactedAccount; //被联系方账号 	VARCHAR(32)
	private String contactAccount; //联系方账号 	VARCHAR(32)
	private String contactType; //通讯方式(1:电话, 2:视频, 3:短信) 	CHAR(1)
	private Date contactTime; //联系时间 	DATE
	private String callType; //呼叫方式（1:呼入, 2:呼出） 	CHAR(1)
	private String contactDuration; //联系时长 	VARCHAR(20)
	private String attUuid; //附件外键 	VARCHAR(32)
	private String smsContent; //短信内容 	VARCHAR(300)
	private String isValid; //记录状态 	CHAR(1)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	DATE
	private Long updator; //修改人 	NUMBER(16)
	private Date updateTime; //修改时间 	DATE
	private String remark; //备注 	VARCHAR(200)
	
	private String contactTimeStr;


	public String getLogUuid() {  //通讯记录主键
		return logUuid;
	}
	public void setLogUuid(String logUuid) { //通讯记录主键
		this.logUuid = logUuid;
	}
	public String getContactType() {  //通讯方式(1:电话, 2:视频, 3:短信)
		return contactType;
	}
	public void setContactType(String contactType) { //通讯方式(1:电话, 2:视频, 3:短信)
		this.contactType = contactType;
	}
	public Date getContactTime() {  //联系时间
		return contactTime;
	}
	public void setContactTime(Date contactTime) { //联系时间
		if (contactTimeStr == null || contactTimeStr.trim().length() == 0) {
			if (contactTime != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				contactTimeStr = sdf.format(contactTime);
			}
		}
		this.contactTime = contactTime;
	}
	public String getContactDuration() {  //联系时长
		return contactDuration;
	}
	public void setContactDuration(String contactDuration) { //联系时长
		this.contactDuration = contactDuration;
	}
	public String getAttUuid() {  //附件外键
		return attUuid;
	}
	public void setAttUuid(String attUuid) { //附件外键
		this.attUuid = attUuid;
	}
	public String getSmsContent() {  //短信内容
		return smsContent;
	}
	public void setSmsContent(String smsContent) { //短信内容
		this.smsContent = smsContent;
	}
	public String getIsValid() {  //记录状态
		return isValid;
	}
	public void setIsValid(String isValid) { //记录状态
		this.isValid = isValid;
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
	public String getRemark() {  //备注
		return remark;
	}
	public void setRemark(String remark) { //备注
		this.remark = remark;
	}
	public String getContactedAccount() {
		return contactedAccount;
	}
	public void setContactedAccount(String contactedAccount) {
		this.contactedAccount = contactedAccount;
	}
	public String getContactAccount() {
		return contactAccount;
	}
	public void setContactAccount(String contactAccount) {
		this.contactAccount = contactAccount;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getContactTimeStr() {
		return contactTimeStr;
	}
	public void setContactTimeStr(String contactTimeStr) {
		this.contactTimeStr = contactTimeStr;
	}

}