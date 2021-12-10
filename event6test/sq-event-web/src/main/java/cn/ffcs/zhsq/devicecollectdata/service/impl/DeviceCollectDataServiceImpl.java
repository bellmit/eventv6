package cn.ffcs.zhsq.devicecollectdata.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.shequ.utils.StringUtils;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectDataService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.AccessControlHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.BidWaterMeterHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.CarBistinguishHis;
import cn.ffcs.zhsq.mybatis.persistence.devicecollectdata.DeviceCollectDataMapper;
import cn.ffcs.zhsq.mybatis.persistence.deviceinfos.DeviceInfosMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.MwHistoryInfoUtil;

import com.alibaba.fastjson.JSON;

/**
 * @Description: 设备采集数据模块服务实现
 * @Author: husp
 * @Date: 10-13 09:04:19
 * @Copyright: 2017 福富软件
 */
@Service("deviceCollectDataServiceImpl")
@Transactional
public class DeviceCollectDataServiceImpl implements DeviceCollectDataService {

	@Autowired
	private DeviceCollectDataMapper deviceCollectDataMapper; //注入设备采集数据模块dao
	@Autowired
	private DeviceInfosMapper deviceInfosMapper; //注入设备信息表模块
	
	//井盖类型值
	public static String  MANHOLECOVER_VALUE = "100005";

	/**
	 * 新增数据
	 * @param bo 设备采集数据业务对象
	 * @return 设备采集数据id
	 */
	@Override
	public Long insert(DeviceCollectData bo) {
		deviceCollectDataMapper.insert(bo);
		return bo.getCollectDataId();
	}

	/**
	 * 修改数据
	 * @param bo 设备采集数据业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DeviceCollectData bo) {
		long result = deviceCollectDataMapper.update(bo);
		return result > 0;
	}

	/**
	 * 保存或修改数据，存在修改，不存在保存
	 * @param runDataList 设备采集数据指标项编码和值集合
	 * @param bo 设备采集数据业务对象
	 * @param resultMap 返回信息
	 * @return 是否修改成功
	 */
	public boolean saveOrUpdate(List<Map<String,Object>> runDataList,DeviceCollectData bo,Map<String, Object> resultMap){
		boolean flag=true;
		int i = 0;
		try{
			RowBounds rowBounds = new RowBounds(0, 20);
			Map<String, Object> params=null;
			List<DeviceCollectData> list = null;
			DeviceCollectData bo1=null;
			for (i = 0; i < runDataList.size(); i++) {  
	            Map<String,Object> obj=runDataList.get(i);  
	            for(Entry<String,Object> entry : obj.entrySet()){  
	                String skey = entry.getKey();  
	                String sval = (String)entry.getValue();  
	                bo1=new DeviceCollectData();
					bo1.setDeviceServiceId(bo.getDeviceServiceId());
					bo1.setCollectItemCode(skey);
					bo1.setCollectItemValue(sval);
					bo1.setBizType(bo.getBizType());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					try{
						bo1.setCollectTime(sdf.parse(bo.getCollTime()));
					}catch(Exception e){
						resultMap.put("status", 1);
						resultMap.put("desc", "采集时间collTime异常，正确格式：2017-01-01 01:01:01");
						return false;
					}
					
					bo1.setStatus("1");
					
					params=new HashMap<String, Object>();
					params.put("deviceServiceId", bo.getDeviceServiceId());
					params.put("collectItemCode", skey);
					params.put("bizType", bo.getBizType());
					
					list = deviceCollectDataMapper.searchList(params, rowBounds);
					if(list!=null && list.size()>0){
						DeviceCollectData bo2=list.get(0);
						if(bo2.getCollectTime().before(bo1.getCollectTime())){
							deviceCollectDataMapper.update(bo1);
						}
					}else{
						deviceCollectDataMapper.insert(bo1);
					}
	                 
	            }  
	        }  
		}catch(Exception e){
			Map<String,Object> obj=runDataList.get(i);  
			String errinfo="调用异常";
			if(obj!=null){
				for(Entry<String,Object> entry : obj.entrySet()){  
	                String skey = entry.getKey();  
	                errinfo=errinfo+":"+skey+"异常";
				}   
			}
			resultMap.put("status", 1);
			resultMap.put("desc", errinfo);
			return false;
		}
		
		return flag;
	}
	
