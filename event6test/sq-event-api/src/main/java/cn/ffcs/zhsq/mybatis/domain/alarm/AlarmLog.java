package cn.ffcs.zhsq.mybatis.domain.alarm;

import cn.ffcs.doorsys.bo.equipment.AfsLog;

public class AlarmLog extends AfsLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8064186933467733702L;
	
	/**
     * 设备名称
     */
    private String eqpName;
    
    /**
     * 抓拍时间
     */
    private String stSnapTimeStr;

	public String getEqpName() {
		return eqpName;
	}

	public void setEqpName(String eqpName) {
		this.eqpName = eqpName;
	}

	public String getStSnapTimeStr() {
		return stSnapTimeStr;
	}

	public void setStSnapTimeStr(String stSnapTimeStr) {
		this.stSnapTimeStr = stSnapTimeStr;
	}
}

