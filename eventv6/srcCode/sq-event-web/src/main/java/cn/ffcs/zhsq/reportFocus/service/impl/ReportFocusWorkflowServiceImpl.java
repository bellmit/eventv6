package cn.ffcs.zhsq.reportFocus.service.impl;

import cn.ffcs.rmq.producer.RmqProducer;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.reportFocus.IReportFocusWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ReadProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 南安重点关注上报工作流相关接口
 * @Description: 
 * @ClassName:   ReportFocusWorkflowServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 上午9:19:44
 */
@Service(value = "reportFocusWorkflowService")
public class ReportFocusWorkflowServiceImpl implements IReportFocusWorkflowService {

	protected final Log logger = LogFactory.getLog(this.getClass());

	//RMQ的ip地址
	private String RMQ_IP = ReadProperties.javaLoadProperties("rmq.ip", "global.properties");
	//RMQ的端口
	private String RMQ_PORT = ReadProperties.javaLoadProperties("rmq.port", "global.properties");
	//南安入格事件RMQ监听流程变化虚拟主机 直接写死
	private String RMQ_VHOST_NA_REPORTFOCUS = "/naReportFocusVhost";//ReadProperties.javaLoadProperties("rmq.vhost_na_reportfocus", "global.properties");
	//南安入格事件RMQ监听流程变化交换机标识 直接写死
	private String RMQ_EXCHANGESTR_NA_REPORTFOCUS = "naReportFocusExchangeStr";//ReadProperties.javaLoadProperties("rmq.exchangeStr_na_reportfocus", "global.properties");
	//是否开启南安入格mq事件环节推送开/关
	protected String RMQ_NA_REPORTFOCUS_OPEN = ReadProperties.javaLoadProperties("naReportFocus.isOpenRMQMsg", "global.properties");

	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Override
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Boolean result = null;

		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(extraParam);
		result = reportFocusWorkflowService.startWorkflow4Report(reportId, userInfo, extraParam);

		//推送rmq消息 主键，实例id，当前环节orgID，当前环节orgName，status
		if(result && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag","start");

			Map<String,Object> afterElement=new HashMap<String,Object>();
			afterElement.put("eventId", reportId);
			if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				afterElement.put("instanceId",Long.valueOf(extraParam.get("instanceId").toString()));
			}
			mqParams.put("afterElement", afterElement);

			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Boolean result = null;
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(extraParam);
		result = reportFocusWorkflowService.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

		//推送rmq消息 主键，实例id，当前环节orgID，当前环节orgName，status
		if(result && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag","submit");

			Map<String,Object> afterElement=new HashMap<String,Object>();
			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("formId").toString()));
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("reportId").toString()));
				}
			} catch(NumberFormatException e) {}
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);

			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Boolean result = null;
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(extraParam);
		result = reportFocusWorkflowService.rejectWorkflow4Report(instanceId, userInfo, extraParam);

		//推送rmq消息 主键，实例id，当前环节orgID，当前环节orgName，status
		if(result && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag","reject");

			Map<String,Object> afterElement=new HashMap<String,Object>();
			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("formId").toString()));
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("reportId").toString()));
				}
			} catch(NumberFormatException e) {}
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);

			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Boolean result = null;
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(extraParam);
		result = reportFocusWorkflowService.recallWorkflow4Report(instanceId, userInfo, extraParam);

		//推送rmq消息 主键，实例id，当前环节orgID，当前环节orgName，status
		if(result && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag","recall");

			Map<String,Object> afterElement=new HashMap<String,Object>();
			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("formId").toString()));
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					afterElement.put("eventId",Long.valueOf(extraParam.get("reportId").toString()));
				}
			} catch(NumberFormatException e) {}
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);

			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(params);
		
		return reportFocusWorkflowService.initWorkflow4Handle(instanceId, userInfo, params);
	}
	
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(params);
		
		return reportFocusWorkflowService.capHandledTaskInfoMap(instanceId, order, params);
	}
	
	@Override
	public Map<String, Object> capProInstance(Long reportId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IReportFocusWorkflowService reportFocusWorkflowService = capReportWorkflowService(params);
		
		return reportFocusWorkflowService.capProInstance(reportId, userInfo, params);
	}
	
	/**
	 * 获取工作流接口
	 * @param params
	 * 			reportType	上报类型，1 两违防治；
	 * @return
	 * @throws Exception 
	 */
	private IReportFocusWorkflowService capReportWorkflowService(Map<String, Object> params) throws Exception {
		String reportType = null;
		IReportFocusWorkflowService reportStatusDecisionMakingService = null;
		ReportServiceAgent<IReportFocusWorkflowService> reportServiceAgent = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		reportServiceAgent = new ReportServiceAgent<IReportFocusWorkflowService>(reportType, ReportServiceAgent.serviceTypeEnum.reportWorkflow.getServiceTypeIndex());
		
		reportStatusDecisionMakingService = reportServiceAgent.capService();
		
		return reportStatusDecisionMakingService;
	}

	public Boolean sendFlowRmqMsg(Map<String,Object> params) throws Exception{

		if(StringUtils.isNotBlank(RMQ_NA_REPORTFOCUS_OPEN) && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)) {

			Object naReportFocusRmqProducer = null;
			try {
				naReportFocusRmqProducer = defaultListableBeanFactory.getBean("naReportFocusRmqProducer");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(naReportFocusRmqProducer == null) {
				//进行初始化
				logger.info("南安入格事件rmq推送流程初始化开始");
				RmqProducer thisProducer=new RmqProducer();

				if(StringUtils.isBlank(RMQ_IP)) {
					logger.error("流程同步失败，未配置rmq连接的IP地址，请在zk中配置rmq.ip后重新启动");
					return false;
				}else {
					thisProducer.setRmqServerIP(RMQ_IP);
				}
				if(StringUtils.isBlank(RMQ_PORT)) {
					logger.error("流程同步失败，未配置rmq连接的PORT端口，请在zk中配置rmq.port后重新启动");
					return false;
				}else {
					thisProducer.setRmqServerPort(Integer.valueOf(RMQ_PORT));
				}
				if(StringUtils.isBlank(RMQ_VHOST_NA_REPORTFOCUS)) {
					logger.error("流程同步失败，未配置rmq连接的虚拟主机，请配置[RMQ_VHOST_NA_REPORTFOCUS]");
					return false;
				}else {
					thisProducer.setRmqVirtualHost(RMQ_VHOST_NA_REPORTFOCUS);
				}
				if(StringUtils.isBlank(RMQ_EXCHANGESTR_NA_REPORTFOCUS)) {
					logger.error("流程同步失败，未配置rmq连接的交换机，请配置[RMQ_EXCHANGESTR_NA_REPORTFOCUS]");
					return false;
				}else {
					thisProducer.setExchangeStr(RMQ_EXCHANGESTR_NA_REPORTFOCUS);
				}

				naReportFocusRmqProducer = thisProducer;
				//将new出的对象放入Spring容器中
				defaultListableBeanFactory.registerSingleton("naReportFocusRmqProducer",thisProducer);
				//自动注入依赖
				beanFactory.autowireBean(thisProducer);

				//重连机制初始化
				thisProducer.afterPropertiesSet();

				logger.info("rmq推送流程初始化结束");
			}

			RmqProducer flowRmqProducer = (RmqProducer) naReportFocusRmqProducer;

			try {

				flowRmqProducer.sendMessage(RMQ_EXCHANGESTR_NA_REPORTFOCUS, params);

			} catch (Exception e) {

				logger.error("发送rmq信息出错，入格事件流程推送失败!");
				e.printStackTrace();
			}
		}
		return true;
	}
}
