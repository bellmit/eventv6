package cn.ffcs.zhsq.mybatis.persistence.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * 事件导入，中间表 T_EVENT_IMPORT
 * @author zhangls
 *
 */
public interface EventImport4FileMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据rowid获取导入记录
	 * @param rowid
	 * @return
	 */
	public Map<String, Object> findByRowid(@Param(value="rowid") String rowid);
	
	/**
	 * 依据rowid删除导入记录，真删除
	 * @param rowid
	 * @return
	 */
	public int deleteByRowid(@Param(value="rowid") String rowid);
	
	/**
	 * 批量新增数据
	 * @param recordMapList
	 * 			key值为表中的字段名称，大写
	 * @return
	 */
	public int insert4Batch(List<Map<String, Object>> recordMapList);
	
	/**
	 * 依据rowid更新记录
	 * @param recordMap
	 * 			updateSql 列更新语句
	 * 			rowid
	 * @return
	 */
	public int updateByRowid(Map<String, Object> recordMap);
	
	/**
	 * 获取表T_EVENT_IMPORT的字段信息
	 * @return
	 * 		COLUMN_NAME	列名称
	 * 		DATA_TYPE	列类型
	 * 		DATA_LENGTH	列长度
	 * 		NULLABLE	是否可空 Y可空；N不可空
	 */
	public List<Map<String, Object>> findTableColInfo();
	
	/**
	 * 分页获取记录，列有转换为小写
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPageList4EventByCriteria(Map<String, Object> param, RowBounds bounds);
}
