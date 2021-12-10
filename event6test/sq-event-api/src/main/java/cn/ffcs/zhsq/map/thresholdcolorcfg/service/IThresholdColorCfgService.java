package cn.ffcs.zhsq.map.thresholdcolorcfg.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;

/**
 * 2015-09-23 liushi add
 * 地图统计热力图显示样式区间设置相关服务接口
 * @author Administrator
 *
 */
public interface IThresholdColorCfgService {
	
	/**
	 * 2015-09-23 liushi add
	 * 列表查询
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findThresholdColorCfgPagination(int pageNo, int pageSize,Map<String, Object> params);
	
	/**
	 * 2015-09-23 liushi add
	 * 信息查询
	 * @param tcId
	 * @return
	 */
	public ThresholdColorCfg findThresholdColorCfgById(Long tcId);
	
	/**
	 * 2015-09-23 liushi add
	 * 保存（包括新增和删除）
	 * @param thresholdColorCfg
	 * @return
	 */
	public boolean saveThresholdColorCfg(ThresholdColorCfg thresholdColorCfg);
	
	/**
	 * 2015-09-23 liushi add
	 * 删除，逻辑删除
	 * @param tcId
	 * @return
	 */
	public boolean deleteThresholdColorCfg(Long tcId);
	
	/**
	 * 2015-09-28 liushi add 查询配置信息
	 * @param param class_   orgCode 信息域code
	 * @return
	 */
	public List<Map<String,Object>> getThresholdColorCfgList(Map<String, Object> param);
	
}
