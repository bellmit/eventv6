package cn.ffcs.zhsq.mybatis.persistence.administrationPenalty;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.administrationPenalty.AdministrationPenalty;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/5.
 */
public interface AdministrationPenaltyMapper extends MyBatisBaseMapper<AdministrationPenalty> {

	/**
	 * 统计行政处罚数据总数
	 *@param params 查询参数
	 *@return 商标总记录数
	 */
	public long countList(Map<String,Object> params);

	/**
	 *查询所有的商标信息（用于分页）
	 *@param params 查询参数
	 *@param rowBounds 分页信息
	 *@return List<Trademark> 所有商标信息
	 */
	public List<AdministrationPenalty> searchList(Map<String,Object> params, RowBounds rowBounds);

	/**
	 * 新增商标信息
	 */
	public int insert(AdministrationPenalty penalty);

	/**
	 * 修改设备信息
	 */
	public int update(AdministrationPenalty penalty);

	/**
	 * 根据商标ID查找商标信息
	 */
	public AdministrationPenalty findById(Long id);

	/**
	 * 删除商标信息
	 */
	public int delete(AdministrationPenalty penalty);

	/**
	 * 根据年份，月份统计处罚信息
	 */
	public List<AdministrationPenalty> findPenaltyByYM(Map<String,Object> param);

	/**
	 * 查询12个月
	 */
	public List<Map<String,Object>> findChart12Month(Map<String,Object> param);
}
