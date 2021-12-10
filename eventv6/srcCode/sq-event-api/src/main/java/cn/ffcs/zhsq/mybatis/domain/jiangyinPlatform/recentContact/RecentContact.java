package cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 最近联系人表模块bo对象
 * @Author: 林树轩
 * @Date: 03-16 08:49:14
 * @table: 表信息描述 T_JY_RECENT_CONTACT 最近联系人表  江阴大屏最近联系人表  序列SEQ_T_JY_RECENT_CONTACT
 * @Copyright: 2020 福富软件
 */
public class RecentContact implements Serializable {

  private static final long serialVersionUID = 1L;

	private String rcUuid; //主键标识 	VARCHAR(32)
	private String contactedAccount; //被联系方账号 	VARCHAR(32)
	private String contactAccount; //联系方账号 	VARCHAR(32)
	private String contactType; //通讯类别(1:电话, 2:视频, 3:短信) 	CHAR(1)
	private Date contactTime; //联系时间 	DATE
	private String callType; //呼叫方式（1:呼入, 2:呼出） 	CHAR(1)
	private String isValid; //记录状态 	CHAR(1)
	private Long creator; //创建人 	NUMBER(16)
	private Date createTime; //创建时间 	DATE
	private Long updator; //修改人 	NUMBER(16)
	private Date updateTime; //修改时间 	DATE
	private String remark; //备注 	VARCHAR(200)
	
	private String contactTimeStr;
	private String abName;
	private String sysUserName;
	private String showTel;

	public String getRcUuid() {  //主键标识
		return rcUuid;
	}
	public void setRcUuid(String rcUuid) { //主键标识
		this.rcUuid = rcUuid;
	}
	public String getContactedAccount() {  //被联系方账号
		return contactedAccount;
	}
	public void setContactedAccount(String contactedAccount) { //被联系方账号
		this.contactedAccount = contactedAccount;
	}
	public String getContactAccount() {  //联系方账号
		return contactAccount;
	}
	public void setContactAccount(String contactAccount) { //联系方账号
		this.contactAccount = contactAccount;
	}
	public String getContactType() {  //通讯类别(1:电话, 2:视频, 3:短信)
		return contactType;
	}
	public void setContactType(String contactType) { //通讯类别(1:电话, 2:视频, 3:短信)
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
	public String getCallType() {  //呼叫方式（1:呼入, 2:呼出）
		return callType;
	}
	public void setCallType(String callType) { //呼叫方式（1:呼入, 2:呼出）
		this.callType = callType;
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
	public String getContactTimeStr() {
		return contactTimeStr;
	}
	public void setContactTimeStr(String contactTimeStr) {
		this.contactTimeStr = contactTimeStr;
	}
	public String getAbName() {
		return abName;
	}
	public void setAbName(String abName) {
		this.abName = abName;
	}
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	public String getShowTel() {
		return showTel;
	}
	public void setShowTel(String showTel) {
		this.showTel = showTel;
	}


}