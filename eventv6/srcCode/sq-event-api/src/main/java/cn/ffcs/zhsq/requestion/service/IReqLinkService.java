package cn.ffcs.zhsq.requestion.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;

import java.util.List;
import java.util.Map;

/**
 * @Description: 诉求联动单位模块服务
 * @Author: caiby
 * @Date: 03-12 10:09:52
 * @Copyright: 2018 福富软件
 */
public interface IReqLinkService {

	public Map<String, Object> insert(ReqLink bo,Map<String, Object> params);
	/**
	 * 新增数据
	 * @param bo 诉求联动单位业务对象
	 * @return 诉求联动单位id
	 */
	public Long insert(ReqLink bo);

	/**
	 * 修改数据
	 * @param bo 诉求联动单位业务对象
	 * @return 是否修改成功
	 */
	public boolean update(ReqLink bo);

	/**
	 * 删除数据
	 * @param bo 诉求联动单位业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(ReqLink bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 诉求联动单位分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 诉求联动单位id
	 * @return 诉求联动单位业务对象
	 */
	public ReqLink searchById(Long id);
	
	public List<ReqLink> searchList(Map<String, Object> params);
	
	public List<Map<String,Object>> queryWFtaskId(Map<String,Object> params);
	
	public List<ReqLink> showBackList(Map<String, Object> params);

}