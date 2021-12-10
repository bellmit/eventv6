package cn.ffcs.zhsq.mybatis.persistence.szzg.school;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.szzg.school.SchoolStat;

/**
 * @Description: 学校人数统计表模块dao接口
 * @Author: os.wuzhj
 * @Date: 04-04 08:43:01
 * @Copyright: 2019 福富软件
 */
public interface SchoolStatMapper {
	
	/**
	 * 新增数据
	 * @param bo 学校人数统计表业务对象
	 * @return 学校人数统计表id
	 */
	public long insert(SchoolStat bo);
	
	/**
	 * 修改数据
	 * @param bo 学校人数统计表业务对象
	 * @return 修改的记录数
	 */
	public long update(SchoolStat bo);
	
	/**
	 * 删除数据
	 * @param bo 学校人数统计表业务对象
	 * @return 删除的记录数
	 */
	public long delete(SchoolStat bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 学校人数统计表数据列表
	 */
	public List<SchoolStat> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 学校人数统计表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 学校人数统计表id
	 * @return 学校人数统计表业务对象
	 */
	public SchoolStat searchById(Long id);
	
	
	/**
	 * 根据关联id和年份查询数据是否存在
	 * @return 记录数 
	 */
	public int exit(SchoolStat bo);

}