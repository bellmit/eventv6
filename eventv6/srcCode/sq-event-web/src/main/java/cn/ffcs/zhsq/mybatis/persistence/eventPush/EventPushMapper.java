package cn.ffcs.zhsq.mybatis.persistence.eventPush;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.eventPush.EventPush;


/**
 * @Description: 事件推送模块dao接口
 * @Author: os.wuzhj
 * @Date: 06-10 09:13:18
 * @Copyright: 2019 福富软件
 */
public interface EventPushMapper {
	
	/**
	 * 新增数据
	 * @param List<EventPush> 业务对象集合
	 * @return 插入的行数
	 */
	public Integer insert(Map<String,Object> map);
	
	/**
	 * 修改数据
	 * @param bo 事件推送业务对象
	 * @return 修改的记录数
	 */
	public Integer update(EventPush eventPush);
	
	/**
	 * 删除数据
	 * @param bo 事件推送业务对象
	 * @return 删除的记录数
	 */
	public Integer delete(EventPush eventPush);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 事件推送数据列表
	 */
	public List<EventPush> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 事件推送数据总数
	 */
	public Integer countList(Map<String, Object> params);
	
	/**
	 * 判断事件是否存在
	 * @param eventIds 事件id数组
	 * @return 事件名字
	 */
	public List<EventPush> searchByEventIds(Map<String, Object> params);

}