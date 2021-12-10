package cn.ffcs.zhsq.mybatis.domain.map.bus;

/**
 * 站点信息(站点和线路的映射关系)
 * @author zkongbai
 *
 */
public class StationBusline implements java.io.Serializable{
	
	private static final long serialVersionUID = -7420732729352500289L;
	
	private Long buslineId;		//线路ID
	private String upDown;		//上下行,1=上行，2=下行
	private Long stationIndex;	//站点序号,从1开始
	private Long stationId;		//站点ID
	
	public Long getBuslineId() {
		return buslineId;
	}
	public void setBuslineId(Long buslineId) {
		this.buslineId = buslineId;
	}
	public String getUpDown() {
		return upDown;
	}
	public void setUpDown(String upDown) {
		this.upDown = upDown;
	}
	public Long getStationIndex() {
		return stationIndex;
	}
	public void setStationIndex(Long stationIndex) {
		this.stationIndex = stationIndex;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	
	
}
