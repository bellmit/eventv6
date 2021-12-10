package cn.ffcs.zhsq.map.buildaddress.service;


import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.buildaddress.BuildAddress;

/**
 * 2015-10-21 liushi add
 * 地址楼宇关联
 * @author Administrator
 *
 */
public interface IBuildAddressService {
	
	/**
	 * 2015-10-21 liushi add
	 * 列表查询
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findBuildAddressPagination(int pageNo, int pageSize,Map<String, Object> params);
	
	/**
	 * 2015-10-22 liushi add
	 * @param buildAddress
	 * @return
	 */
	public boolean saveBuildAddress(BuildAddress buildAddress);
}
