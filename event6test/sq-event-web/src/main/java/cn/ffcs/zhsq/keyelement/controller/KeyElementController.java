package cn.ffcs.zhsq.keyelement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping("/zhsq/keyelement/keyElementController")
public class KeyElementController {

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	/**
	 * 获取人员选择的组织树
	 * @param session
	 * @param request
	 * @param orgId		当前节点的id
	 * @param orgRootId 组织树起始的根节点id orgId为空时，生效
	 * @param nodeCode	办理环节节点编码
	 * @param level		组织层级
	 * @param professionCode	专业编码
	 * @param params
	 * 			eventId	事件id
	 * 			nodeId	下一办理环节节点id
	 * 			formId	表单id
	 * 			formType表单类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTreeForEvent")
	public List<GdZTreeNode> getTreeForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long orgId,
			@RequestParam(value = "orgRootId", required = false) Long orgRootId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode,
			@RequestParam(value = "level", required = false) Integer level,
			@RequestParam(value = "professionCode", required = false) String professionCode,
			@RequestParam Map<String, Object> params) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(orgId == null && orgRootId != null) {//为了使orgRootId只生效一次
			orgId = orgRootId;
			params.put("orgRootId", orgRootId);
		}
		
		return fiveKeyElementService.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
	}
	
	/**
	 * 获取组织下的人员
	 * @param session
	 * @param request
	 * @param orgId		组织id
	 * @param nodeCode	环节编码，如U6R1U5
	 * @param params
	 * 			eventId	事件id，为了兼容历史，保留该参数
	 * 			formId	表单id
	 * 			formType表单类型
	 * 			nodeId	环节id
	 * 			partyName	用户姓名，支持模糊查找
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserListByOrgId")
	public List<cn.ffcs.uam.bo.UserInfo> getUserListByOrgId(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long orgId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<cn.ffcs.uam.bo.UserInfo> userList = new ArrayList<cn.ffcs.uam.bo.UserInfo>();
		
		if (orgId == null || nodeCode == null) {//如果return null，会导致页面报如下错误：SCRIPT5007: 无法获取未定义或 null 引用的属性“length”
			return userList;
		}
		
		Map<String, Object> extraParams = null;
			
		if(params != null && !params.isEmpty()) {
			extraParams = new HashMap<String, Object>();
			
			extraParams.putAll(params);
		}
		
		try {
			//为了让页面中能将上次加载的人员清空
			userList = fiveKeyElementService.getUserInfoList(orgId, nodeCode, userInfo.getUserId(), extraParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	/**
	 * 选择办理环节后，获取办理人员信息
	 * @param session
	 * @param request
	 * @param curnodeName	当前节点名称
	 * @param nodeName		选中的环节名称
	 * @param nodeCode		环节编码，如U6R1U5
	 * @param nodeId		选中的节点id
	 * @param eventId		事件id
	 * @param params
	 * 			formId		表单id
	 * 			formType	表单类型
	 * 			instanceId	流程实例id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getNodeInfoForEvent")
	public Map<String, Object> getNodeInfoForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName,
			@RequestParam(value = "nodeCode") String nodeCode,
			@RequestParam(value = "nodeId") String nodeId,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam Map<String, Object> params) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orgCode = userInfo.getOrgCode(),
			   workflowName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.WORKFLOW_NAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode),
			   specificUrl = "",
			   specificUrlName = "",
			   eventType = "";
		
		params.put("eventId", eventId);
		
		try {
			//详情页获取处置流程等信息
			resultMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		} catch(Exception e) {
			resultMap.put("msg", e.getMessage());
		}
		
		if(eventId != null && eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(event != null) {
				eventType = event.getType();
			}
		}
		
		specificUrl = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, workflowName+"_"+nodeName+"_specificUrl_"+eventType, IFunConfigurationService.CFG_TYPE_URL, orgCode, IFunConfigurationService.DIRECTION_UP_FUZZY);
		specificUrlName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, workflowName+"_"+nodeName+"_specificUrlName_"+eventType, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.DIRECTION_UP_FUZZY);
		
		if(StringUtils.isNotBlank(specificUrl)) {
			specificUrl = specificUrl.replace("$EVENT_DOMAIN", App.EVENT.getDomain(session));
			
			resultMap.put("specificUrl", specificUrl);
			resultMap.put("specificUrlName", specificUrlName);
		}
		
		return resultMap;
	}
}
