package cn.ffcs.zhsq.mybatis.domain.dispute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class DisputeMediationPT implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5568115795203426439L;

	private Date statTime;

    private String statMonth;

    private Long gridId;

    private String regionCode;

    private String regionName;

    private Integer regionLevel;

    private int allNum;

    private int avgNumLevel;

    private float mediationRate;

    private float avgMediationRate;

    private int thismonth2Lastday;

    private int lastmonthInterval;

    private int thisyear2Lastday;

    private int lastyearInterval;
    
    //==
    private int riskNum;//风险评估值
    
    private float allNumIndex;//发生总数差额比例
    
    private float mediationRateIndex;//化解率差额比例
    
    private String allNumRateStr;//总数比较值字符串
    
    private String mediationNumRateStr;//化解率比较值字符串
    
    private String allNumAdvStr;//总数建议字符串
    
    private String mediationNumAdvStr;//化解率建议值字符串
    
    private String previousMaxDate;//上个月最后一天
    
    private String yesterdayDate;//昨天
    
    private Integer previousDate;//上一天

    private String thismonth2LastdayMsg;//与上月比较文字说明
    
    private String mediationRateStr;//化解率百分比文本
    
    private Map<String, Object> monthMediationType;//案件类型最多的

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public String getStatMonth() {
        return statMonth;
    }

    public void setStatMonth(String statMonth) {
        this.statMonth = statMonth == null ? null : statMonth.trim();
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public Integer getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel) {
        this.regionLevel = regionLevel;
    }


	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public int getAvgNumLevel() {
		return avgNumLevel;
	}

	public void setAvgNumLevel(int avgNumLevel) {
		this.avgNumLevel = avgNumLevel;
	}

	public int getRiskNum() {
		return riskNum;
	}

	public void setRiskNum(int riskNum) {
		this.riskNum = riskNum;
	}

	public float getAllNumIndex() {
		return allNumIndex;
	}

	public void setAllNumIndex(float allNumIndex) {
		this.allNumIndex = allNumIndex;
	}

	public float getMediationRate() {
		return mediationRate;
	}

	public void setMediationRate(float mediationRate) {
		this.mediationRate = mediationRate;
	}

	public float getAvgMediationRate() {
		return avgMediationRate;
	}

	public void setAvgMediationRate(float avgMediationRate) {
		this.avgMediationRate = avgMediationRate;
	}

	public float getMediationRateIndex() {
		return mediationRateIndex;
	}

	public void setMediationRateIndex(float mediationRateIndex) {
		this.mediationRateIndex = mediationRateIndex;
	}

	public String getAllNumRateStr() {
		return allNumRateStr;
	}

	public void setAllNumRateStr(String allNumRateStr) {
		this.allNumRateStr = allNumRateStr;
	}

	public String getMediationNumRateStr() {
		return mediationNumRateStr;
	}

	public void setMediationNumRateStr(String mediationNumRateStr) {
		this.mediationNumRateStr = mediationNumRateStr;
	}

	public String getAllNumAdvStr() {
		return allNumAdvStr;
	}

	public void setAllNumAdvStr(String allNumAdvStr) {
		this.allNumAdvStr = allNumAdvStr;
	}

	public String getMediationNumAdvStr() {
		return mediationNumAdvStr;
	}

	public void setMediationNumAdvStr(String mediationNumAdvStr) {
		this.mediationNumAdvStr = mediationNumAdvStr;
	}

	public String getPreviousMaxDate() {
		return previousMaxDate;
	}

	public void setPreviousMaxDate(String previousMaxDate) {
		this.previousMaxDate = previousMaxDate;
	}

	public Integer getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(Integer previousDate) {
		this.previousDate = previousDate;
	}

	public int getThismonth2Lastday() {
		return thismonth2Lastday;
	}

	public void setThismonth2Lastday(int thismonth2Lastday) {
		this.thismonth2Lastday = thismonth2Lastday;
	}

	public int getLastmonthInterval() {
		return lastmonthInterval;
	}

	public void setLastmonthInterval(int lastmonthInterval) {
		this.lastmonthInterval = lastmonthInterval;
	}

	public int getThisyear2Lastday() {
		return thisyear2Lastday;
	}

	public void setThisyear2Lastday(int thisyear2Lastday) {
		this.thisyear2Lastday = thisyear2Lastday;
	}

	public int getLastyearInterval() {
		return lastyearInterval;
	}

	public void setLastyearInterval(int lastyearInterval) {
		this.lastyearInterval = lastyearInterval;
	}

	public String getThismonth2LastdayMsg() {
		return thismonth2LastdayMsg;
	}

	public void setThismonth2LastdayMsg(String thismonth2LastdayMsg) {
		this.thismonth2LastdayMsg = thismonth2LastdayMsg;
	}

	public String getMediationRateStr() {
		return mediationRateStr;
	}

	public void setMediationRateStr(String mediationRateStr) {
		this.mediationRateStr = mediationRateStr;
	}

	public Map<String, Object> getMonthMediationType() {
		return monthMediationType;
	}

	public void setMonthMediationType(Map<String, Object> monthMediationType) {
		this.monthMediationType = monthMediationType;
	}

	public String getYesterdayDate() {
		return yesterdayDate;
	}

	public void setYesterdayDate(String yesterdayDate) {
		this.yesterdayDate = yesterdayDate;
	}
}