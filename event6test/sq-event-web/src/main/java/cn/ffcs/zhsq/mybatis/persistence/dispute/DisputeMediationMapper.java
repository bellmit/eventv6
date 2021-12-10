package cn.ffcs.zhsq.mybatis.persistence.dispute;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;

public interface DisputeMediationMapper{
	
	
	//public int insert(DisputeMediation entity);
	
	public int update(DisputeMediation entity);
	
	public int delete(@Param(value="id") Long id);
	
	public DisputeMediation findById(Long id);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountByCriteria(Map<String, Object> param);

	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<DisputeMediation> findPageListByCriteria(Map<String, Object> param, RowBounds bounds);
	
	int deleteByPrimaryKeyForLogic(@Param("mediationId") Long mediationId, @Param("userId") Long userId);
	
    int deleteByPrimaryKey(Long mediationId);

    int insert(DisputeMediation record);

    int insertSelective(DisputeMediation record);

    DisputeMediation selectByPrimaryKey(Long mediationId);

    int updateByPrimaryKeySelective(DisputeMediation record);

    int updateByPrimaryKey(DisputeMediation record);
    
    public Long getMediationSequence();  
    
    List<DisputeMediation> findList(Map<String, Object> param);
    
    Map<String, Object> findMaxDisputeType(Map<String, Object> params);
    
    /**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountAndZero(Map<String, Object> param);

	/**
	 * 包含零报告根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<DisputeMediation> findPageListAndZero(Map<String, Object> param, RowBounds bounds);
	
	public Long insertZero(DisputeMediation dm);
	/**
	 * 根据 gridId 查询是否存在零报告
	 * @return
	 */
	public Integer findZeroIdByGridId(String gridId);
	
	public int deleteZero(String gridId);
	/**
	 * 查询当天是否存在矛盾纠纷
	 * @param gridId
	 * @return
	 */
	public int findCountByGridId(String gridId);
	
	public List<DisputeMediation> findPageListByCriteriaNC(Map<String, Object> param, RowBounds bounds);
	
	
	public int findCountByCriteriaNC(Map<String, Object> param);

	/**
	 * 根据第三方ID查询数据
	 * @param disputeId
	 * @return
	 */
	public DisputeMediation searchByDisputeId(Long disputeId);

	/**
	 * 删除接口
	 * @param disputeId
	 * @return
	 */
	public int deleteByDisputeId(Long disputeId);
	/**
	 * 获取今日各地区矛盾纠纷新增数量
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGridDisputeNum(Map<String, Object> params);
	/**
	 * 获取 今天/近七天/近一月 网格员上报TOP10
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGridDispiteTop(Map<String, Object> params);

	/**
	 * 获取 今天/近七天/近一月 网格员上报TOP10
	 * @param params
	 * @return
	 */
	public Map<String, Object> getGridDisputeTopGridName(Map<String, Object> params);


	public List<DisputeMediation> searchDisputeJX(Map<String ,Object> params,RowBounds rowBounds);

	public int countDisputeJX(Map<String, Object> params);

	public List<Map<String, Object>> searchDisputeUserJX(Map<String, Object> params, RowBounds rowBounds);

	public int countDisputeUserJX(Map<String, Object> params);

	public int deleteAllByGridId(Map<String, Object> params);
}