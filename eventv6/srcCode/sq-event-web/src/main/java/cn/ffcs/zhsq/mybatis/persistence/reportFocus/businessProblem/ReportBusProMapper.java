package cn.ffcs.zhsq.mybatis.persistence.reportFocus.businessProblem;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;

public interface ReportBusProMapper extends ReportTwoViolationPreMapper {

	/**
	 * 获取流程指定环节记录
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findToDoOrgId(Map<String, Object> params);
	
	public void updateTaskTime(Map<String, Object> params);
}
