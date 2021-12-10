package cn.ffcs.zhsq.mybatis.domain.eventTopicAndKeyWords;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 热点事件主题表模块bo对象
 * @Author: os.wuzhj
 * @Date: 10-15 16:01:04
 * @table: 表信息描述 T_EVENT_TOPIC 热点事件主题表  热点事件主题表  序列SEQ_EVENT_TOPIC_ID
 * @Copyright: 2019 福富软件
 */
public class EventTopic implements Serializable {

  private static final long serialVersionUID = 1L;

	private Long id_; //ID 	NUMBER(9)
	private String topicName; //主题名称 	VARCHAR2(50)
	private String infoOrgCode; //网格编码 	VARCHAR2(20)
	private String rule; //分析规则 	VARCHAR2(200)
	private String isRelease; //是否发布 	CHAR(1)
	private String isReleaseStr; //是否发布 	
	private Long creator; //创建者 	NUMBER(9)
	private Date createTime; //创建时间 	DATE
	private Long updator; //更新者 	NUMBER(9)
	private Date updateTime; //更新时间 	DATE
	private String remark; //REMARK 	VARCHAR2(500)  存储分析规则转成的sql
	private String isValid; //IS_VAILD 	CHAR(1)
	private String bizType; //业务类型 	CHAR(1)


	public String getTopicName() {  //主题名称
		return topicName;
	}
	public void setTopicName(String topicName) { //主题名称
		this.topicName = topicName;
	}
	public String getInfoOrgCode() {  //网格编码
		return infoOrgCode;
	}
	public void setInfoOrgCode(String infoOrgCode) { //网格编码
		this.infoOrgCode = infoOrgCode;
	}
	public String getRule() {  //分析规则
		return rule;
	}
	public void setRule(String rule) { //分析规则
		this.rule = rule;
	}
	public String getIsRelease() {  //是否发布
		return isRelease;
	}
	public void setIsRelease(String isRelease) { //是否发布
		this.isRelease = isRelease;
	}
	public Long getCreator() {  //创建者
		return creator;
	}
	public void setCreator(Long creator) { //创建者
		this.creator = creator;
	}
	public Date getCreateTime() {  //创建时间
		return createTime;
	}
	public void setCreateTime(Date createTime) { //创建时间
		this.createTime = createTime;
	}
	public Long getUpdator() {  //更新者
		return updator;
	}
	public void setUpdator(Long updator) { //更新者
		this.updator = updator;
	}
	public Date getUpdateTime() {  //更新时间
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) { //更新时间
		this.updateTime = updateTime;
	}
	public String getRemark() {  //REMARK
		return remark;
	}
	public void setRemark(String remark) { //REMARK
		this.remark = remark;
	}
	public String getIsValid() {  //IS_VAILD
		return isValid;
	}
	public void setIsValid(String isValid) { //IS_VALID
		this.isValid = isValid;
	}
	public String getBizType() {  //业务类型
		return bizType;
	}
	public void setBizType(String bizType) { //业务类型
		this.bizType = bizType;
	}
	public String getIsReleaseStr() {
		return isReleaseStr;
	}
	public void setIsReleaseStr(String isReleaseStr) {
		this.isReleaseStr = isReleaseStr;
	}
	public Long getId_() {
		return id_;
	}
	public void setId_(Long id_) {
		this.id_ = id_;
	}


}