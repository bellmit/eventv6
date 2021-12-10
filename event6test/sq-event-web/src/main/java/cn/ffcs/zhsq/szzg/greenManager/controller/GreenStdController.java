package cn.ffcs.zhsq.szzg.greenManager.controller;

import java.util.*;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenStdBO;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenStdService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.ffcs.system.publicUtil.EUDGPagination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping(value="/zhsq/szzg/greenstd")
@Controller
public class GreenStdController {

	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private GreenStdService greenStdService;

	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "greenManager/greenStd";

	/**
	 * 前端首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String index(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002001", userInfo.getOrgCode());

		model.addAttribute("type",list);


		return REAL_PATH + SUB_MAIN + "/gjslbz.ftl";
	}



	@RequestMapping(value="/index")
	public String index(HttpSession session,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codetype", "S002001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002001", userInfo.getOrgCode());
		map.put("type", list);

		return "/szzg/greenManager/greenStd/index_greenStd.ftl";
	}

	@RequestMapping(value="listData")
	@ResponseBody
	public EUDGPagination listData(GreenStdBO greenStd,
			@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows){
		Map<String, Object> params = new HashMap<String, Object>();
		if(greenStd.getName()!=null){
			params.put("name", greenStd.getName());
		}
		if(greenStd.getType()!=null){
			params.put("type", greenStd.getType());
		}
		if(greenStd.getSyear() != null){
			params.put("syear", greenStd.getSyear());
		}
		EUDGPagination eudgPagination = greenStdService.findPageListByCriteria(params, page, rows);
		return eudgPagination;
	}

	@RequestMapping(value="searchData")
	@ResponseBody
	public List<GreenStdBO> searchData(
			GreenStdBO greenStdBO){
		Map<String, Object> params = new HashMap<String, Object>();

		if(greenStdBO.getType()!=null){
			params.put("type", greenStdBO.getType());
		}
		List<GreenStdBO> greenStdBOS =  greenStdService.findGreenStdByParams(params);
		return greenStdBOS;
	}

	@RequestMapping(value="/add")
	public String add(HttpSession session,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codetype", "S002001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002001", userInfo.getOrgCode());
		map.put("type", list);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		return "/szzg/greenManager/greenStd/add_greenStd.ftl";
	}

	@RequestMapping(value="/edit")
	public String edit(HttpSession session,@RequestParam(value="seqid")String seqid,
			ModelMap map){
		Long id = Long.parseLong(seqid);
		GreenStdBO greenStd = greenStdService.findGreenStdById(id);
		map.addAttribute("greenStd",greenStd);

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codetype", "S002001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002001", userInfo.getOrgCode());
		map.put("type", list);


		return "/szzg/greenManager/greenStd/edit_greenStd.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public Map<String, Object> save(HttpSession session, ModelMap map,
									@ModelAttribute(value = "greenStdBO") GreenStdBO greenStdBO) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = false;
		Long seqid = greenStdBO.getSeqid();

		greenStdBO.setStatus("1");

		greenStdBO.setType(greenStdBO.getType().trim());

		Map<String, Object> params = new HashMap<String, Object>();

		if (greenStdBO.getType() != null) {
			params.put("type", greenStdBO.getType());
		}

		if (greenStdBO.getSyear() != null) {
			params.put("syear", greenStdBO.getSyear());
		}
		List<GreenStdBO> greenStdBOS = greenStdService.findGreenStdByParams(params);


		if (seqid != null && seqid > 0L) { // --更新

			greenStdBO.setUpdateTime(new Date());
			result = greenStdService.update(greenStdBO);
			resultMap.put("msg", Msg.EDIT.getMsg(result));


		} else { // --新增
			if (greenStdBOS.size() > 0) {
				resultMap.put("msg", "一个区域同一年份只允许一条数据");

			} else {
				greenStdBO.setOpdate(new Date());
				result = greenStdService.insert(greenStdBO);
				result = greenStdBO.getSeqid() > 0;
				resultMap.put("msg", Msg.ADD.getMsg(result));
			}
		}

		resultMap.put("result", result);
		return resultMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request,HttpSession session,ModelMap map,@RequestParam(value = "seqid") Long seqid)
	{
		GreenStdBO greenStdBO = greenStdService.findGreenStdById(seqid);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("id", seqid);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 添加标注操作
		map.addAttribute("greenStd",greenStdBO);
		return "/szzg/greenManager/greenStd/detail_greenStd.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> del(HttpSession session, HttpServletRequest request,
									  @RequestParam(value = "seqid") Long seqid) {
		boolean isSuccess = greenStdService.del(seqid);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("result", 1);
		} else {
			resultMap.put("result", 0);
		}

		resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
		return resultMap;
	}


	@RequestMapping(value = "/findGreenStdByType")
	@ResponseBody
	public Map<String, Object> findGreenStdByType(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		List<GreenStdBO> greenStdList = greenStdService.findGreenStdByParams(params);
		map.put("greenStdList", greenStdList);
		return map;
	}

}
