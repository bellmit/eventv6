package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.zhsq.event.service.IEventDisposalEsComprehensiveService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:eventDisposalEs综合查询、高亮字段处理
 * @Author: ztc
 * @Date: 2018/9/11 16:48
 */
@Service("eventDisposalEsComprehensiveServiceImpl")
public class EventDisposalEsComprehensiveServiceImpl implements IEventDisposalEsComprehensiveService{

    private static String INDEX = "event";
    private final static String TYPE = "doc";

    /*@Override
    public BoolQueryBuilder getFullQueryCondition(String searchText, Map<String, Object> param) {

        if (param==null || param.get("orgCode")==null) {
            return null;
        }

        //综合查询的条件
        QueryBuilder compreQueryBuilder = null;
        if (searchText!=null && !"".equals(searchText)) {
            compreQueryBuilder = QueryBuilders.matchQuery("comprehensive", searchText);
        }

        //总的查询条件
        BoolQueryBuilder boolQueryEventDisposal = new BoolQueryBuilder();

        //查询限制条件1：必须是当前组织下的
        QueryBuilder orgCodeQueryBuilder = QueryBuilders.matchPhraseQuery("gridCode",param.get("orgCode"));
        //--关联子查询（暂无）

        //查询限制条件2：必须是有效数据
        QueryBuilder statusReceivedBuilder = QueryBuilders.termQuery("status", ConstantValue.EVENT_STATUS_RECEIVED);
        QueryBuilder statusReportBuilder = QueryBuilders.termQuery("status", ConstantValue.EVENT_STATUS_REPORT);
        QueryBuilder statusDistributeBuilder = QueryBuilders.termQuery("status", ConstantValue.EVENT_STATUS_DISTRIBUTE);
        QueryBuilder statusArchiveBuilder = QueryBuilders.termQuery("status", ConstantValue.EVENT_STATUS_ARCHIVE);
        QueryBuilder statusEndBuilder = QueryBuilders.termQuery("status", ConstantValue.EVENT_STATUS_END);

        //情况一：查询主表数据基本信息
        BoolQueryBuilder boolQueryParentPart = new BoolQueryBuilder();
        if (compreQueryBuilder!=null) {
            boolQueryParentPart.must(compreQueryBuilder);//查询的字段
        }

        boolQueryParentPart.must(orgCodeQueryBuilder);

        //多个有效状态，先封装在should里面，最后在总的查询里面用must封装
        BoolQueryBuilder statusQueryPart = new BoolQueryBuilder();

        statusQueryPart.should(statusReceivedBuilder);
        statusQueryPart.should(statusReportBuilder);
        statusQueryPart.should(statusDistributeBuilder);
        statusQueryPart.should(statusArchiveBuilder);
        statusQueryPart.should(statusEndBuilder);
        boolQueryParentPart.must(statusQueryPart);

        boolQueryEventDisposal.should(boolQueryParentPart);

        return boolQueryEventDisposal;
    }*/

    /*@Override
    public Set<String> getHighlightFields() {
        Set<String> highlighFields = new HashSet<>();

        highlighFields.add("eventName");
        highlighFields.add("content");
        highlighFields.add("occurred");
        highlighFields.add("createTime");
        highlighFields.add("happenTime");
        highlighFields.add("contactUser");
        highlighFields.add("tel");
        highlighFields.add("typeCN");
        highlighFields.add("sourceCN");
        highlighFields.add("influenceDegreeCN");
        highlighFields.add("urgencyDegreeCN");
        highlighFields.add("attrFlagMCN");
        highlighFields.add("statusCN");

        return highlighFields;
    }*/

    /*@Override
    public String getText(IRestClientFactory restClientFactory, SearchHit hit, String searchText) {

        //综合查询的条件
        QueryBuilder compreQueryBuilder = null;
        if (searchText!=null && !"".equals(searchText)) {
            compreQueryBuilder = QueryBuilders.matchQuery("comprehensive", searchText);
        }

        //处理高亮字段，拼接
        //所有字段
        Map<String,Object> sourceAsMap = hit.getSourceAsMap();
        //高亮字段
        Map<String,HighlightField> highlightFields = hit.getHighlightFields();
        Map<String,Object> highlightMap = new HashMap<>();

        if (highlightFields.size() > 0) {
            for(String key:highlightFields.keySet()){
                HighlightField highlightField = highlightFields.get(key);
                Text[] fragments = highlightField.fragments();
                String fragmentString = fragments[0].string();
                highlightMap.put(key,fragmentString);
            }
        }

        //查询内容不为空，才显示(查询子关联高亮字段，暂无)
        *//*if (compreQueryBuilder != null) {

        }*//*

        StringBuffer stringBuffer = new StringBuffer();
        *//*
        * 根据需要，展示的字段定义
        * *//*
        //事件标题
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"eventName","事件标题",true);
        //事件描述
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"content","事件描述",true);
        //事发详址
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"occurred","事发详址",true);
        //创建时间
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"createTime","创建时间",true);
        //事发时间
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"happenTime","事发时间",true);
        //联系人员
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"contactUser","联系人员",true);
        //联系电话
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"tel","联系电话",true);
        //事件分类
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"typeCN","事件分类",true);
        //信息来源
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"sourceCN","信息来源",true);
        //影响范围
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"influenceDegreeCN","影响范围",true);
        //紧急程度
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"urgencyDegreeCN","紧急程度",true);
        //附件类型
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"attrFlagMCN","附件类型",false);
        //当前状态
        setOneFieldStr(stringBuffer,highlightMap,sourceAsMap,"statusCN","当前状态",true);

        //去掉最后一个逗号
        stringBuffer.deleteCharAt(stringBuffer.length()-1);

        return stringBuffer.toString();
    }*/

    /*@Override
    public String getTitle(SearchHit hit) {

        Map<String,HighlightField> highlightFieldMap = hit.getHighlightFields();
        if (highlightFieldMap.size() > 0) {
            HighlightField highlightField = highlightFieldMap.get("eventName");;
            if (highlightField != null) {
               Text[] fragments = highlightField.fragments();
               String title =  fragments[0].string();
               return title;
            }
        }
        //所有字段
        Map<String,Object> sourceFields = hit.getSourceAsMap();
        String title = "";
        if (CommonFunctions.isNotBlank(sourceFields,"eventName")) {
            title =  sourceFields.get("eventName").toString();
        } else {
            throw new IllegalArgumentException("获取事件标题出错，请检查参数 ：eventName ！");
        }

        return title;
    }
*/
    /**
    * 设置字段的字符串样式
     * @param stringBuffer
     * @param highlightMap
     * @param sourceAsMap
     * @param fielsName
     * @param chinese
     * @param isNotHighShow 如果没有被高亮要不要显示
     * @return
    * */
    private StringBuffer setOneFieldStr(StringBuffer stringBuffer,Map<String, Object> highlightMap,
                                        Map<String, Object> sourceAsMap,String fielsName,String chinese,boolean isNotHighShow){
        //高亮字段、一定显示
        if (CommonFunctions.isNotBlank(highlightMap,fielsName)) {
            stringBuffer.append(chinese + "：" + highlightMap.get(fielsName) + ",");
        } else if (isNotHighShow && CommonFunctions.isNotBlank(sourceAsMap,fielsName)) {
            //不是高亮字段、根据配置决定要不要显示
            stringBuffer.append(chinese + "：" + (sourceAsMap.get(fielsName)) + ",");
        }

        return stringBuffer;
    }
}
