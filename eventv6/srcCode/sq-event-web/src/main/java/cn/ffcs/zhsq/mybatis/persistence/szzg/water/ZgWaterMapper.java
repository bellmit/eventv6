package cn.ffcs.zhsq.mybatis.persistence.szzg.water;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.szzg.water.ZgWater;

/**
 * @Description: 水质量模块dao接口
 * @Author: linzhu
 * @Date: 08-01 17:05:32
 * @Copyright: 2017 福富软件
 */
public interface ZgWaterMapper {
	
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int insert(ZgWater bo);
	
	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int update(ZgWater bo);
	
	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int delete(ZgWater bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 数据列表
	 */
	public List<ZgWater> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public int countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public ZgWater searchById(Long id);
	
	/**
	 * 查询指定参数查询数据总数
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public int  findCountByParams(Map<String, Object> params);
	/**
	 * 根据组织编码获取最新水质量列表
	 * @param orgCode
	 * @return
	 */
	public List<ZgWater> findListByMaxEndTime(String orgCode);
	/**
	 * 获取最近14天数据
	 * @param orgCode
	 * @return
	 */
	public List<ZgWater> getZgWaterByDay14(String orgCode);

}