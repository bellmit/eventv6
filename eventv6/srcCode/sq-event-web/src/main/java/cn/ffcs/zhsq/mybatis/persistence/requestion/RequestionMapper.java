package cn.ffcs.zhsq.mybatis.persistence.requestion;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.requestion.Requestion;

import java.util.List;
import java.util.Map;

/**
 * @Description: 诉求表模块dao接口
 * @Author: caiby
 * @Date: 03-12 08:45:59
 * @Copyright: 2018 福富软件
 */
public interface RequestionMapper {
	
	/**
	 * 新增数据
	 * @param bo 诉求表业务对象
	 * @return 诉求表id
	 */
	public long insert(Requestion bo);
	
	/**
	 * 修改数据
	 * @param bo 诉求表业务对象
	 * @return 修改的记录数
	 */
	public long update(Requestion bo);
	
	/**
	 * 删除数据
	 * @param bo 诉求表业务对象
	 * @return 删除的记录数
	 */
	public long delete(Requestion bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchDBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countDBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchDBList_Main(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countDBList_Main(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchDBList_Task(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countDBList_Task(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchJBList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countJBList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchJBList_Task(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countJBList_Task(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchAllList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countAllList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchAllList_Main(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求表数据列表
	 */
	public List<Requestion> searchList_Main(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countAllList_Main(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	public Requestion searchById(@Param(value="reqId") Long id,@Param(value="formTypeId") String formTypeId);
	
	/**
	 * 根据网站群关联id查询数据
	 * @param keyId
	 * @return
	 */
	public List<Requestion> searchByKeyId(Long keyId);
	
	
	/**
	 * PC根据业务id查询数据
	 * @param id 诉求表id
	 * @return 诉求表业务对象
	 */
	public Requestion searchBy_id(Long id);
	
	/**
	 * PC查询数据总数
	 * @param params 查询参数
	 * @return 诉求表数据总数
	 */
	public long countRequest(Map<String, Object> params);
	
	/**
	 * 消息提示代办数
	 * @param params
	 * @return
	 */
	public long countDB(Map<String, Object> params);

}