/**
 * 
 */
package cn.ffcs.zhsq.crowd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.TaskInfo;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

/**
 * 综治.重点上访人员
 * @author chenfzh
 *
 */
public interface IVisitRecordService {
	
	/**
	 * 分页查找重点人员上访信息
	 * @param pageNo
	 * @param pageSize
	 * @param name
	 * @param identityCard
	 * @param gender
	 * @param type
	 * @return
	 */
	public EUDGPagination findVisitRecordPagination(int pageNo, int pageSize, Map<String, Object> params);

	/**
	 * 根据ID查找重点人员上访信息
	 * @param recordId
	 * @return
	 */
	public VisitRecord findVisitRecordById(Long prId);
	
	/**
	 * 保存重点人员上访信息，返回ID
	 * @param record
	 * @return
	 */
	public Long saveVisitRecordReturnId(VisitRecord record);
	
	/**
	 * 删除重点人员上访信息
	 * @param recordId
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean deleteVisitRecorddById(Long userId, List<Long> recordIdList);
	
	/**
	 * 更新重点人员上访信息
	 * @param record
	 * @return
	 */
	public boolean updateVisitRecord(VisitRecord record);
	
	/**
	 * 判断人员是否已经在重点人员上访信息中存在
	 * @param ciRsId
	 * @return
	 */
	public boolean isVisitRecordExists(Long ciRsId);
	
	/**
	 * 根据事件ID获取对应的信息
	 */
	public VisitRecord findVisitRecordByEventId(Long eventId);
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long saveAndReport(VisitRecord record, EventDisposal event,TaskInfo taskInfo,
			UserInfo userInfo);
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long saveAndReport(VisitRecord record, EventDisposal event,ArrayList<TaskInfo> taskInfo,
			UserInfo userInfo);
	
	/**
	 * 根据居民ciRsId查找上访人员信息
	 * @param ciRsId
	 * @return
	 */
	public VisitRecord findByCiRsId(Long ciRsId);
	
	/**
	 * 根据人员ID和人员类型查找访问记录表
	 * @param ciRsId
	 * @param visitType
	 * @return
	 */
	public List<VisitRecord> findVisitRecordList(Long ciRsId, String visitType);
}
