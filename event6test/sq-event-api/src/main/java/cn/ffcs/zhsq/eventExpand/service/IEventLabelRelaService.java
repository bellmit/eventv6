package cn.ffcs.zhsq.eventExpand.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

import java.util.List;
import java.util.Map;

/**
 * 事件分类标签关联表相关服务
 * 	EVENT_ID		所关联的事件Id
   	LABEL_ID 		所关联的标签Id（暂时不启用，默认为0）
	LABEL_NAME 		所关联的标签名称
	BIZ_ID 			所关联的标签业务Id       
	IS_VALID 		是否有效（新增时默认为1） 
	CREATOR  		创建人   
	CREATE_DATE 	创建时间
	UPDATER  		更新人  
	UPDATE_DATE 	更新时间 
 * @author youwj
 * 2019/11/12
 */
public interface IEventLabelRelaService {
	
	
	/**
	 * 新增或更新操作
	 * （eventId不存在则执行新增操作否则执行更新操作）
	 * @param 
	 * 		eventLabelRela 	关联表信息
	 * 	 	 必传参数
	 * 			eventId		所关联的事件Id
	 * 			labelName	所对应的标签名称
	 * 			bizId		关联模块的业务Id
	 * 
	 * 		userInfo		操作用户信息（必传）
	 * 2019/11/12
	 * @retuen 成功返回新增条数，失败返回-1 
	 */
	public Integer saveOrUpdate(Map<String,Object> eventLabelRela,UserInfo userInfo);
	
	/**
	 * 删除操作
	 * （eventId不存在则执行新增操作否则执行更新操作）
	 * @param 
	 * 		eventLabelRela 	关联表信息
	 * 		必传参数
	 * 			eventId		所关联的事件Id
	 * 			labelName	所对应的标签名称
	 * 
	 * 		userInfo		操作用户信息（必传）
	 * 2019/11/12
	 * @retuen 成功返回删除条数，失败返回-1 
	 */
	public Integer deleteEventLabelRela(Map<String,Object> eventLabelRela,UserInfo userInfo);
	
	/**
	 * 根据事件Id查询关联标签表信息
	 * @param
	 * 必传参数 
	 * 		eventId 	事件Id
	 * 可选参数 
	 * 		eventLabelRela
	 * 			eventId 	事件Id
	 * 			labelName 	标签名
	 * 			bizId 		业务Id
	 * 2019/11/12
	 */
	public List<Map<String,Object>> searchByEventId(Long eventId,Map<String,Object> eventLabelRela);
	
	/**
	 * 根据关联模块的业务Id模块标签名查询数据（反查事件Id）
	 * @param eventLabelRela
	 * 		labelName 	标签名
	 * 		bizId 		业务Id
	 * 2019/11/12
	 */
	public Map<String,Object> searchByBizInfo(Map<String,Object> eventLabelRela);
	
	
	
	
}
