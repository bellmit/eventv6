package cn.ffcs.zhsq.mybatis.persistence.relatedEvents;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.formula.functions.T;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;

/**
 * 涉及业务案(事)件接口
 *
 */
public interface RelatedEventsMapper extends MyBatisBaseMapper<RelatedEvents>{

	/**
	 * 依据涉事案件主键删除记录
	 */
	public int delete(Long reId);
	
	public int deleteByIds(@Param(value="reIdList") List<Long> reIdList);

	public int deleteByIdAndGridCode(Map<String,Object> params);

	public int deleteByIdsAndGridCode(Map<String,Object> params);

	//全量删除
	public int deleteByBizAndGridCode(@Param(value = "gridCode") String gridCode,@Param(value = "bizType")String bizType);

	/**
	 * 依据业务信息进行删除涉事案件记录
	 * @param bizId 业务ID
	 * @param bizType 业务类型
	 * @return
	 */
	public int deleteByBiz(Long bizId, String bizType);

	/**
	 * 根据嫌疑人或受害人id查询对应的命案防控案件
	 * @param peopleId
	 * @return
	 */
	public RelatedEvents findByPeopleId(Long peopleId);
	
	/**
	 * 涉事案件单表记录统计，没有关联其他业务表
	 * @param param
	 * @return
	 */
	public int findRelatedEventsCountByCriteria(Map<String, Object> param);
	
	/**
	 * 涉事案件单表记录列表，没有关联其他业务表
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<RelatedEvents> findRelatedEventsPageListByCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountForSchoolRE(Map<String, Object> param);
	
	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<RelatedEvents> findPageListForSchoolRE(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountForCommonRE(Map<String, Object> param);
	
	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<RelatedEvents> findPageListForCommonRE(Map<String, Object> param, RowBounds bounds);
	
	
	public int findRelatedEventsForGisOfCount(Map<String, Object> param);
	public List<Map<String, Object>> findRelatedEventsForGisOfPage(Map<String, Object> param, RowBounds bounds);
	
	public int findSchoolEventsForGisOfCount(Map<String, Object> param);
	public List<Map<String, Object>> findSchoolEventsForGisOfPage(Map<String, Object> param, RowBounds bounds);
	
	public int findRoadEventsForGisOfCount(Map<String, Object> param);
	public List<Map<String, Object>> findRoadEventsForGisOfPage(Map<String, Object> param, RowBounds bounds);
	
	public int deleteByIds4CareRoad(Map<String,Object> params);
	
	public RelatedEvents findById4CareRoad(Long id);
	
	public RelatedEvents findById4School(Long id);
	
}
