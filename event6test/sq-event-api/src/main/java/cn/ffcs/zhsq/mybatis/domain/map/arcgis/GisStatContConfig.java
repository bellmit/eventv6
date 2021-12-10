package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;

import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;

/**
 * 地图框选可配置
 * 
 * @table T_GIS_STAT_CONT_CFG
 * @author huangmw
 * 
 */
public class GisStatContConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6445703559927776764L;

	private long contCfgId;// 内容配置ID
	private String statObjName;// 统计对象
	private String objClass;// 所属类别
	private long layCfgId;// 图层配置ID
	private long statCfgId;// 关联统计配置ID
	private int displayOrder;// 显示序号
	private String status;// 状态，0：删除；1：启用；2：禁用
	private GisDataCfg gisDataCfg; // 图层信息

	public long getContCfgId() {
		return contCfgId;
	}

	public void setContCfgId(long contCfgId) {
		this.contCfgId = contCfgId;
	}

	public String getStatObjName() {
		return statObjName;
	}

	public void setStatObjName(String statObjName) {
		this.statObjName = statObjName;
	}

	public String getObjClass() {
		return objClass;
	}

	public void setObjClass(String objClass) {
		this.objClass = objClass;
	}

	public long getLayCfgId() {
		return layCfgId;
	}

	public void setLayCfgId(long layCfgId) {
		this.layCfgId = layCfgId;
	}

	public long getStatCfgId() {
		return statCfgId;
	}

	public void setStatCfgId(long statCfgId) {
		this.statCfgId = statCfgId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GisDataCfg getGisDataCfg() {
		return gisDataCfg;
	}

	public void setGisDataCfg(GisDataCfg gisDataCfg) {
		this.gisDataCfg = gisDataCfg;
	}

}
