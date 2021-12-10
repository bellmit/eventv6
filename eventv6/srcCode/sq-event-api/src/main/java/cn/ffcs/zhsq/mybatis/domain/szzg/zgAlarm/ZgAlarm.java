package cn.ffcs.zhsq.mybatis.domain.szzg.zgAlarm;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.ffcs.shequ.wap.util.DateUtils;

/**
 * @Description: 告警信息模块bo对象
 * @Author: linzhu
 * @Date: 08-07 15:03:54
 * @Copyright: 2017 福富软件
 */
public class ZgAlarm implements Serializable {
	private static final long serialVersionUID = 8139324558557182121L;
	private Long alarmId; //主键
	private String alarmContent; //告警内容
	private String alarmType; //告警类型：如高温预警、火灾告警
	private String alarmUrl; //链接地址
	private String alarmSource; //来源
	@DateTimeFormat(pattern=DateUtils.PATTERN_24TIME)
	private Date invalidDate; //失效日期
	private Long alarmLevel; //优先级:从上到下依次递减.1是最高  （紧急，重要，一般）
	private String status; //状态（1有效0失效）
	@DateTimeFormat(pattern=DateUtils.PATTERN_24TIME)
	private Date createTime; //创建时间
	private Long createUserId; //创建人id
	private Date updateTime; //更新时间
	private Long updateUserId; //更新人
	
	
	//冗余字段
	private String alarmTypeName;//告警类型名称
	private String alarmLevelName;//优先级名称


	public Long getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}
	public String getAlarmContent() {
		return alarmContent;
	}
	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmUrl() {
		return alarmUrl;
	}
	public void setAlarmUrl(String alarmUrl) {
		this.alarmUrl = alarmUrl;
	}
	public String getAlarmSource() {
		return alarmSource;
	}
	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource;
	}
	public Date getInvalidDate() {
		return invalidDate;
	}
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	public Long getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(Long alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getAlarmTypeName() {
		return alarmTypeName;
	}
	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}
	public String getAlarmLevelName() {
		return alarmLevelName;
	}
	public void setAlarmLevelName(String alarmLevelName) {
		this.alarmLevelName = alarmLevelName;
	}


}