package cn.ffcs.zhsq.mybatis.persistence.event;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 事件.涉及人员DAO接口
 * @author zhangyy
 *
 */
public interface InvolvedPeopleMapper extends MyBatisBaseMapper<InvolvedPeople>{

	/**
	 * 依据ipId或者bizId删除涉及人员信息
	 * @param involvedPeople
	 * @return
	 */
	public int delete(InvolvedPeople involvedPeople);

	/**
	 *
	 * @param involvedPeople
	 * @return
	 */
	public int deleteByIpIdsAndGridCode(Map<String,Object> params);

	/**
	 * 无分页获取涉及人员信息
	 * @param involvedPeople
	 * @return
	 */
	public List<InvolvedPeople> findInvolvedPeopleList(InvolvedPeople involvedPeople);
	
	/**
	 * 统计涉及人员信息
	 * @param involvedPeople
	 * @return
	 */
	public Long findInvolvedPeopleCount(InvolvedPeople involvedPeople);
	
	/**
	 * 批量删除 
	 * 根据bizId删除不存在list中的数据
	 * @param params 
	 * @return
	 */
	public long batchDelete( Map<String, Object> params);
	
	/**
	 * 批量保存
	 * @param 
	 * @return
	 */
	public long batchInsert(@Param("list")List<InvolvedPeople> vList);
	/**
	 * 批量保存
	 * @param vList
	 * @return
	 */
	public long batchUpdate(@Param("list")List<InvolvedPeople> vList);


	/**
	 * 批量新增或更新，记录存在则更新，不存在则新增
	 * @param involvedPeopleList
	 * @return
	 */
	public long batchSaveOrUpdate(@Param("list")List<InvolvedPeople> involvedPeopleList);

}
