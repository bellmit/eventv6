package cn.ffcs.zhsq.map.coordinateconversion.service;

import java.util.List;
import java.util.Map;

/**
 * 2014-09-27 liushi add
 * 转换公式接口
 * @author Administrator
 *
 */
public interface IConversionFormulaService {
	
	/**
	 * 2014-09-27 liushi add
	 * 返回map：
	 * lot经度
	 * lat维度
	 * @param x本地坐标x
	 * @param y本地坐标y
	 * @return
	 */
	public Map<String,Object> conversionOfxyTolonlat(Double x, Double y);
	
	/**
	 * 2014-09-27 liushi add
	 * 返回map：
	 * x本地坐标x
	 * y本地坐标y
	 * @param lot经度lot
	 * @param lat维度lat
	 * @return
	 */
	public Map<String,Object> conversionOflonlatToxy(Double lot, Double lat);
}
