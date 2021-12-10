package cn.ffcs.zhsq.mybatis.domain.szzg.resource;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: zg_resource_info模块bo对象
 * @Author: huangwenbin
 * @Date: 09-16 10:02:32
 * @Copyright: 2017 福富软件
 */
public class ZgResourceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3796745128116817765L;
	private Long resId; //主键id,SEQ_ZG_RESOURCE_INFO
	private String resTypeCode; //资源类型编码
	private Long resTableId; //资源相应表主键id
	private String resName; //资源名称
	private Double lng;//经度
	private Double lat;//纬度
	private String orgCode; 
	private String orgName; 
	private String status; //状态,1:有效,0:无效
	private Long createUserId;
	private Date createDate;
	private Long updateUserId;
	private Date updateDate;

	public ZgResourceInfo() {
	}

	public String getOrgName() {
		return orgName;
	}



	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}



	public String getOrgCode() {
		return orgCode;
	}



	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}



	public Long getResId() {
		return resId;
	}
	public void setResId(Long resId) {
		this.resId = resId;
	}
	public String getResTypeCode() {
		return resTypeCode;
	}
	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}
	public Long getResTableId() {
		return resTableId;
	}
	public void setResTableId(Long resTableId) {
		this.resTableId = resTableId;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


}