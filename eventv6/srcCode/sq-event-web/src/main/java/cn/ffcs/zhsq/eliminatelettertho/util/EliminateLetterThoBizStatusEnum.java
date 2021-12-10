package cn.ffcs.zhsq.eliminatelettertho.util;

/**
 * @author liangbzh
 * @create 2021-08-16 15:12
 * 描述：三书一函业务状态
 */
public enum EliminateLetterThoBizStatusEnum {

    DRAFT("草稿", "99"),                                         //制发单位保存任务，不派发
    WAITING_FEEDBACK_INDUSTRY_SECTOR("待反馈", "01"),            //制发单位派发给行业部门
    PENDING_REVIEW_ISSUING_UNIT("待审核", "02"),                 //行业部门完成表单，提交给制发单位
    PENDING_REVIEW_SAME_LEVEL_CRACK_DOWN("待审核", "03"),        //制发单位审核通过提交到同级扫黑办
    PENDING_REVIEW_COUNTY_CRACK_DOWN("待审核", "04"),            //县级扫黑办提交到市级扫黑办
    FILE("归档", "05"),                                          //市级扫黑办审核通过
    WAITING_FEEDBACK_ISSUING_UNIT_COUNTY("待反馈", "06"),        //制发单位审核不通过
    FAIL_TO_FINISH_COUNTY("不通过完结", "07"),                    //县级扫黑办不通过完结
    FAIL_TO_FINISH_CITY("不通过完结", "08"),                      //市级扫黑办不通过完结
    FAIL_TO_FINISH_PROVINCE("不通过完结", "09")                   //省级扫黑办不通过完结
    ;

    private String name; //名称

    private String code; //编码

    private EliminateLetterThoBizStatusEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
