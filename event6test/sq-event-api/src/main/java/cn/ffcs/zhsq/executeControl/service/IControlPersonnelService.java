package cn.ffcs.zhsq.executeControl.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlPersonnel;

import java.util.Map;

/**
 * @Description: 布控库对象模块服务
 * @Author: dtj
 * @Date: 07-16 20:37:45
 * @Copyright: 2020 福富软件
 */
public interface IControlPersonnelService {

	/**
	 * 新增数据
	 * @param bo 布控库对象业务对象
	 * @return 布控库对象id
	 */
	public Long insert(ControlPersonnel bo, String token) throws Exception;

	/**
	 * 修改数据
	 * @param bo 布控库对象业务对象
	 * @return 是否修改成功
	 */
	public boolean update(ControlPersonnel bo, String token) throws Exception;

	/**
	 * 删除数据
	 * @param bo 布控库对象业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(ControlPersonnel bo, String token) throws Exception;

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 布控库对象分页数据对象
	 */
	public EUDGPagination searchList(ControlPersonnel bo, Map<String, Object> params) throws Exception;
	
	/**
	 * 根据业务id查询数据
	 * @param id 布控库对象id
	 * @return 布控库对象业务对象
	 */
	public ControlPersonnel searchById(Long id) throws Exception;

    String getToken() throws Exception;
}