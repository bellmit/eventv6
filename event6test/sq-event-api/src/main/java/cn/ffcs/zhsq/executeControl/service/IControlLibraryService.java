package cn.ffcs.zhsq.executeControl.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 布控库管理模块服务
 * @Author: dtj
 * @Date: 07-16 20:37:41
 * @Copyright: 2020 福富软件
 */
public interface IControlLibraryService {

	/**
	 * 修改数据
	 * @param bo 布控库管理业务对象
	 * @return 是否修改成功
	 */
	public boolean update(ControlLibrary bo, String token) throws Exception;


	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 布控库管理分页数据对象
	 */
	public EUDGPagination searchList(ControlLibrary bo, Map<String, Object> params, String token) throws Exception;
	
	/**
	 * 根据业务id查询数据
	 * @param id 布控库管理id
	 * @return 布控库管理业务对象
	 */
	public ControlLibrary searchById(Long id) throws Exception;

	String getToken() throws Exception;

	List<ControlLibrary> getTitle() throws Exception;

	Long batchInsert(ArrayList<ControlLibrary> list) throws Exception;

	Long batchDelete(String[] ids) throws Exception;
}