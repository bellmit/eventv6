package cn.ffcs.zhsq.controller.service.impl;

import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationJXService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeFlowInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationRes;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationCase;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeFlowInfoMapper;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationMapper;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationResMapper;
import cn.ffcs.zhsq.mybatis.persistence.dispute.MediationCaseMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author hp
 */
@Service(value="disputeMediationJXServiceImpl")
@Transactional
public class DisputeMediationJXServiceImpl implements IDisputeMediationJXService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IBaseDictionaryService dictionaryService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IInvolvedPeopleService involvedPeopleService;
    @Autowired
    private DisputeFlowInfoMapper disputeFlowInfoMapper;
    @Autowired
    private DisputeMediationMapper disputeMediationMapper;
    @Autowired
    private DisputeMediationResMapper disputeMediationResMapper;
    @Autowired
    private MediationCaseMapper mediationCaseMapper;
    @Autowired
    private UserManageOutService userManageOutService;
    @Autowired
    private UserScoreService userScoreService;
    /**
     * ????????????
     * Sep 11, 2014
     * 9:20:45 AM
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    @Override
    public EUDGPagination findDisputePagination(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        List<DisputeMediation> list = disputeMediationMapper.findPageListByCriteria(params, rowBounds);
        int count = disputeMediationMapper.findCountByCriteria(params);
        list = formatData(list, params.get("infoOrgCode").toString());
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    /**
     * ????????????
     * @param bo
     * @return
     */
    @Override
    public int insertOrUpdate(DisputeMediation bo) throws Exception{
        DisputeMediation disputeMediation = disputeMediationMapper.searchByDisputeId(bo.getDisputeId());
        DisputeMediationRes disputeMediationRes = new DisputeMediationRes();
        UserBO userInfo = null;
        if(StringUtils.isNotBlank(bo.getAccepter())){
            userInfo = userManageOutService.getUserInfoByUUId(bo.getAccepter());
            bo.setCreatorId(userInfo.getUserId());
            bo.setUpdateId(Math.toIntExact(userInfo.getUserId()));
        }
        int result;
        //?????????????????????????????????
        if(bo.getPersons()!=null && bo.getPersons().size()>0){
            //logger.error("????????????????????????=====??????????????????????????????");
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediator(bo.getPersons().get(0).getMediator());
            disputeMediationRes.setMediationResult(bo.getMediationResult());
            disputeMediationRes.setIsSuccess("1");
            disputeMediationRes.setMediationId(bo.getPersons().get(0).getMediationId());
            disputeMediationRes.setStatus((short) 1);
            disputeMediationRes.setMediationTel(bo.getPersons().get(0).getMediatorTel());
            disputeMediationRes.setMediationOrgName(bo.getPersons().get(0).getMediationOrgName());
        }else{
            //logger.error("????????????????????????=====?????????????????????????????????");
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediator(bo.getMediator());
            disputeMediationRes.setMediationResult(bo.getMediationResult());
            disputeMediationRes.setIsSuccess(bo.getIsSuccess());
            if(StringUtils.isBlank(bo.getIsSuccess())){
                disputeMediationRes.setIsSuccess("1");
            }
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediationOrgName(bo.getMediationOrgName());
            disputeMediationRes.setStatus((short) 1);
            disputeMediationRes.setMediationTel(bo.getMediationTel());
            disputeMediationRes.setHjCertNumber(bo.getHjCertNumber());
        }
        //?????????????????????????????????
        if(disputeMediation == null){
            //logger.error("????????????????????????");
            //????????????
            bo.setStatus("1");
            //????????????????????????
            if(StringUtils.isEmpty(bo.getDisputeStatus())){ bo.setDisputeStatus("3"); }
            result = disputeMediationMapper.insert(bo);
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationResMapper.insert(disputeMediationRes);
            //????????????
            if(userInfo!=null && StringUtils.isNotBlank(bo.getAccepter())){
                UserDetailScore userdetailscore = new UserDetailScore();
                //???????????????
                userdetailscore.setUserId(userInfo.getUserId());
                //??????????????????
                userdetailscore.setUserName(userInfo.getPartyName());
                //?????????????????????
                userdetailscore.setOrgCode(userInfo.getOrgCode());
                //????????????
                userdetailscore.setDsTime(new Date());
                //?????????
                userdetailscore.setCreateBy(userInfo.getUserId());
                //D1:?????????1??????????????????B1????????????1?????????+??????????????????F1????????????A5??????????????????
                userdetailscore.setScoreType("D1");
                //??????ID ??????????????????id?????????id???
                userdetailscore.setRemark(bo.getMediationId()+"");
                //?????????????????????????????????
                userdetailscore.setSourceRemark(bo.getDisputeEventName()+bo.getHappenTime());
                userScoreService.insertUserDetailScore(userdetailscore);
            }
        }else{
            //logger.error("????????????????????????");
            //????????????????????????
            result = disputeMediationMapper.updateByPrimaryKeySelective(bo);
            disputeMediationResMapper.updateByPrimaryKeySelective(disputeMediationRes);
        }
        //?????????????????????
        if (bo.getInvolvedPeople() != null && bo.getInvolvedPeople().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            involvedPeopleService.deleteInvolvedPeopleByBiz(bo.getMediationId(), InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
            for (InvolvedPeople people : bo.getInvolvedPeople()) {
                if(StringUtils.isBlank(people.getIdCard())){
                    continue;
                }
                people.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
                people.setBizId(bo.getMediationId());
                involvedPeopleService.insertInvolvedPeople(people, false);
            }
        }
        //?????????????????????
        if (bo.getPersons() != null && bo.getPersons().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            Map<String,Object> params = new HashMap<>();
            params.put("mediationId",bo.getMediationId());
            params.put("mediatorType","00");
            long l = mediationCaseMapper.deleteByMediationIdAndType(params);
            for (MediationCase mediationCase : bo.getPersons()) {
                if(StringUtils.isBlank(mediationCase.getMediatorType())){
                    mediationCase.setMediatorType("00");
                }
                mediationCase.setMediationId(bo.getMediationId());
                mediationCaseMapper.insert(mediationCase);
            }
        }
        //?????????????????????
        if (bo.getLeaders() != null && bo.getLeaders().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            Map<String,Object> params = new HashMap<>();
            params.put("mediationId",bo.getMediationId());
            params.put("mediatorType","01");
            long l = mediationCaseMapper.deleteByMediationIdAndType(params);
            for (MediationCase mediationCase : bo.getLeaders()) {
                if(StringUtils.isBlank(mediationCase.getMediatorType())){
                    mediationCase.setMediatorType("01");
                }
                mediationCase.setMediationId(bo.getMediationId());
                mediationCaseMapper.insert(mediationCase);
            }
        }
        return result;
    }

    @Override
    public int insertFlowInfo(List<DisputeFlowInfo> list) throws Exception{
        disputeFlowInfoMapper.deleteByDisputeId(list.get(0).getBizId());
        long l = disputeFlowInfoMapper.insertBatch(list);
        return (int) l;
    }

    @Override
    public DisputeMediation searchByDisputeId(Long disputeId) {
        return disputeMediationMapper.searchByDisputeId(disputeId);
    }

    @Override
    public int deleteByDisputeId(Long disputeId) throws Exception {
        int i = disputeMediationMapper.deleteByDisputeId(disputeId);
        return i;
    }

    @Override
    public List<DisputeFlowInfo> searchFlowList(Long mediationId) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("mediationId",mediationId);
        List<DisputeFlowInfo> disputeFlowInfos = disputeFlowInfoMapper.searchList(params);
        return disputeFlowInfos;
    }

    @Override
    public List<MediationCase> searchCaseList(Long mediationId, String userType) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("mediationId",mediationId);
        params.put("mediatorType",userType);
        List<MediationCase> mediationCases = mediationCaseMapper.searchList(params);
        return mediationCases;
    }

    @Override
    public int insertOrUpdateNoFinish(DisputeMediation bo) throws Exception {
        DisputeMediation disputeMediation = disputeMediationMapper.searchByDisputeId(bo.getDisputeId());
        DisputeMediationRes disputeMediationRes = new DisputeMediationRes();
        int result;
        //?????????????????????????????????
        if(bo.getPersons()!=null && bo.getPersons().size()>0){
            //logger.error("????????????????????????=====??????????????????????????????");
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediator(bo.getPersons().get(0).getMediator());
            disputeMediationRes.setMediationResult(bo.getMediationResult());
            disputeMediationRes.setIsSuccess("1");
            disputeMediationRes.setMediationId(bo.getPersons().get(0).getMediationId());
            disputeMediationRes.setStatus((short) 1);
            disputeMediationRes.setMediationTel(bo.getPersons().get(0).getMediatorTel());
            disputeMediationRes.setMediationOrgName(bo.getPersons().get(0).getMediationOrgName());
        }else{
            //logger.error("????????????????????????=====?????????????????????????????????");
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediator(bo.getMediator());
            disputeMediationRes.setMediationResult(bo.getMediationResult());
            disputeMediationRes.setIsSuccess(bo.getIsSuccess());
            if(StringUtils.isBlank(bo.getIsSuccess())){
                disputeMediationRes.setIsSuccess("1");
            }
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationRes.setMediationOrgName(bo.getMediationOrgName());
            disputeMediationRes.setStatus((short) 1);
            disputeMediationRes.setMediationTel(bo.getMediationTel());
            disputeMediationRes.setHjCertNumber(bo.getHjCertNumber());
        }
        UserBO userInfo = null;
        if(StringUtils.isNotBlank(bo.getAccepter()) && bo.getCreatorId() == null){
            userInfo = userManageOutService.getUserInfoByUUId(bo.getAccepter());
            if(userInfo!=null){
                bo.setCreatorId(userInfo.getUserId());
                bo.setUpdateId(Math.toIntExact(userInfo.getUserId()));
            }else{
                return -2;
            }
        }
        //?????????????????????????????????
        if(disputeMediation == null){
            //????????????
            bo.setStatus("1");
            //????????????????????????
            if(StringUtils.isEmpty(bo.getDisputeStatus())){ bo.setDisputeStatus("2"); }
            result = disputeMediationMapper.insert(bo);
            disputeMediationRes.setMediationId(bo.getMediationId());
            disputeMediationResMapper.insert(disputeMediationRes);
        }else{
            bo.setMediationId(disputeMediation.getMediationId());
            //????????????????????????
            result = disputeMediationMapper.updateByPrimaryKeySelective(bo);
            DisputeMediationRes disputeMediationRes1 = disputeMediationResMapper.selectByMediationId(bo.getMediationId());
            disputeMediationRes.setMediationResId(disputeMediationRes1.getMediationResId());
            disputeMediationResMapper.updateByPrimaryKeySelective(disputeMediationRes);
            if("3".equals(bo.getDisputeStatus())){
                //????????????
                if(userInfo!=null && StringUtils.isNotBlank(bo.getAccepter())){
                    UserDetailScore userdetailscore = new UserDetailScore();
                    //???????????????
                    userdetailscore.setUserId(userInfo.getUserId());
                    //??????????????????
                    userdetailscore.setUserName(userInfo.getPartyName());
                    //?????????????????????
                    userdetailscore.setOrgCode(userInfo.getOrgCode());
                    //????????????
                    userdetailscore.setDsTime(new Date());
                    //?????????
                    userdetailscore.setCreateBy(userInfo.getUserId());
                    //D1:?????????1??????????????????B1????????????1?????????+??????????????????F1????????????A5??????????????????
                    userdetailscore.setScoreType("D1");
                    //??????ID ??????????????????id?????????id???
                    userdetailscore.setRemark(bo.getMediationId()+"");
                    //?????????????????????????????????
                    userdetailscore.setSourceRemark(bo.getDisputeEventName()+bo.getHappenTime());
                    userScoreService.insertUserDetailScore(userdetailscore);
                }
            }
        }
        //?????????????????????
        if (bo.getInvolvedPeople() != null && bo.getInvolvedPeople().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            involvedPeopleService.deleteInvolvedPeopleByBiz(bo.getMediationId(), InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
            for (InvolvedPeople people : bo.getInvolvedPeople()) {
                if(StringUtils.isBlank(people.getIdCard())){
                    continue;
                }
                people.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
                people.setBizId(bo.getMediationId());
                involvedPeopleService.insertInvolvedPeople(people, false);
            }
        }
        //?????????????????????
        if (bo.getPersons() != null && bo.getPersons().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            Map<String,Object> params = new HashMap<>();
            params.put("mediationId",bo.getMediationId());
            params.put("mediatorType","00");
            long l = mediationCaseMapper.deleteByMediationIdAndType(params);
            for (MediationCase mediationCase : bo.getPersons()) {
                if(StringUtils.isBlank(mediationCase.getMediatorType())){
                    mediationCase.setMediatorType("00");
                }
                mediationCase.setMediationId(bo.getMediationId());
                mediationCaseMapper.insert(mediationCase);
            }
        }
        //?????????????????????
        if (bo.getLeaders() != null && bo.getLeaders().size() > 0) {
            //logger.error("?????????????????????????????????=====");
            Map<String,Object> params = new HashMap<>();
            params.put("mediationId",bo.getMediationId());
            params.put("mediatorType","01");
            long l = mediationCaseMapper.deleteByMediationIdAndType(params);
            for (MediationCase mediationCase : bo.getLeaders()) {
                if(StringUtils.isBlank(mediationCase.getMediatorType())){
                    mediationCase.setMediatorType("01");
                }
                mediationCase.setMediationId(bo.getMediationId());
                mediationCaseMapper.insert(mediationCase);
            }
        }
        return result;
    }

    @Override
    public EUDGPagination findDisputeJX(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        List<DisputeMediation> list = disputeMediationMapper.searchDisputeJX(params, rowBounds);
        int count = disputeMediationMapper.countDisputeJX(params);
        list = formatData(list, params.get("orgCode").toString());
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    @Override
    public EUDGPagination findDisputeUserJX(int pageNo, int pageSize, Map<String, Object> params) {
        pageNo = pageNo<1?1:pageNo;
        pageSize = pageSize<1?10:pageSize;
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
        int count = disputeMediationMapper.countDisputeUserJX(params);
        List<Map<String, Object>> list = disputeMediationMapper.searchDisputeUserJX(params, rowBounds);
        EUDGPagination eudgPagination = new EUDGPagination(count, list);
        return eudgPagination;
    }

    private List<DisputeMediation> formatData(List<DisputeMediation> list, String infoOrgCode){
        String disputeTypeDict = "B799";
        List<BaseDataDict> disputeType_9x = new ArrayList<BaseDataDict>();
        List<BaseDataDict> disputeType9x = dictionaryService.getDataDictListOfSinglestage(disputeTypeDict, infoOrgCode);
        disputeType_9x.addAll(disputeType9x);
        for(BaseDataDict baseDataDict : disputeType9x){
            List<BaseDataDict> disputeTypeSubs = dictionaryService.getDataDictListOfSinglestage(baseDataDict.getDictCode(), infoOrgCode);
            disputeType_9x.addAll(disputeTypeSubs);
        }
        List<BaseDataDict> disputeType= dictionaryService.getDataDictListOfSinglestage("B037", infoOrgCode);
        //List<BaseDataDict> disputeType2= dictionaryService.getDataDictListOfSinglestage(disputeTypeDict, infoOrgCode);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("dictPcode",disputeTypeDict);

        List<BaseDataDict> disputeScaleDC= dictionaryService.getDataDictListOfSinglestage(ConstantValue.DISPUTE_SCALE_CODE, infoOrgCode);
        for(DisputeMediation disputeMediation : list){
            DisputeMediationRes disputeMediationRes = disputeMediationResMapper.selectByMediationId(disputeMediation.getMediationId());
            if (disputeMediationRes != null) {
                disputeMediation.setMediationResult(disputeMediationRes.getMediationResult());
            }
            if(disputeMediation.getDisputeStatus() != null){
                if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_ACCEPT)){
                    disputeMediation.setDisputeStatusStr("??????");//1
                }else if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_REPORT)){
                    disputeMediation.setDisputeStatusStr("?????????");//2
                }else if(disputeMediation.getDisputeStatus().equals(ConstantValue.DISPUTESTATUS_CLOSE)){
                    disputeMediation.setDisputeStatusStr("?????????");//3
                }
            }
            if(StringUtils.isNotBlank(disputeMediation.getDisputeScale())){//????????????
                for(BaseDataDict baseDataDict : disputeScaleDC){
                    if(disputeMediation.getDisputeScale().equals(baseDataDict.getDictGeneralCode())){
                        disputeMediation.setDisputeScaleStr(baseDataDict.getDictName());
                    }
                }
            }

            if(null != disputeMediation.getDisputeType2() && StringUtils.isBlank(disputeMediation.getDisputeTypeStr())){
                for (DisputeMediation bo: list) {
                    for (BaseDataDict baseDataDict : disputeType_9x) {
                        if (baseDataDict.getDictGeneralCode().equals(bo.getDisputeType2())) {
                            bo.setDisputeTypeStr(baseDataDict.getDictName());
                            break;
                        }
                    }
                }
            }
            if(null != disputeMediation.getMediationType()){//
                String mediationTypeStr = dictionaryService.changeCodeToName(ConstantValue.MEDIATION_TYPE_CODE, disputeMediation.getMediationType(), null, true);
                disputeMediation.setMediationTypeStr(mediationTypeStr);
            }
            if(null != disputeMediation.getGridPath()) {
                Map<String, String> buildScopeSettingMap = cacheService.getBuildScopeSettingMap();
                if(disputeMediation!=null && StringUtils.isNotBlank(buildScopeSettingMap.get("firstValue"))) {
                    String orgEntityPath = disputeMediation.getGridPath();   //?????????????????????
                    if(StringUtils.isNotBlank(orgEntityPath)&&!orgEntityPath.equals(buildScopeSettingMap.get("firstValue"))) {
                        orgEntityPath = orgEntityPath.replaceAll(buildScopeSettingMap.get("firstValue"), "");
                        disputeMediation.setGridPath(orgEntityPath);
                    }
                    if(StringUtils.isNotBlank(orgEntityPath)&&orgEntityPath.equals(buildScopeSettingMap.get("firstValue"))) {
                        orgEntityPath = orgEntityPath.replaceAll(buildScopeSettingMap.get("firstValue"),buildScopeSettingMap.get("secondValue"));
                        disputeMediation.setGridPath(orgEntityPath);
                    }
                }
            }
        }
        return list;
    }
}
