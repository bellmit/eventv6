package cn.ffcs.zhsq.mybatis.persistence.idssControl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.doorsys.bo.control.ControlApplyInfo;
import cn.ffcs.doorsys.bo.control.ControlTargetRecord;
import cn.ffcs.doorsys.bo.control.ControlTargetRef;

/**
 * 布控相关查询
 * @author zhangls
 *
 */
public interface IdssControlMapper {
	/**
	 * 获取布控申请列表
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<ControlApplyInfo> findApplyInfoByCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 统计布控申请记录
	 * @param param
	 * @return
	 */
	public int findApplyInfoCount(Map<String, Object> param);
	
	/**
	 * 获取布控对象列表
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<ControlTargetRef> findTargetRefByCriteria(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 统计布控对象记录
	 * @param param
	 * @return
	 */
	public int findTargetRefCount(Map<String, Object> param);
	
	/**
	 * 获取布控对象捕获消息列表
	 * @param param
	 * @return
	 */
	public List<ControlTargetRecord> findTargeRecordList(Map<String, Object> param);
	
	public ControlTargetRecord findTargeRecordById(@Param(value="recordId") Long recordId);
}
