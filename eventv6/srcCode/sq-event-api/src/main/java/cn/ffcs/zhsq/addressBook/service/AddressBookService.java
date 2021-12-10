package cn.ffcs.zhsq.addressBook.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;

import java.util.List;
import java.util.Map;

/**
 * @Description: 通讯录表模块服务
 * @Author: linwd
 * @Date: 04-13 15:24:43
 * @Copyright: 2018 福富软件
 */
public interface AddressBookService {

	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	public Long insert(AddressBook bo);

	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(AddressBook bo);

	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(AddressBook bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯录表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 通讯录表id
	 * @return 通讯录表业务对象
	 */
	public AddressBook searchById(Long id);
	
	
	/**
	 * 根据联动单位主键ldId查询数据
	 * @author zhangzhenhai
	 * @date 2018-4-26 下午2:19:43
	 * @param @param ldId
	 * @param @return    
	 * @return AddressBook
	 */
	public List<AddressBook> searchByLdId(Long ldId);
	
	

}