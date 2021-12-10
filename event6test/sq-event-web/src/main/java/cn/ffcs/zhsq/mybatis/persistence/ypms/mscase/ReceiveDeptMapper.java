package cn.ffcs.zhsq.mybatis.persistence.ypms.mscase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.ReceiveDept;

/**
 * @Description: 延平民生派发单位表模块dao接口
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:20
 * @Copyright: 2018 福富软件
 */
public interface ReceiveDeptMapper {
	
	/**
	 * 新增数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 延平民生派发单位表id
	 */
	public long insert(ReceiveDept bo);
	
	/**
	 *  批量新增数据
	 * @author zhangzhenhai
	 * @date 2018-4-26 上午10:33:34
	 * @param @param recevieDeptList    
	 * @return void
	 */
	public void insertBatch(List<ReceiveDept> recevieDeptList);
	/**
	 * 修改数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 修改的记录数
	 */
	public long update(ReceiveDept bo);
	
	/**
	 * 删除数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 删除的记录数
	 */
	public long delete(ReceiveDept bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 延平民生派发单位表数据列表
	 */
	public List<ReceiveDept> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 延平民生派发单位表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生派发单位表id
	 * @return 延平民生派发单位表业务对象
	 */
	public ReceiveDept searchById(Long id);

	/**
	 * 根据参数获取案件的派发单位list,包括单位联系人list
	 * @author zhangzhenhai
	 * @date 2018-4-27 上午9:14:48
	 * @param @param params
	 * @param @return    
	 * @return List<ReceiveDept>
	 */
	public List<ReceiveDept> getReceiveDeptListByRelaCaseId(
			Map<String, Object> params);
	
	/**
	 * 根据关联的案件id删除数据
	 * @author zhangzhenhai
	 * @date 2018-4-27 下午5:13:44
	 * @param @param caseId
	 * @param @return    
	 * @return Long
	 */
	public Long deleteByRelaCaseId(Long caseId);

	/**
	 * 根据参数查询数据
	 * @author zhangzhenhai
	 * @date 2018-4-28 下午4:44:37
	 * @param @param params
	 * @param @return    
	 * @return ReceiveDept
	 */
	public ReceiveDept searchByParams(Map<String, Object> params);

	public long updateByParams(Map<String, Object> params);

	public Long countUnfinishDept(Map<String, Object> params_c);

	public List<ReceiveDept> searchListByParams(Map<String, Object> params_e);

}