	/**
	 * 删除数据
	 * @param bo 设备采集数据业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DeviceCollectData bo) {
		long result = deviceCollectDataMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备采集数据分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeviceCollectData> list = deviceCollectDataMapper.searchList(params, rowBounds);
		formatOutData(list);
		long count = deviceCollectDataMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 设备采集数据id
	 * @return 设备采集数据业务对象
	 */
	@Override
	public DeviceCollectData searchById(Long id) {
		DeviceCollectData bo = deviceCollectDataMapper.searchById(id);
		return bo;
	}
	
	/**
	 * 查询数据
	 * @param params
	 * @return
	 */
	public List<DeviceCollectData> findList( Map<String, Object> params) {		
		List<DeviceCollectData> list = deviceCollectDataMapper.searchList(params);	
		formatOutData(list);
		return list;
	}

	/**
	 * 格式化输出参数
	 * @param twoStations
     */
	private void formatOutData(List<DeviceCollectData> list){
	
		for(DeviceCollectData deviceCollectData : list){
			if(null != deviceCollectData.getCollectTime()){
				deviceCollectData.setCollTime(DateUtils.formatDate(deviceCollectData.getCollectTime(), DateUtils.PATTERN_24TIME));
			}
			
		}
	}

	@Override
	public Map<String, Object> getEnvMonitoringHisMap(String regionCode, String bizType) {
		Map<String, Object> map=null;
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("deviceType", ConstantValue.DEVICE_ENVMONITORING);
		params.put("regionCode",regionCode);
		params.put("bizType",bizType);
		List<DeviceInfos> list=deviceInfosMapper.searchList(params);
		if(list.size()>0){
			params.put("deviceServiceId",list.get(0).getDeviceServiceId());
			map=new HashMap<String, Object>();
			//获取设备属性数据
			List<DeviceCollectData> dataList=findList(params);
			for (DeviceCollectData deviceCollectData : dataList) {
				if(deviceCollectData.getCollectItemCode().equals("pm25")){
					map.put("pm25", deviceCollectData.getCollectItemValue());
				}else if(deviceCollectData.getCollectItemCode().equals("temperature")){
					map.put("temperature", deviceCollectData.getCollectItemValue());
				}else if(deviceCollectData.getCollectItemCode().equals("humidity")){
					map.put("humidity", deviceCollectData.getCollectItemValue());
				}else if(deviceCollectData.getCollectItemCode().equals("noise")){
					map.put("noise", deviceCollectData.getCollectItemValue());
				}
			}
		} 
		return map;
	}
	
	@Override
	public Map<String, Object>  getChargingPileList(String regionCode,
			String bizType) {
		Map<String, Object> map=null;
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("deviceType", ConstantValue.DEVICE_CHARGINGPILE);
		params.put("regionCode",regionCode);
		params.put("bizType",bizType);
		List<DeviceInfos> list=deviceInfosMapper.searchList(params);
		if(list.size()>0){
			Integer free_port=0,use_port=0,fault_port=0,total_port=0;
			List<DeviceCollectData> dataList=null;
			for (int i = 0; i < list.size(); i++) {
				params.put("deviceServiceId",list.get(i).getDeviceServiceId());
				dataList=findList(params);
				for (DeviceCollectData deviceCollectData : dataList) {
					if(deviceCollectData.getCollectItemCode().equals("free_port")){
						if(!StringUtils.isEmpty(deviceCollectData.getCollectItemValue())){
							free_port+=Integer.valueOf(deviceCollectData.getCollectItemValue());
						}
					}else if(deviceCollectData.getCollectItemCode().equals("use_port")){
						if(!StringUtils.isEmpty(deviceCollectData.getCollectItemValue())){
							use_port+=Integer.valueOf(deviceCollectData.getCollectItemValue());
						}
					}else if(deviceCollectData.getCollectItemCode().equals("fault_port")){
						if(!StringUtils.isEmpty(deviceCollectData.getCollectItemValue())){
							fault_port+=Integer.valueOf(deviceCollectData.getCollectItemValue());
						}
					}else if(deviceCollectData.getCollectItemCode().equals("total_port")){
						if(!StringUtils.isEmpty(deviceCollectData.getCollectItemValue())){
							total_port+=Integer.valueOf(deviceCollectData.getCollectItemValue());
						}
					}
				}
			}
			map=new HashMap<String, Object>();
			map.put("free_port", free_port);
			map.put("use_port", use_port);
			map.put("fault_port", fault_port);
			map.put("total_port", total_port);
		}
		return map;
	}

	
	/**
	 * 获取地磁的统计数据
	 * @return
	 */
	public Map<String,Object> findDcData(){
		return deviceCollectDataMapper.findDcData();
	}
	
