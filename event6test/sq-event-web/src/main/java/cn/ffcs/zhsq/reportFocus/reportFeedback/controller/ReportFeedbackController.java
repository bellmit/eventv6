package cn.ffcs.zhsq.reportFocus.reportFeedback.controller;

import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.reportFocus.reportFeedback.service.impl.ReportFeedbackServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import cn.ffcs.zhsq.reportFocus.twoViolationPre.controller.ReportTwoVioPreController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/zhsq/reportFeedback")
public class ReportFeedbackController extends ReportTwoVioPreController {

    @Autowired
    IReportFeedbackService reportFeedbackService;


    /**
     * 新增页面
     * @param session
     * @return
     *//*
    @RequestMapping("/toAdd")
    public String toAdd(HttpSession session,
                        @RequestParam(value = "seUUId", required=false) String seUUId,
                        @RequestParam Map<String, Object> params,
                        ModelMap map){
        return "/zzgl/reportFocus/reportFeedback/add_reportSend.ftl";
    }*/

    /**
     * 新增和编辑反馈信息
     * @param session
     * @param reportFeedBack
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResultObj saveOrUpdateReportFeedback(HttpSession session, @RequestBody Map<String, Object> reportFeedBack) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj result;
        try {
            String seUUId = reportFeedbackService.saveOrUpdateReport(reportFeedBack, userInfo);
            result = Msg.OPERATE.getResult(seUUId!=null, seUUId);
        } catch (ZhsqEventException e) {
            result = Msg.OPERATE.getResult(false, e.getMessage(), null);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 跳转列表页面
     * @param session
     * @param params
     * @param map
     * @return
     */
    @RequestMapping(value = "/toListPage")
    public String toListPage(HttpSession session,
                             @RequestParam Map<String, Object> params,ModelMap map) {
        map.putAll(params);
        map.addAttribute("bizTypeMap", ReportFeedbackServiceImpl.BIZTYPE_MAP);
        return  "/zzgl/reportFocus/reportFeedback/list_report_feedback.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/listData",method = RequestMethod.POST)
    public EUDGPagination listData(HttpSession session,int page, int rows,@RequestParam Map<String, Object> params){
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        if (CommonFunctions.isBlank(params,"regionCode")) {
            params.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        }
        if (CommonFunctions.isNotBlank(params,"listType") && "1".equals(params.get("listType"))) {
            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
            params.put("fbUserId", userInfo.getUserId());
            params.put("fbOrgId", userInfo.getOrgId());
        }
        return reportFeedbackService.findReportFeedbackDataPage(page, rows, params);
    }

    @ResponseBody
    @RequestMapping(value = "/listFeedbackDataByBizSign",method = RequestMethod.POST)
    public Object listFeedbackDataByBizSign(HttpSession session,@RequestParam(value = "bizSign") String bizSign,@RequestParam(value = "bizType") String bizType){
        if (StringUtils.isBlank(bizSign) || StringUtils.isBlank(bizType)) {
            return null;
        }
        return reportFeedbackService.searchReportFeedbackList(bizSign,bizType,null);
    }

