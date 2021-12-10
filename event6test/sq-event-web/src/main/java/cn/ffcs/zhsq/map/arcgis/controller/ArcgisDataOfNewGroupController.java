package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.grid.domain.db.Organization;
import cn.ffcs.shequ.grid.service.IOrganizationService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.zzgl.service.grid.ICorBaseInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.DataDictHelper;

/**
 * 2014-09-20 chenlf add
 * arcgis 两新组织 定位数据加载控制器
 * @author chenlf
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofnewgroup")
public class ArcgisDataOfNewGroupController {
	
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private ICorBaseInfoService corBaseInfoService;	
	@Autowired
	private IDictionaryService dictionaryService;
	///---两新组织 start----------------------------------------------
	
	/**
	 * 新社会组织
	 */
	@RequestMapping(value="/toArcgisDataListOfNewSocialOrg")
	public String toArcgisDataListOfNewSocialOrg(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/newOrg/standardNewSocialOrg.ftl";
	}
	
	/**
	 * 非公有制经济组织
	 */
	@RequestMapping(value="/toArcgisDataListOfNonPublicOrg")
	public String toArcgisDataListOfNonPublicOrg(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/newOrg/standardNonPublicOrg.ftl";
    }
	
	/**
	 * 非公有制经济组织(涉台)
	 */
	@RequestMapping(value="/toArcgisDataListOfNonPublicOrgOfTaiWan")
	public String toArcgisDataListOfNonPublicOrgOfTaiWan(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/newOrg/standardNonPublicOrgOfTaiwan.ftl";
    }
	
	/**
	 * 新社会组织列表信息
	 */
	@ResponseBody
    @RequestMapping(value="/newSocialOrgListData", method= RequestMethod.POST)
    public cn.ffcs.common.EUDGPagination newSocialOrgListData(
				    HttpSession session, @RequestParam(value = "page") int page,
				    @RequestParam(value = "rows") int rows,
				    @RequestParam(value = "gridName", required = false) String gridName,
				    @RequestParam(value = "orgName", required = false) String orgName,
				    @RequestParam(value = "gridId") Long gridId,
				    @RequestParam(value = "type", required = false) String type) {
	    if (page <= 0) page = 1;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gridName", gridName);
        params.put("orgName", orgName);
        params.put("type", type);
        params.put("gridId",gridId);
        cn.ffcs.common.EUDGPagination pagination = organizationService.findOrganizationPagination(page,rows,params);
        return pagination;
    }
	
	/**
	 * 查找非公有制经济组织信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param roomAddress
	 * @param placeType
	 * @param isFocus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/nonPublicOrgListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination nonPublicOrgListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="corName", required=false) String corName,@RequestParam(value="gridName", required=false) String gridName,
			@RequestParam(value="corCode", required=false) String corCode,@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="corType", required=false) String corType) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		if(corName!=null) corName = corName.trim();
		params.put("corName", corName);
		params.put("gridId", gridId);
		params.put("corCode", corCode);
		params.put("corType", corType);
		params.put("gridName", gridName);
		cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findNonPublicOrgPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 查找非公有制经济组织信息汇总表数据(涉台)
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param roomAddress
	 * @param placeType
	 * @param isFocus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/nonPublicOrgListDataOfTaiWan", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination nonPublicOrgListDataOfTaiWan(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="corName", required=false) String corName,@RequestParam(value="gridName", required=false) String gridName,
			@RequestParam(value="corCode", required=false) String corCode,@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="corType", required=false) String corType,@RequestParam(value="involveType", required=false) String involveType) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		if(corName!=null) corName = corName.trim();
		params.put("corName", corName);
		params.put("gridId", gridId);
		params.put("corCode", corCode);
		params.put("corType", corType);
		params.put("gridName", gridName);
		params.put("involveType", involveType);
		cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findNonPublicOrgPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 2014-07-8 chenlf add
	 * 获取两新组织的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfNewGroup")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfNewGroup(HttpSession session, 
			ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, 
			@RequestParam(value="type") String type, 
			@RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			) {
		
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisNewGroupLocateDataListByIds(ids,type,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfNewSociety")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfNewSociety(HttpSession session, 
			ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, 
			@RequestParam(value="type") String type, 
			@RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			) {
		
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisNewSocietyLocateDataListByIds(ids,type,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	
	/**
	 * 新社会组织 概要信息的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/newSocialOrgDetail")
	public String showNewSocialOrgDetail(HttpSession session, @RequestParam(value="orgId") Long orgId, ModelMap map) throws Exception{
		Organization organization = organizationService.getOrganizationById(orgId);
		DataDictHelper.setDictValueForField(organization, "type", "typeName", "B032", "");
    	DataDictHelper.setDictValueForField(organization, "principalCardType", "representativeCardName", "B010", "");
		DataDictHelper.setDictValueForField(organization, "focusDegee", "focusDegeeName", "B031", "");
		map.addAttribute("record",organization);
		return "/map/arcgis/standardmappage/newOrg/newSocialOrgDetail.ftl";
	}

	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	/**
	 * 新经济组织 概要信息的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/nonPublicOrgDetail")
	public String showNonPublicOrgDetail(HttpSession session, @RequestParam(value="orgId") Long orgId, ModelMap map) throws Exception{
		CorBaseInfo  corBaseInfo=new CorBaseInfo();
		corBaseInfo.setCbiId(orgId);
		corBaseInfo=corBaseInfoService.getCorpBaseInfo(corBaseInfo);
		
		Long gridId = corBaseInfo.getGridId();
		String infoOrgCode1 = corBaseInfo.getOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode1);
		}
		map.addAttribute("gridNames", gridNames);
		
		DataDictHelper.setDictValueForField(corBaseInfo, "corType", "corTypeName", "B029", "");
		DataDictHelper.setDictValueForField(corBaseInfo.getCorpExtInfo(), "hidDangerType", "hidDangerName", "B030", "");
		DataDictHelper.setDictValueForField(corBaseInfo.getCorpExtInfo(), "focusDegee", "focusDegeeName", "B031", "");
		DataDictHelper.setDictValueForField(corBaseInfo, "representativeCardType", "representativeCardName", "B010", "");
		map.addAttribute("record", corBaseInfo);
		return "/map/arcgis/standardmappage/newOrg/nonPublicOrgDetail.ftl";
	}
	
	///---两新组织 end----------------------------------------------
}
