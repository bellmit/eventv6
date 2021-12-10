package cn.ffcs.zhsq.mybatis.domain.deviceinfos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;

/**
 * @Description: 设备信息表对象
 * @Author: LINZHU
 * @Date: 10-13 09:29:49
 */
public class DeviceInfos implements Serializable {
	private static final long serialVersionUID = 6744513449569683064L;
	private Long deviceId; //设备ID
	private String deviceName; //设备名称
	private String deviceServiceId; //设备业务标识
	private String deviceInstallAddress; //设备地址
	private String latitude; //设备定位信息(纬度)
	private String longitude; //设备定位信息(经度)
	private String manufacturersNo; //设备厂商 10001  福水智联10002  骐驭10003  星海10004  三鑫隆10005  狄奈克10006  冠林10007  逸充
	private String deviceType; //设备类型 100001  水表（小）100002  水表（大）100003  消防栓100004  烟感100005  井盖100006  门禁100007  地磁100008  梯控100009  梯口机100010  车辆识别100011  路灯100012  电动车充电桩100013  井盖井下设备100014  井盖路面积水设备100015  井盖浊度设备100016  井盖流速设备 100017环境监控
	private String deviceImage; //设备图片
	private String status;
	private Long creator;
	private Date created;
	private String creatorName;
	private Long updater;
	private Date updated;
	private String updaterName;
	private String bizType;
	private String regionCode;
	private String regionName;
	
	private Map<String, Object> dCollectAlert; //设备告警数据
	private String collectTime; //告警数据采集时间
	
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public Map<String, Object> getdCollectAlert() {
		return dCollectAlert;
	}
	public void setdCollectAlert(Map<String, Object> dCollectAlert) {
		this.dCollectAlert = dCollectAlert;
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
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
	public String getDeviceInstallAddress() {
		return deviceInstallAddress;
	}
	public void setDeviceInstallAddress(String deviceInstallAddress) {
		this.deviceInstallAddress = deviceInstallAddress;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getManufacturersNo() {
		return manufacturersNo;
	}
	public void setManufacturersNo(String manufacturersNo) {
		this.manufacturersNo = manufacturersNo;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceImage() {
		return deviceImage;
	}
	public void setDeviceImage(String deviceImage) {
		this.deviceImage = deviceImage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Long getUpdater() {
		return updater;
	}
	public void setUpdater(Long updater) {
		this.updater = updater;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdaterName() {
		return updaterName;
	}
	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


}