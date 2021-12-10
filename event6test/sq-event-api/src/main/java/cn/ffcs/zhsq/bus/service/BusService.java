package cn.ffcs.zhsq.bus.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Busline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarBusline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarLastGps;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Station;
import cn.ffcs.zhsq.mybatis.domain.map.bus.StationBusline;

/**
 * 公交车服务
 * @author zkongbai
 *
 */
public interface BusService {

	public Long batchMergeBusline(List<Busline> list);
	public Long batchMergeCarBusline(List<CarBusline> list);
	public Long batchMergeCarLastGps(List<CarLastGps> list);
	public Long batchMergeStation(List<Station> list);
	public Long batchMergeStationBusline(List<StationBusline> list);
	
	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交线路信息表数据列表
	 */
	public List<Busline> searchBuslineList(Map<String, Object> params);
	
	/**
	 * 查询数据（分页）
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return 公交线路信息表数据列表
	 */
	public EUDGPagination findBuslinePage(int pageNo, int pageSize,Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交线路信息表id
	 * @return 公交线路信息表业务对象
	 */
	public Busline searchBuslineById(Long id);
	
	
	/**
	 * 查询数据
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交车辆信息表数据列表
	 */
	public List<CarBusline> searchCarBuslineList(Map<String, Object> params);
	
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交车辆信息表id
	 * @return 公交车辆信息表业务对象
	 */
	public CarBusline searchCarBuslineById(Long id);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交车最后位置信息表数据列表
	 */
	public List<CarLastGps> searchCarLastGpsList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交车最后位置信息表id
	 * @return 公交车最后位置信息表业务对象
	 */
	public CarLastGps searchCarLastGpsById(Long id);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交站基础信息表数据列表
	 */
	public List<Station> searchStationList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 根据线路等信息获取站点列表(必传参数buslineId,upDown)
	 * @param params
	 * @return
	 */
	public List<Station> searchStationListByLineInfo(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交站基础信息表id
	 * @return 公交站基础信息表业务对象
	 */
	public Station searchStationById(Long id);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交站公交线路数据映射表数据列表
	 */
	public List<StationBusline> searchStationBuslineList(Map<String, Object> params, RowBounds rowBounds);
	
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交站公交线路数据映射表id
	 * @return 公交站公交线路数据映射表业务对象
	 */
	public StationBusline searchStationBuslineById(Long id);
}
