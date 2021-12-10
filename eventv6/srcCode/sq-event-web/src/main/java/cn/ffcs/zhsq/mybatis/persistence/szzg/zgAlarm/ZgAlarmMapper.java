package cn.ffcs.zhsq.mybatis.persistence.szzg.zgAlarm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.szzg.zgAlarm.ZgAlarm;

/**
 * @Description: 告警信息模块dao接口
 * @Author: linzhu
 * @Date: 08-07 15:03:54
 * @Copyright: 2017 福富软件
 */
public interface ZgAlarmMapper {
	
	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int insert(ZgAlarm bo);
	
	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int update(ZgAlarm bo);
	
	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public int delete(ZgAlarm bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 数据列表
	 */
	public List<ZgAlarm> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 数据总数
	 */
	public int countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 业务id
	 * @return 业务对象
	 */
	public ZgAlarm searchById(Long id);
	
	/**
	 * 获取有效告警信息条数
	 * @return
	 */
	public int findZgAlarmCount();
	
	/**
	 * 获取有效的告警信息列表
	 * @return
	 */
	public List<ZgAlarm> findZgAlarmList();
	

}
