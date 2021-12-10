package cn.ffcs.zhsq.map.coordinateconversion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IConversionFormulaService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisConfigInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisInfoMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 经纬度转换对外接口
 * @author Administrator
 *
 */
@Service(value="baseConversionServiceImpl")
public class BaseConversionServiceImpl extends ApplicationObjectSupport implements IBaseConversionService{
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private ArcgisInfoMapper arcgisInfoMapper;
	
	/**
	 * 2014-09-27 liushi add
	 * 经纬度转换本地坐标,没转换服务就原值返回
	 * 参数：
	 * 1、orgCode信息域编码
	 * 2、经度lot
	 * 3、维度lat
	 * 返回值（不同类型的mapt会有多个map）如果返回值为空那么表示没有配置转换类：
	 * map：mapt，lot、lat
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String,Object>> conversionOflonlatToxy(String orgCode, Double lot,
			Double lat) {
		List<ArcgisConfigInfo> list = this.arcgisInfoMapper.findArcgisConfigInfoByGridCode(orgCode, ConstantValue.MAP_ENGINE_NAME);
		List<ArcgisConfigInfo> configs = new ArrayList<ArcgisConfigInfo>();
		Integer mapTypeCode = 0;
		String orgCodeOfConfig = "0";
		//获取当前机构配置的地图信息（包含有转换服务名称）
		if(list.size() > 0) {
			for(ArcgisConfigInfo arcgisConfigInfo : list) {
				if("0".equals(orgCodeOfConfig) || (orgCodeOfConfig == null && arcgisConfigInfo.getGridCode() == null ) || (orgCodeOfConfig != null && orgCodeOfConfig.equals(arcgisConfigInfo.getGridCode()))) {
					orgCodeOfConfig = arcgisConfigInfo.getGridCode();
					if(mapTypeCode != arcgisConfigInfo.getMapTypeCode()) {
						configs.add(arcgisConfigInfo);
						mapTypeCode = arcgisConfigInfo.getMapTypeCode();
					}
				}
			}
		}
		//根据转换服务名称获取转换服务进行转换
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		String maptStr = "";
		for(ArcgisConfigInfo arcgisConfigInfo:configs) {
			//必须确定没有转换过同类型的数据
			if(maptStr.indexOf(String.valueOf(arcgisConfigInfo.getMapType()))<0) {
				maptStr = maptStr+","+String.valueOf(arcgisConfigInfo.getMapType());
				//有转换服务进行转换，没转换服务就原值返回
				if(arcgisConfigInfo.getCoordinateServiceName() != null && !"".equals(arcgisConfigInfo.getCoordinateServiceName())) {
					IConversionFormulaService conversionFormulaService = (IConversionFormulaService)this.getApplicationContext().getBean(arcgisConfigInfo.getCoordinateServiceName());
					Map<String,Object> map = conversionFormulaService.conversionOflonlatToxy(lot, lat);
					map.put("mapt", arcgisConfigInfo.getMapType());
					map.put("x", (Double)map.get("x"));
					map.put("y", (Double)map.get("y"));
					results.add(map);
				}else if(arcgisConfigInfo.getCoordinateServiceName() == null || "".equals(arcgisConfigInfo.getCoordinateServiceName())){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("mapt", arcgisConfigInfo.getMapType());
					map.put("x", lot);
					map.put("y", lat);
					results.add(map);
				}
				
			}
		}
		return results;
	}
	
	/**
	 * 2014-09-29 liushi add
	 * 只根据传入的地图类型进行转换，经纬度转换本地坐标,没转换服务就原值返回
	 * 返回值： map ： x   y
	 * @param orgCode
	 * @param mapt
	 * @param lot
	 * @param lat
	 * @return
	 */
	@Override
	public Map<String,Object> conversionOflonlatToxy(String orgCode,Integer mapt, Double lot,
			Double lat) {
		List<ArcgisConfigInfo> list = this.arcgisInfoMapper.findArcgisConfigInfoByGridCode(orgCode, ConstantValue.MAP_ENGINE_NAME);
		List<ArcgisConfigInfo> configs = new ArrayList<ArcgisConfigInfo>();
		Integer mapTypeCode = 0;
		String orgCodeOfConfig = "0";
		//获取当前机构配置的地图信息（包含有转换服务名称）
		if(list.size() > 0) {
			for(ArcgisConfigInfo arcgisConfigInfo : list) {
				if("0".equals(orgCodeOfConfig) || (orgCodeOfConfig == null && arcgisConfigInfo.getGridCode() == null ) || (orgCodeOfConfig != null && orgCodeOfConfig.equals(arcgisConfigInfo.getGridCode()))) {
					orgCodeOfConfig = arcgisConfigInfo.getGridCode();
					if(mapTypeCode != arcgisConfigInfo.getMapTypeCode()) {
						configs.add(arcgisConfigInfo);
						mapTypeCode = arcgisConfigInfo.getMapTypeCode();
					}
				}
			}
		}
		//根据转换服务名称获取转换服务进行转换
		Map<String,Object> result = new HashMap<String,Object>();
		String maptStr = "";
		Double x = lot;
		Double y = lat;
		for(ArcgisConfigInfo arcgisConfigInfo:configs) {
			//必须确定没有转换过同类型的数据
			if(maptStr.indexOf(String.valueOf(arcgisConfigInfo.getMapType()))<0 && mapt.equals(arcgisConfigInfo.getMapType()) ) {
				//有转换服务进行转换，没转换服务就原值返回
				if(arcgisConfigInfo.getCoordinateServiceName() != null && !"".equals(arcgisConfigInfo.getCoordinateServiceName())) {
					IConversionFormulaService conversionFormulaService = (IConversionFormulaService)this.getApplicationContext().getBean(arcgisConfigInfo.getCoordinateServiceName());
					Map<String,Object> map = conversionFormulaService.conversionOflonlatToxy(lot, lat);
					x = (Double)map.get("x");
					y = (Double)map.get("y");
				}else if(arcgisConfigInfo.getCoordinateServiceName() == null || "".equals(arcgisConfigInfo.getCoordinateServiceName())){
					x = lot;
					y = lat;
				}
			}
		}
		result.put("x", x);
		result.put("y", y);
		return result;
	}
	/**
	 * 2014-09-27 liushi add
	 * 本地坐标转换为经纬度,没转换服务就原值返回
	 * 参数：
	 * 1、orgCode信息域编码
	 * 2、x本地坐标x
	 * 3、y本地坐标y
	 * 返回值（不同类型的mapt会有多个map）如果返回值为空那么表示没有配置转换类：
	 * map：mapt，lot、lat
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String,Object>> conversionOfxyTolonlat(String orgCode, Double x, Double y) {
		List<ArcgisConfigInfo> list = this.arcgisInfoMapper.findArcgisConfigInfoByGridCode(orgCode, ConstantValue.MAP_ENGINE_NAME);
		List<ArcgisConfigInfo> configs = new ArrayList<ArcgisConfigInfo>();
		Integer mapTypeCode = 0;
		String orgCodeOfConfig = "0";
		//获取当前机构配置的地图信息（包含有转换服务名称）
		if(list.size() > 0) {
			for(ArcgisConfigInfo arcgisConfigInfo : list) {
				if("0".equals(orgCodeOfConfig) || (orgCodeOfConfig == null && arcgisConfigInfo.getGridCode() == null ) || (orgCodeOfConfig != null && orgCodeOfConfig.equals(arcgisConfigInfo.getGridCode()))) {
					orgCodeOfConfig = arcgisConfigInfo.getGridCode();
					if(mapTypeCode != arcgisConfigInfo.getMapTypeCode()) {
						configs.add(arcgisConfigInfo);
						mapTypeCode = arcgisConfigInfo.getMapTypeCode();
					}
				}
			}
		}
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		String maptStr = "";
		for(ArcgisConfigInfo arcgisConfigInfo:configs) {
			//必须确定没有转换过同类型的数据
			if(maptStr.indexOf(String.valueOf(arcgisConfigInfo.getMapType()))<0) {
				maptStr = maptStr+","+String.valueOf(arcgisConfigInfo.getMapType());
				//有转换服务进行转换，没转换服务就原值返回
				if(arcgisConfigInfo.getCoordinateServiceName() != null && !"".equals(arcgisConfigInfo.getCoordinateServiceName())) {
					IConversionFormulaService conversionFormulaService = (IConversionFormulaService)this.getApplicationContext().getBean(arcgisConfigInfo.getCoordinateServiceName());
					Map<String,Object> map = conversionFormulaService.conversionOfxyTolonlat(x, y);
					map.put("mapt", arcgisConfigInfo.getMapType());
					map.put("lot", (Double)map.get("lot"));
					map.put("lat", (Double)map.get("lat"));
					results.add(map);
				}else if(arcgisConfigInfo.getCoordinateServiceName() == null || "".equals(arcgisConfigInfo.getCoordinateServiceName())){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("mapt", arcgisConfigInfo.getMapType());
					map.put("lot", x);
					map.put("lat", y);
					results.add(map);
				}
				
			}
		}
		return results;
	}
	/**
	 * 2014-09-29 liushi add
	 * 只根据传入的地图类型进行转换，本地坐标转换为经纬度,没转换服务就原值返回
	 * 返回值：map:  经度lot   纬度lat
	 * @param orgCode
	 * @param mapt
	 * @param x
	 * @param y
	 * @return
	 */
	@Override
	public Map<String,Object> conversionOfxyTolonlat(String orgCode,Integer mapt, Double x,
			Double y) {
		List<ArcgisConfigInfo> list = this.arcgisInfoMapper.findArcgisConfigInfoByGridCode(orgCode, ConstantValue.MAP_ENGINE_NAME);
		List<ArcgisConfigInfo> configs = new ArrayList<ArcgisConfigInfo>();
		Integer mapTypeCode = 0;
		String orgCodeOfConfig = "0";
		//获取当前机构配置的地图信息（包含有转换服务名称）
		if(list.size() > 0) {
			for(ArcgisConfigInfo arcgisConfigInfo : list) {
				if("0".equals(orgCodeOfConfig) || (orgCodeOfConfig == null && arcgisConfigInfo.getGridCode() == null ) || (orgCodeOfConfig != null && orgCodeOfConfig.equals(arcgisConfigInfo.getGridCode()))) {
					orgCodeOfConfig = arcgisConfigInfo.getGridCode();
					if(mapTypeCode != arcgisConfigInfo.getMapTypeCode()) {
						configs.add(arcgisConfigInfo);
						mapTypeCode = arcgisConfigInfo.getMapTypeCode();
					}
				}
			}
		}
		//根据转换服务名称获取转换服务进行转换
		Map<String,Object> result = new HashMap<String,Object>();
		String maptStr = "";
		Double lot = 0D;
		Double lat = 0D;
		for(ArcgisConfigInfo arcgisConfigInfo:configs) {
			//必须确定没有转换过同类型的数据
			if(maptStr.indexOf(String.valueOf(arcgisConfigInfo.getMapType()))<0 && mapt.equals(arcgisConfigInfo.getMapType())) {
				//有转换服务进行转换，没转换服务就原值返回
				if(arcgisConfigInfo.getCoordinateServiceName() != null && !"".equals(arcgisConfigInfo.getCoordinateServiceName())) {
					IConversionFormulaService conversionFormulaService = (IConversionFormulaService)this.getApplicationContext().getBean(arcgisConfigInfo.getCoordinateServiceName());
					Map<String,Object> map = conversionFormulaService.conversionOfxyTolonlat(x, y);
					lot = (Double)map.get("lot");
					lat = (Double)map.get("lat");
				}else if(arcgisConfigInfo.getCoordinateServiceName() == null || "".equals(arcgisConfigInfo.getCoordinateServiceName())){
					lot = x;
					lat = y;
				}
			}
		}
		result.put("lot", lot);
		result.put("lat", lat);
		return result;
	}
	/**
	 * 2015-09-11 liushi add 获取当前机构的事件模块地图使用类型以及转换经纬度
	 * 手机上传事件时需要用gps定位数据进行地图定位，但是因使用地图不同有的需要进行经纬度转换后才能使用
	 * Map<String,Object>内容  (String) mapt:地图类型     (String)longitude经度     (String)dimension纬度
	 * @param infoOrgCode
	 * @param module
	 * @param longitude
	 * @param dimension
	 * @return
	 */
	@Override
	public Map<String,Object> getMaptAndConversionXY(Long gridId,String orgCode,String module,String longitude,String dimension){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		MixedGridInfo gridInfo = null;
		String mapEngineType = null;
		String mapDimension = null;
		String mapt = "5";
		
		if(Double.valueOf(longitude)>180 || Double.valueOf(longitude)<-180 || Double.valueOf(dimension)>90 || Double.valueOf(dimension)<-90) {
			return null;
		}else if(Double.valueOf(longitude) < Double.valueOf(dimension)) {
			String x = dimension;
			dimension = longitude;
			longitude = x;
		}
		
		if(gridId != null && gridId > 0) {
			gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		}
		
		if(gridInfo != null) {
			mapEngineType = gridInfo.getMapType();
			mapDimension = this.funConfigurationService.turnCodeToValue(this.MAP_TYPE_CODE, module,IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			
			// 如果是新地图引擎
			if (mapEngineType.equals(this.NEW_MAP_ENGINE)) {
				// 有配置值
				if (StringUtils.isNotBlank(mapDimension)) {
					if (mapDimension.equals(this.TWO_DIMENSION)) { // 二维
						mapt = this.TWO_DIMENSION_MAPT_OF_NEWMAP; // 二维mapt为5
					} else if (mapDimension.equals(this.THREE_DIMENSION)) { // 三维
						mapt = this.THREE_DIMENSION_MAPT_OF_NEWMAP; // 三维mapt为30
					}
				}
			} else if (mapEngineType.equals(this.OLD_MAP_ENGINE)) { // 旧地图引擎
				// 有配置值
				if (StringUtils.isNotBlank(mapDimension)) {
					if (mapDimension.equals(this.TWO_DIMENSION)) { // 二维
						mapt = this.TWO_DIMENSION_MAPT_OF_OLDMAP;// 二维mapt为2
					} else if (mapDimension.equals(this.THREE_DIMENSION)) {// 三维
						mapt = this.THREE_DIMENSION_MAPT_OF_OLDMAP;// 三维mapt为20
					}
				}
			}
			
			Map<String,Object> res = this.conversionOflonlatToxy(gridInfo.getInfoOrgCode(), Integer.valueOf(mapt),Double.valueOf(longitude), Double.valueOf(dimension));
			resultMap.put("mapt", mapt);
			resultMap.put("longitude", ((Double)res.get("x")).toString());
			resultMap.put("dimension", ((Double)res.get("y")).toString());
		}
		
		return resultMap;
	}
	// 地图标注使用的地图维度（二维、三维）
	private final String MAP_TYPE_CODE = "MAP_TYPE_CODE";
	// 新地图
	private final String NEW_MAP_ENGINE = "005";
	// 旧地图
	private final String OLD_MAP_ENGINE = "004";
	// 二维
	private final String TWO_DIMENSION = "2";
	// 三维
	private final String THREE_DIMENSION = "3";
	// 新地图二维mapt
	private final String TWO_DIMENSION_MAPT_OF_NEWMAP = "5";
	// 新地图三维mapt
	private final String THREE_DIMENSION_MAPT_OF_NEWMAP = "30";
	// 旧地图二维mapt
	private final String TWO_DIMENSION_MAPT_OF_OLDMAP = "2";
	// 旧地图三维mapt
	private final String THREE_DIMENSION_MAPT_OF_OLDMAP = "20";
}
