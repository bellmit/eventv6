package cn.ffcs.zhsq.mybatis.persistence.ldOrg;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.ldOrg.LdOrg;

/**
 * @Description: 联动单位管理表模块dao接口
 * @Author: linwd
 * @Date: 04-13 15:21:53
 * @Copyright: 2018 福富软件
 */
public interface LdOrgMapper {
	
	/**
	 * 新增数据
	 * @param bo 联动单位管理表业务对象
	 * @return 联动单位管理表id
	 */
	public long insert(LdOrg bo);
	
	/**
	 * 修改数据
	 * @param bo 联动单位管理表业务对象
	 * @return 修改的记录数
	 */
	public long update(LdOrg bo);
	
	/**
	 * 删除数据
	 * @param bo 联动单位管理表业务对象
	 * @return 删除的记录数
	 */
	public long deleteById(LdOrg bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 联动单位管理表数据列表
	 */
	public List<LdOrg> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 联动单位管理表数据总数
	 */
	public long countList(Map<String, Object> params);
	
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
	
	/**
	 * 批量新增数据
	 * @param tasks
	 * @return
	 */
	public long batchInsert(List<LdOrg> list);

}