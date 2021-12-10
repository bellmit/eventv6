package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.intermediateData.eventWechat.service.impl.EventWechat4JXServiceImpl;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 南平市(NPS) 随手拍(Pat) 事件审核处理类
 * @author zhangls
 *
 */
@Service("eventWechat4NPSPatService")
@Transactional
public class EventWechat4NPSPatServiceImpl extends EventWechat4JXServiceImpl {
	@Resource(name = "eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private PositionInfoOutService positionInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Override
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		if(CommonFunctions.isBlank(params, "bizPlatformList")) {
			List<String> bizPlatformList = new ArrayList<String>();
			
			bizPlatformList.add("204001");//南平市微信随手拍
			bizPlatformList.add("204002");//南平市APP随手拍
			bizPlatformList.add("204003");//智慧延平对接随手拍
			
			params.put("bizPlatformList", bizPlatformList);
		}
		
		return super.findEventVerifyPagination(pageNo, pageSize, params);
	}
	
	@Override
	public int findEventVerifyCount(Map<String, Object> params) {
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		if(CommonFunctions.isBlank(params, "bizPlatformList")) {
			List<String> bizPlatformList = new ArrayList<String>();
			
			bizPlatformList.add("204001");//南平市微信随手拍
			bizPlatformList.add("204002");//南平市APP随手拍
			bizPlatformList.add("204003");//智慧延平对接随手拍
			
			params.put("bizPlatformList", bizPlatformList);
		}
		
		return super.findEventVerifyCount(params);
	}
	
