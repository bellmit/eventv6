package cn.ffcs.zhsq.map.arcgis.controller;
/**
 * 事件调度模块 controller
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.data.GisMapInfo;
import cn.ffcs.shequ.zzgl.service.event.IEventDisposalService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfEvent;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;


@Controller
@RequestMapping(value = "/zhsq/map/arcgis/dataOfEventScheduling")
public class ArcgisDataOfEventSchedulingController extends ZZBaseController{

	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	
	@RequestMapping(value = "/standardPending")
	public String standardPending(HttpSession session, ModelMap map,
			@RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "standard", required=false) String standard,@RequestParam(value = "statusName") String statusName) {
		String forward = "/zzgl_map_data/statistics/standardPending.ftl";
		List<Map<String, Object>> bigTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_EVENT_DISPOSAL, ConstantValue.COLUMN_EVENT_DISPOSAL_BIG_TYPE, "");
		map.addAttribute("bigTypeDC", bigTypeDC);
		map.addAttribute("gridId", gridId);
		map.addAttribute("statusName",statusName);
		if("standard".equals(standard)) {
			forward = "/map/arcgis/standardmappage/standardPending.ftl";
		}
		return forward;
	}
	
	/**
	 * 指挥调度图层中的代办上报，代办分流,将到期，已过期的调用等的方法
	 * @param session
	 * @param page
	 * @param gridId
	 * @param rows
	 * @param statusName 事件状态： 
	 * @param isExpire 是否紧急 ：1 一般，，2紧急
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pendingData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination pendingData(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "statusName",required=false) String statusName,
			@RequestParam(value = "startTime",required=false) String startTime,
			@RequestParam(value = "endTime",required=false) String endTime,
			@RequestParam(value = "startHappenTime",required=false) String startHappenTime,
			@RequestParam(value = "endHappenTime",required=false) String endHappenTime,
			@RequestParam(value = "content",required=false) String content,
			@RequestParam(value = "isExpire",required=false) String isExpire) {
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		String status = ConstantValue.EVENT_STATUS_DISTRIBUTE;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
//		params.put("gridId", gridId);
		if(statusName.equals("report")){//图层代办上报标记
			/*内平台采集事件加上上报的限制条件
			 */
			status = ConstantValue.EVENT_STATUS_REPORT;
			params.put("status", status);
			statusName = "innerPlatform";//最终的查询都是以内平台采集数据为标准的
			if(isExpire!=null&&!"".equals(isExpire)){
				params.put("urgencyDegree", isExpire.equals("1")?"01":"02");
			}
		}else if(statusName.equals("shunt")){//图层代办分流标记
			/*内平台采集事件加上分流的限制条件
			 */
			status = ConstantValue.EVENT_STATUS_DISTRIBUTE;
			params.put("status", status);
			statusName = "innerPlatform";//最终的查询都是以内平台采集数据为标准的
			if(isExpire!=null&&!"".equals(isExpire)){
				params.put("urgencyDegree", isExpire.equals("1")?"01":"02");
			}
		}else if(statusName.equals("expire")){//图层已过期
			/*内平台采集事件加过期的限制条件
			 */
			List<String> statusList=new ArrayList<String>();
			statusList.add(ConstantValue.EVENT_STATUS_REPORT );
			statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			params.put("handletimeStatus", "02");
			params.put("statusList", statusList);
			statusName = "innerPlatform";//最终的查询都是以内平台采集数据为标准的
			if(isExpire!=null&&!"".equals(isExpire)){
				params.put("urgencyDegree", isExpire.equals("1")?"01":"02");
			}
		}else if(statusName.equals("notExpire")){//图层将到期
			List<String> statusList=new ArrayList<String>();
			statusList.add(ConstantValue.EVENT_STATUS_REPORT );
			statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			params.put("handletimeStatus", "01");
			params.put("statusList", statusList);
			statusName = "innerPlatform";//最终的查询都是以内平台采集数据为标准的
			if(isExpire!=null&&!"".equals(isExpire)){
				params.put("urgencyDegree", isExpire.equals("1")?"01":"02");
			}
		}else if(statusName.equals("ending")){//图层已过期
			List<String> statusList=new ArrayList<String>();
			statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
			params.put("statusList", statusList);
			statusName = "endingEvent";
			if(!"".equals(startTime)){
				params.put("startTime", startTime);
			}
			if(!"".equals(endTime)){
				params.put("endTime", endTime);
			}
		}else{
			params.put("gridId", gridId);
		}
		params.put("orgCode", userInfo.getOrgCode());//图层的也要可以查到一级分流上报的事件（有机构的标记没有分配到人员的）
		params.put("statusName", statusName);
		if(startHappenTime!=null&&!"".equals(startHappenTime)){
			params.put("startHappenTime", startHappenTime);
		}
		if(endHappenTime!=null&&!"".equals(endHappenTime)){
			params.put("endHappenTime", endHappenTime);
		}
		if(content!=null&&!"".equals(content)){
			params.put("content", content);
		}
		cn.ffcs.common.EUDGPagination edugPagination = eventDisposalService.findEventDisposalPagination(page, rows, params);
		return edugPagination;
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
	@RequestMapping(value = "/getArcgisLocateDataListOfPending")
	public List<ArcgisInfoOfEvent> getArcgisPendingLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt", required=false) Integer mapt) {
		String eventIds = "";
		String[] results = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (String result : results) {
			if (StringUtils.isBlank(result)) continue;
			String eventId = result.split("_")[0];
			idList.add(Long.parseLong(eventId));
		}
		List<GisMapInfo> mapDataList = null;
		if (idList.size() > 0) mapDataList = this.eventDisposalService.findMapDataForEventListByIds(idList, String.valueOf(mapt));
		List<ArcgisInfoOfEvent> eventlist = new ArrayList<ArcgisInfoOfEvent>();
		if (mapDataList != null && mapDataList.size() > 0) {
			Long tid = null;
			for(GisMapInfo gisMapInfo:mapDataList){
				for(int i=0; i<results.length; i++) {
					if(results[i].contains((gisMapInfo.getId()+"_"))) {
						try {
							String tidStr = results[i].split("_")[1];
							if(tidStr!=null && tidStr.length()>0) {tid = Long.parseLong(tidStr);}
						} catch (Exception e) {}
					}
				}
				gisMapInfo.setTid(tid);
			}
			for(GisMapInfo gisMapInfo : mapDataList){
				ArcgisInfoOfEvent arcgisInfoOfEvent = new ArcgisInfoOfEvent();
				arcgisInfoOfEvent.setWid(String.valueOf(gisMapInfo.getWid()));
				arcgisInfoOfEvent.setX(gisMapInfo.getX().doubleValue());
				arcgisInfoOfEvent.setY(gisMapInfo.getY().doubleValue());
				arcgisInfoOfEvent.setMapt(gisMapInfo.getMapt());
				arcgisInfoOfEvent.setGridName(gisMapInfo.getName());
				arcgisInfoOfEvent.setName(gisMapInfo.getName());
				arcgisInfoOfEvent.setTaskId(gisMapInfo.getTid());
				eventlist.add(arcgisInfoOfEvent);
			}
		}
		return eventlist;
	}
}
