package cn.ffcs.zhsq.taxi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("taxiConfig")
public class TaxiConfig {
	Logger logger = LoggerFactory.getLogger(TaxiConfig.class);
	
	public static final String GUID = "5a5c9f49-2f53-43ac-86e2-52affd40ac39";//请求方生成的guid序列
	/**
	 * 通过用户ID取得车辆信息
	 */
	public static final String URL_TAXI = "/api/v10/CarInfo/GetCarInfo?GUID="+GUID;
	/**
	 * 获取历史轨迹数据
	 */
	public static final String URL_TAXI_HISTORY_TRACK = "/api/v10/HistoryTrack/GetHistoryTrackZip?GUID="+GUID;
	
	/**
	 * 通过用户id(UserID)获取用户下车辆历史轨迹最后位置数据
	 */
	public static final String URL_TAXI_LAST_POSITION_BY_USERID = "/api/v10/CarInfo/GetCarLastPos?GUID="+GUID;
	
	/**
	 * 通过设备id(dev_id)获取车辆历史轨迹最后位置数据
	 */
	public static final String URL_TAXI_LAST_POSITION_BY_DEVID = "/api/v10/HistoryTrack/GetCarLastPos?GUID="+GUID;
	
	private String accessUrl;
	private String userName;
	private String pwd;
	private String userId;

	public String getAccessUrl() {
		return accessUrl;
	}

	@Value(value="${config.taxi.accessUrl}")
	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
		logger.debug("延平出租车访问接口配置成功,请求地址:{},可使用 TaxiConfig.getAccessUrl()获取请求含协议,ip,端口的url",accessUrl);
	}

	public String getUserName() {
		return userName;
	}
	
	@Value("${config.taxi.userName}")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}
	
	@Value("${config.taxi.password}")
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserId() {
		return userId;
	}
	@Value("${config.taxi.userId}")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
