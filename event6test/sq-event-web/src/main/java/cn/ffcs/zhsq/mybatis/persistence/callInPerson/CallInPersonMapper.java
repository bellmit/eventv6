package cn.ffcs.zhsq.mybatis.persistence.callInPerson;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;

/**
 * @Description: 主叫人员管理模块dao接口
 * @Author: linwd
 * @Date: 04-12 14:55:55
 * @Copyright: 2018 福富软件
 */
public interface CallInPersonMapper {
	
	/**
	 * 新增数据
	 * @param bo 主叫人员管理业务对象
	 * @return 主叫人员管理id
	 */
	public long insert(CallInPerson bo);
	
	/**
	 * 修改数据
	 * @param bo 主叫人员管理业务对象
	 * @return 修改的记录数
	 */
	public long update(CallInPerson bo);
	
	/**
	 * 删除数据
	 * @param bo 主叫人员管理业务对象
	 * @return 删除的记录数
	 */
	public long delete(CallInPerson bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 主叫人员管理数据列表
	 */
	public List<CallInPerson> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 主叫人员管理数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	public CallInPerson searchById(Long id);

	/**
	 * 根据参数
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	public List<CallInPerson> searchByParams(Map<String, Object> params);

}