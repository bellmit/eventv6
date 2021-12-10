package cn.ffcs.zhsq.checkConfig.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.checkConfig.service.ICheckConfigBaseService;
import cn.ffcs.zhsq.checkConfig.service.ICheckConfigService;
import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;

@Service(value = "checkConfigBaseServiceImpl")
public class CheckConfigBaseServiceImpl extends ApplicationObjectSupport implements ICheckConfigBaseService {
	@Autowired
	private ICheckConfigService checkConfigService;
	
	private static final String[] checkConfigServiceImplNames = {"appDomainCheckServiceImpl"};
//	private List<ICheckConfigService> checkConfigServices;
//	
//	public List<ICheckConfigService> getCheckConfigServices() {
//		return checkConfigServices;
//	}
//
//	public void setCheckConfigServices(List<ICheckConfigService> checkConfigServices) {
//		this.checkConfigServices = checkConfigServices;
//	}

	@Override
	public CheckResultInfo startCheck(String orgCode, String checkConfigServiceImplName) {
		ICheckConfigService checkConfigService = (ICheckConfigService)this.getApplicationContext().getBean(checkConfigServiceImplName);
		CheckResultInfo checkResultInfo = new CheckResultInfo();
		try {
			checkResultInfo = checkConfigService.runCheck(orgCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkResultInfo;
	}

	@Override
	public String[] getCheckConfigServiceImplNames() {
		return checkConfigServiceImplNames;
	}

}
