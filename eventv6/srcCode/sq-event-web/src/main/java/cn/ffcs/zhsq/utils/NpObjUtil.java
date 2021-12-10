package cn.ffcs.zhsq.utils;

import java.util.HashMap;
import java.util.Map;

public class NpObjUtil {
    public static final String REGIONCODE = "Nanping_MapCG_Com:regioncode";//资源类型
    public static final String OBJNAME = "Nanping_MapCG_Com:OBJNAME";//资源名称
    public static final String BGCODE = "Nanping_MapCG_Com:BGCODE";//所属网格
    public static final String GEOM = "Nanping_MapCG_Com:the_geom";//坐标
    public static final String OBJCODE = "Nanping_MapCG_Com:OBJCODE";//部件编号
    public static final String ORDATE = "Nanping_MapCG_Com:ORDATE";//创建日期
    public static final String DEPTNAME1 = "Nanping_MapCG_Com:DEPTNAME1";//管理部门
    public static final String CZ = "Nanping_MapCG_Com:CZ";//材质
    public static final String GG = "Nanping_MapCG_Com:GG";//规格
    public static final String ZPLJ = "Nanping_MapCG_Com:ZPLJ";//照片

    public static final Map<String, String> NPOBJMAP = new HashMap<>();

    static {
        NPOBJMAP.put("Nanping_MapCG_Com:regioncode", "REGIONCODE");
        NPOBJMAP.put("Nanping_MapCG_Com:OBJNAME", "OBJNAME");
        NPOBJMAP.put("Nanping_MapCG_Com:BGCODE", "BGCODE");
        NPOBJMAP.put("Nanping_MapCG_Com:the_geom", "GEOM");
        NPOBJMAP.put("Nanping_MapCG_Com:OBJCODE", "OBJCODE");
        NPOBJMAP.put("Nanping_MapCG_Com:ORDATE", "ORDATE");
        NPOBJMAP.put("Nanping_MapCG_Com:DEPTNAME1", "DEPTNAME1");
        NPOBJMAP.put("Nanping_MapCG_Com:CZ", "CZ");
        NPOBJMAP.put("Nanping_MapCG_Com:GG", "GG");
        NPOBJMAP.put("Nanping_MapCG_Com:ZPLJ", "ZPLJ");
    }

    public static final Map<String, String> TYPEMAP = new HashMap<>();

    static {
        TYPEMAP.put("0101", "020107");
    }
}
