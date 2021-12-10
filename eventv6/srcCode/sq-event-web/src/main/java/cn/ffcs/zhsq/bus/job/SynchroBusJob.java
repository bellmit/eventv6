package cn.ffcs.zhsq.bus.job;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.ReflectUtils;
import cn.ffcs.shequ.utils.http.CustomAuthenticator;
import cn.ffcs.zhsq.bus.config.BusConfig;
import cn.ffcs.zhsq.bus.service.BusService;
import cn.ffcs.zhsq.event.service.ITimerTaskService;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Busline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarBusline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarLastGps;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Station;
import cn.ffcs.zhsq.mybatis.domain.map.bus.StationBusline;
import cn.ffcs.zhsq.utils.JsonUtils;

//@Component("synchroBusJob")
public class SynchroBusJob extends ApplicationObjectSupport implements ITimerTaskService {
	private Logger logger = LoggerFactory.getLogger(SynchroBusJob.class);

	private CustomAuthenticator authenticator = null;
	@Autowired
	private BusConfig busConfig;
	@Autowired
	private BusService busService;
	
	@Override
	public void run() {
		logger.info("开始调用定时器...获取公交数据");
		initAuthenticator();
		//公交数据同步数据库
		mergeData(BusConfig.URL_BUS_LINE,Busline.class);//同步线路数据
		mergeData(BusConfig.URL_STATION_BUSLINE,StationBusline.class);//同步站点信息(站点和线路的映射关系)
		mergeData(BusConfig.URL_STATION,Station.class);//同步站点基础信息
		mergeData(BusConfig.URL_CAR_BUSLINE,CarBusline.class);//同步车辆信息(车辆和线路的映射关系)
		mergeData(BusConfig.URL_CAR_LAST_GPS,CarLastGps.class);//同步车辆末次位置信息
	}
	
	@Override
	public void execute() {
		run();
	}
	private void initAuthenticator(){
		if(authenticator == null)
			authenticator = new CustomAuthenticator(busConfig.getUserName(),busConfig.getPwd(),false);
	}
	
	/**
	 * 同步数据
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> void mergeData(String configUrl,Class<T> clazz){
		String url = busConfig.getAccessUrl() + configUrl;
		try {
			JSONObject json = HttpUtil.invokeOutInterface(url, "POST", "", null, authenticator);
			int status = json.getInt("Status");
			if(status == 2000){
				JSONArray array = json.getJSONObject("Content").getJSONArray("Records");
				if("StationBusline".equals(clazz.getSimpleName()) || "CarBusline".equals(clazz.getSimpleName())
						||"CarLastGps".equals(clazz.getSimpleName())){//转换成可识别的JSON数据
					String keyArr = null;
					if("StationBusline".equals(clazz.getSimpleName()))
						keyArr = "BuslineStationModels";
					else if("CarBusline".equals(clazz.getSimpleName()))
						keyArr = "CarModels";
					else if("CarLastGps".equals(clazz.getSimpleName()))
						keyArr = "LastGpsDataModels";
					JSONArray newArray = null,tempArr = null;
					String buslineId = null;
					newArray = new JSONArray();
					Iterator<JSONObject> it = array.iterator();
					JSONObject obj = null;
					while(it.hasNext()){
						obj = it.next();
						if(!obj.containsKey("busline_id") || !obj.containsKey(keyArr))
							continue;
						buslineId = obj.getString("busline_id");
						tempArr = obj.getJSONArray(keyArr);
						Iterator<JSONObject> it2 = tempArr.iterator();
						while(it2.hasNext()){
							it2.next().put("busline_id", buslineId);
						}
						newArray.addAll(tempArr);
					}
					array = newArray;
				}
				
				String jsonArrStr = array.toString();
				List<String> list = ReflectUtils.getAllFieldName(clazz);
				for(String fieldName : list){
					jsonArrStr = jsonArrStr.replaceAll(ReflectUtils.convert2DbField(fieldName, false), fieldName);
				}
				logger.debug("待同步公交数据:{}",jsonArrStr);
				
				List<T> busList = JsonUtils.json2GenericList(jsonArrStr, clazz);
				if(busList != null && !busList.isEmpty() ){
					T firstEntity = busList.get(0);
					if(firstEntity instanceof Busline){
						busService.batchMergeBusline((List<Busline>)busList);
					}else if(firstEntity instanceof StationBusline){
						busService.batchMergeStationBusline((List<StationBusline>)busList);
					}else if(firstEntity instanceof Station){
						busService.batchMergeStation((List<Station>)busList);
					}else if(firstEntity instanceof CarBusline){
						busService.batchMergeCarBusline((List<CarBusline>)busList);
					}else if(firstEntity instanceof CarLastGps){
						busService.batchMergeCarLastGps((List<CarLastGps>)busList);
					}
				}
			}else{
				logger.error("同步公交数据失败",SynchroBusJob.class.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
