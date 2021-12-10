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
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 周边资源现役消防队实现类
 *
 */
@Service(value="zhouBianStatOfActiveFireTeamService")
public class ZhouBianStatOfActiveFireTeamServiceImpl implements IZhouBianStatService {
	
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	
	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		params.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		
		int resultCount = this.zhouBianStatMapper.statOfFireTeamNewZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(this.zhouBianStatMapper.statOfFireTeamNewZhouBianCount(params));
		}
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = this.zhouBianStatMapper.statOfFireTeamNewZhouBianListPage(params, rowBounds);
		pagination.setRows(list);
		return pagination;
	}
	
	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(Map<String, Object> params){
		return null;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_activefireteam.ftl";
	}

}
