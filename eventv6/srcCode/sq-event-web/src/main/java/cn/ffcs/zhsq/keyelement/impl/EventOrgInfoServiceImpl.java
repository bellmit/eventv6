package cn.ffcs.zhsq.keyelement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "eventOrgInfoServiceImpl")
public class EventOrgInfoServiceImpl implements IEventOrgInfoService {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private OrgEntityInfoOutService entityInfoOutService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Override
	public MixedGridInfo findGridInfoBySocialInfoOrgId(Long orgId) {
		MixedGridInfo gridInfo = null;
		if (orgId != null) {
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(orgId);
			if (orgSocialInfoBO != null && orgSocialInfoBO.getLocationOrgId() != null) {
				OrgEntityInfoBO entityInfoBO = entityInfoOutService.findByOrgId(orgSocialInfoBO.getLocationOrgId());
				if (entityInfoBO != null && entityInfoBO.getOrgCode() != null) {
					gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(entityInfoBO.getOrgCode());
				}
			}
		}
		return gridInfo;
	}
	
	@Override
	public boolean isNewOrganization(Long orgId) {
		boolean flag = false;
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgSocialInfo = orgSocialInfoOutService.findByOrgId(orgId);
			if(orgSocialInfo != null){
				flag = orgSocialInfo.getIsNewOrg();
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean isNewOrganization(OrgSocialInfoBO orgSocialInfo) {
		boolean flag = false;
		
		if(orgSocialInfo != null){
			flag = orgSocialInfo.getIsNewOrg();
		}
		
		return flag;
	}
	
	@Override
	public String isNewOrganizationForString(Long orgId) {
		String isNewOrganization = "0";
		
		if(isNewOrganization(orgId)){
			isNewOrganization = "1";
		}
		
		return isNewOrganization;
	}

	@Override
	public String isNewOrganizationForString(OrgSocialInfoBO orgSocialInfo) {
		String isNewOrganization = "0";
		
		if(orgSocialInfo != null && orgSocialInfo.getIsNewOrg()){
			isNewOrganization = "1";
		}
		
		return isNewOrganization;
	}
	
	@Override
	public boolean isGovernment(Long orgId){
		boolean flag = false;
		
		if(orgId!=null && orgId>0){
			OrgSocialInfoBO orgSocialInfo = orgSocialInfoOutService.findByOrgId(orgId);
			flag = isGovernment(orgSocialInfo);
		}
		
		return flag;
	}
	
	@Override
	public boolean isGovernment(OrgSocialInfoBO orgSocialInfo){
		boolean flag = false;
		
		if(orgSocialInfo != null){
			String professionCode = orgSocialInfo.getProfessionCode();
			flag = ConstantValue.GOV_PROFESSION_CODE.equals(professionCode);
		}
		
		return flag;
	}
	
	@Override
	public boolean isGovernment(String professionCode){
		return ConstantValue.GOV_PROFESSION_CODE.equals(professionCode);
	}
	
	@Override
	public String isGovernmentForString(Long orgId){
		String isGovernment = "0";
		
		if(this.isGovernment(orgId)){
			isGovernment = "1";
		}
		
		return isGovernment;
	}
	
	@Override
	public String isGovernmentForString(OrgSocialInfoBO orgSocialInfo){
		String isGovernment = "0";
		
		if(this.isGovernment(orgSocialInfo)){
			isGovernment = "1";
		}
		
		return isGovernment;
	}
	
	@Override
	public String isGovernmentForString(String professionCode){
		String isGovernment = "0";
		
		if(this.isGovernment(professionCode)){
			isGovernment = "1";
		}
		
		return isGovernment;
	}

}
