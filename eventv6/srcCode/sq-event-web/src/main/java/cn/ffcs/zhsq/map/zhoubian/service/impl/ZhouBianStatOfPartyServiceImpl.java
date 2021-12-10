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
 * 党员周边
 * 
 */
@Service(value = "zhouBianStatOfPartyService")
public class ZhouBianStatOfPartyServiceImpl implements IZhouBianStatService {
	@Autowired
	protected ZhouBianStatMapper zhouBianStatMapper;

	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		params.put("peopleTypeTableName", "T_DC_CI_RS_PARTY");
		params.put("empWay", null);
		//geoString, peopleTypeTableName, mapType, empWay,infoOrgCode
		
		int resultCount = this.zhouBianStatMapper.statOfZdPeopleZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("peopleTypeTableName", "T_DC_CI_RS_PARTY");
		params.put("empWay", null);
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(this.zhouBianStatMapper.statOfZdPeopleZhouBianCount(params));
		}
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = this.zhouBianStatMapper.statOfZdPeopleZhouBianListPage(params, rowBounds);
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
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_people.ftl";
	}

}
