package cn.ffcs.zhsq.mybatis.persistence.hotLine;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * @Description: T_HOTLINE模块dao接口
 * @Author: wuxq
 * @Date: 09-08 17:02:47
 * @Copyright: 2020 福富软件
 */
public interface HotlineMapper {
	
	/**
	 * 新增数据
	 * @param hotLine T_HOTLINE业务对象
	 */
	public int insert(Map<String, Object> hotLine);
	
	/**
	 * 新增数据
	 * @param bo T_HOTLINE业务对象
	 * @return T_HOTLINEid
	 */
	public boolean insertAll(@Param("dataList")List<Map<String, Object>> hotLine);
	
	/**
	 * 修改数据
	 * @param hotLine T_HOTLINE业务对象
	 * @return 修改的记录数
	 */
	public int update(Map<String, Object> hotLine);
	
	/**
	 * 删除数据
	 * @param bo T_HOTLINE业务对象
	 * @return 删除的记录数
	 */
	public int delete(Map<String, Object> hotLine);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return T_HOTLINE数据列表
	 */
	public List<Map<String, Object>> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return T_HOTLINE数据总数
	 */
	public int countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id T_HOTLINE caseNo
	 * @return T_HOTLINE 业务对象
	 */
	public Map<String, Object> searchByCaseNo(String caseNo);

}