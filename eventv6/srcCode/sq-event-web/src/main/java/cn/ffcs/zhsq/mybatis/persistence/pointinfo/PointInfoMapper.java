package cn.ffcs.zhsq.mybatis.persistence.pointinfo;

import cn.ffcs.zhsq.mybatis.domain.pointinfo.PointInfo;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * @Description: 设备点位信息表模块dao接口
 * @Author: tangheng
 * @Date: 09-16 16:27:18
 * @Copyright: 2019 福富软件
 */
public interface PointInfoMapper {
	
	/**
	 * 新增数据
	 * @param bo 设备点位信息表业务对象
	 * @return 设备点位信息表id
	 */
	public long insert(PointInfo bo);
	
	/**
	 * 修改数据
	 * @param bo 设备点位信息表业务对象
	 * @return 修改的记录数
	 */
	public long update(PointInfo bo);

    /**
     * 修改数据
     * @param bo 设备点位信息表业务对象
     * @return 修改的记录数
     */
    public long updateByDeviceId(PointInfo bo);


    /**
	 * 删除数据
	 * @param bo 设备点位信息表业务对象
	 * @return 删除的记录数
	 */
	public long delete(PointInfo bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备点位信息表数据列表
	 */
	public List<PointInfo> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 设备点位信息表数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备点位信息表id
	 * @return 设备点位信息表业务对象
	 */
	public PointInfo searchById(Long id);

    /**
     * 删除所有数据
     * @param bo 设备点位信息表业务对象
     * @return 删除的记录数
     */
    public long deleteAll();
    /**
     * 批量新增数据
     * @param bo 设备点位信息表业务对象
     */
    void batchInsert(List<PointInfo> devicePointInfolist);

    /**
     * 查询视频点位周边列表
     * @param params x,y 查询的中心点 ; distance 查询距离（米）
     * @return
     */
    public List<PointInfo> searchPointZhouBianList(Map<String, Object> params, RowBounds rowBounds);

    /**
     * 查询视频点位周边数量
     * @param params x,y 查询的中心点 ; distance 查询距离（米）
     * @return
     */
    public Long countPointZhouBian(Map<String, Object> params);

    public List<Map<String,Object>> exportDeviceList(Map<String, Object> params);

    public List<PointInfo> searchAllList(Map<String, Object> params);

    public List<Map<String,Object>> searchAllListForV6(Map<String, Object> params);

    public List<Map<String,Object>> searchListForV6(Map<String, Object> params, RowBounds rowBounds);

    public List<Map<String,Object>> getExceptionTrend(Map<String, Object> params);

    public List<Map<String,Object>> getAllOrgNum(Map<String, Object> params);
}