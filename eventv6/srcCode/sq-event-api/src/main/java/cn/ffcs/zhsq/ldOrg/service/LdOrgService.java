package cn.ffcs.zhsq.ldOrg.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.ldOrg.LdOrg;

/**
 * @Description: 联动单位管理表模块服务
 * @Author: linwd
 * @Date: 04-13 15:21:53
 * @Copyright: 2018 福富软件
 */
public interface LdOrgService {

	/**
	 * 新增数据
	 * @param bo 联动单位管理表业务对象
	 * @return 联动单位管理表id
	 */
	public Long insert(LdOrg bo);

	/**
	 * 修改数据
	 * @param bo 联动单位管理表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(LdOrg bo);

	/**
	 * 删除数据
	 * @param bo 联动单位管理表业务对象
	 * @return 是否删除成功
	 */
	public boolean deleteById(LdOrg bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 联动单位管理表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 联动单位管理表id
	 * @return 联动单位管理表业务对象
	 */
	public LdOrg searchById(Long id);
	
	/**
	 * 根据单位类型查询数据
	 * @param id 联动单位管理表id
	 * @return 联动单位管理表业务对象
	 */
	public List<LdOrg> searchByLdType(LdOrg bo);

	public void batchInsert(List<LdOrg> list);
}