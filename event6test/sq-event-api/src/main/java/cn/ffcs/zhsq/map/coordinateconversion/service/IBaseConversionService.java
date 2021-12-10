package cn.ffcs.zhsq.map.coordinateconversion.service;

import java.util.List;
import java.util.Map;

/**
 * 2014-09-27 liushi add
 * 手机上传经纬度与本地坐标相互转换处理类
 * @author Administrator
 *
 */
public interface IBaseConversionService {
	
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
	public List<Map<String,Object>> conversionOfxyTolonlat(String orgCode,Double x,Double y);
	
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
	public Map<String,Object> conversionOfxyTolonlat(String orgCode,Integer mapt,Double x,Double y);
	
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
	public List<Map<String,Object>> conversionOflonlatToxy(String orgCode,Double lot,Double lat);
	
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
	public Map<String,Object> conversionOflonlatToxy(String orgCode,Integer mapt,Double lot,Double lat);
	/**
	 * 2015-09-11 liushi add 获取当前机构的事件模块地图使用类型以及转换经纬度
	 * 手机上传事件时需要用gps定位数据进行地图定位，但是因使用地图不同有的需要进行经纬度转换后才能使用
	 * Map<String,Object>内容   mapt(String):地图类型     longitude(String)经度     dimension(String)纬度
	 * @param orgCode
	 * @param module
	 * @param longitude
	 * @param dimension
	 * @return
	 */
	public Map<String,Object> getMaptAndConversionXY(Long gridId,String orgCode,String module,String longitude,String dimension);
}
