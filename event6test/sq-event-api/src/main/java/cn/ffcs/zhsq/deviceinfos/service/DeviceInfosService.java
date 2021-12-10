package cn.ffcs.zhsq.deviceinfos.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;

/**
 * @Description: 设备信息表，序列：SEQ_DEVICE_INFO_ID模块服务
 * @Author: LINZHU
 * @Date: 10-13 09:29:50
 * @Copyright: 2017 福富软件
 */
public interface DeviceInfosService {
	/**
	 * 新增数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 */
	public Long insert(DeviceInfos bo);

	/**
	 * 修改数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 是否修改成功
	 */
	public boolean update(DeviceInfos bo);

	/**
	 * 删除数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 是否删除成功
	 */
	public boolean delete(DeviceInfos bo);

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID分页数据对象
	 */
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 */
	public DeviceInfos searchById(Long id);
	
	/**
	 * 根据id 和mapt获取设备信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDeviceInfosDataListByIds(String ids, Integer mapt,String markerType);
	
	
	/**
	 * 根据参数获取设备列表未分页
	 * @param params
	 * @return
	 */
	public List<DeviceInfos> findList(Map<String, Object> params);
	
	
	
	public Long countList(Map<String, Object> params);
	
	/**
	 * 统计烟感正常，异常，告警数
	 * @param params
	 * @return
	 */
	public Map<String, Object> countStatus(Map<String, Object> params);
	/**
	 * 统计烟感各种告警类型数
	 * 正常、失联、烟雾告警、火警、电量低、自检故障（正常状态=上线、烟雾告警解除、火警解除、电量正常）
	 * @param params
	 * @return
	 */
	public Map<String, Object> getSmokeCountBYStatus(Map<String, Object> params);
	/**
	 * 统计路灯开关数可异常数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getLightingCountBYStatus(Map<String, Object> params);
	/**
	 * 获取设备告警推送中间表对象
	 * @param msgId
	 * @return
	 */
	public Map<String,Object> searchByMsgId(Long msgId);


}