package cn.ffcs.zhsq.nanChang3D.video;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

public class ArtemisHttpUtil
{
  //private static final Logger logger = LoggerFactory.getLogger(ArtemisHttpUtil.class);
  private static final List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList();
  private static final String SUCC_PRE = "2";
  private static final String REDIRECT_PRE = "3";
  
  
  
  
  
  
  
  public static String doPostStringArtemis(Map<String, String> path, String body, Map<String, String> querys, String accept, String contentType)
  {
    String httpSchema = (String)path.keySet().toArray()[0];
    if ((httpSchema == null) || (StringUtils.isEmpty(httpSchema))) {
      throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒httpSchema: " + httpSchema);
    }
    String responseStr = null;
    try
    {
      Map<String, String> headers = new HashMap();
      if (StringUtils.isNotBlank(accept)) {
        headers.put("Accept", accept);
      } else {
        headers.put("Accept", "*/*");
      }
      if (StringUtils.isNotBlank(contentType)) {
        headers.put("Content-Type", contentType);
      } else {
        headers.put("Content-Type", "application/text;charset=UTF-8");
      }
      /*if (header != null) {
        headers.putAll(header);
      }*/
      CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
      
      Request request = new Request(Method.POST_STRING, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
      request.setHeaders(headers);
      request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
      
      request.setQuerys(querys);
      
      request.setStringBody(body);
      
      Response response = Client.execute(request);
      
      responseStr = getResponseResult(response);
    }
    catch (Exception e)
    {
      //logger.error("the Artemis PostString Request is failed[doPostStringArtemis]", e);
    }
    return responseStr;
  }
  
  private static String getResponseResult(Response response)
  {
    String responseStr = null;
    
    int statusCode = response.getStatusCode();
    if ((String.valueOf(statusCode).startsWith("2")) || (String.valueOf(statusCode).startsWith("3")))
    {
      responseStr = response.getBody();
      //System.out.println("the Artemis Request is Success,statusCode:" + statusCode + " SuccessMsg:" + response.getBody());
    }
    else
    {
      String msg = response.getErrorMessage();
      responseStr = response.getBody();
      
      //System.out.println("the Artemis Request is Failed,statusCode:" + statusCode + " errorMsg:" + msg);
    }
    return responseStr;
  }
}
