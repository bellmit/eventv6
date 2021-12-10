package cn.ffcs.zhsq.szzg.water.controller;

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
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.wap.util.DateUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.water.ZgWater;
import cn.ffcs.zhsq.szzg.water.service.IZgWaterService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import freemarker.core.ParseException;

/**   
 * @Description: 水质量模块控制器
 * @Author: linzhu
 * @Date: 08-01 17:05:32
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping(value = "/zhsq/szzg/water")
public class ZgWaterController extends ZZBaseController{
	@Autowired
	private IZgWaterService zgWaterService; //注入水质量模块服务
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "water";
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		map.put("ZG_WATER_TYPE", ConstantValue.ZG_WATER_TYPE);//水质类别
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
		String orgCode=request.getParameter("orgCode");
		if(StringUtils.isEmpty(orgCode)){
			orgCode = this.getDefaultInfoOrgCode(session);
		}
		params.put("orgCode", orgCode);
		params.put("szlb", request.getParameter("szlb"));
		params.put("name", request.getParameter("name"));
		EUDGPagination pagination = zgWaterService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		ZgWater bo = zgWaterService.searchById(id);
		map.addAttribute("bo", bo);
		
		return REAL_PATH + SUB_MAIN + "/detail_" + SUB_MAIN + ".ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object add(HttpServletRequest request, HttpSession session, ModelMap map,Long id) {
		ZgWater bo =null;
		if (id != null) {
			bo = zgWaterService.searchById(id);
		}else{
			bo=new ZgWater();
		}
		map.put("bo", bo);
		//map.addAttribute("SQ_ZHSQ_EVENT_URL", "http://gd.fjsq.org/zhsq_event");
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", ConstantValue.MAP_TYPE_ZG_WATER);
		map.put("ZG_WATER_TYPE", ConstantValue.ZG_WATER_TYPE);//水质类别
		return REAL_PATH + SUB_MAIN + "/add_" + SUB_MAIN + ".ftl";
	}
	
	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public ResultObj saveOrUpdate(HttpSession session, ModelMap map,ZgWater zgWater) throws ParseException {
		// 获取当前用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj rs=null;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("seqId", zgWater.getSeqId());
		params.put("name", zgWater.getName());
		params.put("orgCode", zgWater.getOrgCode());
		params.put("startTime", zgWater.getStartTime());
		params.put("endTime", zgWater.getEndTime());
		if (zgWater.getSeqId() != null
				&& zgWater.getSeqId() > 0L) {// 更新
			zgWater.setUpdateUserId(userInfo.getUserId());
			try {
				//判断是否存在记录 不存在则修改
				if(zgWaterService.isExist(params)){
					rs= Msg.EDIT.getResult(false, "已存在对应记录");
				}else{
					rs= Msg.EDIT.getResult(zgWaterService.update(zgWater));
				}
			
			} catch (Exception e) {
				rs= Msg.EDIT.getResult(false, e.getMessage());
			}
		} else {// 保存
				zgWater.setCreateUserId(userInfo.getUserId());
				// 判断是否存在记录不存在则新增
				if(zgWaterService.isExist(params)){
					rs= Msg.ADD.getResult(false, "已存在对应记录");
				}else{
					rs= Msg.ADD.getResult(zgWaterService.insert(zgWater));
				}
		}
		return rs;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		ZgWater bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = zgWaterService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	@RequestMapping("/report")
	public Object report(HttpServletRequest request, HttpSession session, ModelMap model) {
		String orgCode=  request.getParameter("orgCode");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		if(StringUtils.isEmpty(orgCode)){
			orgCode=infoOrgCode;
		}
		model.put("orgCode", orgCode);
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("orgCode", orgCode);
		List<ZgWater> list=zgWaterService.findListByMaxEndTime(orgCode);
//		List<ZgWater> listDay14=zgWaterService.getZgWaterByDay14(orgCode);
		if(list.size()>0){
			ZgWater zgWater=list.get(0);
			if(zgWater.getStartTime()!=null&&zgWater.getEndTime()!=null)
			model.put("dateTime",DateUtils.formatDate(zgWater.getEndTime(),"yyyy年MM月dd日"));
		}
		model.put("list", list);
//		model.put("list", listDay14);
		return REAL_PATH + SUB_MAIN + "/report_" + SUB_MAIN + ".ftl";
	}
}
