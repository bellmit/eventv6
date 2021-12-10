package cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.addressBook;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook.JYAddressBook;


/**
 * @Description: 通讯录表模块dao接口
 * @Author: 林树轩
 * @Date: 03-06 17:20:57
 * @Copyright: 2020 福富软件
 */
public interface JYAddressBookMapper {
	
	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	public Long insert(JYAddressBook bo);
	
	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 修改的记录数
	 */
	public long update(JYAddressBook bo);
	
	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 删除的记录数
	 */
	public long delete(JYAddressBook bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 通讯录表数据列表
	 */
	public List<JYAddressBook> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 通讯录表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务UUID查询数据
	 * @param UUID 通讯录表UUID
	 * @return 通讯录表业务对象
	 */
	public JYAddressBook searchByUUID(String UUID);
	
	/**
	 * 判重
	 * @param params 查询参数
	 * @return 返回记录数
	 */
	public long checkExist(Map<String, Object> params);

	/**
	 * 查询数据（不分页）
	 * @param params 查询参数
	 * @return 通讯录表数据列表
	 */
	public List<JYAddressBook> searchListNoPagination(Map<String, Object> params);
	
	/**
	 * 根据手机号码和创建人查询数据
	 */
	public JYAddressBook searchByTelAndCreator(Map<String, Object> params);
}