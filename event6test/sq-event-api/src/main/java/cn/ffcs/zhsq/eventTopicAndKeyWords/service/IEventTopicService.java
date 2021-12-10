package cn.ffcs.zhsq.eventTopicAndKeyWords.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.eventTopicAndKeyWords.EventTopic;

/**
 * @Description: 热点事件主题表模块服务
 * @Author: os.wuzhj
 * @Date: 10-15 16:01:04
 * @Copyright: 2019 福富软件
 */
public interface IEventTopicService {

	/**
	 * 新增数据
	 * @param bo 热点事件主题表业务对象
	 * @return 热点事件主题表id
	 */
	public Long insert(EventTopic bo);

	/**
	 * 修改数据
	 * @param bo 热点事件主题表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(EventTopic bo);

	/**
	 * 删除数据
	 * @param bo 热点事件主题表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(EventTopic bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 热点事件主题表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
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