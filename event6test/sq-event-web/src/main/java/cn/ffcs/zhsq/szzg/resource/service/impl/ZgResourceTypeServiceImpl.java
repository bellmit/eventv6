package cn.ffcs.zhsq.szzg.resource.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.resource.ZgResourceType;
import cn.ffcs.zhsq.mybatis.persistence.szzg.resource.ZgResourceTypeMapper;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceTypeService;

/**
 * @Description: zg_resource_type模块服务实现
 * @Author: huangwenbin
 * @Date: 09-16 10:01:50
 * @Copyright: 2017 福富软件
 */
@Service("zgResourceTypeServiceImpl")
@Transactional
public class ZgResourceTypeServiceImpl implements ZgResourceTypeService {

	@Autowired
	private ZgResourceTypeMapper zgResourceTypeMapper; //注入zg_resource_type模块dao


	/**
	 * 删除数据
	 * @param bo zg_resource_type业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Map<String,Object> param) {
		long result = zgResourceTypeMapper.delete(param);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return zg_resource_type分页数据对象
	 */
	@Override
	public EUDGPagination findList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<ZgResourceType> list = zgResourceTypeMapper.findPageListByCriteria(params, rowBounds);
		long count = zgResourceTypeMapper.findCountByCriteria(params);
		return new EUDGPagination(count, list);
	}

	/**
	 * 根据业务id查询数据
	 * @param id zg_resource_typeid
	 * @return zg_resource_type业务对象
	 */
	@Override
	public ZgResourceType findById(Long id) {
		return zgResourceTypeMapper.findById(id);
	}


	@Override
	public int moveNode(ZgResourceType bo) {
		return zgResourceTypeMapper.update(bo);
	}

	/**
	 * 查树
	 */
	@Override
	public List<ZgResourceType> findTree(Map<String, Object> param) {
		return zgResourceTypeMapper.findTree(param);
	}

	/**
	 * 查询typeCode 是否存在
	 * @return
	 */
	public int findByTypeCode(String typeCode){
		return zgResourceTypeMapper.findByTypeCode(typeCode);
	}

	@Override
	public int addOrUpdate(ZgResourceType entity) {
		if(entity.getResTypeId() !=null){
			return zgResourceTypeMapper.update(entity);
		}
		return zgResourceTypeMapper.insert(entity);
	}
	/**
	 * 是否有子项
	 */
	public int isHaveChildren(Long id){
		return zgResourceTypeMapper.isHaveChildren(id);
	}
	/**
	 * 获取详情地址
	 */
	public List<Map<String, Object>> findMenuDetailUrl(Map<String, Object> params){
		return zgResourceTypeMapper.findMenuDetailUrl(params);
	}
}