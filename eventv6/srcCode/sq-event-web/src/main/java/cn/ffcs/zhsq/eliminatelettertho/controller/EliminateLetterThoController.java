package cn.ffcs.zhsq.eliminatelettertho.controller;

import cn.ffcs.common.utils.JsonHelper;
import cn.ffcs.shequ.web.ParamUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoService;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoBizStatusEnum;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoEnum;
import cn.ffcs.zhsq.eliminatelettertho.util.EliminateLetterThoProfessionalTypeEnum;
import cn.ffcs.zhsq.mybatis.domain.common.LayuiPage;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterTho;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.common.utils.StringUtils;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 三书一函主表模块控制器
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 福富软件
 */
@Controller("eliminateLetterThoController")
@RequestMapping("/zhsq/eliminateLetterTho")
public class EliminateLetterThoController extends ZZBaseController {

    private static Logger logger = LoggerFactory.getLogger(EliminateLetterThoController.class);

    @Autowired
    private IEliminateLetterThoService eliminateLetterThoService;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoOutService;

    @Autowired
    private OrgEntityInfoOutService orgEntityInfoOutService;

    @RequestMapping(value = "/form")
    public String form(HttpSession session, HttpServletRequest request, ModelMap map) {

        try {

            map.addAttribute("title", "添加文书");

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            if(logger.isDebugEnabled()) {
                logger.debug("/zhsq/eliminateLetterTho/form, userInfo:{}", JSON.toJSONString(userInfo));
            }

            map.addAttribute("professionalType", EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT.getCode()); //制发单位
            map.addAttribute("bizStatus", EliminateLetterThoBizStatusEnum.DRAFT.getCode()); //草稿

            OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
            Assert.notNull(orgSocialInfoBO, "不匹配的[orgId]");
            Long orgId = orgSocialInfoBO.getParentOrgId();

            List<BaseDataDict> indus =
                    baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS, userInfo.getOrgCode());

            OrgEntityInfoBO orgEntityInfoBO = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(userInfo.getInfoOrgList().get(0).getOrgCode());
            String fbDepartName = orgEntityInfoBO.getOrgName();

            map.addAttribute("letterNo", "【202X】");                                        //文号
            map.addAttribute("letterTypeCode", ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE);         //文书编码
            map.addAttribute("chgTypesCode", ConstantValue.ELIMINATE_LETTER_CHG_TYPE);                  //整改情况
            map.addAttribute("fbDateStr", DateUtils.getToday(DateUtils.PATTERN_24TIME));                //填报时间
            map.addAttribute("fbDepartCode", userInfo.getInfoOrgList().get(0).getOrgCode());            //发布单位地域编码
            map.addAttribute("fbDepartName", fbDepartName);                                             //发布单位地域名称
            map.addAttribute("fbDepartNameDet", userInfo.getOrgName());                                 //具体发布单位名称
            map.addAttribute("indus", indus);                                                           //具体发布单位名称
            map.addAttribute("orgId", orgId);

            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_form.ftl";

        } catch (Exception e) {

            logger.error("/zhsq/eliminateLetterTho/form.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("/zhsq/eliminateLetterTho/form.jhtml异常");

        }

    }

