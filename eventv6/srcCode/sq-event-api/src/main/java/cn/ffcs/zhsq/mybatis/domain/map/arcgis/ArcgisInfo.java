package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

/**
 * 2014-05-16 liushi add
 * 
 * @author liushi
 *
 */
public class ArcgisInfo implements Serializable {
	private Long id;//地图信息id
	private Long wid;//关联信息id
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
	 * 附加字段 更新者id
	 */
	private Long updateUser;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubBusiType() {
		return subBusiType;
	}
	public void setSubBusiType(String subBusiType) {
		this.subBusiType = subBusiType;
	}
	public Float getColorNum() {
		return colorNum;
	}
	public void setColorNum(Float colorNum) {
		this.colorNum = colorNum;
	}
	public String getElementsCollectionStr() {
		return elementsCollectionStr;
	}
	public void setElementsCollectionStr(String elementsCollectionStr) {
		this.elementsCollectionStr = elementsCollectionStr;
	}
	public boolean isEditAble() {
		return editAble;
	}
	public void setEditAble(boolean editAble) {
		this.editAble = editAble;
	}
	public Integer getMapt() {
		return mapt;
	}
	public void setMapt(Integer mapt) {
		this.mapt = mapt;
	}
	public Integer getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWid() {
		return wid;
	}
	public void setWid(Long wid) {
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
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
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

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
}
