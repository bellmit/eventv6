package cn.ffcs.zhsq.hotLine.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * @Description: T_HOTLINE模块服务
 * @Author: wuxq
 * @Date: 09-08 17:02:47
 * @Copyright: 2020 福富软件
 */
public interface HotlineService {

	/**
	 * 新增数据
	 * @param bo T_HOTLINE业务对象
	 * @return T_HOTLINEid
	 */
	public boolean insert(Map<String, Object> hotLine);
	
	
	/**
	 * 批量新增数据
	 * @param bo T_HOTLINE业务对象
	 * @return T_HOTLINEid
	 */
	public boolean insertAll(Map<String, Object> hotLine);
	
	/**
	 * 修改数据
	 * @param bo T_HOTLINE业务对象
	 * @return 是否修改成功
	 */
	public boolean update(Map<String, Object> hotLine);

	/**
	 * 删除数据
	 * @param bo T_HOTLINE业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(Map<String, Object> hotLine);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return T_HOTLINE分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id T_HOTLINEid
	 * @return T_HOTLINE业务对象
	 */
	public Map<String, Object> searchByCaseNo(String caseNo);

}