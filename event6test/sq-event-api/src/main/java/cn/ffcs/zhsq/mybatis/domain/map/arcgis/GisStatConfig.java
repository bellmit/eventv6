package cn.ffcs.zhsq.mybatis.domain.map.arcgis;

import java.io.Serializable;
import java.util.Date;

/**
 * 地图框选可配置
 * 
 * @table T_GIS_STAT_CFG
 * @author huangmw
 * 
 */
public class GisStatConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8960333205504644441L;
	private long statCfgId;// 统计配置ID
	private String statType;// 统计方式，0：框选；1：周边资源检索
	private String bizType;// 业务编码，保持与地图首页类型一致
	private String regionCode;// 地域编码
	private String categories;// 统计分类，支持英文逗号分隔，如：人,地,事,物,情
	private long creator;// 创建人ID
	private Date created;// 创建时间
	private String creatorName;// 创建人姓名
	private long updater;// 修改人ID
	private Date updated;// 修改时间
	private String updaterName;// 修改人姓名
	private String status;// 状态，0：删除；1：启用；2：禁用
	private String classification;// 是否分类，0：不分类；1：分类

	private String gridName;// 所属网格
	private String bizName;// 地图首页名称

	public long getStatCfgId() {
		return statCfgId;
	}

	public void setStatCfgId(long statCfgId) {
		this.statCfgId = statCfgId;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public long getCreator() {
		return creator;
	}

	public void setCreator(long creator) {
		this.creator = creator;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public long getUpdater() {
		return updater;
	}

	public void setUpdater(long updater) {
		this.updater = updater;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

}
