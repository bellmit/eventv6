package cn.ffcs.zhsq.bus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("busConfig")
public class BusConfig {
	Logger logger = LoggerFactory.getLogger(BusConfig.class);

	public static final String URL_BUS_LINE = "/api/Busline/Get";
	public static final String URL_STATION_BUSLINE = "/api/Station/GetLineStation";
	public static final String URL_STATION = "/api/Station/GetStation";
	public static final String URL_CAR_BUSLINE = "/api/Car/Get";
	public static final String URL_CAR_LAST_GPS = "/api/run/GetLastGpsData";
	
	private String accessUrl;
	private String userName;
	private String pwd;
	

	public String getAccessUrl() {
		return accessUrl;
	}

	@Value("${config.bus.accessUrl}")
	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
		logger.debug("延平公交访问接口配置成功,请求地址:{},可使用 BusConfig.getAccessUrl()获取请求含协议,ip,端口的url",accessUrl);
	}

	public String getUserName() {
		return userName;
	}
	
	@Value("${config.bus.userName}")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}
	
	@Value("${config.bus.password}")
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
