package cn.ffcs.zhsq.handlerCfg.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;

/**
 * 环节人员配置接口
 * T_BIZ_REGION_ACTOR_CFG
 * 
 * @author zhangls
 *
 */
public interface IEventHandlerWfActorCfgService {
	/**
	 * 新增环节人员配置记录
	 * @param eventHandlerWfActorCfgPO
	 * @return 新增成功，返回racId；否则返回-1
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Long saveHandlerWfActorCfg(EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws Exception;
	
	/**
	 * 新增环节人员配置记录
	 * @param wfpcId			流程环节配置id
	 * @param transactorIds		人员id，多个值以英文逗号分隔
	 * @param transactorOrgIds	transactorIds对应的组织id，多个值以英文逗号分隔
	 * @param transactorType	办理人类型，0：用户；1：组织；2：角色
	 * @return 新增成功的条数
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int saveHandlerWfActorCfg(Long wfpcId, String transactorIds, String transactorOrgIds, String transactorType) throws Exception;
	
	/**
	 * 依据主键更新环节人员配置记录
	 * @param eventHandlerWfActorCfgPO
	 * @return 更新成功返回true；否则返回false
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateHandlerWfActorCfgById(EventHandlerWfActorCfgPO eventHandlerWfActorCfgPO) throws Exception;
	
	/**
	 * 依据主键删除环节人员配置记录
	 * @param racId	主键id
	 * @return 删除成功返回true；否则返回false
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean delHandlerWfActorCfgById(Long racId);
	
	/**
	 * 依据流程环节配置主键删除环节人员配置记录
	 * @param wfpcId	流程环节配置id
	 * @return 成功删除的记录数
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int delHandlerWfActorCfgByWfpcId(Long wfpcId);
	
	/**
	 * 依据业务配置删除环节人员配置记录
	 * @param wfcId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int delHandlerWfActorCfgByWfcId(Long wfcId);
	
	/**
	 * 分页获取环节人员配置记录
	 * @param pageNo	页面
	 * @param pageSize	每页显示记录数
	 * @param params
	 * 			wfpcId	流程环节配置id T_BIZ_WF_PROC_CFG主键
	 * @return
	 */
	public EUDGPagination findHandlerWfActorCfgPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 无分页获取环节人员配置记录
	 * @param params
	 * 			wfpcId	流程环节配置id T_BIZ_WF_PROC_CFG主键
	 * @return
	 */
	public List<EventHandlerWfActorCfgPO> findHandlerWfActorCfgList(Map<String, Object> params);
}
