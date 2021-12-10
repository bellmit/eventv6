package cn.ffcs.zhsq.mybatis.persistence.addressBook;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;

import java.util.List;
import java.util.Map;

/**
 * @Description: 通讯录表模块dao接口
 * @Author: linwd
 * @Date: 04-13 15:24:43
 * @Copyright: 2018 福富软件
 */
public interface AddressBookMapper {
	
	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	public long insert(AddressBook bo);
	
	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 修改的记录数
	 */
	public long update(AddressBook bo);
	
	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 删除的记录数
	 */
	public long delete(AddressBook bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 通讯录表数据列表
	 */
	public List<AddressBook> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 通讯录表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 通讯录表id
	 * @return 通讯录表业务对象
	 */
	public AddressBook searchById(Long id);

	/**
	 * 
	 * 根据联动单位ldId查询数据
	 * @author zhangzhenhai
	 * @date 2018-4-27 上午9:36:32
	 * @param @param ldId
	 * @param @return    
	 * @return List<AddressBook>
	 */
	public List<AddressBook> searchListByLdId(Long ldId);

	public List<AddressBook> searchByLdId(Long ldId);

}