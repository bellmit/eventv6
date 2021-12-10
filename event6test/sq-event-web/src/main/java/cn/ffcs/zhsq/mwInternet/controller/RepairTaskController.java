package cn.ffcs.zhsq.mwInternet.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserRoleBo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.uam.service.UserRoleOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.MWInternetService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mwInternet.service.RepairTaskService;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.RepairTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**   
 * @Description: 报修任务表模块控制器
 * @Author: guoyd
 * @Date: 04-08 10:19:19
 * @Copyright: 2018 福富软件
 */ 
@Controller("repairTaskController")
@RequestMapping("/zhsq/repairTask")
public class RepairTaskController extends ZZBaseController {

	@Autowired
	private RepairTaskService repairTaskService; //注入报修任务表模块服务
	@Autowired
	private MWInternetService workflowService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
    private UserManageOutService userManageService;
	@Autowired
    private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
    private UserRoleOutService userRoleService;
	
	static String formTypeId = "";
	static String workflowId = "";
	private String REQ_ATTACHMENT_TYPE = "repairTaskAtta";//附件类型
	private String REQ_WFTASK_TYPE = "repairWFTaskAtta";//附件类型
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		map.addAttribute("faultType", ConstantValue.REPAIRTASK_FAULT_TYPE);
		String indexType = request.getParameter("indexType");
		
		if("db".equals(indexType)){
			return "/zzgl/mwInternet/repairTask/list_repairTask_db.ftl";
		}else if("jb".equals(indexType)){
			return "/zzgl/mwInternet/repairTask/list_repairTask_jb.ftl";
		}else if("all".equals(indexType)){
			return "/zzgl/mwInternet/repairTask/list_repairTask_all.ftl";
		}else if("fq".equals(indexType)){
//			initMap(request, session, map);
			return "/zzgl/mwInternet/repairTask/list_repairTask_fq.ftl";
		}else if("end".equals(indexType)){
			return "/zzgl/mwInternet/repairTask/list_repairTask_end.ftl";
		}
		
