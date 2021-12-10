package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

/**
 * 
 * @author huangmw
 *
 */
@Service(value="zhouBianStatOfPeopleService")
public class ZhouBianStatOfPeopleServiceImpl implements IZhouBianStatService {
	@Autowired
	protected ZhouBianStatMapper zhouBianStatMapper;

	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		return this.zhouBianStatMapper.statOfPeopleZhouBianCount(params);
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		// TODO Auto-generated method stub
		return null;
	}

}
