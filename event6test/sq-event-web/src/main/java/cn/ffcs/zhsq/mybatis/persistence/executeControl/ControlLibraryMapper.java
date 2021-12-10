package cn.ffcs.zhsq.mybatis.persistence.executeControl;

import cn.ffcs.zhsq.mybatis.domain.executeControl.ControlLibrary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 布控库管理模块dao接口
 * @Author: dtj
 * @Date: 07-16 20:37:41
 * @Copyright: 2020 福富软件
 */
public interface ControlLibraryMapper {
	
	/**
	 * 新增数据
	 * @param bo 布控库管理业务对象
	 * @return 布控库管理id
	 */
	public long insert(ControlLibrary bo);
	
	/**
	 * 修改数据
	 * @param bo 布控库管理业务对象
	 * @return 修改的记录数
	 */
	public long update(ControlLibrary bo);
	
	/**
	 * 删除数据
	 * @param bo 布控库管理业务对象
	 * @return 删除的记录数
	 */
	public long delete(ControlLibrary bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 布控库管理数据列表
	 */
	public List<ControlLibrary> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 布控库管理数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 布控库管理id
	 * @return 布控库管理业务对象
	 */
	public ControlLibrary searchById(Long id);

	public List<ControlLibrary> getTitle();

    Long batchInsert(ArrayList<ControlLibrary> list);

	Long batchDelete(@Param(value = "ids") String[] ids);
}