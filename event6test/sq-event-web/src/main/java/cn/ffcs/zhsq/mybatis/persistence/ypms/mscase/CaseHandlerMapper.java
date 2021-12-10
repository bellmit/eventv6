package cn.ffcs.zhsq.mybatis.persistence.ypms.mscase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.CaseHandler;

/**
 * @Description: 延平民生案件操作员信息表模块dao接口
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:42
 * @Copyright: 2018 福富软件
 */
public interface CaseHandlerMapper {
	
	/**
	 * 新增数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 延平民生案件操作员信息表id
	 */
	public long insert(CaseHandler bo);
	
	/**
	 * 修改数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 修改的记录数
	 */
	public long update(CaseHandler bo);
	
	/**
	 * 删除数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 删除的记录数
	 */
	public long delete(CaseHandler bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 延平民生案件操作员信息表数据列表
	 */
	public List<CaseHandler> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 延平民生案件操作员信息表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生案件操作员信息表id
	 * @return 延平民生案件操作员信息表业务对象
	 */
	public CaseHandler searchById(Long id);

}