package cn.ffcs.zhsq.eliminatelettertho.controller;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.file.vo.ZipCompress;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoReChgService;
import cn.ffcs.zhsq.eliminatelettertho.service.IEliminateLetterThoService;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterIndus;
import cn.ffcs.zhsq.mybatis.domain.eliminatelettertho.EliminateLetterTho;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: 三书一函主表模块控制器
 * @Author: liangbzh
 * @Date: 08-09 16:36:03
 * @Copyright: 2021 福富软件
 */
@Controller("eliminateLetterThoExportController")
@RequestMapping("/zhsq/eliminateLetterThoExport")
public class EliminateLetterThoExportController {
    private static Logger logger = LoggerFactory.getLogger(EliminateLetterThoExportController.class);

    @Autowired
    private IEliminateLetterThoService eliminateLetterThoService;

    @Autowired
    private IEliminateLetterThoReChgService eliminateLetterThoReChgService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoOutService;
    @Autowired
    private IBaseDictionaryService baseDictionaryService;
    @Autowired
    private FileUploadService fileUploadService;
    private int startRow = 5;//导出数据起始行

    private static final String PROVINCE_LEVEL = "PROVINCE_LEVEL";
    private static final String CITY_LEVEL = "CITY_LEVEL";
    private static final String COUNTY_LEVEL = "COUNTY_LEVEL";

    /**
     * 勾选中 ☑
     */
    private static final String CHECK = "☑";
    /**
     * 未选中  □
     */
    private static final String UN_CHECK = "☐";

    /**
     * 导出
     */
    @ResponseBody
    @RequestMapping("/exportOut")
    public Object exportOut(HttpServletRequest request, HttpServletResponse response, EliminateLetterTho bo) throws IllegalAccessException, IOException {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
        String regionCode = userInfo.getRegionCode();
        Integer page = Integer.valueOf(1);
        Integer row = Integer.valueOf(10000);
        InputStream in = null;
        OutputStream output = null;
        String excelSuffix = ".xlsx";
        String appCode = "三书一函";//三书一函eliminateLetterTho
        try {
            String fileName = "eliminateLetterThoExport" + excelSuffix;
            String templatePath = request.getSession().getServletContext().getRealPath("") + "//export//" + fileName;

            // 将excel导出的文件位置
            String filePath = request.getSession().getServletContext().getRealPath("");
            // 得到此路径下文件
            File fileDir = new File(filePath);
            //创建文件夹
            if (!fileDir.exists() && !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }
            // 用于存放生成的excel文件名称
            List<String> fileNames = new ArrayList<String>();
            List<ZipCompress> zipCompressList = new ArrayList<ZipCompress>();
            // 导出Excel文件路径
            String fullFilePath = "";
            //输出流
            FileOutputStream os = null;
            ByteArrayOutputStream bao = null;
            //循环导出excel到临时文件夹中
            String[] uuidArr = bo.getExportThoUuid().split(",");
            for (int i = 0;i<uuidArr.length; i++) {
                // 往excel填入内容
                in = new FileInputStream(new File(templatePath));
                XSSFWorkbook work = new XSSFWorkbook(in);
                XSSFSheet sheet = null;
                sheet = work.getSheetAt(0);

                EliminateLetterTho exportBean = new EliminateLetterTho();
                exportBean.setThoUuid(uuidArr[i]);
                EliminateLetterTho searchBean = eliminateLetterThoService.searchByThoUuid(exportBean);
                //每次导出的excel的文件名
                Long nowTime = DateUtils.getNowTime();
                String title = setExcel(sheet,searchBean,regionCode)+"_"+nowTime + excelSuffix;
                if (searchBean != null) {
//                    in = new FileInputStream(new File(templatePath));

                    // 导出excel的全路径
                    fullFilePath = filePath + File.separator + title;
                    fileNames.add(fullFilePath);
                    os = new FileOutputStream(fullFilePath);
                    // 写文件
                    work.write(os);
                    bao = new ByteArrayOutputStream();
                    os.write(bao.toByteArray());
                    byte[] bytes = FileUtils.readFileToByteArray(new File(fullFilePath));
                    String singleFilePath = fileUploadService.uploadSingleFile(title, bytes, appCode, nowTime + "");
                    String showPath = App.IMG.getDomain(request.getSession()) + singleFilePath;
                    logger.error("singleFilePath:{}",singleFilePath);
                    logger.error("showPath:{}",showPath);
                    ZipCompress zipCompress = ZipCompress.getInstance(singleFilePath,title);
                    zipCompressList.add(zipCompress);
                }
                //清空流缓冲区数据
                os.flush();
                //关闭流
                bao.close();
                os.close();
                in.close();
                os = null;
                new File(fullFilePath).delete();
            }
            Long zipNowTime = DateUtils.getNowTime();
            logger.error("zipNowTime:{}",zipNowTime);
            String realStorePath = fileUploadService.toZipFile(zipCompressList, appCode, zipNowTime + "");
            logger.error("realStorePath:{}",realStorePath);
            //导出压缩文件的全路径
            String zipFileName = "三书一函_"+DateUtils.getNowTime()+".zip";
            //导出zip
//            File zip = new File(App.IMG.getDomain(request.getSession()) + File.separator + realStorePath);
//            downloadZip(zip,zipFileName,response);
            logger.error("路径:{}",App.IMG.getDomain(request.getSession()) + "/" + realStorePath);
            logger.error("路径:{}",App.IMG.getDomain(request.getSession()));
            return App.IMG.getDomain(request.getSession()) + "/" + realStorePath;
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            String content = "<script type='text/javascript'>alert('下载异常');window.close();</script>";
            output.write(content.getBytes("UTF-8"));

        } finally {
            if (in != null) {
                in.close();
            }
            if (output != null) {
                output.close();
            }
        }
        return null;
    }

