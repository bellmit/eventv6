package cn.ffcs.zhsq.szzg.aqi.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAqiStation;

public interface IZgAqiStationService { 
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 空气质量监测站点信息表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	/**
	 * 插入站点
	 * @param zgAqiStation
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean insert(ZgAqiStation zgAqiStation);
	/**
	 * 根据参数查找站点数量
	 * @param params
	 * @return
	 */
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
	 public List<ArcgisInfoOfPublic> getArcgisInfosDataListByIds(String ids, Integer mapt, String markerType);
	 
	 
	 public  ZgAqiStation   searchById(Map<String, Object> params);
	 
	 
		/**
		 * 根据站点获取采集数据
		 * @param statioId
		 * @return
		 */
		public List<Map<String,Object>>  getDataListByStation(Map<String, Object> params);
	 
	 
	 
	
}
