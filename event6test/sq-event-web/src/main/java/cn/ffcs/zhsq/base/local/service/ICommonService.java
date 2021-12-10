/**
 * 
 */
package cn.ffcs.zhsq.base.local.service;

import java.util.List;

import cn.ffcs.shequ.statistics.domain.pojo.StatisticsInfo;

/**
 * 综治网格通用服务
 * @author guohh
 *
 */
public interface ICommonService {

	/**
	 * 过滤统计信息项
	 * @param orgStatInfoList 统计信息
	 * @param orgCode 组织编码
	 */
	public void filterGridStatInfo(List<StatisticsInfo> orgStatInfoList, String orgCode);
}
