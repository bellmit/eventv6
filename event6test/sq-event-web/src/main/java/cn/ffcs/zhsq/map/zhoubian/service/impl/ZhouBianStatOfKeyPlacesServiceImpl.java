package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

/**
 * @author YangCQ
 */
@Service(value="zhouBianStatOfKeyPlacesService")
public class ZhouBianStatOfKeyPlacesServiceImpl implements IZhouBianStatService {
	
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;

	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		int resultCount = this.zhouBianStatMapper.statOfKeyPlacesZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(this.zhouBianStatMapper.statOfKeyPlacesZhouBianCount(params));
		}
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = this.zhouBianStatMapper.statOfKeyPlacesZhouBianListPage(params, rowBounds);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_keyplaces.ftl";
	}

}
