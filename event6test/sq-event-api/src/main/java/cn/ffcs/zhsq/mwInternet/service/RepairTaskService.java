package cn.ffcs.zhsq.mwInternet.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.RepairTask;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.WarnTask;

import java.util.Map;

/**
 * @Description: 报修任务表模块服务
 * @Author: guoyd
 * @Date: 04-08 10:19:19
 * @Copyright: 2018 福富软件
 */
public interface RepairTaskService {

	/**
	 * 新增数据
	 * @param bo 告警任务表业务对象
	 * @return 告警任务表id
	 */
	public Map<String, Object> insert(RepairTask bo,Map<String, Object> params);
	/**
	 * 新增数据
	 * @param bo 报修任务表业务对象
	 * @return 报修任务表id
	 */
	public Long insert(RepairTask bo);

	/**
	 * 修改数据
	 * @param bo 报修任务表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(RepairTask bo);

	/**
	 * 修改数据
	 * @param bo 报修任务表业务对象
	 * @return 是否修改成功
	 */
	public Map<String, Object> update(RepairTask bo,Map<String, Object> params);
	/**
	 * 删除数据
	 * @param bo 报修任务表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(RepairTask bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 报修任务表分页数据对象
	 */
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 报修任务表分页数据对象
	 */
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 报修任务表分页数据对象
	 */
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 报修任务表分页数据对象
	 */
	public EUDGPagination searchFQList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 报修任务表分页数据对象
	 */
	public EUDGPagination searchEndList(int page, int rows, Map<String, Object> params,String orgCode);
	
	/**
	 * 根据业务id查询数据
	 * @param id 报修任务表id
	 * @return 报修任务表业务对象
	 */
	public RepairTask searchById(Long id,String orgCode);

}