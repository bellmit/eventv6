package cn.ffcs.zhsq.eventDuplication.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;

/**
 * 重复事件处理接口，对应表T_EVENT_DUPLICATION
 * @author zhangls
 *
 */
public interface IEventDuplicationService {
	/**
	 * 保存重复事件信息
	 * @param eventDuplication	重复事件信息
	 * 						leadEventId			主事件id，类型为Long
	 * 						duplicateEventId	重复事件id，类型为Long；多个值时，使用英文逗号连接，类型为String
	 * @param userInfo					操作用户信息
	 * @return 成功操作记录数
	 */
	public int saveEventDuplication(Map<String, Object> eventDuplication, UserInfo userInfo);
	
	/**
	 * 获取重复事件记录数量
	 * @param params	查询参数
	 * 						leadEventId	主事件id
	 * @param userInfo	操作用户信息
	 * @return
	 */
	public int findDuplicatedEventCount(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 无分页获取重复事件记录信息
	 * @param params	查询参数
	 * 						leadEventId	主事件id
	 * 	
	 *						isDictTransfer4Event	是否进行事件字典转换，类型为Boolean，true为进行转换，默认为true
	 * 						userOrgCode			用户组织编码，可用于字典翻译，userInfo中的orgCode优先于该属性使用
	 * @param userInfo	操作用户信息
	 * @return
	 * 			eventId				事件id
	 * 			eventName		事件标题
	 * 			happenTime		事发时间，类型为java.util.Date
	 * 			happenTimeStr	事发时间文本，类型为String，格式为yyyy-MM-dd HH:mm:ss
	 * 			type					事件类别字典业务编码
	 * 			typeName			事件类别
	 * 			eventClass		事件类别全路径
	 * 			status				事件状态
	 * 			statusName		事件状态字典名称
	 * 			gridPath			事件所属网格全路径
	 */
	public List<Map<String, Object>> findDuplicatedEventList(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 删除重复事件信息
	 * @param eventDuplication	重复事件信息
	 * 						duplicationId		主键，类型为Long
	 * 						leadEventId			主事件id，类型为Long
	 * 						duplicateEventId	重复事件id，类型为Long
	 * 						
	 * @param userInfo					操作用户信息
	 * @return 成功删除记录数
	 */
	public int delEventDuplication(Map<String, Object> eventDuplication, UserInfo userInfo);
}
