package cn.ffcs.zhsq.checkConfig.service;

import java.util.List;

import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;

/**
 * 功能配置检测服务接口
 * @author sulch
 * 2015年6月16日 下午4:19:17
 */
public interface ICheckConfigBaseService {
	/**
	 * <p>Description:检测方法入口</p>
	 * @return
	 * add sulch
	 * 2015年6月16日 下午4:20:07
	 */
	public CheckResultInfo startCheck(String orgCode, String checkConfigServiceImplName);
	
	/**
	 * <p>Description:获取所有的检测服务实现类</p>
	 * @return
	 * add sulch
	 * 2015年12月25日 上午9:48:02
	 */
	public String[] getCheckConfigServiceImplNames();
}
