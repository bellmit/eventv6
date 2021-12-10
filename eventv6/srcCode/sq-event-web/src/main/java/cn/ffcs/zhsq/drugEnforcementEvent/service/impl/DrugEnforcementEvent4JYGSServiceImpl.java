package cn.ffcs.zhsq.drugEnforcementEvent.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 嘉峪关市(JYGS) 禁毒事件
 * @author zhangls
 *
 */
@Service("drugEnforcementEvent4JYGSService")
public class DrugEnforcementEvent4JYGSServiceImpl extends
		DrugEnforcementEvent4LXHZServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 判断用户是否可以启动流程
	 * @param userInfo
	 * @throws Exception
	 */
	protected boolean isUserAbleToStart(UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(userInfo != null) {
			Long orgId = userInfo.getOrgId();
			
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				if(orgInfo != null) {
					String chiefLevel = orgInfo.getChiefLevel(),
						   orgType = orgInfo.getOrgType(),
						   orgCode = orgInfo.getOrgCode(),
						   professionCode = orgInfo.getProfessionCode(),
						   msgWrong = null,
						   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT),			//组织类型 单位
						   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);	//组织类型 部门
					
					if(ORG_TYPE_UNIT.equals(orgType)) {
						if(!ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
							msgWrong = "该用户组织类型为单位，但是组织层级既不是社区级也不是街道级！";
						}
					} else if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
						if(ConstantValue.MUNICIPAL_ORG_LEVEL.equals(chiefLevel)) {
							String professionCodes = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.PROFESSION_CODE_4_START, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
							if(StringUtils.isNotBlank(professionCodes)) {
								String[] professionCodeArray = professionCodes.split(";"),
										 professionCodeItemArray = null;
								StringBuffer professionName = new StringBuffer("");
								
								for(String professionCodeItem : professionCodeArray) {
									if(StringUtils.isNotBlank(professionCodeItem)) {
										professionCodeItemArray = professionCodeItem.split(",");
										if(professionCodeItemArray.length > 0 && professionCodeItemArray[0].equals(professionCode)) {
											flag = true; break;
										} else if(professionCodeItemArray.length > 1) {
											professionName.append(professionCodeItemArray[1]).append(";");
										}
									}
								}
								
								if(!flag) {
									msgWrong = "该用户的组织专业不在如下专业中：" + professionName;
								}
							}
						} else {
							msgWrong = "该用户组织类型为部门，但是组织层级不是市级！";
						}
					}
					
					if(StringUtils.isNotBlank(msgWrong)) {
						throw new Exception("该用户不能提交流程！" + msgWrong);
					} else {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}
}
