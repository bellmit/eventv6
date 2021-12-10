package cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportMsgCCCfg;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * @Description: 消息配送人员配置信息相关操作
 * 				  相关表T_EVENT_MSG_CC_CFG
 * @ClassName:   ReportMsgCCCfgMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年9月26日 下午2:35:56
 */
public interface ReportMsgCCCfgMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 删除人员配置信息
	 * @param params
	 * @return
	 */
	public int delete(Map<String, Object> params);
	
	/**
	 * 依据UUID获取人员配置信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> findCfgByUUID(Map<String, Object> params);
	
	/**
	 * 获取人员配置信息数量
	 * @param params
	 * @return
	 */
	public int findCfg4Count(Map<String, Object> params);
	
	/**
	 * 分页获取人员配置信息
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findCfg4Pagination(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取人员配置信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCfg4List(Map<String, Object> params);
}
