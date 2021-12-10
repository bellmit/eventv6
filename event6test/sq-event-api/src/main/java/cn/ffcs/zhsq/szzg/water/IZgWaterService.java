package cn.ffcs.zhsq.szzg.water;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.water.ZgWater;

/**
 * @Description: 水质量模块服务
 * @Author: linzhu
 * @Date: 08-01 17:05:32
 * @Copyright: 2017 福富软件
 */
public interface IZgWaterService {

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean insert(ZgWater bo);

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean update(ZgWater bo);

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean delete(ZgWater bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public ZgWater searchById(Long id);
	
	/**
	 * 判断是否存在记录
	 * @param params
	 * @return
	 */
	public boolean isExist(Map<String, Object> params);
	
	/**
	 * 根据组织编码获取最新水质量列表
	 * @param orgCode
	 * @return
	 */
	public List<ZgWater> findListByMaxEndTime(String orgCode);
}
