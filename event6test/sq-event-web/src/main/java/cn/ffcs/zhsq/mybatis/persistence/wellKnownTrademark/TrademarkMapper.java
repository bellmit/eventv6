package cn.ffcs.zhsq.mybatis.persistence.wellKnownTrademark;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark.Trademark;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/15.
 */
public interface TrademarkMapper extends MyBatisBaseMapper<Trademark> {
	/**
	 * 统计商标数据总数
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
	public List<Trademark> searchList(Map<String,Object> params, RowBounds rowBounds);

	/**
	 * 新增商标信息
	 */
	public int insert(Trademark trademark);

	/**
	 * 修改设备信息
	 */
	public int update(Trademark trademark);

	/**
	 * 根据商标ID查找商标信息
	 */
	public Trademark findById(Long id);

	/**
	 * 删除商标信息
	 */
	public int delete(Trademark trademark);
}
