package cn.ffcs.zhsq.relatedEvents.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.relatedEvents.service.ICommonRelatedEventsService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 涉及校园安全案
 * @author YangCQ
 *
 */
@Controller
@RequestMapping("/zhsq/relatedEvents/SchoolRelatedEventsController")
public class SchoolRelatedEventsController extends ZZBaseController {
	
	@Autowired
	private ICommonRelatedEventsService relatedEventsService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	private String schoolBizType = "2";
	
	/**
	 * 跳转到新增页面或者编辑页面
	 * @param session
	 * @param involvedId 涉事案件主键，不为空时，跳转到编辑页面
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddRelatedEvents")
	public String toAddRelatedEvents(HttpSession session, 
			@RequestParam(value = "reId", required = false) Long reId,
			@RequestParam(value = "bizType", required=false) String bizType,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		bizType = StringUtils.isNotBlank(bizType) ? bizType : schoolBizType;
		RelatedEvents relatedEvents = null;
		if (reId != null && !reId.equals(-1)) {
			relatedEvents = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		} else {
			relatedEvents = new RelatedEvents();
			relatedEvents.setOccuDateStr(DateUtils.getNow());
			relatedEvents.setBizType(bizType);
		}
		
		map.addAttribute("relatedEvents", relatedEvents);
		
		map.addAttribute("prisonersDocTypePcode", ConstantValue.PRISONERS_DOC_TYPE);
		map.addAttribute("naturePcode", ConstantValue.NATURE);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", ConstantValue.MAP_TYPE_SAFE_EVENT);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/zzgl/schoolRelatedEvents/add_schoolRelatedEvents.ftl";
	}
	
	/**
	 * 编辑涉事案件信息
	 * @param session
	 * @param relatedEvents 涉事案件对象
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> editRelatedEvents(HttpSession session,
			@ModelAttribute(value = "relatedEvents") RelatedEvents relatedEvents, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		relatedEvents.setUpdateUserId(userInfo.getUserId());

		boolean result = relatedEventsService.updateRelatedEvents(relatedEvents);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 保存涉事案件信息
	 * @param session
	 * @param relatedEvents 涉事案件对象
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> saveRelatedEvents(HttpSession session,
			@ModelAttribute(value = "relatedEvents") RelatedEvents relatedEvents, ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		relatedEvents.setCreateUserId(userInfo.getUserId());
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		Long result = relatedEventsService.insertRelatedEvents(relatedEvents, infoOrgCode);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result > 0);
		return resultMap;
	}
	
	/**
	 * 删除涉事案件信息
	 * @param session
	 * @param request
	 * @param idStr 涉事案件主键串，以英文逗号分隔
	 * @return 返回受影响的行数，存放在map中，键值为result
	 */
	@ResponseBody
	@RequestMapping(value = "/delRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> delRelatedEvents(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "idStr") String idStr) {
		String[] ids = idStr.split(",");
		List<Long> involvedIdList = new ArrayList<Long>();
		
		for(int index = 0, length = ids.length; index < length; index++){
			long involvedId = Long.parseLong(ids[index]);
			involvedIdList.add(involvedId);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = relatedEventsService.deleteRelatedEventsByIds(involvedIdList);
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param reId 涉事案件主键
	 * @param map result为true表示查询成功；relatedEvents 返回的对象
	 * @return
	 */
	@RequestMapping(value = "/detailRelatedEvents")
	public String detailRelatedEvents(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "reId") Long reId,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents relatedEvents = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		boolean result = false;//用于判断详情是否获取成功
		
		if(relatedEvents != null){
			result = true;
		}else{
			relatedEvents = new RelatedEvents();
		}
		
		map.addAttribute("result", result);
		map.addAttribute("relatedEvents", relatedEvents);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", ConstantValue.MAP_TYPE_SAFE_EVENT);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		String showType = request.getParameter("showType");
		map.addAttribute("showType", showType);
		
		return "/zzgl/schoolRelatedEvents/detail_schoolRelatedEvents.ftl";
	}
	
	/**
	 * 跳转至列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listRelatedEvents")
	public String listRelatedEvents(HttpSession session, HttpServletRequest request,
			@ModelAttribute(value = "relatedEventsForSearch") RelatedEventsForSearch relatedEventsForSearch,
			ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		MixedGridInfo gridInfo = null;
		Object gridIdObj = defaultGridInfo.get(KEY_START_GRID_ID),
			   infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE),
		       gridNameObj = defaultGridInfo.get(KEY_START_GRID_NAME);
		
		Long gridId = relatedEventsForSearch.getGridId();
		String gridCode = relatedEventsForSearch.getGridCode(),
			   bizType = relatedEventsForSearch.getBizType(),
			   nature = relatedEventsForSearch.getNature(),
			   occuDateStartStr = relatedEventsForSearch.getOccuDateStartStr(),
			   occuDateEndStr = relatedEventsForSearch.getOccuDateEndStr();
		
		if (gridId != null && gridId > 0) {
			gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		} else if(StringUtils.isNotBlank(gridCode)) {
			gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
		}
		
		if(gridInfo != null) {
			map.addAttribute("isOuter", true);
			gridIdObj = gridInfo.getGridId();
			infoOrgCode = gridInfo.getInfoOrgCode();
		}
		
		map.addAttribute("gridId", gridIdObj);
		map.addAttribute("gridCode", infoOrgCode);
		map.addAttribute("gridName", gridNameObj);
		
		if (StringUtils.isNotBlank(nature)) {
			map.addAttribute("nature", nature);
		}
		if(StringUtils.isNotBlank(occuDateStartStr)) {
			map.addAttribute("occuDateStartStr", occuDateStartStr);
		}
		if(StringUtils.isNotBlank(occuDateEndStr)) {
			map.addAttribute("occuDateEndStr", occuDateEndStr);
		}
		
		bizType = StringUtils.isNotBlank(bizType) ? bizType : schoolBizType;
		map.addAttribute("bizType", bizType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		String treStyle= funConfigurationService.changeCodeToValue(
				ConstantValue.GRID_CONFIGURATION, ConstantValue.GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL,
				infoOrgCode.toString());
		map.addAttribute("treStyle",treStyle);
		
		return "/zzgl/schoolRelatedEvents/list_schoolRelatedEvents.ftl";
	}
	
	/**
	 * 跳转至列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listRelatedEventsForMap")
	public String listRelatedEventsForMap(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "id", required=false) Long gridId,
			@RequestParam(value = "gridCode", required=false) String gridCode,
			@RequestParam(value = "bizType", required=false) String bizType,
			ModelMap map) {
		if (StringUtils.isBlank(gridCode)) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		}
		map.addAttribute("gridCode", gridCode);
		map.addAttribute("gridId", gridId);
		String nature = request.getParameter("nature");
		if (StringUtils.isNotBlank(nature)) {
			map.addAttribute("nature", nature);
		}
		bizType = StringUtils.isNotBlank(bizType) ? bizType : schoolBizType;
		map.addAttribute("bizType", bizType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/zzgl/schoolRelatedEvents/list_schoolRelatedEvents.ftl";
	}
	
	/**
	 * 列表数据加载
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @param keyWord 关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listRelatedEventsData", method = RequestMethod.POST)
	public EUDGPagination listRelatedEventsData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@ModelAttribute(value = "relatedEventsForSearch") RelatedEventsForSearch relatedEventsForSearch,
			@RequestParam(value = "keyWord", required = false) String keyWord){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		
		Map<String, Object> params = new HashMap<String, Object>();
		String gridCode = relatedEventsForSearch.getGridCode();
		Long gridId = relatedEventsForSearch.getGridId();
		
		if(StringUtils.isBlank(gridCode) && (gridId==null || gridId<0)){
			relatedEventsForSearch.setGridCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		}
		params.put("relatedEventsForSearch", relatedEventsForSearch);
		params.put("keyWord", keyWord);
		return relatedEventsService.findRelatedEventsPagination(page, rows, params, userInfo.getOrgCode());
	}
}