    /**
     * 跳转反馈详情页面
     * @param session
     * @param fbUUId
     * @param fromPage 从列表页面【listPage】过来
     * @param doCheck 签收标记，从【我的反馈】带过来
     * @param map
     * @return
     */
    @RequestMapping(value = "/toFeedbackDetail")
    public String toFeedbackDetail(HttpSession session,
                           @RequestParam(value = "fbUUId") String fbUUId,
                           @RequestParam(value = "fromPage",required = false) String fromPage,
                           @RequestParam(value = "doCheck",required = false) Boolean doCheck,
                           ModelMap map) throws Exception{
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        if("listPage".equals(fromPage)){
            map.put("fromPage",fromPage);
            if (doCheck != null && doCheck) {
                reportFeedbackService.modifyReStatus(fbUUId,userInfo);//签收状态设置
            }
        }
        Map<String, Object> reportFeedbackMap= reportFeedbackService.findReportFeedbackByfbUUId(fbUUId);
        if (!CollectionUtils.isEmpty(reportFeedbackMap)) {
            Object reStatus = reportFeedbackMap.get("reStatus");
            boolean isMine = userInfo.getUserId().equals(Long.valueOf(reportFeedbackMap.get("fbUserId").toString()))
                    && userInfo.getOrgId().equals(Long.valueOf(reportFeedbackMap.get("fbOrgId").toString()));
            map.addAttribute("canDoFeedBack",
                    isMine && (IReportFeedbackService.ReportReadStatus.RECEIVED.getCode().equals(reStatus)
                                ||IReportFeedbackService.ReportReadStatus.TIMEOUT_RECEIVED.getCode().equals(reStatus))
                                && IReportFeedbackService.ReportFeedbackStatus.NOT_FEEDBACK.getCode().equals(reportFeedbackMap.get("fbStatus"))
                            && "listPage".equals(fromPage));
            map.addAllAttributes(reportFeedbackMap);
            if (CommonFunctions.isNotBlank(reportFeedbackMap, "seUUId")) {
                Long extTypeCount  = reportFeedbackService.findReportSendExtCount(reportFeedbackMap.get("seUUId").toString(),IReportFeedbackService.ReportSendExtType.EXT_TYPE.getCode(),null);
                if (extTypeCount!=null && extTypeCount>0) {
                    map.addAttribute("extType",IReportFeedbackService.ReportSendExtType.EXT_TYPE.getCode());
                    map.addAttribute("extTypeCount",extTypeCount);
                }
                Long remindTypeCount = reportFeedbackService.findReportSendExtCount(reportFeedbackMap.get("seUUId").toString(),IReportFeedbackService.ReportSendExtType.REMIND_TYPE.getCode(),null);
                if (remindTypeCount!=null && remindTypeCount>0) {
                    map.addAttribute("remindType",IReportFeedbackService.ReportSendExtType.REMIND_TYPE.getCode());
                    map.addAttribute("remindTypeCount",remindTypeCount);
                }
            }
        }
        return "/zzgl/reportFocus/reportFeedback/detail_report_feedback.ftl";
    }

    /**
     * 跳转补充信息列表或催单记录列表
     * @param session
     * @param seUUId    下达信息UUId
     * @param extType	补充信息类型
     * @param map
     * @return
     */
    @RequestMapping(value = "/toExtRecordList")
    public String toExtRecordList(HttpSession session,
                                  @RequestParam(value = "seUUId") String seUUId,
                                  @RequestParam(value = "extType") String extType,
                                 ModelMap map) {
        map.addAttribute("seUUId", seUUId);
        map.addAttribute("extType", extType);
        return "/zzgl/reportFocus/reportFeedback/list_report_send_ext.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/listExtData",method = RequestMethod.POST)
    public Object listExtData(HttpSession session,
                                              @RequestParam(value = "seUUId") String seUUId,
                                              @RequestParam(value = "extType") String extType){
        if (StringUtils.isBlank(seUUId) || StringUtils.isBlank(extType)) {
            return null;
        }
        return reportFeedbackService.searchReportSendExtList(seUUId,extType,null);
    }

    /**
     * 提交反馈内容并设置反馈状态
     * @param session
     * @param fbUUId 反馈信息uuid
     * @param fbContent 反馈内容
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/doFeedback", method = RequestMethod.POST)
    public ResultObj doFeedback(HttpSession session, @RequestParam("fbUUId") String fbUUId, @RequestParam("fbContent") String fbContent ) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        ResultObj result;
        try {
            boolean isSuccess = reportFeedbackService.modifyFbStatus(fbUUId, fbContent,userInfo);
            result = Msg.OPERATE.getResult(isSuccess);
        } catch (ZhsqEventException e) {
            result = Msg.OPERATE.getResult(false, e.getMessage(), null);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 跳转事件关联的信息反馈列表
     * @param session
     * @param bizSign  bizType为01时，表示事件id；为02时，表示两违reportUUID
     * @param bizType  业务类型，01 事件；02 两违；
     * @param map
     * @return
     */
    @RequestMapping(value = "/toListFeedback")
    public String toListFeedback(HttpSession session,
                                         @RequestParam(value = "bizSign") String bizSign,
                                         @RequestParam(value = "bizType") String bizType,
                                         ModelMap map) {
        map.addAttribute("bizSign", bizSign);
        map.addAttribute("bizType", bizType);
        return "/zzgl/reportFocus/reportFeedback/list_report_feedback_rel.ftl";
    }

