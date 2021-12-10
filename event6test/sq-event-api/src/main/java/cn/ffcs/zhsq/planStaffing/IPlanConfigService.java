package cn.ffcs.zhsq.planStaffing;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanConfig;

/**
 * @Description: 应急预案配置模块服务
 * @Author: LINZHU
 * @Date: 04-23 15:47:50
 * @Copyright: 2021 福富软件
 */
public interface IPlanConfigService {

	/**
	 * 新增数据
	 * @param bo 应急预案配置业务对象
	 * @return 应急预案配置id
	 */
	public String insert(PlanConfig bo);

	/**
	 * 修改数据
	 * @param bo 应急预案配置业务对象
	 * @return 是否修改成功
	 */
	public boolean update(PlanConfig bo);

	/**
	 * 删除数据
	 * @param bo 应急预案配置业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(PlanConfig bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案配置分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 应急预案配置id
	 * @return 应急预案配置业务对象
	 */
	public PlanConfig searchById(String id,String userOrgCode);
	/**
	 * 根据业务参数获取总条数
	 * @return
	 */
	public long countList(Map<String, Object> params);

}