package cn.ffcs.zhsq.addressBook.service.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.addressBook.service.AddressBookService;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.persistence.addressBook.AddressBookMapper;

/**
 * @Description: 通讯录表模块服务实现
 * @Author: linwd
 * @Date: 04-13 15:24:43
 * @Copyright: 2018 福富软件
 */
@Service("addressBookServiceImpl")
@Transactional
public class AddressBookServiceImpl implements AddressBookService {

	@Autowired
	private AddressBookMapper addressBookMapper; //注入通讯录表模块dao

	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	@Override
	public Long insert(AddressBook bo) {
		addressBookMapper.insert(bo);
		return bo.getAbId();
	}

	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(AddressBook bo) {
		long result = addressBookMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(AddressBook bo) {
		long result = addressBookMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯录表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<AddressBook> list = addressBookMapper.searchList(params, rowBounds);
		long count = addressBookMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 通讯录表id
	 * @return 通讯录表业务对象
	 */
	@Override
	public AddressBook searchById(Long id) {
		AddressBook bo = addressBookMapper.searchById(id);
		return bo;
	}

	/**
	 * 根据联动单位主键ldId查询数据
	 * @param ldId 联动单位主键id
	 * @return 通讯录表业务对象list
	 */
	@Override
	public List<AddressBook> searchByLdId(Long ldId) {
		List<AddressBook> addressBookList = addressBookMapper.searchByLdId(ldId);
		return addressBookList;
	}

}