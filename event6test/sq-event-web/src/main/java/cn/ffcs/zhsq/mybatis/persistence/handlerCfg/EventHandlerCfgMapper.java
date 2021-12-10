package cn.ffcs.zhsq.mybatis.persistence.handlerCfg;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;

/**
 * 获取人员配置信息
 * @author zhangls
 *
 */
public interface EventHandlerCfgMapper {
	/**
	 * 获取配置的人员信息
	 * @param param
	 * 			wfId			工作流id
	 * 			eventType		事件类别
	 * 			nodeCode		环节编码
	 * 			infoOrgCode		地域编码
	 * @return
	 */
	public List<EventHandlerWfActorCfgPO> findEeventHandlerWfActorCfgPOs(Map<String, Object> param);
}
