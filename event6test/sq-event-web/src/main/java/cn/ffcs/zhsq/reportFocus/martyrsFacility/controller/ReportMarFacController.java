package cn.ffcs.zhsq.reportFocus.martyrsFacility.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.zzgl.service.grid.IPlaceInfoService;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.resident.bo.CiRs;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.BusProDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.martyrsFacility.impl.FocusReportNode363Enum;
import cn.ffcs.zhsq.reportFocus.martyrsFacility.impl.MarFacDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 烈士纪念设施
 * @ClassName:   ReportMarFacController   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Controller
@RequestMapping("/zhsq/reportMarFac")
public class ReportMarFacController extends ReportTwoVioPreController {

	Logger logger = LoggerFactory.getLogger(ReportMarFacController.class);

	@Autowired
	private PositionInfoOutService positionInfoOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	@Autowired
	private IPlaceInfoService placeInfoService;
	/**
	 * 跳转信访列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="13") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/martyrsFacility/list_mf.ftl";
	}
	
	/**
	 * 跳转信访阅办列表
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
			@RequestParam(value = "reportType", required=false, defaultValue="13") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		this.toList(session, listType, reportType, params, map);
		
		return "/zzgl/reportFocus/martyrsFacility/list_mf_msgReading.ftl";
	}
	
	/**
	 * 跳转信访新增/编辑页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="13") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		super.toAdd(session, reportUUID, listType, reportType, params, map);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userTel", userInfo.getVerifyMobile());
		map.addAttribute("ZZGL_DOMAIN", App.ZZGRID.getDomain(session));
		map.addAttribute("orgCode", userInfo.getInfoOrgList().get(0).getOrgCode());
		return "/zzgl/reportFocus/martyrsFacility/add_mf.ftl";
	}
	
	/**
	 * 跳转信访详情页面
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
			@RequestParam(value = "reportType", required=false, defaultValue="13") String reportType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardUrl = "/zzgl/reportFocus/martyrsFacility/detail_mf.ftl";
		params.put("isCapResMarkerInfo", true);
		super.toDetail(session, reportUUID, listType, reportType, params, map);
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String startDivisionCode = "-1";
		//获取当前登录账号下所管理的所有网格
		List<OrgEntityInfoBO> orgEntityInfoList = userInfo.getInfoOrgList();
		if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
			StringBuffer orgCodeSb = new StringBuffer("");
			String infoOrgCode = null;
			for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
				infoOrgCode = orgEntityInfo.getOrgCode();
				if(StringUtils.isNotBlank(infoOrgCode)) {
					orgCodeSb.append(",").append(infoOrgCode);
				}
			}
			if(orgCodeSb.length() > 0) {
				startDivisionCode = orgCodeSb.substring(1);//标准地址库控件起始网格
			}
		}
		
		map.addAttribute("startDivisionCode", startDivisionCode);
		map.addAttribute("formType", FocusReportNode363Enum.FORM_TYPE.toString());
		map.addAttribute("ZZGL_DOMAIN", App.ZZGRID.getDomain(session));
		map.addAttribute("orgCode", userInfo.getInfoOrgList().get(0).getOrgCode());

		if(CommonFunctions.isNotBlank(map,"isEditable") && Boolean.valueOf(String.valueOf(map.get("isEditable")))){
			forwardUrl = "/zzgl/reportFocus/martyrsFacility/detail_mf_editable.ftl";
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
		
		return "/zzgl/reportFocus/martyrsFacility/toSelectPerson.ftl";
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
					if("0".equals(org.getOrgType())) {
						socialInfoBOs.add(org);
					}
				}
			}
		}else {
			socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
		}
		
		for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
			GdZTreeNode gdZTreeNode = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf());
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
				//市直部门主要领导：szbmzyld
				PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("szbmzyld");
				if(positionInfoBO!=null) {
					userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
				}
				//市直部门分管领导(信访)：szbmfgldxf
				positionInfoBO = positionInfoOutService.queryPositionByCode("szbmfgldxf");
				if(positionInfoBO!=null) {
					userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), orgId));
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
		return MarFacDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
	}

	@Override
	protected String getBizType() {
		return IReportFeedbackService.BizType.MARTYRS_FACILITY.getCode();
	}

	/**
	 * 根据设施id查询
	 * @param session
	 * @param map
	 * @param plaId
	 * @return
	 */
	@RequestMapping(value = "/findPlaceInfoById")
	@ResponseBody
	public ResultObj findPlaceInfoById(HttpSession session, ModelMap map, @RequestParam(name = "plaId", required = true) Long plaId) {
		ResultObj resultObj = new ResultObj("", null);
		try {
			PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
			resultObj.setData(placeInfo);
		} catch (Exception e) {
			logger.error("/zhsq/reportMarFac/findPlaceInfoById", e.getMessage());
			resultObj.setTipMsg("查询设施失败:" + e.getMessage());
			resultObj.setSuccess(false);
		}
		return resultObj;
	}
}
