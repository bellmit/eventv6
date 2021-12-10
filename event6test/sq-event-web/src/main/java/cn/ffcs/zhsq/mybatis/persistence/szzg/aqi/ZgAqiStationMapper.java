package cn.ffcs.zhsq.mybatis.persistence.szzg.aqi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAqiStation;

public interface ZgAqiStationMapper extends MyBatisBaseMapper<ZgAqiStation>{
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 空气质量监测站点信息表数据列表
	 */
	public List<ZgAqiStation> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 空气质量监测站点信息表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	
	
	int findCountByParams(Map<String, Object> params);
	/**
	 * 根据参数查找站点列
	 * @param params
	 * @return
	 */
	public List<ZgAqiStation>  getStationListByParams(Map<String, Object> params);
	
	/**
	 * 根据id 和mapt获取 信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	 public List<ArcgisInfoOfPublic> getArcgisInfosDataListByIds(@Param(value = "ids") String ids, @Param(value = "mapt") Integer mapt, @Param(value = "markerType") String markerType);
	 public  ZgAqiStation   searchById(Map<String, Object> params);
	 
	 List<Map<String, Object>> getDataListByStation(Map<String, Object> params);

}
