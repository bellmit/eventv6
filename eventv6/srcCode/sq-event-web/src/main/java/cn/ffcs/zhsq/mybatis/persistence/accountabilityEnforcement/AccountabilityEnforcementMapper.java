package cn.ffcs.zhsq.mybatis.persistence.accountabilityEnforcement;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 新疆执纪问责
 * Created by 张天慈 on 2018/3/12.
 */

public interface AccountabilityEnforcementMapper extends MyBatisBaseMapper<Map<String,Object>>{

	/**
	 * 统计待办问题总数
	 * @param param
	 * @return
	 * */
	public int findCount4Todo(Map<String,Object> param);

	/**
	 * 分页获取待办问题记录
	 * @param param
	 * @param bounds
	 * @return
	 * */
	public List<Map<String,Object>> findPageList4Todo(Map<String,Object> param, RowBounds bounds);

	/**
	 * 统计经办问题总数
	 * @param param
	 * @return
	 * */
	public int findCount4Handled(Map<String,Object> param);

	/**
	 * 分页获取经办问题记录
	 * @param param
	 * @param bounds
	 * @return
	 * */
	public List<Map<String,Object>> findPageList4Handled(Map<String,Object> param, RowBounds bounds);

	/**
	 * 统计我发起的问题总数
	 * @param param
	 * @return
	 * */
	public int findCount4Initiator(Map<String,Object> param);

	/**
	 * 分页获取我发起的问题记录
	 * @param param
	 * @param bounds
	 * @return
	 * */
	public List<Map<String,Object>> findPageList4Initiator(Map<String,Object> param, RowBounds bounds);

	/**
	 * 统计辖区所有问题总数
	 * @param param
	 * @return
	 * */
	public int findCount4Jurisdiction(Map<String,Object> param);

	/**
	 * 分页获取辖区所有问题记录
	 * @param param
	 * @param bounds
	 * @return
	 * */
	public List<Map<String,Object>> findPageList4Jurisdiction(Map<String,Object> param, RowBounds bounds);
	
	/**
	 * 统计辖区归档问题总数
	 * @param param
	 * @return
	 * */
	public int findCount4JurisdictionArchived(Map<String,Object> param);

	/**
	 * 分页获取辖区归档问题记录
	 * @param param
	 * @param bounds
	 * @return
	 * */
	public List<Map<String,Object>> findPageList4JurisdictionArchived(Map<String,Object> param, RowBounds bounds);
	
	
	/**
	 * 根据问题id删除问题
	 * @param probId	问题id
	 * @param delUserId	删除操作用户id
	 * @return
	 */
	public int deleteByProbId(@Param(value="probId")Long probId, @Param(value="delUserId")Long delUserId);

}
