package cn.ffcs.zhsq.common.server.impl;

import java.util.Map;

import org.springframework.stereotype.Service;


import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.base.export.ExpStruc;
import cn.ffcs.zhsq.base.export.IExporter;
import cn.ffcs.zhsq.base.export.ITransfer;
import cn.ffcs.zhsq.utils.SpringContextUtil;

@Service(value = "expTransfer")
public class ExpTransfer implements ITransfer {

	@Override
	public ExpStruc getExpStruc(String beanId, String ctlType, UserInfo userInfo, Map<String, Object> pmMap)
			throws Exception {
		Object obj = SpringContextUtil.getBean(beanId);
		if (obj instanceof IExporter) {
			IExporter exporter = (IExporter) obj;
			ExpStruc expStruc = exporter.getExpStruc(ctlType, userInfo, pmMap);
			return expStruc;
		} else {
			throw new Exception("该实例【" + obj.getClass().getName() + "】未实现导出接口【IExporter】！");
		}
	}

}
