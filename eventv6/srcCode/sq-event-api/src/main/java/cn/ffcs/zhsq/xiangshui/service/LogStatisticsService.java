package cn.ffcs.zhsq.xiangshui.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.xiangshui.LogStatistics;

public interface LogStatisticsService {
	
	/**
	 * 1根据orgCode查询出这一级和他的下一级的地域名称连接的是zhsq_log.t_login_log
	 * @param params
	 * @return
	 */
	public List<LogStatistics> searchList(Map<String, Object> params);
	
	/**
	 * 1根据orgCode查询出这一级和他的下一级的地域名称连接的是zhsq.T_L_LOG
	 * @param params
	 * @return
	 */
	public List<LogStatistics> searchLists(Map<String, Object> params);
}
