package cn.ffcs.zhsq.taxi.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.ReflectUtils;
import cn.ffcs.shequ.utils.http.CustomAuthenticator;
import cn.ffcs.zhsq.event.service.ITimerTaskService;
import cn.ffcs.zhsq.map.taxi.service.ITaxiService;
import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarInfo;
import cn.ffcs.zhsq.taxi.config.TaxiConfig;
import cn.ffcs.zhsq.utils.JsonUtils;

//@Component("synchroTaxiJob")
public class SynchroTaxiJob extends ApplicationObjectSupport implements ITimerTaskService{
	private Logger logger = LoggerFactory.getLogger(SynchroTaxiJob.class);

	private CustomAuthenticator authenticator = null;
	@Autowired
	private TaxiConfig taxiConfig;
	Map<String,String> headers = new HashMap<String,String>();
	
	@Autowired
	private ITaxiService taxiService;
	
	@Override
	public void run() {
		initAuthenticator();
		mergeData(TaxiConfig.URL_TAXI,CarInfo.class);//同步出租车车辆数据
	}
	@Override
	public void execute() {
		run();
	}
	private void initAuthenticator(){
		if(authenticator == null){
			authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			headers.put("Content-Type", "application/json;charset=utf-8");
		}
	}
	
	/**
	 * 同步数据
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> void mergeData(String configUrl,Class<T> clazz){
		String url = taxiConfig.getAccessUrl() + configUrl;
		try {
			
			JSONObject json = HttpUtil.invokeOutInterface(url, "POST", taxiConfig.getUserId(), headers, authenticator);
			int status = json.getInt("Status");
			if(status == 2000){
				JSONArray array = json.getJSONObject("Content").getJSONArray("Records");
				String jsonArrStr = array.toString();
				List<String> list = ReflectUtils.getAllFieldName(clazz);
				for(String fieldName : list){
					jsonArrStr = jsonArrStr.replaceAll(ReflectUtils.convert2DbField(fieldName, true), fieldName);
				}
				logger.debug("待同步出租车数据:{}",jsonArrStr);
				List<T> carInfoList = JsonUtils.json2GenericList(jsonArrStr, clazz);
				if(carInfoList != null && !carInfoList.isEmpty() && carInfoList.get(0) instanceof CarInfo){
					
					List<CarInfo> carInfos = (List<CarInfo>)carInfoList;
					List<CarInfo> saveList = new ArrayList<CarInfo>();
					int num = carInfoList.size()%100>0?(carInfoList.size()/100+1):carInfoList.size()/100;
					int i = 1;
					for(CarInfo c : carInfos){
						saveList.add(c);
						if(saveList.size() == 100 || i == num){//100条导入数据库一次
							i++;
							this.taxiService.batchMergeCarInfo((List<CarInfo>)carInfoList);
							saveList.clear();
						}
					}
				}
			}else{
				logger.error("同步出租车数据失败",SynchroTaxiJob.class.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
