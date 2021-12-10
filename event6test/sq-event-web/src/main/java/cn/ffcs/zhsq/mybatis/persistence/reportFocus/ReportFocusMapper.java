package cn.ffcs.zhsq.mybatis.persistence.reportFocus;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;


/**
 * @Description: 重点关注上报数据库相关操作
 * 				  相关表：T_EVENT_REPORT_FOCUS
 * @ClassName:   ReportFocusMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午3:33:31
 */
public interface ReportFocusMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据reportId或者reportUUID获取信息
	 * @param reportFocus
	 * @return
	 */
	public Map<String, Object> findById(Map<String, Object> reportFocus);
	
	/**
	 * 依据reportId或者reportUUID删除上报信息
	 * @param params	参数
	 * 			delUserId	删除用户id
	 * 			reportId	上报id，该参数优先reportUUID生效
	 * 			reportUUID	上报UUID
	 * @return
	 */
	public int delete(Map<String, Object> params);
}
