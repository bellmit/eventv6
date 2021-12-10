package cn.ffcs.zhsq.map.pingtanHsDataImport;

import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平潭轮廓数据导入
 *
 * @Author sulch
 * @Date 2016-10-12 9:22
 */
@Controller
@RequestMapping(value = "/zhsq/map/dataImportController")
public class DataImportController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/toIndex")
    public String toIndex(HttpSession session,
                          ModelMap map){

        return "/map/pingtanHsDataImport/pingtanHsDataImport_index.ftl";
    }

    @RequestMapping(value = "/importData")
    public String importData(HttpSession session,
                             @RequestParam(value = "importFile") MultipartFile importFile,
                             ModelMap map) {
        StringBuffer importData = new StringBuffer("");
        String readLine = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(importFile.getInputStream()));
            readLine = reader.readLine();

            while (readLine != null) { // 如果 line 为空说明读完了
                importData.append(readLine); // 读取第一行
                readLine = reader.readLine(); // 读取下一行
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] result = saveMapJzwGis(importData.toString());

        map.addAttribute("count", result.length);

        return this.toIndex(session, map);
    }

    /**
     * 保存信息
     * @param data
     * @return
     */
    private int[] saveMapJzwGis(String data) {
        int[] result = new int[]{};

        if(StringUtils.isNotBlank(data)) {
            data = data.trim();
            if(StringUtils.isNotBlank(data)) {
                List<Map<String, Object>> dataMapList = data2MapList(data);
                List<Object[]> objectList = new ArrayList<Object[]>();

                for(Map<String, Object> dataMap : dataMapList) {
                    String customid = "";//wid
                    String _id = "";

                    Double ringXave = 0D, //X
                            ringYave = 0D;	//Y
                    StringBuffer ringBuffer = new StringBuffer("");//HS



                    if(CommonFunctions.isNotBlank(dataMap, "_id")) {
                        _id = dataMap.get("_id").toString();
                    }
                    if(CommonFunctions.isNotBlank(dataMap, "customid")) {
                        customid = dataMap.get("customid").toString();
                    }
                    if(CommonFunctions.isNotBlank(dataMap, "coordinates")) {
                        List<Double> ringX = new ArrayList<Double>(), ringY = new ArrayList<Double>();
                        String ringStr = dataMap.get("coordinates").toString();

                        ringStr = ringStr.replaceAll("\\[", "").replaceAll("\\]", "");
                        String[] ringArray = ringStr.split("&");

                        for(int index = 0, len = ringArray.length; index < len; index++) {
                            String ring = ringArray[index];
                            Double ringD = null;

                            if(StringUtils.isNotBlank(ring)) {
                                try {
                                    ringD = Double.valueOf(ring);
                                } catch(NumberFormatException e) {
                                    ringD = null;
                                }
                            }

                            if(ringD != null) {
                                //计算中心点
//                                if(index % 2 == 0) {//ringX
//                                    ringX.add(ringD);
//                                } else {//ringY
//                                    ringY.add(ringD);
//                                }

                                ringBuffer.append(",").append(ringD);
                            }
                        }

                        if(ringX.size() > 0) {
                            ringXave = ringAverage(ringX);
                        }
                        if(ringY.size() > 0) {
                            ringYave = ringAverage(ringY);
                        }
                        if(ringBuffer.length() > 0) {
                            ringBuffer = new StringBuffer(ringBuffer.substring(1));//去除起始的,
                        }
                    }
                    if(StringUtils.isNotBlank(customid) && !"0".equals(customid)){
                        objectList.add(new Object[]{ringBuffer, customid});
                    }else {
                        System.out.println("_id="+_id+",不更新，因为customid为"+customid+".");
                    }
                }
                result = saveDataItem(objectList);
            }
        }

        return result;
    }

    /**
     * 将数据转换为map
     * @param data
     * @return
     */
    private List<Map<String, Object>> data2MapList(String data) {
        List<String> leftBrace = new ArrayList<String>(),//大括号
                middleBranket = new ArrayList<String>();//中括号

        StringBuffer itemData = new StringBuffer("");
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();

        if(StringUtils.isNotBlank(data)) {
            for(int index = 0, len = data.length(); index < len; index++) {//去除一对大括号内的数据
                char c = data.charAt(index);

                switch(c) {
                    case '{' :
                        leftBrace.add(c + ""); break;
                    case '}':
                        leftBrace.remove(leftBrace.size() - 1); break;
                    case '[':
                        middleBranket.add(c + ""); break;
                    case ']':
                        middleBranket.remove(middleBranket.size() - 1); break;
                }

                if(middleBranket.size() > 0 && c == ',') {//将rings中的,替换成&，防止后续的数据分割破坏数据完整
                    c = '&';
                }

                if(leftBrace.size() > 0) {
                    itemData.append(c);
                } else if(itemData.length() > 0) {
                    String itemDataStr = itemData.substring(1);
                    Map<String, Object> itemCellMap = data2Map(itemDataStr);

                    if(itemCellMap.keySet().size() > 0) {
                        dataMap.add(itemCellMap);
                        itemData = new StringBuffer("");//清空，防止影响下一次操作
                        itemCellMap = new HashMap<String, Object>();//清空，防止影响下一次操作
                    }
                }

            }
        }

        return dataMap;
    }

    /**
     * 将每一个独立的模块转换为map
     * @param data
     * @return
     */
    private Map<String, Object> data2Map(String data) {
        Map<String, Object> dataMap = new HashMap<String, Object>();

        if(StringUtils.isNotBlank(data)) {
            data = data.replaceAll("\\{", ",").replaceAll("\\}", "");//去除大括号内的数据，并排除大括号的影响
            data = data.replaceAll("\"", "");//去除引号
            String[] itemArray = data.split(",");
            String[] itemCellArray = new String[]{};

            for(String item : itemArray) {
                if(StringUtils.isNotBlank(item)) {
                    itemCellArray = item.split(":");
                    if(itemCellArray.length == 2 && StringUtils.isNotBlank(itemCellArray[1])) {
                        dataMap.put(itemCellArray[0].trim(), itemCellArray[1].trim());
                    }
                }
            }
        }

        return dataMap;
    }

    /**
     * 求取平均值
     * @param ringD
     * @return
     */
    private Double ringAverage(List<Double> ringD) {
        Double average = 0D;

        if(ringD != null) {
            for(Double ring : ringD) {
                average += ring;
            }

            average = average / ringD.size();
        }

        return average;
    }

    /**
     * 向数据库中新增数据
     * @param objectList
     * @return
     */
    private int[] saveDataItem(List<Object[]> objectList) {

        StringBuffer sqlBuffer = new StringBuffer("");

        sqlBuffer.append("UPDATE MAP_WG_JZW_GIS ")
                    .append("SET hs = ? ")
                    .append("WHERE WID = ? AND MAPT = 5 ");

        int result[] = jdbcTemplate.batchUpdate(sqlBuffer.toString(), objectList);
        System.out.println("更新的数据："+result.length);

        return result;
    }
}
