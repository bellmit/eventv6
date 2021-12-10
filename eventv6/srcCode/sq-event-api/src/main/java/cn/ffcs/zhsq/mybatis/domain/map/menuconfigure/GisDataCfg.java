package cn.ffcs.zhsq.mybatis.domain.map.menuconfigure;

import java.io.Serializable;
import java.util.List;

/**
 * 2014-10-09 liushi add 地图菜单信息
 * 
 * @Table T_GIS_DATA_CFG
 * @author liushi
 */
public class GisDataCfg implements Serializable {

	private static final long serialVersionUID = -7734223296801258647L;

	private Long gdcId;// 地图菜单信息主键
	private String callBack;// 回调地址
	private String className;// 样式名称
	private String largeIco;// 菜单图标
	private String treeIcon;//树形小图标
	private String smallIco;// 定位图标
	private Long gdcPid;// 父节点
	private String isLeaf;// 是否叶子节点，叶子节点1 非叶子节点0
	private String level;// 层级
	private String status;// 状态 有效：001 无效： 003
	private String menuCode;// 菜单Code
	private String menuName;// 菜单名称
	private String parentMenuName;// 所属上级菜单
	private String smallIcoSelected;// 定位图标被选中时
	private Integer sort;// 排序
	private String remark;
	private List<GisDataCfg> childrenList;// 子节点list
	private String childrenGdcIds;// 子节点gdcId的集合
	private String orgCode;// 信息域code，第二版时改为组织code
	private String kuangxuanName;// 框选服务注册名称
	private String zhoubianName;// 周边资源服务注册名称
	private String zhoubianIsShowList;// 周边资源是否显示列表
	private String menuListUrl;// 图层列表链接url
	private String menuSummaryUrl;// 概要信息链接url
	private String menuLayerName;// 地图图层名称
	private String menuDetailUrl;// 详细信息url
	private String menuDetailWidth;// 详细信息宽度
	private String menuDetailHeight;// 详细信息高度
	private String menuSummaryWidth;// 概要信息宽度
	private String menuSummaryHeight;// 概要信息高度
	private String menuDetailName; // 菜单详细名称
	private String elementsCollectionStr;// 元素集合对象的有效成员变量以及值的集合组成的字符串
	private String convergeUrl;//汇聚URL
	private String scatterPointUrl;//汇聚撒点URL

	public String getMenuSummaryWidth() {
		return menuSummaryWidth;
	}

	public void setMenuSummaryWidth(String menuSummaryWidth) {
		this.menuSummaryWidth = menuSummaryWidth;
	}

	public String getMenuSummaryHeight() {
		return menuSummaryHeight;
	}

	public void setMenuSummaryHeight(String menuSummaryHeight) {
		this.menuSummaryHeight = menuSummaryHeight;
	}

	public String getMenuDetailWidth() {
		return menuDetailWidth;
	}

	public void setMenuDetailWidth(String menuDetailWidth) {
		this.menuDetailWidth = menuDetailWidth;
	}

	public String getMenuDetailHeight() {
		return menuDetailHeight;
	}

	public void setMenuDetailHeight(String menuDetailHeight) {
		this.menuDetailHeight = menuDetailHeight;
	}

	public String getElementsCollectionStr() {
		return elementsCollectionStr;
	}

	public void setElementsCollectionStr(String elementsCollectionStr) {
		this.elementsCollectionStr = elementsCollectionStr;
	}

	public String getMenuListUrl() {
		return menuListUrl;
	}

	public void setMenuListUrl(String menuListUrl) {
		this.menuListUrl = menuListUrl;
	}

	public String getMenuSummaryUrl() {
		return menuSummaryUrl;
	}

	public void setMenuSummaryUrl(String menuSummaryUrl) {
		this.menuSummaryUrl = menuSummaryUrl;
	}

	public String getMenuLayerName() {
		return menuLayerName;
	}

	public void setMenuLayerName(String menuLayerName) {
		this.menuLayerName = menuLayerName;
	}

	public String getMenuDetailUrl() {
		return menuDetailUrl;
	}

	public void setMenuDetailUrl(String menuDetailUrl) {
		this.menuDetailUrl = menuDetailUrl;
	}

	public String getZhoubianIsShowList() {
		return zhoubianIsShowList;
	}

	public void setZhoubianIsShowList(String zhoubianIsShowList) {
		this.zhoubianIsShowList = zhoubianIsShowList;
	}

	public String getZhoubianName() {
		return zhoubianName;
	}

	public void setZhoubianName(String zhoubianName) {
		this.zhoubianName = zhoubianName;
	}

	public String getKuangxuanName() {
		return kuangxuanName;
	}

	public void setKuangxuanName(String kuangxuanName) {
		this.kuangxuanName = kuangxuanName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public List<GisDataCfg> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<GisDataCfg> childrenList) {
		this.childrenList = childrenList;
	}

	public String getChildrenGdcIds() {
		return childrenGdcIds;
	}

	public void setChildrenGdcIds(String childrenGdcIds) {
		this.childrenGdcIds = childrenGdcIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getGdcId() {
		return gdcId;
	}

	public void setGdcId(Long gdcId) {
		this.gdcId = gdcId;
	}

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

	public String getLargeIco() {
		return largeIco;
	}

	public void setLargeIco(String largeIco) {
		this.largeIco = largeIco;
	}

	public String getSmallIco() {
		return smallIco;
	}

	public void setSmallIco(String smallIco) {
		this.smallIco = smallIco;
	}

	public Long getGdcPid() {
		return gdcPid;
	}

	public void setGdcPid(Long gdcPid) {
		this.gdcPid = gdcPid;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getSmallIcoSelected() {
		return smallIcoSelected;
	}

	public void setSmallIcoSelected(String smallIcoSelected) {
		this.smallIcoSelected = smallIcoSelected;
	}

	public String getMenuDetailName() {
		return menuDetailName;
	}

	public void setMenuDetailName(String menuDetailName) {
		this.menuDetailName = menuDetailName;
	}

	public String getParentMenuName() {
		return parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public String getTreeIcon() {
		return treeIcon;
	}

	public void setTreeIcon(String treeIcon) {
		this.treeIcon = treeIcon;
	}

	public String getConvergeUrl() {
		return convergeUrl;
	}

	public void setConvergeUrl(String convergeUrl) {
		this.convergeUrl = convergeUrl;
	}

	public String getScatterPointUrl() {
		return scatterPointUrl;
	}

	public void setScatterPointUrl(String scatterPointUrl) {
		this.scatterPointUrl = scatterPointUrl;
	}

}
