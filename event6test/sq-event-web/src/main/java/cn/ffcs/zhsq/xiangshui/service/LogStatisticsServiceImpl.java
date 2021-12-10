package cn.ffcs.zhsq.xiangshui.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.zhsq.mybatis.domain.xiangshui.LogStatistics;
import cn.ffcs.zhsq.mybatis.persistence.xiangshui.LogStatisticsMapper;

@Service("logStatisticsServiceImpl")
@Transactional
public class LogStatisticsServiceImpl implements LogStatisticsService{
	
	@Autowired
	private LogStatisticsMapper logStatisticsMapper;

	@Override
	public List<LogStatistics> searchList(Map<String, Object> params) {
		List<LogStatistics> searchGridName = logStatisticsMapper.searchList(params);
		return searchGridName;
	}

	@Override
	public List<LogStatistics> searchLists(Map<String, Object> params) {
		List<LogStatistics> searchLists = logStatisticsMapper.searchLists(params);
		return searchLists;
	}

}
