package cn.ffcs.zhsq.map.menuconfigure.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisOrgAcc;
import cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure.MenuConfigMapper;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-10-09 liushi add 地图菜单配置服务
 * 
 * @author Administrator
 * 
 */
@Service(value = "menuConfigServiceImpl")
public class MenuConfigServiceImpl implements IMenuConfigService {
	@Autowired
	private MenuConfigMapper menuConfigMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 2014-10-09 liushi add 根据pid查询其子节点
	 * 
	 * @param param
	 *            gdcPid
	 * @return
	 */
	@Override
	public List<GisDataCfg> getGisDataCfgListByPid(Long gdcPid) {
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgListByPid(gdcPid);

		/*GisDataCfg parentGisDataCfg = new GisDataCfg();

		for (GisDataCfg gisDataCfg : list) {
			parentGisDataCfg = menuConfigMapper.getGisDataCfgById(gisDataCfg.getGdcPid());

			if (parentGisDataCfg != null) {
				gisDataCfg.setParentMenuName(parentGisDataCfg.getMenuName());
			}
		}*/

		return list;
	}
	
	@Override
	public List<GisDataCfg> getAllGisDataCfgsByPid(Long gdcPid) {
		List<GisDataCfg> list = this.menuConfigMapper.getAllGisDataCfgsByPid(gdcPid);
		return list;
	}

	@Override
	public List<GisDataCfg> getAllGisDataCfgListByPid(Long gdcPid) {
		List<GisDataCfg> list = this.menuConfigMapper.getAllGisDataCfgListByPid(gdcPid);
		return list;
	}
	
	@Override
	public List<GisDataCfg> getGisDataCfgListByPidAndKeywords(Long gdcPid, String keywords) {
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgListByPidAndKeywords(gdcPid, keywords);
		
		GisDataCfg parentGisDataCfg = menuConfigMapper.getGisDataCfgById(gdcPid);
		
		for (GisDataCfg gisDataCfg : list) {
			gisDataCfg.setParentMenuName(parentGisDataCfg.getMenuName());
		}
		
		return list;
	}

	/**
	 * 2014-10-11 liushi add 根据pid查询子节点的数量
	 * 
	 * @param gdcPid
	 * @return
	 */
	@Override
	public Integer getGisDataCfgCountByPid(Long gdcPid) {
		Integer count = this.menuConfigMapper.getGisDataCfgCountByPid(gdcPid);
		return count;
	}

	/**
	 * 2014-10-10 liushi add 根据id查询图层菜单信息
	 * 
	 * @param gdcId
	 * @return
	 */
	@Override
	public GisDataCfg getGisDataCfgById(Long gdcId) {
		GisDataCfg gisDataCfg = null;
		GisDataCfg parentGisDataCfg = null;
		
		if (gdcId != null) {
			gisDataCfg = this.menuConfigMapper.getGisDataCfgById(gdcId);
			
			if (gisDataCfg != null && gisDataCfg.getGdcPid() != null) {
				parentGisDataCfg = menuConfigMapper.getGisDataCfgById(gisDataCfg.getGdcPid());
				
				if (parentGisDataCfg != null) {
					gisDataCfg.setParentMenuName(parentGisDataCfg.getMenuName());
				}
			}
		}

		return gisDataCfg;
	}
	
	@Override
	public GisDataCfg getRootGisDataCfgById(Long gdcId) {
		GisDataCfg gisDataCfg = null;
		
		if (gdcId != null) {
			gisDataCfg = this.menuConfigMapper.getRootGisDataCfgById(gdcId);
		}

		return gisDataCfg;
	}

	/**
	 * 2014-10-10 liushi add 对图层菜单进行插入操作
	 * 
	 * @param gisDataCfg
	 * @return
	 */
	@Override
	public int insertGisDataCfg(GisDataCfg gisDataCfg) {
		int result = this.menuConfigMapper.insertGisDataCfg(gisDataCfg);
		return result;
	}

