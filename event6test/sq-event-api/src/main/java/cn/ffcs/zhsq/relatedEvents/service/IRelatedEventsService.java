package cn.ffcs.zhsq.relatedEvents.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.ResultData;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.event.docking.Result;

public interface IRelatedEventsService {
	/**
	 * 新增涉事案件信息，成功时返回involvedId，否则返回-1
	 * @param relatedEvents
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertRelatedEvents(RelatedEvents relatedEvents);
	
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

	public ResultData saverelated(RelatedEvents relatedEvents);
	
	public ResultData deleterelated(String reId);
	
	public ResultData detailrelated(Long reId, String orgCode);

    //根据受害人或嫌疑人id查询对应案件信息
	public ResultData detailrelatedByPeopleId(Map<String,Object> params);
	
	public ResultData listrelated(int pageNo,int pageSize, Map<String, Object> params, String orgCode);


	// 命案防控对外接口 （新增，更新）
	public ResultData save(RelatedEvents relatedEvents, String orgCode);
	
	// 对外命案防控删除接口
	public ResultData delete(Map<String,String> params);
	/*
	 * 9+X命案防控接口（详情）
	 */
	public ResultData detail(Long id,String orgcode);
	/*
	 * 9+X命案防控接口（列表）
	 */
	Result list(Map<String, Object> params, int page,int rows,String orgCode);

	EUDGPagination findRelatedEventsPageListByCriteriaHom(int pageNo,
			int pageSize, Map<String, Object> params, String orgCode);
	/*
	 * 9+X命案防控接口嫌疑人（列表）
	 */
	public ResultData listpeople( InvolvedPeople involvedPeople);
	/*
	 * 9+X命案防控接口嫌疑人（删除）
	 */
	public ResultData deletePeople(Map<String,String> params);
	/*
	 * 9+X命案防控接口嫌疑人（详情）
	 */
	public ResultData detailPeople(Long ipId);
	/*
	 * 9+X命案防控接口嫌疑人（保存或更新）
	 */
	public ResultData savePeople(InvolvedPeople involvedPeople);
}
