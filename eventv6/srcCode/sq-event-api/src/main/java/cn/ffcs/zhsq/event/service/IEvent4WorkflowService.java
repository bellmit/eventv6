package cn.ffcs.zhsq.event.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;

import java.util.Map;

/**
 * 事件与工作流关联相关接口
 * @author zhangls
 *
 */
public interface IEvent4WorkflowService {
	
	/**
	 * 获取事件记录数量
	 * @param params
	 * 			listType				列表类型
	 * 				1 待办；2 经办；3 辖区所有(支持办理人员查询)；
	 * 				4 归档列表；5 辖区所有；9 辖区所有(展示当前环节)；
	 * 				6 我发起的；7 我的关注；8 辖区内需要督办；
	 * 			
	 * 			listType 为1时
	 * 			curUserId				事件待办用户id，类型为String，为空时，使用属性userId
	 * 			curOrgId				事件待办组织id，类型为String，为空时，使用属性orgId
	 * 
	 * 			listType 为2时
	 * 			handledUserId			事件经办用户id，类型为Long，为空时，使用属性userId
	 * 			handledOrgId			事件经办组织id，类型为Long，为空时，使用属性orgId
	 * 			
	 * 			listType 为3时
	 *			eventOrgUserId			事件待办/经办用户id，类型为String，为空时，使用属性userId
	 * 			eventOrgOrgId			事件待办/经办组织id，类型为String，为空时，使用属性orgId
	 * 
	 * 			listType 为5时
	 * 			南昌个性化页面（辖区所有列表）查询条件
	 * 			handledTaskUnitId		办理单位组织id
	 * 			handledType				办理方式
	 * 
	 * 			listType 为6时
	 * 			initiatorId				事件发起用户id，为空时，类型为Long，使用属性userId
	 * 			initiatorOrgId			事件发起组织id，为空时，类型为Long，使用属性orgId
	 * 
	 * 			listType 为7时
	 * 			attentionUserId			事件关注用户id，为空时，类型为Long，使用属性userId
	 * 
	 * 			listType 为8时
	 * 			辖区内满足如下条件之一的事件：
	 * 				1、事件将到期，系统时间距离办理时限一天以内；
	 * 				2、事件已到期，系统时间超过办理时限；
	 * 				3、事件紧急程度为如下三者之一的：紧急、加急、重大公共应急事件；
	 * 				4、事件影响范围为如下二者之一的：大、重大。
	 * 
	 * 			eInfoOrgCode			加密的地域编码，优先于infoOrgCode使用
	 * 			infoOrgCode				地域编码，优先属性gridId使用
	 * 			gridId					所属网格id
	 * 			code					事件编码，进行模糊查询
	 * 			keyWord					关键字，对事件描述、标题、事发详址进行模糊查询
	 * 			type					事件类别，支持前缀模糊查询
	 * 			collectWayArray			采集渠道，数据类型为String[]，优先属性collectWay使用
	 * 			collectWay				采集渠道，当包含英文逗号时，会转换为collectWayArray
	 * 			influenceDegreeArray	影响范围，数据类型为String[]，优先属性influenceDegree使用
	 * 			influenceDegree			影响范围，当包含英文逗号时，会转换为influenceDegreeArray
	 * 			sourceArray				信息来源，数据类型为String[]，优先属性source使用
	 * 			source					信息来源，当包含英文逗号时，会转换为sourceArray
	 * 			statusList				事件状态，数据类型为List<String>，优先属性statusArray，status使用
	 * 			statusArray				事件状态，数据类型为String[]，优先属性status使用
	 * 			status					事件状态，多个值使用英文逗号分隔
	 * 			creatorOrgCode			采集人所属组织查询条件
	 * 			handleDateDayStart		办结期限开始时间，格式为：yyyy-MM-dd
	 * 			handleDateDayEnd		办结期限结束时间，格式为：yyyy-MM-dd
	 * 
	 * 			isCapMySupervised		是否获取我督办过的事件，true为是；默认为false
	 * 			superviseUserId			督办操作用户id，类型为Long，isCapMySupervised为true时生效，且必填；为空时，使用属性userId
	 * @return
	 * @throws ZhsqEventException
	 */
	public int findEventCount(Map<String, Object> params) throws ZhsqEventException;
	
