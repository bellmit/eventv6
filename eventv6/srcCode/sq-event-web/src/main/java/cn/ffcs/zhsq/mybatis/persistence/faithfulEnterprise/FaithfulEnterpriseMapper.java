package cn.ffcs.zhsq.mybatis.persistence.faithfulEnterprise;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise.FaithfulEnterprise;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/20.
 */
public interface FaithfulEnterpriseMapper extends MyBatisBaseMapper<FaithfulEnterprise> {

	/**
	 * 统计守重企业数据总数
	 *@param params 查询参数
	 *@return 守重企业总记录数
	 */
	public long countList(Map<String,Object> params);

	/**
	 *查询所有的守重企业信息（用于分页）
	 *@param params 查询参数
	 *@param rowBounds 分页信息
	 *@return List<faithfulEnterprise> 所有守重企业信息
	 */
	public List<FaithfulEnterprise> searchList(Map<String,Object> params, RowBounds rowBounds);

	/**
	 * 新增商标信息
	 */
	public int insert(FaithfulEnterprise enterprise);

	/**
	 * 修改设备信息
	 */
	public int update(FaithfulEnterprise enterprise);

	/**
	 * 根据商标ID查找商标信息
	 */
	public FaithfulEnterprise findById(Long id);

	/**
	 * 删除商标信息
	 */
	public int delete(FaithfulEnterprise enterprise);
}
