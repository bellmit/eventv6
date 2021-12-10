package cn.ffcs.zhsq.ypms.mscase.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.ReceiveDept;

/**
 * @Description: 延平民生派发单位表模块服务
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:20
 * @Copyright: 2018 福富软件
 */
public interface ReceiveDeptService {

	/**
	 * 新增数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 延平民生派发单位表id
	 */
	public Long insert(ReceiveDept bo);

	/**
	 * 修改数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(ReceiveDept bo);

	/**
	 * 删除数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(ReceiveDept bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生派发单位表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
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

	public ReceiveDept searchByParams(Map<String, Object> params);

	public Long countUnfinishDept(Map<String, Object> params_c);

	public List<ReceiveDept> searchListByParams(Map<String, Object> params_e);


}