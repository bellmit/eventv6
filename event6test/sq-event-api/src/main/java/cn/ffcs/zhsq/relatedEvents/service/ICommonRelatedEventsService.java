package cn.ffcs.zhsq.relatedEvents.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.ResultData;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;

public interface ICommonRelatedEventsService {
	/**
	 * 新增涉事案件信息，成功时返回involvedId，否则返回-1
	 * @param relatedEvents
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertRelatedEvents(RelatedEvents relatedEvents, String infoOrgCode) throws Exception;
	
	/**
	 * 更新涉事案件信息，成功时返回true，否则返回false
	 * @param relatedEvents
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean updateRelatedEvents(RelatedEvents relatedEvents);
	
	/**
	 * 根据涉事案件主键删除涉事案件信息
	 * @param involvedId 涉事案件信息主键
	 * @return 删除成功时返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean deleteRelatedEventsById(Long reId);
	
	/**
	 * 批量删除涉事案件信息
	 * @param involvedIdList 涉事案件信息主键
	 * @return 返回受影响行数
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public int deleteRelatedEventsByIds(List<Long> reIdList);
	
	/**
	 * 依据业务信息删除涉事案件记录
	 * @param bizId 业务ID
	 * @param bizType 业务类型
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public int deleteRelatedEventsByBiz(Long bizId, String bizType);
	
	/**
	 * 根据涉事案件主键查找相应信息
	 * @param involvedId 涉事案件信息主键
	 * @return
	 */
	public RelatedEvents findRelatedEventsById(Long reId, String orgCode);
	
	/**
	 * 根据相应条件查询涉事案件信息记录数
	 * @param params relatedEvents 对象属性不为空时，也会转换为查询条件
	 * @return
	 */
	public Long findRelatedEventsCount(Map<String, Object> params);
	
	/**
	 * 分页查询涉事案件信息
	 * @param pageNo
	 * @param pageSize
	 * @param params relatedEvents 对象属性不为空时，也会转换为查询条件
	 * @return
	 */
	public EUDGPagination findRelatedEventsPagination(int pageNo, int pageSize, Map<String, Object> params, String orgCode);
	
	/**
	 * 根据相应条件查询涉事案件单表信息记录数
	 * @param params
	 * @return
	 */
	public Long findRelatedEventsCountByCriteria(Map<String, Object> params);
	
	/**
	 * 分页查询涉事案件单表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findRelatedEventsPageListByCriteria(int pageNo, int pageSize, Map<String, Object> params, String orgCode);
	
	/**
	 * 提供地图分页查询涉事事件（包含点位信息）
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @param orgCode
	 * @return
	 */
	public EUDGPagination findRelatedEventsForGis(int pageNo, int pageSize, Map<String, Object> params, String orgCode);
	public EUDGPagination findSchoolEventsForGis(int pageNo, int pageSize, Map<String, Object> params, String orgCode);
	public EUDGPagination findRoadEventsForGis(int pageNo, int pageSize, Map<String, Object> params, String orgCode);
	/*
	 * 9+X涉及师生安全案件接口（新增、修改）
	 */

	ResultData saveRelatedEventsSchool(RelatedEvents bo, String orgCode);
	/*
	 * 9+X涉及师生安全案件接口（删除）
	 */
	ResultData deleteRelatedEventsSchool(RelatedEvents bo, Long userId);
	/*
	 * 9+X涉及师生安全案件接口（详情）
	 */
	ResultData detailRelatedEventsSchool(RelatedEvents bo);
	/*
	 * 9+X涉及师生安全案件接口（列表）
	 */
	ResultData listRelatedEventsSchool(Map<String, Object> params, int pageNo, int pageSize);
	/*
	 * 9+X重大案件案件接口（新增、修改）
	 */
    ResultData save(RelatedEvents relatedEvents, String orgCode);
    /*
	 * 9+X重大案件接口（详情）
	 */
	ResultData detail(Long id, String orgcode);
	/*
	 *  9+X重大案件接口（删除）
	 * params{
	 * 	idStr
	 * 	userId
	 * gridCode
	 * delAll
	 * }
	 */
	ResultData delete(Map<String,String> params);
	/*
	 * 9+X重大案件接口（列表）
	 */
	ResultData list(Map<String, Object> params, int page, int rows,String orgCode);

	EUDGPagination findRelatedEventsMajor(int pageNo, int pageSize,
			Map<String, Object> params, String orgCode);

	EUDGPagination findRelatedEventsSchool(int pageNo, int pageSize,
			Map<String, Object> params, String orgCode);





}