    /**
     * 设置每份Excel的值
     */
    private String setExcel(XSSFSheet sheet, EliminateLetterTho letter, String regionCode){
        changeCodeToName(letter,regionCode);

        String D5 = letter.getThtNo();
        excelSetStringValue(sheet, 5, 4, D5);//三书一函编码
        String D7 = letter.getLetterTypeCN();
        excelSetStringValue(sheet, 7, 4, D7);//文书类型

        Map<String, String> areaLevelMap = getAreaLevel(letter.getFbDepartCode());
        String D9 = areaLevelMap.get(PROVINCE_LEVEL);
        excelSetStringValue(sheet, 9, 4, D9);//省
        String H9 = areaLevelMap.get(CITY_LEVEL);
        excelSetStringValue(sheet, 9, 8, H9);//市
        String L9 = areaLevelMap.get(COUNTY_LEVEL);
        excelSetStringValue(sheet, 9, 12, L9);//县
        //获取Excel标题，标题=制发单位地域+案件名+文书类型名称
        String title =D9 + H9 + L9 +letter.getCaseName()+letter.getLetterTypeCN();

        String P9 = letter.getFbDepartNameDet();
        excelSetStringValue(sheet, 9, 16, P9);//制发单位

        String D10 = DateUtils.formatDate(letter.getFbDate(), "yyyy");
        excelSetStringValue(sheet, 10, 4, D10);//发出时间——年
        String F10 = DateUtils.formatDate(letter.getFbDate(), "MM");
        excelSetStringValue(sheet, 10, 6, F10);//发出时间——月
        String H10 = DateUtils.formatDate(letter.getFbDate(), "dd");
        excelSetStringValue(sheet, 10, 8, H10);//发出时间——日

        String D11 = letter.getLetterNo();
        excelSetStringValue(sheet, 11, 4, D11);//文号
        String D12 = letter.getCaseName();
        excelSetStringValue(sheet, 12, 4, D12);//案件名称
        String D13 = letter.getCaseNo();
        excelSetStringValue(sheet, 13, 4, D13);//案件编码
        //☐ ☑ ☐
        //String D14 = "☑社会治安 ☐乡村治理 ☐金融扶贫 ☐工程建设 \r\n☐交通运输 ☐市场流通 ☐自然环保 ☐信息网络 \r\n☐文化旅游 ☐教育卫生 ☐其他";
        String D14 = letter.getIndusCodeArr();
        //将未选中☐替换为已选中☑
        excelSetStringValue(sheet, 14, 4, D14);//行业领域名称

        String D15 = letter.getIndustrialComment();
        excelSetStringValue(sheet, 15, 4, D15);//补充说明
        String D16 = letter.getLetterContentClob();
        excelSetStringValue(sheet, 16, 4, D16);//文书内容

        OrgEntityInfoBO orgEntityInfoBO = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(letter.getReChgs().getReDepartCode());
        Map<String, String> reDepartLevelMap = getAreaLevel(orgEntityInfoBO.getOrgCode());
        String D19 = reDepartLevelMap.get(PROVINCE_LEVEL);
        excelSetStringValue(sheet, 19, 4, D19);//接收单位——省
        String H19 = reDepartLevelMap.get(CITY_LEVEL);
        excelSetStringValue(sheet, 19, 8, H19);//接收单位——市
        String L19 = reDepartLevelMap.get(COUNTY_LEVEL);
        excelSetStringValue(sheet, 19, 12, L19);//接收单位——县
        String P19 = letter.getReChgs().getReDepartNameDet();
        excelSetStringValue(sheet, 19, 16, P19);//接收单位——制发单位

        String D20 = DateUtils.formatDate(letter.getReChgs().getReDate(), "yyyy");
        excelSetStringValue(sheet, 20, 4, D20);//接收时间——年
        String F20 = DateUtils.formatDate(letter.getReChgs().getReDate(), "MM");
        excelSetStringValue(sheet, 20, 6, F20);//接收时间——月
        String H20 = DateUtils.formatDate(letter.getReChgs().getReDate(), "dd");
        excelSetStringValue(sheet, 20, 8, H20);//接收时间——日

        String D21 = letter.getReChgs().getReType().equals("1") ? "按期回复" : "未按期回复";
        excelSetStringValue(sheet, 21, 4, D21);//回复情况
        String D22 = letter.getReChgs().getReDetail();
        excelSetStringValue(sheet, 22, 4, D22);//回复详情
        //String D23 = letter.getReChgs().getReDissentAgree().equals("1") ? "是" : "否";
        String D23 = StringUtils.isBlank(letter.getReChgs().getReDissentAgree()) ? "" : (
                letter.getReChgs().getReDissentAgree().equals("1") ? "是" : "否"
        );
        excelSetStringValue(sheet, 23, 4, D23);//是否提出异议
        String D24 = letter.getReChgs().getReDissentDetail();
        excelSetStringValue(sheet, 24, 4, D24);//异议详情

        String D26 = StringUtils.isBlank(letter.getReChgs().getDissentAgree()) ? "" : (
                letter.getReChgs().getDissentAgree().equals("1") ? "是" : "否"
        );
        excelSetStringValue(sheet, 26, 4, D26);//是否同意异议
        String D27 = letter.getReChgs().getDissentDetail();
        excelSetStringValue(sheet, 27, 4, D27);//详细信息

        if(D23.equals("是")){//提出异议的话不需要填写以下字段
            excelSetStringValue(sheet, 29, 4, "");//是否开展整改
            excelSetStringValue(sheet, 30, 4, "");//整改详情
            excelSetStringValue(sheet, 31, 4, "");//是否开展行业治理
            excelSetStringValue(sheet, 32, 4, "");//开展行业治理情况
            excelSetStringValue(sheet, 33, 4, "");//是否建立长效机制
            excelSetStringValue(sheet, 34, 4, "");//建立长效机制情况
        }else{
            String D29 = letter.getReChgs().getChgTypeCN();
            excelSetStringValue(sheet, 29, 4, D29);//是否开展整改
            String D30 = letter.getReChgs().getChgDetail();
            excelSetStringValue(sheet, 30, 4, D30);//整改详情
            String D31 = letter.getReChgs().getIndusChgAgree().equals("1") ? "是" : "否";
            excelSetStringValue(sheet, 31, 4, D31);//是否开展行业治理
            String D32 = letter.getReChgs().getIndusChgDetail();
            excelSetStringValue(sheet, 32, 4, D32);//开展行业治理情况
            String D33 = letter.getReChgs().getLongActionAgree().equals("1") ? "是" : "否";
            excelSetStringValue(sheet, 33, 4, D33);//是否建立长效机制
            String D34 = letter.getReChgs().getLongActionDetail();
            excelSetStringValue(sheet, 34, 4, D34);//建立长效机制情况
        }
        String D35 = letter.getReChgs().getOthClob();
        excelSetStringValue(sheet, 35, 4, D35);//其他需要说明的情况

        return title;
    }


