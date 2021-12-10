package cn.ffcs.zhsq.mybatis.domain.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FaceRecordInfo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1804887936163538303L;
	private String strName;
	private String stSex;
	private Integer nAge;
	private String nGlassOn;
	
	private Integer nSimilarity;
	
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStSex() {
		return stSex;
	}
	public void setStSex(String stSex) {
		this.stSex = stSex;
	}
	public String getnGlassOn() {
		return nGlassOn;
	}
	public void setnGlassOn(String nGlassOn) {
		this.nGlassOn = nGlassOn;
	}
	public Integer getnAge() {
		return nAge;
	}
	public void setnAge(Integer nAge) {
		this.nAge = nAge;
	}
	public Integer getnSimilarity() {
		return nSimilarity;
	}
	public void setnSimilarity(Integer nSimilarity) {
		this.nSimilarity = nSimilarity;
	}
	
	
	
	
	
}
