package cn.ffcs.zhsq.eliminatelettertho.util;

/**
 * @author liangbzh
 * @create 2021-08-18 10:31
 * 描述：三书一函专业类型
 */
public enum EliminateLetterThoProfessionalTypeEnum {

    BASE("三书一函专业", "SSYH_SSX_ZY"),
    ISSUING_UNIT("制发单位专业", "SSYH_ZFDW_ZY"),
    INDUSTRY_SECTOR("行业部门专业", "SSYH_HYBM_ZY"),
    ANTI_MAFIA_OFFICE("扫黑办专业", "shbzy");

    private String name; //名称

    private String code; //编码

    private EliminateLetterThoProfessionalTypeEnum(String name, String code) {
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
