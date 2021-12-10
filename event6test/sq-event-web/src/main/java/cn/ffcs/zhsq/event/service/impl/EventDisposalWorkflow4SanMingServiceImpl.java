package cn.ffcs.zhsq.event.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.utils.DateUtils;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 三明市明溪、尤溪、永安 工作流处理类
 * @ClassName: EventDisposalWorkflow4SanMingServiceImpl
 * @author: wuxq
 * @date: 2021年04月08日
 */
@Service(value = "eventDisposalWorkflow4SanMingService")
public class EventDisposalWorkflow4SanMingServiceImpl extends EventDisposalWorkflow4SCUMYPQServiceImpl {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	private static final String taskName = "task7";// 专业部门环节名称

	private SimpleDateFormat df = new SimpleDateFormat(DateUtils.PATTERN_24TIME);

	/**
	 * 获取事件当前环节信息
	 * 
	 * @param 查询参数
	 * @return
	 */
	@Override
	public Map<String, Object> findRmqInitInfo(Map<String, Object> params, String orgCode) {

		Long instanceId = 0L;

		if (CommonFunctions.isNotBlank(params, "instanceId")) {

			instanceId = Long.valueOf(params.get("instanceId").toString());

			// 获取事件当前办理环节信息
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);

			String curNodeName = proInstance.getCurNode();
			// 当前环节为专业部门，返回专业部门名称、部门办理时间
			if (taskName.equals(curNodeName)) {

				if (CommonFunctions.isNotBlank(params, "userInfo")) {
					UserInfo userInfo = (UserInfo) params.get("userInfo");
					params.put("isSanMing", true);
					params.put("orgName", userInfo.getOrgName());// 专业部门名称
					params.put("endTime", df.format(new Date()));// 部门办理时间 
					params.put("eventId", proInstance.getFormId());//事件Id
				}

			}

		}

		return params;
	}

}
