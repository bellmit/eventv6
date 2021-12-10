package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.controller;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.EPCDataSourceEnum;
import cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl.FocusReportNode355Enum;
import cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl.MeetingCollectSourceEnum;
import cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl.MeetingDictEnum;
import cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl.MettingTypeEnum;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:三会一课
 * @Author: zhangtc
 * @Date: 2020/12/4 16:20
 */
@Controller
@RequestMapping("/zhsq/reportMeeting")
public class ReportMeetingController extends ReportTwoVioPreController {
    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    private static Map<String, String[]> MEETING_TYPE = new HashMap<String, String[]>() {
        {
            // key:采集来源 value：可以采集的会议类型
            //村（社区）组织委员（一级网格员）
            put("01", new String[] {MettingTypeEnum.PARTY_MEMBER_MEETING.getDictCode(),
                                    MettingTypeEnum.BRANCH_COMMITTEE_MEETING.getDictCode(),
                                    MettingTypeEnum.PARTY_CLASS_MEETING.getDictCode(),
                                    MettingTypeEnum.PARTY_THEME_ACTIVITIES.getDictCode(),
                                    MettingTypeEnum.PARTY_LIFE_MEETING.getDictCode()}
                                    );
            //村（社区）党小组组长（二级网格长）
            put("02", new String[] {MettingTypeEnum.PARTY_GROUP_MEETING.getDictCode()});
            //镇直单位党组织组织委员
            put("03", new String[] {MettingTypeEnum.PARTY_MEMBER_MEETING.getDictCode(),
                                    MettingTypeEnum.BRANCH_COMMITTEE_MEETING.getDictCode(),
                                    MettingTypeEnum.PARTY_CLASS_MEETING.getDictCode(),
                                    MettingTypeEnum.PARTY_THEME_ACTIVITIES.getDictCode(),
                                    MettingTypeEnum.PARTY_LIFE_MEETING.getDictCode()}
                                    );
            //镇直单位各党小组组长
            put("04", new String[] {MettingTypeEnum.PARTY_GROUP_MEETING.getDictCode()});
            //市直单位党组织组织委员
            put("05", new String[] {MettingTypeEnum.PARTY_MEMBER_MEETING.getDictCode(),
                    MettingTypeEnum.BRANCH_COMMITTEE_MEETING.getDictCode(),
                    MettingTypeEnum.PARTY_CLASS_MEETING.getDictCode(),
                    MettingTypeEnum.PARTY_THEME_ACTIVITIES.getDictCode(),
                    MettingTypeEnum.PARTY_LIFE_MEETING.getDictCode()});
            //市直单位各党小组组长
            put("06", new String[] {MettingTypeEnum.PARTY_GROUP_MEETING.getDictCode()});
        }
    };

