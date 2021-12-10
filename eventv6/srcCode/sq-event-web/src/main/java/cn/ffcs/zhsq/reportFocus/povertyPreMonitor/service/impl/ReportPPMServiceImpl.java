package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.povertyPreMonitor.ReportPPMMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:防止致贫返贫监测信息，相关涉及表：T_EVENT_POVERTY_PRE_MONITOR
 * @Author: zhangtc
 * @Date: 2021/6/1 9:49
 */
@Service("reportPPMService")
public class ReportPPMServiceImpl extends ReportTwoViolationPreServiceImpl {

    @Autowired
    private ReportPPMMapper reportPPMMapper;

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
    public String saveReportExtendInfo(Map<String, Object> ppmParam, UserInfo userInfo) {

        if(ppmParam == null || ppmParam.isEmpty()) {
            throw new IllegalArgumentException("缺少需要操作的监测信息！");
        }
        if(userInfo == null) {
            throw new IllegalArgumentException("缺少操作用户信息！");
        }
        Long userId = userInfo.getUserId();
        String ppmUUID = null;

        if(CommonFunctions.isBlank(ppmParam, "creator")) {
            ppmParam.put("creator", userId);
        }
        if(CommonFunctions.isBlank(ppmParam, "updator")) {
            ppmParam.put("updator", userId);
        }

        if(CommonFunctions.isNotBlank(ppmParam, "ppmUUID")) {
            if(reportPPMMapper.update(ppmParam) > 0) {
                ppmUUID = ppmParam.get("ppmUUID").toString();
            }
        } else {
            if(CommonFunctions.isBlank(ppmParam, "reportUUID")) {
                throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
            }

            //为了保证记录的唯一性
            Map<String, Object> ppmMap = reportPPMMapper.findByIdSimple(ppmParam);

            if(ppmMap != null && !ppmMap.isEmpty()) {
                Map<String, Object> ppmMapAfter = new HashMap<String, Object>(),
                        epcMapAfterRemain = new HashMap<String, Object>();
                ppmMapAfter.putAll(ppmMap);
                ppmMapAfter.putAll(ppmParam);

                for(String key : ppmMapAfter.keySet()) {
                    if(CommonFunctions.isBlank(ppmMap, key)
                            || !ppmMapAfter.get(key).equals(ppmMap.get(key))) {
                        epcMapAfterRemain.put(key, ppmMapAfter.get(key));
                    }
                }

                if(!epcMapAfterRemain.isEmpty()) {
                    ppmUUID = ppmMap.get("ppmUUID").toString();
                    epcMapAfterRemain.put("ppmUUID", ppmUUID);

                    if(reportPPMMapper.update(epcMapAfterRemain) > 0) {
                        ppmUUID = ppmMap.get("ppmUUID").toString();
                    }
                } else {
                    ppmUUID = ppmMap.get("ppmUUID").toString();
                }
            } else if(reportPPMMapper.insert(ppmParam) > 0) {
                if(CommonFunctions.isNotBlank(ppmParam, "ppmUUID")) {
                    ppmUUID = ppmParam.get("ppmUUID").toString();
                }
            }

            saveReportExtraInfo(ppmParam);
        }

        return ppmUUID;
    }

