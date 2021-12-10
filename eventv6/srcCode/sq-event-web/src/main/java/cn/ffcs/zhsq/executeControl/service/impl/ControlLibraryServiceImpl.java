package cn.ffcs.zhsq.executeControl.service.impl;

import cn.ffcs.shequ.utils.SSLClient;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.executeControl.service.IControlLibraryService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibrary;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibraryThree;
import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibraryTwo;
import cn.ffcs.zhsq.mybatis.persistence.executeControl.ControlLibraryMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description: 布控库管理模块服务实现
 * @Author: dtj
 * @Date: 07-16 20:37:41
 * @Copyright: 2020 福富软件
 */
@Service("controlLibraryServiceImpl")
@Transactional
public class ControlLibraryServiceImpl implements IControlLibraryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ControlLibraryMapper controlLibraryMapper; //注入布控库管理模块dao

	@Value(value="${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
	private String domain;

	@Value(value="${LOGINNAME_GANZHOU:18970005533_ysq}")
	private String loginName;

	@Value(value="${PASSWORD_GANZHOU:Kd_123456}")
	private String password;

	/**
	 * 修改数据
	 * @param bo 布控库管理业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(ControlLibrary bo,String token) throws Exception{
		String url = "https://"+domain+"/api/alarm/v1/monitorLib/updateMonitorLib";
		ControlLibraryThree controlLibraryThree = new ControlLibraryThree();
		BeanUtils.copyProperties(controlLibraryThree,bo);
		controlLibraryThree.setId(bo.getControlLibraryId());
		String json = JSON.toJSONString(controlLibraryThree);
		try {
			JSONObject jsonObject = doPost(url, token, json);
			long result = controlLibraryMapper.update(bo);
			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 布控库管理分页数据对象
	 */
	@Override
	public EUDGPagination searchList(ControlLibrary bo, Map<String, Object> params, String token) {
		RowBounds rowBounds = new RowBounds((bo.getPage() - 1) * bo.getRows(), bo.getRows());
		List<ControlLibrary> list = controlLibraryMapper.searchList(params, rowBounds);
		long count = controlLibraryMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 布控库管理id
	 * @return 布控库管理业务对象
	 */
	@Override
	public ControlLibrary searchById(Long id) {
		ControlLibrary bo = controlLibraryMapper.searchById(id);
		return bo;
	}

	@Override
	public String getToken() {
		return this.login();
	}

	@Override
	public List<ControlLibrary> getTitle() {
		List<ControlLibrary> list = controlLibraryMapper.getTitle();
		return list;
	}

	@Override
	public Long batchInsert(ArrayList<ControlLibrary> list) throws Exception{
		String url = "https://"+domain+"/api/alarm/v1/monitorLib/addMonitorLib";
		String token = this.getToken();
		StringBuilder params = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			ControlLibraryTwo controlLibraryTwo = new ControlLibraryTwo();
			BeanUtils.copyProperties(controlLibraryTwo,list.get(i));
			String json = JSON.toJSONString(controlLibraryTwo);
			JSONObject jsonObject = doPost(url, token, json);
			if (jsonObject != null){
				String id = jsonObject.getString("id");
				list.get(i).setControlLibraryId(id);
			}
		}
		return controlLibraryMapper.batchInsert(list);
	}

	@Override
	public Long batchDelete(String[] ids) {
		String token = this.getToken();
		for (String id : ids) {
			ControlLibrary bo = controlLibraryMapper.searchById(Long.valueOf(id));
			String url = "https://"+domain+"/api/alarm/v1/monitorLib/deleteMonitorLib/" + bo.getControlLibraryId();
			String params = "{\"id\":\""+bo.getControlLibraryId()+"\"}";
			doPost(url, token, params);
		}
		return controlLibraryMapper.batchDelete(ids);
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

	public static JSONObject doPost(String url,String token,String params){
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
			httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
			if(token!=null){
				httpPost.setHeader("Authorization", token);
			}
			// 封装请求参数
			if (params != null){
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

}