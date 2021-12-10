package cn.ffcs.zhsq.timeApplication.service.impl;

import cn.ffcs.zhsq.mybatis.persistence.timeApplication.TimeApplicationReportForDelEventMapper;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationReportForDelEventService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2018/12/4 18:32
 */
@Service(value = "timeApplicationReportForDelEventService")
public class TimeApplicationReportFordelEventServiceImpl implements ITimeApplicationReportForDelEventService {


    @Autowired
    private TimeApplicationReportForDelEventMapper delEventMapper;

    @Override
    public List<Map<String, Object>> findListDataOfDelEvent(Map<String, Object> params) throws Exception {
        String infoOrgCode = null,
               beginTime = null,
               endTime = null;
        Long   gridId = null;
        List<Map<String,Object>> resultMap = new ArrayList<>();

        if (CommonFunctions.isNotBlank(params,"infoOrgCode")) {
            infoOrgCode = params.get("infoOrgCode").toString();
        } else {
            throw new IllegalArgumentException("参数【infoOrgCode】为空，请检查！");
        }

        if (CommonFunctions.isNotBlank(params,"gridId")) {
            gridId = Long.valueOf(params.get("gridId").toString());
        } else {
            throw new IllegalArgumentException("参数【gridId】为空，请检查！");
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

        resultMap = delEventMapper.findListDataOfDelEvent(params);

        //遍历list，构造删除率、占比率、
        int rootValue = 0;//当前根节点的总删除量
        Float totalNumDeleted = 0f;//当前节点的总删除量
        Float totalNumReported = 0f;//当前节点的总上报量

        if (resultMap.size() > 0) {
            Map<String,Object> rootMap = resultMap.get(0);
            if (CommonFunctions.isNotBlank(rootMap,"TOTAL_NUM_DELTED")) {
                rootValue = Integer.valueOf(rootMap.get("TOTAL_NUM_DELTED").toString());
            }

            for(int i = 0,len = resultMap.size();i<len;i++){
                Map<String,Object> map = resultMap.get(i);
                if (CommonFunctions.isNotBlank(map,"TOTAL_NUM_DELTED")) {
                    totalNumDeleted = Float.valueOf(map.get("TOTAL_NUM_DELTED").toString());
                }
                if (CommonFunctions.isNotBlank(map,"TOTAL_NUM_REPORTED")) {
                        totalNumReported = Float.valueOf(map.get("TOTAL_NUM_REPORTED").toString());
                }
                //删除率：总上报量大于0
                if (totalNumReported > 0) {
                    if (totalNumDeleted > 0) {
                        map.put("PERCENT_DELETED",String.format("%.2f",(totalNumDeleted*100/totalNumReported)) + "%");
                    } else {
                        map.put("PERCENT_DELETED","0%");
                    }
                } else {
                    if (totalNumDeleted > 0) {
                        map.put("PERCENT_DELETED","-");
                    } else {
                        map.put("PERCENT_DELETED","0%");
                    }
                }

                //占比率
                if (0 == i) {//根节点占比率
                    if (0 == rootValue) {
                        map.put("PERCENT_TAKING","0%");
                    } else {
                        map.put("PERCENT_TAKING","100.00%");
                    }
                } else {//非根节点占比率
                    if (rootValue > 0) {
                        if (totalNumDeleted > 0) {
                            map.put("PERCENT_TAKING",String.format("%.2f",(totalNumDeleted*100/rootValue)) + "%");
                        } else {
                            map.put("PERCENT_TAKING","0%");
                        }
                    } else {
                        if (totalNumDeleted > 0) {
                            map.put("PERCENT_TAKING","-");
                        } else {
                            map.put("PERCENT_TAKING","0%");
                        }
                    }
                }
            }


        }

        return resultMap;
    }
}
