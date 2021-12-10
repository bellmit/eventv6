package cn.ffcs.zhsq.reportFocus.service.impl;

import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusExtendService;
import cn.ffcs.zhsq.reportFocus.IReportFocusWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 重点上报相关接口代理
 * @ClassName:   ReportServiceAgent   
 * @author:      张联松(zhangls)
 * @date:        2020年9月17日 上午11:02:50
 */
@SuppressWarnings("hiding")
public class ReportServiceAgent<T> {
	private String reportType;
	private int serviceType;
	
	/**
	 * @Description: 相关接口类型枚举
	 * @ClassName:   serviceTypeEnum   
	 * @author:      张联松(zhangls)
	 * @date:        2020年9月17日 上午11:03:45
	 */
	public enum serviceTypeEnum {
		reportStatusDecison(1, "上报状态决策"),
		reportWorkflow(2, "上报工作流接口"),
		reportNodeDecision(3, "上报工作流节点决策"),
		reportExtendService(4, "入格业务接口");//入格事件具体模块的接口

		private String serviceTypeName;
		private int serviceTypeIndex;
		
		private serviceTypeEnum(int serviceTypeIndex, String serviceTypeName) {
			this.serviceTypeIndex = serviceTypeIndex;
			this.serviceTypeName = serviceTypeName;
		}
		
		public int getServiceTypeIndex() {
			return serviceTypeIndex;
		}
		
		public String toString() {
			return serviceTypeName;
		}
	};
	
	public ReportServiceAgent(String reportType, int serviceType) {
		this.reportType = reportType;
		this.serviceType = serviceType;
	}
	
	@SuppressWarnings({ "serial", "unchecked" })
	public T capService() throws Exception {
		Map<Integer, String> methodMap = new HashMap<Integer, String>() {
			{
				put(serviceTypeEnum.reportStatusDecison.getServiceTypeIndex(), "capReportStatusDecionService");
				put(serviceTypeEnum.reportWorkflow.getServiceTypeIndex(), "capReportWorkflowService");
				put(serviceTypeEnum.reportNodeDecision.getServiceTypeIndex(), "capReportNodeDecionService");
				put(serviceTypeEnum.reportExtendService.getServiceTypeIndex(), "capReportExtendService");
			}
		};
		
		if(!methodMap.containsKey(serviceType)) {
			throw new IllegalArgumentException("非法的接口类型【"+ serviceType +"】！");
		}
		
		//以下只使用于public修饰的方法
		//this.getClass().getMethod(methodMap.get(serviceType)).invoke(this);
		//以下方法可适用于public、protected、private修饰的方法
		return (T) this.getClass().getDeclaredMethod(methodMap.get(serviceType)).invoke(this);
	}
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * 上报状态决策代理
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private IWorkflowDecisionMakingService<String> capReportStatusDecionService() {
		Map<String, String> reportStatusDecisionMakingServiceMap = new HashMap<String, String>();
		IWorkflowDecisionMakingService<String> reportStatusDecisionMakingService = null;
		
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.twoVioPre.getReportTypeIndex(), "reportStatusDecisionMaking4TwoVioPreService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex(), "reportStatusDecisionMaking4HHDService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex(), "reportStatusDecisionMaking4EHDService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.epidemicPreControl.getReportTypeIndex(), "reportStatusDecisionMaking4EPCService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.waterQuality.getReportTypeIndex(), "reportStatusDecisionMaking4WaterQualityService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.meetingAndLesson.getReportTypeIndex(), "reportStatusDecisionMaking4MeetingService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.poorSupportVisit.getReportTypeIndex(), "reportStatusDecisionMaking4PSVService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.ruralHousing.getReportTypeIndex(), "reportStatusDecisionMaking4RuralHousingService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.forestFirePrevention.getReportTypeIndex(), "reportStatusDecisionMaking4FFPService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.businessProblem.getReportTypeIndex(), "reportStatusDecisionMaking4BusProService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.povertyPreMonitor.getReportTypeIndex(), "reportStatusDecisionMaking4PPMService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.petitionPerson.getReportTypeIndex(), "reportStatusDecisionMaking4PetPerService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.martyrsFacility.getReportTypeIndex(), "reportStatusDecisionMaking4MarFacService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.environmentHealTreatment.getReportTypeIndex(), "reportStatusDecisionMaking4EHTService");
		reportStatusDecisionMakingServiceMap.put(ReportTypeEnum.threeOneTreatment.getReportTypeIndex(), "reportStatusDecisionMaking4TOTService");

		if(reportStatusDecisionMakingServiceMap.containsKey(reportType)) {
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			
			reportStatusDecisionMakingService = (IWorkflowDecisionMakingService<String>) applicationContext.getBean(reportStatusDecisionMakingServiceMap.get(reportType));
		} else {
			throw new IllegalArgumentException("上报类型【" + reportType + "】缺少上报状态决策实现配置！");
		}
		
		return reportStatusDecisionMakingService;
	}
	
