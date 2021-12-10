package cn.ffcs.zhsq.mybatis.persistence.sanitationtrunck;

import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.mybatis.domain.map.sanitationtrunck.SanitationTrunck;

/**
 * @Description: 环卫车基础信息表模块dao接口
 * @Author: dingyw
 * @Date: 01-02 14:08:17
 * @Copyright: 2018 福富软件
 */
public interface SanitationTrunckMapper {
	
	/**
	 * 新增数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 环卫车基础信息表id
	 */
	public long insert(SanitationTrunck bo);
	
	/**
	 * 修改数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 修改的记录数
	 */
	public long update(SanitationTrunck bo);
	
	/**
	 * 删除数据
	 * @param bo 环卫车基础信息表业务对象
	 * @return 删除的记录数
	 */
	public long delete(SanitationTrunck bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 环卫车基础信息表数据列表
	 */
	public List<SanitationTrunck> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 环卫车基础信息表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 环卫车基础信息表id
	 * @return 环卫车基础信息表业务对象
	 */
	public SanitationTrunck searchById(String id);
	
	/**
	 * 批量新增数据
	 * @param list
	 */
	public void batchSave(List<SanitationTrunck> list);
	
	public int delAll();

}