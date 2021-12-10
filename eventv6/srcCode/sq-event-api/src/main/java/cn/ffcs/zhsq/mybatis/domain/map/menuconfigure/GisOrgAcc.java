package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;


import java.io.Serializable;
import java.util.List;

/**
 * 2014-10-09 liushi add
 * 地图菜单机构关联关系
 * @Table Map_Config_Gis
 * @author liushi
 */
public class GisOrgAcc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1197177352579078343L;
	private Long gdcId;//地图菜单信息主键
	private String orgCode;//组织code
	private String isNewConfig;//是否新的配置项
	private String homePageType;//首页类型
	
	public String getIsNewConfig() {
		return isNewConfig;
	}
	public void setIsNewConfig(String isNewConfig) {
		this.isNewConfig = isNewConfig;
	}
	public String getHomePageType() {
		return homePageType;
	}
	public void setHomePageType(String homePageType) {
		this.homePageType = homePageType;
	}
	public Long getGdcId() {
		return gdcId;
	}
	public void setGdcId(Long gdcId) {
		this.gdcId = gdcId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	
}
