package cn.ffcs.zhsq.map.test.controller;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.test.service.IPipeLineService;
import cn.ffcs.zhsq.mybatis.domain.map.test.PipeLine;
import cn.ffcs.zhsq.util.PolygonUtils;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/zhsq/map/mapTestController")
public class MapTestController extends ZZBaseController {
    @Autowired
    private IPipeLineService pipeLineService;
    @Autowired
    private IMixedGridInfoService mixedGridInfoService;
    @Autowired
    private IArcgisInfoService arcgisInfoService;

    @RequestMapping(value="/index")
    public String toMapArcgisOfNewVersion(HttpSession session, HttpServletRequest request,
              HttpServletResponse response, ModelMap map,
              @RequestParam(value="pageType", required=false) String pageType) throws Exception{
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String forward = "/map/test/index.ftl";
        MixedGridInfo gridInfo = getMixedGridInfo(session,request);
        if ("smartSpace".equals(pageType)) {
            forward = "/map/test/smartSpace_index.ftl";
        }else if("pipeLine".equals(pageType)){//这个管道测试的是在v3开发库里面测试的
            forward = "/map/test/pipeLineIndex.ftl";
        }
        map.put("gridId", gridInfo.getGridId());
        map.put("gridCode", gridInfo.getGridCode());
        map.put("gridName", gridInfo.getGridName());
        map.put("gridLevel", gridInfo.getGridLevel());
        map.put("socialOrgCode", userInfo.getOrgCode());
        map.put("pageType", pageType);
        map.put("userName", userInfo.getUserName());

        map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
        map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragrma","no-cache");
        response.setDateHeader("Expires",0);
        PolygonUtils polygonUtils = new PolygonUtils(215858l, arcgisInfoService);
        boolean falg = polygonUtils.insidePolygon(119.013177715995, 26.3490182929688);
        System.out.println("falg:"+falg);
        PolygonUtils polygonUtils2 = new PolygonUtils("320904", mixedGridInfoService, arcgisInfoService);
        boolean falg2 = polygonUtils.insidePolygon(119.013177715995, 26.3490182929688);
        System.out.println("falg2:"+falg2);

        PolygonUtils polygonUtils3 = new PolygonUtils();
        double t = polygonUtils.getDistance(119.013177715995, 26.3490182929688,119.013177715995, 26.3490182929688);
        return forward;
    }

    @RequestMapping(value="/standardPipeLine")
    public String pipeLineIndex(HttpSession session, HttpServletRequest request,
                HttpServletResponse response, ModelMap map) throws Exception{
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String forward = "/map/test/standardPipeLine.ftl";
        MixedGridInfo gridInfo = getMixedGridInfo(session,request);
        map.put("gridId", gridInfo.getGridId());
        map.put("gridCode", gridInfo.getGridCode());
        map.put("gridName", gridInfo.getGridName());
        map.put("gridLevel", gridInfo.getGridLevel());
        map.put("socialOrgCode", userInfo.getOrgCode());
        map.put("userName", userInfo.getUserName());
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragrma","no-cache");
        response.setDateHeader("Expires",0);
        return forward;
    }

    @ResponseBody
    @RequestMapping(value="/pipeLineListData")
    public List<PipeLine> pipeLineListData(HttpSession session, HttpServletRequest request, ModelMap map,
           @RequestParam(required = false) String pipeLineKind
    ){
        Map<String,Object> mixedGridInfo = this.getDefaultGridInfo(session);
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(pipeLineKind)) {
            pipeLineKind = pipeLineKind.trim();
            params.put("pipeLineKind", pipeLineKind);
        }
        params.put("infoOrgCode", mixedGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        List<PipeLine> list = pipeLineService.findPipeLineList(params);
        return list;
    }
}

