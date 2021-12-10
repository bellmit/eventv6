package cn.ffcs.common;/**
 * Created by Administrator on 2017/4/6.
 */

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import org.codehaus.jackson.map.ser.StdSerializers;

import java.lang.reflect.Type;

/**
 * @author zhongshm
 * @create 2017-04-06 11:03
 **/
public class GsonUtils {
    /**
     * @Title: toJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param bean
     * @return String 返回类型
     * @throws：
     */
    public static String toJson(Object bean){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new StdSerializers.UtilDateSerializer())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.toJson(bean);
    }

    public static String toJson(Object bean,Type type){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new StdSerializers.UtilDateSerializer())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.toJson(bean, type);
    }

    /**
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param json
     * @param type
     * @return T 返回类型
     * @throws：
     */
    public static Object fromJson(String json,Type type){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new StdSerializers.UtilDateSerializer())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.fromJson(json, type);
    }

    /**
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param <T>
     * @param json
     * @param classOfT
     * @return T 返回类型
     * @throws：
     */
    public  static <T>T fromJson(String json,Class<T> classOfT){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new StdSerializers.UtilDateSerializer())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.fromJson(json, classOfT);
    }
}
