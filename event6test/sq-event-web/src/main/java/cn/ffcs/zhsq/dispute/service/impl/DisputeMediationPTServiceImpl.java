package cn.ffcs.zhsq.dispute.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationPTService;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationPT;
import cn.ffcs.zhsq.mybatis.persistence.dispute.DisputeMediationPTMapper;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value = "disputeMediationPTServiceImpl")
public class DisputeMediationPTServiceImpl extends ApplicationObjectSupport implements IDisputeMediationPTService {
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private DisputeMediationPTMapper disputeMediationPTMapper;
	@Autowired
	private IDisputeMediationService disputeMediationService;
	@Autowired
	private IFunConfigurationService configurationService;

	DecimalFormat decimalFormat=new DecimalFormat("####");
	
	@Override
	public List<DisputeMediationPT> findDisputeMediationPT(Map<String, Object> param) {
		String biDbName = configurationService.changeCodeToValue("DB_USER_NAME","BI_DB_NAME",IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(biDbName))
			param.put("BI_DB_NAME", biDbName + ".");
		List<DisputeMediationPT> disputes = disputeMediationPTMapper.findDisputeMediationPT(param);
		for(int i = 0;i<disputes.size();i++){
			DisputeMediationPT dispute = disputes.get(i);
			float allNumIndex = getNumIndex((float)dispute.getAllNum(),(float)dispute.getAvgNumLevel());
			float mediationRateIndex = getNumIndex(dispute.getMediationRate(), dispute.getAvgMediationRate());
			dispute.setAllNumIndex(allNumIndex);
			dispute.setMediationRateIndex(mediationRateIndex);
			int riskNum = getRiskNum(allNumIndex) + getRiskNum2(mediationRateIndex);
			dispute.setRiskNum(riskNum);
			dispute.setAllNumRateStr(getRate(allNumIndex, param.get("level").toString(), param.get("infoOrgCode").toString()));
			dispute.setMediationNumRateStr(getRate(mediationRateIndex, param.get("level").toString(), param.get("infoOrgCode").toString()));
			dispute.setAllNumAdvStr(getAllNumAdv(allNumIndex));
			dispute.setMediationNumAdvStr(getMediationNumAdv(mediationRateIndex));
			//?????????0???????????????100%
			if(dispute.getAllNum() == 0)
				dispute.setMediationRate(1f);
			dispute.setMediationRateStr(decimalFormat.format(dispute.getMediationRate() * 100));
//			System.out.println("RegionCode="+dispute.getRegionCode()+" allNumIndex="+allNumIndex + " mediationRateIndex=" + mediationRateIndex+" riskNum="+riskNum);
		}
		Collections.sort(disputes, new Comparator<DisputeMediationPT>() {
            public int compare(DisputeMediationPT arg0, DisputeMediationPT arg1) {
                return arg1.getRiskNum() - arg0.getRiskNum();
            }
        });
		return disputes;
	}
	
	/**
	 * ?????????
	 * @param numIndex
	 * @return
	 */
	private String getMediationNumAdv(float numIndex){
		String str = "";
		if(numIndex > 0.1)
			str = "????????????????????????";
		else if(numIndex < 0.1 && numIndex > -0.1)
			str = "??????????????????";
		else if(numIndex < -0.1)
			str = "????????????????????????";
		return str;
	}
	
	/**
	 * ?????????
	 * @param numIndex
	 * @return
	 */
	private String getAllNumAdv(float numIndex){
		String str = "";
		if(numIndex > 0.1)
			str = "??????????????????";
		else if(numIndex < 0.1 && numIndex > -0.1)
			str = "????????????????????????";
		else if(numIndex < -0.1)
			str = "????????????????????????";
		return str;
	}
	
	private String getRateLabel(String regionLevel, String infoOrgCode){
		String result = "";
		if(infoOrgCode.equals("3510"))//??????
			result = "??????";
		else if(regionLevel.equals("1"))
			result = "??????";
		else if(regionLevel.equals("2"))
			result = "??????";
		else if(regionLevel.equals("3"))
			result = "??????";
		else if(regionLevel.equals("4"))
			result = "?????????";
		else if(regionLevel.equals("5"))
			result = "?????????";
		else if(regionLevel.equals("6"))
			result = "?????????";
		return result;
	}
	
	/**
	 * ???????????????
	 * @param numIndex
	 * @return
	 */
	private String getRate(float numIndex, String regionLevel, String regionCode){
		numIndex = numIndex * 100;
		String rateStr = "";
		if(numIndex > 0)
			rateStr = "???"+getRateLabel(regionLevel, regionCode)+"???????????????" + decimalFormat.format(numIndex) + "%";
		else if(numIndex == 0)
			rateStr = "???"+getRateLabel(regionLevel, regionCode)+"??????????????????";
		else if(numIndex < 0)
			rateStr = "???"+getRateLabel(regionLevel, regionCode)+"???????????????" + decimalFormat.format(Math.abs(numIndex)) + "%";
		return rateStr;
	}
	
	/**
	 * ????????????
	 * @param allNum
	 * @param avgAllNum
	 * @return
	 */
	private float getNumIndex(float allNum, float avgAllNum){
		if(avgAllNum == 0) return 0;
//		if(allNum == 0) allNum =1;
		return (allNum - avgAllNum)/avgAllNum;
	}
	
