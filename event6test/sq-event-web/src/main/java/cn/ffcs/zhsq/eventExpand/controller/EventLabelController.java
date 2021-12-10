package cn.ffcs.zhsq.eventExpand.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventRelaItem;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PointInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapBuidingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapSegmentGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.resident.service.IResidentService;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEventRelaItemService;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPointInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.base.local.service.IGridLogService.ActionType;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposal4ExtraService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventDuplication.service.IEventDuplicationService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.ArrayUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JXLExcelUtil;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/zhsq/event/eventLabelController")
public class EventLabelController extends ZZBaseController {
	
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	protected IEventLabelService eventLabelService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	

	/**
	 * 跳转列表页面
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListPage")
	public String toListPage(HttpSession session,
			@RequestParam Map<String, Object> params,ModelMap map) {
		map.putAll(params);
		return "/zzgl/eventExpand/eventLabel/list_event_label.ftl";
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetailPage")
	public String toDetailPage(HttpSession session,
			@RequestParam Map<String, Object> params,ModelMap map) {
		map.putAll(params);
		if(CommonFunctions.isNotBlank(params, "labelId")) {
			map.put("eventLabel", eventLabelService.searchById(Long.valueOf(params.get("labelId").toString())));
		}
		return "/zzgl/eventExpand/eventLabel/detail_event_label.ftl";
	}
	/**
	 * 跳转新增页面
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddPage")
	public String toAddPage(HttpSession session,
			@RequestParam Map<String, Object> params,ModelMap map) {
		map.putAll(params);
		if(CommonFunctions.isNotBlank(params, "labelId")) {
			Map<String, Object> searchById = eventLabelService.searchById(Long.valueOf(params.get("labelId").toString()));
			map.put("eventLabel", searchById);
			if(CommonFunctions.isNotBlank(searchById, "regionCode")) {
				List<MixedGridInfo> findGridInfoByInfoOrgCode = mixedGridInfoService.findGridInfoByInfoOrgCode(searchById.get("regionCode").toString());
				if(findGridInfoByInfoOrgCode!=null&&findGridInfoByInfoOrgCode.size()>0) {
					map.put("startGridId", findGridInfoByInfoOrgCode.get(0).getGridId());
					map.put("gridName", findGridInfoByInfoOrgCode.get(0).getGridName());
				}
			}
		}
		return "/zzgl/eventExpand/eventLabel/add_event_label.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/saveLabel", method = RequestMethod.POST)
	public Map<String, Object> saveEvent(HttpSession session,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		if(CommonFunctions.isBlank(params, "regionCode")) {
			params.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		Integer saveOrUpdate = eventLabelService.saveOrUpdate(params, userInfo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("labelId", saveOrUpdate);
		resultMap.put("result", saveOrUpdate > 0?"success":"fail");
		resultMap.put("type", "save");
		
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delLabel", method = RequestMethod.POST)
	public Map<String, Object> delLabel(HttpSession session,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Integer saveOrUpdate = eventLabelService.deleteEventLabel(params, userInfo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("labelId", saveOrUpdate);
		resultMap.put("result", saveOrUpdate > 0?"success":"fail");
		resultMap.put("type", "delete");
		
		return resultMap;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam Map<String, Object> paramMap) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(paramMap);
		
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		
		
		EUDGPagination paginationTarget = new EUDGPagination();
		
		paginationTarget.setTotal(0);
		paginationTarget.setRows(new ArrayList<Map<String, Object>>());
		
		try {
			paginationTarget = eventLabelService.searchList(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return paginationTarget;
	}
	
	
	
}
