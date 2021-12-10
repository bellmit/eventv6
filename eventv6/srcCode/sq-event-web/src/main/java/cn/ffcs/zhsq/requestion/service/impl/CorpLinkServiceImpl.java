package cn.ffcs.zhsq.requestion.service.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.requestion.CorpLink;
import cn.ffcs.zhsq.mybatis.persistence.requestion.CorpLinkMapper;
import cn.ffcs.zhsq.requestion.service.ICorpLinkService;

/**
 * @Description: 企业联动单位模块服务实现
 * @Author: caiby
 * @Date: 03-12 10:28:45
 * @Copyright: 2018 福富软件
 */
@Service("corpLinkServiceImpl")
@Transactional
public class CorpLinkServiceImpl implements ICorpLinkService {

	@Autowired
	private CorpLinkMapper corpLinkMapper; //注入企业联动单位模块dao

	/**
	 * 新增数据
	 * @param bo 企业联动单位业务对象
	 * @return 企业联动单位id
	 */
	@Override
	public Long insert(CorpLink bo) {
		corpLinkMapper.insert(bo);
		return bo.getCluId();
	}

	/**
	 * 修改数据
	 * @param bo 企业联动单位业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(CorpLink bo) {
		long result = corpLinkMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 企业联动单位业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(CorpLink bo) {
		long result = corpLinkMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 企业联动单位分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<CorpLink> list = corpLinkMapper.searchList(params, rowBounds);
		long count = corpLinkMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 企业联动单位id
	 * @return 企业联动单位业务对象
	 */
	@Override
	public CorpLink searchById(Long id) {
		CorpLink bo = corpLinkMapper.searchById(id);
		return bo;
	}
	
	/**
	 * 根据业务gridId查询数据
	 * @param gridId 组织树选择的单位gridId
	 * @return 企业联动单位业务对象
	 */
	@Override
	public CorpLink searchByGridId(Long id) {
		CorpLink bo = corpLinkMapper.searchByGridId(id);
		return bo;
	}

}