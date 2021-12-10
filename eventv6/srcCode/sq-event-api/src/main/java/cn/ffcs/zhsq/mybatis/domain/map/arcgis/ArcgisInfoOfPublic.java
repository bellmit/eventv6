package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.util.Date;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfoOfPublic extends ArcgisInfo {
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
}
