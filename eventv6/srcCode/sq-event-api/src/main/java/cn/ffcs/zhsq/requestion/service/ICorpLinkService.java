package cn.ffcs.zhsq.requestion.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.requestion.CorpLink;

import java.util.Map;

/**
 * @Description: 企业联动单位模块服务
 * @Author: caiby
 * @Date: 03-12 10:28:45
 * @Copyright: 2018 福富软件
 */
public interface ICorpLinkService {

	/**
	 * 新增数据
	 * @param bo 企业联动单位业务对象
	 * @return 企业联动单位id
	 */
	public Long insert(CorpLink bo);

	/**
	 * 修改数据
	 * @param bo 企业联动单位业务对象
	 * @return 是否修改成功
	 */
	public boolean update(CorpLink bo);

	/**
	 * 删除数据
	 * @param bo 企业联动单位业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(CorpLink bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 企业联动单位分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 企业联动单位id
	 * @return 企业联动单位业务对象
	 */
	public CorpLink searchById(Long id);
	/**
	 * 根据业务gridId查询数据
	 * @param gridId 组织树选择的单位gridId
	 * @return 企业联动单位业务对象
	 */
	public CorpLink searchByGridId(Long id);

}