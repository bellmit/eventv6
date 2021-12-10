package cn.ffcs.zhsq.mybatis.persistence.planStaffing;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.planStaffing.EmergencyPlan;

/**
 * @Description: 应急预案管理模块dao接口
 * @Author: LINZHU
 * @Date: 04-22 16:31:15
 * @Copyright: 2021 福富软件
 */
public interface EmergencyPlanMapper {
	
	/**
	 * 新增数据
	 * @param bo 应急预案管理业务对象
	 * @return 应急预案管理id
	 */
	public long insert(EmergencyPlan bo);
	
	/**
	 * 修改数据
	 * @param bo 应急预案管理业务对象
	 * @return 修改的记录数
	 */
	public long update(EmergencyPlan bo);
	
	/**
	 * 删除数据
	 * @param bo 应急预案管理业务对象
	 * @return 删除的记录数
	 */
	public long delete(EmergencyPlan bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 应急预案管理数据列表
	 */
	public List<EmergencyPlan> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 应急预案管理数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案管理id
	 * @return 应急预案管理业务对象
	 */
	public EmergencyPlan searchById(String id);

}