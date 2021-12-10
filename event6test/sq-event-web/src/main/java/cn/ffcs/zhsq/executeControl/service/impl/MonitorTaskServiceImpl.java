package cn.ffcs.zhsq.executeControl.service.impl;

import cn.ffcs.shequ.utils.SSLClient;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.executeControl.service.IMonitorTaskService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTaskThree;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTaskTwo;
import cn.ffcs.zhsq.mybatis.persistence.executeControl.MonitorTaskMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 布控任务管理模块服务实现
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */
@Service("monitorTaskServiceImpl")
@Transactional
public class MonitorTaskServiceImpl implements IMonitorTaskService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MonitorTaskMapper monitorTaskMapper; //注入布控任务管理模块dao

	@Value(value = "${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
	private String domain;

	@Autowired
	private IBaseDictionaryService baseDictService;//字典转换

	@Value(value="${LOGINNAME_GANZHOU:18970005533_ysq}")
	private String loginName;

	@Value(value="${PASSWORD_GANZHOU:Kd_123456}")
	private String password;

	/**
	 * 新增数据
	 * @param bo 布控任务管理业务对象
	 * @return 布控任务管理id
	 */
	@Override
	public Long insert(MonitorTask bo,String token) throws Exception{
		String url = "https://" + domain + "/api/alarm/v1/monitorTask/addMonitorTask";
		MonitorTaskTwo monitorTaskTwo = new MonitorTaskTwo();
		BeanUtils.copyProperties(monitorTaskTwo,bo);
		monitorTaskTwo.setValidTime(getTimeStamp(bo.getValidTime()));
		monitorTaskTwo.setInvalidTime(getTimeStamp(bo.getInvalidTime()));
		monitorTaskTwo.setCaptureStartTime(getDayTime(bo.getCaptureStartTime()));
		monitorTaskTwo.setCaptureEndTime(getDayTime(bo.getCaptureEndTime()));
		monitorTaskTwo.setAcceptAlarmUserIds(new String[]{"101000001028"});
		String json = JSON.toJSONString(monitorTaskTwo);
		JSONObject jsonObject = doPost(url, token, json);
		if (jsonObject != null){
			String controlTaskId = jsonObject.getString("id");
			bo.setAcceptAlarmUserIds("101000001028");
			bo.setControlTaskId(controlTaskId);
			monitorTaskMapper.insert(bo);
			return bo.getId();
		}
		return 0L;
	}

	/**
	 * 修改数据
	 * @param bo 布控任务管理业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(MonitorTask bo,String token) throws Exception{

		String url = "https://" + domain + "/api/alarm/v1/monitorTask/updateMonitorTask";
		MonitorTaskThree monitorTaskThree = new MonitorTaskThree();
		BeanUtils.copyProperties(monitorTaskThree,bo);
		monitorTaskThree.setValidTime(getTimeStamp(bo.getValidTime()));
		monitorTaskThree.setInvalidTime(getTimeStamp(bo.getInvalidTime()));
		monitorTaskThree.setCaptureStartTime(getDayTime(bo.getCaptureStartTime()));
		monitorTaskThree.setCaptureEndTime(getDayTime(bo.getCaptureEndTime()));
		monitorTaskThree.setAcceptAlarmUserIds(new String[]{"101000001028"});
		monitorTaskThree.setId(bo.getControlTaskId());
		String json = JSON.toJSONString(monitorTaskThree);
		JSONObject jsonObject = doPost(url, token, json);
		long result = monitorTaskMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 布控任务管理业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(MonitorTask bo,String token) {
		String url = "https://" + domain + "/api/alarm/v1/monitorTask/deleteMonitorTask/" + bo.getControlTaskId();
		String params = "{\"id\":\""+bo.getControlTaskId()+"\"}";
		long result = 0;
		try {
			JSONObject jsonObject = doPost(url,token,params);
			result = monitorTaskMapper.delete(bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 布控任务管理分页数据对象
	 */
	@Override
	public EUDGPagination searchList(MonitorTask bo, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((bo.getPage() - 1) * bo.getRows(), bo.getRows());
		List<MonitorTask> list = monitorTaskMapper.searchList(params, rowBounds);
		for(MonitorTask monitorTask : list) {
			if(monitorTask.getTaskType() != null) {
				monitorTask.setTaskTypeCN(baseDictService.changeCodeToName(ConstantValue.MONITOR_TASK_TYPE,monitorTask.getTaskType(), String.valueOf(params.get("gridCode"))));
				monitorTask.setTaskStatusCN(baseDictService.changeCodeToName(ConstantValue.TASK_TYPE, String.valueOf(monitorTask.getTaskStatus()), String.valueOf(params.get("gridCode"))));
			}
		}
		long count = monitorTaskMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 布控任务管理id
	 * @return 布控任务管理业务对象
	 */
	@Override
	public MonitorTask searchById(Long id,String token) {
		MonitorTask bo = monitorTaskMapper.searchById(id);
		return bo;
	}

	/**
	 * 查询布控库中运行的布控任务数
	 * @param bo
	 * @return
	 */
	@Override
	public long getCount(MonitorTask bo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("libIds",bo.getLibIds());
		params.put("taskStatus",1);
		long count = monitorTaskMapper.countList(params);
		return count;
	}

	@Override
	public MonitorTask searchByControlTaskId(String id, String token) {
		String url = "https://" + domain + "/api/alarm/v1/monitorTask/monitorTasks/" + id;
		String params = "{\"id\":\""+id+"\"}";
		JSONObject jsonObject = doPost(url,token,params);
		return null;
	}

	@Override
	public boolean editIgnoreStatus(MonitorTask bo, String token) {
		String url = "https://" + domain + "/api/alarm/v1/monitorTask/setWhetherIgnoreAlarm";
		String params = "{\"taskId\":\""+bo.getControlTaskId()+"\",\"ignore\":\""+bo.getIgnoreStatus()+"\"}";
		JSONObject jsonObject = doPost(url,token,params);
		long result = monitorTaskMapper.updateStatusOrIgnore(bo);
		return result > 0;
	}

	@Override
	public boolean editTaskStatus(MonitorTask bo, String token) {
		String url = "https://" + domain + "/api/alarm/v1/monitorTask/changeMonitorTaskRunStatus";
		String params = "{\"taskIds\":[\""+bo.getControlTaskId()+"\"],\"type\":\""+bo.getTaskStatus()+"\"}";
		JSONObject jsonObject = doPost(url,token,params);
		long result = monitorTaskMapper.updateStatusOrIgnore(bo);
		return result > 0;
	}

	@Override
	public String getToken() {
		String login = this.login();
		return login;
	}

	//赣州登录获取token
	public String login(){
		String url = "https://"+domain+"/api/user/v1/loginWithoutIdentifyCode";
		String passwordBase = new String(Base64.encode(password.getBytes()));
		String params = "{\"loginName\":\""+loginName+"\",\"userPassword\":\""+passwordBase+"\"}";
		Object result = doPost(url,null,params);
		if(result!=null){
			JSONObject jsonObject = (JSONObject)result;
			String token = jsonObject.get("token").toString();
			logger.info("token = "+token);
			return token;
		}else{
			logger.info("赣州登录失败！");
		}
		return null;
	}

	public String getTimeStamp(String time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
		try {
			if (time != null && !"".equals(time)){
				Date date = simpleDateFormat.parse(time);
				return String.valueOf(date.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDayTime(String time){
		if (time != null && !"".equals(time)){
			return time.substring(11);
		}
		return null;
	}

	public static JSONObject doPost(String url, String token, String params) {
		HttpClient httpclient = null;
		try {
			httpclient = new SSLClient();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		// 执行请求操作，并拿到结果（同步阻塞）
		HttpResponse response = null;
		try {
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			if (token != null) {
				httpPost.setHeader("Authorization", token);
			}
			// 封装请求参数
			if (params != null) {
				HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
				httpPost.setEntity(httpEntity);
			}
			response = httpclient.execute(httpPost, new BasicHttpContext());
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				JSONObject jsonObj = JSONObject.parseObject(jsonString);
				if ("0".equals(jsonObj.get("code").toString())) {
					return jsonObj.getJSONObject("data");
				} else {
					//logger.error("获取羚眸视频图像智能应用平台接口失败");
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}