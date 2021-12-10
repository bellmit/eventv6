package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisDataOfLocalMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

/**
 * 刑释解教人员周边
 * 
 */
@Service(value = "zhouBianStatOfReleasedService")
public class ZhouBianStatOfReleasedServiceImpl implements IZhouBianStatService {
	@Autowired
	protected ZhouBianStatMapper zhouBianStatMapper;
	@Autowired
	private ArcgisDataOfLocalMapper arcgisDataOfLocalMapper;

	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		params.put("peopleTypeTableName", "T_ZZ_RELEASED_RECORD");
		params.put("empWay", null);
		//geoString, peopleTypeTableName, mapType, empWay,infoOrgCode
		
		int resultCount = this.zhouBianStatMapper.statOfZdPeopleZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("peopleTypeTableName", "T_ZZ_RELEASED_RECORD");
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
		String elementsCollectionStr = (String)params.get("elementsCollectionStr");
		params.put("peopleTypeTableName", "T_ZZ_RELEASED_RECORD");
		params.put("empWay", null);
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();

		list = this.arcgisDataOfLocalMapper.statOfZdPeopleZhouBianLocateData(params);
		if (StringUtils.isNotBlank(elementsCollectionStr) && list != null && list.size()>0) {
			for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		return list;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_people.ftl";
	}

}
