package cn.ffcs.zhsq.dispute.service.impl;

import cn.ffcs.shequ.web.CommonFunctions;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationDataService;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationDataMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author os.wangh
 */
@Service(value = "disputeMediationDataServiceImpl")
public class DisputeMediationDataServiceImpl implements IDisputeMediationDataService {
    @Autowired
    private DisputeMediationDataMapper disputeMediationDataMapper;
    @Autowired
    private IBaseDictionaryService dictionaryService;
    /**
     * 获取突出矛盾纠纷数据
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getOutStandingNum(Map<String,Object> params) {
        String monthYear = params.get("monthYear").toString();
        Map<String, Object> outStandingNum = disputeMediationDataMapper.getOutStandingNum(params);
        outStandingNum.put("momTotalRatio","100");
        outStandingNum.put("yoyTotalRatio","100");
        outStandingNum.put("momGroupRatio","100");
        outStandingNum.put("yoyGroupRatio","100");
        Date date = null;
        try {
            date = DateUtils.convertStringToDate(monthYear,"yyyyMM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = DateUtils.addInterval(date, -1, "02");
        String lastMonth = DateUtils.formatDate(date1, "yyyyMM");
        String totalNum = outStandingNum.get("TOTAL_NUM").toString();
        String groupNum = outStandingNum.get("GROUP_NUM").toString();
        params.put("monthYear",lastMonth);
        Map<String, Object> lastOutStandingNum = disputeMediationDataMapper.getOutStandingNum(params);
        String lastTotalNum = lastOutStandingNum.get("TOTAL_NUM").toString();
        String lastGroupNum = lastOutStandingNum.get("GROUP_NUM").toString();
        Date date2 = DateUtils.addInterval(date, -1, "01");
        String lastYearMonth = DateUtils.formatDate(date2, "yyyyMM");
        params.put("monthYear",lastYearMonth);
        Map<String, Object> lastYearOutStandingNum = disputeMediationDataMapper.getOutStandingNum(params);
        String lastYearTotalNum = lastYearOutStandingNum.get("TOTAL_NUM").toString();
        String lastYearGroupNum = lastYearOutStandingNum.get("GROUP_NUM").toString();
        //（本期数－上期数）÷上期数×100%
        if(Double.parseDouble(lastTotalNum) != 0){
            String accuracy = accuracy(Double.parseDouble(totalNum) - Double.parseDouble(lastTotalNum), Double.parseDouble(lastTotalNum), 2);
            outStandingNum.put("momRatio",accuracy.replaceAll(",",""));
        }
        if(Double.parseDouble(lastYearTotalNum) != 0){
            String accuracy = accuracy(Double.parseDouble(totalNum) - Double.parseDouble(lastYearTotalNum), Double.parseDouble(lastYearTotalNum), 2);
            outStandingNum.put("yoyRatio",accuracy.replaceAll(",",""));
        }

        //（本期数－同期数）÷同期数×100%
        if(Double.parseDouble(lastGroupNum) != 0){
            String accuracy = accuracy(Double.parseDouble(groupNum) - Double.parseDouble(lastGroupNum), Double.parseDouble(lastGroupNum), 2);
            outStandingNum.put("momGroupRatio",accuracy.replaceAll(",",""));
        }
        if(Double.parseDouble(lastYearGroupNum) != 0){
            String accuracy = accuracy(Double.parseDouble(groupNum) - Double.parseDouble(lastYearGroupNum), Double.parseDouble(lastYearGroupNum), 2);
            outStandingNum.put("yoyGroupRatio",accuracy.replaceAll(",",""));
        }
        return outStandingNum;
    }

    /**
     * 获取突出矛盾纠纷 案件主要分布
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getOutStandingDistribution(Map<String,Object> params) {
        String orgCode = params.get("orgCode").toString();
        String gridLevel = "2";
        if(orgCode.length() == 2){
            gridLevel = "2";
        }else if(orgCode.length() == 4){
            gridLevel = "3";
        }else if(orgCode.length() == 6){
            gridLevel = "4";
        }else if(orgCode.length() == 9){
            gridLevel = "5";
        }else if(orgCode.length() == 12){
            gridLevel = "6";
        }else if(orgCode.length() == 15){
            gridLevel = "6";
        }
        params.put("gridLevel",gridLevel);
        return disputeMediationDataMapper.getOutStandingDistribution(params);
    }

    /**
     * 获取本级的下一级的化解率TOP5
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getDataForResolutionRateNext(Map<String, Object> params) {
        String orgCode = params.get("orgCode").toString();
        String gridLevel = "2";
        if(orgCode.length() == 2){
            gridLevel = "2";
        }else if(orgCode.length() == 4){
            gridLevel = "3";
        }else if(orgCode.length() == 6){
            gridLevel = "4";
        }else if(orgCode.length() == 9){
            gridLevel = "5";
        }else if(orgCode.length() == 12){
            gridLevel = "6";
        }else if(orgCode.length() == 15){
            gridLevel = "6";
        }
        int orgCodeLength = 0;
        if(orgCode.length()>5) {
            orgCodeLength = orgCode.length() + 3;
        }else {
            orgCodeLength = orgCode.length() + 2;
        }
        params.put("gridLevel",gridLevel);
        params.put("orgCodeLength",orgCodeLength);
        List<Map<String,Object>> dataForResolutionRateNext = disputeMediationDataMapper.getDataForResolutionRateNext(params);
        dataForResolutionRateNext.forEach(in->{
            Double total = Double.parseDouble(in.get("TOTAL").toString());
            Double endTotal = Double.parseDouble(in.get("END_TOTAL").toString());
            if(total == 0){
                in.put("rate","0");
            }else if(total != 0 && endTotal != 0){
                String accuracy = accuracy(endTotal, total, 2);
                in.put("rate",accuracy.replaceAll(",",""));
            }else if(total != 0 && endTotal == 0){
                in.put("rate","0");
            }
        });
        if(dataForResolutionRateNext != null && dataForResolutionRateNext.size() > 1){
            Collections.sort(dataForResolutionRateNext, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Double rate1 = Double.valueOf(o1.get("rate").toString());
                    Double rate2 = Double.valueOf(o2.get("rate").toString());
                    return rate2.compareTo(rate1);
                }
            });
        }
        return dataForResolutionRateNext;
    }

    /**
     * 获取矛盾纠纷数量 矛盾纠纷总数 调解成功数 调解中数 调解率
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getNowMonthNumAndNowYearNum(Map<String, Object> params) {
        params.put("statDateType", "M");
        String nowMonthMediTotal = "0";
        Map<String, Object> nowMonthNum = disputeMediationDataMapper.getConfictMediationNum(params);
        Map<String, Object> par = new HashMap<>();
        String statDate = params.get("monthYear").toString();
        if(nowMonthNum != null){
            nowMonthMediTotal = nowMonthNum.get("MEDI_TOTAL").toString();
            nowMonthNum.put("LINK_MOM_RATE","100%");
            nowMonthNum.put("LINK_YOY_RATE","100%");
        }else{
            nowMonthNum = new HashMap<>();
            nowMonthNum.put("STAT_DATE",statDate);
        }
        Date date = null;
        try {
            date = DateUtils.convertStringToDate(statDate,"yyyyMM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取上个月的时间
        Date date1 = DateUtils.addInterval(date, -1, "02");
        String lastMonth = DateUtils.formatDate(date1, "yyyyMM");
        params.put("monthYear",lastMonth);
        Map<String, Object> lastMonthNum = disputeMediationDataMapper.getConfictMediationNum(params);
        if(lastMonthNum != null){
            String lastMonthMediTotal = lastMonthNum.get("MEDI_TOTAL").toString();
            //（本期数－上期数）÷上期数×100%
            if(Double.parseDouble(lastMonthMediTotal) != 0){
                String accuracy = accuracy(Double.parseDouble(nowMonthMediTotal) - Double.parseDouble(lastMonthMediTotal), Double.parseDouble(lastMonthMediTotal), 2);
                nowMonthNum.put("LINK_MOM_RATE",accuracy.replaceAll(",","")+"%");
            }
        }
        //获取这一年的时间
        String nowYear = DateUtils.formatDate(date, "YYYY");
        params.put("monthYear",nowYear);
        params.put("statDateType", "Y");
        Map<String, Object> nowYearNum = disputeMediationDataMapper.getConfictMediationNum(params);
        if(nowYearNum == null){
            nowYearNum = new HashMap<>();
            nowYearNum.put("STAT_DATE",nowYear);
        }
        //获取上一年这个月的时间
        Date date2 = DateUtils.addInterval(date, -1, "01");
        String lastYearMonth = DateUtils.formatDate(date2, "yyyyMM");
        params.put("monthYear",lastYearMonth);
        Map<String, Object> lastYearNum = disputeMediationDataMapper.getConfictMediationNum(params);
        if(lastYearNum != null){
            String lastYearMediTotal = lastYearNum.get("MEDI_TOTAL").toString();
            //（本期数－上期数）÷上期数×100%
            if(Double.parseDouble(lastYearMediTotal) != 0){
                String accuracy = accuracy(Double.parseDouble(nowMonthMediTotal) - Double.parseDouble(lastYearMediTotal), Double.parseDouble(lastYearMediTotal), 2);
                nowMonthNum.put("LINK_YOY_RATE",accuracy.replaceAll(",","")+"%");
            }
        }
        par.put("nowMonthData",nowMonthNum);
        par.put("nowYearData",nowYearNum);
        return par;
    }

    /**
     * 矛盾纠纷 最近2年数量
     *
     * @param params@return
     */
    @Override
    public Map<String, Object> findTwoYearByMap(Map<String, Object> params) {
        List<String> timeList = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if(!params.containsKey("nowYear")){
            params.put("nowYear",year);
            params.put("lastYear",year-1);
        }else{
            year = Integer.valueOf(params.get("nowYear").toString());
            params.put("lastYear",Integer.valueOf(year)-1);
        }
        for (int i=1;i<=12;i++) {
            timeList.add(year+""+(i<10?"0"+i:i));
        }
        year --;
        for (int i=1;i<=12;i++) {
            timeList.add(year+""+(i<10?"0"+i:i));
        }
        params.put("timeList",timeList);
        params.put("statDateType","M");
        List<Map<String, Object>> twoYearByMap = disputeMediationDataMapper.findTwoYearByMap(params);
        Map<String, Object> result = new HashMap<>();
        result.put("twoYear",twoYearByMap);
        return result;
    }

