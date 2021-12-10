package cn.ffcs.zhsq.api;/**
 * Created by Administrator on 2017/8/19.
 */

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

/**
 *
 */
public class ApiBaseServController {


    @ResponseBody
    @RequestMapping(value="/test")
    public String test(@RequestParam(value="name",defaultValue="success") String name){
//        Map<String, Object> eventVerify = new HashMap<String, Object>();
//        eventVerify.put("infoOrgCode","35");
//        eventVerify.put("userOrgCode","35");
//        eventVerify.put("bizPlatform","202");
//
//        EUDGPagination eventVerifyPagination = eventVerifyBaseService.findEventVerifyPagination(0, 100, eventVerify);
//        System.out.println(eventVerifyPagination);

        return name;
    }

}
