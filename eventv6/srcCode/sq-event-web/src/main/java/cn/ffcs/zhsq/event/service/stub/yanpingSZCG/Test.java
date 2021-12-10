package cn.ffcs.zhsq.event.service.stub.yanpingSZCG;/**
 * Created by Administrator on 2018/2/8.
 */

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.lang.StringUtils;

import javax.xml.namespace.QName;

import java.rmi.RemoteException;

/**
 * 测试
 *
 * @author zhongshm
 * @create 2018-02-08 20:27
 **/
public class Test {

    @org.junit.Test
    public void tt(){
        String xml = "<data><auth><username></username><password></password></auth><event><eventId>48195</eventId><gridCode>320281109</gridCode><orgCode>320281109A03</orgCode><eventName>测试数据</eventName><happenTimeStr>2018-03-23 11:03:10</happenTimeStr><occurred>测试数据</occurred><content>测试数据</content><handleDate>2018-04-03 11:03:23</handleDate><oppoSideBusiCode>48195</oppoSideBusiCode><bizPlatform>042</bizPlatform><eventType>1</eventType><longitude>120.39973122827148</longitude><latitude>31.84801248083496</latitude><urgency>01</urgency><influence>01</influence><source>01</source><creatorName></creatorName><contactUserName>徐洁</contactUserName><contactTel>13812532333</contactTel><registerTimeStr>2018-03-23 11:03:23</registerTimeStr><advice>第三次派发</advice></event></data>";
//        String xml = "<data><auth><username></username><password></password></auth><event><eventId>48141</eventId><closeOrgName>部门处理</closeOrgName><closeDate>2018-04-04 10:49:21</closeDate><advice>结案</advice><bizPlatform>042</bizPlatform></event></data>";
//        String xml = "<data><auth><username></username><password></password></auth><event><eventId>48141</eventId><advice>再次驳回</advice><bizPlatform>042</bizPlatform></event></data>";


        RPCServiceClient serviceClient = null;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            //指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference("http://121.229.39.3:10101/jygaxf/services/messagePush");
            options.setTo(targetEPR);
            //指定sayHello方法的参数值
            Object[] opAddEntryArgs = new Object[] {xml};
            //指定sayHello方法返回值的数据类型的Class对象
            Class[] classes = new Class[] {String.class};
            //指定要调用的sayHello方法及WSDL文件的命名空间 startEvt closeEvt
            QName opAddEntry = new QName("http://util.mbl.com", "startEvt");//rejectEvt closeEvt
            //调用sayHello方法并输出该方法的返回值

            String result = serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0].toString();

            serviceClient.cleanupTransport();  //为了防止连接超时
            System.out.println(result);
        } catch (AxisFault e) {
            e.printStackTrace();
        }
    }

}
