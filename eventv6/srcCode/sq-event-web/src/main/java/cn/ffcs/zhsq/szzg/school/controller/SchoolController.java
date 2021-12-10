package cn.ffcs.zhsq.szzg.school.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolBo;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import cn.ffcs.zhsq.szzg.school.service.SchoolService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;

@Controller
@RequestMapping(value = "/zhsq/szzg/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

/**
 * 
 */
	@Autowired
	private ZgResourceInfoService infoservice;
    @Autowired
    private IBaseDictionaryService dictionaryService;

    @Autowired
    private IResMarkerService resMarkerService;

    @Autowired
    private OrgEntityInfoOutService orgEntityInfoOutService;
    // 模块路径
    private final static String REAL_PATH = "/szzg/";
    // 模块名称
    private final static String SUB_MAIN = "school";


    /**
     * 前端首页
     *
     * @return
     */
    @RequestMapping(value = "/showIndex")
    public String index(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S006001", userInfo.getOrgCode());
        model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("school", list);

        return REAL_PATH + SUB_MAIN + "/xxfb.ftl";
    }


    @RequestMapping(value = "index")
    public String index(HttpSession session, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "S006001");
        List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S006001", userInfo.getOrgCode());
        map.put("orgCode", userInfo.getOrgCode());
        map.put("orgName", userInfo.getOrgName());
        map.put("orgId", userInfo.getOrgId());
        map.addAttribute("type", list);

        return "/szzg/school/index_school.ftl";
    }

    @RequestMapping(value = "listData")
    @ResponseBody
    public EUDGPagination listData(@RequestParam(value = "page") int page,
                                   @RequestParam(value = "rows") int rows,
                                   SchoolBo schoolBo) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (schoolBo.getAddress() != null) {
            params.put("address", schoolBo.getAddress());
        }
        if (schoolBo.getSchoolName() != null) {
            params.put("schoolName", schoolBo.getSchoolName());
        }
        if (schoolBo.getType() != null) {
            params.put("type", schoolBo.getType());
        }
        if (schoolBo.getOrgCode() != null) {
            params.put("orgCode", schoolBo.getOrgCode());
        }
        EUDGPagination eudgPagination = schoolService.findPageListByCriteria(params, page, rows);
        return eudgPagination;
    }

    @RequestMapping(value = "detail")
    public String detail(HttpSession session, ModelMap modelMap, @RequestParam(value = "seqid") Long seqid) {


        SchoolBo school = schoolService.findSchoolById(seqid);
        modelMap.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
        modelMap.addAttribute("id", seqid);
        modelMap.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 添加标注操作
        modelMap.addAttribute("school", school);
        return "/szzg/school/detail_school.ftl";
    }

    @RequestMapping(value = "/add")
    public String add(HttpSession session, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> params = new HashMap<String, Object>();
        map.addAttribute("orgCode", userInfo.getOrgCode());
        params.put("codetype", "S006001");
        List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S006001", userInfo.getOrgCode());
        map.put("type", list);

        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

        map.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 添加标注操作

        return "/szzg/school/add_school.ftl";
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpSession session, @RequestParam(value = "seqid") String seqid,
                       ModelMap map) {
        Long id = Long.parseLong(seqid);

        SchoolBo schoolBo = schoolService.findSchoolById(id);


        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> params = new HashMap<String, Object>();
        map.addAttribute("orgCode", userInfo.getOrgCode());
        params.put("codetype", "S006001");
        List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S006001", userInfo.getOrgCode());
        map.put("type", list);

        ResMarker resMarker = new ResMarker();
        if (schoolBo.getX() != null && schoolBo.getX() != "") {
            resMarker.setX(schoolBo.getX());
        }


        if (schoolBo.getY() != null && schoolBo.getY() != "") {
            resMarker.setY(schoolBo.getY());
        }

        resMarker.setMarkerId(id);
        resMarker.setMarkerType("SCHOOL_MARK");
        schoolBo.setResMarker(resMarker);
        map.addAttribute("SchoolBo", schoolBo);
        map.addAttribute("id", id);
        map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

        return "/szzg/school/edit_school.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate")
    public Map<String, Object> save(HttpSession session, ModelMap map,
                                    @ModelAttribute(value = "schoolBo") SchoolBo schoolBo, String module) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (module != null) {
            schoolBo.getResMarker().setMarkerType(module);
        }
        schoolBo.getResMarker().setCatalog("03");

        if (schoolBo.getResMarker().getX() != null) {
            schoolBo.setX(schoolBo.getResMarker().getX());
        }

        if (schoolBo.getResMarker().getY() != null) {
            schoolBo.setY(schoolBo.getResMarker().getY());
        }

        boolean result = false;
        Long seqid = schoolBo.getSeqid();
        schoolBo.setStatus("1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (seqid != null && seqid > 0L) { // --更新
        	schoolBo.getResMarker().setResourcesId(seqid);
            result = schoolService.save(schoolBo);
            if(result == false) {
            	resultMap.put("msg", "已存在学校,请重新输入！");
            }else {
            	resMarkerService.saveOrUpdateResMarker(schoolBo.getResMarker());
            	resultMap.put("msg", Msg.EDIT.getMsg(result));
            }
        } else { // --新增
            result = schoolService.insert(schoolBo);
            if(result == false) {
            	resultMap.put("msg", "已存在学校,请重新输入！");
            }else {
            	schoolBo.getResMarker().setResourcesId(schoolBo.getSeqid());
                resMarkerService.saveOrUpdateResMarker(schoolBo.getResMarker());
                resultMap.put("msg", Msg.ADD.getMsg(result));
            }
        }

        resultMap.put("result", result);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> del(HttpSession session, HttpServletRequest request,
                                   @RequestParam(value = "seqid") Long seqid) {
        boolean isSuccess = schoolService.delete(seqid);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("resTableId", "61");
        params = infoservice.delete(params);
        System.out.println(params.toString());
        params.clear();
        params.put("resTableId", "61,x");
        params = infoservice.delete(params);
        System.out.println(params.toString());
        params.clear();
        params.put("resTableIds", "61,x");
        params.put("resTypeCode", "02070303");
        params.put("updateUserId", "110");
        params = infoservice.delete(params);
        System.out.println(params.toString());
        params.clear();
        params.put("resTableId", "61");
        params.put("resTypeCode", "02070303");
        params.put("updateUserId", "110");
        params = infoservice.delete(params);
        System.out.println(params.toString());
        params.clear();
        params.put("resTableIds", "62,63,64");
        params.put("resTypeCode", "02070303");
        params.put("updateUserId", "110");
        params = infoservice.delete(params);
        System.out.println(params.toString());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (isSuccess) {
            resultMap.put("result", 1);
        } else {
            resultMap.put("result", 0);
        }

        resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
        return resultMap;
    }

    @RequestMapping(value = "findDataByChart")
    @ResponseBody
    public Map<String, Object> findDataByChart(HttpSession session, HttpServletRequest request, String year) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        String orgCode = userInfo.getInfoOrgCodeStr();
        List<String> stringList = new ArrayList<String>();
        List<OrgEntityInfoBO> list = orgEntityInfoOutService.findOrgListbyOrgCode(orgCode);
        stringList.add(orgCode);
        for (OrgEntityInfoBO orgEntityInfoBO : list) {
            orgCode=orgEntityInfoBO.getOrgCode();
            stringList.add(orgCode);
        }
        params.put("orgCode", StringUtils.strip(stringList.toString(),"[]").trim());
        params.put("year",year);
        Map<String, Object> mapList = schoolService.getSchoolCharts(params);
        resultMap.put("dataList", mapList);
        return resultMap;
    }

    @RequestMapping(value = "searchData")
    @ResponseBody
    public Map<String,Object> searchPointData(HttpSession session, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        params.put("inOrgCode", userInfo.getInfoOrgCodeStr());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("point",schoolService.findSchoolMark(params));
        return resultMap;
    }

}
