package cn.ffcs.zhsq.mwInternet.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserRoleBo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.uam.service.UserRoleOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.MWInternetService;
import cn.ffcs.zhsq.mwInternet.service.WarnTaskService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.WarnTask;
import cn.ffcs.zhsq.mybatis.persistence.mwInternet.WarnTaskMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * @Description: 告警任务表模块服务实现
 * @Author: guoyd
 * @Date: 04-08 10:18:25
 * @Copyright: 2018 福富软件
 */
@Service("warnTaskServiceImpl")
@Transactional
public class WarnTaskServiceImpl implements WarnTaskService {

	@Autowired
	private WarnTaskMapper warnTaskMapper; //注入告警任务表模块dao
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private MWInternetService workflowService;
	@Autowired
    private UserRoleOutService userRoleService;
	@Autowired
    private UserManageOutService userManageService;
	@Autowired
	private IAttachmentService attachmentService;
	
	private String REQ_ATTACHMENT_TYPE = "warnTaskAtta";//附件类型
	private String REQ_WFTASK_TYPE = "warnWFTaskAtta";//附件类型
	/**
	 *告警详情点击生成任务接口
	 */
	public Map<String, Object> insert(WarnTask bo,Map<String, Object> params){
		
		Long dwtId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			warnTaskMapper.insert(bo);
			dwtId = bo.getDwtId();
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "告警任务新增失败");
			System.out.println("-----告警任务新增失败------");
			e.printStackTrace();
		}
		
		if(dwtId!=null){
			try {
				params.put("formId", dwtId);
				resultMap = workflowService.startMWInternetFlow(params);
				resultMap.put("dwtId", dwtId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				warnTaskMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	@Override
	public Long insert(WarnTask bo) {
		warnTaskMapper.insert(bo);
		return bo.getDwtId();
	}

	/**
	 * 修改数据
	 * @param bo 告警任务表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(WarnTask bo) {
		long result = warnTaskMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 告警任务表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(WarnTask bo) {
		long result = warnTaskMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params,String orgCode) {
		if(null == params.get("formTypeId")){
			params.put("formTypeId", workflowService.getFormTypeId(""));
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<WarnTask> list = warnTaskMapper.searchDBList(params, rowBounds);
		formate(list, orgCode);
		long count = warnTaskMapper.countDBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params,String orgCode) {
		if(null == params.get("formTypeId")){
			params.put("formTypeId", workflowService.getFormTypeId(""));
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<WarnTask> list = warnTaskMapper.searchJBList(params, rowBounds);
		formate(list, orgCode);
		long count = warnTaskMapper.countJBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params,String orgCode) {
		if(null == params.get("formTypeId")){
			params.put("formTypeId", workflowService.getFormTypeId(""));
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<WarnTask> list = warnTaskMapper.searchAllList(params, rowBounds);
		formate(list, orgCode);
		long count = warnTaskMapper.countAllList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchFQList(int page, int rows, Map<String, Object> params,String orgCode) {
		if(null == params.get("formTypeId")){
			params.put("formTypeId", workflowService.getFormTypeId(""));
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<WarnTask> list = warnTaskMapper.searchFQList(params, rowBounds);
		formate(list, orgCode);
		long count = warnTaskMapper.countFQList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchEndList(int page, int rows, Map<String, Object> params,String orgCode) {
		if(null == params.get("formTypeId")){
			params.put("formTypeId", workflowService.getFormTypeId(""));
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<WarnTask> list = warnTaskMapper.searchEndList(params, rowBounds);
		formate(list, orgCode);
		long count = warnTaskMapper.countEndList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	private void formate(List<WarnTask> list, String orgCode) {
		//设备类型
		List<BaseDataDict> deviceTypeDD = dictionaryService.getDataDictListOfSinglestage(ConstantValue.DICT_DEVICE_TYPE, orgCode);
		if(list!=null && list.size()>0){
			for(WarnTask re : list){
				if(null==re.getDeviceType()) continue;
				for(BaseDataDict dataDict : deviceTypeDD){
					if(re.getDeviceType().equals(dataDict.getDictGeneralCode())){
						re.setDeviceTypeStr(dataDict.getDictName());
						break;
					}
				}
			}
		}
		//设备厂商
		List<BaseDataDict> deviceManuDD = dictionaryService.getDataDictListOfSinglestage(ConstantValue.DICT_DEVICE_MANU, orgCode);
		if(list!=null && list.size()>0){
			for(WarnTask re : list){
				if(null==re.getManufacturer()) continue;
				for(BaseDataDict dataDict : deviceManuDD){
					if(re.getManufacturer().equals(dataDict.getDictGeneralCode())){
						re.setManufacturerStr(dataDict.getDictName());
						break;
					}
				}
			}
		}
	}

	/**
	 * 根据业务id查询数据
	 * @param id 告警任务表id
	 * @return 告警任务表业务对象
	 */
	@Override
	public WarnTask searchById(Long id,String orgCode) {
		WarnTask bo = warnTaskMapper.searchById(id);
		if(bo!=null){
			List<WarnTask> list = new ArrayList<WarnTask>();
			list.add(bo);
			formate(list, orgCode);
		}
		return bo;
	}

	
	public Object appSave(WarnTask bo, UserInfo userInfo){
		
		Map<String, Object> resultMap = new HashMap<String, Object>(); 
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String result = "fail";
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
					
					resultMap = this.insert(bo,resultMap);
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
	
	@SuppressWarnings("unchecked")
	public Object appInitToDo(Long id, Long instanceId, String taskId, UserInfo userInfo){
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		if (id != null) {
			WarnTask bo = this.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
			
			List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDwtId(), "", "");
			for(Map<String, Object> task : taskList){//流程详情
				String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
				task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
			}
			map.put("taskList", taskList);
			
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
			map.put("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		}
		
		return map;
	}
	
	public Object appSubmit(String buttonType, UserInfo userInfo,Long dwtId, 
			String instanceId, String taskId,String nextNodeName,String overview,
			String userIds,String curOrgIds,String overTime){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
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
				warnTask = this.searchById(dwtId,userInfo.getOrgCode());
				warnTask.setState(getState(warnTask.getState(),"2"));//更新状态
				warnTask.setWarnState("2");
				this.update(warnTask);
//				if(null!=attachmentIds){
//					saveAtta(Long.parseLong(taskId), REQ_WFTASK_TYPE, attachmentIds);
//				}
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流驳回失败-----");
			}
		}else{//提交
			try {
				warnTask = this.searchById(dwtId,userInfo.getOrgCode());
				
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
					warnTask.setOverTime(DateUtils.convertStringToDate(overTime, "yyyy-MM-dd"));
				}
				warnTask.setState(getState(warnTask.getState(),"1"));
				warnTask.setWarnState("1");
				this.update(warnTask);
//				if(null!=attachmentIds){
//					saveAtta(Long.parseLong(taskId), REQ_WFTASK_TYPE, attachmentIds);
//				}
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-----工作流提交失败-----");
			}
		}
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	public Object appDetail(Long id, UserInfo userInfo){
		
		Map<String, Object> map = new HashMap<String, Object>();
		WarnTask bo = this.searchById(id,userInfo.getOrgCode());
		map.put("bo", bo);
		map.put("REQ_ATTACHMENT_TYPE",REQ_ATTACHMENT_TYPE);
		List<Map<String, Object>> taskList = workflowService.queryInstanceDetail(bo.getDwtId(), "", "");
		for(Map<String, Object> task : taskList){//流程详情
			String bizId = ((BigDecimal) task.get("TASK_ID")).toString();
			task.put("fileList", attachmentService.findByBizId(Long.parseLong(bizId), REQ_WFTASK_TYPE));
		}
		map.put("taskList", taskList);
		
		return map;
	}
	
	public Object selectRole(UserInfo userInfo){
		
		List<GdZTreeNode> resultMap = new ArrayList<GdZTreeNode>();
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
		return resultMap;
	}
	
	public Object appSelectUser(String roleId, UserInfo userInfo){
		
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