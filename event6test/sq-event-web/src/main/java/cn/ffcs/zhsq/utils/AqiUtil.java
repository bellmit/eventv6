package cn.ffcs.zhsq.utils;

import cn.ffcs.shequ.utils.StringUtils;

/**
 * 
 * @author linzhu
 *
 */
public class AqiUtil {
/**
 * 根据aqi值获取对应的状态名称	
 * @param aqiStr
 * @return
 */
 public static String  getStateNameByAqi(String aqiStr){
	 String stateName="优";
	 if(!StringUtils.isEmpty(aqiStr)){
		Integer aqi=Integer.valueOf(aqiStr);
		if(aqi>=0&&aqi<=50){
			stateName="优";
		}else if(aqi>=51&&aqi<=100){
			stateName="良";
		}else if(aqi>=101&&aqi<=150){
			stateName="轻度污染";
		}else if(aqi>=151&&aqi<=200){
			stateName="轻度污染";
		}else if(aqi>=201&&aqi<=300){
			stateName="轻度污染";
		}else if(aqi>300){
			stateName="严重污染";
		}
	 }
	 return stateName;
 }
 /**
  * 根据aqi值获取对应的状态标识
  * @param aqiStr
  * @return
  */
 public static String  getStateByAqi(String aqiStr){
		String state="1";
		 if(!StringUtils.isEmpty(aqiStr)){
			int aqi=Integer.valueOf(aqiStr);
			if(aqi>=0&&aqi<=50){
				 state="1";
			}else if(aqi>=51&&aqi<=100){
				 state="2";
			}else if(aqi>=101&&aqi<=150){
				 state="3";
			}else if(aqi>=151&&aqi<=200){
				 state="4";
			}else if(201>=151&&aqi<=300){
				 state="5";
			}else if(aqi>300){
				 state="6";
			}
		 }
		return state;
	}

}
