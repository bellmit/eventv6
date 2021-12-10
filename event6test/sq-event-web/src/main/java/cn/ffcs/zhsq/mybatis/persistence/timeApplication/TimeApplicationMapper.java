package cn.ffcs.zhsq.mybatis.persistence.timeApplication;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * 时限申请
 * @author zhangls
 *
 */
public interface TimeApplicationMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 无分页获取时限申请信息
	 * @param timeApplication
	 * @return
	 */
	public List<Map<String, Object>> findApplicationList(Map<String, Object> timeApplication);
	
	/**
	 * 依据applicationId删除时限申请信息
	 * @param applicationId	主键
	 * @param delUserId		删除操作人员
	 * @return
	 */
	public int delete(@Param(value="applicationId") Long applicationId, @Param(value="delUserId") Long delUserId);
	
	/**
	 * 统计事件时限申请数量
	 * @param param
	 * 			applicationType	申请类别
	 * 			creatorId		时限申请人员id
	 * 			auditorId		时限申请审核人员id
	 * 			auditorOrgid	时限申请审核人员组织id
	 * @return
	 */
	public int findEventTimeAppCountByCriteria(Map<String, Object> param);
	
	/**
	 * 分页获取事件时限申请记录
	 * @param param
	 * 			applicationType	申请类别
	 * 			creatorId		时限申请人员id
	 * 			auditorId		时限申请审核人员id
	 * 			auditorOrgid	时限申请审核人员组织id
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findEventTimeAppListByCriteria(Map<String, Object> param, RowBounds bounds);
	
}
