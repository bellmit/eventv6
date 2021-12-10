package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.file.service.FileUrlProvideService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 呼叫中心
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisCenterControl")
public class ArcgisCenterControlDataController extends ZZBaseController {
	
	@Autowired
	private IGridAdminService gridAdminService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
    private SendSmsService sendSmsService;
	
	@Autowired
	private FileUrlProvideService fileUrlProvideService;
	
	/**
	 * 呼叫中心数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/voiceCall")
	public String gridAdmin(HttpSession session, @RequestParam(value="gridId") Long gridId, 
			@RequestParam(value = "isCross", required=false) String isCross, 
			ModelMap map) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		 map.addAttribute("isCross", isCross);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> dutyDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_GRID_ADMIN, ConstantValue.COLUMN_GRID_ADMIN_DUTY, userInfo.getOrgCode());
	    map.addAttribute("dutyDC", dutyDC);
	    //map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
	    map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格
	    //map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
	    return "/map/arcgis/standardmappage/centerControl/voiceCall.ftl";
	}
	
	/**
	 * 网格管理人员数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/gridAdminListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination gridAdminListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="name", required=false) String name,
			 @RequestParam(value="duty", required=false) String duty) {
		if(page<=0) page=1;
		if(name!=null&&!"".equals(name)){name = name.trim();}else{
			name=null;
		}
		if(duty!=null&&!"".equals(duty)){duty = duty.trim();}else{duty=null;}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("name", name);
		params.put("duty", duty);
		cn.ffcs.common.EUDGPagination pagination = gridAdminService.findGridAdminPagination(page, rows, params);
		return pagination;
	}
	/**
	 * 发送信息
	 * @param session
	 * @param otherDialNum
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sendSMS", method=RequestMethod.POST)
	public  Map<String, Object> sendSMS(HttpSession session, @RequestParam(value="otherDialNum") String otherDialNum, @RequestParam(value="content") String content) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		int row=0;
		if(otherDialNum!=null){
			otherDialNum=otherDialNum.trim();
			String [] arry=otherDialNum.split(",");
			if(content!=null){content=content.trim();}
			if(arry!=null&&arry.length>0){
//				for(int i=0;i<arry.length;i++) {
					try {
						//sendSmsService.sendSmsByOrgCode(defaultGridInfo.get(KEY_START_GRID_ID).toString(), arry, content);
						SendResult rs = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG, userInfo.getUserId(), userInfo.getOrgCode(), arry, content, SendSmsService.TYPE_SMS, null, null);
						row ++;
					} catch (Exception e) { e.printStackTrace(); }
//				}
			}
		}
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("result", row>0?true:false);
		return resultMap;
	}
	
	/* ************************************** */
	/*   短信群发功能      */
	/**
	 * 短信群发
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sendNote")
	public String sentNote(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> dutyDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_GRID_ADMIN, ConstantValue.COLUMN_GRID_ADMIN_DUTY, userInfo.getOrgCode());
	    map.addAttribute("dutyDC", dutyDC);
	   // map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
	    //map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格
	    //map.addAttribute("RESOURSE_SERVER_PATH", ConstantValue.RESOURSE_SERVER_PATH);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
	    return "/map/arcgis/standardmappage/centerControl/sendNote.ftl";
	}
	
}
