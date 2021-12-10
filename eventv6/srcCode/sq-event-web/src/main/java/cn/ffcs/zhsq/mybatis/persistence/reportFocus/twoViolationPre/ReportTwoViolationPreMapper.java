package cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @Description: 两违防治数据库相关操作
 * @ClassName:   ReportTwoViolationPreMapper   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午4:13:23
 */
public interface ReportTwoViolationPreMapper extends MyBatisBaseMapper<Map<String, Object>> {
	/**
	 * 依据twoVioUUID/reportUUID获取两违信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> findByIdSimple(Map<String, Object> params);
	
	/**
	 * 依据twoVioUUID/reportId/reportUUID获取两违信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> findById(Map<String, Object> params);
	
	/**
	 * 获取草稿列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Draft(Map<String, Object> params);
	/**
	 * 分页获取草稿列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Draft(Map<String, Object> params, RowBounds bounds);
	/**
	 * 获取草稿列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Draft(Map<String, Object> params);
	
	/**
	 * 获取待办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> params);
	/**
	 * 分页获取待办列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Todo(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取两违状态跟踪列表数量
	 * @param params
	 * @return
	 */
	public int findCount4StatusTrack(Map<String, Object> params);
	/**
	 * 分页获取两违状态跟踪列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4StatusTrack(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取经办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Handled(Map<String, Object> params);
	/**
	 * 分页获取经办列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Handled(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取我发起的列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Initiator(Map<String, Object> params);
	/**
	 * 分页获取我发起的列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Initiator(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取辖区所有列表(精简)数量
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionSimplify(Map<String, Object> params);
	/**
	 * 分页获取辖区所有列表(精简)记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4JurisdictionSimplify(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取辖区所有列表(精简)记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4JurisdictionSimplify(Map<String, Object> params);
	
	/**
	 * 获取辖区所有列表附带经纬度
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithMarker(Map<String, Object> params);
	/**
	 * 分页获取辖区所有列表附带经纬度
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4JurisdictionWithMarker(Map<String, Object> params, RowBounds bounds);

	/**
	 * 获取辖区所有列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Jurisdiction(Map<String, Object> params);

	/**
	 * 获取辖区所有列表附带地域全路径
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithRegionPath(Map<String, Object> params);

	/**
	 * 分页获取辖区所有列表附带地域全路径
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4JurisdictionWithRegionPath(Map<String, Object> params, RowBounds bounds);

	/**
	 * 分页获取辖区所有列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Jurisdiction(Map<String, Object> params, RowBounds bounds);

	/**
	 * 不分页获取辖区所有列表记录
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findList4JurisdictionNoPage(Map<String, Object> params);

	/**
	 * 获取辖区所有列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Jurisdiction(Map<String, Object> params);
	
	
	/**
	 * 获取辖区归档列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Archived(Map<String, Object> params);
	/**
	 * 分页获取辖区归档列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4Archived(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 获取我的阅办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4MsgReading(Map<String, Object> params);
	/**
	 * 分页获取我的阅办列表记录
	 * @param params
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findList4MsgReading(Map<String, Object> params, RowBounds bounds);
	
}
