package cn.ffcs.zhsq.deviceinfos.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.deviceinfos.service.DeviceInfosService;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.persistence.deviceinfos.DeviceInfosMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 设备信息模块服务实现
 * @Author: LINZHU
 * @Date: 10-13 09:29:50
 */
@Service("deviceInfosServiceImpl")
@Transactional
public class DeviceInfosServiceImpl implements DeviceInfosService {

	@Autowired
	private DeviceInfosMapper deviceInfosMapper; //注入设备信息表，序列：SEQ_DEVICE_INFO_ID模块dao

	/**
	 * 新增数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 */
	@Override
	public Long insert(DeviceInfos bo) {
		deviceInfosMapper.insert(bo);
		return bo.getDeviceId();
	}

	/**
	 * 修改数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DeviceInfos bo) {
		long result = deviceInfosMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DeviceInfos bo) {
		long result = deviceInfosMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<DeviceInfos> list = deviceInfosMapper.searchList(params, rowBounds);
		long count = deviceInfosMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	public List<DeviceInfos> findList(Map<String, Object> params){
		List<DeviceInfos> list = deviceInfosMapper.searchList(params);
		return list;
	}
	
	/**
	 * 根据业务id查询数据
	 * @param id 设备信息表，序列：SEQ_DEVICE_INFO_IDid
	 * @return 设备信息表，序列：SEQ_DEVICE_INFO_ID业务对象
	 */
	@Override
	public DeviceInfos searchById(Long id) {
		DeviceInfos bo = deviceInfosMapper.searchById(id);
		return bo;
	}
	
	/**
	 * 2014-08-21 chenlf add
	 * 根据id 和mapt获取事件调度模块事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDeviceInfosDataListByIds(String ids, Integer mapt,String markerType){
		return this.deviceInfosMapper.getArcgisDeviceInfosDataListByIds(ids, mapt,markerType);
	}
	
	
	/**
	 * 根据参数统计条数
	 * @param params
	 * @return
	 */
	public Long countList(Map<String, Object> params){
		long count = deviceInfosMapper.countList(params);		
		return count;
	}

	/**
	 * 统计烟感正常，异常，告警数
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> countStatus(Map<String, Object> params) {
		params.put("alertFlagType", "0");
		long countNormal = deviceInfosMapper.countStatus(params);
		params.put("alertFlagType", "1");
		long countAlert = deviceInfosMapper.countStatus(params);
		params.put("alertFlagType", "2");
		long countAbnormal = deviceInfosMapper.countStatus(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("countNormal", countNormal);
		result.put("countAlert", countAlert);
		result.put("countAbnormal", countAbnormal);
		
		return result;
	}
	
	/**
	 * 统计烟感各种告警类型数
	 * 正常、失联、烟雾告警、火警、电量低、自检故障（正常状态=上线、烟雾告警解除、火警解除、电量正常）
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> getSmokeCountBYStatus(
			Map<String, Object> params) {
		return deviceInfosMapper.getSmokeCountBYStatus(params);
	}
	

	@Override
	public Map<String, Object> getLightingCountBYStatus(
			Map<String, Object> params) {
		params.put("deviceType", ConstantValue.DEVICE_LIGHTING);
		return deviceInfosMapper.getLightingCountBYStatus(params);
	}

	@Override
	public Map<String, Object> searchByMsgId(Long msgId) {
		return deviceInfosMapper.searchByMsgId(msgId);
	}
	
		

}