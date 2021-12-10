package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.meetingsAndLesson.ReportMeetingMapper;
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
 * @Description:
 * @Author: zhangtc
 * @Date: 2020/12/4 11:47
 */
@Service("reportMeetingService")
public class ReportMeetingServiceImpl extends ReportTwoViolationPreServiceImpl {
    @Autowired
    private ReportMeetingMapper reportMeetingMapper;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
	 * 获取列表方法映射
	 * @return
	 */
	@SuppressWarnings("serial")
	protected Map<Integer, String> capMethodMap() {
		return new HashMap<Integer, String>() {
	        {
	            put(1, "4Draft");
	            put(2, "4Todo");
	            put(3, "4Handled");
	            put(4, "4Initiator");
	            put(5, "4Jurisdiction");
				put(51,"4JurisdictionWithMarker");
				put(52,"4JurisdictionWithRegionPath");
	            put(6, "4Archived");
	            put(7, "4MsgReading");
	        }
	    };
	}
	
    @Override
    public String saveReportExtendInfo(Map<String, Object> meetingParam, UserInfo userInfo) {
        if(meetingParam == null || meetingParam.isEmpty()) {
            throw new IllegalArgumentException("缺少需要操作的会议信息！");
        }
        if(userInfo == null) {
            throw new IllegalArgumentException("缺少操作用户信息！");
        }
        Long userId = userInfo.getUserId();
        String meetingUUID = null;

        if(CommonFunctions.isBlank(meetingParam, "creator")) {
            meetingParam.put("creator", userId);
        }
        if(CommonFunctions.isBlank(meetingParam, "updator")) {
            meetingParam.put("updator", userId);
        }

        if(CommonFunctions.isNotBlank(meetingParam, "meetingUUID")) {
            if(reportMeetingMapper.update(meetingParam) > 0) {
                meetingUUID = meetingParam.get("meetingUUID").toString();
            }
        } else {
            if(CommonFunctions.isBlank(meetingParam, "reportUUID")) {
                throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
            }

            //为了保证记录的唯一性
            Map<String, Object> meetingMap = reportMeetingMapper.findByIdSimple(meetingParam);

            if(meetingMap != null && !meetingMap.isEmpty()) {
                Map<String, Object> meetingMapAfter = new HashMap<String, Object>(),
                        meetingMapAfterRemain = new HashMap<String, Object>();
                meetingMapAfter.putAll(meetingMap);
                meetingMapAfter.putAll(meetingParam);

                for(String key : meetingMapAfter.keySet()) {
                    if(CommonFunctions.isBlank(meetingMap, key)
                            || !meetingMapAfter.get(key).equals(meetingMap.get(key))) {
                        meetingMapAfterRemain.put(key, meetingMapAfter.get(key));
                    }
                }

                if(!meetingMapAfterRemain.isEmpty()) {
                    meetingUUID = meetingMap.get("meetingUUID").toString();
                    meetingMapAfterRemain.put("meetingUUID", meetingUUID);

                    if(reportMeetingMapper.update(meetingMapAfterRemain) > 0) {
                        meetingUUID = meetingMap.get("meetingUUID").toString();
                    }
                } else {
                    meetingUUID = meetingMap.get("meetingUUID").toString();
                }
            } else if(reportMeetingMapper.insert(meetingParam) > 0) {//三会一课副表新增
                if(CommonFunctions.isNotBlank(meetingParam, "meetingUUID")) {
                    meetingUUID = meetingParam.get("meetingUUID").toString();
                }
            }

            saveReportExtraInfo(meetingParam);
        }

        return meetingUUID;
    }

