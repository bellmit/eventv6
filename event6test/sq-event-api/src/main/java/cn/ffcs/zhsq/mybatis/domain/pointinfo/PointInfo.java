package cn.ffcs.zhsq.mybatis.domain.pointinfo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备点位信息表模块bo对象
 * @Author: tangheng
 * @Date: 09-16 16:27:18
 * @Copyright: 2019 福富软件
 */
public class PointInfo implements Serializable {

	private Long id;   //主键
	private String deviceId;  //设备id
	private String deviceCid;  //设备编码
	private String deviceName;  //设备名称
	private String latitude;  //设备定位信息(纬度)
	private String longitude;  //设备定位信息(经度)
	private String deviceSn;  //设备序列号
	private String regionCode;  //地域编码
	private String gridCode;  //网格编码
	private String gridPath;  //网格全路径
    private String deviceTypeCode; //设备类型编码
    private String deviceTypeName; //设备类型名称
    private String isValid;
	private Long creator;
	private Date createTime;
	private Long updator;
	private Date updateTime;

    private String deviceStatus; //设备状态 1:在线；0:离线

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceCid() {
		return deviceCid;
	}
	public void setDeviceCid(String deviceCid) {
		this.deviceCid = deviceCid;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getGridCode() {
		return gridCode;
	}
	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}
	public String getGridPath() {
		return gridPath;
	}
	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }
}