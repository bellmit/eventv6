package cn.ffcs.zhsq.szzg.education.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.education.EducationBO;
import cn.ffcs.zhsq.szzg.education.service.IEducationService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import cn.ffcs.system.publicUtil.EUDGPagination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 文化程度分析控制器
 *
 * @author syhenian
 */
@Controller
@RequestMapping(value = "/zhsq/szzg/education")
public class EducationController extends ZZBaseController {


    @Autowired
    private IEducationService educationService;

    @Autowired
    private OrgEntityInfoOutService orgEntityInfoOutService;

    @Autowired
    private IBaseDictionaryService dictionaryService;    // 模块路径
    private final static String REAL_PATH = "/szzg/";
    // 模块名称
    private final static String SUB_MAIN = "education";



    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");




    /**
     * 获取文化程度类型
     * @return
     */
    @RequestMapping(value="/getEducationDegree")
    @ResponseBody
    public List<BaseDataDict> getEducationDegree(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        List<BaseDataDict> culture = dictionaryService.getDataDictListOfSinglestage("D060001", userInfo.getOrgCode());
        return culture;
    }

    /**
     * 前端首页
     *
     * @return
     */
    @RequestMapping(value = "/showIndex")
    public String index(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {

        List<BaseDataDict> baseDataDicts= getEducationDegree(session);
        model.addAttribute("yearStr", Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("education",baseDataDicts);
        return REAL_PATH + SUB_MAIN + "/education_level.ftl";
    }

    /**
     * 报表
     * @param type 类型
     * @param yearStr 年份
     */
    @ResponseBody
    @RequestMapping(value = "/findChartByParams")
    public Map<String,Object> findChartByParams(HttpSession session, ModelMap map,
                                                @RequestParam(value = "yearStr", required = false) String yearStr,
                                                @RequestParam(value = "type", required = false) String type,
                                                @RequestParam(value = "module", required = false) String module) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("type", type);
        param.put("yearStr", yearStr);
        if("education".equals(module)){//人口基本
            param.put("orgCode", userInfo.getOrgCode());
            param.put("list", educationService.findChartByEducation(param));
        }
        return param;
    }
    /**
     * 获取图表数据
     * @param type
     * @return
     */
    @RequestMapping(value="/getEducationCharts")
    @ResponseBody
    public List<Map<String, Object>> getEducationCharts(HttpSession session,
            @RequestParam(value="type")String type, @RequestParam(value="yearStr")String yearStr){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("yearStr", yearStr);
        params.put("orgCode", userInfo.getOrgCode());
        return educationService.getEducationCharts(params);
    }

    /**
     * 首页
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(HttpSession session, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String orgCode = userInfo.getOrgCode();
        String orgName = userInfo.getOrgName();
        Long orgId = userInfo.getOrgId();



        Map<String, Object> params = new HashMap<String, Object>();
        List<BaseDataDict> culture = dictionaryService.getDataDictListOfSinglestage("A001051004", userInfo.getOrgCode());
        map.put("type", culture);
        Calendar a=Calendar.getInstance();
        int year=a.get(Calendar.YEAR);
        String yearStr=String.valueOf(year);
        map.addAttribute("yearStr", yearStr);
        map.put("orgCode", orgCode);
        map.put("orgName", orgName);
        map.put("orgId", orgId);
        return "/szzg/education/index_education.ftl";
    }

    /**
     * 分页查询
     *
     * @param education
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/listData")
    @ResponseBody
    public EUDGPagination listData(EducationBO education,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "rows") int rows) {

        if (page <= 0)
            page = 1;


        Map<String, Object> params = new HashMap<String, Object>();
        if (education.getOrgCode() != null) {
            params.put("orgCode", education.getOrgCode());
        }
        if (education.getType() != null) {
            params.put("type", education.getType());
        }

        if (education.getYearStr() != null) {
            params.put("yearStr", education.getYearStr());
        }
        EUDGPagination eudgPagination = educationService.findPageListByCriteria(params, page, rows);
        return eudgPagination;
    }


    /**
     * 新增页面
     */
    @RequestMapping("/add")
    public String create(HttpSession session, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String orgCode = userInfo.getInfoOrgCodeStr();
        String orgName = userInfo.getOrgName();
        Long orgId = userInfo.getOrgId();
        Map<String, Object> params = new HashMap<String, Object>();
        List<BaseDataDict> culture = dictionaryService.getDataDictListOfSinglestage("A001051004", userInfo.getOrgCode());
        map.put("type", culture);
        Calendar a=Calendar.getInstance();

        int year=a.get(Calendar.YEAR);
        String yearStr=String.valueOf(year);
        map.addAttribute("yearStr", yearStr);
        map.put("orgCode", orgCode);

        map.put("orgName", orgName);
        map.put("orgId", orgId);
        return "/szzg/education/add_education.ftl";
    }


    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit(HttpSession session, ModelMap map, @RequestParam(value = "seqid") Long seqid) {

        EducationBO educationBO = educationService.findById(seqid);
        if (educationBO != null) {
            map.addAttribute("educationBO", educationBO);
        }


        map.addAttribute("orgCode", getDefaultGridInfo(session).get(KEY_DEFAULT_INFO_ORG_CODE)); //获取默认网格信息
        //数据字典
        map.addAttribute("type", "A001051004");

        return "/szzg/education/edit_education.ftl";
    }


    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate")
    public Map<String, Object> save(HttpSession session, ModelMap map,
                                    @ModelAttribute(value = "educationBO") EducationBO educationBO) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        Long seqid = educationBO.getSeqid();

        educationBO.setStatus("1");
        educationBO.setOrgCode(educationBO.getOrgCode().trim());
        educationBO.setType(educationBO.getType().trim());

        Map<String, Object> params = new HashMap<String, Object>();
        if (educationBO.getOrgCode() != null) {
            params.put("orgCode", educationBO.getOrgCode());
        }
        if (educationBO.getType() != null) {
            params.put("type", educationBO.getType());
        }

        if (educationBO.getYearStr() != null) {
            params.put("yearStr", educationBO.getYearStr());
        }
        List<EducationBO> educationBOS = educationService.getEducationByParams(params);


        if (seqid != null && seqid > 0L) { // --更新

                educationBO.setUpdateTime(new Date());
                result = educationService.update(educationBO);
                resultMap.put("msg", Msg.EDIT.getMsg(result));


        } else { // --新增
            if (educationBOS.size() > 0) {
                resultMap.put("msg", "一个区域同一年份只允许一条数据");

            } else {
                educationBO.setCreateTime(new Date());
                result = educationService.save(educationBO);
                result = educationBO.getSeqid() > 0;
                resultMap.put("msg", Msg.ADD.getMsg(result));
            }
        }

        resultMap.put("result", result);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(HttpSession session, HttpServletRequest request,
                                      @RequestParam(value = "seqid") Long seqid) {
        boolean isSuccess = educationService.delete(seqid);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (isSuccess) {
            resultMap.put("result", 1);
        } else {
            resultMap.put("result", 0);
        }

        resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
        return resultMap;
    }
    
    
    /**
	 * 人文素质跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showHumanityCultural")
	public String showHumanityCultural(HttpServletRequest request,HttpSession session, ModelMap map,String type) {
		map.addAttribute("currentDate", Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1));
		if(null!=type){
			
			if(type.equals("priSchoolEdu")){
				return "/szzg/education/report_pri_education.ftl";
			}else{
				return "/szzg/statistics/index_statistics.ftl";
			}
		}else{
			return "/szzg/statistics/index_statistics.ftl";
		}
	}
	
	/**
	 * 小学就读跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toPriEdu")
	public String toPriEdu(HttpServletRequest request, HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String orgCode = userInfo.getOrgCode();
		String orgName = userInfo.getOrgName();
		Long orgId = userInfo.getOrgId();

		
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		String yearStr = String.valueOf(year);
		map.addAttribute("yearStr", yearStr);
		map.put("orgCode", orgCode);
		map.put("orgName", orgName);
		map.put("orgId", orgId);

		return "/szzg/education/index_pri_education.ftl";

	}
	
	
	/**
     * 分页查询
     *
     * @param education
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/listDataPriEdu")
    @ResponseBody
    public EUDGPagination listDataPriEdu(String orgCode,String yearStr,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "rows") int rows) {

        if (page <= 0)
            page = 1;


        Map<String, Object> params = new HashMap<String, Object>();
        if (orgCode != null) {
            params.put("orgCode", orgCode);
        }
        
        if (yearStr != null) {
            params.put("yearStr", yearStr);
        }
        EUDGPagination eudgPagination = educationService.findPageListByCriteriaPriSchool(params, page, rows);
        return eudgPagination;
    }
    
    
    /**
     * 编辑页面
     */
    @RequestMapping("/editPriEdu")
    public String editPriEdu(HttpSession session, ModelMap map, @RequestParam(value = "seqid") Long seqid) {

        Map<String, Object> findPriEduById = educationService.findPriEduById(seqid);
        if (findPriEduById != null) {
            map.addAttribute("priEdu", findPriEduById);
        }

        return "/szzg/education/edit_pri_education.ftl";
    }
    
    @ResponseBody
    @RequestMapping(value = "/updatePriEdu")
    public Map<String, Object> updatePriEdu(HttpSession session, ModelMap map,
                                    Long seqid,Long newage,Long newread) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;

        Map<String, Object> params = new HashMap<String, Object>();
        if (seqid != null) {
            params.put("seqid", seqid);
        }
        if (newage != null) {
            params.put("newage", newage);
        }

        if (newread != null) {
            params.put("newread", newread);
        }
       
        params.put("updateId", userInfo.getUserId());
        
        
        result = educationService.updatePriEdu(params);
        

        resultMap.put("result", result);
        return resultMap;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/deletePriEdu", method = RequestMethod.POST)
    public Map<String, Object> deletePriEdu(HttpSession session, HttpServletRequest request,
                                      @RequestParam(value = "seqid") Long seqid) {
        boolean isSuccess = educationService.deletePriEdu(seqid);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (isSuccess) {
            resultMap.put("result", 1);
        } else {
            resultMap.put("result", 0);
        }

        resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
        return resultMap;
    }
    
    
    /**
     * 新增页面
     */
    @RequestMapping("/addPriEdu")
    public String addPriEdu(HttpSession session, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String orgCode = userInfo.getInfoOrgCodeStr();
        String orgName = userInfo.getOrgName();
        Long orgId = userInfo.getOrgId();

        Calendar a=Calendar.getInstance();
        int year=a.get(Calendar.YEAR);
        String yearStr=String.valueOf(year);
        map.addAttribute("yearStr", yearStr);
        map.put("orgCode", orgCode);

        map.put("orgName", orgName);
        map.put("orgId", orgId);
        return "/szzg/education/add_pri_education.ftl";
    }
    
    

}
