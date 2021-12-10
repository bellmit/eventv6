package cn.ffcs.zhsq.event.service.stub.nanchang;/**
 * Created by Administrator on 2018/4/27.
 */

import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;
import net.sf.json.JSONObject;

/**
 * 测试类
 *
 * @author zhongshm
 * @create 2018-04-27 14:13
 **/
public class Test {



    @org.junit.Test
    public void testConn(){
        String result = "{\"resData\":{\"returnInfo\":{\"formId\":\"2018050216334933492387087\",\"operCode\":\"1\",\"operMsg\":\"操作成功!\"},\"system\":{\"code\":\"1\",\"msg\":\"调用成功!\"}}}";
        JSONObject jsonObject = JSONObject.fromObject(result);
        System.out.println(jsonObject.get("resData").toString());
    }
    @org.junit.Test
    public void testConn2(){
        String url = "http://12345.yw.nc.gov.cn/nc12345/inter/order/addNetWorkOrder.inter?action=addNetWorkOrder";
        String param = "{\"reqData\":{\"paras\":{\"formOrigin\":\"综治网格化\",\"contentText\":\"工单内容\",\"cusType\":\"1\",\"cusSex\":\"1\",\"cusName\":\"张三\",\"cusPhone\":\"13333333333\",\"cusAddress\":\"市民地址\",\"idCardNum\":\"340827198006300089\",\"whConPerInfo\":\"1\",\"areaCode\":\"南昌市\"}}}";

        APIHttpClient ac = new APIHttpClient(url);

        String result = ac.post(param);

        System.out.println("=====================================");
        System.out.println(result);
        System.out.println("=====================================");

    }
    @org.junit.Test
    public void testConn1(){
        String url = "http://12345.yw.nc.gov.cn/nc12345/inter/order/addNetWorkOrder.inter?action=addNetWorkOrder";

        String param = "{\"reqData\":{\"paras\":{\"formOrigin\":\"工单来源\",\"contentText\":\"工单内容\",\"cusType\":\"客户类型\",\"cusSex\":\"客户性别\",\"cusName\":\"市民姓名\",\"cusPhone\":\"联系号码\",\"cusAddress\":\"市民地址\",\"idCardNum\":\"身份证号码\",\"whConPerInfo\":\"是否公开市民信息\",\"areaCode\":\"区域编码\",\"formAttachList\":[{\"attachName\":\"附件名称\",\"filePath\":\"路径\"},{\"attachName\":\"附件名称\",\"filePath\":\"路径\"}]}}}";

        String doPost = HttpUtil.doPost(url,param);
        System.out.println("=====================================");
        System.out.println(doPost);
        System.out.println("=====================================");

    }
}