	/**
	 * 上报工作流接口代理
	 * @return
	 */
	@SuppressWarnings("unused")
	private IReportFocusWorkflowService capReportWorkflowService() {
		Map<String, String> reportWorkflowServiceMap = new HashMap<String, String>();
		IReportFocusWorkflowService reportWorkflowService = null;
		
		reportWorkflowServiceMap.put(ReportTypeEnum.twoVioPre.getReportTypeIndex(), "reportFocusWorkflow4TwoVioPreService");
		reportWorkflowServiceMap.put(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex(), "reportFocusWorkflow4HHDService");
		reportWorkflowServiceMap.put(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex(), "reportFocusWorkflow4EHDService");
		reportWorkflowServiceMap.put(ReportTypeEnum.epidemicPreControl.getReportTypeIndex(), "reportFocusWorkflow4EPCService");
		reportWorkflowServiceMap.put(ReportTypeEnum.waterQuality.getReportTypeIndex(), "reportFocusWorkflow4WaterQualityService");
		reportWorkflowServiceMap.put(ReportTypeEnum.meetingAndLesson.getReportTypeIndex(), "reportFocusWorkflow4MeetingService");
		reportWorkflowServiceMap.put(ReportTypeEnum.poorSupportVisit.getReportTypeIndex(), "reportFocusWorkflow4PSVService");
		reportWorkflowServiceMap.put(ReportTypeEnum.ruralHousing.getReportTypeIndex(), "reportFocusWorkflow4RuralHousingService");
		reportWorkflowServiceMap.put(ReportTypeEnum.forestFirePrevention.getReportTypeIndex(), "reportFocusWorkflow4FFPService");
		reportWorkflowServiceMap.put(ReportTypeEnum.businessProblem.getReportTypeIndex(), "reportFocusWorkflow4BusProService");
		reportWorkflowServiceMap.put(ReportTypeEnum.povertyPreMonitor.getReportTypeIndex(), "reportFocusWorkflow4PPMService");
		reportWorkflowServiceMap.put(ReportTypeEnum.petitionPerson.getReportTypeIndex(), "reportFocusWorkflow4PetPerService");
		reportWorkflowServiceMap.put(ReportTypeEnum.martyrsFacility.getReportTypeIndex(), "reportFocusWorkflow4MarFacService");
		reportWorkflowServiceMap.put(ReportTypeEnum.environmentHealTreatment.getReportTypeIndex(), "reportFocusWorkflow4EHTService");
		reportWorkflowServiceMap.put(ReportTypeEnum.threeOneTreatment.getReportTypeIndex(), "reportFocusWorkflow4TOTService");

		if(reportWorkflowServiceMap.containsKey(reportType)) {
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			
			reportWorkflowService = (IReportFocusWorkflowService) applicationContext.getBean(reportWorkflowServiceMap.get(reportType));
		} else {
			throw new IllegalArgumentException("上报类型【" + reportType + "】缺少工作流实现配置！");
		}
		
		return reportWorkflowService;
	}
	
