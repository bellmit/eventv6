package cn.ffcs.zhsq.mybatis.persistence.planStaffing;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanConfig;

/**
 * @Description: 应急预案配置模块
 * @Author: LINZHU
 * @Date: 04-23 15:47:50
 * @Copyright: 2021 福富软件
 */
public interface PlanConfigMapper {
	
	/**
	 * 新增数据
	 * @param bo 应急预案配置业务对象
	 * @return 应急预案配置id
	 */
	public long insert(PlanConfig bo);
	
	/**
	 * 修改数据
	 * @param bo 应急预案配置业务对象
	 * @return 修改的记录数
	 */
	public long update(PlanConfig bo);
	
	/**
	 * 删除数据
	 * @param bo 应急预案配置业务对象
	 * @return 删除的记录数
	 */
	public long delete(PlanConfig bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 应急预案配置数据列表
	 */
	public List<PlanConfig> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 应急预案配置数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案配置id
	 * @return 应急预案配置业务对象
	 */
	public PlanConfig searchById(String id);

}