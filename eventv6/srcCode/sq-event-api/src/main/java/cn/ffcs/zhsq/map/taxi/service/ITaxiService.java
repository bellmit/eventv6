package cn.ffcs.zhsq.map.taxi.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarInfo;

public interface ITaxiService {

	/**
	 * 批量保存
	 * @param list
	 * @return
	 */
	public Long batchMergeCarInfo(List<CarInfo> list);
	
	/**
	 * 获取出租车分页数据
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findTaxiPage(int pageNo, int pageSize,Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 公交线路信息表id
	 * @return 公交线路信息表业务对象
	 */
	public CarInfo searchCarInfoById(Long id);
}