	/**
	 * 井盖子设备的所有信息
	 * @return
	 */
	public List<Map<String, Object>>  findManholeCoverData(){
		return deviceCollectDataMapper.findManholeCoverData();
	}
	/**
	 * 获取大表数据
	 * @param params
	 * @return
	 */
	public List<BidWaterMeterHis> findBidWaterList(Map<String, Object> params){
		List<BidWaterMeterHis> list1 =new ArrayList<BidWaterMeterHis>();
		if(params.get("deviceId")!=null && !"".equals(params.get("deviceId").toString()) && params.get("deviceType")!=null && !"".equals(params.get("deviceType").toString())){
			int page=1;//页码
			int rows=20;//页条
			Date shour=null;//开始时间
			Date ehour=null;//结束时间
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");  
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Date date=new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			
			if(params.get("page")!=null && !"".equals(params.get("page").toString())){
				try{
					page=Integer.parseInt(params.get("page").toString());
				}catch(Exception e){
					page=1;
				}
			}
			if(params.get("rows")!=null && !"".equals(params.get("rows").toString())){
				try{
					rows=Integer.parseInt(params.get("rows").toString());
				}catch(Exception e){
					rows=20;
				}
			}
			if(params.get("startHour")!=null && !"".equals(params.get("startHour").toString())){
				try{
					shour=sdf.parse(sdf0.format(date)+" "+params.get("startHour").toString()+":00");
				}catch(Exception e){
					shour=date;
				}
			}
			if(params.get("endHour")!=null && !"".equals(params.get("endHour").toString())){
				try{
					ehour=sdf.parse(sdf0.format(date)+" "+params.get("endHour").toString()+":00");
				}catch(Exception e){
					ehour=cal.getTime();
				}
			}
			
			JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(params.get("deviceId").toString(), params.get("deviceType").toString(), page, rows, null, sdf0.format(date), sdf0.format(cal.getTime()));
			int resCode = json.getInt("res_code");
			if(resCode == 0){//成功
				JSONArray array = null;
				if(json.getJSONArray("device_list") != null){
					array = json.getJSONArray("device_list");
				}
				List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				if(list!=null && list.size()>0){
					for (BidWaterMeterHis bidWaterMeterHis : list) {
						if(bidWaterMeterHis.getRecordtime()!=null && !"".equals(bidWaterMeterHis.getRecordtime())){
							try {
								date = sdf.parse(bidWaterMeterHis.getRecordtime());
								if(shour.before(date) && ehour.after(date)){//在时间点内记录
									SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");  
									bidWaterMeterHis.setRecordtime(sdf1.format(date));
									list1.add(bidWaterMeterHis);
								}
							} catch (Exception e) {
								 
							}  
						}
					}
				}
			}
		}
		
		return  list1;
	}

	
	
	/**
	 * 获取首页井盖的数据
	 * @return
	 */
	public List<Map<String, Object>> manholeCoverCount() {
		List<Map<String, Object>> allData = new ArrayList<Map<String, Object>>();
		// 获取井盖所有设备
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceType", MANHOLECOVER_VALUE);
		List<DeviceInfos> devList = deviceInfosMapper.searchList(params);
		if (devList != null && devList.size() > 0) {
			for (DeviceInfos vo : devList) {
				Map<String, Object> par1 = new HashMap<String, Object>();
				// 父设备名称
				par1.put("deviceName", vo.getDeviceName());
				List<String> sonList = new ArrayList<String>();
				// 获取子设备数据

				Map<String, Object> sonParam = new HashMap<String, Object>();
				sonParam.put("parentDeviceServiceId", vo.getDeviceServiceId());
				List<DeviceCollectData> dataList = deviceCollectDataMapper.searchList(sonParam);
				if (dataList != null && dataList.size() > 0) {
					Map<String, Object> ffParam = new HashMap<String, Object>();
					for (DeviceCollectData dcVo : dataList) {
						String collectItemCode = dcVo.getCollectItemCode();
						// //井下
						if (collectItemCode.equals("under_the_shaft")) {
							ffParam.put("under_the_shaft",
									dcVo.getCollectItemValue());
						}
						// 路面积水
						if (collectItemCode.equals("surface_gathered_water")) {
							ffParam.put("surface_gathered_water",
									dcVo.getCollectItemValue());
						}
						// 浊度
						if (collectItemCode.equals("turbidity")) {
							ffParam.put("turbidity", dcVo.getCollectItemValue());
						}
						// 流速
						if (collectItemCode.equals("velocity_of_flow")) {
							ffParam.put("velocity_of_flow",
									dcVo.getCollectItemValue());
						}
					}

					if (ffParam.get("under_the_shaft") != null) { // 井下 数据为0
						sonList.add((String) ffParam.get("under_the_shaft"));
					} else {
						sonList.add("0"); // 井下 数据为0
					}

					if (ffParam.get("surface_gathered_water") != null) { // 路面积水
																			// 数据为0
						sonList.add((String) ffParam
								.get("surface_gathered_water"));
					} else {
						sonList.add("0"); // 路面积水数据为0
					}

					if (ffParam.get("turbidity") != null) { // 浊度为0
						sonList.add((String) ffParam.get("turbidity"));
					} else {
						sonList.add("0"); // 浊度 数据为0
					}
					if (ffParam.get("velocity_of_flow") != null) { // 流速 数据为0
						sonList.add((String) ffParam.get("velocity_of_flow"));
					} else {
						sonList.add("0"); // 流速 数据为0
					}

				} else {
					sonList.add("0"); // 井下 数据为0
					sonList.add("0"); // 路面积水为0
					sonList.add("0"); // 浊度 为0
					sonList.add("0"); // 流速 为0

				}

				par1.put("list", sonList);
				allData.add(par1);
			}

		}
		return allData;
	}

