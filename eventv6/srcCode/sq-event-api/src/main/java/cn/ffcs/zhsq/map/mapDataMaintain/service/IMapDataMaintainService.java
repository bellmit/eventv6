package cn.ffcs.zhsq.map.mapDataMaintain.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;

import java.util.Map;

/**
 * 地图数据维护接口服务
 *
 * @Author sulch
 * @Date 2018-05-29 16:29
 */
public interface IMapDataMaintainService {
    /**
     * 获取网格分页信息
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findGridInfoPagination(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 获取市政设施列表
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findResInfoPagination(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 获取图层菜单配置
     * @param menuCode
     * @param orgCode
     * @return
     */
    public GisDataCfg getGisDataCfgByCode(String menuCode, String orgCode);
}
