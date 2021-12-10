package cn.ffcs.zhsq.szzg.resource.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceType;

import java.util.List;
import java.util.Map;

/**
 * @Description: zg_resource_type模块服务
 * @Author: huangwenbin
 * @Date: 09-16 10:01:50
 * @Copyright: 2017 福富软件
 */
public interface ZgResourceTypeService {

	/**
	 * 删除数据
	 * @param bo zg_resource_type业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(Map<String,Object> param);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return zg_resource_type分页数据对象
	 */
	public EUDGPagination findList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id zg_resource_typeid
	 * @return zg_resource_type业务对象
	 */
	public ZgResourceType findById(Long id);
	
	/**
	 * 移动节点
	 * @param params
	 * @return
	 */
	public int moveNode(ZgResourceType bo);
	
	/**
	 * 查树
	 * @param param
	 * @return
	 */
	public List<ZgResourceType> findTree(Map<String,Object> param);

	/**
	 * 查询typeCode 是否存在
	 * @param param
	 * @return
	 */
	public int findByTypeCode(String typeCode);

	public int addOrUpdate(ZgResourceType entity);

	/**
	 * 是否有子项
	 */
	public int isHaveChildren(Long id);
	
	/**
	 * 获取详情地址
	 */
	public List<Map<String, Object>> findMenuDetailUrl(Map<String, Object> params);
}