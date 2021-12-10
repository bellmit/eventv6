package cn.ffcs.zhsq.event.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventTypeProcCfg;
import cn.ffcs.zhsq.mybatis.domain.event.EventTypeProcCfgVO;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * @author YangCQ
 */
@Controller
@RequestMapping("/zhsq/event/eventTypeProcCfgController")
public class EventTypeProcCfgController extends ZZBaseController {

	@Autowired
	private IEventTypeProcCfg eventTypeProcCfg;

	@RequestMapping(value = "/index")
	public String index(HttpSession session, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.put("regionName", defaultGridInfo.get(KEY_START_GRID_NAME));
		return "/zzgl/event/typeProcCfg/index.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String regionCode = request.getParameter("regionCode");
		String type = request.getParameter("type");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionCode", regionCode);
		params.put("type", type);
		params.put("userOrgCode", userInfo.getOrgCode());
		EUDGPagination eudgPagination = eventTypeProcCfg.findListPagination(page, rows, params);

		return eudgPagination;
	}

	@RequestMapping(value = "/toSavePage")
	public String toSavePage(HttpSession session, ModelMap map,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "type", required = false) String type) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventTypeProcCfgVO vo = null;
		if (StringUtils.isNotBlank(regionCode) && StringUtils.isNotBlank(type)) {
			vo = eventTypeProcCfg.findVO(regionCode, type, userInfo.getOrgCode());
		} else {
			vo = new EventTypeProcCfgVO();
			map.addAttribute("optType", "0");
		}
		map.addAttribute("vo", vo);
		return "/zzgl/event/typeProcCfg/savePage.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public ResultObj save(HttpSession session, ModelMap map,
			@RequestParam(value = "optType", required = false) String optType,
			@ModelAttribute(value = "vo") EventTypeProcCfgVO vo) throws Exception {
		if (vo != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			if ("0".equals(optType)) {// 新增
				EventTypeProcCfgVO tempVo = eventTypeProcCfg.findVO(vo.getRegionCode(), vo.getType(),
						userInfo.getOrgCode());
				if (tempVo != null) {
					return Msg.OPERATE.getResult(false, "所属地域与事件类型已存在，请重新选择！");
				} else {
					return Msg.ADD.getResult(eventTypeProcCfg.save(vo));
				}
			} else {
				return Msg.EDIT.getResult(eventTypeProcCfg.update(vo));
			}
		} else {
			return Msg.OPERATE.getResult(false, "无效数据！");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public ResultObj batchDeleteCompany(HttpSession session,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "type", required = false) String type) {
		return Msg.DELETE.getResult(eventTypeProcCfg.delete(regionCode, type));
	}
}
