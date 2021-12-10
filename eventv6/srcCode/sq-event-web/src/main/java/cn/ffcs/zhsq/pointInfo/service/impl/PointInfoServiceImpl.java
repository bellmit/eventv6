package cn.ffcs.zhsq.pointInfo.service.impl;

import cn.ffcs.common.NpSoap;
import cn.ffcs.common.SSLClient;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.pointinfo.PointInfo;
import cn.ffcs.zhsq.mybatis.persistence.pointinfo.PointInfoMapper;
import cn.ffcs.zhsq.pointInfo.service.PointInfoService;
import cn.ffcs.zhsq.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 设备点位信息表模块服务实现
 * @Author: tangheng
 * @Date: 09-16 16:27:18
 * @Copyright: 2019 福富软件
 */
@Service("pointInfoServiceImpl")
@Transactional
public class PointInfoServiceImpl implements PointInfoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SimpleDateFormat df = new SimpleDateFormat(DateUtils.PATTERN_24TIME);
    		
    
    @Autowired
    private ICoordinateInverseQueryService coordinateInverseQueryService;

    @Value(value="${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
    private String domain;

    @Value(value="${LOGINNAME_GANZHOU:18950293008_dj}")
    private String loginName;

    @Value(value="${PASSWORD_GANZHOU:ffcs@1357}")
    private String password;

    @Value(value = "${POINT_MAPT:5}")
    private Integer mapt;

    @Value(value="${DOMAIN_INTRANET_VIDEO:vlc.zgwggl.gov.cn}")
    private String intranetVideoDomain;

    @Value(value="${DOMAIN_INTRANET_LOGIN:jxsrapi.zgwggl.gov.cn}")
    private String intranetLoginDomain;

    @Value(value="${POINT_APP_ID:urlo}")
    private String APP_ID;

    @Value(value="${POINT_APP_KEY:3255e314328383a801d3bec52eb86225}")
    private String APP_KEY;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;//字典服务

	@Autowired
	private PointInfoMapper pointInfoMapper; //注入设备点位信息表模块dao
	
	/*@Autowired
	private ReportAlarmsMapper reportAlarmsMapper;//注入云眼告警服务*/

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	/**
	 * 新增数据
	 * @param bo 设备点位信息表业务对象
	 * @return 设备点位信息表id
	 */
	@Override
	public Long insert(PointInfo bo) {
		pointInfoMapper.insert(bo);
		return bo.getId();
	}

	/**
	 * 修改数据
	 * @param bo 设备点位信息表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(PointInfo bo) {
		long result = pointInfoMapper.update(bo);
		return result > 0;
	}

    /**
     * 修改数据
     * @param bo 设备点位信息表业务对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateByDeviceId(PointInfo bo) {
        long result = pointInfoMapper.updateByDeviceId(bo);
        return result > 0;
    }

	/**
	 * 删除数据
	 * @param bo 设备点位信息表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(PointInfo bo) {
		long result = pointInfoMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备点位信息表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<PointInfo> list = pointInfoMapper.searchList(params, rowBounds);
		long count = pointInfoMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 设备点位信息表id
	 * @return 设备点位信息表业务对象
	 */
	@Override
	public PointInfo searchById(Long id) {
		PointInfo bo = pointInfoMapper.searchById(id);
		return bo;
	}

    @Override
    public String getToken() {
        String token = login();
        return token;
    }

    @Override
    public String getIntranetToken(String cid) {
        String token = intranetLogin(cid);
        return token;
    }

    public void synchroDevicePoint(){
        Object token = login();
        if(token==null){
            return;
        }
        JSONArray list = getDevicePointList(token.toString());
        if(list==null || list.isEmpty()){
            return;
        };
        List<PointInfo> pointInfolist = new ArrayList<PointInfo>();
        for (int i = 0;i<list.size();i++){
            JSONObject item = (JSONObject) list.get(i);
            if(item.get("longitude")==null||item.get("latitude")==null){
                System.out.println("设备id : "+item.get("id")+" 无经纬度！");
                continue;
            }
            PointInfo pointInfo = new PointInfo();
            String longitude = item.get("longitude").toString();
            pointInfo.setLongitude(longitude);
            String latitude = item.get("latitude").toString();
            pointInfo.setLatitude(latitude);

            if(item.get("deviceName")!=null){
                pointInfo.setDeviceName(item.get("deviceName").toString());
            }
            pointInfo.setDeviceId(item.get("id").toString());
            pointInfo.setDeviceCid(item.get("cid").toString());
            pointInfo.setDeviceSn(item.get("sn").toString());
            pointInfo.setDeviceStatus(item.get("deviceStatus").toString());
            pointInfolist.add(pointInfo);
        }
        pointInfoMapper.deleteAll();
        pointInfoMapper.batchInsert(pointInfolist);
        updatePointInfoList(pointInfolist);
        System.out.println("插入完成，请等待更新!");
    }

    private void updatePointInfoList(List<PointInfo> pointInfolist){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<pointInfolist.size();i++){
                    PointInfo item = pointInfolist.get(i);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Map<String,Object> params = new HashMap<String,Object>();
                                params.put("x", item.getLongitude());
                                params.put("y", item.getLatitude());
                                params.put("mapt", mapt);  //二维地图
                                List<CoordinateInverseQueryGridInfo> gridInfoList = coordinateInverseQueryService.findGridInfo(params);
                                if (gridInfoList!=null){
                                    StringBuffer gridName = new StringBuffer("");
                                    for (int j = gridInfoList.size()-1;j >=0;j--){
                                        gridName.append(gridInfoList.get(j).getGridName());
                                        if(j==0){
                                            item.setRegionCode(gridInfoList.get(j).getInfoOrgCode());
                                        }
                                    }
                                    item.setGridPath(gridName.toString());
                                    updateByDeviceId(item);
                                }else{
                                    System.out.println("设备id : "+item.getDeviceCid()+" 经纬度转网格失败");
                                }
                            }catch (Exception e){
                                System.out.println("设备id : "+item.getDeviceCid()+" 更新失败");
                            }
                        }
                    };
                    taskExecutor.execute(runnable);
                }
            }
        }).start();
    }

    @Override
    public List<PointInfo> searchPointZhouBianList(int page, int rows, Map<String, Object> params) {
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
	    List<PointInfo> result = pointInfoMapper.searchPointZhouBianList(params,rowBounds);
	    return result;
    }

    @Override
    public Long countPointZhouBian(Map<String, Object> params) {
	    long result = pointInfoMapper.countPointZhouBian(params);
        return result;
    }

    @Override
    public String getVideoUrl(String token,String id) {
        String url = "https://"+domain+"/api/device/v1/devices/"+id;
        String params = "{\"id\":\""+id+"\"}";
        Object result = doPost(url,token,params);
        String videoUrl;
        if(result!=null){
            JSONObject jsonObject =  (JSONObject)result;
            String cid = (String)jsonObject.get("cid").toString();
            String ids = (String)jsonObject.get("id").toString();
            String deviceName = (String)jsonObject.get("deviceName").toString();
            String dateTimeMs = NpSoap.dateTimeMs();
            String signature = NpSoap.getSignature(cid, dateTimeMs);
            videoUrl = "https://"+domain+"/api/staticResource/v1/video/live.m3u8/"+cid+"?signature="+signature+"&expire="+dateTimeMs;
        }else{
            logger.info("赣州获取设备失败！");
            videoUrl = "";
        }
        return videoUrl;
    }

    @Override
    public String getVideoUrlByCid(String token, String cid) {
	    String url;
        if(ConstantValue.GZ_CODE.equals(ConstantValue.PLATFORM_CODE)){//赣州平台走内网接口
            token =  getIntranetToken(cid);
            url = "http://"+intranetVideoDomain+"/v2/"+cid+"/live.m3u8?client_token="+token;
        }else{//外网接口
            token =  getToken();
            url = "https://"+domain+"/staticResource/v2/video/live.m3u8/"+cid+"?Authorization="+token;
        }
        return url;
    }

    //登录获取token
    public String login(){
        String url = "https://"+domain+"/api/user/v1/loginWithoutIdentifyCode";
        String passwordBase = new String(Base64.encode(password.getBytes()));
        String params = "{\"loginName\":\""+loginName+"\",\"userPassword\":\""+passwordBase+"\"}";
        Object result = doPost(url,null,params);
        if(result!=null){
            JSONObject jsonObject =  (JSONObject)result;
            String token = jsonObject.get("token").toString();
            logger.info("token = "+token);
            return token;
        }else{
            logger.info("赣州登录失败！");
        }
        return null;
    }

    //内网登录获取token
    public String intranetLogin(String cid){
        String url = "http://"+intranetLoginDomain+"/v2/devices/tokens";
        String passwordBase = new String(Base64.encode(password.getBytes()));
        String params = "{\"cids\":["+cid+"],\"duration\":86400}";
        Object result = doIntranetPost(url,params);
        if(result!=null){
            JSONObject jsonObject =  (JSONObject)result;
            String token = jsonObject.get("token").toString();
            logger.info("token = "+token);
            return token;
        }else{
            logger.info("内网登录失败！");
        }
        return null;
    }

    //获取设备点位列表
    public JSONArray getDevicePointList(String token){
        String url = "https://"+domain+"/api/device/v1/queryUserDevices";
        String params = "{\"limit\":\""+Integer.MAX_VALUE+"\",\"offset\":\""+0+"\"}";
        Object result = doPost(url,token,params);
        if(result!=null){
            JSONObject jsonObject =  (JSONObject)result;
            JSONArray list = jsonObject.getJSONArray("list");
            return list;
        }else{
            logger.info("赣州获取设备点位列表失败！");
        }
        return null;
    }

    public static JSONObject doPost(String url,String token,String params){
//    	System.setProperty("http.proxySet", "true");
//    	System.setProperty("http.proxyHost", "192.168.13.19");
//     	System.setProperty("http.proxyPort", "7777" );
        HttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        /*RequestConfig requestConfig  = RequestConfig.custom().setConnectTimeout(30000).
                setSocketTimeout(30000).build();
        httpPost.setConfig(requestConfig);*/
        // 执行请求操作，并拿到结果（同步阻塞）
        HttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
            if(token!=null){
                httpPost.setHeader("Authorization", token);
            }
            // 封装请求参数
            HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
            httpPost.setEntity(httpEntity);
            response = httpclient.execute(httpPost, new BasicHttpContext());
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            System.out.println("---------------------------------state =="+state);
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                JSONObject jsonObj = JSONObject.parseObject(jsonString);
                if ("0".equals(jsonObj.get("code").toString())){
                    return jsonObj.getJSONObject("data");
                }else {
                    //logger.error("获取羚眸视频图像智能应用平台接口失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject doIntranetPost(String url,String params){
        HttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        HttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type","application/json;charset=UTF-8");

            if(ConstantValue.GZ_CODE.equals(ConstantValue.PLATFORM_CODE)) {//赣州平台走内网接口
                httpPost.setHeader("X-APP-ID", APP_ID);
                httpPost.setHeader("X-APP-Key", APP_KEY);
            }
            // 封装请求参数
            HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
            httpPost.setEntity(httpEntity);
            response = httpclient.execute(httpPost, new BasicHttpContext());
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            System.out.println("state ="+state);
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                JSONObject jsonObj = JSONObject.parseObject(jsonString);
                JSONArray tokens = jsonObj.getJSONArray("tokens");
                if (tokens.size()>0){
                    JSONObject token = tokens.getJSONObject(0);
                    return token;
                }else {
                    //logger.error("获取羚眸视频图像智能应用平台接口失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	//字符串转时间戳
	public String dateToStamp(String str){
        Date date = null;
		try {
			date = DateUtils.convertStringToDate(str, DateUtils.PATTERN_24TIME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return String.valueOf(date.getTime());
    }
	
	@Override
	public EUDGPagination searchAlarmsList(int page, int rows, Map<String, Object> params) {
		JSONObject jsonObject = queryAlarms(page,rows,params);
		int intValue = jsonObject.getIntValue("total");
		if (intValue <= 0) {
			return new EUDGPagination(0,new ArrayList<>());
		}
		List<Map<String,Object>> listMap = new ArrayList<>();
		Map<String,String> violationStatusMap = listToMap(baseDictionaryService.getDataDictListOfSinglestage("D505", null));
		JSONArray list = jsonObject.getJSONArray("list");
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map =  (Map<String, Object>) list.get(i);
			if(CommonFunctions.isNotBlank(map, "violationStatus")) {
				map.put("violationStatusCN", violationStatusMap.get(map.get("violationStatus").toString()));
	    	}
			listMap.add(map);
		}
		return new EUDGPagination(intValue,listMap);
	}
	
	public JSONObject queryAlarms(int page,int rows,Map<String, Object> params) {
		int[] status = new int[] {-1,0,1};
		String[] eventTypes = new String[] {"101516"};
		String startTime = null;
		String endTime = null;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		if(CommonFunctions.isNotBlank(params, "startTime")) {
			startTime = dateToStamp(params.get("startTime").toString() + " 00:00:00");
			paramsMap.put("startTime", startTime);
    	}
    	if(CommonFunctions.isNotBlank(params, "endTime")) {
    		endTime = dateToStamp(params.get("endTime").toString() + " 23:59:59");
    		paramsMap.put("endTime", endTime);
    	}
    	if(CommonFunctions.isNotBlank(params, "status")) {
    		status = new int[] {Integer.valueOf(params.get("status").toString())};
    	}
		String token = getToken();
		
		String url = "https://"+domain+"/api/prevention/result/v1/queryAlarms";
		
		paramsMap.put("eventTypes", eventTypes);
		paramsMap.put("status", status);
		paramsMap.put("limit", page * rows);
		paramsMap.put("offset", (page-1) * rows);
		JSONObject jsonObj=new JSONObject(paramsMap);
		Object result = doPost(url,token,jsonObj.toString());
		if(result!=null){
			return (JSONObject)result;
        }else{
            logger.info("赣州获取告警信息列表失败！");
        }
		
		return null;
	}

	@Override
	public Object searchAlarmsById(String id) {
		return queryAlarmsById(id);
	}
	
	public Object queryAlarmsById(String id) {
		String token = getToken();
		String url = "https://"+domain+"/api/prevention/result/v1/alarms/" + id;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("id", id);
		JSONObject jsonObj=new JSONObject(paramsMap);
		Object result = doPost(url,token,jsonObj.toString());
		if(result!=null){
			try {
				Map<String, Object> objectToMap = getObjectToMap(result);
				Map<String, Object> resultMap = (Map<String, Object>) objectToMap.get("map");
				if(CommonFunctions.isNotBlank(resultMap, "alarmTime")) {
					resultMap.put("alarmTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(resultMap.get("alarmTime").toString()))));
				}
				return resultMap;
			} catch (Exception e) {	
				e.printStackTrace();
			}
        }else{
            logger.info("赣州获取告警信息列表失败！");
        }
		return null;
	}
	
	//字典list转Map
	public static Map<String,String> listToMap(List<BaseDataDict> list){
		Map<String,String> map = new HashMap<>();
		for(int i = 0,l=list.size();i<l;i++) {
			map.put(list.get(i).getDictGeneralCode(), list.get(i).getDictName());
		}
		return map;
	}
	

	public static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
	    Map<String, Object> map = new HashMap<String, Object>();
	    Class<?> clazz = obj.getClass();
	    for (Field field : clazz.getDeclaredFields()) {
	        field.setAccessible(true);
	        String fieldName = field.getName();
	        Object value = field.get(obj);
	        if (value == null){
	            value = "";
	        }
	        map.put(fieldName, value);
	    }
	    return map;
	}

	/*@Override
	public EUDGPagination searchMyAlarmsList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		long count = reportAlarmsMapper.countList(params);
		if (count>0) {
		   List<BaseDataDict> dataDict = baseDictionaryService.getDataDictListOfSinglestage("D505",null);
			list = reportAlarmsMapper.searchList(params, rowBounds);
			for (Map<String, Object> alarsMap:list) {
				DataDictHelper.setDictValueForField(alarsMap, "violationStatus", "violationStatusCN",dataDict);
			}
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	@Override
	public Map<String, Object> searchMyAlarmsById(String id) {
		Map<String, Object> alarm = reportAlarmsMapper.searchById(id);
		if(CommonFunctions.isNotBlank(alarm, "alarmTime")) {
			alarm.put("alarmTime", df.format(new Date(Long.valueOf(alarm.get("alarmTime").toString()))));
		}
		return alarm;
	}*/


	@Override
    public List<Map<String,Object>> exportDeviceList(Map<String,Object> params) throws Exception{
        List<Map<String,Object>> list = pointInfoMapper.exportDeviceList(params);
        String time="";
        return list;
    }

    @Override
    public EUDGPagination searchAllList(Map<String, Object> params) {

        List<PointInfo> list = pointInfoMapper.searchAllList(params);
        long count = pointInfoMapper.countList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    @Override
    public List<Map<String, Object>>  searchAllListForV6(Map<String, Object> params) {
        List<Map<String, Object>> list = pointInfoMapper.searchAllListForV6(params);
        return list;
    }

    /**
     * beta环境gis工程引用事件api包是v6的,调用的接口却是v3的,所以v6要拷贝一份，具体实现以v3事件为准
     * @param page
     * @param rows
     * @param params
     * @return
     */
    @Override
    public EUDGPagination searchListForV6(int page, int rows,Map<String, Object> params) {
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Map<String, Object>> list = pointInfoMapper.searchListForV6(params, rowBounds);
        long count = pointInfoMapper.countList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * beta环境gis工程引用事件api包是v6的,调用的接口却是v3的,所以v6要拷贝一份，具体实现以v3事件为准
     * @param page
     * @param rows
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getOnLineRateForV6(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        long allNum = pointInfoMapper.countList(params);
        map.put("TOTAL",allNum);
        params.put("deviceStatus","1");
        long onLineNum = pointInfoMapper.countList(params);
        map.put("NUM",onLineNum);
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String rate = "0";
        if(onLineNum != 0){
            rate = df.format((float)onLineNum*100/allNum);
        }
        map.put("RATE",rate);
        return map;
    }

    /**
     * 同上面
     * @param params
     * @return
     */
    @Override
    public List<Map<String,Object>> getExceptionTrend(Map<String, Object> params) {
        List<Map<String,Object>> map =  pointInfoMapper.getExceptionTrend(params);
        return map;
    }

    /**
     * 同上面
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllOrgNum(Map<String, Object> params) {
        List<Map<String,Object>> map =  pointInfoMapper.getAllOrgNum(params);
        return map;
    }
}