package cn.ffcs.zhsq.checkConfig.service;

import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;

/**
 * 功能配置检测接口
 * @author sulch
 * 2015年6月16日 下午3:03:12
 */
public interface ICheckConfigService {
	/**
	 * <p>Description:功能配置检测入口</p>
	 * add sulch
	 * 2015年6月16日 下午3:03:01
	 */
	public CheckResultInfo runCheck(String orgCode);
	
	
}
