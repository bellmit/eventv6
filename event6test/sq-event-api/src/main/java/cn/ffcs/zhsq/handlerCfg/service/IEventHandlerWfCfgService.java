package cn.ffcs.zhsq.handlerCfg.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;

/**
 * 业务配置接口
 * T_BIZ_WF_CFG
 * 
 * @author zhangls
 *
 */
public interface IEventHandlerWfCfgService {
	/**
	 * 添加业务配置信息
	 * @param eventHandlerWfCfgPO
	 * @return	新增成功，返回wfcId；否则返回-1
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Long saveHandlerWfCfg(EventHandlerWfCfgPO eventHandlerWfCfgPO) throws Exception;
	
	/**
	 * 依据主键更新业务配置信息
	 * @param eventHandlerWfCfgPO
	 * @return 更新成功，返回true；否则返回false
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean updateHandlerWfCfgById(EventHandlerWfCfgPO eventHandlerWfCfgPO) throws Exception;
	
	/**
	 * 依据主键删除业务配置信息
	 * @param wfcId		主键
	 * @param delUserId	删除操作的用户id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean delHandlerWfCfgById(Long wfcId, Long delUserId);
	
	/**
	 * 依据主键获取业务配置信息
	 * @param wfcId	主键
	 * @return 未找到对应记录时，返回null
	 */
	public EventHandlerWfCfgPO findHandlerWfCfgById(Long wfcId);
	
	/**
	 * 分页获取业务配置记录
	 * @param pageNo		页码
	 * @param pageSize		每页显示条数
	 * @param params
	 * 			regionCode	地域编码
	 * 			bizType		业务类型 00 工作流
	 * 			wfCfgId		与bizType对应的id，00 为工作流流程id
	 * @return
	 */
	public EUDGPagination findHandlerWfCfgPagination(int pageNo, int pageSize, Map<String, Object> params);
}
