/**
 *
 */
package cn.ffcs.zhsq.base.controller;

import cn.ffcs.common.DictPcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.CiRsTop;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.CultMemberRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.DangerousGoodsRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.DrugRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.GuardianBizType;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.KeyPersonnel;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.MentalIllnessRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.MlmRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.PetitionRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.ReleasedRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.YouthPerson;
import cn.ffcs.shequ.resident.service.IResidentService;
import cn.ffcs.shequ.utils.DataDictHelper;
import cn.ffcs.shequ.zzgl.service.crowd.ICorrectionalRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.ICultMemberRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IDangerousGoodsRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IDrugRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IMentalIllnessRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IPetitionRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IReleasedRecordService;
import cn.ffcs.shequ.zzgl.service.crowd.IYouthService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.AidsPerson;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.CorrectionalRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.InvolPerson;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.Petitioner;
import cn.ffcs.system.publicUtil.StringUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.crowd.IAidsPersonService;
import cn.ffcs.shequ.zzgl.service.crowd.IPetitionerService;
import cn.ffcs.shequ.zzgl.service.grid.IAreaRoomRentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 居民控制器
 *
 * @author guohh
 */
@Controller
@RequestMapping(value = "/resident")
public class ResidentController extends ZZBaseController {

    @Autowired
    private IResidentService residentService;
    @Autowired
    private PartyIndividualService partyIndividualService;
    @Autowired
    private CiRsService cirsService;
    @Autowired
    private IMentalIllnessRecordService mentalIllnessRecordService;
    @Autowired
    private IDrugRecordService drugRecordService;
    @Autowired
    private IPetitionRecordService petitionRecordService;
    @Autowired
    private IReleasedRecordService releasedRecordService;
    @Autowired
    private IBaseDictionaryService dictionaryService;
    @Autowired
    private ICultMemberRecordService cultMemberRecordService;
   /* @Autowired
    private IVisitArrangeService visitArrangeService;*/
    @Autowired
    private ICorrectionalRecordService correctionalRecordService;
    @Autowired
    private IDangerousGoodsRecordService dangerousGoodsRecordService;
    /*@Autowired
    private IMlmRecordService service1;*/
    @Autowired
    private IYouthService youthService;
   /* @Autowired
    private IKeyPersonnelService personnelService;*/
    @Autowired
    private IAidsPersonService aidsService;
    @Autowired
    private IPetitionerService petitionerService;
    /*@Autowired
    private IInvolPersonService involService;*/
    /*@Autowired
    private IPetitionEventService petitionEventService;*/
    @Autowired
    private IMixedGridInfoService gridInfoService;
    @Autowired
    private IAreaRoomRentService areaRoomRentService;
    @Autowired
    private IBaseDictionaryService dictService;

