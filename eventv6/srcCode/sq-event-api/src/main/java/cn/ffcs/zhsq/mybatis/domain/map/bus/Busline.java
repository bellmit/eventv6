package cn.ffcs.zhsq.mybatis.domain.map.bus;

/**
 * 线路信息
 * @author zkongbai
 *
 */
public class Busline implements java.io.Serializable{
	
	private static final long serialVersionUID = 3987726859992290935L;
	private Long buslineId;		//线路ID
	private String buslineNo;	//线路编号
	private String buslineName;	//线路名称
	private Double courseUp;	//上行里程,单位：千米
	private Double courseDown;	//下行里程,单位：千米
	private String runTime;		//运行时长,单位：分钟
	private String stimeUp;		//上行首班,格式：HH24:MI
	private String etimeUp;		//上行末班,格式：HH24:MI
	private String stimeDown;	//下行首班,格式：HH24:MI
	private String etimeDown;	//下行末班,格式：HH24:MI
	private Double priceBase;	//基本票价,单位：元
	private Double priceAll;	//全程票价,单位：元
	
	public Long getBuslineId() {
		return buslineId;
	}
	public void setBuslineId(Long buslineId) {
		this.buslineId = buslineId;
	}
	public String getBuslineNo() {
		return buslineNo;
	}
	public void setBuslineNo(String buslineNo) {
		this.buslineNo = buslineNo;
	}
	public String getBuslineName() {
		return buslineName;
	}
	public void setBuslineName(String buslineName) {
		this.buslineName = buslineName;
	}
	public Double getCourseUp() {
		return courseUp;
	}
	public void setCourseUp(Double courseUp) {
		this.courseUp = courseUp;
	}
	public Double getCourseDown() {
		return courseDown;
	}
	public void setCourseDown(Double courseDown) {
		this.courseDown = courseDown;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getStimeUp() {
		return stimeUp;
	}
	public void setStimeUp(String stimeUp) {
		this.stimeUp = stimeUp;
	}
	public String getEtimeUp() {
		return etimeUp;
	}
	public void setEtimeUp(String etimeUp) {
		this.etimeUp = etimeUp;
	}
	public String getStimeDown() {
		return stimeDown;
	}
	public void setStimeDown(String stimeDown) {
		this.stimeDown = stimeDown;
	}
	public String getEtimeDown() {
		return etimeDown;
	}
	public void setEtimeDown(String etimeDown) {
		this.etimeDown = etimeDown;
	}
	public Double getPriceBase() {
		return priceBase;
	}
	public void setPriceBase(Double priceBase) {
		this.priceBase = priceBase;
	}
	public Double getPriceAll() {
		return priceAll;
	}
	public void setPriceAll(Double priceAll) {
		this.priceAll = priceAll;
	}
	@Override
	public String toString() {
		return "Busline [buslineId=" + buslineId + ", buslineNo=" + buslineNo
				+ ", buslineName=" + buslineName + ", courseUp=" + courseUp
				+ ", courseDown=" + courseDown + ", runTime=" + runTime
				+ ", stimeUp=" + stimeUp + ", etimeUp=" + etimeUp
				+ ", stimeDown=" + stimeDown + ", etimeDown=" + etimeDown
				+ ", priceBase=" + priceBase + ", priceAll=" + priceAll + "]";
	}
	
}
