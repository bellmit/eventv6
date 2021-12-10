package cn.ffcs.zhsq.szzg.corp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.domain.App;
/**
 * 企业区域分布 注册资金分析
 * @author linzhu
 *
 */
@Controller
@RequestMapping(value = "/zhsq/szzg/corp")
public class CorpController extends ZZBaseController{
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
    // 模块名称
	private final static String SUB_MAIN = "corp";
	@RequestMapping(value = "/report")
	public String index(HttpServletRequest request,HttpSession session, ModelMap map) {
		String orgCode=request.getParameter("orgCode");
		if(StringUtils.isEmpty(orgCode)){
			orgCode = this.getDefaultInfoOrgCode(session);
		}
		map.put("orgCode", orgCode);
		map.addAttribute("BI_URL", App.BI.getDomain(session));
	    return REAL_PATH + SUB_MAIN + "/report_" + SUB_MAIN + ".ftl";
	 } 
}
