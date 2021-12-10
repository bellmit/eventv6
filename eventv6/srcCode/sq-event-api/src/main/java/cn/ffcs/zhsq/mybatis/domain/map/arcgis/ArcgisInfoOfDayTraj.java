package cn.ffcs.zhsq.mybatis.domain.map.arcgis;


import cn.ffcs.shequ.utils.date.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: sulch
 */
public class ArcgisInfoOfDayTraj extends ArcgisInfo {

	private static final long serialVersionUID = 2759762667292525991L;

	private Long gridId;
	private Date statisticDate;
	private String statisticDateStr;
	private String gpsArr;
	private String gpsTimeArr;
	private String inGridArr;
	private List<ArcgisInfoOfTrajectory> subDayTraj = new ArrayList<ArcgisInfoOfTrajectory>();

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public String getGpsArr() {
		return gpsArr;
	}

	public void setGpsArr(String gpsArr) {
		this.gpsArr = gpsArr;
	}

	public String getGpsTimeArr() {
		return gpsTimeArr;
	}

	public void setGpsTimeArr(String gpsTimeArr) {
		this.gpsTimeArr = gpsTimeArr;
	}

	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
		if(statisticDate != null){
			this.statisticDateStr = DateUtils.formatDate(statisticDate, DateUtils.PATTERN_24TIME);
		}
	}

	public String getStatisticDateStr() {
		return statisticDateStr;
	}

	public void setStatisticDateStr(String statisticDateStr) {
		this.statisticDateStr = statisticDateStr;
		if(StringUtils.isNotBlank(statisticDateStr)){
			this.statisticDate = DateUtils.formatDate(statisticDateStr, DateUtils.PATTERN_24TIME);
		}
	}

	public List<ArcgisInfoOfTrajectory> getSubDayTraj() {
		return subDayTraj;
	}

	public void setSubDayTraj(List<ArcgisInfoOfTrajectory> subDayTraj) {
		this.subDayTraj = subDayTraj;
	}

	public String getInGridArr() {
		return inGridArr;
	}

	public void setInGridArr(String inGridArr) {
		this.inGridArr = inGridArr;
	}
}