	/**
	 * 节点决策代理
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private IWorkflowDecisionMakingService<String> capReportNodeDecionService() {
		Map<String, String> reportWorkflowDecisionMakingServiceMap = new HashMap<String, String>();
		IWorkflowDecisionMakingService<String> reportWorkflowDecisionMakingService = null;
		
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.twoVioPre.getReportTypeIndex(), "reportWorkflowDecisionMaking4TwoVioPreService");
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.epidemicPreControl.getReportTypeIndex(), "reportWorkflowDecisionMaking4EPCService");
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.meetingAndLesson.getReportTypeIndex(), "reportWorkflowDecisionMaking4MeetinService");
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.poorSupportVisit.getReportTypeIndex(), "reportWorkflowDecisionMaking4PSVService");
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex(), "reportWorkflowDecisionMaking4HHDService");
		reportWorkflowDecisionMakingServiceMap.put(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex(), "reportWorkflowDecisionMaking4EHDService");
		
		if(reportWorkflowDecisionMakingServiceMap.containsKey(reportType)) {
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			
			reportWorkflowDecisionMakingService = (IWorkflowDecisionMakingService<String>) applicationContext.getBean(reportWorkflowDecisionMakingServiceMap.get(reportType));
		}
		
		return reportWorkflowDecisionMakingService;
	}
	
	/**
	 * 获取上报扩展信息实现接口
	 * @return
	 */
	@SuppressWarnings("unused")
	private IReportFocusExtendService capReportExtendService() {
		Map<String, String> reportExtendServiceMap = new HashMap<String, String>();
		IReportFocusExtendService reportFocusExtendService = null;
		
		reportExtendServiceMap.put(ReportTypeEnum.twoVioPre.getReportTypeIndex(), "reportTwoViolationPreService");
		reportExtendServiceMap.put(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex(), "reportEHDService");
		reportExtendServiceMap.put(ReportTypeEnum.epidemicPreControl.getReportTypeIndex(), "reportEPCService");
		reportExtendServiceMap.put(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex(), "reportHHDService");
		reportExtendServiceMap.put(ReportTypeEnum.waterQuality.getReportTypeIndex(), "reportWaterQualityService");
		reportExtendServiceMap.put(ReportTypeEnum.meetingAndLesson.getReportTypeIndex(), "reportMeetingService");
		reportExtendServiceMap.put(ReportTypeEnum.poorSupportVisit.getReportTypeIndex(), "reportPSVService");
		reportExtendServiceMap.put(ReportTypeEnum.ruralHousing.getReportTypeIndex(), "reportRuralHousingService");
		reportExtendServiceMap.put(ReportTypeEnum.forestFirePrevention.getReportTypeIndex(), "reportFFPService");
		reportExtendServiceMap.put(ReportTypeEnum.businessProblem.getReportTypeIndex(), "reportBusProService");
		reportExtendServiceMap.put(ReportTypeEnum.povertyPreMonitor.getReportTypeIndex(), "reportPPMService");
		reportExtendServiceMap.put(ReportTypeEnum.petitionPerson.getReportTypeIndex(), "reportPetPerService");
		reportExtendServiceMap.put(ReportTypeEnum.martyrsFacility.getReportTypeIndex(), "reportMarFacService");
		reportExtendServiceMap.put(ReportTypeEnum.environmentHealTreatment.getReportTypeIndex(), "reportEHTService");
		reportExtendServiceMap.put(ReportTypeEnum.threeOneTreatment.getReportTypeIndex(), "reportTOTService");

		if(CommonFunctions.isNotBlank(reportExtendServiceMap, reportType)) {
			ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			
			reportFocusExtendService = (IReportFocusExtendService) applicationContext.getBean(reportExtendServiceMap.get(reportType));
		} else {
			throw new IllegalArgumentException("上报类型【" + reportType + "】缺少实现配置！");
		}
		
		return reportFocusExtendService;
	}
}
