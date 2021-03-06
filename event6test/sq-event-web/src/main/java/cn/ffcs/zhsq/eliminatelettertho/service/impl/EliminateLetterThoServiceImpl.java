package cn.ffcs.zhsq.eliminatelettertho.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.system.publicUtil.JSONUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterIndusService;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoReChgService;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoService;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoBizStatusEnum;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoEnum;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoProfessionalTypeEnum;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterIndus;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterTho;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterThoReChg;
import cn.ffcs.zhsq.mybatis.persistence.eliminatelettertho.EliminateLetterThoMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: ????????????????????????????????????
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 ????????????
 */
@Service("eliminateLetterThoServiceImpl")
@Transactional
public class EliminateLetterThoServiceImpl implements IEliminateLetterThoService {

    private static final Logger logger = LoggerFactory.getLogger(EliminateLetterThoServiceImpl.class);

    @Autowired
    private EliminateLetterThoMapper eliminateLetterThoMapper;

    @Autowired
    private IEliminateLetterIndusService eliminateLetterIndusService;

    @Autowired
    private IEliminateLetterThoReChgService eliminateLetterThoReChgService;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    @Autowired
    private IWorkflow4BaseService workflow4BaseService;

    @Autowired
    private INumberConfigureService numberConfigureService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private UserInfoOutService userInfoOutService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoOutService;

    @Override
    @Transactional
    public Long startWorkFlow(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        //???????????????
        this.saveRaft(bo, userInfo);

        //????????????
        this.start(bo, userInfo);

        return bo.getThoId();

    }

    @Override
    @Transactional
    public Long commitWorkFlow(EliminateLetterTho bo, UserInfo userInfo, String type) throws Exception {

        //????????????
        EliminateLetterThoProfessionalTypeEnum professionalType = getProfessionalType(type);

        //????????????
        Map<String, Object> result = this.validateFeedback(professionalType, bo, userInfo);

        //???????????????????????????
        this.updateFeedback(professionalType, bo, userInfo);

        //?????????????????????
        this.subNextStep(
                (Long)result.get("thoId"),
                (String)result.get("advice"),
                userInfo,
                Arrays.asList((UserInfo)result.get("nextUserInfo")),
                (String)result.get("nextNodeName")
        );

        //????????????
        this.alterBizStatus(
                (Long)result.get("thoId"),
                (Long)result.get("instanceId"),
                (String)result.get("curNodeName"),
                (String)result.get("nextNodeName"),
                userInfo,
                true
        );

        return bo.getThoId();

    }

    @Override
    public Long reject(EliminateLetterTho bo, UserInfo userInfo, String type) throws Exception {

        //????????????
        EliminateLetterThoProfessionalTypeEnum professionalType = getProfessionalType(type);

        //????????????
        Map<String, Object> result = this.validateReject(professionalType, bo, userInfo);

        //???????????????????????????
        this.updateReject(professionalType, bo, userInfo);

        //????????????
        this.alterBizStatus(
                (Long)result.get("thoId"),
                (Long)result.get("instanceId"),
                (String)result.get("curNodeName"),
                (String)result.get("nextNodeName"),
                userInfo,
                false
        );

        //?????????????????????
        this.subNextStep(
                (Long)result.get("thoId"),
                (String)result.get("advice"),
                userInfo,
                Arrays.asList((UserInfo)result.get("nextUserInfo")),
                (String)result.get("nextNodeName")
        );

        return bo.getThoId();

    }

