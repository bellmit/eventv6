package cn.ffcs.zhsq.publicSentiment.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 舆情
 *
 * @Author sulch
 * @Date 2017-01-03 9:18
 */
@Controller
@RequestMapping(value = "/zhsq/publicSentiment/publicSentimentController")
public class PublicSentimentController extends ZZBaseController {
    @Autowired
    private IFunConfigurationService funConfigurationService;

    @RequestMapping(value = "/index")
    public String index(HttpSession session,HttpServletResponse response, ModelMap map){
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String firstUrl = "http://yqg.yingyan189.com/r/mobile/login?account=ffwgsy&password=123456";
        JSONObject firstJson = HttpUtil.doBodyPost(firstUrl, "");
        JSONObject secondJson = new JSONObject();
        String sessionId = "";
        if(firstJson != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject = firstJson.getJSONObject("data");
            if(jsonObject != null && jsonObject.size() >0){
                sessionId = (String) jsonObject.get("sessionId");
            }
        }
//        if(StringUtils.isNotBlank(sessionId)){
//            map.addAttribute("sessionId", sessionId);
//        }
        String DETAIL_URL = "";
        DETAIL_URL =  funConfigurationService.
                changeCodeToValue(ConstantValue.PUBLIC_SENTIMENT, ConstantValue.SENTIMENT_URL, IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
        if(StringUtils.isNotBlank(DETAIL_URL)){
            if(StringUtils.isNotBlank(sessionId)){
                DETAIL_URL = DETAIL_URL + "&sessionId=" +sessionId;
            }
            map.addAttribute("DETAIL_URL", DETAIL_URL);
        }
        return "/zzgl/publicSentiment/index.ftl";
    }


    public static void main(String[] args){
        PublicSentimentController publicSentimentController = new PublicSentimentController();
    }

}
