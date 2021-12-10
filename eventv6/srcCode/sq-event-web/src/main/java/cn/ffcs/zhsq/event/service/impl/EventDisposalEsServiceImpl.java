package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.IEventDisposalEsConnectFactoryService;
import cn.ffcs.zhsq.event.service.IEventDisposalEsService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposalEs;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: ztc
 * @Date: 2018/9/10 15:56
 */
@Service("eventDisposalEsServiceImpl")
public class EventDisposalEsServiceImpl implements IEventDisposalEsService<EventDisposalEs> {

    private final static String INDEX = "event";
    private final static String TYPE = "doc";
    private final static String MODULE = "event";

    @Autowired
    private IEventDisposalEsConnectFactoryService restClientFactory;

    @Autowired
    private EventDisposalMapper eventDisposalMapper;

    @Autowired
    private IBaseDictionaryService dictionaryService;


    //@Override
    public boolean create(EventDisposalEs bean) {
        if (bean == null || bean.getEventId() == null) {
            return false;
        }

        //字典翻译
        this.escapeCode(bean);
        //设置文档类型、关联关系（暂时没有）
        bean.setModuleType(MODULE);
        String jsonStr = JSONObject.toJSONString(bean, SerializerFeature.WriteDateUseDateFormat);
        jsonStr = jsonStr.replaceAll("[\\u0001-\\u001F]", ""); //处理不可见字符
        //JSONObject json = JSONObject.parseObject(jsonStr);

        IndexRequest indexRequest = new IndexRequest(INDEX,TYPE,MODULE + "_" + bean.getEventId());
        indexRequest.source(jsonStr,XContentType.JSON);
        indexRequest.routing(bean.getEventId().toString());

        try {
            IndexResponse indexResponse = restClientFactory.getHighLevelClient().index(indexRequest);

            if(indexResponse.getVersion() >0){
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    //@Override
    public boolean update(EventDisposalEs bean) {

        if (bean == null || bean.getEventId() == null) {
            return false;
        }
        //字典翻译
        this.escapeCode(bean);
        //设置文档类型、关联关系（暂时没有）
        bean.setModuleType(MODULE);
        String jsonStr = JSONObject.toJSONString(bean, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat);

        UpdateRequest updateRequest = new UpdateRequest(INDEX,TYPE,MODULE + "_" + bean.getEventId());
        updateRequest.doc(jsonStr,XContentType.JSON);
        updateRequest.routing(bean.getEventId().toString());

        try {
            UpdateResponse updateResponse = restClientFactory.getHighLevelClient().update(updateRequest);

            if (updateResponse.getVersion() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    //@Override
    public boolean delete(EventDisposalEs bean) {

        if (bean == null || bean.getEventId() == null) {
            return false;
        }

        //有关联的话，先删除子关联（暂时没有）
        //删除
        DeleteRequest deleteRequest = new DeleteRequest(INDEX,TYPE,MODULE + "_" + bean.getEventId());
        deleteRequest.routing(bean.getEventId().toString());

        try {
            DeleteResponse deleteResponse = restClientFactory.getHighLevelClient().delete(deleteRequest);

            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean findAndSave(Long eventId) {

        EventDisposalEs eventDisposalEs = eventDisposalMapper.findEventDisposalEsById(eventId);
        boolean result = this.create(eventDisposalEs);

        if (result) {
            //eventDisposal 新增到 es 后更新判断标志--待定用什么做判定--不做判断标志，根据时间更新和新增
            System.out.println("事件Id为：" + eventId + " 的事件成功保存到 Es。");
        }
        return result;
    }

    @Override
    public boolean findAndUpdate(Long eventId) {

        EventDisposalEs eventDisposalEs = eventDisposalMapper.findEventDisposalEsById(eventId);
        boolean result = this.update(eventDisposalEs);

        if (result) {
            //eventDisposal 更新到 es 后更新判断标志--待定用什么做判定
            System.out.println("事件Id为：" + eventId + " 的事件成功更新到 Es。");
        }
        return result;
    }



    /**
     * 字典翻译
     * */
    private <T> T escapeCode(T t){
        if (t == null) {
            return null;
        }

        String orgCode = "";
        Class<?> clas = t.getClass();

        try {
            Field[] fields = clas.getDeclaredFields();

            for(Field field:fields){
                field.setAccessible(true);
                String str = field.getName();

                if (str.endsWith("CN")) {
                    String name = "";
                    //一对多字段翻译
                    if (str.endsWith("MCN")) {
                        name = str.substring(0,str.length() - 3);
                    } else if (str.endsWith("CN")) {
                    //一对一字段翻译
                        name = str.substring(0,str.length() - 2);
                    }
                    if (StringUtils.isBlank(name)) {
                        continue;
                    }

                    Field coldField = clas.getDeclaredField(name);
                    coldField.setAccessible(true);
                    Object codeObj = coldField.get(t);
                    if (codeObj == null) {
                        continue;
                    }
                    String[] objs = codeObj.toString().split(",");
                    String cnName = "";
                    if (objs.length > 0) {
                        //翻译字典
                        cnName = this.changeCodeToName(objs,name,cnName,orgCode);
                        field.set(t,cnName);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String changeCodeToName(String[] objs,String name,String cnName,String orgCode){

        List<BaseDataDict> dictList = new ArrayList<>();
        String dictPcode = "";
        Map<String,Object> param = new HashMap<>();

        if("type".equals(name)){
            param.put("orgCode",orgCode);
            param.put("dictPcode",ConstantValue.BIG_TYPE_PCODE);
            dictList = dictionaryService.findDataDictListByCodes(param);

            StringBuffer eventClass = new StringBuffer("");
            String bigType = objs[0],bigTypeName = "",bigDictCode = null;
            do {
                bigTypeName = "";

                for(BaseDataDict dataDict : dictList) {
                    if((StringUtils.isNotBlank(bigDictCode) && !ConstantValue.BIG_TYPE_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
                            ||
                            (StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
                        bigTypeName = dataDict.getDictName();
                        bigDictCode = dataDict.getDictPcode();
                        bigType = null;
                        break;
                    }
                }

                if(StringUtils.isNotBlank(bigTypeName)) {
                    eventClass.insert(0, bigTypeName).insert(0, "-");
                }
            } while(StringUtils.isNotBlank(bigTypeName));

            if(eventClass.length() > 0) {
                cnName = eventClass.substring(eventClass.indexOf("-") + 1);
            }

        } else {
            if ("source".equals(name)) {
                dictPcode = ConstantValue.SOURCE_PCODE;
            } else if ("influenceDegree".equals(name)) {
                dictPcode = ConstantValue.INFLUENCE_DEGREE_PCODE;
            } else if ("urgencyDegree".equals(name)) {
                dictPcode = ConstantValue.URGENCY_DEGREE_PCODE;
            } else if ("attrFlag".equals(name)) {
                dictPcode = ConstantValue.ATTR_FLAG_PCODE;
            } else if ("status".equals(name)) {
                dictPcode = ConstantValue.STATUS_PCODE;
            }

            if (StringUtils.isBlank(dictPcode)) {
                throw new IllegalArgumentException("字段： " + name + "的字典编码不能为空，请检查！");
            }

            /*if (StringUtils.isBlank(orgCode)) {
                throw new IllegalArgumentException("参数 orgCode 不能为空，请检查！");
            }*/

            try {
                dictList = dictionaryService.getDataDictListOfSinglestage(dictPcode,orgCode);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dictList.size() > 0) {
                StringBuffer nameValue = new StringBuffer();
                for (String code:objs) {
                    for (BaseDataDict dict:dictList) {
                        if (code.equals(dict.getDictGeneralCode())) {
                            nameValue.append(dict.getDictName()).append(",");
                            break;
                        }
                    }
                }
                cnName = nameValue.substring(0,nameValue.length()-1);
            }
        }

        return cnName;
    }
}
