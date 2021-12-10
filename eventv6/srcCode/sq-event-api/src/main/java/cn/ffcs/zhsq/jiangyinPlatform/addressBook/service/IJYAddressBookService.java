package cn.ffcs.zhsq.jiangyinPlatform.addressBook.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook.JYAddressBook;

/**
 * @Description: 通讯录表模块服务
 * @Author: 林树轩
 * @Date: 03-06 17:20:57
 * @Copyright: 2020 福富软件
 */
public interface IJYAddressBookService {

	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	public String insert(JYAddressBook bo);

	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(JYAddressBook bo);

	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(JYAddressBook bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯录表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务UUID查询数据
	 * @param UUID 通讯录表UUID
	 * @return 通讯录表业务对象
	 */
	public JYAddressBook searchByUUID(String UUID);
	
	/**
	 * 判重
	 * @param params 查询参数
	 * @return 是否重复
	 */
	public boolean checkExist(String tel, Long creator);

	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @return 通讯录表数据列表
	 */
	public List<JYAddressBook> searchListNoPagination(Map<String, Object> params);
	
	/**
	 * 根据手机号码查询数据
	 */
	public JYAddressBook searchByTelAndCreator(Map<String, Object> params);
}