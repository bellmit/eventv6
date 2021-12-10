package cn.ffcs.zhsq.mybatis.persistence.szzg.resource;


import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceType;


/**
 * @Description: zg_resource_type模块dao接口
 * @Author: huangwenbin
 * @Date: 09-16 10:01:50
 * @Copyright: 2017 福富软件
 */
public interface ZgResourceTypeMapper extends  MyBatisBaseMapper<ZgResourceType>{
	
	/**
	 * 查树
	 * @param param
	 * @return
	 */
	public List<ZgResourceType> findTree(Map<String,Object> param);

	/**
	 * 查询父节点及子节点 
	 * @param params
	 * @return
	 */
	public List<ZgResourceType> zgResourceTypeMapper(Map<String, Object> params);

	/**
	 * 查询typeCode 是否存在
	 * @return
	 */
	public int findByTypeCode(String typeCode);

	public long delete(Map<String, Object> param);
	/**
	 * 是否有子项
	 */
	public int isHaveChildren(Long id);
	/**
	 * 获取详情地址
	 */
	public List<Map<String, Object>> findMenuDetailUrl(Map<String, Object> params);
	
}