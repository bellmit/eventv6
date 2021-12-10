package cn.ffcs.zhsq.mwInternet.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import cn.ffcs.zhsq.mwInternet.service.WarnTaskService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.WarnTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**   
 * @Description: 告警任务表模块控制器
 * @Author: guoyd
 * @Date: 04-08 10:18:25
 * @Copyright: 2018 福富软件
 */ 
@Controller("warnTaskController")
@RequestMapping("/zhsq/warnTask")
public class WarnTaskController extends ZZBaseController {

	@Autowired
	private WarnTaskService warnTaskService; //注入告警任务表模块服务
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
	private String REQ_ATTACHMENT_TYPE = "warnTaskAtta";//附件类型
	private String REQ_WFTASK_TYPE = "warnWFTaskAtta";//附件类型
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		map.addAttribute("deviceType", ConstantValue.DICT_DEVICE_TYPE);
		
		String indexType = request.getParameter("indexType");
		if("db".equals(indexType)){
			return "/zzgl/mwInternet/warnTask/list_warnTask_db.ftl";
		}else if("jb".equals(indexType)){
			return "/zzgl/mwInternet/warnTask/list_warnTask_jb.ftl";
		}else if("all".equals(indexType)){
			return "/zzgl/mwInternet/warnTask/list_warnTask_all.ftl";
		}else if("fq".equals(indexType)){
			return "/zzgl/mwInternet/warnTask/list_warnTask_fq.ftl";
		}else if("end".equals(indexType)){
			return "/zzgl/mwInternet/warnTask/list_warnTask_end.ftl";
		}
		
