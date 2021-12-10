package cn.ffcs.zhsq.mybatis.persistence.ypms.mscase;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.MsCase;


/**
 * @Description: 延平民生案件表模块dao接口
 * @Author: zhangzhenhai
 * @Date: 04-13 14:43:28
 * @Copyright: 2018 福富软件
 */
public interface MsCaseMapper {
	
	/**
	 * 新增数据
	 * @param bo 延平民生案件表业务对象
	 * @return 延平民生案件表id
	 */
	public long insert(MsCase bo);
	
	/**
	 * 修改数据
	 * @param bo 延平民生案件表业务对象
	 * @return 修改的记录数
	 */
	public long update(MsCase bo);
	
	/**
	 * 删除数据
	 * @param bo 延平民生案件表业务对象
	 * @return 删除的记录数
	 */
	public long delete(MsCase bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 延平民生案件表数据列表
	 */
	public List<MsCase> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 延平民生案件表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生案件表id
	 * @return 延平民生案件表业务对象
	 */
	public MsCase searchById(Long id);
	
	/**
	 * 根据参数获取案件信息
	 * @author zhangzhenhai
	 * @date 2018-4-20 上午10:24:32
	 * @param @param params_a
	 * @param @return    
	 * @return MsCase
	 */
	public MsCase searchByParams(Map<String, Object> params_a);
	
	/**
	 * 根据参数获取最大流水号
	 * @author zhangzhenhai
	 * @date 2018-4-23 下午5:16:04
	 * @param @param params
	 * @param @return    
	 * @return Long
	 */
	public Long getMaxCaseNoSerial(Map<String, Object> params);

	public List<MsCase> searchListForWait(Map<String, Object> params,
			RowBounds rowBounds);

	public long countListForWait(Map<String, Object> params);

	public List<MsCase> searchListForHanlde(Map<String, Object> params,
			RowBounds rowBounds);

	public long countListForHanlde(Map<String, Object> params);

	public List<MsCase> searchListForUntreated(Map<String, Object> params,
			RowBounds rowBounds);

	public long countListForUntreated(Map<String, Object> params);

}