		return "/zzgl/mwInternet/repairTask/list_repairTask_all.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		
		initParams();
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", request.getParameter("state"));
		params.put("infoOrgCode", request.getParameter("infoOrgCode"));
		params.put("faultType", request.getParameter("faultType"));
		params.put("formTypeId", formTypeId);
		String startTimeStr = request.getParameter("startTimeStr");
		if(!StringUtils.isBlank(startTimeStr)){ 
			startTimeStr += " 00:00:00";
			params.put("startTimeStr", startTimeStr);
		}
		String endTimeStr = request.getParameter("endTimeStr");
		if(!StringUtils.isBlank(endTimeStr)){
			endTimeStr += " 23:59:59";
			params.put("endTimeStr", endTimeStr);
		}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		EUDGPagination pagination = null;
		String indexType = request.getParameter("indexType");
		if("db".equals(indexType)){
			params.put("dbOrgId", userInfo.getOrgId().toString());
			params.put("dbUserId", userInfo.getUserId().toString());
			pagination = repairTaskService.searchDBList(page, rows, params,userInfo.getOrgCode());
		}else if("jb".equals(indexType)){
			params.put("transactorOrgId", userInfo.getOrgId());
			params.put("transactorId", userInfo.getUserId());
			pagination = repairTaskService.searchJBList(page, rows, params,userInfo.getOrgCode());
		}else if("all".equals(indexType)){
			pagination = repairTaskService.searchAllList(page, rows, params,userInfo.getOrgCode());
		}else if("fq".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = repairTaskService.searchFQList(page, rows, params,userInfo.getOrgCode());
		}else if("end".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = repairTaskService.searchEndList(page, rows, params,userInfo.getOrgCode());
		}
		
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
			Long id,String wfInstanceId) {
		
		map.addAttribute("bizType", REQ_ATTACHMENT_TYPE);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RepairTask bo = repairTaskService.searchById(id,userInfo.getOrgCode());
		map.addAttribute("bo", bo);
		
		if(wfInstanceId!=null && !StringUtils.isBlank(wfInstanceId)){//流程详情
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(id, "", "");
			for(Map<String, Object> task : taskList){
				String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
				task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
			}
			map.addAttribute("taskList", taskList);
			
			return "/zzgl/mwInternet/repairTask/detail_repairTask_tab.ftl";//包含流程的详情
		}
		
		return "/zzgl/mwInternet/repairTask/detail_repairTask.ftl";//不包含流程的详情
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		RepairTask bo = new RepairTask();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (id != null) {
			bo = repairTaskService.searchById(id,userInfo.getOrgCode());
		}else{
			bo.setInfoOrgName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
			bo.setInfoOrgCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
			bo.setOrderTimeStr(DateUtils.getToday("yyyy-MM-dd"));
		}
		map.addAttribute("bo", bo);
		map.addAttribute("faultType", ConstantValue.REPAIRTASK_FAULT_TYPE);
		map.addAttribute("bizType", REQ_ATTACHMENT_TYPE);
		
		return "/zzgl/mwInternet/repairTask/form_repairTask.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		RepairTask bo,String buttonType,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		
		if("1".equals(buttonType)){//保存草稿
			Long id = null;
			if (bo.getDrtId() == null) { //新增
				bo.setState("0");
				bo.setOrgId(userInfo.getOrgId());
				bo.setCreateBy(userInfo.getUserId());
				bo.setCreateTime(new Date());
				
				repairTaskService.insert(bo);
				id = bo.getDrtId();
				if (id != null && id > 0) {
					result = "success";
				};
			} else { //修改
				bo.setUpdateBy(userInfo.getUserId());
				bo.setUpdateTime(new Date());
				boolean updateResult = repairTaskService.update(bo);
				if (updateResult) {
					id = bo.getDrtId();
					result = "success";
				}
			}
			if(null!=attachmentIds&&id!=null){
				saveAtta(id, REQ_ATTACHMENT_TYPE, attachmentIds);
			}
		}else if("2".equals(buttonType)){//提交
			bo.setRepairTime(new Date());
			resultMap.put("userId", userInfo.getUserId());
			resultMap.put("userName", userInfo.getPartyName());
			resultMap.put("orgId", userInfo.getOrgId());
			resultMap.put("orgName", userInfo.getOrgName());
			//TODO查找所属小区的管理员bo.infoOrgCode
			String userIds = "";
			String orgIds = "";
			UserRoleBo userRoleBo = null;
			try {
				userRoleBo = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_GLY);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "0");
				resultMap.put("message", "管理人员获取失败");
				System.out.println("-------------管理人员获取失败---------------orgId="+userInfo.getOrgId()+"role="+ConstantValue.MWWLW_GLY);
			}

			if(userRoleBo!=null){
				//地域转组织
			    OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.getOrgIdByLocationCode(bo.getInfoOrgCode());
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(userRoleBo.getRoleId(), null, orgSocialInfoBO.getOrgId());
				if(!userList.isEmpty()&&userList.size()>0){
					for(UserBO temp : userList){
						userIds += temp.getUserId() + ",";
						orgIds += userInfo.getOrgId() + ",";

					}
					if(!StringUtils.isBlank(userIds)){
						userIds = userIds.substring(0, userIds.length()-1);
						orgIds = orgIds.substring(0, orgIds.length()-1);
					}
				} 
				if(!StringUtils.isBlank(userIds)){
					resultMap.put("userIds", userIds);
					resultMap.put("curOrgIds", orgIds);
					resultMap.put("nextNodeName", "task2");
					bo.setState("1");
					
					if (bo.getDrtId() == null) { //新增
						bo.setOrgId(userInfo.getOrgId());
						bo.setCreateBy(userInfo.getUserId());
						bo.setCreateTime(new Date());
						
						resultMap = repairTaskService.insert(bo,resultMap);
					} else { //修改
						bo.setUpdateBy(userInfo.getUserId());
						bo.setUpdateTime(new Date());
						resultMap = repairTaskService.update(bo,resultMap);
					}
					String status = (String) resultMap.get("status");
					if("1".equals(status)){
						if(resultMap.get("formId")!=null){
							Long keyId = Long.parseLong(resultMap.get("formId").toString());
							if(null!=attachmentIds&&keyId!=null){
								saveAtta(keyId, REQ_ATTACHMENT_TYPE, attachmentIds);
							}
						}
						result = "success";
					}
				}else{
					resultMap.put("status", "0");
					resultMap.put("message", "为获取到管理人员");
					System.out.println("-------------管理人员获取失败---------------orgId="+userInfo.getOrgId()+"role="+ConstantValue.MWWLW_GLY);
				}
			}else{
				resultMap.put("status", "0");
				resultMap.put("message", "管理人员获取失败");
				System.out.println("-------------管理人员获取失败---------------orgId="+userInfo.getOrgId()+"role="+ConstantValue.MWWLW_GLY);

			}
		}
		
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 跳转到办理界面
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/todo")
	public Object todo(HttpServletRequest request, HttpSession session, ModelMap map,Long id,
		@RequestParam(value = "instanceId", required = true) Long instanceId,
		@RequestParam(value = "taskId", required=true) String taskId) {
		
		if (id != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			RepairTask bo = repairTaskService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
			
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDrtId(), "", "");
			for(Map<String, Object> task : taskList){
				String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
				task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
			}
			map.addAttribute("taskList", taskList);
			
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> rs = workflowService.initFlowInfo(taskId, instanceId, params);
			Node curNode = (Node)rs.get("curNode");
			map.put("dynamicSelect", curNode.getDynamicSelect());//是否选人
			map.put("nodeNameZH", curNode.getNodeNameZH());//当前环节
			List<Node> taskNodes = (List<Node>) rs.get("taskNodes");
			if(!taskNodes.isEmpty()){
				map.put("nextNodeName", taskNodes.get(0).getNodeName());//下一环节
			}
			List<OperateBean> operateLists = (List<OperateBean>) rs.get("operateLists");
			if(!operateLists.isEmpty()){
				map.put("operateLists", operateLists.size());//驳回，提交数量
			}
			map.put("instanceId", instanceId);
			map.put("taskId", taskId);
			map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
			map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		}
		return "/zzgl/mwInternet/repairTask/task_repairTask.ftl";
	}
	
	/**
	 * 提交归档
	 */
	@ResponseBody
	@RequestMapping("/submit")
	public Object submit(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value = "buttonType", required=true) String buttonType,
		@RequestParam(value = "taskId", required=true) String taskId,
		@RequestParam(value = "nextNodeName", required=true) String nextNodeName,
		Long drtId,String overview,String instanceId,String userIds,String curOrgIds,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		//buttonType(1不予受理,2派发,3归档)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		RepairTask repairTask = null;
		
		if("2".equals(buttonType)){//驳回
			try {
				UserBO user = new UserBO();
				user.setUserId(userInfo.getUserId());
				user.setPartyName(userInfo.getPartyName());
				OrgSocialInfoBO org = new OrgSocialInfoBO();
				org.setOrgId(userInfo.getOrgId());
				org.setOrgName(userInfo.getOrgName());
				workflowService.rejectWorkFlow(taskId, overview, instanceId, user, org);
				repairTask = repairTaskService.searchById(drtId,userInfo.getOrgCode());
				repairTask.setState(getState(repairTask.getState(),"2"));
				repairTask.setWarnState("2");
				repairTaskService.update(repairTask);
				if(null!=attachmentIds){
					saveAtta(Long.parseLong(taskId), REQ_WFTASK_TYPE, attachmentIds);
				}
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流驳回失败-----");
			}
		}else{//提交
			try {
				repairTask = repairTaskService.searchById(drtId,userInfo.getOrgCode());
				
				resultMap.put("taskId", taskId);
				resultMap.put("nextNodeName", nextNodeName);
				resultMap.put("userId", userInfo.getUserId());
				resultMap.put("userName", userInfo.getPartyName());
				resultMap.put("remarks", overview);
				resultMap.put("orgId", userInfo.getOrgId());
				resultMap.put("orgName", userInfo.getOrgName());
				
				if("task4".equals(nextNodeName)){//审核取人
					userIds = repairTask.getTaskUserId().toString();
					curOrgIds = repairTask.getTaskOrgId().toString();
				}
				resultMap.put("userIds", userIds);
				resultMap.put("curOrgIds", curOrgIds);
				
				resultMap =  workflowService.submitMWInternetFlow(resultMap);
				
				if("task3".equals(nextNodeName)){//任务分配
					repairTask.setTaskUserId(userInfo.getUserId());
					repairTask.setTaskOrgId(userInfo.getOrgId());
					repairTask.setOverTime(DateUtils.convertStringToDate(request.getParameter("overTime"), "yyyy-MM-dd"));
				}
				
				repairTask.setState(getState(repairTask.getState(),"1"));
				repairTask.setWarnState("1");
				repairTaskService.update(repairTask);
				if(null!=attachmentIds){
					saveAtta(Long.parseLong(taskId), REQ_WFTASK_TYPE, attachmentIds);
				}
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流提交失败-----");
			}
		}
		
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		RepairTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = repairTaskService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

	private void initParams() {
		
		if(StringUtils.isBlank(formTypeId)){
			formTypeId = workflowService.getFormTypeId("");
		}
		if(StringUtils.isBlank(workflowId)){
			workflowId = workflowService.getWorkflowId("");
		}
	}
	
	/**
	 * 保存附件信息
	 * @param id 主表ID
	 * @param bizType 附件类型
	 * @param attachmentIds 附件ID
	 */
	private void saveAtta(Long id,String bizType,Long[] attachmentIds) {
		if(attachmentIds!=null && attachmentIds.length>0){
			attachmentService.updateBizId(id, bizType, attachmentIds);
		}
	}
	/**
	 * 更新状态
	 * @param state
	 * @param buttonType
	 * @return
	 */
	private String getState(String state,String buttonType){
		
		if("2".equals(buttonType)){
			if("2".equals(state)){
				return "1";
			}else if("3".equals(state)){
				return "2";
			}
		}else{
			if("1".equals(state)){
				return "2";
			}else if("2".equals(state)){
				return "3";
			}else if("3".equals(state)){
				return "4";
			}
		}
		
		return state;
	}
}