package cn.ffcs.zhsq.mybatis.persistence.szzg.hydropowerCoal;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.szzg.hydropowerCoal.HydropowerCoal;
/**
 * 居民水电煤模块dao接口
 * @author linzhu
 *
 */
public interface HydropowerCoalMapper {
	
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long insert(HydropowerCoal bo);
	
	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long update(HydropowerCoal bo);
	
	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public long delete(HydropowerCoal bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 数据列表
	 */
	public List<HydropowerCoal> searchList(Map<String, Object> params, RowBounds rowBounds);
	
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
	public HydropowerCoal searchById(Long id);
	
	/**
	 * 查询是否已存在数据
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public int findCountByParams(Map<String, Object> params);
	
	/**
	 * 批量删除信息
	 * @param userId
	 * @param idList
	 * @return
	 */
	 int deleteByIds(@Param(value = "userId")Long userId,@Param(value = "idList") List<Long> idList);
	 
	 /**
	  * 根据年份获取当前年份和上一年的使用记录
	  * @param orgCode
	  * @param year
	  * @param type
	  * @return
	  */
	 public List<HydropowerCoal> getUsageDataByYear(@Param(value = "orgCode")String orgCode,@Param(value = "syear")String syear,@Param(value = "type")String type);
}