    /**
     * 跳转三课一会列表
     * @param session
     * @param listType		列表类型
     * 			1 草稿
     * 			2 待办
     * 			3 经办
     * 			4 我发起的
     * 			5 辖区所有
     * @param reportType	上报类型
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toList")
    public String toList(HttpSession session,
                         @RequestParam(value = "listType") Integer listType,
                         @RequestParam(value = "reportType", required=false, defaultValue="6") String reportType,
                         @RequestParam Map<String, Object> params,
                         ModelMap map) {
        super.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/meetingsAndLesson/list_meeting.ftl";
    }

    /**
     * 跳转三会一课阅办列表
     * @param session
     * @param listType		列表类型
     * @param reportType	上报类型
     * @param params
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toMsgReadingList")
    public String toMsgReadingList(HttpSession session,
                                   @RequestParam(value = "listType") Integer listType,
                                   @RequestParam(value = "reportType", required=false, defaultValue="6") String reportType,
                                   @RequestParam Map<String, Object> params,
                                   ModelMap map) {
        this.toList(session, listType, reportType, params, map);

        return "/zzgl/reportFocus/meetingsAndLesson/list_meeting_msgReading.ftl";
    }

    /**
     * 跳转三会一课新增/编辑页面
     * @param session
     * @param reportUUID	上报UUID
     * @param reportType	上报类型
     * @param params		额外参数
     * @param map
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/toAdd")
    public String toAdd(HttpSession session,
                        @RequestParam(value = "reportUUID", required=false) String reportUUID,
                        @RequestParam(value = "listType") Integer listType,
                        @RequestParam(value = "reportType", required=false, defaultValue="6") String reportType,
                        @RequestParam Map<String, Object> params,
                        ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

        String collectSource = MeetingCollectSourceEnum.GRID_REPORT.getCollectSource(), meetingTypeDictCodes = null;
        String[] initMeetingType = null;

        super.toAdd(session, reportUUID, listType, reportType, params, map);

        if(null != map.get("reportFocus")) {
            Map<String,Object> reportFocus = (Map<String, Object>) map.get("reportFocus");

            if(CommonFunctions.isNotBlank(reportFocus,"collectSource")){
                collectSource = reportFocus.get("collectSource").toString();
            }
        }

        if(CommonFunctions.isNotBlank(MEETING_TYPE, collectSource)) {
            initMeetingType = MEETING_TYPE.get(collectSource);
        }

        if(initMeetingType != null) {
            Map<String, Object> dictParams = new HashMap<String, Object>();
            List<BaseDataDict> meetingTypeDictList = null;

            dictParams.put("orgCode", userInfo.getOrgCode());
            dictParams.put("dictPcode", MeetingDictEnum.MEETING_TYPE.getDictCode());
            dictParams.put("dictGeneralCodeList", initMeetingType);

            meetingTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(dictParams);

            if(meetingTypeDictList != null) {
                StringBuffer meetingTypeDictBuffer = new StringBuffer("");

                for(BaseDataDict meetingTypeDict : meetingTypeDictList) {
                    meetingTypeDictBuffer.append(",").append(meetingTypeDict.getDictCode());
                }

                if(meetingTypeDictBuffer.length() > 0) {
                    meetingTypeDictCodes = "'" + meetingTypeDictBuffer.substring(1).replaceAll(",", "','") + "'";
                }
            }
        }

        map.addAttribute("meetingTypeDictCodes", meetingTypeDictCodes);

        return "/zzgl/reportFocus/meetingsAndLesson/add_meeting.ftl";
    }

    /**
     * 跳转三会一课详情页面
     * @param session
     * @param reportUUID	上报UUID
     * @param reportType	上报类型
     * @param params		额外参数
     * 			instanceId	流程实例id
     * @param map
     * @return
     */
    @Override
    @RequestMapping(value = "/toDetail")
    public String toDetail(HttpSession session,
                           @RequestParam(value = "reportUUID") String reportUUID,
                           @RequestParam(value = "listType") Integer listType,
                           @RequestParam(value = "reportType", required=false, defaultValue="6") String reportType,
                           @RequestParam Map<String, Object> params,
                           ModelMap map) {
    	boolean isEditable = false;
    	String forwardPath = "/zzgl/reportFocus/meetingsAndLesson/detail_meeting.ftl";
    	ModelMap mapDetail = new ModelMap();
    	
    	mapDetail.putAll(map);
    	
    	super.toDetail(session, reportUUID, listType, reportType, params, mapDetail);
    	
    	if(CommonFunctions.isNotBlank(mapDetail,"isEditable")) {
    		isEditable = Boolean.valueOf(String.valueOf(mapDetail.get("isEditable")));
    	}
    	
    	if(isEditable) {
    		toAdd(session, reportUUID, listType, reportType, params, map);
    	}
    	
    	map.putAll(mapDetail);
    	
        map.addAttribute("formType", FocusReportNode355Enum.FORM_TYPE.toString());

        if(isEditable){
        	forwardPath = "/zzgl/reportFocus/meetingsAndLesson/detail_meeting_editable.ftl";
		}
        
        return forwardPath;
    }

    @Override
    protected boolean isFrom12345(Object dataSource) {
        return EPCDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource);
    }

    @Override
    protected String getBizType() {
        return IReportFeedbackService.BizType.EPIDEMIC_PREVENTION_CONTROL.getCode();
    }
}
