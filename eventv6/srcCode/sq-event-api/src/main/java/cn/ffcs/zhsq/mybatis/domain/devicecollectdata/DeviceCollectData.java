package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备采集数据模块bo对象
 * @Author: husp
 * @Date: 10-13 09:04:18
 * @Copyright: 2017 福富软件
 */
public class DeviceCollectData implements Serializable {
	private static final long serialVersionUID = 1L;
	//接口传入参数
	private String appKey;//验证Key
	private String sign;//验证签名
	private String timestamp;//日期验证是否过期  yyyyMMddHHmm
	private String deviceServiceId; //设备业务标识
	private String runningData;//设备最新运行数据集合
	private String itemCodes; //设备采集指标项编码
	private String itemValues; //设备采集指标项值
	private String bizType; //业务类型，00：马尾智慧社区物联网设备
	private String collTime; //设备采集时间
	
	//非接口传入参数
	private Long collectDataId; //采集数据ID
	private String collectItemCode; //设备采集指标项编码
	private String collectItemValue; //设备采集指标项值
	private String status; //状态，0：删除；1：有效；
	private Date collectTime; //设备采集时间
	private Date created; //创建时间
	private Date updated; //修改时间


	public Long getCollectDataId() {
		return collectDataId;
	}
	public void setCollectDataId(Long collectDataId) {
		this.collectDataId = collectDataId;
	}

	public String getCollectItemCode() {
		return collectItemCode;
	}
	public void setCollectItemCode(String collectItemCode) {
		this.collectItemCode = collectItemCode;
	}
	public String getCollectItemValue() {
		return collectItemValue;
	}
	public void setCollectItemValue(String collectItemValue) {
		this.collectItemValue = collectItemValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getItemCodes() {
		return itemCodes;
	}
	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}
	public String getItemValues() {
		return itemValues;
	}
	public void setItemValues(String itemValues) {
		this.itemValues = itemValues;
	}
	public String getCollTime() {
		return collTime;
	}
	public void setCollTime(String collTime) {
		this.collTime = collTime;
	}
	public String getRunningData() {
		return runningData;
	}
	public void setRunningData(String runningData) {
		this.runningData = runningData;
	}
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
}