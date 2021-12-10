package cn.ffcs.zhsq.base.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value="/sys")
public class SysController extends ZZBaseController{
	
	@ResponseBody
	@RequestMapping(value="/getSessionId")
	public Map<String, String> getSessionId(HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (userInfo == null) {
			resultMap.put("userId", "");
			resultMap.put("partyName", "");
		} else {
			resultMap.put("userId", userInfo.getUserId().toString());
			resultMap.put("partyName", userInfo.getPartyName());
		}
		resultMap.put("sessionId", session.getId());
		return resultMap;
	}

}

