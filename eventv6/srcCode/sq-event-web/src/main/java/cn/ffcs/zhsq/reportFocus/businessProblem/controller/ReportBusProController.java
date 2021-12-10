package cn.ffcs.zhsq.reportFocus.businessProblem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.BusProDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.FocusReportNode359Enum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 营商问题
 * @ClassName:   ReportBusProController   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Controller
@RequestMapping("/zhsq/reportBusPro")
public class ReportBusProController extends ReportTwoVioPreController {
	@Autowired
	private PositionInfoOutService positionInfoOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	/**
	 * 跳转营商问题列表
	 * @param session
	 * @param listType		列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param reportType	上报类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="10") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/businessProblem/list_bp.ftl";
	}
	
	/**
	 * 跳转营商问题阅办列表
	 * @param session
	 * @param listType		列表类型
	 * @param reportType	上报类型
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toMsgReadingList")
	public String toMsgReadingList(HttpSession session, 
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="10") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/businessProblem/list_bp_msgReading.ftl";
	}
	
	/**
	 * 跳转营商问题新增/编辑页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "reportUUID", required=false) String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="10") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userTel", userInfo.getVerifyMobile());
		return "/zzgl/reportFocus/businessProblem/add_bp.ftl";
	}
	
	/**
	 * 跳转营商问题详情页面
	 * @param session
	 * @param reportUUID	上报UUID
	 * @param reportType	上报类型
	 * @param params		额外参数
	 * 			instanceId	流程实例id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "reportUUID") String reportUUID,
			@RequestParam(value = "listType") Integer listType,
			@RequestParam(value = "reportType", required=false, defaultValue="10") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardUrl = "/zzgl/reportFocus/businessProblem/detail_bp.ftl";
		
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		map.addAttribute("formType", FocusReportNode359Enum.FORM_TYPE.toString());
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(userInfo!=null) {
			map.addAttribute("orgScopeName",userInfo.getOrgName());
		}
		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			forwardUrl = "/zzgl/reportFocus/businessProblem/detail_bp_editable.ftl";
		}
		
		return forwardUrl;
	}
	
	@RequestMapping(value = "/toSelectPerson")
	public String toSelectPerson(HttpSession session, @RequestParam Map<String, Object> params,
			@RequestParam(value = "selectUserIds") String selectUserIds,
			@RequestParam(value = "selectOrgIds") String selectOrgIds,
			@RequestParam(value = "selectUserNames") String selectUserNames,
			@RequestParam(value = "selectOrgNames") String selectOrgNames,
			@RequestParam(value = "regionCode") String regionCode,
			ModelMap map) {
		
		map.addAttribute("userIds", selectUserIds);
		map.addAttribute("curOrgIds", selectOrgIds);
		map.addAttribute("userNames", selectUserNames);
		map.addAttribute("curOrgNames", selectOrgNames);
		
		map.addAttribute("regionCode", regionCode);
		
		return "/zzgl/reportFocus/businessProblem/toSelectPerson.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTreeForEvent")
	public List<GdZTreeNode> getTreeForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long orgId,
			@RequestParam(value = "regionCode") String regionCode,
			@RequestParam Map<String, Object> params) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		return getTreeForEvent(userInfo, orgId, regionCode, params);
	}
	
	private List<GdZTreeNode> getTreeForEvent(UserInfo userInfo, Long orgId, String regionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		List<OrgSocialInfoBO> socialInfoBOs = null;
		
		if(orgId == null || orgId < 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode("350583");
			if(orgInfo != null && orgInfo.getOrgId() != null) {
				socialInfoBOs = new ArrayList<OrgSocialInfoBO>();
				List<OrgSocialInfoBO> orgList = orgSocialInfoService.findOrgSocialListByParentId(orgInfo.getOrgId());
				for(OrgSocialInfoBO org : orgList) {
					if("0".equals(org.getOrgType())||("1".equals(org.getOrgType())&&regionCode.startsWith(org.getOrgCode()))) {
						socialInfoBOs.add(org);
					}
				}
			}
		}else {
			socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
		}
		
		for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
			GdZTreeNode gdZTreeNode = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf());
			if(regionCode.startsWith(gdZTreeNode.getOrgCode())) {
				gdZTreeNode.setIsParent(false);
			}
			nodes.add(gdZTreeNode);
		}
		
		return nodes;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getUserListByOrgId")
	public List<cn.ffcs.uam.bo.UserInfo> getUserListByOrgId(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long orgId,
			@RequestParam(value = "regionCode") String regionCode,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<cn.ffcs.uam.bo.UserInfo> userList = new ArrayList<cn.ffcs.uam.bo.UserInfo>();
		
		if (orgId == null) {//如果return null，会导致页面报如下错误：SCRIPT5007: 无法获取未定义或 null 引用的属性“length”
			return userList;
		}
		
		Map<String, Object> extraParams = null;
		if(params != null && !params.isEmpty()) {
			extraParams = new HashMap<String, Object>();
			extraParams.putAll(params);
		}
		
		try {
			//为了让页面中能将上次加载的人员清空
			userList = getUserInfoList(orgId, regionCode, userInfo.getUserId(), extraParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	private List<UserInfo> getUserInfoList(Long orgId, String regionCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		List<UserInfo> users = new ArrayList<UserInfo>();//获取组织下的所有用户
		if (regionCode != null) {
			List<UserBO> userBOList = new ArrayList<UserBO>();
			Map<Long, UserBO> userBOMap = new HashMap<Long, UserBO>();
			String userOrgName = "";
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				userOrgName = orgInfo.getOrgName();
				if(regionCode.startsWith(orgInfo.getOrgCode())) {
					//乡镇主要领导：xzzyld
					PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("xzzyld");
					userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
				}else {
					//市直部门主要领导：szbmzyld
					PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("szbmzyld");
					if(positionInfoBO!=null) {
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
					}
					//市直部门分管领导(营商环境)：szbmfgldyshj
					positionInfoBO = positionInfoOutService.queryPositionByCode("szbmfgldyshj");
					if(positionInfoBO!=null) {
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
					}
					//市督导组：swb
					positionInfoBO = positionInfoOutService.queryPositionByCode("swb");
					if(positionInfoBO!=null) {
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
					}
				}
				
				if(userBOList != null && userBOList.size() > 0) {
					for(UserBO userBO : userBOList) {
						if(!userBOMap.containsKey(userBO.getUserId())) {
							userBOMap.put(userBO.getUserId(), userBO);
						}
					}
					userBOList.clear();//清除原有的人员信息
					
					if(!userBOMap.isEmpty()) {
						for(Long userId : userBOMap.keySet()) {
							userBOList.add(userBOMap.get(userId));
						}
					}
				}
			} 
			if(!userBOList.isEmpty()) {
				UserInfo userInfoTmp = null;
				for(UserBO userBO : userBOList) {
					userInfoTmp = new UserInfo();
					BeanUtils.copyProperties(userBO, userInfoTmp);
					userInfoTmp.setOrgId(orgId);
					userInfoTmp.setOrgName(userOrgName);
					userInfoTmp.setUserName(userBO.getRegisValue());
					
					users.add(userInfoTmp);
				}
			}
		} else {
			throw new Exception("区域编码不能为空！");
		}
		
		return users;
	}
	
	@Override
	protected boolean isFrom12345(Object dataSource) {
		return BusProDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.BUSINESS_PROBLEM.getCode();
	}
}
