package cn.ffcs.zhsq.mybatis.domain.map.bus;

/**
 * 站点基础信息
 * @author zkongbai
 *
 */
public class Station implements java.io.Serializable{
	private static final long serialVersionUID = -756355318131141979L;
	
	private Long stationId;			//站点ID
	private String stationName;		//站点名称
	private Long lng;				//经纬,乘以10的六次方
	private Long lat;				//纬度,乘以10的六次方
	
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Long getLng() {
		return lng;
	}
	public void setLng(Long lng) {
		this.lng = lng;
	}
	public Long getLat() {
		return lat;
	}
	public void setLat(Long lat) {
		this.lat = lat;
	}
	
}
