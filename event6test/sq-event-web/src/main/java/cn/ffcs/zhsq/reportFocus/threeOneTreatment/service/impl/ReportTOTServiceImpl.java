package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.threeOneTreatment.ReportTOTMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/8/16 10:26
 */
@Service("reportTOTService")
public class ReportTOTServiceImpl extends ReportTwoViolationPreServiceImpl {
    @Autowired
    private ReportTOTMapper reportTOTMapper;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    @Autowired
    private IHolidayInfoService holidayInfoService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取列表方法映射
     * @return
     */
    @Override
    @SuppressWarnings("serial")
    protected Map<Integer, String> capMethodMap() {
        return new HashMap<Integer, String>() {
            {
                put(1, "4Draft");
                put(2, "4Todo");
                put(3, "4Handled");
                put(4, "4Initiator");
                put(5, "4Jurisdiction");
                put(50,"4JurisdictionSimplify");
                put(51,"4JurisdictionWithMarker");
                put(52,"4JurisdictionWithRegionPath");
                put(6, "4Archived");
                put(7, "4MsgReading");
                put(8, "4StatusTrack");
            }
        };
    }

    @Override
    public String saveReportExtendInfo(Map<String, Object> totParam, UserInfo userInfo) {
        if(totParam == null || totParam.isEmpty()) {
            throw new IllegalArgumentException("缺少需要操作的整治信息！");
        }
        if(userInfo == null) {
            throw new IllegalArgumentException("缺少操作用户信息！");
        }
        Long userId = userInfo.getUserId();
        String totUUID = null;

        if(CommonFunctions.isBlank(totParam, "creator")) {
            totParam.put("creator", userId);
        }
        if(CommonFunctions.isBlank(totParam, "updator")) {
            totParam.put("updator", userId);
        }
        if(CommonFunctions.isNotBlank(totParam, "feedbackTime")) {
            Object feedbackTimeObj = totParam.get("feedbackTime");
            Date feedbackTime = null;

            if(feedbackTimeObj instanceof String) {
                try {
                    feedbackTime = DateUtils.convertStringToDate(feedbackTimeObj.toString(), DateUtils.PATTERN_24TIME);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if(feedbackTimeObj instanceof Date) {
                feedbackTime = (Date) feedbackTimeObj;
            }

            totParam.put("feedbackTime", feedbackTime);
        } else if(CommonFunctions.isBlank(totParam, "feedbackTime") && CommonFunctions.isNotBlank(totParam, "feedbackTimeStr")) {
            Date feedbackTime = null;
            String feedbackTimeStr = totParam.get("feedbackTimeStr").toString();

            try {
                feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(feedbackTime != null) {
                totParam.put("feedbackTime", feedbackTime);
            }
        }

        if(CommonFunctions.isNotBlank(totParam, "extensionDate")) {
            Object extensionDateObj = totParam.get("extensionDate");
            Date extensionDate = null;

            if(extensionDateObj instanceof String) {
                try {
                    extensionDate = DateUtils.convertStringToDate(extensionDateObj.toString(), DateUtils.PATTERN_DATE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if(extensionDateObj instanceof Date) {
                extensionDate = (Date) extensionDateObj;
            }

            totParam.put("extensionDate", extensionDate);
        } else if(CommonFunctions.isBlank(totParam, "extensionDate") && CommonFunctions.isNotBlank(totParam, "extensionDateStr")) {
            Date extensionDate = null;
            String extensionDateStr = totParam.get("extensionDateStr").toString();

            try {
                extensionDate = DateUtils.convertStringToDate(extensionDateStr, DateUtils.PATTERN_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(extensionDate != null) {
                totParam.put("extensionDate", extensionDate);
            }
        }

        if(CommonFunctions.isNotBlank(totParam, "totUUID")) {
            if(reportTOTMapper.update(totParam) > 0) {
                totUUID = totParam.get("totUUID").toString();
            }
        } else {
            if(CommonFunctions.isBlank(totParam, "reportUUID")) {
                throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
            }

            //为了保证记录的唯一性
            Map<String, Object> totMap = reportTOTMapper.findByIdSimple(totParam);

            if(totMap != null && !totMap.isEmpty()) {
                Map<String, Object> totMapAfter = new HashMap<String, Object>(),
                        totMapAfterRemain = new HashMap<String, Object>();
                totMapAfter.putAll(totMap);
                totMapAfter.putAll(totParam);

                for(String key : totMapAfter.keySet()) {
                    if(CommonFunctions.isBlank(totMap, key)
                            || (CommonFunctions.isNotBlank(totMapAfter,key) && !totMapAfter.get(key).equals(totMap.get(key)))
                    ) {
                        totMapAfterRemain.put(key, totMapAfter.get(key));
                    }
                }

                if(!totMapAfterRemain.isEmpty()) {
                    totUUID = totMap.get("totUUID").toString();
                    totMapAfterRemain.put("totUUID", totUUID);

                    if(reportTOTMapper.update(totMapAfterRemain) > 0) {
                        totUUID = totMap.get("totUUID").toString();
                    }
                } else {
                    totUUID = totMap.get("totUUID").toString();
                }
            } else if(reportTOTMapper.insert(totParam) > 0) {
                if(CommonFunctions.isNotBlank(totParam, "totUUID")) {
                    totUUID = totParam.get("totUUID").toString();
                }
            }

            saveReportExtraInfo(totParam);
        }

        return totUUID;
    }

    @Override
    public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
                                                                Map<String, Object> params) {
        Map<String, Object> totParam = new HashMap<String, Object>(), totParamMap = null;
        boolean isWithReportFocus = true;
        boolean isCapResMarkerInfo = true;
        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
            isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
        }
        if(CommonFunctions.isNotBlank(params, "isCapResMarkerInfo")) {
            isCapResMarkerInfo = Boolean.valueOf(params.get("isCapResMarkerInfo").toString());
        }

        if(StringUtils.isNotBlank(reportUUID)) {
            totParam.put("reportUUID", reportUUID);
        } else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
            totParam.put("reportUUID", params.get("reportUUID"));
        } else if(CommonFunctions.isNotBlank(params, "totUUID")) {
            totParam.put("totUUID", params.get("totUUID"));
        } else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
            totParam.put("reportId", params.get("reportId"));
        }

        if(totParam.isEmpty()) {
            throw new IllegalArgumentException("缺失唯一性条件，不可查询整治信息！");
        }

        if(isWithReportFocus) {
            totParamMap = reportTOTMapper.findById(totParam);

            if(totParamMap != null && !totParamMap.isEmpty()) {
                List<Map<String, Object>> totMapList = new ArrayList<Map<String, Object>>();

                totMapList.add(totParamMap);

                if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
                    params.put("userOrgCode", userInfo.getOrgCode());
                }

                formatDataOut(totMapList, params);
            }
        } else {
            totParamMap = reportTOTMapper.findByIdSimple(totParam);
        }

        params.put("isCapResMarkerInfo",isCapResMarkerInfo);
        findReportExtraInfo(totParamMap, params);

        return totParamMap;
    }

    @Override
    public String capNumberCfgBizCode() {
        return ReportNumberCfgBizCodeEnum.threeOneTreatment.getBizCode();
    }

    @Override
    public void formatParamIn4Report(Map<String, Object> params) throws Exception {
        params = params == null ? new HashMap<String, Object>() : params;

        super.formatParamIn4Report(params);
    }

    /**
     * 保存整治信息额外信息
     * @param params	额外参数
     * 			reportId	上报id，如果没有，通过外部总入口进行调整，不要内部再次通过对外接口进行信息转换获取
     */
    @Override
    protected void saveReportExtraInfo(Map<String, Object> params) {
        super.saveReportExtraInfo(params);
    }

    /**
     * 判断地域信息是否符合要求
     * @param orgEntityInfo	地域信息
     * @param params
     * 			dataSource		数据来源
     * 			collectSource	采集方式
     * 			isEditableNode	是否可编辑，true表示当前节点需要选择到村二级网格，默认为false
     * @return
     * @throws Exception
     */
    protected boolean isRegionSatisfied(OrgEntityInfoBO orgEntityInfo, Map<String, Object> params) throws Exception {
        boolean flag = true, isEditableNode = false;
        String regionChiefLevel = null, dataSource = null,collectSource = null, regionCode = null;

        if(orgEntityInfo != null) {
            regionCode = orgEntityInfo.getOrgCode();
            regionChiefLevel = orgEntityInfo.getChiefLevel();
        }
        if(CommonFunctions.isNotBlank(params,"collectSource")){
            collectSource = String.valueOf(params.get("collectSource"));
        }
        if(CommonFunctions.isNotBlank(params,"dataSource")){
            dataSource = String.valueOf(params.get("dataSource"));
        }
        if(CommonFunctions.isNotBlank(params,"isEditableNode")){
            isEditableNode = Boolean.valueOf(String.valueOf(params.get("isEditableNode")));
        }

        //采集来源为 01 02 的，地域必须选择到网格层级
        //或者当前环节为第一副网格长核实环节时，无论哪个来源都需要选择到网格层级
        //add by ztc 无法核实的、核实后不属实的或者不在本网格/本村的，所属区域不需要到二级网格
        if( TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                /*|| TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)*/
                || isEditableNode ) {
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
                if(logger.isErrorEnabled()) {
                    logger.error("地域编码【" + regionCode + "】不是网格层级！");
                }

                throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
            }
        }

        return flag;
    }

    @Override
    protected ReportTwoViolationPreMapper capReportExtendMapper() {
        return reportTOTMapper;
    }


    @Override
    protected String capBizType4Attachment() {
        return "THREE_ONE_TREATMENT";
    }

    @Override
    protected String capBizType4ResmarkerInfo() {
        return "THREE_ONE_TREATMENT";
    }

    /**
     * 格式化输入参数
     * @param params
     * @throws ZhsqEventException
     */
    @Override
    protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
        super.formatParamIn(params);

        if(CommonFunctions.isNotBlank(params, "hiddenDangerType")) {
            String hiddenDangerType = params.get("hiddenDangerType").toString();

            if(hiddenDangerType.contains(",")) {
                params.put("hiddenDangerTypeArray", hiddenDangerType.split(","));
                params.remove("hiddenDangerType");
            }
        }
    }

    /**
     * 数据格式化输出
     * @param reportList
     * @param params
     */
    @Override
    protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
        if(reportList != null && reportList.size() > 0) {
            String userOrgCode = null, trafficModeStr = null;
            boolean isDictTransfer = true;
            List<BaseDataDict> reportStatusDictList = null,
                    hiddenDangerTypeDictList = null,
                    dataSourceDictList = null;
            int listType = 0;

            super.formatDataOut(reportList, params);

            if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
                userOrgCode = params.get("userOrgCode").toString();
            }
            if(CommonFunctions.isNotBlank(params, "isDictTransfer")) {
                isDictTransfer = Boolean.valueOf(params.get("isDictTransfer").toString());
            }
            if(CommonFunctions.isNotBlank(params, "listType")) {
                try {
                    listType = Integer.valueOf(params.get("listType").toString());
                } catch(NumberFormatException e) {}
            }

            if(isDictTransfer && StringUtils.isNotBlank(userOrgCode)) {
                hiddenDangerTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(TOTDictEnum.PROBLEM_TYPE.getDictCode(), userOrgCode);
                dataSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(TOTDictEnum.DATA_SOURCE.getDictCode(), userOrgCode);

                if(listType == 1) {
                    reportStatusDictList = new ArrayList<BaseDataDict>();
                    //草稿不增添字典，因为只有草稿列表会用到
                    BaseDataDict draftDict = new BaseDataDict();
                    draftDict.setDictGeneralCode(TOTReportStatusEnum.DRAFT_STATUS.getReportStatus());
                    draftDict.setDictName(TOTReportStatusEnum.DRAFT_STATUS.getReportStatusName());
                    reportStatusDictList.add(draftDict);
                } else {
                    reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(TOTDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
                }
            }

            for(Map<String, Object> reportMap : reportList) {
                if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
                    reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
                }
                if(CommonFunctions.isNotBlank(reportMap, "extensionDate")) {
                    reportMap.put("extensionDateStr", DateUtils.formatDate((Date) reportMap.get("extensionDate"), DateUtils.PATTERN_DATE));
                }
                if(CommonFunctions.isNotBlank(reportMap, "communityDuedate")) {
                    reportMap.put("communityDuedateStr", DateUtils.formatDate((Date) reportMap.get("communityDuedate"), DateUtils.PATTERN_DATE));
                }
                if(CommonFunctions.isNotBlank(reportMap, "streetDuedate")) {
                    reportMap.put("streetDuedateStr", DateUtils.formatDate((Date) reportMap.get("streetDuedate"), DateUtils.PATTERN_DATE));
                }
                if(CommonFunctions.isNotBlank(reportMap, "disposalResult")) {
                    String disposalResult = String.valueOf(reportMap.get("disposalResult"));
                    if(TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult)){
                        reportMap.put("disposalResultStr", TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResultName());
                    }else if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)){
                        reportMap.put("disposalResultStr", TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResultName());
                    }else if(TOTDisposalResultEnum.STREET_HANDLE.getDisposalResult().equals(disposalResult)){
                        reportMap.put("disposalResultStr", TOTDisposalResultEnum.STREET_HANDLE.getDisposalResultName());
                    }
                }

                DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
                DataDictHelper.setDictValueForField(reportMap, "hiddenDangerType", "hiddenDangerTypeName", hiddenDangerTypeDictList);
                DataDictHelper.setDictValueForField(reportMap, "dataSource", "dataSourceName", dataSourceDictList);
            }
        }
    }

    @Override
    public EUDGPagination findPagination4ReportExtend(int pageNo, int pageSize, Map<String, Object> params) throws Exception {
        EUDGPagination reportPagination = null;
        int listType = 0;
        String METHOD_FORMAT_IN = "formatParamIn", METHOD_LIST = "findPagination";

        params = params == null ? new HashMap<String, Object>() : params;

        formatParamIn(params);

        if(CommonFunctions.isNotBlank(params, "listType")) {
            try {
                listType = Integer.valueOf(params.get("listType").toString());
            } catch(NumberFormatException e) {}
        }

        if(capMethodMap().containsKey(listType)) {
            try {
                //getMethod () 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
                this.getClass().getMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
            } catch(NoSuchMethodException e) {
                //getDeclaredMethod() 获取的是类自身声明的所有方法，包含public、protected和private方法。
                this.getClass().getDeclaredMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
            }

            try {
                reportPagination = (EUDGPagination) this.getClass().getMethod(METHOD_LIST + capMethodMap().get(listType), new Class[] {int.class, int.class, Map.class}).invoke(this, pageNo, pageSize, params);
            } catch(NoSuchMethodException e) {
                reportPagination = (EUDGPagination) this.getClass().getDeclaredMethod(METHOD_LIST + capMethodMap().get(listType), new Class[] {int.class, int.class, Map.class}).invoke(this, pageNo, pageSize, params);
            }
        }

        return reportPagination;
    }

    @SuppressWarnings("unused")
    public void formatParamIn4StatusTrack(Map<String, Object> params) {
        int listType = 0, subFormTypeId = 0, routineInterval = 0;
        Date today = null, routineDayStart = null, routineDayEnd = null;
        try {
            today = DateUtils.convertStringToDate(DateUtils.getToday(), DateUtils.PATTERN_DATE);
        } catch (ParseException e) {}

        formatParamIn4Todo(params);

        if(CommonFunctions.isNotBlank(params, "listType")) {
            listType = Integer.valueOf(params.get("listType").toString());
        }

        routineDayEnd = holidayInfoService.getWorkDateByAfterWorkDay(today, -1);

        if(!today.equals(routineDayEnd)) {
            routineDayEnd = today;
        }

        switch(listType) {
            case 8: {
                subFormTypeId = Integer.valueOf(FocusReportNode36401Enum.FORM_TYPE_ID.toString());
                routineInterval = 0;
                break;
            }
        }

        //传入的间隔需要减1，因为无法确认传入的间隔是否减了1，因此该操作有调用方完成
        //传入的间隔需要减1是因为语句中简单的+1无法将节假日剔除
        if(CommonFunctions.isNotBlank(params, "routineInterval")) {
            try {
                routineInterval = Integer.valueOf(params.get("routineInterval").toString());
            } catch(NumberFormatException e) {}
        }

        routineDayStart = holidayInfoService.getWorkDateByAfterWorkDay(routineDayEnd, -routineInterval);

        params.put("subFormTypeId", subFormTypeId);
        params.put("routineDayStart", routineDayStart);
        params.put("routineDayEnd", routineDayEnd);
    }

    /**
     * 获取待办列表数量
     * @param params
     * @return
     */
    @SuppressWarnings("unused")
    public int findCount4StatusTrack(Map<String, Object> params) {
        int total = 0;

        total = reportTOTMapper.findCount4StatusTrack(params);

        return total;
    }

    /**
     * 分页获取待办列表记录
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    @SuppressWarnings("unused")
    public EUDGPagination findPagination4StatusTrack(int pageNo, int pageSize, Map<String, Object> params) {
        int total = 0;
        List<Map<String, Object>> reportList = null;

        total = reportTOTMapper.findCount4StatusTrack(params);

        if(total > 0) {
            pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 10 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

            reportList = reportTOTMapper.findList4StatusTrack(params, rowBounds);

            formatDataOut(reportList, params);
        } else {
            reportList = new ArrayList<Map<String, Object>>();
        }

        EUDGPagination reportPagination = new EUDGPagination(total, reportList);

        return reportPagination;
    }
}
