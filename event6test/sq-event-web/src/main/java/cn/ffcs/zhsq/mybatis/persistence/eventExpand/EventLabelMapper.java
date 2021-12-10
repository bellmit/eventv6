package cn.ffcs.zhsq.mybatis.persistence.eventExpand;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * @Description: 事件分类标签表模块mapper接口
 * @Author: os.youwj
 * @Date: 11-11 18:10:40
 * @Copyright: 2019 福富软件
 */
public interface EventLabelMapper{
	
	/**
	 * 新增数据
	 * @param Map<String,Object> 事件分类标签表Map对象
	 * 必传参数
	 * 		labelName		标签名称
	 * 		labelModel		标签所属业务模块
	 */
	public int insert(Map<String, Object> eventLabel);
	
	
	/**
	 * 删除数据(伪删除)
	 * @param Map<String,Object> 事件分类标签表Map对象
	 * 必传参数
	 * 		labelId		标签Id
	 * 		updater		删除人Id
	 */
	public int deleteByLabelId(Map<String, Object> eventLabel);
	
	
	/**
	 * 修改数据
	 * @param Map<String, Object> 事件分类标签表Map对象
	 * 必传参数
	 * 		labelId			标签Id
	 * 		labelName		所对应的标签名称
	 * 		labelModel		标签所属业务模块
	 * @return 修改的记录数
	 */
	public int update(Map<String, Object> eventLabel);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 事件分类标签表数据列表
	 */
	public List<Map<String, Object>> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 事件分类标签表数据总数
	 */
	public Long countList(Map<String, Object> params);
	
	
	
	/**
	 * 根据条件查询数据
	 * @param 
	 * 可选参数
	 * 		labelModel		标签所属业务模块
	 * 		labelName		标签名
	 * @return 事件分类标签表业务对象
	 */
	public List<Map<String, Object>> searchByParams(Map<String, Object> eventLabel);
	
	/**
	 * 根据Id查询数据
	 * @param 

	 * @return 事件分类标签表业务对象
	 */
	public Map<String, Object> searchById(Long id);
	
	

}