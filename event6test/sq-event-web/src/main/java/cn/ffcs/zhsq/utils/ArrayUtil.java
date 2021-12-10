package cn.ffcs.zhsq.utils;

import org.apache.commons.lang.StringUtils;


/**
 * @文件描述: TODO
 * @内容摘要: 
 * @完成日期: Aug 7, 2014
 * 修改日期			修改人
 * Aug 7, 2014		zhongshm
 */
public class ArrayUtil {
	
	/**
	 * 数组转换String（如果有空返回空）
	 * @param resource
	 * @return
	 */
	public static String ArrayToString(String[] resource){
		StringBuffer retVal = new StringBuffer("");
		
		if(resource!=null && resource.length>0){
			for(String tmp : resource){
				if(StringUtils.isNotBlank(tmp)){
					retVal.append(tmp).append(",");
				}
			}
			
			if(retVal.length() > 0){
				retVal = new StringBuffer(retVal.substring(0, retVal.length() - 1));
			}
		}
		
		return retVal.toString();
	}
}
