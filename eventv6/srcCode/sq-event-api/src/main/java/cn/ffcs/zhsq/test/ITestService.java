package cn.ffcs.zhsq.test;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;

import java.util.List;

/**
 * 测试服务接口
 *
 * @Author sulch
 * @Date 2017-01-19 8:53
 */
public interface ITestService {
    public List<ArcgisInfoOfGrid> getMixedGridInfoByLocation(String lon, String lat);
}
