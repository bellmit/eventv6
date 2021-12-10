package cn.ffcs.zhsq.planStaffing;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.EmergencyPlan;

/**
 * @Description: 应急预案管理模块服务
 * @Author: LINZHU
 * @Date: 04-22 16:31:15
 * @Copyright: 2021 福富软件
 */
public interface IEmergencyPlanService {

	/**
	 * 新增数据
	 * @param bo 应急预案管理业务对象
	 * @return 应急预案管理id
	 */
	public String insert(EmergencyPlan bo);

	/**
	 * 修改数据
	 * @param bo 应急预案管理业务对象
	 * @return 是否修改成功
	 */
	public boolean update(EmergencyPlan bo);

	/**
	 * 删除数据
	 * @param bo 应急预案管理业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(EmergencyPlan bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案管理分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案管理id
	 * @return 应急预案管理业务对象
	 */
	public EmergencyPlan searchById(String id,String userOrgCode);
	
	/**
	 * 根据业务参数获取总条数
	 * @return
	 */
	public long countList(Map<String, Object> params);
	

}