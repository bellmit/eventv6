package cn.ffcs.zhsq.eventExpand.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省平台个性化事件拓展处理类
 * @ClassName:   EventDisposalExpand4JXPlatformServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年9月3日 上午8:48:46
 */
@Service(value = "eventDisposalExpand4JXPlatformService")
public class EventDisposalExpand4JXPlatformServiceImpl extends EventDisposalExpandBaseServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public EventDisposal init4Event(EventDisposal event, UserInfo userInfo, Map<String, Object> params) throws Exception {
		EventDisposal eventTmp = null;
		String orgChiefLevel = null, orgType = null, defaultSource = null;
		
		if(CommonFunctions.isNotBlank(params, "orgType")) {
			orgType = params.get("orgType").toString();
		}
		if(CommonFunctions.isNotBlank(params, "orgChiefLevel")) {
			orgChiefLevel = params.get("orgChiefLevel").toString();
		}
		
		if(CommonFunctions.isBlank(params, "orgType") || CommonFunctions.isBlank(params, "orgChiefLevel")) {
			Long userOrgId = userInfo.getOrgId();
			OrgSocialInfoBO orgInfo = null;
			
			if(userOrgId == null || userOrgId < 0) {
				if(CommonFunctions.isNotBlank(params, "userOrgId")) {
					try {
						userOrgId = Long.valueOf(params.get("userOrgId").toString());
					} catch(NumberFormatException e) {
						throw new IllegalArgumentException("组织id【userOrgId】：【" + params.get("userOrgId") + "】不是有效的数值！");
					}
				}
			}
			
			if(userOrgId == null || userOrgId < 0) {
				throw new IllegalArgumentException("缺少有效的组织id【userOrgId】！");
			}
			
			orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
			
			if(orgInfo != null) {
				if(StringUtils.isBlank(orgChiefLevel)) {
					orgChiefLevel = orgInfo.getChiefLevel();
				}
				
				if(StringUtils.isBlank(orgType)) {
					orgType = orgInfo.getOrgType();
				}
			} else {
				throw new IllegalArgumentException("组织id【userOrgId】：【" + userOrgId + "】未能找到有效的组织信息！");
			}
		}
		
		if(StringUtils.isBlank(orgChiefLevel)) {
			throw new IllegalArgumentException("缺少组织层级信息【orgChiefLevel】！");
		}
		
		if(StringUtils.isBlank(orgType)) {
			throw new IllegalArgumentException("缺少组织机构类型信息【orgType】！");
		}
		
		if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)) {
			defaultSource = "01";//网格员上报
		} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
			defaultSource = "03";//横向部门上报
		} else {
			defaultSource = "04";//综治中心上报
		}
		
		eventTmp = super.init4Event(event, userInfo, params);
		
		eventTmp.setSource(defaultSource);
		
		return eventTmp;
	}
}
