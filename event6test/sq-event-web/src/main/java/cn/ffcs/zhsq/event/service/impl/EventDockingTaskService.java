package cn.ffcs.zhsq.event.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;

import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Encoder;
import cn.ffcs.common.ImageBit64Utils;
import cn.ffcs.file.service.FileUrlProvideService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.shequ.utils.net.HttpUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub.CommonResult;
import cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub;
import cn.ffcs.zhsq.event.service.stub.WebServicePublishImplServiceStub;
import cn.ffcs.zhsq.event.service.stub.WebServiceStub;
import cn.ffcs.zhsq.event.service.stub.sgdj.ReceiveRecServiceServiceStub;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataAuth;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResponse;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlEventResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlTaskResult;
import cn.ffcs.zhsq.mybatis.domain.event.docking.AHResultData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedFeedback;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedIncident;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCDatas;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJLCResult;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJXXData;
import cn.ffcs.zhsq.mybatis.domain.event.docking.SJXXDatas;
import cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub;
import cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub.PostEventResponse;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.ReadProperties;


/**
 * 定时器任务
 * @author zhongshm
 * @create 2015-6-10下午2:51:23
 */
public class EventDockingTaskService{
	//spring中已经内置的几种事件：
	//ContextClosedEvent、ContextRefreshedEvent、ontextStartedEvent、ContextStoppedEvent、RequestHandleEvent 
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**迪艾斯便民服务*/
	private String BIZPLATFORM_DS = "004";
	/**南威市级平台*/	
	private String BIZPLATFORM_NW = "007";
	/**西滨*/
	private String BIZPLATFORM_XB = "008";
	/**安海*/
	private String BIZPLATFORM_AH = "009";
	/**梅岭街道*/
	private String BIZPLATFORM_ML = "010";
	/**美亚柏科*/
	private String BIZPLATFORM_MYBK = "015";

	/**数字城管*/
	private String BIZPLATFORM_SZCG = "014";
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private FileUrlProvideService fileUrlProvideService;


	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	private String nwWebServiceUri;
	private String nwWebServicTaskUri;
	private String dsWebServiceUri;
	private String xbWebServiceUri = "http://220.162.239.191:7777/services/eventDockingServiceImpl?wsdl";
	private String ahWebServiceUri;
	
