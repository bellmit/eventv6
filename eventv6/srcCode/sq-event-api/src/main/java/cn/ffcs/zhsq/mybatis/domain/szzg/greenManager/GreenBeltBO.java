package cn.ffcs.zhsq.mybatis.domain.szzg.greenManager;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;

import java.io.Serializable;
import java.util.Date;

public class GreenBeltBO implements Serializable{
	private static final long serialVersionUID = -4418911572918830762L;
	private Long seqid;
	private String syear="";
	private String gName="";
	private String gArea="";
	private String gType="";
	private String gTypeName;
	private String gRate="";
	private String gCoverarea="";
	private String gCoverrate="";
	private String location="";
	private String plant="";
	private String plantdate="";
	private String gSubtype="";
	private String gSubtypename="";
	private String std="";
	private String stdName;
	private String x="";
	private String y="";
	private ResMarker resMarker;

	public ResMarker getResMarker() {
		return resMarker;
	}

	public void setResMarker(ResMarker resMarker) {
		this.resMarker = resMarker;
	}

	private String longitude="";
	private String dimensions="";
	private String geometry="";
	private String remark="";
	private String orgCode="";
	private String orgName="";
	private String orgPath="";
	private String extattr1="";
	private String extattr2="";
	private String extattr3="";
	private String extattr4="";
	private String extattr5="";
	private Date opdate;
	private String status;
	private Date updateTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	public String getgTypeName() {
		return gTypeName;
	}
	public void setgTypeName(String gTypeName) {
		this.gTypeName = gTypeName;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getSeqid() {
		return seqid;
	}
	public void setSeqid(Long seqid) {
		this.seqid = seqid;
	}
	public String getSyear() {
		return syear;
	}
	public void setSyear(String syear) {
		this.syear = syear;
	}
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public String getgArea() {
		return gArea;
	}
	public void setgArea(String gArea) {
		this.gArea = gArea;
	}
	public String getgType() {
		return gType;
	}
	public void setgType(String gType) {
		this.gType = gType;
	}
	public String getgRate() {
		return gRate;
	}
	public void setgRate(String gRate) {
		this.gRate = gRate;
	}
	public String getgCoverarea() {
		return gCoverarea;
	}
	public void setgCoverarea(String gCoverarea) {
		this.gCoverarea = gCoverarea;
	}
	public String getgCoverrate() {
		return gCoverrate;
	}
	public void setgCoverrate(String gCoverrate) {
		this.gCoverrate = gCoverrate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getPlantdate() {
		return plantdate;
	}
	public void setPlantdate(String plantdate) {
		this.plantdate = plantdate;
	}
	public String getgSubtype() {
		return gSubtype;
	}
	public void setgSubtype(String gSubtype) {
		this.gSubtype = gSubtype;
	}
	public String getgSubtypename() {
		return gSubtypename;
	}
	public void setgSubtypename(String gSubtypename) {
		this.gSubtypename = gSubtypename;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
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
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getExtattr1() {
		return extattr1;
	}
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	public String getExtattr2() {
		return extattr2;
	}
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	public String getExtattr3() {
		return extattr3;
	}
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	public String getExtattr4() {
		return extattr4;
	}
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	public String getExtattr5() {
		return extattr5;
	}
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	public Date getOpdate() {
		return opdate;
	}
	public void setOpdate(Date opdate) {
		this.opdate = opdate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
