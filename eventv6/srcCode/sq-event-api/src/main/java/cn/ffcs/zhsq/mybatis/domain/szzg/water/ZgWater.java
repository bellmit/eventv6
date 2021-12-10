package cn.ffcs.zhsq.mybatis.domain.szzg.water;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.wap.util.DateUtils;

/**
 * @Description: 水质量模块bo对象
 * @Author: linzhu
 * @Date: 08-01 16:57:47
 * @Copyright: 2017 福富软件
 */
public class ZgWater implements Serializable {
	private static final long serialVersionUID = -8076358886202207168L;
	private String orgCode;//组织编码
	private Long seqId;
	private String name; //检测点名称
	private String datetime; //检测时间
	private String phvalue;
	private String rjy;
	private String mgzs;
	private String al;
	private String szlb; //质量级别
	private String longitude; //经度
	private String dimensions; //纬度
	@DateTimeFormat(pattern=DateUtils.PATTERN_24TIME)
	private Date createTime; //创建时间
	private Long createUserId; //创建人id
	@DateTimeFormat(pattern=DateUtils.PATTERN_24TIME)
	private Date updateTime; //更新时间
	private Long updateUserId; //更新人id
	private String status; //状态：1正常0删除
	
	@DateTimeFormat(pattern=DateUtils.PATTERN_DATE)
	private Date startTime; //开始创建时间
	@DateTimeFormat(pattern=DateUtils.PATTERN_DATE)
	private Date endTime; //结束时间
	//冗余字段
	private String orgName;
	
	//地图标注
		private ResMarker resMarker;
	
	public Long getSeqId() {
		return seqId;
	}
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getPhvalue() {
		return phvalue;
	}
	public void setPhvalue(String phvalue) {
		this.phvalue = phvalue;
	}
	public String getRjy() {
		return rjy;
	}
	public void setRjy(String rjy) {
		this.rjy = rjy;
	}
	public String getMgzs() {
		return mgzs;
	}
	public void setMgzs(String mgzs) {
		this.mgzs = mgzs;
	}
	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public String getSzlb() {
		return szlb;
	}
	public void setSzlb(String szlb) {
		this.szlb = szlb;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ResMarker getResMarker() {
		return resMarker;
	}
	public void setResMarker(ResMarker resMarker) {
		this.resMarker = resMarker;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


}
