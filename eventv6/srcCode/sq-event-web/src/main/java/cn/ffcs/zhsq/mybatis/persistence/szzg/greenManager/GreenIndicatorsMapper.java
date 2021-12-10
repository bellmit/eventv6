package cn.ffcs.zhsq.mybatis.persistence.szzg.greenManager;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.StatsGreenBO;
import org.apache.ibatis.annotations.Param;



public interface GreenIndicatorsMapper extends MyBatisBaseMapper<StatsGreenBO> {
	
	public int delete(Long seqid);
	
	public List<StatsGreenBO> findStatsGreen();

	
	public List<StatsGreenBO> findStatsGreenByParams(Map<String, Object> params);


	public List<Map<String,Object>> findCharDataByQs(Map<String, Object> param);

	public List<Map<String,Object>> findCharDataByQs_2(Map<String, Object> param);
}
