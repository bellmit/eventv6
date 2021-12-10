package cn.ffcs.zhsq.ldOrg.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ldOrg.service.LdOrgService;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.domain.ldOrg.LdOrg;
import cn.ffcs.zhsq.mybatis.persistence.addressBook.AddressBookMapper;
import cn.ffcs.zhsq.mybatis.persistence.ldOrg.LdOrgMapper;

/**
 * @Description: 联动单位管理表模块服务实现
 * @Author: linwd
 * @Date: 04-13 15:21:53
 * @Copyright: 2018 福富软件
 */
@Service("ldOrgServiceImpl")
@Transactional
public class LdOrgServiceImpl implements LdOrgService {

	@Autowired
	private LdOrgMapper ldOrgMapper; //注入联动单位管理表模块dao
	@Autowired
	private AddressBookMapper addressBookMapper; //注入联动单位通讯录表模块dao

	/**
	 * 新增数据
	 * @param bo 联动单位管理表业务对象
	 * @return 联动单位管理表id
	 */
	@Override
	public Long insert(LdOrg bo) {
		ldOrgMapper.insert(bo);
		return bo.getLdId();
	}

	/**
	 * 修改数据
	 * @param bo 联动单位管理表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(LdOrg bo) {
		long result = ldOrgMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 联动单位管理表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean deleteById(LdOrg bo) {
		long result = ldOrgMapper.deleteById(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 联动单位管理表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<LdOrg> list = ldOrgMapper.searchList(params, rowBounds);
		List<AddressBook> addressBookList = new ArrayList<AddressBook>();
		for (LdOrg ldOrg : list) {
			addressBookList = addressBookMapper.searchListByLdId(ldOrg.getLdId());
			ldOrg.setAddressBookList(addressBookList);
		}
		long count = ldOrgMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 联动单位管理表id
	 * @return 联动单位管理表业务对象
	 */
	@Override
	public LdOrg searchById(Long id) {
		LdOrg bo = ldOrgMapper.searchById(id);
		return bo;
	}

	/**
	 * 根据单位类型查询数据
	 * @param id 联动单位管理表id
	 * @return 联动单位管理表业务对象
	 */
	public List<LdOrg> searchByLdType(LdOrg bo) {
		List<LdOrg> list = ldOrgMapper.searchByLdType(bo);
		return list;
	}
	
	@Override
	public void batchInsert(List<LdOrg> list) {
		ldOrgMapper.batchInsert(list);
	}
}