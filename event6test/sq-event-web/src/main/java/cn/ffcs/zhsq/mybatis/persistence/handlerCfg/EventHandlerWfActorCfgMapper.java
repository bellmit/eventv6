package cn.ffcs.zhsq.mybatis.persistence.handlerCfg;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;

/**
 * 环节区域办理人配置
 * @author zhangls
 *
 */
public interface EventHandlerWfActorCfgMapper extends MyBatisBaseMapper<EventHandlerWfActorCfgPO> {
	/**
	 * 依据流程环节配置id删除环节人员配置记录，去除无用的人员配置
	 * @param wfpcId			流程环节配置主键
	 * @param affectedRacIdList	受影响的人员配置/需要保留的人员配置列表
	 * @return
	 */
	public int deleteByWfpcId(@Param("wfpcId")Long wfpcId, @Param("list")List<Long> affectedRacIdList);
	
	
	/**
	 * 依据业务配置id删除环节人员配置记录
	 * @param wfcId		业务配置主键
	 * @return
	 */
	public int deleteByWfcId(Long wfcId);
	
	/**
	 * 判重数量统计
	 * @param params
	 * 			transactorId	办理人id
	 * 			transactorOrgId	办理人组织
	 * 			transactorType	办理人类型 0 用户；1 组织；3 角色
	 * 			wfpcId			流程环节配置id T_BIZ_WF_PROC_ID 主键
	 * 			racId			主键id
	 * @return 返回值大于0，表示有重复；否则未有重复
	 */
	public int findCount4Duplicate(Map<String, Object> params);
	
	/**
	 * 无分页获取环节人员配置记录
	 * @param param
	 * @return
	 */
	public List<EventHandlerWfActorCfgPO> findPageListByCriteria(Map<String, Object> param);
}
