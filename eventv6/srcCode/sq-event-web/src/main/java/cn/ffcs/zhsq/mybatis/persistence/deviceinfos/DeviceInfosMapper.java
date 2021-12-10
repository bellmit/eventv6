package cn.ffcs.zhsq.mybatis.persistence.deviceinfos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;

/**
 * @Description: 设备信息表dao接口
 * @Author: LINZHU
 * @Date: 10-13 09:29:50
 */
public interface DeviceInfosMapper {
	
	/**
	 * 新增数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 */
	public long insert(DeviceInfos bo);
	
	/**
	 * 修改数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 修改的记录数
	 */
	public long update(DeviceInfos bo);
	
	/**
	 * 删除数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 删除的记录数
	 */
	public long delete(DeviceInfos bo);
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @param rowBounds 分页对象
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID数据列表
	 */
	public List<DeviceInfos> searchList(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 不分页查询
	 * @param params
	 * @return
	 */
	public List<DeviceInfos> searchList(Map<String, Object> params);
	
	/**
	 * 查询数据总数
	 * @param params 查询参数
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID数据总数
	 */
	public long countList(Map<String, Object> params);
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 */
	public DeviceInfos searchById(Long id);
	/**
	 * 根据id 和mapt获取案件警情信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDeviceInfosDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);

	public long countStatus(Map<String, Object> params);
	/**
	 * 统计路灯开关数可异常数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getLightingCountBYStatus(Map<String, Object> params);

	public Map<String, Object> getSmokeCountBYStatus(Map<String, Object> params);
	
	/**
	 * 获取设备告警推送中间表对象
	 * @param msgId
	 * @return
	 */
	public Map<String,Object> searchByMsgId(Long msgId);
	

}