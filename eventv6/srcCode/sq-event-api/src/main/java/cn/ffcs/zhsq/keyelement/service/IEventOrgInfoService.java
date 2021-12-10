package cn.ffcs.zhsq.keyelement.service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;

public interface IEventOrgInfoService {
	/**
	 * 依据组织编码获取网格信息
	 * @param orgId 组织编码
	 * @return
	 */
	public MixedGridInfo findGridInfoBySocialInfoOrgId(Long orgId);
	
	/**
	 * 判断对应组织是否使用新的组织树
	 * @param orgId 组织编码
	 * @return 是则返回true；否则返回false
	 */
	public boolean isNewOrganization(Long orgId);
	
	/**
	 * 判断对应组织是否使用新的组织树
	 * @param orgSocialInfo	组织对象
	 * @return 是则返回true；否则返回false
	 */
	public boolean isNewOrganization(OrgSocialInfoBO orgSocialInfo);
	
	/**
	 * 判断对应组织是否使用新的组织树 工作流使用
	 * @param orgId 组织编码
	 * @return 启用新组织，返回"1"；否则，返回"0"
	 */
	public String isNewOrganizationForString(Long orgId);
	
	/**
	 * 判断对应组织是否使用新的组织树 工作流使用
	 * @param orgSocialInfo	组织对象
	 * @return 启用新组织，返回"1"；否则，返回"0"
	 */
	public String isNewOrganizationForString(OrgSocialInfoBO orgSocialInfo);
	
	/**
	 * 判断组织的专业是否是政府专业(zfgl)
	 * @param orgId 组织编码
	 * @return 是则返回true；否则返回false
	 */
	public boolean isGovernment(Long orgId);
	/**
	 * 判断组织的专业是否是政府专业(zfgl)
	 * @param orgSocialInfo 组织信息
	 * @return 是则返回true；否则返回false
	 */
	public boolean isGovernment(OrgSocialInfoBO orgSocialInfo);
	/**
	 * 判断组织专业是否是政府专业(zfgl)
	 * @param professionCode 组织专业编码
	 * @return 是则返回true；否则返回false
	 */
	public boolean isGovernment(String professionCode);
	/**
	 * 判断组织的专业是否是政府专业(zfgl) 工作流使用
	 * @param orgId 组织编码
	 * @return 是返回"1"；否则，返回"0"
	 */
	public String isGovernmentForString(Long orgId);
	/**
	 * 判断组织的专业是否是政府专业(zfgl) 工作流使用
	 * @param orgSocialInfo 组织信息
	 * @return 是返回"1"；否则，返回"0"
	 */
	public String isGovernmentForString(OrgSocialInfoBO orgSocialInfo);
	/**
	 * 判断组织的专业是否是政府专业(zfgl) 工作流使用
	 * @param professionCode 组织专业编码
	 * @return 是返回"1"；否则，返回"0"
	 */
	public String isGovernmentForString(String professionCode);
}
