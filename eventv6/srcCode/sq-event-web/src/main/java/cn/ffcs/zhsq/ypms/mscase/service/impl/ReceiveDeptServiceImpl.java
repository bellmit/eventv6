package cn.ffcs.zhsq.ypms.mscase.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.addressBook.AddressBook;
import cn.ffcs.zhsq.mybatis.domain.ypms.mscase.ReceiveDept;
import cn.ffcs.zhsq.mybatis.persistence.addressBook.AddressBookMapper;
import cn.ffcs.zhsq.mybatis.persistence.ypms.mscase.ReceiveDeptMapper;
import cn.ffcs.zhsq.ypms.mscase.service.ReceiveDeptService;

/**
 * @Description: 延平民生派发单位表模块服务实现
 * @Author: zhangzhenhai
 * @Date: 04-16 09:33:20
 * @Copyright: 2018 福富软件
 */
@Service("receiveDeptServiceImpl")
@Transactional
public class ReceiveDeptServiceImpl implements ReceiveDeptService {

	@Autowired
	private ReceiveDeptMapper receiveDeptMapper; //注入延平民生派发单位表模块dao
	@Autowired
	private AddressBookMapper addressBookMapper; //注入延平民生派发单位表模块dao

	/**
	 * 新增数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 延平民生派发单位表id
	 */
	@Override
	public Long insert(ReceiveDept bo) {
		receiveDeptMapper.insert(bo);
		return bo.getRdId();
	}

	/**
	 * 修改数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(ReceiveDept bo) {
		long result = receiveDeptMapper.update(bo);
		return result > 0;
	}

	
	/**
	 * 删除数据
	 * @param bo 延平民生派发单位表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(ReceiveDept bo) {
		long result = receiveDeptMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 延平民生派发单位表分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<ReceiveDept> list = receiveDeptMapper.searchList(params, rowBounds);
		long count = receiveDeptMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 延平民生派发单位表id
	 * @return 延平民生派发单位表业务对象
	 */
	@Override
	public ReceiveDept searchById(Long id) {
		ReceiveDept bo = receiveDeptMapper.searchById(id);
		return bo;
	}

	@Override
	public List<ReceiveDept> getReceiveDeptListByRelaCaseId(
			Map<String, Object> params) {
		List<ReceiveDept> receiveDeptList = receiveDeptMapper.getReceiveDeptListByRelaCaseId(params);
		//添加联动单位的联系人list
		for (ReceiveDept receiveDept : receiveDeptList) {
			Long ldId = receiveDept.getLdorgId();
			List<AddressBook> addressBookList = addressBookMapper.searchListByLdId(ldId);
			receiveDept.setAddressBookList(addressBookList);
		}
		
		return receiveDeptList;
	}

	@Override
	public ReceiveDept searchByParams(Map<String, Object> params) {
		ReceiveDept receiveDept = receiveDeptMapper.searchByParams(params);
		return receiveDept;
	}

	@Override
	public Long countUnfinishDept(Map<String, Object> params_c) {
		Long count = receiveDeptMapper.countUnfinishDept(params_c);
		return count;
	}

	@Override
	public List<ReceiveDept> searchListByParams(Map<String, Object> params_e) {
		List<ReceiveDept> list = receiveDeptMapper.getReceiveDeptListByRelaCaseId(params_e);
		return list;
	}

	

}