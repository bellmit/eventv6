package cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg;


import java.io.Serializable;
import java.util.List;

/**
 * 2015-9-23 liushi add
 * 地图统计热力图显示样式区间设置实体
 * @Table T_ZZ_THRESHOLD_COLOR_CFG
 * @author liushi
 */
public class ThresholdColorCfg implements Serializable{

	private static final long serialVersionUID = -7734223296801258647L;
	
	private Long tcId;//主键
	private Long gridId;//网格id
	private String gridName;//网格名称
	private String orgCode;//信息域code
	private String class_;//类别
	private String className;//
	private String subClass;//小类别
	private String colorVal;//16进制颜色
	private String status_;//状态 0：已删除，1：启用，2：禁用
	private String createTime;//
	private String updated;//
	private String remark_;//备注
	private Integer minValue;//值范围
	private Integer maxValue;//值范围
	private String colorNum;//颜色透明度
	private String paramCfgStr;//参数配置json格式
	private Integer indexNum;//排序
	
	public Integer getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(Integer indexNum) {
		this.indexNum = indexNum;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Long getGridId() {
		return gridId;
	}
	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	public Long getTcId() {
		return tcId;
	}
	public void setTcId(Long tcId) {
		this.tcId = tcId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getClass_() {
		return class_;
	}
	public void setClass_(String class_) {
		this.class_ = class_;
	}
	public String getSubClass() {
		return subClass;
	}
	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}
	public String getColorVal() {
		return colorVal;
	}
	public void setColorVal(String colorVal) {
		this.colorVal = colorVal;
	}
	public String getStatus_() {
		return status_;
	}
	public void setStatus_(String status_) {
		this.status_ = status_;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getRemark_() {
		return remark_;
	}
	public void setRemark_(String remark_) {
		this.remark_ = remark_;
	}
	public Integer getMinValue() {
		return minValue;
	}
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	public Integer getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	public String getColorNum() {
		return colorNum;
	}
	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}
	public String getParamCfgStr() {
		return paramCfgStr;
	}
	public void setParamCfgStr(String paramCfgStr) {
		this.paramCfgStr = paramCfgStr;
	}
	
	
}
