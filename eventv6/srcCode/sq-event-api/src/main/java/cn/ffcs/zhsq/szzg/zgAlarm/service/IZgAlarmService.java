package cn.ffcs.zhsq.szzg.zgAlarm.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.zgAlarm.ZgAlarm;

/**
 * 告警信息模块服务
 * @author linzhu
 *
 */
public interface IZgAlarmService {
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean insert(ZgAlarm bo);

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean update(ZgAlarm bo);

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean delete(ZgAlarm bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 数据列表
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public ZgAlarm searchById(Long id);
	/**
	 * 获取有效的告警信息列表
	 * @return
	 */
	public List<ZgAlarm> findZgAlarmList();
	/**
	 * 获取有效告警信息条数
	 * @return
	 */
	public int findZgAlarmCount();
}
