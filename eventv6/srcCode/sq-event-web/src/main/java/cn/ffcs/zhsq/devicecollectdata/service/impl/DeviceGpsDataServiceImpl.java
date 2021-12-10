package cn.ffcs.zhsq.devicecollectdata.service.impl;

import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectDataService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceGpsDataService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceGpsData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.AccessControlHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.BidWaterMeterHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.CarBistinguishHis;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceCollectDataMapper;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceGpsDataMapper;
import cn.ffcs.zhsq.mybatis.persistence.deviceinfos.DeviceInfosMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.MwHistoryInfoUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 设备采集GPS数据模块bo对象
 * @Author: huangjianming
 * @Date: 2021/3/9 15:39
 * @Copyright: 2021 福富软件
 */
@Service("deviceGpsDataServiceImpl")
public class DeviceGpsDataServiceImpl implements DeviceGpsDataService {

	@Autowired
	private DeviceGpsDataMapper deviceGpsDataMapper; //注入设备采集数据模块dao

	/**
	 * 新增数据
	 * @param bo 设备采集数据业务对象
	 * @return 设备采集数据id
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String insert(DeviceGpsData bo) {
		deviceGpsDataMapper.insert(bo);
		return bo.getGpsId();
	}
	private static  ConcurrentHashMap<String,Long> deviceMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,String> locationMap = new ConcurrentHashMap<>(); //缓存最新的定位数据
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String saveOrUpdateLastestGpsData(DeviceGpsData bo) {
		//先缓存最新的定位数据，每次都覆盖
		locationMap.put(bo.getBizCode(),bo.getLon()+","+bo.getLat());
		//如果上次更新时间小于 10分钟
		if(deviceMap.containsKey(bo.getBizCode())
				&& System.currentTimeMillis() < (deviceMap.get(bo.getBizCode()) + 1000 * 600)){
			return bo.getGpsId();
		}
		deviceMap.put(bo.getBizCode(),System.currentTimeMillis());
		int updateRows = deviceGpsDataMapper.updateLastestGpsData(bo);
		if(updateRows==0){
			deviceGpsDataMapper.insertLastestGpsData(bo);
		}
		return bo.getGpsId();
	}

	@Override
	public List<Map<String, Object>> searchGpsDataList(Map<String, Object> params) {
		return deviceGpsDataMapper.searchGpsDataList(params);
	}

	@Override
	public Map<String, Object> selectOneLastestGpsData(String resno) {
		Map<String, Object> gpsData = deviceGpsDataMapper.selectOneLastestGpsData(resno);
		this.formatGpsData(gpsData);
		return gpsData;
	}

	@Override
	public List<Map<String, Object>> fetchEnForceRecorderLatestLocateDataListByResno(String resno,String mapt){
		List<Map<String, Object>> resultList = deviceGpsDataMapper.fetchEnForceRecorderLatestLocateDataListByResno(resno, mapt);
		this.formatGpsData(resultList);
		return resultList;
	}

	@Override
	public List<Map<String, Object>> fetchMoreEnForceRecorderLatestLocateDataList(Map<String, Object> params) {
		List<Map<String, Object>> resultList = deviceGpsDataMapper.fetchMoreEnForceRecorderLatestLocateDataList(params);
		this.formatGpsData(resultList);
		return resultList;
	}

	private void formatGpsData(Map<String, Object> gpsDataMap){ //缓存是实时更新，缓存有就从缓存取出覆盖定位数据
		if (!CollectionUtils.isEmpty(gpsDataMap)) {
			if (CommonFunctions.isNotBlank(gpsDataMap,"bizCode")
					&& CommonFunctions.isNotBlank(locationMap,gpsDataMap.get("bizCode").toString())) {
				String locationStr = locationMap.get(gpsDataMap.get("bizCode").toString());
				if (!StringUtils.isBlank(locationStr) && locationStr.contains(",")) {
					String[] locationArr = locationStr.split(",");
					gpsDataMap.put("x",locationArr[0]);
					gpsDataMap.put("y",locationArr[1]);
				}
			}
		}
	}

	private void formatGpsData(List<Map<String, Object>> gpsDataList){
		if (!CollectionUtils.isEmpty(gpsDataList)) {
			for (Map<String, Object> gpsDataMap:gpsDataList) {
				this.formatGpsData(gpsDataMap);
			}
		}
	}

}