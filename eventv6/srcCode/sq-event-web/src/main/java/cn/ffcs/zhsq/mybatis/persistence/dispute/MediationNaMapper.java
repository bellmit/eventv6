package cn.ffcs.zhsq.mybatis.persistence.dispute;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.dispute.MediationNa;

import java.util.List;
import java.util.Map;

/**
 * @Description: 南安矛盾纠纷模块dao接口
 * @Author: 黄文斌
 * @Date: 06-03 09:05:22
 * @Copyright: 2020 福富软件
 */
public interface MediationNaMapper {
	
	/**
	 * 新增数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	public long insert(MediationNa bo);
	
	/**
	 * 修改数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 修改的记录数
	 */
	public long update(MediationNa bo);
	
	/**
	 * 删除数据
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 删除的记录数
	 */
	public long delete(MediationNa bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 南安矛盾纠纷数据列表
	 */
	public List<MediationNa> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 南安矛盾纠纷数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 南安矛盾纠纷id
	 * @return 南安矛盾纠纷业务对象
	 */
	public MediationNa searchById(Long id);
	
	public long findCountById(Long id);

	/**
	 * csk
	 * 批量新增、更新
	 * @param bo 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	public long insertAll( List<MediationNa> bo);
	/**
	 * csk
	 * 返回map
	 * @param 南安矛盾纠纷业务对象
	 * @return 南安矛盾纠纷id
	 */
	public List<Map<String,String>> getCodebyName(Map<String, Object> params);


	/**

	 *	 采集近12个月趋势
	 * @param map
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> findAcquisitionTrendData(Map<String, Object> params);

	/**
	 *	 南安纠纷类型数量统计（前五类型排名）
	 * @param map
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> findHotEventData(Map<String, Object> params);

	/**
	 * 根据业务id查询数据
	 * @param id 南安矛盾纠纷id
	 * @return 网格名
	 */
	public MediationNa searchGrid(Long id);


	/**
	 * 根据业务id查询数据
	 * @param id 南安矛盾纠纷id
	 * @return 排查数，化解数
	 */
	public Long searchNum(Map<String, Object> params);


	/**
	 *	 南安纠纷化解率排名
	 * @param
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> searchPaiming(Map<String, Object> params);



	/**
	 *	 南安纠矛盾纠纷汇聚
	 * @param
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> DisputesConfluence (Map<String, Object> params);

	/**
	 * 矛盾纠纷撒点查询（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 南安矛盾纠纷数据列表
	 */
	public List<MediationNa> DisputesDotsJsonp(Map<String, Object> params, RowBounds rowBounds);


	/**
	 *	 南安纠纷状态类型数量统计
	 * @param map
	 *
	 *
	 * @return
	 */
	public List<Map<String,Object>> findStatusData(Map<String, Object> params);


	public Map<String,Object> findTypeNum(Map<String, Object> params);

	public List<Map<String, Object>> findDataTime(Map<String, Object> params);

	public List<Map<String, Object>> fetchEventDay(Map<String, Object> params);
}

