package cn.ffcs.zhsq.mybatis.domain.mwInternet;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;

/**
 * @Description: 告警任务表模块bo对象
 * @Author: guoyd
 * @Date: 04-08 10:18:25
 * @Copyright: 2018 福富软件
 */
public class WarnTask implements Serializable {

	private Long dwtId; //任务编号
	private String infoOrgCode; //所属小区
	private String infoOrgName; //小区名称
	private Long deviceId; //设备编号
	private String deviceName; //设备名称
	private String deviceGlag; //设备标识
	private Date warnTime; //告警时间
	private String warnInfo; //告警信息
	private String deviceAddr; //设备地址
	private String isValid="1"; //是否有效:0 无效 1 有效
	private Long createBy; //创建人
	private Date createTime; //创建时间
	private Long orgId;
	private Long updateBy; //修改人
	private Date updateTime; //修改时间
	
	private Date overTime;//处理时限
	private String state;//环节状态(0草稿，1待派发，2待反馈，3.待审核，4已归档)
	private String manufacturer;//厂商
	private String deviceType;//设备类型
	private String deviceStatus;//设备状态
	private Long taskUserId;//任务审核人信息
	private String taskUserName;
	private Long taskOrgId;
	private String deviceImage;//设备图片
	private String warnState = "1";//提醒状态（1正常，2处于驳回状态）
	//--标注
    private ResMarker resMarker;

    //工作流
    private Long wfDbid_;
	private String wfActivityName_;
	private String wfStatus;
	private Long wfInstanceId;
	private String wfCurUser;
	//
	private String warnTimeStr;
	private String overTimeStr;
	private String manufacturerStr;//厂商
	private String deviceTypeStr;//设备类型
	private String deviceStatusStr;//设备状态
	

	public Long getDwtId() {
		return dwtId;
	}
	public void setDwtId(Long dwtId) {
		this.dwtId = dwtId;
	}
	public String getInfoOrgCode() {
		return infoOrgCode;
	}
	public void setInfoOrgCode(String infoOrgCode) {
		this.infoOrgCode = infoOrgCode;
	}
	public String getInfoOrgName() {
		return infoOrgName;
	}
	public void setInfoOrgName(String infoOrgName) {
		this.infoOrgName = infoOrgName;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceGlag() {
		return deviceGlag;
	}
	public void setDeviceGlag(String deviceGlag) {
		this.deviceGlag = deviceGlag;
	}
	public Date getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}
	public String getWarnInfo() {
		return warnInfo;
	}
	public void setWarnInfo(String warnInfo) {
		this.warnInfo = warnInfo;
	}
	public String getDeviceAddr() {
		return deviceAddr;
	}
	public void setDeviceAddr(String deviceAddr) {
		this.deviceAddr = deviceAddr;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public ResMarker getResMarker() {
		return resMarker;
	}
	public void setResMarker(ResMarker resMarker) {
		this.resMarker = resMarker;
	}
	public Long getWfDbid_() {
		return wfDbid_;
	}
	public void setWfDbid_(Long wfDbid_) {
		this.wfDbid_ = wfDbid_;
	}
	public String getWfActivityName_() {
		return wfActivityName_;
	}
	public void setWfActivityName_(String wfActivityName_) {
		this.wfActivityName_ = wfActivityName_;
	}
	public String getWfStatus() {
		return wfStatus;
	}
	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}
	public Long getWfInstanceId() {
		return wfInstanceId;
	}
	public void setWfInstanceId(Long wfInstanceId) {
		this.wfInstanceId = wfInstanceId;
	}
	public String getWarnTimeStr() {
		return warnTimeStr;
	}
	public void setWarnTimeStr(String warnTimeStr) {
		this.warnTimeStr = warnTimeStr;
	}
	public String getDeviceTypeStr() {
		return deviceTypeStr;
	}
	public void setDeviceTypeStr(String deviceTypeStr) {
		this.deviceTypeStr = deviceTypeStr;
	}
	public String getDeviceStatusStr() {
		return deviceStatusStr;
	}
	public void setDeviceStatusStr(String deviceStatusStr) {
		this.deviceStatusStr = deviceStatusStr;
	}
	public String getManufacturerStr() {
		return manufacturerStr;
	}
	public void setManufacturerStr(String manufacturerStr) {
		this.manufacturerStr = manufacturerStr;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	public String getOverTimeStr() {
		return overTimeStr;
	}
	public void setOverTimeStr(String overTimeStr) {
		this.overTimeStr = overTimeStr;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getWfCurUser() {
		return wfCurUser;
	}
	public void setWfCurUser(String wfCurUser) {
		this.wfCurUser = wfCurUser;
	}
	public Long getTaskUserId() {
		return taskUserId;
	}
	public void setTaskUserId(Long taskUserId) {
		this.taskUserId = taskUserId;
	}
	public String getTaskUserName() {
		return taskUserName;
	}
	public void setTaskUserName(String taskUserName) {
		this.taskUserName = taskUserName;
	}
	public Long getTaskOrgId() {
		return taskOrgId;
	}
	public void setTaskOrgId(Long taskOrgId) {
		this.taskOrgId = taskOrgId;
	}
	public String getDeviceImage() {
		return deviceImage;
	}
	public void setDeviceImage(String deviceImage) {
		this.deviceImage = deviceImage;
	}
	public String getWarnState() {
		return warnState;
	}
	public void setWarnState(String warnState) {
		this.warnState = warnState;
	}
	

}