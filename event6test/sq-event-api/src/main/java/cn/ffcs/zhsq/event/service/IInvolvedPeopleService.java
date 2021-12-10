package cn.ffcs.zhsq.event.service;

import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IInvolvedPeopleService {
	/**
	 * 新增涉及人员信息
	 * @param involvedPeople
	 * @return 新增成功返回ipId，否则返回-1L
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertInvolvedPeople(InvolvedPeople involvedPeople, Boolean isNotDuplicated) throws Exception;

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public Long insertInvolvedPeople(InvolvedPeople involvedPeople) throws Exception;

	/**
	 * 依据bizId或者ipId更新涉及人员信息，ipId优先
	 * @param involvedPeople
	 * @return 更新成功返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean updateInvolvedPeople(InvolvedPeople involvedPeople) throws Exception;

	/**
	 * 依据ipId删除涉及人员信息
	 * @param ipId
	 * @return 删除成功返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean deleteInvolvedPeopleById(Long ipId);
	
	/**
	 * 依据bizId和bizType删除涉及人员信息
	 * @param bizId
	 * @param bizType
	 * @return 删除成功返回true，否则返回false
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean deleteInvolvedPeopleByBiz(Long bizId, String bizType);
	
	/**
	 * 依据ipId查找涉及人员信息
	 * @param ipId
	 * @return 未查找到相应信息时，返回null
	 */
	public InvolvedPeople findInvolvedPeopleById(Long ipId);
	/**
	 * 依据ipId查找涉及人员信息
	 * @param ipId
	 * @param orgCode	当前登陆人员组织编码
	 * @return 未查找到相应信息时，返回null
	 */
	public InvolvedPeople findInvolvedPeopleByIdAndOrgCode(Long ipId,String orgCode);

	/**
	 * 无分页查找涉及人员信息
	 * @param involvedPeople
	 * @return 未查找到相应信息时，返回null
	 */
	public List<InvolvedPeople> findInvolvedPeopleList(InvolvedPeople involvedPeople);
	
	/**
	 * 依据bizId和bizType查找涉及人员信息
	 * @param bizId
	 * @param bizType
	 * @return
	 */
	public List<InvolvedPeople> findInvolvedPeopleListByBiz(Long bizId, String bizType);

	/**
	 * 依据bizId、bizType和额外扩展参数查找涉及人员信息
	 * @param bizId
	 * @param bizType
	 * @param params
	 * @return
	 */
	public List<InvolvedPeople> findInvolvedPeopleListByBizAndExtPar(Long bizId, String bizType,Map params);

	/**
	 * 统计涉及人员记录数
	 * @param involvedPeople
	 * @return
	 */
	public Long findInvolvedPeopleCount(InvolvedPeople involvedPeople);
	
	/**
	 * 根据bizId批量删除不存在list的数据
	 * @param params
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchDelete( Map<String, Object> params);
	
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchInsert(List<InvolvedPeople> vList);
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchUpdate(List<InvolvedPeople> vList);

	/**
	 * 批量新增或更新，记录存在则更新，不存在则新增
	 * @param bizId					主业务模块id
	 * @param involvedPeopleList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public long batchSaveOrUpdate(Long bizId,List<InvolvedPeople> involvedPeopleList);
}
