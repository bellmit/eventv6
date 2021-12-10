package cn.ffcs.zhsq.mybatis.persistence.eventTopicAndKeyWords;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.eventTopicAndKeyWords.EventTopic;

/**
 * @Description: 热点事件主题表模块dao接口
 * @Author: os.wuzhj
 * @Date: 10-15 16:01:04
 * @Copyright: 2019 福富软件
 */
public interface EventTopicMapper {
	
	/**
	 * 新增数据
	 * @param bo 热点事件主题表业务对象
	 * @return 热点事件主题表id
	 */
	public long insert(EventTopic bo);
	
	/**
	 * 修改数据
	 * @param bo 热点事件主题表业务对象
	 * @return 修改的记录数
	 */
	public long update(EventTopic bo);
	
	/**
	 * 删除数据
	 * @param bo 热点事件主题表业务对象
	 * @return 删除的记录数
	 */
	public long delete(EventTopic bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 热点事件主题表数据列表
	 */
	public List<EventTopic> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 热点事件主题表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 热点事件主题表id
	 * @return 热点事件主题表业务对象
	 */
	public EventTopic searchById(Long id);
	
	/**
	 * 根据业务类型查询发布的数量
	 * @param bizType 业务类型 
	 * 		  1、热点主题  
	 * 		  2、词云
	 * @return Integer
	 */
	public Integer findReleaseCount(String bizType);

}