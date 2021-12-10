package cn.ffcs.zhsq.mybatis.persistence.szzg.resource;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: zg_resource_info模块dao接口
 * @Author: huangwenbin
 * @Date: 09-16 10:02:32
 * @Copyright: 2017 福富软件
 */
public interface ZgResourceInfoMapper extends  MyBatisBaseMapper<ZgResourceInfo>{
	
	/**
	 * 删除数据
	 * @param bo zg_resource_info业务对象
	 * @return 删除的记录数
	 */
	public int deleteByBean(ZgResourceInfo bo);
	/**
	 * 查询 资源类型 是否存在
	 * @param typeCode 资源类型
	 */
	public int findByTypeCode(Map<String, Object> params);
	
	/**
	 * resTableIds
	 * updateUser
	 * resTypeId
	 */
	public int deleteByList(Map<String, Object> params);
	
	public int insertByList(List<ZgResourceInfo> list);
	
	public int updateByList(List<ZgResourceInfo> list);
	
	/**
	 * 查询数据
	 * @param name 部件名称可选
	 * @param stype 多字典编码	'','',''
	 * @param xmin,xmax,ymin,ymax
	 * @return 数据列表
	 */
	public List<ZgResourceInfo> findByParam(Map<String, Object> params);
	
	public Map<String,Object> findComponentResource(Map<String, Object> params);
	
	public Map<String,Object> findResource(Map<String, Object> params);
	
	public List<Map<String,Object>> findComponentByParam(Map<String, Object> params);

	/**
	 * 资源条数带类型带网格查询(v3搬过来的)
	 * @param param
	 * @return
	 */
	public int findCountByParam(Map<String, Object> param);

	/**
	 * 源列表带类型带网格查询(v3搬过来的)
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String,Object>> findListByParam(Map<String, Object> params, RowBounds bounds);


}