package cn.ffcs.zhsq.dispute.controller;

import cn.ffcs.file.mybatis.domain.attachment.Attachment;
import cn.ffcs.file.service.IAttachmentService;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationDataService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.ParamUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/zhsq/disputeData")
public class DisputeMediationDataController {
    @Autowired
    private IDisputeMediationDataService disputeMediationDataService;
    @Autowired
    private IDisputeMediationService disputeMediationService;
    @Autowired
    private IInvolvedPeopleService involvedPeopleService;
    @Autowired
    private IBaseDictionaryService dictionaryService;
    @Autowired
    private IAttachmentService attachmentService;
    /**
     * 获取突出矛盾纠纷数量
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOutStandingNum")
    public Object getOutStandingNum(HttpServletRequest request,String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        Map<String, Object> outStandingNum = disputeMediationDataService.getOutStandingNum(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(outStandingNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(outStandingNum);
        }
        return jsonpcallback;
    }

    /**
     * 突出矛盾纠纷案件分布
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOutStandingDistribution")
    public Object getOutStandingDistribution(HttpServletRequest request,String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        List<Map<String, Object>> outStandingDistribution = disputeMediationDataService.getOutStandingDistribution(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(outStandingDistribution) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(outStandingDistribution);
        }
        return jsonpcallback;
    }

    /**
     * 矛盾纠纷案件化解前五
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDataForResolutionRateNext")
    public Object getDataForResolutionRateNext(HttpServletRequest request,String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        List<Map<String, Object>> dataForResolutionRateNext = disputeMediationDataService.getDataForResolutionRateNext(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(dataForResolutionRateNext) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(dataForResolutionRateNext);
        }
        return jsonpcallback;
    }

    /**
     * 获取矛盾纠纷当月采集数 调解数，当年采集数 调解数
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNowMonthNumAndNowYearNum")
    public Object getNowMonthNumAndNowYearNum(HttpServletRequest request,String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        Map<String, Object> nowMonthNumAndNowYearNum = disputeMediationDataService.getNowMonthNumAndNowYearNum(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(nowMonthNumAndNowYearNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(nowMonthNumAndNowYearNum);
        }
        return jsonpcallback;
    }

    /**
     * 年度同比环比
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findTwoYearByMap")
    public Object findTwoYearByMap(HttpServletRequest request,String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        Map<String, Object> twoYearByMap = disputeMediationDataService.findTwoYearByMap(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(twoYearByMap) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(twoYearByMap);
        }
        return jsonpcallback;
    }

    /**
     * 矛盾纠纷类型
     * @param request
     * @param orgCode
     * @param monthYear
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getConfictTopListByOrgCode")
    public Object getConfictTopListByOrgCode(HttpServletRequest request, String orgCode, String monthYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("monthYear",monthYear);
        params.put("statDateType", ParamUtils.getString(request,"statDateType","M"));
        List<Map<String, Object>> confictTopListByOrgCode = disputeMediationDataService.getConfictTopListByOrgCode(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(confictTopListByOrgCode) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(confictTopListByOrgCode);
        }
        return jsonpcallback;
    }

    /**
     * 获取矛盾纠纷数量
     * @param orgCode 必传 不传直接报错
     * @return TOTAL 总数， END_TOTAL 处置数，DEAL_TOTAL 处置中，处置率 DEAL_RATE
     */
    @ResponseBody
    @RequestMapping(value = "/getDisputeNum")
    public Object getDisputeNum(HttpServletRequest request, String orgCode, String nowYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        params.put("nowYear",nowYear);
        Map<String, Object> disputeNum = disputeMediationDataService.getDisputeNum(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(disputeNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(disputeNum);
        }
        return jsonpcallback;
    }
    /**
     * 获取同比环比
     * @param orgCode 必传 不传直接报错
     * @return twoYear 同比环比（YEAR_ 年份，MONTH_ 月份，TOTAL_ 总数，END_TOTAL 处置数） ； nowYear 办理趋势（YEAR_ 年份，MONTH_ 月份，TOTAL_ 总数，END_TOTAL 处置数）
     */
    @ResponseBody
    @RequestMapping(value = "/getTwoYearMoM")
    public Object getTwoYearMoM(HttpServletRequest request, String orgCode, String nowYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        Map<String, Object> disputeNum = disputeMediationDataService.getTwoYearMoM(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(disputeNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(disputeNum);
        }
        return jsonpcallback;
    }
    /**
     * 获取矛盾纠纷类型
     * @param orgCode
     * @return DICT_NAME 字典名称， TOTAL_ 对应字典数量
     */
    @ResponseBody
    @RequestMapping(value = "/getDisputeTypeNum")
    public Object getDisputeTypeNum(HttpServletRequest request, String orgCode, String nowYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        List<Map<String, Object>> disputeNum = disputeMediationDataService.getDisputeTypeNum(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(disputeNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(disputeNum);
        }
        return jsonpcallback;
    }
    /**
     * 获取事件分布排行
     * @param orgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDisputeDistribution")
    public Object getDisputeDistribution(HttpServletRequest request, String orgCode, String nowYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        List<Map<String, Object>> disputeNum = disputeMediationDataService.getDisputeDistribution(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(disputeNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(disputeNum);
        }
        return jsonpcallback;
    }
    /**
     * 获取各地区纠纷类型TOP1
     * @param orgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRegionsTypeTop1")
    public Object getRegionsTypeTop1(HttpServletRequest request, String orgCode, String nowYear){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        params.put("orgCode",orgCode);
        List<Map<String, Object>> disputeNum = disputeMediationDataService.getRegionsTypeTop1(params);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(disputeNum) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(disputeNum);
        }
        return jsonpcallback;
    }

    /**
     * 获取各地区纠纷类型TOP1
     * @param orgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public Object getList(HttpServletRequest request, String orgCode, String disputeType, String disputeEventName){
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        Map<String,Object> params = new HashMap<>();
        Integer integer = Integer.valueOf(disputeType);
        if(integer<10){
            disputeType = disputeType+" ";
        }
        params.put("orgCode",orgCode);
        params.put("disputeType",disputeType);
        params.put("disputeEventName",disputeEventName);
        EUDGPagination eudgPagination = disputeMediationDataService.searchList(params, 1, 20);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(eudgPagination) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(eudgPagination);
        }
        return jsonpcallback;
    }

    /**
     * 获取详情
     * @param request
     * @param mediationId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getDisputeInfo")
    public Object getDisputeInfo(HttpServletRequest request,Long mediationId){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> result = new HashMap<>();
        String jsonpcallback = ParamUtils.getString(request,"jsonpcallback");
        DisputeMediation disputeMediation = disputeMediationService.selectByPrimaryKey(mediationId);
        if(StringUtils.isNotBlank(disputeMediation.getDisputeScale())){//事件规模
            disputeMediation.setDisputeScaleStr(dictionaryService.changeCodeToName(ConstantValue.DISPUTE_SCALE_CODE,disputeMediation.getDisputeScale(),userInfo.getOrgCode()));
        }
        if(StringUtils.isNotBlank(disputeMediation.getDisputeType3())){
            disputeMediation.setDisputeTypeStr(dictionaryService.changeCodeToName("DM01588",disputeMediation.getDisputeType3().trim(),""));
        }
        List<InvolvedPeople> involvedPeopleListByBiz = involvedPeopleService.findInvolvedPeopleListByBiz(mediationId, InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
        List<BaseDataDict> peopleTypeDictList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.PEOPLETYPE_PCODE, userInfo.getOrgCode());
        involvedPeopleListByBiz.forEach(people->{
            //人员类别字典转换
            for(BaseDataDict baseDataDict : peopleTypeDictList){
                List<BaseDataDict> peopleTypeDictListSub = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), userInfo.getOrgCode());
                try {
                    DataDictHelper.setDictValueForField(people, "peopleType", "peopleTypeName", peopleTypeDictListSub);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        List<Attachment> attList = attachmentService.findByBizId(mediationId, ConstantValue.DISPUTE_ATTACHMENT_TYPE);
        result.put("bo",disputeMediation);
        result.put("peoList",involvedPeopleListByBiz);
        result.put("attList",attList);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(result) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(result);
        }
        return jsonpcallback;
    }
}
