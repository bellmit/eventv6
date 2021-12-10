package cn.ffcs.zhsq.mybatis.persistence.requestion;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.requestion.ReqLink;

import java.util.List;
import java.util.Map;

/**
 * @Description: 诉求联动单位模块dao接口
 * @Author: caiby
 * @Date: 03-12 10:09:52
 * @Copyright: 2018 福富软件
 */
public interface ReqLinkMapper {
	
	/**
	 * 新增数据
	 * @param bo 诉求联动单位业务对象
	 * @return 诉求联动单位id
	 */
	public long insert(ReqLink bo);
	
	/**
	 * 修改数据
	 * @param bo 诉求联动单位业务对象
	 * @return 修改的记录数
	 */
	public long update(ReqLink bo);
	
	/**
	 * 删除数据
	 * @param bo 诉求联动单位业务对象
	 * @return 删除的记录数
	 */
	public long delete(ReqLink bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 诉求联动单位数据列表
	 */
	public List<ReqLink> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 诉求联动单位数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 诉求联动单位id
	 * @return 诉求联动单位业务对象
	 */
	public ReqLink searchById(Long id);
	
	/**
	 * 查询数据（）
	 * @param params 查询参数
	 * @return 诉求联动单位数据列表
	 */
	public List<ReqLink> searchList(Map<String, Object> params);
	
	
	public List<Map<String,Object>> queryWFtaskId(Map<String,Object> params);
	
	/**
	 * 驳回信息列表
	 * @param params
	 * @return
	 */
	public List<ReqLink> showBackList(Map<String, Object> params);
	
}