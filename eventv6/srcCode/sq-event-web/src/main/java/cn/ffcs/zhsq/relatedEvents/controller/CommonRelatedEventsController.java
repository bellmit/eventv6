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
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.relatedEvents.service.ICommonRelatedEventsService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 通用涉事安全案
 * @author YangCQ
 *
 */
@Controller
@RequestMapping("/zhsq/relatedEvents/CommonRelatedEventsController")
public class CommonRelatedEventsController extends ZZBaseController {
	
	@Autowired
	private ICommonRelatedEventsService relatedEventsService;
	
	private String commonBizType = "9";// 通用类型
	
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
		bizType = StringUtils.isNotBlank(bizType) ? bizType : commonBizType;
		RelatedEvents relatedEvents = null;
		if (reId != null && !reId.equals(-1)) {
			relatedEvents = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		} else {
			relatedEvents = new RelatedEvents();
			relatedEvents.setOccuDateStr(DateUtils.getNow());
			relatedEvents.setBizType(bizType);
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			relatedEvents.setGridCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
			relatedEvents.setGridName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
		}
		
		map.addAttribute("relatedEvents", relatedEvents);
		
		map.addAttribute("prisonersDocTypePcode", ConstantValue.PRISONERS_DOC_TYPE);
		map.addAttribute("naturePcode", ConstantValue.NATURE);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/zzgl/commonRelatedEvents/add_commonRelatedEvents.ftl";
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
	public String detailRelatedEvents(HttpSession session, 
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
		return "/zzgl/commonRelatedEvents/detail_commonRelatedEvents.ftl";
	}
	
	/**
	 * 跳转至列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listRelatedEvents")
	public String listRelatedEvents(HttpSession session, 
			@RequestParam(value = "gridCode", required=false) String gridCode,
			@RequestParam(value = "bizType", required=false) String bizType,
			ModelMap map) {
		if (StringUtils.isBlank(gridCode)) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		}
		map.addAttribute("gridCode", gridCode);
		bizType = StringUtils.isNotBlank(bizType) ? bizType : commonBizType;
		map.addAttribute("bizType", bizType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/zzgl/commonRelatedEvents/list_commonRelatedEvents.ftl";
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
