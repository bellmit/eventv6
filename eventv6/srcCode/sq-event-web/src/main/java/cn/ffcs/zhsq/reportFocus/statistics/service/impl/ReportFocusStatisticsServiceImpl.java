package cn.ffcs.zhsq.reportFocus.statistics.service.impl;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.statistics.ReportFocusStatisticsMapper;
import cn.ffcs.zhsq.reportFocus.statistics.IReportFocusStatisticsService;
import cn.ffcs.zhsq.szzg.event.service.IEventAndReportJsonpService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/3/30 16:50
 */
@Service("reportFocusStatisticsService")
public class ReportFocusStatisticsServiceImpl implements IReportFocusStatisticsService {

    @Autowired
    private ReportFocusStatisticsMapper reportFocusStatisticsMapper;
    @Autowired
    private IEventAndReportJsonpService	eventAndReportJsonpService;

   
    @Override
    public List<Map<String, Object>> findListData(Map<String, Object> params) throws Exception {

        String regionCode = null,
                beginTime = null,
                endTime = null;

        List<Map<String,Object>> resultList = new ArrayList<>();

        if (CommonFunctions.isNotBlank(params,"infoOrgCode")) {
            regionCode = params.get("infoOrgCode").toString();
            params.put("regionCode",regionCode);
        } else {
            throw new IllegalArgumentException("参数【infoOrgCode】为空，请检查！");
        }

        //查询时间如果为空，默认查询当天数据
        if (CommonFunctions.isBlank(params,"beginTime")) {
            beginTime = DateUtils.getToday();
            params.put("beginTime",beginTime);
        }

        if (CommonFunctions.isBlank(params,"endTime")) {
            endTime = DateUtils.getToday();
            params.put("endTime",endTime);
        }

        /*
          上报类型，1 两违防治业务；2 房屋安全隐患；3 企业安全隐患；4 疫情防控；5 流域水质；6 三会一课；7 扶贫走访；
		*/
        resultList = reportFocusStatisticsMapper.findListData(params);

        //List<String> reportTypeList = new ArrayList<>();
        List<Map<String, String>> list  = eventAndReportJsonpService.getReportFocusTypeList(null);
        Map<String, String> capMethodMap  =new HashMap<String, String>();
        for (Map<String, String> map : list) {
			capMethodMap.put(map.get("REPORT_TYPE"), map.get("REPORT_TITLE"));
		}
        capMethodMap.remove("6");//删除 三会一课
        if(resultList != null && resultList.size() > 0){
            for(Map<String,Object> map:resultList){
                Float BANJIE = 0f;
                Float TOTAL = 0f;
                if(CommonFunctions.isNotBlank(map,"TOTAL")){
                    TOTAL = Float.valueOf(String.valueOf(map.get("TOTAL")));
                }
                if(CommonFunctions.isNotBlank(map,"BANJIE")){
                    BANJIE = Float.valueOf(String.valueOf(map.get("BANJIE")));
                }
                if(TOTAL > 0){
                    map.put("BANJIELV",String.format("%.2f",(BANJIE*100/TOTAL)) + "%");
                }
                if(CommonFunctions.isNotBlank(map,"REPORT_TYPE")){
//                    reportTypeList.add(String.valueOf(map.get("REPORT_TYPE")));
                    map.put("moduleName",capMethodMap.get(map.get("REPORT_TYPE")));
                    capMethodMap.remove(map.get("REPORT_TYPE"));
                }
            }
        }
        //查找业务模块数据为不存在项 构造为0数据
        Map<String,Object> dataMap = new HashMap<>();
        for(String key:capMethodMap.keySet()){
                dataMap = new HashMap<>();
                dataMap.put("moduleName",capMethodMap.get(key));
                dataMap.put("REPORT_TYPE",key);
                dataMap.put("TOTAL","0");
                dataMap.put("BANJIE","0");
                dataMap.put("CQ_TOTAL","0");
                dataMap.put("ZBCQ","0");
                dataMap.put("BJCQ","0");
                dataMap.put("DUBAN","0");
                dataMap.put("BANJIELV","0.00%");
                resultList.add(dataMap);
        }

        return resultList;
    }