    /**
     * 居民头像，先读blob字段，再读photoUrl，再默认图片
     *
     * @param session
     * @param response
     * @param ciRsId
     * @return
     */
    @RequestMapping(value = "/photo/{ciRsId}")
    public ModelAndView createRisk(HttpSession session, HttpServletResponse response, @PathVariable(value = "ciRsId") long ciRsId) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.reset();// 清空输出流
            response.setContentType("image/png");// 定义输出类型
            //-- 取居民数据，优先读取头像路径
            boolean usePhotoUrl = true; // 标识是否使用头像照片路径
            String photoPath = session.getServletContext().getRealPath("/") + "images/untitled.png"; // 默认头像
            Map<String, Object> residentDetail = residentService.getResidentDetail(ciRsId);
            //-- 新接口,头像由partyIndividualService服务提供 modify by zhongshm 2013.10.16
            PartyIndividual partyIndividual = partyIndividualService.findById(Long.parseLong(residentDetail.get("PARTY_ID").toString()));
            if (partyIndividual != null && partyIndividual.getPicUrl() != null) {
                try {
                    String photoUrl = partyIndividual.getPicUrl();
                    if (photoUrl.indexOf("http") != -1) {
                        //  /mnt/mfs/sq_upload/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
                        //  http://img.cnsq.org/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
                        int end = photoUrl.indexOf("/rs/perfile");
                        String needReplaceStr = photoUrl.substring(0, end + 1);
                        photoUrl = photoUrl.replaceAll(needReplaceStr, ConstantValue.getRootResourseSavePath());
                    } else if (photoUrl.indexOf("/rs/perfile") != -1) {
                        photoUrl = ConstantValue.getRootResourseSavePath() + photoUrl;
                    } else if (photoUrl.indexOf("/ictfile/upload") != -1) {
                        photoUrl = photoUrl.replace("\\", "/");
                        photoUrl = ConstantValue.getRootResourseSavePath() + photoUrl;
                    } else {
                        photoUrl = ConstantValue.RESOURSE_SAVE_ROOT_PATH + photoUrl;
                    }
                    File tmpFile = new File(photoUrl);
                    if (tmpFile.exists()) photoPath = photoUrl;
                } catch (Exception e) {
                    e.printStackTrace();
                    usePhotoUrl = false;
                }
            } else usePhotoUrl = false;
            //-- 取TOP表的的头像
//			if(residentDetail!=null && residentDetail.get("I_PHOTO_URL")!=null) {
//				try {
//					String photoUrl = residentDetail.get("I_PHOTO_URL").toString();
//					if(photoUrl.indexOf("http")!=-1) {
//						//  /mnt/mfs/sq_upload/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
//						//  http://img.cnsq.org/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
//						int end = photoUrl.indexOf("/rs/perfile");
//						String needReplaceStr = photoUrl.substring(0, end+1);
//						photoUrl = photoUrl.replaceAll(needReplaceStr, ConstantValue.getRootResourseSavePath());
//					} else if(photoUrl.indexOf("/rs/perfile")!=-1) {
//						photoUrl = ConstantValue.getRootResourseSavePath()+photoUrl;
//					} else {
//						photoUrl = ConstantValue.RESOURSE_SAVE_ROOT_PATH+photoUrl;
//					}
//					File tmpFile = new File(photoUrl);
//					if(tmpFile.exists()) photoPath = photoUrl;
//				} catch (Exception e) {
//					e.printStackTrace();
//					usePhotoUrl = false;
//				}
//			} else usePhotoUrl = false;
            //-- 如果头像路径未读取到或者读取出错，则读取存在数据库中的头像数据
            //-- 如果未读取到头像数据，则使用默认头像
            if (!usePhotoUrl) {
                try {
                    byte photoData[] = residentService.getResidentPhoto(ciRsId);
                    if (photoData != null) outputStream.write(photoData);
                    else usePhotoUrl = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    usePhotoUrl = true;
                }
            }
            if (usePhotoUrl) {
                File photoFile = new File(photoPath);
                byte[] tempbytes = new byte[1024];
                int byteread = 0;
                FileInputStream in = new FileInputStream(photoFile);
                while ((byteread = in.read(tempbytes)) != -1) {
                    outputStream.write(tempbytes, 0, byteread);
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("居民头像读取异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 居民详细信息
     *
     * @param session
     * @param ciRsId
     * @param map
     * @return
     */
    @RequestMapping(value = "/detail/{ciRsId}")
    public String detailInfo(HttpSession session, @PathVariable(value = "ciRsId") Long ciRsId, ModelMap map) {
        String url = App.RS.getDomain(session);
        map.addAttribute("csId", ciRsId);
        map.addAttribute("rsUrl", url);
        return "/resident/residentDetail.ftl";
    }

    @RequestMapping(value = "/detail62/{ciRsId}")
    public String detail62Info(HttpSession session, @PathVariable(value = "ciRsId") Long ciRsId, ModelMap map) {
        String url = App.RS.getDomain(session);
        map.addAttribute("csId", ciRsId);
        map.addAttribute("rsUrl", url);
        return "/resident/residentDetail-62.ftl";
    }

    @RequestMapping(value = "/crowd/d/{ciRsId}")
    public String detailCrowd(HttpServletRequest req, @PathVariable(value = "ciRsId") Long ciRsId, String crowdType) {
        if (crowdType.equals(DrugRecord.REL_CODE)) {
            DrugRecord r = drugRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/drug/detail.jhtml?drId=" + r.getDrId();
            }
        } else if (crowdType.equals(ReleasedRecord.REL_CODE)) {
            ReleasedRecord r = releasedRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/releasedRecord/detail.jhtml?releasedId=" + r.getReleasedId();
            }
        } else if (crowdType.equals(CorrectionalRecord.REL_CODE)) {
            CorrectionalRecord r = correctionalRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/correctional/detail.jhtml?coId=" + r.getCoId();
            }
        } else if (crowdType.equals(CultMemberRecord.REL_CODE)) {
            CultMemberRecord r = cultMemberRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/cultMember/detail.jhtml?cultId=" + r.getCultId();
            }
        } else if (crowdType.equals(PetitionRecord.REL_CODE)) {
            PetitionRecord r = petitionRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/petitionRecord/detail.jhtml?prId=" + r.getPrId();
            }
        } else if (crowdType.equals(DangerousGoodsRecord.REL_CODE)) {
            DangerousGoodsRecord r = dangerousGoodsRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/dangerousGoods/detail.jhtml?recordId=" + r.getRecordId();
            }
        } else if (crowdType.equals(MentalIllnessRecord.REL_CODE)) {
            MentalIllnessRecord r = mentalIllnessRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/mentalIllnessRecord/detail.jhtml?miId=" + r.getMiId();
            }
        } else if (crowdType.equals(AidsPerson.REL_CODE)) {
            AidsPerson r = aidsService.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/aids/detail.jhtml?id=" + r.getId();
            }
        } else if (crowdType.equals(YouthPerson.REL_CODE)) {
            YouthPerson r = youthService.findByCiRsId(ciRsId, null);
            if (r != null) {
                return "redirect:/zzgl/crowd/youth/detail.jhtml?id=" + r.getId();
            }
        } /*else if (crowdType.equals(MlmRecord.REL_CODE)) {
            MlmRecord r = service1.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/mlm/detail.jhtml?id=" + r.getId();
            }
        } else if (crowdType.equals(KeyPersonnel.REL_CODE)) {
            KeyPersonnel r = personnelService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/keyPer/detail.jhtml?keyId=" + r.getKeyId();
            }
        }*/else if (crowdType.equals(Petitioner.REL_CODE)) {
            Petitioner r = petitionerService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/petitioner/detail.jhtml?miId=" + r.getMiId();
            }
        } /*else if (crowdType.equals(InvolPerson.REL_CODE)) {
            InvolPerson r = involService.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/invol/detail.jhtml?id=" + r.getId();
            }
        } */else if (crowdType.equals("S")) {  //2253733
            Long rentId = areaRoomRentService.getRentIdByCirsIdOfHier(ciRsId);
            if (rentId != null) {
                return "redirect:/zzgl/grid/areaRoomRent/view.jhtml?rentId=" + rentId;
            } else {
                return null;
            }
        }

        return null;
    }

    @RequestMapping(value = "/crowd/ce/{ciRsId}")
    public String createOrEditCrowd(HttpServletRequest req, @PathVariable(value = "ciRsId") Long ciRsId, String crowdType) {
        if (crowdType.equals(DrugRecord.REL_CODE)) {
            DrugRecord r = drugRecordService.findByCiRsId(ciRsId);
            if (r != null) {// 2017-11-22 Modify By YangCQ 改为吸毒国标
                return "redirect:/zzgl/crowd/drug/toAdd.jhtml?from=rs&drId=" + r.getDrId();
            }
            return "redirect:/zzgl/crowd/drug/toAdd.jhtml?from=rs&ciRsId=" + ciRsId;
        } else if (crowdType.equals(ReleasedRecord.REL_CODE)) {// 2017-11-22 Modify By YangCQ 改为刑释解教国标
            ReleasedRecord r = releasedRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/releasedRecord/edit_nanchang.jhtml?from=rs&releasedId=" + r.getReleasedId();
            }
            return "redirect:/zzgl/crowd/releasedRecord/create_nanchang.jhtml?from=rs&cirsId=" + ciRsId;
        } else if (crowdType.equals(CorrectionalRecord.REL_CODE)) {// 2017-11-22 Modify By YangCQ 改为矫正国标
            CorrectionalRecord r = correctionalRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/correctional/edit.jhtml?from=rs&standard=standard&coId=" + r.getCoId();
            }
            return "redirect:/zzgl/crowd/correctional/create.jhtml?from=rs&standard=standard&cirsId=" + ciRsId;
        } else if (crowdType.equals(CultMemberRecord.REL_CODE)) {
            CultMemberRecord r = cultMemberRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/cultMember/edit.jhtml?cultId=" + r.getCultId();
            }
            return "redirect:/resident/create/cultMember.jhtml?cirsId=" + ciRsId;
        } else if (crowdType.equals(PetitionRecord.REL_CODE)) {
            PetitionRecord r = petitionRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/petitionRecord/editPage.jhtml?prId=" + r.getPrId();
            }
            return "redirect:/resident/create/petitionRecord.jhtml?cirsId=" + ciRsId;
        } else if (crowdType.equals(DangerousGoodsRecord.REL_CODE)) {
            DangerousGoodsRecord r = dangerousGoodsRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/dangerousGoods/edit.jhtml?recordId=" + r.getRecordId();
            }
            return "redirect:/resident/create/dangerousGoods.jhtml?cirsId=" + ciRsId;
        } else if (crowdType.equals(MentalIllnessRecord.REL_CODE)) {// 2017-11-22 Modify By YangCQ 改为精神病症国标
            MentalIllnessRecord r = mentalIllnessRecordService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/mentalIllnessRecord/edit.jhtml?from=rs&standard=standard&miId=" + r.getMiId();
            }
            return "redirect:/zzgl/crowd/mentalIllnessRecord/create.jhtml?from=rs&standard=standard&cirsId=" + ciRsId;
        } else if (crowdType.equals(AidsPerson.REL_CODE)) {
            AidsPerson r = aidsService.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/aids/edit.jhtml?id=" + r.getId();
            }
            return "redirect:/zzgl/crowd/aids/create.jhtml?cirsId=" + ciRsId;
        } else if (crowdType.equals(YouthPerson.REL_CODE)) {
            YouthPerson r = youthService.findByCiRsId(ciRsId, null);
            if (r != null) {
                return "redirect:/zzgl/crowd/youth/edit.jhtml?id=" + r.getId();
            }
            return "redirect:/zzgl/crowd/youth/create.jhtml?cirsId=" + ciRsId;
        }/* else if (crowdType.equals(MlmRecord.REL_CODE)) {
            MlmRecord r = service1.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/mlm/edit.jhtml?id=" + r.getId();
            }
            return "redirect:/zzgl/crowd/mlm/create.jhtml?cirsId=" + ciRsId;
        } else if (crowdType.equals(KeyPersonnel.REL_CODE)) {
            KeyPersonnel r = personnelService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/petitionRecord/editPage.jhtml?prId=" + r.getKeyId();
            }
            return "redirect:/resident/create/keyPersonnel.jhtml?cirsId=" + ciRsId;
        } */else if (crowdType.equals(Petitioner.REL_CODE)) {
            Petitioner r = petitionerService.findByCiRsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/petitioner/edit.jhtml?miId=" + r.getMiId();
            }
            return "redirect:/zzgl/crowd/petitioner/create.jhtml?cirsId=" + ciRsId;
        } /*else if (crowdType.equals(InvolPerson.REL_CODE)) {
            InvolPerson r = involService.findByCirsId(ciRsId);
            if (r != null) {
                return "redirect:/zzgl/crowd/invol/edit.jhtml?id=" + r.getId();
            }
            return "redirect:/zzgl/crowd/invol/create.jhtml?cirsId=" + ciRsId;
        }*/

        return null;
    }

    /**
     * 根据家庭编号获取居民家庭信息
     *
     * @param session
     * @param familySN 家庭编号
     * @param map
     * @return
     */
    @RequestMapping(value = "/family/{familySN}")
    public String familyInfo(HttpSession session, @PathVariable(value = "familySN") String familySN,
                             @RequestParam(value = "orgCode", required = false) String orgCode, ModelMap map) {
        if (orgCode == null) {
            Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
            orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE) + "";
        }
        List<?> l = cirsService.findRsTopByFamilySn(familySN, orgCode);
        if (l != null && l.size() > 0) {
            map.addAttribute("familyList", l);
        }
        return "/resident/family.ftl";
    }

    /**
     * 居民查找公用
     *
     * @param orgCode
     * @param fromType
     * @param map
     * @return
     */
    @RequestMapping(value = "/search")
    public String searchResident(HttpSession session, @RequestParam(value = "orgCode") String orgCode, @RequestParam(value = "fromType") String fromType,
                                 @RequestParam(value = "cbiId", required = false) Long cbiId,
                                 @RequestParam(value = "teamId", required = false) Long teamId,
                                 @RequestParam(value = "bizType", required = false) Long bizType,
                                 @RequestParam(value = "startGridId", required = false) Long startGridId,
                                 @RequestParam(value = "startGridCode", required = false) String startGridCode,
                                 @RequestParam(value = "identityCard", required = false) String identityCard,
                                 @RequestParam(value = "corpDepartmentId", required = false) Long corpDepartmentId, ModelMap map) {
        map.addAttribute("orgCode", orgCode);
        map.addAttribute("fromType", fromType);
        if (startGridId == null || startGridId == -99L) {//增加个可选项企业库那边需要增加个企业部门ID
            map.addAttribute("startGridId", -99L);
        } else {
            map.addAttribute("startGridId", startGridId);
        }
        startGridCode = startGridCode == null ? "0" : startGridCode;
        map.addAttribute("startGridCode", startGridCode);
        if (cbiId == null) {//增加个可选项企业库那边需要增加个企业ID
            map.addAttribute("cbiId", 0);
        } else {
            map.addAttribute("cbiId", cbiId);
        }
        if (teamId == null) {//增加个可选项群防群治队员那边需要增加个群防群治队伍ID
            map.addAttribute("teamId", 0);
        } else {
            map.addAttribute("teamId", teamId);
        }
        if (bizType == null) {//队伍类型
            map.addAttribute("bizType", 0);
        } else {
            map.addAttribute("bizType", bizType);
        }
        if (corpDepartmentId == null) {//增加个可选项企业库那边需要增加个企业部门ID
            map.addAttribute("corpDepartmentId", null);
        } else {
            map.addAttribute("corpDepartmentId", corpDepartmentId);
        }
        if (identityCard == null) identityCard = "0";
        map.addAttribute("identityCard", identityCard);
        //map.addAttribute("PLATFORM_DOMAIN_ROOT", ConstantValue.PLATFORM_DOMAIN_ROOT);
        //map.addAttribute("PLATFORM_DOMAIN_ROOT", session.getAttribute(ConstantValue.NEW_ZZ_PLATFORM_DOMAIN));
        map.addAttribute("RESIDENT_DOMAIN", App.RS.getDomain(session));

        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        map.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
//		//兴旺定制需求
//		String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)+"";
//		if(defaultInfoOrgCode!=null && defaultInfoOrgCode.startsWith(ConstantValue.XINGWANG_FUNC_ORG_CODE)){
//			map.addAttribute("moduleName", "人员选择列表");
//			return "/resident/search_xingwang.ftl";
//		}
//		return "/resident/search"+ConstantValue.CSS_NAME+".ftl";
        return "/resident/search_xingwang.ftl";

        //return "/resident/search.ftl";
    }

    /**
     * 居民查找数据
     * 新增网格id来区分是否为重点人口走访
     *
     * @param page
     * @param rows
     * @param orgCode
     * @param name
     * @param identityCard
     * @param address
     * @param startGridId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search/listData")
    public cn.ffcs.common.EUDGPagination searchResidentListData(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "rows") int rows,
            @RequestParam(value = "orgCode") String orgCode,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "startGridId", required = false) Long startGridId,
            @RequestParam(value = "identityCard", required = false) String identityCard,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "fromType", required = false) String fromType) {
        if (name != null) name = name.trim();
        if (identityCard != null) identityCard = identityCard.trim();
        if (address != null) address = address.trim();
        if (startGridId != null && startGridId != -99L) {//重口人员信息 人员选择
            if ("crowd_visit".equals(fromType)) {//重点人员走访登记
                return residentService.searchKeyPersonResidentPage(page, rows, orgCode, name, identityCard, address, startGridId);
            } else {
                return residentService.searchResidentPage(page, rows, orgCode, name, identityCard, address);
            }
        } else {
            if (status != null) {
                return residentService.searchResidentPageEx(page, rows, orgCode, name, identityCard, address, status);
            } else {
                return residentService.searchResidentPage(page, rows, orgCode, name, identityCard, address);
            }
        }

    }

    @ResponseBody
    @RequestMapping(value = "/search/listData2")
    public cn.ffcs.common.EUDGPagination searchResidentListData(HttpServletRequest req, int page, int rows, String orgCode) {
        UserInfo u = (UserInfo) req.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
        String q = req.getParameter("q");
        cn.ffcs.common.EUDGPagination p = residentService.searchResidentPageEx2(page, rows, orgCode, q);
        if (p != null && p.getRows() != null) {
                /*for(Map<String, Object> m : (List<Map<String, Object>>)p.getRows()){
                    if(m.get("BIRTHDAY_STR") != null){
                        String age = DateUtils.getAge((String)m.get("BIRTHDAY_STR"));
                        m.put("I_AGE", age);
                    }
					if(m.get("I_EDUCATION") != null){
						String strEdu = (String)m.get("I_EDUCATION");
						if(!StringUtils.isEmpty(strEdu)){
							m.put("I_EDUCATION_CN", dictService.changeCodeToName(DictPcode.EDUCATION_LEVEL, strEdu, u.getOrgCode()));
						}
					}
                }*/
        }
        return p;
    }

    /***
     * 藏族，维吾尔族
     * @param req
     * @param page
     * @param rows
     * @param orgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search/listData4")
    public cn.ffcs.common.EUDGPagination searchResidentListData3(HttpServletRequest req, int page, int rows, String orgCode) {
        String q = req.getParameter("q");
        return residentService.searchResidentPageEx3(page, rows, orgCode, q);
    }

    //重点人员新增跳转
    @RequestMapping(value = "/create/{type}")
    public String create(HttpServletRequest req, ModelMap model, @PathVariable(value = "type") String type,
                         @RequestParam(value = "area", required = false) String area,
                         @RequestParam(value = "gridId", required = false) Long gridId) {
        UserInfo userInfo = (UserInfo) req.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(req.getSession());
        if (gridId != null) {
            model.addAttribute("startGridId", gridId);
            MixedGridInfo g = gridInfoService.findMixedGridInfoById(gridId, false);
            if (g != null) {
                model.addAttribute("gridCode", g.getGridCode());
                model.addAttribute("orgCode", g.getInfoOrgCode());
            }
        } else {
            model.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
            model.addAttribute("gridCode", defaultGridInfo.get(KEY_START_GRID_CODE));
            model.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        }

        //吸毒
        model.addAttribute("controlDescDC", DataDictHelper.getDataDictList(DictPcode.DRUG_CONTROLDESC, userInfo.getOrgCode()));
        model.addAttribute("dtypeDC", DataDictHelper.getDataDictList(DictPcode.DRUG_DTYPE, userInfo.getOrgCode()));
        //精神病
        model.addAttribute("frequencyDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_FREQUENCY, userInfo.getOrgCode()));
        model.addAttribute("economicStatusDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_ECONOMIC_STATUS, userInfo.getOrgCode()));
        model.addAttribute("riskDegreeDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_RISK_DEGREE, userInfo.getOrgCode()));
        if (userInfo.getOrgCode().startsWith(ConstantValue.JIANGXI_FUNC_ORG_CODE)) {
            model.addAttribute("rescueDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_RESCUE, userInfo.getOrgCode()));
            model.addAttribute("freeDurgDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_FREE_DURG, userInfo.getOrgCode()));
            model.addAttribute("currsitDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_CURR_SIT, userInfo.getOrgCode()));
            model.addAttribute("mentalTypeDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_TYPE, userInfo.getOrgCode()));
            model.addAttribute("diseaseDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_DISEASE, userInfo.getOrgCode()));
        }
        //危险品从业人员
        model.addAttribute("workTypeDC", DataDictHelper.getDataDictList(DictPcode.DANGER_WORK_TYPE, userInfo.getOrgCode()));
        model.addAttribute("workStatusDC", DataDictHelper.getDataDictList(DictPcode.DANGER_WORK_STATUS, userInfo.getOrgCode()));
        //其他重点人员数据字典
        model.addAttribute("dutyDC", DataDictHelper.getDataDictList(DictPcode.KEY_ACCESS_CATALOG, userInfo.getOrgCode()));
        //矫正人员
        model.addAttribute("correctTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_CORRECTTYPE, userInfo.getOrgCode()));
        model.addAttribute("managementLevelDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_MANLEVEL, userInfo.getOrgCode()));
        //刑释解教
        model.addAttribute("teachTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_TEACHTYPE, userInfo.getOrgCode()));
        model.addAttribute("originalOccupatiorDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_ORIGINAL_OCCUP, userInfo.getOrgCode()));
        model.addAttribute("curOccupatiorDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_CURR_OCCUP, userInfo.getOrgCode()));
        model.addAttribute("admonishTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_ADMONISHTYPE, userInfo.getOrgCode()));

        List<BaseDataDict> takeoffTypesDC = dictService.getDataDictListOfSinglestage("D065007", userInfo.getOrgCode());
        model.addAttribute("takeoffTypesDC", takeoffTypesDC);

        model.addAttribute("joinTypeDC", DataDictHelper.getDataDictList("B015", userInfo.getOrgCode()));
        model.addAttribute("wentDC", DataDictHelper.getDataDictList("D065008", userInfo.getOrgCode()));
        model.addAttribute("docedDC", DataDictHelper.getDataDictList("B456", userInfo.getOrgCode()));
        model.addAttribute("placementSituationDC", DataDictHelper.getDataDictList("B457", userInfo.getOrgCode()));

        model.addAttribute("record", new DrugRecord());
        //治安重点人员
        model.addAttribute("typeDC", DataDictHelper.getDataDictList(DictPcode.PUBLIC_SECURITY_KP, userInfo.getOrgCode()));
        //罪名
        model.addAttribute("accusationDC", DataDictHelper.getDataDictList("D057001", userInfo.getOrgCode()));
        //危险性评估类型
        model.addAttribute("riskAssessmentDC", DataDictHelper.getDataDictList("D057005", userInfo.getOrgCode()));
        //甘肃的特殊字典

        if (userInfo.getOrgCode().startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {
            //刑释解教
            model.addAttribute("reconomicStatusDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_ECONOMIC_STATUS, userInfo.getOrgCode()));
            model.addAttribute("personnelTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_PERSONNEL_TYPE, userInfo.getOrgCode()));
            model.addAttribute("joinTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_JOIN_TYPE, userInfo.getOrgCode()));
            model.addAttribute("riskAssessmentDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_RISK_ASSESSMENT, userInfo.getOrgCode()));
            model.addAttribute("placementTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_PLACEMENT_TYPE, userInfo.getOrgCode()));
            model.addAttribute("criminalTypeDC", DataDictHelper.getDataDictList(DictPcode.RELEASE_CRIMINAL_TYPE, userInfo.getOrgCode()));

            //矫正
            model.addAttribute("receiveTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RECEIVE_TYPE, userInfo.getOrgCode()));
            model.addAttribute("fourHistoryDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_FOUR_HISTORY, userInfo.getOrgCode()));
            model.addAttribute("recidivistDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RECIDIVIST, userInfo.getOrgCode()));
            model.addAttribute("threeInvolveDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_THREE_INVOLVE, userInfo.getOrgCode()));
            model.addAttribute("createCorrectGroupDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_GROUP, userInfo.getOrgCode()));
            model.addAttribute("correctGroupTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_GROUP_TYPE, userInfo.getOrgCode()));
            model.addAttribute("releaseTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RELEASE_TYPE, userInfo.getOrgCode()));
            model.addAttribute("riskDegreeDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_RISK_DEGREE_GANSU, userInfo.getOrgCode()));
            model.addAttribute("economicStatusDC", DataDictHelper.getDataDictList(DictPcode.MENTAL_ECONOMIC_STATUS_GANSU, userInfo.getOrgCode()));
            model.addAttribute("correctTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_CORRECTTYPE_GANSU, userInfo.getOrgCode()));
        }
        if (area != null) {
            area = "_" + area;
        } else {
            area = "";
        }
        String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)+"";
        String cirsId = req.getParameter("cirsId");
        if (!StringUtils.isEmpty(cirsId)) {
            req.setAttribute("cirsId", cirsId);
        }else if(defaultInfoOrgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)){
            if("petitionRecord".equals(type)){
                area = "gansu";
            }
		}
        return "/zzgl_crowd/" + type + "/create" + area + ".ftl";
    }

    /**
     * 通过名字和身份证号获取居民信息
     *
     * @param session
     * @param name
     * @param identityCard
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCiRsIdByNameAndIdentityCard", method = RequestMethod.POST)
    public Map<String, Object> getCiRsIdByNameAndIdentityCard(HttpSession session, @RequestParam(value = "name") String name, @RequestParam(value = "identityCard") String identityCard, ModelMap map) {
        try {
            name = java.net.URLDecoder.decode(name, "utf-8").trim();

        } catch (UnsupportedEncodingException e) {
        }
        Long ciRsId = residentService.getCiRsIdByNameAndIdentityCard(name, identityCard);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("ciRsId", ciRsId > 0 ? ciRsId : 0L);
        return resultMap;
    }

    @RequestMapping(value = "getCiRsId")
    public String getCiRsId(HttpSession session, ModelMap map, @RequestParam(value = "ciRsId") Long ciRsId) {
        map.put("rsId", ciRsId);
        return "/resident/value.ftl";
    }


    /**
     * 获取重点人员统计数据
     *
     * @param session
     * @param orgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/keyPersonnelData", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String keyPersonnelData(HttpSession session, @RequestParam(value = "orgCode") String orgCode,
                                   @RequestParam(value = "callback", required = false) String callback) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -0);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime());
        List<Map<String, Object>> list = residentService.getKeyPersonnelData(orgCode, firstDay);
        JSONArray jsonArray = JSONArray.fromObject(list);
        String data = jsonArray.toString();
        if (callback != null && !"".equals(callback)) {
            data = callback + "(" + data + ")";
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/getResidentInfoByNameAndIdentityCard", method = RequestMethod.POST)
    public Map<String, Object> getResidentInfoByNameAndIdentityCard(HttpSession session, @RequestParam(value = "name") String name, @RequestParam(value = "identityCard") String identityCard, ModelMap map) {
        try {
            name = java.net.URLDecoder.decode(name, "utf-8").trim();

        } catch (UnsupportedEncodingException e) {
        }

        Long ciRsId = residentService.getCiRsIdByNameAndIdentityCard(name, identityCard);
//
//		Long ciRsId = cirsService.get.getCiRsIdByNameAndIdentityCard(name, identityCard);
        if (ciRsId > 0) {
            Map<String, Object> residentDetail = residentService.getResidentDetail(ciRsId);
            residentDetail.put("ciRsId", ciRsId > 0 ? ciRsId : 0L);
            return residentDetail;
        }
        return null;

    }

    @ResponseBody
    @RequestMapping(value = "/getResidentInfoById")
    public Map<String, Object> getResidentInfoById(HttpServletRequest req, Long cirsId) {
        return residentService.getResidentDetail(cirsId);
    }

    @ResponseBody
    @RequestMapping(value = "/search/listData3")
    public EUDGPagination searchResidentListData2(HttpServletRequest req, int page, int rows, String orgCode) {
        String q = req.getParameter("q");
        CiRsCriteria criteria = new CiRsCriteria();
        criteria.setOrgCode(orgCode);
//		criteria.setName(q);
        criteria.setQ(q);
        Pagination pagination = new Pagination();
        try {
            //pagination = cirsService.findPage(criteria, page, rows);
            pagination = cirsService.findRsPage(criteria, page, rows);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        EUDGPagination p = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
        return p;
    }

    /**
     * 判断姓名和cirsId是否一致
     *
     * @param session
     * @param cirsId
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isCiRsTopsByCiRsIdAndName", method = RequestMethod.POST)
    public Map<String, Object> findCiRsTopsByCiRsId(HttpSession session,
                                                    @RequestParam(value = "cirsId") Long cirsId,
                                                    @RequestParam(value = "name") String name, ModelMap map) {
        Map<String, Object> result = new HashMap<String, Object>();
        Boolean flag = false;
        CiRsTop ciRsTop = new CiRsTop();
        if (cirsId != null) {
            ciRsTop = cirsService.findCiRsTopsByCiRsId(cirsId);
        }
        if (ciRsTop != null && ciRsTop.getName() != null) {
            if (name.equals(ciRsTop.getName())) {
                flag = true;
            }
        }
        result.put("flag", flag);
        return result;

    }


    /**
     * 获取人员信息
     *
     * @param cirsId
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRsInfoByCiRsId", method = RequestMethod.POST)
    public CiRsTop getRsInfoByCiRsId(HttpSession session,
                                     @RequestParam(value = "cirsId") Long cirsId, ModelMap map) {
        return cirsService.findCiRsTopsByCiRsId(cirsId);
    }


    /*************************跳转至各重点人员页面 begin***********************************/
    //重点人员单独详情页面
    @RequestMapping(value = "/detailCrowd/{type}")
    public String create(HttpSession session, ModelMap map, HttpServletRequest req, @PathVariable(value = "type") String type,
                         @RequestParam(value = "area", required = false) String area,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "ciRsId", required = false) Long ciRsId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        if (type.equals("correctional")) {
            return Tcorrectional(id, map, userInfo.getOrgCode());
        } else if (type.equals("aid")) {
            return Taid(id);
        } else if (type.equals("cultMember")) {
            return TcultMember(id, map);
        } else if (type.equals("dangerousGoods")) {
            return TdangerousGoods(id, map, userInfo.getOrgCode());
        } else if (type.equals("drug")) {
            return Tdrug(id, map, userInfo.getOrgCode());
        } /*else if (type.equals("keyPersonnel")) {
            return TKeyPersonnel(id, map);
        }*/ else if (type.equals("mentalIllness")) {
            return TMentalIllness(id, map, userInfo.getOrgCode());
        } /*else if (type.equals("mlm")) {
            return TMlm(id, req);
        }*/ else if (type.equals("petition")) {
            return TPetition(id, map, userInfo.getOrgCode());
        } else if (type.equals("released")) {
            return Treleased(id, map, userInfo.getOrgCode());
        } else if (type.equals("youth")) {
            if (ciRsId != null && ciRsId != 0)
                return TYouthByRsId(ciRsId, req, userInfo.getOrgCode());
            return TYouth(id, req, userInfo.getOrgCode());
        }

        return "/error.ftl";
    }

    //艾滋病
    private String Taid(Long id) {
        return "redirect:../../zzgl/crowd/aids/detail.jhtml?itype=0&id=" + id;
    }

    //矫正
    private String Tcorrectional(Long id, ModelMap map, String orgCode) {
        CorrectionalRecord record = correctionalRecordService.findCorrectionalRecordById(id);

        map.addAttribute("ciRsId", record.getCiRsId());

        map.addAttribute("correctTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_CORRECTTYPE, orgCode));
        map.addAttribute("originalOccupatiorDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_ORIGINAL_OCCUP, orgCode));
        map.addAttribute("curOccupatiorDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_CURR_OCCUP, orgCode));
        map.addAttribute("record", record);
        if (orgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
            map.addAttribute("managementLevelDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_MANLEVEL, orgCode));
            return "/zzgl_crowd/haicangCorrectional/i_detail.ftl";
        } else if (orgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {
            map.addAttribute("correctTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_CORRECTTYPE_GANSU, orgCode));
            map.addAttribute("receiveTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RECEIVE_TYPE, orgCode));
            map.addAttribute("fourHistoryDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_FOUR_HISTORY, orgCode));
            map.addAttribute("recidivistDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RECIDIVIST, orgCode));
            map.addAttribute("threeInvolveDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_THREE_INVOLVE, orgCode));
            map.addAttribute("createCorrectGroupDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_GROUP, orgCode));
            map.addAttribute("correctGroupTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_GROUP_TYPE, orgCode));
            map.addAttribute("releaseTypeDC", DataDictHelper.getDataDictList(DictPcode.CORRECT_RELEASE_TYPE, orgCode));
            if (!StringUtils.isBlank(record.getCorrectType())) {
                record.setCorrectTypeLabel(dictionaryService.changeCodeToName(DictPcode.CORRECT_CORRECTTYPE_GANSU, record.getCorrectType(), orgCode));
            }
            map.addAttribute("record", record);
            return "/zzgl_crowd/correctional/i_detail_gansu.ftl";
        }
        return "/zzgl_crowd/correctional/i_detail.ftl";
    }

    private String TcultMember(Long id, ModelMap map) {
        CultMemberRecord record = cultMemberRecordService.findCultMemberRecordById(id);
        map.addAttribute("record", record);
        map.addAttribute("ciRsId", record.getCiRsId());
        return "/zzgl_crowd/cultMember/i_detail.ftl";
    }

    private String TdangerousGoods(Long id, ModelMap map, String orgCode) {
        DangerousGoodsRecord record = dangerousGoodsRecordService.findDangerousGoodsRecordById(id, orgCode);
        map.addAttribute("record", record);
        map.addAttribute("ciRsId", record.getCiRsId());
        return "/zzgl_crowd/dangerousGoods/i_detail.ftl";
    }

    private String Tdrug(Long id, ModelMap map, String orgCode) {
        DrugRecord record = drugRecordService.findDrugRecordById(id, orgCode);
        map.addAttribute("record", record);
        map.addAttribute("ciRsId", record.getCiRsId());
        if (orgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {
            return "/zzgl_crowd/drug/i_detail_gansu.ftl";
        }
        return "/zzgl_crowd/drug/i_detail.ftl";
    }

  /*  private String TKeyPersonnel(Long id, ModelMap map) {
        KeyPersonnel key = personnelService.findKeyPersonnelById(id);
        Long access = personnelService.findAccessById(id, key.getRsId());
        map.addAttribute("access", access + "");
        map.put("key", key);
        map.addAttribute("dutyDC", DataDictHelper.getDataDictList(DictPcode.KEY_ACCESS_CATALOG, null));
        return "/zzgl_crowd/keyPersonnel/i_detail.ftl";

    }*/

    private String TMentalIllness(Long id, ModelMap map, String orgCode) {
        MentalIllnessRecord record = mentalIllnessRecordService.findMentalIllnessRecordById(id, orgCode);
        map.addAttribute("record", record);
        map.addAttribute("ciRsId", record.getCiRsId());

        if (orgCode.startsWith(ConstantValue.JINJIANG_FUNC_ORG_CODE)) {
            map.addAttribute("bizType", GuardianBizType.MENTALILLNESS);
            return "/zzgl_crowd/mentalIllnessRecord/i_detail_jinjiang.ftl";
        }
        if (orgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
            return "/zzgl_crowd/mentalIllnessRecord/i_detail_haicang.ftl";
        }
        if (orgCode.startsWith(ConstantValue.JIANGXI_FUNC_ORG_CODE)) {
            map.addAttribute("moduleName", "精神病人员新增");
            return "/zzgl_crowd/mentalIllnessRecord/i_detail_jiangxi.ftl";
        }
        if (orgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {
            if (!StringUtils.isBlank(record.getRiskDegree())) {
                record.setRiskDegreeLabel(dictionaryService.changeCodeToName(DictPcode.MENTAL_RISK_DEGREE_GANSU, record.getRiskDegree(), orgCode));
            }
            if (!StringUtils.isBlank(record.getEconomicStatus())) {
                record.setEconomicStatusLabel(dictionaryService.changeCodeToName(DictPcode.MENTAL_ECONOMIC_STATUS_GANSU, record.getEconomicStatus(), orgCode));
            }
            return "/zzgl_crowd/mentalIllnessRecord/i_detail_gansu.ftl";
        }
        return "/zzgl_crowd/mentalIllnessRecord/i_detail.ftl";
    }

  /*  private String TMlm(Long id, HttpServletRequest req) {
        MlmRecord bo = service1.findById(id);
        if (bo != null) {
            req.setAttribute("bo", bo);
        } else {
            req.setAttribute("errorMsg", "找不到记录");
            return "/error.ftl";
        }
        return "/zzgl_crowd/mlm/i_detail.ftl";
    }*/

    private String TPetition(Long id, ModelMap map, String orgCode) {
        PetitionRecord record = petitionRecordService.findPetitionRecordById(id);
        map.put("record", record);
        map.put("ciRsId", record.getCiRsId());

        //上访信息
    /*    List<PetitionEvent> pl = petitionEventService.findByPrid(record.getPrId());
        map.put("pl", pl);
        if (pl != null && pl.size() > 0) {
            String lastVisitDate = "";
            for (PetitionEvent p : pl) {
                if (p.getVisitDate() != null && (lastVisitDate.equals("") || lastVisitDate.compareTo(p.getVisitDate()) < 0)) {
                    lastVisitDate = p.getVisitDate();
                }
            }
            map.put("lastVisitDate", lastVisitDate);
        }*/
        if (orgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {

            return "/zzgl_crowd/petitionRecord/i_detail_gansu.ftl";
        }
        return "/zzgl_crowd/petitionRecord/i_detail.ftl";
    }

    private String Treleased(Long id, ModelMap map, String orgCode) {
        ReleasedRecord record = releasedRecordService.findReleasedRecordById(id, orgCode);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("objId", record.getReleasedId());
        param.put("objType", ConstantValue.OBJ_TYPE_RELEASE);
       /* VisitArrange visitArrange = visitArrangeService.findVisitArrangeByObjIdAndObjType(param);
        record.setVisitArrange(visitArrange);*/
        map.addAttribute("record", record);
        map.addAttribute("ciRsId", record.getCiRsId());
        if (orgCode.startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)) {
            return "/zzgl_crowd/releasedRecord/i_detail_gansu.ftl";
        }
        //针对海沧350205司法需求，跳转到不同页面
        if (orgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
            return "/zzgl_crowd/haicangReleasedRecord/i_detail.ftl";
        }
        return "/zzgl_crowd/releasedRecord/i_detail.ftl";
    }

    private String TYouth(Long id, HttpServletRequest req, String orgCode) {
        YouthPerson bo = youthService.findById(id);
        if (bo != null) {
            if (!StringUtils.isEmpty(bo.getType())) {
                bo.setType(dictionaryService.changeCodeToName(DictPcode.YOUTH_MI_TYPE, bo.getType(), orgCode));
            }
            req.setAttribute("bo", bo);
        } else {
            req.setAttribute("errorMsg", "找不到记录");
            return "/error.ftl";
        }
        return "/zzgl_crowd/youth/i_detail.ftl";
    }

    private String TYouthByRsId(Long rsId, HttpServletRequest req, String orgCode) {
        YouthPerson bo = youthService.findByCiRsId(rsId, null);
        if (bo != null) {
            if (!StringUtils.isEmpty(bo.getType())) {
                bo.setType(dictionaryService.changeCodeToName(DictPcode.YOUTH_MI_TYPE, bo.getType(), orgCode));
            }
            req.setAttribute("bo", bo);
        } else {
            req.setAttribute("errorMsg", "找不到记录");
            return "/error.ftl";
        }
        return "/zzgl_crowd/youth/i_detail.ftl";
    }

    /*************************跳转至各重点人员页面 end***********************************/

    @RequestMapping("/residentForUnitList")
    public String residentForUnitList() {
        return "/component/residentForUnitList.ftl";
    }

    @ResponseBody
    @RequestMapping(value = "/search/listData5")
    public EUDGPagination searchResidentListData5(HttpServletRequest req, int page, int rows, CiRsCriteria criteria) {
        Pagination pagination = new Pagination();
        try {
            pagination = cirsService.findPage(criteria, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EUDGPagination eudgPagination = new EUDGPagination(pagination.getTotalCount(), pagination.getList());
        return eudgPagination;
    }

    //人口 ciRsId改造partyId
    @RequestMapping(value = "/crowd/index/{partyId}")
    public String crowdIndex(HttpServletRequest req, @PathVariable(value = "partyId") Long partyId, String crowdType,
                             @RequestParam(value = "isDetail", required = false) String isDetail) {
        if (crowdType.equals(CultMemberRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/cultMember/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/cultMember/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(ReleasedRecord.REL_CODE)) {// 2017-11-22 Modify By YangCQ 改为刑释解教国标
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/releasedRecord/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/releasedRecord/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(DrugRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/drug/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/drug/index.jhtml?partyId=" + partyId;
                }

            }
        } else if (crowdType.equals(DangerousGoodsRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/dangerousGoods/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                }else {
                    return "redirect:/zzgl/crowd/dangerousGoods/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(MlmRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/mlm/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/mlm/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(PetitionRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/petitionRecord/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/petitionRecord/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(InvolPerson.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/invol/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/invol/index.jhtml?partyId=" + partyId;
                }
            }

        } else if (crowdType.equals(AidsPerson.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/aids/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/aids/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(CorrectionalRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/correctional/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                }else {
                    return "redirect:/zzgl/crowd/correctional/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(KeyPersonnel.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/keyPer/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                }else {
                    return "redirect:/zzgl/crowd/keyPer/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(MentalIllnessRecord.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/mentalIllnessRecord/index.jhtml?partyId=" + partyId+ "&isDetail=detail";
                }else {
                    return "redirect:/zzgl/crowd/mentalIllnessRecord/index.jhtml?partyId=" + partyId;
                }
            }
        } else if (crowdType.equals(YouthPerson.REL_CODE)) {
            if (partyId != null) {
                if (isDetail != null) {
                    return "redirect:/zzgl/crowd/youth/index.jhtml?partyId=" + partyId + "&isDetail=detail";
                } else {
                    return "redirect:/zzgl/crowd/youth/index.jhtml?partyId=" + partyId;
                }
            }
        }
        return null;
    }

    /**
     * 人员选择器用
     */
    @ResponseBody
    @RequestMapping("/fetchFamilyMembersById")
    public Object fetchFamilyMembersById(HttpServletRequest request,
                                         Model model,
                                         @RequestParam("partyId") String partyId) {
        Long id = null;
        if (!StringUtils.isEmpty(partyId)) {
            try {
                id = Long.valueOf(partyId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        List<PartyIndividual> members = new ArrayList<>();
        if (id != null && id > 0) {
            members = partyIndividualService.findSameMember(id);
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", members.size());
        jsonMap.put("rows", members);
        return jsonMap;
    }

    @RequestMapping(value="/test")
    public String test(HttpSession session, HttpServletRequest request,ModelMap map){
        map.addAttribute("partyId",request.getParameter("partyId"));
        return "/component/partyMembersTest.ftl";
    }
}