package cn.ffcs.zhsq.mybatis.persistence.crowd;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;

/**
 * 综治.重点人群走访记录DAO接口
 * @author guohh
 *
 */
public interface VisitRecordMapper extends MyBatisBaseMapper<VisitRecord> {

	/**
	 * 根据居民ID查找走访记录数量
	 * @param ciRsId
	 * @return
	 */
	public int countRecordByCiRsId(Long ciRsId);
	
	/**
	 * 根据居民ciRsId查找走访记录信息
	 * @param ciRsId
	 * @return
	 */
	public VisitRecord findByCiRsId(Long ciRsId);
	
	public VisitRecord findVisitRecordByEventId(Long eventId);
	
	/**
	 * 根据人员ID和人员类型查找访问记录表
	 * @param ciRsId
	 * @param visitType
	 * @return
	 */
	public List<VisitRecord> findVisitRecordList(@Param(value="ciRsId") Long ciRsId, @Param(value="visitType") String visitType);
}