package cn.ffcs.zhsq.szzg.event.controller;

import cn.ffcs.common.GdZTreeNode;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.jiangYinPlatform.service.IJiangYinPlatformService;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceType;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IEventDisposal4JsonpService;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceTypeService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.util.*;


/**
 * 事件Jsonp接口
 * @author os.wuzhj
 *
 */

@Controller
@RequestMapping("/zhsq/szzg/eventJsonpController")
public class EventDisposal4JsonpController extends ZZBaseController{
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private IJiangYinPlatformService jiangYinPlatformService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	
	@Autowired
	private ZgResourceTypeService zgResourceTypeService;
	
	@Autowired
	private IZhouBianStatService zhouBianStatService;
	
	@Autowired
    private IBaseDictionaryService dictionaryService;
	
	@Autowired
	private ZgResourceInfoService zgResourceInfoService;
	
	@Autowired
	private IEventDisposal4JsonpService eventDisposal4JsonpService;

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 *  热点事件
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchHotEventData4Jsonp")
	public String fetchHotEventData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		 List<Map<String, Object>> resultMap = jiangYinPlatformService.findHotEventData(params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 获取采集趋势的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchAcquisitionTrendData4Jsonp")
	public String fetchAcquisitionTrendData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		 List<Map<String, Object>> resultMap = jiangYinPlatformService.findAcquisitionTrendData(params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 获取累计采集，累计办结，满意率的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchCumulativeData4Jsonp")
	public String fetchCumulativeData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		Map<String, Object> resultMap = jiangYinPlatformService.findCumulativeData(params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 区域排名TOP5
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchAreaRankTop5Data4Jsonp")
	public String fetchAreaRankTop5Data4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		List<Map<String, Object>> resultMap = jiangYinPlatformService.findAreaRankTop5Data(params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 左侧事件类型数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventTypeData4Jsonp")
	public String fetchEventTypeData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String,Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String own = "1"; //默认查本级
		if(CommonFunctions.isNotBlank(params, "own")) {
			own = params.get("own").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			String infoOrgCode = params.get("infoOrgCode").toString();
			OrgEntityInfoBO selectOrgEntityInfoByOrgCode = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(infoOrgCode);
			params.put("orgId",selectOrgEntityInfoByOrgCode.getOrgId());
			params.put("orgCode",selectOrgEntityInfoByOrgCode.getOrgCode());
			params.put("infoOrgCode", infoOrgCode);
		}else {
			params.put("orgCode", userInfo.getOrgCode());
			params.put("orgId",userInfo.getUserId());
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
			
		}
		
		String createTimeStart = params.get("createTimeStart").toString();
		String createTimeEnd = params.get("createTimeEnd").toString();
		params.put("userId", userInfo.getUserId());
		params.put("listType", "5");
		params.put("own", own);
		params.put("createTimeStart", createTimeStart.replace("-", ""));
		params.put("createTimeEnd", createTimeEnd.replace("-", ""));
		
		List<Map<String, Object>> findUrgencyNum_zd = null;
		List<Map<String, Object>> findUrgencyNum_jj = null;
		List<Map<String, Object>> findUrgencyNum_yb = null;
		List<Map<String, Object>> findOverseeNum_db = null;
		
		List<Map<String, Object>> nextData = null;
		//  已经评价过
 		int evaluationTotal=0;
		try {
				
			if("1".equals(own)) {
				if(CommonFunctions.isNotBlank(params, "type")) {
					String type = params.get("type").toString();
					if("db".equals(type)) {
						nextData = jiangYinPlatformService.findOverseeNum(params);
					}else {
						String[] urgencyDegreeArr = params.get("urgencyDegrees").toString().split(",");
						params.put("urgencyDegreeArray", urgencyDegreeArr);
						nextData = jiangYinPlatformService.findUrgencyNum(params);
					}
				}
			}else {
				String[] urgencyDegreeArray = new String[] {"04"};
				//重大
				params.put("urgencyDegreeArray", urgencyDegreeArray);
				findUrgencyNum_zd = jiangYinPlatformService.findUrgencyNum(params);
				//紧急
				urgencyDegreeArray = new String[] {"02","03"};
				params.put("urgencyDegreeArray", urgencyDegreeArray);
				findUrgencyNum_jj = jiangYinPlatformService.findUrgencyNum(params);
				//一般
				urgencyDegreeArray = new String[] {"01"};
				params.put("urgencyDegreeArray", urgencyDegreeArray);
				findUrgencyNum_yb = jiangYinPlatformService.findUrgencyNum(params);
				//督办
				params.remove("urgencyDegreeArray");
				findOverseeNum_db = jiangYinPlatformService.findOverseeNum(params);
				
				String[] statusList = new String[] {"04"};
				List<String> evaLevelList = new ArrayList<>(); 
				evaLevelList.add("01");
				evaLevelList.add("02");
				evaLevelList.add("03");
				params.put("listType", "4");
				params.put("createTimeStart", createTimeStart);
				params.put("createTimeEnd", createTimeEnd);
				params.put("statusList", statusList);
				params.put("evaLevelList", evaLevelList);
				params.put("isUseTSjEvaResult", true);
				
				evaluationTotal = event4WorkflowService.findEventCount(params);
			}
			
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		resultMap.put("zd",findUrgencyNum_zd);
		resultMap.put("jj",findUrgencyNum_jj);
		resultMap.put("yb",findUrgencyNum_yb);
		resultMap.put("db",findOverseeNum_db);
		resultMap.put("evaluationTotal", evaluationTotal);
		
		
		resultMap.put("nextData",nextData);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	// 紧急事件，督办事件 list
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/fetchEventList4Jsonp")
	public String fetchEventList4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			String infoOrgCode = params.get("infoOrgCode").toString();
			OrgEntityInfoBO selectOrgEntityInfoByOrgCode = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(infoOrgCode);
			params.put("orgId",selectOrgEntityInfoByOrgCode.getOrgId());
			params.put("orgCode",selectOrgEntityInfoByOrgCode.getOrgCode());
			params.put("infoOrgCode", infoOrgCode);
		}else {
			params.put("orgCode", userInfo.getOrgCode());
			params.put("orgId",userInfo.getUserId());
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
			
		}
		params.put("userId", userInfo.getUserId());
		params.put("listType", "5");
		params.put("status", "00,01,02,03,04");
		
		String type=params.get("clickType").toString();
		
		//督办事件
		if("db".equals(type)) {
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				params.remove("urgencyDegree");
			}
			params.put("superviseMark", '1');
		}
		
		
		cn.ffcs.system.publicUtil.EUDGPagination findEventListPagination = null;
		List<Map<String, Object>> findEventXY = null;
		try {
			findEventListPagination = event4WorkflowService.findEventListPagination(1, 100000, params);
			
			String eventId = "";
			for (int i = 0,l = findEventListPagination.getTotal();i<l;i++) {
				Map<String,Object>  mapE = (Map<String,Object>) findEventListPagination.getRows().get(i);
				eventId += mapE.get("eventId") + ",";
			}
			eventId = eventId.substring(0, eventId.length()-1);
			params.put("eventIdArr", eventId.split(","));
			findEventXY = jiangYinPlatformService.findEventXY(params);
			
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(findEventXY) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(findEventXY);
		}
		
		return jsonpcallback;
	}
	
	//两违事件撒点
	@ResponseBody
	@RequestMapping(value = "/fetchTwoViolationEventList4Jsonp")
	public String fetchTwoViolationEventList4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			String infoOrgCode = params.get("infoOrgCode").toString();
			OrgEntityInfoBO selectOrgEntityInfoByOrgCode = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(infoOrgCode);
			params.put("orgId",selectOrgEntityInfoByOrgCode.getOrgId());
			params.put("orgCode",selectOrgEntityInfoByOrgCode.getOrgCode());
			params.put("infoOrgCode", infoOrgCode);
		}else {
			params.put("orgCode", userInfo.getOrgCode());
			params.put("orgId",userInfo.getUserId());
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
			
		}
		params.put("userId", userInfo.getUserId());
		params.put("listType", "5");
		params.put("status", "00,01,02,03,04");
		
		
		cn.ffcs.system.publicUtil.EUDGPagination findEventListPagination = null;
		List<Map<String, Object>> findEventXY = null;
		try {
			findEventListPagination = event4WorkflowService.findEventListPagination(1, 100000, params);
			
			String eventId = "";
			for (int i = 0,l = findEventListPagination.getTotal();i<l;i++) {
				Map<String,Object>  mapE = (Map<String,Object>) findEventListPagination.getRows().get(i);
				eventId += mapE.get("eventId") + ",";
			}
			eventId = eventId.substring(0, eventId.length()-1);
			params.put("eventIdArr", eventId.split(","));
			findEventXY = jiangYinPlatformService.findEventXY(params);
			
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(findEventXY) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(findEventXY);
		}
		
		return jsonpcallback;
	}
	
