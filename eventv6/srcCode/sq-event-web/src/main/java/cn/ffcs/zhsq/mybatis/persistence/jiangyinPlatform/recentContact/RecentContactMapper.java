package cn.ffcs.zhsq.mybatis.persistence.jiangyinPlatform.recentContact;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;

/**
 * @Description: 最近联系人表模块dao接口
 * @Author: 林树轩
 * @Date: 03-16 08:49:14
 * @Copyright: 2020 福富软件
 */
public interface RecentContactMapper {
	
	/**
	 * 新增数据
	 * @param bo 最近联系人表业务对象
	 * @return 最近联系人表id
	 */
	public long insert(RecentContact bo);
	
	/**
	 * 删除数据
	 * @param bo 最近联系人表业务对象
	 * @return 删除的记录数
	 */
	public long delete(RecentContact bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 最近联系人表数据列表
	 */
	public List<RecentContact> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 根据业务id查询数据
	 * @param id 最近联系人表id
	 * @return 最近联系人表业务对象
	 */
	public RecentContact searchById(String id);

}