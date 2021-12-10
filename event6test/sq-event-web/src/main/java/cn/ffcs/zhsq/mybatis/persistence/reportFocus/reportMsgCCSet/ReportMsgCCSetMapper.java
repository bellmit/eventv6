package cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportMsgCCSet;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息已配送人员信息相关操作
 * 				  相关表T_EVENT_MSG_CC_SET
 * @ClassName:   ReportMsgCCSetMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年10月13日 上午9:30:18
 */
public interface ReportMsgCCSetMapper {
	/**
	 * 新增已配送人员信息
	 * @param reportMsgCCSetMap
	 * @return
	 */
	public int insert(Map<String, Object> reportMsgCCSetMap);
	
	/**
	 * 查找已配送人员信息
	 * @param params
	 * 			taskId	任务id
	 * 			ccType	配送类型
	 * 			userId	已配送人员id
	 * 			orgId	已配送组织id
	 * @return
	 */
	public List<Map<String, Object>> findMsgCCSetByParams(Map<String, Object> params);
	
	/**
	 * 依据实例id获取已配送人员信息
	 * @param params
	 * 			instanceId 流程实例id
	 * 			taskId		任务id
	 * 			ccType		配送类型
	 * 			userId		已配送人员id
	 * 			orgId		已配送组织id
	 * @return
	 */
	public List<Map<String, Object>> findMsgCCSetByInstanceId(Map<String, Object> params);
	
}
