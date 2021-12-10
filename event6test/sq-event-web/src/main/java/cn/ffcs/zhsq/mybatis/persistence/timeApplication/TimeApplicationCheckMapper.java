package cn.ffcs.zhsq.mybatis.persistence.timeApplication;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * 时限审核
 * @author zhangls
 *
 */
public interface TimeApplicationCheckMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据checkId删除时限审核信息
	 * @param checkId	主键
	 * @param delUserId	删除操作人员
	 * @return
	 */
	public int delete(@Param(value="checkId") Long checkId, @Param(value="delUserId") Long delUserId);
	
	/**
	 * 无分页获取时限申请审核信息
	 * @param timeAppCheck
	 * 			applicationId	时限申请记录id
	 * @return
	 */
	public List<Map<String, Object>> findTimeAppCheckList(Map<String, Object> timeAppCheck);
	
}
