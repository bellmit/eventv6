package cn.ffcs.zhsq.mybatis.domain.hotLine;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: T_HOTLINE模块bo对象
 * @Author: wuxq
 * @Date: 09-08 17:02:47
 * @Copyright: 2020 福富软件
 */
public class Hotline implements Serializable {

	private Long caseId;//主键
	private String caseNo; //工单编号
	private String contactUser; //联系人
	private String contactTel; //联系电话
	private String tel; //电话
	private String sex; //性别
	private String age; //年龄范围
	private String sources; //来源渠道
	private String appealType; //诉求类型
	private Date callTime; //来电时间
	private String title; //标题
	private String content; //诉求内容
	private String email; //电子邮箱
	private String isWeb; //网站公示
	private String occurred; //事件发生地址
	private String address; //具体地址
	private String underType; //归口类型
	private String urgency; //紧急事项
	private String appealPublic; //诉求保密
	private String returnType; //回访类型
	private String isValid; //状态
	private Date createTime; //创建时间
	private Long creatorId; //创建人
	private Date updateTime; //修改时间
	private Long updateorId; //修改人
	private String eventStatus; //事件状态
	private Long eventId; //事件Id

	
	private String callTimeStr;

	
	public Long getCaseId() {
		return caseId;
	}
	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getCallTimeStr() {
		return callTimeStr;
	}
	public void setCallTimeStr(String callTimeStr) {
		this.callTimeStr = callTimeStr;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getContactUser() {
		return contactUser;
	}
	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	public String getAppealType() {
		return appealType;
	}
	public void setAppealType(String appealType) {
		this.appealType = appealType;
	}
	public Date getCallTime() {
		return callTime;
	}
	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsWeb() {
		return isWeb;
	}
	public void setIsWeb(String isWeb) {
		this.isWeb = isWeb;
	}
	public String getOccurred() {
		return occurred;
	}
	public void setOccurred(String occurred) {
		this.occurred = occurred;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUnderType() {
		return underType;
	}
	public void setUnderType(String underType) {
		this.underType = underType;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getAppealPublic() {
		return appealPublic;
	}
	public void setAppealPublic(String appealPublic) {
		this.appealPublic = appealPublic;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateorId() {
		return updateorId;
	}
	public void setUpdateorId(Long updateorId) {
		this.updateorId = updateorId;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}


}