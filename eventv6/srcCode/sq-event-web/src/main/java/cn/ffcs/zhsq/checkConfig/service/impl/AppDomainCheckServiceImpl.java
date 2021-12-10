package cn.ffcs.zhsq.checkConfig.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.FunConfigureOM;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.IFunConfigureService;
import cn.ffcs.zhsq.checkConfig.service.ICheckConfigService;
import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;
/**
 * 域名检测服务接口
 * @author sulch
 * 2015年6月16日 下午4:23:00
 */
@Service(value = "appDomainCheckServiceImpl")
public class AppDomainCheckServiceImpl implements ICheckConfigService {
	//获取指定功能配置
	@Autowired
	private IFunConfigureService funConfigureService;
	//获取功能配置的设置值
	@Autowired
	private IFunConfigurationService funConfigurationService;
	//日志
	private static final Logger logger = LoggerFactory.getLogger(AppDomainCheckServiceImpl.class.getClass());
	
	//域名配置
	private static final String APP_DOMAIN = "APP_DOMAIN";
	
	 /**
	  * 		<p>工商网格应用code:$ICGRID_DOMAIN</p>
	  * 		<p>公共样式应用code：$STATIC_DOMAIN</p>
	  * 		<p>系统管理应用code：$SYSTEM_DOMAIN</p>
	  * 		<p>新事件应用code：$EVENT_DOMAIN</p>
	  * 		<p>综治网格应用code：$ZZGRID_DOMAIN</p>
	  * 		<p>人口应用code：$RS_DOMAIN</p>
	  * 		<p>GMIS应用code：$GMIS_DOMAIN</p>
	  * 		<p>WORKFLOW应用code：$WORKFLOW_DOMAIN</p>
	  * 		<p>旧地图域名code：$OLD_GIS_MAP_DOMAIN</p>
	  * 		<p>SMS应用code：$SMS_DOMAIN</p>
	  * 		<p>UAM应用code：$UAM_DOMAIN</p>
	  * 		<p>导入导出应用code：$IMPEXP_DOMAIN</p>
	  * 		<p>资源域名code：$RESOURCE_DOMAIN</p>
	  * 		<p>BI应用code：$BI_DOMAIN</p>
	  * 		<p>WATER应用code：$WATER_DOMAIN</p>
	  */
	private static final String[] APP_DOMAIN_CONFIG = {
		"$ICGRID_DOMAIN","$STATIC_DOMAIN","$SYSTEM_DOMAIN","$EVENT_DOMAIN","$ZZGRID_DOMAIN","$RS_DOMAIN",
		"$GMIS_DOMAIN","$WORKFLOW_DOMAIN","$OLD_GIS_MAP_DOMAIN","$SMS_DOMAIN","$UAM_DOMAIN","$IMPEXP_DOMAIN",
		"$RESOURCE_DOMAIN","$BI_DOMAIN","$WATER_DOMAIN"
	};
	
	private static final String[] APP_DOMAIN_CONFIG_NAME = {
		"工商网格应用","公共样式应用","系统管理应用","新事件应用","综治网格应用","人口应用","GMIS应用",
		"WORKFLOW应用","旧地图","SMS应用","UAM应用","导入导出应用","资源",
		"BI应用","WATER应用"
	};
	
	private static final Map<String, String> APP_DOMAIN_CONFIG_MAP = new HashMap<String, String>(){
		{
			put("$ICGRID_DOMAIN", "工商网格应用");
			put("$STATIC_DOMAIN", "公共样式应用");
			put("$SYSTEM_DOMAIN", "系统管理应用");
			put("$EVENT_DOMAIN", "新事件应用");
			put("$ZZGRID_DOMAIN", "综治网格应用");
			put("$RS_DOMAIN", "人口应用");
			put("$GMIS_DOMAIN", "GMIS应用");
			put("$WORKFLOW_DOMAIN", "WORKFLOW应用");
			put("$OLD_GIS_MAP_DOMAIN", "旧地图");
			put("$SMS_DOMAIN", "SMS应用");
			put("$UAM_DOMAIN", "UAM应用");
			put("$IMPEXP_DOMAIN", "导入导出应用");
			put("$RESOURCE_DOMAIN", "资源");
			put("$BI_DOMAIN", "BI应用");
			put("$WATER_DOMAIN", "WATER应用");
		}
	};
	
	private CheckResultInfo checkResultInfo;
	
	private String SUCCESS = "1";
	private String ERROR = "1";
	private String WARM = "2";
	
