package cn.ffcs.zhsq.mybatis.persistence.reportFocus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

public interface ReportFocusExtendMapper {
	/**
	 * 统计入格事件主流程待办数量
	 * @param param
	 * @return
	 */
	public List<Map<String, Integer>> findCount4IntegrationTodo(Map<String, Object> param);
	
	/**
	 * 获取入格事件我的阅办记录数量
	 * @param param
	 * @return
	 */
	public int findCount4IntegrationMsgReading(Map<String, Object> param);
	
	/**
	 * 获取入格事件我的阅办记录列表
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findList4IntegrationMsgReading(Map<String, Object> param, RowBounds bounds);
}
