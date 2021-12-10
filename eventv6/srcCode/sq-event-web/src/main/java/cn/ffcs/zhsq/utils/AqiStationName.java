package cn.ffcs.zhsq.utils;
/**
 * AQI检测站点
 * @author linzhu
 *
 */
public enum AqiStationName {
	江阴("jiangyin"), 
	虹桥邮政("jiangyinA"),
	五星公园("jiangyinB"),
	第二实验小学("jiangyinC");
	private String stationId;
	// 构造方法
	private AqiStationName(String stationId) {
		this.stationId = stationId;
	}

	// get 
	public String getStationId() {
		return stationId;
	}

}
