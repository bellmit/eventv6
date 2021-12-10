package cn.ffcs.zhsq.api;/**
 * Created by Administrator on 2017/8/19.
 */

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhongshm
 * @create 2017-08-19 15:07
 * 诉求门户对接服务
 **/


@Controller
@RequestMapping("/service/publicDemand")
public class PublicDemandServController {

    @Autowired
    private PublicAppealService publicAppealService; //注入公众诉求模块服务
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserManageOutService userManageService;

    @ResponseBody
    @RequestMapping(value="/test")
    public String test(@RequestParam(value="name",defaultValue="success") String name) throws IOException {
        name = "1" + name;
        System.out.println("test");
        List<UserBO> listByUserExampleParamsOut = userManageService.getUserListByUserExampleParamsOut(962L, null, null);
        return name;
    }

    @ResponseBody
    @RequestMapping(value="/save")
    public String save(HttpServletRequest request, HttpSession session, ModelMap map,
                       PublicAppeal bo) throws JSONException, IOException {

        String check = check(bo);
        if(!check.equals("{}"))
            return check;
//        bo.getAppealTime()
        bo.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_01);
//        bo.
        Long insert = publicAppealService.insert(bo);


        JSONObject resultJson = new JSONObject();
        resultJson.put("status", 1);
        resultJson.put("desc", "操作成功");
        resultJson.put("data", insert);
        return resultJson.toString();
    }

    private String check(PublicAppeal bo) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if(bo.getOutId() == null){
            jsonObject.put("status", 0);
            jsonObject.put("desc", "操作失败");
            jsonObject.put("data", "[outId]外置ID不可为空");
        }else if(StringUtils.isBlank(bo.getAppealNo())){
            jsonObject.put("status", 0);
            jsonObject.put("desc", "操作失败");
            jsonObject.put("data", "[appealNo]诉求编号不可为空");
        }
        System.out.println(jsonObject);
        return jsonObject.toString();
    }
}
