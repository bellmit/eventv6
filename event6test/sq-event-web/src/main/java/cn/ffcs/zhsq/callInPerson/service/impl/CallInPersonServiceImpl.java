package cn.ffcs.zhsq.callInPerson.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.callInPerson.service.CallInPersonService;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;
import cn.ffcs.zhsq.mybatis.persistence.callInPerson.CallInPersonMapper;

/**
 * @Description: 主叫人员管理模块服务实现
 * @Author: linwd
 * @Date: 04-12 14:55:55
 * @Copyright: 2018 福富软件
 */
@Service("callInPersonServiceImpl")
@Transactional
public class CallInPersonServiceImpl implements CallInPersonService {

	@Autowired
	private CallInPersonMapper callInPersonMapper; //注入主叫人员管理模块dao

	/**
	 * 新增数据
	 * @param bo 主叫人员管理业务对象
	 * @return 主叫人员管理id
	 */
	@Override
	public Long insert(CallInPerson bo) {
		callInPersonMapper.insert(bo);
		return bo.getCpId();
	}

	/**
	 * 新增数据(判断电话号码有没有重复)
	 * @param bo 主叫人员管理业务对象
	 * @return 主叫人员管理id
	 */
	@Override
	public Long insertCheckMobile(CallInPerson bo) {
		Map<String, Object> params = new HashMap<>();
		params.put("cpMobile",bo.getCpMobile());
		List<CallInPerson> info = callInPersonMapper.searchByParams(params);
		if (info == null || info.size() > 0 ) {
			callInPersonMapper.insert(bo);
		}
		return bo.getCpId();
	}
	
	/**
	 * 修改数据
	 * @param bo 主叫人员管理业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(CallInPerson bo) {
		long result = callInPersonMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 主叫人员管理业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(CallInPerson bo) {
		long result = callInPersonMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 主叫人员管理分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<CallInPerson> list = callInPersonMapper.searchList(params, rowBounds);
		long count = callInPersonMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	@Override
	public CallInPerson searchById(Long id) {
		CallInPerson bo = callInPersonMapper.searchById(id);
		return bo;
	}
	
	/**
	 * 根据参数
	 * @param id 主叫人员管理id
	 * @return 主叫人员管理业务对象
	 */
	@Override
	public List<CallInPerson> searchByParams(Map<String, Object> params) {
		List<CallInPerson> list = callInPersonMapper.searchByParams(params);
		return list;
	}

}