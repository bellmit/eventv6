package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 
 * @author huangmw
 *
 */
@Service(value="zhouBianStatOfGlobalEyesService")
public class ZhouBianStatOfGlobalEyesServiceImpl implements IZhouBianStatService {
	@Autowired
	protected ZhouBianStatMapper zhouBianStatMapper;
	@Autowired
	private MonitorService monitorService; // 全球眼服务

	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		int resultCount = this.zhouBianStatMapper.statOfGlobalEyesZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize,
			Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		if(CommonFunctions.isNotBlank(params, "withoutPage")&&params.get("withoutPage").toString().equals("1")) {
			pagination.setTotal(this.zhouBianStatMapper.statOfGlobalEyesZhouBianCount(params));
			list = this.zhouBianStatMapper.statOfGlobalEyesZhouBianListWithoutPage(params);
		}else {
			
			if (pageNo == 1) {
				pagination.setTotal(this.zhouBianStatMapper.statOfGlobalEyesZhouBianCount(params));
			}
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			list = this.zhouBianStatMapper.statOfGlobalEyesZhouBianListPage(params, rowBounds);
		}
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_globaleyes.ftl";
	}

}