	/**
	 * 分页获取事件列表
	 * @param pageNo					页码
	 * @param pageSize					每页记录数
	 * @param params
	 * 			listType				列表类型
	 * 				1 待办；2 经办；3 辖区所有(支持办理人员查询)；
	 * 				4 归档列表；5 辖区所有；9 辖区所有(展示当前环节)；
	 * 				6 我发起的；7 我的关注；8 辖区内需要督办；
	 * 			
	 * 			listType 为1时
	 * 				curUserId				事件待办用户id，类型为String，为空时，使用属性userId
	 * 				curOrgId				事件待办组织id，类型为String，为空时，使用属性orgId
	 * 
	 * 			listType 为2时
	 * 				handledUserId			事件经办用户id，类型为Long，为空时，使用属性userId
	 * 				handledOrgId			事件经办组织id，类型为Long，为空时，使用属性orgId
	 * 			
	 * 			listType 为3时
	 *				eventOrgUserId		事件待办/经办用户id，类型为String，为空时，使用属性userId
	 * 				eventOrgOrgId			事件待办/经办组织id，类型为String，为空时，使用属性orgId
	 * 
	 * 			listType 为5时
	 * 				isCapMapInfo			是否获取定位信息，true为是；默认为false
	 * 					mapType				地图类型，类型为Integer，默认为5，isCapMapInfo为true时使用
	 * 				
	 * 				isMapDistanceSearch4SelfEvent	是否查找指定事件的指定范围内事件，true为是；默认为false
	 * 					selfEventId			指定的事件id，类型为Long
	 * 
	 * 				isMapDistanceSearch	是否在指定范围内查找事件，true为是；默认为false
	 * 					mapDistanceWithin	地图查找的距离范围，类型为Long，默认为0，isMapDistanceSearch为true时使用
	 * 					mapType					地图类型，类型为Integer，默认为5，isMapDistanceSearch为true时使用
	 * 					longitude					经度，类型为Long，isMapDistanceSearch为true时使用
	 * 					latitude						纬度，类型为Long，isMapDistanceSearch为true时使用
	 * 				
	 * 
	 * 				南昌个性化页面（辖区所有列表）查询条件
	 * 				handledTaskUnitId		办理单位组织id
	 * 				handledType				办理方式
	 * 
	 * 			listType 为6时
	 * 				initiatorId				事件发起用户id，为空时，类型为Long，使用属性userId
	 * 				initiatorOrgId			事件发起组织id，为空时，类型为Long，使用属性orgId
	 * 
	 * 			listType 为7时
	 * 				attentionUserId			事件关注用户id，为空时，类型为Long，使用属性userId
	 * 
	 * 			listType 为8时
	 * 			辖区内满足如下条件之一的事件：
	 * 				1、事件将到期，系统时间距离办理时限一天以内；
	 * 				2、事件已到期，系统时间超过办理时限；
	 * 				3、事件紧急程度为如下三者之一的：紧急、加急、重大公共应急事件；
	 * 				4、事件影响范围为如下二者之一的：大、重大。
	 * 
	 * 			eInfoOrgCode			加密的地域编码，优先于infoOrgCode使用
	 * 			infoOrgCode				地域编码，优先属性gridId使用
	 * 			gridId					所属网格id
	 * 			code					事件编码，进行模糊查询
	 * 			keyWord					关键字，对事件描述、标题、事发详址进行模糊查询
	 * 			type					事件类别，支持前缀模糊查询
	 * 			collectWayArray			采集渠道，数据类型为String[]，优先属性collectWay使用
	 * 			collectWay				采集渠道，当包含英文逗号时，会转换为collectWayArray
	 * 			influenceDegreeArray	影响范围，数据类型为String[]，优先属性influenceDegree使用
	 * 			influenceDegree			影响范围，当包含英文逗号时，会转换为influenceDegreeArray
	 * 			sourceArray				信息来源，数据类型为String[]，优先属性source使用
	 * 			source					信息来源，当包含英文逗号时，会转换为sourceArray
	 * 			statusList				事件状态，数据类型为List<String>，优先属性statusArray，status使用
	 * 			statusArray				事件状态，数据类型为String[]，优先属性status使用
	 * 			status					事件状态，多个值使用英文逗号分隔
	 * 			creatorOrgCode			采集人所属组织查询条件
	 * 			handleDateDayStart		办结期限开始时间，格式为：yyyy-MM-dd
	 * 			handleDateDayEnd		办结期限结束时间，格式为：yyyy-MM-dd
	 * 			isCapCurNodeInfo		是否获取当前办理环节信息，数据类型为Boolean，true则获取，会进行当前办理环节中文转换；默认为false
	 * 			supervisionTypeArray	督办类型，数据类型为String[]，优先属性supervisionType使用
	 * 			supervisionType			督办类型，当包含英文逗号时，会转换为supervisionTypeArray
	 * 			eventName					事件标题模糊匹配
	 * 			eventNameAccurate		事件标题精确匹配
	 * 			
	 * 			isCapMySupervised		是否获取我督办过的事件，true为是；默认为false
	 * 			superviseUserId			督办操作用户id，类型为Long，isCapMySupervised为true时生效，且必填；为空时，使用属性userId
	 * 
	 * 			eliminateEventIdList		 需要移除的事件id，支持类型为List<Long>、String、String[]；为String时，使用英文逗号进行分隔
	 * 			
	 * 			isCapEventAdditionalColumn 是否查询事件列表额外列，true为是，默认为false
	 * 									        			 额外列包含：T_EVENT.CONTACT_USER，T_EVENT.TEL
	 * 			isGitAttr 是否查询事件附件列表，true为是，默认为false
	 * @return
	 * 		返回的记录类型为 Map<String, Object>
	 * @throws ZhsqEventException 
	 */
	public EUDGPagination findEventListPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException;
}
