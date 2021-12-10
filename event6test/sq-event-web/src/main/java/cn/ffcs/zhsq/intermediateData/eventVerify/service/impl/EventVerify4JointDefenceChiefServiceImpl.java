package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 甘肃十户联防 联防长(JointDefenceChief)
 * @author zhangls
 *
 */
@Service("eventVerify4JointDefenceChiefService")
@Transactional
public class EventVerify4JointDefenceChiefServiceImpl extends
	EventVerify4WechatServiceImpl {
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//甘肃十户联防-联防长
	private final String JOINT_DEFENCE_CHIEF_BIZ_PLATFORM = "205";
	
	@Override
	public Long saveEventVerify(Map<String, Object> eventVerify) throws Exception {
		Long eventWechatId = -1L;
		
		if(eventVerify != null && !eventVerify.isEmpty()) {
			defaultParam(eventVerify);
			
			eventWechatId = super.saveEventVerify(eventVerify);
		}
		
		return eventWechatId;
	}
	
	@Override
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		List<String> bizPlatformList = new ArrayList<String>();
		
		defaultParam(params);
		
		
		String userOrgCode = "";
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		String bizPlatform4Query = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_VERIFY_ATTRIBUTE,"bizPlatform4Query",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		if(StringUtils.isNotBlank(bizPlatform4Query)) {
			
			params.put("bizPlatformList", Arrays.asList(bizPlatform4Query.split(",")));
			
		}else {
			bizPlatformList.add(JOINT_DEFENCE_CHIEF_BIZ_PLATFORM);
			//甘肃反馈信息至公众版APP（天阙）
			bizPlatformList.add(BizPlatfromEnum.GS_TQ.getValue());
			bizPlatformList.add("6217001");//张掖公众App对接
			bizPlatformList.add("6217002");//张掖微信公众号对接
			params.put("bizPlatformList", bizPlatformList);
		}
		
		return super.findEventVerifyPagination(pageNo, pageSize, params);
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		defaultParam(eventVerify);
		
		Map<String, Object> resultMap = super.fetchParam4Event(eventVerify);
		
		resultMap.put("isReport", false);
		resultMap.put("isShowCloseBtn", true);
		
		return resultMap;
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 */
	private void defaultParam(Map<String, Object> params) {
		String JOINT_DEFENCE_CHIEF_MODULE_CODE = "JOINT_DEFENCE_CHIEF",//地图定位类型
			   JOINT_DEFENCE_CHIEF_ATTACHMENT_TYPE = "JOINT_DEFENCE_CHIEF";//附件类型
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		if(CommonFunctions.isBlank(params, "bizPlatform")) {
			params.put("bizPlatform", JOINT_DEFENCE_CHIEF_BIZ_PLATFORM);
		}
		if(CommonFunctions.isBlank(params, "attachmentType")) {
			params.put("attachmentType", JOINT_DEFENCE_CHIEF_ATTACHMENT_TYPE);
		}
		if(CommonFunctions.isBlank(params, "markerType")) {
			params.put("markerType", JOINT_DEFENCE_CHIEF_MODULE_CODE);
		}
	}
}
