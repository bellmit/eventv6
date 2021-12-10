package cn.ffcs.zhsq.map.arcgis.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import cn.ffcs.resident.bo.HomeHelp;
import cn.ffcs.resident.bo.HomeInfo;
import cn.ffcs.resident.bo.HomeInfoCriteria;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.resident.bo.RsPoorHold;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.resident.service.HomeHelpService;
import cn.ffcs.resident.service.HomeInfoService;
import cn.ffcs.resident.service.RsPoorHoldService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 精准扶贫
 */
@Controller
@RequestMapping(value = "/zhsq/map/arcgis/arcgisDataOfPrecisionPoverty")
public class ArcgisDataOfPrecisionPovertyController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private HomeInfoService homeInfoService; // 贫困户
	@Autowired
	private HomeHelpService homeHelpService; // 帮扶
	@Autowired
	private RsPoorHoldService rsPoorHoldService;
	@Autowired
	private CiRsService ciRsService; // 人口
	@Autowired
	private IBaseDictionaryService baseDictionaryService; // 新字典服务

	/**
	 * 精准扶贫首页
	 */
	@RequestMapping(value = "/toArcgisDataListOfPrecisionPoverty")
	public String toArcgisDataListOfPrecisionPoverty(HttpSession session, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value = "noneGetLayer", required = false) String noneGetLayer,
			@RequestParam(value = "lowIncome") String lowIncome, ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("noneGetLayer", noneGetLayer);
		map.addAttribute("lowIncome", lowIncome);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> typeName = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.POOR_HOLD,
				userInfo.getOrgCode());
		map.addAttribute("typeName", typeName);
		return "/map/arcgis/standardmappage/precisionPoverty/standardPrecisionPoverty.ftl";
	}

	/**
	 * 精准扶贫列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/precisionPovertListData", method = RequestMethod.POST)
	public Pagination precisionPovertListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "lowIncome", required = false) String lowIncome,
			@RequestParam(value = "houseHoldName", required = false) String houseHoldName,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "poorHold", required = false) String poorHold) {
		if (page <= 0) {
			page = 1;
		}

		HomeInfoCriteria criteria = new HomeInfoCriteria();
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		criteria.setOrgCode(gridInfo.getInfoOrgCode());

		if (StringUtils.isNotBlank(houseHoldName)) {
			criteria.setName(houseHoldName.trim());
		}

		if (StringUtils.isNotBlank(poorHold)) {// 贫困户标识
												// （字典值：未脱贫（001）、脱贫（002）、预脱贫（003）、返贫（004）字典pcode：D186003）
			criteria.setPoorHold(poorHold.trim());
		}

		if (StringUtils.isNotBlank(lowIncome)) {// 户类型
												// （字典值：低保户（1）、贫困户（2）、政策保障户（3）、非贫困户（0）字典pcode：D186002）
			criteria.setLowIncome(lowIncome);
		}

		Pagination pagination = homeInfoService.findPage(criteria, page, rows);
		return pagination;
	}

	/**
	 * 精准扶贫概要信息的查看
	 * 
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getSummaryInfo")
	public String getSummaryInfo(HttpSession session, @RequestParam(value = "familyId") Long familyId,
			@RequestParam(value = "lowIncome", required = false) String lowIncome, ModelMap map) throws Exception {
		HomeInfo homeInfo = homeInfoService.findHouseholderById(familyId);
		map.addAttribute("homeInfo", homeInfo);

		String familySn = homeInfo.getFamilySn();
		String orgCode = homeInfo.getOrgCode();
		int popTotal = ciRsService.findPageByFamilySn(familySn, orgCode, 1, 1).getTotalCount(); // 家庭人口数
		map.addAttribute("popTotal", popTotal);

		if (StringUtils.isNotBlank(lowIncome) && !lowIncome.equals("0")) { // 低保户（1）、贫困户（2）、政策保障户（3）
			List<HomeHelp> homeHelps = homeHelpService.findByFamilyId(familyId);
			StringBuffer homeHelpInfoSb = new StringBuffer();

			for (HomeHelp homeHelp : homeHelps) {
				String str = "";
				if (StringUtils.isNotBlank(homeHelp.getHelpDutyman())) {
					str = homeHelp.getHelpDutyman();
				}
				if (StringUtils.isNotBlank(homeHelp.getHelpDutymanPhone())) {
					str += str.length() > 0 ? ("（" + homeHelp.getHelpDutymanPhone() + "）") : homeHelp.getHelpDutymanPhone();
				}
				if (str.length() > 0) homeHelpInfoSb.append(str).append("、");
			}

			String homeHelpInfo = homeHelpInfoSb.toString();

			if (homeHelpInfo.length() > 0) {
				map.addAttribute("homeHelpPhone", homeHelpInfo.substring(0, homeHelpInfo.length() - 1));
			}

			return "/map/arcgis/standardmappage/precisionPoverty/pkh.ftl";
		}

		return "/map/arcgis/standardmappage/precisionPoverty/nonPkh.ftl";
	}

	/**
	 * 详情
	 * 
	 * @param session
	 * @param familyId
	 * @param lowIncome
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getDetailInfo")
	public String getDetailInfo(HttpSession session, @RequestParam(value = "familyId") Long familyId,
			@RequestParam(value = "lowIncome", required = false) String lowIncome,
			@RequestParam(value = "gridId") long gridId, ModelMap map) {
		HomeInfo homeInfo = homeInfoService.findHouseholderById(familyId);
		map.addAttribute("homeInfo", homeInfo);

		String familySn = homeInfo.getFamilySn();
		String orgCode = homeInfo.getOrgCode();
		int popTotal = ciRsService.findPageByFamilySn(familySn, orgCode, 1, 1).getTotalCount(); // 家庭人口数
		map.addAttribute("popTotal", popTotal);

		if (StringUtils.isNotBlank(lowIncome) && !lowIncome.equals("0")) { // 低保户（1）、贫困户（2）、政策保障户（3）
			return "/map/arcgis/standardmappage/precisionPoverty/pkhDetail.ftl";
		}

		return "/map/arcgis/standardmappage/precisionPoverty/nonPkhDetail.ftl";
	}

	/**
	 * 家庭人员列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/cirListData", method = RequestMethod.POST)
	public EUDGPagination cirListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "familyId") Long familyId,
			@RequestParam(value = "gridId", required = false) Long gridId) {
		if (page <= 0) {
			page = 1;
		}

		HomeInfo homeInfo = homeInfoService.findHouseholderById(familyId);
		String familySn = homeInfo.getFamilySn();
		String orgCode = homeInfo.getOrgCode();
		Pagination pagination = ciRsService.findPageByFamilySn(familySn, orgCode, rows, page);

		EUDGPagination eudgPagination = new EUDGPagination();
		eudgPagination = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
		return eudgPagination;
	}

	/**
	 * 家庭人员详情
	 * 
	 * @param session
	 * @param cirId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/rsPoorHoldDetail")
	public String rsPoorHoldDetail(HttpSession session, @RequestParam(value = "cirId") Long cirId, ModelMap map) {
		RsPoorHold rsPoorHold = rsPoorHoldService.findByRsId(cirId);
		map.addAttribute("rsPoorHold", rsPoorHold);
		return "/map/arcgis/standardmappage/precisionPoverty/rsPoorHoldDetail.ftl";
	}

	/**
	 * 帮扶人员列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/homeHelpListData", method = RequestMethod.POST)
	public EUDGPagination homeHelpListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "familyId") Long familyId) {
		if (page <= 0) {
			page = 1;
		}

		List<HomeHelp> homeHelps = homeHelpService.findByFamilyId(familyId);
		int totalCount = homeHelps.size();
		Pagination pagination = new Pagination(page, rows, totalCount, homeHelps);

		EUDGPagination eudgPagination = new EUDGPagination();
		eudgPagination = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
		return eudgPagination;
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
		for(MixedGridInfo obj:gridList) {
			//获取各节点的轮廓数据
			//获取各节点贫困户统计数据
			ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisDataOfLocalService.getArcgisGridLocateData(obj.getGridId(), mapt);
			
			List<Map<String,Object>> colorList = this.arcgisDataOfLocalService.getColorList("PRECISION_POVERTY");
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
					if(percent >= ((BigDecimal)mapObj.get("MIN_VALUE")).floatValue() && percent < ((BigDecimal)mapObj.get("MAX_VALUE")).floatValue()) {
						areaColor = (String)mapObj.get("COLOR_VAL");
						colorNum = ((BigDecimal)mapObj.get("COLOR_NUM")).floatValue();
						break;
					}
				}
				arcgisInfoOfGrid.setAreaColor(areaColor);
				arcgisInfoOfGrid.setColorNum(colorNum);
				arcgisInfoOfGrid.setName(arcgisInfoOfGrid.getGridName()+"("+countObj+"/"+countObjTotal+")"+"占比："+percent+"%");
				arcgisInfoOfGrid.setElementsCollectionStr(elementsCollectionStr);
				
				arcgisInfoOfGridList.add(arcgisInfoOfGrid);
			}
		}
		return arcgisInfoOfGridList;
	}
	
	/**
	 * 2016-01-27 sulch add
	 * 获取精准扶贫的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfPrecisionPoverty")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPrecisionPoverty(HttpSession session, 
			ModelMap map, HttpServletResponse res,
			@RequestParam(value="resourcesIds") String resourcesIds, 
			@RequestParam(value="lowIncome") String lowIncome, 
			@RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		try{
			String markerType = "";
			if("1".equals(lowIncome)){
				markerType = ConstantValue.LOW_INCOME_H;//低保户
			}else if("2".equals(lowIncome)){
				markerType = ConstantValue.POOR_H;//贫困户
			}else if("3".equals(lowIncome)){
				markerType = ConstantValue.SUPPORT_H;//政策保障户
			}else if("0".equals(lowIncome)){
				markerType = ConstantValue.NON_POOR_H;//非贫困户
			}
			if (StringUtils.isNotBlank(resourcesIds)) {
				list = this.arcgisDataOfLocalService.getArcgisLocateDataListOfPrecisionPovertyByIds(resourcesIds,mapt,markerType);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return list;
	}
}
