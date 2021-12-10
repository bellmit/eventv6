package cn.ffcs.zhsq.eventExpand.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

import java.util.List;
import java.util.Map;

/**
 * 事件拓展ExpandServcie
 * 用于拓展事件基础服务类(IEventDisposalService)的功能
 * @author youwj
 * 2019/06/06
 */
public interface IEventDisposal4ExpandService {
	
	
	/**
	 * 更新事件信息
	 * @param event
	 * @param params
	 *          parentEventId	父级事件id
	 * 该方法用于更新事件紧急程度需要发送消息提醒
	 * @return
	 * */
	public boolean updateEventDisposal(EventDisposal event,Map<String,Object> params,UserInfo userInfo);

	/**
	 * 保存事件评价信息
	 * 必填参数
	 * @param userInfo
	 * @param eventId     事件id
	 * @param evaLevel    评价级别
	 * @param evaContent  评价内容
	 * 非必填参数
	 * @param params
	 * 		  evaObj        评价对象，该参数为空时，默认为'03'评价对象为事件
	 *        createDate    评价时间，Date类型，该参数为空时，评价时间取系统时间
	 * */
	public boolean saveOrUpdateEventEvaluate(UserInfo userInfo,Long eventId,String evaLevel,String evaContent,Map<String,Object> params) throws Exception;

	/**
	 * 查询事件评价信息
	 * 必填参数
	 * @param eventId		事件id
	 * @param evaObj		评价对象---新事件
	 * @param userInfo		用户
	 *
	 * @param params
	 * */
	public List<Map<String,Object>> findEvaResultList(Long eventId,String evaObj,UserInfo userInfo,Map<String,Object> params)throws Exception;
	
	/**
     * 依据事件id删除事件信息
     * @param eventIdList	事件id
     * @param userInfo		删除操作用户
     * @param params		额外参数
     * @return
     * 		total			需要删除记录数量
     * 		successTotal	删除失败记录数量
     * 		msgWrong		异常信息
     */
    public Map<String, Object> delEventById(List<Long> eventIdList, UserInfo userInfo, Map<String, Object> params);

    /**
	 * 事件列表个性化格式数据
	 * @param eventMap 事件数据
	 * @param params		额外参数
	 *                      getHandleDateLight 获取超期事件指示灯（重庆铜梁区）
	 * */
    public void expandFormatMapDataOut(Map<String, Object> eventMap, UserInfo userInfo,Map<String, Object> params);
    
    /**
     * 获取应急预案人员配置信息
     * @param planType	预案类型
     * @param planLevel	预案等级
     * @param userInfo	操作用户
     * @param params	额外参数
     * 		regionCode	地域编码，为空时使用userInfo中的组织信息进行转换
     * @return
     * 		userType		用户类型，1 牵头领导；2 配合领导；3 主办科室；4 配合科室；
     * 		planConfigId	预案配置id
     * 		configUserIds	用户id，多个值使用英文逗号分隔，有去重；
     * 		configUserNames	用户姓名，多个值使用英文逗号分隔，有去重；
     * 		configOrgIds	组织id，多个值使用英文逗号分隔，有去重；
     * 		configOrgNames	组织名称，多个值使用英文逗号分隔，有去重；
     * 		configStaff		配置人员信息，类型为List<Map<String, Object>>
     * 			userId		用户id
     * 			userName	用户姓名
     * 			orgId		组织id
     * 			orgName		组织名称
     * @throws Exception 
     */
    public List<Map<String, Object>> capPlanConfigStaff(String planType, String planLevel, UserInfo userInfo, Map<String, Object> params) throws Exception;
    
    /**
     * 为事件设置默认属性
     * @param event		需要获取默认属性的事件
     * @param userInfo	操作用户信息
     * @param params	额外参数
     * @return
     * @throws Exception
     */
	public EventDisposal init4Event(EventDisposal event, UserInfo userInfo, Map<String, Object> params) throws Exception;
}
