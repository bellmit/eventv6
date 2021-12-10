package cn.ffcs.zhsq.eventPush.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.eventPush.EventPush;


/**
 * @Description: 事件推送模块服务
 * @Author: os.wuzhj
 * @Date: 06-10 09:13:18
 * @Copyright: 2019 福富软件
 */
public interface IEventPushService {

	/**
	 * 新增数据
	 * @param eventPushList 事件推送业务对象
	 * @return 插入的行数
	 */
	public Integer insert(Map<String,Object> map);

	/**
	 * 修改数据
	 * @param bo 事件推送业务对象
	 * @return 是否修改成功
	 */
	public boolean update(EventPush eventPush);

	/**
	 * 删除数据
	 * @param bo 事件推送业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(EventPush eventPush);


	/**
	 * 查询数据（分页）
	 * @param params 查询参数  
	 * @param userId 当前登录人的id   必传
	 * @param infoOrgCode 组织的infoOrgCode,如果参数为null取当前登录组织的infoOrgCode 
	 * @param eventName 事件标题
	 * @param code 事件编号
	 * @param pushName 推送人
	 * @param createTimeStart 采集事件开始
	 * @param createTimeEnd 采集事件结束
	 * @return 事件推送分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 判断事件是否存在
	 * @param eventids 事件id数组
	 * @return 事件名字
	 */
	public List<EventPush> searchByEventIds(Map<String, Object> params);
	
}