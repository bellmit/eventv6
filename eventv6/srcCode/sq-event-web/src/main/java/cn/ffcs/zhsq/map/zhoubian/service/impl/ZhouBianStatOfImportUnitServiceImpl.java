package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.domain.db.ImportUnit;
import cn.ffcs.shequ.zzgl.service.main.IImportUnitService;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

@Service(value="zhouBianStatOfImportUnitService")
public class ZhouBianStatOfImportUnitServiceImpl extends
		ApplicationObjectSupport implements IZhouBianStatService {

	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	
	@Autowired
	private IImportUnitService importUnitService;//重点单位
	
	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		return zhouBianStatMapper.statOfImportUnitZhouBianCount(params);
	}

	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(
			Map<String, Object> params) {
		return null;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize,
			Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		if(pageNo < 0){
			pageNo = 1;
		}
		if (pageNo == 1) {
			pagination.setTotal(this.zhouBianStatMapper.statOfImportUnitZhouBianCount(params));
		}
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		List<Map<String, Object>> list = this.zhouBianStatMapper.statOfImportUnitZhouBianListPage(params, rowBounds);
		formatImportUnit(list);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_importunit.ftl";
	}

	/**
	 * 格式化重点单位列表信息
	 * @param importUnitlist
	 */
	private void formatImportUnit(List<Map<String, Object>> importUnitlist){
		if(importUnitlist!=null && importUnitlist.size()>0){
			for(Map<String, Object> map : importUnitlist){
				Object importUnitIdObj = map.get("IMPORT_UNIT_ID");
				if(importUnitIdObj != null){
					Long importUnitId = Long.valueOf(importUnitIdObj.toString());
					ImportUnit importUnit = importUnitService.getImportUnitById(importUnitId);
					if(importUnit != null){
						map.put("TYPE_LABEL", importUnit.getTypeLabel());
					}
				}
			}
		}
	}
}
