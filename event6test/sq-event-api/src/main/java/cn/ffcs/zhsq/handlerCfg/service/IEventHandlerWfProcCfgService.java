package cn.ffcs.zhsq.handlerCfg.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfProcCfgPO;

/**
 * 流程环节配置
 * T_BIZ_WF_PROC_CFG
 * 
 * @author zhangls
 *
 */
public interface IEventHandlerWfProcCfgService {
	/**
	 * 新增流程环节配置
	 * @param eventHandlerWfProcCfgPO
	 * @return 新增成功，返回wfpcId；否则返回-1
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Long saveHandlerWfProcCfg(EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws Exception;
	
	/**
	 * 依据主键更新流程环节配置
	 * @param eventHandlerWfProcCfgPO
	 * @return 更新成功，返回true；否则返回false
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateHandlerWfProcCfgById(EventHandlerWfProcCfgPO eventHandlerWfProcCfgPO) throws Exception;
	
	/**
	 * 依据主键删除流程环节配置(真删除)
	 * @param wfpcId	主键id
	 * @return 删除成功返回true；否则返回false
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean delHandlerWfProcCfgById(Long wfpcId);
	
	/**
	 * 依据业务配置id删除流程环节配置(真删除)
	 * @param wfcId	业务配置id T_BIZ_WF_CFG的主键
	 * @return 成功删除的记录数
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int delHandlerWfProcCfgByWfcId(Long wfcId);
	
	/**
	 * 依据主键查找流程环节配置
	 * @param wfpcId	主键id
	 * @return 未找到对应记录时，返回null
	 */
	public EventHandlerWfProcCfgPO findHandlerWfProcCfgById(Long wfpcId);
	
	/**
	 * 分页获取流程环节配置记录
	 * @param pageNo	页码
	 * @param pageSize	每页显示记录条数
	 * @param params
	 * 			regionCode	地域编码
	 * 			taskCode	环节编码/环节名称
	 * 			wfcId		业务配置id
	 * 			eventCodes	事件类别
	 * @return
	 */
	public EUDGPagination findHandlerWfProcCfgPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 无分页获取流程环节配置记录
	 * @param params
	 * 			regionCode	地域编码
	 * 			searchType  eq：地域编码使用精确查找
	 * 			taskCode	环节编码/环节名称
	 * 			wfcId		业务配置id
	 * 			eventCodes	事件类别
	 * @return
	 */
	public List<EventHandlerWfProcCfgPO> findHandlerWfProcCfgList(Map<String, Object> params);
}
