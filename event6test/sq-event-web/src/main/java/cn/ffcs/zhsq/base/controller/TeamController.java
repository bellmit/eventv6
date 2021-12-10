/**
 * 
 */
package cn.ffcs.zhsq.base.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.gmis.prvetionTeam.service.IPrvetionTeamService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 队伍控制器
 * @author zhongshm
 *
 */
@Controller
@RequestMapping(value="/team")
public class TeamController extends ZZBaseController{
	@Autowired
	private IPrvetionTeamService prvetionTeamService;
	
	@ResponseBody
	@RequestMapping(value="/search/listData")
	public cn.ffcs.common.EUDGPagination searchResidentListData(HttpSession session,HttpServletRequest req, int page, int rows, String orgCode){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
	    String q = req.getParameter("q");
	    String bizType = req.getParameter("bizType");
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("manager", q);
	    params.put("orgCode", userInfo.getOrgCode());
	    params.put("regionCode", this.getDefaultInfoOrgCode(session));
	    params.put("bizType", bizType);
	    return prvetionTeamService.findPrvetionTeamPagination(page, rows, params);
	}
}