	/**
	 * 同一节点下菜单名称是否唯一
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int checkMenuName(Map<String, Object> params) {
		int result = this.menuConfigMapper.checkMenuName(params);
		return result;
	}

	/**
	 * 2014-10-10 liushi add 保存图层菜单与infoOrgCode的关系
	 * 
	 * @param gdcIdsStr
	 * @param org
	 * @return
	 */
	@Override
	public Boolean saveGisOrgAcc(String gdcIdsStr, String orgCode) {
		List<GisOrgAcc> gisOrgAccList = this.menuConfigMapper.getGisOrgAccList(orgCode);
		String gdcIdsOldStr = "";
		for (GisOrgAcc gisOrgAcc : gisOrgAccList) {
			gdcIdsOldStr = ("".equals(gdcIdsOldStr)) ? String.valueOf(gisOrgAcc.getGdcId()) : gdcIdsOldStr + ","
					+ String.valueOf(gisOrgAcc.getGdcId());
			if (("," + gdcIdsStr + ",").indexOf("," + String.valueOf(gisOrgAcc.getGdcId()) + ",") < 0) {
				this.menuConfigMapper.deleteGisOrgAcc(gisOrgAcc.getGdcId(), orgCode);
			}
		}
		String[] gdcIds = gdcIdsStr.split(",");
		for (String gdcId : gdcIds) {
			if (("," + gdcIdsOldStr + ",").indexOf("," + gdcId + ",") < 0) {
				this.menuConfigMapper.insertGisOrgAcc(Long.valueOf(gdcId), orgCode);
			}
		}
		return true;
	}

	@Override
	public Boolean saveGisOrgAccVersionNoe(String gdcIdsStr, String orgCode, String homePageType) {
		List<GisOrgAcc> gisOrgAccList = this.menuConfigMapper.getGisOrgAccListVersionNoe(orgCode, homePageType);
		String gdcIdsOldStr = "";
		for (GisOrgAcc gisOrgAcc : gisOrgAccList) {
			gdcIdsOldStr = ("".equals(gdcIdsOldStr)) ? String.valueOf(gisOrgAcc.getGdcId()) : gdcIdsOldStr + ","
					+ String.valueOf(gisOrgAcc.getGdcId());
			if (("," + gdcIdsStr + ",").indexOf("," + String.valueOf(gisOrgAcc.getGdcId()) + ",") < 0) {
				this.menuConfigMapper.deleteGisOrgAccVersionNoe(gisOrgAcc.getGdcId(), orgCode, homePageType);
			}
		}
		String[] gdcIds = gdcIdsStr.split(",");
		for (String gdcId : gdcIds) {
			if (("," + gdcIdsOldStr + ",").indexOf("," + gdcId + ",") < 0) {
				this.menuConfigMapper.insertGisOrgAccVersionNoe(Long.valueOf(gdcId), orgCode, homePageType);
			}
		}
		return true;
	}

	/**
	 * 2014-10-10 liushi add 对图层菜单进行修改操作
	 * 
	 * @param gisDataCfg
	 * @return
	 */
	@Override
	public int updateGisDataCfg(GisDataCfg gisDataCfg) {
		int result = this.menuConfigMapper.updateGisDataCfg(gisDataCfg);
		return result;
	}

	/**
	 * 2014-10-11 liushi add 根据id删除对应的图层菜单
	 * 
	 * @param gisDataCfg
	 * @return
	 */
	@Override
	public int deleteGisDataCfg(Long gdcId) {
		int result = this.menuConfigMapper.deleteGisDataCfg(gdcId);
		return result;
	}