    /**
     * 获取矛盾纠纷类型
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getConfictTopListByOrgCode(Map<String, Object> params) {
        return disputeMediationDataMapper.getConfictTopListByOrgCode(params);
    }

    /**
     * 获取矛盾纠纷数量
     *
     * @param params
     * @return TOTAL 总数， END_TOTAL 处置数，DEAL_TOTAL 处置中，处置率 DEAL_RATE
     */
    @Override
    public Map<String, Object> getDisputeNum(Map<String, Object> params) {
        Map<String, Object> disputeNum = disputeMediationDataMapper.getDisputeNum(params);
        Double endTotal = Double.valueOf(disputeNum.get("END_TOTAL").toString());
        Double total = Double.valueOf(disputeNum.get("TOTAL").toString());
        disputeNum.put("DEAL_RATE","0");
        if(total > 0){
            String accuracy = accuracy(endTotal, total, 2);
            disputeNum.put("DEAL_RATE",accuracy);
        }
        return disputeNum;
    }

    /**
     * 获取同比环比
     * @param params orgCode 必传 不传直接报错
     * @return twoYear 同比环比 ； nowYear 办理趋势
     */
    @Override
    public Map<String, Object> getTwoYearMoM(Map<String, Object> params) {
        //最近俩年同环比
        int year = Calendar.getInstance().get(Calendar.YEAR);
        params.put("lastYear", (year - 1));
        params.put("nowYear", year);
        List<Map<String, Object>> twoYearMoM = disputeMediationDataMapper.getTwoYearMoM(params);
        params.put("twoYear",twoYearMoM);
        params.put("lastYear", null);
        List<Map<String, Object>> nowYear = disputeMediationDataMapper.getTwoYearMoM(params);
        params.put("nowYear",nowYear);
        return params;
    }

