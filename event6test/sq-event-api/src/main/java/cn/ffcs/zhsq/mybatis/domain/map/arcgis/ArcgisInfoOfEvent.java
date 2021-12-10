package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;
import java.util.Date;

/**
 * 2014-05-16 zhongshm add
 * 
 * @author zhongshm
 *
 */
public class ArcgisInfoOfEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6607309609557917769L;
	private String markerType;
	private String catalog;
	private Long workFlowId;
	private Long instanceId;
	private Long taskId;
	private String eventType;
	private String gridName;
	
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
	private boolean editAble=false;
	private String urgencyDegree;
	private Date handleDate;
	private String handleStatus;
	private String elementsCollectionStr;//地图专题图层相关配置集合
	private String modeType;
	
	public String getElementsCollectionStr() {
		return elementsCollectionStr;
	}

	public void setElementsCollectionStr(String elementsCollectionStr) {
		this.elementsCollectionStr = elementsCollectionStr;
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

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
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

	public boolean isEditAble() {
		return editAble;
	}

	public void setEditAble(boolean editAble) {
		this.editAble = editAble;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(String modeType) {
		this.modeType = modeType;
	}
}
