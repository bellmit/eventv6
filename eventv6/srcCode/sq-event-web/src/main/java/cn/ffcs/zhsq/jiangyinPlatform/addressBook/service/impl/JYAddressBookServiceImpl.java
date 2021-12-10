package cn.ffcs.zhsq.jiangyinPlatform.addressBook.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.jiangyinPlatform.addressBook.service.IJYAddressBookService;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.addressBook.JYAddressBook;
import cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.addressBook.JYAddressBookMapper;

/**
 * @Description: 通讯录表模块服务实现
 * @Author: 林树轩
 * @Date: 03-06 17:20:57
 * @Copyright: 2020 福富软件
 */
@Service("jyAddressBookServiceImpl")
@Transactional
public class JYAddressBookServiceImpl implements IJYAddressBookService {

	@Autowired
	private JYAddressBookMapper jyAddressBookMapper; //注入通讯录表模块dao

	/**
	 * 新增数据
	 * @param bo 通讯录表业务对象
	 * @return 通讯录表id
	 */
	@Override
	public String insert(JYAddressBook bo) {
		jyAddressBookMapper.insert(bo);
		return bo.getUuid();
	}

	/**
	 * 修改数据
	 * @param bo 通讯录表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(JYAddressBook bo) {
		long result = jyAddressBookMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 通讯录表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(JYAddressBook bo) {
		long result = jyAddressBookMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 通讯录表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		long count = jyAddressBookMapper.countList(params);
		List<JYAddressBook> list = null;
		if (count > 0) {
			list = jyAddressBookMapper.searchList(params, rowBounds);
//			for (JYAddressBook bo : list) {
//				try {
//	 	 			//替换手机号
//	 	 			StringBuffer telBuffer = new StringBuffer(bo.getTel());
//	 	 			if(telBuffer.length()>=7) {
//	 	 				telBuffer.replace(3, 7, "****");
//	 	 			}
//	 	 			bo.setTel(telBuffer.toString());
//	 	 		} catch (Exception e) {
//	 	 			e.printStackTrace();
//	 	 		}
//			}
		} else {
			list = new ArrayList<JYAddressBook>();
		}
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 通讯录表id
	 * @return 通讯录表业务对象
	 */
	@Override
	public JYAddressBook searchByUUID(String UUID) {
		JYAddressBook bo = jyAddressBookMapper.searchByUUID(UUID);
		return bo;
	}

	@Override
	public boolean checkExist(String tel, Long creator) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tel", tel);
		params.put("creator", creator);
		long count = jyAddressBookMapper.checkExist(params);
		return count > 0;
	}

	@Override
	public List<JYAddressBook> searchListNoPagination(Map<String, Object> params) {
		return jyAddressBookMapper.searchListNoPagination(params);
	}

	@Override
	public JYAddressBook searchByTelAndCreator(Map<String, Object> params) {
		return jyAddressBookMapper.searchByTelAndCreator(params);
	}

}