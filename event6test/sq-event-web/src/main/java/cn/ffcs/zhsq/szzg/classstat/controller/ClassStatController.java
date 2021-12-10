package cn.ffcs.zhsq.szzg.classstat.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.classstat.ClassStat;
import cn.ffcs.zhsq.szzg.classstat.IClassStatService;

/**   
 * @Description: 户籍人口模块控制器
 * @Author: linzhu
 * @Date: 08-10 17:20:03
 * @Copyright: 2017 福富软件
 */ 
@Controller
@RequestMapping(value = "/zhsq/szzg/classStat")
public class ClassStatController extends ZZBaseController{

	@Autowired
	private IClassStatService classStatService; //注入户籍人口模块服务
	
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	 // 模块名称
	private final static String SUB_MAIN = "classStat";
	
	/**
	 * 列表页面
	 */
	  @RequestMapping(value = "/report")
	  public String index(HttpSession session, ModelMap map) {
		  Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		    map.put("KEY_DEFAULT_INFO_ORG_CODE", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		    map.put("KEY_DEFAULT_INFO_ORG_NAME", getDefaultInfoOrgName(session));
	    	return REAL_PATH + SUB_MAIN + "/report_" + SUB_MAIN + ".ftl";
	  } 
	  /**
	   * 按区域
	   * @param request
	   * @param response
	   * @param session
	   * @return
	   * @throws Exception
	   */
	  	@ResponseBody
		@RequestMapping("/regionEchart")
		public Map regionEchart(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception {
			List<Map> regionList = new ArrayList<Map>();
			List<String> orgNameList = new ArrayList<String>();
			List<Map> sexList = new ArrayList<Map>();
			Map regionMap = new HashMap();
			int max = 0;
			String orgCode=  request.getParameter("orgCode");
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			if(StringUtils.isEmpty(orgCode)){
				orgCode=infoOrgCode;
			}
			List<ClassStat> list= classStatService.findhjrkRegionListByOrgCode(orgCode);
			for(ClassStat resultList :list){
				Map<String, String> resultMap = new HashMap<String, String>();
				orgNameList.add(resultList.getOrgName().toString());
				resultMap.put("value", resultList.getTotal().toString());
				resultMap.put("code", resultList.getOrgCode().toString());	
				if(max<Integer.parseInt(resultList.getTotal().toString())){
					max = Integer.parseInt(resultList.getTotal().toString());
				}
				regionList.add(resultMap);
			}
			
			List<ClassStat> sexlist= classStatService.findhjrkSexAgeListByOrgCode(orgCode);
			for(ClassStat HJRKList :sexlist){
				Map<String, String> maleMap = new HashMap<String, String>();
				Map<String, String> femaleMap = new HashMap<String, String>();
				maleMap.put("value", HJRKList.getMaleTotal().toString());
				maleMap.put("name", "男");
				femaleMap.put("value",HJRKList.getFemaleTotal().toString());
				femaleMap.put("name", "女");
				sexList.add(maleMap);
				sexList.add(femaleMap);
			}
			regionMap.put("regionList", regionList);
			regionMap.put("orgNameList", orgNameList);
			regionMap.put("sexList", sexList);
			regionMap.put("max", max);			
			return regionMap;
		}
	    /**
	     * 按年龄
	     * @param request
	     * @param response
	     * @param session
	     * @return
	     * @throws Exception
	     */
		//构造echart堆积条形图数据
		@ResponseBody
		@RequestMapping("/ageEchart")
		public Map ageEchart(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception {
			List<String> maleAgeList = new ArrayList<String>();
			List<String> femaleAgeList = new ArrayList<String>();
			Map ageMap = new HashMap();
			String orgCode=  request.getParameter("orgCode");
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			if(StringUtils.isEmpty(orgCode)){
				orgCode=defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();;
			}
			List<ClassStat> list= classStatService.findhjrkSexAgeListByOrgCode(orgCode);
			for(ClassStat ageList :list){
				maleAgeList.add(ageList.getAgeM10().toString());
				maleAgeList.add(ageList.getAgeM20().toString());
				maleAgeList.add(ageList.getAgeM30().toString());
				maleAgeList.add(ageList.getAgeM40().toString());
				maleAgeList.add(ageList.getAgeM50().toString());
				maleAgeList.add(ageList.getAgeM60().toString());
				maleAgeList.add(ageList.getAgeM70().toString());
				maleAgeList.add(ageList.getAgeM80().toString());
//				maleAgeList.add(ageList.getAgeM90().toString());
//				maleAgeList.add(ageList.getAgeM100().toString());

				femaleAgeList.add(ageList.getAgeF10().toString());
				femaleAgeList.add(ageList.getAgeF20().toString());
				femaleAgeList.add(ageList.getAgeF30().toString());
				femaleAgeList.add(ageList.getAgeF40().toString());
				femaleAgeList.add(ageList.getAgeF50().toString());
				femaleAgeList.add(ageList.getAgeF60().toString());
				femaleAgeList.add(ageList.getAgeF70().toString());
				femaleAgeList.add(ageList.getAgeF80().toString());
//				femaleAgeList.add(ageList.getAgeF90().toString());
//				femaleAgeList.add(ageList.getAgeF100().toString());
			}
			ageMap.put("maleAgeList", maleAgeList);
			ageMap.put("femaleAgeList", femaleAgeList);
			return ageMap;
		}
		
		
}