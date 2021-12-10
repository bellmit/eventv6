package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


import java.io.Serializable;

/**
 * 2014-08-06 liushi add
 * 地图服务信息
 * @Table Map_Config_Gis
 * @author liushi
 */
public class ArcgisServiceInfo implements Serializable{
	
	private static final long serialVersionUID = -7519046668480238824L;
	
	private Integer arcgisServiceInfoId;//arcgis服务图层Id
	private Integer arcgisConfigInfoId;//arcgis配置信息Id
	private String serviceUrl;//arcgis服务图层url
	private String serviceLoadType;//arcgis服务图层加载类型
	private Float serviceXmin;//服务图层信息xy
	private Float serviceYmin;//服务图层信息xy
	private Float serviceXmax;//服务图层信息xy
	private Float serviceYmax;//服务图层信息xy
	private String serviceImageFormat;//切片格式
	private String serviceServiceMode;//服务模型
	private String serviceLayerId;//服务图层Id
	private String serviceTileMartrixSetId;//服务瓦片矩阵设置id
	private String serviceTileMartrix;//服务瓦片矩阵
	private String tokenJs;
	private String agentServiceUrl;
	
	
	public String getServiceTileMartrix() {
		return serviceTileMartrix;
	}
	public void setServiceTileMartrix(String serviceTileMartrix) {
		this.serviceTileMartrix = serviceTileMartrix;
	}
	public Integer getArcgisServiceInfoId() {
		return arcgisServiceInfoId;
	}
	public void setArcgisServiceInfoId(Integer arcgisServiceInfoId) {
		this.arcgisServiceInfoId = arcgisServiceInfoId;
	}
	public Integer getArcgisConfigInfoId() {
		return arcgisConfigInfoId;
	}
	public void setArcgisConfigInfoId(Integer arcgisConfigInfoId) {
		this.arcgisConfigInfoId = arcgisConfigInfoId;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getServiceLoadType() {
		return serviceLoadType;
	}
	public void setServiceLoadType(String serviceLoadType) {
		this.serviceLoadType = serviceLoadType;
	}
	public Float getServiceXmin() {
		return serviceXmin;
	}
	public void setServiceXmin(Float serviceXmin) {
		this.serviceXmin = serviceXmin;
	}
	public Float getServiceYmin() {
		return serviceYmin;
	}
	public void setServiceYmin(Float serviceYmin) {
		this.serviceYmin = serviceYmin;
	}
	public Float getServiceXmax() {
		return serviceXmax;
	}
	public void setServiceXmax(Float serviceXmax) {
		this.serviceXmax = serviceXmax;
	}
	public Float getServiceYmax() {
		return serviceYmax;
	}
	public void setServiceYmax(Float serviceYmax) {
		this.serviceYmax = serviceYmax;
	}
	public String getServiceImageFormat() {
		return serviceImageFormat;
	}
	public void setServiceImageFormat(String serviceImageFormat) {
		this.serviceImageFormat = serviceImageFormat;
	}
	public String getServiceServiceMode() {
		return serviceServiceMode;
	}
	public void setServiceServiceMode(String serviceServiceMode) {
		this.serviceServiceMode = serviceServiceMode;
	}
	public String getServiceLayerId() {
		return serviceLayerId;
	}
	public void setServiceLayerId(String serviceLayerId) {
		this.serviceLayerId = serviceLayerId;
	}
	public String getServiceTileMartrixSetId() {
		return serviceTileMartrixSetId;
	}
	public void setServiceTileMartrixSetId(String serviceTileMartrixSetId) {
		this.serviceTileMartrixSetId = serviceTileMartrixSetId;
	}
	public String getTokenJs() {
		return tokenJs;
	}
	public void setTokenJs(String tokenJs) {
		this.tokenJs = tokenJs;
	}
	public String getAgentServiceUrl() {
		return agentServiceUrl;
	}
	public void setAgentServiceUrl(String agentServiceUrl) {
		this.agentServiceUrl = agentServiceUrl;
	}
	
}
