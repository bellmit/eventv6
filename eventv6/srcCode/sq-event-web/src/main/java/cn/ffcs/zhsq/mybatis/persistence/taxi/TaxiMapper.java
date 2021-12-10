package cn.ffcs.zhsq.mybatis.persistence.taxi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarInfo;

public interface TaxiMapper {

	public Long batchMergeCarInfo(List<CarInfo> list);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 公交线路信息表数据列表
	 */
	public List<CarInfo> searchCarInfoList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 公交线路信息表数据总数
	 */
	public long countCarInfoList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交线路信息表id
	 * @return 公交线路信息表业务对象
	 */
	public CarInfo searchCarInfoById(Long id);
	
}
