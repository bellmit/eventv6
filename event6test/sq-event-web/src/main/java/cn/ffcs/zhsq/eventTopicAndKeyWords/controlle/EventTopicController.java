package cn.ffcs.zhsq.eventTopicAndKeyWords.controlle;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.eventTopicAndKeyWords.service.IEventTopicService;
import cn.ffcs.zhsq.mybatis.domain.eventTopicAndKeyWords.EventTopic;
import cn.ffcs.zhsq.utils.ConstantValue;

/**   
 * @Description: 热点事件主题表模块控制器
 * @Author: os.wuzhj
 * @Date: 10-15 16:01:04
 * @Copyright: 2019 福富软件
 */ 
@Controller("eventTopicController")
@RequestMapping("/zhsq/eventTopic")
public class EventTopicController {

	@Autowired
	private IEventTopicService eventTopicService; //注入热点事件主题表模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="bizType",required=true)String bizType) {
		
		map.put("bizType", bizType);
		map.put("isRelease", ConstantValue.STATUS_DICTCODE); //发布状态字典编码
		return getTargetPath(bizType,"list");
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="bizType",required=true)String bizType,
			@RequestParam(value="topicName",required=false)String topicName,
			@RequestParam(value="isRelease",required=false)String isRelease,
			@RequestParam(value="page",required=false)int page, 
			@RequestParam(value="rows",required=false)int rows,
			Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		params.put("bizType", bizType);
		params.put("topicName", topicName);
		params.put("isRelease", isRelease);
		return eventTopicService.searchList(page, rows, params);
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/view")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="bizType",required=true)String bizType,
			@RequestParam(value="id",required=false)Long id) {
		EventTopic bo = eventTopicService.searchById(id);
		map.addAttribute("bo", bo);
		return getTargetPath(bizType,"view");
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="bizType",required=true)String bizType,
			@RequestParam(value="id",required=false)Long id) {
		if (id != null) {
			EventTopic bo = eventTopicService.searchById(id);
			map.put("bo", bo);
		}
		return getTargetPath(bizType,"form");
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		EventTopic bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		if (bo.getId_() == null) { //新增
			bo.setInfoOrgCode(userInfo.getOrgCode());
			bo.setCreator(userInfo.getUserId());  //设置创建人
			Long id = eventTopicService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = eventTopicService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		EventTopic bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = eventTopicService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 查询发布的数量
	 */
	@ResponseBody
	@RequestMapping("/findReleaseCount")
	public Integer findReleaseCount(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value="bizType",required=true)String bizType) {
		return eventTopicService.findReleaseCount(bizType);
	}
	
	
	//根据bizType判断是热点主题还是词云
	public String getTargetPath(String bizType,String type) {
		String path = "";
		switch(bizType) {
			case "1":
				path = "/zzgl/eventTopicAndKeyWords/eventTopic/event_topic_"+type+".ftl";
				break;
			case "2":
				path = "/zzgl/eventTopicAndKeyWords/keyWords/key_words_"+type+".ftl";
				break;
		}
		return path;
	}

}