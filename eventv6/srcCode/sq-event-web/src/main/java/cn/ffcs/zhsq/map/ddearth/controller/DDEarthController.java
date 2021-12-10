package cn.ffcs.zhsq.map.ddearth.controller;

import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.SSLClient;
import cn.ffcs.common.WaterMarkImgUtils;
import cn.ffcs.file.mybatis.domain.attachment.Attachment;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.file.service.IAttachmentService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.map.arcgis.service.INanchangVideoDataService;
import cn.ffcs.zhsq.nanChang3D.video.ArtemisConfig;
import cn.ffcs.zhsq.nanChang3D.video.ArtemisHttpUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value = "/zhsq/map/DDEarthController")
public class DDEarthController extends ZZBaseController {
	
	static {
		ArtemisConfig.host ="10.20.111.27"; // 代理API网关nginx服务器ip端口
		ArtemisConfig.appKey ="25570941";  // 秘钥appkey
		ArtemisConfig.appSecret ="p0xqC58DLsUHHe95SVJK";// 秘钥appSecret
	}


	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private INanchangVideoDataService nanchangVideoDataService;
	@Autowired
	private IResMarkerService resMarkerService;

	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;
	
	private static final String ARTEMIS_PATH = "/artemis";

	@RequestMapping(value = "/index")
	public String index(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String moudle = request.getParameter("moudle");
		if (StringUtils.isBlank(moudle)) {
			moudle = "";
		}
		map.put("moudle", moudle);
		String forward = "/map/ddearth/ddearth_index_versionone" + moudle + ".ftl";

		String SHOW_CURRENT_GRID_LEVEL_OUTLINE = this.funConfigurationService.turnCodeToValue(
				ConstantValue.SHOW_CURRENT_GRID_LEVEL_OUTLINE, null, IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if (StringUtils.isNotBlank(SHOW_CURRENT_GRID_LEVEL_OUTLINE)) {
			map.put("SHOW_CURRENT_GRID_LEVEL_OUTLINE", SHOW_CURRENT_GRID_LEVEL_OUTLINE);
			List<Long> infoOrgIdList = new ArrayList<Long>();
			if (userInfo.getInfoOrgList() != null && userInfo.getInfoOrgList().size() > 0) {
				for (OrgEntityInfoBO org : userInfo.getInfoOrgList()) {
					infoOrgIdList.add(org.getOrgId());
				}
			}
			// -- 获取对应的网格根节点
			List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList,
					ConstantValue.ORG_TYPE_ALL);
			if (list != null && list.size() > 0) {
				String gridIds = "", infoOrgCodes = "";
				for (int i = 0; i < list.size(); i++) {
					gridIds = gridIds + list.get(i).getGridId() + ",";
					infoOrgCodes = infoOrgCodes + list.get(i).getInfoOrgCode() + ",";
				}
				map.put("gridIds", gridIds);
				map.put("infoOrgCodes", infoOrgCodes);
				map.put("currentUserGridLevel", list.get(0).getGridLevel());
			}
		}
		
		
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragrma", "no-cache");
		response.setDateHeader("Expires", 0);
		return forward;
	}
	
	
	/**
	 * 设备、点位列表
	 * @param session
	 * @param orgCode
	 * @param map
	 * @param standard
	 * @return
	 */
	@RequestMapping(value="/camList")
	public String camList(HttpSession session,  ModelMap map, @RequestParam(value="line",required=false) String line,
						  @RequestParam(value="searchList",required=false) String searchList,@RequestParam(value="infoOrgCode",required=false) String infoOrgCode) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("orgCode", userInfo.getOrgCode());
		map.addAttribute("line", line);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("searchList", searchList);
		return "/map/arcgis/arcgis_3d/nanchang_wenming_rightlist.ftl";
	}
	
	
	
	/**
	 * 监控设备列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param companyType 海康威视：4
	 * @param line 是否在线  1-在线，0-离线 ，2-全部
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listData")
	public EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode,@RequestParam(value="camSearch") String camSearch,
			@RequestParam(value="line") String line,@RequestParam(value="companyType",required=false) String companyType,
			@RequestParam(value="ymax",required=false) String ymax,
			@RequestParam(value="ymin",required=false) String ymin,
			@RequestParam(value="xmax",required=false) String xmax,
			@RequestParam(value="xmin",required=false) String xmin,
			@RequestParam(value="pointTypes",required=false) String pointTypes) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 10 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		params.put("markType", ConstantValue.MARKER_TYPE_GLOBAL_EYES);
		params.put("camSearch", camSearch);
		params.put("companyType", companyType);
		params.put("line", line);
		params.put("ymax", ymax);
		params.put("ymin", ymin);
		params.put("xmax", xmax);
		params.put("xmin", xmin);
		if(StringUtils.isNotBlank(pointTypes)) {
			params.put("pointTypes",pointTypes.split(","));//点位类别
		}
		EUDGPagination pagination = new EUDGPagination(0,new ArrayList<>());
		if(!"-1".equals(line)) {
			pagination = nanchangVideoDataService.listCams( page, rows, params);
		}
		return pagination;
	}
	
	/**
	 * 监控点位信息
	 * @param session
	 * @param orgCode
	 * @param companyType 海康威视：4
	 * @param line 是否在线  1-在线，0-离线 ，2-全部
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pointListData")
	public List<Map<String, Object>> pointListData(HttpSession session,
			@RequestParam(value="orgCode") String orgCode,@RequestParam(value="camSearch",required=false) String camSearch,
			@RequestParam(value="line") String line,@RequestParam(value="companyType",required=false) String companyType,
			@RequestParam(value="ymax",required=false) String ymax,
			@RequestParam(value="ymin",required=false) String ymin,
			@RequestParam(value="xmax",required=false) String xmax,
			@RequestParam(value="xmin",required=false) String xmin,
			@RequestParam(value="pointTypes",required=false) String pointTypes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		params.put("markType", ConstantValue.MARKER_TYPE_GLOBAL_EYES);
		params.put("companyType", companyType);
		params.put("line", line);
		params.put("camSearch", camSearch);
		params.put("ymax", ymax);
		params.put("ymin", ymin);
		params.put("xmax", xmax);
		params.put("xmin", xmin);
		if(StringUtils.isNotBlank(pointTypes)) {
			params.put("pointTypes",pointTypes.split(","));//点位类别
		}
		List<Map<String, Object>> pointList = new ArrayList<>();
		if(!"-1".equals(line)) {
			pointList = nanchangVideoDataService.findCamPointlistByParams(params);
		}
		return pointList;
	}
	
	/**
	 * 根据资源id获取坐标信息
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPointByResourcesId")
	public ResMarker getPointByResourcesId(HttpSession session,@RequestParam(value="monitorById") Long monitorById) {
		return resMarkerService.findResMarkerByResId(ConstantValue.MARKER_TYPE_GLOBAL_EYES, monitorById, "5");
	}
	
	/**
	 * 获取AppSecret
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getVideoUrl")
	public String getVideoUrl(HttpSession session,@RequestParam(value="indexCode") String indexCode) {
		final String previewURLsDataApi = ARTEMIS_PATH +"/api/video/v1/cameras/previewURLs";
		String body="{\"cameraIndexCode\":"+indexCode+",\"streamType\":1,\"protocol\":\"hls\",\"transmode\":0,\"expand\":\"transcode=0\"}";
		Map<String,String> path = new HashMap<String,String>();
		path.put("https://",previewURLsDataApi);
		return ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
		
	}
	
	
	/**
	 * 获取AppSecret
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGridByXY")
	public Map<String, Object> getGridByXY(HttpSession session,
			@RequestParam(value="x") String x,
			@RequestParam(value="y") String y,
			@RequestParam(value="gridLevel") String gridLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridLevel", gridLevel);
		params.put("x", x);
		params.put("y", y);
		params.put("list", coordinateInverseQueryService.findGridInfo(params));
		return params;
	}
	
	  @ResponseBody
	   @RequestMapping(value = "/saveImg")
	   public Object  saveImg(HttpSession session,@RequestParam(value="base64") String base64,String content){
		  UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		   byte[] bytes = WaterMarkImgUtils.markImageToTextBybase64(base64, new Color(0,0,0),userInfo.getUserName(),-30);
	       Map<String, Object> resultMap = fileUploadService.uploadSingleFile(System.currentTimeMillis()+".jpg",bytes,"zhsq_event", "attachment", null);
	       cn.ffcs.file.mybatis.domain.attachment.Attachment attr = new cn.ffcs.file.mybatis.domain.attachment.Attachment();
	       attr.setImgWidth(WaterMarkImgUtils.IMG_WIDTH.toString());
	       attr.setImgHeight(WaterMarkImgUtils.IMG_HEIGHT.toString());
	       if(CommonFunctions.isNotBlank(resultMap, "filePath")) {
				attr.setFilePath(resultMap.get("filePath").toString());
			}
			if(CommonFunctions.isNotBlank(resultMap, "thumbnailPath")) {
				attr.setThumbnailPath(resultMap.get("thumbnailPath").toString());
			}
			return fileAttachmentService.saveAttachment(attr);
	   }

	  @Autowired
		private FileUploadService fileUploadService;
	  @Autowired
		private IAttachmentService<Attachment> fileAttachmentService;
	  
	    @Value(value="${DOMAIN_GANZHOU:10.20.142.52:82}")
	    private String domain;
	    
	    @Value(value="${LOGINNAME_GANZHOU:ncdsj01}")
	    private String loginName;
	    
	    @Value(value="${PASSWORD_GANZHOU:Ffcs@1234}")
	    private String password;
	    
	  //羚眸登录获取token
	    public String login(){
	        String url = "http://"+domain+"/api/user/v1/loginWithoutIdentifyCode";
	        String passwordBase = new String(Base64.encode(password.getBytes()));
	        String params = "{\"loginName\":\""+loginName+"\",\"userPassword\":\""+passwordBase+"\"}";
	        
	        Object result = doPost(url,null,params);
	        if(result!=null){
	        	//data层级数据
	            JSONObject data =  (JSONObject)result;//data
	            String token = data.get("token").toString();
	            //System.out.println("token = "+token);
	            return token;
	        }else{
	            System.out.println("南昌智慧云眼登录失败！");
	        }
	        return null;
	    }
	    
		public static JSONObject doPost(String url,String token,String params){
	        JSONObject json = null;
	        try {
	        	@SuppressWarnings("resource")
				HttpClient  httpclient = new SSLClient();
		        HttpPost httpPost = new HttpPost(url);// 创建httpPost
		        // 执行请求操作，并拿到结果（同步阻塞）
		        HttpResponse response = null;
	        
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
	            if (state == HttpStatus.SC_OK) {
	                HttpEntity responseEntity = response.getEntity();
	                String jsonString = EntityUtils.toString(responseEntity);
	                JSONObject jsonObj = JSONObject.parseObject(jsonString);
	                if ("0".equals(jsonObj.get("code").toString())){
	                	//System.out.println("");
	                	json= jsonObj.getJSONObject("data");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return json;
	        }
			return json;
			
	    }
	    
	    
	    
		@ResponseBody
		@RequestMapping(value="/getAntelopeVideoUrl")
		public String getAntelopeVideoUrl(ModelMap map, HttpSession session,@RequestParam(value="indexCode") String indexCode) {
			String baseUrl = "http://"+domain+"/staticResource/v2/video/live.flv/"+indexCode+"?Authorization=";
			String url = baseUrl +session.getAttribute("NANCHANG_YUNYAN");
	    	if(checkToken(url)) {//如果token有效，直接返回
		    	return url;
		    }
		    String token = login();//重新登录获取
		    session.setAttribute("NANCHANG_YUNYAN", token);
			return baseUrl+token;
		}

		
		@RequestMapping(value="/openAntelopeVideoUrl")
		public String openAntelopeVideoUrl(ModelMap map, HttpSession session,@RequestParam(value="indexCode") String indexCode) {
			String baseUrl = "http://"+domain+"/staticResource/v2/video/live.flv/"+indexCode+"?Authorization=";
			String url = baseUrl +session.getAttribute("NANCHANG_YUNYAN");
	    	if(!checkToken(url)) {//如果token有效，直接返回
			    String token = login();//重新登录获取
			    session.setAttribute("NANCHANG_YUNYAN", token);
	     	   	url = baseUrl+token;
		    }
			map.addAttribute("url",url);
			return "/map/arcgis/arcgis_3d/live_yunyan.ftl";
		}
	    public boolean checkToken(String urlStr) {
	    	HttpURLConnection con = null;
	    	try {
		    	URL url = new URL(urlStr);
		    	con = (HttpURLConnection) url.openConnection();
		    	
				if(con.getResponseCode() == 200) {
					return true;
				}
			} catch (IOException e) {				
				e.printStackTrace();
				return false;
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
	    	
	    	return false;
	    }
	    
	    
}
