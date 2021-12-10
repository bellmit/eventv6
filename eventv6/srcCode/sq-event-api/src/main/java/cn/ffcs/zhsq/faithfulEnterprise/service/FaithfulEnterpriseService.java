package cn.ffcs.zhsq.faithfulEnterprise.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise.FaithfulEnterprise;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/20.
 */
public interface FaithfulEnterpriseService {

	/**
	 * 统计守重企业总记录数
	 *@param params 查询条件
	 *@return 守重企业总记录数
	 */
	public long countList(Map<String,Object> params);

	/**
	 * 查询守重企业列表
	 * @param page
	 * @param rows
	 * @param params 查询条件
	 * @return List<Trademark>
	 */
	public List<FaithfulEnterprise> findEnterpriseList(int page, int rows, Map<String,Object> params);

	/**
	 * 查询守重企业信息（分页）
	 * @param params 查询参数
	 * @return 守重企业分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String,Object> params);

	/**
	 * 新增守重企业信息
	 */
	public boolean insert(HttpSession session, FaithfulEnterprise enterprise);

	/**
	 * 修改守重企业信息
	 */
	public boolean update(HttpSession session,FaithfulEnterprise enterprise);

	/**
	 * 根据守重企业ID查找守重企业
	 */
	public FaithfulEnterprise findById(Long id);

	/**
	 * 删除守重企业信息
	 */
	public boolean delete(FaithfulEnterprise enterprise);
}