    @Override
    public EUDGPagination findPagination4EpcOverdue(int pageNo, int pageSize, Map<String, Object> params) throws Exception {

        EUDGPagination epcOverduePagination = null;
        params = params == null ? new HashMap<String, Object>() : params;

        int total = 0;
        List<Map<String, Object>> epcOverdueList = null;

        total = reportFocusStatisticsMapper.findCount4EpcOverdue(params);
        if(total > 0) {
            pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 10 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

            epcOverdueList = reportFocusStatisticsMapper.findList4EpcOverdue(params, rowBounds);
            formatDataOut(epcOverdueList, params);
        } else {
            epcOverdueList = new ArrayList<Map<String, Object>>();
        }

        epcOverduePagination = new EUDGPagination(total, epcOverdueList);

        return epcOverduePagination;
    }

    @Override
    public List<Map<String, Object>> findListDataOfEpcOverdue(Map<String, Object> params) throws Exception {
        String infoOrgCode = null,
                beginTime = null,
                endTime = null;

        List<Map<String,Object>> resultMap = new ArrayList<>();

        if (CommonFunctions.isNotBlank(params,"infoOrgCode")) {
            infoOrgCode = params.get("infoOrgCode").toString();
        } else {
            throw new IllegalArgumentException("参数【infoOrgCode】为空，请检查！");
        }

        //查询时间如果为空，默认查询当天数据
        if (CommonFunctions.isBlank(params,"beginTime")) {
            beginTime = DateUtils.getToday();
            params.put("beginTime",beginTime);
        }

        if (CommonFunctions.isBlank(params,"endTime")) {
            endTime = DateUtils.getToday();
            params.put("endTime",endTime);
        }

        resultMap = reportFocusStatisticsMapper.findList4EpcOverdue(params);

        formatDataOut(resultMap, params);

        return resultMap;
    }

    private void formatDataOut(List<Map<String, Object>> epcOverdueList,Map<String, Object> params){
        String HANDLING_TASK = "1",HANDLED_TASK = "2";

        if(null != epcOverdueList && epcOverdueList.size() > 0){
            String taskType = null,interTime = null;
            Long intervalTime = 0L,periodTime = 0L,diffenceTime = 0L;

            for(Map<String,Object> epcMap:epcOverdueList){
                if(CommonFunctions.isNotBlank(epcMap,"taskType")){
                    taskType = String.valueOf(epcMap.get("taskType"));
                }
                //历史超时 超时时长处理
                if(HANDLED_TASK.equals(taskType)){
                    interTime = "历史超期";
                    if(CommonFunctions.isNotBlank(epcMap,"intervalTime")){
                        intervalTime = Long.valueOf(String.valueOf(epcMap.get("intervalTime")));
                    }
                    if(CommonFunctions.isNotBlank(epcMap,"periodTime")){
                        periodTime = Long.valueOf(String.valueOf(epcMap.get("periodTime")));
                    }
                    //diffenceTime 单位：秒
                    if(intervalTime > periodTime){
                        diffenceTime = intervalTime - periodTime;
                    }else{
                        diffenceTime = intervalTime;
                    }
                    interTime = formatTime(String.valueOf(diffenceTime));

                    if(StringUtils.isNotBlank(interTime)){
                        epcMap.put("interTime",interTime);
                    }else{
                        epcMap.put("interTime","历史环节超期");
                    }
                }
            }
        }
    }

    public  String formatTime(String timeTotal){
        if("0".equals(timeTotal)){
            return "";
        }
        Long value = Math.round(Integer.valueOf(timeTotal)*1.0);
        String v = "";
        Long n = value / 3600;//12300
        if( n > 1 ){
            v = n +"小时";
        }
        if(value % 3600 > 0){
            n = (value % 3600) / 60 ;
            if(n > 1){v += n  +"分钟";}

            if(value % 60 > 0){
                v +=   (value % 60)+"秒";
            }
        }
        return v;
    }

}