	/**
	 * ??????????????????
	 * @param r
	 * @return
	 */
	private int getRiskNum(float r){
		int result = 0;
		if(r <= -0.2) result = 0;
		if(r > -0.2 && r <= -0.1) result = 1;
		if(r > -0.1 && r <= 0) result = 2;
		if(r > 0 && r <= 0.1) result = 3;
		if(r > 0.1 && r <= 0.2) result = 4;
		if(r > 0.2) result = 5;
		return result;
	}
	
	private int getRiskNum2(float r){
		int result = 0;
		if(r <= -0.2) result = 5;
		if(r > -0.2 && r <= -0.1) result = 4;
		if(r > -0.1 && r <= 0) result = 3;
		if(r > 0 && r <= 0.1) result = 2;
		if(r > 0.1 && r <= 0.2) result = 1;
		if(r > 0.2) result = 0;
		return result;
	}

	/**
	 * ????????????
	 */
	@Override
	public DisputeMediationPT getWholeInfo(Map<String, Object> param) {
		String biDbName = configurationService.changeCodeToValue("DB_USER_NAME","BI_DB_NAME",IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(biDbName))
			param.put("BI_DB_NAME", biDbName + ".");
		List<DisputeMediationPT> disputes = disputeMediationPTMapper.findDisputeMediationPT(param);
		if(disputes!=null && disputes.size() > 0){
			DisputeMediationPT dispute = disputes.get(0);
			dispute.setPreviousMaxDate(getPreviousMaxDate());
			dispute.setPreviousDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) -1);
			dispute.setThismonth2LastdayMsg(getThismonth2LastdayMsg(dispute.getThismonth2Lastday(), dispute.getLastmonthInterval()));
			dispute.setRiskNum(getRiskIndex(dispute));
			Map<String, Object> params = new HashMap<String, Object>();
			System.out.println("param.get(\"gridId\")----------"+param.get("gridId"));
			params.put("gridId", param.get("gridId"));
			params.put("startHappenTime", DateUtils.getMonthFirstDay());
			params.put("endHappenTime", DateUtils.getToday());
			System.out.println(params);
			Map<String, Object> result = null;
//			Map<String, Object> result = disputeMediationService.findMaxDisputeType(params);
			if(result!=null){
				result.put("DISPUTE_TYPE_STR", format(result.get("DISPUTE_TYPE").toString(),param.get("regionCode").toString()));
			}
			dispute.setMonthMediationType(result);
			return dispute;
		}
		return null;
	}
	
	private String format(String disputeType2, String infoOrgCode){
		if(StringUtils.isBlank(disputeType2)) return "";
		String disputeType2Str = "";
		List<BaseDataDict> disputeType= dictionaryService.getDataDictListOfSinglestage("B415", infoOrgCode);
        for(BaseDataDict baseDataDict : disputeType){
        	if(disputeType2.substring(0, 2).equals(baseDataDict.getDictGeneralCode())){
        		disputeType2Str = baseDataDict.getDictName();
        	}
        }
		return disputeType2Str;
	}
	
	private int getRiskIndex(DisputeMediationPT dispute){
		int num1 = 0;
		int num2 = 0;
		int num3 = 0;
		float mediationRate = dispute.getMediationRate();
		if(mediationRate >= 0.95)
			num1 = 0;
		else if(mediationRate >= 0.85 && mediationRate < 0.95)
			num1 = 1;
		else if(mediationRate >= 0.75 && mediationRate < 0.85)
			num1 = 2;
		else if(mediationRate >= 0.65 && mediationRate < 0.75)
			num1 = 3;
		else if(mediationRate >= 0.55 && mediationRate < 0.65)
			num1 = 4;
		else if(mediationRate < 0.55)
			num1 = 5;
		int lastmonthInterval = dispute.getLastmonthInterval();
		int thismonth2Lastday = dispute.getThismonth2Lastday();
		int monthDeff = thismonth2Lastday - lastmonthInterval;
		if(monthDeff <= 0)
			num2 = 0;
		else if(monthDeff <= 10 && monthDeff > 0)
			num2 = 1;
		else if(monthDeff <= 30 && monthDeff > 10)
			num2 = 2;
		else if(monthDeff > 30)
			num2 = 3;
		int thisyear2Lastday = dispute.getThisyear2Lastday();
		int lastyearInterval = dispute.getLastyearInterval();
		float yearDeff = ((float)thisyear2Lastday - (float)lastyearInterval)/(float)lastyearInterval;
		if(yearDeff <= 0)
			num3 = 0;
		else if(yearDeff <= 0.1 && yearDeff > 0)
			num3 = 1;
		else if(yearDeff > 0.1)
			num3 = 2;
		return num1 + num2 + num3;
	}
	
	private String getThismonth2LastdayMsg(int thismonth2Lastday, int lastmonthInterval){
		String result = "";
		int monthDispute = thismonth2Lastday - lastmonthInterval;
		if(monthDispute < 0)
			result = "??????????????????<span class=\"szadd\">??????"+Math.abs(monthDispute)+"???</span>??????????????????";
		else if(monthDispute > 0)
			result = "??????????????????<span class=\"szadd\">??????"+monthDispute+"???</span>??????????????????";
		else if(monthDispute == 0)
			result = "?????????????????????";
		return result;
	}
	
	/**
	 * ??????
	 * @return
	 */
	private String getPreviousMaxDate(){
		return DateUtils.getYesterday("yyyy???MM???dd???");
	}
}
