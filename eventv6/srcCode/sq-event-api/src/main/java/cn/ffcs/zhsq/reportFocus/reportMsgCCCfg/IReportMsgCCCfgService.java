package cn.ffcs.zhsq.reportFocus.reportMsgCCCfg;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;

/**
 * @Description: 消息配送人员配置信息相关操作
 * 				  相关表T_EVENT_MSG_CC_CFG
 * @ClassName:   IReportMsgCCCfg   
 * @author:      张联松(zhangls)
 * @date:        2020年9月26日 下午2:59:53
 */
public interface IReportMsgCCCfgService {
	/**
	 * 新增或者更新人员配置信息
	 * cfgUUID有效时，进行更新操作；否则进行信息操作
	 * @param cfgMap	人员配置信息
	 * 			orgChiefLevel	组织层级，该属性启用时，orgCode无效
	 * 			orgCode			组织编码，该属性需要orgChiefLevel无效时生效
	 * @param userInfo	操作用户信息
	 * @return
	 * 		新增/更新成功返回cfgUUID，否则返回null
	 * @throws Exception 
	 */
	public String saveOrUpdateCfgInfo(Map<String, Object> cfgMap, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据UUID删除人员配置信息
	 * @param cfgUUID	人员配置UUID
	 * @param userInfo	操作用户信息
	 * @return
	 */
	public boolean delCfgInfoByUUID(String cfgUUID, UserInfo userInfo);
	
	/**
	 * 依据UUID获取人员配置信息
	 * @param cfgUUID	人员配置UUID
	 * @param userInfo	操作用户信息
	 * @return
	 * 		没有找到返回null
	 */
	/**
	 * 依据UUID获取人员配置信息
	 * @param cfgUUID	人员配置UUID
	 * @param params	额外参数
	 * 			userOrgCode		组织编码
	 * 			isDictTransfer	是否进行字典转换，默认为true
	 * @return
	 * 		没有找到返回null
	 */
	public Map<String, Object> findCfgInfoByUUID(String cfgUUID, Map<String, Object> params);
	
	/**
	 * 分页获取人员配置信息
	 * @param pageNo	页码
	 * @param pageSize	每页分页数量
	 * @param params
	 * 			userOrgCode			组织编码
	 * 			isDictTransfer		是否进行字典转换，默认为true
	 * 			workflowName		流程图名称
	 * 			workflowNameFuzzy	流程图名称模糊查找
	 * 			wfNodeName			流程节点名称
	 * 			wfNodeNameFuzzy		流程节点名称模糊查找
	 * @return
	 */
	public EUDGPagination findCfg4Pagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 获取人员配置信息
	 * @param params
	 * 			workflowName	流程图名称
	 * 			wfNodeName		流程节点名称
	 * @return
	 * 		orgChiefLevel	组织层级
	 * 		orgCode			组织编码
	 * 		ccType			配送类型
	 * 		cfgType			配置类型
	 * 		cfgValue		配置值
	 */
	public List<Map<String, Object>> findCfg4List(Map<String, Object> params);
	
	/**
	 * 获取人员配置信息
	 * @param params
	 * @param benchmarkOrgCode	基准组织编码，当使用orgChiefLevel进行人员配置时使用，只支持向上组织查找
	 * @return
	 * 		抄送类型 ：抄送用户
	 */
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, String benchmarkOrgCode);
	
	/**
	 * 获取人员配置信息
	 * @param params
	 * 			wfStartNodeName	起始节点名称
	 * 			wfEndNodeName	目标节点名称，如果携带->，如a->b，则a会解析为起始节点名称，此时需要wfStartNodeName为空则a生效；b解析为目标节点名称
	 * 			wfNodeNameList	节点名称，类型为List<Map<String, Object>，该参数优先于wfStartNodeName、wfEndNodeName生效
	 * 				wfStartNodeName	起始节点名称
	 * 				wfEndNodeName	目标节点名称
	 * @param benchmarkOrgInfo 基准组织信息，当使用orgChiefLevel进行人员配置时使用，只支持向上组织查找
	 * @return
	 * 		抄送类型 ：抄送用户
	 */
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, OrgSocialInfoBO benchmarkOrgInfo);
	
	/**
	 * 获取人员配置信息
	 * @param params
	 * @param benchmarkOrgInfo	基准组织信息，当使用orgChiefLevel进行人员配置时使用，只支持向上组织查找
	 * @param cfgMapList		分送、选送配置信息
	 * @return
	 * 		抄送类型 ：抄送用户
	 */
	public Map<String, List<UserBO>> findCfg4User(Map<String, Object> params, OrgSocialInfoBO benchmarkOrgInfo, List<Map<String, Object>> cfgMapList);
}
