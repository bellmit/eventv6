package cn.ffcs.zhsq.map.arcgis.controller;

/**
 * 
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import cn.ffcs.gmis.ctIllegalConstruction.service.ICtIllegalConstructionService;
import cn.ffcs.gmis.logisticsSafety.service.ILogisticsSafetyService;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.grid.service.ICasesService;
import cn.ffcs.shequ.grid.service.IOrganizationService;
import cn.ffcs.shequ.mybatis.domain.ict.SafeCheckDetail;
import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.workflow.ict.service.ISafeCheckDetailService;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-06-23 chenlf add
 * arcgis 警情相关 定位数据加载控制器
 * @author chenlf
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfSocietyController")
public class ArcgisDataOfSocietyController extends ZZBaseController {
	
	@Autowired 
	protected IGisInfoService gisInfoService; //gis地图相关服务 
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private ICasesService casesService;
	@Autowired
	private ISafeCheckDetailService safeCheckDetailService;
	@Autowired
	private ICtIllegalConstructionService ctIllegalConstructionService;
	@Autowired
	private ILogisticsSafetyService logisticsSafetyService;
	@Autowired
	private IDisputeMediationService disputeMediationService;
	
	/**
	 * 巡逻段警数据页
	 * @param session
	 * @param gridId
	 * @param duty
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/standardXldj")
	public String standardXldj(HttpSession session, @RequestParam(value="gridId") Long gridId, 
			@RequestParam(value="duty", required=false) String duty, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if (!StringUtils.isEmpty(duty)) {
			map.addAttribute("duty",duty);
			map.addAttribute("photo", "xlgj"); 
		}else {
			map.addAttribute("photo", "admingrid");
		}
		return "/map/arcgis/standardmappage/standardXldj.ftl";
	}
		
	 /**
	    * @Title: index
	    * @Description: 巡逻队、警务室、治保组织等社会组织列表
	    * @param @param session
	    * @param @param gridId
	    * @param @param resTypeId
	    * @param @param map
	    * @param @return
	    * @return String
	    * @throws
	     */
		@RequestMapping(value="/standardSociety")
		public String standardSocial(HttpSession session, @RequestParam(value="gridId") Long gridId, @RequestParam(value="type", required=false) String type,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
			if((type == null || "".equals(type))&&(elementsCollectionStr!=null||"".equals(elementsCollectionStr))){
				String menuCode = this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
				if("xld".equals(menuCode)) {
					type="05";
				}else if("zbh".equals(menuCode)) {
					type="03";
				}else if("jws".equals(menuCode)) {
					type="04";
				}
			}
			map.addAttribute("gridId", gridId);
			map.addAttribute("type", type);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			return "/map/arcgis/standardmappage/standardSociety.ftl";
		}
		
		
		/**
		* @Title: listData
		* @Description: 图层获取巡逻队、警务室、治保组织等社会组织列表
		* @param @param session
		* @param @param page
		* @param @param rows
		* @param @param resTypeId
		* @param @param gridId
		* @param @param resName
		* @param @return
		* @return EUDGPagination
		* @throws
		 */
		@ResponseBody
		@RequestMapping(value="/societyListData", method=RequestMethod.POST)
		public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
				@RequestParam(value="type") String type, @RequestParam(value="gridId") Long gridId,
				@RequestParam(value="resName", required=false) String resName,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			if((type == null || "".equals(type))&&(elementsCollectionStr!=null||"".equals(elementsCollectionStr))){
				String menuCode = this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
				if("xld".equals(menuCode)) {
					type="05";
				}else if("zbh".equals(menuCode)) {
					type="03";
				}else if("jws".equals(menuCode)) {
					type="04";
				}
			}
			if(page<=0) page=1;
			if(resName!=null) resName = resName.trim();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orgName", resName);
			params.put("type", type);
			params.put("startGridId", gridId);
			cn.ffcs.common.EUDGPagination pagination = organizationService.findTuCengPagination(page, rows, params);
			return pagination;
		}
		
		/**
		 * 2014-09-03 chenlf add
		 * 获取巡逻队、警务室、治保组织等社会组织的定位信息
		 * @param session
		 * @param ids ids
		 * @param mapt 地图类型
		 * @param showType 地图显示类型1、全显示，2、只显示当前页
		 * @return
		 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfSocietyOrg")
	public List<ArcgisInfoOfPublic> getArcgisSocietyOrgLocateDataList(HttpSession session, ModelMap map,
			HttpServletResponse res, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "markerType", required = false) String markerType,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		markerType = "01B032";
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisSocietyOrgLocateDataListByIds(ids, mapt,
				markerType);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
		
		/**
		 * 案件警情
		 * @param session
		 * @param map
		 * @param gridId
		 * @return
		 */
		@RequestMapping(value = "/standardCases")
		public String Cases(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			//案情类型
//	        List<Map<String, Object>> type = dictionaryService.getTableColumnDC(ConstantValue.T_ZZ_CASES, ConstantValue.COLUMN_CASES_TYPE, null);
			List<BaseDataDict> standardSwjc = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.CASES_TYPE, null);
	        map.addAttribute("TYPE_", standardSwjc);
			map.addAttribute("gridId", gridId);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			String forward ="/map/arcgis/standardmappage/standardCases.ftl";
			return forward;
		}
		
		/**
		 * 矛盾纠纷
		 * @param session
		 * @param map
		 * @param gridId
		 * @param elementsCollectionStr
		 * @return
		 */
		@RequestMapping(value = "/standardDispute")
		public String dispute(HttpServletRequest req, HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
			String infoOrgCode = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			List<BaseDataDict> standardSwjc = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.CASES_TYPE, infoOrgCode);
	        map.addAttribute("TYPE_", standardSwjc);
			map.addAttribute("gridId", gridId);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			String itype = req.getParameter("itype");
			map.addAttribute("itype", itype);
			String forward ="/map/arcgis/standardmappage/standardDispute.ftl";
			if(StringUtils.isNotBlank(itype)){
				//map.addAttribute("itype", itype);
				forward ="/map/arcgis/standardmappage/standardDispute_taiwang.ftl";
			}
			return forward;
		}
		
		/**
		 * 矛盾纠纷列表数据
		 * @param session
		 * @param page
		 * @param rows
		 * @param disputeEventName
		 * @param gridId
		 * @return
		 */
		@ResponseBody
	    @RequestMapping(value="/disputeListData", method= RequestMethod.POST)
	    public cn.ffcs.common.EUDGPagination disputeListData(
					    HttpSession session, @RequestParam(value = "page") int page,
					    @RequestParam(value = "rows") int rows,
					    @RequestParam(value = "disputeEventName", required = false) String disputeEventName,
					    @RequestParam(value = "itype", required = false) String itype,
					    @RequestParam(value = "handleDateFlag", required = false) String handleDateFlag,
					    @RequestParam(value = "gridId", required = false) Long gridId) {
		    if (page <= 0) page = 1;
		    Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("disputeEventName", disputeEventName);
	        if(gridId==null){  
	        	//默认网格设置
	    		gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
	        }
	        params.put("handleDateFlag",handleDateFlag);
	        params.put("gridId",gridId);
			params.put("involveType", itype);
	        params.put("infoOrgCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
	        cn.ffcs.common.EUDGPagination pagination = disputeMediationService.findDisputePagination(page,rows,params);
	        return pagination;
	    }
		
		@ResponseBody
		@RequestMapping(value = "/getArcgisLocateDataListOfDispute")
		public List<ArcgisInfoOfPublic> getArcgisDisputeLocateDataList(HttpSession session,
				ModelMap map, HttpServletResponse res,
				@RequestParam(value = "ids") String ids,
				@RequestParam(value = "mapt") Integer mapt
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisDisputeLocateDataListByIds(ids,mapt);
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					if(StringUtils.isNotBlank(list.get(i).getName()) && list.get(i).getName().length()>10){
						list.get(i).setName(list.get(i).getName().substring(0, 9));
					}
					list.get(i).setElementsCollectionStr(elementsCollectionStr);
				}
			}
			return list;
		}
		
		@RequestMapping(value="/showDisputeDetail")
		public String showDisputeDetail(HttpSession session, 
				@RequestParam(value="mediationId") Long mediationId, ModelMap map) {
			DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
			map.addAttribute("disputeMediation", disputeMediation);
			MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(disputeMediation.getGridId()), false);
			Long gridId = gridInfo.getGridId();
			String infoOrgCode=gridInfo.getInfoOrgCode();
			
			String gridNames="";
			if(gridId!=null){
				gridNames= mixedGridInfoService.getGridPath(gridId);
			}else{
				gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
			}
			map.addAttribute("gridNames", gridNames);
			map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格
			
			return "/map/arcgis/standardmappage/disputeDetail.ftl";
		}
		
		/**
	     * 案件警情信息--列表
	     * @param session
	     * @param page
	     * @param rows
	     * @param gridId
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value="/casesListData", method= RequestMethod.POST)
	    public cn.ffcs.common.EUDGPagination listData(
					    HttpSession session, @RequestParam(value = "page") int page,
					    @RequestParam(value = "rows") int rows,
					    @RequestParam(value = "type", required = false) String type,
					    @RequestParam(value = "startTime", required = false) String startTime,
					    @RequestParam(value = "endTime", required = false) String endTime,
					    @RequestParam(value = "content", required = false) String content,
					    @RequestParam(value = "gridId", required = false) Long gridId) {

		    if (page <= 0) page = 1;
		    Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("type", type);
	        params.put("startTime", startTime);
	        params.put("endTime", endTime);
	        params.put("content", content);
	        if(gridId==null){  
	        	//默认网格设置
	    		gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
	        }
	        params.put("gridId",gridId);
	        cn.ffcs.common.EUDGPagination pagination = casesService.findCasesPagination(page,rows,params);
	        return pagination;
	    }
		
	    /**
		 * 2014-09-03 chenlf add
		 * 获取案件警情的定位信息
		 * @param session
		 * @param ids ids
		 * @param mapt 地图类型
		 * @param showType 地图显示类型1、全显示，2、只显示当前页
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getArcgisLocateDataListOfCases")
		public List<ArcgisInfoOfPublic> getArcgisCasesLocateDataList(HttpSession session,
				ModelMap map, HttpServletResponse res,
				@RequestParam(value = "ids") String ids,
				@RequestParam(value = "mapt") Integer mapt
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisCasesLocateDataListByIds(ids,mapt);
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					if(StringUtils.isNotBlank(list.get(i).getName()) && list.get(i).getName().length()>10){
						list.get(i).setName(list.get(i).getName().substring(0, 9));
					}
					list.get(i).setElementsCollectionStr(elementsCollectionStr);
				}
			}
			return list;
		}
	    
		/**
		 * 隐患
		 * @param session
		 * @param map
		 * @param gridId
		 * @return
		 */
		@RequestMapping(value = "/standardDangous")
		public String dangous(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			map.addAttribute("gridId", gridId);
			map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			String forward ="/map/arcgis/standardmappage/standardDangous.ftl";
			return forward;
		}
		
		/**
		 * 隐患信息--列表
		 */
		@RequestMapping(value="/dangousListData")
		@ResponseBody
		public cn.ffcs.common.EUDGPagination zgMapList(HttpSession session,HttpServletRequest request,ModelMap map,
				@RequestParam(value = "page") int page,
			    @RequestParam(value = "rows") int rows,
			    @RequestParam(value = "gridId", required = false) Long gridId,
				@RequestParam(value="placeName",required = false) String placeName,
				@RequestParam(value="buildingName",required = false) String buildingName,
				@RequestParam(value = "checkDetail", required = false) String checkDetail){
			Map<String, Object> params = new HashMap<String, Object>();
			if(checkDetail!=null&&!"".equals(checkDetail)){
				params.put("checkDetail", checkDetail);
			}
			if(StringUtils.isNotBlank(placeName)){
				params.put("placeName", placeName);
			}
			if(StringUtils.isNotBlank(buildingName)){
				params.put("buildingName", buildingName);
			}

			if(gridId==null&&"".equals(gridId)){
				Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
				gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
				params.put("gridId", gridId);
			}
			params.put("gridId", gridId);
			
			cn.ffcs.common.EUDGPagination pagination = safeCheckDetailService.findCheckDetailPagination(page,rows,params);
			return pagination;
		}
		
		/**
		 * 2014-09-03 chenlf add
		 * 获取隐患的定位信息
		 * @param session
		 * @param ids ids
		 * @param mapt 地图类型
		 * @param showType 地图显示类型1、全显示，2、只显示当前页
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getArcgisLocateDataListOfDangous")
		public List<ArcgisInfoOfPublic> getArcgisDangousLocateDataList(HttpSession session,
				ModelMap map, HttpServletResponse res,
				@RequestParam(value = "ids") String ids,
				@RequestParam(value = "mapt") Integer mapt
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisDangousLocateDataListByIds(ids,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
			return list;
		}
		
		@RequestMapping(value="/viewDangerous")
		public String viewDangerous(HttpSession session, @RequestParam(value="id") Long id, ModelMap map) throws Exception{
			SafeCheckDetail d = safeCheckDetailService.selectSafeCheckById(id);
			if(d == null){
			   return null;
			}else{
				if (d.getGisx()==null && d.getGisy()==null){//两个都为空,填入空格&nbsp;
					   map.put("xy", "X:"+"&nbsp;&nbsp;"+"Y:"+"&nbsp;&nbsp;");
		           }else if (d.getGisx()==null && d.getGisy()!=null){
		        	   map.put("xy", "X:"+"&nbsp;&nbsp;"+"Y:"+d.getGisy());
		           }else if (d.getGisx()!=null && d.getGisy()==null){
		        	   map.put("xy", "X:"+d.getGisx()+"Y:"+"&nbsp;&nbsp;");
		           }else {
		        	   map.put("xy", "X:"+d.getGisx()+"Y:"+d.getGisy());
		           }
				
				if (d.getIsOld() != null) {
					map.put("old", d.getIsOld().equals("1")?d.getOldNum()+"人":"不存在");
				}
				
				map.put("record", d);
				
				
				return "/map/arcgis/standardmappage/taijiang/viewdangerous.ftl";
			}
		}
		
		/**
		 * 违法建设
		 * @param session
		 * @param map
		 * @param gridId
		 * @return
		 */
		@RequestMapping(value = "/standardCtIllegal")
		public String CtIllegal(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
				@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			//性质类型
			List<BaseDataDict> standardNature = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.CTILLEGAL_TYPE, null);
	        map.addAttribute("TYPE_", standardNature);
			map.addAttribute("gridId", gridId);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			String forward ="/map/arcgis/standardmappage/standardCtIllegal.ftl";
			return forward;
		}
		
		
		/**
	     * 违法建设信息--列表
	     * @param session
	     * @param page
	     * @param rows
	     * @param gridId
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value="/ctIllegalListData", method= RequestMethod.POST)
	    public cn.ffcs.common.EUDGPagination ctIllegalListData(
					    HttpSession session, @RequestParam(value = "page") int page,
					    @RequestParam(value = "rows") int rows,
					    @RequestParam(value = "nature", required = false) String nature,
					    @RequestParam(value = "owner", required = false) String owner,
					    @RequestParam(value = "startTime", required = false) String startTime,
					    @RequestParam(value = "endTime", required = false) String endTime,
					    @RequestParam(value = "gridId", required = false) Long gridId) {

		    if (page <= 0) page = 1;
		    Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("startTime", startTime);
	        params.put("endTime", endTime);
	        if(gridId==null){  
	        	//默认网格设置
	    		gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
	        }
	        params.put("gridId",gridId);
	        //违法业主
	        if(StringUtils.isNotBlank(owner)){  
	        	params.put("owner",owner);
	        }
	        //违法类型
	        if(StringUtils.isNotBlank(nature)){  
	        	params.put("nature",nature);
	        }
	        
	        cn.ffcs.common.EUDGPagination pagination = ctIllegalConstructionService.findCtIllegalConstructionPagination(page,rows,params);
	        return pagination;
	    }
		
	    /**
		 * 2015-05-22 sulch add
		 * 获取违法建设的定位信息
		 * @param session
		 * @param ids ids
		 * @param mapt 地图类型
		 * @param showType 地图显示类型1、全显示，2、只显示当前页
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getArcgisLocateDataListOfCtIllegal")
		public List<ArcgisInfoOfPublic> getArcgisCtIllegalLocateDataList(HttpSession session,
				ModelMap map, HttpServletResponse res,
				@RequestParam(value = "ids") String ids,
				@RequestParam(value = "mapt") Integer mapt
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisCtIllegalLocateDataListByIds(ids,mapt);
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					if(StringUtils.isNotBlank(list.get(i).getName()) && list.get(i).getName().length()>10){
						list.get(i).setName(list.get(i).getName().substring(0, 9));
					}
					list.get(i).setElementsCollectionStr(elementsCollectionStr);
				}
			}
			return list;
		}
		
	@RequestMapping(value = "/standardLogistics")
	public String standardLogistics(
			HttpSession session,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardLogistics.ftl";
	}
	
	/**************************************寄递物流安全管理***********************************/
	@ResponseBody
	@RequestMapping(value = "/logisticsListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination logisticsListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "corpName", required = false) String corpName,
			@RequestParam(value = "checkBefSealFlag", required = false) String checkBefSealFlag,
			@RequestParam(value = "realNameFlag", required = false) String realNameFlag,
			@RequestParam(value = "safetyCheckFlag", required = false) String safetyCheckFlag,
			@RequestParam(value = "status", required = false) String status) {
		if (page <= 0)
			page = 1;

		if (gridId == null || gridId == 0L) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			Object gridIdObj = defaultGridInfo.get(KEY_START_GRID_ID);
			if (gridIdObj != null) {
				gridId = Long.valueOf(gridIdObj.toString());
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("corpName", corpName);
		params.put("checkBefSealFlag", checkBefSealFlag);
		params.put("realNameFlag", realNameFlag);
		params.put("safetyCheckFlag", safetyCheckFlag);
		params.put("status", status);
		cn.ffcs.common.EUDGPagination pagination = logisticsSafetyService.findLogisticsSafetyPagination(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisLogisticsSafetyLocateDataListByIds")
	public List<ArcgisInfoOfPublic> getArcgisLogisticsSafetyLocateDataList(HttpSession session, ModelMap map,
			HttpServletResponse res, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisLogisticsSafetyLocateDataListByIds(ids, mapt);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**************************************寄递物流安全管理***********************************/
}
