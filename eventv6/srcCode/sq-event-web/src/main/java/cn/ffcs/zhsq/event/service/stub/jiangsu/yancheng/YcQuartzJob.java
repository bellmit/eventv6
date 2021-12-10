package cn.ffcs.zhsq.event.service.stub.jiangsu.yancheng;/**
 * Created by Administrator on 2018/8/4.
 */

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ctc.wstx.util.StringUtil;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;
import cn.ffcs.common.FileUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.utils.domain.App;
/**
 * 12345外网工单
 * @author tursh
 * @create 2018年8月23日 15:41:13
 **/


@DisallowConcurrentExecution
public class YcQuartzJob implements Job {
    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");
    private IDataExchangeStatusService dataExchangeStatusService;
    private IDataExchangeStatusTwoWayService iDataExchangeStatusTwoWayService;
    private IFunConfigurationService funConfigurationService;
    private IEventExtendService eventExtendService;
    private IAttachmentService attachmentService;
    protected IMixedGridInfoService mixedGridInfoService;
    private UserManageOutService userManageService;
    private IResMarkerService resMarkerService;
    private IBaseDictionaryService baseDictionaryService;

    private String IMG_URL = "";
    private String BIZ_PLATFORM = "206";//12345外网工单
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String USER_ID = "";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("tursh====================江苏盐城外网工单接入对接");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.iDataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
            this.resMarkerService = (IResMarkerService) appCtx.getBean("resMarkerService");
            this.baseDictionaryService = (IBaseDictionaryService) appCtx.getBean("baseDictionaryService");
            this.eventExtendService = (IEventExtendService) appCtx.getBean("eventExtendService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        String netUrl=funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JS_NET_URL", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(netUrl)){
        	 netUrl="http://2.142.2.13:8180/yc12345/inter/order/addNetWorkOrder.inter?action=addNetWorkOrder";
        }      
        USER_ID=funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JS_YC_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(USER_ID)){
        	USER_ID="30018390";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId",USER_ID);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        System.out.println("reportEventSize="+reportEvent.size());
        if( null!= reportEvent && reportEvent.size() > 0){
        	 JSONObject reqData = null;
             JSONObject dataJson =null;
             JSONObject paras = null;  
            for(Map<String, Object> event : reportEvent){
            	reqData=new JSONObject();
            	dataJson =new JSONObject();
            	paras = new JSONObject();            	
            	String event_id = event.get("EVENT_ID").toString();
            	paras.put("formOrigin","ZW");
            	paras.put("cusType","1");
            	paras.put("eventId",event_id);
            	if(event.get("CONTENT_")!=null){
            	  	paras.put("contentText",event.get("CONTENT_").toString());//工单内容-事件描述
            	}else{
            	  	paras.put("contentText","测试工单内容");//工单内容-事件描述
            	}        
            	paras.put("cusSex","1");
            	if(event.get("CREATOR_NAME")!=null){
            		paras.put("cusName",event.get("CREATOR_NAME").toString());
            	}else{
            		paras.put("cusName","张杰");
            	}
            	if(event.get("TEL")!=null){
            		paras.put("cusPhone",event.get("TEL").toString());
            	}else{
            		paras.put("cusPhone","15234567890");
            	}
                if(event.get("IDENTITY_CARD")!=null){
                     paras.put("idCardNum",event.get("IDENTITY_CARD").toString());
                }else{
                	 paras.put("idCardNum","");
                }
                paras.put("whConPerInfo","1");
                paras.put("areaCode","003"); 
                dataJson.put("paras",paras);
                reqData.put("reqData",dataJson);
                String dataresult = HttpUtil.SendBusiFormByPostRequest(netUrl,reqData.toString());
                JSONObject resultObj = null;
                if (StringUtils.isNotBlank(dataresult)) {
                	resultObj = JSONObject.fromObject(dataresult);
				}
                if(resultObj!=null){
                	JSONObject result=(JSONObject)resultObj.get("resData");
                	JSONObject system=(JSONObject)result.get("system");
                	if(system.get("code")!=null && "1".equals(system.get("code").toString())){//调用成功
                		 JSONObject returnInfo=(JSONObject)result.get("returnInfo");
                		 String oppId=returnInfo.get("formId").toString();           
                         DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                         dataExchangeStatus.setExchangeFlag("1");
                         dataExchangeStatus.setOppoSideBizCode(oppId);
                         dataExchangeStatus.setOwnSideBizCode(event_id);
                         dataExchangeStatus.setStatus("1");
                         dataExchangeStatus.setXmlData("请求参数:"+reqData+"返回结果:"+resultObj);
                         dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                         dataExchangeStatus.setSrcPlatform("000");

                         dataExchangeStatus.setOppoSideBizType("2");
                         dataExchangeStatus.setOwnSideBizType("2");
                         this.dataExchangeStatusService.save(dataExchangeStatus);
                	}else{
                		  DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                          dataExchangeStatus.setExchangeFlag("1");
                          dataExchangeStatus.setOppoSideBizCode("");
                          dataExchangeStatus.setOwnSideBizCode(event_id);
                          dataExchangeStatus.setStatus("1");
                          dataExchangeStatus.setXmlData("请求参数:"+reqData+"返回结果:"+resultObj);
                          dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                          dataExchangeStatus.setSrcPlatform("000");
                          dataExchangeStatus.setOppoSideBizType("2");
                          dataExchangeStatus.setOwnSideBizType("2");
                          this.dataExchangeStatusService.save(dataExchangeStatus);
                	}
                }else{
                	  DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                      dataExchangeStatus.setExchangeFlag("1");
                      dataExchangeStatus.setOppoSideBizCode("");
                      dataExchangeStatus.setOwnSideBizCode(event_id);
                      dataExchangeStatus.setStatus("1");
                      dataExchangeStatus.setXmlData("请求参数:"+reqData+"返回结果:"+resultObj);
                      dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                      dataExchangeStatus.setSrcPlatform("000");
                      dataExchangeStatus.setOppoSideBizType("2");
                      dataExchangeStatus.setOwnSideBizType("2");
                      this.dataExchangeStatusService.save(dataExchangeStatus);
                }
                
            }
        }
    }  
}