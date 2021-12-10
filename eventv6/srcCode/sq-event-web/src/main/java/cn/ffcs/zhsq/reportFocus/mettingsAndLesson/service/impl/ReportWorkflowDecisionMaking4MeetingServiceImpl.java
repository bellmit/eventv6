package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2020/12/4 11:17
 */
@Service(value = "reportWorkflowDecisionMaking4MeetinService")
public class ReportWorkflowDecisionMaking4MeetingServiceImpl implements IWorkflowDecisionMakingService<String> {
    @Autowired
    private IReportFocusService reportFocusService;

    @Override
    public String makeDecision(Map<String, Object> params) throws Exception {
        String curNodeName = null, nextNodeName = null, collectSource = null;
        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isNotBlank(params, "curNodeName")) {
            curNodeName = params.get("curNodeName").toString();
        }

        if(CommonFunctions.isNotBlank(params, "collectSource")) {
            collectSource = params.get("collectSource").toString();
        } else if(CommonFunctions.isNotBlank(params, "reportId") || CommonFunctions.isNotBlank(params, "reportUUID")) {
            Map<String, Object> reportMap = null;

            if(CommonFunctions.isBlank(params, "reportType")) {
                params.put("reportType", ReportTypeEnum.epidemicPreControl.getReportTypeIndex());
            }

            reportMap = reportFocusService.findReportFocusByUUID(null, null, params);

            if(CommonFunctions.isNotBlank(reportMap, "collectSource")) {
                collectSource = reportMap.get("collectSource").toString();
            }
        }

        if(FocusReportNode355Enum.COLLECT_MEETING.getTaskNodeName().equals(curNodeName)) {
            //一级网格员上报
            if(MeetingCollectSourceEnum.COMMUNITY_REPORT.getCollectSource().equals(collectSource)) {
                nextNodeName = FocusReportNode355Enum.COMMUNITY_REPORT.getTaskNodeName();
            } else if(MeetingCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource)){
                //二级网格员上报
                nextNodeName = FocusReportNode355Enum.GRID_REPORT.getTaskNodeName();
            }else if(MeetingCollectSourceEnum.STREET_COMMITTEE_REPORT.getCollectSource().equals(collectSource)){
                //镇直单位党组织组织委员上报
                nextNodeName = FocusReportNode355Enum.STREET_COMMITTEE_REPORT.getTaskNodeName();
            }else if(MeetingCollectSourceEnum.STREET_GROUPLEADER_REPORT.getCollectSource().equals(collectSource)){
                //镇直单位党组织党小组组长上报
                nextNodeName = FocusReportNode355Enum.STREET_GROUPLEADER_REPORT.getTaskNodeName();
            }else if(MeetingCollectSourceEnum.CITY_COMMITTEE_TYPE.getCollectSource().equals(collectSource)){
                //市直单位党组织组织委员
                nextNodeName = FocusReportNode355Enum.CITY_COMMITTEE_TYPE.getTaskNodeName();
            }else if(MeetingCollectSourceEnum.CITY_GROUPLEADER_TYPE.getCollectSource().equals(collectSource)){
                //市直单位各党小组组长
                nextNodeName = FocusReportNode355Enum.CITY_GROUPLEADER_TYPE.getTaskNodeName();
            }
        }

        if(StringUtils.isBlank(nextNodeName)) {
            throw new Exception("没有可用的下一环节！");
        }

        return nextNodeName;
    }

}
