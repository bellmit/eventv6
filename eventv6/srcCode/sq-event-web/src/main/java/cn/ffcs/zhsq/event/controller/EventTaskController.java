package cn.ffcs.zhsq.event.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件任务转派
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventTask")
public class EventTaskController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String DEPT_ORG_TYPE = "0";//部门组织类型
	private static final String COLLECT_NODE_CODE = "CJ";//事件采集环节节点编码
	private static final String CONFIRM_NODE_CODE = "QR";//确认环节节点编码
	
	/**
	 * 跳转事件转派列表
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListEventTask")
	public String toListEventTask(HttpSession session, 
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long curOrgId = userInfo.getOrgId(),
			 curUserId = userInfo.getUserId();
		
		//构造nodeCode
		map.addAttribute("nodeCode", "__"+INodeCodeHandler.OPERATE_GLOBAL+"___");
		
		//获取转派按钮权限
		String userNameOrgCodes = configurationService.turnCodeToValue(ConstantValue.IS_TRANSFER_ABLE, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		boolean isTransferAble = false;
		
		if(StringUtils.isNotBlank(userNameOrgCodes)) {
			String[] userNameOrgCodeArray = userNameOrgCodes.split(";"),//分割各用户id和组织id组合
					 nameCodeArray = new String[]{},
			 		 regisValueArray = new String[]{};
			String regisValues = "",
				   userOrgCode = "",
				   userNameOrgCode = "";
			OrgSocialInfoBO orgSocialInfo = null;
			UserBO userBo = null;
			
			for(int index = 0, len = userNameOrgCodeArray.length; index < len && !isTransferAble; index++) {
				userNameOrgCode = userNameOrgCodeArray[index];
				
				if(StringUtils.isNotBlank(userNameOrgCode)) {
					nameCodeArray = userNameOrgCode.split(":");//分割用户id和组织id
					if(nameCodeArray.length == 2) {
						userOrgCode = nameCodeArray[0];
						regisValues = nameCodeArray[1];
						
						orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
						if(orgSocialInfo != null && orgSocialInfo.getOrgId() != null) {
							if(StringUtils.isNotBlank(regisValues)) {
								regisValueArray = regisValues.split(",");
								for(String regisValue : regisValueArray) {
									if(StringUtils.isNotBlank(regisValue)) {
										userBo = userManageService.getUserInfoByRegistValue(regisValue);
										if(userBo != null) {
											isTransferAble = curUserId.equals(userBo.getUserId()) && curOrgId.equals(orgSocialInfo.getOrgId());
											if(isTransferAble) {
												break;
											}
										} else {
											logger.error("用户名：" + regisValue + " 没有对应的用户！");
										}
									}
								}
							}
						}
					} else {
						logger.error(userNameOrgCode + " 格式不正确！正确格式为：orgCode:regisValue;。");
					}
				}
			} 
		}
		
		map.addAttribute("isTransferAble", isTransferAble);
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		
		return "/zzgl/event/eventTask/list_event_task.ftl";
	}
	
	/**
	 * 加载转派事件列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param event
	 * @param curUserIds 当前任务办理人员，以英文逗号分隔
	 * @param curOrgIds 当前任务办理人员组织，以英文逗号分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventTaskData", method = RequestMethod.POST)
	public EUDGPagination listEventTaskData(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "curUserIds", required = false) String curUserIds,
			@RequestParam(value = "curOrgIds", required = false) String curOrgIds) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		String infoOrgCode = event.getInfoOrgCode();
		String eventName = event.getEventName(),
			   code = event.getCode();
		
		params.put("userId", userInfo.getUserId());
		if(StringUtils.isNotBlank(infoOrgCode)) {
			params.put("infoOrgCode", infoOrgCode);
		}
		if(StringUtils.isNotBlank(eventName)) {
			params.put("eventName", eventName);
		}
		if(StringUtils.isNotBlank(code)) {
			params.put("code", code);
		}
		if(StringUtils.isNotBlank(curUserIds)) {
			params.put("curUserId", curUserIds);
		}
		if(StringUtils.isNotBlank(curOrgIds)) {
			params.put("curOrgId", curOrgIds);
		}
		
		return eventDisposalService.findEventTaskMapPagination(page, rows, params);
	}
	
	/**
	 * 转派事件任务
	 * @param session
	 * @param params
	 * @param map
	 * 		taskId		当前taskId
	 * 		instanceId	流程实例id
	 * 		userIds		转派人员id串
	 * 		userNames	转派人员name串
	 * 		orgIds		转派人员组织id串
	 * 		orgNames	转派人员组织name串
	 * 		userId		当前办理人
	 * 		userName	当前办理人名称
	 * 		remarks		备注
	 * @return
	 * 		result：true转派成功；false转派失败
	 * 		msg：result为false时，为异常信息
	 */
	@ResponseBody
	@RequestMapping(value = "/transferTask", method = RequestMethod.POST)
	public Map<String, Object> transferTask(HttpSession session,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		params.put("userId", userInfo.getUserId());
		params.put("userName", userInfo.getPartyName());
		
		resultMap = eventDisposalWorkflowService.turnTodoPerson(params);
		
		return resultMap;
	}
	
	/**
	 * 构建当前任务对于的nodeCode
	 * @param session
	 * @param params
	 * @param instanceId 流程实例id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/transferNodeCode", method = RequestMethod.POST)
	public Map<String, Object> transferNodeCode(HttpSession session,
			@RequestParam Map<String, Object> params,
			@RequestParam(value = "instanceId") Long instanceId,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> curNodeData = eventDisposalWorkflowService.curNodeData(instanceId);
		
		if(CommonFunctions.isNotBlank(curNodeData, "NODE_ID")) {
			resultMap.put("nodeId", curNodeData.get("NODE_ID"));
		}
		if(CommonFunctions.isNotBlank(curNodeData, "NODE_CODE")) {
			String nodeCode = "",
				   toNodeCode = curNodeData.get("NODE_CODE").toString().trim();
			
			if(COLLECT_NODE_CODE.equals(toNodeCode) || CONFIRM_NODE_CODE.equals(toNodeCode)) {//事件采集
				
				if(CommonFunctions.isNotBlank(curNodeData, "WF_ORGID")) {
					Long orgId = -1L;
					String toOrgType = INodeCodeHandler.ORG_UINT;
					
					try {
						orgId = Long.valueOf(curNodeData.get("WF_ORGID").toString());
					} catch(NumberFormatException e) {
						orgId = -1L;
					}
					
					if(orgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						if(orgInfo != null) {
							boolean isNewGov = eventOrgInfoService.isNewOrganization(orgId);
							String desOrgType = orgInfo.getOrgType(),
								   toOrgLevel = orgInfo.getChiefLevel();
							
							if((!isNewGov && DEPT_ORG_TYPE.equals(desOrgType)) || (isNewGov && !eventOrgInfoService.isGovernment(orgInfo))) {
								toOrgType = INodeCodeHandler.ORG_DEPT;
								
								if(!isNewGov) {//旧组织部门的组织层级替换为对应的地域层级
									OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.findByOrgId(orgInfo.getLocationOrgId());
									if(orgEntityInfo != null && StringUtils.isNotBlank(orgEntityInfo.getChiefLevel())) {
										toOrgLevel = orgEntityInfo.getChiefLevel();
									}
								}
							}
							
							if(StringUtils.isNotBlank(toOrgLevel)) {
								toNodeCode = toOrgType + toOrgLevel;
							}
						}
					}
				}
			}
			
			if(toNodeCode.length() == 2) {
				String toOrgType = toNodeCode.substring(0, 1),
					   toOrgLevel = toNodeCode.substring(1);
				int toLevel = 0;
				
				if(INodeCodeHandler.ORG_UINT.equals(toOrgType) || INodeCodeHandler.ORG_DEPT.equals(toOrgType)) {
					try {
						toLevel = Integer.valueOf(toOrgLevel);
					} catch(NumberFormatException e) {
						toLevel = 0;
					}
				} else {//默认将目标设置为U7
					toOrgType = INodeCodeHandler.ORG_UINT;
					toLevel = 7;
				}
				
				if(toLevel > 0) {
					UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
					
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
					if(orgInfo != null) {
						boolean isNewGov = eventOrgInfoService.isNewOrganization(userInfo.getOrgId());
						String chiefLevel = orgInfo.getChiefLevel(),
								   orgType = orgInfo.getOrgType(),
								   fromOrgType = INodeCodeHandler.ORG_UINT,
								   operateCode = INodeCodeHandler.OPERATE_SEND;//操作为下派
						
						if(StringUtils.isNotBlank(chiefLevel)) {
							int fromLevel = 0, levelGap = 0;
							
							if((!isNewGov && DEPT_ORG_TYPE.equals(orgType)) || (isNewGov && !eventOrgInfoService.isGovernment(orgInfo))) {
								
								fromOrgType = INodeCodeHandler.ORG_DEPT;
								
								if(!isNewGov) {//旧组织部门的组织层级替换为对应的地域层级
									OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.findByOrgId(orgInfo.getLocationOrgId());
									if(orgEntityInfo != null && StringUtils.isNotBlank(orgEntityInfo.getChiefLevel())) {
										chiefLevel = orgEntityInfo.getChiefLevel();
									}
								}
							}
							
							try {
								fromLevel = Integer.valueOf(chiefLevel);
							} catch(NumberFormatException e) {
								fromLevel = 0;
							}
							
							if(fromLevel > 0) {
								levelGap = fromLevel - toLevel;
								
								if(levelGap < 0) {
									levelGap = -levelGap;
								} else if(levelGap > 0 || toOrgType.equals(fromOrgType)) {
									operateCode = INodeCodeHandler.OPERATE_REPORT;
								} else if(levelGap == 0) {
									operateCode = INodeCodeHandler.OPERATE_FLOW;
								} 
								
								nodeCode = fromOrgType + fromLevel + operateCode + levelGap + toOrgType + toLevel;
							}
						}
					}
				}
			}
			
			resultMap.put("nodeCode", nodeCode);
		}
		
		return resultMap;
	}
	
}
