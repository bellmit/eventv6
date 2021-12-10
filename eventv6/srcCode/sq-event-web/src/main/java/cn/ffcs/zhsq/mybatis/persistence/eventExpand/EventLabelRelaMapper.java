package cn.ffcs.zhsq.mybatis.persistence.eventExpand;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * @Description: 事件分类标签关联表模块mapper接口
 * @Author: os.youwj
 * @Date: 11-11 18:10:40
 * @Copyright: 2019 福富软件
 */
public interface EventLabelRelaMapper{
	
	/**
	 * 新增数据
	 * @param Map<String,Object> 事件分类标签关联表Map对象
	 * 必传参数
	 * 		eventId		所关联的事件Id
	 * 		labelName	所对应的标签名称
	 * 		bizId		关联模块的业务Id
	 */
	public int insert(Map<String, Object> eventLabelRela);
	
	
	/**
	 * 删除数据(伪删除)
	 * @param Map<String,Object> 事件分类标签关联表Map对象
	 * 必传参数
	 * 		eventId		所关联的事件Id
	 * 		labelName	所对应的标签名称
	 * 		updater		删除人Id
	 */
	public int deleteByEventIdAndLabelInfo(Map<String, Object> eventLabelRela);
	
	
	/**
	 * 修改数据
	 * @param Map<String, Object> 事件分类标签关联表Map对象
	 * 必传参数
	 * 		eventId		所关联的事件Id
	 * 		labelName	所对应的标签名称
	 * @return 修改的记录数
	 */
	public int update(Map<String, Object> eventLabelRela);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 事件分类标签关联表数据列表
	 */
	public List<Map<String, Object>> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 事件分类标签关联表数据总数
	 */
	public Long countList(Map<String, Object> params);
	
	
	
	/**
	 * 根据条件查询数据
	 * @param 
	 * 必传参数
	 * 		eventId 事件Id
	 * 可选参数
	 * 		bizId		标签业务Id
	 * 		labelName	标签名
	 * @return 事件分类标签关联表业务对象
	 */
	public List<Map<String, Object>> searchByEventId(Map<String, Object> eventLabelRela);
	
	
	
	/**
	 * 根据关联模块的业务Id模块标签名查询数据（反查事件Id）
	 * @param Map<String, Object>
	 * 必传参数
	 * 		labelName	标签名
	 * 		bizId 		关联模块业务Id
	 * @return 事件分类标签关联表业务对象
	 */
	public Map<String, Object> searchByBizInfo(Map<String, Object> eventLabelRela);
	

}