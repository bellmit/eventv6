package cn.ffcs.zhsq.szzg.zgAlarm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.zgAlarm.ZgAlarm;
import cn.ffcs.zhsq.szzg.zgAlarm.service.IZgAlarmService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**   
 * @Description: 告警信息模块控制器
 * @Author: linzhu
 * @Date: 08-07 15:03:54
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping(value = "/zhsq/szzg/zgAlarm")
public class ZgAlarmController {

	@Autowired
	private IZgAlarmService zgAlarmService; //注入告警信息模块服务
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "zgAlarm";
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		map.put("ZG_ALARM_TYPE", ConstantValue.ZG_ALARM_TYPE);//告警类型
		map.put("ZG_ALARM_LEVEL", ConstantValue.ZG_ALARM_LEVEL);//告警级别
		return REAL_PATH + SUB_MAIN + "/list_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("alarmContent", request.getParameter("alarmContent"));
		params.put("alarmType", request.getParameter("alarmType"));
		params.put("alarmUrl", request.getParameter("alarmUrl"));
		params.put("alarmSource", request.getParameter("alarmSource"));
		params.put("invalidDate", request.getParameter("invalidDate"));
		params.put("alarmLevel", request.getParameter("alarmLevel"));
		EUDGPagination pagination = zgAlarmService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		ZgAlarm bo = zgAlarmService.searchById(id);
		map.addAttribute("bo", bo);
		return REAL_PATH + SUB_MAIN + "/detail_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object add(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		ZgAlarm bo =null;
		if (id != null) {
			 bo = zgAlarmService.searchById(id);
		}else{
			bo=new ZgAlarm();
		}
		map.put("bo", bo);
		map.put("ZG_ALARM_TYPE", ConstantValue.ZG_ALARM_TYPE);//告警类型
		map.put("ZG_ALARM_LEVEL", ConstantValue.ZG_ALARM_LEVEL);//告警级别
		return REAL_PATH + SUB_MAIN + "/add_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public ResultObj save(HttpServletRequest request, HttpSession session, ModelMap map,
		ZgAlarm bo) {
		// 获取当前用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj rs=null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if(bo!=null){
			if(bo.getInvalidDate()==null){
				bo.setInvalidDate(DateUtils.formatDate("2099-12-31", DateUtils.PATTERN_DATE));
			}
		}
		if (bo.getAlarmId() == null) { //新增
			bo.setCreateUserId(userInfo.getUserId());
			rs= Msg.ADD.getResult(zgAlarmService.insert(bo));
		} else { //修改
			bo.setUpdateUserId(userInfo.getUserId());
			rs= Msg.EDIT.getResult(zgAlarmService.update(bo));
		}
		resultMap.put("result", result);
		return rs;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		ZgAlarm bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = zgAlarmService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	@RequestMapping("/report")
	public Object report(HttpServletRequest request, HttpSession session, ModelMap model) {
	    List<ZgAlarm> list=zgAlarmService.findZgAlarmList();
	    model.put("list", list);
		return REAL_PATH + SUB_MAIN + "/report_" + SUB_MAIN + ".ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/findZgAlarmCount")
	public int findZgAlarmCount(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		return zgAlarmService.findZgAlarmCount();
	}

}