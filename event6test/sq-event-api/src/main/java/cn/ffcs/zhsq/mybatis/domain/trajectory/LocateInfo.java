package cn.ffcs.zhsq.mybatis.domain.trajectory;

import java.io.Serializable;
import java.sql.Date;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;

public class LocateInfo implements Serializable{

	private static final long serialVersionUID = -7921026847241418014L;

	private Long locateId;
	
	private Long locateTerminal;
	
	private Date locateTime;
	
	private String x;  //lng
	
	private String y;  //lat
	
	private String status;

	
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

	public Long getLocateId() {
		return locateId;
	}

	public void setLocateId(Long locateId) {
		this.locateId = locateId;
	}

	public Long getLocateTerminal() {
		return locateTerminal;
	}

	public void setLocateTerminal(Long locateTerminal) {
		this.locateTerminal = locateTerminal;
	}

	public Date getLocateTime() {
		return locateTime;
	}

	public void setLocateTime(Date locateTime) {
		this.locateTime = locateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
