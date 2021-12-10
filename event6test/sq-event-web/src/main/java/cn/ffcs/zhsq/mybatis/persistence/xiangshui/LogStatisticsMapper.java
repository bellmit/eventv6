package cn.ffcs.zhsq.mybatis.persistence.xiangshui;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.mybatis.domain.xiangshui.LogStatistics;

public interface LogStatisticsMapper {
	/**
	 * 根据orgCode查询出这一级和他的下一级的地域名称连接的是zhsq_log.t_login_log
	 * @param params
	 * @return
	 */
	public List<LogStatistics> searchList(@Param(value = "params") Map<String, Object> params);
	
	/**
	 * 根据orgCode查询出这一级和他的下一级的地域名称连接的是zhsq.T_L_LOG
	 * @param params
	 * @return
	 */
	public List<LogStatistics> searchLists(@Param(value = "params") Map<String, Object> params);
}
