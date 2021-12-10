package cn.ffcs.zhsq.jiangyinPlatform.recentContact.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;

/**
 * @Description: 最近联系人表模块服务
 * @Author: 林树轩
 * @Date: 03-16 08:49:14
 * @Copyright: 2020 福富软件
 */
public interface IRecentContactService {

	/**
	 * 新增数据
	 * @param bo 最近联系人表业务对象
	 * @return 最近联系人表id
	 */
	public String insert(RecentContact bo);

	/**
	 * 删除数据
	 * @param bo 最近联系人表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(RecentContact bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 最近联系人表分页数据对象
	 */
	public List<RecentContact> searchList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 最近联系人表id
	 * @return 最近联系人表业务对象
	 */
	public RecentContact searchById(String id);

}