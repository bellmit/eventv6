package cn.ffcs.zhsq.mybatis.persistence.map.buildaddress;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import cn.ffcs.zhsq.mybatis.domain.map.buildaddress.BuildAddress;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;

/**
 * 2014-10-21 liushi add
 * 地址楼宇关联
 * @author Administrator
 *
 */
public interface BuildAddressMapper{
	
	/**
	 * 2015-10-21 liushi add
	 * 列表查询
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<ThresholdColorCfg> findPageBuildAddressCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 2015-10-21 liushi add
	 * 查询总数
	 * @param param
	 * @return
	 */
	public Integer findCountBuildAddressCriteria(Map<String, Object> param);
	
	/**
	 * 2015-10-22 liushi add 根据buildingId查询已存在的数据
	 * @param buildingId
	 * @return
	 */
	public BuildAddress findBuildAddressByBuildingId(@Param(value="buildingId")Long buildingId);
	
	/**
	 * 2015-10-22 liushi add 修改
	 * @param buildAddress
	 * @return
	 */
	public int updateBuildAddress(BuildAddress buildAddress);
	
	/**
	 * 2015-10-22 liushi add 新增
	 * @param buildAddress
	 * @return
	 */
	public int insertBuildAddress(BuildAddress buildAddress);
	
}
