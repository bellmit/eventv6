package cn.ffcs.zhsq.callInPerson.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;

/**
 * @Description: 主叫人员管理模块服务
 * @Author: linwd
 * @Date: 04-12 14:55:55
 * @Copyright: 2018 福富软件
 */
public interface CallInPersonService {

	/**
	 * 新增数据
	 * @param bo 主叫人员管理业务对象
	 * @return 主叫人员管理id
	 */
	public Long insert(CallInPerson bo);
	
	/**
	 * 新增数据(判断电话号码有没有重复)
	 * @param bo 主叫人员管理业务对象
	 * @return 主叫人员管理id
	 */
	public Long insertCheckMobile(CallInPerson bo);

	/**
	 * 修改数据
	 * @param bo 主叫人员管理业务对象
	 * @return 是否修改成功
	 */
	public boolean update(CallInPerson bo);

	/**
	 * 删除数据
	 * @param bo 主叫人员管理业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(CallInPerson bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 主叫人员管理分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	public CallInPerson searchById(Long id);
	
	/**
	 * 根据参数查询数据
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	public List<CallInPerson> searchByParams(Map<String, Object> params);

}