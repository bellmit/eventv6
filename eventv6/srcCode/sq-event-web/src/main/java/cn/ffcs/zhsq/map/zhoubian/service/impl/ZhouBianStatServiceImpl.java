package cn.ffcs.zhsq.map.zhoubian.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;

/**
 * 2015-03-12 周边资源对外接口实现类
 * @author liush
 *
 */
@Service(value="zhouBianStatService")
public class ZhouBianStatServiceImpl extends ApplicationObjectSupport implements IZhouBianStatService{
	private static final Map<String, Object> keyPersonMap;
	static {
		keyPersonMap = new HashMap<String, Object>();
		//吸毒、矫正、邪教、刑释解教、精神病患者、信访、危险品从业人员
		keyPersonMap.put("drugs", "zhouBianStatOfDrugService");//吸毒
		keyPersonMap.put("rectify", "zhouBianStatOfCorrectionalService");//矫正
		keyPersonMap.put("camps", "zhouBianStatOfReleasedService");//刑释解教
		keyPersonMap.put("petition", "zhouBianStatOfPetitionRecordService");//上访
		keyPersonMap.put("neuropathy", "zhouBianStatOfMentalService");//重精神病
		keyPersonMap.put("dangerous", "zhouBianStatOfDangrousService");//危险品从业人员
		keyPersonMap.put("heresy", "zhouBianStatOfCultService");//邪教
	}
	@Autowired
	private IMenuConfigService menuConfigService;
	
	@Override
	public String statOfZhouBianIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int statOfZhouBianCount(Map<String, Object> params) {
		String zhoubianType = (String)params.get("zhoubianType");
		IZhouBianStatService zhouBianStatService = (IZhouBianStatService)this.getApplicationContext().getBean(zhoubianType);
		int resultCount = zhouBianStatService.statOfZhouBianCount(params);
		return resultCount;
	}

	@Override
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		String zhoubianType = (String)params.get("zhoubianType");
		IZhouBianStatService zhouBianStatService = (IZhouBianStatService)this.getApplicationContext().getBean(zhoubianType);
		return zhouBianStatService.statOfZhouBianList(pageNo, pageSize, params);
	}

	@Override
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(
			Map<String, Object> params) {
		String zhoubianTypeStr = (String)params.get("zhoubianType");
		String elementsCollectionStr = (String)params.get("elementsCollectionStr");
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();

		//如果是查询周边的重点人员的话，就查询七类重点人员
		if(StringUtils.isNotBlank(zhoubianTypeStr)){
			String zhoubianType = (String) keyPersonMap.get(zhoubianTypeStr);
			if(StringUtils.isNotBlank(zhoubianType)){
				params.put("zhoubianType",zhoubianType);
//				GisDataCfg gisDataCfg = menuConfigService.getGisDataCfgByCode(zhoubianType,null);
//				if(gisDataCfg != null && StringUtils.isNotBlank(gisDataCfg.getElementsCollectionStr())){
//					params.put("elementsCollectionStr",gisDataCfg.getElementsCollectionStr());
//				}
				if(StringUtils.isNotBlank(elementsCollectionStr)){
					params.put("elementsCollectionStr",elementsCollectionStr);
				}
				IZhouBianStatService zhouBianStatService = (IZhouBianStatService)this.getApplicationContext().getBean(zhoubianType);
				List<ArcgisInfoOfPublic> subList = zhouBianStatService.statOfZhouBianMapInfoList(params);
				if(subList != null && subList.size()>0){
					list.addAll(subList);
				}
			}else{
				IZhouBianStatService zhouBianStatService = (IZhouBianStatService)this.getApplicationContext().getBean(zhoubianTypeStr);
				list = zhouBianStatService.statOfZhouBianMapInfoList(params);
			}
		}

		return list;
	}

	@Override
	public String getZhouBianPagePath(String zhoubianType) {
		IZhouBianStatService zhouBianStatService = (IZhouBianStatService) this.getApplicationContext().getBean(zhoubianType);
		return zhouBianStatService.getZhouBianPagePath(zhoubianType);
	}

}
