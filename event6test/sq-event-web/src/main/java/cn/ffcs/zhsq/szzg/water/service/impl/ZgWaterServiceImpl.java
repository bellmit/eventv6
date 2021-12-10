package cn.ffcs.zhsq.szzg.water.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.water.ZgWater;
import cn.ffcs.zhsq.mybatis.persistence.szzg.water.ZgWaterMapper;
import cn.ffcs.zhsq.szzg.water.service.IZgWaterService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 水质量模块服务实现
 * @Author: linzhu
 * @Date: 08-01 17:05:32
 * @Copyright: 2017 福富软件
 */
@Service("zgWaterServiceImpl")
@Transactional
public class ZgWaterServiceImpl implements IZgWaterService {

	@Autowired
	private ZgWaterMapper zgWaterMapper; //注入水质量模块dao
	@Autowired
	private IResMarkerService resMarkerService;

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean insert(ZgWater zgWater) {
		int result=zgWaterMapper.insert(zgWater);
		if (result > 0) {
			ResMarker resMarker = zgWater.getResMarker();
			if (resMarker != null && !StringUtils.isBlank(resMarker.getMapType())
					&& !StringUtils.isBlank(resMarker.getX()) && !StringUtils.isBlank(resMarker.getY())) {
			    resMarker.setCatalog("03");
				resMarker.setResourcesId(zgWater.getSeqId());
				resMarker.setMarkerType(ConstantValue.MAP_TYPE_ZG_WATER);
				resMarkerService.saveOrUpdateResMarker(resMarker);
			}
		}
		return result>0;
	}

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean update(ZgWater zgWater) {
		int result = zgWaterMapper.update(zgWater);
		if (result > 0) {
			ResMarker resMarker = zgWater.getResMarker();
			if (resMarker != null && !StringUtils.isBlank(resMarker.getMapType())
					&& !StringUtils.isBlank(resMarker.getX()) && !StringUtils.isBlank(resMarker.getY())) {
				resMarker.setCatalog("03");
				resMarker.setResourcesId(zgWater.getSeqId());
				resMarker.setMarkerType(ConstantValue.MAP_TYPE_ZG_WATER);
				resMarkerService.saveOrUpdateResMarker(resMarker);
			}
		}
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	@Override
	public boolean delete(ZgWater bo) {
		long result = zgWaterMapper.delete(bo);
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
		List<ZgWater> list = zgWaterMapper.searchList(params, rowBounds);
		long count = zgWaterMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	@Override
	public ZgWater searchById(Long id) {
		ZgWater bo = zgWaterMapper.searchById(id);
		return bo;
	}

	@Override
	public boolean isExist(Map<String, Object> params) {
		return zgWaterMapper.findCountByParams(params)>0;
	}

	@Override
	public List<ZgWater> findListByMaxEndTime(String orgCode) {
		return zgWaterMapper.findListByMaxEndTime(orgCode);
	}

	@Override
	public List<ZgWater> getZgWaterByDay14(String orgCode) {
		return zgWaterMapper.getZgWaterByDay14(orgCode);
	}

}