	/**
     *	根据区域编码返回子区域数据 
     *	jsonpcallback	回调方法
     *	gridId			网格id
     *	infoOrgCode		网格编码
     *	createTimeStart		开始时间
     *	createTimeEnd		结束时间
     *	parentType	事件类型父编码
     */
    @ResponseBody
    @RequestMapping("/getEventNumGroupByType")
    public String getEventNumGroupByType(@RequestParam Map<String, Object> params) {
    	String jsonpcallback = "";
    	if(params.get("jsonpcallback") != null) {
    		jsonpcallback = params.get("jsonpcallback").toString();
    	}
        return jsonpcallback + "(" + JsonHelper.getJsonString(jiangYinPlatformService.getEventNumGroupByType(params)) + ")";
    }
	
	
	/**
	 * 今日数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchGetToDayData4Jsonp")
	public String fetchGetToDayData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String,Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			String infoOrgCode = String.valueOf(params.get("infoOrgCode"));
			OrgEntityInfoBO selectOrgEntityInfoByOrgCode = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(infoOrgCode);
			params.put("orgId",selectOrgEntityInfoByOrgCode.getOrgId());
			params.put("orgCode",selectOrgEntityInfoByOrgCode.getOrgCode());
			params.put("infoOrgCode", infoOrgCode);
		}else {
			params.put("orgCode", userInfo.getOrgCode());
			params.put("orgId",userInfo.getUserId());
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		params.put("userId", userInfo.getUserId());
		
		String today = DateUtils.getToday();
		if(CommonFunctions.isNotBlank(params, "today")) {
			today = params.get("today").toString();
		}
		//办结          新增    满意      已经评价          今日处置                                                         
		int bjTotal = 0,total=0,myTotal=0,evaluationTotal=0,dealTotal=0;
		try {
			
			//今日办结
			Map<String,Object> params1=new HashMap<String,Object>();
			params1.putAll(params);
			params1.put("status", "03,04");
			params1.put("listType", "5");
			params1.put("updateTimeStart", today);
			params1.put("updateTimeEnd", today);
			bjTotal = event4WorkflowService.findEventCount(params1);
			
			//今日处置
			Map<String,Object> params2=new HashMap<String,Object>();
			params2.putAll(params);
			params2.put("status", "03,04");
			params2.put("listType", "5");
			params2.put("createTimeStart", today);
			params2.put("createTimeEnd", today);
			dealTotal = event4WorkflowService.findEventCount(params2);
			
			//今日新增
			Map<String,Object> params3=new HashMap<String,Object>();
			params3.putAll(params);
			params3.put("createTimeStart", today);
			params3.put("createTimeEnd", today);
			params3.put("status", "00,01,02,03,04");
			params3.put("listType", "5");
			total = event4WorkflowService.findEventCount(params3);
			
			//查询满意的事件数
			Map<String,Object> params4=new HashMap<String,Object>();
			params4.putAll(params);
			List<String> evaLevelList = new ArrayList<>(); 
			evaLevelList.add("02");
			evaLevelList.add("03");
			params4.put("listType", "4");
			params4.put("statusList", "04");
			params4.put("evaLevelList", "02,03");
			params4.put("isUseTSjEvaResult", true);
			params4.put("createTimeStart", today);
			params4.put("createTimeEnd", today);
			myTotal = event4WorkflowService.findEventCount(params4);
			
			//查询已评价的事件数
			Map<String,Object> params5=new HashMap<String,Object>();
			params5.putAll(params);
			params5.put("listType", "4");
			params5.put("statusList", "04");
			params5.put("evaLevelList", evaLevelList);
			params5.put("isUseTSjEvaResult", true);
			params5.put("createTimeStart", today);
			params5.put("createTimeEnd", today);
			evaLevelList.add("01");
			params5.put("evaLevelList", "01,02,03");
			evaluationTotal = event4WorkflowService.findEventCount(params5);
			
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
	
		resultMap.put("bjTotal", bjTotal);
		resultMap.put("total", total);
		resultMap.put("myTotal", myTotal);
		resultMap.put("evaluationTotal", evaluationTotal);
		resultMap.put("dealTotal", dealTotal);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
	/**
	 *  资源树
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findTypeTree4Jsonp")
	public String findTypeTree4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		List<ZgResourceType> findTree = zgResourceTypeService.findTree(null);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(findTree) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(findTree);
		}
		
		return jsonpcallback;
	}
	
	/**
	 *   网格员或者视频探头
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findGriderOrGlobalEys4Jsonp")
	public String findGriderOrGlobalEys4Jsonp(HttpSession session,
			@RequestParam int pageNo,
			@RequestParam int pageSize,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		}
		
		EUDGPagination statOfZhouBianList = zhouBianStatService.statOfZhouBianList(pageNo, pageSize, params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(statOfZhouBianList) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(statOfZhouBianList);
		}
		
		return jsonpcallback;
	}

	/**
	 *   部件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findbjData4Jsonp")
	public String findbjData4Jsonp(HttpSession session,
			@RequestParam(required = false, value = "dictCode") String dictCode,
            @RequestParam(required = false, value = "dictPcode") String dictPcode,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        List<BaseDataDict> baseDataDictList = null;
        boolean isOpen = false;
        if (StringUtils.isBlank(dictPcode)) {
            isOpen = true;
            baseDataDictList = this.dictionaryService.getDataDictListByDictCode(dictCode);
        } else {
            baseDataDictList = this.dictionaryService.getDataDictListOfSinglestage(dictPcode, userInfo.getOrgCode());
        }
        List<GdZTreeNode> treeNodes = new ArrayList<GdZTreeNode>();
        for (BaseDataDict baseDataDict : baseDataDictList) {
            GdZTreeNode node = new GdZTreeNode();
            node.setOpen(isOpen);
            node.setIsParent("1".equals(baseDataDict.getIsLeaf()) ? false : true);
            node.setId(String.valueOf(baseDataDict.getDictId()));
            node.setPId(String.valueOf(baseDataDict.getDictPid()));
            node.setName(baseDataDict.getDictName());
            node.setDictGeneralCode(baseDataDict.getDictGeneralCode());
            node.setDictCode(baseDataDict.getDictCode());
            treeNodes.add(node);
        }
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(treeNodes) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(treeNodes);
		}
		
		return jsonpcallback;
	}
	
	
	/**
	 *   周边资源   应急场地，救援机构，救援车辆
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findZYData4Jsonp")
	public String findZYData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("res", zgResourceTypeService.findMenuDetailUrl(params));
		resultMap.put("list", zgResourceInfoService.findByParam(params));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/**
	 *   部件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getComponent4Jsonp")
	public String getComponent4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", zgResourceInfoService.findComponentByParam(params));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	
	
	
	/**
	 *   事件总量相关统计查询
	 *   参数
	 *   事件采集时间(起)：createTimeStart
	 *   事件采集时间(终)：createTimeEnd
	 *   事件更新时间(起)：updateTimeStart
	 *   事件更新时间(终)：updateTimeEnd
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeTotalByCondition")
	public String getEventTypeTotalByCondition(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		Integer pageNo=1;
		if(CommonFunctions.isNotBlank(params, "pageNo")) {
			pageNo=Integer.valueOf(params.get("pageNo").toString());
		}
		Integer pageSize=100;
		if(CommonFunctions.isNotBlank(params, "pageSize")) {
			pageSize=Integer.valueOf(params.get("pageSize").toString());
		}
		resultMap.put("list", eventDisposal4JsonpService.getEventTotalTypeByCondition(params, pageNo, pageSize));
		//获取事件总数
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("userId", userInfo.getUserId());
		searchParams.put("orgId", userInfo.getOrgId());
		searchParams.put("orgCode", userInfo.getOrgCode());
		searchParams.put("infoOrgCode", params.get("orgCode").toString());
		searchParams.put("listType", 5);
		searchParams.put("createTimeStart", params.get("createTimeStart").toString());
		searchParams.put("createTimeEnd", params.get("createTimeEnd").toString());
		
		try {
			resultMap.put("total", event4WorkflowService.findEventCount(searchParams));
		} catch (ZhsqEventException e) {
			resultMap.put("total", 0);
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 *   事件总量相关统计查询
	 *   参数
	 *   事件采集时间(起)：createTimeStart
	 *   事件采集时间(终)：createTimeEnd
	 *   事件更新时间(起)：updateTimeStart
	 *   事件更新时间(终)：updateTimeEnd
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventSourceTotalByCondition")
	public String getEventSourceTotalByCondition(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		Integer pageNo=1, pageSize=100;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "pageNo")) {
			pageNo=Integer.valueOf(params.get("pageNo").toString());
		}
		if(CommonFunctions.isNotBlank(params, "pageSize")) {
			pageSize=Integer.valueOf(params.get("pageSize").toString());
		}
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
			params.put("userOrgCode", userInfo.getOrgCode());
		}
		
		try {
			resultMap.put("list", eventDisposal4JsonpService.getEventTotalSourceByCondition(params, pageNo, pageSize));
		} catch(Exception e) {
			resultMap.put("msgWrong", e.getMessage());
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
	
	/**
	 *   地图事件列表查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapEventList")
	public String getMapEventList(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		resultMap.put("result", eventDisposal4JsonpService.getMapEventList(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 *   地图事件总数查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapEventListCount")
	public String getMapEventListCount(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		resultMap.put("result", eventDisposal4JsonpService.getMapEventListCount(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 *   地图部件列表查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapComponentList")
	public String getMapComponentList(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		resultMap.put("result", eventDisposal4JsonpService.getMapComponentList(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 *   地图部件总数查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapComponentListCount")
	public String getMapComponentListCount(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		resultMap.put("result", eventDisposal4JsonpService.getMapComponentListCount(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/********************************数据清洗相关jsonp接口整合start*******************************/
	/**
	 * 事件总量相关数据清洗查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTotalByStatistics")
	public String getEventTotalByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		resultMap.put("result", eventDisposal4JsonpService.getEventTotalByStatistics(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件区域分布清洗表查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventAreaDistributionByStatistics")
	public String getEventAreaDistributionByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = null;
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
	
		resultMap.put("result", eventDisposal4JsonpService.getEventAreaDistributionByStatistics(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/********************************数据清洗相关jsonp接口整合end*******************************/
	