	/**
	 * 2014-10-13 liushi add 根据机构编码orgCode获取配置的id信息
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccListByOrgCode(String orgCode) {
		List<GisOrgAcc> list = this.menuConfigMapper.getGisOrgAccList(orgCode);
		return list;
	}

	/**
	 * 2014-10-20 liushi add 查询机构关联树用于页面首页提取菜单
	 * 
	 * @param orgCode
	 * @param gdcPid
	 * @return
	 */
	@Override
	public GisDataCfg getGisDataCfgRelationTree(String orgCode, Long gdcId) {
		GisDataCfg gisDataCfg = new GisDataCfg();
		// 查询与当前网格距离最近的上级（或本级）关联的根节点，确定当前使用的网格orgCode以及根节点
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgRelationRootList(orgCode, gdcId);
		if (list.size() > 0) {
			gisDataCfg = list.get(0);
		}
		List<GisDataCfg> childrenList = this.getGisDataCfgTreeNodes(gisDataCfg.getOrgCode(), gisDataCfg.getGdcId());
		if (childrenList.size() > 0) {
			String childrenGdcIds = ",";
			for (GisDataCfg childGisDataCfg : childrenList) {
				childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
			}
			gisDataCfg.setChildrenList(childrenList);
			gisDataCfg.setChildrenGdcIds(childrenGdcIds);
			return gisDataCfg;
		} else {
			return gisDataCfg;
		}

	}

	public List<GisDataCfg> getGisDataCfgTreeNodes(String orgCode, Long gdcPid) {
		List<GisDataCfg> gisDataCfgList = this.menuConfigMapper.getGisDataCfgRelationListByPid(orgCode, gdcPid);
		for (GisDataCfg gisDataCfg : gisDataCfgList) {
			List<GisDataCfg> childrenList = this.getGisDataCfgTreeNodes(gisDataCfg.getOrgCode(), gisDataCfg.getGdcId());
			if (childrenList.size() > 0) {
				String childrenGdcIds = ",";
				for (GisDataCfg childGisDataCfg : childrenList) {
					childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
				}
				gisDataCfg.setChildrenList(childrenList);
				gisDataCfg.setChildrenGdcIds(childrenGdcIds);
			}
		}
		return gisDataCfgList;
	}

