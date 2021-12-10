package cn.ffcs.zhsq.api;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

@Controller
@RequestMapping("/zhsq/event/syncEventDataForRJController")
public class SyncEventDataForRJController extends ZZBaseController {

	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;

	@RequestMapping(value = "/syncData")
	public String syncData(HttpSession session, ModelMap map) {
		return "/zzgl/event/syncData/syncData_RJ.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/excuteSync")
	public Map<String, Object> excuteSync(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "from") int from, @RequestParam(value = "limit") int limit) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		String infoOrgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> data = getEventData(from, limit);
		if (data != null && "0".equals(String.valueOf(data.get("retcode")))) {
			List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get("data");
			if (rows != null && rows.size() > 0) {
				try {
					for (Map<String, Object> map : rows) {
						EventDisposal event = map2bean(map, gridId, infoOrgCode);
						eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userInfo, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
					data.put("retcode", "7");
					data.put("msg", "调用事件采集并结案接口失败：" + e.getMessage());
				}
			} else {
				data.put("retcode", "8");
				data.put("msg", "查无数据");
			}
		}
		/*
		retcode =0  成功返回
		retcode =1  参数错误
		retcode =2  查询无结果
		retcode =3  服务器内部错误
		retcode =6  鉴权失败
		 */
		return data;
	}
	
	public EventDisposal map2bean(Map<String, Object> map, Long gridId, String regionCode) {
		EventDisposal eventDisposal = new EventDisposal();
		eventDisposal.setEventName(getVal(map.get("title")));// 标题
		eventDisposal.setContent(getVal(map.get("contents")));// 内容
		eventDisposal.setType(getEventType(getVal(map.get("catalogId"))));
		eventDisposal.setContactUser(getVal(map.get("callerName")));// 诉求人
		eventDisposal.setTel(getVal(map.get("callerPhone")));// 联系电话
		eventDisposal.setSource(getSource(getVal(map.get("comeFrom"))));
		eventDisposal.setOccurred(getVal(map.get("eventAddr")));
		eventDisposal.setGridId(gridId);
		eventDisposal.setGridCode(regionCode);
		eventDisposal.setHappenTimeStr(getVal(map.get("eventTime")) + " 00:00:00");
		/*try {
			// 发生事件
			eventDisposal.setHappenTime(DateUtils.convertStringToDate(getVal(map.get("eventTime")), "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		eventDisposal.setCollectWay("06");
		eventDisposal.setUrgencyDegree("01");
		eventDisposal.setBizPlatform("205");
		
		return eventDisposal;
	}

	public Map<String, Object> getEventData(int from, int limit) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			String urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getSearchCall&format=json&sq=%7BsearchField:1%7D";
			urlNameString += "&zoneId=34665";
			urlNameString += "&handleStatusList=4";
			urlNameString += "&from=" + from;
			urlNameString += "&limit=" + limit;
			String rs = HttpUtil.doPost(urlNameString, "");
			data = JsonUtils.json2Map(rs);
			if (data != null && "0".equals(String.valueOf(data.get("retcode")))) {
				List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get("data");
				if (rows != null && rows.size() > 0) {
					try {
						for (Map<String, Object> map : rows) {
							urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getCall";
							urlNameString += "&callid=" + getVal(map.get("callId"));
							urlNameString += "&Code=" + getVal(map.get("validateCode"));
							urlNameString += "&format=json";
							rs = HttpUtil.doPost(urlNameString, "");
							Map<String, Object> detail = JsonUtils.json2Map(rs);
							if (detail != null && "0".equals(getVal(detail.get("retcode")))) {
								detail = (Map<String, Object>) detail.get("data");
								String contents = getVal(detail.get("contents"));
								map.put("contents", contents);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						data.put("retcode", "17");
						data.put("msg", "调用对方详情接口失败：" + e.getMessage());
					}
				} else {
					data.put("retcode", "8");
					data.put("msg", "查无数据");
				}
			}
		} catch (Exception e) {
			data.put("retcode", "9");
			data.put("msg", "调用对方接口出错：" + e.getMessage());
		}
		return data;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEventNum")
	public Object getEventNum(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "zoneId", required = false, defaultValue = "34665") String zoneId,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String jsoncallback = request.getParameter("jsoncallback");
		try {
			String baseUrl = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getSearchCall&format=json";
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			//所属区域
			if(StringUtils.isNotBlank(zoneId)) {
				paramMap.put("zoneId",zoneId);
			}else {
				paramMap.put("zoneId","34665");
			}
			//开始时间
			if(StringUtils.isNotBlank(startTime)) {
				paramMap.put("startTime",startTime + " 00:00:00");
			}
			//结束时间
			if(StringUtils.isNotBlank(endTime)) {
				paramMap.put("endTime",endTime+ " 23:59:59");
			}
			
			//全部
			String urlNameString = baseUrl + "&sq=" + URLEncoder.encode(JsonUtils.mapToJson(paramMap),"utf-8");
			String rs = HttpUtil.doPost(urlNameString, "");
			Map<String, Object> allData = JsonUtils.json2Map(rs);
			//已完成
			paramMap.put("handleStatusList","4");
			urlNameString = baseUrl + "&sq=" + URLEncoder.encode(JsonUtils.mapToJson(paramMap),"utf-8");
			rs = HttpUtil.doPost(urlNameString, "");
			Map<String, Object> finishData = JsonUtils.json2Map(rs);
			
			Map<String, Object> data = new HashMap<String, Object>();
			if(allData != null && "0".equals(String.valueOf(allData.get("retcode")))){
				int totalNum = Integer.parseInt(String.valueOf(allData.get("totalNum")));
				data.put("totalNum", totalNum);
			}else {
				returnMap.put("code", allData.get("retcode"));
				returnMap.put("msg", getErrorMsg(getVal(allData.get("retcode"))));
				jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(returnMap) + ")";
				return jsoncallback;
			}
			if(finishData != null && "0".equals(String.valueOf(finishData.get("retcode")))){
				int finishNum = Integer.parseInt(String.valueOf(finishData.get("totalNum")));
				data.put("finishNum", finishNum);
			}else {
				returnMap.put("code", allData.get("retcode"));
				returnMap.put("msg", getErrorMsg(getVal(allData.get("retcode"))));
				jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(returnMap) + ")";
				return jsoncallback;
			}
			returnMap.put("data", data);
		} catch (Exception e) {
			returnMap.put("code", "9");
			returnMap.put("msg", "调用对方接口出错：" + e.getMessage());
			jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(returnMap) + ")";
			return jsoncallback;
			//System.out.println("调用对方接口出错：" + e.getMessage());
		}
		returnMap.put("code", "0");
		returnMap.put("msg", "成功返回");
		jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(returnMap) + ")";
		return jsoncallback;
	}
	
	public String getRegionCode(String deptId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("34665", ""); // 延平区
		return getVal(map.get(deptId));
	}
	
	public String getEventType(String catalogId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("101", "20026001"); // 政治综合
		map.put("276", "20026002"); // 经济综合
		map.put("277", "20026003"); // 宣传舆论
		map.put("89", "20026004"); // 纪检监察
		map.put("278", "20026005"); // 政法
		map.put("279", "20026006"); // 劳动社保
		map.put("280", "20026007"); // 教育
		map.put("185", "20026008"); // 卫生计生
		map.put("105", "20026009"); // 科技文体
		map.put("288", "20026010"); // 组织人事
		map.put("287", "20026011"); // 交通能源环保
		map.put("281", "20026012"); // 民政
		map.put("282", "20026013"); // 农村农业
		map.put("283", "20026014"); // 国土资源水利林业
		map.put("284", "20026015"); // 城乡建设
		map.put("285", "20026016"); // 信息产业
		map.put("286", "20026017"); // 商贸旅游
		map.put("172", "20026999"); // 其他
		String type = getVal(map.get(catalogId));
		if (StringUtils.isBlank(type)) {
			type = "20026999";
		}
		return type;
	}
	
	public String getSource(String comeFrom) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "12");// 0	网站	
		map.put("1", "07");// 1	电话	
		map.put("2", "06");// 2	短信	
		map.put("3", "30");// 3	邮件	
		map.put("4", "31");// 4	传真	
		map.put("5", "32");// 5	市长信箱	
		map.put("6", "99");// 6	其他	
		map.put("7", "33");// 7	录音	
		map.put("8", "04");// 8	QQ	
		map.put("9", "34");// 9	APP
		return getVal(map.get(comeFrom));
	}
	
	private String getErrorMsg(String retcode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "成功返回");
		map.put("1", "参数错误");
		map.put("2", "查询无结果");
		map.put("3", "服务器内部错误");
		map.put("6", "鉴权失败");
		return getVal(map.get(retcode));
	}
	
	public String getVal(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

}
