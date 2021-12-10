package cn.ffcs.zhsq.mybatis.persistence.dispute;

import cn.ffcs.zhsq.mybatis.domain.dispute.MediationCase;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 矛盾纠纷调解人表模块dao接口
 * @Author: wangh
 * @Date: 12-02 14:26:34
 * @Copyright: 2020 福富软件
 */
public interface MediationCaseMapper {
	
	/**
	 * 新增数据
	 * @param bo 矛盾纠纷调解人表业务对象
	 * @return 矛盾纠纷调解人表id
	 */
	public long insert(MediationCase bo);
	
	/**
	 * 修改数据
	 * @param bo 矛盾纠纷调解人表业务对象
	 * @return 修改的记录数
	 */
	public long update(MediationCase bo);
	
	/**
	 * 删除数据
	 * @param bo 矛盾纠纷调解人表业务对象
	 * @return 删除的记录数
	 */
	public long delete(MediationCase bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 矛盾纠纷调解人表数据列表
	 */
	public List<MediationCase> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 矛盾纠纷调解人表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 矛盾纠纷调解人表id
	 * @return 矛盾纠纷调解人表业务对象
	 */
	public MediationCase searchById(Long id);

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	public long insertBatch(List<MediationCase> list);

	/**
	 * 根据人员类型 矛盾纠纷ID
	 * @param params
	 * @return
	 */
	public long deleteByMediationIdAndType(Map<String,Object> params);


	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 矛盾纠纷调解人表数据列表
	 */
	public List<MediationCase> searchList(Map<String, Object> params);
}