	@Override
	public List<Map<String, Object>> findAccessControlList(Map<String, Object> params) {
		List<Map<String, Object>> accessControlList=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=null;
		Object obj= params.get("oper");
		
		String deviceType=ConstantValue.DEVICE_ACCESS_CONTROL;
		if(obj!=null&&obj.toString().equals("car")){
			deviceType= ConstantValue.DEVICE_CAR_CONTROL;	
		}
		params.put("deviceType", deviceType);
		List<DeviceInfos> infos=deviceInfosMapper.searchList(params);
		for (DeviceInfos deviceInfo : infos) {
			if(!StringUtils.isEmpty(deviceInfo.getDeviceServiceId())){
				JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(deviceInfo.getDeviceServiceId(),deviceType, 1,Integer.MAX_VALUE, null, params.get("startTime").toString(),params.get("endTime").toString());
				int resCode = json.getInt("res_code");
				if(resCode == 0){//成功
					JSONArray array = null;
					if(json.getJSONArray("device_list") != null){
						array = json.getJSONArray("device_list");
					}
					if(deviceType.equals(ConstantValue.DEVICE_CAR_CONTROL)){
						List<CarBistinguishHis> list =(List<CarBistinguishHis>) array.toCollection(array, CarBistinguishHis.class); 
						for (CarBistinguishHis carBistinguishHis : list) {
							map=new HashMap<String, Object>();
							map.put("userName", carBistinguishHis.getCar_number());
							String operName=null;
							if(carBistinguishHis.getIn_out_status()!=null){
								operName=carBistinguishHis.getIn_out_status().equals("1")?"车辆进库":"车辆出库";
							}else{
								operName="";
							}
							map.put("operName", operName);
							map.put("accessTime", carBistinguishHis.getRecordtime());
							accessControlList.add(map);
						}
					}else{
						List<AccessControlHis> list = (List<AccessControlHis>) array.toCollection(array, AccessControlHis.class);
						for (AccessControlHis accessControlHis : list) {
							if(accessControlHis.getIn_out_status().equals("0")){
								map=new HashMap<String, Object>();
								map.put("userName", accessControlHis.getUser_name());
								map.put("operName", "门禁进门");
								map.put("accessTime", accessControlHis.getRecordtime());
								accessControlList.add(map);
							}
						}
					}
					
				}
			}
		}
		Collections.sort(accessControlList, new Comparator<Map<String, Object>>() {  
            @Override  
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {  
                if (o1.get("accessTime") == null && o2.get("accessTime") == null)  
                    return 0;  
                if (o1.get("accessTime") == null)  
                    return -1;  
                if (o2.get("accessTime") == null)  
                    return 1;  
                try {
                	return DateUtils.compareMinDate(o1.get("accessTime").toString(), DateUtils.PATTERN_24TIME, o2.get("accessTime").toString(),  DateUtils.PATTERN_24TIME)?1:-1;
                } catch (ParseException e) {
					return 0;  
				}
                
            }  
        }); 
		return accessControlList;
	}
}