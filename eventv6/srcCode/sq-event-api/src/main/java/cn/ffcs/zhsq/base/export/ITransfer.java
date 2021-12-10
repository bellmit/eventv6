package cn.ffcs.zhsq.base.export;

import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;

public interface ITransfer {

	public ExpStruc getExpStruc(String beanId, String ctlType, UserInfo userInfo, Map<String, Object> pmMap)
			throws Exception;
}
