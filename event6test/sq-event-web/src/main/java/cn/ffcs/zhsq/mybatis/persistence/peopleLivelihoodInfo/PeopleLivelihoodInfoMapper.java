package cn.ffcs.zhsq.mybatis.persistence.peopleLivelihoodInfo;

import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 民生信息表模块dao接口
 * @Author: 游伟进
 * @Date: 12-17 08:59:50
 * @Copyright: 2020 福富软件
 */
public interface PeopleLivelihoodInfoMapper {
	
	
	/**
	 * 新增数据
	 * @param LivelihoodInfo 民生信息表业务对象
	 * @return 民生信息表id
	 */
	public Long insert(Map<String,Object> LivelihoodInfo);
	
	/**
	 * 修改数据
	 * @param LivelihoodInfo 民生信息表业务对象
	 * @return 修改的记录数
	 */
	public Long update(Map<String,Object> LivelihoodInfo);
	
	/**
	 * 修改数据
	 * @param LivelihoodInfo 民生信息表业务对象
	 * @return 修改的记录数
	 */
	public Long updateBySeqId(Map<String,Object> LivelihoodInfo);
	
	/**
	 * 删除数据
	 * @param LivelihoodInfo 民生信息表业务对象
	 * @return 删除的记录数
	 */
	public Long delete(Map<String,Object> LivelihoodInfo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据（分页）待办
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchDraftList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据（分页）待办
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchTodoList(Map<String, Object> params, RowBounds rowBounds);
	
	
	
	/**
	 * 查询数据（分页）经办
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchDoneList(Map<String, Object> params, RowBounds rowBounds);
	
	
	
	/**
	 * 查询数据（分页）辖区所有
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchAllList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchListByParams(Map<String, Object> params);
	
	/**
	 * 查询数据（不分页）草稿
	 * @param params 查询参数
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchDraftListByParams(Map<String, Object> params);
	
	/**
	 * 查询数据（不分页）待办
	 * @param params 查询参数
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchTodoListByParams(Map<String, Object> params);
	
	/**
	 * 查询数据（不分页）经办
	 * @param params 查询参数
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchDoneListByParams(Map<String, Object> params);
	
	/**
	 * 查询数据（不分页）辖区所有
	 * @param params 查询参数
	 * @return 民生信息表数据列表
	 */
	public List<Map<String,Object>> searchAllListByParams(Map<String, Object> params);
	
	
	
	
	/**
	 * 根据业务id查询数据
	 * @param id 民生信息表uuid
	 * @return 民生信息表业务对象
	 */
	public Map<String, Object> searchById(String id);
	
	/**
	 * 根据业务id查询数据
	 * @param id 民生信息表uuid
	 * @return 民生信息表业务对象
	 */
	public Map<String, Object> searchBySeqId(Long id);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countList(Map<String, Object> params);
	
	/**
	 * 查询数据总数(草稿列表)
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countDraftList(Map<String, Object> params);
	
	/**
	 * 查询数据总数(待办列表)
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countTodoList(Map<String, Object> params);
	
	/**
	 * 查询数据总数(经办列表)
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countDoneList(Map<String, Object> params);
	
	/**
	 * 查询数据总数(辖区所有列表)
	 * @param params 查询参数
	 * @return 民生信息表数据总数
	 */
	public Long countAllList(Map<String, Object> params);

}