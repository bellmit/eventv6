package cn.ffcs.zhsq.idssControl.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.doorsys.bo.control.ControlTargetRecord;
import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 布控相关查询接口
 * @author zhangls
 *
 */
public interface IIdssControlService {
	/**
	 * 获取布控申请列表
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findIdssControlApplyInfo(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 获取布控对象列表
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findIdssControlTargetRef(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 获取布控对象捕获消息列表
	 * @param params
	 * @return
	 */
	public List<ControlTargetRecord> findIdssControlTargetRecord(Map<String, Object> params);
	
	public ControlTargetRecord findIdssControlTargetRecordById(Long recordId);
}
