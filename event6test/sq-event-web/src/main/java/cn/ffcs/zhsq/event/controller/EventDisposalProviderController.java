package cn.ffcs.zhsq.event.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.zhsq.event.service.IEventDisposalProviderService;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.zhsq.mybatis.domain.event.EventRent;
import cn.ffcs.zhsq.mybatis.domain.event.EventSurvey;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.ParamUtils;


/**
 * 第三方事件调用接口
 * @author youzh
 *
 */
@Controller
@RequestMapping("/api/event/eventDisposalProvider")
public class EventDisposalProviderController
{
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private IEventDisposalProviderService eventDisposalProviderService;
  
  @Autowired
  private FileUploadService fileUploadService;

  @RequestMapping({"/insertEvent"})
  public void insertEvent(HttpServletRequest request, HttpServletResponse response) throws Exception { 
	request.setCharacterEncoding("UTF-8");
    logger.info("插入一条事件-------------------------------获取客户端请求串:" + request.getQueryString());

    String eventId = ParamUtils.getString(request, "eventId", "");
    String buildingName = ParamUtils.getString(request, "buildingName", "");
    String content = ParamUtils.getString(request, "content", "");

    String gridCode = ParamUtils.getString(request, "gridCode", "");
    String happenTime = ParamUtils.getString(request, "happenTime", "");
    String occurred = ParamUtils.getString(request, "occurred", "");
    String reporteTelephone = ParamUtils.getString(request, "reporteTelephone", "");
    String reporter = ParamUtils.getString(request, "reporter", "");
    String source = ParamUtils.getString(request, "source", "");
    String type = ParamUtils.getString(request, "type", "");
    String urgencyDegree = ParamUtils.getString(request, "urgencyDegree", "");

    String fileName = ParamUtils.getString(request, "firstImg", "");
    logger.info("插入一条事件-----------------------------------------处理图片开始:" + fileName);
    
    String filePath="";
    
    if(!("").equals(fileName)){
    	try{
	    	File file = new File(fileName);
	
	        String filename = file.getName();
	
	        String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
	
	        //InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
	
	        URL url = new URL(fileName);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(10 * 1000);  
	        conn.setRequestMethod("GET");  
	        conn.connect();
			InputStream fis = conn.getInputStream();
	        
	        byte[] picture = new byte[fis.available()];
	
	        filePath= this.fileUploadService.uploadSingleFile(filename, picture, ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
    	}catch(FileNotFoundException e){
    		logger.info(fileName+"不是有效的文件路径！");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
    }
    
    logger.info("插入一条事件-----------------------------------------处理图片结束:" + filePath);
    
    net.sf.json.JSONObject json = new net.sf.json.JSONObject();
    json.put("eventId", eventId);
    json.put("buildingName", buildingName);
    json.put("content", content);

    json.put("firstImg", filePath);

    json.put("gridCode", gridCode);
    json.put("happenTime", happenTime);
    json.put("occurred", occurred);
    json.put("reporteTelephone", reporteTelephone);
    json.put("reporter", reporter);
    json.put("source", source);
    json.put("type", type);
    json.put("urgencyDegree", urgencyDegree);
    
    if(!StringUtils.isBlank(type)){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if("0205".equals(type)){
    		VisitRecord visitRecord = new VisitRecord();
    		String criminalFacts = ParamUtils.getString(request, "criminalFacts", "");
    		String visitedType = ParamUtils.getString(request, "visitedType", "");
    		String visitTime = ParamUtils.getString(request, "visitTime", "");
    		String talkContent = ParamUtils.getString(request, "talkContent", "");
    		String visitCause = ParamUtils.getString(request, "visitCause", "");
    		String visitForm = ParamUtils.getString(request, "visitForm", "");
    		String visitEffect = ParamUtils.getString(request, "visitEffect", "");
    		String recentState = ParamUtils.getString(request, "recentState", "");
    		String measures = ParamUtils.getString(request, "measures", "");
    		String remarks = ParamUtils.getString(request, "remarks", "");
    		String visitName = ParamUtils.getString(request, "visitName", "");
    		
    		if(!StringUtils.isBlank(criminalFacts)){
    			visitRecord.setCriminalFacts(criminalFacts);
    		}
    		if(!StringUtils.isBlank(visitedType)){
    			visitRecord.setVisitedType(visitedType);
    		}
    		if(!StringUtils.isBlank(visitTime)){
    			visitRecord.setVisitTime(sdf.parse(visitTime));
    		}else{
    			visitRecord.setVisitTime(new Date());
    		}
    		if(!StringUtils.isBlank(talkContent)){
    			visitRecord.setTalkContent(talkContent);
    		}
    		if(!StringUtils.isBlank(visitCause)){
    			visitRecord.setVisitCause(visitCause);
    		}
    		if(!StringUtils.isBlank(visitForm)){
    			visitRecord.setVisitForm(visitForm);
    		}
    		if(!StringUtils.isBlank(visitEffect)){
    			visitRecord.setVisitEffect(visitEffect);
    		}
    		if(!StringUtils.isBlank(recentState)){
    			visitRecord.setRecentState(recentState);
    		}
	    	if(!StringUtils.isBlank(measures)){
	    		visitRecord.setMeasures(measures);
    		}
	    	if(!StringUtils.isBlank(remarks)){
	    		visitRecord.setRemarks(remarks);
	    	}
	    	if(!StringUtils.isBlank(visitName)){
	    		visitRecord.setVisitName(visitName);
	    	}
    		
    		json.put("visitRecord", visitRecord);
    	}else if("0207".equals(type)){
    		EventRent eventRent = new EventRent();
    		String lessor = ParamUtils.getString(request, "lessor", "");//出租人   LESSOR
    		String lessee = ParamUtils.getString(request, "lessee", "");//承租人  LESSEE
    		String rentStart = ParamUtils.getString(request, "rentStart", "");//租赁开始时间   RENT_START
    		String rentEnd = ParamUtils.getString(request, "rentEnd", "");//租赁结束时间    RENT_END
    		String rentNum = ParamUtils.getString(request, "rentNum", "");//租住人数  RENT_NUM
    		String rentalStaff = ParamUtils.getString(request, "rentalStaff", "");//租住人名单  RENTAL_STAFF
    		String remarks = ParamUtils.getString(request, "remarks", "");//备注  REMARKS
    		
    		if(!StringUtils.isBlank(lessor)){
    			eventRent.setLessor(lessor);
    		}
    		if(!StringUtils.isBlank(lessee)){
    			eventRent.setLessee(lessee);
    		}
    		if(!StringUtils.isBlank(rentStart)){
    			eventRent.setRentStart(sdf.parse(rentStart));
    		}
    		if(!StringUtils.isBlank(rentEnd)){
    			eventRent.setRentEnd(sdf.parse(rentEnd));
    		}
    		if(!StringUtils.isBlank(rentNum)){
    			eventRent.setRentNum(Long.valueOf(rentNum));
    		}
    		if(!StringUtils.isBlank(rentalStaff)){
    			eventRent.setRentalStaff(rentalStaff);
    		}
    		if(!StringUtils.isBlank(remarks)){
    			eventRent.setRemarks(remarks);
    		}
    		
    		json.put("eventRent", eventRent);
    		
    	}else if("0602".equals(type)){
    		EventSurvey eventSurvey = new EventSurvey();
    		String refitScheme = ParamUtils.getString(request, "refitScheme", "");//整改措施
    		String refitResult = ParamUtils.getString(request, "refitResult", "");//整改结果
    		
    		if(!StringUtils.isBlank(refitScheme)){
    			eventSurvey.setRefitScheme(refitScheme);
    		}
    		if(!StringUtils.isBlank(refitResult)){
    			eventSurvey.setRefitResult(refitResult);
    		}
    		
    		json.put("eventSurvey", eventSurvey);
    	}
    	
    }
    
    String jsonStr = json.toString();

    String result = this.eventDisposalProviderService.insertEvent(jsonStr, null);
	
    responseClient(response, result);
  }

  @RequestMapping({"/extractClosedEvent"})
  public void extractClosedEvent(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setCharacterEncoding("UTF-8");
    logger.info("拉取结案事件---------------------------------------------获取客户端请求串:" + request.getQueryString());

    String gridCode = ParamUtils.getString(request, "gridCode", "");
    String result = this.eventDisposalProviderService.extractClosedEvent(gridCode);
    responseClient(response, result);
  }

  @RequestMapping({"/extractTodoEvent"})
  public void extractTodoEvent(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setCharacterEncoding("UTF-8");
    logger.info("拉取待办事件---------------------------------------------获取客户端请求串:" + request.getQueryString());

    String gridCode = ParamUtils.getString(request, "gridCode", "");
    if (StringUtils.isEmpty(gridCode)) {
      org.json.JSONObject json = new org.json.JSONObject();
      json.put("status", "1");
      json.put("desc", "调用失败,未指定gridCode");
      responseClient(response, json.toString());
      return;
    }

    String result = this.eventDisposalProviderService.extractTodoEvent(gridCode);
    responseClient(response, result);
  }

  @RequestMapping({"/receiveFeedback"})
  public void receiveFeedback(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setCharacterEncoding("UTF-8");
    logger.info("接收完结案数据后要调用此接口-----------------------------------获取客户端请求串:" + request.getQueryString());

    String ids = ParamUtils.getString(request, "eventIds", "");

    String result = this.eventDisposalProviderService.receiveFeedback(ids);
    responseClient(response, result);
  }

  @RequestMapping({"/closeEvent"})
  public void closeEvent(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setCharacterEncoding("UTF-8");
    logger.info("接收完结案数据后要调用此接口----------------------------------获取客户端请求串:" + request.getQueryString());

    String eventId = ParamUtils.getString(request, "eventId", "");
    String procUserId = ParamUtils.getString(request, "procUserId", "");
    String procUserName = ParamUtils.getString(request, "procUserName", "");
    String procTime = ParamUtils.getString(request, "procTime", "");
    String procResult = ParamUtils.getString(request, "procResult", "");
    String lastImg = ParamUtils.getString(request, "lastImg", "");

    String fileName = ParamUtils.getString(request, "lastImg", "");
    logger.info("接收完结案数据后要调用此接口----------------------------------图片处理开始:" +fileName);

     String filePath="";
    
    if(!("").equals(fileName)){
    	try{
    	    File file = new File(fileName);
    	    String filename = file.getName();
    	    String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
    	    //InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
    	    URL url = new URL(fileName);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(10 * 1000);  
	        conn.setRequestMethod("GET");  
	        conn.connect();
			InputStream fis = conn.getInputStream();
    	    byte[] picture = new byte[fis.available()];

    	    filePath = this.fileUploadService.uploadSingleFile(filename, picture, ConstantValue.RESOURSE_DOMAIN_KEY, "eventPhoto");
    	}catch(FileNotFoundException e){
    		logger.info(fileName+"不是有效的文件路径！");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
   

    logger.info("接收完结案数据后要调用此接口----------------------------------图片处理结束:" +filePath);
    
    net.sf.json.JSONObject json = new net.sf.json.JSONObject();
    json.put("eventId", eventId);
    json.put("procUserId", procUserId);
    json.put("procUserName", procUserName);
    json.put("procTime", procTime);
    json.put("procResult", procResult);

    json.put("lastImg", filePath);

    String jsonStr = json.toString();

    String result = this.eventDisposalProviderService.closeEvent(jsonStr, null);
    responseClient(response, result);
  }

  private void responseClient(HttpServletResponse response, String result)
    throws Exception
  {
    if (!StringUtils.isEmpty(result)) {
      logger.debug("-->call return:" + result);

      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0L);
      PrintWriter out = response.getWriter();
      out.write(result);
      out.flush();
      out.close();
      logger.info("end request");
    }
  }
}