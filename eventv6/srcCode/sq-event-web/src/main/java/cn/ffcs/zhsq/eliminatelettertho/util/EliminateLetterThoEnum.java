package cn.ffcs.zhsq.eliminatelettertho.util;

/**
 * @author linwei10
 * @create 2021-08-10 10:31
 * 描述：三书一函流程环节
 */
public enum EliminateLetterThoEnum {

    START_DEPT_DISPATCH("制发单位发起", "task1"),
    INDUSTRY_FEEDBACK("行业部门反馈", "task2"),
    START_DEPT_AUDIT("制发单位审核", "task3"),
    COUNTY_AUDIT("县级扫黑办审核", "task4"),
    CITY_AUDIT("市级扫黑办审核", "task5"),
    PROVINCE_AUDIT("省级制发单位审核", "task6"),
    SUC_END("归档", "end1"),
    FAIL_END("不通过完结", "end2");

    private String name; //名称

    private String code; //编码

    private EliminateLetterThoEnum(String name, String code) {
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
