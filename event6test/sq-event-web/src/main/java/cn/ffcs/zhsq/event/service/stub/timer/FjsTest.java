package cn.ffcs.zhsq.event.service.stub.timer;/**
 * Created by Administrator on 2017/12/7.
 */

import cn.ffcs.common.FileUtils;
import cn.ffcs.common.FtpUtil;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.InputStream;

/**
 * 省环保厅推送事件测试类
 *
 * @author zhongshm
 * @create 2017-12-07 14:58
 **/
public class FjsTest {


    @org.junit.Test
    public void ftpTest(){
        String host = "218.66.59.97";
        int port = 13000;
        String username = "FJHB02";
        String password = "fjhb123";
        String basePath = "/MediaRoot/city_02";

        String filePath = "";
        String filename = "1.jpg";
        String imgUrl = "http://img2.bug.aishequ.org/zzgrid/attachment/2015/06/17/zzgrid-attachment-e2d6523abb50472d8303f3d5e513d7e8.jpg";

        InputStream input= HttpUtil.returnBitMap(imgUrl);
        boolean b = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
        System.out.println(b);
    }


    @org.junit.Test
    public void fjsTest(){
        String url = "http://218.66.59.97:8181/gather/rec";
        JSONObject params = new JSONObject();
        params.put("cityCode","02");
        params.put("token","FJB01");
        params.put("uploadKey",System.currentTimeMillis());

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskNum","20161104_60010101_00003");
        jsonObject.put("sourceID","7225");
        jsonObject.put("regionCode","350200000000");
        jsonObject.put("mainType","1");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
        jsonObject.put("eventDesc","问题描述");
        jsonObject.put("createTime","2016-11-04 10:48:24");
        jsonObject.put("address","详细地址");
        jsonObject.put("patrolID","30014792");
        jsonObject.put("patrolName","张三");
        jsonObject.put("archive","处理结果");
        jsonObject.put("archiveTime","2016-11-04 13:47:32");//结案时间
        jsonArray.add(jsonObject);
        params.put("list",jsonArray.toString());

        String p = "{\"cityCode\":\"03\",\"token\":\"FJC01\",\"uploadKey\":1512629040327,\"list\":[{\"taskNum\":\"20171205_0213_00570\",\"sourceID\":185512,\"regionCode\":\"350605005002\",\"mainType\":\"1\",\"eventDesc\":\"礤头旧村复垦项目工程施工现场\",\"createTime\":\"2017-12-05 23:33:19\",\"address\":\"礤头\",\"patrolName\":\"王辉赐\",\"archive\":\"复垦项目工程施工\",\"archiveTime\":\"2017-12-05 23:33:44\"}]}";
//        APIHttpClient ac = new APIHttpClient(url);
//        String result = ac.post(params.toString());
//        System.out.println(result);

        String str = "{\"cityCode\":\"02\",\"token\":\"FJB01\",\"uploadKey\":1512700020122,\"list\":[{\"sourceID\":30001730,\"sex\":1,\"regionCode\":\"350205100212\",\"inspArea\":\"囷瑶村\",\"inspFreq\":4,\"training\":\"\"}]}";
        String s = HttpUtil.jsonPost(url, str);
        System.out.println(s);
    }

    @org.junit.Test
    public void gridAdminTs(){

        String str = "{\"cityCode\":\"02\",\"token\":\"FJB01\",\"uploadKey\":1512700020122,\"list\":[{\"sourceID\":30001730,\"sex\":1,\"regionCode\":\"350205100212\",\"inspArea\":\"囷瑶村\",\"inspFreq\":4,\"training\":\"\"}]}";
        String s = HttpUtil.jsonPost("http://218.66.59.97:8181/gather/patrol/add", str);
        System.out.println(s);
    }
}