    /**
     * 设置单元格的值
     * @param sheet
     * @param rowNum
     * @param cellNum
     * @param value
     */
    public void excelSetValue(XSSFSheet sheet, int rowNum, int cellNum, Long value){
        if (sheet == null) {
            logger.info("获取sheet失败");
            return;
        }
        if ((rowNum < 0) || (cellNum < 0)) {
            logger.info("行列值有误");
            return;
        }
        XSSFRow row = sheet.getRow(rowNum-1);
        XSSFCell cell = row.getCell(cellNum-1);
        if ((value != null) && (!"".equals(value))) {
            cell.setCellValue(value);
        }else{
            Long defaultValue = 0L;
            cell.setCellValue(defaultValue);
        }

    }

    public void excelSetStringValue(XSSFSheet sheet, int rowNum, int cellNum, String value){
        if (sheet == null) {
            logger.info("获取sheet失败");
            return;
        }
        if ((rowNum < 0) || (cellNum < 0)) {
            logger.info("行列值有误");
            return;
        }
        XSSFRow row = sheet.getRow(rowNum-1);
        XSSFCell cell = row.getCell(cellNum-1);
        if ((value != null) && (!"".equals(value))) {
            cell.setCellValue(value);//文本格式
        }else{
            cell.setCellValue("");
        }

    }

/*    public void upload(String title, HttpServletResponse response, XSSFWorkbook work, OutputStream output)
            throws IOException{
        output = response.getOutputStream();
        response.reset();
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/x-msdownload");
        String downloadFileName = title + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(downloadFileName.getBytes("GBK"), "ISO8859_1") + "\"");
        work.write(output);
        output.flush();
    }*/


