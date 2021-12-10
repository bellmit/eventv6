package cn.ffcs.zhsq.base.export;

import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;

public interface IExporter {

	public static String EXP_LIST = "1";

	public static String EXP_TEMP = "2";

	/**
	 * 获取导出数据结构
	 * 
	 * @param ctlType
	 *            ：逻辑控制类型
	 * @param userInfo
	 *            ：用户信息
	 * @param pmMap
	 *            ：request请求参数
	 * @return
	 */
	public ExpStruc getExpStruc(String ctlType, UserInfo userInfo, Map<String, Object> pmMap);
}