    @Override
    public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
                                                                Map<String, Object> params) {
        Map<String, Object> meetingParam = new HashMap<String, Object>(), meetingParamMap = null;
        boolean isWithReportFocus = true;
        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
            isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
        }

        if(StringUtils.isNotBlank(reportUUID)) {
            meetingParam.put("reportUUID", reportUUID);
        } else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
            meetingParam.put("reportUUID", params.get("reportUUID"));
        } else if(CommonFunctions.isNotBlank(params, "meetingUUID")) {
            meetingParam.put("meetingUUID", params.get("meetingUUID"));
        } else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
            meetingParam.put("reportId", params.get("reportId"));
        }

        if(meetingParam.isEmpty()) {
            throw new IllegalArgumentException("缺失唯一性条件，不可查询三会一课！");
        }

        if(isWithReportFocus) {
            meetingParamMap = reportMeetingMapper.findById(meetingParam);

            if(meetingParamMap != null && !meetingParamMap.isEmpty()) {
                List<Map<String, Object>> meetingMapList = new ArrayList<Map<String, Object>>();

                meetingMapList.add(meetingParamMap);

                if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
                    params.put("userOrgCode", userInfo.getOrgCode());
                }

                formatDataOut(meetingMapList, params);
            }
        } else {
            meetingParamMap = reportMeetingMapper.findByIdSimple(meetingParam);
        }

        findReportExtraInfo(meetingParamMap, params);

        return meetingParamMap;
    }

    @Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.meetingAndLesson.getBizCode();
	}
    
    protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportMeetingMapper;
	}

    protected String capBizType4Attachment() {
		return "MEETINGS_AND_LESSON";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "MEETINGS_AND_LESSON";
	}

    /**
     * 格式化输入参数
     * @param params
     * @throws ZhsqEventException
     */
    protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
    	super.formatParamIn(params);
    	
    	if(CommonFunctions.isNotBlank(params, "meetingType")) {
            String meetingType = params.get("meetingType").toString();

            if(meetingType.contains(",")) {
                params.put("meetingTypeArray", meetingType.split(","));
                params.remove("meetingType");
            }
        }

        if(CommonFunctions.isNotBlank(params, "collectSourceArray")) {
            params.put("collectSourceArray", params.get("collectSourceArray").toString().split(","));
        }
    	
    }

    /**
     * 数据格式化输出
     * @param reportList
     * @param params
     */
    protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
        if(reportList != null && reportList.size() > 0) {
            String userOrgCode = null;
            boolean isDictTransfer = true;
            List<BaseDataDict> reportStatusDictList = null,//上报状态
            				   meetingTypeDictList = null;//会议类型

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
                meetingTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(MeetingDictEnum.MEETING_TYPE.getDictCode(), userOrgCode);

                if(listType == 1) {
                    reportStatusDictList = new ArrayList<BaseDataDict>();
                    //草稿不增添字典，因为只有草稿列表会用到
                    BaseDataDict draftDict = new BaseDataDict();
                    draftDict.setDictGeneralCode(MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
                    draftDict.setDictName(MeetingReportStatusEnum.DRAFT_STATUS.getReportStatusName());
                    reportStatusDictList.add(draftDict);
                } else {
                    reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(MeetingDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
                }
            }

            for(Map<String, Object> reportMap : reportList) {
                DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
                DataDictHelper.setDictValueForField(reportMap, "meetingType", "meetingTypeName", meetingTypeDictList);
            }
        }
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
        String regionChiefLevel = null, dataSource = null,collectSource = null, regionCode = null;

        if(orgEntityInfo != null) {
            regionCode = orgEntityInfo.getOrgCode();
            regionChiefLevel = orgEntityInfo.getChiefLevel();
        }
        if(CommonFunctions.isNotBlank(params,"dataSource")){
            dataSource = String.valueOf(params.get("dataSource"));
        }
        if(CommonFunctions.isNotBlank(params,"collectSource")){
            collectSource = String.valueOf(params.get("collectSource"));
        }
        if(CommonFunctions.isNotBlank(params,"isEditableNode")){
            isEditableNode = Boolean.valueOf(String.valueOf(params.get("isEditableNode")));
        }

        //来源为 02 的，地址控件必须选择到网格层级
        if(MeetingCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource) || isEditableNode) {
            if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
                if(logger.isErrorEnabled()) {
                    logger.error("地域编码【" + regionCode + "】不是网格层级！");
                }

                throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
            }
        } else if(MeetingCollectSourceEnum.COMMUNITY_REPORT.getCollectSource().equals(collectSource)
                    || MeetingCollectSourceEnum.STREET_COMMITTEE_REPORT.getCollectSource().equals(collectSource)
                    || MeetingCollectSourceEnum.STREET_GROUPLEADER_REPORT.getCollectSource().equals(collectSource) ){
                //村社区、乡镇街道采集的 所属区域需要精确到 村社区、网格
                //地址控件需要选择到网格层级或村社区层级
                if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
                    throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
                }
            }else if(MeetingCollectSourceEnum.CITY_COMMITTEE_TYPE.getCollectSource().equals(collectSource)
                    || MeetingCollectSourceEnum.CITY_GROUPLEADER_TYPE.getCollectSource().equals(collectSource) ){
                //市职部门 只需精确到市层级
                if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)
                        && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)
                        && !ConstantValue.STREET_ORG_LEVEL.equals(regionChiefLevel)
                        && !ConstantValue.DISTRICT_ORG_LEVEL.equals(regionChiefLevel)
                ){
                    throw new ZhsqEventException("地域编码【" + regionCode + "】至少需要精确至【南安市】层级！");
                }
            }


        return flag;
    }
    
}
