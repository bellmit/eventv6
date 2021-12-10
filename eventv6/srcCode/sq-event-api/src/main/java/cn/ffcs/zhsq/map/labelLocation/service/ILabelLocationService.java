package cn.ffcs.zhsq.map.labelLocation.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.BuildingLLInfo;

import java.util.Map;

/**
 * 标注地理位置组件服务接口
 * @Author sulch
 * @Date 2016-11-10 14:17
 */
public interface ILabelLocationService {
    /**
     * 获取模块地理位置信息分页信息
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findLabelLocationPagination(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 获取楼宇地理位置标注信息
     * @param params
     * @return
     */
    public BuildingLLInfo findBuildingLLInfo(Map<String, Object> params);
}
