package cn.ffcs.zhsq.szzg.hydropowerCoal;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.hydropowerCoal.HydropowerCoal;

/**
 *居民水电煤 
 * @author linzhu
 *
 */
public interface IHydropowerCoalService {

	/**
	 * 新增数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean insert(HydropowerCoal hydropowerCoal);

	/**
	 * 修改数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean update(HydropowerCoal hydropowerCoal);

	/**
	 * 删除数据
	 * @param bo 业务对象
	 * @return 结果
	 */
	public boolean delete(HydropowerCoal hydropowerCoal);

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
	public HydropowerCoal searchById(Long id);
	
    /**
     * 批量删除信息
     * @param userId
     * @param idList
     * @return
     */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    public boolean deleteByIds(Long userId,List<Long> idList);
	
	/**
	 * 根据参数查找是否存在
	 * @param params
	 * @return
	 */
	public boolean findByParams(Map<String, Object> params);
	
	/**
	 * 根据年份获取当前年份和上一年的使用记录
	 * @param orgCode
	 * @param year
	 * @return
	 */
	public List<HydropowerCoal>   getUsageDataByYear(String orgCode,String year,String type);

}