    @Override
    public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
                                                                Map<String, Object> params) {
        Map<String, Object> ppmParam = new HashMap<String, Object>(), ppmParamMap = null;
        boolean isWithReportFocus = true;
        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
            isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
        }

        if(StringUtils.isNotBlank(reportUUID)) {
            ppmParam.put("reportUUID", reportUUID);
        } else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
            ppmParam.put("reportUUID", params.get("reportUUID"));
        } else if(CommonFunctions.isNotBlank(params, "ppmUUID")) {
            ppmParam.put("ppmUUID", params.get("ppmUUID"));
        } else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
            ppmParam.put("reportId", params.get("reportId"));
        }

        if(ppmParam.isEmpty()) {
            throw new IllegalArgumentException("缺失唯一性条件，不可查询监测信息！");
        }

        if(isWithReportFocus) {
            ppmParamMap = reportPPMMapper.findById(ppmParam);

            if(ppmParamMap != null && !ppmParamMap.isEmpty()) {
                List<Map<String, Object>> ppmMapList = new ArrayList<Map<String, Object>>();

                ppmMapList.add(ppmParamMap);

                if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
                    params.put("userOrgCode", userInfo.getOrgCode());
                }

                formatDataOut(ppmMapList, params);
            }
        } else {
            ppmParamMap = reportPPMMapper.findByIdSimple(ppmParam);
        }

        findReportExtraInfo(ppmParamMap, params);

        return ppmParamMap;
    }

    @Override
    public String capNumberCfgBizCode() {
        return ReportNumberCfgBizCodeEnum.povertyPreMonitor.getBizCode();
    }


    /**
     * 保存监测额外信息
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
        if( (PPMDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode)) {
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
                if(logger.isErrorEnabled()) {
                    logger.error("地域编码【" + regionCode + "】不是网格层级！");
                }

                throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
            }

        } else {
            //采集来源是 02 03 04 06 的，地域需要选择到网格层级或村社区层级
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
                throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
            }
        }

        return flag;
    }

    @Override
    protected ReportTwoViolationPreMapper capReportExtendMapper() {
        return reportPPMMapper;
    }

    @Override
    protected String capBizType4Attachment() {
        return "POVERTY_PRE_MONITOR";
    }

    @Override
    protected String capBizType4ResmarkerInfo() {
        return "POVERTY_PRE_MONITOR";
    }


    /**
     * 格式化输入参数
     * @param params
     * @throws ZhsqEventException
     */
    @Override
    protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
        super.formatParamIn(params);

        if(CommonFunctions.isNotBlank(params, "povBackReason")) {
            String povBackReason = params.get("povBackReason").toString();

            if(povBackReason.contains(",")) {
                params.put("povBackReasonArray", povBackReason.split(","));
                params.remove("povBackReason");
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

            List<BaseDataDict> reportStatusDictList = null, dataSourceDictList = null,povBackReasonDictList = null,ppmRiskDictList = null;
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
                dataSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(PPMDictEnum.DATA_SOURCE.getDictCode(), userOrgCode);
                povBackReasonDictList = baseDictionaryService.getDataDictListOfSinglestage(PPMDictEnum.POV_BACK_REASON.getDictCode(), userOrgCode);
                ppmRiskDictList = baseDictionaryService.getDataDictListOfSinglestage(PPMDictEnum.PPM_RISK.getDictCode(), userOrgCode);


                if(listType == 1) {
                    reportStatusDictList = new ArrayList<BaseDataDict>();
                    //草稿不增添字典，因为只有草稿列表会用到
                    BaseDataDict draftDict = new BaseDataDict();
                    draftDict.setDictGeneralCode(PPMReportStatusEnum.DRAFT_STATUS.getReportStatus());
                    draftDict.setDictName(PPMReportStatusEnum.DRAFT_STATUS.getReportStatusName());
                    reportStatusDictList.add(draftDict);
                } else {
                    reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(PPMDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
                }
            }

            for(Map<String, Object> reportMap : reportList) {
                DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
                DataDictHelper.setDictValueForField(reportMap, "dataSource", "dataSourceName", dataSourceDictList);
                DataDictHelper.setDictValueForField(reportMap, "povBackReason", "povBackReasonName", povBackReasonDictList);
                DataDictHelper.setDictValueForField(reportMap, "ppmRisk", "ppmRiskName", ppmRiskDictList);

                if(CommonFunctions.isNotBlank(reportMap,"isHonmura")){
                    reportMap.put("isHonmura",reportMap.get("isHonmura").toString().equals("1")?"属于本村":"不属于本村");
                }
            }
        }
    }
}
