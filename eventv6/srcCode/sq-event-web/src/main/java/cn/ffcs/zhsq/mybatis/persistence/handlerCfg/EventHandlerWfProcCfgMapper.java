package cn.ffcs.zhsq.mybatis.persistence.handlerCfg;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfProcCfgPO;

/**
 * 流程环节配置
 * @author zhangls
 *
 */
public interface EventHandlerWfProcCfgMapper extends MyBatisBaseMapper<EventHandlerWfProcCfgPO> {
	/**
	 * 依据业务配置主键删除流程环节配置记录
	 * @param wfcId	业务配置id
	 * @return
	 */
	public int deleteByWfcId(Long wfcId);
	
	/**
	 * 判重数量统计
	 * @param params
	 * 			regionCode	地域编码
	 * 			taskCode	环节编码
	 * 			wfcId		业务配置id T_BIZ_WF_CFG主键
	 * 			eventCode	事件类别(单个)
	 * 			wfpcId		主键id
	 * @return 返回值大于0，表示有重复；否则未有重复
	 */
	public int findCount4Duplicate(Map<String, Object> params);

	/**
	 * 无分页获取流程环节配置记录
	 * @param param
	 * 			regionCode	地域编码
	 * 			searchType	eq：地域编码使用精确查找
	 * 			taskCode	环节编码
	 * 			wfcId		业务配置id T_BIZ_WF_CFG主键
	 * 			eventCode	事件类别(单个)
	 * 			wfpcId		主键id
	 * @return
	 */
	public List<EventHandlerWfProcCfgPO> findPageListByCriteria(Map<String, Object> param);
}
