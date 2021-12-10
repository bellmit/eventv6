package cn.ffcs.zhsq.reportFocus.reportMsgCCSet;

import cn.ffcs.uam.bo.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息已配送人员信息相关操作
 * 				  相关表T_EVENT_MSG_CC_SET
 * @ClassName:   IReportMsgCCSetService   
 * @author:      张联松(zhangls)
 * @date:        2020年10月13日 上午9:57:03
 */
public interface IReportMsgCCSetService {
	/**
	 * 保存已配置人员信息
	 * @param msgCCSetMap	已配置人员信息
	 * 	必填参数
	 * 		taskId	任务ID
	 * 		ccType	配送类型
	 * 		orgId	配置用户组织id
	 * 		userId	配置用户id
	 *  非必填参数
	 *  	userName	用户姓名，为空时，通过userId转换
	 *  	orgName		组织名称，为空时，通过orgId转换
	 *  	creator		新增操作用户id，为空时，通过userInfo获取
	 *  	updator		更新操作用户id，为空时，通过userInfo获取
	 * @param userInfo		操作用户信息
	 * @return 保存成功返回UUID，否则返回null
	 * @throws Exception 
	 */
	public String saveMsgCCSet(Map<String, Object> msgCCSetMap, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据任务id获取已配置人员信息
	 * @param params	查询参数
	 * 			taskId	任务id，类型为Long
	 * 			ccType	配送类型，类型为String
	 * @return
	 * 		taskId	任务id
	 * 		ccType	配送类型，1 分送；2 选送；3 主送
	 * 		userId	已配置人员id
	 * 		userName已配置人员姓名
	 * 		orgId	已配置人员组织id
	 * 		orgName	已配置人员组织名称
	 */
	public List<Map<String, Object>> findMsgCCSetByParams(Map<String, Object> params);
	
	/**
	 * 依据任务id获取已配置人员信息
	 * @param instanceId	流程实例id
	 * @param params		查询参数
	 * 			taskId	任务id，类型为Long
	 * 			ccType	配送类型，类型为String
	 * @return
	 * 		taskId	任务id
	 * 		ccType	配送类型，1 分送；2 选送；
	 * 		userId	已配置人员id
	 * 		userName已配置人员姓名
	 * 		orgId	已配置人员组织id
	 * 		orgName	已配置人员组织名称
	 */
	public List<Map<String, Object>> findMsgCCSetByInstanceId(Long instanceId, Map<String, Object> params);
}
