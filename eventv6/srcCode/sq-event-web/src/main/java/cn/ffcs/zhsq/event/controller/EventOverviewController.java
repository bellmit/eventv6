package cn.ffcs.zhsq.event.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.WindowHelper;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 事件绩效总览
 * 
 * @author YangCQ
 * 
 */
@Controller
@RequestMapping("/zhsq/event/eventOverviewController")
public class EventOverviewController extends EventDisposalController {
	
	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;

	@RequestMapping(value = "/indexA")
	public String indexA(HttpSession session, ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = WindowHelper.getPmStr(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		Map<String, Object> dates = DateUtils.getFirstday_Lastday_Month(new Date(), false);
		map.addAttribute("startTime", dates.get("first"));
		map.addAttribute("endTime", dates.get("last"));
		map.addAttribute("bigTypes", getBigTypes(userInfo));
		map.addAttribute("isShowSatisfyRate", CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode()) ? "1" : "0");
		StringBuffer sqlSB = new StringBuffer(1024 * 4);
		sqlSB.append("<tr> \n");
		sqlSB.append("	<td>&nbsp;</td> \n");
		sqlSB.append("	<th>采集量</th> \n");
		sqlSB.append("	<th>办结量</th> \n");
		sqlSB.append("	<th>完成率(%)</th> \n");
		if (CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode())) {// 江西南昌县
			sqlSB.append("	<th>满意率(%)</th> \n");
		}
		List<BaseDataDict> bigTypeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
		if (bigTypeList.size() > 1) {
			sqlSB.append("	<td>&nbsp;</td> \n");
			sqlSB.append("	<th>采集量</th> \n");
			sqlSB.append("	<th>办结量</th> \n");
			sqlSB.append("	<th>完成率(%)</th> \n");
			if (CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode())) {// 江西南昌县
				sqlSB.append("	<th>满意率(%)</th> \n");
			}
		}
		sqlSB.append("</tr> \n");
		for (int i = 0; i < bigTypeList.size(); i+=2) {
			BaseDataDict dataDict = bigTypeList.get(i);
			sqlSB.append("<tr> \n");
			sqlSB.append("	<th>" + dataDict.getDictName() + "</th> \n");
			sqlSB.append("	<td> \n");
			sqlSB.append("		<label id='cj_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "' class='light'>0</label> \n");
			sqlSB.append("	</td> \n");
			sqlSB.append("	<td> \n");
			sqlSB.append("		<label id='bj_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "' class='light'>0</label> \n");
			sqlSB.append("	</td> \n");
			sqlSB.append("	<td> \n");
			sqlSB.append("		<label id='wcl_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "'>0.00%</label> \n");
			sqlSB.append("	</td> \n");
			if (CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode())) {// 江西南昌县
				sqlSB.append("	<td> \n");
				sqlSB.append("		<label id='myl_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "'>0.00%</label> \n");
				sqlSB.append("	</td> \n");
			}
			if (bigTypeList.size() > 1) {
				if (i + 1 <= bigTypeList.size() - 1) {
					dataDict = bigTypeList.get(i + 1);
					sqlSB.append("	<th>" + dataDict.getDictName() + "</th> \n");
					sqlSB.append("	<td> \n");
					sqlSB.append("		<label id='cj_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "' class='light'>0</label> \n");
					sqlSB.append("	</td> \n");
					sqlSB.append("	<td> \n");
					sqlSB.append("		<label id='bj_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "' class='light'>0</label> \n");
					sqlSB.append("	</td> \n");
					sqlSB.append("	<td> \n");
					sqlSB.append("		<label id='wcl_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "'>0.00%</label> \n");
					sqlSB.append("	</td> \n");
					if (CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode())) {// 江西南昌县
						sqlSB.append("	<td> \n");
						sqlSB.append("		<label id='myl_" + dataDict.getDictGeneralCode() + "_" + dataDict.getDictCode() + "'>0.00%</label> \n");
						sqlSB.append("	</td> \n");
					}
				} else {
					sqlSB.append("	<th>&nbsp;</th> \n");
					sqlSB.append("	<td>&nbsp;</td> \n");
					sqlSB.append("	<td>&nbsp;</td> \n");
					sqlSB.append("	<td>&nbsp;</td> \n");
					if (CommonFunctions.isShowSatisfyRate(userInfo.getOrgCode())) {// 江西南昌县
						sqlSB.append("	<td>&nbsp;</td> \n");
					}
				}
			}
			sqlSB.append("</tr> \n");
		}
		map.addAttribute("bigTypeTBody", sqlSB.toString());
		return "/zzgl/assessment/overview_index.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listDataA")
	public Map<String, Object> listDataA(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "gridId") String gridId,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "bigTypes", required = false) String bigTypes) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("infoOrgCode", infoOrgCode);
		if (StringUtils.isNotBlank(startTime)) {
			params.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			params.put("endTime", endTime);
		}
		if (StringUtils.isNotBlank(bigTypes)) {
			params.put("type", bigTypes);
		}
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		//params.put("bigTypes", WindowHelper.addQuotes(getBigTypes(userInfo)));
		return eventDisposalService.findEventCountA(params);
	}
	
	@RequestMapping(value = "/getDoublePaperForGridEventBi")
	public String getDoublePaperForGridEventBi(HttpSession session, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime){
		map.put("infoOrgCode", infoOrgCode); 
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return "/zzgl/gridEventBi/doublepaper_gridevent_bi.ftl";
	}
	
	@RequestMapping(value = "/listEventA")
	public String listEventA(HttpSession session, ModelMap map,
			@RequestParam(value = "gridId",required = false) String gridId,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "isNeedType", required = false) String isNeedType,
			@RequestParam(value = "eventStatus", required = false) String eventStatus,
			@RequestParam(value = "remindStatus", required = false) String remindStatus,
			@RequestParam(value = "dictFilterData", required = false) String dictFilterData,
			@RequestParam(value = "urgencyDegrees", required = false) String urgencyDegrees,
			@RequestParam(value = "handleDateFlag", required = false) String handleDateFlag)throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		super.listEvent(session,"all", "l", null, null, null,map);
		if(gridId != null){
			MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(gridId), false);
			map.put("gridId", gridId);
			map.put("gridName", gridInfo.getGridName());
		}
		
		if (StringUtils.isNotBlank(startTime)) {
			map.put("createTimeStart", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("createTimeEnd", endTime);
		}
		if (StringUtils.isNotBlank(handleDateFlag)) {
			map.put("handleDateFlag", handleDateFlag);
		}
		map.put("infoOrgCode", infoOrgCode);
		if (!"false".equals(isNeedType)) {
			if (StringUtils.isNotBlank(type)) {
				String[] types = type.split("_");
				if (types.length > 2) {
					map.put("type", types[1]);
					if (StringUtils.isNotBlank(dictFilterData)) {
						map.put("pCode", "{'" + types[2] + "':["+WindowHelper.addQuotes(dictFilterData)+"]}");
					} else {
						map.put("pCode", "{ 'A001093199' : [ '"+types[2]+"' ] }");
					}
				}else{
					map.put("type", type);
				}
			} else {
				map.put("type", getBigTypes(userInfo));
			}
		}
		map.put("eventStatus", eventStatus);
		map.put("remindStatus", remindStatus);
		map.put("urgencyDegrees", urgencyDegrees);
		return "/zzgl/assessment/overview_list.ftl";
	}
	
	private String getBigTypes(UserInfo userInfo) {
		StringBuffer sb = new StringBuffer();
		List<BaseDataDict> bigTypeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
		for (BaseDataDict baseDataDict : bigTypeList) {
			if (baseDataDict.getDictGeneralCode() != null) {
				if (sb.length() > 0) sb.append(",");
				sb.append(baseDataDict.getDictGeneralCode().trim());
			}
		}
		return sb.toString();
	}
	
	@RequestMapping(value = "/stat_newevent/index")
	public String statIndex(
			HttpSession session,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "gridId", required = false) String gridId,
			@RequestParam(value = "queryKey", required = false) String queryKey,
			ModelMap map) {
		map.put("userId", userId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("gridId", gridId);
		map.put("infoOrgCode", infoOrgCode);
		map.put("queryKey", queryKey);
		return "/zzgl/assessment/overview_stat_list.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/stat_newevent/listData", method = RequestMethod.POST)
	public EUDGPagination statNewEventListData(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "gridId", required = false) String gridId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "queryKey", required = false) String queryKey) throws ParseException {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String startTime = (String) request.getParameter("startTime");
		String endTime = (String) request.getParameter("endTime");
		String infoOrgCode = (String) request.getParameter("infoOrgCode");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("queryKey", queryKey);
		params.put("gridId", gridId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orgCode", userInfo.getOrgCode());
		params.put("infoOrgCode", infoOrgCode);
		EUDGPagination pagination = eventDisposalService.findEventDataForCommon(page, rows, params);
		return pagination;
	}
	
}
