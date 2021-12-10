package cn.ffcs.zhsq.wellKnownTrademark.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark.Trademark;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by 张天慈 on 2017/12/15.
 */
public interface TrademarkService {

	/**
	 * 统计商标总记录数
	 *@param params 查询条件
	 *@return 商标总记录数
	 */
	public long countList(Map<String,Object> params);

	/**
	 * 查询商标列表
	 * @param page
	 * @param rows
	 * @param params 查询条件
	 * @return List<Trademark>
	 */
	public List<Trademark> findTrademarkList(int page, int rows, Map<String,Object> params);

	/**
	 * 查询商标信息（分页）
	 * @param params 查询参数
	 * @return 商标分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String,Object> params);

	/**
	 * 新增商标信息
	 */
	public boolean insert(HttpSession session, Trademark trademark);

	/**
	 * 修改设备信息
	 */
	public boolean update(HttpSession session,Trademark trademark);

	/**
	 * 根据商标ID查找商标信息
	 */
	public Trademark findById(Long id);

	/**
	 * 删除商标信息
	 */
	public boolean delete(Trademark trademark);
}