    /**
     * ???????????????????????????
     * @param instanceId
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> queryAllLinks(Long instanceId) throws Exception {

        List<Map<String, Object>> tempList = new ArrayList<>();

        if(instanceId == null) {
            return tempList;
        }

        //????????????????????????
        Map<String, Object> curMap = baseWorkflowService.findCurTaskData(instanceId);
        ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);

        //??????????????????
        if(proInstance != null && "2".equals(proInstance.getStatus())) {
            curMap = new HashMap<>();
            curMap.put("TASK_NAME", "??????");
            curMap.put("TASK_CODE", "end");
            curMap.put("ORG_NAME", "");
            curMap.put("TRANSACTOR_NAME", "");
        } else {
            curMap.put("TASK_NAME", curMap.get("WF_ACTIVITY_NAME_"));
            curMap.put("TASK_CODE", curMap.get("NODE_NAME"));
            curMap.put("ORG_NAME", curMap.get("WF_ORGNAME"));
            curMap.put("TRANSACTOR_NAME", curMap.get("WF_USERNAME"));
        }

        //??????????????????
        if(curMap != null) {
            tempList.add(0, curMap);
        }

        //??????????????????
        List<Map<String, Object>> allLinks = baseWorkflowService.capHandledTaskInfoMap(instanceId, "DESC", new HashMap<>());

        if(null != allLinks && allLinks.size() > 0){
            tempList.addAll(allLinks);
        }

        return tempList;

    }

    private Map<String, Object> validateReject(EliminateLetterThoProfessionalTypeEnum professionalType, EliminateLetterTho bo, UserInfo userInfo) {

        Long thoId = null;
        Long instanceId = null;
        String curNodeName = null;
        String nextNodeName = null;
        UserInfo nextUserInfo = null;
        String advice = null;

        Assert.notNull(professionalType, "??????[professionalType]");

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getThoUuid(), "??????[thoUuid]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "????????????[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());
        bo.setInstanceId(eliminateLetterTho.getInstanceId());
        thoId = bo.getThoId();
        instanceId = bo.getInstanceId();
        advice = bo.getAdvice();

        EliminateLetterThoReChg reChgs = bo.getReChgs();

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            //????????????

            curNodeName = EliminateLetterThoEnum.START_DEPT_AUDIT.getCode();
            nextNodeName = EliminateLetterThoEnum.INDUSTRY_FEEDBACK.getCode();

            EliminateLetterThoReChg eliminateLetterThoReChg = eliminateLetterThoReChgService.searchByThoId(bo.getThoId());

            nextUserInfo = JSON.parseArray(eliminateLetterThoReChg.getReUser(), UserInfo.class).get(0);
            Assert.notNull(nextUserInfo, "??????[nextUserInfo]");

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE) {

            if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.COUNTY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 6) {
                curNodeName = EliminateLetterThoEnum.COUNTY_AUDIT.getCode();
            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.CITY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 4) {
                curNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();
            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.PROVINCE_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 2) {
                curNodeName = EliminateLetterThoEnum.PROVINCE_AUDIT.getCode();
            }

            nextNodeName = EliminateLetterThoEnum.FAIL_END.getCode();
            nextUserInfo = userInfo;

        }

        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

        Map<String, Object> result = new HashMap<>();
        result.put("thoId", thoId);
        result.put("instanceId", instanceId);
        result.put("curNodeName", curNodeName);
        result.put("nextNodeName", nextNodeName);
        result.put("nextUserInfo", nextUserInfo);
        result.put("advice", advice);

        return result;

    }

    private void updateReject(EliminateLetterThoProfessionalTypeEnum professionalType, EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            EliminateLetterThoReChg reChgs = bo.getReChgs();
            reChgs.setThoId(bo.getThoId());
            reChgs.setUpdator(userInfo.getUserId());

            //??????????????????
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());
            bo.setUpdator(userInfo.getUserId());

            eliminateLetterThoMapper.update(et);

            //????????????????????????
            eliminateLetterThoReChgService.updateIssuingUnitFeedbackByThoId(reChgs);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE) {

            //????????????

        } else {

            throw new RuntimeException("???????????????????????????");

        }

    }

    private EliminateLetterThoProfessionalTypeEnum getProfessionalType(String type) {
        if(type.equals(EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT.getCode())) {
            return EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT;
        } else if(type.equals(EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR.getCode())) {
            return EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR;
        } else if(type.equals(EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode())) {
            return EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE;
        }
        throw new RuntimeException("??????????????????");
    }

    private Map<String, Object> validateFeedback(EliminateLetterThoProfessionalTypeEnum professionalType, EliminateLetterTho bo, UserInfo userInfo) {

        String curNodeName = null;
        String nextNodeName = null;
        UserInfo nextUserInfo = null;
        String advice = null;

        Assert.notNull(professionalType, "??????[professionalType]");

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getThoUuid(), "??????[thoUuid]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "????????????[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());

        advice = bo.getAdvice();

        EliminateLetterThoReChg reChgs = bo.getReChgs();

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            //????????????

            curNodeName = EliminateLetterThoEnum.START_DEPT_AUDIT.getCode();

            if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 6) {
                // ???
                nextNodeName = EliminateLetterThoEnum.COUNTY_AUDIT.getCode();

            } else if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 4) {
                //???
                nextNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

            } else if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 2) {
                //???
                nextNodeName = EliminateLetterThoEnum.PROVINCE_AUDIT.getCode();

            }

            List<UserInfo> userInfos = JSONArray.parseArray(bo.getNextUserStr(), UserInfo.class);
            Assert.notEmpty(userInfos, "???????????????????????????");
            nextUserInfo = userInfos.get(0);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR) {

            //????????????
            Assert.hasLength(reChgs.getReDateStr(), "??????[????????????]");
            Assert.hasLength(reChgs.getReType(), "??????[????????????]");
            Assert.hasLength(reChgs.getReDetail(), "??????[????????????]");

            if(!StringUtil.isBlank(reChgs.getReDissentAgree())) {

                if(reChgs.getReDissentAgree().equals("2")) {
                    Assert.hasLength(reChgs.getChgType(), "??????[????????????]");
                    if(reChgs.getChgType().equals("1")) {
                        Assert.hasLength(reChgs.getChgDetail(), "??????[????????????]");
                    }
                    Assert.hasLength(reChgs.getIndusChgAgree(), "??????[????????????????????????]");
                    if(reChgs.getIndusChgAgree().equals("1")) {
                        Assert.hasLength(reChgs.getIndusChgDetail(), "??????[????????????????????????]");
                    }
                    Assert.hasLength(reChgs.getLongActionAgree(), "??????[????????????????????????]");
                    if(reChgs.getLongActionAgree().equals("1")) {
                        Assert.hasLength(reChgs.getLongActionDetail(), "??????[??????????????????]");
                    }
                    Assert.hasLength(reChgs.getOthClob(), "??????[??????????????????]");
                } else {
                    Assert.hasLength(reChgs.getReDissentDetail(), "??????[????????????]");

                    reChgs.setChgType(null);
                    reChgs.setChgDetail(null);
                    reChgs.setIndusChgAgree(null);
                    reChgs.setLongActionAgree(null);
                    reChgs.setLongActionDetail(null);
                    reChgs.setOthClob(null);

                }

            }

            curNodeName = EliminateLetterThoEnum.INDUSTRY_FEEDBACK.getCode();
            nextNodeName = EliminateLetterThoEnum.START_DEPT_AUDIT.getCode();

            nextUserInfo = userInfoOutService.findUserInfoByUserIdAndOrgCode(eliminateLetterTho.getCreator(), eliminateLetterTho.getOrgCode());
            Assert.notNull(nextUserInfo, "??????[nextUserInfo]");

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE) {

            //?????????

            if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.COUNTY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 6) {

                //????????????
                curNodeName = EliminateLetterThoEnum.COUNTY_AUDIT.getCode();

                //????????????
                nextNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

                List<UserInfo> userInfos = JSONArray.parseArray(bo.getNextUserStr(), UserInfo.class);
                Assert.notEmpty(userInfos, "???????????????????????????");
                nextUserInfo = userInfos.get(0);

            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.CITY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 4) {

                //????????????
                curNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

                //??????
                nextNodeName = EliminateLetterThoEnum.SUC_END.getCode();

                //?????????????????????
                nextUserInfo = userInfo;

            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.PROVINCE_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 2) {

                //????????????
                curNodeName = EliminateLetterThoEnum.PROVINCE_AUDIT.getCode();

                //??????
                nextNodeName = EliminateLetterThoEnum.SUC_END.getCode();

                //?????????????????????
                nextUserInfo = userInfo;

            } else {
                throw new RuntimeException("??????[flowStep:" + eliminateLetterTho.getFlowStep() + "]");
            }

        } else {

            throw new RuntimeException("??????????????????");

        }

        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

        Map<String, Object> result = new HashMap<>();
        result.put("nextUserInfo", nextUserInfo);
        result.put("thoId", bo.getThoId());
        result.put("instanceId", eliminateLetterTho.getInstanceId());
        result.put("curNodeName", curNodeName);
        result.put("nextNodeName", nextNodeName);
        result.put("advice", advice);

        return result;

    }

    private void updateFeedback(EliminateLetterThoProfessionalTypeEnum professionalType, EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            EliminateLetterThoReChg reChgs = bo.getReChgs();
            reChgs.setThoId(bo.getThoId());
            reChgs.setUpdator(userInfo.getUserId());

            //??????????????????
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN.getCode());
            eliminateLetterThoMapper.update(et);

            //????????????????????????
            eliminateLetterThoReChgService.updateIssuingUnitFeedbackByThoId(reChgs);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR) {

            EliminateLetterThoReChg reChgs = bo.getReChgs();
            reChgs.setThoId(bo.getThoId());
            reChgs.setUpdator(userInfo.getUserId());

            //??????????????????
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_ISSUING_UNIT.getCode());
            eliminateLetterThoMapper.update(et);

            //????????????????????????
            reChgs.setReDate(DateUtils.convertStringToDate(reChgs.getReDateStr(), DateUtils.PATTERN_24TIME));
            eliminateLetterThoReChgService.updateIndustrySectorfeedbackByThoId(reChgs);

        }

    }

    private void start(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        Map<String, Object> extraParam = new HashMap<String, Object>() {
            {
                put("userType", 3);
            }
        };

        Boolean success = workflow4BaseService.startWorkflow4Base(
                bo.getThoId(),
                ConstantValue.ELIMINATE_LETTER_THO_WORKFLOW_NAME,
                ConstantValue.ELIMINATE_LETTER_THO_WFTYPE_ID,
                userInfo,
                extraParam
        );

        if(!success) {
            throw new RuntimeException("??????????????????");
        }

        success = baseWorkflowService.subWorkflow4Base(
                ConstantValue.ELIMINATE_LETTER_THO_WORKFLOW_NAME,
                ConstantValue.ELIMINATE_LETTER_THO_WFTYPE_ID,
                bo.getThoId(),
                "task2",
                JSONArray.parseArray(bo.getReChgs().getReUser(), UserInfo.class),
                userInfo,
                extraParam
        );

        if(!success) {
            throw new RuntimeException("??????????????????");
        }

        this.alterBizStatus(
                bo.getThoId(),
                (Long)extraParam.get("instanceId"),
                "task1",
                "task2",
                userInfo,
                true
        );

    }

    private void subNextStep(Long thoId, String advice, UserInfo currentUserInfo, List<UserInfo> nextUserInfoList, String nextNodeName) throws Exception {

        Map<String, Object> extraParam = new HashMap<String, Object>() {
            {
                put("userType", 3);
                put("advice", advice);
            }
        };

        Boolean success = baseWorkflowService.subWorkflow4Base(
                ConstantValue.ELIMINATE_LETTER_THO_WORKFLOW_NAME,
                ConstantValue.ELIMINATE_LETTER_THO_WFTYPE_ID,
                thoId,
                nextNodeName,
                nextUserInfoList,
                currentUserInfo,
                extraParam
        );

        if(!success) {
            throw new RuntimeException("??????????????????");
        }

    }

    private void alterBizStatus(Long thoId, Long instanceId, String curNodeName, String nextNodeName, UserInfo userInfo, boolean isPass) throws Exception {

        String bizStatus = initStatusMap(isPass).get(curNodeName);

        Assert.hasLength(bizStatus, "????????????????????????");

        EliminateLetterTho bo = new EliminateLetterTho();
        bo.setThoId(thoId);
        bo.setInstanceId(instanceId);
        bo.setFlowStep(nextNodeName);
        bo.setBizStatus(bizStatus);
        bo.setUpdator(userInfo.getUserId());
        eliminateLetterThoMapper.update(bo);

    }

    private Map<String, String> initStatusMap(boolean isPass) {

        Map<String, String> statusMap = new HashMap<>();

        if(isPass) {    //??????

            //??????(???????????? ????????? ????????????)
            statusMap.put("task1", EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());

            //??????(???????????? ????????? ????????????)
            statusMap.put("task2", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_ISSUING_UNIT.getCode());

            //??????(???????????? ???????????? ????????? ????????????)
            statusMap.put("task3", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN.getCode());

            //??????(??????????????? ???????????? ????????? ???????????????)
            statusMap.put("task4", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_COUNTY_CRACK_DOWN.getCode());

            //??????(??????????????? ???????????? ??????)
            statusMap.put("task5", EliminateLetterThoBizStatusEnum.FILE.getCode());

            //??????(??????????????? ???????????? ??????)
            statusMap.put("task6", EliminateLetterThoBizStatusEnum.FILE.getCode());

        } else {    //??????

            //??????(???????????? ????????? ????????????)
            statusMap.put("task3", EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());

            //???????????????(??????????????????????????????)
            statusMap.put("task4", EliminateLetterThoBizStatusEnum.FAIL_TO_FINISH_COUNTY.getCode());

            //???????????????(??????????????????????????????)
            statusMap.put("task5", EliminateLetterThoBizStatusEnum.FAIL_TO_FINISH_CITY.getCode());

        }

        return statusMap;

    }

    /**
     * ????????????
     *
     * @param bo ??????????????????????????????
     * @param userInfo ??????????????????
     * @return ??????????????????id
     */
    @Override
    @Transactional
    public Long saveRaft(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        if(this.isInsertRaft(bo)) {

            // ????????????
            return this.insertRaft(bo, userInfo);

        } else {

            //????????????
            return this.updateRaft(bo, userInfo);

        }

    }

