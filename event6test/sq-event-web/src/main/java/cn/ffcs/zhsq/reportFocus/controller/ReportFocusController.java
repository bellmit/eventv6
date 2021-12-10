package cn.ffcs.zhsq.reportFocus.controller;

import cn.ffcs.system.publicFilter.CommonController;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JXLExcelUtil;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 重点关注上报
 * @ClassName:   ReportFocusController   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 上午10:10:50
 */
@Controller
@RequestMapping("/zhsq/reportFocus")
public class ReportFocusController extends ZZBaseController {
	@Autowired
	protected IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	/**
	 * 保存/更新重点关注上报信息
	 * @param session
	 * @param attachmentId	附件id，直接使用map接收时，不能将多个附件值合并，会导致附件丢失
	 * @param reportFocus	上报信息
	 * 			reportId	上报id
	 * 			reportUUID	上报UUID
	 * 			reportType	上报类型，1 两违防治；
	 * 			isStart		是否启动流程，true为启动；
	 * @return
	 * 		success		办理结果，true成功；false失败；
	 * 		tipMsg		提示信息
	 * 		reportId	上报id
	 * 		instanceId	流程实例id
	 */
	@ResponseBody
	@RequestMapping(value = "/saveReportFocus")
	public Map<String, Object> saveReportFocus(HttpSession session, 
			@RequestParam(value = "attachmentId", required = false) Long[] attachmentId,
			@RequestParam Map<String, Object> reportFocus) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							params = new HashMap<String, Object>();
		reportFocus = reportFocus == null ? new HashMap<String, Object>() : reportFocus;
		
		if(attachmentId != null && attachmentId.length > 0) {
			reportFocus.put("attachmentId", attachmentId);
		}
		
		params.putAll(reportFocus);
		