	/**
	 * 事件区域分布实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventAreaDistributionByActual")
	public String getEventAreaDistributionByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
	
		resultMap.put("result", eventDisposal4JsonpService.getEventAreaDistributionByActual(params, userInfo));
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件类型占比实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeProportionByActual")
	public String getEventTypeProportionByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("total", 0);
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		if(CommonFunctions.isNotBlank(params, "typeLevel")) {
			String str = params.get("typeLevel").toString();
			if(str.equals("2")) {
				params.put("typeBit", 4);
			}else if(str.equals("3")) {
				params.put("typeBit", 6);
			}
		}else {
			params.put("typeBit", 2);
		}
		
		//获取事件总数
		try {
			params.put("listType", "5");
			resultMap.put("total", event4WorkflowService.findEventCount(params));
			resultMap.put("data", eventDisposal4JsonpService.getEventTypeProportionByActual(params, userInfo));
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 * 事件类型占比实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeProportionByStatistics")
	public String getEventTypeProportionByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("total", 0);
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		if(CommonFunctions.isNotBlank(params, "typeLevel")) {
			String str = params.get("typeLevel").toString();
			if(str.equals("2")) {
				params.put("typeBit", 4);
			}else if(str.equals("3")) {
				params.put("typeBit", 6);
			}
		}else {
			params.put("typeBit", 2);
		}
		
		//获取事件总数
		try {
			params.put("listType", "5");
			resultMap.put("total", eventDisposal4JsonpService.getEventTotalByStatistics(params, userInfo));
			resultMap.put("data", eventDisposal4JsonpService.getEventTypeProportionByStatistics(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	
	
	/**
	 * 事件趋势清洗查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTrendByStatistics")
	public String getEventTrendByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo =null;
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			userInfo =  (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);;
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		/*
		 * if(CommonFunctions.isBlank(params, "userId")) { params.put("userId",
		 * userInfo.getUserId()); } if(CommonFunctions.isBlank(params, "orgId")) {
		 * params.put("orgId", userInfo.getOrgId()); }
		 * if(CommonFunctions.isBlank(params, "orgCode")) { params.put("orgCode",
		 * userInfo.getOrgCode()); }
		 */
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getEventTrendByStatistics(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件趋势实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTrendByActual")
	public String getEventTrendByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getEventTrendByActual(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

	/**
	 * 重庆万州领导对事件时限进行调整jsonp接口
	 * @param session
	 * @param params
	 * 		dateType 传日期类型 : 1，当前日期新增，必须传addDayFromNow，整数
	 * 	                     2，自定义时限，必须传handleDate，日期时间字符串
	 * 	                     0或不传，原有期限新增，必须传addDayFromOld，整数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateEventHandleInfoJsonp")
	public String updateEventHandleInfoJsonp(HttpSession session,
													 @RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		boolean updateResult = userInfo != null && userInfo.getUserId() != null ;
		Long eventId = null;
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		updateResult = updateResult && eventId != null && eventId > 0;
		Date handleDate = null;
		Date oldHandleDate = null;
		if(updateResult) { //处理办结期限
			EventDisposal oldEvent = eventDisposalService.findEventByIdSimple(eventId);
			if (oldEvent != null) {
				oldHandleDate = oldEvent.getHandleDate();
			}
			String dateType = "0";
			if(CommonFunctions.isNotBlank(params, "dateType")) {
				dateType = params.get("dateType").toString();
			}
			switch (dateType) {
				case "1": //当前日期新增
					if (CommonFunctions.isNotBlank(params, "addDayFromNow")) {
						Integer addDayFromNow = null;
						try {
							addDayFromNow = Integer.valueOf(params.get("addDayFromNow").toString());
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						if (addDayFromNow != null) {
							handleDate = DateUtils.addInterval(new Date(), addDayFromNow, "00");
						}
					}
					break;
				case "2": //自定义时限
					if (CommonFunctions.isNotBlank(params, "handleDate")) {
						try {
							handleDate = DateUtils.convertStringToDate(params.get("handleDate").toString(), "yyyy-MM-dd HH:mm:ss");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					break;
				default: //原有期限新增
					if (CommonFunctions.isNotBlank(params, "addDayFromOld")) {
						Integer addDayFromOld = null;
						try {
							addDayFromOld = Integer.valueOf(params.get("addDayFromOld").toString());
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						if (addDayFromOld != null) {
							handleDate = DateUtils.addInterval(oldHandleDate, addDayFromOld, "00");
						}
					}
					break;
			}
		}
		updateResult = handleDate!= null;
		String resultMsg = "事件办理时限调整失败";
		if(updateResult && oldHandleDate != null && handleDate.compareTo(oldHandleDate) == 0){
			resultMsg = "事件办理时限与原办结时限相同，不做调整";
			updateResult = false;
		}
		if(updateResult) {
			EventDisposal event = new EventDisposal();
			event.setEventId(eventId);
			event.setHandleDate(handleDate);
			event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL); //时限状态先设置正常，再调用存储过程修改时限状态
			event.setUpdatorId(userInfo.getUserId());
			updateResult = eventDisposalService.updateEventDisposalById(event) > 0L;
			if(updateResult){
				final String storedProc = "{call PROC_UPDATE_HDF_BY_ID(?,?)}";
				final Long tmpEventId = eventId;
				String returnVal = "0";
				try {
					JdbcTemplate jdbcTemplateOracle = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplateOracle");
					returnVal = jdbcTemplateOracle.execute(
						conn -> {
							CallableStatement cs = conn.prepareCall(storedProc);
							cs.setLong(1, tmpEventId);
							cs.registerOutParameter(2, java.sql.Types.VARCHAR);
							return cs;
						}, (CallableStatementCallback<String>) cs -> {
							cs.execute();
							return cs.getString(2);
						});
				} catch (Exception e) {
					e.printStackTrace();
					returnVal = e.getMessage();
				}
				System.out.println("returnVal = [" + returnVal + "]");
				switch (returnVal){
					case ConstantValue.LIMIT_TIME_STATUS_NORMAL:
						resultMsg="事件办理时限调整成功，时限状态为正常";
						break;
					case ConstantValue.LIMIT_TIME_STATUS_TO_EXPIRE:
						resultMsg="事件办理时限调整成功，时限状态为即将超时";
						break;
					case ConstantValue.LIMIT_TIME_STATUS_EXPIRED:
						resultMsg="事件办理时限调整成功，时限状态为超时";
						break;
					default: break;
				}
			}
		}
		resultMap.put("tipMsg",resultMsg);
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

	/**
	 *   周边资源   应急场地，救援机构，救援车辆，有返回resTypeName
	 *   name 查询功能  非必填
	 *   typeCodes  ","分隔
	 *   orgCode
	 *   x,y,distance
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findzyListByParamJsonp")
	public String findzyListByParamJsonp(HttpSession session,
										 @RequestParam Map<String, Object> params,int page, int rows) {
		String jsonpcallback = params.get("jsonpcallback").toString();
		JSONObject json =new JSONObject();
		json.put("list", zgResourceInfoService.findListByParam(page,rows,params));
		json.put("params", params);
		return jsonpcallback+ "(" + json.toString() + ")";
	}

	/**
	 * 跳转督办/催办操作页面
	 * @param session
	 * @param instanceId	流程实例id
	 * @param operateType	操作类型
	 * 				0		催办
	 * 				1		督办
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddUrgeOrRemind")
	public String toAddUrgeOrRemind(HttpSession session, 
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "backUrl", required = false) String backUrl,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		params = params == null ? new HashMap<String, Object>() : params;
		
		map.addAllAttributes(params);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("operateType", operateType);
		if(StringUtils.isNotBlank(page)) {
			map.addAttribute("color",color);
			if (StringUtils.isBlank(backUrl)) {
				String biDomain = App.BI.getDomain(session);
				backUrl = biDomain + "/report/nanan/leader/eventProgress.jhtml";
			}
			map.addAttribute("backUrl",backUrl);
			return "/zzgl/event/workflow/basic/add_urge_base"+page+".ftl";
		}
		return "/zzgl/event/workflow/basic/add_urge_base.ftl";
	}
	
	/**
	 * 获取职位相关事件统计信息
	 * @param session
	 * @param params
	 * 			statMonthStr	统计月份，格式为：yyyy-MM
	 * 			infoOrgCode		地域编码
	 * 			positionName	职位名称，使用英文逗号分隔
	 * @return
	 * 		
	 */
	@ResponseBody
	@RequestMapping(value = "/findPositionEventByStatistics")
	public String findPositionEventByStatistics(HttpSession session, @RequestParam Map<String, Object> params) {
		String jsonpcallback = null;
		JSONObject json = new JSONObject();
		
		try {
			json.put("data", eventDisposal4JsonpService.findPositionEventByStatistics(params));
		} catch (Exception e) {
			json.put("msgWrong", e.getMessage());
			
			if(logger.isErrorEnabled()) {
				logger.error("获取职位相关统计失败！", e);
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString() + "(" + json.toString() + ")";
		} else {
			jsonpcallback = json.toString();
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 获取用户相关事件统计信息
	 * @param session
	 * @param params
	 * 			statMonthStr	统计月份，格式为：yyyy-MM
	 * 			infoOrgCode		地域编码
	 * 			statisticsUserId用户id
	 * 			statisticsOrgId	组织id
	 * @return
	 * 		
	 */
	@ResponseBody
	@RequestMapping(value = "/findUserEventByStatistics")
	public String findUserEventByStatistics(HttpSession session, @RequestParam Map<String, Object> params) {
		String jsonpcallback = null;
		JSONObject json = new JSONObject();
		
		try {
			json.put("data", eventDisposal4JsonpService.findUserEventByStatistics(params));
		} catch (Exception e) {
			json.put("msgWrong", e.getMessage());
			
			if(logger.isErrorEnabled()) {
				logger.error("获取用户相关统计失败！", e);
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString() + "(" + json.toString() + ")";
		} else {
			jsonpcallback = json.toString();
		}
		
		return jsonpcallback;
	}
}
