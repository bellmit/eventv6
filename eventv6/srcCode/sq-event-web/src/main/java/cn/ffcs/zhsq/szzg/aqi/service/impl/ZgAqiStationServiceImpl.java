package cn.ffcs.zhsq.szzg.aqi.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAqiStation;
import cn.ffcs.zhsq.mybatis.persistence.szzg.aqi.ZgAqiStationMapper;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAqiStationService;
import cn.ffcs.zhsq.utils.AqiUtil;
import cn.ffcs.zhsq.utils.DateUtils;
/**
 * 检测站点实现类
 * @author Administrator
 *
 */
@Service(value="zgAqiStationServiceImpl")
public class ZgAqiStationServiceImpl implements IZgAqiStationService{
	@Autowired
	private ZgAqiStationMapper zgAqiStationMapper;
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 空气质量监测站点信息表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<ZgAqiStation> list = zgAqiStationMapper.searchList(params, rowBounds);
		for (ZgAqiStation zgAqiStation : list) {
			if(zgAqiStation!=null){
				zgAqiStation.getZgAQI().setState(AqiUtil.getStateByAqi(zgAqiStation.getZgAQI().getAqi()));
				zgAqiStation.getZgAQI().setStateName(AqiUtil.getStateNameByAqi(zgAqiStation.getZgAQI().getAqi()));
			}
		}
		long count = zgAqiStationMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 新增数据
	 * @param bo 空气质量监测站点信息表业务对象
	 * @return 空气质量监测站点信息表id
	 */
	@Override
	public boolean insert(ZgAqiStation zgAqiStation) {
		return zgAqiStationMapper.insert(zgAqiStation)>0;
	}
	@Override
	public int findCountByParams(Map<String, Object> params) {
		return zgAqiStationMapper.findCountByParams(params);
	}
	@Override
	public List<ZgAqiStation> getStationListByParams(
			Map<String, Object> params) {
		return zgAqiStationMapper.getStationListByParams(params);
	}
	@Override
	public List<ArcgisInfoOfPublic> getArcgisInfosDataListByIds(String ids,
			Integer mapt, String markerType) {
		return zgAqiStationMapper.getArcgisInfosDataListByIds(ids, mapt, markerType);
	}
	
	/**
	 * 得到当前时间的前N小时
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getBeforeByHourTime(int ihour){ 
		String returnstr = ""; 
		Calendar calendar = Calendar.getInstance(); 
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);  
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		returnstr = df.format(calendar.getTime());  
		return returnstr;  
		}
	@Override
	public ZgAqiStation searchById(Map<String, Object> params) {
		return zgAqiStationMapper.searchById(params);
	}
	@Override
	public List<Map<String, Object>> getDataListByStation(Map<String, Object> params) {
		List<Map<String, Object>> list=zgAqiStationMapper.getDataListByStation(params);
		for (Map<String, Object> map : list) { 
			if(map.get("MONITORTIME")!=null) {
				Date monitortime=(Date)map.get("MONITORTIME");
				map.put("DAY_STR", DateUtils.formatDate(monitortime, "yyyy年MM月dd日")); 
				map.put("HOUR_STR", DateUtils.formatDate(monitortime, "dd日HH时")) ;
			}
		}
		return list;
	}
 

}
