package cn.ffcs.zhsq.homicideCase.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicFilter.CommonController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.resident.bo.CiRsTop;
import cn.ffcs.resident.service.CiRsRelService;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.relatedEvents.service.IRelatedEventsService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import dm.jdbc.util.StringUtil;

/**
 * 命案防控(9+x)
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value="/zhsq/relatedEvents/homicideCase")
public class HomicideCaseController extends ZZBaseController {

	@Autowired
	private IRelatedEventsService relatedEventsService;//涉事案件服务
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;//涉及人员服务
	
	@Autowired
	private CiRsService ciRsService;
	
	@Autowired
	private CiRsRelService ciRsRelService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 跳转命案防控列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toHomicideCaseList")
	public String toHomicideCaseList(HttpSession session, 
			@RequestParam(value = "isOuter", required = false, defaultValue="false") boolean isOuter,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "type", required = false) String type,
			ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
		
		Object defaultInfoOrgCode = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE) ;
		if(isOuter) {
			OrgEntityInfoBO orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
			if(orgEntityInfo != null && orgEntityInfo.getOrgId() != null) {
				defaultInfoOrgCode = orgEntityInfo.getOrgCode();
				
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(defaultInfoOrgCode.toString());
				if(gridInfo != null && gridInfo.getGridId() != null) {
					gridCode = gridInfo.getGridCode();
					gridName=gridInfo.getGridName();
				}
			}
		}
		
		map.addAttribute("infoOrgCode", gridCode);
		map.addAttribute("gridName", gridName);
		map.addAttribute("isOuter", isOuter);
		map.addAttribute("isOuterregionCode", regionCode);
		
		String RS_DOMAIN = funConfigurationService.getAppDomain("$RS_DOMAIN",null,null);
		map.put("RS_DOMAIN", RS_DOMAIN);
		
		String treStyle= funConfigurationService.changeCodeToValue(
				ConstantValue.GRID_CONFIGURATION, ConstantValue.GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL);
		
		map.addAttribute("COMPONENTS_DOMAIN",App.COMPONENTS.getDomain(session));
		map.addAttribute("treStyle", treStyle);
		
		//跳转南昌页面
		if (StringUtils.isNotBlank(type) && type.equals("nanchang")) {
			
			return "/zzgl/homicideCase/list_homicideCase_nc.ftl";
			
		}
		
		return "/zzgl/homicideCase/list_homicideCase.ftl";
	}
	
	/**
	 * 跳转命案防控-嫌疑人/受害人列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toSuspectList")
	public String toSuspectList(HttpSession session, 
			@RequestParam(value = "bizType", required = false) String bizType,
			@RequestParam(value = "bizId", required = false) String bizId,
			@RequestParam(value = "isNotDetail", required = false, defaultValue = "true") String isNotDetail,
			ModelMap map) {
		if(StringUtils.isNotBlank(bizType)) {
			map.addAttribute("bizType", bizType);
		}
		if( StringUtil.isNotEmpty(bizId) ) {
			map.addAttribute("bizId", bizId);
		}
		if(StringUtils.isNotBlank(isNotDetail)) {
			map.addAttribute("isNotDetail", Boolean.valueOf(isNotDetail));
		}
		
		return "/zzgl/homicideCase/list_suspect.ftl";
	}
	
	/**
	 * 跳转新增/编辑命案防控页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddHomicideCase")
	public String toAddHomicideCase(HttpSession session, 
			@RequestParam(value = "reId", required = false) String reId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents relatedEvent = null;
		
		if( StringUtil.isNotEmpty(reId) ) {
			Long id = HashIdManager.decryptLong(reId);
			relatedEvent = relatedEventsService.findRelatedEventsById(id, userInfo.getOrgCode());
			relatedEvent.setReId(0l);
		}
		
		if(relatedEvent == null) {
			relatedEvent = new RelatedEvents();
			
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
			
			relatedEvent.setOccuDateStr(DateUtils.getToday());
			relatedEvent.setGridCode(gridCode);
			relatedEvent.setGridName(gridName);
		}
		
		map.addAttribute("relatedEvent", relatedEvent);
		
		return "/zzgl/homicideCase/add_homicideCase.ftl";
	}
	
	/**
	 * 跳转编辑页面(包含嫌疑人和受害人列表)
	 * @param session
	 * @param reId
	 * @param tabIndex 首先展示的tab页签的索引
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEditHomicideCase")
	public String toEditHomicideCase(HttpSession session, 
			@RequestParam(value = "reId") String reId,
			@RequestParam(value = "tabIndex", required = false, defaultValue="0") String tabIndex,
			ModelMap map) {
		
		map.addAttribute("reId", reId);
		map.addAttribute("tabIndex", tabIndex);
		
		return "/zzgl/homicideCase/edit_homicideCase.ftl";
	}
	
	/**
	 * 跳转新增/编辑命案防控-嫌疑人/受害人页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddSuspect")
	public String toAddSuspect(HttpSession session, 
			@ModelAttribute(value = "involvedPeople") InvolvedPeople involvedPeople,
			ModelMap map) {
		if (involvedPeople != null) {
			involvedPeople.setReOrgCode("-1");
			involvedPeople.setBirthPlace("-1");
		}
		Long ipId = 0l;
		String bizId = involvedPeople.getHashBizId();
		if(StringUtil.isNotEmpty(involvedPeople.getHashId())) {
			ipId = HashIdManager.decryptLong(involvedPeople.getHashId());
		}
		String peopleBizType = "", fieldCfgModuleCode = "";

		if(ipId != null && ipId > 0) {
			involvedPeople = involvedPeopleService.findInvolvedPeopleById(ipId);
			
		}
		
		if(involvedPeople == null) {
			involvedPeople = new InvolvedPeople();
			involvedPeople.setHashBizId(bizId);
			involvedPeople.setReOrgCode("-1");
			involvedPeople.setBirthPlace("-1");
		}
		involvedPeople.setIpId(0l);
		map.put("people", involvedPeople);
		
		map.addAttribute("GEO_DOMAIN", App.GEO.getDomain(session));//地址选择使用
		
		map.addAttribute("infoOrgCode", this.getDefaultInfoOrgCode(session));
		
		peopleBizType = involvedPeople.getBizType();
		if(InvolvedPeople.BIZ_TYPE.HOMICIDE_SUSPECT.toString().equals(peopleBizType)) {
			fieldCfgModuleCode = "homicideCaseSuspect";
		} else if(InvolvedPeople.BIZ_TYPE.HOMICIDE_VITICM.toString().equals(peopleBizType)) {
			fieldCfgModuleCode = "homicideCaseVictim";
		}
		
		map.addAttribute("fieldCfgModuleCode", fieldCfgModuleCode);
		
		return "/zzgl/homicideCase/add_suspect.ftl";
	}
	
	/**
	 * 删除命案防控信息
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delHomicideCase")
	public Map<String, Object> delHomicideCase(HttpSession session, 
			@RequestParam(value = "reId") String reId,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = HashIdManager.decryptLong(reId);
		boolean result = relatedEventsService.deleteRelatedEventsById(id);
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 删除命案防控-嫌疑人/受害人信息
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delSuspect")
	public Map<String, Object> delSuspect(HttpSession session, 
			@RequestParam(value = "ipId") String ipId,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = HashIdManager.decryptLong(ipId);
		boolean result = involvedPeopleService.deleteInvolvedPeopleById(id);
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 跳转命案防控详情页面
	 * @param session
	 * @param reId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetailHomicide")
	public String toDetailHomicide(HttpSession session, 
			@RequestParam(value = "reId") String reId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long id = HashIdManager.decryptLong(reId);
		RelatedEvents relatedEvent = relatedEventsService.findRelatedEventsById(id, userInfo.getOrgCode());
		
		if(relatedEvent == null) {
			relatedEvent = new RelatedEvents();
		}
		relatedEvent.setReId(0l);
		map.addAttribute("relatedEvent", relatedEvent);
		
		return "/zzgl/homicideCase/detail_homicide.ftl";
	}
	
	/**
	 * 跳转命案防控详情页面，包含嫌疑人和受害人列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetailHomicideCase")
	public String toDetailHomicideCase(HttpSession session, 
			@RequestParam(value = "reId") String reId,
			ModelMap map) {
		map.addAttribute("reId", reId);
		
		return "/zzgl/homicideCase/detail_homicideCase.ftl";
	}
	
	/**
	 * 跳转命案防控-嫌疑人/受害人详情页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetailSuspect")
	public String toDetailSuspect(HttpSession session, 
			@RequestParam(value = "ipId") String ipId,
			ModelMap map) {
		Long id = HashIdManager.decryptLong(ipId);
		InvolvedPeople involvedPeople = involvedPeopleService.findInvolvedPeopleById(id);
		
		if(involvedPeople == null) {
			involvedPeople = new InvolvedPeople();
		}
		involvedPeople.setIpId(0l);
		map.addAttribute("people", involvedPeople);
		
		return "/zzgl/homicideCase/detail_suspect.ftl";
	}
	
	/**
	 * 保存命案防控信息
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveHomicideCase")
	public Map<String, Object> saveHomicideCase(HttpSession session, 
			@ModelAttribute(value = "relatedEvent") RelatedEvents relatedEvent,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(relatedEvent.getHashId())) {
			relatedEvent.setReId( HashIdManager.decryptLong(relatedEvent.getHashId()));
		}
		Long reId = relatedEvent.getReId();
		boolean result = false;
		
		if(reId != null && reId > 0) {
			relatedEvent.setUpdateUserId(userInfo.getUserId());
			if(userInfo.getInfoOrgCodeStr()!=null && relatedEvent.getGridCode().startsWith(userInfo.getInfoOrgCodeStr())){
				result = relatedEventsService.updateRelatedEvents(relatedEvent);
			}
		} else {
			relatedEvent.setCreateUserId(userInfo.getUserId());
			reId = relatedEventsService.insertRelatedEvents(relatedEvent);
			result = reId > 0;
			
			if(result) {
				relatedEvent = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
				resultMap.put("relatedEvent", relatedEvent);
			}
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 保存命案防控-嫌疑人/受害人信息
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveSuspect")
	public Map<String, Object> saveSuspect(HttpSession session, 
			@ModelAttribute(value = "involvedPeople") InvolvedPeople involvedPeople,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(involvedPeople.getHashId())) {
			involvedPeople.setIpId( HashIdManager.decryptLong(involvedPeople.getHashId()));
		}
		Long ipId = involvedPeople.getIpId();
		boolean result = false;
		
		try {
			involvedPeople.setBizId(HashIdManager.decryptLong(involvedPeople.getHashBizId()));
			if(ipId != null && ipId > 0) {
				involvedPeople.setUpdateUserId(userInfo.getUserId());
				result = involvedPeopleService.updateInvolvedPeople(involvedPeople);
			} else {
				involvedPeople.setCreateUserId(userInfo.getUserId());
				ipId = involvedPeopleService.insertInvolvedPeople(involvedPeople);
				result = ipId > 0;
			}
		} catch(ZhsqEventException e) {
			resultMap.put("msg", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("msg", e.getMessage());
			e.printStackTrace();
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 加载命案防控列表信息
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listHomicideCaseData", method = RequestMethod.POST)
	public EUDGPagination listHomicideCaseData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@ModelAttribute(value = "relatedEventsForSearch") RelatedEventsForSearch relatedEventsForSearch) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isBlank(relatedEventsForSearch.getGridCode())) {
			relatedEventsForSearch.setGridCode(userInfo.getInfoOrgCodeStr());
		}

		boolean flag = false;
		Long gridId = relatedEventsForSearch.getGridId();
		if (gridId!=null && gridId > 0) {
			MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, gridInfo !=null ?gridInfo.getInfoOrgCode():null);
		}
		String gridCode = relatedEventsForSearch.getGridCode();
		if (StringUtils.isNotBlank(gridCode)) {
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, gridCode);
		}

		if(!flag){
			return new EUDGPagination();
		}

		params.put("relatedEventsForSearch", relatedEventsForSearch);
		params.put("keyWord", keyWord);
		EUDGPagination list = relatedEventsService.findRelatedEventsPageListByCriteria(page, rows, params, userInfo.getInfoOrgCodeStr());
		List<RelatedEvents> listR = (List<RelatedEvents>) list.getRows();
		for (RelatedEvents r : listR) {
			r.setReId(0l);
		}
		return list;
	}
	
	/**
	 * 加载命案防控-嫌疑人/受害人列表信息
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listSuspectData", method = RequestMethod.POST)
	public List<InvolvedPeople> listSuspectData(
			HttpServletRequest request,
			HttpSession session,
			@ModelAttribute(value = "involvedPeople") InvolvedPeople involvedPeople) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long bizId = 0l;
		if(StringUtil.isNotEmpty(involvedPeople.getHashBizId()) ) {
			bizId = HashIdManager.decryptLong(involvedPeople.getHashBizId());
		}
		RelatedEvents re = relatedEventsService.findRelatedEventsById(bizId, userInfo.getInfoOrgCodeStr());
		List<InvolvedPeople> peopleList = new ArrayList<InvolvedPeople>();
		if(re != null) {
			involvedPeople.setBizId(bizId);
			peopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);
			for (InvolvedPeople i : peopleList) {
				i.setIpId(0l);
			}
		}
		return peopleList;
	}
	
	/**
	 * 获取人员信息
	 * @param session
	 * @param identityCard 身份证号
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/fetchRsInfo", method=RequestMethod.POST)
	public Map<String, Object> fetchRsInfo(HttpSession session, 
			@RequestParam(value="identityCard") String identityCard, 
			ModelMap map) {
		Long ciRsId = ciRsService.getCiRsIdByOrgCodeAndIdCard(this.getDefaultInfoOrgCode(session), identityCard);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(ciRsId != null && ciRsId > 0) {
			CiRsTop ciRsTop = ciRsService.findCiRsTopsByCiRsId(ciRsId);
			
			if(ciRsTop != null) {
				resultMap.put("ciRsTop", ciRsTop);
				//isUniqueRel返回true表示无精神病，返回false表示有精神病
				resultMap.put("isMentalDisease", !ciRsRelService.isUniqueRel(ciRsId, "L"));
			}
		}
		
		return resultMap;
	}
}
