package cn.ffcs.zhsq.map.arcgis.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.doorsys.bo.record.CarRecord;
import cn.ffcs.doorsys.bo.record.CarRecord.CarEquipmentType;
import cn.ffcs.doorsys.bo.record.ResidentRecord;
import cn.ffcs.doorsys.bo.record.ResidentRecord.RsEquipmentType;
import cn.ffcs.doorsys.service.record.ICarRecordService;
import cn.ffcs.doorsys.service.record.IResidentRecordService;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 立体防控地图信息
 * @author zhongshm
 * @create 2016-8-2上午9:17:56
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/equ")
public class ArcgisDataOfEquController extends ZZBaseController {

	@Autowired
	private IResidentRecordService residentRecordService;
	@Autowired
	private ICarRecordService carRecordService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@RequestMapping(value="/imp/list")
	public String imp_list(HttpServletRequest req, HttpSession session, 
			ModelMap map) {
		map.addAttribute("orgCode", "");
		String buildingId = req.getParameter("buildingId");
		map.addAttribute("buildingId", buildingId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("gridId", this.getDefaultGridInfo(session).get(KEY_START_GRID_ID));
		return "/map/arcgis/standardmappage/equipment/imp/list_imp.ftl";
	}

	@RequestMapping(value="/imp/listData",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String imp_list_data(HttpSession session,HttpServletRequest request,ModelMap map,
			@RequestParam(value = "page") int page,
		    @RequestParam(value = "rows") int rows){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String buildingId = request.getParameter("buildingId");
//		String buildingId = "1050568";
		
		String buildingName = "";
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isBlank(buildingId)){
			return sb.toString();
		}
		AreaBuildingInfo building = areaBuildingInfoService.findAreaBuildingInfoById(Long.valueOf(buildingId));
		if(building!=null){
			buildingName = building.getBuildingName();
		}
		List<ResidentRecord> records = residentRecordService.findKeyPersonExceptionStatisticsListByBuildId(RsEquipmentType.ACSLog, Long.valueOf(buildingId), page, rows);
		for(ResidentRecord rsRecord : records){
			String birthday = DateUtils.formatDate(rsRecord.getCiRsTop().getBirthday(), DateUtils.PATTERN_DATE);
			//EXCEPTIONCOUNT异常出入次数
			String EXCEPTIONCOUNT = rsRecord.getRecord().get("exceptionCount").toString();
			//重口
			Long ciRsId = rsRecord.getRsId();
			String rsName = rsRecord.getCiRsTop().getName();
			Map<String, Object> tagFlag = ciRsService.getRsTagFlag(ciRsId);// 人员标签
			
			//是否启用国标
			String ENABLE_GB = this.funConfigurationService.turnCodeToValue(
					ConstantValue.ENABLE_GB, null,
					IFunConfigurationService.CFG_TYPE_FACT_VAL,
					userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			List tagList = new ArrayList();
			Set set = tagFlag.keySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				String s = (String) it.next();
				if (s.equals("hbcare")) {
					continue;
				}
				if (tagFlag.get(s).toString().equals("1")) {// 1为存在标签，0为不存在
					String typeVal = getTypeVlue(s);
					if (StringUtils.isNotBlank(typeVal)) {
						String[] tag = { typeVal, getValue(s) };
						tagList.add(tag);
					}
				}
			}
			
			sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			sb.append("<tr>");
			sb.append("<td rowspan=\"4\"><img src=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_img/zd_img1.jpg\" width=\"54\" height=\"63\" /></td>");
			sb.append("<td>"+rsRecord.getCiRsTop().getName()+"&nbsp;&nbsp;&nbsp;"+rsRecord.getCiRsTop().getGenderCN()+"&nbsp;&nbsp;&nbsp;"+birthday+"出生</td>");
			sb.append("<td rowspan=\"3\"><div class=\"ycbox\">"+EXCEPTIONCOUNT+"<div class=\"yc-tit\">异常出入</div></div></td>");
			sb.append("</tr>");
			sb.append("<tr>");
			sb.append("<td>所属楼宇："+buildingName+"</td>");
			sb.append("</tr>");
			sb.append("<tr>");
			sb.append("<td>身份证：<span class=\"c-sfz\">"+rsRecord.getCiRsTop().getIdentityCard()+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
			sb.append("</tr>");
			sb.append("<tr>");
			sb.append("<td>");
			Iterator its = tagList.iterator();
			while (its.hasNext()) {
				String name = "";
				String color = "";
				String[] tag = (String[]) its.next();
				if(tag[0] == "11"){
					name = "吸毒人员";
					color = "#bf3630";
				}else if(tag[0] == "14"){
					name = "刑释解教人员";
					color = "#871dca";
				}else if(tag[0] == "7"){
					name = "残障";
					color = "#FA8072";
				}else if(tag[0] == "8"){
					name = "重精神病";
					color = "#A9A9A9";
				}else if(tag[0] == "13"){
					name = "矫正人员";
					color = "#90EE90";
				}else if(tag[0] == "9"){
					name = "危险品从业人员";
					color = "#B03060";
				}else if(tag[0] == "6"){
					name = "低保";
					color = "#8B8B00";
				}else if(tag[0] == "2"){
					name = "退休";
					color = "#8B5F65";
				}else if(tag[0] == "5"){
					name = "失业";
					color = "#5F9EA0";
				}else if(tag[0] == "19"){
					name = "重点青少年";
					color = "#5F9EA0";
				}else if(tag[0] == "15"){
					name = "志愿者";
					color = "#5F9EA0";
				}else{
					name = "无";
					color = "#FFFFFF";
				}
				sb.append("<div style='background:"+color+";' class='tag'><img style='cursor:pointer;' onclick='showDetail("+ciRsId+","+tag[0]+",\""+tag[1]+"("+rsName+")"+"\")' src=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_config/images/"+name+".png\" width=\"30\" height=\"30\" /></div>&nbsp;&nbsp;");
			}
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("</table>");

//			sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
//			sb.append("<tr>");
//			sb.append("<td rowspan=\"3\"><img Sr=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_img/zd_img1.jpg\" width=\"54\" height=\"63\" /></td>");
//			sb.append("<td>"+rsRecord.getCiRsTop().getName()+"&nbsp;&nbsp;&nbsp;"+rsRecord.getCiRsTop().getGenderCN()+"&nbsp;&nbsp;&nbsp;1978-12-10出生</td>");
//			sb.append("<td rowspan=\"3\"><div class=\"ycbox\">9<div class=\"yc-tit\">异常出入</div></div></td>");
//			sb.append("</tr>");
//			sb.append("<tr>");
//			sb.append("<td>所属楼宇：红谷滩世纪花园社区</td>");
//			sb.append("</tr>");
//			sb.append("<tr>");
//			sb.append("<td>身份证：<span class=\"c-sfz\">350898909098767876</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_img/zd_icon1.png\" width=\"15\" height=\"15\" /></td>");
//			sb.append("</tr>");
//			sb.append("</table>");
		}
		
//		for(int i=0;i<20;i++){
//			sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
//			sb.append("<tr>");
//			sb.append("<td rowspan=\"3\"><img src=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_img/zd_img1.jpg\" width=\"54\" height=\"63\" /></td>");
//			sb.append("<td>王连盛&nbsp;&nbsp;&nbsp;男&nbsp;&nbsp;&nbsp;1978-12-10出生</td>");
//			sb.append("<td rowspan=\"3\"><div class=\"ycbox\">9<div class=\"yc-tit\">异常出入</div></div></td>");
//			sb.append("</tr>");
//			sb.append("<tr>");
//			sb.append("<td>所属楼宇：红谷滩世纪花园社区</td>");
//			sb.append("</tr>");
//			sb.append("<tr>");
//			sb.append("<td>身份证：<span class=\"c-sfz\">350898909098767876</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\""+App.UI.getDomain(session)+"/images/map/gisv0/special_img/zd_icon1.png\" width=\"15\" height=\"15\" /></td>");
//			sb.append("</tr>");
//			sb.append("</table>");
//		}
		return sb.toString();
	}

	@RequestMapping(value="/car/list")
	public String car_list(HttpServletRequest req, HttpSession session, 
			ModelMap map) {
		String equSn = req.getParameter("equSn");
		map.addAttribute("equSn", equSn);
		return "/map/arcgis/standardmappage/equipment/imp/list_car.ftl";
	}
	
	@RequestMapping(value="/car/listData",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String car_list_data(HttpSession session,HttpServletRequest request,ModelMap map,
			@RequestParam(value = "page") int page,
		    @RequestParam(value = "rows") int rows) throws ParseException{
//		var request.get
		String equSn = request.getParameter("equSn");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eqpSn", equSn);
		//默认凌晨0点~5点
		List<CarRecord> carRecords = carRecordService.findExceptionStatisticsList(null, params, page, rows);

		StringBuffer sb = new StringBuffer();
//		sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		for(CarRecord carRecord : carRecords){
			String carNumber = StringUtils.isBlank(carRecord.getCarNumber())?"未识别":carRecord.getCarNumber();
			Long idRsCar = null;
			if(carRecord.getCarInfo() != null){
				idRsCar = carRecord.getCarInfo().getIdRsCar();
			}
			String lastTime = "";
			if(null != carRecord.getRecord().get("lastTime")){
				lastTime = carRecord.getRecord().get("lastTime").toString();
			}
			lastTime = DateUtils.formatDate(lastTime, DateUtils.PATTERN_24TIME);
			//<li><span class="plate">闽AU1234</span><span class="degree">异常出入：<span class="red">23339</span>次</span><span class="time">最近异常：2016-06-01 12：00</span></li>
			sb.append("<li>");
			sb.append("<span class='plate'>"+carNumber+"</span>");
			sb.append("<span class='degree'>异常出入：<span class='red'>"+carRecord.getRecord().get("exceptionCount")+"</span>次</span>");
			sb.append("<span class='time'>最近异常："+lastTime+"</span>");
			sb.append("</li>");
//			sb.append("<td><p style='cursor:pointer;height:40px;line-height:40px;float:left;' onclick='show("+idRsCar+")'><span style='padding:4px;background-color:#FFA07A;width:70px;'>"+carNumber+"</span>&nbsp;&nbsp;");
//			sb.append("异常出入次数：").append("<font style='color:red'>"+carRecord.getRecord().get("exceptionCount")+"</font>").append("次&nbsp;&nbsp;");
//			sb.append("最近异常："+carRecord.getRecord().get("lastTime")+"</p></td>");
//			sb.append("<li>");
//			sb.append("</tr>");
		}
//		sb.append("</table>");
		return sb.toString();
	}
	
	public String getTypeVlue(String key) {
		Map map = new HashMap();
		map.put("party", "1");
		map.put("retire", "2");
		map.put("old", "3");
		map.put("hbcare", "3");
		map.put("army", "4");
		map.put("unemployed", "5");
		map.put("poor", "6");
		map.put("deformity", "7");
		map.put("mentalIllnessRecord", "8");
		map.put("dangerousGoodsRecord", "9");
		map.put("petitionRecord", "10");
		map.put("drugRecord", "11");
		map.put("cultMemberRecord", "12");
		map.put("correctionalRecord", "13");
		map.put("releasedRecord", "14");
		map.put("volunteer", "15");
		map.put("taibao", "16");
		map.put("motoMemberRecord", "17");
		map.put("youthPerson", "19");
		map.put("cleaner", "20");
		map.put("aidsPerson", "21");
		return map.get(key) != null ? map.get(key).toString() : "";
	}
	
	public String getValue(String key) {
		Map map = new HashMap();
		map.put("party", "党员");
		map.put("retire", "退休");
		map.put("old", "老年人");
		map.put("hbcare", "居家养老");
		map.put("army", "服兵役");
		map.put("unemployed", "失业");
		map.put("poor", "低保");
		map.put("deformity", "残障");
		map.put("mentalIllnessRecord", "精神障碍");
		map.put("dangerousGoodsRecord", "危险品");
		map.put("petitionRecord", "上访");
		map.put("drugRecord", "吸毒");
		map.put("cultMemberRecord", "邪教人员");
		map.put("motoMemberRecord", "摩托车工");
		map.put("correctionalRecord", "矫正");
		map.put("releasedRecord", "刑释");
		map.put("volunteer", "志愿者");
		map.put("taibao", "台胞");
		map.put("youthPerson", "重点青少年");
		map.put("cleaner", "清扫保洁人员");
		map.put("aidsPerson", "艾滋病人员");
		return map.get(key) != null ? map.get(key).toString() : "";
	}
}
