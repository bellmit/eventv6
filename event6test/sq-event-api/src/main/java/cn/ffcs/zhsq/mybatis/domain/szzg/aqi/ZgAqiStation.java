package cn.ffcs.zhsq.mybatis.domain.szzg.aqi;

import java.io.Serializable;
/**
 * 空气质量检测站点
 * @author Administrator
 *
 */
public class ZgAqiStation implements Serializable{
	private static final long serialVersionUID = -735796876288546624L;
	private long seqid;
	private String statioId;//监测点ID
	private String stationName;//监测点名称
	private String location;//站点地址
	private String longitude;//精度
	private String dimensions;//维度
	private Integer sources;//数据来源
	private String codeAirlevel;//站点级别
	
	private String  codeRegion;//行政区代码
	private String  regionName;//行政区名称
	private String  yearNumber;//年份
	
	private ZgAQI zgAQI;//空气质量
	
	
	
	public String getStatioId() {
		return statioId;
	}
	public void setStatioId(String statioId) {
		this.statioId = statioId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public Integer getSources() {
		return sources;
	}
	public void setSources(Integer sources) {
		this.sources = sources;
	}
	public String getCodeAirlevel() {
		return codeAirlevel;
	}
	public void setCodeAirlevel(String codeAirlevel) {
		this.codeAirlevel = codeAirlevel;
	}
	public String getCodeRegion() {
		return codeRegion;
	}
	public void setCodeRegion(String codeRegion) {
		this.codeRegion = codeRegion;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getYearNumber() {
		return yearNumber;
	}
	public void setYearNumber(String yearNumber) {
		this.yearNumber = yearNumber;
	}
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public ZgAQI getZgAQI() {
		return zgAQI;
	}
	public void setZgAQI(ZgAQI zgAQI) {
		this.zgAQI = zgAQI;
	}


}