    private boolean isInsertRaft(EliminateLetterTho bo) {
        return bo == null || StringUtils.isBlank(bo.getThoUuid());
    }

    private Long updateRaft(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        //????????????
        Long thoId = this.preUpdateValidate(bo, userInfo);

        //??????????????????
        this.updateTho(bo, userInfo);

        //???????????????????????????
        this.updateIndusList(bo.getInduss(), thoId, userInfo);

        //???????????????????????????
        this.updateReChgs(bo.getReChgs(), thoId, userInfo);

        return thoId;

    }

    private Long preUpdateValidate(EliminateLetterTho bo, UserInfo userInfo) {

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getThoUuid(), "??????[thoUuid]");
        Assert.hasLength(bo.getThtNo(), "??????[thtNo]");
        Assert.hasLength(bo.getLetterType(), "??????[????????????]");
        Assert.hasLength(bo.getFbDepartCode(), "??????[??????????????????]");
        Assert.hasLength(bo.getFbDepartNameDet(), "??????[????????????????????????]");
        Assert.hasLength(bo.getFbDateStr(), "??????[????????????]");
        Assert.hasLength(bo.getLetterNo(), "??????[??????]");
        Assert.hasLength(bo.getCaseName(), "??????[????????????]");
        Assert.hasLength(bo.getCaseNo(), "??????[????????????]");
        Assert.hasLength(bo.getLetterContentClob(), "??????[????????????]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "????????????[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());

        if(!eliminateLetterTho.getBizStatus().equals(EliminateLetterThoBizStatusEnum.DRAFT.getCode())) {
            throw new RuntimeException("??????[bizStatus]["+bo.getBizStatus()+"]");
        }

        List<EliminateLetterIndus> induss = bo.getInduss(); //????????????
        Assert.notEmpty(induss, "??????[???????????????]");
        for(Iterator<EliminateLetterIndus> iterator = induss.iterator(); iterator.hasNext();) {
            EliminateLetterIndus indus = iterator.next();
            Assert.hasLength(indus.getIndustrialCode(), "??????[????????????CODE]");
            if("10".equals(indus.getIndustrialCode())) { //???????????????"??????"
                Assert.hasLength(bo.getIndustrialComment(), "??????[????????????????????????]");
            }
        }

        EliminateLetterThoReChg reChgs = bo.getReChgs(); //?????????????????????
        Assert.notNull(reChgs, "??????[?????????????????????]");
        Assert.hasLength(reChgs.getReDepartCode(), "??????[?????????????????????]");
        Assert.hasLength(reChgs.getReDepartNameDet(), "??????[????????????????????????]");
        Assert.notNull(reChgs.getReUserId(), "??????[????????????ID]");
        Assert.hasLength(reChgs.getReUserName(), "??????[??????????????????]");

        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

        return eliminateLetterTho.getThoId();

    }

    private Long updateTho(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        bo.setBizStatus(EliminateLetterThoBizStatusEnum.DRAFT.getCode());
        bo.setUpdator(userInfo.getUserId());
        bo.setFbDate(DateUtils.convertStringToDate(bo.getFbDateStr(), DateUtils.PATTERN_24TIME));

        eliminateLetterThoMapper.updateBlankByThoUuid(bo);

        return bo.getThoId();

    }

