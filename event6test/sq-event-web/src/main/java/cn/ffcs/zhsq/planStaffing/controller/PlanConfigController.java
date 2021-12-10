package cn.ffcs.zhsq.planStaffing.controller;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.EmergencyPlan;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanConfig;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;
import cn.ffcs.zhsq.planStaffing.IEmergencyPlanService;
import cn.ffcs.zhsq.planStaffing.IPlanConfigService;
import cn.ffcs.zhsq.planStaffing.IPlanMemberService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**   
 * @Description: 应急预案配置模块控制器
 * @Author: LINZHU
 * @Date: 04-23 15:47:50
 * @Copyright: 2021 福富软件
 */ 
@Controller("planConfigController")
@RequestMapping("/zhsq/zzgl/planConfig")
public class PlanConfigController {

	@Autowired
	private IPlanConfigService planConfigService; //注入应急预案配置模块服务  
	@Autowired
	private IEmergencyPlanService emergencyPlanService; //注入应急预案管理模块服务
	@Autowired
	private IPlanMemberService planMemberService; //注入应急预案人员模块服务



	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
	
		String planId=request.getParameter("planId")==null?"":request.getParameter("planId").toString();
		map.put("planId", planId);
		if (StringUtils.isNotBlank(planId)) {
			//预案信息
			EmergencyPlan emergencyPlan=emergencyPlanService.searchById(planId, userInfo.getOrgCode());
			map.put("emergencyPlan", emergencyPlan);
		}

		return "/zzgl/planStaffing/list_planConfig.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planLevel", request.getParameter("planLevel"));
		params.put("planId", request.getParameter("planId"));
		params.put("userOrgCode", userInfo.getOrgCode());
		EUDGPagination pagination = planConfigService.searchList(page, rows, params);
		return pagination;
	}
	

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		String id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PlanConfig bo =null;
		String planId="";
		EmergencyPlan emergencyPlan=null;
		List<PlanMember> list=null;
		if (StringUtils.isNotBlank(id)) {
		   bo = planConfigService.searchById(id,userInfo.getOrgCode());
		   if(bo!=null) {
				planId=bo.getPlanId();
				if (StringUtils.isNotBlank(planId)) {
					//预案信息
				    emergencyPlan=emergencyPlanService.searchById(planId, userInfo.getOrgCode());
				}
			}
			if(emergencyPlan!=null) {
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("planType", emergencyPlan.getPlanType());
				params.put("planLevel", bo.getPlanLevel());
				params.put("planConfigId", id);
				list=planMemberService.findPlanMemberListByParams(params);
			}
		}
		map.put("emergencyPlan", emergencyPlan);
		map.addAttribute("bo", bo);
		map.put("EMERGENC_PLAN_ROLE", ConstantValue.EMERGENC_PLAN_ROLE);
		map.put("memberList", list);
		return "/zzgl/planStaffing/detail_planConfig.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
			String id,String planId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EmergencyPlan emergencyPlan=null;
		List<PlanMember> list=null;
		if (StringUtils.isNotBlank(planId)) {
			//预案信息
			emergencyPlan=emergencyPlanService.searchById(planId, userInfo.getOrgCode());
		}
		if (StringUtils.isNotBlank(id)) {
			PlanConfig bo = planConfigService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
			if(emergencyPlan!=null) {
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("planType", emergencyPlan.getPlanType());
				params.put("planLevel", bo.getPlanLevel());
				params.put("planConfigId", id);
				list=planMemberService.findPlanMemberListByParams(params);
			}
		}
		map.put("emergencyPlan", emergencyPlan);
		map.put("EMERGENC_PLAN_ROLE", ConstantValue.EMERGENC_PLAN_ROLE);
		map.put("memberList", list);
		map.addAttribute("orgId", userInfo.getOrgId());
		map.addAttribute("planId", planId);
		return "/zzgl/planStaffing/add_planConfig.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		PlanConfig bo, @RequestParam Map<String,Object> param) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<PlanMember> invopeopleList = null;
		  if (CommonFunctions.isNotBlank(param,"planMemberArr")) {
			 invopeopleList = JSON.parseArray(param.get("planMemberArr").toString(),PlanMember.class);
		  }
		String id=bo.getPlanConfigId();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (StringUtils.isBlank(bo.getPlanConfigId())) { //新增
			bo.setCreator(userInfo.getUserId());
			bo.setUpdator(userInfo.getUserId());
			id=planConfigService.insert(bo);
			if (StringUtils.isNotBlank(id)) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = planConfigService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		
		if(StringUtils.isNotBlank(id)&&result.equals("success")) {
		
			List<PlanMember> insertList=new ArrayList<PlanMember>();
			List<PlanMember> updateList=new ArrayList<PlanMember>();
			List<String>  list=new ArrayList<String>();
			for (PlanMember planMember : invopeopleList) {
				planMember.setPlanConfigId(id);	
				planMember.setUpdator(userInfo.getUserId());
				if(StringUtils.isBlank(planMember.getPlanMemberId())) {
					planMember.setCreator(userInfo.getUserId());
					insertList.add(planMember);
				}else {
					updateList.add(planMember);
					list.add(planMember.getPlanMemberId());
				}
			}
			//删除数据
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("planConfigId", id);
			params.put("list",list);
			planMemberService.batchDelete(params);
			//新增数据
			if(insertList.size()>0)
				planMemberService.batchInsert(insertList);
			//修改数据
			if(updateList.size()>0)
				planMemberService.batchUpdate(updateList);
			
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
		PlanConfig bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = planConfigService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 根据条件查询条数
	 * @param request
	 * @param session
	 * @param map
	 * @param params   planId   panLevel
	 * regionCodeAll  完全匹配      planType：类型   excludeId:排查的id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/countList")
	public long countList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam Map<String, Object> params) {
		return planConfigService.countList(params);
	}

}