package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.environmentalHealthTreatment.ReportEHTMapper;
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
 * @Description:环境卫生问题处置，业务信息表：T_EVENT_EHT
 * @Author: zhangtc
 * @Date: 2021/7/26 14:53
 */
@Service("reportEHTService")
public class ReportEHTServiceImpl extends ReportTwoViolationPreServiceImpl {

    @Autowired
    private ReportEHTMapper reportEHTMapper;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

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
            }
        };
    }

    @Override
    public String saveReportExtendInfo(Map<String, Object> ehtParam, UserInfo userInfo) {

        if(ehtParam == null || ehtParam.isEmpty()) {
            throw new IllegalArgumentException("缺少需要操作的问题处置信息！");
        }
        if(userInfo == null) {
            throw new IllegalArgumentException("缺少操作用户信息！");
        }
        Long userId = userInfo.getUserId();
        String ehpUUID = null;

        if(CommonFunctions.isBlank(ehtParam, "creator")) {
            ehtParam.put("creator", userId);
        }
        if(CommonFunctions.isBlank(ehtParam, "updator")) {
            ehtParam.put("updator", userId);
        }

        if(CommonFunctions.isNotBlank(ehtParam, "feedbackTime")) {
            Object feedbackTimeObj = ehtParam.get("feedbackTime");
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

            ehtParam.put("feedbackTime", feedbackTime);
        } else if(CommonFunctions.isBlank(ehtParam, "feedbackTime") && CommonFunctions.isNotBlank(ehtParam, "feedbackTimeStr")) {
            Date feedbackTime = null;
            String feedbackTimeStr = ehtParam.get("feedbackTimeStr").toString();

            try {
                feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(feedbackTime != null) {
                ehtParam.put("feedbackTime", feedbackTime);
            }
        }

        if(CommonFunctions.isNotBlank(ehtParam, "ehpUUID")) {
            if(reportEHTMapper.update(ehtParam) > 0) {
                ehpUUID = ehtParam.get("ehpUUID").toString();
            }
        } else {
            if(CommonFunctions.isBlank(ehtParam, "reportUUID")) {
                throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
            }

            //为了保证记录的唯一性
            Map<String, Object> ehtMap = reportEHTMapper.findByIdSimple(ehtParam);

            //发现渠道需要和数据来源保持一致
            if(CommonFunctions.isNotBlank(ehtParam,"discoveryChannel")){
                String discoveryChannel = String.valueOf(ehtParam.get("discoveryChannel"));

                //上级派发
                if(discoveryChannel.equals("1")){
                    ehtParam.put("dataSource",EHTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource());
                }else if(discoveryChannel.equals("2")){
                    //群众举报
                    ehtParam.put("dataSource",EHTDataSourceEnum.MASSES_REPORT.getDataSource());
                }
            }

            if(ehtMap != null && !ehtMap.isEmpty()) {
                Map<String, Object> ehtMapAfter = new HashMap<String, Object>(),
                        epcMapAfterRemain = new HashMap<String, Object>();
                ehtMapAfter.putAll(ehtMap);
                ehtMapAfter.putAll(ehtParam);

                for(String key : ehtMapAfter.keySet()) {
                    if(CommonFunctions.isBlank(ehtMap, key)
                            || !ehtMapAfter.get(key).equals(ehtMap.get(key))) {
                        epcMapAfterRemain.put(key, ehtMapAfter.get(key));
                    }
                }

                if(!epcMapAfterRemain.isEmpty()) {
                    ehpUUID = ehtMap.get("ehpUUID").toString();
                    epcMapAfterRemain.put("ehpUUID", ehpUUID);

                    if(reportEHTMapper.update(epcMapAfterRemain) > 0) {
                        ehpUUID = ehtMap.get("ehpUUID").toString();
                    }
                } else {
                    ehpUUID = ehtMap.get("ehpUUID").toString();
                }
            } else if(reportEHTMapper.insert(ehtParam) > 0) {
                if(CommonFunctions.isNotBlank(ehtParam, "ehpUUID")) {
                    ehpUUID = ehtParam.get("ehpUUID").toString();
                }
            }

            saveReportExtraInfo(ehtParam);
        }

        return ehpUUID;
    }

    @Override
    public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params) {

        Map<String, Object> ehtParam = new HashMap<String, Object>(), ehtParamMap = null;
        boolean isWithReportFocus = true;
        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
            isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
        }

        if(StringUtils.isNotBlank(reportUUID)) {
            ehtParam.put("reportUUID", reportUUID);
        } else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
            ehtParam.put("reportUUID", params.get("reportUUID"));
        } else if(CommonFunctions.isNotBlank(params, "ehpUUID")) {
            ehtParam.put("ehpUUID", params.get("ehpUUID"));
        } else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
            ehtParam.put("reportId", params.get("reportId"));
        }

        if(ehtParam.isEmpty()) {
            throw new IllegalArgumentException("缺失唯一性条件，不可查询问题处置信息！");
        }

        if(isWithReportFocus) {
            ehtParamMap = reportEHTMapper.findById(ehtParam);

            if(ehtParamMap != null && !ehtParamMap.isEmpty()) {
                List<Map<String, Object>> ehtMapList = new ArrayList<Map<String, Object>>();

                ehtMapList.add(ehtParamMap);

                if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
                    params.put("userOrgCode", userInfo.getOrgCode());
                }

                formatDataOut(ehtMapList, params);
            }
        } else {
            ehtParamMap = reportEHTMapper.findByIdSimple(ehtParam);
        }

        findReportExtraInfo(ehtParamMap, params);

        return ehtParamMap;
    }

    @Override
    public String capNumberCfgBizCode() {
        return ReportNumberCfgBizCodeEnum.environmentHealTreatment.getBizCode();
    }

    /**
     * 保存换位处置信息
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
     * 			isEditableNode	是否可编辑，true表示当前节点需要选择到村二级网格，默认为false
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isRegionSatisfied(OrgEntityInfoBO orgEntityInfo, Map<String, Object> params) throws Exception {

        boolean flag = true, isEditableNode = false;
        String regionChiefLevel = null, dataSource = null, regionCode = null,verifyStatus=null;

        if(orgEntityInfo != null) {
            regionCode = orgEntityInfo.getOrgCode();
            regionChiefLevel = orgEntityInfo.getChiefLevel();
        }
        if(CommonFunctions.isNotBlank(params,"dataSource")){
            dataSource = String.valueOf(params.get("dataSource"));
        }
        if(CommonFunctions.isNotBlank(params,"isEditableNode")){
            isEditableNode = Boolean.valueOf(String.valueOf(params.get("isEditableNode")));
        }
        if(CommonFunctions.isNotBlank(params,"verifyStatus")){
            verifyStatus = String.valueOf(params.get("verifyStatus"));
        }

        //采集来源为 01 的，地域必须选择到网格层级
        //或者当前环节为第一副网格长核实环节时，无论哪个来源都需要选择到网格层级
        //add by ztc 无法核实的、核实后不属实的或者不在本网格/本村的，所属区域不需要到二级网格
        if( (EHTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode)) {
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
                if(logger.isErrorEnabled()) {
                    logger.error("地域编码【" + regionCode + "】不是网格层级！");
                }

                throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
            }

        } else {
            //采集来源是 02 03 04 的，地域需要选择到网格层级或村社区层级
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
                throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
            }
        }

        return flag;
    }

    @Override
    protected ReportTwoViolationPreMapper capReportExtendMapper() {
        return reportEHTMapper;
    }

    @Override
    protected String capBizType4Attachment() {
        return "ENVIRONMENTAL_HEALTH_TREATMENT";
    }

    @Override
    protected String capBizType4ResmarkerInfo() {
        return "ENVIRONMENTAL_HEALTH_TREATMENT";
    }

    /**
     * 格式化输入参数
     * @param params
     * @throws ZhsqEventException
     */
    @Override
    protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
        super.formatParamIn(params);

        if(CommonFunctions.isNotBlank(params, "problemType")) {
            String problemType = params.get("problemType").toString();

            if(problemType.contains(",")) {
                params.put("problemTypeArray", problemType.split(","));
                params.remove("problemType");
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
            String userOrgCode = null;
            boolean isDictTransfer = true;

            List<BaseDataDict> reportStatusDictList = null, dataSourceDictList = null,problemTypeDictList = null,discoveryChannelDictList = null;
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
                dataSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(EHTDictEnum.DATA_SOURCE.getDictCode(), userOrgCode);
                problemTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(EHTDictEnum.PROBLEM_TYPE.getDictCode(), userOrgCode);
                discoveryChannelDictList = baseDictionaryService.getDataDictListOfSinglestage(EHTDictEnum.DISCOVERY_CHANNELS.getDictCode(), userOrgCode);


                if(listType == 1) {
                    reportStatusDictList = new ArrayList<BaseDataDict>();
                    //草稿不增添字典，因为只有草稿列表会用到
                    BaseDataDict draftDict = new BaseDataDict();
                    draftDict.setDictGeneralCode(EHTReportStatusEnum.DRAFT_STATUS.getReportStatus());
                    draftDict.setDictName(EHTReportStatusEnum.DRAFT_STATUS.getReportStatusName());
                    reportStatusDictList.add(draftDict);
                } else {
                    reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(EHTDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
                }
            }

            for(Map<String, Object> reportMap : reportList) {
                DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
                DataDictHelper.setDictValueForField(reportMap, "dataSource", "dataSourceName", dataSourceDictList);
                DataDictHelper.setDictValueForField(reportMap, "problemType", "problemTypeName", problemTypeDictList);
                DataDictHelper.setDictValueForField(reportMap, "discoveryChannel", "discoveryChannelName", discoveryChannelDictList);

                if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
                    reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
                }

                if(CommonFunctions.isNotBlank(reportMap,"discoveryStaff")){
                    reportMap.put("discoveryStaffName",reportMap.get("discoveryChannel").toString().equals("1")?"发现部门":"发现群众");
                }
            }
        }
    }
}
