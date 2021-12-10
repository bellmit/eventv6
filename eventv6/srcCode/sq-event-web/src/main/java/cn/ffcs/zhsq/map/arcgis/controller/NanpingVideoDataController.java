package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.arcgis.service.INanpingVideoDataService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/zhsq/map/nanpingVideoDataController")
public class NanpingVideoDataController extends ZZBaseController {
	
	@Autowired 
	protected INanpingVideoDataService nanpingVideoDataService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private MonitorService monitorService;
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	
	
	/**
	 * 区域信息
	 * @param request
	 * @param session
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value="/listData")
	 public List<Map<String, Object>> listData(HttpServletRequest request,
			    HttpSession session,@RequestParam Map<String, Object> params) {
//		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		Long orgId = userInfo.getOrgId();
		MixedGridInfo gridInfo = this.getMixedGridInfo(session, request);
//		Long gridId = gridInfo.getGridId();
//		params.put("orgCode", gridInfo.getInfoOrgCode());
//		params.put("gridId", gridId);
//		params.put("orgId", orgId);
		if(null==params.get("orgCode")) {
			params.put("orgCode", gridInfo.getInfoOrgCode());
		}
		 return nanpingVideoDataService.findData(params);
		}
	 
	 
	 /**
	 * 区域信息
	 * @param request
	 * @param session
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value="/listOtherData")
	 public Map<String, Object> listOtherData(HttpServletRequest request,
			    HttpSession session,@RequestParam Map<String, Object> params) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		 Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		 MixedGridInfo gridInfo = mixedGridInfoService
					.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
		Long orgId = userInfo.getOrgId();
		if(null==params.get("orgCode")) {
			params.put("orgCode", gridInfo.getInfoOrgCode());
		}
		if(null==params.get("gridId")) {
			params.put("gridId", gridInfo.getGridId());
		}
		params.put("orgId", orgId);
		return nanpingVideoDataService.findOtherData(params);
	}
	 
	 
	 /**
	 * 周边网格员
	 * @param request
	 * @param session
	 * @return
	 */
	 @ResponseBody
	 @RequestMapping(value="/gridListData")
	 public List<Map<String, Object>> gridListData(HttpServletRequest request,
			 	@RequestParam(value="x", required=false) String x,
			 	@RequestParam(value="y", required=false) String y,
			 	@RequestParam(value="distance", required=false) String distance,
			 	@RequestParam(value="time", required=false) String time,
			 	@RequestParam(value="orgCode", required=false) String orgCode,
			    HttpSession session) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = this.getMixedGridInfo(session, request);
		Long gridId = gridInfo.getGridId();
		Map<String, Object> params=new HashMap<String, Object>();
		if(StringUtils.isBlank(orgCode)) {
			orgCode=gridInfo.getInfoOrgCode();
		}
		params.put("orgCode", orgCode);
		params.put("gridId", gridId);
		if(!StringUtils.isEmpty(x) ) {
			params.put("x", x);
			
		}
		if(!StringUtils.isEmpty(y) ) {
			params.put("y", y);	
			
		}
		if(!StringUtils.isEmpty(distance) ) {
			//截掉最后一位“m”
			distance = distance.substring(0,distance.length() - 1);
			params.put("distance", distance);	
			
		}
		if(!StringUtils.isEmpty(time) ) {
			params.put("time", time);	
			
		}
		List<Map<String, Object>> map = nanpingVideoDataService.gridListData(params);
		 return map;
		}
	 
	 
	 /**
		 * 周边全球眼
		 * @param request
		 * @param session
		 * @return
		 */
		 @ResponseBody
		 @RequestMapping(value="/globalsEyeList")
		 public List<Map<String, Object>> globalsEyeList(HttpServletRequest request,
				 	@RequestParam(value="x", required=false) String x,
				 	@RequestParam(value="y", required=false) String y,
				 	@RequestParam(value="distance", required=false) String distance,
				 	@RequestParam(value="time", required=false) String time,
				    HttpSession session) {
			 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			 String orgCode = userInfo.getOrgCode();
			MixedGridInfo gridInfo = this.getMixedGridInfo(session, request);
			Long gridId = gridInfo.getGridId();
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("orgCode", gridInfo.getInfoOrgCode());
			params.put("infoOrgCode", gridInfo.getInfoOrgCode());
			params.put("gridId", gridId);
			if(!StringUtils.isEmpty(x) ) {
				params.put("x", x);
				
			}
			if(!StringUtils.isEmpty(y) ) {
				params.put("y", y);	
				
			}
			if(!StringUtils.isEmpty(distance) ) {
				//截掉最后一位“m”
				distance = distance.substring(0,distance.length() - 1);
				params.put("distance", distance);	
				
			}
			if(!StringUtils.isEmpty(time) ) {
				params.put("time", time);	
				
			}
			List<Map<String, Object>> map = nanpingVideoDataService.globalsEyeList(params);
			 return map;
			}
		 
		 
		 /**
		 * 事件详情
		 * @param request
		 * @param session
		 * @return
		 */
		 @RequestMapping(value="/eventDetail")
		 public String eventDetail(HttpServletRequest request,
				 	@RequestParam(value="eventId", required=false) String eventId,
				 	@RequestParam(value="mapt", required=false) String mapt,
				    HttpSession session,ModelMap map) {
			 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			 String IMG_URL = App.IMG.getDomain(session);
			 
			String userOrgCode = userInfo.getOrgCode();
			if(eventId.indexOf(",")>0) {
				eventId=eventId.substring(0,eventId.indexOf(","));
			}
			
			 //事件的图片
			 List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(eventId), ConstantValue.EVENT_ATTACHMENT_TYPE);
			 if(attachments!=null && attachments.size()> 0){
            	for(Attachment attachment : attachments){
                    if(attachment.getFileName().indexOf(".png") > 0 ||attachment.getFileName().indexOf(".jpg") > 0){
                    	if(attachment.getEventSeq().equals("1")) {
                    		String path = IMG_URL+attachment.getFilePath();
                    		map.put("path1", path);
                    	}
                    	if(attachment.getEventSeq().equals("2")) {
                    		String path = IMG_URL+attachment.getFilePath();
                    		map.put("path2", path);
                    	}
                    	if(attachment.getEventSeq().equals("3")) {
                    		String path = IMG_URL+attachment.getFilePath();
                    		map.put("path3", path);
                    	}
                    	
                    }
            	}
            }

			
			EventDisposal event = eventDisposalService.findEventByIdAndMapt(Long.valueOf(eventId), mapt, userOrgCode);
			
			if(event == null) {
				return "-1";
			}
			map.put("event", event);
			map.put("eventId", eventId);
			map.put("zzgridDomain", App.ZZGRID.getDomain(session));
			 return "/map/arcgis/standardmappage/monitor_detail_event.ftl";
			}
		 
			/**
			 * 大华视频播放页面
			 * @return
			 */
			@RequestMapping(value="/playPage")
			public String playPage(ModelMap map) {
				map.addAttribute("tempdir",System.getProperty("java.io.tmpdir"));
				return "/map/arcgis/standardmappage/monitor/globalVideo16.ftl";
			}
			/**
			 * 全球眼信息
			 * @param request
			 * @param response
			 * @param id
			 * @throws Exception
			 */
			@RequestMapping(value="/findGlabalEyeInfo")
			public void findGlabalEyeInfo(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="id") Long id) throws Exception {
				MonitorBO n = monitorService.getMonitorById(id);
				JSONObject jsonObj = JSONObject.fromObject(n);
				String dataJson = jsonObj.toString();
				String callback = request.getParameter("callback");
				if(callback!=null){
					outJosn(response,callback+"("+dataJson+")");
				}else{
					outJosn(response,dataJson+"");
				}
			}

			/**
			 * 紧急待办列表
			 * @param request
			 * @param session
			 * @return
			 */
			 @ResponseBody
			 @RequestMapping(value="/findListByUrgencyDegree")
			 public List<Map<String, Object>> findListByUrgencyDegree(HttpServletRequest request,
					    HttpSession session,@RequestParam Map<String, Object> params) {
				 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
				Long orgId = userInfo.getOrgId();
				params.put("orgId", orgId);//orgCode 没有传时默认登录用户的id
				List<Map<String, Object>> list = nanpingVideoDataService.findListByUrgencyDegree(params);
				return list;
			}
			 /**
			  * 人员所在组织的辖区待办列表
			  * @param request
			  * @param session
			  * @param page
			  * @param rows
			  * @return
			  */
			 @ResponseBody
			 @RequestMapping(value="/findPagListByUrgencyDegree")
			 public EUDGPagination findPagListByUrgencyDegree(
						HttpServletRequest request,
						HttpSession session,
						@RequestParam(value = "page") int page,
						@RequestParam(value = "rows") int rows,@RequestParam Map<String, Object> params) {
					if (page <= 0)
						page = 1;
				 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
				Long orgId = userInfo.getOrgId();
				params.put("orgId", orgId);
				return nanpingVideoDataService.findPagListByUrgencyDegree(page, rows, params);
			}

}