		try {
			resultMap = reportIntegrationService.saveReportFocus(reportFocus, userInfo, params);
		} catch(ZhsqEventException e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "result")) {
			result = Boolean.valueOf(resultMap.get("result").toString());
			resultObj = Msg.OPERATE.getResult(result);
		}
		
		resultMap.put("success", resultObj.isSuccess());
		resultMap.put("tipMsg", resultObj.getTipMsg());
		resultMap.putAll(resultMap);
		
		return resultMap;
	}
	
	/**
	 * 删除上报信息
	 * @param session
	 * @param reportUUID	上报uuid
	 * @param params		额外参数
	 * 			reportId	上报id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delReportFocus")
	public ResultObj delReportFocus(HttpSession session, 
			@RequestParam(value = "reportUUID") String reportUUID,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			boolean result = reportIntegrationService.delReportFocusByUUID(reportUUID, userInfo, params) > 0;
			
			resultObj = Msg.DELETE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 加载上报列表记录
	 * @param session
	 * @param page	页码
	 * @param rows	每页记录数
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listReportFocusData")
	public EUDGPagination listReportFocusData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		EUDGPagination reportFocusPagination = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params = params == null ? new HashMap<String, Object>() : params;
		boolean flag = true;
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, new String(decodedData));
		}

		try {
			if(flag){
				params.put("startRegionCode", this.getDefaultInfoOrgCode(session));
				params.put("operateUserId", userInfo.getUserId());
				params.put("operateOrgId", userInfo.getOrgId());
				params.put("userOrgCode", userInfo.getOrgCode());
				reportFocusPagination = reportIntegrationService.findPagination4ReportFocus(page, rows, params);
			}else{
				reportFocusPagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>());
			}
		} catch (Exception e) {
			reportFocusPagination = new EUDGPagination();
			e.printStackTrace();
		}
		
		return reportFocusPagination;
	}
	
	/**
	 * 加载上报列表记录
	 * @param session
	 * @param page	页码
	 * @param rows	每页记录数
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listIntegrationMsgReadingData")
	public EUDGPagination listIntegrationMsgReadingData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		EUDGPagination reportFocusPagination = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params = params == null ? new HashMap<String, Object>() : params;
		boolean flag = true;
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, new String(decodedData));
		}
		try {
			if(flag){
				params.put("startRegionCode", this.getDefaultInfoOrgCode(session));
				params.put("operateUserId", userInfo.getUserId());
				params.put("operateOrgId", userInfo.getOrgId());
				params.put("userOrgCode", userInfo.getOrgCode());
				reportFocusPagination = reportIntegrationService.findPaginationIntegrationMsgReading(page, rows, params);
			}else{
				reportFocusPagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>());
			}
		} catch (Exception e) {
			reportFocusPagination = new EUDGPagination();
			e.printStackTrace();
		}
		
		return reportFocusPagination;
	}
	
	/**
	 * 启动流程
	 * @param session
	 * @param reportId	上报id
	 * @param params	额外参数
	 * 			reportUUID	上报UUID，当reportId为空时，需要传递该参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startWorkflow4ReportFocus")
	public Map<String, Object> startWorkflow4ReportFocus(HttpSession session,
			@RequestParam(value = "reportId", required = false, defaultValue = "-1") Long reportId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultObj resultObj = null;
		
		try {
			boolean result = reportIntegrationService.startWorkflow4Report(reportId, userInfo, params);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		resultMap.putAll(params);
		resultMap.put("success", resultObj.isSuccess());
		resultMap.put("tipMsg", resultObj.getTipMsg());
		
		return resultMap;
	}
	
	/**
	 * 提交流程
	 * @param session
	 * @param instanceId	流程实例id，由于多流程，无法通过业务id获取具体的实例信息
	 * @param nextNodeName	下一办理环节信息
	 * @param params
	 * 			advice			办理意见
	 * 			nextUserIds		下一环节办理人员id，以英文逗号分隔
	 * 			nextOrgIds		下一环节办理人员组织id，以英文逗号分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/subWorkflow4ReportFocus")
	public ResultObj subWorkflow4ReportFocus(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "nextNodeName") String nextNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		try {

			boolean result = reportIntegrationService.subWorkflow4Report(instanceId, nextNodeName, null, userInfo, params);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 驳回上一步
	 * @param session
	 * @param instanceId	流程实例id，由于多流程，无法通过业务id获取具体的实例信息
	 * @param params
	 * 			advice			驳回意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectWorkflow4ReportFocus")
	public ResultObj rejectWorkflow4ReportFocus(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			boolean result = reportIntegrationService.rejectWorkflow4Report(instanceId, userInfo, params);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 流程环节撤回
	 * 要求撤回操作的用户是当前任务的上一任务办理人员，不只是经办人员，且上一任务为task1
	 * 当前环节没有办理过，即没有被提交、驳回、反馈、处理中填写等，只是查看仍可撤回
	 * 
	 * @param session
	 * @param instanceId	流程实例id
	 * @param params		额外参数
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/recallWorkflow4ReportFocus")
	public ResultObj recallWorkflow4ReportFocus(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			boolean result = reportIntegrationService.recallWorkflow4Report(instanceId, userInfo, params);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(false);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 获取处理环节详情
	 * @param session
	 * @param instanceId	实例id
	 * @param params		额外参数
	 * 			reportType	上报类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/flowDetail")
	public String flowDetail(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId, 
			@RequestParam Map<String, Object> params,
			ModelMap map){
		List<Map<String, Object>> queryTaskList = null;
		
		try {
			queryTaskList = reportIntegrationService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.addAttribute("taskList", queryTaskList);
		
		return "/zzgl/event/workflow/detail_workflow.ftl";
	}
	
	/**
	 * 获取短信模板内容
	 * @param session
	 * @param params
	 * 			smsTemplateName	短信模板名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/capSmsContent")
	public Map<String, Object> capSmsContent(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long instanceId = null;
		
		try {
			resultMap.put("smsContent", reportIntegrationService.capSmsContent(params, userInfo));
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance != null) {
				resultMap.put("remindedUserName", proInstance.getCurUser());
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @param session
	 * @param listType
	 * @param reportType
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toIntegrationMsgReadingList")
	public String toIntegrationMsgReadingList(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		List<Map<String, String>> reportTypeMapList = new ArrayList<Map<String, String>>();
		Map<String, String> reportTypeMap = null;
		
		for(ReportTypeEnum reportTypeEnum : ReportTypeEnum.values()) {
			reportTypeMap = new HashMap<String, String>();
			
			reportTypeMap.put("value", reportTypeEnum.getReportTypeIndex());
			reportTypeMap.put("name", reportTypeEnum.getReportTypeName());
			
			reportTypeMapList.add(reportTypeMap);
		}
		
		map.addAttribute("reportTypeDictJson", JsonUtils.listToJson(reportTypeMapList));
		map.addAllAttributes(params);
		
		return "/zzgl/reportFocus/reportFocusExtend/list_reportFocusExtend_msgReading.ftl";
	}
	
	/**
	 * 读取消息
	 * @param session
	 * @param msgId		消息id
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/readMsg")
	public ResultObj readMsg(HttpSession session,
			@RequestParam(value = "msgId") Long msgId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		boolean result = reportIntegrationService.readMsgById(msgId, userInfo);
		
		resultObj = Msg.OPERATE.getResult(result);
		
		return resultObj;
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
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		params = params == null ? new HashMap<String, Object>() : params;
		
		map.addAllAttributes(params);
		map.addAttribute("instanceId", instanceId);
		map.addAttribute("operateType", operateType);
		
		return "/zzgl/reportFocus/base/add_urge_base.ftl";
	}
	
	/**
	 * 添加督办/催办记录
	 * @param session
	 * @param instanceId		流程实例id
	 * @param remarks			督办/催办意见
	 * @param otherMobileNums	接收短信信息手机号码
	 * @param smsContent		短信内容
	 * @param operateType		操作类型
	 * 				0			催办
	 * 				1			督办
	 * @param params			额外参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addUrgeOrRemind")
	public Map<String, Object> addUrgeOrRemind(
			HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "remarks", required = false) String remindRemark,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		StringBuffer msgWrong = new StringBuffer("");
		boolean result = false;
		String category = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		switch(operateType) {
			case 0: {//催办
				category = "1";
				break;
			}
			case 1: {//督办
				category = "2";
				break;
			}
		}
		
		params.put("category", category);
		params.put("remindRemark", remindRemark);
		
		try {
			result = reportIntegrationService.addUrgeOrRemind(instanceId, params, userInfo);
		} catch (Exception e) {
			msgWrong.append(e.getMessage());
			e.printStackTrace();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operateType", operateType);
		resultMap.put("result", result);
		if(msgWrong.length() > 0) {
			resultMap.put("msgWrong", msgWrong.toString());
		}
		
		return resultMap;
	}

	/**
	 * 导出入格相关数据
	 * */
	@RequestMapping(value = "/exportReportFocusData")
	public void exportReportFocusData(HttpSession session, HttpServletResponse response,
									 HttpServletRequest request,
									 @RequestParam Map<String,Object> params){
		List<Map<String,Object>> dataList = null;
		String[] titles = null;
		String[] columns =null;
		Integer[] columnWidths = null;
		String excelName="场景运用事件导出模板",
				reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex(),
				modelName = ReportTypeEnum.epidemicPreControl.getReportTypeName();

		params = params == null ? new HashMap<String, Object>() : params;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean flag = true;

		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			flag = super.checkDataPermission(session, CommonController.TYPE_GRID, new String(decodedData));
		}

		if(CommonFunctions.isNotBlank(params,"reportType")){
			reportType = String.valueOf(params.get("reportType"));
		}
		if(CommonFunctions.isBlank(params,"listType")){
			params.put("listType","5");
		}

		switch (reportType) {
			case "1":
				//两违防治
				modelName = "两违防治";
				break;
			case "2":
				//房屋安全隐患
				modelName = "房屋安全隐患";
				break;
			case "3":
				//企业安全隐患
				modelName = "企业安全隐患";
				break;
			case "4":
				//疫情防控
				modelName = "疫情防控";
				break;
			case "5":
				//流域水质
				modelName = "流域水质";
				break;
			case "6":
				//三会一课
				modelName = "三会一课";
				break;
			case "7":
				//扶贫走访
				modelName = "扶贫走访";
				break;
			case "8":
				//农村建房
				modelName = "农村建房";
				break;
			case "9":
				//森林防灭火
				modelName = "森林防灭火";
				break;
			case "10":
				//营商问题
				modelName = "营商问题";
				break;
			case "11":
				//致贫返贫监测
				modelName = "致贫返贫监测";
				break;
			case "12":
				//信访人员稳控
				modelName = "信访人员稳控";
				break;
			case "13":
				//烈士纪念设施
				modelName = "烈士纪念设施";
				break;
			case "14":
				//环境卫生问题处置
				modelName = "环境卫生问题处置";
				break;
			case "15":
				//三合一整治
				modelName = "三合一整治";
		}

		excelName= modelName + "场景运用事件导出模板";

		//校验通过采取查询数据
		if(flag){
			params.put("startRegionCode", this.getDefaultInfoOrgCode(session));
			params.put("operateUserId", userInfo.getUserId());
			params.put("operateOrgId", userInfo.getOrgId());
			params.put("userOrgCode", userInfo.getOrgCode());
			//进行环节意见的获取
			params.put("isCapTaskAdvice", true);
			try {
				dataList = reportIntegrationService.findList4ReportFocus(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			dataList = new ArrayList<>();
		}


		//疫情防控
		if(ReportTypeEnum.epidemicPreControl.getReportTypeIndex().equals(reportType)){
			titles=new String[] { "序号","报告编号","报告人","报告时间","所属镇","所属村/社区",
					"所属网格", "人员姓名", "来自何国", "何地入南","入南时间","交通方式",
					"居住地址（发生地址）","管控类型", "管控措施", "报告内容（报告人发布内容）","核实情况（第一副网格长(驻村工作队长)核实）",
					"处置情况（第一网格长(驻村领导)处置）","处置情况（乡镇(街道)疫情防控指挥部处置）","报告状态" };
			columns = new String[] { "rowNum","reportCode","reporterName","reportTimeStr", "streetName","communityName",
					"gridName","kpcName", "kpcOrigin","kpcWhere","arriveTime","trafficModeName",
					"occurred","kpcTypeName", "disposalModeName","task1Advice","task3_4Advice",
					"task5Advice", "task6_9_10Advice","reportStatusName",};
			columnWidths = new Integer[] { 5, 15, 15,20, 15, 30,
											15,20,20,20,30,50,
											30,30,30,30,30,
											30,30,30};
		}else if(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex().equals(reportType)){
			//企业安全隐患
			titles=new String[] { "序号","报告编号","报告人","报告时间","所属镇","所属村/社区","所属网格","企业名称",
					"报告内容", "是否存在隐患", "隐患类型", "备注","核实内容（第一副网格长核实内容）","隐患认定情况",
					"处置情况（第一副网格长查处报告内容）","处置情况（乡镇查处报告内容）", "立案查处办理人（立案查处环节报告人）", "处置情况（行政处罚报告内容）"
					,"报告状态"};
			columns = new String[] { "rowNum","reportCode","reporterName","reportTimeStr", "streetName","communityName",
					"gridName","enterpriseName", "task1Advice","isHiddenDangerName","hiddenDangerTypeName","remark",
					"task3Advice","isTrueName","task5Advice", "task6Advice","task9Advice","task10Advice","reportStatusName",};
			columnWidths = new Integer[] { 5, 20, 15,20, 15, 30,
					15,20,30,20,30,50,
					30,30,30,30,30,
					30,30,30};
		}else if(ReportTypeEnum.twoVioPre.getReportTypeIndex().equals(reportType)){
			//两违防治
			titles=new String[] { "序号","报告编号","图斑编号","报告人","报告时间","所属镇","所属村/社区","所属网格","发生地址",
					"业主姓名", "报告内容", "建设状态", "占地面积","建（构）物用途","土地证号","规划许可证号","备注",
					"核实内容（第一副网格长核实内容）","两违认定情况", "网格处置情况（第一网格长处置报告内容）", "镇级处置情况（镇级分管领导处置报告内容）"
					,"市直部门查处情况（部门办结报告内容）","市直部门核验情况（市直业务部门核验反馈报告内容）","市两违办监督检查结果（市两违办报告内容）","报告状态"};
			columns = new String[] { "rowNum","reportCode","mapNum","reporterName","reportTimeStr", "streetName","communityName",
					"gridName","occurred","personInvolved","tipoffContent","constructionStatusName","areaCovered", "buildingUsageName","lecCode","ppnCode","remark",
					"task3Advice","verifyStatusName","task6Advice", "task8Advice","task10Advice","task11Advice","task7Advice","reportStatusName"};
			columnWidths = new Integer[] { 5, 20, 15,20, 15, 30,
					15,20,30,20,30,30,
					30,30,30,30,30,
					30,30,30,30,30,30,30,20};
		}

		JXLExcelUtil t= new JXLExcelUtil<Map<String, Object>>();
		Map map = new HashMap();
		map.put("list", dataList);
		map.put("columns", columns);
		map.put("titles", titles);
		map.put("widths", columnWidths);
		map.put("name", excelName);

		t.buildExcelDocument(map, request, response);

	}
}