	@Override
	public CheckResultInfo runCheck(String orgCode) {
		checkResultInfo = new CheckResultInfo();
		checkResultInfo.setFuncCode(APP_DOMAIN);
		logger.info("开始==========>检测“应用域名配置”的功能配置。。。。。。。。");
		
		List<FunConfigureOM> list = new ArrayList<FunConfigureOM>();
		try {
			list = funConfigureService.findConfigureInfoList(APP_DOMAIN);
			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getStatus().equals("001")){
						checkResultInfo.setFuncName(list.get(0).getFuncName());
					}
				}
				if(StringUtils.isBlank(checkResultInfo.getFuncName())){
					checkResultInfo.addReasonDesc("域名配置已配置但是未启用。");
					checkResultInfo.addSolutionDesc("进入功能配置，启用功能编码为“APP_DOMAIN”的功能配置。");
				}
				this.checkAppDmainConfig();
			} else {
				// 没有配置“APP_DOMAIN”的功能配置
				checkResultInfo.addReasonDesc("域名配置未配置。");
				checkResultInfo.addSolutionDesc("进入功能配置，配置功能编码为“APP_DOMAIN”的功能配置并启用。");
				logger.info("==========>该环境尚未配置“应用域名配置”，可能导致某些功能使用异常，请联系管理员进行配置！！！！");
			}
		} catch (Exception e) {
			checkResultInfo.addReasonDesc("funConfigureService.findConfigureInfoList调用失败。");
			checkResultInfo.addSolutionDesc("排查IFunConfigureService接口是否正常。");
			logger.error("=========>获取功能配置失败",e);
		}

		logger.info("结束==========>“应用域名配置”的功能配置检测结束！");
		if(checkResultInfo!=null && checkResultInfo.getReasonDesc()!=null && checkResultInfo.getReasonDesc().size()>0){
			checkResultInfo.setResultCode(ERROR);
		}
		return checkResultInfo;
	}

	
	/**
	 * <p>
	 * Description:检测“应用域名配置”的设置值配置
	 * </p>
	 * add sulch 2015年6月12日 上午9:42:33
	 */
	private void checkAppDmainConfig() {
		Map<String, String> appDomains = new HashMap<String, String>();
		for (String key : APP_DOMAIN_CONFIG_MAP.keySet()) {  
			String appDomainConfig = funConfigurationService.getAppDomain(key);
			if (StringUtils.isBlank(appDomainConfig)) {
				checkResultInfo.addReasonDesc("该环境尚未配置" + APP_DOMAIN_CONFIG_MAP.get(key) + "的域名。");
				checkResultInfo.addSolutionDesc("配置触发条件为“"+ APP_DOMAIN_CONFIG_MAP.get(key) +"”的设置值并启用。");
				logger.warn("==========>该环境尚未配置" + APP_DOMAIN_CONFIG_MAP.get(key) + "的域名，可能导致某些功能使用异常，请联系管理员进行配置！！！！");
				appDomains.put(key, "");
			} else {
				appDomains.put(key, appDomainConfig);
			}
        }  
		if (appDomains != null && appDomains.size() > 0) {
			this.ping(appDomains);
		}
	}
	
	/**
	 * 对指定的ip(数组)进行ping探测
	 * 
	 * @param ips
	 *            key为ip，value为ip的中文名称
	 * @return 返回格式为 ip:是否成功
	 */
	public LinkedHashMap<String, Boolean> ping(Map<String, String> ips) {
		LinkedHashMap<String, Boolean> ret = new LinkedHashMap<String, Boolean>();
		Map<String, String> args = new HashMap<String, String>();
		BufferedReader br = null;
		for (String key : ips.keySet()) {
			String ip = ips.get(key);
			//------start:判断域名是否合法
			if(!ip.startsWith("http://")){
				checkResultInfo.addReasonDesc("触发条件为“"+ key +"”配置的不是一个合法的域名");
				checkResultInfo.addSolutionDesc("检测触发条件为“"+ key +"”的设置值是否配置正确。");
				logger.warn("==========>" + APP_DOMAIN_CONFIG_MAP.get(key) + "域名不合法！请排查!!!");
				continue;
			}
			//------end:判断域名是否合法
			if (StringUtils.isNotBlank(ip)) {// 截取域名
				//ip = http://zzgriddev.fjsq.org/zzgrid
				ip = ip.substring(7);
				ip = ip.split("/")[0];
			}
			args.put("ip", ip);
			// 获取服务器的系统版本
			String osName = System.getProperty("os.name").toLowerCase();
			String value = "";
			// 根据服务器的系统版本使用不用的ping命令
			if (-1 != osName.indexOf("windows")) {
				value = "ping -n 1 -w 1 -l 8 ${ip}";
			} else if (-1 != osName.indexOf("linux")) {
				value = "ping -c 1 -s 8 ${ip}";
			}
			// 拼接完整的ping命令行
			Set<Map.Entry<String, String>> values = args.entrySet();
			for (Map.Entry<String, String> j : values) {
				value = value.replaceAll("\\${1}\\{{1}" + j.getKey() + "\\}{1}", j.getValue());
			}
			try {
				// 执行ping命令
				Process p = Runtime.getRuntime().exec(value);
				StringBuilder sb = new StringBuilder();
				String line = null;
				br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = br.readLine()) != null) {
					sb.append(line.trim());
					sb.append('\n');
				}
				br.close();
				int index = sb.toString().indexOf("TTL");
				ret.put(ip, index != -1);
				if (index == -1) {
					checkResultInfo.addReasonDesc(APP_DOMAIN_CONFIG_MAP.get(key) + "域名ping不通。");
					checkResultInfo.addSolutionDesc("检测触发条件为“"+ key +"”的设置值是否配置正确。");
					logger.warn("==========>" + APP_DOMAIN_CONFIG_MAP.get(key) + "域名ping不通！请排查!!!");
				}
			} catch (IOException e) {
				logger.warn("==========>", e);
			}
		}
		return ret;
	}
	
}