    private Map<String, String> getAreaLevel(String regionCode){
        //制发单位地域
        String createProvince = "";
        String createCity = "";
        String createCounty = "";
        List<Map<String,Object>> createRegions = eliminateLetterThoReChgService.searchRegion(regionCode);
        for(Iterator<Map<String, Object>> iterator = createRegions.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            if(next.get("PROVINCE_") != null) {
                createProvince = (String)next.get("PROVINCE_");
            }
            if(next.get("CITY_") != null) {
                createCity = (String)next.get("CITY_");
            }
            if(next.get("COUNTY_") != null) {
                createCounty = (String)next.get("COUNTY_");
            }
        }
        Map<String, String> areaMap = new HashMap<>();
        areaMap.put(PROVINCE_LEVEL,createProvince);
        areaMap.put(CITY_LEVEL,createCity);
        areaMap.put(COUNTY_LEVEL,createCounty);
        return areaMap ;
    }


    //详情页使用的字典转换
    private void changeCodeToName(EliminateLetterTho bo,String regionCode){
        //文书类型
        if(StringUtils.isNotEmpty(bo.getLetterType())){
            bo.setLetterTypeCN(baseDictionaryService.changeCodeToName(ConstantValue.ELIMINATE_LETTER_THO_LETTER_TYPE,bo.getLetterType(),regionCode));
        }
        //整改情况
        if(StringUtils.isNotEmpty(bo.getReChgs().getChgType())){
            bo.getReChgs().setChgTypeCN(baseDictionaryService.changeCodeToName(ConstantValue.ELIMINATE_LETTER_CHG_TYPE,bo.getReChgs().getChgType(),regionCode));
        }
        //行业领域
        //字典转义
        Map<String, BaseDataDict> indusMap = dictMap(ConstantValue.ELIMINATE_LETTER_THO_INDUS, regionCode);
        String indusCN = "";
        for (EliminateLetterIndus indusObj : bo.getInduss()) {
            String industrialCode = indusObj.getIndustrialCode();
            if (StringUtils.isNotBlank(industrialCode)) {
                indusCN += indusMap.get(industrialCode).getDictName() + ",";
            }
        }
        indusCN = indusCN.substring(0,indusCN.length()-1);
        //☑ ☐
        String D14 = "☐社会治安 ☐乡村治理 ☐金融扶贫 ☐工程建设 \r\n☐交通运输 ☐市场流通 ☐自然环保 ☐信息网络 \r\n☐文化旅游 ☐教育卫生 ☐其他";
        String test = "☐社会治安";
        test = test.replace("☐社会治安","☑社会治安");
        for(int i=0;i<indusCN.split(",").length;i++){
            String temp = indusCN.split(",")[i];
            if(D14.indexOf(temp)!=-1){
                D14 = D14.replace(UN_CHECK+temp,CHECK+temp);
            }
        }
        bo.setIndusCodeArr(D14);
    }

    //获取字典编码
    private Map<String, BaseDataDict> dictMap (String dictCode, String orgCode){
        List<BaseDataDict> dicts = baseDictionaryService.getDataDictListOfSinglestage(dictCode,orgCode);
        Map<String, BaseDataDict> dictMap = new HashMap<>();
        for (BaseDataDict baseDataDict : dicts) {
            dictMap.put(baseDataDict.getDictGeneralCode(), baseDataDict);
        }
        return dictMap;
    }

    //压缩文件
    public void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载zip文件
     * @param file
     * @param response
     */
    public void downloadZip(File file,String fileName,HttpServletResponse response) throws Exception{
        //将excel文件生成压缩文件
/*        File srcfile[] = new File[fileNames.size()];
        for (int j = 0, n1 = fileNames.size(); j < n1; j++) {
            srcfile[j] = new File(fileNames.get(j));
        }
        ZipFiles(srcfile, file);*/
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));//zip.getName()
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = new FileInputStream(file.getPath());
        byte[] buffer = new byte[1024];
        int i = -1;
        while ((i = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, i);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        outputStream = null;
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }
}
