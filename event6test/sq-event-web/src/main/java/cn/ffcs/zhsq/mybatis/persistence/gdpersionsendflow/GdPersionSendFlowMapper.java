package cn.ffcs.zhsq.mybatis.persistence.gdpersionsendflow;

import cn.ffcs.zhsq.mybatis.domain.gdpersionsendflow.GdPersionSendFlow;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 网格员协助送达流程模块dao接口
 * @Author: zhangch
 * @Date: 10-16 09:18:15
 * @Copyright: 2019 福富软件
 */
public interface GdPersionSendFlowMapper {
	
	/**
	 * 新增数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 网格员协助送达流程id
	 */
	public long insert(GdPersionSendFlow bo);
	
	/**
	 * 修改数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 修改的记录数
	 */
	public long update(GdPersionSendFlow bo);
	
	/**
	 * 删除数据
	 * @param bo 网格员协助送达流程业务对象
	 * @return 删除的记录数
	 */
	public long delete(GdPersionSendFlow bo);
	
	/**
	 * 根据业务id查询数据
	 * @param id 网格员协助送达流程id
	 * @return 网格员协助送达流程业务对象
	 */
	public GdPersionSendFlow searchById(Long id);

	
	/**
	 * 查询数据（分页）：我的发起
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 网格员协助送达流程数据列表
	 */
	public List<GdPersionSendFlow> getPaginationForMyCreate(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 查询数据总数：我的发起
	 * @param params 查询参数
	 * @return 网格员协助送达流程数据总数
	 */
	public long getCountForMyCreate(Map<String, Object> params);
	/**
	 * 查询数据（分页）：我的待办
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 网格员协助送达流程数据列表
	 */
	public List<GdPersionSendFlow> getPaginationForMyWait(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数：我的待办
	 * @param params 查询参数
	 * @return 网格员协助送达流程数据总数
	 */
	public long getCountForMyWait(Map<String, Object> params);
	/**
	 * 查询数据（分页）：我的经办
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 网格员协助送达流程数据列表
	 */
	public List<GdPersionSendFlow> getPaginationForMyHandle(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数：我的经办
	 * @param params 查询参数
	 * @return 网格员协助送达流程数据总数
	 */
	public long getCountForMyHandle(Map<String, Object> params);

}