	/**
	 * liushi add 根据组织、首页类型、父节点id查询图层菜单
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	@Override
	public GisDataCfg getGisDataCfgRelationTreeVersionNoe(String orgCode, String homePageType, Long gdcId) {
		boolean isNew = true;
		if (isNew) {
			GisDataCfg gisDataCfg = new GisDataCfg();
			gisDataCfg.setMenuCode("ROOT");
			gisDataCfg.setGdcId(gdcId);
			gisDataCfg.setOrgCode(orgCode);
			List<GisDataCfg> childrenList = this.menuConfigMapper.getNewGisDataCfgListForRoot(orgCode, homePageType);
			if (childrenList.size() > 0) {
				for (GisDataCfg descGisDataCfg : childrenList) {
					if (descGisDataCfg.getGdcId() > 0) {
						String elementsCollectionStr = "";
						elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
						elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
						elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
						elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
						elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
						elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
						elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
						elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
						elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
						elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
						elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
						elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
						elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
						elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
						elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
						elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
						elementsCollectionStr += "convergeUrl_,_" + gisDataCfg.getConvergeUrl() + ",_,";
						elementsCollectionStr += "scatterPointUrl_,_" + gisDataCfg.getScatterPointUrl() + ",_,";
						elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
						elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
						descGisDataCfg.setElementsCollectionStr(elementsCollectionStr);
						String callBackStr = descGisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
						descGisDataCfg.setOrgCode(orgCode);
						descGisDataCfg.setCallBack(callBackStr);
						descGisDataCfg.setChildrenList(this.getGisDataCfgTreeNodesVersionNoe(orgCode, homePageType, descGisDataCfg.getGdcId()));
					}
				}
				gisDataCfg.setChildrenList(childrenList);
				return gisDataCfg;
			} else {
				gisDataCfg.setChildrenList(new ArrayList<GisDataCfg>());
				return gisDataCfg;
			}
		} else {
			GisDataCfg gisDataCfg = new GisDataCfg();
			// 查询与当前网格距离最近的上级（或本级）关联的根节点，确定当前使用的网格orgCode以及根节点
			List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgRelationRootListVersionNoe(orgCode, homePageType,
					gdcId);
			
			if (!list.isEmpty()) {
				gisDataCfg = list.get(0);
				List<GisDataCfg> childrenList = this.getGisDataCfgTreeNodesVersionNoe(gisDataCfg.getOrgCode(), homePageType,
						gisDataCfg.getGdcId());
				
				if (childrenList.size() > 0) {
					String childrenGdcIds = ",";
					for (GisDataCfg childGisDataCfg : childrenList) {
						childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
					}
					gisDataCfg.setChildrenList(childrenList);
					gisDataCfg.setChildrenGdcIds(childrenGdcIds);
					return gisDataCfg;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
	
	@Override
	public GisDataCfg getGisDataCfgsByPid(Long gdcPid) {
		GisDataCfg gisDataCfg = new GisDataCfg();
		List<GisDataCfg> childrenList = this.getGisDataCfgs(gdcPid);
		if (childrenList.size() > 0) {
			String childrenGdcIds = ",";
			for (GisDataCfg childGisDataCfg : childrenList) {
				childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
			}
			gisDataCfg.setChildrenList(childrenList);
			gisDataCfg.setChildrenGdcIds(childrenGdcIds);
			return gisDataCfg;
		} else {
			return null;
		}
	}

	@Override
	public GisDataCfg getGisDataCfgRelationTreeVersionTwo(String orgCode, String homePageType, Long gdcId,
			Integer isRootSearch) {
		boolean isNew = true;
		GisDataCfg gisDataCfg = new GisDataCfg();
		if (isNew) {
			gisDataCfg.setMenuCode("ROOT");
			gisDataCfg.setGdcId(gdcId);
			gisDataCfg.setOrgCode(orgCode);
			List<GisDataCfg> childrenList = null;
			if (isRootSearch.equals(1)) {
				childrenList = this.menuConfigMapper.getNewGisDataCfgListForRoot(orgCode, homePageType);
			} else {
				childrenList = this.menuConfigMapper.getNewGisDataCfgListByPid(orgCode, homePageType, gdcId);
			}
			if (childrenList.size() > 0) {
				for (GisDataCfg descGisDataCfg : childrenList) {
					if (descGisDataCfg.getGdcId() > 0) {
						String elementsCollectionStr = "";
						elementsCollectionStr += "gdcId_,_" + String.valueOf(descGisDataCfg.getGdcId()) + ",_,";
						elementsCollectionStr += "orgCode_,_" + orgCode + ",_,";
						elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
						elementsCollectionStr += "largeIco_,_" + descGisDataCfg.getLargeIco() + ",_,";
						elementsCollectionStr += "smallIco_,_" + descGisDataCfg.getSmallIco() + ",_,";
						elementsCollectionStr += "treeIcon_,_" + descGisDataCfg.getTreeIcon() + ",_,";
						elementsCollectionStr += "menuCode_,_" + descGisDataCfg.getMenuCode() + ",_,";
						elementsCollectionStr += "menuName_,_" + descGisDataCfg.getMenuName() + ",_,";
						elementsCollectionStr += "smallIcoSelected_,_" + descGisDataCfg.getSmallIcoSelected() + ",_,";
						elementsCollectionStr += "menuListUrl_,_" + descGisDataCfg.getMenuListUrl() + ",_,";
						elementsCollectionStr += "menuSummaryUrl_,_" + descGisDataCfg.getMenuSummaryUrl() + ",_,";
						elementsCollectionStr += "menuLayerName_,_" + descGisDataCfg.getMenuLayerName() + ",_,";
						elementsCollectionStr += "menuDetailUrl_,_" + descGisDataCfg.getMenuDetailUrl() + ",_,";
						elementsCollectionStr += "menuDetailWidth_,_" + descGisDataCfg.getMenuDetailWidth() + ",_,";
						elementsCollectionStr += "menuDetailHeight_,_" + descGisDataCfg.getMenuDetailHeight() + ",_,";
						elementsCollectionStr += "menuSummaryWidth_,_" + descGisDataCfg.getMenuSummaryWidth() + ",_,";
						elementsCollectionStr += "menuSummaryHeight_,_" + descGisDataCfg.getMenuSummaryHeight() + ",_,";
						elementsCollectionStr += "convergeUrl_,_" + descGisDataCfg.getConvergeUrl() + ",_,";
						elementsCollectionStr += "scatterPointUrl_,_" + descGisDataCfg.getScatterPointUrl() + ",_,";
						elementsCollectionStr += "remark_,_" + descGisDataCfg.getRemark() + ",_,";
						elementsCollectionStr += "callBack_,_" + descGisDataCfg.getCallBack() + ",_,";
						descGisDataCfg.setElementsCollectionStr(elementsCollectionStr);
						String callBackStr = descGisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
						descGisDataCfg.setOrgCode(orgCode);
						descGisDataCfg.setCallBack(callBackStr);
					}
				}
				
				gisDataCfg.setChildrenList(childrenList);
				return gisDataCfg;
			} else {
				gisDataCfg.setChildrenList(new ArrayList<GisDataCfg>());
				return gisDataCfg;
			}
		}
		if (isRootSearch.equals(1)) {
			// 查询与当前网格距离最近的上级（或本级）关联的根节点，确定当前使用的网格orgCode以及根节点
			List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgRelationRootListVersionNoe(orgCode, homePageType, gdcId);
			gisDataCfg = list.get(0);
		} else {
			gisDataCfg.setGdcId(gdcId);
			gisDataCfg.setOrgCode(orgCode);
		}
		List<GisDataCfg> childrenList = this.getGisDataCfgTreeNodesVersionTwo(gisDataCfg.getOrgCode(), homePageType,
				gisDataCfg.getGdcId());
		if (childrenList.size() > 0) {
			gisDataCfg.setChildrenList(childrenList);
			return gisDataCfg;
		} else {
			return null;
		}
	}
	
	@Override
	public List<GisDataCfg> getGisDataCfgTree(String orgCode, String homePageType) {
		List<GisDataCfg> gisDataCfgs = this.menuConfigMapper.getGisDataCfgTree(orgCode, homePageType);
		
		if (gisDataCfgs.size() > 0) {
			for (GisDataCfg descGisDataCfg : gisDataCfgs) {
				if (descGisDataCfg.getGdcId() > 0) {
					String elementsCollectionStr = "";
					elementsCollectionStr += "gdcId_,_" + String.valueOf(descGisDataCfg.getGdcId()) + ",_,";
					elementsCollectionStr += "orgCode_,_" + orgCode + ",_,";
					elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
					elementsCollectionStr += "largeIco_,_" + descGisDataCfg.getLargeIco() + ",_,";
					elementsCollectionStr += "smallIco_,_" + descGisDataCfg.getSmallIco() + ",_,";
					elementsCollectionStr += "treeIcon_,_" + descGisDataCfg.getTreeIcon() + ",_,";
					elementsCollectionStr += "menuCode_,_" + descGisDataCfg.getMenuCode() + ",_,";
					elementsCollectionStr += "menuName_,_" + descGisDataCfg.getMenuName() + ",_,";
					elementsCollectionStr += "smallIcoSelected_,_" + descGisDataCfg.getSmallIcoSelected() + ",_,";
					elementsCollectionStr += "menuListUrl_,_" + descGisDataCfg.getMenuListUrl() + ",_,";
					elementsCollectionStr += "menuSummaryUrl_,_" + descGisDataCfg.getMenuSummaryUrl() + ",_,";
					elementsCollectionStr += "menuLayerName_,_" + descGisDataCfg.getMenuLayerName() + ",_,";
					elementsCollectionStr += "menuDetailUrl_,_" + descGisDataCfg.getMenuDetailUrl() + ",_,";
					elementsCollectionStr += "menuDetailWidth_,_" + descGisDataCfg.getMenuDetailWidth() + ",_,";
					elementsCollectionStr += "menuDetailHeight_,_" + descGisDataCfg.getMenuDetailHeight() + ",_,";
					elementsCollectionStr += "menuSummaryWidth_,_" + descGisDataCfg.getMenuSummaryWidth() + ",_,";
					elementsCollectionStr += "menuSummaryHeight_,_" + descGisDataCfg.getMenuSummaryHeight() + ",_,";
					elementsCollectionStr += "convergeUrl_,_" + descGisDataCfg.getConvergeUrl() + ",_,";
					elementsCollectionStr += "scatterPointUrl_,_" + descGisDataCfg.getScatterPointUrl() + ",_,";
					elementsCollectionStr += "remark_,_" + descGisDataCfg.getRemark() + ",_,";
					elementsCollectionStr += "callBack_,_" + descGisDataCfg.getCallBack() + ",_,";
					descGisDataCfg.setElementsCollectionStr(elementsCollectionStr);
					String callBackStr = descGisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
					descGisDataCfg.setOrgCode(orgCode);
					descGisDataCfg.setCallBack(callBackStr);
				}
			}
		}
		
		return gisDataCfgs;
	}

	public List<GisDataCfg> getGisDataCfgTreeNodesVersionTwo(String orgCode, String homePageType, Long gdcPid) {
		List<GisDataCfg> gisDataCfgList = this.menuConfigMapper.getGisDataCfgRelationListByPidVersionNoe(orgCode,
				homePageType, gdcPid);
		// this.replaceDomainUrl(gisDataCfgList);
		for (GisDataCfg gisDataCfg : gisDataCfgList) {
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
			elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "convergeUrl_,_" + gisDataCfg.getConvergeUrl() + ",_,";
			elementsCollectionStr += "scatterPointUrl_,_" + gisDataCfg.getScatterPointUrl() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
		}
		return gisDataCfgList;
	}

	public List<GisDataCfg> getGisDataCfgs(Long gdcPid) {
		List<GisDataCfg> gisDataCfgList = this.menuConfigMapper.getGisDataCfgListByPid(gdcPid);
		for (GisDataCfg gisDataCfg : gisDataCfgList) {
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
			List<GisDataCfg> childrenList = this.getGisDataCfgs(gisDataCfg.getGdcId());
			if (childrenList.size() > 0) {
				String childrenGdcIds = ",";
				for (GisDataCfg childGisDataCfg : childrenList) {
					childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
				}
				gisDataCfg.setChildrenList(childrenList);
				gisDataCfg.setChildrenGdcIds(childrenGdcIds);
			}
		}
		return gisDataCfgList;
	}
	
	public List<GisDataCfg> getGisDataCfgTreeNodesVersionNoe(String orgCode, String homePageType, Long gdcPid) {
		List<GisDataCfg> gisDataCfgList = this.menuConfigMapper.getNewGisDataCfgListByPid(orgCode, homePageType, gdcPid);
		/*List<GisDataCfg> gisDataCfgList = this.menuConfigMapper.getGisDataCfgRelationListByPidVersionNoe(orgCode,
				homePageType, gdcPid);*/
		// this.replaceDomainUrl(gisDataCfgList);
		for (GisDataCfg gisDataCfg : gisDataCfgList) {
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
			List<GisDataCfg> childrenList = this.getGisDataCfgTreeNodesVersionNoe(gisDataCfg.getOrgCode(),
					homePageType, gisDataCfg.getGdcId());
			if (childrenList.size() > 0) {
				String childrenGdcIds = ",";
				for (GisDataCfg childGisDataCfg : childrenList) {
					childrenGdcIds = childrenGdcIds + String.valueOf(childGisDataCfg.getGdcId());
				}
				gisDataCfg.setChildrenList(childrenList);
				gisDataCfg.setChildrenGdcIds(childrenGdcIds);
			}
		}
		return gisDataCfgList;
	}

	/**
	 * 需要获取session 废弃
	 */
	private void replaceDomainUrl(List<GisDataCfg> list) {
		for (GisDataCfg obj : list) {
			String menuListUrl = obj.getMenuListUrl();
			String menuSummaryUrl = obj.getMenuSummaryUrl();
			String menuDetailUrl = obj.getMenuDetailUrl();
			if (menuListUrl != null && !"".equals(menuListUrl)) {
				String menuListUrlDomain = menuListUrl.split("/")[0];
				menuListUrl = menuListUrl.replaceFirst(menuListUrlDomain, Enum.valueOf(App.class, menuListUrlDomain)
						.getDebugUrl());
				obj.setMenuListUrl(menuListUrl);
			}
			if (menuSummaryUrl != null && !"".equals(menuSummaryUrl)) {
				String menuSummaryUrlDomain = menuSummaryUrl.split("/")[0];
				menuSummaryUrl = menuSummaryUrl.replaceFirst(menuSummaryUrlDomain,
						Enum.valueOf(App.class, menuSummaryUrlDomain).getDebugUrl());
				obj.setMenuSummaryUrl(menuSummaryUrl);
			}
			if (menuDetailUrl != null && !"".equals(menuDetailUrl)) {
				String menuDetailUrlDomain = menuDetailUrl.split("/")[0];
				menuDetailUrl = menuDetailUrl.replaceFirst(menuDetailUrlDomain,
						Enum.valueOf(App.class, menuDetailUrlDomain).getDebugUrl());
				obj.setMenuDetailUrl(menuDetailUrl);
			}
		}
	}

	/**
	 * 2015-03-30 liushi add 根据机构编码orgCode和首页类型获取配置的id信息
	 * 
	 * @param orgCode
	 * @return
	 */
	@Override
	public List<GisOrgAcc> getGisOrgAccListByOrgCodeVersionNoe(String orgCode, String homePageType) {
		List<GisOrgAcc> list = this.menuConfigMapper.getGisOrgAccListVersionNoe(orgCode, homePageType);
		return list;
	}

	/**
	 * 2015-06-05 liushi add 根据组织、首页类型、父节点id查询图层名称
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	@Override
	public String getGisDataLayerNameVersionNoe(String orgCode, String homePageType, Long gdcId) {
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataLayerNameVersionNoe(orgCode, homePageType, gdcId);
		String resultStr = "";
		for (GisDataCfg gisDataCfg : list) {
			if (resultStr.indexOf("gisDataCfg.getMenuLayerName()") < 0) {
				resultStr = resultStr + gisDataCfg.getMenuLayerName() + ",";
			}
		}
		return resultStr;
	}

	@Override
	public GisDataCfg getGisDataCfgByKXServiceName(String orgCode, String homePageType, String serviceName) {
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgByKXServiceName(orgCode, homePageType, serviceName);
		GisDataCfg gisDataCfg = new GisDataCfg();
		if (list.size() > 0) {
			gisDataCfg = list.get(0);
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
			elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "convergeUrl_,_" + gisDataCfg.getConvergeUrl() + ",_,";
			elementsCollectionStr += "scatterPointUrl_,_" + gisDataCfg.getScatterPointUrl() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
			return gisDataCfg;
		} else {
			return null;
		}

	}

	@Override
	public GisDataCfg getGisDataCfgByCode(String menuCode, String orgCode) {
		GisDataCfg gisDataCfg = this.menuConfigMapper.getGisDataCfgByCode(menuCode);
		if (gisDataCfg != null) {
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
			return gisDataCfg;
		}
		return null;
	}

	@Override
	public GisDataCfg getGisDataCfgByZBServiceName(String orgCode, String homePageType, String serviceName) {
		List<GisDataCfg> list = this.menuConfigMapper.getGisDataCfgByZBServiceName(orgCode, homePageType, serviceName);
		GisDataCfg gisDataCfg = new GisDataCfg();
		if (list.size() > 0) {
			gisDataCfg = list.get(0);
			String elementsCollectionStr = "";
			elementsCollectionStr += "gdcId_,_" + String.valueOf(gisDataCfg.getGdcId()) + ",_,";
			elementsCollectionStr += "orgCode_,_" + gisDataCfg.getOrgCode() + ",_,";
			elementsCollectionStr += "homePageType_,_" + homePageType + ",_,";
			elementsCollectionStr += "largeIco_,_" + gisDataCfg.getLargeIco() + ",_,";
			elementsCollectionStr += "smallIco_,_" + gisDataCfg.getSmallIco() + ",_,";
			elementsCollectionStr += "treeIcon_,_" + gisDataCfg.getTreeIcon() + ",_,";
			elementsCollectionStr += "menuCode_,_" + gisDataCfg.getMenuCode() + ",_,";
			elementsCollectionStr += "menuName_,_" + gisDataCfg.getMenuName() + ",_,";
			elementsCollectionStr += "smallIcoSelected_,_" + gisDataCfg.getSmallIcoSelected() + ",_,";
			elementsCollectionStr += "menuListUrl_,_" + gisDataCfg.getMenuListUrl() + ",_,";
			elementsCollectionStr += "menuSummaryUrl_,_" + gisDataCfg.getMenuSummaryUrl() + ",_,";
			elementsCollectionStr += "menuLayerName_,_" + gisDataCfg.getMenuLayerName() + ",_,";
			elementsCollectionStr += "menuDetailUrl_,_" + gisDataCfg.getMenuDetailUrl() + ",_,";
			elementsCollectionStr += "menuDetailWidth_,_" + gisDataCfg.getMenuDetailWidth() + ",_,";
			elementsCollectionStr += "menuDetailHeight_,_" + gisDataCfg.getMenuDetailHeight() + ",_,";
			elementsCollectionStr += "menuSummaryWidth_,_" + gisDataCfg.getMenuSummaryWidth() + ",_,";
			elementsCollectionStr += "menuSummaryHeight_,_" + gisDataCfg.getMenuSummaryHeight() + ",_,";
			elementsCollectionStr += "convergeUrl_,_" + gisDataCfg.getConvergeUrl() + ",_,";
			elementsCollectionStr += "scatterPointUrl_,_" + gisDataCfg.getScatterPointUrl() + ",_,";
			elementsCollectionStr += "remark_,_" + gisDataCfg.getRemark() + ",_,";
			elementsCollectionStr += "callBack_,_" + gisDataCfg.getCallBack() + ",_,";
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			String callBackStr = gisDataCfg.getCallBack() + "('" + elementsCollectionStr + "')";
			gisDataCfg.setCallBack(callBackStr);
			return gisDataCfg;
		} else {
			return null;
		}
	}
	
	@Override
	public String getDisplayStyle(String orgCode, String homePageType) {
		return menuConfigMapper.getDisplayStyle(orgCode, homePageType);
	}
	
	@Override
	public boolean saveSort(List<String> ids) {
		String sql = "UPDATE T_GIS_DATA_CFG SET SORT_ = ? WHERE GDC_ID = ?";
		List<Object[]> args = new ArrayList<Object[]>();
		for (int i = 0; i < ids.size(); i++) {
			Object[] pms = new Object[2];
			pms[0] = i + 1;
			pms[1] = ids.get(i);
			args.add(pms);
		}
		return jdbcTemplate.batchUpdate(sql, args).length == ids.size();
	}

	@Override
	public EUDGPagination findLadderDiagrmData(Map<String, Object> map) {
		List<Map<String, Object>> list = menuConfigMapper.getLadderDiagrmData(map);
		return new EUDGPagination(list.size(),list);
	}

	@Transactional
	@Override
	public Boolean saveLadderDiagrm(Map<String, Object> map) {
		Boolean flag = false;
		String mapType = map.get("mapType").toString();
		if (mapType!=null && "1".equals(mapType)) {
			map.put("mapName", "矢量图");
			map.put("layerId_a", "vec");
			map.put("layerId_b", "cva");
		} else {
			map.put("mapName", "影像图");
			map.put("layerId_a", "img");
			map.put("layerId_b", "cia");
		}
		map.put("url", "http://t0.tianditu.com/");
		flag = flag || menuConfigMapper.saveLadderDiagrmAcoi(map);
		flag = flag && menuConfigMapper.saveLadderDiagrmAsei(map);
		flag = flag && menuConfigMapper.saveLadderDiagrmAsci();
		return flag;
	}

	@Transactional
	@Override
	public int deleteLadderDiagrm(Long id) {
		int result = 0;
		result = menuConfigMapper.deleteLadderDiagrmAcoi(id);
		result = result + menuConfigMapper.deleteLadderDiagrmAsei(id);
		result = result + menuConfigMapper.deleteLadderDiagrmAAsci(id);
		return result;
	}

}
