package cn.ffcs.zhsq.mwInternet.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.MWInternetService;
import cn.ffcs.zhsq.mwInternet.service.DailyTaskService;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.DailyTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**   
 * @Description: 日常任务表模块控制器
 * @Author: guoyd
 * @Date: 04-08 10:20:12
 * @Copyright: 2018 福富软件
 */ 
@Controller("dailyTaskController")
@RequestMapping("/zhsq/dailyTask")
public class DailyTaskController  extends ZZBaseController{

	@Autowired
	private DailyTaskService dailyTaskService; //注入日常任务表模块服务
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static String formTypeId = "";
	static String workflowId = "";
	private String REQ_ATTACHMENT_TYPE = "dailyTaskAtta";//附件类型
	private String REQ_WFTASK_TYPE = "dailyWFTaskAtta";//附件类型
	
	/**
	 * 管理员，处置员权限
	 * @param request
	 * @param session
	 * @param map
	 */
	private void initMap(HttpServletRequest request, HttpSession session, ModelMap map){
		
		String flag = "";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		UserRoleBo userRoleBo1 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_GLY);
		List<UserBO> userList1 = userManageService.getUserListByUserExampleParamsOut(userRoleBo1.getRoleId(), null, userInfo.getOrgId());
		if(!userList1.isEmpty()&&userList1.size()>0){
			for(UserBO temp : userList1){
				if(userInfo.getUserId().toString().equals(temp.getUserId().toString())){
					flag = "1";
					break;
				}
			}
		}
		if(StringUtils.isBlank(flag)){
			UserRoleBo userRoleBo2 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_CZY);
			List<UserBO> userList2 = userManageService.getUserListByUserExampleParamsOut(userRoleBo2.getRoleId(), null, userInfo.getOrgId());
			for(UserBO temp : userList2){
				if(userInfo.getUserId().toString().equals(temp.getUserId().toString())){
					flag = "1";
					break;
				}
			}
		}
		map.addAttribute("flag", flag);
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		String indexType = request.getParameter("indexType");
		
		if("db".equals(indexType)){
			return "/zzgl/mwInternet/dailyTask/list_dailyTask_db.ftl";
		}else if("jb".equals(indexType)){
			return "/zzgl/mwInternet/dailyTask/list_dailyTask_jb.ftl";
		}else if("all".equals(indexType)){
			return "/zzgl/mwInternet/dailyTask/list_dailyTask_all.ftl";
		}else if("fq".equals(indexType)){
			initMap(request, session, map);
			return "/zzgl/mwInternet/dailyTask/list_dailyTask_fq.ftl";
		}else if("end".equals(indexType)){
			return "/zzgl/mwInternet/dailyTask/list_dailyTask_end.ftl";
		}
		
		return "/zzgl/mwInternet/dailyTask/list_dailyTask_all.ftl";
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
		params.put("taskTitle", request.getParameter("taskTitle"));
		params.put("infoOrgCode", request.getParameter("infoOrgCode"));
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
			pagination = dailyTaskService.searchDBList(page, rows, params,userInfo.getOrgCode());
		}else if("jb".equals(indexType)){
			params.put("transactorOrgId", userInfo.getOrgId());
			params.put("transactorId", userInfo.getUserId());
			pagination = dailyTaskService.searchJBList(page, rows, params,userInfo.getOrgCode());
		}else if("all".equals(indexType)){
			pagination = dailyTaskService.searchAllList(page, rows, params,userInfo.getOrgCode());
		}else if("fq".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = dailyTaskService.searchFQList(page, rows, params,userInfo.getOrgCode());
		}else if("end".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = dailyTaskService.searchEndList(page, rows, params,userInfo.getOrgCode());
		}
		
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
			Long id,String wfInstanceId) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		DailyTask bo = dailyTaskService.searchById(id,userInfo.getOrgCode());
		map.addAttribute("bo", bo);
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		if(wfInstanceId!=null && !StringUtils.isBlank(wfInstanceId) && !wfInstanceId.equals("null")){
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDdtId(), "", "");
			for(Map<String, Object> task : taskList){
				String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
				task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
			}
			map.addAttribute("taskList", taskList);
			
			return "/zzgl/mwInternet/dailyTask/detail_dailyTask_tab.ftl";
		}
		
		return "/zzgl/mwInternet/dailyTask/detail_dailyTask.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		
		DailyTask bo = new DailyTask();
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		if (id != null) {
			bo = dailyTaskService.searchById(id, userInfo.getOrgCode());
		} else {
			bo.setInfoOrgName(defaultGridInfo.get(KEY_START_GRID_NAME).toString());
			bo.setInfoOrgCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		}
		map.addAttribute("bo", bo);
		map.addAttribute("bizType", REQ_ATTACHMENT_TYPE);
		
		String flag = "";
		UserRoleBo userRoleBo1 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_GLY);
		List<UserBO> userList1 = userManageService.getUserListByUserExampleParamsOut(userRoleBo1.getRoleId(), null, userInfo.getOrgId());
		if(!userList1.isEmpty()&&userList1.size()>0){//管理员分配
			for(UserBO temp : userList1){
				if(userInfo.getUserId().toString().equals(temp.getUserId().toString())){
					flag = "MWWLW_GLY";
					break;
				}
			}
		}
		if(StringUtils.isBlank(flag)){
			UserRoleBo userRoleBo2 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_CZY);
			List<UserBO> userList2 = userManageService.getUserListByUserExampleParamsOut(userRoleBo2.getRoleId(), null, userInfo.getOrgId());
			for(UserBO temp : userList2){//处置员默认自己
				if(userInfo.getUserId().toString().equals(temp.getUserId().toString())){
					flag = "MWWLW_CZY";
					map.addAttribute("userIds", userInfo.getUserId());
					map.addAttribute("userNames", userInfo.getPartyName());
					map.addAttribute("curOrgIds", userInfo.getOrgId());
					map.addAttribute("curOrgNames", userInfo.getOrgName());
					break;
				}
			}
		}
		
		map.addAttribute("flag", flag);
		
		return "/zzgl/mwInternet/dailyTask/form_dailyTask.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		DailyTask bo,String buttonType,String userIds,String curOrgIds,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		 
		if("1".equals(buttonType)){//保存草稿
			Long id = null;
			if (bo.getDdtId() == null) { //新增
				bo.setState("0");
				bo.setOrgId(userInfo.getOrgId());
				bo.setCreateBy(userInfo.getUserId());
//				bo.setCreateTime(new Date());
				
				dailyTaskService.insert(bo);
				id = bo.getDdtId();
				if (id != null && id > 0) {
					result = "success";
				};
			} else { //修改
				bo.setUpdateBy(userInfo.getUserId());
				bo.setUpdateTime(new Date());
				boolean updateResult = dailyTaskService.update(bo);
				if (updateResult) {
					id = bo.getDdtId();
					result = "success";
				}
			}
			if(null!=attachmentIds&&id!=null){
				saveAtta(id, REQ_ATTACHMENT_TYPE, attachmentIds);
			}
		}else if("2".equals(buttonType)){//提交
			bo.setCreateTime(new Date());
			resultMap.put("userId", userInfo.getUserId());
			resultMap.put("userName", userInfo.getPartyName());
			resultMap.put("orgId", userInfo.getOrgId());
			resultMap.put("orgName", userInfo.getOrgName());
			resultMap.put("userIds", userInfo.getUserId());
			resultMap.put("curOrgIds", userInfo.getOrgId());
			resultMap.put("nextNodeName", "task2");
			
			if (bo.getDdtId() == null) { //新增
				bo.setState("0");
				bo.setOrgId(userInfo.getOrgId());
				bo.setCreateBy(userInfo.getUserId());
				resultMap = dailyTaskService.insert(bo,resultMap);
			} else { //修改
				bo.setUpdateBy(userInfo.getUserId());
				bo.setUpdateTime(new Date());
				resultMap = dailyTaskService.update(bo,resultMap);
			}
			
			String status = (String) resultMap.get("status");
			if("1".equals(status)){
				if(resultMap.get("formId")!=null){
					Long keyId = Long.parseLong(resultMap.get("formId").toString());
					if(null!=attachmentIds&&keyId!=null){
						saveAtta(keyId, REQ_ATTACHMENT_TYPE, attachmentIds);
					}
					//再提交
					String taskId = resultMap.get("taskId").toString();
					resultMap.clear();
					resultMap.put("taskId", taskId);
					resultMap.put("nextNodeName", "task3");
					resultMap.put("userId", userInfo.getUserId());
					resultMap.put("userName", userInfo.getPartyName());
					resultMap.put("orgId", userInfo.getOrgId());
					resultMap.put("orgName", userInfo.getOrgName());
					resultMap.put("userIds", userIds);
					resultMap.put("curOrgIds", curOrgIds);
					String overview = request.getParameter("overview");
					resultMap.put("remarks", overview);
					resultMap =  workflowService.submitMWInternetFlow(resultMap);
					
					bo.setState("2");
					bo.setTaskUserId(userInfo.getUserId());
					bo.setTaskOrgId(userInfo.getOrgId());
					dailyTaskService.update(bo);
					
					result = "success";
				}
			}
		}
		
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 跳转到办理页面
	 * @param request
	 * @param session
	 * @param map
	 * @param id
	 * @param instanceId
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/todo")
	public Object todo(HttpServletRequest request, HttpSession session, ModelMap map,Long id,
		@RequestParam(value = "instanceId", required = true) Long instanceId,
		@RequestParam(value = "taskId", required=true) String taskId) {
		
		if (id != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			DailyTask bo = dailyTaskService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
			
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDdtId(), "", "");
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
		return "/zzgl/mwInternet/dailyTask/task_dailyTask.ftl";
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
		Long ddtId,String overview,String instanceId,String userIds,String curOrgIds,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		//buttonType(1不予受理,2派发,3归档)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		DailyTask dailyTask = null;
		
		if("2".equals(buttonType)){//驳回
			try {
				UserBO user = new UserBO();
				user.setUserId(userInfo.getUserId());
				user.setPartyName(userInfo.getPartyName());
				OrgSocialInfoBO org = new OrgSocialInfoBO();
				org.setOrgId(userInfo.getOrgId());
				org.setOrgName(userInfo.getOrgName());
				workflowService.rejectWorkFlow(taskId, overview, instanceId, user, org);
				dailyTask = dailyTaskService.searchById(ddtId,userInfo.getOrgCode());
				dailyTask.setState(getState(dailyTask.getState(),"2"));
				dailyTask.setWarnState("2");
				dailyTaskService.update(dailyTask);
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
				dailyTask = dailyTaskService.searchById(ddtId,userInfo.getOrgCode());
				
				resultMap.put("taskId", taskId);
				resultMap.put("nextNodeName", nextNodeName);
				resultMap.put("userId", userInfo.getUserId());
				resultMap.put("userName", userInfo.getPartyName());
				resultMap.put("remarks", overview);
				resultMap.put("orgId", userInfo.getOrgId());
				resultMap.put("orgName", userInfo.getOrgName());
				
				if("task4".equals(nextNodeName)){
					userIds = dailyTask.getTaskUserId().toString();
					curOrgIds = dailyTask.getTaskOrgId().toString();
				}
				resultMap.put("userIds", userIds);
				resultMap.put("curOrgIds", curOrgIds);
				
				resultMap =  workflowService.submitMWInternetFlow(resultMap);
				
				dailyTask.setState(getState(dailyTask.getState(),"1"));
				dailyTask.setWarnState("1");
				dailyTaskService.update(dailyTask);
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
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,DailyTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = dailyTaskService.delete(bo);
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