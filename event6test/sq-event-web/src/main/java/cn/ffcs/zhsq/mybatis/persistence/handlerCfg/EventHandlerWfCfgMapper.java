package cn.ffcs.zhsq.mybatis.persistence.handlerCfg;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;

/**
 * 工作流业务配置
 * @author zhangls
 *
 */
public interface EventHandlerWfCfgMapper extends MyBatisBaseMapper<EventHandlerWfCfgPO> {
	/**
	 * 判重数量统计
	 * @param params
	 * 			regionCode	地域编码
	 * 			bizType		业务类型 00 工作流业务配置；
	 * 			wfcId		主键id
	 * 			wfCfgId		业务id bizType为00时，为工作流流程id
	 * 			
	 * @return 返回值大于0，表示有重复；否则未有重复
	 */
	public int findCount4Duplicate(Map<String, Object> params);
	
	/**
	 * 依据主键删除业务配置，并设置删除操作人员
	 * @param wfcId		主键id
	 * @param delUserId	删除操作人员id
	 * @return
	 */
	public int delete(@Param(value="id") Long wfcId, @Param(value="delUserId") Long delUserId);
	
}
