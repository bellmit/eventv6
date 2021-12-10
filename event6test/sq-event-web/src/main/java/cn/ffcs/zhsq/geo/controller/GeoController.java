package cn.ffcs.zhsq.geo.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @Author sulch
 * @Date 2018-04-12 11:33
 */
@Controller
@RequestMapping(value = "/zhsq/testController")
public class GeoController {

    @RequestMapping(value = "/index")
    public String index(HttpSession session, ModelMap map) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
        map.addAttribute("GEO_DOMAIN", App.GEO.getDomain(session));
        return "/zzgl/geo/index4.ftl";
    }
}