    /**
     *  测试接口
     * @param session
     * @param map
     * @param reportFeedBack
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/doTest")
    public Object doTest(HttpSession session, ModelMap map, @RequestBody(required = false) Map<String, Object> reportFeedBack) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String choose = null;
        if (CommonFunctions.isNotBlank(reportFeedBack,"choose")) {
            choose = reportFeedBack.get("choose").toString();
        }
        if("1".equals(choose)) {
            //根据seUUid查询下达信息和反馈信息
            String seUUId = "B3FAD11A614129B8E0530100007F580E";
            if (CommonFunctions.isNotBlank(reportFeedBack,"seUUId")) {
                seUUId = reportFeedBack.get("seUUId").toString();
            }
            return reportFeedbackService.searchReportDataBySeUUId(seUUId);
        }else if("2".equals(choose)){
            //反馈信息分页列表加载
            int pageNo = 1;
            int pageSize = 20;
            return reportFeedbackService.findReportFeedbackDataPage(pageNo, pageSize, reportFeedBack);
        }else if("3".equals(choose)){
            //根据fbUUId获取反馈信息
            String fbUUId = "B4455BE1C10D4518E0530100007FD01A";
            if (CommonFunctions.isNotBlank(reportFeedBack,"fbUUId")) {
                fbUUId = reportFeedBack.get("fbUUId").toString();
            }
            return reportFeedbackService.findReportFeedbackByfbUUId(fbUUId);
        }else if("4".equals(choose)){
            //根据事件或两违的id和业务类型获取反馈信息列表
            String bizSign = "454164"; //B3D11DDED62D29ABE0530100007FE05B
            String bizType = "01";
            if (CommonFunctions.isNotBlank(reportFeedBack,"bizSign")) {
                bizSign = reportFeedBack.get("bizSign").toString();
            }
            if (CommonFunctions.isNotBlank(reportFeedBack,"bizType")) {
                bizType = reportFeedBack.get("bizType").toString();
            }
           // Map<String, Object> extraParam = new HashMap<>();
            return reportFeedbackService.searchReportFeedbackList(bizSign,bizType,reportFeedBack);
        }else if("5".equals(choose)){
            //签收状态设置
            String fbUUid="B4455BE1C10D4518E0530100007FD01A";
            if (CommonFunctions.isNotBlank(reportFeedBack,"fbUUid")) {
                fbUUid = reportFeedBack.get("fbUUid").toString();
            }
            return reportFeedbackService.modifyReStatus(fbUUid, userInfo);
        }else if("6".equals(choose)){
            //反馈状态设置
            String fbUUid="B4455BE1C10D4518E0530100007FD01A";
            String fbContent="test123456";
            if (CommonFunctions.isNotBlank(reportFeedBack,"fbUUid")) {
                fbUUid = reportFeedBack.get("fbUUid").toString();
            }
            if (CommonFunctions.isNotBlank(reportFeedBack,"fbContent")) {
                fbContent = reportFeedBack.get("fbContent").toString();
            }
            return reportFeedbackService.modifyFbStatus(fbUUid, fbContent,userInfo);
        }else if("7".equals(choose)){
            //新增和编辑下达信息和反馈信息
            return reportFeedbackService.saveOrUpdateReport(reportFeedBack,userInfo);
        }else if("8".equals(choose)){
            //下达信息分页列表加载
            int pageNo = 1;
            int pageSize = 20;
            return reportFeedbackService.findReportDataPage(pageNo,pageSize,reportFeedBack);
        }else if("9".equals(choose)){
            //查询事件关联的反馈信息数量
            String bizSign ="454164",bizType ="01";
            if (CommonFunctions.isNotBlank(reportFeedBack,"bizSign")) {
                bizSign = reportFeedBack.get("bizSign").toString();
            }
            if (CommonFunctions.isNotBlank(reportFeedBack,"bizType")) {
                bizType = reportFeedBack.get("bizType").toString();
            }
            return reportFeedbackService.findReportFeedbackCount(bizSign,bizType, reportFeedBack);
        }else if("10".equals(choose)){
            return reportFeedbackService.findSeUUIdByParam(reportFeedBack);
        }else if("11".equals(choose)){
            return reportFeedbackService.saveOrUpdateReportSend(reportFeedBack,userInfo);
        }else if("12".equals(choose)){
            return reportFeedbackService.saveOrUpdateReportFeedback(reportFeedBack,userInfo);
        }
        return null;
    }
}
