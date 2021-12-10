package cn.ffcs.zhsq.checkConfig.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.checkConfig.service.ICheckConfigBaseService;
import cn.ffcs.zhsq.checkConfig.service.ICheckConfigService;
import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;

/**
 * 检测功能配置控制器
 * @author sulch
 * 2015年6月16日 下午2:27:54
 */
@Controller
@RequestMapping(value = "/zhsq/checkConfigController")
public class CheckConfigController extends ZZBaseController {
	@Autowired 
	private ICheckConfigService checkConfigService;
	@Autowired
	private ICheckConfigBaseService checkConfigBaseService;
	/**
	 * <p>Description:检测功能配置入口</p>
	 * @param session
	 * @param map
	 * @return
	 * add sulch
	 * 2015年6月16日 下午2:34:17
	 */
	@RequestMapping(value = "/index")
	public String index(HttpSession session, ModelMap map){
		String[] checkConfigServiceImplNames = checkConfigBaseService.getCheckConfigServiceImplNames();
		List<String> checkConfigServicesList = Arrays.asList(checkConfigServiceImplNames);
		map.addAttribute("checkConfigServicesList", checkConfigServicesList);
		return "/zzgl/checkConfig/index.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/startCheck")
	public CheckResultInfo startCheck(HttpSession session,
			@RequestParam(value = "checkConfigServiceName",required = false) String checkConfigServiceName,
			ModelMap map){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		String orgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		//开始检测
		CheckResultInfo checkResultInfo = new CheckResultInfo();
		if(StringUtils.isNotBlank(checkConfigServiceName)){
			checkResultInfo = checkConfigBaseService.startCheck(orgCode,checkConfigServiceName);
		}
		return checkResultInfo;
	} 
}
