package cn.ffcs.zhsq.map.gisstat.controller;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisHeatMapInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.HomeInfoCriteria;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.resident.service.HomeInfoService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.crowd.ICorrectionalRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.ICultMemberRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IDangerousGoodsRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IDrugRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IMentalIllnessRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IPetitionRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IReleasedRecordService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.gisstat.service.IGisStatService;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2015-09-06 liushi add
 * 地图统计
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/gisstat/gisStat")
public class GisStatController extends ZZBaseController {
	  
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private HomeInfoService homeInfoService; // 贫困户
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IThresholdColorCfgService thresholdColorCfgService;
	//残疾人员
	@Autowired
	private CiRsService ciRsService;
	//精神病人员
	@Autowired
	private IMentalIllnessRecordService mentalIllnessRecordService;
	//危险品从业
	@Autowired
	private IDangerousGoodsRecordService dangerousGoodsRecordService;
	//上访人员
	@Autowired
	private IPetitionRecordService petitionRecordService;
	//吸毒
	@Autowired
	private IDrugRecordService drugRecordService;
	//邪教
	@Autowired
	private ICultMemberRecordService cultMemberRecordService;
	//矫正
	@Autowired
	private ICorrectionalRecordService correctionalRecordService;
	//刑释解教
	@Autowired
	private IReleasedRecordService releasedRecordService;

	@Autowired
	private IGisStatService gisStatService;
	