    @RequestMapping(value = "/edit")
    public String edit(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo,String onlyView) {

        try {

            String commitBtnName = "提交";

            String title = "编辑文书";

            Boolean hasRejectBtn = false;

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            bo = eliminateLetterThoService.searchByThoUuid(bo);

            String professionalType = null;
            Long nextPersonOrgid = null;
            String nextPersonTypeName = null;
            if(bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.DRAFT.getCode())) {
                //制发单位草稿 创建表单
                professionalType = EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT.getCode();

            } else if(bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode()) && bo.getFlowStep().equals("task2")) {
                //行业部门编辑表单

                title = "审核文书";

                if(StringUtils.isBlank(bo.getReChgs().getDissentAgree()) || "1".equals(bo.getReChgs().getDissentAgree())) {
                    //制发单位未填写或者同意


                } else {
                    //制发单位驳回

                }

                //制发单位派发给行业部门
                professionalType = EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR.getCode();

            } else if(bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_ISSUING_UNIT.getCode()) && bo.getFlowStep().equals("task3")) {

                //行业部门完成表单，提交给制发单位

                title = "审核文书";

                commitBtnName = "完成";

                hasRejectBtn = true;

                professionalType = EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT.getCode();

                //选人
                List<OrgSocialInfoBO> orgSocialInfoBOs = orgSocialInfoOutService.findOrgSocialListByPara(new HashMap<String, Object>() {
                    {
                        put("regionCode", userInfo.getInfoOrgList().get(0).getOrgCode());
                        put("professionCode",EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode());
                    }
                });

                Assert.noNullElements(orgSocialInfoBOs, "找不到同级扫黑办");

                nextPersonOrgid = orgSocialInfoBOs.get(0).getOrgId();
                nextPersonTypeName = "请选择扫黑办办理人";

            } else if(bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN.getCode())) {

                //当前处于同级扫黑办 制发单位提交给同级扫黑办

                title = "审核文书";

                if(bo.getFlowStep().equals(EliminateLetterThoEnum.COUNTY_AUDIT.getCode())) {

                    //县扫黑办审核 不填写内容 只做审核

                    hasRejectBtn = true;

                    commitBtnName = "上报";

                    nextPersonTypeName = "市扫黑办接收人";

                    OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
                    Long cityOrgId = orgSocialInfoBO.getParentOrgId();
                    orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(cityOrgId);//县orgid
                    Long countryOrgId = orgSocialInfoBO.getParentOrgId();
                    orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(countryOrgId);//市orgid

                    OrgEntityInfoBO orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(orgSocialInfoBO.getOrgCode());

                    List<OrgSocialInfoBO> orgSocialInfoBOs = orgSocialInfoOutService.findOrgSocialListByPara(new HashMap<String, Object>() {
                        {
                            put("regionCode", orgEntityInfoBO.getOrgCode());
                            put("professionCode", EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode());
                        }
                    });

                    Assert.notEmpty(orgSocialInfoBOs, "找不到市扫黑办");

                    nextPersonOrgid = orgSocialInfoBOs.get(0).getOrgId();

                } else if (bo.getFlowStep().equals(EliminateLetterThoEnum.CITY_AUDIT.getCode())){

                    //市扫黑办 不填写内容 只做审核

                    commitBtnName = "归档";

                    hasRejectBtn = true;

                } else {

                    //省扫黑办 不填写内容 只做审核

                    commitBtnName = "归档";

                    nextPersonTypeName = "省扫黑办接收人";

                }

                professionalType = EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode();

            } else if(bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.PENDING_REVIEW_COUNTY_CRACK_DOWN.getCode()) && bo.getFlowStep().equals("task5")) {

                //县级扫黑办提交到市级扫黑办

                title = "审核文书";

                commitBtnName = "归档";

                hasRejectBtn = true;

                professionalType = EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode();

                //下一环节名称
                nextPersonTypeName = "市级扫黑办接收人";

                OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
                Long cityOrgId = orgSocialInfoBO.getParentOrgId();
                orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(cityOrgId);//县orgid
                Long countryOrgId = orgSocialInfoBO.getParentOrgId();
                orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(countryOrgId);//市orgid

                OrgEntityInfoBO orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(orgSocialInfoBO.getOrgCode());

                List<OrgSocialInfoBO> orgSocialInfoBOs = orgSocialInfoOutService.findOrgSocialListByPara(new HashMap<String, Object>() {
                    {
                        put("regionCode", orgEntityInfoBO.getOrgCode());
                        put("professionCode", EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode());
                    }
                });

                Assert.notEmpty(orgSocialInfoBOs, "找不到市扫黑办");

                nextPersonOrgid = orgSocialInfoBO.getOrgId();

            }

            //按钮名称
            map.addAttribute("commitBtnName", commitBtnName);

            //驳回按钮
            map.addAttribute("hasRejectBtn", hasRejectBtn);

            //标题
            map.addAttribute("title", title);

            //处理当前流程的专业
            map.addAttribute("professionalType", professionalType);

            //下一环节办理人
            map.addAttribute("nextPersonOrgid", nextPersonOrgid);

            //下一环节接收人名称
            map.addAttribute("nextPersonTypeName", nextPersonTypeName);

            //业务状态
            map.addAttribute("bizStatus", bo.getBizStatus());

            //接收人员
            OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
            Assert.notNull(orgSocialInfoBO, "不匹配的[orgId]");
            Long orgId = orgSocialInfoBO.getParentOrgId();

            //文书类型
            List<BaseDataDict> letterTypes =
                    baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE, userInfo.getOrgCode());

            String letterTypeName = null;
            for(Iterator<BaseDataDict> iterator = letterTypes.iterator(); iterator.hasNext();) {
                BaseDataDict dataDict = iterator.next();
                if(dataDict.getDictGeneralCode().equals(bo.getLetterType())) {
                    letterTypeName = dataDict.getDictName();
                    break;
                }
            }

            //行业、领域类型
            List<BaseDataDict> indus =
                    baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS, userInfo.getOrgCode());


            map.addAttribute("thoUuid", bo.getThoUuid());                                                   //uuid
            map.addAttribute("thtNo", bo.getThtNo());                                                       //编码
            map.addAttribute("letterType", bo.getLetterType());                                             //文书类型
            map.addAttribute("letterTypeName", letterTypeName);                                             //文书类型
            map.addAttribute("letterTypeCode", ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE);             //文书编码
            map.addAttribute("chgTypesCode", ConstantValue.ELIMINATE_LETTER_CHG_TYPE);                      //整改情况
            map.addAttribute("fbDateStr", DateUtils.formatDate(bo.getFbDate(), DateUtils.PATTERN_24TIME));  //发出时间

            OrgEntityInfoBO orgEntityInfoBO = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(bo.getFbDepartCode());
            String fbDepartName = orgEntityInfoBO.getOrgName();

            map.addAttribute("fbDepartCode", bo.getFbDepartCode());                                         //发布单位地域编码
            map.addAttribute("fbDepartName", fbDepartName);                                                 //发布单位地域名称
            map.addAttribute("fbDepartNameDet", bo.getFbDepartNameDet());                                   //具体发布单位名称
            map.addAttribute("reUser", bo.getReChgs().getReUser());                                         //接收人json
            map.addAttribute("reUserName", bo.getReChgs().getReUserName());                                 //接收人
            map.addAttribute("reUserId", bo.getReChgs().getReUserId());                                     //接收人id
            map.addAttribute("reDepartCode", bo.getReChgs().getReDepartCode());                             //接收单位编码
            map.addAttribute("reDepartNameDet", bo.getReChgs().getReDepartNameDet());                       //具体接收单位名称
            String reDateStr = DateUtils.formatDate(bo.getReChgs().getReDate(), DateUtils.PATTERN_24TIME);
            if(StringUtil.isBlank(reDateStr)
                    && bo.getBizStatus().equals(EliminateLetterThoBizStatusEnum.WAITING_FEEDBACK_INDUSTRY_SECTOR.getCode())) {
                reDateStr = DateUtils.getToday(DateUtils.PATTERN_24TIME);
            }
            map.addAttribute("reDateStr", reDateStr); //接收时间
            map.addAttribute("letterNo", bo.getLetterNo());                                                 //文号
            map.addAttribute("caseName", bo.getCaseName());                                                 //案件名称
            map.addAttribute("caseNo", bo.getCaseNo());                                                     //案件编码
            map.addAttribute("letterContentClob", bo.getLetterContentClob());                               //文书内容
            map.addAttribute("industrialComment", bo.getIndustrialComment());                               //行业、领域补充说明
            map.addAttribute("indus", indus);                                                               //行业、领域
            map.addAttribute("checkedIndus", bo.getInduss());                                               //选中的行业、领域
            map.addAttribute("orgId", orgId);

            if(!StringUtil.isBlank(bo.getReChgs().getReType())) {
                map.addAttribute("reType", bo.getReChgs().getReType());                                     //回复情况
                map.addAttribute("reTypeName", bo.getReChgs().getReType().equals("1") ? "按期回复" : "未按期回复"); //回复情况
                map.addAttribute("reDetail", bo.getReChgs().getReDetail());                                 //回复详情
            }

            if(!StringUtil.isBlank(bo.getReChgs().getReDissentAgree())) {
                map.addAttribute("reDissentAgree", bo.getReChgs().getReDissentAgree());                     //行业部门提出异议
                map.addAttribute("reDissentAgreeName", bo.getReChgs().getReDissentAgree().equals("1") ? "是" : "否");//行业部门提出异议
                map.addAttribute("reDissentDetail", bo.getReChgs().getReDissentDetail());                   //行业部门异议详情
            } else {
                map.addAttribute("reDissentAgree", null);                       //行业部门提出异议
                map.addAttribute("reDissentAgreeName", "否");                    //行业部门提出异议
                map.addAttribute("reDissentDetail", null);                      //行业部门异议详情
            }

            if(!StringUtil.isBlank(bo.getReChgs().getDissentAgree())) {
                map.addAttribute("dissentAgree", bo.getReChgs().getDissentAgree());                         //是否同意异议
                map.addAttribute("dissentAgreeName", bo.getReChgs().getDissentAgree().equals("1") ? "否" : "是");//是否同意异议
            }

            if(!StringUtil.isBlank(bo.getReChgs().getDissentDetail())) {
                map.addAttribute("dissentDetail", bo.getReChgs().getDissentDetail());                       //异议详情
            }

            if(!StringUtil.isBlank(bo.getReChgs().getChgType())) {

                //行业、领域类型
                List<BaseDataDict> chgTypeDataDicts =
                        baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_CHG_TYPE, userInfo.getOrgCode());

                String chgTypeName = null;
                for(BaseDataDict baseDataDict : chgTypeDataDicts) {
                    if(baseDataDict.getDictGeneralCode().equals(bo.getReChgs().getChgType())) {
                        chgTypeName = baseDataDict.getDictName();
                        break;
                    }
                }

                map.addAttribute("chgType", bo.getReChgs().getChgType());                                   //整改情况
                map.addAttribute("chgTypeName", chgTypeName);                                               //整改情况
                map.addAttribute("chgDetail", bo.getReChgs().getChgDetail());                               //整改详情
            }

            if(!StringUtil.isBlank(bo.getReChgs().getIndusChgAgree())) {
                map.addAttribute("indusChgAgree", bo.getReChgs().getIndusChgAgree());                      //是否开展行业治理
                map.addAttribute("indusChgAgreeName", bo.getReChgs().getIndusChgAgree().equals("1") ? "是" : "否");//是否开展行业治理
                map.addAttribute("indusChgDetail", bo.getReChgs().getIndusChgDetail());                    //行业治理情况详情
            }

            if(!StringUtil.isBlank(bo.getReChgs().getLongActionAgree())) {
                map.addAttribute("longActionAgree", bo.getReChgs().getLongActionAgree());                   //是否建立长效机制
                map.addAttribute("longActionAgreeName", bo.getReChgs().getLongActionAgree().equals("1") ? "是" : "否");//是否建立长效机制
                map.addAttribute("longActionDetail", bo.getReChgs().getLongActionDetail());                 //长效机制详情
            }

            map.addAttribute("othClob", bo.getReChgs().getOthClob());                                       //其他情况说明

            map.put("onlyView",onlyView);//跨域跳转关键词

            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_form.ftl";

        } catch (Exception e) {

            logger.error("/zhsq/eliminateLetterTho/form.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("/zhsq/eliminateLetterTho/form.jhtml异常");

        }

    }

    @RequestMapping(value = "/detail")
    public String detail(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo) {

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.detail(map, bo, userInfo);

            map.addAttribute("isBigScreen", request.getParameter("isBigScreen"));

            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_detail.ftl";

        } catch (Exception e) {

            logger.error("/zhsq/eliminateLetterTho/detail.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("/zhsq/eliminateLetterTho/detail.jhtml异常");

        }

    }

    @ResponseBody
    @RequestMapping(value = "/saveRaft")
    public Map<String, Object> saveRaft(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.saveRaft(bo, userInfo);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/saveRaft.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/startWorkFlow")
    public Map<String, Object> startWorkFlow(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.startWorkFlow(bo, userInfo);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/startWorkFlow.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/commitWorkFlow/{type}")
    public Map<String, Object> commitWorkFlow(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo, @PathVariable(name = "type") String type) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.commitWorkFlow(bo, userInfo, type);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/commitWorkFlow.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/reject/{type}")
    public Map<String, Object> reject(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo, @PathVariable(name = "type") String type) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.reject(bo, userInfo, type);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/reject.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public Map<String, Object> delete(HttpSession session, HttpServletRequest request, ModelMap map, EliminateLetterTho bo) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.delete(bo, userInfo);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/delete.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }

    @ResponseBody
    @RequestMapping(value = "/deleteBatch")
    public Map<String, Object> deleteBatch(HttpSession session, HttpServletRequest request, ModelMap map, @RequestBody List<EliminateLetterTho> thos) {

        Map<String, Object> result = new HashMap<>();
        boolean success = false;
        String message = "";
        Object data = null;

        try {

            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

            eliminateLetterThoService.deleteBatch(thos, userInfo);

            success = true;

        } catch (Exception e) {

            message = StringUtil.isBlank(e.getMessage()) ? e.getClass().toString() : e.getMessage();
            logger.error("/zhsq/eliminateLetterTho/deleteBatch.jhtml,{}", e.getClass(), e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        return result;

    }


    /**
     * 新增文书列表页
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/add/index")
    public String myAddList(HttpSession session, HttpServletRequest request, ModelMap map) {
        try {
            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
            putListPageParam(map,userInfo);
            map.put("moduleName","新增文书");
            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_add_list.ftl";
        } catch (Exception e) {

            logger.error("/zhsq/eliminateLetterTho/eliminate_letter_tho_add_list.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("/zhsq/eliminateLetterTho/eliminate_letter_tho_add_list.jhtml异常");

        }
    }

    /**
     * 新增文书列表数据
     * @param request
     * @param map
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/add/listData")
    public Object myAddListData(HttpServletRequest request,ModelMap map, LayuiPage page,EliminateLetterTho bo){
        EUDGPagination pagination = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
            setSearchListCommonParams(request,params,bo);
            params.put("creator", userInfo.getUserId());
            pagination = eliminateLetterThoService.searchList(page,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 待办文书列表
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/wait/index")
    public String waitList(HttpSession session, HttpServletRequest request, ModelMap map) {
        EUDGPagination pagination = null;
        String LIST_TYPE_AUDIT = "audit";//审核列表标识
        try {
            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
            putListPageParam(map,userInfo);
            String listType = ParamUtils.getString(request,"listType");
            if(LIST_TYPE_AUDIT.equals(listType)){
                map.put("moduleName","文书审核");
            }else{
                map.put("moduleName","待办文书");
            }
            map.put("LIST_TYPE",listType);
            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_wait_list.ftl";
        } catch (Exception e) {
            logger.error("/zhsq/eliminateLetterTho/wait_list.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("eliminate_letter_tho_wait_list.ftl异常");
        }
    }

    /**
     * 待办文书列表数据
     * @param request
     * @param map
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/wait/listData")
    public Object waitListData(HttpServletRequest request,ModelMap map, LayuiPage page,EliminateLetterTho bo){
        EUDGPagination pagination = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
            setSearchListCommonParams(request,params,bo);
            params.put("curUserId", userInfo.getUserId());
            params.put("curOrgId", userInfo.getOrgId());
//            String bizStatusString = ParamUtils.getString(request,"bizStatusString","99");
//            String[] bizStatusArr = bizStatusString.split(",");
//            params.put("bizStatusArr",bizStatusArr);
            pagination = eliminateLetterThoService.searchWaitList(page,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 待办文书列表数据（不分页），提供给捷平使用
     * @param request
     * @param curUserId ： 必传，登录用户的用户ID
     * @param curOrgId ： 必传，登录用户所属的组织ID
     * @param curOrgCode ： 必传，登录用户所属的组织编码
     * @param jsoncallback
     * @return 待办列表数据
     */
    @ResponseBody
    @RequestMapping("/waitListDataForJsonp")
    public Object waitListDataForJsonp(HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            //setSearchListCommonParams(request,params,bo);
            params.put("curUserId", ParamUtils.getString(request,"curUserId"));
            params.put("curOrgId", ParamUtils.getString(request,"curOrgId"));
            String curOrgCode = ParamUtils.getString(request, "curOrgCode");

            List<EliminateLetterTho> waitList = eliminateLetterThoService.searchWaitListAll(params);
            resultMap.put("waitList",waitList);
            List<BaseDataDict> indusCodeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS,curOrgCode);
            resultMap.put("indusCodeDictList", indusCodeDictList);
            //resultMap.put("indusCodeDictListJson", JSONObject.toJSONString(indusCodeDictList));
            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 辖区文书列表
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/jurisdiction/index")
    public String jurisdictionList(HttpSession session, HttpServletRequest request, ModelMap map) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        putListPageParam(map,userInfo);
        map.put("moduleName","辖区文书");
        return "/zzgl/eliminateLetterTho/eliminate_letter_tho_jurisdiction_list.ftl";
    }

    /**
     * 辖区文书列表数据
     * @param request
     * @param map
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/jurisdiction/listData")
    public Object jurisdictionListData(HttpServletRequest request,ModelMap map, LayuiPage page,EliminateLetterTho bo){
        EUDGPagination pagination = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
            setSearchListCommonParams(request, params,bo);
            String selectedThoUuidStr = ParamUtils.getString(request,"selectedThoUuidStr","");
            if(StringUtils.isNotEmpty(selectedThoUuidStr)){
                params.put("selectedThoUuidArr",selectedThoUuidStr.split(","));
            }
            String selectedThoIdStr = ParamUtils.getString(request,"selectedThoIdStr","");
            if(StringUtils.isNotEmpty(selectedThoIdStr)){
                params.put("selectedThoIdArr",selectedThoIdStr.split(","));
            }
//            String bizStatusString = ParamUtils.getString(request,"bizStatusString","99");
//            String[] bizStatusArr = bizStatusString.split(",");
//            params.put("bizStatusArr",bizStatusArr);
            pagination = eliminateLetterThoService.searchJurisdictionList(page,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 归档文书列表
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value="/archive/index")
    public String archiveList(HttpSession session, HttpServletRequest request, ModelMap map) {
        try {
            UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
            putListPageParam(map,userInfo);
            return "/zzgl/eliminateLetterTho/eliminate_letter_tho_archive_list.ftl";
        } catch (Exception e) {

            logger.error("/zhsq/eliminateLetterTho/archive_list.jhtml,{}", e.getClass(), e);
            throw new RuntimeException("/zhsq/eliminateLetterTho/archive_list.jhtml异常");

        }
    }

    /**
     * 归档文书列表数据
     * @param request
     * @param map
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/archive/listData")
    public Object archiveListData(HttpServletRequest request,ModelMap map, LayuiPage page,EliminateLetterTho bo){
        EUDGPagination pagination = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            //UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
            setSearchListCommonParams(request, params,bo);
            String bizStatusString = ParamUtils.getString(request,"bizStatusString", EliminateLetterThoBizStatusEnum.FILE.getCode());
            String[] bizStatusArr = bizStatusString.split(",");
            params.put("bizStatusArr",bizStatusArr);


            String selectedThoUuidStr = ParamUtils.getString(request,"selectedThoUuidStr","");
            if(StringUtils.isNotEmpty(selectedThoUuidStr)){
                params.put("selectedThoUuidArr",selectedThoUuidStr.split(","));
            }
            String selectedThoIdStr = ParamUtils.getString(request,"selectedThoIdStr","");
            if(StringUtils.isNotEmpty(selectedThoIdStr)){
                params.put("selectedThoIdArr",selectedThoIdStr.split(","));
            }

            pagination = eliminateLetterThoService.searchArchiveList(page,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagination;
    }

    /**
     * 列表页面通用参数赋值
     * @param map
     * @param userInfo
     */
    private void putListPageParam(ModelMap map, UserInfo userInfo){
        map.put("regionCode",userInfo.getInfoOrgList().get(0).getOrgCode());
        map.put("regionName",userInfo.getInfoOrgList().get(0).getOrgName());
        map.put("letterTypeDict",ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE);
        List<BaseDataDict> indusCodeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ELIMINATE_LETTER_THO_INDUS,null);
        map.put("indusCodeDictList", JSONObject.toJSONString(indusCodeDictList));
    }

    /**
     * 列表查询参数赋值
     * @param request
     * @param params
     * @param bo
     */
    private void setSearchListCommonParams(HttpServletRequest request, Map<String, Object> params, EliminateLetterTho bo){
        params.put("regionCode", bo.getRegionCode());
        params.put("letterType", bo.getLetterType());
        params.put("fbDepartNameDet", bo.getFbDepartNameDet());
        params.put("fbDateStart", bo.getFbDateStart());
        params.put("fbDateEnd", bo.getFbDateEnd());
    }



    /**
     * 三书一函大屏接口
     * 左上角统计总数部分
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/statisticsMainCount")
    public String statisticsMainCountForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            String regionCode = ParamUtils.getString(request,"regionCode");
            if(StringUtils.isNotEmpty(regionCode)){
                params.put("regionCode", regionCode);
            }
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.statisticsMainCount(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口
     * 行业领域分布情况
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getIndusDistribution")
    public String getIndusDistributionForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> indusList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            String regionCode = ParamUtils.getString(request,"regionCode");
            if(StringUtils.isNotEmpty(regionCode)){
                params.put("regionCode", regionCode);
            }
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getIndusDistribution(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口
     * 文书类型分布情况
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getLetterTypeDistribution")
    public String getLetterTypeDistributionForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> letterTypeList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            String regionCode = ParamUtils.getString(request,"regionCode");
            if(StringUtils.isNotEmpty(regionCode)){
                params.put("regionCode", regionCode);
            }
            params.put("searchType",ParamUtils.getString(request,"searchType"));
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getLetterTypeDistribution(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口
     * 部门排行情况
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getDeptRankList")
    public String getDeptRankListForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> letterTypeList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            String regionCode = ParamUtils.getString(request,"regionCode");
            String provinceOrgCode = ParamUtils.getString(request,"provinceOrgCode");
            params.put("rankType", ParamUtils.getString(request,"rankType",""));
/*            //根据地域编码查找部门
            OrgSocialInfoBO baseOrg = this.getBaseOrgByRegionCodeAndProfessionCode(regionCode,EliminateLetterThoProfessionalTypeEnum.BASE.getCode());
            Assert.notNull(baseOrg, "baseOrg is null,查找不到匹配的组织信息");
            params.put("orgCode", baseOrg.getOrgCode());
            params.put("chiefLevel", baseOrg.getChiefLevel());*/
            params.put("regionCode",regionCode);
            params.put("provinceOrgCode",provinceOrgCode);
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getDeptRankList(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口
     * 根据部门获取文书类型分布情况
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getLetterTypeDistributionByDeptCode")
    public String getLetterTypeDistributionByDeptCodeForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> letterTypeList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            params.put("provinceOrgCode",ParamUtils.getString(request,"provinceOrgCode"));
            params.put("professionCode",ParamUtils.getString(request,"professionCode"));
            params.put("regionCode", ParamUtils.getString(request,"regionCode"));
            params.put("searchType", ParamUtils.getString(request,"searchType"));
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getLetterTypeDistributionByDeptCode(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏部门排行弹框
     * 根据专业编码查找文书地域分布情况(部门排行弹框使用)
     * @param session
     * @param request
     * @return 文书地域分布情况
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getAreaDistributionByProfessionCode")
    public String getAreaDistributionByProfessionCode(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> letterTypeList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            params.put("provinceOrgCode",ParamUtils.getString(request,"provinceOrgCode"));
            params.put("professionCode",ParamUtils.getString(request,"professionCode"));
            params.put("regionCode", ParamUtils.getString(request,"regionCode"));
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getAreaDistributionByProfessionCode(params);
            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口中间地图部分
     * 根据地域编码获取下级文书总数
     * @param session
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getTotalDistributionByRegionCode")
    public String getTotalDistributionByRegionCodeForJsonp(HttpSession session, HttpServletRequest request,String jsoncallback){
        Map<String,Object> resultMap = null;
        //List<Map<String, Object>> letterTypeList = new ArrayList<>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();

            String regionCode = ParamUtils.getString(request,"regionCode");
            if(StringUtils.isNotEmpty(regionCode)){
                params.put("regionCode", regionCode);
            }
            String searchType = ParamUtils.getString(request,"searchType");
            params.put("searchType", searchType);
            putCommonParamsForJsonp(request,params);
            resultMap = eliminateLetterThoService.getTotalDistributionByRegionCode(params);

            if(StringUtils.isEmpty(jsoncallback)){
                jsoncallback =  request.getParameter("callback");
            }
            jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsoncallback;
    }

    /**
     * 三书一函大屏接口
     * 文书列表
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/searchPage")
    public void searchPageForJsonp(
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response,
            String callback
    ) throws Exception {

        Map<String, Object> result = new HashMap<>();

        Boolean success = false;
        String message = "";
        Object data = null;

        try {

            Map<String, Object> params = new HashMap<>();

            params.put("page", ParamUtils.getString(request,"page"));
            params.put("rows", ParamUtils.getString(request,"rows"));
            params.put("queryType", ParamUtils.getString(request,"queryType"));
            params.put("regionCode", ParamUtils.getString(request,"regionCode"));
            params.put("professionCode", ParamUtils.getString(request,"professionCode"));
            params.put("provinceOrgCode", ParamUtils.getString(request,"provinceOrgCode"));

            putCommonParamsForJsonp(request, params);

            data = eliminateLetterThoService.searchPage(params);
            success = true;

        } catch (Exception e) {

            success = false;

            message = new StringBuilder()
                    .append("/zhsq/eliminateLetterTho/getListByCondition")
                    .append(",className:[").append(e.getClass().getName()).append("]")
                    .append(",message:[").append(e.getMessage()).append("]")
                    .append(",currentTimeMillis:[").append(System.currentTimeMillis()).append("]")
                    .toString();

            logger.error(message, e);

        }

        result.put("success", success);
        result.put("message", message);
        result.put("data", data);

        String outString =
                new StringBuffer()
                        .append(callback)
                        .append("(")
                        .append(JSONObject.toJSONString(result))
                        .append(")")
                        .toString();

        outJosn(response, outString);

    }

    private OrgSocialInfoBO getBaseOrgByRegionCodeAndProfessionCode(String regionCode,String professionCode){
        Map<String,Object> orgParams = new HashMap<String,Object>();
        orgParams.put("regionCode",regionCode);
        orgParams.put("professionCodeArr",
                new String[]{
                        EliminateLetterThoProfessionalTypeEnum.BASE.getCode(),
                        EliminateLetterThoProfessionalTypeEnum.ISSUING_UNIT.getCode(),
                        EliminateLetterThoProfessionalTypeEnum.INDUSTRY_SECTOR.getCode(),
                        EliminateLetterThoProfessionalTypeEnum.ANTI_MAFIA_OFFICE.getCode()
                });
        List<OrgSocialInfoBO> orgList = orgSocialInfoOutService.findOrgSocialListByPara(orgParams);
        List<OrgSocialInfoBO> baseOrgList = orgList.stream().filter(obj -> professionCode.equals(obj.getProfessionCode())).collect(Collectors.toList());
        OrgSocialInfoBO org = baseOrgList.get(0);
        return org;
    }

    private void putCommonParamsForJsonp(HttpServletRequest request,Map<String,Object> params) throws Exception{
        String  createTimeStartStr = ParamUtils.getString(request,"createTimeStartStr");
        String  createTimeEndStr = ParamUtils.getString(request,"createTimeEndStr");
        if(StringUtils.isNotEmpty(createTimeStartStr)){
            params.put("createTimeStart",DateUtils.convertStringToDate(createTimeStartStr,DateUtils.PATTERN_DATE));
        }
        if(StringUtils.isNotEmpty(createTimeEndStr)){
            params.put("createTimeEnd",DateUtils.convertStringToDate(createTimeEndStr,DateUtils.PATTERN_DATE));
        }
        params.put("letterType", ParamUtils.getString(request,"letterType"));
    }

}