package cn.ffcs.zhsq.bianminPlatform.controller;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/zhsq/event/bianminPlatformDataController")
public class BianminPlatformDataController extends ZZBaseController {
    private static Logger logger = LoggerFactory.getLogger(BianminPlatformDataController.class);

    @RequestMapping(value = "/index")
    public String platformData(HttpSession session, ModelMap map) {
        Map<String, Object> categorysMap = getAllCategorys();
        map.addAttribute("categorys", JsonUtils.mapToJson(categorysMap));

        Date today = new Date();
        map.addAttribute("startTime", DateUtils.formatDate(today, "yyyy") + "-01-01");
        map.addAttribute("endTime", DateUtils.formatDate(today, DateUtils.PATTERN_DATE));

        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        String infoOrgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);

        return "/zzgl/event/syncData/list_fj12345.ftl";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "/listData")
    public Object listData(HttpServletRequest request, HttpSession session, ModelMap map, int page, int rows,
                           String searchField, String keyWord, String catalogId, String handleStatusList, String comeFrom,
                           String kindId, String zoneId, String startTime, String endTime) {
        page = page < 1 ? 1 : page;
        rows = rows < 1 ? 20 : rows;
        Long count = 0L;
        List<Map<String, Object>> callDataList = new ArrayList<Map<String, Object>>();
        try {
            //String urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getSearchCall&format=json";
            String urlNameString = "http://172.21.111.1:80/callcenter/api/fzCallServlet?act=getSearchCall&format=json";
            urlNameString += "&from=" + ((page - 1) * rows);
            urlNameString += "&limit=" + rows;

            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(searchField)) {
                paramMap.put("searchField", searchField.trim());
            } else {
                paramMap.put("searchField", "1");
            }

            if (StringUtils.isNotBlank(keyWord)) {
                paramMap.put("keyWord", URLEncoder.encode(keyWord.trim(), "utf-8"));
            }

            //所属区域
            if (StringUtils.isNotBlank(zoneId) && !"-1".equals(zoneId)) {
                paramMap.put("zoneId", zoneId);
            }

            //诉求类别
            if (StringUtils.isNotBlank(catalogId) && !"-1".equals(catalogId)) {
                paramMap.put("catalogId", catalogId);
            }

            //诉求状态
            if (StringUtils.isNotBlank(handleStatusList) && !"-1".equals(handleStatusList)) {
                paramMap.put("handleStatusList", handleStatusList);
            }

            //诉求来源
            if (StringUtils.isNotBlank(comeFrom) && !"-1".equals(comeFrom)) {
                paramMap.put("comeFrom", comeFrom);
            }

            //诉求类型
            if (StringUtils.isNotBlank(kindId) && !"-1".equals(kindId)) {
                paramMap.put("kindId", kindId);
            }

            //开始时间
            if (StringUtils.isNotBlank(startTime)) {
                paramMap.put("startTime", startTime + " 00:00:00");
            }

            //结束时间
            if (StringUtils.isNotBlank(endTime)) {
                paramMap.put("endTime", endTime + " 23:59:59");
            }

            urlNameString += "&sq=" + URLEncoder.encode(JsonUtils.mapToJson(paramMap), "utf-8");

            String rs = HttpUtil.doPost(urlNameString, "");
            Map<String, Object> data = JsonUtils.json2Map(rs);
            if (data != null && "0".equals(String.valueOf(data.get("retcode")))) {
                count = Long.parseLong(String.valueOf(data.get("totalNum")));
                callDataList = (List<Map<String, Object>>) data.get("data");
                for (int i = 0; i < callDataList.size(); i++) {
                    callDataList.get(i).put("kindName", getKindName(getVal(callDataList.get(i).get("kindId"))));
                    callDataList.get(i).put("source", getSource(getVal(callDataList.get(i).get("comeFrom"))));
                    callDataList.get(i).put("createTime", getTime(callDataList.get(i).get("createTime")));
                }
            } else {
                System.out.println(data.toString());
            }
        } catch (Exception e) {
            logger.info("调用对方接口出错：" + e.getMessage());
        }

        EUDGPagination callDataPagination = new EUDGPagination(count, callDataList);
        return callDataPagination;
    }

    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public Object view(HttpServletRequest request, HttpSession session, ModelMap map, String callId) {
        try {
            //String urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getCall&format=json";
            String urlNameString = "http://172.21.111.1:80/callcenter/api/fzCallServlet?act=getCall&format=json";
            urlNameString += "&callid=" + callId;
            //urlNameString += "&Code=" + getVal(map.get("validateCode"));
            String rs = HttpUtil.doPost(urlNameString, "");
            Map<String, Object> data = JsonUtils.json2Map(rs);
            if (data != null && "0".equals(getVal(data.get("retcode")))) {
                data = (Map<String, Object>) data.get("data");
                data.put("kindName", getKindName(getVal(data.get("kindId"))));
                data.put("source", getSource(getVal(data.get("comeFrom"))));
                data.put("createTime", getTime(data.get("createTime")));
                List<Map<String, Object>> replyList = getReplyList(getVal(data.get("callId")));
                data.put("replyList", replyList);
                map.addAttribute("bo", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.addAttribute("retcode", "17");
            map.addAttribute("msg", "调用对方详情接口失败：" + e.getMessage());
        }
        return "/zzgl/event/syncData/detail_fj12345.ftl";
    }

    private List<Map<String, Object>> getReplyList(String callId) {
        List<Map<String, Object>> replyList = null;
        try {
            //String urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getReplyList&format=json";
            String urlNameString = "http://172.21.111.1:80/callcenter/api/fzCallServlet?act=getReplyList&format=json";

            urlNameString += "&callid=" + callId;
            String rs = HttpUtil.doPost(urlNameString, "");
            Map<String, Object> data = JsonUtils.json2Map(rs);
            if (data != null && "0".equals(getVal(data.get("retcode")))) {
                replyList = (List<Map<String, Object>>) data.get("data");
                for (int i = 0; i < replyList.size(); i++) {
                    replyList.get(i).put("num", i + 1);
                    replyList.get(i).put("replyTime", getTime(replyList.get(i).get("replyTime")));
                }
            }
        } catch (Exception e) {
            logger.info("调用对方接口出错：" + e.getMessage());
        }
        return replyList;
    }

    /**
     * 获取所有诉求件类别接口
     *
     * @return
     */
    private Map<String, Object> getAllCategorys() {
        Map<String, Object> categorysMap = new HashMap<String, Object>();
        try {
            //String urlNameString = "http://np.fj12345.gov.cn/callcenter/api/fzCallServlet?act=getAllCategorys&format=json";
            String urlNameString = "http://172.21.111.1:80/callcenter/api/fzCallServlet?act=getAllCategorys&format=json";
            String rs = HttpUtil.doPost(urlNameString, "");
            Map<String, Object> data = JsonUtils.json2Map(rs);
            if (data != null && "0".equals(getVal(data.get("retcode")))) {
                List<Map<String, Object>> categorys = (List<Map<String, Object>>) data.get("data");
                for (int i = 0; i < categorys.size(); i++) {
                    categorysMap.put(getVal(categorys.get(i).get("catalogId")), categorys.get(i).get("catalogName"));
                }
            }
        } catch (Exception e) {
            logger.info("调用对方接口出错：" + e.getMessage());
        }
        return categorysMap;
    }

    /**
     * 诉求来源
     *
     * @param comeFrom
     * @return
     */
    private String getSource(String comeFrom) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("0", "网站");
        map.put("1", "电话");
        map.put("2", "短信");
        map.put("3", "邮件");
        map.put("4", "传真");
        map.put("5", "市长信箱");
        map.put("6", "其他");
        map.put("7", "录音");
        map.put("8", "QQ");
        map.put("9", "APP");
        return getVal(map.get(comeFrom));
    }

    /**
     * 诉求类型
     *
     * @param kindId
     * @return
     */
    private String getKindName(String kindId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "投诉");
        map.put("2", "建议");
        map.put("3", "咨询");
        map.put("5", "求助");
        return getVal(map.get(kindId));
    }

    private String getVal(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

    private String getTime(Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
        Date dateTime = null;
        try {
            dateTime = sdf.parse(getVal(obj));
        } catch (ParseException e) {
            dateTime = new Date(getVal(obj));
        }
        return DateUtils.formatDate(dateTime, DateUtils.PATTERN_24TIME);
    }
}