    private void updateIndusList(List<EliminateLetterIndus> induss, Long thoId, UserInfo userInfo) throws Exception {

        EliminateLetterIndus bo = new EliminateLetterIndus();
        bo.setThoId(thoId);
        bo.setUpdator(userInfo.getUserId());
        eliminateLetterIndusService.invalidByThoId(bo);

        for(int i=0; i<induss.size(); i++) {
            EliminateLetterIndus item = induss.get(i);
            item.setThoId(thoId);
            item.setIndusUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            item.setOrgCode(userInfo.getOrgCode());
            item.setCreator(userInfo.getUserId());
            eliminateLetterIndusService.insert(item);
        }

    }

    private void updateReChgs(EliminateLetterThoReChg reChgs, Long thoId, UserInfo userInfo) throws Exception {

        reChgs.setThoId(thoId);
        reChgs.setReDate(DateUtils.convertStringToDate(reChgs.getReDateStr(), DateUtils.PATTERN_24TIME));
        reChgs.setReDepartCodeSub(reChgs.getReDepartCode().substring(reChgs.getReDepartCode().length() - 2));
        reChgs.setRegionCode(userInfo.getInfoOrgList().get(0).getOrgCode());
        reChgs.setOrgCode(userInfo.getOrgCode());
        reChgs.setCreator(userInfo.getUserId());
        eliminateLetterThoReChgService.updateBlankByThoId(reChgs);

    }

    private Long insertRaft(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        //????????????
        preInsertValidate(bo, userInfo);

        //??????????????????
        Long thoId = insertTho(bo, userInfo);

        //???????????????????????????
        insertIndusList(bo.getInduss(), thoId, userInfo);

        //???????????????????????????
        insertReChgs(bo.getReChgs(), thoId, userInfo);

        return bo.getThoId();

    }

    private void preInsertValidate(EliminateLetterTho bo, UserInfo userInfo) {

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getLetterType(), "??????[????????????]");
        Assert.hasLength(bo.getFbDepartCode(), "??????[??????????????????]");
        Assert.hasLength(bo.getFbDepartNameDet(), "??????[????????????????????????]");
        Assert.hasLength(bo.getFbDateStr(), "??????[????????????]");
        Assert.hasLength(bo.getLetterNo(), "??????[??????]");
        Assert.hasLength(bo.getCaseName(), "??????[????????????]");
        Assert.hasLength(bo.getCaseNo(), "??????[????????????]");
        Assert.hasLength(bo.getLetterContentClob(), "??????[????????????]");

        List<EliminateLetterIndus> induss = bo.getInduss(); //????????????
        Assert.notEmpty(induss, "??????[???????????????]");
        for(Iterator<EliminateLetterIndus> iterator = induss.iterator(); iterator.hasNext();) {
            EliminateLetterIndus indus = iterator.next();
            Assert.hasLength(indus.getIndustrialCode(), "??????[????????????CODE]");
            if("10".equals(indus.getIndustrialCode())) { //???????????????"??????"
                Assert.hasLength(bo.getIndustrialComment(), "??????[????????????????????????]");
            }
        }

