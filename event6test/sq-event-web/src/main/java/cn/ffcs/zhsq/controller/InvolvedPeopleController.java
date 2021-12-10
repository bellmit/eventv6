package cn.ffcs.zhsq.controller;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.resident.service.CiRsRelService;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 参与人
 * @author 
 */
@Controller
@RequestMapping("/zhsq/involvedPeople")
public class InvolvedPeopleController extends ZZBaseController {

	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private PartyIndividualService partyIndividualService;
	@Autowired
	private CiRsRelService ciRsRelService;


	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResultObj save(
			HttpSession session,
			HttpServletRequest request,InvolvedPeople involvedPeople,
            ModelMap map) throws Exception {
		ResultObj resultObj = null;
		involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
		Long record = involvedPeopleService.insertInvolvedPeople(involvedPeople, false);
		resultObj = Msg.OPERATE.getResult(record > 0, record);
		return resultObj;
	}
	
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object index(HttpSession session,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String index = request.getParameter("index");
		String ipId = request.getParameter("ipId");
		String hashId = request.getParameter("hashId");
		InvolvedPeople involvedPeople = null;
		if(StringUtils.isEmpty(hashId)){
			involvedPeople = involvedPeopleService.findInvolvedPeopleById(Long.valueOf(ipId));
		}else{
			involvedPeople = involvedPeopleService.findInvolvedPeopleById(HashIdManager.decryptLong(hashId));
		}
		toDictCN(involvedPeople);
		map.put("involvedPeople",involvedPeople);
		map.put("index",index);
		return map;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(HttpSession session,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String index = request.getParameter("index");
		String ipId = request.getParameter("ipId");
		InvolvedPeople involvedPeople = involvedPeopleService.findInvolvedPeopleById(Long.valueOf(ipId));
		map.put("involvedPeople",involvedPeople);
		map.put("index",index);
		map.put("orgCode",this.getDefaultInfoOrgCode(session));
		return map;
	}

	@RequestMapping(value = "/create")
	public String create(HttpSession session,
			HttpServletRequest request, ModelMap map) {
		String index = request.getParameter("index");
		String id = request.getParameter("id");
		InvolvedPeople involvedPeople = new InvolvedPeople();
//		involvedPeople.setIpId(ipId);
		involvedPeople.setCardType("1");//默认证件类型：身份证
		involvedPeople.setNation("汉族");//默认民族
		map.addAttribute("involvedPeople",involvedPeople);
		map.addAttribute("index",index);
		map.addAttribute("id",id);
		map.addAttribute("orgCode",this.getDefaultInfoOrgCode(session));
		return "/zzgl/involvedPeople/create.ftl";
	}
	
	/**
	 * 修改字典为中文
	 * @param faultType
	 */
	private void toDictCN(InvolvedPeople bo) {
		bo.setCardTypeName(dictionaryService.changeCodeToName("D030001", bo.getCardType(), null));
	}

	/**
	 * 判断人口是否是精神病患者
	 * 判断某个人口是否不具有某种标签
	 * 如果是 返回 false
	 * 如果否 返回 true
	 * @param partyId
	 * @return
	 * */
	@ResponseBody
	@RequestMapping(value = "/isUniqueRel")
	public Map<String,Object> isUniqueRel(HttpServletRequest req,
							 HttpSession session,
							 @RequestParam(value = "id", required = false) Long id,
							 @RequestParam(value = "partyId", required = false) Long partyId) {

		Map<String,Object> resultMap = new HashMap<>();
		String relCode = "L";
		//默认不具有标签信息
		Boolean isUniqueRel = true;

		if(null != partyId && partyId > 0){
			isUniqueRel = ciRsRelService.isUniqueRel(partyId,relCode);
		}

		resultMap.put("isUniqueRel",isUniqueRel);

		return resultMap;
	}
}
