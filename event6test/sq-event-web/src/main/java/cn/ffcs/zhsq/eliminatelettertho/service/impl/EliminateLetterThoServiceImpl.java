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
 * @Description: 三书一函主表模块服务实现
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 福富软件
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

        //保存为草稿
        this.saveRaft(bo, userInfo);

        //启动流程
        this.start(bo, userInfo);

        return bo.getThoId();

    }

    @Override
    @Transactional
    public Long commitWorkFlow(EliminateLetterTho bo, UserInfo userInfo, String type) throws Exception {

        //单位类型
        EliminateLetterThoProfessionalTypeEnum professionalType = getProfessionalType(type);

        //数据校验
        Map<String, Object> result = this.validateFeedback(professionalType, bo, userInfo);

        //三书一函接收整改表
        this.updateFeedback(professionalType, bo, userInfo);

        //扭转到下一环节
        this.subNextStep(
                (Long)result.get("thoId"),
                (String)result.get("advice"),
                userInfo,
                Arrays.asList((UserInfo)result.get("nextUserInfo")),
                (String)result.get("nextNodeName")
        );

        //更新状态
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

        //单位类型
        EliminateLetterThoProfessionalTypeEnum professionalType = getProfessionalType(type);

        //数据校验
        Map<String, Object> result = this.validateReject(professionalType, bo, userInfo);

        //三书一函接收整改表
        this.updateReject(professionalType, bo, userInfo);

        //更新状态
        this.alterBizStatus(
                (Long)result.get("thoId"),
                (Long)result.get("instanceId"),
                (String)result.get("curNodeName"),
                (String)result.get("nextNodeName"),
                userInfo,
                false
        );

        //扭转到下一环节
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
     * 查询所有的流程环节
     * @param instanceId
     * @return
     * @throws Exception
     */
    private List<Map<String,Object>> queryAllLinks(Long instanceId) throws Exception {

        List<Map<String, Object>> tempList = new ArrayList<>();

        if(instanceId == null) {
            return tempList;
        }

        //处理当前环节信息
        Map<String, Object> curMap = baseWorkflowService.findCurTaskData(instanceId);
        ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);

        //流程已经完结
        if(proInstance != null && "2".equals(proInstance.getStatus())) {
            curMap = new HashMap<>();
            curMap.put("TASK_NAME", "归档");
            curMap.put("TASK_CODE", "end");
            curMap.put("ORG_NAME", "");
            curMap.put("TRANSACTOR_NAME", "");
        } else {
            curMap.put("TASK_NAME", curMap.get("WF_ACTIVITY_NAME_"));
            curMap.put("TASK_CODE", curMap.get("NODE_NAME"));
            curMap.put("ORG_NAME", curMap.get("WF_ORGNAME"));
            curMap.put("TRANSACTOR_NAME", curMap.get("WF_USERNAME"));
        }

        //拼接当前环节
        if(curMap != null) {
            tempList.add(0, curMap);
        }

        //历史环节信息
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

        Assert.notNull(professionalType, "缺少[professionalType]");

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getThoUuid(), "缺少[thoUuid]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "不匹配的[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());
        bo.setInstanceId(eliminateLetterTho.getInstanceId());
        thoId = bo.getThoId();
        instanceId = bo.getInstanceId();
        advice = bo.getAdvice();

        EliminateLetterThoReChg reChgs = bo.getReChgs();

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            //制发单位

            curNodeName = EliminateLetterThoEnum.START_DEPT_AUDIT.getCode();
            nextNodeName = EliminateLetterThoEnum.INDUSTRY_FEEDBACK.getCode();

            EliminateLetterThoReChg eliminateLetterThoReChg = eliminateLetterThoReChgService.searchByThoId(bo.getThoId());

            nextUserInfo = JSON.parseArray(eliminateLetterThoReChg.getReUser(), UserInfo.class).get(0);
            Assert.notNull(nextUserInfo, "缺少[nextUserInfo]");

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

        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

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

            //更新流程状态
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());
            bo.setUpdator(userInfo.getUserId());

            eliminateLetterThoMapper.update(et);

            //保存制发单位反馈
            eliminateLetterThoReChgService.updateIssuingUnitFeedbackByThoId(reChgs);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE) {

            //扫黑部门

        } else {

            throw new RuntimeException("其他单位不支持驳回");

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
        throw new RuntimeException("不匹配的单位");
    }

    private Map<String, Object> validateFeedback(EliminateLetterThoProfessionalTypeEnum professionalType, EliminateLetterTho bo, UserInfo userInfo) {

        String curNodeName = null;
        String nextNodeName = null;
        UserInfo nextUserInfo = null;
        String advice = null;

        Assert.notNull(professionalType, "缺少[professionalType]");

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getThoUuid(), "缺少[thoUuid]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "不匹配的[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());

        advice = bo.getAdvice();

        EliminateLetterThoReChg reChgs = bo.getReChgs();

        if(professionalType == EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT) {

            //制发单位

            curNodeName = EliminateLetterThoEnum.START_DEPT_AUDIT.getCode();

            if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 6) {
                // 县
                nextNodeName = EliminateLetterThoEnum.COUNTY_AUDIT.getCode();

            } else if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 4) {
                //市
                nextNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

            } else if(userInfo.getInfoOrgList().get(0).getOrgCode().length() == 2) {
                //省
                nextNodeName = EliminateLetterThoEnum.PROVINCE_AUDIT.getCode();

            }

            List<UserInfo> userInfos = JSONArray.parseArray(bo.getNextUserStr(), UserInfo.class);
            Assert.notEmpty(userInfos, "缺少下一环节办理人");
            nextUserInfo = userInfos.get(0);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR) {

            //行业部门
            Assert.hasLength(reChgs.getReDateStr(), "缺少[接收时间]");
            Assert.hasLength(reChgs.getReType(), "缺少[回复情况]");
            Assert.hasLength(reChgs.getReDetail(), "缺少[回复详情]");

            if(!StringUtil.isBlank(reChgs.getReDissentAgree())) {

                if(reChgs.getReDissentAgree().equals("2")) {
                    Assert.hasLength(reChgs.getChgType(), "缺少[整改情况]");
                    if(reChgs.getChgType().equals("1")) {
                        Assert.hasLength(reChgs.getChgDetail(), "缺少[整改详情]");
                    }
                    Assert.hasLength(reChgs.getIndusChgAgree(), "缺少[是否开展行业治理]");
                    if(reChgs.getIndusChgAgree().equals("1")) {
                        Assert.hasLength(reChgs.getIndusChgDetail(), "缺少[行业治理情况详情]");
                    }
                    Assert.hasLength(reChgs.getLongActionAgree(), "缺少[是否建立长效机制]");
                    if(reChgs.getLongActionAgree().equals("1")) {
                        Assert.hasLength(reChgs.getLongActionDetail(), "缺少[长效机制详情]");
                    }
                    Assert.hasLength(reChgs.getOthClob(), "缺少[其他情况说明]");
                } else {
                    Assert.hasLength(reChgs.getReDissentDetail(), "缺少[异议详情]");

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
            Assert.notNull(nextUserInfo, "缺少[nextUserInfo]");

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE) {

            //扫黑办

            if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.COUNTY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 6) {

                //县扫黑办
                curNodeName = EliminateLetterThoEnum.COUNTY_AUDIT.getCode();

                //市扫黑办
                nextNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

                List<UserInfo> userInfos = JSONArray.parseArray(bo.getNextUserStr(), UserInfo.class);
                Assert.notEmpty(userInfos, "缺少下一环节办理人");
                nextUserInfo = userInfos.get(0);

            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.CITY_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 4) {

                //市扫黑办
                curNodeName = EliminateLetterThoEnum.CITY_AUDIT.getCode();

                //归档
                nextNodeName = EliminateLetterThoEnum.SUC_END.getCode();

                //下一环节办理人
                nextUserInfo = userInfo;

            } else if(eliminateLetterTho.getFlowStep().equals(EliminateLetterThoEnum.PROVINCE_AUDIT.getCode()) && userInfo.getInfoOrgList().get(0).getOrgCode().length() == 2) {

                //省扫黑办
                curNodeName = EliminateLetterThoEnum.PROVINCE_AUDIT.getCode();

                //归档
                nextNodeName = EliminateLetterThoEnum.SUC_END.getCode();

                //下一环节办理人
                nextUserInfo = userInfo;

            } else {
                throw new RuntimeException("异常[flowStep:" + eliminateLetterTho.getFlowStep() + "]");
            }

        } else {

            throw new RuntimeException("不匹配的专业");

        }

        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

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

            //更新流程状态
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN.getCode());
            eliminateLetterThoMapper.update(et);

            //保存制发单位反馈
            eliminateLetterThoReChgService.updateIssuingUnitFeedbackByThoId(reChgs);

        } else if(professionalType == EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR) {

            EliminateLetterThoReChg reChgs = bo.getReChgs();
            reChgs.setThoId(bo.getThoId());
            reChgs.setUpdator(userInfo.getUserId());

            //更新流程状态
            EliminateLetterTho et = new EliminateLetterTho();
            et.setThoId(bo.getThoId());
            et.setBizStatus(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_ISSUING_UNIT.getCode());
            eliminateLetterThoMapper.update(et);

            //保存行业部门反馈
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
            throw new RuntimeException("流程启动失败");
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
            throw new RuntimeException("流程扭转失败");
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
            throw new RuntimeException("流程扭转失败");
        }

    }

    private void alterBizStatus(Long thoId, Long instanceId, String curNodeName, String nextNodeName, UserInfo userInfo, boolean isPass) throws Exception {

        String bizStatus = initStatusMap(isPass).get(curNodeName);

        Assert.hasLength(bizStatus, "不匹配的流程名称");

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

        if(isPass) {    //通过

            //发起(制发单位 派送给 行业部门)
            statusMap.put("task1", EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());

            //反馈(行业部门 反馈给 制发单位)
            statusMap.put("task2", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_ISSUING_UNIT.getCode());

            //审核(制发单位 审核通过 反馈给 县扫黑办)
            statusMap.put("task3", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN.getCode());

            //审核(县级扫黑办 审核通过 反馈给 市级扫黑办)
            statusMap.put("task4", EliminateLetterThoBizStatusEnum.PENDING_REVIEW_COUNTY_CRACK_DOWN.getCode());

            //审核(市级扫黑办 审核通过 归档)
            statusMap.put("task5", EliminateLetterThoBizStatusEnum.FILE.getCode());

            //审核(省级扫黑办 审核通过 归档)
            statusMap.put("task6", EliminateLetterThoBizStatusEnum.FILE.getCode());

        } else {    //驳回

            //驳回(制发单位 驳回给 行业部门)
            statusMap.put("task3", EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode());

            //审核不通过(县级扫黑办审核不通过)
            statusMap.put("task4", EliminateLetterThoBizStatusEnum.FAIL_TO_FINISH_COUNTY.getCode());

            //审核不通过(市级扫黑办审核不通过)
            statusMap.put("task5", EliminateLetterThoBizStatusEnum.FAIL_TO_FINISH_CITY.getCode());

        }

        return statusMap;

    }

    /**
     * 保存草稿
     *
     * @param bo 三书一函主表业务对象
     * @param userInfo 登录用户信息
     * @return 三书一函主表id
     */
    @Override
    @Transactional
    public Long saveRaft(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        if(this.isInsertRaft(bo)) {

            // 新增草稿
            return this.insertRaft(bo, userInfo);

        } else {

            //编辑草稿
            return this.updateRaft(bo, userInfo);

        }

    }

    private boolean isInsertRaft(EliminateLetterTho bo) {
        return bo == null || StringUtils.isBlank(bo.getThoUuid());
    }

    private Long updateRaft(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        //数据校验
        Long thoId = this.preUpdateValidate(bo, userInfo);

        //三书一函主表
        this.updateTho(bo, userInfo);

        //三书一函行业领域表
        this.updateIndusList(bo.getInduss(), thoId, userInfo);

        //三书一函接收整改表
        this.updateReChgs(bo.getReChgs(), thoId, userInfo);

        return thoId;

    }

    private Long preUpdateValidate(EliminateLetterTho bo, UserInfo userInfo) {

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getThoUuid(), "缺少[thoUuid]");
        Assert.hasLength(bo.getThtNo(), "缺少[thtNo]");
        Assert.hasLength(bo.getLetterType(), "缺少[文书类型]");
        Assert.hasLength(bo.getFbDepartCode(), "缺少[发布单位编码]");
        Assert.hasLength(bo.getFbDepartNameDet(), "缺少[具体发布单位名称]");
        Assert.hasLength(bo.getFbDateStr(), "缺少[发布时间]");
        Assert.hasLength(bo.getLetterNo(), "缺少[文号]");
        Assert.hasLength(bo.getCaseName(), "缺少[案件名称]");
        Assert.hasLength(bo.getCaseNo(), "缺少[案件编码]");
        Assert.hasLength(bo.getLetterContentClob(), "缺少[文书内容]");

        EliminateLetterTho eliminateLetterTho = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());
        Assert.notNull(eliminateLetterTho, "不匹配的[thoUuid]");
        bo.setThoId(eliminateLetterTho.getThoId());

        if(!eliminateLetterTho.getBizStatus().equals(EliminateLetterThoBizStatusEnum.DRAFT.getCode())) {
            throw new RuntimeException("异常[bizStatus]["+bo.getBizStatus()+"]");
        }

        List<EliminateLetterIndus> induss = bo.getInduss(); //行业领域
        Assert.notEmpty(induss, "缺少[行业、领域]");
        for(Iterator<EliminateLetterIndus> iterator = induss.iterator(); iterator.hasNext();) {
            EliminateLetterIndus indus = iterator.next();
            Assert.hasLength(indus.getIndustrialCode(), "缺少[行业领域CODE]");
            if("10".equals(indus.getIndustrialCode())) { //行业领域为"其他"
                Assert.hasLength(bo.getIndustrialComment(), "缺少[行业领域补充说明]");
            }
        }

        EliminateLetterThoReChg reChgs = bo.getReChgs(); //接收与整改情况
        Assert.notNull(reChgs, "缺少[接收与整改情况]");
        Assert.hasLength(reChgs.getReDepartCode(), "缺少[接收与整改情况]");
        Assert.hasLength(reChgs.getReDepartNameDet(), "缺少[具体接收单位名称]");
        Assert.notNull(reChgs.getReUserId(), "缺少[接收人员ID]");
        Assert.hasLength(reChgs.getReUserName(), "缺少[接收人员名称]");

        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

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

        //数据校验
        preInsertValidate(bo, userInfo);

        //三书一函主表
        Long thoId = insertTho(bo, userInfo);

        //三书一函行业领域表
        insertIndusList(bo.getInduss(), thoId, userInfo);

        //三书一函接收整改表
        insertReChgs(bo.getReChgs(), thoId, userInfo);

        return bo.getThoId();

    }

    private void preInsertValidate(EliminateLetterTho bo, UserInfo userInfo) {

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getLetterType(), "缺少[文书类型]");
        Assert.hasLength(bo.getFbDepartCode(), "缺少[发布单位编码]");
        Assert.hasLength(bo.getFbDepartNameDet(), "缺少[具体发布单位名称]");
        Assert.hasLength(bo.getFbDateStr(), "缺少[发布时间]");
        Assert.hasLength(bo.getLetterNo(), "缺少[文号]");
        Assert.hasLength(bo.getCaseName(), "缺少[案件名称]");
        Assert.hasLength(bo.getCaseNo(), "缺少[案件编码]");
        Assert.hasLength(bo.getLetterContentClob(), "缺少[文书内容]");

        List<EliminateLetterIndus> induss = bo.getInduss(); //行业领域
        Assert.notEmpty(induss, "缺少[行业、领域]");
        for(Iterator<EliminateLetterIndus> iterator = induss.iterator(); iterator.hasNext();) {
            EliminateLetterIndus indus = iterator.next();
            Assert.hasLength(indus.getIndustrialCode(), "缺少[行业领域CODE]");
            if("10".equals(indus.getIndustrialCode())) { //行业领域为"其他"
                Assert.hasLength(bo.getIndustrialComment(), "缺少[行业领域补充说明]");
            }
        }

        EliminateLetterThoReChg reChgs = bo.getReChgs(); //接收与整改情况
        Assert.notNull(reChgs, "缺少[接收与整改情况]");
        Assert.hasLength(reChgs.getReDepartCode(), "缺少[接收与整改情况]");
        Assert.hasLength(reChgs.getReDepartNameDet(), "缺少[具体接收单位名称]");
        Assert.notNull(reChgs.getReUserId(), "缺少[接收人员ID]");
        Assert.hasLength(reChgs.getReUserName(), "缺少[接收人员名称]");

        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

    }

    private Long insertTho(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        String thtNo = numberConfigureService.getNumber(userInfo.getInfoOrgList().get(0).getOrgCode(), ConstantValue.ELIMINATE_LETTER_THO_SYS_CODE);
        thtNo = DateUtils.getToday("yyyyMMdd") + thtNo;

        bo.setThtNo(thtNo);
        bo.setThoUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        bo.setBizStatus(EliminateLetterThoBizStatusEnum.DRAFT.getCode());   //草稿
        bo.setFlowStep(EliminateLetterThoBizStatusEnum.DRAFT.getCode());    //草稿
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
     * 修改数据
     *
     * @param bo 三书一函主表业务对象
     * @return 是否修改成功
     */
    @Override
    public void update(EliminateLetterTho bo) throws Exception {
        long result = eliminateLetterThoMapper.update(bo);
        Assert.isTrue(result > 0, "删除失败");
    }

    /**
     * 删除数据
     *
     * @param bo 三书一函主表业务对象
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public void delete(EliminateLetterTho bo, UserInfo userInfo) throws Exception {

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getThoUuid(), "缺少[bo.thoUuid]");
        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

        EliminateLetterTho tho =
                eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());

        Assert.notNull(tho, "不匹配的[thoUuid]");
        Assert.hasLength(tho.getBizStatus(), "数据异常[bizStatus]");
        //Assert.isTrue(EliminateLetterThoBizStatusEnum.DRAFT.getCode().equals(tho.getBizStatus()), "不是草稿状态,不能删除");

        bo.setUpdator(userInfo.getUserId());
        long result = eliminateLetterThoMapper.delete(bo);
        Assert.isTrue(result > 0, "删除失败");

        EliminateLetterThoReChg reChg = new EliminateLetterThoReChg();
        reChg.setThoId(tho.getThoId());
        reChg.setUpdator(userInfo.getUserId());
        boolean isSuccess = eliminateLetterThoReChgService.deleteByThoId(reChg);
        Assert.isTrue(isSuccess, "删除失败[reChg]");

        EliminateLetterIndus indus = new EliminateLetterIndus();
        indus.setThoId(tho.getThoId());
        indus.setUpdator(userInfo.getUserId());
        isSuccess = eliminateLetterIndusService.deleteByThoId(indus);
        Assert.isTrue(isSuccess, "删除失败[indus]");

    }

    @Override
    @Transactional
    public void deleteBatch(List<EliminateLetterTho> thos, UserInfo userInfo) throws Exception {

        Assert.notEmpty(thos, "缺少[thos]");

        Assert.notNull(userInfo, "缺少[userInfo]");
        Assert.notNull(userInfo.getUserId(), "缺少[userInfo.userId]");

        for(EliminateLetterTho bo : thos) {

            this.delete(bo, userInfo);

        }

    }

    @Override
    public EliminateLetterTho searchByThoUuid(EliminateLetterTho bo) throws Exception {

        Assert.notNull(bo, "缺少[bo]");
        Assert.hasLength(bo.getThoUuid(), "缺少[bo.thoUuid]");

        bo = eliminateLetterThoMapper.searchByThoUuid(bo.getThoUuid());

        Assert.notNull(bo, "不匹配的thoUuid");

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

        //三书一函编码
        map.addAttribute("thtNo", bo.getThtNo());

        //文书类型名称
        map.addAttribute("letterTypeName",
                getDataDict(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, userInfo.getOrgCode(), bo.getLetterType()));

        //发布单位编码
        map.addAttribute("fbDepartCode", bo.getFbDepartCode());

        //具体发布单位名称
        map.addAttribute("fbDepartNameDet", bo.getFbDepartNameDet());

        //发布时间 年
        map.addAttribute("fbDateYear", DateUtils.formatDate(bo.getFbDate(), "yyyy"));

        //发布时间 月
        map.addAttribute("fbDateMonth", DateUtils.formatDate(bo.getFbDate(), "MM"));

        //发布时间 日
        map.addAttribute("fbDateDate", DateUtils.formatDate(bo.getFbDate(), "dd"));

        //文号
        map.addAttribute("letterNo", bo.getLetterNo());

        //案件名称
        map.addAttribute("caseName", bo.getCaseName());

        //案件编码
        map.addAttribute("caseNo", bo.getCaseNo());

        //文书内容
        map.addAttribute("letterContentClob", bo.getLetterContentClob());

        //行业领域补充说明
        map.addAttribute("industrialComment", bo.getIndustrialComment());

        //行业领域
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

        //行业、领域
        map.addAttribute("indus", indusArray);

        if(reChgs != null) {

            //制发单位地域
            Map<String, String> createRegion = searchRegion(bo.getFbDepartCode());

            //制发单位省
            map.addAttribute("createProvince", createRegion.get("province"));

            //制发单位市
            map.addAttribute("createCity", createRegion.get("city"));

            //制发单位县
            map.addAttribute("createCounty", createRegion.get("county"));


            //接收单位地域
            OrgEntityInfoBO orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(reChgs.getReDepartCode());
            Map<String, String> reRegion = searchRegion(orgEntityInfoBO.getOrgCode());

            //接收单位省
            map.addAttribute("reProvince", reRegion.get("province"));

            //接收单位市
            map.addAttribute("reCity", reRegion.get("city"));

            //接收单位县
            map.addAttribute("reCounty", reRegion.get("county"));

            //接收单位编码
            map.addAttribute("reDepartCode", reChgs.getReDepartCode());

            //具体接收单位名称
            map.addAttribute("reDepartNameDet", reChgs.getReDepartNameDet());

            //接收时间
            if(reChgs.getReDate() != null) {

                //接收时间 年
                map.addAttribute("reDateYear", DateUtils.formatDate(reChgs.getReDate(), "yyyy"));

                //接收时间 月
                map.addAttribute("reDateMonth", DateUtils.formatDate(reChgs.getReDate(), "MM"));

                //接收时间 日
                map.addAttribute("reDateDate", DateUtils.formatDate(reChgs.getReDate(), "dd"));

            }

            if(!StringUtil.isBlank(reChgs.getReType())) {

                //回复情况
                map.addAttribute("reType", reChgs.getReType().equals("1") ? "按期回复" : "未按期回复");

                //回复详情
                map.addAttribute("reDetail", reChgs.getReDetail());

            }

            if(!StringUtil.isBlank(reChgs.getReDissentAgree())) {

                //是否提出异议
                map.addAttribute("reDissentAgree", reChgs.getReDissentAgree().equals("1") ? "是" : "否");

                //异议详情
                map.addAttribute("reDissentDetail", reChgs.getReDissentDetail());

            }

            if(!StringUtil.isBlank(reChgs.getDissentAgree())) {

                //是否同意异议
                map.addAttribute("dissentAgree", reChgs.getDissentAgree().equals("1") ? "否" : "是");

            }

            if(!StringUtil.isBlank(reChgs.getDissentDetail())) {
                //异议详情
                map.addAttribute("dissentDetail", reChgs.getDissentDetail());
            }

            if(!StringUtil.isBlank(reChgs.getChgType())) {

                //整改情况
                map.addAttribute("chgType",
                        getDataDict(ConstantValue.ELIMINATE_LETTER_CHG_TYPE, userInfo.getOrgCode(), reChgs.getChgType()));

                //整改详情
                map.addAttribute("chgDetail", reChgs.getChgDetail());

            }

            if(!StringUtil.isBlank(reChgs.getIndusChgAgree())) {

                //是否开展行业治理
                map.addAttribute("indusChgAgree", reChgs.getIndusChgAgree().equals("1") ? "是" : "否");

                //行业治理情况详情
                map.addAttribute("indusChgDetail", reChgs.getIndusChgDetail());

            }

            if(!StringUtil.isBlank(reChgs.getLongActionAgree())) {

                //是否建立长效机制
                map.addAttribute("longActionAgree", reChgs.getLongActionAgree().equals("1") ? "是" : "否");

                //长效机制详情
                map.addAttribute("longActionDetail", reChgs.getLongActionDetail());

            }

            //其他情况说明
            map.addAttribute("othClob", reChgs.getOthClob());

        }
        //-99表示该数据是DB导入的，不存在流程环节数据
        if(bo.getInstanceId() == -99){
            map.put("instanceId",bo.getInstanceId());
            return;
        }
        //所有环节信息
        List<Map<String,Object>> allLinks = this.queryAllLinks(bo.getInstanceId());
        map.addAttribute("allLinks", allLinks);

        if(allLinks.size() > 1) {
            //获取上一环节办理意见
            map.addAttribute("advice",allLinks.get(1).get("REMARKS"));
        }

    }

    private Map<String, String> searchRegion(String regionCode) {

        Assert.hasLength(regionCode, "缺少[regionCode]");

        Map<String, String> result = new HashMap<>();

        List<Map<String,Object>> reRegions = eliminateLetterThoReChgService.searchRegion(regionCode);
        for(Iterator<Map<String, Object>> iterator = reRegions.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            if(next.get("PROVINCE_") != null) {
                result.put("province", (String)next.get("PROVINCE_"));  //省
            }
            if(next.get("CITY_") != null) {
                result.put("city", (String)next.get("CITY_"));  //市
            }
            if(next.get("COUNTY_") != null) {
                result.put("county", (String)next.get("COUNTY_"));  //县
            }
        }

        return result;

    }

    private String getDataDict(String type, String orgCode, String dictGeneralCode) {

        Assert.hasLength(type, "缺少[type]");
        Assert.hasLength(orgCode, "缺少[orgCode]");
        Assert.hasLength(dictGeneralCode, "缺少[dictGeneralCode]");

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
     * 查询数据（分页）
     *
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
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
     * 查询待办数据（分页）
     *
     * @param page
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
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
     * 查询待办数据（不分页）
     * @param page
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    @Override
    public List<EliminateLetterTho> searchWaitListAll(Map<String, Object> params) throws Exception {
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchWaitList(params);
        formatListData(list,true);
        return list;
    }

    /**
     * 查询辖区数据（分页）
     *
     * @param page
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
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
     * 查询归档数据（分页）
     *
     * @param page
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
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
     * 查询导出列表
     *
     * @param page
     * @param params 查询参数
     * @return 三书一函主表分页数据对象
     */
    @Override
    public List<EliminateLetterTho> searchExportList(LayuiPage page, Map<String, Object> params) throws Exception {
        RowBounds rowBounds = new RowBounds((page.getPage() - 1) * page.getLimit(), page.getLimit());
        List<EliminateLetterTho> list = eliminateLetterThoMapper.searchArchiveList(params, rowBounds);
        formatListData(list,true);
        return list;
    }

    /**
     * 根据业务id查询数据
     *
     * @param id 三书一函主表id
     * @return 三书一函主表业务对象
     */
    @Override
    public EliminateLetterTho searchById(Long id) throws Exception {
        EliminateLetterTho bo = eliminateLetterThoMapper.searchById(id);
        return bo;
    }

    /**
     * 扭转至下一环节
     *
     * @param bo
     * @param userInfo
     * @return 三书一函主表业务对象
     */
    @Override
    public Map<String, Object> handle(EliminateLetterTho bo, UserInfo userInfo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;
        //nextUserInfoList 中的 userInfo 需要用到userId，partyName，orgCode，orgId，orgName
        JSONArray.parseArray(bo.getReChgs().getReUser(), UserInfo.class);
        List<UserInfo> nextUserInfoList = new ArrayList<>();
        nextUserInfoList.add(bo.getNextUser());
        Map<String,Object> extraParams = new HashMap<String,Object>();
        extraParams.put ("advice",bo.getAdvice());
        //todo 在此之前可能需要验证重复提交问题(目前先略过)
        flag = baseWorkflowService.subWorkflow4Base(bo.getInstanceId(), bo.getFlowStep(), nextUserInfoList, userInfo, extraParams);
        return map;
    }

    /**
     * 统计大屏各种类型的主表总数
     *
     * @param params 查询参数
     * @return 三书一函主表数据总数
     */
    @Override
    public Map<String, Object> statisticsMainCount(Map<String, Object> params) throws Exception {
        Map<String, Object> countMap = new HashMap<>();
        Assert.hasLength((String) params.get("regionCode"),"REGION_CODE不能为空");

        params.put("statisticsType","total");
        long totalCount = eliminateLetterThoMapper.statisticsMainCount(params);

        params.put("statisticsType","feedback");
        long feedbackCount = eliminateLetterThoMapper.statisticsMainCount(params);

        params.put("statisticsType","rectify");
        params.put("chgType","1");//已完成整改
        long rectifyCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //行业治理数
        params.put("statisticsType","indusChg");
        params.put("indusChgAgree","1");
        long indusChgCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //长效机制数
        params.put("indusChg","");
        params.put("statisticsType","longAction");
        params.put("longActionAgree","1");
        long longActionCount = eliminateLetterThoMapper.statisticsMainCount(params);

        //不通过数量
        params.put("longAction","");
        params.put("statisticsType","failEnd");
        params.put("bizStatus","10");//表示不完结归档
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
     * 行业领域文书分布情况
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public Map<String, Object> getIndusDistribution(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode不能为空");
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
     * 文书类型分布情况
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public Map<String, Object> getLetterTypeDistribution(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode不能为空");
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
     * 部门排行集合
     *
     * @param params 查询参数
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
     * 根据部门查找文书分布情况
     * 部门排行弹框使用
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public Map<String, Object> getLetterTypeDistributionByDeptCode(Map<String, Object> params) throws Exception {


        String provinceOrgCode = (String) params.get("provinceOrgCode");
        Assert.hasLength(provinceOrgCode,"provinceOrgCode不能为空");

        String professionCode = (String) params.get("professionCode");
        Assert.hasLength(professionCode,"professionCode不能为空");

        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode不能为空");



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
     * 根据专业编码查找文书地域分布情况(部门排行弹框使用)
     *
     * @param params
     * @return 文书地域分布情况
     * @throws Exception
     */
    @Override
    public Map<String, Object> getAreaDistributionByProfessionCode(Map<String, Object> params) throws Exception {
        String provinceOrgCode = (String) params.get("provinceOrgCode");
        Assert.hasLength(provinceOrgCode,"provinceOrgCode不能为空");

        String professionCode = (String) params.get("professionCode");
        Assert.hasLength(professionCode,"professionCode不能为空");

        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode不能为空");

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
     * 根据地域获取获取总数分布情况
     * 地图使用
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public Map<String, Object> getTotalDistributionByRegionCode(Map<String, Object> params) throws Exception {
        String regionCode = (String) params.get("regionCode");
        Assert.hasLength(regionCode,"regionCode不能为空");
//        String searchType = (String) params.get("searchType");
//        Assert.hasLength(regionCode,"searchType不能为空");

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

        Assert.notNull(params, "params不能为空");
        Assert.notNull(params.get("queryType"), "queryType不能为空");
        Assert.hasLength((String)params.get("queryType"),"queryType不能为空字符串");
        Assert.notNull(params.get("regionCode"), "regionCode不能为空");
        Assert.hasLength((String)params.get("regionCode"),"regionCode不能为空字符串");

        // 0所有文书   1不通过文书  2所有的部门接收数文书  3所有的部门反馈数文书  4所有的部门整改数文书  5部门区域分布全部文书  6部门区域分布不通过数文书
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
            throw new RuntimeException("不匹配的queryType");
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

            Assert.notNull(params.get("letterType"), "letterType不能为空");
            Assert.hasLength((String)params.get("letterType"),"letterType不能为空字符串");
            sqlParams.put("letterType", params.get("letterType"));

            Assert.notNull(params.get("professionCode"), "professionCode不能为空");
            Assert.hasLength((String)params.get("professionCode"),"professionCode不能为空字符串");
            sqlParams.put("professionCode", params.get("professionCode"));

            Assert.notNull(params.get("provinceOrgCode"), "provinceOrgCode不能为空");
            Assert.hasLength((String)params.get("provinceOrgCode"),"provinceOrgCode不能为空字符串");
            sqlParams.put("provinceOrgCode", params.get("provinceOrgCode"));

        }

        // 区域分布
        if("5".equals(queryType)) {

            Assert.notNull(params.get("professionCode"), "professionCode不能为空");
            Assert.hasLength((String)params.get("professionCode"),"professionCode不能为空字符串");
            sqlParams.put("professionCode", params.get("professionCode"));

            Assert.notNull(params.get("provinceOrgCode"), "provinceOrgCode不能为空");
            Assert.hasLength((String)params.get("provinceOrgCode"),"provinceOrgCode不能为空字符串");
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
            //主键
            item.put("thoId", tho.getThoId());
            //uuid
            item.put("thoUuid", tho.getThoUuid());
            //工作流对象id
            item.put("instanceId", tho.getInstanceId());
            //文书名称
            item.put("caseName", tho.getCaseName());
            //发出时间
            item.put("fbDate", tho.getFbDate() != null ? DateUtils.formatDate(tho.getFbDate(), DateUtils.PATTERN_24TIME) : "");
            //文书类型
            item.put("letterTypeName", letterTypeDictMap.get(tho.getLetterType()).getDictName());
            //接收部门名称
            item.put("reDepartNameDet", tho.getReChgs() != null ? tho.getReChgs().getReDepartNameDet() : "");
            //行业领域
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

    //获取字典编码
    private Map<String, BaseDataDict> dictMap (String dictCode, String orgCode){
        List<BaseDataDict> dicts = baseDictionaryService.getDataDictListOfSinglestage(dictCode,orgCode);
        Map<String, BaseDataDict> dictMap = new HashMap<>();
        for (BaseDataDict baseDataDict : dicts) {
            dictMap.put(baseDataDict.getDictGeneralCode(), baseDataDict);
        }
        return dictMap;
    }

    /**
     * 列表格式化
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
        //行业领域信息
        List<Long> idList = list.stream().map(EliminateLetterTho::getThoId).collect(Collectors.toList());
        Map<String, String> indusNameStrMap = eliminateLetterIndusService.getIndusNameStr(idList, JSONUtils::getString);
        //logger.error("行业code：{}", Arrays.toString(indusNameStrMap.entrySet().toArray()));
        list.forEach(d->{
                    d.setIndusCodeArr(indusNameStrMap.get(d.getThoId().toString()));
        });

    }

    /**
     * 列表格式化
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
        //实现Collections接口进行排序
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