	public void eventDockingTask(){
		String dataChangeEffectDs = ReadProperties.javaLoadProperties("event.datachange.effect.ds", "global.properties");//数据库用户名
		String effectDs = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.EFFECT_12345, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
//		System.out.println(!"false".equals(dataChangeEffectDs));
		if(StringUtils.isNotBlank(effectDs) && effectDs.equals("true")){
			doDockingForDS12345();//迪艾斯 便民服务中心
		}
		String dataChangeEffectNw = ReadProperties.javaLoadProperties("event.datachange.effect.nw", "global.properties");//数据库用户名
		String effectNw = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.EFFECT_NW, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(effectNw) && effectNw.equals("true") && !"false".equals(dataChangeEffectNw)){
			doDockingForNW();//南威 市级平台
		}
		String dataChangeEffectXb = ReadProperties.javaLoadProperties("event.datachange.effect.xb", "global.properties");//数据库用户名
		String xbIct = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.XB_ICT, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if("true".equals(dataChangeEffectXb)){
			doDockingForXB();//西滨网格
		}
		String dataChangeEffectAh = ReadProperties.javaLoadProperties("event.datachange.effect.ah", "global.properties");//数据库用户名
		String effectAh = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.EFFECT_AH, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(effectAh) && effectAh.equals("true")){
			doDockingForAH();//安海
		}
		//扁平化平台警情推送
		String isDocking = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "XGY_WB", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(isDocking) && isDocking.equals("true")){
			doDocking();
		}
		//数字城管
		String sgdj = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "SGDJ", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(sgdj) && sgdj.equals("true")){
			doSGDJ(); 
		}
		//数字城管
		String mybk = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "MYBK", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(mybk) && mybk.equals("true")){
			doMYBK();
//			doMYBK_test();

		}
	}


	private void doMYBK_test(){
		System.out.println("==================================MYBK test==================================");
		String oppoSideBizCode = "2198";
		String remarks = "测试";
		String url = "http://qfqz.cunnar.com/mass/s/deal_clue";
//		String body = "{\"id\":\""+oppoSideBizCode+"\",\"remark\":\""+remarks+"\",\"status\":\"2\"}";
		String body = "id="+oppoSideBizCode+"&remark="+remarks+"&status=2";
		Map map = HttpUtils.doPostRequest(url, body, null, null, null);
		String postBody = null;
		try {
			postBody = HttpUtil.postBody(url, body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(postBody);
		System.out.println(map);
	}

	private void doMYBK(){
		System.out.println("==================================MYBK==================================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", BIZPLATFORM_MYBK);
		List<Map<String, Object>> results = dataExchangeStatusTwoWayService.findCloseEventAppointed(params);

		for(Map<String, Object> result : results){
			String oppoSideBizCode = result.get("OPPO_SIDE_BIZ_CODE").toString();
			String interId = result.get("INTER_ID").toString();
//			String remarks = result.get("RESULT").toString();
//			String occurred = result.get("OCCURRED").toString();
//			String eventId = result.get("EVENT_ID").toString();
			String xmlData = result.get("XML_DATA").toString();
//			String finTimeStr = "";
//			if(result.get("FIN_TIME")!=null){
//				Date finTime = (Date)result.get("FIN_TIME");
//				finTimeStr = DateUtils.formatDate(finTime, DateUtils.PATTERN_24TIME);
//			}
			//返回结果
			String resultXmlData = "";

			Long eventId = Long.valueOf(result.get("EVENT_ID").toString());
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			Map<String, Object> task8 = eventDisposalWorkflowService.capDoneTaskInfo(instanceId, "task8");
			List<MyTask> myTasks = (List<MyTask>) task8.get("tasks");
			MyTask myTask = myTasks.get(0);
//			System.out.println("myTask.getRemarks()-"+myTask.getRemarks());
			String url = "http://qfqz.cunnar.com/mass/s/deal_clue";
//			String body = "{\"id\":\""+oppoSideBizCode+"\",\"remark\":\""+myTask.getRemarks()+"\",\"status\":\"2\"}";
			String body = "id="+oppoSideBizCode+"&remark="+myTask.getRemarks()+"&status=2&score=1";
			System.out.println(body);
			String postBody = null;
			try {
				postBody = HttpUtil.postBody(url, body);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(postBody);

			//保存结果
			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
			dataExchangeStatus.setInterId(Long.valueOf(interId));
			dataExchangeStatus.setExchangeFlag("1");
			dataExchangeStatus.setXmlData(xmlData + "---" + postBody);
			dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeStatus);
		}
	}

	private void doSGDJ(){
		System.out.println("---doSGDJ");
		getReportEventForSGDJ();
//		syncSGDJ("");
//		syncSGDJPic();
	}
	
	private void getReportEventForSGDJ(){String path = fileUrlProvideService.getFileDomain();System.out.println("path---"+path);
		String userId = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "defaultCurrentUserId_"+BIZPLATFORM_SZCG, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		String orgId = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "defaultCurrentGroupId_"+BIZPLATFORM_SZCG, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(orgId)) return;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orgId", orgId);
		params.put("eventType", "60");//城市管理（数字城管）
		params.put("bizPlatform", BIZPLATFORM_SZCG); 
		List<Map<String, Object>> results = this.dataExchangeStatusService.findReportEvent(params);
		if(results==null || results.size() <1) return;
		Map<String, Object> result = results.get(0);
		System.out.println(result);
		String eventId = "";
		if(result.get("EVENT_ID")!=null){
			eventId = result.get("EVENT_ID").toString();
		}
		String eventType = "";
		if(result.get("TYPE_")!=null){
			String eType = result.get("TYPE_").toString();
			if(eType.startsWith("6001"))
				eventType = "部件";
			else if(eType.startsWith("6002"))
				eventType = "事件";
		}
		String evtaddress = "";
		if(result.get("OCCURRED")!=null){
			evtaddress = result.get("OCCURRED").toString();
		}
		String evtdescribe = "";
		if(result.get("CONTENT_")!=null){
			evtdescribe = result.get("CONTENT_").toString();
		}
		String tel = "";
		if(result.get("TEL")!=null){
			tel = result.get("TEL").toString();
		}
		String contactUser = "";
		if(result.get("CONTACT_USER")!=null){
			contactUser = result.get("CONTACT_USER").toString();
		}
		String reportCard = "";
		if(result.get("IDENTITY_CARD")!=null){
			reportCard = result.get("IDENTITY_CARD").toString();
		}
		String formdate = "";
		if(result.get("START_TIME")!=null){
			formdate = result.get("START_TIME").toString();
			formdate = formdate.substring(0, formdate.length()-2);
		}
		System.out.println("formdate:"+formdate);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		sb.append("<params>");
		sb.append("<Formdate>"+formdate+"</Formdate>");
		sb.append("<EvtsrcID>"+eventId+"</EvtsrcID>");
		sb.append("<EventType>"+eventType+"</EventType>");
		sb.append("<CoordinateX>33.157209</CoordinateX>");
		sb.append("<CoordinateY>116.211253</CoordinateY>");
		sb.append("<Evtaddress>"+evtaddress+"</Evtaddress>");
		sb.append("<Evtdescribe>"+evtdescribe+"</Evtdescribe>");
		sb.append("<reportName>"+contactUser+"</reportName>");
		sb.append("<reportContact>"+tel+"</reportContact>");
		sb.append("<reportCard>"+reportCard+"</reportCard>");
		sb.append("</params>");
		sb.append("</request>");
//		System.out.println("---"+sb.toString());
		String stubResult = syncSGDJ(sb.toString());
//		String stubResult = "";
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
		dataExchangeResult.setOppoSideBizCode("0");
		dataExchangeResult.setOwnSideBizCode(eventId);
		dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeResult.setDestPlatform(BIZPLATFORM_SZCG);
		dataExchangeResult.setXmlData(sb.toString() + "---result---" + stubResult);
		Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
		
		if(StringUtils.isBlank(stubResult)) return;
		List<Attachment> attas = attachmentService.findByBizId(Long.valueOf(eventId), "ZHSQ_EVENT");
		String imgDomain = "http://img2.fjsq.org/";
		
		String imgDomainCfg = funConfigurationService.turnCodeToValue("APP_DOMAIN", "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(imgDomainCfg)){
			imgDomain = imgDomainCfg;
		}
		
		sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		sb.append("<params>");
		sb.append("<TaskNum>"+stubResult+"</TaskNum>");
		sb.append("<PictureTypeID>1</PictureTypeID>");
		sb.append("</params>");
		sb.append("<attachment>");
		for(Attachment atta:attas){
			String content = "";
			
			try {
				URL url = new URL(imgDomain+atta.getFilePath());	
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(3*1000);
				//防止屏蔽程序抓取而返回403错误  
		        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		        //得到输入流  
		        InputStream inputStream = conn.getInputStream();
		        //获取自己数组  
		        byte[] getData = readInputStream(inputStream);
		        
		        BASE64Encoder encoder = new BASE64Encoder();
		        content = encoder.encode(getData);
//		        System.out.println("r---"+r);
//		        ImageBit64Utils.GenerateImage(r, "d:\\wangyc.jpg");
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			sb.append("<item>");
			sb.append("<Content>"+content+"</Content>");
			sb.append("<Type>01</Type>");
			sb.append("<FileName>"+atta.getFileName()+"</FileName>");
			sb.append("</item>");
		}
		
		sb.append("</attachment>");
		sb.append("</request>");
		String picResult = syncSGDJPic(sb.toString());
		
//		dataExchangeResult = new DataExchangeStatus();
//		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
//		dataExchangeResult.setOppoSideBizCode("0");
//		dataExchangeResult.setOwnSideBizCode(eventId);
//		dataExchangeResult.setOppoSideBizType("04");
//		dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
//		dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		dataExchangeResult.setDestPlatform(BIZPLATFORM_SZCG);
//		dataExchangeResult.setXmlData(sb.toString() + "---result---" + picResult);
//		dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
	}	
	
	private String syncSGDJPic(String params){System.out.println("params---"+params);
		String content = "";
		try {
			URL url = new URL("http://img2.fjsq.org/zzgrid/eventPhoto/2016/12/27/zzgrid-eventPhoto-ed972be797034f71bbdb2b0766111314.png");	
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(3*1000);
			//防止屏蔽程序抓取而返回403错误  
	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        //得到输入流  
	        InputStream inputStream = conn.getInputStream();
	        //获取自己数组  
	        byte[] getData = readInputStream(inputStream);
	        
	        BASE64Encoder encoder = new BASE64Encoder();
	        content = encoder.encode(getData);
//	        System.out.println("r---"+r);
//	        ImageBit64Utils.GenerateImage(r, "d:\\wangyc.jpg");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		String szcgUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "SZCG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		sb.append("<params>");
		sb.append("<TaskNum>16123000084</TaskNum>");
		sb.append("<PictureTypeID>1</PictureTypeID>");
		sb.append("</params>");
		sb.append("<attachment>");
		sb.append("<item>");
		sb.append("<Content>"+content+"</Content>");
		sb.append("<Type>01</Type>");
		sb.append("<FileName>zzgrid-eventPhoto-ed972be797034f71bbdb2b0766111314.png</FileName>");
		sb.append("</item>");
		sb.append("</attachment>");
		sb.append("</request>");
//		String test = "<?xml version='1.0' encoding='utf-8'?><request><params><TaskNum>16123000084</TaskNum><PictureTypeID>1</PictureTypeID></params><attachment><item><Content></Content><Type>01</Type><FileName></FileName></item></attachment></request>";
//		System.out.println("sb-"+sb.toString());
		
		org.apache.axis2.databinding.types.soapencoding.String str = new org.apache.axis2.databinding.types.soapencoding.String();
		str.setString(params);
		System.out.println("str-"+str);
		ReceiveRecServiceServiceStub stub = null;
		try {
			if(StringUtils.isNotBlank(szcgUri)){
				stub = new ReceiveRecServiceServiceStub(szcgUri);
			}else{
				stub = new ReceiveRecServiceServiceStub();
			}
			ReceiveRecServiceServiceStub.UploadPic uploadPic = new ReceiveRecServiceServiceStub.UploadPic();
			uploadPic.setRequestXml(str);
			ReceiveRecServiceServiceStub.UploadPicResponse r = stub.uploadPic(uploadPic);
			System.out.println("r.getProcessReturn()---"+r.getUploadPicReturn());
			return r.getUploadPicReturn().toString();

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String syncSGDJ(String params){
		String szcgUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "SZCG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		String test = "<?xml version='1.0' encoding='utf-8'?><request><params><Formdate>2016-12-29 14:32:23</Formdate><EvtsrcID>13322</EvtsrcID><EventType>事件</EventType><CoordinateX>424883.750432</CoordinateX><CoordinateY>3669716.964710</CoordinateY><Evtaddress>福建省福州市</Evtaddress><Evtdescribe>测试数据</Evtdescribe><reportName>测试人员</reportName><reportContact>13333333333</reportContact><reportCard>421123197709114197</reportCard></params></request>";
		org.apache.axis2.databinding.types.soapencoding.String str = new org.apache.axis2.databinding.types.soapencoding.String();
		str.setString(params);
		System.out.println("params---"+str);
		ReceiveRecServiceServiceStub stub = null;
		try {
			if(StringUtils.isNotBlank(szcgUri)){
				stub = new ReceiveRecServiceServiceStub(szcgUri);
			}else{
				stub = new ReceiveRecServiceServiceStub();
			}
			ReceiveRecServiceServiceStub.Process p = new ReceiveRecServiceServiceStub.Process();
			p.setRequestXml(str);
			ReceiveRecServiceServiceStub.ProcessResponse r = stub.process(p);
			System.out.println("r.getProcessReturn()---"+r.getProcessReturn());
			return r.getProcessReturn().toString();

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private void doDocking(){
		String bizPlatform = "013";
		
		//对接组织
		String outOrgCode = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "XGY_OUT_ORGCODE", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + bizPlatform);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + bizPlatform);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", bizPlatform);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		
		if(dataExchangeStatuss!=null && dataExchangeStatuss.size() >0){
			Map<String,Object> event = dataExchangeStatuss.get(0);
			String eventName = "";
			if(event.get("EVENT_NAME")!=null)
				eventName = event.get("EVENT_NAME").toString();
			String eventId = "";
			if(event.get("EVENT_ID")!=null)
				eventId = event.get("EVENT_ID").toString();
			String eventAddr = "";
			if(event.get("OCCURRED")!=null)
				eventAddr = event.get("OCCURRED").toString();
			String eventContent = "";
			if(event.get("CONTENT_")!=null)
				eventContent = event.get("CONTENT_").toString(); 
			String eventDate = "";
			if(event.get("HAPPEN_TIME")!=null)
				eventDate = event.get("HAPPEN_TIME").toString();
			String eventType = "";
			if(event.get("TYPE_")!=null)
				eventType = event.get("TYPE_").toString().substring(0, 2);
			String affectRange = "";
			if(event.get("INFLUENCE_DEGREE")!=null)
				affectRange = event.get("INFLUENCE_DEGREE").toString();
			String emergencyLevel = "";
			if(event.get("URGENCY_DEGREE")!=null)
				emergencyLevel = event.get("URGENCY_DEGREE").toString();
			String eventSource = "";
			if(event.get("SOURCE")!=null)
				eventSource = event.get("SOURCE").toString();
			
			StringBuffer sb = new StringBuffer();
			sb.append("<data>");
			sb.append("<auth>");
			sb.append("<username></username>");
			sb.append("<password></password>");
			sb.append("</auth>");
			sb.append("<event>");
			sb.append("<eventId>"+eventId+"</eventId>");
			sb.append("<eventTitle>"+eventName+"</eventTitle>");//事件标题
			sb.append("<eventContent>"+eventContent+"</eventContent>");//事件详细内容
			sb.append("<eventDate>"+eventDate+"</eventDate>");//发生时间
			sb.append("<eventAddr>"+eventAddr+"</eventAddr>");//发生地点
			sb.append("<longitude></longitude>");//经度
			sb.append("<latitude></latitude>");//维度
			sb.append("<affectRange>"+affectRange+"</affectRange>");//影响范围
			sb.append("<emergencyLevel>"+emergencyLevel+"</emergencyLevel>");//紧急程度
			sb.append("<eventSource>"+eventSource+"</eventSource>");//事件来源
			sb.append("<eventType>"+eventType+"</eventType>");//事件类型
			sb.append("</event>");
			sb.append("</data>");
			org.apache.axis2.databinding.types.soapencoding.String param = new org.apache.axis2.databinding.types.soapencoding.String();
			param.setString(sb.toString());
			UserServiceServiceStub stub = null;
			try {
				stub = new UserServiceServiceStub();
				UserServiceServiceStub.PostEvent postEvent = new UserServiceServiceStub.PostEvent();
				postEvent.setIn0(param);
				PostEventResponse postEventResponse = stub.postEvent(postEvent);
				DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
				dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
				dataExchangeResult.setOppoSideBizCode("0");
				dataExchangeResult.setOwnSideBizCode(eventId);
				dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				dataExchangeResult.setDestPlatform(bizPlatform);
				dataExchangeResult.setXmlData(postEventResponse.getPostEventReturn().getString());
				Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void doDockingForAH(){
		ahWebServiceUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.AH_WS_URL, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		System.out.println(ahWebServiceUri);
		System.out.println("begin report AH");
		reportAh();
		System.out.println("end report AH");
		System.out.println("begin syncTask AH");
		syncTaskAh();
		System.out.println("end syncTask AH");
		System.out.println("begin commitTask AH");
		commitTaskAh();
		System.out.println("end commitTask AH");
	}
	
	private void doDockingForXB(){
		String xbWebServiceUriCfg = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.XB_WS_URL, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(xbWebServiceUriCfg)){
			xbWebServiceUri = xbWebServiceUriCfg;
		}
		System.out.println(xbWebServiceUri);
		System.out.println("begin report XB");
		reportXb();
		System.out.println("end report XB");
		System.out.println("begin syncTask XB");
		syncTaskXb();
		System.out.println("end syncTask XB");
		System.out.println("begin commitTask XB");
		commitTaskXb();
		System.out.println("end commitTask XB");
	}
	
	private void doDockingForNW(){
		nwWebServicTaskUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.NW_WS_URL_TASK, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		nwWebServiceUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.NW_WS_URL, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		System.out.println("begin report NW");
		report();
		System.out.println("end report NW");
		System.out.println("begin syncTask NW");
		syncTask();
		System.out.println("end syncTask NW");
		System.out.println("begin commitTask NW");
		commitTask();
		System.out.println("end commitTask NW");
	}
	
	private void doDockingForDS12345(){
		dsWebServiceUri = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DS_WS_URL, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		System.out.println("begin reportFeedback");
		reportFeedback();//接收反馈
		System.out.println("end reportFeedback");
		System.out.println("begin eventReport");
		eventReport();//上报
		System.out.println("end eventReport");
		System.out.println("begin rejected");
//		rejected();//回退
		System.out.println("end rejected");
		System.out.println("begin eventFeedback");
		eventFeedback();//结案
		System.out.println("end eventFeedback");
		System.out.println("begin taskFeedback");
//		taskFeedback();//结案时返回中间任务
		System.out.println("end taskFeedback");
	}
	
	private void commitTaskAh(){
		Map<String, Object> params = new HashMap<String, Object>();
		String[] eventStatuss = {"03","04"};
		params.put("eventStatuss", eventStatuss);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", BIZPLATFORM_AH);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		List<Map<String, Object>> closeEvents = dataExchangeStatusTwoWayService.findEventNewDocking(params);
		System.out.println(closeEvents.size());
		for(Map<String, Object> task : closeEvents){
			
			String eventId = task.get("EVENT_ID") == null ? "" : task.get("EVENT_ID").toString();
			String closeId = task.get("CLOSE_ID") == null ? "" : task.get("CLOSE_ID").toString();
			String createId = task.get("CREATOR_ID") == null ? "" : task.get("CREATOR_ID").toString();
			String createName = task.get("CREATOR_NAME") == null ? "" : task.get("CREATOR_NAME").toString();
			
		}
		
//		
//		Map<String, Object> closeEvent = closeEvents.get(0);
//		String remark = closeEvent.get("RESULT").toString();
//		String ownSideBizCode = closeEvent.get("EVENT_ID").toString();
//		String finTime = closeEvent.get("FIN_TIME").toString();
//		
//		XmlDataResult xmlDataResult = new XmlDataResult();
//		XmlEventResult xmlEventResult = new XmlEventResult();
//		xmlEventResult.setAdvice(remark);
//		xmlEventResult.setEventId(ownSideBizCode);
//		xmlEventResult.setCloseDate(finTime);
//		xmlEventResult.setCloserName("");
//		
//		xmlDataResult.setEvent(xmlEventResult);
//		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
//    	String result =jaxbBinder.toXml(xmlDataResult, "UTF-8");
//    	
//    	System.out.println("commitTaskXb->"+result);
    	
	}
	
	private void syncTaskAh(){
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("eventId", ownSideBizCode);
		params.put("destPlatform", "000");
		params.put("srcPlatform", BIZPLATFORM_AH);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("destPlatformTask", BIZPLATFORM_AH);
		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);

		String effectAh = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "EFFECT_AH_SYNCTASK", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(effectAh)&&effectAh.equals("true")){
		
		
		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		for(Map<String, Object> task : srcTasks){
			String taskName = task.get("TASK_NAME").toString();
			String eventId = null == task.get("EVENT_ID") ? "" : task.get("EVENT_ID").toString();
			String teskId = null == task.get("TASK_ID") ? "" : task.get("TASK_ID").toString();
			String transactorId = null == task.get("TRANSACTOR_ID") ? "" : task.get("TRANSACTOR_ID").toString();
			String transactorName = null == task.get("TRANSACTOR_NAME") ? "" : task.get("TRANSACTOR_NAME").toString();
			String orgName = task.get("ORG_NAME").toString();
			String orgId = task.get("ORG_ID").toString();
			String remarks = task.get("REMARKS").toString();
			if(taskName.equals("结案")){
				StringBuffer xml = new StringBuffer();
				xml.append("<data>");
				xml.append("<auth>");
				xml.append("<username>ahgrid</username>");
				xml.append("<password>345B0155CDD18BFC4E864E6B59ED22F6</password>");
				xml.append("</auth>");
				xml.append("<event>");
				xml.append("<eventId>").append(eventId).append("</eventId>");
				xml.append("<closerID>").append(transactorId).append("</closerID>");
				xml.append("<closerName>").append(transactorName).append("</closerName>");;
				xml.append("<closeOrgID>").append(orgId).append("</closeOrgID>");;
				xml.append("<closeOrgName>").append(orgName).append("</closeOrgName>");;
				xml.append("<advice>").append(remarks).append("</advice>");
				xml.append("</event>");
				xml.append("</data>");
				
				String xmlDate = xml.toString();
				System.out.println(xmlDate);
				WebServiceStub stub = null;
				try {
					stub = new WebServiceStub(ahWebServiceUri);
					
					stub = new WebServiceStub();
					Options options = stub._getServiceClient().getOptions();
					options.setTimeOutInMilliSeconds(1000*60*30);
					options.setProperty(HTTPConstants.CHUNKED, false);
					stub._getServiceClient().setOptions(options);
					
					WebServiceStub.Flow_Close flowClose = new WebServiceStub.Flow_Close();
					flowClose.setXmlData(xmlDate);
					String result = stub.flow_Close(flowClose).getFlow_CloseResult();
					System.out.println(result);
					
					JaxbBinder resultJaxbBinder = new JaxbBinder(AHResultData.class);
					AHResultData resultDate = resultJaxbBinder.fromXml(result);
					if(resultDate.getCode().equals("1")){
						DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
						dataExchangeStatus.setOppoSideBizCode(resultDate.getMessage());
						dataExchangeStatus.setOppoSideBizType("3");
						dataExchangeStatus.setOwnSideBizCode(teskId);
						dataExchangeStatus.setOwnSideBizType("3");
						dataExchangeStatus.setExchangeFlag("1");
						dataExchangeStatus.setDestPlatform(BIZPLATFORM_AH);
						dataExchangeStatus.setSrcPlatform("000");
						dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
					}
				} catch (AxisFault e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} 
			}else{
			
			
//			System.out.println(task.get("EVENT_ID").toString());
			
			StringBuffer xml = new StringBuffer();
			xml.append("<data>");
			xml.append("<auth>");
			xml.append("<username>ahgrid</username>");
			xml.append("<password>345B0155CDD18BFC4E864E6B59ED22F6</password>");
			xml.append("</auth>");
			xml.append("<task>");
			xml.append("<eventId>").append(StringUtils.isBlank(eventId) ? teskId : eventId).append("</eventId>");
			xml.append("<transactorId>").append(transactorId).append("</transactorId>");
			xml.append("<transactorName>").append(transactorName).append("</transactorName>");
			xml.append("<transactOrgID>").append(orgId).append("</transactOrgID>");
			xml.append("<transactOrgName>").append(orgName).append("</transactOrgName>");
			xml.append("<results>").append(remarks).append("</results>");
			xml.append("</task>");
			xml.append("</data>");

			String xmlDate = xml.toString();
			System.out.println(xmlDate);
			WebServiceStub stub = null;
			try {
				stub = new WebServiceStub(ahWebServiceUri);
				
				stub = new WebServiceStub();
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*30);
				options.setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				WebServiceStub.Flow_Next flowNext = new WebServiceStub.Flow_Next();
				flowNext.setXmlData(xmlDate);
				String result = stub.flow_Next(flowNext).getFlow_NextResult();
				System.out.println(result);

//				String xmlResult = stub.request(request).get_return().replace("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>", "");
				JaxbBinder resultJaxbBinder = new JaxbBinder(AHResultData.class);
				AHResultData resultDate = resultJaxbBinder.fromXml(result);
//				if(resultDate.getCode().equals("1")){

					DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
					dataExchangeStatus.setOppoSideBizCode(resultDate.getMessage());
					dataExchangeStatus.setOppoSideBizType("3");
					dataExchangeStatus.setOwnSideBizCode(teskId);
					dataExchangeStatus.setOwnSideBizType("3");
					dataExchangeStatus.setExchangeFlag("1");
					dataExchangeStatus.setDestPlatform(BIZPLATFORM_AH);
					dataExchangeStatus.setSrcPlatform("000");

					dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
//				}
				
				
			} catch (AxisFault e) {
				e.printStackTrace();
				return;
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			} 
			
		}
		}

		}	
	}

	private void reportAh(){
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_AH);
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_AH);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_AH);
//		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_AH);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", BIZPLATFORM_AH);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		
		String effectAh = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "EFFECT_AH_REPORT", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(effectAh)&&effectAh.equals("true")){
			
		
		
//		System.out.println(dataExchangeStatuss.size());
		for(Map<String,Object> dataExchangeStatus : dataExchangeStatuss){
			String ownSideBizCode = dataExchangeStatus.get("EVENT_ID").toString();
			String eventName = null == dataExchangeStatus.get("EVENT_NAME") ? "":dataExchangeStatus.get("EVENT_NAME").toString();
			String eventType = dataExchangeStatus.get("TYPE_").toString();
			String content = null == dataExchangeStatus.get("CONTENT_") ? "" : dataExchangeStatus.get("CONTENT_").toString();
			String occurred = null == dataExchangeStatus.get("OCCURRED") ? "" : dataExchangeStatus.get("OCCURRED").toString();
			String happenTime = null == dataExchangeStatus.get("HAPPEN_TIME") ? "" : dataExchangeStatus.get("HAPPEN_TIME").toString();
			String gridCode = dataExchangeStatus.get("GRID_CODE").toString();
			String source = null == dataExchangeStatus.get("SOURCE") ? "" : dataExchangeStatus.get("SOURCE").toString();
			String involvedNum = dataExchangeStatus.get("INVOLVED_NUM").toString();
			String influenceDegree = null == dataExchangeStatus.get("INFLUENCE_DEGREE") ? "" : dataExchangeStatus.get("INFLUENCE_DEGREE").toString();
			String urgencyDegree = null == dataExchangeStatus.get("URGENCY_DEGREE") ? "" : dataExchangeStatus.get("URGENCY_DEGREE").toString();
//			String contactUser = dataExchangeStatus.get("CONTACT_USER").toString();
			String tel = null == dataExchangeStatus.get("TEL")?"":dataExchangeStatus.get("TEL").toString();
			String status = dataExchangeStatus.get("STATUS").toString();
			String creatorId = dataExchangeStatus.get("CREATOR_ID").toString();
			String creatorName = dataExchangeStatus.get("CREATOR_NAME").toString();
			String createTime = null == dataExchangeStatus.get("CREATE_TIME") ? "" : dataExchangeStatus.get("CREATE_TIME").toString();
			String handleDate = dataExchangeStatus.get("HANDLE_DATE").toString();
			
			if(null != dataExchangeStatus.get("FIN_TIME")){
				String finTime = dataExchangeStatus.get("FIN_TIME").toString();
			}
			String updateTime = dataExchangeStatus.get("UPDATE_TIME").toString();
			String collectWay = dataExchangeStatus.get("COLLECT_WAY").toString();
			String remark = null == dataExchangeStatus.get("REMARK") ? "" :dataExchangeStatus.get("REMARK").toString();
//			if(null != dataExchangeStatus.get("REMARK")){
//				String remark = dataExchangeStatus.get("REMARK").toString();
//			}
			if(null != dataExchangeStatus.get("RESULT")){
				String result = dataExchangeStatus.get("RESULT").toString();
			}
			
			System.out.println(ownSideBizCode);
			
			EventDisposal event = eventDisposalService.findEventById(Long.valueOf(ownSideBizCode), null);
//			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(event.getGridCode());
			MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(event.getGridId(), false);
//			MixedGridInfo gridInfo = mixedGridInfoService.getGridLevelByOrgCodeOrGridId(null, event.getGridId());
			
			StringBuffer xml = new StringBuffer();
			xml.append("<data>");
			xml.append("<auth>");
			xml.append("<username>ahgrid</username>");
			xml.append("<password>345B0155CDD18BFC4E864E6B59ED22F6</password>");
			xml.append("</auth>");
			xml.append("<flow>");
			xml.append("<gridCode>").append(gridInfo.getInfoOrgCode()).append("</gridCode>");
			xml.append("<eventName>").append(eventName).append("</eventName>");
			xml.append("<happenTimeStr>").append(happenTime).append("</happenTimeStr>");
			xml.append("<occurred>").append(occurred).append("</occurred>");
			xml.append("<content>").append(content).append("</content>");
			xml.append("<eventId>").append(ownSideBizCode).append("</eventId>");
			xml.append("<bizPlatform>001</bizPlatform>");//业务平台
			xml.append("<purCode>09</purCode>");//受理类型
			xml.append("<concede>26</concede>");//事件类型
			xml.append("<contactTel>").append(tel).append("</contactTel>");//电话
			xml.append("<urgency>").append(urgencyDegree).append("</urgency>");
			xml.append("<influence>").append(influenceDegree).append("</influence>");
			xml.append("<source>").append(source).append("</source>");
			xml.append("<registerTimerStr>").append(createTime).append("</registerTimerStr>");
			xml.append("<advice>").append(remark).append("</advice>");
			xml.append("</flow>");
			xml.append("</data>");
			String xmlDate = xml.toString();
			System.out.println(xmlDate);
			
			WebServiceStub stub = null;
			try {
				stub = new WebServiceStub(ahWebServiceUri);
				
				stub = new WebServiceStub();
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*3);
				options.setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				WebServiceStub.Flow_Create flowCreate = new WebServiceStub.Flow_Create();
				flowCreate.setXmlData(xmlDate);
				String result = stub.flow_Create(flowCreate).getFlow_CreateResult();
				System.out.println(result);
				
				JaxbBinder resultJaxbBinder = new JaxbBinder(AHResultData.class);
				AHResultData resultDate = resultJaxbBinder.fromXml(result);
//				if(resultDate.getCode().equals("1")){

					DataExchangeStatus dataExchange = new DataExchangeStatus();
					dataExchange.setOppoSideBizCode(resultDate.getMessage());
					dataExchange.setOppoSideBizType("2");
					dataExchange.setOwnSideBizCode(ownSideBizCode);
					dataExchange.setOwnSideBizType("2");
					dataExchange.setExchangeFlag("1");
					dataExchange.setDestPlatform(BIZPLATFORM_AH);
					dataExchange.setSrcPlatform("000");

					dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
//				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
			
			break;
		}
		}
	}

	private String format(String source){
		if(source.equals("07")){
			source = "03";
		}else if(source.equals("03")){
			source = "05";
		}else if(source.equals("04")){
			source = "06";
		}else if(source.equals("05")){
			source = "07";
		}else if(source.equals("06")){
			source = "08";
		}
		return source;
	}
	
	private String formatEventType(String type, String bizPlatform){
		if(bizPlatform.equals("008")){
			String eventType = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dictPcode", ConstantValue.EVENT_DOCKING_TYPE);
			List<BaseDataDict> baseDataDicts = baseDictionaryService.getDataDictListOfSinglestage(params);
//			List<BaseDataDict> baseDataDicts = baseDictionaryService.getDataDictListByDictCode(ConstantValue.EVENT_DOCKING_TYPE);
			System.out.println("type:"+type);
			for(BaseDataDict baseDataDict : baseDataDicts){
				if(baseDataDict.getDictGeneralCode().equals(type)){
					eventType = baseDataDict.getDictName();
					break;
				}
			}
			System.out.println("eventType:"+eventType);
			return StringUtils.isNotBlank(eventType) ? eventType : type;
		}
		return type;
	}
	
	private void reportXb(){
		Map<String, Object> params = new HashMap<String, Object>();
		String activityNameTrigger = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, 
				ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_XB, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
//		String userIdTrigger = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, 
//				ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_XB, IFunConfigurationService.CFG_TYPE_FACT_VAL);
//		String groupIdTrigger = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, 
//				ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_XB, IFunConfigurationService.CFG_TYPE_FACT_VAL);
		String orgIdTrigger = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, 
				ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_XB, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_XB);
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_XB);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_XB);
		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_XB);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM_XB);

