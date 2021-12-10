package cn.ffcs.zhsq.handlerCfg.service;

import java.util.List;
import cn.ffcs.zhsq.mybatis.domain.event.EventNodeHandler;

/**
 * 获取人员配置信息
 * @author zhangls
 *
 */
public interface IEventHandlerCfgService {
	/**
	 * 获取事件环节人员配置信息
	 * 以infoOrgCode为起始找到gridLevel层级的地域编码
	 * @param wfId			工作流id
	 * @param eventType		事件类别
	 * @param nodeCode		环节编码
	 * @param infoOrgCode	地域编码
	 * @param gridLevel		网格层级
	 * @return
	 */
	public List<EventNodeHandler> findEventNodeHandlers(Long wfId, String eventType, String nodeCode, String infoOrgCode, int gridLevel);
	
	/**
	 * 获取事件环节人员配置信息
	 * @param wfId			工作流id
	 * @param eventType		事件类别
	 * @param nodeCode		环节编码
	 * @param infoOrgCode	地域编码
	 * @return
	 */
	public List<EventNodeHandler> findEventNodeHandlers(Long wfId, String eventType, String nodeCode, String infoOrgCode);

}
