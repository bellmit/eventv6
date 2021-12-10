package cn.ffcs.zhsq.mybatis.persistence.smsTask;

import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.smsTask.SmsTask;

public interface SmsTaskMapper extends MyBatisBaseMapper<SmsTask> {
	/**
	 * 无分页获取记录列表
	 * @param param
	 * @return
	 */
	public List<SmsTask> findPageListByCriteria(Map<String, Object> param);
	
	/**
	 * 动态语句查询记录列表
	 * @param param
	 * @return
	 */
	public List<Map> findPageListByDynamic(Map<String, Object> param);
	
	/**
	 * 从视图中查询记录列表
	 * @param param
	 * @return
	 */
	public List<Map> findListByView(Map<String, Object> param);
	
	/**
	 * 批量新增记录
	 * @param smsTaskInsertList
	 * @return 处理成功的记录数
	 */
	public int insertSmsTaskBatch(List<SmsTask> smsTaskInsertList);
	
	/**
	 * 批量新增记录
	 * @param smsTaskUpdateList
	 * @return 处理成功的记录数
	 */
	public int updateSmsTaskBatch(List<SmsTask> smsTaskUpdateList);
	
}
