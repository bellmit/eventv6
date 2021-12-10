package cn.ffcs.zhsq.mybatis.persistence.dispute;

import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeFlowInfo;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 办理流程表模块dao接口
 * @Author: wangh
 * @Date: 12-02 14:26:46
 * @Copyright: 2020 福富软件
 */
public interface DisputeFlowInfoMapper {
	
	/**
	 * 新增数据
	 * @param bo 办理流程表业务对象
	 * @return 办理流程表id
	 */
	public long insert(DisputeFlowInfo bo);
	
	/**
	 * 修改数据
	 * @param bo 办理流程表业务对象
	 * @return 修改的记录数
	 */
	public long update(DisputeFlowInfo bo);
	
	/**
	 * 删除数据
	 * @param bo 办理流程表业务对象
	 * @return 删除的记录数
	 */
	public long delete(DisputeFlowInfo bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 办理流程表数据列表
	 */
	public List<DisputeFlowInfo> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 办理流程表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 办理流程表id
	 * @return 办理流程表业务对象
	 */
	public DisputeFlowInfo searchById(Long id);

	/**
	 * 批量新增接口
	 * @param list
	 * @return
	 */
	public long insertBatch(List<DisputeFlowInfo> list);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 办理流程表数据列表
	 */
	public List<DisputeFlowInfo> searchList(Map<String, Object> params);

	/**
	 * 根据bizId进行数据删除
	 * @param disputeId
	 * @return
	 */
	public Long deleteByDisputeId(Long disputeId);
}