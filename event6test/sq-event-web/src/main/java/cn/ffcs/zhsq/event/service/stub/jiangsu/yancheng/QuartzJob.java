package cn.ffcs.zhsq.event.service.stub.jiangsu.yancheng;/**
 * Created by Administrator on 2018/3/6.
 */

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;

/**
 * 省综治对接
 *
 * @author zhongshm
 * @create 2018-03-06 9:07
 **/
@DisallowConcurrentExecution
public class QuartzJob implements Job {
    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");
    private IDataExchangeStatusService dataExchangeStatusService;

    private IFunConfigurationService funConfigurationService;
    private UserManageOutService userManageService;

    private OrgSocialInfoOutService orgSocialInfoService;

    protected IMixedGridInfoService mixedGridInfoService;
    private IBasDataExchangeService basDataExchangeService;
    private IAttachmentService attachmentService;
    private IEventDisposalService eventDisposalService;
    private IBaseDictionaryService baseDictionaryService;

    //业务编码
    private final String BIZ_PLATFORM = "062";
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schCtx = null;
        try {
            schCtx = context.getScheduler().getContext();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
        this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
        this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
        this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
        this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
        this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");       
        this.orgSocialInfoService = (OrgSocialInfoOutService) appCtx.getBean("orgSocialInfoService");
        this.basDataExchangeService = (IBasDataExchangeService) appCtx.getBean("basDataExchangeServiceImpl");
        this.eventDisposalService = (IEventDisposalService) appCtx.getBean("eventDisposalServiceImpl");
        this.baseDictionaryService = (IBaseDictionaryService) appCtx.getBean("baseDictionaryService");

        String signUrl = "http://172.23.10.16:8080/signapi/alluser/jspt/getsign";
        String url = "http://172.23.10.15:8080/mobile/issue/issueDubboService/receptIssue";
        String appKey = "Q3kRX9t_P1rHCZdc";
        String userName = "jys";
        String passWordMD5 = "dd50f65d7eaf7ddb347b0011d4c9412a";	   
        String sign = "";//证书
        String result = "";//接口返回结果
        String resultId = "";//对方id
        String eventId="";
        Map<String, Object> para = new HashMap<>();
        //获取需要对接的结案归档事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", "000");
        params.put("descPlatform", BIZ_PLATFORM);
        String date=funConfigurationService.changeCodeToValue("THREE_PART_JOINT_UNOIN", "DF_TQ_DATE", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String dicCode=funConfigurationService.changeCodeToValue("THREE_PART_JOINT_UNOIN", "DF_TQ_DICCODE", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isNotBlank(date)){
            params.put("dicCode", dicCode);
        }else{
            params.put("dicCode", "B0912");
        }
        if(StringUtils.isNotBlank(date)){
        	params.put("createTime",date);
        }else{
        	params.put("createTime","2018-12-07");
        }
        JSONObject dataJson = new JSONObject();
        List<Map<String, Object>> fileEvent =this.dataExchangeStatusService.findFileEvent(params);
        if(null != fileEvent && fileEvent.size() > 0){
               for(Map<String, Object> fileEvt : fileEvent){
            	   eventId=fileEvt.get("EVENT_ID").toString();
            	   dataJson = new JSONObject();
            	   dataJson.put("userName",userName);
         	       dataJson.put("appKey", appKey);
         	       dataJson.put("passWordMD5", passWordMD5);	      
         	       dataJson.put("issueInfo.sourceCode", "ycsdf12345");
         	       dataJson.put("issueInfo.issueTypeId", fileEvt.get("OPP0_TYPE").toString());//事件类型ID
         	       dataJson.put("issueInfo.taskMode", "done");
         	       dataJson.put("issueInfo.occurDate", fileEvt.get("HAPPEN_TIME").toString());//发生时间      
                   if(fileEvt.get("OCCURRED")!=null){
                	   dataJson.put("issueInfo.occurLocation", fileEvt.get("OCCURRED").toString());//发生地点
                   }else{
                	   dataJson.put("issueInfo.occurLocation", "江苏省盐城市大丰区");
                   }
                   if(fileEvt.get("CONTENT_")!=null){
               	       dataJson.put("issueInfo.content", fileEvt.get("CONTENT_").toString());//事件简述              
                   }else{
               	       dataJson.put("issueInfo.content", "大丰省综治事件内容");
                   }
                   if(fileEvt.get("CONTACT_USER")!=null){
                       dataJson.put("issueInfo.sourcePerson", fileEvt.get("CONTACT_USER").toString());//事件来源人姓名             	 
                   }else{
                	   dataJson.put("issueInfo.sourcePerson", "李四");//事件来源人姓名
                   }
                   if(fileEvt.get("TEL")!=null){
                	    dataJson.put("issueInfo.sourceMobile",fileEvt.get("TEL").toString());//事件来源人号码
                   }else{
                	    dataJson.put("issueInfo.sourceMobile", "18977777777");//事件来源人号码
                   }
                   if(fileEvt.get("EVENT_NAME")!=null){
            	       dataJson.put("issueInfo.title", fileEvt.get("EVENT_NAME").toString());//事件主题
                   }else{
            	       dataJson.put("issueInfo.title", "大丰省综治事件");//事件主题
                   }                    
        	       dataJson.put("issueInfo.occurOrg", fileEvt.get("GRID_NAME").toString());//事件发生网格
         	       dataJson.put("city", "无锡市");//调用方 所在市 中文名称
         	       dataJson.put("district", "盐城市");//调用方 所在区 中文名称        	       
                   String gridCode=fileEvt.get("GRID_CODE").toString();
                   if(StringUtils.isNotBlank(gridCode)){
                	   if(gridCode.length()==9){//街镇级别
                			dataJson.put("town", fileEvt.get("GRID_NAME").toString());//调用方 所在街道 中文名称 
                	   }else if(gridCode.length()==12 || gridCode.length()==15){//村级和网格别
                		    para = new HashMap<>();
                	  	    para.put("gridCode", gridCode.substring(0,9));//取镇名称
                	    	List<MixedGridInfo> grid=this.mixedGridInfoService.findByGridCode(para);
                	    	if(grid!=null && grid.size()>0){
                	    		dataJson.put("town", grid.get(0).getGridName());//调用方 所在街道 中文名称 
                	    	}else{
                	    		dataJson.put("town", "大丰区");//调用方 所在街道 中文名称 
                	    	}
                	   }else{
                		   dataJson.put("town", "大丰区");//调用方 所在街道 中文名称 
                	   }
                   }else{
                	   dataJson.put("town", "大丰区");//调用方 所在街道 中文名称 
                   }
                   //进行sign请求
                   logger.debug("signUrlparm-"+dataJson.toString());
                   String signResult = HttpUtil.doPost2(signUrl, dataJson.toString());     
                   logger.debug("signResult"+signResult);
                   if(null !=signResult){
	                   JSONObject signResultJson = JSONObject.fromObject(signResult);
	                   if(null != signResultJson.get("success") && signResultJson.get("success").toString().equals("true")){
	                       if(null != signResultJson.get("sign")){
	                           sign = signResultJson.get("sign").toString();
	                       }
	                   }
                   }else{//sign请求失败
                	   this.saveEventDataExchangeStatus("4",resultId, eventId,"请求sign参数:"+dataJson.toString()+"返回sign结果:"+result);
                   }
                   if(StringUtils.isNotBlank(sign)){
                       dataJson.put("sign", sign);
                       logger.debug("sParam"+ dataJson.toString());
                       //数据的推送
                       result = HttpUtil.doPost2(url, dataJson.toString());
                       logger.debug("sParamResult"+result);
                       if(null ==result){
                    	   this.saveEventDataExchangeStatus("4",resultId, eventId,"请求参数:"+dataJson.toString()+"返回结果:"+result);
                       }else{
	                       JSONObject resultJson = JSONObject.fromObject(result);
	                       if(null != resultJson.get("status") 
	                    		   && resultJson.get("status").toString().equals("200")){
	                           if(null != resultJson.get("body")){
	                               JSONObject body = JSONObject.fromObject(resultJson.get("body"));
	                               if(null != body.get("result")){
	                            	   //第三方事件ID
	                                   resultId = body.get("result").toString();
	                               }
	                           }          
	                           this.saveEventDataExchangeStatus("2",resultId, eventId,"请求参数:"+dataJson.toString()+"返回结果:"+result);
	                       }else{//失败了                         
	                           this.saveEventDataExchangeStatus("4",resultId, eventId,"请求参数:"+dataJson.toString()+"返回结果:"+result);
	                       }
	                   }
                   }

               }
        }

    }
    
	private Boolean saveEventDataExchangeStatus(String type,String oppo, String own, String xmlData){
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setDestPlatform(BIZ_PLATFORM);
		dataExchangeStatus.setSrcPlatform("000");
		dataExchangeStatus.setOppoSideBizCode(oppo);
		dataExchangeStatus.setOppoSideBizType(type);
		dataExchangeStatus.setOwnSideBizCode(own);
		dataExchangeStatus.setOwnSideBizType(type);
		dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
		dataExchangeStatus.setXmlData(xmlData);
		return dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus) > 0;
	}


}