	@Override
	public Long saveEventVerify(Map<String, Object> eventVerify) throws Exception {
		Long eventVerifyId = null;
		boolean isSendSMS = CommonFunctions.isBlank(eventVerify, "eventVerifyId");
		
		eventVerifyId = super.saveEventVerify(eventVerify);
		
		//防止编辑操作时，二次发送短信，因为更新方法updateEventVerifyById会调用saveEventVerify
		if(isSendSMS && eventVerifyId != null && eventVerifyId > 0) {
			this.sendSms(eventVerifyId, eventVerify, null);
		}
		
		return eventVerifyId;
	}
	
	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception {
		boolean isForce2Update = false,
				flag = false;
		String beforeStatus = null, afterStatus = null,
			   DEFAULT_VALID_STATUS = "01",//默认状态 待审核
			   REJECT_STATUS = "03";//驳回状态
		Long eventVerifyId = null;
		
		if(CommonFunctions.isNotBlank(eventVerify, "isForce2Update")) {
			isForce2Update = Boolean.valueOf(eventVerify.get("isForce2Update").toString());
		}
		if(CommonFunctions.isNotBlank(eventVerify, "status")) {
			afterStatus = eventVerify.get("status").toString();
		}
		
		if(isForce2Update && StringUtils.isNotBlank(afterStatus)) {
			if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
				try {
					eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(eventVerifyId != null && eventVerifyId > 0) {
				Map<String, Object> eventVerifyMap = this.findEventVerifyById(eventVerifyId, null);
				
				if(CommonFunctions.isNotBlank(eventVerifyMap, "status")) {
					beforeStatus = eventVerifyMap.get("status").toString();
				}
			}
		}
		
		//不回调手机端接口
		if(CommonFunctions.isNotBlank(eventVerify, "operateType")) {
			eventVerify.remove("operateType");
		}
		
		flag = super.updateEventVerifyById(eventVerify, userInfo);
		
		if(flag && isForce2Update && REJECT_STATUS.equals(beforeStatus) && DEFAULT_VALID_STATUS.equals(afterStatus)) {
			if(eventVerifyId != null && eventVerifyId > 0) {
				this.sendSms(eventVerifyId, eventVerify, userInfo);
			}
		}
		
		return flag;
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		//南平市随手拍不是以第三方方式进行对接的，故不生成第三方关联记录(REPORT_EVENT_RECORD)
		resultMap.remove("moduleCode");
		
		return resultMap;
	}
	
	/**
	 * 短信发送
	 * @param eventVerifyId	审核记录id
	 * @param params
	 * 			userOrgCode	组织编码
	 * @param userInfo
	 */
	private void sendSms(Long eventVerifyId, Map<String, Object> params, UserInfo userInfo) {
		if(eventVerifyId != null && eventVerifyId > 0) {
			Map<String, Object> eventVerifyMap = this.findEventVerifyById(eventVerifyId, null);
			String SMS_CONTENT = "【随手拍】您一条事件待审核，标题：@eventName，请及时登录随手拍事件平台处理，谢谢！",
				   smsContent = "",
				   infoOrgCode = null,
				   userOrgCode = null,
				   verifyOrgCode = null;//审核记录相关的组织编码
			Long userOrgId = null,
				 verifyOrgId = null;	//审核记录相关的组织id
			OrgSocialInfoBO orgInfo = null;
			List<UserBO> userBOList = null;
			
			if(userInfo != null) {
				userOrgCode = userInfo.getOrgCode();
				userOrgId = userInfo.getOrgId();
			} else {
				userInfo = new UserInfo();
			}
			
			if(CommonFunctions.isNotBlank(eventVerifyMap, "eventName")) {
				String eventName = eventVerifyMap.get("eventName").toString();
				
				smsContent = SMS_CONTENT.replace("@eventName", eventName);
			}
			
			if(StringUtils.isBlank(userOrgCode)) {
				if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
					userOrgCode = params.get("userOrgCode").toString();
				} else {
					if(CommonFunctions.isNotBlank(eventVerifyMap, "infoOrgCode")) {
						infoOrgCode = eventVerifyMap.get("infoOrgCode").toString();
					} else if(CommonFunctions.isNotBlank(eventVerifyMap, "eventInfoOrgCode")) {
						infoOrgCode = eventVerifyMap.get("eventInfoOrgCode").toString();
					}
				}
			}
			
			if(userOrgId != null) {
				orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
			} else if(StringUtils.isNotBlank(userOrgCode)) {
				orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
			} else if(StringUtils.isNotBlank(infoOrgCode)) {
				orgInfo = orgSocialInfoService.getOrgIdByLocationCode(infoOrgCode);
			}
			
			if(orgInfo != null) {
				userOrgId = orgInfo.getOrgId();
				userInfo.setOrgCode(orgInfo.getOrgCode());
			}
			
			if(userOrgId != null && userOrgId > 0) {
				String nodeCode = null;
				List<GdZTreeNode> orgZTreeList = null;
				
				if(CommonFunctions.isNotBlank(eventVerifyMap, "infoOrgCode")) {
					OrgSocialInfoBO verifyOrgInfo = null;
					String eventVerifyInfoOrgCode = eventVerifyMap.get("infoOrgCode").toString();
					
					verifyOrgInfo = orgSocialInfoService.getOrgIdByLocationCode(eventVerifyInfoOrgCode);
					
					if(verifyOrgInfo != null) {
						String sourceLevelStr = verifyOrgInfo.getChiefLevel(),
							   sourceOrgType = null,
							   operateType = null,
							   TARGET_ORG_TYPE = INodeCodeHandler.ORG_DEPT;
						Integer sourceLevel = 0, TARGET_LEVEL = 3, intervalLevel = null;
						
						verifyOrgCode = verifyOrgInfo.getOrgCode();
						verifyOrgId = verifyOrgInfo.getOrgId();
						
						if(StringUtils.isNotBlank(sourceLevelStr)) {
							try {
								sourceLevel = Integer.valueOf(sourceLevelStr);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						if(sourceLevel > 0) {
							intervalLevel = sourceLevel - TARGET_LEVEL;
							
							if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(verifyOrgInfo.getOrgType())) {
								sourceOrgType = INodeCodeHandler.ORG_UINT;
							}
							
							if(intervalLevel > 0) {//上报
								operateType = INodeCodeHandler.OPERATE_REPORT;
							} else if(intervalLevel == 0) {//分流
								operateType = INodeCodeHandler.OPERATE_FLOW;
							}
							
							if(operateType != null) {
								nodeCode = sourceOrgType + sourceLevel + operateType + Math.abs(intervalLevel) + TARGET_ORG_TYPE + TARGET_LEVEL;
							}
						}
					}
				}
				
				if(StringUtils.isNotBlank(nodeCode)) {
					UserInfo verifyUserInfo = new UserInfo();
					
					verifyUserInfo.setOrgCode(verifyOrgCode);
					verifyUserInfo.setOrgId(verifyOrgId);
					
					try {
						orgZTreeList = fiveKeyElementService.getTreeForEvent(verifyUserInfo, verifyOrgId, nodeCode, null, null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(orgZTreeList != null && orgZTreeList.size() > 0) {
					Long ztreeOrgId = null;
					String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE",
						   EVENT_VERIFY_POSITIONNAME_SMS = "eventVerifyPositionName4SMS",//短信发送职位名称
						   EVENT_VERIFY_PROFESSIONCODE_SMS = "eventVerifyProfessionCode4SMS",//短信发送组织专业编码
						   professionCodeStr = null,
						   positionNameStr = null,
						   ztreeProfessionCode = null;
					String[] professionCodeArray = null;
					Map<String, Object> positionParam = new HashMap<String, Object>();
					List<PositionInfoBO> positionInfoList = null;
					Set<Long> positionIdSet = new HashSet<Long>();
					boolean isOrgRemained = false;//是否保留组织
					
					userBOList = new ArrayList<UserBO>();
					
					positionNameStr = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, EVENT_VERIFY_POSITIONNAME_SMS, IFunConfigurationService.CFG_TYPE_FACT_VAL, verifyOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
					
					if(StringUtils.isNotBlank(positionNameStr)) {
						String[] positionNameArray = positionNameStr.split(",");
						positionInfoList = new ArrayList<PositionInfoBO>();
						
						for(String positionName : positionNameArray) {
							positionParam.put("positionName", positionName);
							positionInfoList.addAll(positionInfoService.queryPositionByPara(positionParam));
						}
						
						if(positionInfoList != null) {
							for(PositionInfoBO position : positionInfoList) {
								positionIdSet.add(position.getPositionId());
							}
						}
					}
					
					//只有配置了接收短信的职位，才发送短信
					if(positionIdSet != null && !positionIdSet.isEmpty()) {
						professionCodeStr = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, EVENT_VERIFY_PROFESSIONCODE_SMS, IFunConfigurationService.CFG_TYPE_FACT_VAL, verifyOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
						
						if(StringUtils.isNotBlank(professionCodeStr)) {
							professionCodeArray = professionCodeStr.split(",");
						}
						
						for(GdZTreeNode ztreeNode : orgZTreeList) {
							ztreeProfessionCode = ztreeNode.getProfessionCode();
							isOrgRemained = false;
							
							if(professionCodeArray == null || professionCodeArray.length == 0) {
								isOrgRemained = true;
							} else {
								for(String professionCode : professionCodeArray) {
									if(professionCode.equals(ztreeProfessionCode)) {
										isOrgRemained = true; break;
									}
								}
							}
							
							if(isOrgRemained) {
								ztreeOrgId = Long.valueOf(ztreeNode.getId());
								
								for(Long positionId : positionIdSet) {
									userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionId, ztreeOrgId));
								}
							}
						}
					}
				}
			}
			
			if(userBOList != null && userBOList.size() > 0) {
				StringBuffer userIds = new StringBuffer(""),
							 phoneNums = new StringBuffer("");
				Long userId = null, verifyMobile = null;
				
				for(UserBO user : userBOList) {
					userId = user.getUserId();
					verifyMobile = user.getVerifyMobile();
					
					if(userId != null && userId > 0 && verifyMobile != null && verifyMobile > 0) {
						userIds.append(",").append(userId);
						phoneNums.append(",").append(verifyMobile);
					}
				}
				
				if(userIds.length() > 0) {
					eventDisposalWorkflowService.sendSms(userIds.substring(1), phoneNums.substring(1), smsContent, userInfo);
				}
			}
		}
	}
}
