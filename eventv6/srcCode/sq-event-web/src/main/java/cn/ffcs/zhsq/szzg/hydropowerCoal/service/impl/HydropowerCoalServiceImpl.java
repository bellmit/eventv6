package cn.ffcs.zhsq.szzg.hydropowerCoal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.hydropowerCoal.HydropowerCoal;
import cn.ffcs.zhsq.mybatis.persistence.szzg.hydropowerCoal.HydropowerCoalMapper;
import cn.ffcs.zhsq.szzg.hydropowerCoal.IHydropowerCoalService;

/**
 * 居民水电煤模块服务
 * @author linzhu
 *
 */
@Service("hydropowerCoalServiceImpl")
@Transactional
public class HydropowerCoalServiceImpl implements IHydropowerCoalService {

	@Autowired
	private HydropowerCoalMapper hydropowerCoalMapper; //注入hydropowerCoal模块dao

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean insert(HydropowerCoal hydropowerCoal) {
		return hydropowerCoalMapper.insert(hydropowerCoal)>0;
	}

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean update(HydropowerCoal hydropowerCoal) {
		long result = hydropowerCoalMapper.update(hydropowerCoal);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean delete(HydropowerCoal hydropowerCoal) {
		long result = hydropowerCoalMapper.delete(hydropowerCoal);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<HydropowerCoal> list = hydropowerCoalMapper.searchList(params, rowBounds);
		long count = hydropowerCoalMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	@Override
	public HydropowerCoal searchById(Long id) {
		HydropowerCoal hydropowerCoal = hydropowerCoalMapper.searchById(id);
		return hydropowerCoal;
	}
	/**
	 * 批量删除信息
	 * @param userId
	 * @param idList
	 * @return
	 */
	@Override
	public boolean deleteByIds(Long userId, List<Long> idList) {
		return hydropowerCoalMapper.deleteByIds(userId, idList)>0;
	}

	@Override
	public boolean findByParams(Map<String, Object> params) {
		int count=hydropowerCoalMapper.findCountByParams(params);
		return count>0?true:false;
	}

	@Override
	public List<HydropowerCoal> getUsageDataByYear(String orgCode,String year,String type) {
		return hydropowerCoalMapper.getUsageDataByYear(orgCode,year,type);
	}

}