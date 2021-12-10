package cn.ffcs.zhsq.map.buildaddress.service.impl;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.buildaddress.service.IBuildAddressService;
import cn.ffcs.zhsq.mybatis.domain.map.buildaddress.BuildAddress;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;
import cn.ffcs.zhsq.mybatis.persistence.map.buildaddress.BuildAddressMapper;

/**
 * 2014-10-21 liushi add
 * 地址楼宇关联
 * @author Administrator
 *
 */
@Service(value="buildAddressServiceImpl")
public class BuildAddressServiceImpl implements IBuildAddressService{
	@Autowired
	private BuildAddressMapper buildAddressMapper;
	
	@Override
	public EUDGPagination findBuildAddressPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<ThresholdColorCfg> list = this.buildAddressMapper.findPageBuildAddressCriteria(params, rowBounds);
		int count = this.buildAddressMapper.findCountBuildAddressCriteria(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 允许地址库：楼宇  一对多
	 */
	@Override
	public boolean saveBuildAddress(BuildAddress buildAddress) {
		BuildAddress findObj = this.buildAddressMapper.findBuildAddressByBuildingId(buildAddress.getBuildingId());
		int resultNum=0;
		if(findObj != null) {
			resultNum = this.buildAddressMapper.updateBuildAddress(buildAddress);
		}else {
			resultNum = this.buildAddressMapper.insertBuildAddress(buildAddress);
		}
		return resultNum == 1 ? true:false;
	}
}
