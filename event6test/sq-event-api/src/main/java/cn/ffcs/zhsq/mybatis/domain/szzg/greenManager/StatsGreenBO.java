package cn.ffcs.zhsq.mybatis.domain.szzg.greenManager;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;

import java.io.Serializable;
import java.util.Date;

public class StatsGreenBO implements Serializable{
	private static final long serialVersionUID = 6343679212518799629L;
	private Long seqid;
	private String syear="";
	private String orgCode="";
	private String orgName="";
	private String orgPath="";
	private String builtupArea="";
	private String regionalArea="";
	private String popu="";
	private String gRate="";
	private String gCoverrate="";
	private String gCoverarea="";
	private String gArea="";
	private String gParkarea="";
	private String gPerparkarea="";
	private String forestArea="";
	private String forestCoverarea="";
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
	public String getBuiltupArea() {
		return builtupArea;
	}
	public void setBuiltupArea(String builtupArea) {
		this.builtupArea = builtupArea;
	}
	public String getRegionalArea() {
		return regionalArea;
	}
	public void setRegionalArea(String regionalArea) {
		this.regionalArea = regionalArea;
	}
	public String getPopu() {
		return popu;
	}
	public void setPopu(String popu) {
		this.popu = popu;
	}
	public String getgRate() {
		return gRate;
	}
	public void setgRate(String gRate) {
		this.gRate = gRate;
	}
	public String getgCoverrate() {
		return gCoverrate;
	}
	public void setgCoverrate(String gCoverrate) {
		this.gCoverrate = gCoverrate;
	}
	public String getgCoverarea() {
		return gCoverarea;
	}
	public void setgCoverarea(String gCoverarea) {
		this.gCoverarea = gCoverarea;
	}
	public String getgArea() {
		return gArea;
	}
	public void setgArea(String gArea) {
		this.gArea = gArea;
	}
	public String getgParkarea() {
		return gParkarea;
	}
	public void setgParkarea(String gParkarea) {
		this.gParkarea = gParkarea;
	}
	public String getgPerparkarea() {
		return gPerparkarea;
	}
	public void setgPerparkarea(String gPerparkarea) {
		this.gPerparkarea = gPerparkarea;
	}
	public String getForestArea() {
		return forestArea;
	}
	public void setForestArea(String forestArea) {
		this.forestArea = forestArea;
	}
	public String getForestCoverarea() {
		return forestCoverarea;
	}
	public void setForestCoverarea(String forestCoverarea) {
		this.forestCoverarea = forestCoverarea;
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