    /**
     * 获取矛盾纠纷类型
     * @param params
     * @return DICT_NAME 字典名称， TOTAL_ 对应字典数量
     */
    @Override
    public List<Map<String, Object>> getDisputeTypeNum(Map<String, Object> params) {
        List<BaseDataDict> baseDataDicts = dictionaryService.getDataDictListOfSinglestage("DM01588", (String)params.get("orgCode"));
        List<Map<String, Object>> listData = disputeMediationDataMapper.getDisputeTypeNum(params);
        for (Map<String, Object> data : listData) {
            for (BaseDataDict baseDataDict : baseDataDicts) {
                if (baseDataDict.getDictGeneralCode().equals(data.get("TYPE_"))) {
                    data.put("DICT_NAME", baseDataDict.getDictName());
                    break;
                }
            }
        }
        return listData;
    }

    /**
     * 获取事件分布排行
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getDisputeDistribution(Map<String, Object> params) {
        String disputeType = "";
        if(CommonFunctions.isNotBlank(params,"disputeType")){
            disputeType = params.get("disputeType").toString();
        }
        List<Map<String, Object>> gridInfoByInfoOrgCode = disputeMediationDataMapper.getGridInfoByInfoOrgCode(params);
        List<Map<String, Object>> nextGridInfoList = null;
        if(gridInfoByInfoOrgCode !=null && gridInfoByInfoOrgCode.size()>0){
            Map<String, Object> temp = new HashMap<>();
            temp.put("parentGridId",gridInfoByInfoOrgCode.get(0).get("GRID_ID"));
            nextGridInfoList = disputeMediationDataMapper.getGridInfoByInfoOrgCode(temp);
            String finalDisputeType = disputeType;
            nextGridInfoList.forEach(in->{
                String infoOrgCode = in.get("INFO_ORG_CODE").toString();
                Map<String, Object> inTemp = new HashMap<>();
                inTemp.put("orgCode",infoOrgCode);
                inTemp.put("disputeType", finalDisputeType);
                Map<String, Object> disputeNum = this.getDisputeNum(inTemp);
                in.putAll(disputeNum);
            });
            Collections.sort(nextGridInfoList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Double rate1 = Double.valueOf(o1.get("TOTAL").toString());
                    Double rate2 = Double.valueOf(o2.get("TOTAL").toString());
                    return rate2.compareTo(rate1);
                }
            });
        }
        return nextGridInfoList;
    }

    /**
     * 获取各地区纠纷类型TOP1
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getRegionsTypeTop1(Map<String, Object> params) {
        List<Map<String, Object>> gridInfoByInfoOrgCode = disputeMediationDataMapper.getGridInfoByInfoOrgCode(params);
        List<Map<String, Object>> nextGridInfoList = null;
        if(gridInfoByInfoOrgCode !=null && gridInfoByInfoOrgCode.size()>0){
            Map<String, Object> temp = new HashMap<>();
            temp.put("parentGridId",gridInfoByInfoOrgCode.get(0).get("GRID_ID"));
            nextGridInfoList = disputeMediationDataMapper.getGridInfoByInfoOrgCode(temp);
            nextGridInfoList.forEach(in->{
                String infoOrgCode = in.get("INFO_ORG_CODE").toString();
                Map<String, Object> inTemp = new HashMap<>();
                inTemp.put("orgCode",infoOrgCode);
                inTemp.put("nowYear",DateUtils.getToday("yyyy"));
                Map<String, Object> disputeNum = disputeMediationDataMapper.getRegionsTypeTop1(inTemp);
                if(disputeNum != null){
                    Double total_ = Double.valueOf(disputeNum.get("TOTAL_").toString());
                    Double end_total = Double.valueOf(disputeNum.get("END_TOTAL").toString());
                    String type_ = disputeNum.get("TYPE_").toString();
                    String dictName = dictionaryService.changeCodeToName("DM01588", type_, infoOrgCode);
                    disputeNum.put("DICT_NAME",dictName);
                    disputeNum.put("DEAL_RATE","0");
                    if (total_ > 0){
                        String accuracy = accuracy(end_total, total_, 2);
                        disputeNum.put("DEAL_RATE",accuracy);
                    }
                }else{
                    disputeNum = new HashMap<>();
                    disputeNum.put("TYPE_","99");
                    disputeNum.put("DICT_NAME","其他");
                    disputeNum.put("TOTAL_","0");
                    disputeNum.put("END_TOTAL","0");
                    disputeNum.put("DEAL_RATE","0");
                }
                in.putAll(disputeNum);
            });
            Collections.sort(nextGridInfoList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Double rate1 = Double.valueOf(o1.get("DEAL_RATE").toString());
                    Double rate2 = Double.valueOf(o2.get("DEAL_RATE").toString());
                    return rate2.compareTo(rate1);
                }
            });
        }
        return nextGridInfoList;
    }

    /**
     * 列表查询
     *
     * @param params
     * @return
     */
    @Override
    public EUDGPagination searchList(Map<String, Object> params,int page, int rows) {
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Map<String, Object>> list = disputeMediationDataMapper.searchList(params, rowBounds);
        long count = disputeMediationDataMapper.countList(params);
        return new EUDGPagination(count,list);
    }

    /**
     * 计算方法
     * @param num
     * @param total
     * @param scale
     * @return
     */
    public static String accuracy(double num, double total, int scale){
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num);
    }
}
