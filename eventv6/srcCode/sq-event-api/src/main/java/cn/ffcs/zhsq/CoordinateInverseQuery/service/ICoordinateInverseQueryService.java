package cn.ffcs.zhsq.CoordinateInverseQuery.service;

import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;

import java.util.List;
import java.util.Map;

/**
 * 通过坐标反向查询接口
 *
 * @Author sulch
 * @Date 2017-02-09 17:17
 */
public interface ICoordinateInverseQueryService {
    /**
     * 通过坐标反向查询所属网格
     * @param params
     * 			x			经度
     * 			y			纬度
     * 			gridLevel	网格层级
     * @return
     */
    public List<CoordinateInverseQueryGridInfo> findGridInfo(Map<String, Object> params);
    
    
    
    /**
     * 通过经纬度，职位编码查询用户列表
     * @param x             经度
     *        y             纬度
     *        orgCode       组织编码（用于查询功能配置获取默认职位编码）       
     * 		  params
     * 			mapt		地图类型（不传则默认为二维地图）
     *          gridLevel	网格层级(不传则默认为3，区县层级)
     *          positionCode  职位编码（不传则默认通过功能配置取）
     * @return
     */
    public List<UserBO> findUserInfoByRegionCodeAndPosition(String x,String y,String orgCode,Map<String, Object> params) throws Exception;
}
