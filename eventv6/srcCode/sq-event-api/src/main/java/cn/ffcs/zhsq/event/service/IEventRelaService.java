package cn.ffcs.zhsq.event.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.event.EventRela;

/**
 * 事件关联关系
 * @author zhongshm
 *
 */
public interface IEventRelaService {

	/**
	 * 数据列表（带分页）
	 * Sep 28, 2014
	 * 10:11:41 AM
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPatrolPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	/**
	 * 新增事件关联关系
	 * Oct 14, 2014
	 * 10:41:13 AM
	 * @param eventRela
	 * @return
	 */
	public int insertEventRela(EventRela eventRela);
	
//	/**
//	 * 新增巡防信息
//	 * Sep 28, 2014
//	 * 3:30:24 PM
//	 * @param patrol 
//	 * @return
//	 */
//	public int insertPatrol(Patrol patrol);
//
//	/**
//	 * 修改巡防信息
//	 * Sep 28, 2014
//	 * 3:44:03 PM
//	 * @param patrol
//	 * @return
//	 */
//	public int updatePatrol(Patrol patrol);
//
//	/**
//	 * 获取编号
//	 * Sep 29, 2014
//	 * 9:35:19 AM
//	 * @return
//	 */
//	public String getPatrolCode();
//
//	/**
//	 * 根据主键查找对象
//	 * Sep 29, 2014
//	 * 10:36:34 AM
//	 * @param patrolId
//	 * @return
//	 */
//	public Patrol findById(Long patrolId);
//
//	/**
//	 * 更新
//	 * Sep 29, 2014
//	 * 10:55:43 AM
//	 * @param patrol
//	 * @return
//	 */
//	public int updateByPrimaryKeySelective(Patrol patrol);
//
//	/**
//	 * 根据Id删除对应信息（逻辑）
//	 * Sep 29, 2014
//	 * 2:26:41 PM
//	 * @param patrol
//	 * @return
//	 */
//	public int deleteByIdForLogic(Patrol patrol);

}