	@RequestMapping(value="/toPoorHouseholdsStatIndex")
	public String toPoorHouseholdsStatIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "lowIncome") String lowIncome,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) throws Exception{
		String forward = "/map/arcgis/arcgis_base/gis_stat/poor_household_stat_index.ftl";
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		map.put("gridId", gridId);
		map.put("gridName", mixedGridInfo.getGridName());
		map.put("lowIncome", lowIncome);
		map.put("elementsCollectionStr", elementsCollectionStr);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return forward;
	}
	@ResponseBody
	@RequestMapping(value = "/getPoorHouseholdsStatCount")
	public List<Map<String,Object>> getPoorHouseholdsStatCount(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "lowIncome") String lowIncome) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String lableTypeName = "";
		if("1".equals(lowIncome)){
			lableTypeName = "低保户";
		}else if("2".equals(lowIncome)){
			lableTypeName = "贫困户";
		}else if("3".equals(lowIncome)){
			lableTypeName = "政策保障户";
		}else if("0".equals(lowIncome)){
			lableTypeName = "非贫困户";
		}
		HomeInfoCriteria criteria = new HomeInfoCriteria();
		criteria.setOrgCode(mixedGridInfo.getInfoOrgCode());
		Pagination paginationTotal = homeInfoService.findPage(criteria, 1, 1);
		int countTotal = paginationTotal.getTotalCount();
		float percent = 0.0f;
		if (StringUtils.isNotBlank(lowIncome)) {// 户类型
			// （字典值：低保户（1）、贫困户（2）、政策保障户（3）、非贫困户（0）字典pcode：D186002）
			criteria.setLowIncome(lowIncome);
		}
		Pagination pagination = homeInfoService.findPage(criteria, 1, 1);
		int count = pagination.getTotalCount();
		
		DecimalFormat df2 = new DecimalFormat("###.000");
		Map<String,Object> mapObj = new HashMap<String,Object>();
		mapObj.put("label", "总户数");
		mapObj.put("count", countTotal);
		mapObj.put("label1", lableTypeName);
		mapObj.put("count1", count);
		mapObj.put("name", mixedGridInfo.getGridName()+lableTypeName+"情况");
		mapObj.put("chartName", "各网格"+lableTypeName+"比例表（%）");
		if(countTotal > 0){
			percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		}
		mapObj.put("label2", lableTypeName+"占比");
		mapObj.put("count2", percent);
		resultList.add(mapObj);
		//获取子节点列表
		List<MixedGridInfo> gridList = this.mixedGridInfoService.getMixedGridListByParentId(gridId, false, false, 1);
		for(MixedGridInfo obj:gridList) {
			percent = 0.0f;
			HomeInfoCriteria criteriaObj = new HomeInfoCriteria();
			criteriaObj.setOrgCode(obj.getInfoOrgCode());
			
			Pagination paginationObjTotal = homeInfoService.findPage(criteriaObj, 1, 1);
			int countObjTotal = paginationObjTotal.getTotalCount();
			int countObj=0;
			if(countObjTotal > 0){
				if (StringUtils.isNotBlank(lowIncome)) {// 户类型
					// （字典值：低保户（1）、贫困户（2）、政策保障户（3）、非贫困户（0）字典pcode：D186002）
					criteriaObj.setLowIncome(lowIncome);
				}
				Pagination paginationObj = homeInfoService.findPage(criteriaObj, 1, 1);
				countObj = paginationObj.getTotalCount();
				percent = Float.parseFloat(df2.format(countObj/(countObjTotal*1.000)))*100;
			}
			
			Map<String,Object> mapObj1 = new HashMap<String,Object>();
			mapObj1.put("label", obj.getGridName()+"("+countObj+"/"+countObjTotal+")");
			mapObj1.put("value", Float.parseFloat(df2.format(percent)));
			resultList.add(mapObj1);
		}
		return resultList;
	}
	/**
	 * 
	 * @param session
	 * @param map
	 * @param res
	 * @param gridId
	 * @param lowIncome
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPrecisionPovertyLayer")
	public List<ArcgisInfoOfGrid> getPrecisionPovertyLayer(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "lowIncome") String lowIncome,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String lableTypeName = "";
		if("1".equals(lowIncome)){
			lableTypeName = "低保户";
		}else if("2".equals(lowIncome)){
			lableTypeName = "贫困户";
		}else if("3".equals(lowIncome)){
			lableTypeName = "政策保障户";
		}else if("0".equals(lowIncome)){
			lableTypeName = "非贫困户";
		}
		DecimalFormat df2 = new DecimalFormat("###.000");
		//获取子节点列表
		List<MixedGridInfo> gridList = this.mixedGridInfoService.getMixedGridListByParentId(gridId, false, false, 1);
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("class_", "PRECISION_POVERTY");
		param.put("orgCode", this.getDefaultInfoOrgCode(session));
		List<Map<String,Object>> colorList = this.thresholdColorCfgService.getThresholdColorCfgList(param);
		for(MixedGridInfo obj:gridList) {
			//获取各节点的轮廓数据
			//获取各节点贫困户统计数据
			ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisDataOfLocalService.getArcgisGridLocateData(obj.getGridId(), mapt);
			
			if(arcgisInfoOfGrid != null) {
				float percent = 0.0f;
				HomeInfoCriteria criteriaObj = new HomeInfoCriteria();
				criteriaObj.setOrgCode(obj.getInfoOrgCode());
				
				Pagination paginationObjTotal = homeInfoService.findPage(criteriaObj, 1, 1);
				int countObjTotal = paginationObjTotal.getTotalCount();
				int countObj=0;
				if(countObjTotal > 0){
					if (StringUtils.isNotBlank(lowIncome)) {// 户类型
						// （字典值：低保户（1）、贫困户（2）、政策保障户（3）、非贫困户（0）字典pcode：D186002）
						criteriaObj.setLowIncome(lowIncome);
					}
					Pagination paginationObj = homeInfoService.findPage(criteriaObj, 1, 1);
					countObj = paginationObj.getTotalCount();
					percent = Float.parseFloat(df2.format(countObj/(countObjTotal*1.000)))*100;
				}
				//还需要从数据库中获取配置数据来进行比对确定颜色跟透明度
				String areaColor = "#FF0000";
				float colorNum = 0.1f;
				
				for(Map<String,Object> mapObj:colorList) {
					if(percent >= ((Float)mapObj.get("minValue")) && percent < ((Float)mapObj.get("maxValue"))) {
						areaColor = (String)mapObj.get("colorVal");
						colorNum = ((Float)mapObj.get("colorNum"));
						break;
					}
				}
				arcgisInfoOfGrid.setAreaColor(areaColor);
				arcgisInfoOfGrid.setLineColor(areaColor);
				arcgisInfoOfGrid.setColorNum(colorNum);
				arcgisInfoOfGrid.setName(arcgisInfoOfGrid.getGridName()+"("+countObj+"/"+countObjTotal+")"+"占比："+percent+"%");
				arcgisInfoOfGrid.setElementsCollectionStr(elementsCollectionStr);
				
				arcgisInfoOfGridList.add(arcgisInfoOfGrid);
			}
		}
		return arcgisInfoOfGridList;
	}
	@RequestMapping(value="/toHouseholdsTypeStatIndex")
	public String toHouseholdsTypeStatIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) throws Exception{
		String forward = "/map/arcgis/arcgis_base/gis_stat/household_type_stat_index.ftl";
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		map.put("gridId", gridId);
		map.put("gridName", mixedGridInfo.getGridName());
		map.put("elementsCollectionStr", elementsCollectionStr);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return forward;
	}
	@ResponseBody
	@RequestMapping(value = "/getHouseholdsTypeStatCount")
	public List<Map<String,Object>> getHouseholdsTypeStatCount(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		HomeInfoCriteria criteria = new HomeInfoCriteria();
		criteria.setOrgCode(mixedGridInfo.getInfoOrgCode());
		Pagination paginationTotal = homeInfoService.findPage(criteria, 1, 1);
		int countTotal = paginationTotal.getTotalCount();
		// （字典值：低保户（1）、贫困户（2）、政策保障户（3）、非贫困户（0）字典pcode：D186002）
		criteria.setLowIncome("1");
		Pagination pagination = homeInfoService.findPage(criteria, 1, 1);
		int count1 = pagination.getTotalCount();
		criteria.setLowIncome("2");
		pagination = homeInfoService.findPage(criteria, 1, 1);
		int count2 = pagination.getTotalCount();
		criteria.setLowIncome("3");
		pagination = homeInfoService.findPage(criteria, 1, 1);
		int count3 = pagination.getTotalCount();
		criteria.setLowIncome("0");
		pagination = homeInfoService.findPage(criteria, 1, 1);
		int count0 = pagination.getTotalCount();
		
		DecimalFormat df2 = new DecimalFormat("###.000");
		Map<String,Object> mapObj = new HashMap<String,Object>();
		mapObj.put("label", "其它户数");
		mapObj.put("count", countTotal-count1-count2-count3-count0);
		mapObj.put("label1", "低保户");
		mapObj.put("count1", count1);
		mapObj.put("label2", "贫困户");
		mapObj.put("count2", count2);
		mapObj.put("label3", "政策保障户");
		mapObj.put("count3", count1);
		mapObj.put("label0", "非贫困户");
		mapObj.put("count0", count0);
		mapObj.put("chartName", "网格各类型户比例");
		resultList.add(mapObj);
		return resultList;
	}
	
	@RequestMapping(value="/toKeyPopulationStatIndex")
	public String toKeyPopulationStatIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) throws Exception{
		String forward = "/map/arcgis/arcgis_base/gis_stat/key_population_stat_index.ftl";
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		map.put("gridId", gridId);
		map.put("gridName", mixedGridInfo.getGridName());
		map.put("elementsCollectionStr", elementsCollectionStr);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return forward;
	}
	@RequestMapping(value="/toKeyPopulationTypeStatIndex")
	public String toKeyPopulationTypeStatIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) throws Exception{
		String forward = "/map/arcgis/arcgis_base/gis_stat/key_population_type_stat_index.ftl";
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		map.put("gridId", gridId);
		map.put("gridName", mixedGridInfo.getGridName());
		map.put("elementsCollectionStr", elementsCollectionStr);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return forward;
	}
	@ResponseBody
	@RequestMapping(value = "/getKeyPopulationStatCount")
	public List<Map<String,Object>> getKeyPopulationStatCount(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		Map<String,Object> mapObj = null;
		float percent = 0.0f;
		//获取该区域总人口数
		CiRsCriteria criteria = new CiRsCriteria();
		String infoOrgCode = mixedGridInfo.getInfoOrgCode();
		criteria.setOrgCode(infoOrgCode);
		Pagination pagination = ciRsService.findPage(criteria,1,1);
		Long countTotal = Long.parseLong(String.valueOf(pagination.getTotalCount()));
		
		String type=this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
		mapObj = getKeyPopulationStat(type, gridId);
		DecimalFormat df2 = new DecimalFormat("###.000");
		String label1 = (String)mapObj.get("label");
		Long count1 = (Long)mapObj.get("count");
		
		mapObj.put("label", "总人数");
		mapObj.put("count", countTotal);
		mapObj.put("label1", label1);
		mapObj.put("count1", count1);
		mapObj.put("name", mixedGridInfo.getGridName()+label1+"情况");
		mapObj.put("chartName", "各网格"+label1+"比例表（%）");
		if(countTotal > 0){
			percent = Float.parseFloat(df2.format(count1/(countTotal*1.000)))*100;
		}
		mapObj.put("label2", label1+"人占比");
		mapObj.put("count2", percent);
		resultList.add(mapObj);
		
		//获取子节点列表
		List<MixedGridInfo> gridList = this.mixedGridInfoService.getMixedGridListByParentId(gridId, false, false, 1);
		for(MixedGridInfo obj:gridList) {
			percent = 0.0f;
			criteria.setOrgCode(obj.getInfoOrgCode());
			pagination = ciRsService.findPage(criteria,1,1);
			Long countObjTotal = Long.parseLong(String.valueOf(pagination.getTotalCount()));
			Long countObj=0l;
			if(countObjTotal > 0){
				mapObj = getKeyPopulationStat(type, gridId);
				countObj = (Long)mapObj.get("count");
				percent = Float.parseFloat(df2.format(countObj/(countObjTotal*1.000)))*100;
			}
			
			Map<String,Object> mapObj1 = new HashMap<String,Object>();
			mapObj1.put("label", obj.getGridName()+"("+countObj+"/"+countObjTotal+")");
			mapObj1.put("value", Float.parseFloat(df2.format(percent)));
			resultList.add(mapObj1);
		}
		return resultList;
	}
	@ResponseBody
	@RequestMapping(value = "/getKeyPopulationTypeStatCount")
	public List<Map<String,Object>> getKeyPopulationTypeStatCount(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		MixedGridInfo  mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(gridId, false);
		//获取该区域总人口数
		CiRsCriteria criteria = new CiRsCriteria();
		String infoOrgCode = mixedGridInfo.getInfoOrgCode();
		criteria.setOrgCode(infoOrgCode);
		Pagination pagination = ciRsService.findPage(criteria,1,1);
		Long countTotal = Long.parseLong(String.valueOf(pagination.getTotalCount()));
		DecimalFormat df2 = new DecimalFormat("###.000");
		float percent = 0.0f;
		Map<String,Object> mapObj = null;
		Map<String, Object> params = null;
		cn.ffcs.common.EUDGPagination eudgPagination = new cn.ffcs.common.EUDGPagination();
		
		Long count = 0l;
		String label = "";
		mapObj = new HashMap<String,Object>();
		label = "总人数";
		mapObj.put("name", mixedGridInfo.getGridName()+"重点人口情况表");
		mapObj.put("count", countTotal);
		mapObj.put("label", label);
		mapObj.put("percent", " ");
		resultList.add(mapObj);
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = mentalIllnessRecordService.findMentalIllnessRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "重精神病";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("startGridId", gridId);
		eudgPagination = dangerousGoodsRecordService.findDangerousGoodsRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "危险品从业";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		

		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = petitionRecordService.findPetitionRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "上访";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = drugRecordService.findDrugRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "吸毒";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = cultMemberRecordService.findCultMemberRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "邪教";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = correctionalRecordService.findCorrectionalRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "矫正";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		
		params = new HashMap<String, Object>();
		mapObj = new HashMap<String,Object>();
		params.put("gridId", gridId);
		eudgPagination = correctionalRecordService.findCorrectionalRecordPagination(1, 1, params);
		count = eudgPagination.getTotal();
		percent = Float.parseFloat(df2.format(count/(countTotal*1.000)))*100;
		label = "刑释";
		mapObj.put("count", count);
		mapObj.put("label", label);
		mapObj.put("percent", percent);
		resultList.add(mapObj);
		return resultList;
	}
	@ResponseBody
	@RequestMapping(value = "/getKeyPopulationStatLayer")
	public List<ArcgisInfoOfGrid> getKeyPopulationStatLayer(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		DecimalFormat df2 = new DecimalFormat("###.000");
		Map<String,Object> mapObj = null;
		String type=this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
		//获取子节点列表
		List<MixedGridInfo> gridList = this.mixedGridInfoService.getMixedGridListByParentId(gridId, false, false, 1);
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = new ArrayList<ArcgisInfoOfGrid>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("class_", "PRECISION_POVERTY");
		param.put("orgCode", this.getDefaultInfoOrgCode(session));
		List<Map<String,Object>> colorList = this.thresholdColorCfgService.getThresholdColorCfgList(param);
		
		CiRsCriteria criteria = new CiRsCriteria();
		Pagination pagination = null;
		for(MixedGridInfo obj:gridList) {
			//获取各节点的轮廓数据
			//获取各节点贫困户统计数据
			ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisDataOfLocalService.getArcgisGridLocateData(obj.getGridId(), mapt);
			
			if(arcgisInfoOfGrid != null) {
				float percent = 0.0f;
				criteria.setOrgCode(obj.getInfoOrgCode());
				pagination = ciRsService.findPage(criteria,1,1);
				Long countObjTotal = Long.parseLong(String.valueOf(pagination.getTotalCount()));
				Long countObj=0l;
				if(countObjTotal > 0){
					mapObj = getKeyPopulationStat(type, gridId);
					countObj = (Long)mapObj.get("count");
					percent = Float.parseFloat(df2.format(countObj/(countObjTotal*1.000)))*100;
				}
				//还需要从数据库中获取配置数据来进行比对确定颜色跟透明度
				String areaColor = "#FF0000";
				float colorNum = 0.1f;
				
				for(Map<String,Object> colorMapObj:colorList) {
					if(percent >= ((Float)colorMapObj.get("minValue")) && percent < ((Float)colorMapObj.get("maxValue"))) {
						areaColor = (String)colorMapObj.get("colorVal");
						colorNum = ((Float)colorMapObj.get("colorNum"));
						break;
					}
				}
				arcgisInfoOfGrid.setAreaColor(areaColor);
				arcgisInfoOfGrid.setLineColor(areaColor);
				arcgisInfoOfGrid.setColorNum(colorNum);
				arcgisInfoOfGrid.setName(arcgisInfoOfGrid.getGridName()+"("+countObj+"/"+countObjTotal+")"+"占比："+percent+"%");
				arcgisInfoOfGrid.setElementsCollectionStr(elementsCollectionStr);
				
				arcgisInfoOfGridList.add(arcgisInfoOfGrid);
			}
		}
		return arcgisInfoOfGridList;
	}
	
	private Map<String,Object> getKeyPopulationStat(String type,Long gridId) {
		Map<String,Object> mapObj = new HashMap<String,Object>();
		cn.ffcs.common.EUDGPagination eudgPagination = new cn.ffcs.common.EUDGPagination();
		Long count = 0l;
		String label = "";
		if("neuropathy".equals(type)) {//重精神病人员数据查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = mentalIllnessRecordService.findMentalIllnessRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "重精神病";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("dangerous".equals(type)){//危险品从业人员数据查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startGridId", gridId);
			eudgPagination = dangerousGoodsRecordService.findDangerousGoodsRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "危险品从业";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("petition".equals(type)) {//上访人员数据查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = petitionRecordService.findPetitionRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "上访";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("drugs".equals(type)) {//吸毒人员数据查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = drugRecordService.findDrugRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "吸毒";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("heresy".equals(type)) {//邪教人员数据查询
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = cultMemberRecordService.findCultMemberRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "邪教";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("rectify".equals(type)) {//矫正
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = correctionalRecordService.findCorrectionalRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "矫正";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}else if("camps".equals(type)) {//刑释
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridId", gridId);
			eudgPagination = correctionalRecordService.findCorrectionalRecordPagination(1, 1, params);
			count = eudgPagination.getTotal();
			label = "刑释";
			mapObj.put("count", count);
			mapObj.put("label", label);
		}
		return mapObj;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRentRoom")
	public List<Map<String,Object>> getRentRoom(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.findRentRoom(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStatPartyOrg")
	public List<Map<String,Object>> getStatPartyOrg(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getStatPartyOrg(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStatCor")
	public List<Map<String,Object>> getStatCor(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getStatCor(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPartyMember")
	public List<Map<String,Object>> getPartyMember(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getPartyMember(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRectify")
	public List<Map<String,Object>> getRectify(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getRectify(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCamps")
	public List<Map<String,Object>> getCamps(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getCamps(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getJiangxiPetition")
	public List<Map<String,Object>> getJiangxiPetition(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getJiangxiPetition(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUrbanCount")
	public List<Map<String,Object>> getUrbanCount(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "urbanCode", required = false) String urbanCode) {
		List<Map<String,Object>> list = gisStatService.getUrbanCount(gridId, urbanCode);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/getUrbanCountByCodes")
	public List<Map<String,Object>> getUrbanCountByCodes(HttpSession session, ModelMap map,HttpServletResponse res,
												  @RequestParam(value = "infoOrgCode") String infoOrgCode,
												  @RequestParam(value = "urbanCode", required = false) String urbanCode) {
		List<Map<String,Object>> list = gisStatService.getUrbanCountByCodes(infoOrgCode, urbanCode);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getKeyPop")
	public List<Map<String,Object>> getKeyPop(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId, 
			@RequestParam(value = "sYear") String sYear, 
			@RequestParam(value = "pCode") String pCode) {
		List<Map<String,Object>> list = gisStatService.getKepPop(gridId, sYear, pCode);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRealPeople")
	public List<Map<String,Object>> getRealPeople(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getRealPeople(gridId);
		return list;
	}
	
	@RequestMapping(value = "/getRealPeopleDetail")
	public String getRealPeopleDetail(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "id") Long gridId) {
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		List<Map<String,Object>> list = gisStatService.getRealPeopleDetail(gridId);
		map.put("list", list);
		map.addAttribute("gridInfo", gridInfo);
		return "/map/arcgis/standardmappage/realPeopleDetail.ftl";
	}
	
	@RequestMapping(value = "/getRentRoomRelatedInfo")
	public String getRentRoomRelatedInfo(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "id") Long gridId) {
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		Map<String,Object> info = gisStatService.getRentRoomRelatedInfo(gridId, gridInfo.getParentGridId(), gridInfo.getInfoOrgCode());
		map.put("info", info);
		map.addAttribute("gridInfo", gridInfo);
		map.addAttribute("BI_REPORT", App.BI.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("GMIS_URL", App.GMIS.getDomain(session));
		return "/map/arcgis/standardmappage/rentRoomRelatedInfo.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMajorRelatedEvents")
	public List<Map<String,Object>> getMajorRelatedEvents(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getMajorRelatedEvents(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getSchoolRelatedEvents")
	public List<Map<String,Object>> getSchoolRelatedEvents(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getSchoolRelatedEvents(gridId);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRelatedRoadEvents")
	public List<Map<String,Object>> getRelatedRoadEvents(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getRelatedRoadEvents(gridId);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/querySpectialPopulationData")
	public List<ArcgisHeatMapInfo> querySpectialPopulationData(HttpSession session, ModelMap map, HttpServletRequest request,
				@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
				@RequestParam(value = "mapType", required = false) Integer mapType) {
		String spectialPopulations = request.getParameter("spectialPopulations");
		if(StringUtils.isBlank(infoOrgCode)){
			infoOrgCode = super.getDefaultInfoOrgCode(session);
		}
		List<ArcgisHeatMapInfo> statDatas = gisStatService.querySpectialPopulationData(spectialPopulations, infoOrgCode);
		return statDatas;
	}

	@ResponseBody
	@RequestMapping(value = "/queryPopulationData")
	public List<ArcgisHeatMapInfo> queryPopulationData(HttpSession session, ModelMap map, HttpServletRequest request,
			   @RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			   @RequestParam(value = "mapType", required = false) Integer mapType) {
		if(StringUtils.isBlank(infoOrgCode)){
			infoOrgCode = super.getDefaultInfoOrgCode(session);
		}
		List<ArcgisHeatMapInfo> statDatas = gisStatService.queryPopulationData(infoOrgCode);
		return statDatas;
	}

	@ResponseBody
	@RequestMapping(value = "/getGlobalEyes")
	public List<Map<String,Object>> getGlobalEyes(HttpSession session, ModelMap map,HttpServletResponse res,
				@RequestParam(value = "gridId") Long gridId) {
		List<Map<String,Object>> list = gisStatService.getGlobalEyes(gridId);
		return list;
	}


	/**
	 	吸毒--P 、邪教人员--O、 刑满释放人员--K 、涉羟--IH 、2次以上行政拘留人员--DE
		上访人员--Q、重症精神病人员--L、重点信访事项的挑头人--TD、暖心对象--WH
		残疾人--E、重点青少年--AQ、独居人--LA、危重人--WZ、贫困人员--BH
	 */
	@ResponseBody
	@RequestMapping(value = "/peopleConverge")
	public List<Map<String, Object>> peopleConverge(HttpSession session, ModelMap map, HttpServletRequest request) {
		String infoOrgCode = request.getParameter("infoOrgCode");
		String gridId = request.getParameter("gridId");
		String relCode = request.getParameter("relCode");
		List<Map<String,Object>> list = ciRsService.countRsByOrgCodeAndRelCode(infoOrgCode, relCode);
		return list;
	}

	//矛盾纠纷汇聚
	@ResponseBody
	@RequestMapping(value = "/getDisputeRectify")
	public List<Map<String,Object>> getDisputeRectify(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId",required = false) Long gridId) {
		List<Map<String,Object>> list = gisStatService.getDisputeRectify(gridId);
		return list;
	}
	
	//矛盾纠纷柱状图页面
	@RequestMapping(value = "/getDisputeBar")
	public String getDisputeBar(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "gridId",required = false) Long gridId,@RequestParam(value = "infoOrgCode",required = false) Long infoOrgCode) {
		map.addAttribute("infoOrgCode",infoOrgCode);
		return "/map/arcgis/standardmappage/get_dispute_bar.ftl";
	}
	
	//矛盾纠纷柱状图数据
	@ResponseBody
	@RequestMapping(value = "/getDisputeBarData")
	public List<Map<String,Object>> getDisputeBarData(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "infoOrgCode",required = false) Long infoOrgCode) {
		//infoOrgCode=217134l;
		List<Map<String,Object>> list = gisStatService.getDisputeRectify(infoOrgCode);
		return list;
	}
}
