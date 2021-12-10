package cn.ffcs.zhsq.map.coordinateconversion.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.map.coordinateconversion.service.IConversionFormulaService;

/**
 * 红谷滩三维地图经纬度转换
 * @author Administrator
 *
 */
@Service(value="conversionFormulaOfHgt3dService")
public class ConversionFormulaOfHgt3dServiceImpl implements IConversionFormulaService{

	/**
	 * 2014-09-27 liushi add
	 * 返回map：
	 * lot经度
	 * lat维度
	 * @param x本地坐标x
	 * @param y本地坐标y
	 * @return
	 */
	@Override
	public Map<String,Object> conversionOflonlatToxy(Double lot, Double lat) {
		Map<String,Object> map = new HashMap<String,Object>();
		double a1=-34856725.1435511; 
		double a2=233579.427067049; 
		double a3=272045.511832879; 
		double b1=13035098.6218239; 
		double b2=-155264.688968232; 
		double b3 = 172998.475572514; 
		Double x = a1 + a2 * lot + a3 * lat; 
		Double y = b1 + b2 * lot + b3 * lat; 
		map.put("x", x);
		map.put("y", y);
		return map;
	}
	/**
	 * 2014-09-27 liushi add
	 * 返回map：
	 * x本地坐标x
	 * y本地坐标y
	 * @param lot经度lot
	 * @param lat维度lat
	 * @return
	 */
	@Override
	public Map<String,Object> conversionOfxyTolonlat(Double x, Double y) {
		Map<String,Object> map = new HashMap<String,Object>();
		double a1 = 115.86858216128; 
		double a2 = 2.09319750478101e-06; 
		double a3 = -3.29161852248095e-06; 
		double b1 = 28.6430312882165; 
		double b2 = 1.87862730265895e-06; 
		double b3 = 2.82619758519193e-06; 
		Double lot = a1 + a2 * x + a3 * y; 
		Double lat = b1 + b2 * x + b3 * y; 
		map.put("lot", lot);
		map.put("lat", lat);
		return map;
	}
	
}
