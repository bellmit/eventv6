package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;
import java.util.Date;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfoOfHashId  implements Serializable  {
	private Long id;//地图信息id
	private String wid;//关联信息id
	private String name;//关联信息名称
	private Double x;//中心点x
	private Double y;//中心点y
	private String hs;//边界点集合
	private Integer mapt;//地图类型
	private String lineColor;//边界线颜色
	private Integer lineWidth;//边界线宽度
	private String areaColor;//区域颜色
	private String nameColor;//区域颜色
	private Float colorNum;//区域颜色透明参数
	private boolean editAble=false;
	private String elementsCollectionStr;//地图专题图层相关配置集合
	private String subBusiType;//业务子类型
	private String address;//地址
	private String bizId;//业务id
	private String type;//关联类型
	private String channelId;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1826668827585986654L;
	private String markerType;
	private String catalog;
	private String urgencyDegree;
	private Date handleDate;
	private String status;//事件状态
	private String handleStatus;
	private Integer distance;
	private String ids;
	private String names;
	private String bizType;

	private Boolean reLoadSummaryData;//是否需要重新去请求概要信息链接，如果为false，就把data直接做作为参数传到概要信息链接

	
	private EventDisposal eventDisposal = new EventDisposal();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public EventDisposal getEventDisposal() {
		return eventDisposal;
	}

	public void setEventDisposal(EventDisposal eventDisposal) {
		this.eventDisposal = eventDisposal;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getUrgencyDegree() {
		return urgencyDegree;
	}

	public void setUrgencyDegree(String urgencyDegree) {
		this.urgencyDegree = urgencyDegree;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getMarkerType() {
		return markerType;
	}

	public void setMarkerType(String markerType) {
		this.markerType = markerType;
	}

	public Boolean getReLoadSummaryData() {
		return reLoadSummaryData;
	}

	public void setReLoadSummaryData(Boolean reLoadSummaryData) {
		this.reLoadSummaryData = reLoadSummaryData;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public Integer getMapt() {
		return mapt;
	}

	public void setMapt(Integer mapt) {
		this.mapt = mapt;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}

	public String getAreaColor() {
		return areaColor;
	}

	public void setAreaColor(String areaColor) {
		this.areaColor = areaColor;
	}

	public String getNameColor() {
		return nameColor;
	}

	public void setNameColor(String nameColor) {
		this.nameColor = nameColor;
	}

	public Float getColorNum() {
		return colorNum;
	}

	public void setColorNum(Float colorNum) {
		this.colorNum = colorNum;
	}

	public boolean isEditAble() {
		return editAble;
	}

	public void setEditAble(boolean editAble) {
		this.editAble = editAble;
	}

	public String getElementsCollectionStr() {
		return elementsCollectionStr;
	}

	public void setElementsCollectionStr(String elementsCollectionStr) {
		this.elementsCollectionStr = elementsCollectionStr;
	}

	public String getSubBusiType() {
		return subBusiType;
	}

	public void setSubBusiType(String subBusiType) {
		this.subBusiType = subBusiType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
