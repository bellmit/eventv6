package cn.ffcs.zhsq.szzg.school.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolStat;
import cn.ffcs.zhsq.szzg.school.service.ISchoolStatService;

/**   
 * @Description: 学校人数统计表模块控制器
 * @Author: os.wuzhj
 * @Date: 04-04 08:43:01
 * @Copyright: 2019 福富软件
 */ 
@Controller("schoolStatController")
@RequestMapping("/zhsq/szzg/schoolStat")
public class SchoolStatController {

	@Autowired
	private ISchoolStatService schoolStatService; //注入学校人数统计表模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  
		map.put("orgCode", userInfo.getOrgCode());
		return "/szzg/schoolStat/school_stat_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows,@RequestParam(value = "schoolName",required=false) String schoolName,
		@RequestParam(value = "orgCode",required=false) String orgCode,
		@RequestParam(value = "statYear",required=false) String statYear) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(orgCode)) {
			UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
			orgCode = userInfo.getOrgCode();
		}
		params.put("orgCode", orgCode);
		params.put("statYear", statYear);
		params.put("schoolName", schoolName);
		
		EUDGPagination pagination = schoolStatService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		SchoolStat bo = schoolStatService.searchById(id);
		map.addAttribute("bo", bo);
		return "/szzg/schoolStat/school_stat_view.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  
		map.put("orgCode", userInfo.getOrgCode());
		SchoolStat bo =new SchoolStat();
		if (id != null) {
			bo = schoolStatService.searchById(id);
		}else {
			bo.setStatYear(Calendar.getInstance().get(Calendar.YEAR));
		}
		map.put("bo", bo);
		return "/szzg/schoolStat/school_stat_form.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		SchoolStat bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");  //替换成本地常量
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String msg = "";
		if (bo.getStatId() == null) { //新增
			bo.setCreateUser(userInfo.getUserId());  //设置创建人
			bo.setUpdateUser(userInfo.getUserId());  //设置更新人
			Long id = schoolStatService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
				msg= "保存成功！";
			}else{
				msg= "已存在同年份数据,请重新输入！";
			};
		} else { //修改
			bo.setUpdateUser(userInfo.getUserId());  //设置更新人
			boolean updateResult = schoolStatService.update(bo);
			if (updateResult) {
				result = "success";
				msg= "修改成功！";
			}else {
				msg= "已存在同年份数据,请重新输入！";
			}
		}
		resultMap.put("result", result);
		resultMap.put("msg", msg);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		SchoolStat bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION"); //替换成本地常量
		bo.setUpdateUser(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = schoolStatService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 获取学校
	 */
	@ResponseBody
	@RequestMapping("/getSchool")
	public Object getSchool(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		return null;
	}

}