		return "/zzgl/mwInternet/warnTask/list_warnTask_all.ftl";
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
		params.put("deviceType", request.getParameter("deviceType"));
		params.put("formTypeId", formTypeId);
		params.put("infoOrgCode", request.getParameter("infoOrgCode"));
		
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
			pagination = warnTaskService.searchDBList(page, rows, params,userInfo.getOrgCode());
		}else if("jb".equals(indexType)){
			params.put("transactorOrgId", userInfo.getOrgId());
			params.put("transactorId", userInfo.getUserId());
			pagination = warnTaskService.searchJBList(page, rows, params,userInfo.getOrgCode());
		}else if("all".equals(indexType)){
			pagination = warnTaskService.searchAllList(page, rows, params,userInfo.getOrgCode());
		}else if("fq".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = warnTaskService.searchFQList(page, rows, params,userInfo.getOrgCode());
		}else if("end".equals(indexType)){
			params.put("orgId", userInfo.getOrgId());
			params.put("createBy", userInfo.getUserId());
			pagination = warnTaskService.searchEndList(page, rows, params,userInfo.getOrgCode());
		}
		
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		WarnTask bo = warnTaskService.searchById(id,userInfo.getOrgCode());
		map.addAttribute("bo", bo);
		map.addAttribute("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDwtId(), "", "");
		for(Map<String, Object> task : taskList){//流程详情
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
		}
		map.addAttribute("taskList", taskList);
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", "7778");//设备地图类型
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		return "/zzgl/mwInternet/warnTask/detail_warnTask.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			WarnTask bo = warnTaskService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
		}
		return "/zzgl/mwInternet/warnTask/form_warnTask.ftl";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/todo")
	public Object todo(HttpServletRequest request, HttpSession session, ModelMap map,Long id,
		@RequestParam(value = "instanceId", required = true) Long instanceId,
		@RequestParam(value = "taskId", required=true) String taskId) {
		
		if (id != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			WarnTask bo = warnTaskService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
			
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDwtId(), "", "");
			for(Map<String, Object> task : taskList){//流程详情
				String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
				task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
			}
			map.addAttribute("taskList", taskList);
			
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> rs = workflowService.initFlowInfo(taskId, instanceId, params);//初始化参数
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
			
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
			map.addAttribute("gridId", gridId);
			map.addAttribute("module", "7778"); 
			map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		}
		return "/zzgl/mwInternet/warnTask/task_warnTask.ftl";
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
		Long dwtId,String overview,String instanceId,String userIds,String curOrgIds,
		@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		
		//buttonType(1不予受理,2派发,3归档)
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		WarnTask warnTask = null;
		
		if("2".equals(buttonType)){//驳回
			try {
				UserBO user = new UserBO();
				user.setUserId(userInfo.getUserId());
				user.setPartyName(userInfo.getPartyName());
				OrgSocialInfoBO org = new OrgSocialInfoBO();
				org.setOrgId(userInfo.getOrgId());
				org.setOrgName(userInfo.getOrgName());
				workflowService.rejectWorkFlow(taskId, overview, instanceId, user, org);
				warnTask = warnTaskService.searchById(dwtId,userInfo.getOrgCode());
				warnTask.setState(getState(warnTask.getState(),"2"));//更新状态
				warnTask.setWarnState("2");
				warnTaskService.update(warnTask);
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
				warnTask = warnTaskService.searchById(dwtId,userInfo.getOrgCode());
				
				resultMap.put("taskId", taskId);
				resultMap.put("nextNodeName", nextNodeName);
				resultMap.put("userId", userInfo.getUserId());
				resultMap.put("userName", userInfo.getPartyName());
				resultMap.put("remarks", overview);
				resultMap.put("orgId", userInfo.getOrgId());
				resultMap.put("orgName", userInfo.getOrgName());
				
				if("task4".equals(nextNodeName)){
					userIds = warnTask.getTaskUserId().toString();
					curOrgIds = warnTask.getTaskOrgId().toString();
				}
				resultMap.put("userIds", userIds);
				resultMap.put("curOrgIds", curOrgIds);
				
				resultMap =  workflowService.submitMWInternetFlow(resultMap);
				
				if("task3".equals(nextNodeName)){//管理员分派
					warnTask.setTaskUserId(userInfo.getUserId());
					warnTask.setTaskOrgId(userInfo.getOrgId());
					warnTask.setOverTime(DateUtils.convertStringToDate(request.getParameter("overTime"), "yyyy-MM-dd"));
				}
				warnTask.setState(getState(warnTask.getState(),"1"));
				warnTask.setWarnState("1");
				warnTaskService.update(warnTask);
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
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		WarnTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		bo = tempSave(userInfo);
		if (bo.getDwtId() == null) { //新增
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
//				OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.getOrgIdByLocationCode(bo.getInfoOrgCode());
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(userRoleBo.getRoleId(), null, userInfo.getOrgId());
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
					resultMap.put("userIds", userIds);//下一办理人
					resultMap.put("curOrgIds", orgIds);//下一办理人组织
					resultMap.put("nextNodeName", "task2");//下一办理环节
					
					resultMap = warnTaskService.insert(bo,resultMap);
					String status = (String) resultMap.get("status");
					if("1".equals(status)){
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
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/toSave")
	public Object toSave(HttpServletRequest request, HttpSession session, ModelMap map,	WarnTask bo) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
		bo = tempSave(userInfo);
		if (bo.getDwtId() == null) { //新增
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
//				OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.getOrgIdByLocationCode(bo.getInfoOrgCode());
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(userRoleBo.getRoleId(), null, userInfo.getOrgId());
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
					
					resultMap = warnTaskService.insert(bo,resultMap);
					String status = (String) resultMap.get("status");
					if("1".equals(status)){
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

	
	@ResponseBody
	@RequestMapping("/selectRole")
	public Object selectRole(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		List<GdZTreeNode> resultMap = new ArrayList<GdZTreeNode>();
		String flag = request.getParameter("type");
		if("MWWLW_GLY".equals(flag)){//管理员进入
			UserRoleBo	userRoleBo1 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_GLY);
			if(userRoleBo1!=null){
				GdZTreeNode temp = new GdZTreeNode();
				temp.setId(userRoleBo1.getRoleId().toString());
				temp.setName(userRoleBo1.getRoleName());
				temp.setPId("1");
				resultMap.add(temp);
			}
			UserRoleBo	userRoleBo2 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_CZY);
			if(userRoleBo2!=null){
				GdZTreeNode temp = new GdZTreeNode();
				temp.setId(userRoleBo2.getRoleId().toString());
				temp.setName(userRoleBo2.getRoleName());
				temp.setPId("1");
				resultMap.add(temp);
			}
		}else if("MWWLW_CZY".equals(flag)){//处置员进入
			UserRoleBo	userRoleBo2 = userRoleService.queryUserRoleByRoleName(ConstantValue.MWWLW_CZY);
			if(userRoleBo2!=null){
				GdZTreeNode temp = new GdZTreeNode();
				temp.setId(userRoleBo2.getRoleId().toString());
				temp.setName(userRoleBo2.getRoleName());
				temp.setPId("1");
				resultMap.add(temp);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 角色选人
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUser")
	public Object selectUser(HttpServletRequest request, HttpSession session, ModelMap map) {
		 
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String roleId = request.getParameter("id");
		if(!StringUtils.isBlank(roleId)){
			List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(Long.parseLong(roleId), null, userInfo.getOrgId());
			if(!userList.isEmpty()&&userList.size()>0){
				List<cn.ffcs.uam.bo.UserInfo> tempList = new ArrayList<cn.ffcs.uam.bo.UserInfo>();
				for(UserBO user : userList){
					cn.ffcs.uam.bo.UserInfo temp = new cn.ffcs.uam.bo.UserInfo();
					temp.setUserId(user.getUserId());
					temp.setPartyName(user.getPartyName());
					temp.setOrgId(userInfo.getOrgId());
					temp.setOrgName(userInfo.getOrgName());
					tempList.add(temp);
				}
				return tempList;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		WarnTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = warnTaskService.delete(bo);
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
	
	private WarnTask tempSave(UserInfo userInfo){
		
		WarnTask bo = new WarnTask();
		
		bo.setInfoOrgCode("3501");
		bo.setInfoOrgName("福州市");
		bo.setDeviceId(800L);
		bo.setDeviceName("垃圾箱");
		bo.setDeviceGlag("c17a3151-f395-4448-9893-3f8d8bfce472");
		bo.setWarnTime(new Date());
		bo.setWarnInfo("烟雾报警器异常");
		bo.setDeviceAddr("晋安区江滨东大道68号名城国际17号楼1层电梯口");
		bo.setCreateBy(userInfo.getUserId());
		bo.setCreateTime(new Date());
		bo.setOrgId(userInfo.getOrgId());
		bo.setState("1");
		bo.setManufacturer("10002");
		bo.setDeviceType("100004");
		bo.setDeviceImage("http://okb9fznx7.bkt.clouddn.com/WechatIMG201.jpeg");
		
		return bo;
	}

	/**
	 * 提交，驳回状态更新
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