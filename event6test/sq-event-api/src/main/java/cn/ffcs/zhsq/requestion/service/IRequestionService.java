package cn.ffcs.zhsq.requestion.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;

import java.util.List;
import java.util.Map;

/**
 * @Description: 诉求表模块服务
 * @Author: caiby
 * @Date: 03-12 08:45:59
 * @Copyright: 2018 福富软件
 */
public interface IRequestionService {

	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	public Long insert(Requestion bo);

	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	public Map<String, Object> insert(Requestion bo,Map<String, Object> params);
	
	/**
	 * 修改数据
	 * @param bo 诉求表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(Requestion bo);

	/**
	 * 删除数据
	 * @param bo 诉求表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(Requestion bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	public Requestion searchById(Long id,String formTypeId);
	
	
	
	/**
	 * 根据网站群关联id查询数据
	 * @param keyId
	 * @return
	 */
	public Requestion searchByKeyId(Long keyId);
	
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchDBList_Main(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchDBList_Task(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchJBList_Task(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchAllList_Main(int page, int rows, Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求表分页数据对象
	 */
	public EUDGPagination searchList_Main(int page, int rows, Map<String, Object> params);
	
	
	/**
	 * PC根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	public Requestion searchBy_id(Long id);
	
	
	public long countDB(Long orgId);
}