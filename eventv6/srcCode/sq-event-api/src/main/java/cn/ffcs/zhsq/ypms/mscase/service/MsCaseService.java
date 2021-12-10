package cn.ffcs.zhsq.ypms.mscase.service;

import java.util.Map;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.MsCase;

/**
 * @Description: 延平民生案件表模块服务
 * @Author: zhangzhenhai
 * @Date: 04-13 14:43:28
 * @Copyright: 2018 福富软件
 */
public interface MsCaseService {

	/**
	 * 新增数据
	 * @param params_a 延平民生案件表业务对象
	 * @return 延平民生案件表id
	 */
	public Long insert(Map<String, Object> params_a);

	/**
	 * 修改数据
	 * @param params_a 延平民生案件表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(Map<String, Object> params_a);

	/**
	 * 删除数据
	 * @param bo 延平民生案件表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(MsCase bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生案件表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
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
	 * 插入操作员
	 * @author zhangzhenhai
	 * @date 2018-4-27 下午3:32:13
	 * @param @param params_a
	 * @param @return    
	 * @return Long
	 */
	public Long insertCaseHandler(Map<String, Object> params);

	/**
	 * 待办案件列表
	 * @author zhangzhenhai
	 * @date 2018-4-28 上午10:08:39
	 * @param @param page
	 * @param @param rows
	 * @param @param params
	 * @param @return    
	 * @return EUDGPagination
	 */
	public EUDGPagination searchListForWait(int page, int rows,
			Map<String, Object> params);

	/**
	 * 经办案件列表
	 * @author zhangzhenhai
	 * @date 2018-4-28 上午10:29:11
	 * @param @param page
	 * @param @param rows
	 * @param @param params
	 * @param @return    
	 * @return EUDGPagination
	 */
	public EUDGPagination searchListForHanlde(int page, int rows,
			Map<String, Object> params);

	public EUDGPagination searchListForUntreated(int page, int rows,
			Map<String, Object> params);

	public boolean updateSimple(MsCase msCase);

	public boolean isArchiver(MsCase bo);

	public boolean deptReject(Map<String, Object> params);

	public boolean centerRegresses(Map<String, Object> params);

}