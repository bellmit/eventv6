package cn.ffcs.zhsq.pointInfo.service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.pointinfo.PointInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备点位信息表模块服务
 * @Author: tangheng
 * @Date: 09-16 16:27:18
 * @Copyright: 2019 福富软件
 */
public interface PointInfoService {

	/**
	 * 新增数据
	 * @param bo 设备点位信息表业务对象
	 * @return 设备点位信息表id
	 */
	public Long insert(PointInfo bo);

	/**
	 * 修改数据
	 * @param bo 设备点位信息表业务对象
	 * @return 是否修改成功
	 */
	public boolean update(PointInfo bo);

    /**
     * 修改数据
     * @param bo 设备点位信息表业务对象
     * @return 是否修改成功
     */
    public boolean updateByDeviceId(PointInfo bo);
	/**
	 * 删除数据
	 * @param bo 设备点位信息表业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(PointInfo bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备点位信息表分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备点位信息表id
	 * @return 设备点位信息表业务对象
	 */
	public PointInfo searchById(Long id);

	/**
     * 获取token
	* */
    public String getToken();

    /**
     * 获取内网token
     * */
    public String getIntranetToken(String cid);
    /**
     *  同步设备点位数据
     */
    public void synchroDevicePoint();

    /**
     * 查询视频点位周边列表
     * @param params x,y 查询的中心点 ; distance 查询距离（米）
     * @return
     */
    public List<PointInfo> searchPointZhouBianList(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 查询视频点位周边数量
     * @param params x,y 查询的中心点 ; distance 查询距离（米）
     * @return
     */
    public Long countPointZhouBian(Map<String, Object> params);

    public String getVideoUrl(String token, String id);


    public EUDGPagination searchAlarmsList(int page, int rows, Map<String, Object> params);
    public Object searchAlarmsById(String id);

    public String getVideoUrlByCid(String token, String cid);

    //public EUDGPagination searchMyAlarmsList(int page, int rows, Map<String, Object> params);
    //public Map<String, Object> searchMyAlarmsById(String id);

    public List<Map<String,Object>> exportDeviceList(Map<String, Object> params) throws Exception;

    public EUDGPagination searchAllList(Map<String, Object> params);

    public List<Map<String,Object>> searchAllListForV6(Map<String, Object> params);

    public EUDGPagination searchListForV6(int page, int rows, Map<String, Object> params);

    public Map<String, Object> getOnLineRateForV6(Map<String, Object> params);

    public List<Map<String,Object>> getExceptionTrend(Map<String, Object> params);

    public List<Map<String,Object>> getAllOrgNum(Map<String, Object> params);
}