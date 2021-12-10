package cn.ffcs.zhsq.mybatis.persistence.map.thresholdcolorcfg;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import cn.ffcs.zhsq.mybatis.domain.map.thresholdcolorcfg.ThresholdColorCfg;

/**
 * 2014-10-09 liushi add
 * 地图统计热力图显示样式区间设置dao层
 * @author Administrator
 *
 */
public interface ThresholdColorCfgMapper{
	
	/**
	 * 2015-09-23 liushi add
	 * 列表查询
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<ThresholdColorCfg> findPageThresholdColorCfgCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 2015-09-25 liushi add
	 * 查询总数
	 * @param param
	 * @return
	 */
	public Integer findCountThresholdColorCfgCriteria(Map<String, Object> param);
	/**
	 * 2015-09-23 liushi add
	 * 信息查询
	 * @param tcId
	 * @return
	 */
	public ThresholdColorCfg findThresholdColorCfgById(@Param(value="tcId")Long tcId);
	
	/**
	 * 2015-09-23 liushi add
	 * 新增
	 * @param thresholdColorCfg
	 * @return
	 */
	public Integer insertThresholdColorCfg(ThresholdColorCfg thresholdColorCfg);
	/**
	 * 2015-09-28 liushi add
	 * 修改
	 * @param thresholdColorCfg
	 * @return
	 */
	public Integer updateThresholdColorCfg(ThresholdColorCfg thresholdColorCfg);
	/**
	 * 2015-09-23 liushi add
	 * 删除，逻辑删除
	 * @param tcId
	 * @return
	 */
	public Integer deleteThresholdColorCfg(@Param(value="tcId")Long tcId);
	
	/**
	 * 2015-09-28 liushi add 查询配置信息
	 * @param param
	 * @return
	 */
	public List<ThresholdColorCfg> getThresholdColorCfgList(Map<String, Object> param);
}
