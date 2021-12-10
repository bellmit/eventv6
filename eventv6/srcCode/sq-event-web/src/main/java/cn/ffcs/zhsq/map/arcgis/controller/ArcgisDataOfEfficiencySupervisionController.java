package cn.ffcs.zhsq.map.arcgis.controller;
/**
 * 党建工作-在办事件和历史事件
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventDisposal;
import cn.ffcs.shequ.zzgl.service.event.IEventDisposalService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfEvent;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;


@Controller
@RequestMapping(value = "/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController")
public class ArcgisDataOfEfficiencySupervisionController extends ZZBaseController{

	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	

	/**
	 * 在办事件列表
	 * 
	 * @param session
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/workingEvent")
	public String workingEvent(HttpSession session,
			@RequestParam(value = "gridId") Long gridId, ModelMap map
			,@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.put("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/efficiencySupervision/standardWorkingEvent.ftl";
	}

	/**
	 * 历史事件列表
	 * 
	 * @param session
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/historicalEvent")
	public String historicalEvent(HttpSession session,
			@RequestParam(value = "gridId") Long gridId, ModelMap map
			,@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.put("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/efficiencySupervision/standardHistoricalEvent.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/workingEventListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination workingEventListData(HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "gridId", required = false) Long gridId) {
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;
		// --在办事件
		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_REPORT);
		statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
		params.put("gridId", gridId);
		params.put("type", "0105");// 效能投诉
		params.put("statusList", statusList);
		params.put("statusName", "innerPlatform");// 内平台事件
		// params.put("statusName", "workingEvent");
		params.put("orgCode", userInfo.getOrgCode());
		cn.ffcs.common.EUDGPagination pagination = eventDisposalService.findEventDisposalPagination(page, rows, params);
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("userId", userInfo.getUserId());
		params2.put("gridId", gridId);
		params2.put("type", "0105");// 效能投诉
		params2.put("statusList", statusList);
		params.put("statusName", "shunted");
		params2.put("orgCode", userInfo.getOrgCode());
		cn.ffcs.common.EUDGPagination pagination2 = eventDisposalService.findEventDisposalPagination(page, rows, params);
		List<EventDisposal> evList = (List<EventDisposal>) pagination.getRows();
		List<EventDisposal> evList2 = (List<EventDisposal>) pagination2
				.getRows();
		evList.addAll(evList2);
		pagination.setRows(evList);
		return pagination;
	}

	@ResponseBody
	@RequestMapping(value = "/historicalEventListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination historicalEventListData(HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "gridId") Long gridId) {
		if (page <= 0)
			page = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("type", "0105");// 效能投诉
		List<String> statusList = new ArrayList<String>();
		// statusList.add("01");
		// statusList.add("02");
		// statusList.add("03");
		statusList.add("04");
		// statusList.add("05");
		// statusList.add("06");
		params.put("statusList", statusList);
		cn.ffcs.common.EUDGPagination pagination = eventDisposalService.findEventDisposalPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 获取事件的定位信息
	 * @param session
	 * @param eventIds 人员ids
	 * @param mapt 地图类型
	 * @param type 人员类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfEvent")
	public List<ArcgisInfoOfEvent> getArcgisEventLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String eventIds = "";
		String[] results = ids.split(",");
		for(String result : results){
			String eventId = result.split("!")[0];
			eventIds = eventIds + "," + eventId;
		}
		eventIds = eventIds.substring(1, eventIds.length());
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisEventDisposalLocateDataListByIds(eventIds,mapt);
		List<ArcgisInfoOfEvent> eventlist = new ArrayList<ArcgisInfoOfEvent>();
		for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
			ArcgisInfoOfEvent arcgisInfoOfEvent = new ArcgisInfoOfEvent();
			arcgisInfoOfEvent.setWid(String.valueOf(arcgisInfoOfPublic.getWid()));
			arcgisInfoOfEvent.setX(arcgisInfoOfPublic.getX());
			arcgisInfoOfEvent.setY(arcgisInfoOfPublic.getY());
			arcgisInfoOfEvent.setMapt(arcgisInfoOfPublic.getMapt());
			arcgisInfoOfEvent.setGridName(arcgisInfoOfPublic.getName());
			arcgisInfoOfEvent.setName(arcgisInfoOfPublic.getName());
			for(String result : results){
				if(arcgisInfoOfEvent.getWid().toString().equals(result.split("!")[0])){
					arcgisInfoOfEvent.setWorkFlowId(Long.parseLong(result.split("!")[1]));
					arcgisInfoOfEvent.setInstanceId(Long.parseLong(result.split("!")[2]));
				}
			}
			arcgisInfoOfEvent.setElementsCollectionStr(elementsCollectionStr);
			eventlist.add(arcgisInfoOfEvent);
		}
		
		return eventlist;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisImportantEventLocateDataList")
	public List<ArcgisInfoOfEvent> getArcgisImportantEventLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt", required=false) Integer mapt) {
		List<Long> idList = new ArrayList<Long>();
		String[] idss = ids.split(",");
		try {
			//循环获取事件详细信息并放入列表中
			for(int i=0; i<idss.length; i++) {
				Long eventId = Long.parseLong(idss[i].split("_")[0]);
				idList.add(eventId);
			}
		} catch (Exception e) {/*e.printStackTrace();*/}
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisImportantEventDisposalLocateDataListByIds(idList.toString().substring(1, idList.toString().lastIndexOf(']')),mapt);
		List<ArcgisInfoOfEvent> eventlist = new ArrayList<ArcgisInfoOfEvent>();
		
		Long tid = null;
		
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			for(int i=0; i<idss.length; i++) {
				if(idss[i].contains((arcgisInfoOfPublic.getId()+"_"))) {
					try {
						String tidStr = idss[i].split("_")[1];
						if(tidStr!=null && tidStr.length()>0) {
							tid = Long.parseLong(tidStr);
						}
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			}
			
			ArcgisInfoOfEvent arcgisInfoOfEvent = new ArcgisInfoOfEvent();
			arcgisInfoOfEvent.setWid(String.valueOf(arcgisInfoOfPublic.getWid()));
			arcgisInfoOfEvent.setX(arcgisInfoOfPublic.getX());
			arcgisInfoOfEvent.setY(arcgisInfoOfPublic.getY());
			arcgisInfoOfEvent.setMapt(arcgisInfoOfPublic.getMapt());
			arcgisInfoOfEvent.setGridName(arcgisInfoOfPublic.getName());
			arcgisInfoOfEvent.setName(arcgisInfoOfPublic.getName());
			arcgisInfoOfEvent.setTaskId(tid);
			eventlist.add(arcgisInfoOfEvent);
		}
		
		return eventlist;
	}
	
}
