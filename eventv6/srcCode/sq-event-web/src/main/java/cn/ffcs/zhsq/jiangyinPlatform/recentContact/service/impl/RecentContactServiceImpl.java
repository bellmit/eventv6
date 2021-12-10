package cn.ffcs.zhsq.jiangyinPlatform.recentContact.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.zhsq.jiangyinPlatform.recentContact.service.IRecentContactService;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;
import cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.recentContact.RecentContactMapper;

/**
 * @Description: 最近联系人表模块服务实现
 * @Author: 林树轩
 * @Date: 03-16 08:49:14
 * @Copyright: 2020 福富软件
 */
@Service("recentContactServiceImpl")
@Transactional
public class RecentContactServiceImpl implements IRecentContactService {

	@Autowired
	private RecentContactMapper recentContactMapper; //注入最近联系人表模块dao

	/**
	 * 新增数据
	 * @param bo 最近联系人表业务对象
	 * @return 最近联系人表id
	 */
	@Override
	public String insert(RecentContact bo) {
		recentContactMapper.insert(bo);
		return bo.getRcUuid();
	}

	/**
	 * 删除数据
	 * @param bo 最近联系人表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(RecentContact bo) {
		long result = recentContactMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 最近联系人表分页数据对象
	 */
	@Override
	public List<RecentContact> searchList(Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds(0, 20);
		List<RecentContact> list = recentContactMapper.searchList(params, rowBounds);
		return list;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 最近联系人表id
	 * @return 最近联系人表业务对象
	 */
	@Override
	public RecentContact searchById(String id) {
		RecentContact bo = recentContactMapper.searchById(id);
		return bo;
	}

}