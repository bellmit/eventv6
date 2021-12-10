package cn.ffcs.zhsq.map.arcgis.controller;

import cn.ffcs.common.DictPcode;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.gmis.mybatis.domain.teamMembers.TeamMembers;
import cn.ffcs.gmis.teamMembers.service.ITeamMembersService;
import cn.ffcs.lzly.mybatis.domain.twoMember.TwoMember;
import cn.ffcs.lzly.service.twoMember.ITwoMemberService;
import cn.ffcs.resident.bo.*;
import cn.ffcs.resident.service.*;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.infoOpen.service.IInfoOpenService;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.DrugRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.productSafety.SafetyPersonManage;
import cn.ffcs.shequ.zzgl.service.crowd.*;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.productSafety.IRoutineExaminationService;
import cn.ffcs.shequ.zzgl.service.res.IMemberInfoService;
import cn.ffcs.uam.bo.*;
import cn.ffcs.uam.service.*;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2014-05-28 liushi add arcgis地图定位数据加载控制器
 * 
 * @author liushi
 *
 */
@Controller
@RequestMapping(value = "/zhsq/map/arcgis/arcgisdataofpoplocal")
public class ArcgisDataOfPopLocalController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	// 残疾人员
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private CiRsRelService ciRsRelService;
	// 精神病人员
	@Autowired
	private IMentalIllnessRecordService mentalIllnessRecordService;
	// 危险品从业
	@Autowired
	private IDangerousGoodsRecordService dangerousGoodsRecordService;
	// 上访人员
	@Autowired
	private IPetitionRecordService petitionRecordService;
	// 吸毒
	@Autowired
	private IDrugRecordService drugRecordService;
	// 邪教
	@Autowired
	private ICultMemberRecordService cultMemberRecordService;
	// 矫正
	@Autowired
	private ICorrectionalRecordService correctionalRecordService;
	// 刑释解教
	@Autowired
	private IReleasedRecordService releasedRecordService;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private IMixedGridInfoService iMixedGridInfoService;

	@Autowired
	private IRoutineExaminationService routineExaminationService;

	@Autowired
	private IPetitionerService petitionerService; // 江西信访

	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	private String JIANGXI_PETITIONER = "jiangxiPetitioner"; // 江西信访
	@Autowired
	private IYouthService youthService;// 重点青少年

	@Autowired
	private IMemberInfoService iMemberInfoService;
	
	@Autowired
	private IAidsPersonService aidsPersonService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private VisitRecordService visitRecordService;
	
	@Autowired
	private IInfoOpenService iInfoOpenService;

	@Autowired
	private ITeamMembersService teamMembersService;

	@Autowired
	private CiRsPartyService ciRsPartyService;
	@Autowired
	private UserManageOutService userManageOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private PositionInfoOutService positionInfoOutService;
	@Autowired
	private PartyIndividualService partyIndividualService;
	@Autowired
	private IInvolveHydroxylService involveHydroxylService;//涉羟人员
	@Autowired
	private IDetentionService detentionService;//二次刑拘
	@Autowired
	private ILiveAloneService liveAloneService;//独居人
	@Autowired
	private CriticalRecordService criticalRecordService;//危重人
	@Autowired
	private CiRsWarmHeartService ciRsWarmHeartService;

	/**
	 * 晋江地图-人 消防安全管理员
	 * 
	 * @param map
	 * @param gridId
	 * @param standard
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfSafetyPersonManage")
	public String safetyPersonManage(ModelMap map, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value = "standard", required = false) String standard, HttpSession session) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardSafetyPersonManage.ftl";
	}

	/**
	 * 消防安全管理员人员数据
	 * 
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param mobileTelephone
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/safetyPersonManageListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination safetyPersonManageListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "identityCard", required = false) String identityCard) {
		if (page <= 0)
			page = 1;
		if (name != null)
			name = name.trim();
		if (identityCard != null)
			identityCard = identityCard.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String infoOrgCode = mixedGridInfo.getInfoOrgCode();
		params.put("infoOrgCode", infoOrgCode);// 信息域代码
		params.put("name", name);// 姓名
		params.put("identityCard", identityCard);// 身份证号
		params.put("siteType", null);// null不过滤代表全部siteType=1企业siteType=2场所siteType=3出租屋
		params.put("order", order);
		cn.ffcs.common.EUDGPagination pagination = routineExaminationService.findSafetyPersonManageByGridPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 消防安全管理员
	 * 
	 * @param session
	 * @param corpStaffId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/corpStaffDetail")
	public String corpStaffDetail(HttpSession session,
			@RequestParam(value = "corpStaffId", required = false) Long corpStaffId,
			@RequestParam(value = "wid", required = false) Long wid, ModelMap map) {
		if (wid != null && wid != 0l) {
			corpStaffId = wid;
		}
		SafetyPersonManage safetyPersonManage = routineExaminationService.findSafetyPersonManageById(corpStaffId);
		map.addAttribute("safetyPersonManage", safetyPersonManage);
		return "/map/arcgis/standardmappage/jinjiang/corpStaffDetail.ftl";
	}

	/**
	 * 2015-02-25 huangmw add 根据id 和mapt获取消防安全管理员定位信息
	 * 
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfSafetyPersonManage")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfSafetyPersonManage(HttpSession session, ModelMap map,
			HttpServletResponse res, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr)
			throws Exception {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService
				.getArcgisSafetyPersonManageLocateDataListByIds(ids, mapt);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 2014-05-28 liushi add 转到arcgis轮廓编辑的panel（主要针对网格grid、楼栋build） 需要获取目标的名称
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param targetType目标类型
	 *            grid build
	 * @param wid
	 *            目标关联id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toArcgisDataListOfPeople")
	public String toArcgisDataListOfPeople(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value = "type") String type) {
		String forward = "/map/arcgis/standardmappage/standardPeople.ftl";
		map.addAttribute("type", type);// 人员类型
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		if ((type == null || "".equals(type)) && (elementsCollectionStr != null || "".equals(elementsCollectionStr))) {
			type = this.analysisOfElementsCollection(elementsCollectionStr, "menuCode");
		}
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if ("partyMerber".equals(type)) {// 党员
			map.addAttribute("imgpath", "images/unselected/person_0101.png");
			map.addAttribute("imgtype", "0101");
			map.addAttribute("persontype", "1");
		} else if ("volunteer".equals(type)) {// 志愿者
			map.addAttribute("imgpath", "images/unselected/person_0304.png");
			map.addAttribute("imgtype", "0304");
			map.addAttribute("persontype", "2");
		} else if ("retire".equals(type)) {// 退休人员
			map.addAttribute("imgpath", "images/unselected/person_h_0401.png");
			map.addAttribute("imgtype", "0401");
			map.addAttribute("persontype", "3");
		} else if ("homeAge".equals(type)) {// 居家养老
			map.addAttribute("imgpath", "images/unselected/person_h_0303.png");
			map.addAttribute("imgtype", "0303");
			map.addAttribute("persontype", "4");
		} else if ("military".equals(type)) {// 服兵役人员
			map.addAttribute("imgpath", "images/unselected/person_h_1001.png");
			map.addAttribute("imgtype", "1001");
			map.addAttribute("persontype", "5");
		} else if ("unemployment".equals(type)) {// 失业人员
			map.addAttribute("imgpath", "images/unselected/person_0402.png");
			map.addAttribute("imgtype", "0402");
			map.addAttribute("persontype", "6");
		} else if ("welfare".equals(type)) {// 低保人员
			map.addAttribute("imgpath", "images/unselected/person_0302.png");
			map.addAttribute("imgtype", "0302");
			map.addAttribute("persontype", "7");
		} else if ("disability".equals(type)) {// 残障人员
			map.addAttribute("imgpath", "images/unselected/person_0301.png");
			map.addAttribute("imgtype", "0301");
			map.addAttribute("persontype", "8");
		} else if ("neuropathy".equals(type)) {// 精神障碍患者
			map.addAttribute("imgpath", "images/unselected/person_0207.png");
			map.addAttribute("imgtype", "0207");
			map.addAttribute("persontype", "9");
		} else if ("dangerous".equals(type)) {// 危险品从业人员
			map.addAttribute("imgpath", "images/unselected/person_0206.png");
			map.addAttribute("imgtype", "0206");
			map.addAttribute("persontype", "10");
		} else if ("petition".equals(type)) {// 上访人员
			map.addAttribute("imgpath", "images/unselected/person_0205.png");
			map.addAttribute("imgtype", "0205");
			map.addAttribute("persontype", "11");
		} else if ("drugs".equals(type)) {// 吸毒人员
			map.addAttribute("imgpath", "images/unselected/person_0201.png");
			map.addAttribute("imgtype", "0201");
			map.addAttribute("persontype", "12");
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			if(userInfo != null && StringUtils.isNotBlank(userInfo.getOrgCode())){
				if(userInfo.getOrgCode().startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)){
					if(StringUtils.isNotBlank(elementsCollectionStr)){
						map.addAttribute("elementsCollectionStr1", formatElementsCollectionStr(elementsCollectionStr, "1"));//在控
						map.addAttribute("elementsCollectionStr2", formatElementsCollectionStr(elementsCollectionStr, "2"));//失控
						map.addAttribute("elementsCollectionStr3", formatElementsCollectionStr(elementsCollectionStr, "3"));//其他
					}
					map.addAttribute("GANSU_FLAG","yes");
				}
			}

		} else if ("heresy".equals(type)) {// 邪教人员
			map.addAttribute("imgpath", "images/unselected/person_0204.png");
			map.addAttribute("imgtype", "0204");
			map.addAttribute("persontype", "13");
		} else if ("rectify".equals(type)) {// 矫正人员
			map.addAttribute("imgpath", "images/unselected/person_0203.png");
			map.addAttribute("imgtype", "0203");
			map.addAttribute("persontype", "14");
		} else if ("camps".equals(type)) {// 刑事解教人员
			map.addAttribute("path", "images/unselected/person_0202.png");
			map.addAttribute("imgtype", "0202");
			map.addAttribute("persontype", "15");
		} else if ("taibao".equals(type)) {// 台胞人员
			map.addAttribute("path", "images/unselected/person_0208.png");
			map.addAttribute("imgtype", "0208");
			map.addAttribute("persontype", "16");
		} else if ("heresy2".equals(type)) {// 摩托车工
			map.addAttribute("imgpath", "images/unselected/person_0208.png");
			map.addAttribute("imgtype", "0208");
			map.addAttribute("persontype", "17");
		} else if ("wgy".equals(type)) {// 网格员
			map.addAttribute("imgpath", "images/unselected/person_0302.png");
			map.addAttribute("imgtype", "person");
			map.addAttribute("persontype", "18");
		} else if ("xldj".equals(type)) {// 巡逻段警
			map.addAttribute("imgpath", "images/unselected/person_0302.png");
			map.addAttribute("imgtype", "person");
			map.addAttribute("persontype", "19");
		} else if ("jiangxiPetition".equals(type)) { // 江西信访
			map.addAttribute("imgpath", "images/unselected/person_0205.png");
			map.addAttribute("imgtype", "0205");
			map.addAttribute("persontype", "11");
		} else if ("aids".equals(type)) { // 艾滋病
			//map.addAttribute("imgpath", "images/unselected/person_0205.png");
			//map.addAttribute("imgtype", "0205");
			//map.addAttribute("persontype", "11");
		} else if ("involveHydroxyl".equals(type)) { // 涉羟人员

		} else if ("detention".equals(type)) { // 2次以上行政拘留人员

		} else if ("liveAlone".equals(type)) { // 独居人

		} else if ("petitionRecordLeader".equals(type)) { // 重点信访事项的挑头人

		} else if ("criticalRecord".equals(type)) { // 危重人

		} else if ("warmHeart".equals(type)) { // 暖心对象

		}

		return forward;
	}

	@RequestMapping(value = "/toArcgisDataListOfYouth")
	public String toArcgisDataListOfYouth(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forward = "/map/arcgis/standardmappage/standardPeopleYouth.ftl";
		map.addAttribute("gridId", gridId);
		map.put("typeDC",
				baseDictionaryService.getDataDictListOfSinglestage(DictPcode.YOUTH_MI_TYPE, userInfo.getOrgCode()));
		map.addAttribute("type", type);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return forward;
	}

	@RequestMapping(value = "/toArcgisDataListOfPeopleOne")
	public String toArcgisDataListOfPeopleOne(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String forward = "/map/arcgis/standardmappage/standardPeopleOne.ftl";
		map.addAttribute("gridId", gridId);
		map.addAttribute("type", type);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return forward;
	}

	@RequestMapping(value = "/toArcgisDataListOfPeopleThree")
	public String toArcgisDataListOfPeopleThree(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String forward = "/map/arcgis/standardmappage/standardPeopleThree.ftl";
		map.addAttribute("gridId", gridId);
		map.addAttribute("type", type);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return forward;
	}

	/// ---台江地图图层分类管理重点人员 start----------------------------------------------
	@RequestMapping(value = "/gisPop")
	public String gisPop(ModelMap map, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "type") String type, HttpSession session) {
		map.addAttribute("type", type);// 人员类型
		if ("partyMerber".equals(type)) {// 党员
			map.addAttribute("imgpath", "images/unselected/person_0101.png");
			map.addAttribute("imgtype", "partyMember");
			map.addAttribute("persontype", "1");
		} else if ("2".equals(type)) {// 志愿者
			map.addAttribute("imgpath", "images/unselected/person_h_person.png");
			map.addAttribute("imgtype", "volunteer");
			map.addAttribute("persontype", "2");
		} else if ("retire".equals(type)) {// 退休人员
			map.addAttribute("imgpath", "images/unselected/person_h_0401.png");
			map.addAttribute("imgtype", "retire");
			map.addAttribute("persontype", "3");
		} else if ("homeAge".equals(type)) {// 居家养老
			map.addAttribute("imgpath", "images/unselected/person_h_0303.png");
			map.addAttribute("imgtype", "yanglao");
			map.addAttribute("persontype", "4");
		} else if ("military".equals(type)) {// 服兵役人员
			map.addAttribute("imgpath", "images/unselected/person_h_1001.png");
			map.addAttribute("imgtype", "serveArmy");
			map.addAttribute("persontype", "5");
		} else if ("unemployment".equals(type)) {// 失业人员
			map.addAttribute("imgpath", "images/unselected/person_0402.png");
			map.addAttribute("imgtype", "unemployed");
			map.addAttribute("persontype", "6");
		} else if ("welfare".equals(type)) {// 低保人员
			map.addAttribute("imgpath", "images/unselected/person_0302.png");
			map.addAttribute("imgtype", "lowObject");
			map.addAttribute("persontype", "7");
		} else if ("disability".equals(type)) {// 残障人员
			map.addAttribute("imgpath", "images/unselected/person_0301.png");
			map.addAttribute("imgtype", "physicalDisabilities");
			map.addAttribute("persontype", "8");
		} else if ("neuropathy".equals(type)) {// 重精神病
			map.addAttribute("imgpath", "images/unselected/person_0207.png");
			map.addAttribute("imgtype", "psychosis");
			map.addAttribute("persontype", "9");
		} else if ("dangerous".equals(type)) {// 危险品从业人员
			map.addAttribute("imgpath", "images/unselected/person_0206.png");
			map.addAttribute("imgtype", "dangerousGoods");
			map.addAttribute("persontype", "10");
		} else if ("petition".equals(type)) {// 上访人员
			map.addAttribute("imgpath", "images/unselected/person_0205.png");
			map.addAttribute("imgtype", "petitioner");
			map.addAttribute("persontype", "11");
		} else if ("drugs".equals(type)) {// 吸毒人员
			map.addAttribute("imgpath", "images/unselected/person_0201.png");
			map.addAttribute("imgtype", "drugAddict");
			map.addAttribute("persontype", "12");
		} else if ("heresy".equals(type)) {// 邪教人员
			map.addAttribute("imgpath", "images/unselected/person_0204.png");
			map.addAttribute("imgtype", "heresy");
			map.addAttribute("persontype", "13");
		} else if ("rectify".equals(type)) {// 矫正人员
			map.addAttribute("imgpath", "images/unselected/person_0203.png");
			map.addAttribute("imgtype", "rectify");
			map.addAttribute("persontype", "14");
		} else if ("camps".equals(type)) {// 刑事解教人员
			map.addAttribute("path", "images/unselected/person_0202.png");
			map.addAttribute("imgtype", "RehabilitationXieJiao");
			map.addAttribute("persontype", "15");
		} else if ("taibao".equals(type)) {// 台胞人员
			map.addAttribute("path", "images/unselected/person_0208.png");
			map.addAttribute("imgtype", "0208");
			map.addAttribute("persontype", "16");
		} else if ("heresy2".equals(type)) {// 摩托车工
			map.addAttribute("path", "images/unselected/person_0208.png");
			map.addAttribute("imgtype", "0208");
			map.addAttribute("persontype", "17");
		}
		map.addAttribute("gridId", gridId);
		return "/map/arcgis/standardmappage/taijiang/people.ftl";
	}
	
	@RequestMapping(value = "/toPopuPage")
	public String toPopuPage(ModelMap map, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "type") String type, HttpSession session) {
		map.addAttribute("type", type);// 人员类型
		map.addAttribute("gridId", gridId);
		MixedGridInfo gridInfo = this.iMixedGridInfoService.findMixedGridInfoById(gridId, false);
		if (gridInfo != null) {
			map.addAttribute("gridName", gridInfo.getGridName());
		}
		map.addAttribute("RS_URL", App.RS.getDomain(session));
		return "/map/arcgis/standardmappage/standardPeopleList.ftl";
	}
	
	@RequestMapping(value = "/toKeyPopuPage")
	public String toKeyPopuPage(ModelMap map, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "types", required = false) String types, HttpSession session) {
		if (StringUtils.isNotBlank(types)) {
			map.addAttribute("types", Arrays.asList(types.split(",")));// 人员类型
		}
		map.addAttribute("gridId", gridId);
		MixedGridInfo gridInfo = this.iMixedGridInfoService.findMixedGridInfoById(gridId, false);
		if (gridInfo != null) {
			map.addAttribute("gridName", gridInfo.getGridName());
		}
		map.addAttribute("RS_URL", App.RS.getDomain(session));
		return "/map/arcgis/standardmappage/standardKeyPeopleList.ftl";
	}

	/// ---重点人员 end----------------------------------------------

	/**
	 * 2014-06-17 liushi add 重点人员数据查询
	 * 
	 * @param session
	 * @param page
	 * @param gridId
	 * @param rows
	 * @param order
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfPeople", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination getArcgisDataListOfPeople(HttpSession session, HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "isVisit",required = false) String isVisit,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String identityCard = request.getParameter("identityCard");
		cn.ffcs.common.EUDGPagination eudgPagination = new cn.ffcs.common.EUDGPagination();
		if ((type == null || "".equals(type)) && (elementsCollectionStr != null || "".equals(elementsCollectionStr))) {
			type = this.analysisOfElementsCollection(elementsCollectionStr, "menuCode");
		}
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if ("partyMerber".equals(type)) {// 党员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId.toString());
			if(StringUtils.isNotBlank(infoOrgCode)) {
				criteria.setOrgCode(infoOrgCode);
			}
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			Pagination pagination = ciRsPartyService.findAllPage(null, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("volunteer".equals(type)) {// 志愿者
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId.toString());
			if (name != null && !"".equals(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setOrderColumn(order);
			Pagination pagination = ciRsService.getRsInfoPage(8, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("retire".equals(type)) {// 退休人员
			CiRsCriteria criteria = new CiRsCriteria();
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setSubdistrictid(gridId + "");
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			Pagination pagination = ciRsService.getRsInfoPage(2, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("homeAge".equals(type)) {// 居家养老
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			if (gridInfo.getGridCode().startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
				Pagination pagination = ciRsService.getRsInfoPage(14, criteria, page, rows);
				eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
			} else {
				Pagination pagination = ciRsService.getRsInfoPage(3, criteria, page, rows);
				eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
			}
		} else if ("military".equals(type)) {// 服兵役人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
            criteria.setRelCode("G");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("unemployment".equals(type)) {// 失业人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			Pagination pagination = ciRsService.getRsInfoPage(5, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("welfare".equals(type)) {// 低保人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			Pagination pagination = ciRsService.getRsInfoPage(6, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("disability".equals(type)) {// 残疾人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("E");

			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("neuropathy".equals(type)) {// 重精神病人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("L");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("dangerous".equals(type)) {// 危险品从业人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("M");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);

			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("petition".equals(type)) {// 上访人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("Q");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("drugs".equals(type)) {// 吸毒人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("P");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("heresy".equals(type)) {// 邪教人员数据查询
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("O");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("rectify".equals(type)) {// 矫正
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("N");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("camps".equals(type)) {// 刑释人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			if (StringUtils.isNotEmpty(order)){
				if (order.equals("name")){
					criteria.setSort("partyName");
				}else {
					criteria.setSort(order);
				}
			}
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("K");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("jiangxiPetition".equals(type)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", name);
			if(gridInfo != null && StringUtils.isNotBlank(gridInfo.getInfoOrgCode())){
				params.put("regionCode", gridInfo.getInfoOrgCode());
			}
			params.put("order", order);
			params.put("sOrgCode", userInfo.getOrgCode());
			eudgPagination = petitionerService.findPetitionerPagination(page, rows, params);
		} else if ("aidsPatient".equals(type)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", name);
			if (StringUtils.isNotBlank(identityCard)) {
				params.put("identityCard", identityCard);
			}
			params.put("order", order);
			if(gridInfo != null && StringUtils.isNotBlank(gridInfo.getInfoOrgCode())){
				params.put("infoOrgCode", gridInfo.getInfoOrgCode());
				params.put("orgCode", gridInfo.getInfoOrgCode());
			}
			params.put("sOrgCode", userInfo.getOrgCode());
			eudgPagination = aidsPersonService.findByPagination(page, rows, params);
		} else if ("youth".equals(type)) {// 重点青少年
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("AQ");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("involveHydroxyl".equals(type)) {// 涉羟人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("IH");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("detention".equals(type)) { // 2次以上行政拘留人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("DE");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("liveAlone".equals(type)) { // 独居人
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("LA");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("petitionRecordLeader".equals(type)) { // 重点信访事项的挑头人
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("TD");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("criticalRecord".equals(type)) { // 危重人
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("WZ");

			Pagination pagination= ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		} else if ("warmHeart".equals(type)) { // 暖心对象
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("WH");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		} else if ("poor".equals(type)) { // 贫困人口
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			if (StringUtils.isNotBlank(isVisit)) {
				criteria.setIsVisit(isVisit);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			criteria.setRelCode("BH");
			Pagination pagination = ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		}else if ("flow".equals(type)) {//流动人口
			CiRsCriteria criteria = new CiRsCriteria();
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setSubdistrictid(gridId + "");
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			Pagination pagination = ciRsService.getRsInfoPage(11,criteria, page, rows);//流动人口
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(),pagination.getList());
		}else if ("listArmy".equals(type)) {// 涉军人员
			CiRsCriteria criteria = new CiRsCriteria();
			criteria.setSubdistrictid(gridId + "");
			if (StringUtils.isNotBlank(name)) {
				criteria.setName(name);
			}
			if (StringUtils.isNotBlank(identityCard)) {
				criteria.setIdCard(identityCard);
			}
			criteria.setOrderColumn(order);
			criteria.setOrgCode(gridInfo.getInfoOrgCode());
			Pagination pagination = ciRsService.getRsInfoPage(4, criteria, page, rows);
			eudgPagination = new cn.ffcs.common.EUDGPagination(pagination.getTotalCount(), pagination.getList());
		}

		return eudgPagination;
	}

	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfPeopleOne", method = RequestMethod.POST)
	public EUDGPagination getArcgisDataListOfPeopleOne(HttpSession session, HttpServletResponse response,
			@RequestParam(value = "order", required = false) String order, @RequestParam(value = "page") int page,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "rows") int rows,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "idCard", required = false) String idCard,
			@RequestParam(value = "type", required = false) String type) {
		EUDGPagination eudgPagination = new EUDGPagination();
		CiRsCriteria criteria = new CiRsCriteria();
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if(mixedGridInfo!=null){
			String infoOrgCode = mixedGridInfo.getInfoOrgCode();
			criteria.setOrgCode(infoOrgCode);
		}
		criteria.setName(name);
		criteria.setIdCard(idCard);
		criteria.setType(type);
		criteria.setOrderColumn(order);
		if (StringUtils.isNotEmpty(order)){
			if (order.equals("name")){
				criteria.setSort("partyName");
			}else {
				criteria.setSort(order);
			}
		}
		//默认是倒序
		criteria.setOrder("desc");
		Pagination pagination = ciRsService.findRsPage(criteria, page, rows);
		eudgPagination = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
		return eudgPagination;
	}

	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfPeopleYouth", method = RequestMethod.POST)
	public EUDGPagination getArcgisDataListOfPeopleYouth(HttpSession session, HttpServletResponse response,
			@RequestParam(value = "order", required = false) String order, @RequestParam(value = "page") int page,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "rows") int rows,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "isVisit",required = false) String isVisit,
			@RequestParam(value = "mitype", required = false) String mitype,
			@RequestParam(value = "idCard", required = false) String idCard,
			@RequestParam(value = "type", required = false) String type) {
		EUDGPagination eudgPagination = new EUDGPagination();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		CiRsCriteria criteria = new CiRsCriteria();
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String infoOrgCode = mixedGridInfo.getInfoOrgCode();
		criteria.setSubdistrictid(gridId + "");
		if (StringUtils.isNotBlank(name)) {
			criteria.setName(name);
		}
		if (StringUtils.isNotBlank(idCard)) {
			criteria.setIdCard(idCard);
		}
		if (StringUtils.isNotBlank(isVisit)) {
			criteria.setIsVisit(isVisit);
		}
		criteria.setOrderColumn(order);
		criteria.setOrgCode(infoOrgCode);
		criteria.setRelCode("AQ");

		Pagination pagination= ciRsService.getRsInfoPageWithVisit(userInfo,criteria,page,rows);
		eudgPagination = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
		return eudgPagination;
	}

	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfPeopleThree", method = RequestMethod.POST)
	public EUDGPagination getArcgisDataListOfPeopleThree(HttpSession session, HttpServletResponse response,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "gridId") Long gridId, @RequestParam(value = "rows") int rows,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value = "idCard", required = false) String idCard) {
		EUDGPagination eudgPagination = new EUDGPagination();
		Map<String, Object> params = new HashMap<String, Object>();
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if ((type == null || "".equals(type)) && (elementsCollectionStr != null || "".equals(elementsCollectionStr))) {
			type = this.analysisOfElementsCollection(elementsCollectionStr, "menuCode");
		}
		if ("cleaner".equals(type)) {
			params.put("name", name);
			params.put("memberType", "001");
			params.put("gridCode", gridInfo.getGridCode());
			params.put("idCard", idCard);
			cn.ffcs.system.publicUtil.EUDGPagination pagination = iMemberInfoService.findMemberInfoMapPagination(page,
					rows, params);
			eudgPagination = new EUDGPagination(pagination.getTotal(), pagination.getRows());
		} else if ("driver".equals(type)) {
			params.put("name", name);
			params.put("memberType", "002");
			params.put("gridCode", gridInfo.getGridCode());
			params.put("idCard", idCard);
			cn.ffcs.system.publicUtil.EUDGPagination pagination = iMemberInfoService.findMemberInfoMapPagination(page,
					rows, params);
			eudgPagination = new EUDGPagination(pagination.getTotal(), pagination.getRows());
		} else if ("manage".equals(type)) {
			params.put("name", name);
			params.put("memberType", "003");
			params.put("gridCode", gridInfo.getGridCode());
			params.put("idCard", idCard);
			cn.ffcs.system.publicUtil.EUDGPagination pagination = iMemberInfoService.findMemberInfoMapPagination(page,
					rows, params);
			eudgPagination = new EUDGPagination(pagination.getTotal(), pagination.getRows());
		}

		return eudgPagination;
	}

	/**
	 * 2014-06-19 liushi add 获取人员的定位信息
	 * 
	 * @param session
	 * @param userIds
	 *            人员ids
	 * @param mapt
	 *            地图类型
	 * @param type
	 *            人员类型
	 * @param showType
	 *            地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfPeople")
	public List<ArcgisInfoOfPeople> getArcgisPopLocateDataList(HttpSession session,
			@RequestParam(value = "userIds") String userIds,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {

		List<ArcgisInfoOfPeople> list = new ArrayList<ArcgisInfoOfPeople>();

		if (StringUtils.isNotBlank(userIds)) {
			String[] uIds = userIds.split(",");
			if (uIds.length > 1000) {
				List<String> listIds = new ArrayList<String>();
				for (int i = 0; i < 1000; i++) {
					listIds.add(uIds[i]);
				}
				userIds = listIds.toString().replace("[", "").replace("]", "").replace(" ", "");
			}
			list = this.arcgisDataOfLocalService.getArcgisPopLocateDataListByUserIds(userIds, mapt, infoOrgCode);
			for (ArcgisInfoOfPeople arcgisInfoOfPeople : list) {
				arcgisInfoOfPeople.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		return list;
	}
	
	/**
	 * 2016-04-14 qiudong add 获取人员的定位信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfPeopleWithMemberId")
	public List<ArcgisInfoOfPublic> getArcgisPopLocateDataList(HttpSession session,
			@RequestParam(value = "memberId") String memberId,
			@RequestParam(value = "type", required = false) String type, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {

		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();

		list = this.arcgisDataOfLocalService.getArcgisPopLocateDataListByMemberId(memberId, mapt);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	@RequestMapping(value = "/getPeopleInfoDetailOnMap")
	public String getBuildInfoDetailOnMap(HttpSession session,
			@RequestParam(value = "ciRsId", required = false) Long ciRsId,
			@RequestParam(value = "partyId", required = false) Long partyId,
			@RequestParam(value = "wid", required = false) String wid,
			@RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {
		// Map<String, Object> residentDetail = new HashMap<String, Object>();

		Long buildingId = null;
		if (StringUtils.isNotBlank(wid)) {
			String[] widArr = wid.split("-");
			if(widArr != null && widArr.length >0) {
				partyId = Long.parseLong(widArr[0]);
			}
			if(widArr != null && widArr.length >1){
				ciRsId = Long.parseLong(widArr[1]);
			}
			if(widArr != null && widArr.length >2){
				buildingId = Long.parseLong(widArr[2]);
			}
		}

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PartyIndividual party = partyIndividualService.findById(userInfo,partyId);
		//CiRsTop residentDetail = ciRsService.findCiRsTopsByCiRsId(ciRsId);
		//TODO:人员房屋绑定的功能还没有
		List<Map<String, Object>> listOfBuild = new ArrayList<>();
		if(buildingId != null){
//			CiRs ciRs = ciRsService.findById(ciRsId);
//			map.addAttribute("ciRs", ciRs);

			//TODO:人员房屋绑定的功能还没有
			listOfBuild = this.arcgisDataOfLocalService.getBuildOfPopByBuildingId(buildingId);
		}

		String buildingName = "";
		if(listOfBuild != null && listOfBuild.size() >0) {
			for (Map<String, Object> obj : listOfBuild) {
				buildingName += "".equals(buildingName)
						? "<a style='text-align:left;color:#0075a9;line-height:24px;font-size:12px;font-weight:bold;' href='javascript:void(0)' onclick='showBuild("
						+ obj.get("BUILDING_ID").toString() + ")'>" + (String) obj.get("BUILDING_NAME") + "</a>"
						: ",<a href='javascript:void(0)' style='text-align:left;color:#0075a9;line-height:24px;font-size:12px;font-weight:bold;' onclick='showBuild("
						+ obj.get("BUILDING_ID").toString() + ")'>" + (String) obj.get("BUILDING_NAME") + "</a>";
			}
		}

		List<CiRsRel> tagFlag = ciRsRelService.findByPartyIdAndOrgCode(partyId, userInfo.getOrgCode());// 人员标签
		
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);

		List<String[]> tagList = new ArrayList<String[]>();
		if(tagFlag != null && tagFlag.size() >0){
			for (int i=0;i<tagFlag.size();i++){
				CiRsRel c = tagFlag.get(i);
				if (c.getViewUrl() != null) {
					c.setViewUrl(c.getViewUrl().replaceAll("\\$\\{partyId\\}", String.valueOf(partyId)));
				}
				
				String typeVal = getTypeVlue(tagFlag.get(i).getRelCode());
				if (StringUtils.isNotBlank(typeVal)) {
					String[] tag = { typeVal, getValue(tagFlag.get(i).getRelCode()) };
					tagList.add(tag);
				}
			}
		}



		List<Map<String, Object>> tags = dictionaryService.getTableColumnDCKeyPerson(String.valueOf(partyId));

		if (tags != null && tags.size() > 0) {
			for (Map<String, Object> tag : tags) {
				String value = (String) tag.get("value");

				if (value.equals("AR")) {
					tagList.add(new String[] { "18", "信访" });
					break;
				}
			}
		}

		MixedGridInfo mixedGridInfo = new MixedGridInfo();
		if(party != null && StringUtils.isNotBlank(party.getOrgCode())) {
			mixedGridInfo = iMixedGridInfoService.getDefaultGridByOrgCode(party.getOrgCode());
		}
		Long gridId2 = 0l;
		String infoOrgCode = "";

		if(mixedGridInfo != null) {
			if(mixedGridInfo.getGridId() != null) {
				gridId2 = mixedGridInfo.getGridId();
			}
			if(StringUtils.isNotBlank(mixedGridInfo.getInfoOrgCode())){
				infoOrgCode=mixedGridInfo.getInfoOrgCode();
			}
		}

		
		String gridNames="";
		if(gridId2!=null && gridId2 >0l){
			gridNames= mixedGridInfoService.getGridPath(gridId2);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("partyId", partyId);

		CiRsCriteria  criteria =new CiRsCriteria();
		criteria.setServiceObject(""+partyId);
		criteria.setOrgCode(userInfo.getOrgCode());
		Pagination pagination = visitRecordService.findPageVisitRecord(criteria, 1,
				5);
		map.addAttribute("visitRecordList", pagination.getList());
		map.addAttribute("visitRecordCount", pagination.getTotalCount());
		
		List<BaseDataDict> crowdDict = baseDictionaryService.getDataDictListOfSinglestage("B912008", userInfo.getOrgCode());//特殊人群字典
		map.addAttribute("crowdDict", crowdDict);	

		List<String> tagCodeList = new ArrayList<String>();
		params = new HashMap<String, Object>();
		params.put("catalogId", 40000009);
		params.put("dictCode", "B912008003");
		params.put("orgCode", userInfo.getOrgCode());
		params.put("pubUserId", userInfo.getUserId());
		params.put("status", "2");
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("subClassPcode", "B913");
		cn.ffcs.common.EUDGPagination policyLow = iInfoOpenService.findReleaseList(1, 9999, params);
		if(policyLow != null && policyLow.getRows() != null && policyLow.getTotal() >0){
			map.addAttribute("showPolicyLawBut", "yes");
		}

		for(String[] str : tagList){
			for(BaseDataDict dict : crowdDict){
				if(getDictMap(str[0]).equals(dict.getDictGeneralCode())){
					tagCodeList.add(dict.getDictCode());
				}
			}
		}
		map.addAttribute("tagCodeList", tagCodeList);
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("buildingName", buildingName);
		map.addAttribute("isCross", isCross);
		map.addAttribute("gridId", gridId);
		map.addAttribute("tag", tagList);
		map.addAttribute("tagFlag", tagFlag);
		map.addAttribute("residentDetail", party);
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("RS_DOMAIN", App.RS.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_GMIS_URL", App.GMIS.getDomain(session));
		map.addAttribute("SQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/peopleInfoDetailOnMap.ftl";
	}
	
	@RequestMapping(value = "/getPartyDetailOnMap")
	public String getPartyDetailOnMap(HttpSession session,
			@RequestParam(value = "partyId", required = false) Long partyId,
			@RequestParam(value = "wid", required = false) Long wid, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {

		CiRsTop residentDetail = null;
		CiRsParty ciRsParty = ciRsPartyService.findById(partyId);
		if (ciRsParty != null && ciRsParty.getCiRsId() != null) {
			residentDetail = ciRsService.findCiRsTopsByCiRsId(ciRsParty.getCiRsId());
		}
		
		if (ciRsParty != null) {
			MixedGridInfo mixedGridInfo = iMixedGridInfoService.getDefaultGridByOrgCode(ciRsParty.getOrgCode());
			Long gridId2 = mixedGridInfo.getGridId();
			String infoOrgCode = mixedGridInfo.getInfoOrgCode();
			String gridNames = "";
			if (gridId2 != null) {
				gridNames = mixedGridInfoService.getGridPath(gridId);
			} else {
				gridNames = mixedGridInfoService.getGridPath(infoOrgCode);
			}
			map.addAttribute("gridNames", gridNames);
		}
		
		map.addAttribute("isCross", isCross);
		map.addAttribute("gridId", gridId);
		map.addAttribute("ciRsParty", ciRsParty);
		map.addAttribute("residentDetail", residentDetail);
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/partyDetailOnMap.ftl";
	}

	@RequestMapping("/showPolicyLaw")
	public String showPolicyLaw(HttpSession session, ModelMap map, 
			@RequestParam(value = "tagKey", required = true) String tagKey,
			@RequestParam(value = "gridId", required = true) Long gridId) throws Exception{
		MixedGridInfo gridInfo = this.iMixedGridInfoService.findMixedGridInfoById(gridId, false);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> crowdDict = baseDictionaryService.getDataDictListOfSinglestage("B912008", userInfo.getOrgCode());//特殊人群字典
		List<String> tagCodeList = new ArrayList<String>();
		List<String> tagNameList = new ArrayList<String>();
		String[] tagKeyArray = tagKey.split(",");
		for(int i = 0; i<tagKeyArray.length; i++){
			for(BaseDataDict dict : crowdDict){
				if(getDictMap(tagKeyArray[i]).equals(dict.getDictGeneralCode())){
					tagCodeList.add(dict.getDictCode());
					tagNameList.add(dict.getDictName());
				}
			}
		}
		String subClassName = "法律法规类型";
		if (StringUtils.isNotBlank(subClassName)) {
			subClassName = URLEncoder.encode(subClassName.trim(), "UTF-8").replaceAll("%", "!");
			map.put("subClassName", subClassName);
		}
		
		map.addAttribute("tagCodeList", tagCodeList);
		map.addAttribute("tagNameList", tagNameList);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("orgCode", userInfo.getOrgCode());
		map.addAttribute("modetype", "policyLaw");//政策法规标识
		return "/map/arcgis/standardmappage/spelist.ftl";
	}
	
	@RequestMapping("/showEmergencyWay")
	public String showEmergencyWay(HttpSession session, ModelMap map, 
			@RequestParam(value = "catalogId", required = true) String catalogId,
			@RequestParam(value = "subClassPcode", required = true) String subClassPcode) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> emergencyWayDict = baseDictionaryService.getDataDictTree("B177", userInfo.getOrgCode());//应急预案字典
		List<String> dictCodeList = new ArrayList<String>();
		List<String> dictNameList = new ArrayList<String>();
		String dictCodeStr = "";
		String dictNameStr = "";
		for(BaseDataDict dict : emergencyWayDict){
			if(dict.getDictLevel() != null && dict.getDictLevel() == 3){//dict.getIsLeaf() != null && "0".equals(dict.getIsLeaf())
				if(dictCodeStr != ""){
					dictCodeList.add(dictCodeStr);
				}
				if(dictNameStr != ""){
					dictNameList.add(dictNameStr);
				}
				dictCodeStr = "";
				dictCodeStr += dict.getDictCode()+"/";
				dictNameStr = "";
				dictNameStr += dict.getDictName()+"/";
			}else if(dict.getDictLevel() != null && dict.getDictLevel() == 4){
				dictCodeStr += dict.getDictCode()+"/";
				dictNameStr += dict.getDictName()+"/";
			}
		}
		dictCodeList.add(dictCodeStr);
		dictNameList.add(dictNameStr);
		map.addAttribute("dictCodeList", dictCodeList);
		map.addAttribute("dictNameList", dictNameList);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("orgCode", userInfo.getOrgCode());
		map.addAttribute("catalogId", catalogId);
		map.addAttribute("subClassPcode", subClassPcode);
		return "/map/arcgis/standardmappage/emergencySpelist.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/releaseList")
	public cn.ffcs.common.EUDGPagination releaseList(
			HttpSession session,
			ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "startPubDate", required = false) String startPubDate,
			@RequestParam(value = "endPubDate", required = false) String endPubDate,
			@RequestParam(value = "orgId", required = false) String orgId,
			@RequestParam(value = "orgCode", required = false) String orgCode,
			@RequestParam(value = "orgName", required = false) String orgName,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "catalogId", required = false) Long catalogId,
			@RequestParam(value = "catalogCode", required = false) String catalogCode,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "mode", required = false) String mode,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "subClassName", required = false) String subClassName,
			@RequestParam(value = "subClassPcode", required = false) String subClassPcode,
			@RequestParam(value = "onlyShowLevel", required = false) String onlyShowLevel,
			@RequestParam(value = "onlyShowMyData", required = false) String onlyShowMyData) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (rows <= 0) {
			rows = ConstantValue.DEFAULT_PAGE_SIZE;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyWord", keyWord);
		params.put("startPubDate", startPubDate);
		params.put("endPubDate", endPubDate);
		params.put("catalogId", catalogId);
		params.put("dictCode", catalogCode);
		params.put("infoOrgCode", infoOrgCode);
		params.put("orgId", orgId);
		params.put("orgCode", orgCode);
		params.put("infoOrgCode", infoOrgCode);
		params.put("onlyShowLevel", onlyShowLevel);
		params.put("onlyShowMyData", onlyShowMyData);
		params.put("pubUserId", userInfo.getUserId());
		params.put("status", status);
		params.put("orgName", orgName);
		if (StringUtils.isNotBlank(subClassName)) {
			params.put("subClassName", subClassName.trim());
		}
		if (StringUtils.isNotBlank(subClassPcode)) {
			params.put("userOrgCode", userInfo.getOrgCode());
			params.put("subClassPcode", subClassPcode.trim());
		}
		if ("view".equals(mode)) {
			params.put("isViewModel", true);
		}
		cn.ffcs.common.EUDGPagination pagination = iInfoOpenService.findReleaseList(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping("/getGridPath")
	public String getGridPath(
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode) {
		String path="";
		if(gridId!=null){
			path= mixedGridInfoService.getGridPath(gridId);
		}else{
			path= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		return path;
	}


	/**
	 * 信访责任人
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/petition")
	public String petition(HttpSession session, HttpServletRequest request, ModelMap map,
						   @RequestParam(value = "gridId") Long gridId,
						   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("bizType", "PETITION");//信访责任人
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardPetition.ftl";
	}

	/**
	 * 获取信访责任人列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/petitionListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination petitionListData(HttpSession session,
										   @RequestParam(value="page") int page,
										   @RequestParam(value="rows") int rows,
										   @RequestParam(value="gridId") Long gridId,
										   @RequestParam(value="name", required=false) String name,
										   @RequestParam(value="orgCode", required=false) String orgCode,
										   @RequestParam(value="bizType", required=false) String bizType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(name!=null) name = name.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if ((orgCode == null || "".equals(orgCode)) && gridId != null) {
			MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			orgCode = mixedGridInfo.getInfoOrgCode();
		}
		params.put("orgCode", orgCode);
		if(StringUtils.isNotBlank(name)){
			params.put("name", name);
		}
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getOrgCode())){
			params.put("orgCode", userInfo.getOrgCode());
		}
		if(StringUtils.isNotBlank(bizType)){
			params.put("bizType", bizType);
		}else{
			params.put("bizType", "PETITION");
		}
		cn.ffcs.common.EUDGPagination pagination = teamMembersService.findMembersPagination(page, rows, params);
		return pagination;
	}

	/**
	 * * 获取信访责任人的定位信息
	 * @param session
	 * @param request
	 * @param ids 网格员ids
	 * @param mapt 地图类型
	 * @param order
	 * @param name
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLocateDataListOfPetition")
	public List<ArcgisInfoOfPublic> getLocateDataListOfPetition(HttpSession session,
																HttpServletRequest request,
																@RequestParam(value = "ids") String ids,
																@RequestParam(value = "order", required=false) String order,
																@RequestParam(value = "name", required=false) String name,
																@RequestParam(value = "mapt") Integer mapt,
																@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisPetitionLocateDataListByIds(ids,mapt);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 信访责任人信息
	 * @param session
	 * @param memberId
	 * @param mapt
	 * @param bizType
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/petitionDetail")
	public String petitionDetail(HttpSession session,
								 @RequestParam(value = "memberId") Long memberId,
								 @RequestParam(value = "mapt", required = false) Integer mapt,
								 @RequestParam(value = "bizType", required = false) String bizType,
								 ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("tel_show", 1); // 拨号
		map.addAttribute("gota_show", 0); // gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		//是否启用视频通话
		String IS_USER_MMP = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_MMP, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserMmp", IS_USER_MMP);
		try{
			TeamMembers teamMember = teamMembersService.getTeamMembersById(memberId, userInfo.getOrgCode());

//		if(teamMember != null && StringUtils.isNotBlank(teamMember.getOrgCode())){
//			List<MixedGridInfo> gridInfo = mixedGridInfoService.getMixedGridMappingListByOrgCode(teamMember.getOrgCode());
//			Long parentGridId = 0l;
//			if(gridInfo != null && gridInfo.size() >0){
//				parentGridId = gridInfo.get(0).getParentGridId();
//			}
//			String gridPath = mixedGridInfoService.getGridPath(parentGridId);
//			map.addAttribute("gridNames", gridPath);
//		}

			map.addAttribute("teamMember", teamMember);
			map.addAttribute("memberId", memberId);
			map.addAttribute("imsi", teamMember.getImsi());
			map.addAttribute("bizType", bizType);

		}catch (Exception e){
			e.printStackTrace();
		}


		return "/map/arcgis/standardmappage/petitionDetail.ftl";
	}

	/**
	 * 两站两员
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/twoMember")
	public String twoMember(HttpSession session, HttpServletRequest request, ModelMap map,
										   @RequestParam(value = "gridId") Long gridId,
										   @RequestParam(value = "infoOrgCode") String infoOrgCode,
										   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("bizType", "PETITION");//信访责任人
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/lzly/twoMember.ftl";
	}

	@Autowired
	private ITwoMemberService twoMemberService;

	/**
	 * 获取两站两员列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/twoMemberListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination twoMemberListData(HttpSession session,
			   	@RequestParam(value="page") int page,
				@RequestParam(value="rows") int rows,
				@RequestParam(value="regionCode") String regionCode,
				@RequestParam(value="name", required=false) String name,
				@RequestParam(value="orgCode", required=false) String orgCode,
				@RequestParam(value="bizType", required=false) String bizType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(name!=null) name = name.trim();
		Map<String, Object> params = new HashMap<String, Object>();
//		if ((orgCode == null || "".equals(orgCode)) && gridId != null) {
//			MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
//			orgCode = mixedGridInfo.getInfoOrgCode();
//		}
		params.put("regionCode", regionCode);
		params.put("orgCode", userInfo.getOrgCode());
		if(StringUtils.isNotBlank(name)){
			params.put("memberName", name);
		}
		cn.ffcs.common.EUDGPagination pagination = twoMemberService.findPagination(page, rows, params);
		return pagination;
	}

	/**
	 * * 获取两站两员的定位信息
	 * @param session
	 * @param request
	 * @param ids 网格员ids
	 * @param mapt 地图类型
	 * @param order
	 * @param name
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLocateDataListOfTwoMember")
	public List<ArcgisInfoOfPublic> getLocateDataListOfTwoMember(HttpSession session,
			 HttpServletRequest request,
			 @RequestParam(value = "ids") String ids,
			 @RequestParam(value = "order", required=false) String order,
			 @RequestParam(value = "name", required=false) String name,
			 @RequestParam(value = "mapt") Integer mapt,
			 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisTwoMemberLocateDataListByIds(ids,mapt);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 信访责任人信息
	 * @param session
	 * @param memberId
	 * @param mapt
	 * @param bizType
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/twoMemberDetail")
	public String twoMemberDetail(HttpSession session,
			@RequestParam(value = "tmId") Long tmId,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "bizType", required = false) String bizType,
		ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("tel_show", 1); // 拨号
		map.addAttribute("gota_show", 0); // gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		//是否启用视频通话
		String IS_USER_MMP = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_MMP, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserMmp", IS_USER_MMP);
		try{
			TwoMember twoMember = twoMemberService.findById(tmId, userInfo.getOrgCode());
			map.addAttribute("teamMember", twoMember);
			map.addAttribute("memberId", tmId);
//			map.addAttribute("imsi", teamMember.getImsi());
			map.addAttribute("bizType", bizType);

		}catch (Exception e){
			e.printStackTrace();
		}
		return "/map/arcgis/standardmappage/lzly/twoMemberDetail.ftl";
	}

	/**
	 * 甘肃禁毒
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfGanSuDrug")
	public String toArcgisDataListOfGanSuDrug(HttpSession session, HttpServletRequest request, ModelMap map,
										   @RequestParam(value = "gridId") Long gridId,
										   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String forward = "/map/arcgis/standardmappage/standardGanSuDrug.ftl";
		map.addAttribute("gridId", gridId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return forward;
	}

	/**
	 * 获取甘肃禁毒列表数据
	 * @param session
	 * @param response
	 * @param request
	 * @param page
	 * @param gridId
	 * @param rows
	 * @param order
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfOfGanSuDrug", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination getArcgisDataListOfOfGanSuDrug(HttpSession session, HttpServletResponse response,
													HttpServletRequest request,
													@RequestParam(value = "page") int page, @RequestParam(value = "gridId") Long gridId,
													@RequestParam(value = "rows") int rows, @RequestParam(value = "order", required = false) String order,
													@RequestParam(value = "name", required = false) String name,
													@RequestParam(value = "dType", required = false) String dType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String identityCard = request.getParameter("identityCard");
		cn.ffcs.common.EUDGPagination eudgPagination = new cn.ffcs.common.EUDGPagination();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("name", name);
		if (StringUtils.isNotBlank(identityCard)) {
			params.put("identityCard", identityCard);
		}
		if (StringUtils.isNotBlank(dType)) {
			params.put("dtype", dType);
		}
		params.put("order", order);
		params.put("sOrgCode", userInfo.getOrgCode());
		eudgPagination = drugRecordService.findDrugRecordPagination620D(page, rows, params);
		return eudgPagination;
	}

	/**
	 * 获取甘肃禁毒定位数据
	 * @param session
	 * @param userIds
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfGanSuDrug")
	public List<ArcgisInfo> getArcgisPopLocateDataListOfGanSuDrug(HttpSession session,
													   @RequestParam(value = "userIds") String userIds,
													   @RequestParam(value = "mapt") Integer mapt,
													   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = new ArrayList<ArcgisInfo>();
		if (StringUtils.isNotBlank(userIds)) {
			String[] uIds = userIds.split(",");
			if (uIds.length > 1000) {
				List<String> listIds = new ArrayList<String>();
				for (int i = 0; i < 1000; i++) {
					listIds.add(uIds[i]);
				}
				userIds = listIds.toString().replace("[", "").replace("]", "").replace(" ", "");
			}
			list = this.arcgisDataOfLocalService.getArcgisGanSuDrugLocateDataListByUserIds(userIds, mapt);
			for (ArcgisInfo arcgisInfo : list) {
				String elementsCollectionStrReplace = "";
				if(arcgisInfo != null && StringUtils.isNotBlank(arcgisInfo.getSubBusiType())){
					elementsCollectionStrReplace = formatElementsCollectionStr2(elementsCollectionStr, "ganSuDrugs", arcgisInfo.getSubBusiType());
				}
				arcgisInfo.setElementsCollectionStr(elementsCollectionStrReplace);
			}
		}
		return list;
	}

	@RequestMapping(value = "/getGanSuDrugInfoDetailOnMap")
	public String getGanSuDrugInfoDetailOnMap(HttpSession session,
										  @RequestParam(value = "ciRsId", required = false) Long ciRsId,
										  @RequestParam(value = "wid", required = false) Long wid, @RequestParam(value = "gridId") long gridId,
										  @RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {

		if (wid != null && wid != 0) {
			ciRsId = wid;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		CiRsTop residentDetail = ciRsService.findCiRsTopsByCiRsId(ciRsId);
		DrugRecord drugRecord = new DrugRecord();
		drugRecord = drugRecordService.findByCiRsId(ciRsId, userInfo.getOrgCode());

		List<Map<String, Object>> listOfBuild = this.arcgisDataOfLocalService.getBuildOfPopByUserId(ciRsId);
		String buildingName = "";
		for (Map<String, Object> obj : listOfBuild) {
			buildingName += "".equals(buildingName)
					? "<a style='text-align:left;color:#0075a9;line-height:24px;font-size:12px;font-weight:bold;' href='javascript:void(0)' onclick='showBuild("
					+ obj.get("BUILDING_ID").toString() + ")'>" + (String) obj.get("BUILDING_NAME") + "</a>"
					: ",<a href='javascript:void(0)' style='text-align:left;color:#0075a9;line-height:24px;font-size:12px;font-weight:bold;' onclick='showBuild("
					+ obj.get("BUILDING_ID").toString() + ")'>" + (String) obj.get("BUILDING_NAME") + "</a>";
		}


		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		//是否启用国标
		String ENABLE_GB = this.funConfigurationService.turnCodeToValue(
				ConstantValue.ENABLE_GB, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);


		MixedGridInfo mixedGridInfo = iMixedGridInfoService.getDefaultGridByOrgCode(residentDetail.getOrgCode());
		Long gridId2 = mixedGridInfo.getGridId();
		String infoOrgCode=mixedGridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId2!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ciRsId", ciRsId);
		CiRsCriteria ciRsCriteria=new CiRsCriteria();
		ciRsCriteria.setCiRsId(ciRsId);
		int visitRecordCount = visitRecordService.countVisit(ciRsCriteria).intValue();
		if(drugRecord != null){
			map.addAttribute("drugRecord", drugRecord);
		}
		map.addAttribute("visitRecordCount", visitRecordCount);

		map.addAttribute("gridNames", gridNames);
		map.addAttribute("buildingName", buildingName);
		map.addAttribute("ENABLE_GB", ENABLE_GB);
		map.addAttribute("isCross", isCross);
		map.addAttribute("gridId", gridId);
		map.addAttribute("residentDetail", residentDetail);
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/gansuDrugInfoDetailOnMap.ftl";
	}

	/**
	 * 执法人员
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/lawEnforceOffic")
	public String lawEnforceOffic(HttpSession session, HttpServletRequest request, ModelMap map,
											  @RequestParam(value = "gridId") Long gridId,
											  @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		System.out.println(elementsCollectionStr);
		String orgCode = ConstantValue.LAW_ENFORCE_OFFIC_FUNC_ORG_CODE;
		OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.selectOrgSocialInfoByOrgCode(orgCode);
		if(orgSocialInfoBO != null && orgSocialInfoBO.getOrgId() != null){
			map.addAttribute("socialOrgId", orgSocialInfoBO.getOrgId());
		}
		return "/map/arcgis/standardmappage/lawEnforceOffic/standardLawEnforceOffic.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/lawEnforceOfficListData", method=RequestMethod.POST)
	public EUDGPagination lawEnforceOfficListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
												  @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="socialOrgId") Long socialOrgId,
			@RequestParam(value="name", required=false) String name) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		if(name!=null) {
			name = name.trim();
			params.put("partyName",name);
		}
		if(socialOrgId!=null) {
			params.put("orgId",socialOrgId);
		}

		cn.ffcs.system.publicUtil.EUDGPagination pagination = userManageOutService.getOutUserPageByParams(params, page, rows);
		EUDGPagination eudgPagination = null;
		if(pagination != null){
			List<UserBO> userBOs = (List<UserBO>) pagination.getRows();
			if(userBOs.size() > 0){
				for(UserBO userBO : userBOs){
					Long userId = userBO.getUserId();
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("userId", userId);
					List<PositionInfoBO> positionInfoBOs = positionInfoOutService.queryPositionByPara(param);
					if(null != positionInfoBOs && positionInfoBOs.size()>0){
						userBO.setPositionName(positionInfoBOs.get(0).getPositionName());
					}
				}
			}
			eudgPagination = new EUDGPagination(pagination.getTotal(),pagination.getRows());
		}
		return eudgPagination;
	}

	@RequestMapping(value="/lawEnforceOfficDetail")
	public String lawEnforceOfficDetail(HttpSession session, ModelMap map
			, @RequestParam(value="userId", required=false) Long userId
			, @RequestParam(value="wid", required=false) Long wid
			, @RequestParam(value = "startTime", required=false) String startTime
			, @RequestParam(value = "endTime", required=false) String endTime
			, @RequestParam(value = "play", required=false) String play) {
		if(wid != null && wid !=0l){
			userId = wid;
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -24);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(StringUtils.isBlank(startTime)){
			startTime = sdf.format(ca.getTime());
		}
		if(StringUtils.isBlank(endTime)){
			endTime = sdf.format(new Date());
		}
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("currentDate", sdf.format(new Date()));
		if(StringUtils.isNotBlank(play)){//是否立即播放轨迹
			map.addAttribute("play", play);
		}

		UserBO userBO = userManageOutService.getUserInfoByUserId(userId);
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		System.out.println(userId);
		List<PositionInfoBO> positionInfoBOs = positionInfoOutService.queryPositionByPara(param);
		if(null != positionInfoBOs && positionInfoBOs.size()>0){
			userBO.setPositionName(positionInfoBOs.get(0).getPositionName());
		}
		System.out.println(userBO.getPositionName());
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		map.addAttribute("userBO", userBO);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		return "/map/arcgis/standardmappage/lawEnforceOffic/lawEnforceOfficDetail.ftl";
	}


	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOflawEnforceOffic")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOflawEnforceOffic(HttpSession session,
			HttpServletRequest request,
			 @RequestParam(value = "ids") String ids,
			 @RequestParam(value = "mapt") Integer mapt,
			 @RequestParam(value = "socialOrgId") Integer socialOrgId,
			 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);

		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		boolean getByUserNameFlag = false;
		if(StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)){
			getByUserNameFlag = true;
		}else{
			getByUserNameFlag = false;
		}
		list = this.arcgisDataOfLocalService.getArcgisLawEnforceOfficLocateDataListByIds(ids,mapt, socialOrgId);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			UserBO userBO = new UserBO();
			userBO = userManageOutService.getUserInfoByUserId(arcgisInfoOfPublic.getWid());
			String partyName = "";
			if(userBO != null && StringUtils.isNotBlank(userBO.getPartyName())){
				partyName = userBO.getPartyName();
			}
			if(arcgisInfoOfPublic.getHandleDate() != null){
				partyName = partyName + "（最后定位时间：" + DateUtils.formatDate(arcgisInfoOfPublic.getHandleDate(), "yyyy-MM-dd HH:mm:ss") +"）";
			}
			arcgisInfoOfPublic.setName(partyName);

			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}


	public String getTypeVlue(String key) {
		Map map = new HashMap();
		map.put("A", "1");
		map.put("C", "2");
		map.put("F", "3");
		map.put("AI", "3");
		map.put("G", "4");
		map.put("D", "5");
		map.put("B", "6");
		map.put("E", "7");
		map.put("L", "8");
		map.put("M", "9");
		map.put("Q", "10");
		map.put("P", "11");
		map.put("O", "12");
		map.put("N", "13");
		map.put("K", "14");
		map.put("ZY", "15");
		map.put("W", "16");
		map.put("AQ", "19");
		map.put("AS", "21");
		map.put("IH", "22");
		map.put("DE", "23");
		map.put("TD", "24");
		map.put("LA", "25");
		map.put("WZ", "26");
		map.put("WH", "27");

		return map.get(key) != null ? map.get(key).toString() : "";
	}

	public String getValue(String key) {
		Map map = new HashMap();
		map.put("A", "党员");
		map.put("C", "退休");
		map.put("F", "老年人");
		map.put("AI", "居家养老");
		map.put("G", "服兵役");
		map.put("D", "失业");
		map.put("B", "低保");
		map.put("E", "残障");
		map.put("L", "精神障碍");
		map.put("M", "危险品");
		map.put("Q", "上访");
		map.put("P", "吸毒");
		map.put("O", "邪教人员");
		map.put("N", "矫正");
		map.put("K", "刑释");
		map.put("ZY", "志愿者");
		map.put("W", "台胞");
		map.put("AQ", "重点青少年");
		map.put("AS", "艾滋病人员");
		map.put("IH", "涉羟人员");
		map.put("DE", "2次以上行政拘留人员");
		map.put("TD", "重点信访事项的挑头人");
		map.put("LA", "独居人");
		map.put("WZ", "危重人");
		map.put("WH", "暖心对象");
		return map.get(key) != null ? map.get(key).toString() : "";
	}
	
	public String getDictMap(String key) {
		Map map = new HashMap();
		map.put("8", "04");
		map.put("11", "03");
		map.put("13", "02");
		map.put("14", "01");
		map.put("21", "05");
		return map.get(key) != null ? map.get(key).toString() : "";
	}

	/**
	 * 甘肃吸毒人员定位图标分为在控和失控
	 * @param elementsCollectionStr
	 * @param controlDesc	管控情况
	 * @return
	 */
	private String formatElementsCollectionStr(String elementsCollectionStr, String controlDesc){
		System.out.println(elementsCollectionStr);
		if(controlDesc.equals("1")){//在控
			elementsCollectionStr = elementsCollectionStr
					.replace("/images/map/gisv0/map_config/unselected/mark_drugs.png", "/images/map/gisv0/map_config/unselected/drug/mark_drug_zaikong.png")
					.replace("drugsLayer", "drugsLayer1");
		}else if(controlDesc.equals("2")){//失控
			elementsCollectionStr = elementsCollectionStr
					.replace("/images/map/gisv0/map_config/unselected/mark_drugs.png", "/images/map/gisv0/map_config/unselected/drug/mark_drugs_shikong.png")
					.replace("drugsLayer", "drugsLayer2");
		}else{//其他管控类型
			elementsCollectionStr = elementsCollectionStr
					.replace("/images/map/gisv0/map_config/unselected/mark_drugs.png", "/images/map/gisv0/map_config/unselected/drug/mark_drug_qita.png")
					.replace("drugsLayer", "drugsLayer3");
		}
		return elementsCollectionStr;
	}

	/**
	 * 根据业务类型替换不同的定位图标
	 * @param elementsCollectionStr
	 * @param modelType
	 * @param typeCode
	 * @return
	 */
	private String formatElementsCollectionStr2(String elementsCollectionStr, String modelType, String typeCode){
		System.out.println(elementsCollectionStr);
		String elementsCollectionStrType = elementsCollectionStr;
		if(StringUtils.isNotBlank(modelType)){
			if("ganSuDrugs".equals(modelType)){//吸毒
				elementsCollectionStrType = elementsCollectionStr
						.replaceAll("/images/map/gisv0/map_config/unselected/mark_drugs.png", "/images/map/gisv0/map_config/unselected/drug/mark_drug_" + typeCode + ".png")
						.replaceAll("ganSuDrugsLayer", "ganSuDrugsLayer" + typeCode);
			}
		}
		return elementsCollectionStrType;
	}
}
