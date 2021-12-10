package cn.ffcs.zhsq.executeControl.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.executeControl.service.IMonitorTaskService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**   
 * @Description: 布控任务管理模块控制器
 * @Author: dtj
 * @Date: 07-22 17:50:39
 * @Copyright: 2020 福富软件
 */ 
@Controller("monitorTaskController")
@RequestMapping("/zhsq/event/monitorTask")
public class MonitorTaskController {

	@Autowired
	private IMonitorTaskService monitorTaskService; //注入布控任务管理模块服务

	/**
	 * 列表页面
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		map.put("taskType", ConstantValue.MONITOR_TASK_TYPE);
		map.put("taskStatus", ConstantValue.TASK_TYPE);
		return "/zzgl/executeControl/list_monitorTask.ftl";
	}

	/**
	 * 列表数据
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,MonitorTask bo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskType",bo.getTaskType());
		params.put("name",bo.getName());
		params.put("libIds",bo.getLibIds());
		params.put("queryType",bo.getQueryType());
		params.put("ignoreStatus",bo.getIgnoreStatus());
		params.put("taskStatus",bo.getTaskStatus());
		EUDGPagination pagination = null;
		try {
			pagination = monitorTaskService.searchList(bo, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 详情页面
	 * @param request
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping("/view")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		String id) {
		try {
			String token = monitorTaskService.getToken();
			MonitorTask bo = monitorTaskService.searchByControlTaskId(id,token);
			map.addAttribute("bo", bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/zzgl/executeControl/list_executeControl.ftl";
	}

	/**
	 * 表单页面
	 * @param request
	 * @param session
	 * @param map
	 * @param id
	 * @param libIds
	 * @return
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
					   @RequestParam(value = "id", required = false)Long id, String libIds) {
		MonitorTask bo = new MonitorTask();
		if (id != null) {
			try {
				String token = monitorTaskService.getToken();
				bo = monitorTaskService.searchById(id,token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			bo.setLibIds(libIds);
		}
		map.put("bo", bo);
		map.put("taskType", ConstantValue.MONITOR_TASK_TYPE);
		map.put("alarmMode", ConstantValue.ALARM_MODE);
		map.put("repeatMode", ConstantValue.REPEAT_MODE);
		map.put("expireType", ConstantValue.EXPIRE_TYPE);

		return "/zzgl/executeControl/form_monitorTask.ftl";
	}

	/**
	 * 布控设备
	 * @param request
	 * @param session
	 * @param map
	 * @param json
	 * @return
	 */
	@RequestMapping("/deviceDisForm")
	public Object deviceDisForm(HttpServletRequest request, HttpSession session, ModelMap map
		,String json) {
		map.put("json",json);
		return "/zzgl/executeControl/form_deviceDis.ftl";
	}

	/**
	 * 保存数据
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		MonitorTask bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		Map<String, Object> resultMap = null;
		try {
			String token = monitorTaskService.getToken();
			resultMap = new HashMap<String, Object>();
			String result = "fail";
			if (bo.getId() == null) { //新增
				bo.setCreator(userInfo.getUserId());  //设置创建人
				Long id = monitorTaskService.insert(bo,token);
				if (id > 0) {
					result = "success";
				};
			} else { //修改
				bo.setUpdator(userInfo.getUserId());  //设置更新人
				boolean updateResult = monitorTaskService.update(bo,token);
				if (updateResult) {
					result = "success";
				}
			}
			resultMap.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 删除数据
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		MonitorTask bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = null;
		try {
			String token = monitorTaskService.getToken();
			resultMap = new HashMap<String, Object>();
			String result = "fail";
			boolean delResult = monitorTaskService.delete(bo,token);
			if (delResult) {
				result = "success";
			}
			resultMap.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 根据布控库ID查询布控任务数
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCount")
	public Object getCount(HttpServletRequest request, HttpSession session, ModelMap map,
					  MonitorTask bo) {
		long count = 0;
		try {
			count = monitorTaskService.getCount(bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 设置是否忽略布控任务页面
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@RequestMapping("/editIgnoreIndex")
	public Object editIgnoreIndex(HttpServletRequest request, HttpSession session, ModelMap map,
									  MonitorTask bo){
		map.put("bo",bo);
		map.put("ignoreStatus", ConstantValue.IGNORE_STATUS);
		return "/zzgl/executeControl/form_ignoreStatus.ftl";
	}

	/**
	 * 设置布控任务状态页面
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@RequestMapping("/editTaskIndex")
	public Object editTaskIndex(HttpServletRequest request, HttpSession session, ModelMap map,
									  MonitorTask bo){
		map.put("bo",bo);
		return "/zzgl/executeControl/form_taskStatus.ftl";
	}

	/**
	 * 设置是否忽略报警消息
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editIgnoreStatus")
	public Object editIgnoreStatus(HttpServletRequest request, HttpSession session, ModelMap map,
						   MonitorTask bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = null;
		try {
			String token = monitorTaskService.getToken();
			resultMap = new HashMap<String, Object>();
			String result = "fail";
			boolean delResult = monitorTaskService.editIgnoreStatus(bo,token);
			if (delResult) {
				result = "success";
			}
			resultMap.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 设置布控任务状态
	 * @param request
	 * @param session
	 * @param map
	 * @param bo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editTaskStatus")
	public Object editTaskStatus(HttpServletRequest request, HttpSession session, ModelMap map,
								   MonitorTask bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = null;
		try {
			String token = monitorTaskService.getToken();
			resultMap = new HashMap<String, Object>();
			String result = "fail";
			boolean delResult = monitorTaskService.editTaskStatus(bo,token);
			if (delResult) {
				result = "success";
			}
			resultMap.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}