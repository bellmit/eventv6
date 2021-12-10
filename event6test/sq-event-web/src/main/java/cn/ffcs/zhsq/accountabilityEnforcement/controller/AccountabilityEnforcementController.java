package cn.ffcs.zhsq.accountabilityEnforcement.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.accountabilityEnforcement.service.IAccountabilityEnforcementService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2018/3/10.
 */
@Controller("accountabilityEnforcement")
@RequestMapping("/zhsq/accountabilityEnforcement")
public class AccountabilityEnforcementController extends ZZBaseController{

	@Autowired
	private IAccountabilityEnforcementService accountabilityEnforcementService;

	private String FORM_TYPE = "accountEnforcement",//表单类型
				   ATTACHMENT_TYPE = "accountabilityEnforcement";//附件类型
	
	/**
	 * 跳转列表页面
	 * @param session
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toList")
	public Object tolist(HttpSession session, 
			@RequestParam(value = "listType", required=false, defaultValue="1") int listType,
			ModelMap map){
		map.addAttribute("listType", listType);
		
		return "zzgl/accountabilityEnforcement/list_accountEnforce.ftl";
	}

	/**
	 * 获取列表记录
	 * @param session
	 * @param page		页码
	 * @param rows		每页记录数
	 * @param params
	 * 			listType	列表类型
	 * 				1 草稿
	 * 				2 待办
	 * 				3 经办
	 * 				4 我发起的
	 * 				5 辖区所有
	 * 			regionCode	地域编码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listData",method = RequestMethod.POST)
	public EUDGPagination listData(HttpSession session,
						    @RequestParam(value = "page") int page,
						    @RequestParam(value = "rows") int rows,
						    @RequestParam Map<String, Object> params){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		
		EUDGPagination accountEn = accountabilityEnforcementService.findAccountEnPagination(page, rows, params, userInfo);
		
		return accountEn;
	}

	//新增执纪问责信息
	@RequestMapping("/toAdd")
	public Object addAcc(HttpSession session, ModelMap map,Long probId){
		//获取当前登录用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> record = null;

		if(probId != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userOrgCode", userInfo.getOrgCode());
			
			record = accountabilityEnforcementService.findByProbId(probId,params);
		}
		
		if(record == null || record.isEmpty()) {
			//获取默地域信息
			Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
			
			map.addAttribute("regionCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
			map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		}
		
		map.addAttribute("record", record);
		map.addAttribute("attachmentType",ATTACHMENT_TYPE);

		map.addAttribute("currentDate",new Date());
		
		return "zzgl/accountabilityEnforcement/add_accountabilityEnforcement.ftl";
	}

	/**
	 * 新增/编辑问题信息
	 * @param session
	 * @param probId	问题id
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveProb")
	public Map<String, Object> saveProb(HttpSession session, 
			Long probId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(probId != null && probId > 0) {//更新信息
			try {
				result = accountabilityEnforcementService.updateByProbId(probId, params, userInfo);
				
				resultObj = Msg.EDIT.getResult(result);
			} catch (Exception e) {
				e.printStackTrace();
				resultObj = Msg.EDIT.getResult(e);
			}
		} else {//新增信息
			try {
				probId = accountabilityEnforcementService.insertProb(params, userInfo);
				
				result = probId > 0;
				
				resultObj = Msg.ADD.getResult(result);
			} catch (Exception e) {
				e.printStackTrace();
				resultObj = Msg.ADD.getResult(e);
			}
		}
		
		resultMap.put("result", result);
		resultMap.put("tipMsg", resultObj.getTipMsg());
		resultMap.put("probId", probId);
		
		return resultMap;
	}

	//根据问题id删除问题
	@ResponseBody
	@RequestMapping("/delProb")
	public Object delProb(HttpSession session,Long probId){
		//获取当前登录用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;

		if(probId != null){
			result = accountabilityEnforcementService.deleteByProbId(probId,userInfo.getUserId());
		}
		
		resultObj = Msg.DELETE.getResult(result);
		
		return resultObj;
	}

	/**
	 * 跳转详情页面
	 * @param session
	 * @param probId	问题id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping("/toDetail")
	public String toDetail(HttpSession session,
			Long probId,
			@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
			ModelMap map) {
		Map<String, Object> probMap = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		if(probId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("userOrgCode", userInfo.getOrgCode());

			probMap = accountabilityEnforcementService.findByProbId(probId, params);
		}

		if(probMap == null) {
			probMap = new HashMap<String, Object>();
		}

		try {
			this.capWorkflowRelativeData(probId, listType, userInfo, map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("probMap", probMap);
		map.addAttribute("attachmentType", ATTACHMENT_TYPE);
		map.addAttribute("listType", listType);

		return "zzgl/accountabilityEnforcement/detail_accountEnforce.ftl";
	}

	/**
	 * 启动案件流程
	 * @param session
	 * @param probId	问题id
	 * @param params
	 * 			advice	办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startWorkflow4AccountEn", method = RequestMethod.POST)
	public ResultObj startWorkflow4Case(HttpSession session,
			@RequestParam(value = "probId") Long probId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;

		try {
			boolean result = accountabilityEnforcementService.updateByProbId(probId, params, userInfo);

			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}

		return resultObj;
	}

	/**
	 * 提交至下一环节
	 * @param session
	 * @param probId			问题id
	 * @param nextNodeName		下一环节名称
	 * @param params
	 * 			nextUserIds 	下一环节办理人员，以英文逗号分隔
	 * 			nextOrgIds 		下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应
	 * 			advice 			办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/subWorkflow4AccountEn", method = RequestMethod.POST)
	public ResultObj subWorkflow4AccountEn(HttpSession session,
			@RequestParam(value = "formId") Long probId,
			@RequestParam(value = "nextNodeName") String nextNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;

		try {
			boolean flag = accountabilityEnforcementService.subWorkflow4AccountEn(probId, nextNodeName, null, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 驳回上一步
	 * @param session
	 * @param probId			问题id
	 * @param rejectToNodeName	驳回环节的名称
	 * @param params
	 * 			advice 			驳回意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectWorkflow4AccountEn", method = RequestMethod.POST)
	public ResultObj rejectWorkflow4AccountEn(HttpSession session,
			@RequestParam(value = "formId") Long probId,
			@RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;

		try {
			boolean flag = accountabilityEnforcementService.rejectWorkflow4AccountEn(probId, rejectToNodeName, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 批量归档当前用户的草稿问题
	 * @param session
	 * @param params
	 * 			listType	列表类型，默认为1 草稿列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/endAccountEnDraft", method = RequestMethod.POST)
	public Map<String, Object> endAccountEnDraft(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		int pageNo = 1, pageSize = 100, total = 0, recordTotal = 0, successTotal = 0;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		EUDGPagination pagination = null;
		
		do {
			pagination = accountabilityEnforcementService.findAccountEnPagination(pageNo, pageSize, params, userInfo);
			
			total = pagination.getTotal();
			
			if(total > 0) {
				List<Map<String, Object>> accountEnMapList = (List<Map<String, Object>>)pagination.getRows();
				Map<String, Object> startMap = new HashMap<String, Object>();
				boolean result = false;
				Long probId = null;
				
				startMap.put("isClose", true);
				
				for(Map<String, Object> accountEnMap : accountEnMapList) {
					probId = -1L;
					result = false;
					
					try {
						if(CommonFunctions.isNotBlank(accountEnMap, "probId")) {
							probId = Long.valueOf(accountEnMap.get("probId").toString());
						}
						
						result = accountabilityEnforcementService.startWorkflow4AccountEn(probId, userInfo, startMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(result) {
						successTotal++;
					}
				}
				
				recordTotal += total; 
				pageNo++;
				
				if(successTotal == 0) {//防止归档失败，导致循环无法结束
					break;
				}
			}
		} while(total > 0);
		
		resultMap.put("recordTotal", recordTotal);
		resultMap.put("successTotal", successTotal);
		
		return resultMap;
	}

	/**
	 * 获取工作流相关信息
	 * @param probId	问题id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param userInfo
	 * @param map
	 * @throws Exception
	 */
	private void capWorkflowRelativeData(Long probId, Integer listType, UserInfo userInfo, ModelMap map) throws Exception {
		Map<String, Object> curTaskData = null;
		Long taskId = -1L,
			 instanceId = accountabilityEnforcementService.capInstanceId(probId, userInfo);

		if(instanceId > 0) {
			curTaskData = accountabilityEnforcementService.findCurTaskData(probId, userInfo);

			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			}

			List<Map<String, Object>> participantMapList = accountabilityEnforcementService.findParticipantsByTaskId(taskId);
			if(participantMapList != null) {
				StringBuffer taskPersonStr = new StringBuffer(";");

				for(Map<String, Object> participant : participantMapList){
					Object orgNameObj = participant.get("ORG_NAME");

					if(CommonFunctions.isNotBlank(participant, "USER_NAME")){
						taskPersonStr.append(participant.get("USER_NAME"));
						if(orgNameObj != null){
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					}else if(orgNameObj != null){
						taskPersonStr.append(orgNameObj).append(";");
					}
				}

				map.addAttribute("taskPersonStr", taskPersonStr.substring(1));

				if(listType == 2) {//待办
					Map<String, Object> nextTaskMap = new HashMap<String, Object>();

					map.addAttribute("isCurHandler", accountabilityEnforcementService.isCurTaskPaticipant(participantMapList, userInfo, null));

					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						map.addAttribute("curNodeName", curTaskData.get("NODE_NAME"));
					}
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
						Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
						map.addAttribute("operateList", accountabilityEnforcementService.findOperateByNodeId(nodeId));
					}

					nextTaskMap.put("instanceId", instanceId);

					map.addAttribute("nextTaskNodes", accountabilityEnforcementService.findNextTaskNodes(probId, userInfo, nextTaskMap));
				}
			}

			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				map.addAttribute("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
			}

			map.addAttribute("formId", probId);
			map.addAttribute("formType", FORM_TYPE);
			map.addAttribute("instanceId", instanceId);
		}
	}
}
