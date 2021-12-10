package cn.ffcs.zhsq.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.ffcs.common.ExportHelper;


import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.base.export.ExpStruc;
import cn.ffcs.zhsq.base.export.IExporter;
import cn.ffcs.zhsq.base.export.ITransfer;
import cn.ffcs.zhsq.utils.ConstantValue;

// @author YangChQ
@Controller
@RequestMapping("/common/export")
public class ExportController {
	
	@Autowired
	private ITransfer transfer;

	@RequestMapping(value="/excel")
	public void export(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String beanId = request.getParameter("beanId");
		String ctlType = request.getParameter("ctlType");
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> pmMap = new HashMap<String, Object>();
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue != null && paramValue.length() != 0) {
					pmMap.put(paramName, paramValue);
				}
			}
		}
		if (StringUtils.isBlank(beanId)) throw new Exception("参数【beanId】不能为空，请检查！");
		ExpStruc expStruc = transfer.getExpStruc(beanId, ctlType, userInfo, pmMap);
		if (expStruc != null) {
			ExportHelper exportHelper = new ExportHelper(request, response);
			if (IExporter.EXP_LIST.equals(expStruc.getExpType())) {
				exportHelper.exportExcel(expStruc.getFileName(), expStruc.getKeys(), expStruc.getVals());
			} else if (IExporter.EXP_TEMP.equals(expStruc.getExpType())) {
				exportHelper.exportExcel(expStruc.getFileName(), expStruc.getTempPath(), expStruc.getTempData());
			} else {
				throw new Exception("未知的导出类型！");
			}
		} else {
			throw new Exception("获取导出数据结构失败！");
		}
	}
}
