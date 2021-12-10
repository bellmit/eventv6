package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.common.DictPcode;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.utils.DataDictHelper;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 2015-03-12 周边资源消防队实现类
 * @author liush
 *
 */
@Service(value="zhouBianStatOfGridAdminService")
public class ZhouBianStatOfGridAdminServiceImpl extends ApplicationObjectSupport implements IZhouBianStatService{
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	
	@Autowired
	IBaseDictionaryService baseDictionaryService;
	
	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		int resultCount = this.zhouBianStatMapper.statOfGridAdminZhouBianCount(params);
		return resultCount;
	}

	/**
	 * 
	 */
	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		if(CommonFunctions.isBlank(params, "x")||CommonFunctions.isBlank(params, "y")) {
			pagination.setTotal(0);
			pagination.setRows(new ArrayList<Map<String,Object>>());
			return pagination;
		}
		//System.out.println("params.get(\"infoOrgCode\")---"+params.get("infoOrgCode"));
		//System.out.println(params.get("infoOrgCode").toString().startsWith("320281"));
		List<Map<String,Object>> gridAdminZhouBianList = new ArrayList<Map<String, Object>>();
		if(CommonFunctions.isNotBlank(params, "withoutPage")) {
			if (params.get("infoOrgCode") != null && params.get("infoOrgCode").toString().startsWith("3202")) {
				pagination.setTotal(this.zhouBianStatMapper.statJYGridAdminZhouBianCount(params));
			} else {
				pagination.setTotal(this.zhouBianStatMapper.statOfGridAdminZhouBianCount(params));
			}
			
			
			if(params.get("infoOrgCode")!=null && params.get("infoOrgCode").toString().startsWith("3202")){
				gridAdminZhouBianList = this.zhouBianStatMapper.statJYGridAdminZhouBianListWithoutPage(params);
			}else{
				gridAdminZhouBianList = this.zhouBianStatMapper.statOfGridAdminZhouBianListWithoutPage(params);
			}

		}else {
			if (pageNo == 1) {
				//江阴特殊处理
				if(params.get("infoOrgCode")!=null && params.get("infoOrgCode").toString().startsWith("3202"))
					pagination.setTotal(this.zhouBianStatMapper.statJYGridAdminZhouBianCount(params));
				else
					pagination.setTotal(this.zhouBianStatMapper.statOfGridAdminZhouBianCount(params));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

			if(params.get("infoOrgCode")!=null && params.get("infoOrgCode").toString().startsWith("3202")){
				gridAdminZhouBianList = this.zhouBianStatMapper.statJYGridAdminZhouBianListPage(params, rowBounds);
			}else{
				gridAdminZhouBianList = this.zhouBianStatMapper.statOfGridAdminZhouBianListPage(params, rowBounds);
			}
//	        List<Map<String,Object>> gridAdminZhouBianList = this.zhouBianStatMapper.statOfGridAdminZhouBianListPage(params, rowBounds);
	        
		}
		
		List<BaseDataDict> dutyDC = baseDictionaryService.getDataDictListOfSinglestage(DictPcode.GRID_ADMIN_DUTY, params.get("infoOrgCode").toString());
		if(gridAdminZhouBianList.size() > 0){
			for(Map<String, Object> m : gridAdminZhouBianList){
				DataDictHelper.setDictValueForField(m, "DUTY", "DUTY_LABEL", dutyDC);
			}
		}
		
		pagination.setRows(gridAdminZhouBianList);
		return pagination;
	}
	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(Map<String, Object> params){
		return null;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_gridadmin.ftl";
	}

}
