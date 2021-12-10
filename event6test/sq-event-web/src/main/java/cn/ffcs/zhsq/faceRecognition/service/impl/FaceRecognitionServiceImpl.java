package cn.ffcs.zhsq.faceRecognition.service.impl;

import cn.ffcs.shequ.utils.SSLClient;
import cn.ffcs.zhsq.faceRecognition.service.IFaceRecognitionService;
import cn.ffcs.zhsq.mybatis.domain.faceRecognition.FaceRecognition;
import cn.ffcs.zhsq.mybatis.domain.faceRecognition.PageBean;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description: 布控任务管理模块服务实现
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */
@Service("faceRecognitionServiceImpl")
@Transactional
public class FaceRecognitionServiceImpl implements IFaceRecognitionService {

	@Value(value = "${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
	private String domain;

	@Override
	public String upload(MultipartFile multipartFile, File file, String token) {
		try {
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String contentType = multipartFile.getContentType();
			HashMap<String, String> hashMap = new HashMap<>();
			String url = "https://"+domain+"/api/disa/v1/face/img/uploadImg";
			Map<String, Object> map = uploadFileByHTTP(file, url, hashMap, token);
			String jsonString = (String) map.get("data");
			JSONObject jsonObj = JSONObject.parseObject(jsonString);
			if ("0".equals(jsonObj.get("code").toString())) {
				String imgUrl = jsonObj.getJSONObject("data").getString("url");
				return imgUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				file.delete();
			}
		}
		return null;
	}

	@Override
	public PageBean<FaceRecognition> searchList(HashMap<String, Object> hashMap, String token) {
		PageBean<FaceRecognition> pb = new PageBean<>();
		List<FaceRecognition> recognitionList = new ArrayList<>();
		String placeTags = (String) hashMap.get("placeTags");
		String headTags = (String) hashMap.get("headTags");
		String upperBodyTags = (String) hashMap.get("upperBodyTags");
		String cids = (String) hashMap.get("cids");
		String score = (String) hashMap.get("score");
		String startTime = (String) hashMap.get("startTime");
		String endTime = (String) hashMap.get("endTime");
		String limit = (String) hashMap.get("limit");
		String feature = (String) hashMap.get("feature");

		String sexTags = (String) hashMap.get("sexTags");
		String ageSectionTags = (String) hashMap.get("ageSectionTags");
		String minId = (String) hashMap.get("minId");
		String maxId = (String) hashMap.get("maxId");
		//String id = (String) hashMap.get("id");

		if (feature != null && !"".equals(feature)){
			String url = "https://"+domain+"/api/disa/v1/face/queryFacesByFeature";
			String param = "{\"startTime\":\""+getStamp(startTime)+"\",\"endTime\":\""+getStamp(endTime)+"\",\"score\":"+score+"," +
					"\"placeTags\":"+placeTags+"," + "\"headTags\":"+headTags+","+"\"upperBodyTags\":"+upperBodyTags+","+"\"cids\":"+cids+"," +
					"\"sexTags\":["+sexTags+"],"+"\"ageSectionTags\":["+ageSectionTags+"],"+"\"feature\":\""+feature+"\"}";
			JSONObject jsonObject = doPost(url, token, param);
			String jsonList = jsonObject.getString("list");
			JSONArray faceArray = JSONArray.fromObject(jsonList);
			for (int i = 0; i < faceArray.size(); i++) {
				net.sf.json.JSONObject json = faceArray.getJSONObject(i);
				FaceRecognition faceRecognition = new FaceRecognition();
				if (StringUtils.isNotEmpty(json.getString("id"))){
					faceRecognition.setId(json.getString("id"));
				}
				if (StringUtils.isNotEmpty(json.getString("imgId"))){
					faceRecognition.setImgId(json.getString("imgId"));
				}
				if (StringUtils.isNotEmpty(json.getString("cid"))){
					faceRecognition.setCid(json.getString("cid"));
				}
				if (StringUtils.isNotEmpty(json.getString("deviceId"))){
					faceRecognition.setDeviceId(json.getString("deviceId"));
				}
				if (StringUtils.isNotEmpty(json.getString("captureTime"))){
					faceRecognition.setCaptureTime(json.getString("captureTime"));
				}
				if (StringUtils.isNotEmpty(json.getString("longitude"))){
					faceRecognition.setLongitude(json.getString("longitude"));
				}
				if (StringUtils.isNotEmpty(json.getString("latitude"))){
					faceRecognition.setLatitude(json.getString("latitude"));
				}
				if (StringUtils.isNotEmpty(json.getString("cameraTags"))){
					faceRecognition.setCameraTags(json.getString("cameraTags"));
				}
				if (StringUtils.isNotEmpty(json.getString("sceneUrl"))){
					faceRecognition.setSceneUrl(json.getString("sceneUrl"));
				}
				/*if (StringUtils.isNotEmpty(json.getString("alikeAids"))){
					faceRecognition.setAlikeAids(json.getString("alikeAids"));
				}*/
				if (StringUtils.isNotEmpty(json.getString("aid"))){
					faceRecognition.setAid(json.getString("aid"));
				}
				if (StringUtils.isNotEmpty(json.getString("faceRect"))){
					faceRecognition.setFaceRect(json.getString("faceRect"));
				}
				if (StringUtils.isNotEmpty(json.getString("personTags"))){
					faceRecognition.setPersonTags(json.getString("personTags"));
				}
				if (StringUtils.isNotEmpty(json.getString("deviceName"))){
					faceRecognition.setDeviceName(json.getString("deviceName"));
				}
				if (StringUtils.isNotEmpty(json.getString("address"))){
					faceRecognition.setAddress(json.getString("address"));
				}
				if (StringUtils.isNotEmpty(json.getString("personInfoUrl"))){
					faceRecognition.setPersonInfoUrl(json.getString("personInfoUrl"));
				}
				if (StringUtils.isNotEmpty(json.getString("faceUrl"))){
					faceRecognition.setFaceUrl(json.getString("faceUrl"));
				}
				faceRecognition.setHasBody(json.getBoolean("hasBody"));

				if (StringUtils.isNotEmpty(json.getString("faceConfidence"))){
					faceRecognition.setFaceConfidence(json.getString("faceConfidence"));

				}
				if (StringUtils.isNotEmpty(json.getString("score"))){
					faceRecognition.setScore(json.getDouble("score"));
				}
				recognitionList.add(faceRecognition);
				pb.setTotalCount(faceArray.size());
			}
			pb.setList(recognitionList);

		}else {
			String url = "https://"+domain+"/api/disa/v1/face/queryFaces";
			String param = "";
			if (minId == null && maxId == null){
				param = "{\"startTime\":\""+getStamp(startTime)+"\",\"endTime\":\""+getStamp(endTime)+"\",\"limit\":"+limit+",\"score\":"+score+"," +
						"\"placeTags\":"+placeTags+"," + "\"headTags\":"+headTags+","+"\"upperBodyTags\":"+upperBodyTags+","+"\"cids\":"+cids+"," +
						"\"sexTags\":["+sexTags+"],"+"\"ageSectionTags\":["+ageSectionTags+"]}";
			}else if ("".equals(minId) && maxId != null){
				//上一页
				param = "{\"startTime\":\""+getStamp(startTime)+"\",\"endTime\":\""+getStamp(endTime)+"\",\"limit\":"+limit+",\"score\":"+score+"," +
						"\"placeTags\":"+placeTags+"," + "\"headTags\":"+headTags+","+"\"upperBodyTags\":"+upperBodyTags+","+"\"cids\":"+cids+"," +
						"\"sexTags\":["+sexTags+"],"+"\"ageSectionTags\":["+ageSectionTags+"],"+"\"maxId\":\""+maxId+"\"}";
			}else if (minId != null && "".equals(maxId)){
				//下一页
				param = "{\"startTime\":\""+getStamp(startTime)+"\",\"endTime\":\""+getStamp(endTime)+"\",\"limit\":"+limit+",\"score\":"+score+"," +
						"\"placeTags\":"+placeTags+"," + "\"headTags\":"+headTags+","+"\"upperBodyTags\":"+upperBodyTags+","+"\"cids\":"+cids+"," +
						"\"sexTags\":["+sexTags+"],"+"\"ageSectionTags\":["+ageSectionTags+"],"+"\"minId\":\""+minId+"\"}";

			}
			JSONObject jsonObject = doPost(url, token, param);
			System.out.println("param = " + param);
			//设置总记录数
			String countFacesUrl = "https://"+domain+"/api/disa/v1/face/countFaces";
			String countFacesParam = "{\"startTime\":\""+getStamp(startTime)+"\",\"endTime\":\""+getStamp(endTime)+"\",\"limit\":"+limit+",\"score\":"+score+"," +
					"\"placeTags\":"+placeTags+"," + "\"headTags\":"+headTags+","+"\"upperBodyTags\":"+upperBodyTags+","+"\"cids\":"+cids+"," +
					"\"sexTags\":["+sexTags+"],"+"\"ageSectionTags\":["+ageSectionTags+"]}";
			JSONObject countJson = doPost(countFacesUrl, token, countFacesParam);
			int totalCount = 0;
			if (countJson != null){
				totalCount = countJson.getInteger("count");
				pb.setTotalCount(totalCount);
			}

			if (jsonObject != null){
				String jsonList = jsonObject.getString("list");
				JSONArray faceArray = JSONArray.fromObject(jsonList);
				for (int i = 0; i < faceArray.size(); i++) {
					net.sf.json.JSONObject json = faceArray.getJSONObject(i);
					FaceRecognition faceRecognition = new FaceRecognition();
					if (StringUtils.isNotEmpty(json.getString("id"))){
						faceRecognition.setId(json.getString("id"));
					}
					if (StringUtils.isNotEmpty(json.getString("imgId"))){
						faceRecognition.setImgId(json.getString("imgId"));
					}
					if (StringUtils.isNotEmpty(json.getString("cid"))){
						faceRecognition.setCid(json.getString("cid"));
					}
					if (StringUtils.isNotEmpty(json.getString("deviceId"))){
						faceRecognition.setDeviceId(json.getString("deviceId"));
					}
					if (StringUtils.isNotEmpty(json.getString("captureTime"))){
						faceRecognition.setCaptureTime(json.getString("captureTime"));
					}
					/*if (StringUtils.isNotEmpty(json.getString("longitude"))){
						faceRecognition.setLongitude(json.getString("longitude"));
					}
					if (StringUtils.isNotEmpty(json.getString("latitude"))){
						faceRecognition.setLatitude(json.getString("latitude"));
					}*/
					if (StringUtils.isNotEmpty(json.getString("cameraTags"))){
						faceRecognition.setCameraTags(json.getString("cameraTags"));
					}
					if (StringUtils.isNotEmpty(json.getString("sceneUrl"))){
						faceRecognition.setSceneUrl(json.getString("sceneUrl"));
					}
					if (StringUtils.isNotEmpty(json.getString("aid"))){
						faceRecognition.setAid(json.getString("aid"));
					}
					if (StringUtils.isNotEmpty(json.getString("faceRect"))){
						faceRecognition.setFaceRect(json.getString("faceRect"));
					}
					if (StringUtils.isNotEmpty(json.getString("personTags"))){
						faceRecognition.setPersonTags(json.getString("personTags"));
					}
					if (StringUtils.isNotEmpty(json.getString("deviceName"))){
						faceRecognition.setDeviceName(json.getString("deviceName"));
					}
					if (StringUtils.isNotEmpty(json.getString("address"))){
						faceRecognition.setAddress(json.getString("address"));
					}
					if (StringUtils.isNotEmpty(json.getString("personInfoUrl"))){
						faceRecognition.setPersonInfoUrl(json.getString("personInfoUrl"));
					}
					if (StringUtils.isNotEmpty(json.getString("faceUrl"))){
						faceRecognition.setFaceUrl(json.getString("faceUrl"));
					}
					faceRecognition.setHasBody(json.getBoolean("hasBody"));

					if (StringUtils.isNotEmpty(json.getString("faceConfidence"))){
						faceRecognition.setFaceConfidence(json.getString("faceConfidence"));

					}
					recognitionList.add(faceRecognition);
				}
			}
			pb.setList(recognitionList);
		}

		return pb;
	}

	@Override
	public FaceRecognition findById(String id,String token) {
		String url = "https://" + domain + "/api/disa/v1/face/faces/" + id;
		String params = "{\"id\":\""+id+"\"}";
		FaceRecognition bo = new FaceRecognition();
		JSONObject jsonObject = doPost(url,token,params);
		if (jsonObject != null){
			bo.setId(jsonObject.getString("id"));
			bo.setFaceRect(jsonObject.getString("faceFeature"));
			bo.setImgId(jsonObject.getString("imgId"));
			bo.setCaptureTime(jsonObject.getString("captureTime"));
			bo.setSceneUrl(jsonObject.getString("sceneUrl"));
			bo.setLatitude(jsonObject.getString("latitude"));
			bo.setLongitude(jsonObject.getString("longitude"));
			bo.setDeviceId(jsonObject.getString("deviceId"));
			bo.setDeviceName(jsonObject.getString("deviceName"));
			bo.setCameraTags(jsonObject.getString("cameraTags"));
			bo.setPersonTags(jsonObject.getString("placeTags"));
			bo.setPersonInfoUrl(jsonObject.getString("personInfoUrl"));
			bo.setFacePose(jsonObject.getString("facePose"));
			bo.setHasBody(jsonObject.getBoolean("hasBody"));
			bo.setImgType(jsonObject.getString("imgType"));
			bo.setAddress(jsonObject.getString("address"));
			bo.setImgSubType(jsonObject.getString("imgSubType"));
			bo.setAidTags(jsonObject.getString("aidTags"));
			bo.setFaceQuality(jsonObject.getString("faceQuality"));
			bo.setFaceLowQuality(jsonObject.getBoolean("faceLowQuality"));
			bo.setTimeTags(jsonObject.getString("timeTags"));
			bo.setHasArchives(jsonObject.getBoolean("hasArchives"));
			bo.setAlikeAids(jsonObject.getString("alikeAids"));
			bo.setRealName(jsonObject.getString("realName"));
			bo.setFaceUrl(jsonObject.getString("faceUrl"));
			bo.setFaceFeatureVersion(jsonObject.getString("faceFeatureVersion"));
			bo.setCardId(jsonObject.getString("cardId"));
			bo.setFaceConfidence(jsonObject.getString("faceConfidence"));
			bo.setFaceRect(jsonObject.getString("faceRect"));
			bo.setAid(jsonObject.getString("aid"));
			bo.setPersonTags(jsonObject.getString("personTags"));
			bo.setCid(jsonObject.getString("cid"));
		}
		return bo;
	}

	@Override
	public ArrayList<FaceRecognition> findByIdS(String[] ids, String token) {
		ArrayList<FaceRecognition> list = new ArrayList<>();
		for (String id : ids) {
			String url = "https://" + domain + "/api/disa/v1/face/faces/" + id;
			String params = "{\"id\":\""+id+"\"}";
			FaceRecognition bo = new FaceRecognition();
			JSONObject jsonObject = doPost(url,token,params);
			if (jsonObject != null){
				bo.setCaptureTime(jsonObject.getString("captureTime"));
				bo.setLatitude(jsonObject.getString("latitude"));
				bo.setLongitude(jsonObject.getString("longitude"));
				bo.setDeviceName(jsonObject.getString("deviceName"));
				bo.setFaceUrl(jsonObject.getString("faceUrl"));
			}
			list.add(bo);
		}
		return list;
	}

	@Override
	public Map<String, Object> getFeature(String imageUrl, String token) {
		HashMap<String, Object> hashMap = new HashMap<>();
		String url = "https://"+domain+"/api/disa/v1/face/getFeature";
		String params = "{\"url\":\""+imageUrl+"\"}";
		JSONObject jsonObject = doPost(url, token, params);
		if (jsonObject != null){
			String jsonList = jsonObject.getString("list");
			JSONArray jsonArray = JSONArray.fromObject(jsonList);
			net.sf.json.JSONObject json = jsonArray.getJSONObject(0);
			net.sf.json.JSONObject rect = json.getJSONObject("rect");
			hashMap.put("top",rect.getString("top"));
			hashMap.put("left",rect.getString("left"));
			hashMap.put("width",rect.getString("width"));
			hashMap.put("height",rect.getString("height"));
			String feature = json.getString("feature");
			hashMap.put("feature",feature);
			String quality = json.getString("quality");
			hashMap.put("quality",quality);
		}
		return hashMap;
	}

	@Override
	public JSONObject getFeatureById(String id, String token) {
		HashMap<String, Object> hashMap = new HashMap<>();
		String url = "https://"+domain+"/api/disa/v1/face/faces/" + id;
		String params = "{\"id\":\""+id+"\"}";
		JSONObject jsonObject = doPost(url, token, params);
		return jsonObject;
	}


	/**
	 * 模拟表单上传文件
	 */
	public static Map<String, Object> uploadFileByHTTP(File postFile, String postUrl, Map<String, String> postParam,String token) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {

			//把一个普通参数和文件上传给下面这个地址    是一个servlet
			HttpPost httpPost = new HttpPost(postUrl);
			//把文件转换成流对象FileBody
			//FileBody fundFileBin = new FileBody(postFile);
			//设置传输参数
			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
			multipartEntity.addBinaryBody("file", postFile);//相当于<input type="file" name="picFile"/>
			//设计文件以外的参数
			if (postParam != null) {
				Set<String> keySet = postParam.keySet();
				for (String key : keySet) {
					//相当于<input type="text" name="name" value=name>
					//System.out.println(key+" : " + postParam.get(key));
					multipartEntity.addPart(key, new StringBody(postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));
				}
			}
			HttpEntity reqEntity = multipartEntity.build();
			if (token != null) {
				httpPost.setHeader("Authorization", token);
			}
			httpPost.setEntity(reqEntity);

			//System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());
			//发起请求   并返回请求的响应
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				//System.out.println("----------------------------------------");
				//打印响应状态
				//System.out.println(response.getStatusLine());
				resultMap.put("statusCode", response.getStatusLine().getStatusCode());
				//获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					//打印响应长度
					//System.out.println("Response content length: " + resEntity.getContentLength());
					//打印响应内容
					resultMap.put("data", EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
				}
				//销毁
				EntityUtils.consume(resEntity);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("uploadFileByHTTP result:"+resultMap);
		return resultMap;
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

	//获取时间戳
	public static String getStamp(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(date.getTime());
	}

}