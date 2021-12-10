package cn.ffcs.zhsq.event.service.stub.lingwu;/**
 * Created by Administrator on 2017/6/20.
 */

import cn.ffcs.shequ.utils.*;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.zhsq.event.service.stub.gansu.QuartzJob;
import cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-06-20 14:55
 **/
public class LingwuTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private String uri = "http://221.228.70.21:90/mobile/zhzf/sjsb/saveExtEReport.jsp";
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><auth><username>username</username><password>password</password></auth><event><eventCode>20170707_0701_00002</eventCode><gridCode>620102</gridCode><gridName>测试街道</gridName><eventName>dd</eventName><happenTimeStr>2017-07-07 17:52:50</happenTimeStr><occurred>dd</occurred><content>dd</content><handleDate>2017-07-17 17:25:20</handleDate><oppoSideBusiCode>8864</oppoSideBusiCode><urgency>01</urgency><influence>01</influence><contactUserName>城关区</contactUserName><contactTel>13400235188</contactTel><creatorName>城关区</creatorName><registerTimeStr>2017-07-07 17:52:50</registerTimeStr><advice>处理意见</advice></event></data>";


    @Test
    public void registerTest(){
//        String s = "2017/10/24 10:30:44";
//        Date date = DateUtils.formatDate(s, DateUtils.PATTERN_24TIME);
//        System.out.println(date);



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gridCode2", "1");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("gridCode", "1");
        jsonObject1.put("jsonArray",jsonArray);

        System.out.println(jsonObject1.toString());
    }

    @Test
    public void quertzTest() throws Exception {
        String doPost = HttpUtil.sendPost(uri,"data="+xml);
        System.out.println(doPost);
//        URLEncoder.encode(uri + "?data="+xml, "utf-8");
//        String doPost = HttpUtil.doPost(uri,"?data="+xml);
//        System.out.println(doPost);
//        Map<String, String> xmlData = new HashMap<String, String>();
//        xmlData.put("data",xml);
//        String s = HttpClientUtil.sendHttpsRequestByPost(uri, xmlData);
//        System.out.println(s);
//        Map map = HttpUtils.doPostRequest(uri, "?data=" + xml, null, null, null);
//        System.out.println(map);
//        Map map1 = HttpUtils.doGetRequest(uri + "?data=" + xml);
//        System.out.println(map1);
    }

    @Test
    public void test1(){
        String path = "http://event.bug.aishequ.org/zhsq_event/images/404.png";
        String encode = "";

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            encode = encoder.encode(getData);
            System.out.println(encode);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
