package cn.ffcs.zhsq.ypms.mscase.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.CaseHandler;

/**
 * @Description: 延平民生案件操作员信息表模块服务
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:42
 * @Copyright: 2018 福富软件
 */
public interface CaseHandlerService {

	/**
	 * 新增数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 延平民生案件操作员信息表id
	 */
	public Long insert(CaseHandler bo);

	/**
	 * 修改数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(CaseHandler bo);

	/**
	 * 删除数据
	 * @param bo 延平民生案件操作员信息表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(CaseHandler bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生案件操作员信息表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 延平民生案件操作员信息表id
	 * @return 延平民生案件操作员信息表业务对象
	 */
	public CaseHandler searchById(Long id);

}