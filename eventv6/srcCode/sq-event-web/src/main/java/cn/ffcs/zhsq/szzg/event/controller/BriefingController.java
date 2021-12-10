package cn.ffcs.zhsq.szzg.event.controller;

import cn.ffcs.file.mybatis.domain.attachment.AttachmentByUUID;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.file.service.IAttachmentByUUIDService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.utils.http.MyX509TrustManager;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.szzg.event.service.IBriefingService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;


import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import freemarker.template.Template;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 江西省市域社会治理大数据平台项目（运营月报需求）列表页面与数据请求
 * @author lijiaxiong、zhangshengfan
 *
 */

@Controller
@RequestMapping("/zhsq/zzgl/briefingController")
public class BriefingController extends ZZBaseController {
    @Autowired
    private IBriefingService briefingService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IFunConfigurationService funConfigurationService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private IAttachmentByUUIDService attachmentByUUIDService;


    //简报列表页面
    @RequestMapping("/list")
    public ModelAndView listBriefing(HttpServletRequest request, HttpSession session,@RequestParam Map<String, Object> paramMap) throws Exception {
        ModelMap model = new ModelMap();
        String forwardUrl = "zzgl/briefing/briefing_list.ftl";
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        model.addAttribute("defaultInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        model.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
        model.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        model.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE));
        model.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
        model.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
        model.addAllAttributes(paramMap);
        return new ModelAndView(forwardUrl, model);
    }

    //简报详情页面
    @RequestMapping("/report")
    public ModelAndView monthlyReport(HttpServletRequest request, HttpSession session,@RequestParam Map<String, Object> paramMap) throws Exception {
        ModelMap model = new ModelMap();
        String forwardUrl = "zzgl/briefing/monthly_report.ftl";
        String reportId = request.getParameter("reportId");
        String dateStr = paramMap.get("dateStr").toString();
        String gridId = paramMap.get("gridId").toString();
        String infoOrgCode = paramMap.get("infoOrgCode").toString();
        model.addAttribute("gridId", gridId);
        model.addAttribute("infoOrgCode", infoOrgCode);
        model.addAttribute("reportId", reportId);
        model.addAttribute("dateStr",dateStr);
        Map<String, Object> searchparams = new HashMap<String, Object>();
        searchparams.put("bizId", Long.valueOf(reportId));
        List<Map<String, Object>> featureList = briefingService.getEditMessageList(searchparams);
        List<Map<String, Object>> mainFeatureCSList = new ArrayList<>();
        List<Map<String, Object>> mainFeatureCLList = new ArrayList<>();
        List<Map<String, Object>> problemList = new ArrayList<>();
        for (Map<String, Object> temp : featureList) {
            if(CommonFunctions.isNotBlank(temp,"KEY_")){
                if (temp.get("KEY_").toString().startsWith("mainFeatureCS")) {
                    mainFeatureCSList.add(temp);
                    continue;
                } else if (temp.get("KEY_").toString().startsWith("mainFeatureCL")) {
                    mainFeatureCLList.add(temp);
                    continue;
                } else if (temp.get("KEY_").toString().startsWith("problem")) {
                    problemList.add(temp);
                    continue;
                } else {
                    if(CommonFunctions.isNotBlank(temp,"VAL_")){
                        model.addAttribute(temp.get("KEY_").toString(), temp.get("VAL_").toString());
                    }
                }
            }
        }
        model.addAttribute("mainFeatureCSList", mainFeatureCSList);
        model.addAttribute("mainFeatureCLList", mainFeatureCLList);
        model.addAttribute("problemList", problemList);

        //页面表格等其他数据
        Map<String,Object> getDataMap = new HashMap<String,Object>();
        Map<String, Object> lastAndThisDate = getLastAndThisDate(dateStr);
        getDataMap.putAll(lastAndThisDate);
        getDataMap.put("infoOrgCode",infoOrgCode);
        getDataMap.put("gridId",gridId);
        Map<String, Object> pageDataMap = briefingService.queryEventCount(getDataMap);
        Set<String> dataKeySet = pageDataMap.keySet();
        for(String dataKey:dataKeySet){
            model.addAttribute(dataKey,pageDataMap.get(dataKey));
        }
        return new ModelAndView(forwardUrl, model);
    }

    //获取简报列表数据
    @ResponseBody
    @RequestMapping(value = "/briefing/listData", method = RequestMethod.POST)
    public EUDGPagination briefingListData(
            HttpServletRequest request,
            HttpSession session,
            @RequestParam(value = "createTimeStart", required = false) String createTimeStart,
            @RequestParam(value = "createTimeEnd", required = false) String createTimeEnd,
            @RequestParam(value = "eventSource", required = false) String eventSource,
            @RequestParam(value = "gridId", required = false) String gridId,
            @RequestParam Map<String, Object> paramMap) throws Exception {
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
        paramMap.put("infoOrgCode",infoOrgCode);
        if(CommonFunctions.isNotBlank(paramMap,"dateEnd")){
            paramMap.put("dateEnd",paramMap.get("dateEnd").toString());
        }else{
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.MONTH, -1);
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date time = ca.getTime();
            String reportDate = DateUtils.formatDate(time, "yyyy-MM-dd");
            paramMap.put("dateEnd",reportDate);
        }
        return briefingService.getList(paramMap);
    }

    @ResponseBody
	@RequestMapping(value = "/briefing4Jsonp")
	public String briefing4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) throws Exception {
		String jsonpcallback = "";
		List<Map<String, Object>> resultMap = null;
		if(CommonFunctions.isNotBlank(params, "gridCode")) {
			resultMap = briefingService.getBriefingList(params);
			MixedGridInfo mg = getGridInfoByOrgCode(params.get("gridCode").toString());
			Long gridId = mg.getGridId();
			String gridCode = params.get("gridCode").toString();
			for (Map<String, Object> map : resultMap) {
				map.put("URL", "/business/stat/report.jhtml?infoOrgCode="+gridCode+"&gridId="+gridId+
						"&dateStr="+DateUtils.formatDate(map.get("DATE_").toString(), "yyyyMM")+"&reportId="+map.get("REPORT_ID").toString());
			}
		}else {
			resultMap = new ArrayList<Map<String,Object>>();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
    //保存编辑信息
    @ResponseBody
    @RequestMapping(value = "/briefing/saveDetail", method = RequestMethod.POST)
    public String saveDetail(
            HttpServletRequest request,
            HttpSession session,
            @RequestParam(value = "mainFeatureCS", required = false) String[] mainFeatureCSs,
            @RequestParam(value = "mainFeatureCL", required = false) String[] mainFeatureCLs,
            @RequestParam(value = "problem", required = false) String[] problems,
            @RequestParam Map<String, Object> paramMap) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();

        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        String reportId = request.getParameter("reportId");

        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);

        String regionCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();

        if(CommonFunctions.isNotBlank(paramMap,"overallSituation")){
            map.put("overallSituation", paramMap.get("overallSituation"));
        }

        if(CommonFunctions.isNotBlank(paramMap,"typeAnalysis")){
            map.put("typeAnalysis", paramMap.get("typeAnalysis"));
        }

        if(CommonFunctions.isNotBlank(paramMap,"reportChannel")){
            map.put("reportChannel", paramMap.get("reportChannel"));
        }

        if(paramMap.containsKey("cityLevelSituation")){
            if(CommonFunctions.isNotBlank(paramMap,"cityLevelSituation")){
                map.put("cityLevelSituation", paramMap.get("cityLevelSituation"));
            }
        }

        if(CommonFunctions.isNotBlank(paramMap,"townLevelSituation")){
            map.put("townLevelSituation", paramMap.get("townLevelSituation"));
        }

        Set<String> sets = map.keySet();
        for (String s : sets) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if(CommonFunctions.isNotBlank(paramMap,s)){
                tempMap.put("key", s);
                tempMap.put("val", map.get(s).toString());
                tempMap.put("bizId", Integer.valueOf(reportId));
                tempMap.put("regionCode", regionCode);
                tempMap.put("creator", userInfo.getUserId());
                tempMap.put("updater", userInfo.getUserId());
            }
            dataList.add(tempMap);
        }

        for (int i = 0, j = mainFeatureCSs.length; i < j; i++) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if(!StringUtils.isBlank(mainFeatureCSs[i])){
                tempMap.put("key", "mainFeatureCS_" + i);
                tempMap.put("val", mainFeatureCSs[i]);
                tempMap.put("bizId", Integer.valueOf(reportId));
                tempMap.put("regionCode", regionCode);
                tempMap.put("creator", userInfo.getUserId());
                tempMap.put("updater", userInfo.getUserId());
            }
            dataList.add(tempMap);
        }

        for (int i = 0, j = mainFeatureCLs.length; i < j; i++) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if(!StringUtils.isBlank(mainFeatureCLs[i])){
                tempMap.put("key", "mainFeatureCL_" + i);
                tempMap.put("val", mainFeatureCLs[i]);
                tempMap.put("bizId", Integer.valueOf(reportId));
                tempMap.put("regionCode", regionCode);
                tempMap.put("creator", userInfo.getUserId());
                tempMap.put("updater", userInfo.getUserId());
            }
            dataList.add(tempMap);
        }

        for (int i = 0, j = problems.length; i < j; i++) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            if(!StringUtils.isBlank(problems[i])){
                tempMap.put("key", "problem_" + i);
                tempMap.put("val", problems[i]);
                tempMap.put("bizId", Integer.valueOf(reportId));
                tempMap.put("regionCode", regionCode);
                tempMap.put("creator", userInfo.getUserId());
                tempMap.put("updater", userInfo.getUserId());
            }
            dataList.add(tempMap);
        }
        result.put("dataList", dataList);
        briefingService.deleteEditMessage(Integer.valueOf(reportId));
        briefingService.addEditMessage(result);
        return "success";
    }

    @RequestMapping("/importPDF")
    @ResponseBody
    public Object importPdf(HttpServletRequest request, HttpSession session, ModelMap map,
                            @RequestParam Map<String, Object> paramMap,
                            @RequestParam(value = "attachmentId", required = false) Long[] ids) {
        String reportId = paramMap.get("reportId").toString();
        boolean result = false;

        if (ids.length == 0) {
            return 0;
        }
        Long attachmentId = ids[0];
        
        //先删除库中存在的附件
        attachmentService.deleteByBizId(Long.valueOf(reportId), "BRIEF_REPORT");
        if (StringUtils.isNotBlank(attachmentId.toString()) && reportId != null && Long.valueOf(reportId) > 0) {
            result = attachmentService.updateBizId(Long.valueOf(reportId), "BRIEF_REPORT", attachmentId.toString());
        }
        if (result) {
            Attachment byId = attachmentService.findById(attachmentId);
            String pdfPath = byId.getFilePath();
            Map<String, Object> pdfUrlMap = new HashMap<String, Object>();
            pdfUrlMap.put("pdfUrl", pdfPath);
            pdfUrlMap.put("reportId", reportId);
            Integer update = briefingService.update(pdfUrlMap);
            return update;
        } else {
            return 0;
        }
    }

    @RequestMapping("/exportPdf")
    public void exportPdf(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap map,
                            @RequestParam Map<String, Object> paramMap) throws Exception {

//        String reportId = paramMap.get("reportId").toString();
////        Map<String, Object> getReportMap = new HashMap<String, Object>();
////        getReportMap.put("reportId", reportId);
////        List<Map<String, Object>> reports = briefingService.findById(getReportMap);
//        String reportId = paramMap.get("reportId").toString();

        String attaId = "";
        String fileUrl = "";
        String reportId = paramMap.get("reportId").toString();

        List<Attachment> brief_report = attachmentService.findByBizId(Long.valueOf(reportId), "BRIEF_REPORT");
        if(brief_report!=null&&brief_report.size()>0){
            attaId=brief_report.get(0).getAttachmentId().toString();
            fileUrl=brief_report.get(0).getFilePath();
        }
        String userAgent = getHeaderString(request, "User-Agent","");
        String fileName = null;
        String filePath = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        if( StringUtils.isNotEmpty(fileUrl)){
            filePath =  funConfigurationService.getAppDomain("$IMG_DOMAIN", null, null) + fileUrl;
            List<Map<String, Object>> findById = briefingService.findById(paramMap);
            Calendar ca = Calendar.getInstance();
            ca.setTime((Date) findById.get(0).get("DATE_"));
            Date time = ca.getTime();
            String reportDate = DateUtils.formatDate(time, "yyyy年MM月");
            
            String  INFOORGCODE =  findById.get(0).get("INFOORGCODE").toString();
            MixedGridInfo gridInfoByInfoOrgCode = mixedGridInfoService.getGridInfoByInfoOrgCode(INFOORGCODE);
            String gridName="";
            if(gridInfoByInfoOrgCode!=null) {
            	gridName=gridInfoByInfoOrgCode.getGridName();
            }
            
            fileName = reportDate+gridName+"月报.pdf";

        }else if(attaId != null) {
            AttachmentByUUID att = attachmentByUUIDService.findById(Long.valueOf(attaId));

            if (att != null) {
                filePath = funConfigurationService.getAppDomain("$IMG_DOMAIN", null, null) + att.getFilePath();
                fileName = att.getFileName();
            }
        }
        try {
            // url判断
            if (null == filePath || filePath.length() == 0) {
                throw new RuntimeException("附件URL异常");
            }

            //========判断浏览器类型======
            //判断是否是IE11以下版本
            boolean flag= userAgent.toLowerCase(Locale.ENGLISH).matches("(?:trident)(?:.*rv:([\\w.]+))");
            flag = flag && !(userAgent.toUpperCase().indexOf("MSIE") > 0);
            //判断是否是火狐
           // boolean flagFox = false;
            if (flag) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replace("+", "%20");//encode后替换  解决空格问题
            }
            // 清除response中的缓存
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName="+ fileName);
            byte[] buff_ = fileUploadService.downloadFileWithPath(fileUrl);
            response.setHeader("Content-Length", String.valueOf(buff_.length));
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(buff_);
            // 将所有的读取的流返回给客户端
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件下载失败！");
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("关闭流异常！");
            }
        }
    }

    public static String getHeaderString(HttpServletRequest request, String headerName, String defaultString) {
        String temp = request.getHeader(headerName);
        return (temp == null)||(temp.length()<=0) ? defaultString : temp;
    }

    @RequestMapping(value = "/exportWord")
    public void exportWord(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap map,
                           @RequestParam Map<String, Object> params,
                           @RequestParam(value = "mainFeatureCSList", required = false) String[] mainFeatureCSList,
                           @RequestParam(value = "mainFeatureCLList", required = false) String[] mainFeatureCLList,
                           @RequestParam(value = "problemList", required = false) String[] problemList) throws Exception {
    	String infoOrgCode =null;
    	String gridId = null;
        if(params.get("infoOrgCode") != null || params.get("gridId") != null) {
        	 infoOrgCode = params.get("infoOrgCode").toString();
             gridId = params.get("gridId").toString();
        }else {
        	Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
            infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
            gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
        }
        Map<String,Object> queryEventCountParams = new HashMap<String, Object>();
        queryEventCountParams.put("gridId",gridId);
        queryEventCountParams.put("infoOrgCode",infoOrgCode);
        queryEventCountParams.putAll(getLastAndThisDate(params.get("dateStr").toString()));
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            Map<String, Object> overallSituationDataMap = briefingService.queryEventCount(queryEventCountParams);
            dataMap.putAll(overallSituationDataMap);
            //图1
            String img1Data = params.get("img1Data").toString();
            dataMap.put("img1",handleImg(img1Data));
            //图2
            String img2Data = params.get("img2Data").toString();
            dataMap.put("img2",handleImg(img2Data));
            //图3
            String img3Data = params.get("img3Data").toString();
            dataMap.put("img3",handleImg(img3Data));
            //图4
            String img4Data = params.get("img4Data").toString();
            dataMap.put("img4",handleImg(img4Data));
                //图5
                String img5Data = params.get("img5Data").toString();
                dataMap.put("img5",handleImg(img5Data));
                //图6
                String img6Data = params.get("img6Data").toString();
                dataMap.put("img6",handleImg(img6Data));
                //图7
                String img7Data = params.get("img7Data").toString();
                dataMap.put("img7",handleImg(img7Data));
                //图8
                String img8Data = params.get("img8Data").toString();
                dataMap.put("img8",handleImg(img8Data));
                //图9
                String img9Data = params.get("img9Data").toString();
                dataMap.put("img9",handleImg(img9Data));
                //图10
                String img10Data = params.get("img10Data").toString();
                dataMap.put("img10",handleImg(img10Data));
                //图11
                String img11Data = params.get("img11Data").toString();
                dataMap.put("img11",handleImg(img11Data));
                //图12
                String img12Data = params.get("img12Data").toString();
                dataMap.put("img12",handleImg(img12Data));
                //图12
                String img13Data = params.get("img13Data").toString();
                dataMap.put("img13",handleImg(img13Data));


            dataMap.put("OverallSituation",params.get("overallSituation"));
            dataMap.put("TypeAnalysis",params.get("typeAnalysis"));
            dataMap.put("ReportChannel",params.get("reportChannel"));
            dataMap.put("CitySituation",params.get("cityLevelSituation"));
            dataMap.put("CountySituation",params.get("townLevelSituation"));
            dataMap.put("mainFeatureCSList",mainFeatureCSList);
            dataMap.put("mainFeatureCLList",mainFeatureCLList);
            dataMap.put("problemList",problemList);

            String fileName ;
            Calendar ca = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
            Date date = sdf1.parse(params.get("dateStr").toString());
            ca.setTime(date);
            Date time = ca.getTime();
            String reportDate = DateUtils.formatDate(time, "yyyy年MM月");

            MixedGridInfo gridInfoByInfoOrgCode = mixedGridInfoService.getGridInfoByInfoOrgCode(infoOrgCode);
            String gridName="";
            if(gridInfoByInfoOrgCode!=null) {
                gridName=gridInfoByInfoOrgCode.getGridName();
            }

            fileName = reportDate+gridName+"月报";
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            fileName = java.net.URLEncoder.encode(fileName, "UTF8");
            response.setHeader("Content-Disposition", "attachment;filename=" + (fileName + ".doc"));
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setDirectoryForTemplateLoading(new File(request.getSession().getServletContext().getRealPath("") + File.separator + "upload" + File.separator + "excel" + File.separator + "model" + File.separator));//ָftl模板位置
            PrintWriter outw = response.getWriter();
            Template t = configuration.getTemplate("provinceBriefing.ftl", "utf-8");//设置编码
            t.process(dataMap, outw);
            outw.flush();
            outw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String handleImg(String str){
       // Map<String,Object> dataMap = new HashMap<>();
        String[] arr = str.split("base64,");
        String res = arr[1];
        return res;
    }
    /**
     * 根据附件地址获取输入流
     *
     * @param filePath
     * @return InputStream
     * @throws Exception
     */
    public InputStream fileURL2InputStream(String filePath) throws Exception {

        InputStream inputStream = null;
        if (StringUtils.isBlank(filePath)) {
            return null;
        } else {
            URL url = new URL(filePath);

            if (url.toURI().getScheme().equals("https")) {

                TrustManager[] tm = {new MyX509TrustManager()};
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
// 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();

                HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn.setSSLSocketFactory(ssf);

// 防止屏蔽程序抓取而返回403错误
                httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                inputStream = httpUrlConn.getInputStream();
            } else if (url.toURI().getScheme().equals("ftp")) {
// 获取图片在ftp上的路径(上传后地址 ftp://222.76.242.47/事件对接/同安区环保网格/20200509/图片.jpg)
// 挂载：/var/ftp/pub/xmhbwg + /事件对接/同安区环保网格/20200509/图片.jpg
                String resouceUrl = "/mnt/mfs/sq_upload";
                String path = resouceUrl + url.getPath();
                inputStream = new FileInputStream(path);

            } else {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3 * 5000);
// 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                inputStream = conn.getInputStream();
            }

        }
        return inputStream;
    }
    public  Map<String, Object> getLastAndThisDate(String datastr) {
        Map<String,Object> params = new HashMap<>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        Date date = null;
        try {
            date = sdf1.parse(datastr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date date1= cal.getTime();
        String thisMonth = sdf1.format(date);
        String lastMonth = sdf1.format(date1);
        params.put("thisMonth",thisMonth);
        params.put("lastMonth",lastMonth);
        return params;
    }

    /**
     * 事件统计分析之事件弹窗列表
     * @param request
     * @param session
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/eventList")
    public ModelAndView eventList(HttpServletRequest request, HttpSession session,@RequestParam Map<String, Object> paramMap) throws Exception {
        ModelMap model = new ModelMap();
        String forwardUrl = "zzgl/briefing/eventList.ftl";
        if(CommonFunctions.isNotBlank(paramMap,"infoOrgCode")){
            MixedGridInfo findMixedGridInfoByInfoOrgCode = mixedGridInfoService.getGridInfoByInfoOrgCode(paramMap.get("infoOrgCode").toString());
            paramMap.put("gridId",findMixedGridInfoByInfoOrgCode.getGridId());
        }
        model.addAllAttributes(paramMap);
        return new ModelAndView(forwardUrl, model);
    }
 /**
	 * 获取报表事件列表页
	 */
	@RequestMapping("/getEventListData")
	@ResponseBody
	public EUDGPagination getEventListData(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		return briefingService.getEventListData(params, userInfo);
	}

/**
     * 获取附件ID
     * @param request
     * @param session
     * @param paramMap
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getAttachmentId")
    public Map<String,Object> getAttachmentId(HttpServletRequest request, HttpSession session,@RequestParam Map<String, Object> paramMap){
        Map<String,Object> map = new HashMap<String,Object>();
        if(CommonFunctions.isNotBlank(paramMap,"reportId")){
            List<Attachment> byBizId = attachmentService.findByBizId(Long.valueOf(paramMap.get("reportId").toString()), "BRIEF_REPORT");
            if(byBizId!=null && byBizId.size()>0){
                map.put("attaId",byBizId.get(0).getAttachmentId());
            }
        }
        return map;
    }

    /**
     * 删除简报附件后删除简报表PdfUrl
     * @param request
     * @param session
     * @param paramMap
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delReportPdfUrl")
    public Map<String,Object> delReportPdfUrl(HttpServletRequest request, HttpSession session,@RequestParam Map<String, Object> paramMap){
        Map<String,Object> map = new HashMap<String,Object>();
        if(CommonFunctions.isNotBlank(paramMap,"reportId")){
            String reportId = paramMap.get("reportId").toString();
            map.put("reportId",Long.valueOf(reportId));
            map.put("pdfUrl","");
            briefingService.update(map);
        }
        return map;
    }
}

