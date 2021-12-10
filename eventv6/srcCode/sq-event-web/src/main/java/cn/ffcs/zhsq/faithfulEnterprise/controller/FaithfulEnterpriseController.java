package cn.ffcs.zhsq.faithfulEnterprise.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.faithfulEnterprise.service.FaithfulEnterpriseService;
import cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise.FaithfulEnterprise;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * Created by 张天慈 on 2017/12/20.
 */
@Controller("faithfulEnterpriseController")
@RequestMapping("/zhsq/szzg/zgFaithfulEnterprise")
public class FaithfulEnterpriseController extends ZZBaseController {

	//注入企业信息服务模块
	@Autowired
	private FaithfulEnterpriseService enterpriseService;

	/**
	 * 数据列表页面
	 */
	@RequestMapping(value = "/index")
	public Object index(HttpSession session, ModelMap map, HttpServletRequest request){

		//获取当前登录用户的信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		//获取默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));

		//BD工程域的域名
		map.addAttribute("IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));
		map.put("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));

		return "/szzg/faithfulEnterprise/list_enterprise.ftl";
	}

	/**
	 * 分页显示数据
	 */
	@ResponseBody
	@RequestMapping(value = "/listData",method = RequestMethod.POST)
	public Object listData(HttpSession session, FaithfulEnterprise enterprise, ModelMap map, int page, int rows){

		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;

		Map<String,Object> params = new HashedMap();
		params.put("gridCode",enterprise.getGridCode());
		params.put("enterpriseName",enterprise.getEnterpriseName());
		params.put("registrationId",enterprise.getRegistrationId());
		params.put("evaluationTime",enterprise.getEvaluationTime());
		params.put("evaluationLevel",enterprise.getEvaluationLevel());

		EUDGPagination pagination = enterpriseService.searchList(page,rows,params);

		return pagination;

	}

	/**
	 * 新增企业信息
	 */
	@RequestMapping("add")
	public Object addTrademark(HttpSession session,ModelMap map){

		FaithfulEnterprise enterprise = new FaithfulEnterprise();


		//获取默认网格信息
		Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
		enterprise.setGridId((Long)defaultGridInfo.get(KEY_START_GRID_ID));
		enterprise.setGridCode((String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		enterprise.setGridName((String)defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("enterprise",enterprise);
		//标注地理位置
		map.addAttribute("SQ_ZHSQ_EVENT_URL",App.EVENT.getDomain(session)); //Event项目路径
		map.addAttribute("module",ConstantValue.FAITHFUL_ENTERPRISE_MARKER_TYPE); //模块，自定义常量
		map.addAttribute("markerOperation",ConstantValue.ADD_MARKER); //添加地图标注


		return "/szzg/faithfulEnterprise/add_enterprise.ftl";
	}

	/**
	 * 修改企业信息
	 */
	@RequestMapping("/edit")
	public Object update(ModelMap map,HttpSession session,Long id){
		FaithfulEnterprise enterprise = enterpriseService.findById(id);
		map.addAttribute("enterprise",enterprise);


		//标注地理位置
		map.addAttribute("SQ_ZHSQ_EVENT_URL",App.EVENT.getDomain(session)); //Event项目路径
		map.addAttribute("module",ConstantValue.FAITHFUL_ENTERPRISE_MARKER_TYPE); //模块，自定义常量
		map.addAttribute("markerOperation",ConstantValue.EDIT_MARKER); //添加标注操作
		return "/szzg/faithfulEnterprise/add_enterprise.ftl";
	}

	/**
	 * 保存新增或修改的企业信息
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object saveOrUpdate(HttpServletRequest request,HttpSession session,FaithfulEnterprise enterprise){

		//数据是否保存成功，success OR fail
		String result = "fail";
		Map<String,Object> resultMap = new HashMap();

		//时间格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//认定时间
		String evaluationTime = request.getParameter("evaluationTimeStr");
		Date evaluationDate = null;
		if(evaluationTime != ""){
			try {
				evaluationDate = sdf.parse(evaluationTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}


		//获取商标信息
		enterprise.setEnterpriseName(request.getParameter("enterpriseName"));
		enterprise.setEvaluationTime(evaluationDate);
		enterprise.setRegistrationId(request.getParameter("registrationId"));
		enterprise.setEvaluationLevel(request.getParameter("evaluationLevel"));
		enterprise.setEnterpriseAddress(request.getParameter("enterpriseAddress"));
		enterprise.setLatitude(request.getParameter("latitude"));
		enterprise.setLongitude(request.getParameter("longitude"));
		enterprise.setRegionCode(request.getParameter("gridCode"));

		//判断商标信息是否存在
		if(enterprise.getEnterpriseId() == null){
			//商标信息不存在，新增商标信息
			Boolean flag = enterpriseService.insert(session,enterprise);
			if(flag == true){
				result = "success";
			}
		}else{//存在时，修改设备信息
			Boolean flag = enterpriseService.update(session,enterprise);
			if(flag == true){
				result = "success";
			}
		}
		resultMap.put("result",result);
		return result;
	}

	/**
	 * 删除商标信息
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(FaithfulEnterprise enterprise,HttpSession session){
		Map<String,Object> resultMap = new HashMap();
		String result = "fail";

		//获取登录用户的信息，时间
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		enterprise.setUpdater(userInfo.getUserId()); //删除人Id
		enterprise.setUpdaterName(userInfo.getUserName()); //删除人姓名

		boolean flag = enterpriseService.delete(enterprise);
		if(flag == true){
			result = "success";
		}
		resultMap.put("result",result);
		return resultMap;
	}

	/**
	 * 商标详情页面
	 */
	@RequestMapping("/detail")
	public Object detail(HttpSession session,Long id,ModelMap map,String showClose){

		//event调用，隐藏关闭按钮
		map.addAttribute("showClose",showClose);

		if( id != null){
			FaithfulEnterprise enterprise = enterpriseService.findById(id);
			map.addAttribute("enterprise",enterprise);
		}

		return "/szzg/faithfulEnterprise/detail_enterprise.ftl";
	}



}
