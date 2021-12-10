package cn.ffcs.zhsq.smsTask.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.smsTask.SmsTask;

public interface ISmsTaskService {
	
	public static final String BIZ_TYPE_EVENT = "00";//新事件
	public static final String TASK_TYPE_0 = "0";//将到期发送短信
	public static final String TASK_STATUS_UNFINISH = "0";//未完成
	public static final String TASK_STATUS_FINISH = "1";//已完成
	
	/**
	 * 新增短信发送记录
	 * 如果存在同类未完成的任务，则更新相应的成功发送和失败发送的数值，新值为原有值累加上传入的数值；
	 * 如果存在同类已完成的任务，则不再新增该记录，即新增失败；
	 * 同类任务的判断条件为：bizId,bizType,taskType,phoneNum这四个值均一致
	 * @param smsTask
	 * @return 新增成功则返回当前记录taskId，否则返回-1L
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertSmsTask(SmsTask smsTask);
	
	/**
	 * 批量新增短信发送记录
	 * @param smsTaskList
	 * @return 返回操作成功的记录数
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertSmsTask(List<SmsTask> smsTaskList);
	/**
	 * 依据记录taskId更新记录
	 * @param smsTask
	 * @return 更新成功返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean updateSmsTaskById(SmsTask smsTask);
	
	/**
	 * 依据记录taskId删除记录
	 * @param taskId
	 * @return 删除成功返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean deleteSmsTaskById(Long taskId);
	
	/**
	 * 获取记录总数
	 * @param params
	 * @return
	 */
	public Integer findSmsTaskCount(Map<String, Object> params);
	
	/**
	 * 分页获取记录列表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findSmsTaskPagination(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 无分页获取记录列表信息
	 * @param params
	 * @return
	 */
	public List<SmsTask> findSmsTaskList(Map<String, Object> params);
	
	/**
	 * 动态查询记录列表信息
	 * @param param
	 * selectCols 动态查询的列
	 * tableName 动态查询的表名，需要写出连接方式
	 * whereCondition 查询条件，以AND打头，首尾需要空格
	 * T_BAS_SMS_TASK 别名为T1
	 * @return
	 */
	public List<Map> findPageListByDynamic(Map<String, Object> param);
	
	/**
	 * 获取视图中的记录
	 * @param param
	 * @return
	 */
	public List<Map> findListByView(Map<String, Object> param);
	
	/**
	 * 判断是否存在与smsTask同类的记录
	 * @param smsTask
	 * @return 存在则返回true，否则返回false
	 */
	public boolean isSmsTaskExist(SmsTask smsTask);
	
	/**
	 * 判断是否smsTask是否可操作
	 * @param smsTask
	 * @return 可操作则返回true，否则返回false
	 */
	public boolean isSmsTaskAble(SmsTask smsTask);
}
