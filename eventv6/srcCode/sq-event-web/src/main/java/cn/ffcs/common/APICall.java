package cn.ffcs.common;

import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author huangjianming
 * @Date 2021/3/2 14:59
 */
public class APICall {

    /**
     * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
     */
    public static void initCfg(String ip,String port,String appKey,String appSecret){
        ArtemisConfig.host = ip + ":" + port; // artemis网关服务器ip端口
        ArtemisConfig.appKey = appKey;  // 秘钥appkey
        ArtemisConfig.appSecret = appSecret;// 秘钥appSecret
    }

    /**
     * STEP2：设置OpenAPI接口的上下文
     */
    final static String ARTEMIS_PATH = "/artemis";

    public static String startReq(String apiUrl, JSONObject jsonBody) {
        /**
         * STEP3：设置接口的URI地址
         */
        final String api = ARTEMIS_PATH + apiUrl;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", api);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        /*JSONObject jsonBody = new JSONObject();
        int[] arr = new int[]{851969};
        jsonBody.put("eventTypes", arr);
        jsonBody.put("eventDest", "https://ip:port/eventRcv");
        String body = jsonBody.toString();*/

        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody.toString(), null, null, contentType , null);// post请求application/json类型参数
        return result;
    }
}