        EliminateLetterThoReChg reChgs = bo.getReChgs(); //?????????????????????
        Assert.notNull(reChgs, "??????[?????????????????????]");
        Assert.hasLength(reChgs.getReDepartCode(), "??????[?????????????????????]");
        Assert.hasLength(reChgs.getReDepartNameDet(), "??????[????????????????????????]");
        Assert.notNull(reChgs.getReUserId(), "??????[????????????ID]");
        Assert.hasLength(reChgs.getReUserName(), "??????[??????????????????]");

        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

    }

    private Long insertTho(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        String thtNo = numberConfigureService.getNumber(userInfo.getInfoOrgList().get(0).getOrgCode(), ConstantValue.ELIMINATE_LETTER_THO_SYS_CODE);
        thtNo = DateUtils.getToday("yyyyMMdd") + thtNo;

        bo.setThtNo(thtNo);
        bo.setThoUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        bo.setBizStatus(EliminateLetterThoBizStatusEnum.DRAFT.getCode());   //??????
        bo.setFlowStep(EliminateLetterThoBizStatusEnum.DRAFT.getCode());    //??????
        bo.setRegionCode(userInfo.getInfoOrgList().get(0).getOrgCode());
        bo.setOrgCode(userInfo.getOrgCode());
        bo.setCreator(userInfo.getUserId());
        bo.setFbDate(DateUtils.convertStringToDate(bo.getFbDateStr(), DateUtils.PATTERN_24TIME));

        eliminateLetterThoMapper.insert(bo);

        return bo.getThoId();

    }

    private void insertIndusList(List<EliminateLetterIndus> induss, Long thoId, UserInfo userInfo) throws Exception {

        for(int i=0; i<induss.size(); i++) {
            EliminateLetterIndus item = induss.get(i);
            item.setThoId(thoId);
            item.setIndusUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            item.setOrgCode(userInfo.getOrgCode());
            item.setCreator(userInfo.getUserId());
            eliminateLetterIndusService.insert(item);
        }

    }

    private void insertReChgs(EliminateLetterThoReChg reChgs, Long thoId, UserInfo userInfo) throws Exception {

        reChgs.setThoId(thoId);
        reChgs.setChgUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        reChgs.setReDate(DateUtils.convertStringToDate(reChgs.getReDateStr(), DateUtils.PATTERN_24TIME));
        reChgs.setReDepartCodeSub(reChgs.getReDepartCode().substring(reChgs.getReDepartCode().length() - 2));
        reChgs.setRegionCode(userInfo.getInfoOrgList().get(0).getOrgCode());
        reChgs.setOrgCode(userInfo.getOrgCode());
        reChgs.setCreator(userInfo.getUserId());
        eliminateLetterThoReChgService.insert(reChgs);

    }

    /**
     * ????????????
     *
     * @param bo ??????????????????????????????
     * @return ??????????????????
     */
    @Override
    public void update(EliminateLetterTho bo) throws Exception {
        long result = eliminateLetterThoMapper.update(bo);
        Assert.isTrue(result > 0, "????????????");
    }

    /**
     * ????????????
     *
     * @param bo ??????????????????????????????
     * @return ??????????????????
     */
    @Override
    @Transactional
    public void delete(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getThoUuid(), "??????[bo.thoUuid]");
        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

        EliminateLetterTho tho =
                eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());

        Assert.notNull(tho, "????????????[thoUuid]");
        Assert.hasLength(tho.getBizStatus(), "????????????[bizStatus]");
        //Assert.isTrue(EliminateLetterThoBizStatusEnum.DRAFT.getCode().equals(tho.getBizStatus()), "??????????????????,????????????");

        bo.setUpdator(userInfo.getUserId());
        long result = eliminateLetterThoMapper.delete(bo);
        Assert.isTrue(result > 0, "????????????");

        EliminateLetterThoReChg reChg = new EliminateLetterThoReChg();
        reChg.setThoId(tho.getThoId());
        reChg.setUpdator(userInfo.getUserId());
        boolean isSuccess = eliminateLetterThoReChgService.deleteByThoId(reChg);
        Assert.isTrue(isSuccess, "????????????[reChg]");

        EliminateLetterIndus indus = new EliminateLetterIndus();
        indus.setThoId(tho.getThoId());
        indus.setUpdator(userInfo.getUserId());
        isSuccess = eliminateLetterIndusService.deleteByThoId(indus);
        Assert.isTrue(isSuccess, "????????????[indus]");

    }

    @Override
    @Transactional
    public void deleteBatch(List<EliminateLetterTho> thos, UserInfo userInfo) throws Exception {

        Assert.notEmpty(thos, "??????[thos]");

        Assert.notNull(userInfo, "??????[userInfo]");
        Assert.notNull(userInfo.getUserId(), "??????[userInfo.userId]");

        for(EliminateLetterTho bo : thos) {

            this.delete(bo, userInfo);

        }

    }

    @Override
    public EliminateLetterTho searchByThoUuid(EliminateLetterTho bo) throws Exception {

        Assert.notNull(bo, "??????[bo]");
        Assert.hasLength(bo.getThoUuid(), "??????[bo.thoUuid]");

        bo = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());

        Assert.notNull(bo, "????????????thoUuid");

        List<EliminateLetterIndus> induses = eliminateLetterIndusService.searchByThoId(bo.getThoId());
        bo.setInduss(induses);

        EliminateLetterThoReChg reChg = eliminateLetterThoReChgService.searchByThoId(bo.getThoId());
        bo.setReChgs(reChg);

        return bo;

    }

    @Override
    public void detail(ModelMap map, EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        bo = this.searchByThoUuid(bo);

        EliminateLetterThoReChg reChgs = bo.getReChgs();

        List<EliminateLetterIndus> induss = bo.getInduss();

        //??????????????????
        map.addAttribute("thtNo", bo.getThtNo());

        //??????????????????
        map.addAttribute("letterTypeName",
                getDataDict(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, userInfo.getOrgCode(), bo.getLetterType()));

        //??????????????????
        map.addAttribute("fbDepartCode", bo.getFbDepartCode());

        //????????????????????????
        map.addAttribute("fbDepartNameDet", bo.getFbDepartNameDet());

        //???????????? ???
        map.addAttribute("fbDateYear", DateUtils.formatDate(bo.getFbDate(), "yyyy"));

        //???????????? ???
        map.addAttribute("fbDateMonth", DateUtils.formatDate(bo.getFbDate(), "MM"));

        //???????????? ???
        map.addAttribute("fbDateDate", DateUtils.formatDate(bo.getFbDate(), "dd"));

        //??????
        map.addAttribute("letterNo", bo.getLetterNo());

        //????????????
        map.addAttribute("caseName", bo.getCaseName());

        //????????????
        map.addAttribute("caseNo", bo.getCaseNo());

        //????????????
        map.addAttribute("letterContentClob", bo.getLetterContentClob());

        //????????????????????????
        map.addAttribute("industrialComment", bo.getIndustrialComment());

        //????????????
        JSONArray indusArray = new JSONArray();

        List<BaseDataDict> indus =
                baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS, userInfo.getOrgCode());

        int index = 0;
        for(Iterator<BaseDataDict> iterator = indus.iterator(); iterator.hasNext();) {

            BaseDataDict baseDataDict = iterator.next();

            JSONObject jsonItem = new JSONObject();
            jsonItem.put("name", baseDataDict.getDictName());

            boolean isChecked = false;
            if(induss != null && induss.size() > 0) {
                for(EliminateLetterIndus item : induss) {
                    if(item.getIndustrialCode().equals(baseDataDict.getDictGeneralCode())) {
                        isChecked = true;
                        break;
                    }
                }
            }

            jsonItem.put("isChecked", isChecked);
            jsonItem.put("index", index++);
            indusArray.add(jsonItem);

        }

        //???????????????
        map.addAttribute("indus", indusArray);

        if(reChgs != null) {

            //??????????????????
            Map<String, String> createRegion = searchRegion(bo.getFbDepartCode());

            //???????????????
            map.addAttribute("createProvince", createRegion.get("province"));

            //???????????????
            map.addAttribute("createCity", createRegion.get("city"));

            //???????????????
            map.addAttribute("createCounty", createRegion.get("county"));


            //??????????????????
            OrgEntityInfoBO orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(reChgs.getReDepartCode());
            Map<String, String> reRegion = searchRegion(orgEntityInfoBO.getOrgCode());

            //???????????????
            map.addAttribute("reProvince", reRegion.get("province"));

            //???????????????
            map.addAttribute("reCity", reRegion.get("city"));

            //???????????????
            map.addAttribute("reCounty", reRegion.get("county"));

            //??????????????????
            map.addAttribute("reDepartCode", reChgs.getReDepartCode());

            //????????????????????????
            map.addAttribute("reDepartNameDet", reChgs.getReDepartNameDet());

            //????????????
            if(reChgs.getReDate() != null) {

                //???????????? ???
                map.addAttribute("reDateYear", DateUtils.formatDate(reChgs.getReDate(), "yyyy"));

                //???????????? ???
                map.addAttribute("reDateMonth", DateUtils.formatDate(reChgs.getReDate(), "MM"));

                //???????????? ???
                map.addAttribute("reDateDate", DateUtils.formatDate(reChgs.getReDate(), "dd"));

            }

            if(!StringUtil.isBlank(reChgs.getReType())) {

                //????????????
                map.addAttribute("reType", reChgs.getReType().equals("1") ? "????????????" : "???????????????");

                //????????????
                map.addAttribute("reDetail", reChgs.getReDetail());

            }

            if(!StringUtil.isBlank(reChgs.getReDissentAgree())) {

                //??????????????????
                map.addAttribute("reDissentAgree", reChgs.getReDissentAgree().equals("1") ? "???" : "???");

                //????????????
                map.addAttribute("reDissentDetail", reChgs.getReDissentDetail());

            }

            if(!StringUtil.isBlank(reChgs.getDissentAgree())) {

                //??????????????????
                map.addAttribute("dissentAgree", reChgs.getDissentAgree().equals("1") ? "???" : "???");

            }

            if(!StringUtil.isBlank(reChgs.getDissentDetail())) {
                //????????????
                map.addAttribute("dissentDetail", reChgs.getDissentDetail());
            }

            if(!StringUtil.isBlank(reChgs.getChgType())) {

                //????????????
                map.addAttribute("chgType",
                        getDataDict(ConstantValue.ELIMINATE_LETTER_CHG_TYPE, userInfo.getOrgCode(), reChgs.getChgType()));

                //????????????
                map.addAttribute("chgDetail", reChgs.getChgDetail());

            }

            if(!StringUtil.isBlank(reChgs.getIndusChgAgree())) {

                //????????????????????????
                map.addAttribute("indusChgAgree", reChgs.getIndusChgAgree().equals("1") ? "???" : "???");

                //????????????????????????
                map.addAttribute("indusChgDetail", reChgs.getIndusChgDetail());

            }

            if(!StringUtil.isBlank(reChgs.getLongActionAgree())) {

                //????????????????????????
                map.addAttribute("longActionAgree", reChgs.getLongActionAgree().equals("1") ? "???" : "???");

                //??????????????????
                map.addAttribute("longActionDetail", reChgs.getLongActionDetail());

            }

            //??????????????????
            map.addAttribute("othClob", reChgs.getOthClob());

        }
        //-99??????????????????DB???????????????????????????????????????
        if(bo.getInstanceId() == -99){
            map.put("instanceId",bo.getInstanceId());
            return;
        }
        //??????????????????
        List<Map<String,Object>> allLinks = this.queryAllLinks(bo.getInstanceId());
        map.addAttribute("allLinks", allLinks);

        if(allLinks.size() > 1) {
            //??????????????????????????????
            map.addAttribute("advice",allLinks.get(1).get("REMARKS"));
        }

    }

    private Map<String, String> searchRegion(String regionCode) {

        Assert.hasLength(regionCode, "??????[regionCode]");

        Map<String, String> result = new HashMap<>();

        List<Map<String,Object>> reRegions = eliminateLetterThoReChgService.searchRegion(regionCode);
        for(Iterator<Map<String, Object>> iterator = reRegions.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            if(next.get("PROVINCE_") != null) {
                result.put("province", (String)next.get("PROVINCE_"));  //???
            }
            if(next.get("CITY_") != null) {
                result.put("city", (String)next.get("CITY_"));  //???
            }
            if(next.get("COUNTY_") != null) {
                result.put("county", (String)next.get("COUNTY_"));  //???
            }
        }

        return result;

    }

    private String getDataDict(String type, String orgCode, String dictGeneralCode) {

        Assert.hasLength(type, "??????[type]");
        Assert.hasLength(orgCode, "??????[orgCode]");
        Assert.hasLength(dictGeneralCode, "??????[dictGeneralCode]");

        String dictName = null;

        List<BaseDataDict> letterTypeBaseDataDicts =
                baseDictionaryService.getDataDictListOfSinglestage(type, orgCode);

        for(Iterator<BaseDataDict> iterator = letterTypeBaseDataDicts.iterator(); iterator.hasNext();) {
            BaseDataDict next = iterator.next();
            if(dictGeneralCode.equals(next.getDictGeneralCode())) {
                dictName = next.getDictName();
                break;
            }
        }

        return dictName;

    }

    /**
     * ????????????????????????
     *
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public EUDGPagination searchList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchList(params, rowBounds);
        formatListData(list,true);
        long count = eliminateLetterThoMapper.countList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * ??????????????????????????????
     *
     * @param page
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public EUDGPagination searchWaitList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchWaitList(params, rowBounds);
        formatListData(list,true);
        long count = eliminateLetterThoMapper.countWaitList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * ?????????????????????????????????
     * @param page
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public List<EliminateLetterTho> searchWaitListAll(Map<String, Object> params) throws Exception {
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchWaitList(params);
        formatListData(list,true);
        return list;
    }

    /**
     * ??????????????????????????????
     *
     * @param page
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public EUDGPagination searchJurisdictionList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchJurisdictionList(params, rowBounds);
        formatListData(list,true);
        long count = eliminateLetterThoMapper.countJurisdictionList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * ??????????????????????????????
     *
     * @param page
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public EUDGPagination searchArchiveList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchArchiveList(params, rowBounds);
        formatListData(list,true);
        long count = eliminateLetterThoMapper.countArchiveList(params);
        EUDGPagination pagination = new EUDGPagination(count, list);
        return pagination;
    }

    /**
     * ??????????????????
     *
     * @param page
     * @param params ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public List<EliminateLetterTho> searchExportList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchArchiveList(params, rowBounds);
        formatListData(list,true);
        return list;
    }

    /**
     * ????????????id????????????
     *
     * @param id ??????????????????id
     * @return ??????????????????????????????
     */
    @Override
    public EliminateLetterTho searchById(Long id) throws Exception {
        EliminateLetterTho bo = eliminateLetterThoMapper.searchById(id);
        return bo;
    }

    /**
     * ?????????????????????
     *
     * @param bo
     * @param userInfo
     * @return ??????????????????????????????
     */
    @Override
    public Map<String, Object> handle(EliminateLetterTho bo, UserInfo userInfo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;
        //nextUserInfoList ?????? userInfo ????????????userId???partyName???orgCode???orgId???orgName
        JSONArray.parseArray(bo.getReChgs().getReUser(), UserInfo.class);
        List<UserInfo> nextUserInfoList = new ArrayList<>();
        nextUserInfoList.add(bo.getNextUser());
        Map<String,Object> extraParams = new HashMap<String,Object>();
        extraParams.put ("advice",bo.getAdvice());
        //todo ????????????????????????????????????????????????(???????????????)
        flag = baseWorkflowService.subWorkflow4Base(bo.getInstanceId(), bo.getFlowStep(), nextUserInfoList, userInfo, extraParams);
        return map;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param params ????????????
     * @return ??????????????????????????????
     */
    @Override
    public Map<String, Object> statisticsMainCount(Map<String, Object> params) throws Exception {
        Map<String, Object> countMap = new HashMap<>();
        Assert.hasLength((String) params.get("regionCode"),"REGION_CODE????????????");

        params.put("statisticsType","total");
        long totalCount = eliminateLetterThoMapper.statisticsMainCount(params);

        params.put("statisticsType","feedback");
        long feedbackCount = eliminateLetterThoMapper.statisticsMainCount(params);

        params.put("statisticsType","rectify");
        params.put("chgType","1");//???????????????
        long rectifyCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //???????????????
        params.put("statisticsType","indusChg");
        params.put("indusChgAgree","1");
        long indusChgCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //???????????????
        params.put("indusChg","");
        params.put("statisticsType","longAction");
        params.put("longActionAgree","1");
        long longActionCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //???????????????
        params.put("longAction","");
        params.put("statisticsType","failEnd");
        params.put("bizStatus","10");//?????????????????????
        long failEndCount = eliminateLetterThoMapper.statisticsMainCount(params);

        countMap.put("totalCount",totalCount);
        countMap.put("feedbackCount",feedbackCount);
        countMap.put("rectifyCount",rectifyCount);
        countMap.put("indusChgCount",indusChgCount);
        countMap.put("longActionCount",longActionCount);
        countMap.put("failEndCount",failEndCount);
        return countMap;
    }

    /**
     * ??????????????????????????????
     *
     * @param params ????????????
     * @return
     */
    @Override
    public Map<String, Object> getIndusDistribution(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode????????????");
        List<Map<String, Object>> indusList = new ArrayList<>();
        Map<String, Object> indusMap = new HashMap<>();

        indusList = eliminateLetterThoMapper.getIndusDistribution(params);
        indusList.forEach(d->{
            String key = (String) d.get("industrialCode");
            indusMap.put(key,d.get("total"));
        });
        Map<String, BaseDataDict> dataDictMap = dictMap(ConstantValue.ELIMINATE_LETTER_THO_INDUS, regionCode);
        List<Map<String, Object>> dictList = new ArrayList<>();
        long total = 0;
        for(String key:dataDictMap.keySet()){
            BaseDataDict dictBean = dataDictMap.get(key);
            System.out.println("key:"+key+" "+"Value:"+ dictBean);
            Long count = 0L;
            Object countObj = indusMap.get(key);
            if(countObj!=null){
                count = Long.valueOf(String.valueOf(countObj));
            }
            Map<String, Object> temp = new HashMap<>();
            temp.put("industrialCode",key);
            temp.put("industrialName",dictBean.getDictName());
            temp.put("total", count);
            total += count;
            dictList.add(temp);
        }



        List<Map<String, Object>> listResult = sortList(dictList);

        //resultMap.put("total",total);
        resultMap.put("indusList",listResult);
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param params ????????????
     * @return
     */
    @Override
    public Map<String, Object> getLetterTypeDistribution(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode????????????");
        String searchType = (String) params.get("searchType");
        if("6".equals(searchType)){
            int length = regionCode.length();
            if(length == 2){
                params.put("chiefLevel","2");
                params.put("bizStatusArr","07,08,09".split(","));
            }else if(length == 4){
                params.put("chiefLevel","3");
                params.put("bizStatusArr","07,08".split(","));
            }else if(length == 6){
                params.put("bizStatusArr","07".split(","));
            }
        }
        List<Map<String, Object>> letterTypeList = new ArrayList<>();
        Map<String, Object> letterTypeMap = new HashMap<>();
        letterTypeList = eliminateLetterThoMapper.getLetterTypeDistribution(params);

        letterTypeList.forEach(d->{
            String key = (String) d.get("letterType");
            letterTypeMap.put(key,d.get("total"));
        });
        Map<String, BaseDataDict> dataDictMap = dictMap(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, regionCode);
        List<Map<String, Object>> dictList = new ArrayList<>();
        long total = 0;
        for(String key:dataDictMap.keySet()){
            BaseDataDict dictBean = dataDictMap.get(key);
            System.out.println("key:"+key+" "+"Value:"+ dictBean);
            Long count = 0L;
            Object countObj = letterTypeMap.get(key);
            if(countObj!=null){
                count = Long.valueOf(String.valueOf(countObj));
            }
            Map<String, Object> temp = new HashMap<>();
            temp.put("letterType",key);
            temp.put("letterName",dictBean.getDictName());
            temp.put("total", count);
            total += count;
            dictList.add(temp);
        }
        //resultMap.put("total",total);
        resultMap.put("letterTypeList",dictList);
        return resultMap;
    }

    /**
     * ??????????????????
     *
     * @param params ????????????
     * @return
     */
    @Override
    public Map<String, Object> getDeptRankList(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        String[] professionCodeArr = ConstantValue.SSYH_PROFESSION_CODE.split(",");
        List<String> professionCodeList = new ArrayList<>(professionCodeArr.length);
        Collections.addAll(professionCodeList,professionCodeArr);
        params.put("professionCodeArr",professionCodeList);

        List<Map<String, Object>> deptRankList = eliminateLetterThoMapper.getDeptRankList(params);
        String[] professionNameArr = ConstantValue.SSYH_PROFESSION_NAME.split(",");
        Map<String,String> professionMap = new HashMap<>();

        for(int i=0;i<professionCodeArr.length;i++){
            professionMap.put(professionCodeArr[i],professionNameArr[i]);
        }

        deptRankList.forEach(d->{
            String key = (String) d.get("PROFESSION_CODE");
            d.put("PROFESSION_NAME",professionMap.get(key));
        });

        resultMap.put("deptRankList",deptRankList);
        return resultMap;
    }

    /**
     * ????????????????????????????????????
     * ????????????????????????
     *
     * @param params ????????????
     * @return
     */
    @Override
    public Map<String, Object> getLetterTypeDistributionByDeptCode(Map<String, Object> params) throws Exception {


        String provinceOrgCode = (String) params.get("provinceOrgCode");
        Assert.hasLength(provinceOrgCode,"provinceOrgCode????????????");

        String professionCode = (String) params.get("professionCode");
        Assert.hasLength(professionCode,"professionCode????????????");

        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode????????????");



        List<Map<String, Object>> letterTypeList = new ArrayList<>();
        Map<String, Object> letterTypeMap = new HashMap<>();
        letterTypeList = eliminateLetterThoMapper.getLetterTypeDistributionByDeptCode(params);
        letterTypeList.forEach(d->{
            String key = (String) d.get("letterType");
            letterTypeMap.put(key,d.get("total"));
        });
        Map<String, BaseDataDict> dataDictMap = dictMap(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, regionCode);
        List<Map<String, Object>> dictList = new ArrayList<>();
        long total = 0;
        for(String key:dataDictMap.keySet()){
            BaseDataDict dictBean = dataDictMap.get(key);
            System.out.println("key:"+key+" "+"Value:"+ dictBean);
            Long count = 0L;
            Object countObj = letterTypeMap.get(key);
            if(countObj!=null){
                count = Long.valueOf(String.valueOf(countObj));
            }
            Map<String, Object> temp = new HashMap<>();
            temp.put("letterType",key);
            temp.put("letterName",dictBean.getDictName());
            temp.put("total", count);
            total += count;
            dictList.add(temp);
        }
        Map<String, Object> resultMap = new HashMap<>();
        //resultMap.put("total",total);
        resultMap.put("letterTypeList",dictList);
        return resultMap;
    }

    /**
     * ????????????????????????????????????????????????(????????????????????????)
     *
     * @param params
     * @return ????????????????????????
     * @throws Exception
     */
    @Override
    public Map<String, Object> getAreaDistributionByProfessionCode(Map<String, Object> params) throws Exception {
        String provinceOrgCode = (String) params.get("provinceOrgCode");
        Assert.hasLength(provinceOrgCode,"provinceOrgCode????????????");

        String professionCode = (String) params.get("professionCode");
        Assert.hasLength(professionCode,"professionCode????????????");

        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode????????????");

        Map<String, Object> resultMap = new HashMap<>();
        String[] professionCodeArr = ConstantValue.SSYH_PROFESSION_CODE.split(",");
        List<String> professionCodeList = new ArrayList<>(professionCodeArr.length);
        Collections.addAll(professionCodeList,professionCodeArr);
        params.put("professionCodeArr",professionCodeList);
        int length = regionCode.length();
        if(length == 2){
            params.put("chiefLevel","2");
        }else if(length == 4){
            params.put("chiefLevel","3");
        }else if(length == 6){
            params.put("chiefLevel","4");
        }
        List<Map<String, Object>> areaList = eliminateLetterThoMapper.getAreaDistributionByProfessionCode(params);
        resultMap.put("areaList",areaList);
        return resultMap;
    }

    /**
     * ??????????????????????????????????????????
     * ????????????
     *
     * @param params ????????????
     * @return
     */
    @Override
    public Map<String, Object> getTotalDistributionByRegionCode(Map<String, Object> params) throws Exception {
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode????????????");
//        String searchType = (String) params.get("searchType");
//        Assert.hasLength(regionCode,"searchType????????????");

//        params.put("searchType",searchType);
        int length = regionCode.length();
        if(length == 2){
            params.put("chiefLevel","2");
            params.put("codeLength","4");
        }else if(length == 4 || length == 6){
            params.put("chiefLevel","3");
            params.put("codeLength","6");
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> totalList = eliminateLetterThoMapper.getTotalDistributionByRegionCode(params);
        resultMap.put("totalList",totalList);
        return resultMap;
    }

    @Override
    public Map<String, Object> searchPage(Map<String, Object> params) throws Exception {

        Map<String, Object> result = new HashMap<>();

        int page = 1;
        int rows = 10;
        long count = 0L;
        JSONArray data = new JSONArray();
        Map<String, Object> sqlParams = new HashMap<>();

        Assert.notNull(params, "params????????????");
        Assert.notNull(params.get("queryType"), "queryType????????????");
        Assert.hasLength((String)params.get("queryType"),"queryType?????????????????????");
        Assert.notNull(params.get("regionCode"), "regionCode????????????");
        Assert.hasLength((String)params.get("regionCode"),"regionCode?????????????????????");

        // 0????????????   1???????????????  2??????????????????????????????  3??????????????????????????????  4??????????????????????????????  5??????????????????????????????  6????????????????????????????????????
        String queryType = (String)params.get("queryType");
        String regionCode = (String) params.get("regionCode");
        String letterType = params.get("letterType") != null ? (String) params.get("letterType") : "";

        if(params.get("page") != null) {
            page = Integer.valueOf((String)params.get("page"));
        }

        if(params.get("rows") != null) {
            rows = Integer.valueOf((String)params.get("rows"));
        }

        if(!Arrays.asList("0", "1", "2", "3", "4", "5", "6").contains(queryType)) {
            throw new RuntimeException("????????????queryType");
        }

        sqlParams.put("queryType", queryType);
        sqlParams.put("letterType", letterType);
        sqlParams.put("regionCode", regionCode);
        sqlParams.put("createTimeStart", params.get("createTimeStart"));
        sqlParams.put("createTimeEnd", params.get("createTimeEnd"));

        if("1".equals(queryType)) {

            int length = regionCode.length();

            if(length == 2){
                sqlParams.put("bizStatusArr", Arrays.asList("07", "08", "09"));
            } else if(length == 4){
                sqlParams.put("bizStatusArr", Arrays.asList("07", "08"));
            } else if(length == 6){
                sqlParams.put("bizStatusArr", Arrays.asList("07"));
            }

        }

        if("2".equals(queryType) || "3".equals(queryType) || "4".equals(queryType)) {

            Assert.notNull(params.get("letterType"), "letterType????????????");
            Assert.hasLength((String)params.get("letterType"),"letterType?????????????????????");
            sqlParams.put("letterType", params.get("letterType"));

            Assert.notNull(params.get("professionCode"), "professionCode????????????");
            Assert.hasLength((String)params.get("professionCode"),"professionCode?????????????????????");
            sqlParams.put("professionCode", params.get("professionCode"));

            Assert.notNull(params.get("provinceOrgCode"), "provinceOrgCode????????????");
            Assert.hasLength((String)params.get("provinceOrgCode"),"provinceOrgCode?????????????????????");
            sqlParams.put("provinceOrgCode", params.get("provinceOrgCode"));

        }

        // ????????????
        if("5".equals(queryType)) {

            Assert.notNull(params.get("professionCode"), "professionCode????????????");
            Assert.hasLength((String)params.get("professionCode"),"professionCode?????????????????????");
            sqlParams.put("professionCode", params.get("professionCode"));

            Assert.notNull(params.get("provinceOrgCode"), "provinceOrgCode????????????");
            Assert.hasLength((String)params.get("provinceOrgCode"),"provinceOrgCode?????????????????????");
            sqlParams.put("provinceOrgCode", params.get("provinceOrgCode"));

        }

        count = eliminateLetterThoMapper.searchPageCount(sqlParams);
        List<EliminateLetterTho> thos = eliminateLetterThoMapper.searchPage(sqlParams, new RowBounds((page - 1) * rows, rows));

        Map<String, BaseDataDict> letterTypeDictMap =
                dictMap(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, regionCode);

        Map<String, BaseDataDict> indusDictMap =
                dictMap(ConstantValue.ELIMINATE_LETTER_THO_INDUS, regionCode);

        for(EliminateLetterTho tho : thos) {

            JSONObject item = new JSONObject();
            //??????
            item.put("thoId", tho.getThoId());
            //uuid
            item.put("thoUuid", tho.getThoUuid());
            //???????????????id
            item.put("instanceId", tho.getInstanceId());
            //????????????
            item.put("caseName", tho.getCaseName());
            //????????????
            item.put("fbDate", tho.getFbDate() != null ? DateUtils.formatDate(tho.getFbDate(), DateUtils.PATTERN_24TIME) : "");
            //????????????
            item.put("letterTypeName", letterTypeDictMap.get(tho.getLetterType()).getDictName());
            //??????????????????
            item.put("reDepartNameDet", tho.getReChgs() != null ? tho.getReChgs().getReDepartNameDet() : "");
            //????????????
            List<EliminateLetterIndus> induses = eliminateLetterIndusService.searchByThoId(tho.getThoId());
            JSONArray induseArray = new JSONArray();
            for(EliminateLetterIndus induse : induses) {
                String dictName = indusDictMap.get(induse.getIndustrialCode()).getDictName();
                JSONObject induseItem = new JSONObject();
                induseItem.put("induse", dictName);
                induseArray.add(induseItem);
            }
            item.put("induses", induseArray);

            data.add(item);
        }

        result.put("page", page);
        result.put("rows", rows);
        result.put("data", data);
        result.put("count", count);

        return result;

    }

    //??????????????????
    private Map<String, BaseDataDict> dictMap (String dictCode, String orgCode){
        List<BaseDataDict> dicts = baseDictionaryService.getDataDictListOfSinglestage(dictCode,orgCode);
        Map<String, BaseDataDict> dictMap = new HashMap<>();
        for (BaseDataDict baseDataDict : dicts) {
            dictMap.put(baseDataDict.getDictGeneralCode(), baseDataDict);
        }
        return dictMap;
    }

    /**
     * ???????????????
     * @param list
     */
    private void formatListData(List<EliminateLetterTho> list,boolean needIndus) throws Exception{
        if(list.size()==0){
            return;
        }
        changeCodeToName(list);
        if(!needIndus){
            return;
        }
        //??????????????????
        List<Long> idList = list.stream().map(EliminateLetterTho::getThoId).collect(Collectors.toList());
        Map<String, String> indusNameStrMap = eliminateLetterIndusService.getIndusNameStr(idList, JSONUtils::getString);
        //logger.error("??????code???{}", Arrays.toString(indusNameStrMap.entrySet().toArray()));
        list.forEach(d->{
                    d.setIndusCodeArr(indusNameStrMap.get(d.getThoId().toString()));
        });

    }

    /**
     * ???????????????
     * @param list
     */
    private void changeCodeToName(List<EliminateLetterTho> list){
        Map<String, BaseDataDict> letterTypeMap = dictMap(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, "");
        for (EliminateLetterTho letterTho : list) {
            String letterType = letterTho.getLetterType();
            if(StringUtils.isNotBlank(letterType)){
                letterTho.setLetterTypeCN(letterTypeMap.get(letterType).getDictName());
            }
        }
    }

    private List<Map<String, Object>> sortList(List<Map<String, Object>> list){
        //??????Collections??????????????????
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Long a = Long.valueOf(String.valueOf(o1.get("total")));
                Long b = Long.valueOf(String.valueOf(o2.get("total")));
                return -a.compareTo(b);
            }
        });
        return list;
    }

}