//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET_NW);
//		params.put("trigger", ConstantValue.DEFAULT_REPORT_ACTIVITY_NAME_TRIGGER+ "_007");
//		params.put("EXCHANGE_FLAG", IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);

//		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusService.findEventNewRejected(params);
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		
//		System.out.println(dataExchangeStatuss.size());
		for(Map<String,Object> dataExchangeStatus : dataExchangeStatuss){
			String ownSideBizCode = dataExchangeStatus.get("EVENT_ID").toString();
			String eventType = dataExchangeStatus.get("TYPE_").toString();
			eventType = formatEventType(eventType,BIZPLATFORM_XB);
			String content = dataExchangeStatus.get("CONTENT_").toString();
			String occurred = dataExchangeStatus.get("OCCURRED").toString();
			String happenTime = dataExchangeStatus.get("HAPPEN_TIME").toString();
			String gridCode = dataExchangeStatus.get("GRID_CODE").toString();
			String source = dataExchangeStatus.get("SOURCE").toString();
			String involvedNum = dataExchangeStatus.get("INVOLVED_NUM").toString();
			String influenceDegree = dataExchangeStatus.get("INFLUENCE_DEGREE").toString();
			String urgencyDegree = dataExchangeStatus.get("URGENCY_DEGREE").toString();
			String contactUser = dataExchangeStatus.get("CONTACT_USER").toString();
			String tel = dataExchangeStatus.get("TEL").toString();
			String status = dataExchangeStatus.get("STATUS").toString();
			String creatorId = dataExchangeStatus.get("CREATOR_ID").toString();
			String creatorName = dataExchangeStatus.get("CREATOR_NAME").toString(); 
			String createTime = dataExchangeStatus.get("CREATE_TIME").toString();
			if(null != dataExchangeStatus.get("FIN_TIME")){
				String finTime = dataExchangeStatus.get("FIN_TIME").toString();
			}
			String updateTime = dataExchangeStatus.get("UPDATE_TIME").toString();
			String collectWay = dataExchangeStatus.get("COLLECT_WAY").toString();
			String handleDate = dataExchangeStatus.get("HANDLE_DATE").toString();
			if(null != dataExchangeStatus.get("REMARK")){
				String remark = dataExchangeStatus.get("REMARK").toString();
			}
			if(null != dataExchangeStatus.get("RESULT")){
				String result = dataExchangeStatus.get("RESULT").toString();
			}
			
			System.out.println(ownSideBizCode);
			
			EventDisposal event = eventDisposalService.findEventById(Long.valueOf(ownSideBizCode), null);
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(event.getGridCode());
			
//			String oppoSideBizCode = DigestUtils.md5Hex(ownSideBizCode);
			
			EventDockingServiceImplStub stub = null;
//			WebServicePublishImplServiceStub stub = null;
			try {
				stub = new EventDockingServiceImplStub(xbWebServiceUri);
				
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*30);
				options.setProperty(HTTPConstants.CHUNKED, false);
//				serviceClient.getOptions().setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				XmlDataResult xmlDataResult = new XmlDataResult();
				XmlEventResult xmlEventResult = new XmlEventResult();
				xmlEventResult.setEventType(eventType);
				xmlEventResult.setContent(content);
				xmlEventResult.setOccurred(occurred);
				xmlEventResult.setInfluence(influenceDegree);
				xmlEventResult.setUrgency(urgencyDegree);
				xmlEventResult.setContactTel(tel);
				System.out.println("source->"+source);
				xmlEventResult.setSource(format(source));
				System.out.println("source->"+format(source));
				xmlEventResult.setCreatorName(creatorName);
				xmlEventResult.setGridCode(gridCode);
				xmlEventResult.setStatus(status);
				xmlEventResult.setHappenTimeStr(happenTime);
				xmlEventResult.setRegisterTimeStr(createTime);
				xmlEventResult.setOppoSideBusiCode(ownSideBizCode);
				//--
				xmlEventResult.setInfluence(influenceDegree);
//				xmlEventResult.
				
				List<XmlTaskResult> xmlTaskResults = new ArrayList<XmlTaskResult>();
				Map<String, Object> tasksParams = new HashMap<String, Object>();
				tasksParams.put("eventId", ownSideBizCode);
				tasksParams.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				tasksParams.put("destPlatform", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
				tasksParams.put("ownSideBizType", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
				
				List<Map<String, Object>> eventTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(tasksParams);
				for(Map<String, Object> eventTask : eventTasks){
					XmlTaskResult xmlTaskResult = new XmlTaskResult();
					xmlTaskResult.setTaskId(eventTask.get("TASK_ID").toString());
					xmlTaskResult.setResults(eventTask.get("REMARKS").toString());
					xmlTaskResult.setTransactorName(eventTask.get("TRANSACTOR_NAME").toString());
					xmlTaskResult.setTransactOrgName(eventTask.get("ORG_NAME").toString());
					xmlTaskResult.setEndTime(eventTask.get("END_TIME").toString());
					xmlTaskResult.setTaskName(eventTask.get("TASK_NAME").toString());
//					eventTask.get("");
					xmlTaskResults.add(xmlTaskResult);
				}

				XmlDataAuth xmlDataAuth = new XmlDataAuth();
				xmlDataResult.setAuth(xmlDataAuth);
//				xmlEventResult.setTasks(xmlTaskResults);
				xmlDataResult.setEvent(xmlEventResult);//设置事件信息
//				xmlEventResult.setTasks(xmlTaskResults);
				
				JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		    	String dataResult =jaxbBinder.toXml(xmlDataResult, "UTF-8");
		    	System.out.println("reportXb->");
		    	System.out.println(dataResult);
//				
		    	EventDockingServiceImplStub.ReportEvent reportEvent = new EventDockingServiceImplStub.ReportEvent();
		    	reportEvent.setXmlData(dataResult);
		    	
		    	String responseResult = stub.reportEvent(reportEvent).get_return();
				
		    	jaxbBinder = new JaxbBinder(XmlDataResponse.class);
		    	XmlDataResponse xmlDataResponse = jaxbBinder.fromXml(responseResult);
		    	
		    	System.out.println("xmlDataResponse->" + xmlDataResponse.getCode() + "->" +xmlDataResponse.getMessage());
		    	//====

				DataExchangeStatus dataExchange = new DataExchangeStatus();
				dataExchange.setOppoSideBizCode(xmlDataResponse.getMessage());
				dataExchange.setOppoSideBizType("2");
				dataExchange.setOwnSideBizCode(ownSideBizCode);
				dataExchange.setOwnSideBizType("2");
				dataExchange.setExchangeFlag("1");
				dataExchange.setDestPlatform(BIZPLATFORM_XB);
				dataExchange.setSrcPlatform("000");

				dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
		    	
			} catch (AxisFault e) {
				e.printStackTrace();
				return;
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			} 
			
			break;
		}
	}
	
	private void commitTaskXb(){
		Map<String, Object> params = new HashMap<String, Object>();
		String[] eventStatuss = {"03","04"};
		params.put("eventStatuss", eventStatuss);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("exchangeFlag", IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
		List<Map<String, Object>> closeEvents = dataExchangeStatusTwoWayService.findEventNewDocking(params);
		System.out.println(closeEvents.size());
		for(Map<String, Object> closeEvent : closeEvents){
			String eventId = closeEvent.get("EVENT_ID").toString();
			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
			dataExchangeStatus.setOppoSideBizType("2");
			dataExchangeStatus.setOwnSideBizType("2");
			dataExchangeStatus.setDestPlatform("000");
			dataExchangeStatus.setOwnSideBizCode(eventId);
			dataExchangeStatus.setExchangeFlag("0");
			List<DataExchangeStatus> listDataExchangeStatus = dataExchangeStatusService.findDataExchangeList(dataExchangeStatus);
			if(listDataExchangeStatus.size() < 1){
				System.out.println(eventId + "对应事件eventId中间表不存在");
				continue;
			}
			DataExchangeStatus resultDataExchangeStatus = listDataExchangeStatus.get(0);
			String OppoEventId = resultDataExchangeStatus.getOppoSideBizCode();
			Long interId = resultDataExchangeStatus.getInterId();
			
			EventDockingServiceImplStub stub = null;
			try {
				stub = new EventDockingServiceImplStub(xbWebServiceUri);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Options options = stub._getServiceClient().getOptions();
			options.setTimeOutInMilliSeconds(1000*60*30);
			options.setProperty(HTTPConstants.CHUNKED, false);
			stub._getServiceClient().setOptions(options);

			XmlEventResult event = new XmlEventResult();
			event.setEventId(OppoEventId);
//			event.setCloseDate(closeDate)
			if(null != closeEvent.get("RESULT")){
				event.setAdvice(closeEvent.get("RESULT").toString());
			}
			event.setCloserName(closeEvent.get("CLOSER_NAME").toString());
			
			XmlDataResult xmlDataResult = new XmlDataResult();
			xmlDataResult.setEvent(event);
			
			JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
	    	String result =jaxbBinder.toXml(xmlDataResult, "UTF-8");
	    	System.out.println("commitTaskXb->"+result);
	    	EventDockingServiceImplStub.CloseEvent closeEventStub = new EventDockingServiceImplStub.CloseEvent();
	    	closeEventStub.setXmlData(result);
	    	try {
				String responseResult = stub.closeEvent(closeEventStub).get_return();
				
		    	jaxbBinder = new JaxbBinder(XmlDataResponse.class);
		    	XmlDataResponse xmlDataResponse = jaxbBinder.fromXml(responseResult);
		    	
		    	System.out.println("xmlDataResponse->" + xmlDataResponse.getCode() + "->" +xmlDataResponse.getMessage());
		    	//====
		    	String oppoSideBizCode = xmlDataResponse.getMessage();
		    	
//		    	DataExchangeStatus dataExchange = new DataExchangeStatus();
//				dataExchange.setOppoSideBizCode(oppoSideBizCode);
//				dataExchange.setOppoSideBizType("2");
//				dataExchange.setOwnSideBizCode(eventId);
//				dataExchange.setOwnSideBizType("2");
//				dataExchange.setExchangeFlag("1");
//				dataExchange.setDestPlatform(BIZPLATFORM_XB);
//				dataExchange.setSrcPlatform("000");
//				dataExchangeStatusService.saveDataExchangeStatus(dataExchange);

				DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
				dataExchangeResult.setInterId(interId);
				dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
				boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
//		
//		Map<String, Object> closeEvent = closeEvents.get(0);
//		String remark = closeEvent.get("RESULT").toString();
//		String ownSideBizCode = closeEvent.get("EVENT_ID").toString();
//		String finTime = closeEvent.get("FIN_TIME").toString();
//		
//		XmlDataResult xmlDataResult = new XmlDataResult();
//		XmlEventResult xmlEventResult = new XmlEventResult();
//		xmlEventResult.setAdvice(remark);
//		xmlEventResult.setEventId(ownSideBizCode);
//		xmlEventResult.setCloseDate(finTime);
//		xmlEventResult.setCloserName("");
//		
//		xmlDataResult.setEvent(xmlEventResult);
//		JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
//    	String result =jaxbBinder.toXml(xmlDataResult, "UTF-8");
//    	
//    	System.out.println("commitTaskXb->"+result);
    	
	}
	
	private void syncTaskXb(){

		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("eventId", ownSideBizCode);
//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("destPlatformTask", IDataExchangeStatusService.PLATFORM_XIBIN_ICT);
		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);

		List<XmlTaskResult> tasks = new ArrayList<XmlTaskResult>();
		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		for(Map<String, Object> task : srcTasks){
			XmlTaskResult xmlTaskResult = new XmlTaskResult();
			String taskId = task.get("TASK_ID").toString();
			String remarks = task.get("REMARKS").toString();
			String transactorName = task.get("TRANSACTOR_NAME").toString();
			String startTime = task.get("START_TIME").toString();
			String endTime = task.get("END_TIME").toString();
			String orgName = task.get("ORG_NAME").toString();
			if(task.get("ORG_ID")==null ||task.get("ORG_CODE")==null){
				continue;
			}
			String orgId = task.get("ORG_ID").toString();
			String orgCode = task.get("ORG_CODE").toString();
			String eventId = task.get("EVENT_ID").toString();
//			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
//			List<DataExchangeStatus> listDataExchangeStatus = dataExchangeStatusService.findDataExchangeList(dataExchangeStatus);
			xmlTaskResult.setEventId(eventId);
			xmlTaskResult.setTransactorName(transactorName);
			xmlTaskResult.setTransactOrgName(orgName);
			xmlTaskResult.setNextOrgCode(orgCode);
			xmlTaskResult.setTaskId(taskId);
			xmlTaskResult.setCreateTime(startTime);
			xmlTaskResult.setEndTime(endTime);
			xmlTaskResult.setResults(remarks);
//			xmlTaskResult.setOppoSideBizCode(oppoSideBizCode)
			tasks.add(xmlTaskResult);
			EventDockingServiceImplStub stub = null;
			String OppoSideBizCode = "0";
			try {
				stub = new EventDockingServiceImplStub(xbWebServiceUri);
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*30);
				options.setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				XmlDataResult xmlDataResult = new XmlDataResult(); 
				xmlDataResult.setTasks(tasks);
				
				JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
		    	String result =jaxbBinder.toXml(xmlDataResult, "UTF-8");
		    	
		    	System.out.println("syncTaskXb->"+result);
		    	
		    	EventDockingServiceImplStub.SyncEvent syncEvent = new EventDockingServiceImplStub.SyncEvent();
		    	syncEvent.setXmlData(result);
		    	String responseResult = stub.syncEvent(syncEvent).get_return();
				
		    	jaxbBinder = new JaxbBinder(XmlDataResponse.class);
		    	XmlDataResponse xmlDataResponse = jaxbBinder.fromXml(responseResult);
		    	
		    	System.out.println("xmlDataResponse->" + xmlDataResponse.getCode() + "->" +xmlDataResponse.getMessage());
		    	//====
		    	OppoSideBizCode = xmlDataResponse.getMessage();

			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataExchangeStatus dataExchange = new DataExchangeStatus();
			dataExchange.setOppoSideBizCode(OppoSideBizCode);
			dataExchange.setOppoSideBizType("3");
			dataExchange.setOwnSideBizCode(taskId);
			dataExchange.setOwnSideBizType("3");
			dataExchange.setExchangeFlag("1");
			dataExchange.setDestPlatform(BIZPLATFORM_XB);
			dataExchange.setSrcPlatform("000");
			dataExchangeStatusService.saveDataExchangeStatus(dataExchange);
			break;
		}
		
			
	}
	
	private void report(){
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET_NW);
		params.put("trigger", ConstantValue.DEFAULT_REPORT_ACTIVITY_NAME_TRIGGER+ "_007");
		params.put("EXCHANGE_FLAG", IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);

		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusService.findEventNewRejected(params);
//		System.out.println(dataExchangeStatuss.size());
		for(Map<String,Object> dataExchangeStatus : dataExchangeStatuss){
			String ownSideBizCode = dataExchangeStatus.get("EVENT_ID").toString();
			
			EventDisposal event = eventDisposalService.findEventById(Long.valueOf(ownSideBizCode), null);
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(event.getGridCode());
			
			String oppoSideBizCode = DigestUtils.md5Hex(ownSideBizCode);
			
			WebServicePublishImplServiceStub stub = null;
			try {
				stub = new WebServicePublishImplServiceStub(nwWebServiceUri);
				
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*30);
				options.setProperty(HTTPConstants.CHUNKED, false);
//				serviceClient.getOptions().setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				SJXXData sjxxDate = new SJXXData();
				sjxxDate.setSJXX_COPY_ID(oppoSideBizCode);//
				sjxxDate.setWGBH(event.getGridCode());//网格
				sjxxDate.setWGMC(gridInfo.getGridName());//网格名称
				sjxxDate.setSJLX("31");//事件类型 字典 其他问题：31
				sjxxDate.setSJWZJD("0");//经度
				sjxxDate.setSJWZWD("0");//纬度
//				sjxxDate.setLYXX("测试楼宇");//楼宇
				sjxxDate.setSJMS(event.getContent());
				sjxxDate.setFSSJ(event.getHappenTimeStr());//发生时间
				sjxxDate.setFKRY(event.getContactUser());//反馈人员
				sjxxDate.setSJRS("1");//涉及人数
				sjxxDate.setSJXZ(event.getOccurred());//事件详址
				sjxxDate.setYXFW(event.getInfluenceDegreeName());//影响范围
//				sjxxDate.setXXLY("3");//信息来源 字典（目击：3；举报：4；热线：5）
				sjxxDate.setJJCD("4");//紧急程度 字典（一级：4；二级：5；三级：6；四级：7）
//				sjxxDate.setSXZT("正常");//时限状态
				sjxxDate.setYJZT("5");//预警状态，必须提供 字典（一级：5；二级：6；三级：7）
				sjxxDate.setSJZT("1");//事件状态，必须提供字典（未上报：1；未结案：2； 已办结：3；已结案：4；已评价：6；已分流：7）
				sjxxDate.setSJMC(event.getEventName());//事件名称
				sjxxDate.setDSFSJBS(String.valueOf(event.getEventId()));
				
				List<SJXXData> list = new ArrayList<SJXXData>();
				list.add(sjxxDate);
				
				SJXXDatas sjxxDates = new SJXXDatas();
				sjxxDates.setSjxxDates(list);
				sjxxDates.setCount("1");
				sjxxDates.setTableName("SJXX");
				
				JaxbBinder jaxbBinder = new JaxbBinder(SJXXDatas.class);
		    	String result =jaxbBinder.toXml(sjxxDates, "UTF-8");
		    	System.out.println("result->");
		    	System.out.println(result);
				
				WebServicePublishImplServiceStub.Request request = new WebServicePublishImplServiceStub.Request();
				request.setArg0(result);
				WebServicePublishImplServiceStub.RequestResponse requestResponse = stub.request(request);
				
				System.out.println("return:"+requestResponse.get_return());
				String xmlResult = requestResponse.get_return().replace("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>", "");
				JaxbBinder sjlcResultJaxbBinder = new JaxbBinder(SJLCResult.class);
				SJLCResult sjlcResult = sjlcResultJaxbBinder.fromXml(xmlResult);
				
//				if(StringUtils.isNotBlank(sjlcResult.getResult()) && sjlcResult.getResult().equals("1")){
//					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//					dataExchangeResult.setInterId(dataExchangeStatus.getInterId());
//					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//					dataExchangeResult.setOppoSideBizCode(oppoSideBizCode);
//					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
//					if(!record)
//						System.out.println("更新中间表失败！"+record);
//				}

				if(StringUtils.isNotBlank(sjlcResult.getResult()) && sjlcResult.getResult().equals("1")){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//					dataExchangeResult.setInterId(dataExchangeStatus.getInterId());
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(oppoSideBizCode);
					dataExchangeResult.setOwnSideBizCode(dataExchangeStatus.get("EVENT_ID").toString());
					dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
					dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET_NW);
					Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
					if(!(record>0))
						System.out.println("更新中间表失败！"+record);
				}
				
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private void syncTask(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET_NW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM_NW);
		params.put("OWN_SIDE_BIZ_TYPE", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("OPPO_SIDE_BIZ_TYPE", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		params.put("eventStatus", "'00','01','02','03','04'");
		List<Map<String, Object>> result = dataExchangeStatusService.findEventNewToFeedback(params);
		for(Map<String, Object> resultMap : result){
			String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
			String ownSideBizCode = resultMap.get("OWN_SIDE_BIZ_CODE").toString();
			String eventStatus = resultMap.get("STATUS").toString();
			//初始化任务
			boolean initTaskFeedback = dataExchangeStatusService.isEventTaskFeedback(Long.valueOf(ownSideBizCode), BIZPLATFORM_NW, oppoSideBizCode);
			if(!initTaskFeedback) return;
		}
	}
	
	private Boolean commitTeskEvent(String ownSideBizCode){
		EventDisposal event = eventDisposalService.findEventById(Long.valueOf(ownSideBizCode), null);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(event.getGridId(), false);
		
		String oppoSideBizCode = DigestUtils.md5Hex(ownSideBizCode);
		
		WebServicePublishImplServiceStub stub = null;
		try {
			stub = new WebServicePublishImplServiceStub(nwWebServiceUri);
			
			Options options = stub._getServiceClient().getOptions();
			options.setTimeOutInMilliSeconds(1000*60*30);
			options.setProperty(HTTPConstants.CHUNKED, false);
//			serviceClient.getOptions().setProperty(HTTPConstants.CHUNKED, false);
			stub._getServiceClient().setOptions(options);
			
			SJXXData sjxxDate = new SJXXData();
			sjxxDate.setSJXX_COPY_ID(oppoSideBizCode);//
			sjxxDate.setWGBH(gridInfo.getInfoOrgCode());//网格
			sjxxDate.setWGMC(gridInfo.getGridName());//网格名称
			sjxxDate.setSJLX("31");//事件类型 字典 其他问题：31
			sjxxDate.setSJWZJD("0");//经度
			sjxxDate.setSJWZWD("0");//纬度
//			sjxxDate.setLYXX("测试楼宇");//楼宇
			sjxxDate.setSJMS(event.getContent());
			sjxxDate.setFSSJ(event.getHappenTimeStr());//发生时间
			sjxxDate.setFKRY(event.getContactUser());//反馈人员
			sjxxDate.setSJRS("1");//涉及人数
			sjxxDate.setSJXZ(event.getOccurred());//事件详址
			sjxxDate.setYXFW(event.getInfluenceDegreeName());//影响范围
//			sjxxDate.setXXLY("3");//信息来源 字典（目击：3；举报：4；热线：5）
			sjxxDate.setJJCD("4");//紧急程度 字典（一级：4；二级：5；三级：6；四级：7）
//			sjxxDate.setSXZT("正常");//时限状态
			sjxxDate.setYJZT("5");//预警状态，必须提供 字典（一级：5；二级：6；三级：7）
			sjxxDate.setSJZT("3");//事件状态，必须提供字典（未上报：1；未结案：2； 已办结：3；已结案：4；已评价：6；已分流：7）
			sjxxDate.setSJMC(event.getEventName());//事件名称
			sjxxDate.setDSFSJBS(String.valueOf(event.getEventId()));
			
			List<SJXXData> list = new ArrayList<SJXXData>();
			list.add(sjxxDate);
			
			SJXXDatas sjxxDates = new SJXXDatas();
			sjxxDates.setSjxxDates(list);
			sjxxDates.setCount("1");
			sjxxDates.setTableName("SJXX");
			
			JaxbBinder jaxbBinder = new JaxbBinder(SJXXDatas.class);
	    	String result =jaxbBinder.toXml(sjxxDates, "UTF-8");
	    	System.out.println("result->");
	    	System.out.println(result);
			
			WebServicePublishImplServiceStub.Request request = new WebServicePublishImplServiceStub.Request();
			request.setArg0(result);
			WebServicePublishImplServiceStub.RequestResponse requestResponse = stub.request(request);
			
			System.out.println("return:"+stub.request(request).get_return());
			String xmlResult = stub.request(request).get_return().replace("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>", "");
			JaxbBinder sjlcResultJaxbBinder = new JaxbBinder(SJLCResult.class);
			SJLCResult sjlcResult = sjlcResultJaxbBinder.fromXml(xmlResult);
			if("1".equals(sjlcResult.getResult())){
				return true;
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	private void commitTask(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET_NW);
		params.put("bizPlatform", BIZPLATFORM_NW);
		List<Map<String, Object>> results = dataExchangeStatusService.findTaskNewToFeedback(params);
		int i = 1;
		for(Map<String, Object> resultMap : results){
			resultMap.get("REMARKS");
			Long interId = Long.valueOf(resultMap.get("INTER_ID").toString());
			String ownSideBizCode = resultMap.get("OWN_SIDE_BIZ_CODE").toString();
			String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
			String eventId = resultMap.get("EVENT_ID").toString();
//			String oppoSideBizCode = DigestUtils.md5Hex(ownSideBizCode);
			String orgName = resultMap.get("ORG_NAME").toString();
			//事件更新
			if(!commitTeskEvent(eventId)) return;
			
//			Date date = (Date)resultMap.get("START_TIME");
			String dateStr = DateUtils.formatDate(new Date(), DateUtils.PATTERN_24TIME);
			System.out.println("dateStr->"+dateStr);
			WebServicePublishImplServiceStub stub = null;
			
			try {
				stub = new WebServicePublishImplServiceStub(nwWebServicTaskUri);
				
				Options options = stub._getServiceClient().getOptions();
				options.setTimeOutInMilliSeconds(1000*60*30);
				options.setProperty(HTTPConstants.CHUNKED, false);
//				serviceClient.getOptions().setProperty(HTTPConstants.CHUNKED, false);
				stub._getServiceClient().setOptions(options);
				
				
				SJLCData sjlcDate = new SJLCData();
				sjlcDate.setSJLC_ID(DigestUtils.md5Hex(ownSideBizCode));
				sjlcDate.setJSSJ(dateStr);//接收时间，必须提供
				sjlcDate.setBLSJ(dateStr);//办理时间，必须提供
				sjlcDate.setBLRY(resultMap.get("TRANSACTOR_NAME").toString().substring(0, 9));//办理人员，必须提供
				sjlcDate.setBLYJ(resultMap.get("REMARKS").toString());//办理意见，必须提供
				sjlcDate.setBLBM(orgName);//办理部门，必须提供
				sjlcDate.setSJID(oppoSideBizCode);
				sjlcDate.setWGMC(orgName);//网格名称
				sjlcDate.setLZBJ(i+"");//流转标记，按流程办理时间顺序，存入“1”、“2”、“3”等值。
//				sjlcDate.setBLLB("");//办理类别
				
				
				List<SJLCData> list = new ArrayList<SJLCData>();
				list.add(sjlcDate);
				
				SJLCDatas sjlcDates = new SJLCDatas();
				sjlcDates.setSjlcDates(list);
				sjlcDates.setCount("1");
				sjlcDates.setTableName("SJXX");
				
				JaxbBinder jaxbBinder = new JaxbBinder(SJLCDatas.class);
		    	String result =jaxbBinder.toXml(sjlcDates, "UTF-8");
		    	System.out.println("result->");
		    	System.out.println(result);
				
				WebServicePublishImplServiceStub.Request request = new WebServicePublishImplServiceStub.Request();
				request.setArg0(result);
				
				WebServicePublishImplServiceStub.RequestResponse requestResponse = stub.request(request);
				
				JaxbBinder sjlcResultJaxbBinder = new JaxbBinder(SJLCResult.class);
				System.out.println("return:"+requestResponse.get_return()); 
				SJLCResult sjlcResult = sjlcResultJaxbBinder.fromXml(requestResponse.get_return());
				if(StringUtils.isNotBlank(sjlcResult.getResult()) && sjlcResult.getResult().equals("1")){
//					dataExchangeStatusService.f
//					interId
					
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(interId);
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(oppoSideBizCode);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！"+record);
				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
			i++;
		}
	
	}
	
	private void reportFeedbackE(Map<String, Object> resultMap){

		String doTime = "";
		if(resultMap.get("START_TIME")!=null){
			oracle.sql.TIMESTAMP t = (oracle.sql.TIMESTAMP)resultMap.get("START_TIME");
			try {
				Date date = t.dateValue();
				doTime = DateUtils.formatDate(date, DateUtils.PATTERN_24TIME);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
		String taskId = resultMap.get("TASK_ID").toString();
		
		DailyIncidentServiceStub stub = null;

		ReportedFeedback reportedFeedback = new ReportedFeedback();
		reportedFeedback.setCommandCode(oppoSideBizCode);//对方标识
		reportedFeedback.setFeedbackSource(5);
		reportedFeedback.setFeedbackType("0");//0：正常反馈，1：退单，4：办结反馈
		reportedFeedback.setContent(resultMap.get("REMARKS").toString());
		reportedFeedback.setFeedbackPerson(resultMap.get("TRANSACTOR_NAME").toString());
//		reportedFeedback.setEditPerson("测试填写人姓名");
		reportedFeedback.setFeedbackDate(doTime);//办理时间
		reportedFeedback.setFeedbackSourceCode(taskId);//网格平台编号
//		reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
		
		JaxbBinder receivedCommandJaxbBinder = new JaxbBinder(ReportedFeedback.class);
    	String reportedFeedbackResult = receivedCommandJaxbBinder.toXml(reportedFeedback, "UTF-8");
		try {
			stub = new DailyIncidentServiceStub(dsWebServiceUri);
			DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
			DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
			///...
			commonRequest.setFUNC("reportFeedback");
			commonRequest.setSPID("S5");
			commonRequest.setSPPWD("grid@ffcs.cn");
			commonRequest.setVER("1.0");
			
			commonRequest.setREQXML(reportedFeedbackResult);
			commonRequestE.setCommonRequest(commonRequest);
			CommonResult commonResult = stub.process(commonRequestE).getCommonResponse().getCommonResult();
			if(commonResult.getERRCD() == 1){
				commonResult.getRESXML();
			}
			DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
			dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
			dataExchangeResult.setOppoSideBizCode(commonResult.getRESXML());
			dataExchangeResult.setOwnSideBizCode(taskId);
			dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
			dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
			dataExchangeResult.setXmlData(reportedFeedbackResult);
			Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
		} catch (Exception e) {
			e.printStackTrace();
			DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
			dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
			dataExchangeResult.setOppoSideBizCode("0");
			dataExchangeResult.setOwnSideBizCode(taskId);
			dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
			dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
			dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
			dataExchangeResult.setXmlData(reportedFeedbackResult + "---" + e.getMessage());
			Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
		}
	}
	
	private void reportFeedback(){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("srcPlatform", BIZPLATFORM_DS);
			params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
			params.put("oppoSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
			params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
			params.put("bizPlatform", BIZPLATFORM_DS);
			List<Map<String, Object>> result = dataExchangeStatusTwoWayService.findSubTaskAppointed(params);
			if(result != null && result.size() > 0){
				reportFeedbackE(result.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	private void eventReport(){
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
//		params.put("trigger", ConstantValue.DEFAULT_REPORT_ACTIVITY_NAME_TRIGGER+ "_004");
////		params.put("bizPlatform", BIZPLATFORM);
//		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusService.findEventNewRejected(params);
		
		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_DS);
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_DS);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_DS);
//		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_AH);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", BIZPLATFORM_DS);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		String effectDs = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "EFFECT_12345_DO", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(dataExchangeStatuss!=null&&dataExchangeStatuss.size()>0 && effectDs.equals("true")){
			Map<String,Object> dataExchangeStatus = dataExchangeStatuss.get(0);
			EventDisposal event = eventDisposalService.findEventById(Long.valueOf(dataExchangeStatus.get("EVENT_ID").toString()), null);
			ReportedIncident reportedIncident = new ReportedIncident();
			reportedIncident.setIncidentName(event.getEventName());
			reportedIncident.setIncidentType("109999");//事件类型 字典
			reportedIncident.setRegionCode("350582000000");//行政区域
//			reportedIncident.setIncidentCode("");
			reportedIncident.setReportOrgCode("DW-00000001");//上报单位
			reportedIncident.setIncidentSource(5);
			reportedIncident.setIncidentSourceCode(dataExchangeStatus.get("EVENT_ID").toString());
			reportedIncident.setDescription(event.getContent());
			reportedIncident.setOccurPlace(event.getOccurred());
			reportedIncident.setReportPerson(event.getContactUser());
			reportedIncident.setReportMobile(event.getTel());
			reportedIncident.setOccurDate(event.getHappenTimeStr());
			
			JaxbBinder jaxbBinder = new JaxbBinder(ReportedIncident.class);
	    	String result =jaxbBinder.toXml(reportedIncident, "UTF-8");
			
			
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub(dsWebServiceUri);
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				commonRequest.setFUNC("reportDailyIncident");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				
				
				
				commonRequest.setREQXML(result);
				System.out.println("XMLresult:"+result);
				commonRequestE.setCommonRequest(commonRequest);
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerIncidentCode>", "").replace("</innerIncidentCode >", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
					dataExchangeResult.setOppoSideBizCode(RESXML);
					dataExchangeResult.setOwnSideBizCode(dataExchangeStatus.get("EVENT_ID").toString());
					dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
					dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
					dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
					dataExchangeResult.setXmlData(result);
					Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
				dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
				dataExchangeResult.setOppoSideBizCode("0");
				dataExchangeResult.setOwnSideBizCode(dataExchangeStatus.get("EVENT_ID").toString());
				dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
				dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
				dataExchangeResult.setXmlData(result + "---" + e.getLocalizedMessage());
				Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
			} finally{
				
			}
		}
	}
	
	private void rejected(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM_DS);
		params.put("exchangeFlag", IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		List<Map<String, Object>> result = dataExchangeStatusService.findEventNewRejected(params);
		for(Map<String, Object> resultMap : result){
			resultMap.get("REMARKS");
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub(dsWebServiceUri);
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				ReportedFeedback reportedFeedback = new ReportedFeedback();
//				reportedFeedback.setCommandCode(resultMap.get("OPPO_SIDE_BIZ_CODE").toString());//对方标识
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("1");//0：正常反馈，1：退单，4：办结反馈
				System.out.println("REMARKS->"+resultMap.get("REMARKS"));
				reportedFeedback.setContent(resultMap.get("REMARKS").toString());
				reportedFeedback.setFeedbackPerson(resultMap.get("TRANSACTOR_NAME").toString());
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(resultMap.get("EVENT_ID").toString());//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
				System.out.println("reportedFeedbackresult->"+reportedFeedbackresult);
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//					dataExchangeResult.setOppoSideBizCode(RESXML);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！");
				}
			} catch (AxisFault e) {
				e.printStackTrace();
				return;
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			} 
			catch (BizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void taskFeedback(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("bizPlatform", BIZPLATFORM_DS);
		List<Map<String, Object>> result = dataExchangeStatusService.findTaskNewToFeedback(params);
		for(Map<String, Object> resultMap : result){
			resultMap.get("REMARKS");
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub(dsWebServiceUri);
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				ReportedFeedback reportedFeedback = new ReportedFeedback();
				reportedFeedback.setCommandCode(resultMap.get("OPPO_SIDE_BIZ_CODE").toString());//对方标识
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("0");//0：正常反馈，1：退单，4：办结反馈
				reportedFeedback.setContent(resultMap.get("REMARKS").toString());
				reportedFeedback.setFeedbackPerson(resultMap.get("TRANSACTOR_NAME").toString());
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(resultMap.get("OWN_SIDE_BIZ_CODE").toString());//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//					dataExchangeResult.setOppoSideBizCode(RESXML);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！");
				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (BizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void eventFeedback(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", BIZPLATFORM_DS);
		List<Map<String, Object>> results = dataExchangeStatusTwoWayService.findCloseEventAppointed(params);
		for(Map<String, Object> result : results){
			String oppoSideBizCode = result.get("OPPO_SIDE_BIZ_CODE").toString();
			String interId = result.get("INTER_ID").toString();
			String remarks = result.get("RESULT").toString();
			String occurred = result.get("OCCURRED").toString();
			String eventId = result.get("EVENT_ID").toString();
			String xmlData = result.get("XML_DATA").toString();
			String finTimeStr = "";
			if(result.get("FIN_TIME")!=null){
				Date finTime = (Date)result.get("FIN_TIME");
				finTimeStr = DateUtils.formatDate(finTime, DateUtils.PATTERN_24TIME);
			}
			
			DailyIncidentServiceStub stub = null;
			ReportedFeedback reportedFeedback = new ReportedFeedback();
			reportedFeedback.setCommandCode(oppoSideBizCode);//
			reportedFeedback.setFeedbackSource(5);
			reportedFeedback.setFeedbackType("4");//0：正常反馈，1：退单，4：办结反馈
			reportedFeedback.setContent(remarks);
			reportedFeedback.setFeedbackPerson(occurred);
//			reportedFeedback.setEditPerson("测试填写人姓名");
			reportedFeedback.setFeedbackDate(finTimeStr);
			reportedFeedback.setFeedbackSourceCode(eventId);//网格平台编号
//			reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
			
			JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
	    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
	    	
	    	//返回结果
	    	String resultXmlData = "";
	    	try {
				stub = new DailyIncidentServiceStub(dsWebServiceUri);
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					resultXmlData = RESXML;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	//保存结果
			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
			dataExchangeStatus.setInterId(Long.valueOf(interId));
			dataExchangeStatus.setExchangeFlag("1");
			dataExchangeStatus.setXmlData(xmlData + "---" + resultXmlData);
			dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeStatus);
		}
	}
	
	private void eventFeedback1(){
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
//		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("bizPlatform", BIZPLATFORM_DS);
//		params.put("eventStatus", "'00','01','02','03','04'");
//		params.put("OWN_SIDE_BIZ_TYPE", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
//		params.put("OPPO_SIDE_BIZ_TYPE", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
//		List<Map<String, Object>> result = dataExchangeStatusService.findEventNewToFeedback(params);

		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("eventId", ownSideBizCode);
		String[] eventStatuss = {"03","04"};
//		params.put("eventStatuss", eventStatuss);
		params.put("destPlatform", "000");
		params.put("srcPlatform", BIZPLATFORM_DS);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("destPlatformTask", BIZPLATFORM_DS);
		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);

		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		for(Map<String, Object> task : srcTasks){
			String oppoSideBizCode = task.get("OPPO_SIDE_BIZ_CODE").toString();
			String oppoSideBizType = task.get("OPPO_SIDE_BIZ_TYPE").toString();
			String taskName = task.get("TASK_NAME").toString();
			String eventId = null == task.get("EVENT_ID") ? "" : task.get("EVENT_ID").toString();
			String teskId = null == task.get("TASK_ID") ? "" : task.get("TASK_ID").toString();
			String transactorId = null == task.get("TRANSACTOR_ID") ? "" : task.get("TRANSACTOR_ID").toString();
			String transactorName = null == task.get("TRANSACTOR_NAME") ? "" : task.get("TRANSACTOR_NAME").toString();
			String orgName = task.get("ORG_NAME").toString();
			String orgId = task.get("ORG_ID").toString();
			String remarks = task.get("REMARKS").toString();
			if(taskName.equals("结案")){
				DailyIncidentServiceStub stub = null;
				ReportedFeedback reportedFeedback = new ReportedFeedback();
				reportedFeedback.setCommandCode(oppoSideBizCode);//
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("4");//0：正常反馈，1：退单，4：办结反馈
				reportedFeedback.setContent(remarks);
				reportedFeedback.setFeedbackPerson(transactorName);
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(eventId);//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
		    	
				try {
					stub = new DailyIncidentServiceStub(dsWebServiceUri);
					DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
					DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
					///...
					commonRequest.setFUNC("reportFeedback");
					commonRequest.setSPID("S5");
					commonRequest.setSPPWD("grid@ffcs.cn");
					commonRequest.setVER("1.0");
					
					commonRequest.setREQXML(reportedFeedbackresult);
					commonRequestE.setCommonRequest(commonRequest);
					
					DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
					System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
					System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
					String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
					RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
					if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
//						DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//						dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
//						dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//						dataExchangeResult.setOppoSideBizCode(RESXML);
//						dataExchangeResult.setXmlData(reportedFeedbackresult);
//						boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
						
						DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
						dataExchangeStatus.setOppoSideBizCode(RESXML);
						dataExchangeStatus.setOppoSideBizType("3");
						dataExchangeStatus.setOwnSideBizCode(teskId);
						dataExchangeStatus.setOwnSideBizType("3");
						dataExchangeStatus.setExchangeFlag("1");
						dataExchangeStatus.setDestPlatform(BIZPLATFORM_DS);
						dataExchangeStatus.setSrcPlatform("000");
						dataExchangeStatus.setXmlData(reportedFeedbackresult + "---" + RESXML);
						dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
//					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
//					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
////					dataExchangeResult.setOppoSideBizCode("0");
//					dataExchangeResult.setXmlData(reportedFeedbackresult + "---" + e.getMessage());
//					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);

					DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
					dataExchangeStatus.setOppoSideBizCode("0");
					dataExchangeStatus.setOppoSideBizType("3");
					dataExchangeStatus.setOwnSideBizCode(teskId);
					dataExchangeStatus.setOwnSideBizType("3");
					dataExchangeStatus.setExchangeFlag("1");
					dataExchangeStatus.setDestPlatform(BIZPLATFORM_DS);
					dataExchangeStatus.setSrcPlatform("000");
					dataExchangeStatus.setXmlData(reportedFeedbackresult + "---" + e.getMessage());
					dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
				}
				
			}
		}
		
//		Map<String, Object> params = new HashMap<String, Object>();
//		String[] eventStatuss = {"03","04"};
//		params.put("eventStatuss", eventStatuss);
//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("destPlatform", BIZPLATFORM_DS);
//		params.put("", IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
//		List<Map<String, Object>> result = dataExchangeStatusTwoWayService.findEventNewDocking(params);
//		feedback(result);
	}
	
	private void feedback(List<Map<String, Object>> result){
		for(Map<String, Object> resultMap : result){
			String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
			String ownSideBizCode = resultMap.get("OWN_SIDE_BIZ_CODE").toString();
			String eventStatus = resultMap.get("STATUS").toString();
			//初始化任务
//			boolean initTaskFeedback = dataExchangeStatusService.isEventTaskFeedback(Long.valueOf(ownSideBizCode), BIZPLATFORM_DS, oppoSideBizCode);
//			if(!initTaskFeedback) return;
			//03,04才反馈
//			if(!ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) || !ConstantValue.EVENT_STATUS_END.equals(eventStatus)) continue;
			

			DailyIncidentServiceStub stub = null;
			ReportedFeedback reportedFeedback = new ReportedFeedback();
			reportedFeedback.setCommandCode(oppoSideBizCode);//
			reportedFeedback.setFeedbackSource(5);
			reportedFeedback.setFeedbackType("4");//0：正常反馈，1：退单，4：办结反馈
			reportedFeedback.setContent(resultMap.get("RESULT").toString());
			reportedFeedback.setFeedbackPerson(resultMap.get("CLOSER").toString());
//			reportedFeedback.setEditPerson("测试填写人姓名");
//			reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
			reportedFeedback.setFeedbackSourceCode(ownSideBizCode);//网格平台编号
//			reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
			
			JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
	    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
	    	
			try {
				stub = new DailyIncidentServiceStub(dsWebServiceUri);
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(RESXML);
					dataExchangeResult.setXmlData(reportedFeedbackresult);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
				}
			} catch (Exception e) {
				e.printStackTrace();
				DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
				dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
				dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//				dataExchangeResult.setOppoSideBizCode("0");
				dataExchangeResult.setXmlData(reportedFeedbackresult + "---" + e.getMessage());
				boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
			}
		}
	}
	
	/** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }
}
